<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<html>
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Agent " />
		<script type="text/javascript"
			src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
		<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
	</head>
	<body bgcolor="#ffffff">

		<div align="right">
			<a href="distributorform.html" class="linktext"> Add Agent 
			</a>&nbsp;&nbsp;
		</div>
		<div id="successMsg" class="infoMsg" style="display:none;"></div>


		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

		<ec:table items="distributorModelList" var="distributorModel"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/distributormanagement.html"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Agent.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Agent.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Agent" fileName="Agent.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Agent.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="name" title="Agent" />
				<ec:column property="contactName" />
				<ec:column property="phoneNo" escapeAutoFormat="true"/>
				<ec:column property="email" />
				<ec:column property="national" filterable="false" title="Is National" >
				<c:if test="${not distributorModel.national}">
				<c:out value="No"></c:out>
				</c:if>
				<c:if test="${distributorModel.national}">
				<c:out value="Yes"></c:out>
				</c:if>
				</ec:column>

				<ec:column property="distributorId" viewsDenied="xls" title="Edit"
					filterable="false" sortable="false">
					<%--		<a href="${pageContext.request.contextPath}/distributorform.html?distributorId=${distributorModel.distributorId}" >Edit</a>--%>
					<input type="button" class="button" style="width='90px'"
						value="Edit"
						onclick="javascript:changeInfo('${pageContext.request.contextPath}/distributorform.html?distributorId=${distributorModel.distributorId}');" />
				</ec:column>

				<ec:column property="distributorId" viewsDenied="xls"
					title="Deactivate" filterable="false" sortable="false">
					<c:if test="${not distributorModel.national}">
						<tags:activatedeactivate id="${distributorModel.distributorId}"
							model="com.inov8.microbank.common.model.DistributorModel"
							property="active" propertyValue="${distributorModel.active}"
							callback="actdeact" error="defaultError" />
					</c:if>

					<!-- 		<a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/distributormanagement.html?_setActivate=${!distributorModel.active}&versionNo=${distributorModel.versionNo}&distributorId=${distributorModel.distributorId}')"><c:if test="${distributorModel.active}">Deactivate</c:if><c:if test="${!distributorModel.active}">Activate</c:if></a> -->
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
	function changeInfo(link)
				{
				    
				    
				      window.location.href=link;
				    
				}
</script>
	</body>
</html>
