<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp" %>
<%--
  ~ Licensed to the Apache Software Foundation (ASF) under one
  ~ or more contributor license agreements.  See the NOTICE file
  ~ distributed with this work for additional information
  ~ regarding copyright ownership.  The ASF licenses this file
  ~ to you under the Apache License, Version 2.0 (the
  ~ "License"); you may not use this file except in compliance
  ~ with the License.  You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>

<fmt:setBundle basename="messages"/>
<%--@elvariable id="page" type="org.apache.rave.portal.model.Page"--%>
<div class="columns_3_newuser_static">
    <fmt:message key="page.layout.newuser.introtext"/>
</div>

<div class="columns_3_newuser_widgets">
    <div class="columns_3_newuser_subtitle"><fmt:message key="page.layout.newuser.subtitle"/></div>
    <div class="widgetRow upperRow">
        <rave:region region="${page.regions[0]}" regionIdx="1" widgets="${widgets}" />
        <rave:region region="${page.regions[1]}" regionIdx="2" widgets="${widgets}" />
    </div>
    <div class="widgetRow bottomRow">
        <rave:region region="${page.regions[2]}" regionIdx="3" widgets="${widgets}" />
    </div>
</div>