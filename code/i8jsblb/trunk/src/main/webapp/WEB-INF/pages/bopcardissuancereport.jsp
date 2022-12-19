<%--
  Created by IntelliJ IDEA.
  User: Abubakar Farooque
  Date: 10/13/2020
  Time: 2:08 PM
  To change this template use File | Settings | File Templates.
--%>
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
    <meta name="title" content="BOP Card Issuance Report" />
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

<html:form name='bopCardIssuanceReport' commandName="bopcardissuancereport" method="post"
           action="bopcardissuancereport.html" onsubmit="return validateFormData(this)" >
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
                Transaction Date - Start:
            </td>
            <td align="left">
                <html:input path="createdOn" id="startDate" readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="sDate" tabindex="11" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="sDate" tabindex="12" title="Clear Date" name="popcal"  onclick="javascript:$('startDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
            <td class="formText" align="right">
                Transaction Date - End:
            </td>
            <td align="left">
                <html:input path="createdOnToDate" id="endDate"  readonly="true" tabindex="-1"  cssClass="textBox" maxlength="10"/>
                <img id="eDate" tabindex="13" name="popcal"  align="top" style="cursor:pointer" src="images/cal.gif" border="0" />
                <img id="eDate" tabindex="14" title="Clear Date" name="popcal"  onclick="javascript:$('endDate').value=''"   align="middle" style="cursor:pointer" src="images/refresh.png" border="0" />
            </td>
        </tr>

        <tr>
            <td class="formText" align="right">

            </td>
            <td align="left">
                <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
                <input name="reset" type="reset"
                    onclick="javascript: window.location='bopcardissuancereport.html?actionId=${retriveAction}'"
                    class="button" value="Cancel" tabindex="13" />
            </td>
            <td class="formText" align="right">

            </td>
            <td align="left">&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table></html:form>

<ec:table items="bopCardIssuanceList" var="history"
          action="${contextPath}/bopcardissuancereport.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="BOP Card Issuance Requests.xls" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="BOP Card Issuance Requests.xlsx" tooltip="Export Excel" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da" headerTitle="Third Party Account Opening Requests"
                      fileName="BOP Card Issuance Requests.pdf" tooltip="Export PDF" />
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="BOP Card Issuance Requests.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column property="agentId" title="Agent Id" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="agentLocation" title="Agent Location" escapeAutoFormat="True" style="text-align: center"/>
        <ec:column property="customerMobileNumber" title="Customer Mobile No" escapeAutoFormat="true"/>
        <ec:column property="customerNic" title="Customer CNIC" escapeAutoFormat="true"/>
        <ec:column property="debitCardNumber" title="Debit Card No" escapeAutoFormat="true"/>
        <ec:column property="segment" title="Segment" escapeAutoFormat="true"/>
        <ec:column property="issuanceDate" cell="date" format="dd/MM/yyyy hh:mm a" title="Issuance Date"/>
        <ec:column property="createdOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Created On"/>
        <ec:column property="updatedOn" cell="date" format="dd/MM/yyyy hh:mm a" title="Updated On"/>
    </ec:row>
</ec:table>

<script language="javascript" type="text/javascript">

    Calendar.setup({
        inputField : "startDate", // id of the input field
        button : "sDate", // id of the button
    });

    Calendar.setup({
        inputField : "endDate", // id of the input field
        button : "eDate", // id of the button
        isEndDate : true
    });

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>
