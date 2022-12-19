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
		<meta name="title" content="Service Type" />

	</head>

	<body bgcolor="#ffffff">
			<div align="right"><a href="servicetypeform.html" class="linktext">Add Service Type</a><br>&nbsp;&nbsp;</div>
			
			
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
				items="serviceTypeList" 
				var="serviceTypeListViewModel" 
				retrieveRowsCallback="limit" 
				filterRowsCallback="limit" 
				sortRowsCallback="limit" 
				action="${pageContext.request.contextPath}/servicetype.html"
				title="">
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
					<ec:exportXls fileName="Service Type.xls" tooltip="Export Excel"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
					<ec:exportXlsx fileName="Service Type.xlsx" tooltip="Export Excel" />
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
					<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Service Type" fileName="Service Type.pdf" tooltip="Export PDF"/>
				</authz:authorize>
				<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
					<ec:exportCsv fileName="Service Type.csv" tooltip="Export CSV"></ec:exportCsv>
				</authz:authorize>
				<ec:row>
    				<ec:column property="name" filterable="true" title="Service Type"/>
    				
    				<ec:column property="description" filterable="true" title="Description"  />
    				<ec:column alias=" " filterable="false"  title="Edit" width="6%" viewsDenied="xls" >						
						<a href="${pageContext.request.contextPath}/servicetypeform.html?serviceTypeId=${serviceTypeListViewModel.serviceTypeId}">Edit</a>						
					</ec:column>
      			
      			</ec:row>		
      		</ec:table>	


	</body>
		

</html>











































































