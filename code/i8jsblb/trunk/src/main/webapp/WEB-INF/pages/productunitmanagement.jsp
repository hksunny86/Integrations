<!--Author: Asad Hayat-->

<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Product Unit " />

<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>

</head>
<body bgcolor="#ffffff">
<%-- 
<div align="right">

  <c:choose>
    <c:when test="${isShipmentActive && isShipmentNotExpired}">
      <a href="productunitform.html?shipmentId=${param.shipmentId}&productId=${param.productId}" class="linktext">
          Add Product Unit
      </a>&nbsp;
    </c:when>
    <c:otherwise>
        <span class="disabledlinktext">
            Add Product Unit
        </span>&nbsp;
    </c:otherwise>
  </c:choose>
</div>
--%>


<div id="successMsg" class ="infoMsg" style="display:none;"></div>
<c:if test="${not empty messages}">
	<div class="infoMsg" id="successMessages">
		<c:forEach var="msg" items="${messages}">
			<c:out value="${msg}" escapeXml="false"/><br/>
		</c:forEach>
	</div>
	<c:remove var="messages" scope="session"/>
</c:if>

<table border="0" style="border-collapse: collapse">
<tr><td>
<ec:table
items="productUnitModelList"
var = "productUnitModel"
retrieveRowsCallback="limit"
filterRowsCallback="limit"
sortRowsCallback="limit"
action="${pageContext.request.contextPath}/productunitmanagement.html">
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
			<ec:exportXls fileName="Product Unit.xls" tooltip="Export Excel"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
			<ec:exportXlsx fileName="Product Unit.xlsx" tooltip="Export Excel" />
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
			<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Product Unit" fileName="Product Unit.pdf" tooltip="Export PDF"/>
		</authz:authorize>
		<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
			<ec:exportCsv fileName="Product Unit.csv" tooltip="Export CSV"></ec:exportCsv>
		</authz:authorize>
<ec:row>


	<ec:column property="userName"/>
	<ec:column property="serialNo"/>
	<ec:column property="isSold">
	   
	</ec:column>
	<ec:column property="additionalField1" filterable="false"/>
	<ec:column property="additionalField2" filterable="false"/>
        <ec:column property="productUnitId" viewsDenied="xls" title="Deactivate" filterable="false" sortable="false" >
				<tags:activatedeactivate id="${productUnitModel.productUnitId}"
						model="com.inov8.microbank.common.model.ProductUnitModel"
						url="activatedeactivateproductunit.html"
						property="active" propertyValue="${productUnitModel.active}"
						callback="actdeact" error="defaultError" />
	</ec:column>

</ec:row>
</ec:table>
</td></tr>
<tr>
<td align="center">
<div align="center">
	<input type="button" class="button" value=" Back " onclick="window.location='shipmentmanagement.html'"/>&nbsp;&nbsp;
</div>
</td></tr>
</table>

<script language="javascript" type="text/javascript">
function confirmUpdateStatus(link,sold)
{
	var flag = false;
	if(sold == 'Sold')
	{
		flag = true;
	}
    if ( flag == true){
        alert('A sold unit cannot be deactivated.');
    }
    else{
	    if (confirm('Are you sure you want to update status?')==true)
	    {
	      window.location.href=link;
	    }
    }
}
</script>
</body>
</html>

