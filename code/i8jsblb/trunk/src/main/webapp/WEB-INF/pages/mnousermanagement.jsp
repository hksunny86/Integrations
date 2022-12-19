
<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Service Operator User"/>
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
<div align="right">
  <a href="mnouserform.html" class="linktext">Add Service Operator User</a>
  &nbsp;&nbsp;
</div>
<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
  <div class="infoMsg" id="successMessages">
    <c:forEach var="msg" items="${messages}">
      <c:out value="${msg}" escapeXml="false"/>
      <br/>
    </c:forEach>
  </div>
  <c:remove var="messages" scope="session"/>
</c:if>
<table width="85%">
</table>
<ec:table items="mnoUserModelList" var="mnoUserModel" 
	retrieveRowsCallback="limit" filterRowsCallback="limit" 
	sortRowsCallback="limit" 
	action="${pageContext.request.contextPath}/mnousermanagement.html" title="">
  		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Service Operator User.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Service Operator User.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Service Operator User" fileName="Service Operator User.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Service Operator User.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
  <ec:row>
    <ec:column property="username" filterable="true" title="User Name"/>
    <ec:column property="firstName" title="First Name"/>
    <ec:column property="lastName" filterable="true" title="Last Name"/>
    <ec:column property="mobileNo" filterable="true" title="Mobile No" escapeAutoFormat="true"/>
    <ec:column property="partnerGroupName" title="Partner Group Name" />
    <ec:column property="email" filterable="true"/>
    <ec:column property="name" filterable="true" title="Service Operator"/>
      
    <ec:column property="mnoUserId" title="Edit" filterable="false" sortable="false" viewsDenied="xls">
      <a href="${pageContext.request.contextPath}/mnouserform.html?mnoUserId=${mnoUserModel.mnoUserId}&appUserId=${mnoUserModel.appUserId}">Edit</a>
    </ec:column>
   

    <ec:column  property="mnoUserId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
       <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/mnousermanagement.html?&_setActivate=${!mnoUserModel.active}&mnoUserId=${mnoUserModel.mnoUserId}&appUserId=${mnoUserModel.appUserId}')"><c:if test="${mnoUserModel.active}">Deactivate</c:if><c:if test="${!mnoUserModel.active}">Activate</c:if></a> --%>
        <tags:activatedeactivate 
			id="${mnoUserModel.appUserId}" 
			model="com.inov8.microbank.common.model.AppUserModel"
			property="accountEnabled"
			propertyValue="${mnoUserModel.active}"
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
