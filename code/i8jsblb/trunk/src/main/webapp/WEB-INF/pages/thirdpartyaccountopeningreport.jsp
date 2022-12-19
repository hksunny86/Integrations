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
    <meta name="title" content="Third Party Account Opening Report" />
    <script language="javascript" type="text/javascript">
        var jq=$.noConflict();
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
        function error(request)
        {
            alert("An unknown error has occured. Please contact with the administrator for more details");
        }

        var childWindow;
        function closeChild()
        {
            try
            {
                if(childWindow != undefined)
                {
                    childWindow.close();
                    childWindow=undefined;
                }
            }catch(e){}
        }
    </script>

</head>
<body bgcolor="#ffffff" onunload="javascript:closeChild();">

<div id="rsp" class="ajaxMsg"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false" /><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session" />
</c:if>

<html:form name='thirdPartyAccountOpeningReport' commandName="thirdPartyAccountOpeningReport" method="post"
           action="thirdpartyaccountopeningreport.html" onsubmit="return validateFormData(this)" >
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left"><html:input path="customerMobileNumber" cssClass="textBox" maxlength="11" tabindex="3" /></td>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left"><html:input path="customerNic" cssClass="textBox" maxlength="13" tabindex="4" /></td>
        </tr>
        <tr>
            <td class="formText" align="right">Card No:</td>
            <td align="left"><html:input path="debitCardNumber" cssClass="textBox" maxlength="16" tabindex="4" /></td>
            <td class="formText" align="right">Agent ID:</td>
            <td align="left"><html:input path="agentId" cssClass="textBox" maxlength="16" tabindex="4" /></td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Created On From Date:
            </td>
            <td align="left">
                <html:input path="createdOn" id="createdOnFromDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal" onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Created On To Date:
            </td>
            <td align="left">
                <html:input path="createdOnToDate" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox" maxlength="10"/>
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
                       onclick="javascript: window.location='thirdpartyaccountopeningreport.html?actionId=${retriveAction}'"
                       class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="thirdPartyList" var="thirdPartyModel"
          action="${contextPath}/thirdpartyaccountopeningreport.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Third Party Account Opening Requests.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Third Party Account Opening Requests.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Third Party Account Opening Requests"
                      fileName="Third Party Account Opening Requests.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Third Party Account Opening Requests.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column property="agentId" title="Agent Id" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="customerMobileNumber" title="Customer Mobile No" escapeAutoFormat="true"/>
        <ec:column property="customerNic" title="Customer CNIC" escapeAutoFormat="true"/>
        <ec:column property="debitCardNumber" title="Debit Card No" escapeAutoFormat="true"/>
        <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Created On"/>
        <ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Updated On"/>
        <ec:column property="status" title="Status" style="text-align: center">
            <c:if test="${!thirdPartyModel.status}">Un-Successful</c:if>
            <c:if test="${thirdPartyModel.status }">Successful</c:if>
        </ec:column>
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
