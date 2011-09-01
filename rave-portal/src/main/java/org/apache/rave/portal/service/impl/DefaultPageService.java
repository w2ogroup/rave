/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.service.impl;

import java.util.ArrayList;
import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.RegionRepository;
import org.apache.rave.portal.repository.RegionWidgetRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.apache.rave.portal.model.PageLayout;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.service.UserService;
import org.springframework.beans.factory.annotation.Value;

@Service
public class DefaultPageService implements PageService {
    private final PageRepository pageRepository;
    private final RegionRepository regionRepository;
    private final RegionWidgetRepository regionWidgetRepository;
    private final WidgetRepository widgetRepository;
    private final PageLayoutRepository pageLayoutRepository;
    private final UserService userService;    
    private final String defaultPageName;
    
    private final long MOVE_PAGE_DEFAULT_POSITION_INDEX = -1L;

    @Autowired
    public DefaultPageService(PageRepository pageRepository, 
                              RegionRepository regionRepository, 
                              WidgetRepository widgetRepository, 
                              RegionWidgetRepository regionWidgetRepository,
                              PageLayoutRepository pageLayoutRepository,
                              UserService userService,
                              @Value("${portal.page.default_name}") String defaultPageName) {
        this.pageRepository = pageRepository;
        this.regionRepository = regionRepository;
        this.regionWidgetRepository = regionWidgetRepository;
        this.widgetRepository = widgetRepository;
        this.pageLayoutRepository = pageLayoutRepository;
        this.userService = userService;
        this.defaultPageName = defaultPageName;
    }

    @Override
    public Page getPage(long pageId) {
        return pageRepository.get(pageId);
    }
    
    @Override
    public List<Page> getAllPages(long userId) {
        return pageRepository.getAllPages(userId);
    }
    
    @Override
    public Page getPageFromList(long pageId, List<Page> pages) {
        Page pageToFind = new Page(pageId);
        int index = pages.indexOf(pageToFind);
        return index == -1 ? null : pages.get(index);
    }

    @Override
    @Transactional
    public Page addNewPage(String pageName, String pageLayoutCode) {                     
        return addNewPage(userService.getAuthenticatedUser(), pageName, pageLayoutCode);
    }    
    
    @Override
    @Transactional
    public Page addNewDefaultPage(User user, String pageLayoutCode) {                       
        return addNewPage(user, defaultPageName, pageLayoutCode);
    }       
    
    @Override
    public String getDefaultPageName() {
        return defaultPageName;
    }

    @Override
    @Transactional
    public void deletePage(long pageId) {                    
        User user = userService.getAuthenticatedUser();
        // first delete the page        
        pageRepository.delete(pageRepository.get(pageId));
        // now re-sequence the page sequence numbers

        //TODO RAVE-237:  We should be able to delete these lines.  If there are gaps in the sequence numbers, then it will still
        //TODO RAVE-237:  return values in the correct order.  We only need to update sequences when there is a change in order
        List<Page> pages = pageRepository.getAllPages(user.getId());
        updatePageRenderSequences(pages);
    }    
    
    @Override
    @Transactional
    public RegionWidget moveRegionWidget(long regionWidgetId, int newPosition, long toRegion, long fromRegion) {
        Region target = getFromRepository(toRegion, regionRepository);
        if (toRegion == fromRegion) {
            moveWithinRegion(regionWidgetId, newPosition, target);
        } else {
            moveBetweenRegions(regionWidgetId, newPosition, fromRegion, target);
        }
        target = regionRepository.save(target);
        return findRegionWidgetById(regionWidgetId, target.getRegionWidgets());
    }

    @Override
    @Transactional
    public Region removeWidgetFromPage(long regionWidgetId) {
        RegionWidget widget = getFromRepository(regionWidgetId, regionWidgetRepository);
        regionWidgetRepository.delete(widget);
        return getFromRepository(widget.getRegion().getId(), regionRepository);
    }

    @Override
    @Transactional
    public RegionWidget addWidgetToPage(long pageId, long widgetId) {
        Page page = getFromRepository(pageId, pageRepository);
        Widget widget = getFromRepository(widgetId, widgetRepository);
        Region region = page.getRegions().get(0);
        return createWidgetInstance(widget, region, 0);
    }
    
    @Override
    @Transactional
    public Page movePage(long pageId, long moveAfterPageId) {
        return doMovePage(pageId, moveAfterPageId);
    }    
    
    @Override
    @Transactional
    public Page movePageToDefault(long pageId) {    
        return doMovePage(pageId, MOVE_PAGE_DEFAULT_POSITION_INDEX);    
    }

