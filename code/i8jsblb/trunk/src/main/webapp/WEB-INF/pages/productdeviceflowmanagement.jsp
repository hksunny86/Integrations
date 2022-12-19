<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Channel Product Matrix" />
</head>
<body bgcolor="#ffffff">
<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<div align="right"><a href="productdeviceflowform.html" class="linktext">Add Product Device Flow</a><br>&nbsp;&nbsp;</div>
	<ec:table 
		items="productDeviceFlowListViewModelList" 
                var = "productDeviceFlowModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 


		action="${pageContext.request.contextPath}/productdeviceflowmanagement.html"
		title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Channel Product Matrix.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Channel Product Matrix.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Channel Product Matrix" fileName="Channel Product Matrix.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Channel Product Matrix.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="productName" title="Product" filterable="true"/>
			<ec:column property="deviceTypeName" title="Device Type" filterable="true"/>
			<ec:column property="deviceFlowName" title="MWallet Device Flow" filterable="true"/>
			
            <ec:column  property="productDeviceFlowId" title="Edit" viewsDenied="xls" filterable="false" sortable="false" style="text-align:center;">
              <a href="${pageContext.request.contextPath}/productdeviceflowform.html?productdeviceflowid=${productDeviceFlowModel.productDeviceFlowId}">Edit</a>
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

