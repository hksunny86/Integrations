<%--
  Created by IntelliJ IDEA.
  User: Muhammad Aqeel
  Date: 1/11/2022
  Time: 11:17 AM
  To change this template use File | Settings | File Templates.
--%>

<%@include file="/common/taglibs.jsp" %>
<%@ page import='com.inov8.microbank.common.util.PortalConstants' %>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils' %>
<%@include file="/common/ajax.jsp" %>
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
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css"/>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <link rel="stylesheet"
          href="${contextPath}/styles/extremecomponents.css"
          type="text/css">
    <%@include file="/common/ajax.jsp" %>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();
        var serverDate = "<%=PortalDateUtils.getServerDate()%>";
    </script>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <script>
        setTimeout(function () {
            document.getElementById("successMessages").style.display = "none";
        }, 7000);

    </script>
    <link rel="stylesheet" href="${contextPath}/styles/jquery-ui-custom.min.css" type="text/css">

    <meta name="title" content="Update Account To Merchant"/>

</head>
<body bgcolor="#ffffff" id="body-content">
<div class="infoMsg" id="errorMessages" style="display:none;"></div>
<div id="successMsg" class="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/>
            <br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>


<html:form name="blinkCustomerListViewForm"
           commandName="merchantAccountModel" action="p_updateaccounttoMerchantinfo.html"
           onsubmit="return validateForm(this)">
    <table width="950px">
        <tr>
            <td align="right" class="formText">Mobile No:</td>
            <td align="left">
                <html:input path="mobileNo" id="mobileNo" cssClass="textBox" maxlength="11" tabindex="6" value=""/>
            </td>

            <td align="right" class="formText">CNIC #:</td>
            <td align="left">
                <html:input path="cnic" id="cnic" cssClass="textBox" maxlength="13" tabindex="2" value=""/>
            </td>

        </tr>

        <tr>
            <td align="right" class="formText">Account Updated:
            </td>
            <td align="left" class="formText">
                <html:select path="accUpdate" id="selecter" tabindex="2" cssClass="textBox">
                    <html:option value="">---All---</html:option>
                    <html:option value="0">Updated</html:option>
                    <html:option value="1">Not Updated</html:option>
                </html:select>
            </td>
            <td align="right" class="formText">Registration Status:
            </td>
            <td align="left" class="formText">
                <html:select path="registrationStatus" id="selecter" tabindex="2" cssClass="textBox">
                    <html:option value="">---All---</html:option>
                    <html:option value="2">Request Received</html:option>
                    <html:option value="3">Blink Approved</html:option>
                    <html:option value="4">Blink Discrepent</html:option>
                    <html:option value="6">Blink Rejected</html:option>
                </html:select>
            </td>

            </td>
        </tr>
        <tr>
            <td align="right" class="formText">Action Status:
            </td>
            <td align="left" class="formText">
                <html:select path="actionStatusId" id="selecter" tabindex="2" cssClass="textBox">
                    <html:option value="">---All---</html:option>
                    <html:option value="3">Approved</html:option>
                    <html:option value="4">Pending Approval</html:option>
                    <html:option value="5">Approval Denied</html:option>
                    <html:option value="7">Resolved</html:option>
                    <html:option value="8">WithDraw Request</html:option>
                    <html:option value="9">Assigned Back</html:option>
                </html:select>
            </td>

            <td align="right" class="formText">Tracker ID:</td>
            <td align="left">
                <html:input path="merchanAccountId" id="merchanAccountId" cssClass="textBox" maxlength="13" tabindex="2" value=""/>
            </td>

        </tr>

        <tr>
            <td class="formText" align="right">
                Created On - Start:
            </td>
            <input type="hidden" id="stDate" name="stDate">
            <input type="hidden" id="edDate" name="edDate">
            <td align="left">
                <html:input path="start" id="startDate" readonly="true" tabindex="7" cssClass="textBox" maxlength="20"/>
                <img id="sDate" tabindex="11" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="sDate" tabindex="12" title="Clear Date" name="popcal"
                     onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
            <td class="formText" align="right">
                Created On - End:
            </td>
            <td align="left">
                <html:input path="end" id="endDate" name="endDate" readonly="true" tabindex="8" cssClass="textBox"
                            maxlength="20"/>
                <img id="eDate" tabindex="13" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="eDate" tabindex="14" title="Clear Date" name="popcal"
                     onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
        </tr>


        <br>
        <tr>
            <td align="left" colspan="2" class="formText"></td>
            <td align="left" colspan="2" class="formText">
                <input type="submit" class="button" value="Search" name="_search" tabindex="6"/>
                <input type="reset" class="button" value="Cancel" name="_reset" tabindex="14"
                       onclick="javascript: window.location='home.html'"/>
            </td>
        </tr>
    </table>
</html:form>


