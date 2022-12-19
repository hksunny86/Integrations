<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Product " />

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
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
<div align="right"><a href="productform.html" class="linktext">Add Product</a><br>&nbsp;&nbsp;</div>
<table width="85%" >

</table>

	<ec:table 
		items="productModelList" 
                var = "productModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit" 


		action="${pageContext.request.contextPath}/productmanagement.html"
		title="">
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Product.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Product.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Product" fileName="Product.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Product.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="productName" title="Product" filterable="true"/>
			<ec:column property="supplierName" title="Supplier" filterable="true"/>
			<ec:column property="serviceName" title="Service" filterable="true"/>
			<ec:column property="costPrice" filterable="true" title="Cost Price" cell="currency" format="0.00"/>
			<ec:column property="unitPrice" filterable="true" title="Unit Price" cell="currency" format="0.00"/>
			
            <ec:column  property="productId" alias="edit" title="Edit" viewsDenied="xls" filterable="false" sortable="false" style="text-align:center;">
              <a href="${pageContext.request.contextPath}/productform.html?productId=${productModel.productId}">Edit</a>
            </ec:column>
			<ec:column  property="productId" title="Deactivate" viewsDenied="xls" filterable="false" sortable="false">
                        <%--  <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/productmanagement.html?&_setActivate=${!productModel.active}&productId=${productModel.productId}')">
                          <c:if test="${productModel.active}">Deactivate</c:if>
                          <c:if test="${!productModel.active}">Activate</c:if></a> --%>
					<tags:activatedeactivate id="${productModel.productId}"
						model="com.inov8.microbank.common.model.ProductModel"
						property="active" propertyValue="${productModel.active}"
						callback="actdeact" error="defaultError" />
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