    private RegionWidget createWidgetInstance(Widget widget, Region region, int position) {
        RegionWidget regionWidget = new RegionWidget();
        regionWidget.setRenderOrder(position);
        regionWidget.setWidget(widget);
        region.getRegionWidgets().add(position, regionWidget);
        updateRenderSequences(region.getRegionWidgets());
        Region persistedRegion = regionRepository.save(region);
        return persistedRegion.getRegionWidgets().get(position);
    }

    private void moveWithinRegion(long regionWidgetId, int newPosition, Region target) {
        replaceRegionWidget(regionWidgetId, newPosition, target, target);
        updateRenderSequences(target.getRegionWidgets());
    }

    private void moveBetweenRegions(long regionWidgetId, int newPosition, long fromRegion, Region target) {
        Region source = getFromRepository(fromRegion, regionRepository);
        replaceRegionWidget(regionWidgetId, newPosition, target, source);
        updateRenderSequences(source.getRegionWidgets());
        updateRenderSequences(target.getRegionWidgets());
        regionRepository.save(source);
    }

    private void replaceRegionWidget(long regionWidgetId, int newPosition, Region target, Region source) {
        RegionWidget widget = findRegionWidgetById(regionWidgetId, source.getRegionWidgets());
        source.getRegionWidgets().remove(widget);
        target.getRegionWidgets().add(newPosition, widget);
    }

    private Page addNewPage(User user, String pageName, String pageLayoutCode) {
        PageLayout pageLayout = pageLayoutRepository.getByPageLayoutCode(pageLayoutCode);
        
        // Create regions
        List<Region> regions = new ArrayList<Region>();
        int regionCount;
        for (regionCount = 0; regionCount < pageLayout.getNumberOfRegions(); regionCount++) {
            Region region = new Region();
            regions.add(region);
        }

        // Create a Page object and register it.
        long renderSequence = getAllPages(user.getId()).size() + 1;
        Page page = new Page();
        page.setName(pageName);       
        page.setOwner(user);
        page.setPageLayout(pageLayout);
        page.setRenderSequence(renderSequence);
        page.setRegions(regions);        
        pageRepository.save(page);
        
        return page;
    }

    private void updatePageRenderSequences(List<Page> pages) {       
        if (pages != null && !pages.isEmpty()) {
            for (int i = 0; i < pages.size(); i++) {
                Page p = pages.get(i);                
                p.setRenderSequence((long)i+1);                               
            }

            for (Page page : pages) {
                pageRepository.save(page);
            }
        }       
    } 
    
    private Page doMovePage(long pageId, long moveAfterPageId) {
        // get the logged in user
        User user = userService.getAuthenticatedUser();

        // get the page to move and the page to move after
        Page movingPage = pageRepository.get(pageId);
        Page afterPage = null;
        int newIndex = 0;
        
        // check to see if we should move the page to beginning
        if (moveAfterPageId != MOVE_PAGE_DEFAULT_POSITION_INDEX) {
            afterPage = pageRepository.get(moveAfterPageId);
        }

        // get all of the user's pages
        // the pageRepository returns an un-modifiable list
        // so we need to create a modifyable arraylist
        List<Page> pages = new ArrayList<Page>(pageRepository.getAllPages(user.getId()));

        // first remove it from the list         
        if (!pages.remove(movingPage)) {
            throw new RuntimeException("unable to find pageId " + pageId + " attempted to be moved for user " + user);
        }

        // ...now re-insert in new location
        if (afterPage != null) {
            newIndex = pages.indexOf(afterPage) + 1;
        }
        pages.add(newIndex, movingPage);

        // persist the new page order
        updatePageRenderSequences(pages);
        
        return movingPage;
    }

    private static <T> T getFromRepository(long id, Repository<T> repo) {
        T object = repo.get(id);
        if (object == null) {
            throw new IllegalArgumentException("Could not find object of given id in " + repo.getClass().getSimpleName());
        }
        return object;
    }

    private static void updateRenderSequences(List<RegionWidget> regionWidgets) {
        int count = 0;
        for (RegionWidget widget : regionWidgets) {
            widget.setRenderOrder(count);
            count++;
        }
    }

    private static RegionWidget findRegionWidgetById(Long id, List<RegionWidget> regionWidgets) {
        for (RegionWidget widget : regionWidgets) {
            if (widget.getId().equals(id)) {
                return widget;
            }
        }
        throw new IllegalArgumentException("Invalid RegionWidget ID");
    }
}
