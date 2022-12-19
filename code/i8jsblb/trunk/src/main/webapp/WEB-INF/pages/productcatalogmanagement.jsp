<!--Title: i8Microbank-->
<!--Description: i8Microbank Application; catalog management-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.PortalConstants'%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Product Catalog"/>

<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.7.2.min.js"></script>
	<script type="text/javascript">
		var jq=$.noConflict();
		function actdeact(request) {
			isOperationSuccessful(request);
		}
	</script>
	<%
		String createPermission = PortalConstants.ADMIN_GP_CREATE;
		createPermission +=	"," + PortalConstants.PG_GP_CREATE;
		createPermission +=	"," + PortalConstants.MNG_PRDCT_CAT_CREATE;
		
		String readPermission = PortalConstants.ADMIN_GP_READ;
		readPermission +=	"," + PortalConstants.PG_GP_READ;
		readPermission +=	"," + PortalConstants.MNG_PRDCT_CAT_READ;
		
		String updatePermission = PortalConstants.ADMIN_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.PG_GP_UPDATE;
		updatePermission +=	"," + PortalConstants.MNG_PRDCT_CAT_UPDATE;
	 %>
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

<authz:authorize ifAnyGranted="<%=createPermission %>">
	<div align="right"><a href="productcatalogform.html" class="linktext">Add Product Catalog</a><br>&nbsp;&nbsp;</div>
</authz:authorize>

<ec:table items="productCatalogModelList" var="productCatalogModel" retrieveRowsCallback="limit" filterRowsCallback="limit" sortRowsCallback="limit" action="${pageContext.request.contextPath}/productcatalogmanagement.html">
  			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				  <ec:exportXls fileName="Product Catalog.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Product Catalog.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Product Catalog" fileName="Product Catalog.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Product Catalog.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
  <ec:row>
	  <ec:column property="name" title="Product Catalog" width="30%">    </ec:column>
	  <ec:column property="description" width="30%"/>
	  <ec:column property="userType" title="User Type"  width="25%"></ec:column>

    <authz:authorize ifAnyGranted="<%=updatePermission%>">
	    <ec:column property="productCatalogId" alias="edit" title="Edit" filterable="false" sortable="false" viewsDenied="xls" width="4%" style="text-align:center;">
	      <a href="${pageContext.request.contextPath}/productcatalogform.html?productCatalogId=${productCatalogModel.productCatalogId}">Edit</a>
	    </ec:column>
	
	    <ec:column property="productCatalogId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls" width="11%">
	      	<tags:activatedeactivate id="${productCatalogModel.productCatalogId}"
							model="com.inov8.microbank.common.model.ProductCatalogModel"
							property="active" propertyValue="${productCatalogModel.active}"
							callback="actdeact" error="defaultError" />
	    </ec:column>
    </authz:authorize>
  </ec:row>
</ec:table>
<script language="javascript" type="text/javascript">
function confirmUpdateStatus(link) {
    if (confirm('Are you sure you want to update status?')) {
      window.location.href=link;
    }
}
</script>
</body>
</html>
