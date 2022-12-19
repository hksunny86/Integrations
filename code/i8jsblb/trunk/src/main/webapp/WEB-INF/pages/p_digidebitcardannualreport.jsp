<%--
  Created by IntelliJ IDEA.
  User: abubakar.farooque
  Date: 9/23/2022
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<!--Title: i8Microbank-->
<%@include file="/common/taglibs.jsp" %>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants' %>
<html>
<head>
    <c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %>
    </c:set>
    <c:set var="createAction"><%=PortalConstants.ACTION_CREATE %>
    </c:set>
    <meta name="decorator" content="decorator">
    <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar_new.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/calendar-setup.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>

    <link rel="stylesheet" type="text/css" href="styles/deliciouslyblue/calendar.css"/>
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <meta name="title" content="Digi Debit Card Annual Report"/>
    <script type="text/javascript">
        var jq = $.noConflict();
        var serverDate = "<%=PortalDateUtils.getServerDate()%>";
    </script>
    <%--    <%--%>
    <%--        String createPermission = PortalConstants.ADMIN_GP_CREATE;--%>
    <%--        createPermission +=	"," + PortalConstants.PG_GP_CREATE;--%>
    <%--        createPermission +=	"," + PortalConstants.MNG_L3_ACT_CREATE;--%>

    <%--        String updatePermission = PortalConstants.ADMIN_GP_UPDATE;--%>
    <%--        updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;--%>
    <%--        updatePermission +=	"," + PortalConstants.MNG_L3_ACT_UPDATE;--%>

    <%--        String readPermission = PortalConstants.ADMIN_GP_UPDATE;--%>
    <%--        readPermission +=	"," + PortalConstants.PG_GP_UPDATE;--%>
    <%--        readPermission +=	"," + PortalConstants.MNG_L3_AMDF_READ;--%>
    <%--        readPermission +=	"," + PortalConstants.MNG_L3_KYC_READ;--%>
    <%--        readPermission +=	"," + PortalConstants.MNG_L3_ACT_READ;--%>

    <%--        String agentUpdatePermission = PortalConstants.ADMIN_GP_UPDATE;--%>
    <%--        agentUpdatePermission +=	"," + PortalConstants.PG_GP_UPDATE;--%>
    <%--        agentUpdatePermission +=	"," + PortalConstants.MNG_AGNTS_UPDATE;--%>
    <%--    %>--%>
</head>
<body bgcolor="#ffffff">

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
<html:form name="digidebitcardviewForm" commandName="DigiDebitCardAnnualReportController" method="post"
           action="${contextPath}/p_digidebitcardannualreport.html" onsubmit="return validateForm(this)">
    <table width="1000px" border="0">
        <tr>
            <td align="right" class="formText">
                Start Date:
            </td>
            <td align="left">
                <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox"
                            maxlength="10"/>
                <img id="sDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="sDate" tabindex="7" title="Clear Date" name="popcal"
                     onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
            <td align="right" class="formText">
                End Date:
            </td>
            <td align="left">
                <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox"
                            maxlength="10"/>
                <img id="eDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif"
                     border="0"/>
                <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''"
                     align="middle" style="cursor:pointer" src="images/refresh.png" border="0"/>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                &nbsp;
            </td>
            <td align="left">
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
                       value="<%=PortalConstants.ACTION_RETRIEVE%>">
                <input name="_search" type="submit" class="button" value="Search" tabindex="10"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_digidebitcardannualreport.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="11"/>
            </td>
            <td colspan="2">
                &nbsp;
            </td>
        </tr>
    </table>
</html:form>
<ec:table items="reqList" var="model"
          retrieveRowsCallback="limit" filterRowsCallback="limit"
          sortRowsCallback="limit" action="${contextPath}/p_digidebitcardannualreport.html?actionId=${retriveAction}"
          title="" width="100%" filterable="false" sortable="true">

    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Digi Debit Card Report.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Digi Debit Card Report.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Digi Debit Report" fileName="Digi Debit Card Report.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Digi Debit Card Report.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <%--        <c:set var="appUserId">--%>
        <%--            <security:encrypt strToEncrypt="${bdeKpiReportViewModel.appUserId}"></security:encrypt>--%>
        <%--        </c:set>--%>
        <ec:column title="Embossing Name" property="embossingName"/>
        <ec:column title="Card No" property="cardNo"/>
        <%--        <ec:column title="Core Ac #" property="coreAccountNo"/>--%>
        <ec:column title="Cnic" property="cnic"/>
        <ec:column title="Mobile No" property="mobileNo"/>
        <ec:column title="Issuance Date" property="issuanceDate"/>
        <ec:column title="Current Date" property="currentDate"/>
        <ec:column title="Address" property="address"/>
        <ec:column title="Days" property="days"/>
        <ec:column title="Is Deduction Required" property="isDeductionRequired"/>
        <ec:column title="Customer Account Type" property="customerAccountType"/>
        <ec:column title="From Account" property="fromAccount"/>
        <ec:column title="From Account Name" property="fromAccountName"/>
        <ec:column title="To Account" property="toAccount"/>
        <ec:column title="To Account Name" property="toAccountName"/>
        <ec:column title="Microbank Segment" property="microbankSegment"/>
        <ec:column title="Deduction Amount" property="deductionAmount"/>
        <ec:column title="Deduction Date" property="deductionDate"/>
        <ec:column title="Comments" property="comments"/>
        <ec:column title="Card Request Date" property="cardRequestDate"/>
    </ec:row>
</ec:table>

<script language="javascript" type="text/javascript">
    // document.forms[0].userId.focus();

    Calendar.setup(
        {
            inputField: "startDate", // id of the input field
            button: "sDate",    // id of the button
        }
    );
    Calendar.setup(
        {
            inputField: "endDate", // id of the input field
            button: "eDate",    // id of the button
            isEndDate: true
        }
    );
</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>

