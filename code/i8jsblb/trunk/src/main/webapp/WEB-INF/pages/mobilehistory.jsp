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
		<meta name="title" content="Mobile History" />

	</head>

	<body bgcolor="#ffffff">
			
			
			
			<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if>
			
			
			
			<ec:table 
				items="mobileHistoryList" 
				var="mobileHistoryListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/mobilehistory.html"
				title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Mobile History.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Mobile History.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Mobile History" fileName="Mobile History.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Mobile History.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
				<ec:row>
    				<ec:column property="mfsId" filterable="true" title="Inov8 MWallet ID"/>
    				<ec:column property="firstName" filterable="true" title="First Name"/>
    				<ec:column property="lastName" filterable="true" title="Last Name"/>
    				<ec:column property="mobileNo"  title="Mobile No"/>
					<ec:column property="fromDate"  title="From Date" cell="date" format="dd/MM/yyyy"/>
				<ec:column property="toDate"  filterable="true" title="To Date" cell="date" format="dd/MM/yyyy"/>

    				
      			</ec:row>		
      		</ec:table>	


	</body>
		

</html>











































































