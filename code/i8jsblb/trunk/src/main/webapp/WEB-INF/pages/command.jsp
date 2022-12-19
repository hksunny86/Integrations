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
		<meta name="title" content="Command" />

	</head>

	<body bgcolor="#ffffff">

			<ec:table 
				items="commandList" 
				var="CommandListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/command.html"
				title="">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
					<ec:exportXls fileName="Command.xls" tooltip="Export Excel"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
					<ec:exportXlsx fileName="Command.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Command" fileName="Command.pdf" tooltip="Export PDF"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
					<ec:exportCsv fileName="Command.csv" tooltip="Export CSV"></ec:exportCsv>
				</authz:authorize>
				<ec:row>
    				<ec:column property="name"  title="Command"/>
    				<ec:column property="className"  title="Class Name"/>
    				<ec:column property="description"  title="Description"/>
    				<ec:column property="active" title="Status" filterable="false" sortable="false" viewsDenied="xls">
      				     	<c:if test="${CommandListViewModel.active}">Active</c:if>
        					<c:if test="${!CommandListViewModel.active}">Deactive</c:if>
    					
    				</ec:column>			
      			</ec:row>		
      		</ec:table>	


	</body>
		

</html>











































































