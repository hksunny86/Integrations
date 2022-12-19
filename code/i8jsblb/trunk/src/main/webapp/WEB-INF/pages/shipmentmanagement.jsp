<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Shipment" />
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>

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
&nbsp;&nbsp;&nbsp;&nbsp;
<table width="85%" border ="0" align="center">

</table>
<div align="right" ><a href="shipmentform.html" class="linktext"> Add Shipment </a>&nbsp;&nbsp;</div>
<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<br>
<ec:table
items="shipmentModelList"
var = "shipmentModel" 
action="${pageContext.request.contextPath}/shipmentmanagement.html"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
title=""
>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Shipment.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Shipment.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Shipment" fileName="Shipment.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Shipment.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
<ec:row>

	<ec:column property="supplierName" filterable="true" alias="Supplier"/>
	<ec:column property="price" filterable="true" cell="currency"  format="0.00"/>
	<ec:column property="productName" filterable="true" alias="Product"/>
	<ec:column property="quantity" filterable="true" cell="currency"  format="0"/>
	<ec:column property="outstandingCredit" filterable="true" cell="currency"  format="0.00"/>
	<ec:column property="purchaseDate" cell="date" format="dd/MM/yyyy" filterable="true"/>
    <ec:column property="expiryDate" width="50%" cell="date" format="dd/MM/yyyy" filterable="true"/>
	<ec:column  property="shipmentId" title="View Product Unit" viewsDenied="xls"  filterable="false" sortable="false" >
		<a href="${pageContext.request.contextPath}/productunitmanagement.html?shipmentId=${shipmentModel.shipmentId}&productId=${shipmentModel.productId}">View</a>
	</ec:column>

	<ec:column  property="shipmentId" title="Edit" filterable="false"  sortable="false"  viewsDenied="xls">
       <a href="${pageContext.request.contextPath}/shipmentform.html?shipmentId=${shipmentModel.shipmentId}">Edit</a>
    </ec:column>
	<ec:column  property="shipmentId" title="Deactivate" viewsDenied="xls" filterable="false" sortable="false" >
		<%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/shipmentmanagement.html?&_setActivate=${!shipmentModel.active}&shipmentId=${shipmentModel.shipmentId}')"><c:if test="${shipmentModel.active}">Deactivate</c:if><c:if test="${!shipmentModel.active}">Activate</c:if></a> --%>
		
		<tags:activatedeactivate id="${shipmentModel.shipmentId}"
						model="com.inov8.microbank.common.model.ShipmentModel"
						property="active" propertyValue="${shipmentModel.active}"
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

