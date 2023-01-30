<%--
  Created by IntelliJ IDEA.
  User: muhammad.aqeel
  Date: 1/24/2023
  Time: 12:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@page import="com.inov8.microbank.common.util.PortalDateUtils"%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

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
    <%@include file="/common/ajax.jsp"%>
    <meta name="title" content="IVR MPIN Verification Report"/>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();
        var serverDate = "<%=PortalDateUtils.getServerDate()%>";
    </script>
    <%@include file="/WEB-INF/pages/export_zip.jsp"%>

</head>
<body bgcolor="#ffffff">
<div id="rsp" class="ajaxMsg"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>

<html:form name='ivrMpinVerificationReportForm' commandName="iVRMpinVerificationViewModel" method="post"
           action="p_ivrMpinVerificationReport.html" onsubmit="return validateFormData(this)">
    <table width="100%" border="0">
        <tr>
            <td class="formText" align="right" width="20%">
                Mobile No:
            </td>
            <td align="left" width="30%">
                <html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3"
                            onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right" width="20%">

            </td>
            <td align="left" width="30%">
                    <%--                <html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3" onkeypress="return maskNumber(this,event)"/>--%>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                From Date:
            </td>
            <td align="left">
                <html:input path="logStartDate" id="createdOnFromDate" readonly="true" tabindex="4" cssClass="textBox"
                            maxlength="10"/>
                <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer"
                     src="images/cal.gif" border="0"/>
                <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal"
                     onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
            <td class="formText" align="right">
                To Date:
            </td>
            <td align="left">
                <html:input path="logEndDate" id="createdOnToDate" readonly="true" tabindex="5" cssClass="textBox"
                            maxlength="10"/>
                <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer"
                     src="images/cal.gif" border="0"/>
                <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal"
                     onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">

            </td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_ivrMpinVerificationReport.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13"/>
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table>
</html:form>
<ec:table items="ivrMpinVerificationList" var="iVRMpinVerificationViewModel"
          action="${contextPath}/p_ivrMpinVerificationReport.html?actionId=${retriveAction}"
          title=""
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"
>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="IVR Mpin Verification.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="IVR Mpin Verification.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="IVR Mpin Verification" fileName="IVR Mpin Verification.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="IVR Mpin Verification.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true" filterable="true"
                   style="text-align: center"/>
        <ec:column property="verificationDate" title="Date" escapeAutoFormat="true" format="dd/MM/yyyy"
                   filterable="false" style="text-align: center"/>
        <ec:column property="verificationTime" title="Time" escapeAutoFormat="true" format="hh:mm a" filterable="false"
                   style="text-align: center"/>
        <ec:column property="responseDescription" title="Status" escapeAutoFormat="true" filterable="false"
                   style="text-align: center"/>
    </ec:row>
</ec:table>


<%--<ec:table items="ivrMpinVerificationList" var="ivrMpinVerification"--%>
<%--          action="${contextPath}/p_ivrMpinVerificationReport.html"--%>
<%--          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">--%>
<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">--%>
<%--        <ec:exportXls fileName="IVR Mpin Verification.xls" tooltip="Export Excel" />--%>
<%--    </authz:authorize>--%>
<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">--%>
<%--        <ec:exportXlsx fileName="IVR Mpin Verification.xlsx" tooltip="Export Excel" />--%>
<%--    </authz:authorize>--%>
<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">--%>
<%--        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="IVR Mpin Verification Report"--%>
<%--                      fileName="IVR Mpin Verification.pdf" tooltip="Export PDF" />--%>
<%--    </authz:authorize>--%>
<%--    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">--%>
<%--        <ec:exportCsv fileName="IVR Mpin Verification.csv" tooltip="Export CSV"></ec:exportCsv>--%>
<%--    </authz:authorize>--%>

<%--    <ec:row>--%>
<%--        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true" filterable="true" style="text-align: center"/>--%>
<%--        <ec:column property="verificationDate" title="Date" escapeAutoFormat="true" format="dd/MM/yyyy"   filterable="false" style="text-align: center"/>--%>
<%--        <ec:column property="verificationTime" title="Time" escapeAutoFormat="true" format="hh:mm a"  filterable="false" style="text-align: center"/>--%>
<%--        <ec:column property="responseDescription" title="Status" escapeAutoFormat="true" filterable="false" style="text-align: center"/>--%>

<%--    </ec:row>--%>
<%--</ec:table>--%>


<script language="javascript" type="text/javascript">

    Calendar.setup(
        {
            inputField: "createdOnFromDate", // id of the input field
            button: "CreatedFromDate",    // id of the button
        }
    );
    Calendar.setup(
        {
            inputField: "createdOnToDate", // id of the input field
            button: "CreatedToDate",    // id of the button
            isEndDate: true
        }
    );

    function validateFormData(form) {
        var isValid = validateForm(form);
        return isValid;
    }

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>
