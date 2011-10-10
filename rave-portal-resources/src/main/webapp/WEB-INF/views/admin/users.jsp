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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave" %>
<fmt:setBundle basename="messages"/>
<%--@elvariable id="searchResult" type="org.apache.rave.portal.model.util.SearchResult"--%>

<fmt:message key="admin.users.title" var="pagetitle"/>
<rave:rave_generic_page pageTitle="${pagetitle}">
    <rave:header pageTitle="${pagetitle}"/>
    <rave:admin_tabsheader/>
    <div class="pageContent">
        <article class="admincontent">
            <ul class="horizontal-list searchbox">
                <li><a href="<spring:url value="/app/newaccount.jsp"/>">__Add user</a></li>
                <li>
                <form action="" method="GET">
                    <fieldset>
                        <input type="hidden" name="referringPageId" value="${referringPageId}">
                        <label for="searchTerm">__Search users</label>
                        <input type="search" id="searchTerm" name="searchTerm"
                               value="<c:out value="${searchTerm}"/>"/>
                        <fmt:message key="page.store.search.button" var="searchButtonText"/>
                        <input type="submit" value="${searchButtonText}">
                    </fieldset>
                </form>
                </li>
            </ul>
            <c:choose>
                <c:when test="${empty searchTerm and (empty searchResult or searchResult.totalResults eq 0)}">
                    <fmt:message key="page.store.list.noresult" var="listheader"/>
                </c:when>
                <c:when test="${empty searchTerm}">
                    <fmt:message key="page.store.list.result.x.to.y" var="listheader">
                        <fmt:param value="${offset + 1}"/>
                        <fmt:param value="${offset + fn:length(searchResult.resultSet)}"/>
                        <fmt:param value="${searchResult.totalResults}"/>
                    </fmt:message>
                </c:when>
                <c:when test="${not empty searchTerm and searchResult.totalResults eq 0}">
                    <fmt:message key="page.store.list.search.noresult" var="listheader">
                        <fmt:param><c:out value="${searchTerm}"/></fmt:param>
                    </fmt:message>
                </c:when>
                <c:otherwise>
                    <fmt:message key="page.store.list.search.result.x.to.y" var="listheader">
                        <fmt:param value="${offset + 1}"/>
                        <fmt:param value="${offset + fn:length(searchResult.resultSet)}"/>
                        <fmt:param value="${searchResult.totalResults}"/>
                        <fmt:param><c:out value="${searchTerm}"/></fmt:param>
                    </fmt:message>
                </c:otherwise>
            </c:choose>
            <h2>__${listheader}</h2>

            <ul class="paging">
                <li><a href="#">&lt;</a></li>
                <li><a href="#">1</a></li>
                <li>2</li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li><a href="#">&gt;</a></li>
            </ul>

            <table class="datatable userstable">
                <thead>
                <tr>
                    <th class="textcell">Username</th>
                    <th class="largetextcell">Email</th>
                    <th class="booleancell">Enabled</th>
                </tr>
                </thead>
                <tbody>
                <spring:url value="/app/admin/userdetail/john.doe" var="userdetail"/>
                <tr data-detaillink="${userdetail}">
                    <td><a href="${userdetail}">canonical</a></td>
                    <td><a href="${userdetail}">canonical@example.com</a></td>
                    <td><a href="${userdetail}">X</a></td>
                </tr>
                <tr>
                    <td>john.doe</td>
                    <td>john.doe@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>jane.doe</td>
                    <td>jane.doe@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>canonical</td>
                    <td>canonical@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>john.doe</td>
                    <td>john.doe@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>jane.doe</td>
                    <td>jane.doe@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>canonical</td>
                    <td>canonical@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>john.doe</td>
                    <td>john.doe@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>jane.doe</td>
                    <td>jane.doe@example.com</td>
                    <td>X</td>
                </tr>
                <tr>
                    <td>jane.doe</td>
                    <td>jane.doe@example.com</td>
                    <td>X</td>
                </tr>
                </tbody>
            </table>
            
            <ul class="paging">
                <li><a href="#">&lt;</a></li>
                <li><a href="#">1</a></li>
                <li>2</li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li><a href="#">&gt;</a></li>
            </ul>
        </article>
    </div>

    <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
    <script src="<spring:url value="/script/rave_admin.js"/>"></script>
    <script>$(function() {
        rave.admin.initAdminUi();
    });</script>
</rave:rave_generic_page>