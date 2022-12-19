<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Service " />
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
<div align="right"><a href="serviceform.html" class="linktext">Add Service</a><br>&nbsp;&nbsp;</div>
<div id="successMsg" class ="infoMsg" style="display:none;"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<ec:table
items="serviceModelList"
var = "serviceModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/servicemanagement.html"
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Service.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Service.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Service" fileName="Service.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Service.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>
  <ec:column property="name" title="Service"/>
  <ec:column property="serviceTypeName" title="Service Type"/>
  <ec:column property="description"/>
  <ec:column  property="areaId" title="Edit" filterable="false" sortable="false" viewsDenied="xls">
    <a href="${pageContext.request.contextPath}/serviceform.html?serviceId=${serviceModel.serviceId}">Edit</a>
  </ec:column>
  <ec:column  property="areaId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls" width="10">
   <%--  <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/servicemanagement.html?&_setActivate=${!serviceModel.active}&serviceId=${serviceModel.serviceId}')">
    <c:if test="${serviceModel.active}">Deactivate</c:if>
    <c:if test="${!serviceModel.active}">Activate</c:if>
    </a> --%>
    
    <tags:activatedeactivate 
			id="${serviceModel.serviceId}" 
			model="com.inov8.microbank.common.model.ServiceModel" 
			property="active"
			propertyValue="${serviceModel.active}"
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
