<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<html>
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Agent Level " />
	</head>
	<body bgcolor="#ffffff">
<div align="right"><a href="distributorlevelform.html" class="linktext"> Add Agent Level </a><br>&nbsp;&nbsp;</div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false"/><br />
				</c:forEach>
			</div>
		<c:remove var="messages" scope="session"/>
		</c:if>

		<ec:table
		items="distributorLevelList"
		var = "distributorModel"
		retrieveRowsCallback="limit"
		filterRowsCallback="limit"
		sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/distributorlevelmanagement.html"
		title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent Level.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent Level.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Agent Level" fileName="Agent Level.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent Level.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="distributorLevelName" title="Agent Level"/>
				<ec:column property="name" title="Agent"/>
				<ec:column property="managingLevelName" title="Managing Level"/>
				<ec:column property="ultimateLevelName" title="Ultimate Level"/>
				<ec:column  property="distributorLevelId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false" >
					<a href="${pageContext.request.contextPath}/distributorlevelform.html?distributorLevelId=${distributorModel.distributorLevelId}">Edit</a>
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
