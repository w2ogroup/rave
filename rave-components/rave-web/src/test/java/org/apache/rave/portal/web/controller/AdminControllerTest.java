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

package org.apache.rave.portal.web.controller;

import org.apache.rave.portal.web.util.ViewNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Test for {@link AdminController}
 */
public class AdminControllerTest {

    AdminController controller;

    @Test
    public void adminHome() throws Exception {
        Model model = new ExtendedModelMap();
        String homeView = controller.viewDefault(model);
        assertEquals(ViewNames.ADMIN_HOME, homeView);
        assertTrue(model.containsAttribute("tabs"));
    }

    @Test
    public void adminUsers() throws Exception {
        Model model = new ExtendedModelMap();
        String adminUsersView = controller.viewUsers(model);
        assertEquals(ViewNames.ADMIN_USERS, adminUsersView);
        assertTrue(model.containsAttribute("tabs"));
    }

    @Test
    public void testAdminUserDetail() throws Exception {
        Model model = new ExtendedModelMap();
        String userid = "dummyUserId";
        String adminUserDetailView = controller.viewUserDetail(userid, model);
        assertEquals(ViewNames.ADMIN_USERDETAIL, adminUserDetailView);
        assertTrue(model.containsAttribute("tabs"));
        assertEquals(userid, model.asMap().get("userid"));

    }

    @Test
    public void adminWidgets() throws Exception {
        Model model = new ExtendedModelMap();
        String adminWidgetsView = controller.viewWidgets(model);
        assertEquals(ViewNames.ADMIN_WIDGETS, adminWidgetsView);
        assertTrue(model.containsAttribute("tabs"));
    }
    

    @Before
    public void setUp() throws Exception {
        controller = new AdminController();
    }
}