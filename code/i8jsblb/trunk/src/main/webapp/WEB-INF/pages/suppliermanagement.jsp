<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Asad Hayat-->
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<meta name="decorator" content="decorator">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<meta name="title" content="Supplier " />
<script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>
</head>
<body bgcolor="#ffffff">
<div align="right"><a href="supplierform.html" class="linktext">Add Supplier</a>&nbsp;&nbsp;</div>
<div id="successMsg" class ="infoMsg" style="display:none;"></div>

<c:if test="${not empty messages}">
    <div class="infoMsg" id="successMessages">
        <c:forEach var="msg" items="${messages}">
            <c:out value="${msg}" escapeXml="false"/><br/>
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

	<ec:table
		items="supplierModelList"
                var = "supplierModel"
                retrieveRowsCallback="limit"
                filterRowsCallback="limit"
                sortRowsCallback="limit"
		action="${pageContext.request.contextPath}/suppliermanagement.html"
		>
            <authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Supplier.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Supplier.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Supplier" fileName="Supplier.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Supplier.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		<ec:row>
			<ec:column property="name" title="Supplier"/>
			<ec:column property="contactName"/>
			<ec:column property="phoneNo" title="Phone No"/>
			<ec:column property="email"/>
            <ec:column  property="supplierId" title="Edit" filterable="false" sortable="false" viewsDenied="xls">
                          <a href="${pageContext.request.contextPath}/supplierform.html?supplierId=${supplierModel.supplierId}">Edit</a>
                        </ec:column>
			<ec:column  property="supplierId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
                       <%--    <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/suppliermanagement.html?&_setActivate=${!supplierModel.active}&supplierId=${supplierModel.supplierId}');"><c:if test="${supplierModel.active}">Deactivate</c:if><c:if test="${!supplierModel.active}">Activate</c:if></a> --%>
                       
           <tags:activatedeactivate 
			id="${supplierModel.supplierId}" 
			model="com.inov8.microbank.common.model.SupplierModel" 
			property="active"
			propertyValue="${supplierModel.active}"
			callback="actdeact"
			error="defaultError"
			/>
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

