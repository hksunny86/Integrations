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
    <meta name="title" content="Update Customer Name"/>
    <script language="javascript" type="text/javascript">
        var jq = $.noConflict();
        var serverDate = "<%=PortalDateUtils.getServerDate()%>";
    </script>

</head>
<body bgcolor="#ffffff">

<html:form name='updateCustomerNameModelForm' commandName="updateCustomerNameModel" method="post"
           action="p_updateCustomerNameInfo.html" onsubmit="return validateFormData(this)">
    <table width="100%" border="0">
        <tr>
            <td align="right" class="formText">Mobile #:</td>
            <td align="left"><html:input path="mobileNo" cssClass="textBox" maxlength="11" tabindex="3"/></td>
            <td align="right" class="formText">CNIC #:</td>
            <td align="left"><html:input path="cnic" cssClass="textBox" maxlength="13" tabindex="4"/></td>
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

        <tr>
            <td align="left" colspan="2" class="formText"></td>
        <td align="left">
        <input name="_search" type="submit" class="button" value="Search" tabindex="12"/>
            <input type="reset" class="button" value="Cancel" name="_reset" onclick="javascript: window.location='p_updateCustomerNameInfo.html'" tabindex="13" />
        </td>
        </tr>



        <input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
    </table>
</html:form>

<ec:table items="updateCustomerNameModelList" var="updateCustomerName"
          action="${contextPath}/p_expiredNicReport.html"
          title="" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" filterable="false">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Update Customer Name.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Update Customer Name.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Expired NIC Report"
                      fileName="Update Customer Name.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Update Customer Name.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>

    <ec:row>
        <ec:column property="firstName" title="First Name" escapeAutoFormat="true" filterable="false"
                   style="text-align: center"/>
        <ec:column property="lastName" title="Last Name" escapeAutoFormat="true" filterable="false"
                   style="text-align: center"/>
        <ec:column property="mobileNo" title="Mobile Number" escapeAutoFormat="true" filterable="true"
                   style="text-align: center"/>
        <ec:column property="cnic" title="CNIC" escapeAutoFormat="true" filterable="true" style="text-align: center"/>
        <ec:column property="nadraName" title="Nadra Name" escapeAutoFormat="true" filterable="true"
                   style="text-align: center"/>
        <ec:column property="actionStatusIdActionStatusModel.name" filterable="false" title="Action Status"/>

        <ec:column property="cnic" title="View Details" filterable="false" sortable="false"
                   viewsAllowed="html">
            <c:if test="${updateCustomerName.updated == true}">
                <input type="button" class="button" value="Change Info" disabled="disabled"/>
            </c:if>
            <c:if test="${updateCustomerName.updated == false}">
                <input type="button" class="button" value="Change Info"
                       onclick="window.location='p_updateCustomerNameDetail.html?cnic=${updateCustomerName.cnic}'"/>
            </c:if>
            <%--            <input type="button" class="button" value="Rejected" onclick="window.location='p_updateCustomerNameDetail.html?cnic=${updateCustomerName.cnic}'"/>--%>
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

</script>

</body>
</html>
