<!--Author: Rizwan ur Rehman-->

<%@include file="/common/taglibs.jsp"%>

<html>
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<meta name="title" content="Stakeholders" />
	</head>
	<body bgcolor="#ffffff">
		<div align="right">
			<a href="commissionstakeholderform.html" class="linktext">
				Add Stakeholder
			</a><br>&nbsp;&nbsp;
		</div>
		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false"/><br />
				</c:forEach>
			</div>
		<c:remove var="messages" scope="session"/>
		</c:if>

		<ec:table
		items="commissionStakeholderList"
		var = "commissionStakeholderModel"
		retrieveRowsCallback="limit"
		filterRowsCallback="limit"
		sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/commissionstakeholdermanagement.html"
		title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Stakeholders.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Stakeholders.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Stakeholders" fileName="Stakeholders.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Stakeholders.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="commissionStakeholderName" title="Stakeholder " />
				<ec:column property="commSthContactName" title="Contact Name" />
				<ec:column property="stakeholderTypeName" title="Stakeholder Type" />				
				<ec:column property="operatorName" title="Operator" />
				<ec:column property="bankName" title="Bank" />					
<%--				<ec:column property="retailerName" title="Retailer" />--%>
<%--				<ec:column property="distributorName" title="Agent " />--%>
				<ec:column  property="commissionStakeholderId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false" escapeAutoFormat="true">
					<a href="${pageContext.request.contextPath}/commissionstakeholderform.html?commissionStakeholderId=${commissionStakeholderModel.commissionStakeholderId}">Edit</a>
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
