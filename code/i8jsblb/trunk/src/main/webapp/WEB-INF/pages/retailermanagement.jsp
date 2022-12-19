]
<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Ahmad Iqbal-->
<!-- Modified By: Fahad Tariq -->

<%@include file="/common/taglibs.jsp"%>
<%@page import="com.inov8.microbank.common.util.PortalConstants"%>
<html>
	<head>
<meta name="decorator" content="decorator">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
	<meta name="title" content="Franchise/Branch"/>
	<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
		<script type="text/javascript">
			function actdeact(request)
			{
				isOperationSuccessful(request);
			}
		</script>
	</head>
	<body bgcolor="#ffffff">
		<div id="successMsg" class ="infoMsg" style="display:none;"></div>
		<c:if test="${not empty messages}">
		  <div class="infoMsg" id="successMessages">
		    <c:forEach var="msg" items="${messages}">
		      <c:out value="${msg}" escapeXml="false"/>
		      <br/>
		    </c:forEach>
		  </div>
		  <c:remove var="messages" scope="session"/>
		</c:if>

		<ec:table items="retailerModelList" var = "retailerModel" 
			action="${pageContext.request.contextPath}/retailermanagement.html"
			retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Franchise/Branch.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Franchise/Branch.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
					headerTitle="Franchise/Branch" fileName="Franchise/Branch.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Franchise/Branch.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
			<ec:row>
				<ec:column property="distributorName" title="Agent Network"/>
				<ec:column property="regionName" title="Region"/>
			    <ec:column property="name" title="Franchise/Branch"/>
			    <ec:column property="contactName" title="Contact Name"/>
			    <ec:column property="phoneNo" title="Phone No" escapeAutoFormat="True"/>
			    <ec:column property="email" title="Email"/>
			  </ec:row>
			</ec:table>
			<script language="javascript" type="text/javascript">
				function changeInfo(link)
				{
				      window.location.href=link;
				}

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
