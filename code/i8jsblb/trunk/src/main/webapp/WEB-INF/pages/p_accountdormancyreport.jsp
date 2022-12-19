<%--
  Created by IntelliJ IDEA.
  User: abubakar.farooque
  Date: 10/27/2022
  Time: 7:32 PM
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
    <meta name="title" content="Account Dormancy Report" />
    <script type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
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
<html:form name="accountDormancyReportForm" commandName="accountDormancyReportViewModel" method="post"
           action="p_accountdormancyreport.html" onsubmit="return validateForm(this)">
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
                <html:input path="mbAccountNo" cssClass="textBox" maxlength="11" tabindex="3" onkeypress="return maskNumber(this,event)"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">
                Account Type:
            </td>
            <td align="left">
                <html:select path="customerAccountTypeId" cssClass="textBox" tabindex="4">
                    <html:option value="">---All---</html:option>
                    <c:if test="${olaCustomerAccountTypeModelList != null}">
                        <html:options items="${olaCustomerAccountTypeModelList}" itemValue="customerAccountTypeId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
            <td class="formText" align="right">
                Segment:
            </td>
            <td align="left">
                <html:select path="segmentId" cssClass="textBox" tabindex="10" >
                    <html:option value="">---All---</html:option>
                    <c:if test="${segmentModelList != null}">
                        <html:options items="${segmentModelList}" itemValue="segmentId" itemLabel="name"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">Dormancy Status:</td>
            <td align="left">
                <html:select path="dormantStatus" cssClass="textBox" tabindex="10">
                    <html:option value="">---All---</html:option>
                    <html:option value="YES">Yes</html:option>
                    <html:option value="NO">No</html:option>
                </html:select>
            </td>
            <td align="right" class="formText">Account Status:</td>
            <td align="left">
                <html:select path="accountStatus" cssClass="textBox" tabindex="9">
                    <html:option value="">---All---</html:option>
                    <html:option value="ACTIVE">ACTIVE</html:option>
                    <html:option value="IN-ACTIVE">IN-ACTIVE</html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">
                Start Date:
            </td>
            <td align="left">
                <html:input path="startDate" id="startDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="sDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('startDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td align="right" class="formText">
                End Date:
            </td>
            <td align="left">
                <html:input path="endDate" id="endDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="eDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('endDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">
                Dormancy Removed On Start Date:
            </td>
            <td align="left">
                <html:input path="dormancyRemovedOnStartDate" id="dormancyRemovedOnStartDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="dormancyRemovedOnSDate" tabindex="6" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="dormancyRemovedOnSDate" tabindex="7" title="Clear Date" name="popcal" onclick="javascript:$('dormancyRemovedOnStartDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td align="right" class="formText">
                Dormancy Removed On End Date:
            </td>
            <td align="left">
                <html:input path="dormancyRemovedOnEndDate" id="dormancyRemovedOnEndDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="dormancyRemovedOnEDate" tabindex="8" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="dormancyRemovedOnEDate" tabindex="9" title="Clear Date" name="popcal" onclick="javascript:$('dormancyRemovedOnEndDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
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
                       onclick="javascript: window.location='p_accountdormancyreport.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="11" />
            </td>
            <td colspan="2">
                &nbsp;
            </td>
        </tr>
    </table>
</html:form>
<ec:table items="accountDormancyReportViewModelList" var="accountDormancyReportViewModel"
          retrieveRowsCallback="limit" filterRowsCallback="limit"
          sortRowsCallback="limit" action="${contextPath}/p_accountdormancyreport.html?actionId=${retriveAction}"
          title="" width="100%" filterable="false" sortable="true">

    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Account Dormancy Report.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Account Dormancy Report.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Account Dormancy Report" fileName="Dormancy Report.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Account Dormancy Report.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <%--        <c:set var="appUserId">--%>
        <%--            <security:encrypt strToEncrypt="${bdeKpiReportViewModel.appUserId}"></security:encrypt>--%>
        <%--        </c:set>--%>
        <ec:column title="Agent/Customer Id" property="agentId"/>
        <ec:column title="Mobile No" property="mobileNo"/>
        <ec:column title="CNIC #" property="cnic"/>
        <ec:column title="MB Account #" property="mbAccountNo"/>
        <ec:column title="Debit Card Number" property="debitCardNumber"/>
        <ec:column title="Account Type" property="accountType"/>
        <ec:column title="Customer Name" property="customerName"/>
        <ec:column title="Regime" property="taxRegime"/>
        <ec:column title="City" property="city"/>
        <ec:column title="Account Opening Date" property="accountOpeningDate" cell="date" format="yyyy-MM-dd"/>
        <ec:column title="Last Activity Date" property="lastActivityDate" cell="date" format="yyyy-MM-dd"/>
        <ec:column title="Segment" property="segmentName"/>
        <ec:column title="Account Status" property="accountStatus"/>
        <ec:column title="Dormancy Marked On" property="dormancyMarkedOn" cell="date" format="yyyy-MM-dd"/>
        <ec:column title="Dormancy Removed On" property="dormancyRemovedOn" cell="date" format="yyyy-MM-dd"/>
        <ec:column title="Dormancy Removed By" property="dormancyRemovedBy"/>
        <ec:column title="Available Balance" property="accountBalance"/>
        <ec:column title="Dormant Required" property="dormantStatus"/>
        <ec:column title="Reason" property="reason"/>
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

    Calendar.setup(
        {
            inputField  : "dormancyRemovedOnStartDate", // id of the input field
            button      : "dormancyRemovedOnSDate",    // id of the button
        }
    );
    Calendar.setup(
        {
            inputField  : "dormancyRemovedOnEndDate", // id of the input field
            button      : "dormancyRemovedOnEDate",    // id of the button
            isEndDate: true
        }
    );

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>


