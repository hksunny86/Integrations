<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jawwad Farooq-->
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Verifly " />
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
	            <c:out value="${msg}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
   <div align="right"><a href="veriflyform.html" class="linktext"> Add Verifly </a><br>&nbsp;&nbsp;</div>
<ec:table
items="veriflyModelList"
var = "veriflyModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/veriflymanagement.html"
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Verifly.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Verifly.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Verifly" fileName="Verifly.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Verifly.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>
  <ec:column property="name" title="Verifly"/>  
  <ec:column property="url" title="URL"/>
  <ec:column property="description"/>
      
    <ec:column property="veriflyId" alias="edit" title="Edit" filterable="false" sortable="false" viewsDenied="xls" style="text-align:center;">
      <a href="${pageContext.request.contextPath}/veriflyform.html?veriflyId=${veriflyModel.veriflyId}">Edit</a>
    </ec:column>  
      
    <ec:column  property="active" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
   <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/veriflymanagement.html?&_setActivate=${!veriflyModel.active}&veriflyId=${veriflyModel.veriflyId}')">
    <c:if test="${veriflyModel.active}">Deactivate</c:if>
    <c:if test="${!veriflyModel.active}">Activate</c:if>
    </a> --%>
    <tags:activatedeactivate 
			id="${veriflyModel.veriflyId}" 
			model="com.inov8.microbank.common.model.VeriflyModel"
			property="active"
			propertyValue="${veriflyModel.active}"
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
