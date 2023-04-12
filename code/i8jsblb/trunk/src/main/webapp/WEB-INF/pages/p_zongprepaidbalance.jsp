<%--
  Created by IntelliJ IDEA.
  User: muhammad.aqeel
  Date: 4/11/2023
  Time: 12:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp" %>
<%@ page import='com.inov8.microbank.common.util.PortalConstants' %>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils" %>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %>
</c:set>

<html>
<head>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet"
          href="${contextPath}/styles/extremecomponents.css"
          type="text/css">
    <%@include file="/common/ajax.jsp" %>
    <meta name="title" content="Zong Prepaid Balance"/>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();
        var serverDate = "<%=PortalDateUtils.getServerDate()%>";
    </script>
    <%@include file="/WEB-INF/pages/export_zip.jsp" %>
    <style>
        .tb { border-collapse: collapse; }
        .tb th, .tb td { padding: 5px; border: solid 1px #777; }
        .tb th { background-color: /*#298be6*/ rgb(2, 146, 224); text-align: center; color: white }
        .tb tr { text-align: center}

    </style>

</head>
<body bgcolor="#ffffff">
<div id="rsp" class="ajaxMsg"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<html:form name='zongPreparedBalanceForm' commandName="zongPreparedBalanceModel" method="post"
           action="p_zongprepaidbalance.html">
    <table width="100%" border="0" class="tb">

        <tr>
            <th>Mobile No:</th>
            <th>Balance:</th>
        </tr>
        <tr>
            <td> <c:out value="+92${zongModel.mobileNo}"/></td>
            <td> <c:out value="${zongModel.balance}"/></td>
        </tr>

    </table>
</html:form>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>

