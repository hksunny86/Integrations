<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Area " />
	<%
		String createPermission = PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.RET_GP_CREATE;
		createPermission +=	"," + PortalConstants.ADMIN_GP_CREATE;

		String updatePermission = PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.RET_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.ADMIN_GP_UPDATE;
	 %>
</head>
<body bgcolor="#ffffff">
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br />
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
<authz:authorize ifAnyGranted="<%=createPermission%>">
<div align="right"><a href="areaform.html" class="linktext"> Add Area </a><br>&nbsp;&nbsp;</div>
</authz:authorize>

<ec:table
items="areaModelList"
var = "areaModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/areamanagement.html"
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Area.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Area.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Area" fileName="Area.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Area.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>
  <ec:column property="name" title="Area"/>
  <ec:column property="parentAreaName" title="Parent Area"/>
    <ec:column property="ultimateParentAreaName" title="Ultimate Area"/>

<authz:authorize ifAnyGranted="<%=updatePermission%>">
  <ec:column  property="areaId" title="Edit" filterable="false" sortable="false" width="6%" viewsDenied="xls" style="text-align:center;">
    <a href="${pageContext.request.contextPath}/areaform.html?areaId=${areaModel.areaId}">Edit</a>
  </ec:column>
</authz:authorize>
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
