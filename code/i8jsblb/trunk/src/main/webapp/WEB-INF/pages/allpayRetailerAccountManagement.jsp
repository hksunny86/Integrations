<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@ page import='com.inov8.microbank.common.util.*'%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<%@include file="/common/taglibs.jsp"%>
<html>

<head>
    <meta name="decorator" content="decorator">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
    <meta name="title" content="Agents"   id="<%=ReportIDTitleEnum.Agents.getId()%>" />
    <script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
    <script type="text/javascript">
        var serverDate ="<%=PortalDateUtils.getServerDate()%>";
        var jq=$.noConflict();
        var username = "<%=UserUtils.getCurrentUser().getUsername()%>";
        var appUserId= "<%=UserUtils.getCurrentUser().getAppUserId()%>";
        var email= "<%=UserUtils.getCurrentUser().getEmail()%>";

        function actdeact(request)
        {
            isOperationSuccessful(request);
        }
    </script>
    <%@include file="/WEB-INF/pages/export_zip.jsp"%>
    <%-- <script type="text/javascript" src="${contextPath}/scripts/exportzip.js"></script> --%>
    <%
        String readPermission = PortalConstants.MNG_AGNTS_READ;
        readPermission +=	"," + PortalConstants.PG_GP_READ;
        readPermission +=	"," + PortalConstants.CSR_GP_READ;
        readPermission +=	"," + PortalConstants.MNO_GP_READ;

        String printAgentFormPermission = PortalConstants.ADMIN_GP_READ;
        printAgentFormPermission += "," + PortalConstants.PG_GP_READ;
        printAgentFormPermission += "," + PortalConstants.MNO_GP_READ;
        printAgentFormPermission += "," + PortalConstants.AGNT_PRINT_FORM_READ;
    %>
</head>
<body bgcolor="#ffffff">

<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<ec:table items="retailerContactModelList" var="retailerContactListViewModel" retrieveRowsCallback="limit"
          filterRowsCallback="limit"
          sortRowsCallback="limit" action="${pageContext.request.contextPath}/allpayRetailerAccountManagement.html"
          filterable="true">
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
        <ec:exportXls fileName="Agents.xls" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
        <ec:exportXlsx fileName="Agents.xlsx" tooltip="Export Excel"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
        <ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
                      headerTitle="Agents" fileName="Agents.pdf" tooltip="Export PDF"/>
    </authz:authorize>
    <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
        <ec:exportCsv fileName="Agents.csv" tooltip="Export CSV"></ec:exportCsv>
    </authz:authorize>
    <ec:row>
        <ec:column property="allpayId" title="Agent ID" escapeAutoFormat="true"/>
        <ec:column property="agentBusinessName" title="Agent Business Name" escapeCommas="true"/>
        <ec:column property="firstName"/>
        <ec:column property="lastName"/>
        <ec:column property="retailerAddress" title="Address" escapeCommas="true"/>
        <ec:column property="nic" title="CNIC #" escapeAutoFormat="true"/>
        <ec:column property="mobileNo" escapeAutoFormat="true"/>
        <ec:column property="accountNo" escapeAutoFormat="true"/>
        <ec:column property="coreAccountStatus" escapeAutoFormat="true"/>
        <ec:column property="distributorName" title="Agent Network"/>
        <ec:column property="regionName" title="Region"/>
        <ec:column property="areaName" title="Area"/>
        <ec:column property="areaLevelName" title="Area Level"/>
        <ec:column property="cityName" title="City"/>
        <ec:column property="salesUserName" title="Sales User"/>
        <ec:column property="distributorLevelName" title="Agent Level"/>
        <ec:column property="headString" title="Is Main Agent"/>
        <ec:column property="parentAgentId" title="Parent Agent ID"/>
        <ec:column property="accountOpeningDate" filterable="false" cell="date" format="dd/MM/yyyy"
                   title="A/C Opening Date"/>
        <ec:column property="accountActive" filterable="false" title="Status (Active/Deactive)"
                   escapeAutoFormat="true"/>
        <ec:column property="isAccountLocked" filterable="false" title="Status (Locked/Unlocked)"
                   escapeAutoFormat="true"/>
        <ec:column property="isAccountClosed" filterable="false" title="Status (Open/Closed)" escapeAutoFormat="true"/>
        <ec:column property="isCredentialExpired" filterable="false" title="Credential Expired"
                   escapeAutoFormat="true"/>
        <authz:authorize ifAnyGranted="<%=readPermission%>">
            <ec:column property="retailerContactId" title="View Details" filterable="false" sortable="false"
                       viewsAllowed="html">
                <input type="button" class="button" value="Details"
                       onclick="javascript:window.location.href='${pageContext.request.contextPath}/allpayRetailerAccountDetails.html?retailerContactId=${retailerContactListViewModel.retailerContactId}&appUserId=${retailerContactListViewModel.appUserId}';"/>
            </ec:column>
        </authz:authorize>

        <authz:authorize ifAnyGranted="<%=printAgentFormPermission%>">
            <ec:column property="retailerContactId" title="Print Form" filterable="false" sortable="false"
                       viewsAllowed="html">
                <input type="button" class="button" value="Print"
                       onclick="openPrinterWindow(${retailerContactListViewModel.retailerContactId})"/>
            </ec:column>
        </authz:authorize>

        <!-- authz:authorize ifAnyGranted="PortalConstants.PG_GP_UPDATE%>">
        c:if test="${not retailerContactListViewModel.head}">
        ec:column property="retailerContactId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">

        tags:activatedeactivate
        id="${retailerContactListViewModel.appUserId}"
        model="com.inov8.microbank.common.model.AppUserModel"
        property="accountEnabled"
        propertyValue="${retailerContactListViewModel.accountEnabled}"
        callback="actdeact"
        error="defaultError"
        />

        ec:column>
        c:if>
        authz:authorize>
        -->
    </ec:row>
</ec:table>
<script language="javascript" type="text/javascript">
    function confirmUpdateStatus(link)
    {
        if (confirm('Are you sure you want to update status?')==true)
        {
            window.location.href=link;
        }
    }

    function openPrinterWindow(retailerContactId){
        var location ='${pageContext.request.contextPath}/allpayRetailerAccountForm_printer.html?retailerContactId='+ retailerContactId;
        window.open(location,'printWindow');
    }
</script>
</body>
</html>
