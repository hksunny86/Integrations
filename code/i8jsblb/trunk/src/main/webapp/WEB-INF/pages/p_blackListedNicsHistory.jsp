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
    <meta name="title" content="BlackListed CNIC Report" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
    </script>

</head>
<body bgcolor="#ffffff">

<html:form name='blackListedNicHistory'
           commandName="blackListedNicHistoryViewModel" method="post"
           action="p_blackListedNicsHistory.html" onsubmit="return validateFormData(this)" >
    <table width="850px">
        <tr>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left">
                <html:input path="cNic" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13" tabindex="2" value=""/>
            </td>
            <td align="right" class="formText">User:</td>
            <td align="left">
                <html:select path="appUserTypeId" cssClass="textBox"  tabindex="5">
                    <html:option value="">---All---</html:option>
                    <c:if test="${appUserTypeModelList != null}">
                        <html:options items="${appUserTypeModelList}" itemLabel="name" itemValue="appUserTypeId"/>
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">User ID:</td>
            <td align="left">
                <html:input path="userId" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13" tabindex="2" value=""/>
            </td>

            <td align="right" class="formText">Mobile #:</td>
            <td align="left">
                <html:input path="mobileNo" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13" tabindex="2" value=""/>
            </td>
        </tr>
        <tr>
            <td class="formText" align="right">From Date:</td>
            <td align="left">
                <html:input path="actionDate" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">To Date:</td>
            <td align="left">
                <html:input path="actionDateEnd" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
                <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal" onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>
        <tr>
            <td align="right" class="formText">Blacklisted Status</td>
            <td align="left">
                <html:select path="action" cssClass="textBox" tabindex="3">
                    <html:option value="">---All---</html:option>
                    <html:option value="Marked">Marked</html:option>
                    <html:option value="Unmarked">Unmarked</html:option>
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
            <td  align="right" class="formText">
                Account State:
            </td>
            <td align="left">
                <html:select path="accountStatus" cssClass="textBox">
                    <html:option value="">--All---</html:option>
                    <html:option value="OPEN">OPEN</html:option>
                    <html:option value="CLOSED UNSETTLED<">CLOSED UNSETTLED</html:option>
                    <html:option value="CLOSED SETTLED">CLOSED SETTLED</html:option>
                </html:select>
            </td>
            <td align="right" class="formText">
                <span style="color: #FF0000">*</span>Customer Account Type:
            </td>
            <td align="left" cssClass="textBox">
                <html:select id="userAccountTypeId" path="userAccountTypeId" cssClass="textBox">
                    <html:option value="">[All]</html:option>
                    <c:if test="${customerAccountTypeList != null}">
                        <html:options items="${customerAccountTypeList}" itemLabel="name" itemValue="customerAccountTypeId" />
                    </c:if>
                </html:select>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText"></td>
            <td align="left" colspan="2" class="formText">
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
                       value="<%=PortalConstants.ACTION_RETRIEVE%>">
                <input type="submit" class="button" id="searchButton" value="Search" tabindex="13" />
                <input type="reset" class="button" value="Cancel" name="_reset" tabindex="14" onclick="javascript: window.location='p_blackListedNicsHistory.html'"/>
            </td>
            <td align="left" class="formText"></td>
            <td align="left"></td>
        </tr>
    </table>
</html:form>
<ec:table items="blackListedNicHistoryList" var="history"
          action="${contextPath}/p_blackListedNicsHistory.html?actionId=${retriveAction}"
          title="Hello"
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"
>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="BlackListed NIC History.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="BlackListed NIC History.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="BlackListed NIC History"
                      fileName="BlackListed NIC History.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="BlackListedNICHistory.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="userId" title="Customer/Agent ID" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="firstName" title="First Name" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="lastName" title="Last Name" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="cardNo" title="Debit Card #" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="segmentName" title="Segment" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="cNic" title="CNIC" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="userAccountTypeName" title="Level of Account" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="accountNumber" title="Account Number" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="businessName" title="Business Name" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="accountOpeningDate" title="A/C Opening Date" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="lastActivityDate" title="Last Activity Date" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="regStateName" title="Registration Status" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="taxRegimeId" title="Tax Regime Id" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="taxRegimeName" title="Tax Regime Name" escapeAutoFormat="true" filterable="false" style="text-align: center"/>


        <ec:column property="accountStatus" title="Account Status" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="action" title="Status" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="actionDate" title="Action Date" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="actionPerformedBy" title="Action Performed By" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
        <ec:column property="comments" title="Comments" escapeAutoFormat="true" filterable="false" style="text-align: center"/>
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
