<!--Title: i8Microbank-->
<!--Description: i8Microbank Application; action management-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Action Management"/>
</head>
<body bgcolor="#ffffff">
<div align="right">
  <a href="actionform.html" class="linktext">Add Action</a>
  &nbsp;&nbsp;
</div>
<c:if test="${not empty messages}">
  <div class="infoMsg" id="successMessages">
    <c:forEach var="msg" items="${messages}">
      <c:out value="${msg}" escapeXml="false"/>
      <br/>
    </c:forEach>
  </div>
  <c:remove var="messages" scope="session"/>
</c:if>
<ec:table items="actionModelList" var="actionModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" action="${pageContext.request.contextPath}/actionmanagement.html">
  			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Action Management.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Action Management.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Action Management" fileName="Action Management.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Action Management.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
  
  
  <ec:row>
    <ec:column property="name">    </ec:column>
    <ec:column property="description"/>
    <ec:column property="actionId" title="Edit" filterable="false" sortable="false" viewsDenied="xls">


	  <a href="${pageContext.request.contextPath}/actionform.html?actionId=${actionModel.actionId}">Edit</a>
    </ec:column>
    <ec:column property="actionId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
      <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/actionmanagement.html?&_setActivate=${!actionModel.active}&actionId=${actionModel.actionId}');">
        <c:if test="${actionModel.active}">Deactivate</c:if>
        <c:if test="${!actionModel.active}">Activate</c:if>
      </a>
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
