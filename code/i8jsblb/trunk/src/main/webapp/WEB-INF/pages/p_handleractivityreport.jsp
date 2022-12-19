<%--
  Created by IntelliJ IDEA.
  User: abubakar.farooque
  Date: 10/10/2022
  Time: 10:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<html>
<head>
    <c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>
    <c:set var="createAction"><%=PortalConstants.ACTION_CREATE %></c:set>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <meta name="title" content="Handler Activity Report" />
    <script type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
    </script>
    <%
        String createPermission = PortalConstants.ADMIN_GP_CREATE;
        createPermission +=	"," + PortalConstants.PG_GP_CREATE;
        createPermission +=	"," + PortalConstants.MNG_L3_ACT_CREATE;

        String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
        updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
        updatePermission +=	"," + PortalConstants.MNG_L3_ACT_UPDATE;

        String readPermission = PortalConstants.ADMIN_GP_UPDATE;
        readPermission +=	"," + PortalConstants.PG_GP_UPDATE;
        readPermission +=	"," + PortalConstants.MNG_L3_AMDF_READ;
        readPermission +=	"," + PortalConstants.MNG_L3_KYC_READ;
        readPermission +=	"," + PortalConstants.MNG_L3_ACT_READ;

        String agentUpdatePermission = PortalConstants.ADMIN_GP_UPDATE;
        agentUpdatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
        agentUpdatePermission +=	"," + PortalConstants.MNG_AGNTS_UPDATE;
    %>
</head>
<body bgcolor="#ffffff">

<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" />
            <br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>
<html:form name="handlerActivityReportForm" commandName="handlerActivityReportModel" method="post"
           action="p_handleractivityreport.html" onsubmit="return validateForm(this)">
    <table width="1000px" border="0">
        <tr>
            <td align="right" class="formText">
                Agent ID:
            </td>
            <td align="left">
                <html:input path="agentId" cssClass="textBox" maxlength="10" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right" width="20%">
                Mobile No:
            </td>
            <td align="left" width="30%">
                <html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right" width="20%">
                CNIC:
            </td>
            <td align="left" width="30%">
                <html:input path="cnic" cssClass="textBox" maxlength="13" tabindex="2" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right" width="20%">
                MB Account No:
            </td>
            <td align="left" width="30%">
                <html:input path="jsCashAccountNo" cssClass="textBox" maxlength="11" tabindex="3" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">
                Account Type:
            </td>
            <td align="left">
                <html:select path="customerAccountTypeId" cssClass="textBox" tabindex="4">
                    <html:option value="">---All---</html:option>
                    <c:if test="${accountTypeList != null}">
                        <html:options items="${accountTypeList}" itemValue="customerAccountTypeId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
            <td align="right" class="formText">Account Status:</td>
            <td align="left">
                <html:select path="jcashAccountStatus" cssClass="textBox" tabindex="9">
                    <html:option value="">---All---</html:option>
                    <html:option value="ACTIVE">ACTIVE</html:option>
                    <html:option value="IN-ACTIVE">IN-ACTIVE</html:option>
                </html:select>
            </td>
        </tr>
        <tr>
        <tr>
            <td align="right" class="formText">
                Core Ac #:
            </td>
            <td align="left">
                <html:input path="coreAccountNo" cssClass="textBox" maxlength="10" tabindex="1" onkeypress="return maskNumber(this,event)"/>
            </td>
            <td class="formText" align="right" width="20%">
                Sales User:
            </td>
            <td align="left" width="30%">
                <html:input path="salesUser" cssClass="textBox" maxlength="150" tabindex="3" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">
                Transaction Start Date:
            </td>
            <td align="left">
                <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="sDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td align="right" class="formText">
                Transaction End Date:
            </td>
            <td align="left">
                <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                &nbsp;
            </td>
            <td align="left">
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
                <input name="_search" type="submit" class="button" value="Search" tabindex="10"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_handleractivityreport.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="11" />
            </td>
            <td colspan="2">
                &nbsp;
            </td>
        </tr>
    </table>
</html:form>
<ec:table items="handlerActivityReportModelList" var="handlerActivityReportModel"
          retrieveRowsCallback="limit" filterRowsCallback="limit"
          sortRowsCallback="limit" action="${contextPath}/p_handleractivityreport.html?actionId=${retriveAction}"
          title="" width="100%" filterable="false" sortable="true">

    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Handler Activity Report.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Handler Activity Report.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Handler Activity Report" fileName="Handler Activity Report.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Handler Activity Report.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <%--        <c:set var="appUserId">--%>
        <%--            <security:encrypt strToEncrypt="${bdeKpiReportViewModel.appUserId}"></security:encrypt>--%>
        <%--        </c:set>--%>
        <ec:column title="Agent Id" property="agentId"/>
        <ec:column title="J Cash Ac #" property="jsCashAccountNo"/>
        <ec:column title="Core Ac #" property="coreAccountNo"/>
        <ec:column title="Account Type" property="accountType"/>
        <ec:column title="Agent Business Name" property="agentBusinessName"/>
        <ec:column title="First Name" property="firstName"/>
        <ec:column title="Last Name" property="lastName"/>
        <ec:column title="Address" property="address"/>
        <ec:column title="CNIC #" property="cnic"/>
        <ec:column title="Mobile No" property="mobileNo"/>
        <ec:column title="Filer Status" property="filerStatus"/>
        <ec:column title="Agent Network" property="agentNetwork"  style="text-align:center;"/>
        <ec:column title="Region" property="region"/>
        <ec:column title="Area" property="area"/>
        <ec:column title="Area Level" property="areaLevelName"/>
        <ec:column title="City" property="city"/>
        <ec:column title="Tax Regime" property="taxRegime"/>
        <ec:column title="Sales User" property="salesUser"/>
        <ec:column title="Agent Level" property="agentLevel"/>
        <ec:column title="Is Main Agent" property="isMainAgent"/>
        <ec:column title="Handler Agent ID" property="handlerAgentId"/>
        <ec:column title="Account Opening Date" property="accountOpeningDate" cell="date" format="yyyy-MM-dd"/>
        <ec:column title="Transaction Type" property="transactionType"/>
        <ec:column title="Transaction Count" property="transactionCount"/>
        <ec:column title="Transaction Amount" property="transactionAmount"/>
        <ec:column title="Transaction Date" property="transactionSummaryDate" cell="date" format="yyyy-MM-dd"/>
        <ec:column title="AC Closure Date" property="accountClosingDate"/>
        <ec:column title="J Cash Account Status" property="jcashAccountStatus"/>
        <ec:column title="Core Account Status" property="coreAccountStatus"/>
        <ec:column title="Credentials Expired" property="credentialExpired"/>
        <ec:column title="Available Balance" property="accountBalance"/>
    </ec:row>
</ec:table>

<script language="javascript" type="text/javascript">
    // document.forms[0].userId.focus();

    Calendar.setup(
        {
            inputField  : "startDate", // id of the input field
            button      : "sDate",    // id of the button
        }
    );
    Calendar.setup(
        {
            inputField  : "endDate", // id of the input field
            button      : "eDate",    // id of the button
            isEndDate: true
        }
    );
</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>


