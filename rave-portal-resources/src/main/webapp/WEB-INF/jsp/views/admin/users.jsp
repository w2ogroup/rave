<%--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  --%>
<%@ page language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave" %>
<fmt:setBundle basename="messages"/>
<%--@elvariable id="searchResult" type="org.apache.rave.portal.model.util.SearchResult<org.apache.rave.portal.model.User>"--%>

<fmt:message key="admin.users.title" var="pagetitle"/>
<rave:rave_generic_page pageTitle="${pagetitle}">
    <rave:header pageTitle="${pagetitle}"/>
    <rave:admin_tabsheader/>

    <div class="pageContent">
        <article class="admincontent">
            <c:if test="${actionresult eq 'delete' or actionresult eq 'update'}">
                <div class="alert-message success">
                    <p>
                        <fmt:message key="admin.userdetail.action.${actionresult}.success"/>
                    </p>
                </div>
            </c:if>

            <ul class="horizontal-list searchbox">
                <li><a href="<spring:url value="/app/newaccount.jsp"/>"><fmt:message key="admin.users.add"/></a></li>
                <li>
                    <form action="<spring:url value="/app/admin/users/search"/>" method="GET">
                        <fieldset>
                            <label for="searchTerm"><fmt:message key="admin.users.search"/></label>
                            <input type="search" id="searchTerm" name="searchTerm"
                                   value="<c:out value="${searchTerm}"/>"/>
                            <fmt:message key="page.store.search.button" var="searchButtonText"/>
                            <input type="submit" value="${searchButtonText}"/>
                        </fieldset>
                    </form>
                </li>
                <c:if test="${not empty searchTerm}">
                    <li><a href="<spring:url value="/app/admin/users"/>"><fmt:message key="admin.clearsearch"/></a></li>
                </c:if>
            </ul>

            <rave:admin_listheader/>
            <rave:admin_paging/>

            <c:if test="${searchResult.totalResults > 0}">
            <table class="datatable userstable">
                <thead>
                <tr>
                    <th class="textcell"><fmt:message key="admin.userdata.username"/></th>
                    <th class="largetextcell"><fmt:message key="admin.userdata.email"/></th>
                    <th class="booleancell"><fmt:message key="admin.userdata.enabled"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${searchResult.resultSet}">
                    <spring:url value="/app/admin/userdetail/${user.entityId}" var="detaillink"/>
                    <tr data-detaillink="${detaillink}">
                        <td><a href="${detaillink}"><c:out value="${user.username}"/></a></td>
                        <td><a href="${detaillink}"><c:out value="${user.email}"/></a></td>
                        <td><a href="${detaillink}">${user.enabled}</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            </c:if>

            <rave:admin_paging/>

        </article>
    </div>

    <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
    <script src="<spring:url value="/script/rave_admin.js"/>"></script>
    <script>$(function() {
        rave.admin.initAdminUi();
    });</script>
</rave:rave_generic_page>