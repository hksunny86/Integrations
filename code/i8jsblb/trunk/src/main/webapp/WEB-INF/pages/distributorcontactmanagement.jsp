<!--Author: Jawwad Farooq-->

<%@include file="/common/taglibs.jsp"%>
    
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Agent Contact " />
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
<div align="right"><a href="distributorcontactform.html" class="linktext">Add Agent Contact</a>&nbsp;&nbsp;</div>
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
items="distributorContactList"
var = "distributorContactListViewModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/distributorcontactmanagement.html"
title=""
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent Contact.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent Contact.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Agent Contact" fileName="Agent Contact.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent Contact.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>
	<ec:column property="username" filterable="true" title="Agent Contact"/>
	<ec:column property="firstName" filterable="true"/>
    <ec:column property="lastName" filterable="true"/>
    <ec:column property="mobileNo" filterable="true" escapeAutoFormat="true"/>
    <ec:column property="partnerGroupName" title="Partner Group Name" />
    <ec:column property="email" filterable="true"/>
    <ec:column property="balance" filterable="true" cell="currency"  format="0.00" style="text-align: right;"/>
    <ec:column property="distributorName" filterable="true" alias="Agent"/>
	<ec:column property="managingContactName" filterable="true" title="Managing Contact"/>
	<ec:column property="distributorLevelName" filterable="true" title="Agent Level"/>
	
	<ec:column property="distributorContactId" viewsDenied="xls" escapeAutoFormat="true" title="Edit" filterable="false" sortable="false" >
		<a href="${pageContext.request.contextPath}/distributorcontactform.html?distributorContactId=${distributorContactListViewModel.distributorContactId}&appUserId=${distributorContactListViewModel.appUserId}">Edit</a>
	</ec:column>
	<ec:column property="distributorContactId" viewsDenied="xls" title="Deactivate" filterable="false" sortable="false" >
 		<%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/distributorcontactmanagement.html?&_setActivate=${!distributorContactListViewModel.accountEnabled}&distributorContactId=${distributorContactListViewModel.distributorContactId}')">
 			<c:if test="${distributorContactListViewModel.accountEnabled}">Deactivate</c:if>
 			<c:if test="${!distributorContactListViewModel.accountEnabled}">Activate</c:if>
 		</a>--%>
 		<tags:activatedeactivate 
			id="${distributorContactListViewModel.appUserId}" 
			model="com.inov8.microbank.common.model.AppUserModel" 
			property="accountEnabled"
			propertyValue="${distributorContactListViewModel.accountEnabled}"
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


