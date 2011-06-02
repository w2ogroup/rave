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

package org.apache.rave.provider.opensocial.web.renderer;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.web.renderer.Renderer;
import org.apache.rave.provider.opensocial.Constants;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OpenSocialWidgetRendererTest {
    private OpenSocialService openSocialService;
    private Renderer<RegionWidget> renderer;

    private static final String VALID_METADATA = "metadata";
    private static final String VALID_GADGET_URL = "http://example.com/gadgets/1";

    @Before
    public void setup() {
        openSocialService = createNiceMock(OpenSocialService.class);
        renderer = new OpenSocialWidgetRenderer(openSocialService);
    }

    @Test
    public void getContext() {
        assertThat(renderer.getSupportedContext(), is(equalTo(Constants.WIDGET_TYPE)));
    }

    @Test
    public void render_valid() {
        expect(openSocialService.getGadgetMetadata(VALID_GADGET_URL)).andReturn(VALID_METADATA);
        replay(openSocialService);

        Widget w = new Widget();
        w.setId(1L);
        w.setType(Constants.WIDGET_TYPE);
        w.setUrl(VALID_GADGET_URL);
        RegionWidget rw = new RegionWidget();
        rw.setId(1L);
        rw.setWidget(w);

        String result = renderer.render(rw);
        assertThat(result.matches(".*regionWidgetId[ ]*:[ ]*1,.*"), is(true));
        assertThat(result.matches(".*type[ ]*:[ ]*'OpenSocial',.*"), is(true));
        assertThat(result.matches(".*widgetUrl[ ]*:[ ]*'http://example.com/gadgets/1',.*"), is(true));
        assertThat(result.matches(".*metadata[ ]*:[ ]*" + VALID_METADATA + ",.*"), is(true));
    }

    @Test
    public void render_null() {
        Widget w = new Widget();
        w.setType(Constants.WIDGET_TYPE);
        RegionWidget rw = new RegionWidget();
        rw.setWidget(w);

        String result = renderer.render(rw);
        assertThat(result.matches(".*regionWidgetId[ ]*:[ ]*null,.*"), is(true));
        assertThat(result.matches(".*type[ ]*:[ ]*'OpenSocial',.*"), is(true));
        assertThat(result.matches(".*widgetUrl[ ]*:[ ]*'null',.*"), is(true));
        assertThat(result.matches(".*metadata[ ]*:[ ]*null,.*"), is(true));
    }

    @Test(expected = NotSupportedException.class)
    public void render_invalid() {
        Widget w = new Widget();
        w.setType("NONE");
        w.setUrl("http://example.com/gadgets/1");
        RegionWidget rw = new RegionWidget();
        rw.setId(1L);
        rw.setWidget(w);

        renderer.render(rw);
    }
}