<ec:table items="blinkCustomerModelList" var="modelList"
          action="${contextPath}/p_updateaccounttoMerchantinfo.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Update Account to Merchant List.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Update Account to Merchant List.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Update Account to Merchant Report"
                      fileName="Update Account to Merchanrt List.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Update Account to Merchant List.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column filterable="false" property="merchanAccountId" title="Tracker Id" escapeAutoFormat="true"/>
        <ec:column filterable="false" property="mobileNo" title="Mobile #" escapeAutoFormat="true"/>
        <ec:column property="cnic" title="CNIC" filterable="false" escapeAutoFormat="true"/>
        <ec:column filterable="false" property="consumerName" title="Customer Name"/>
        <ec:column property="createdBy" filterable="false" title="Created By"/>
        <ec:column property="updatedBy" filterable="false" title="Updated By"/>
=        <ec:column property="createdOn" filterable="false" cell="date" format="dd/MM/yyyy" alias="Created on"/>
        <ec:column property="updatedOn" filterable="false" cell="date" format="dd/MM/yyyy" alias="Updated on"/>
        <ec:column property="registrationStateName" filterable="false" title="Registration Status"/>
        <ec:column property="actionStatusIdActionStatusModel.name" filterable="false" title="Action Status"/>
        <ec:column property="comments" filterable="false" title="Maker Comments"/>
        <ec:column property="chkComments" filterable="false" title="Checker Comments"/>

        <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
            <c:if test="${modelList.accUpdate==1}">
                <input type="button" id="cancel" class="button" value="Cancel" onclick="window.location='home.html'">
            </c:if>
            <c:if test="${modelList.accUpdate==0}">
                <input type="button" id="cancel" class="button" disabled="disabled" value="Cancel"
                       onclick="window.location='home.html'">
            </c:if>
        </ec:column>
        <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
            <c:if test="${modelList.accUpdate==1}">
                <input type="button" id="upgrade" class="button"
                       value="Upgrade"
                       onclick="window.location='p_updateaccounttomerchantdetail.html?accTypeId=${modelList.merchanAccountId}'">
            </c:if>
            <c:if test="${modelList.accUpdate==0}">
                <input type="button" id="upgrade" class="button"
                       value="Upgrade" disabled="disabled"
                       onclick="window.location='p_updateaccounttomerchantdetail.html?accTypeId=${modelList.merchanAccountId}'">
            </c:if>
        </ec:column>
        <ec:column alias=" " viewsAllowed="html" filterable="false" sortable="false">
            <c:choose>
                <c:when test="${modelList.registrationStatus eq 4 or modelList.registrationStatus eq 6}">

                    <input type="button" class="button" style="width='90px'" value="View Details"
                           onclick="openActionDetailWindow(${modelList.merchanAccountId})"/>
                </c:when>
                <c:otherwise>
                    <input type="button" class="button" disabled="disabled" style="width='90px'" value="View Details"
                           onclick="openActionDetailWindow()"/>
                </c:otherwise>
            </c:choose>
            <%--            <c:if test="${modelList.registrationStatus eq 4 and modelList.registrationStatus eq 6}">--%>
            <%--                <input type="button" id="viewDetails" class="button"--%>
            <%--                       value="View Details"--%>
            <%--                       onclick="window.location='p_updateaccounttoblinkdetail.html?accTypeId=${modelList.merchanAccountId}'">--%>
            <%--            </c:if>--%>
            <%--            <c:if test="${modelList.registrationStatus eq 2 and modelList.registrationStatus eq 4}">--%>
            <%--                <input type="button" id="viewDetails" class="button"--%>
            <%--                       value="View Details" disabled="disabled"--%>
            <%--                       onclick="window.location='p_updateaccounttoblinkdetail.html?accTypeId=${modelList.merchanAccountId}'">--%>
            <%--            </c:if>--%>
        </ec:column>

    </ec:row>
</ec:table>
<script>
    Calendar.setup({
        inputField: "startDate", // id of the input field
        button: "sDate", // id of the button
    });

    Calendar.setup({
        inputField: "endDate", // id of the input field
        button: "eDate", // id of the button
        isEndDate: true
    });

    function openActionDetailWindow(merchanAccountId) {
        var url = null;
        var popupWidth = 800;
        var popupHeight = 550;
        var popupLeft = (window.screen.width - popupWidth) / 2;
        var popupTop = (window.screen.height - popupHeight) / 2;

        url = 'p-updatecustomerauthorizationdetail.html?accTypeId=' + merchanAccountId + '&escalateRequest=true&resolveRequest=false';

        childWindow = window.open(url, 'Action Detail Window', 'width=' + popupWidth + ',height=' + popupHeight + ',menubar=no,toolbar=no,left=' + popupLeft + ',top=' + popupTop + ',directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
        if (window.focus) childWindow.focus();
        return false;
    }
</script>
</body>
</html>
