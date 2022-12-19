<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->

<%@include file="/common/taglibs.jsp"%>
<html>

	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Retailer Type" />
	</head>

	<body bgcolor="#ffffff">

		<div align="right">
			<a href="retailertypeform.html" class="linktext">Add Retailer
				Type</a>
			<br>
			&nbsp;&nbsp;
		</div>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>

		<ec:table items="retailerTypeList" var="serviceTypeListViewModel"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/retailertype.html"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Retailer Type.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Retailer Type.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Retailer Type" fileName="Retailer Type.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Retailer Type.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="name" filterable="true" title="Retailer Type" />

				<ec:column property="description" filterable="true"
					title="Description" />
				<ec:column alias=" " filterable="false" title="Edit"
					viewsDenied="xls">

					<a
						href="${pageContext.request.contextPath}/retailertypeform.html?retailerTypeId=${serviceTypeListViewModel.retailerTypeId}">Edit</a>
				</ec:column>

			</ec:row>
		</ec:table>
	</body>
</html>


