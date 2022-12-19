<!--Author: Rizwan ur Rehman-->

<%@include file="/common/taglibs.jsp"%>

<html>
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Application Version " />
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
			<a href="applicationversionform.html" class="linktext">
				Add Application Version
			</a><br>&nbsp;&nbsp;
		</div>
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
		items="appVersionList"
		var = "appVersionModel"
		retrieveRowsCallback="limit"
		filterRowsCallback="limit"
		sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/applicationversionmanagement.html"
		title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Application Version.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Application Version.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Application Version" fileName="Application Version.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Application Version.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="appVersionNumber" title="Application Version" />
				<ec:column property="releaseDate" title="Release Date" cell="date" format="dd/MM/yyyy"/>
				<ec:column property="toCompatibleVersion" title="To Compatible Version" />
				<ec:column property="fromCompatibleVersion" title="From Compatible Version" />
				<ec:column property="blackListed" title="Black Listed" filterable="false" sortable="false">
					<c:if test="${appVersionModel.blackListed}">Yes</c:if>
					<c:if test="${!appVersionModel.blackListed}">No</c:if>
				</ec:column>
				<ec:column property= "deviceTypeIdDeviceTypeModel.name" sortable="false" filterable="false" title = "Device Type"/>
				<ec:column  property="appVersionId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false" >
					<a href="${pageContext.request.contextPath}/applicationversionform.html?appVersionId=${appVersionModel.appVersionId}">
						Edit
					</a>
				</ec:column>
				<ec:column  property="appVersionId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
	            	<%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/applicationversionmanagement.html?&_setActivate=${!appVersionModel.active}&appVersionId=${appVersionModel.appVersionId}');"><c:if test="${appVersionModel.active}">Deactivate</c:if><c:if test="${!appVersionModel.active}">Activate</c:if></a> --%>
           		<tags:activatedeactivate 
			id="${appVersionModel.appVersionId}" 
			model="com.inov8.microbank.common.model.AppVersionModel" 
			property="active"
			propertyValue="${appVersionModel.active}"
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
