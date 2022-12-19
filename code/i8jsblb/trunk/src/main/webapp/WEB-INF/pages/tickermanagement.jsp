
<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Jalil-Ur-Rehman-->
<%@include file="/common/taglibs.jsp"%>
<html>
	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Ticker String" />
	</head>
	<body bgcolor="#ffffff">
		<div align="right">
			<a href="tickerform.html" class="linktext">Add Ticker</a><br>
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
		<ec:table items="tickerModelList" var="tickerListViewModel"
			retrieveRowsCallback="limit" filterRowsCallback="limit"
			sortRowsCallback="limit"
			action="${pageContext.request.contextPath}/tickermanagement.html"
			title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Ticker String.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Ticker String.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Ticker String" fileName="Ticker String.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Ticker String.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>

				<ec:column property="mfsId" title="Inov8 MWallet ID" />

				<ec:column property="tickerString" title="Ticker String" />


				<ec:column property="tickerId" title="Edit" filterable="false"
					sortable="false" viewsDenied="xls">
					<a
						href="${pageContext.request.contextPath}/tickerform.html?tickerId=${tickerListViewModel.tickerId}">Edit</a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
