<%@include file="/common/taglibs.jsp" %>
<%@ page import='com.inov8.microbank.common.util.PortalConstants' %>
<%@ page import='com.inov8.microbank.common.util.PortalDateUtils' %>

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
    <meta name="title" content="BlackListed Walk-In CNIC Report"/>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();
        var serverDate = "<%=PortalDateUtils.getServerDate()%>";
    </script>

</head>
<body bgcolor="#ffffff">

<html:form name='blackListedNicHistory'
           commandName="walkInBlackListedNicViewModel" method="post"
           action="p_walkInBlackListedNicReport.html" onsubmit="return validateFormData(this)">
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left">
                <html:input path="cNic" onkeypress="return maskInteger(this,event)" cssClass="textBox" maxlength="13"
                            tabindex="2" value=""/>
            </td>

            <td align="right" class="formText">Blacklisted Status</td>
            <td align="left">
                <html:select path="action" cssClass="textBox" tabindex="3">
                    <html:option value="">All[]</html:option>
                    <html:option value="Marked">Marked</html:option>
                    <html:option value="Unmarked">Unmarked</html:option>
                </html:select>
            </td>

            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td class="formText" align="right">
                Marked On From Date:
            </td>
            <td align="left">
                <html:input path="actionDate" id="createdOnFromDate" readonly="true" tabindex="-1" cssClass="textBox"
                            maxlength="10"/>
                <img id="CreatedFromDate" tabindex="7" name="popcal" align="top" style="cursor:pointer"
                     src="images/cal.gif" border="0"/>
                <img id="CreatedFromDate" tabindex="8" title="Clear Date" name="popcal"
                     onclick="javascript:$('createdOnFromDate').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
            <td class="formText" align="right">
                Marked On To Date:
            </td>
            <td align="left">
                <html:input path="actionEndDate" id="createdOnToDate" readonly="true" tabindex="-1" cssClass="textBox"
                            maxlength="10"/>
                <img id="CreatedToDate" tabindex="9" name="popcal" align="top" style="cursor:pointer"
                     src="images/cal.gif" border="0"/>
                <img id="CreatedToDate" tabindex="10" title="Clear Date" name="popcal"
                     onclick="javascript:$('createdOnToDate').value=''" align="middle" style="cursor:pointer"
                     src="images/refresh.png" border="0"/>
            </td>
        </tr>
        <tr>
            <td align="right" class="formText"></td>
            <td align="left" colspan="2" class="formText">
                <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>"
                       value="<%=PortalConstants.ACTION_RETRIEVE%>">
                <input name="_search" type="submit" class="button" value="Search" tabindex="13"/>
                <input type="reset" class="button" value="Cancel" name="_reset" tabindex="14"
                       onclick="javascript: window.location='p_walkInBlackListedNicReport.html'"/>
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table>
</html:form>
<ec:table items="walkInBlackListedNicList" var="history"
          action="${contextPath}/p_walkInBlackListedNicReport.html"
          title="Hello"
          retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit"
          filterable="false"
>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="BlackListed (Walk-In) NIC History.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="BlackListed (Walk-In) NIC History.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="BlackListed (Walk-In) NIC History"
                      fileName="BlackListed (Walk-In) NIC History.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="WalkInBlackListedNICHistory.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="cNic" title="CNIC" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="action" title="Blacklisted Status" escapeAutoFormat="true" filterable="false"
                   style="text-align: center"/>
        <ec:column property="actionDate" title="Marked/Un-Marked Date" escapeAutoFormat="true" filterable="false"
                   style="text-align: center"/>
        <ec:column property="actionPerformedBy" title="Marked/Un-Marked By" escapeAutoFormat="true" filterable="false"
                   style="text-align: center"/>
<%--        <ec:column property="unMarkedDate" title="Unmarked Date" escapeAutoFormat="true" filterable="false"--%>
<%--                   style="text-align: center"/>--%>
<%--        <ec:column property="unMarkedBy" title="Unmarked By" escapeAutoFormat="true" filterable="false"--%>
<%--                   style="text-align: center"/>--%>


        <ec:column property="comments" title="Comments" escapeAutoFormat="true" filterable="false"
                   style="text-align: center"/>
    </ec:row>
</ec:table>


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
        if (isValid) {
            var _fDate = undefined;
            var _tDate = undefined;

            if (form.createdOnFromDate != undefined) {
                _fDate = form.createdOnFromDate.value;
            }
            if (form.createdOnToDate != undefined) {
                _tDate = form.createdOnToDate.value;
            }

            var startlbl = "Created On From Date";
            var endlbl = "Created On To Date";

            isValid = isValidDateRange(_fDate, _tDate, startlbl, endlbl, serverDate);
        }
        return isValid;
    }

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>

</body>
</html>
