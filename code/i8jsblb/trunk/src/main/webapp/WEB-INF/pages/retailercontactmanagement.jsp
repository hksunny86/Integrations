<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<html>

<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Retailer Contact " />
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
<div align="right"><a href="retailercontactformentry.html" class="linktext">Add Retailer Contact</a>&nbsp;&nbsp;</div>
<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<table width="85%" >
</table>
<ec:table
		items="retailerContactModelList"
                var = "retailerContactListViewModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
				action="${pageContext.request.contextPath}/retailercontactmanagement.html">
	            <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
					<ec:exportXls fileName="Retailer Contact.xls" tooltip="Export Excel"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
					<ec:exportXlsx fileName="Retailer Contact.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Retailer Contact" fileName="Retailer Contact.pdf" tooltip="Export PDF"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
					<ec:exportCsv fileName="Retailer Contact.csv" tooltip="Export CSV"></ec:exportCsv>
				</authz:authorize>
		<ec:row>
                  <ec:column property="username" filterable="true" title="Retailer Contact"/>
                  <ec:column property="firstName" filterable="true"/>
                  <ec:column property="lastName" filterable="true"/>
                  <ec:column property="mobileNo" filterable="true" escapeAutoFormat="true"/>
                  <ec:column property="partnerGroupName" title="Partner Group Name" />
                  <ec:column property="email" filterable="true"/>
                  <ec:column property="balance" filterable="true" cell="currency"  format="0.00" style="text-align: right;"/>
                  <ec:column property="retailerName" filterable="true" title="Retailer"/>
                  <ec:column property="areaName" filterable="true" title="Area"/>
                  <ec:column  property="retailerContactId" title="Edit" filterable="false" sortable="false" width="30%" viewsDenied="xls">
                          <a href="${pageContext.request.contextPath}/retailercontactformentry.html?retailerContactId=${retailerContactListViewModel.retailerContactId}">Edit</a>
                  </ec:column>
                  <ec:column  property="retailerContactId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
                         <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/retailercontactmanagement.html?_setActivate=${!retailerContactListViewModel.accountEnabled}&retailerContactId=${retailerContactListViewModel.retailerContactId}')"><c:if test="${retailerContactListViewModel.accountEnabled}">Deactivate</c:if><c:if test="${!retailerContactListViewModel.accountEnabled}">Activate</c:if></a> --%>
                         
              <tags:activatedeactivate 
			id="${retailerContactListViewModel.appUserId}" 
			model="com.inov8.microbank.common.model.AppUserModel" 
			property="accountEnabled"
			propertyValue="${retailerContactListViewModel.accountEnabled}"
			callback="actdeact"
			error="defaultError"
			/>
                  </ec:column>
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
</script>
</body>
</html>
