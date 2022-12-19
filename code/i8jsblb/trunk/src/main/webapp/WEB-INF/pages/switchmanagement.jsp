<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Switch " />
	<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
	<div id="successMsg" class ="infoMsg" style="display:none;"></div>
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
<div align="right"><a href="switchform.html" class="linktext">Add Switch</a><br>&nbsp;&nbsp;</div>
<ec:table
items="switchModelList"
var = "switchModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/switchmanagement.html"
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Switch.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Switch.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Switch" fileName="Switch.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Switch.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>
  <ec:column property="name" title="Switch"/>
  <ec:column property="className"/>
  <ec:column property="url" title="URL"/>
  <ec:column property="description"/>
  <ec:column property="switchId" alias="edit" title="Edit" filterable="false" sortable="false" viewsDenied="xls" style="text-align:center;">
      <a href="${pageContext.request.contextPath}/switchform.html?switchId=${switchModel.switchId}">Edit</a>
  </ec:column>
    <ec:column property="active" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
    <tags:activatedeactivate 
			id="${switchModel.switchId}" 
			model="com.inov8.microbank.common.model.SwitchModel" 
			property="active"
			propertyValue="${switchModel.active}"
			callback="actdeact"
			error="defaultError"
			/>
    
    <!-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/switchmanagement.html?&_setActivate=${!switchModel.active}&switchId=${switchModel.switchId}')">
    	 <c:if test="${switchModel.active}">Deactivate</c:if>
    	 <c:if test="${!switchModel.active}">Activate</c:if>
    	 </a> -->
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
