<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils'%>

<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

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
    <link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
    <link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
    <meta name="title" content="CNIC Expiry Report" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
    </script>

</head>
<body bgcolor="#ffffff">

<html:form name='expiredNicRportForm' commandName="expiredNicViewModel" method="post"
           action="p_expiredNicReport.html" onsubmit="return validateFormData(this)" >
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left"><html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3" /></td>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left"><html:input path="cNic" cssClass="textBox" maxlength="13" tabindex="4" /></td>
        </tr>
        <tr>
            <td align="right" class="formText">User ID:</td>
            <td align="left">
                <html:input path="userId" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13" tabindex="5" value=""/>
            </td>
            <td align="right" class="formText">Account #:</td>
            <td align="left">
                <html:input path="accountNumber" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13" tabindex="6" value=""/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">Account Type:</td>
            <td align="left">
                <html:select path="customerAccountTypeId" cssClass="textBox" tabindex="7">
                    <html:option value="">All[]</html:option>
                    <<c:if test="${olaCustomerAccountTypeModelList != null}">
                    <html:options items="${olaCustomerAccountTypeModelList}" itemLabel="name" itemValue="customerAccountTypeId"/>
                </c:if>
                </html:select>
            </td>
            <td align="right" class="formText">Segment:</td>
            <td align="left">
                <html:select path="segmentId" cssClass="textBox" tabindex="8">
                    <html:option value="">All[]</html:option>
                    <<c:if test="${segmentModelList != null}">
                    <html:options items="${segmentModelList}" itemLabel="name" itemValue="segmentId"/>
                </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">Account Status:</td>
            <td align="left">
                <html:select path="accountStatus" cssClass="textBox" tabindex="9">
                    <html:option value="">All[]</html:option>
                    <html:option value="OPEN">Open</html:option>
                    <html:option value="CLOSED UNSETTLED">Closed Unsetteled</html:option>
                    <html:option value="CLOSED SETTLED">Closed Setteled</html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Expired On From Date:
            </td>
            <td align="left">
                <html:input path="cNicExpiryDate" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Expired On To Date:
            </td>
            <td align="left">
                <html:input path="cNicEpiryDateEnd" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">

            </td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                       onclick="javascript: window.location='p_expiredNicReport.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="expiredNicDataList" var="expiredNic"
          action="${contextPath}/p_expiredNicReport.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Expired Nic.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Expired Nic.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Expired NIC Report"
                      fileName="Expired Nic.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Expired Nic.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column property="userId" title="Customer/Agent ID" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="accountType" title="Level of Account" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="accountNumber" title="Account Number" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="firstName" title="First Name" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="lastName" title="Last Name" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="businessName" title="Business Name" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="cNic" title="CNIC" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="accountOpeningDate" title="A/C Opening Date" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="lastActivityDate" title="Last Activity Date" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="cNicExpiryDate" title="NIC Expiry Date" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="regStatus" title="Registration Status" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="accountStatus" title="Account Status" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="segment" title="Segment" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="cardNo" title="Debit Card No." escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="city" title="City" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="regime" title="Regime" escapeAutoFormat="true" filterable="false" style="text-align: center"/>

    </ec:row>
</ec:table>


<script language="javascript" type="text/javascript">

    Calendar.setup(
        {
            inputField  : "createdOnFromDate", // id of the input field
            button      : "CreatedFromDate",    // id of the button
        }
    );
    Calendar.setup(
        {
            inputField  : "createdOnToDate", // id of the input field
            button      : "CreatedToDate",    // id of the button
            isEndDate: true
        }
    );

    function validateFormData(form){
        var isValid=validateForm(form);
        if(isValid){
            var _fDate =undefined;
            var _tDate = undefined;

            if(form.createdOnFromDate!=undefined)
            {
                _fDate = form.createdOnFromDate.value;
            }
            if(form.createdOnToDate!=undefined)
            {
                _tDate = form.createdOnToDate.value;
            }

            var startlbl = "Created On From Date";
            var endlbl = "Created On To Date";

            isValid=	isValidDateRange(_fDate,_tDate,startlbl,endlbl,serverDate);
        }
        return isValid;
    }

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>
