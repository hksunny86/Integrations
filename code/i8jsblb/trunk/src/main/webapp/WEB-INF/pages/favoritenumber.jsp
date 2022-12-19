<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Haider Ali-->

<%@include file="/common/taglibs.jsp"%>
<html>

	<head>
<meta name="decorator" content="decorator">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath}/styles/extremecomponents.css"
			type="text/css">
		<meta name="title" content="Quick Pay Numbers" />

	</head>

	<body bgcolor="#ffffff">

			<ec:table 
				items="favoriteNumberListViewModelList" 
				var="favoriteNumberListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/favoritenumber.html"
				title="">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
					<ec:exportXls fileName="Quick Pay Numbers.xls" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
					<ec:exportXlsx fileName="Quick Pay Numbers.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Quick Pay Numbers" fileName="Quick Pay Numbers.pdf" tooltip="Export PDF"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
					<ec:exportCsv fileName="Quick Pay Numbers.csv" tooltip="Export CSV"></ec:exportCsv>
				</authz:authorize>
				<ec:row>
    				<ec:column property="userId" title="Inov8 MWallet ID"/>
    				<ec:column property="mobileNo" title="Mobile No"/>
    				<ec:column property="name" title="Quick Pay Name"/>
    				<ec:column property="favoriteType" title="Quick Pay Type"/>
    				<ec:column property="favoriteNumber" title="Quick Pay Number"/>
      			</ec:row>		
      		</ec:table>	
	</body>
</html>











































































