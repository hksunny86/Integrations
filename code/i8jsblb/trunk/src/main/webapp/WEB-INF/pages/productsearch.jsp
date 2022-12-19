

<%@include file="/common/taglibs.jsp"%>
<%@ page import='com.inov8.microbank.common.util.*,com.inov8.microbank.common.util.PortalConstants'%>
<c:set var="retriveAction"><%=PortalConstants.ACTION_RETRIEVE %></c:set>

<html>
<head>
<meta name="decorator" content="decorator">
<link type="text/css" rel="stylesheet" href="styles/ajaxtags.css" />
<link rel="stylesheet" href="${contextPath}/styles/extremecomponents.css" type="text/css">
<script type="text/javascript" src="${contextPath}/scripts/jquery-1.11.0.js"></script>

<%@include file="/common/ajax.jsp"%>
<meta name="title" content="Product " />
<%
		String productUpdatePermissions = PortalConstants.PG_GP_UPDATE + "," + PortalConstants.MNG_PRDCT_LMTS_UPDATE;
%>
	<script type="text/javascript">
	var jq=$.noConflict();
	var serverDate ="<%=PortalDateUtils.getServerDate()%>";
	
		function error(request)
		{
			alert("An unknown error has occured. Please contact with the administrator for more details");
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
			<html:form name='productSearchForm'
				commandName="productListViewModel" method="post"
				action="productsearch.html" onsubmit="return validateForm(this)">
				
<table width="750px" border="0">
				<tr>
					<td class="formText" align="right">
						Supplier:
					</td>
					<td align="left">
						<html:select path="supplierId" cssClass="textBox" tabindex="5" >
							<html:option value="">---All---</html:option>
							<c:if test="${supplierModelList != null}">
								<html:options items="${supplierModelList}" itemValue="supplierId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>
					<td class="formText" align="right">
						Product:
					</td>
					<td align="left">
						<html:select path="productId" cssClass="textBox" tabindex="6" >
							<html:option value="">---All---</html:option>
							<c:if test="${productModelList != null}">
								<html:options items="${productModelList}" itemValue="productId" itemLabel="name"/>
							</c:if>
						</html:select>
					</td>				
				</tr>
				
				<tr>
					<td class="formText" align="right">

					</td>
					<td align="left">
						<input name="_search" type="submit" class="button" value="Search" tabindex="11"/> 
						<input name="reset" type="reset" 
							onclick="javascript: window.location='productsearch.html?actionId=${retriveAction}'"
							class="button" value="Cancel" tabindex="12" />
					</td>
					<td class="formText" align="right">

					</td>
					<td align="left">
					</td>
				</tr>
				<input type="hidden" name="<%=PortalConstants.KEY_ACTION_ID%>" value="<%=PortalConstants.ACTION_RETRIEVE%>">
				</table>
				</html:form>
	
		
		<ec:table filterable="false" items="productListViewModelList" var="productModel" retrieveRowsCallback="limit" filterRowsCallback="limit"
		sortRowsCallback="limit" action="${contextPath}/productsearch.html" title="">

			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="List Product Management.xls" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="List Product Management.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="List Product Management" fileName="List Product Management.pdf" tooltip="Export PDF" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="List Product Management.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
		
		<ec:row>
			<ec:column property="productName" title="Product" />
			<ec:column property="supplierName" title="Supplier"/>
			<ec:column property="serviceName" title="Service" />
			<ec:column property="minLimit" title="Min. Limit" />
			<ec:column property="maxLimit" title="Max. Limit"/>
			<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_LMT_READ%>">
				<ec:column  property="productId" alias="Define Product Limit Rules" title="Product Limit Rules" viewsAllowed="html" sortable="false" style="text-align:center;">
					<input type="button" class="button" style="width='90px'"
						   value="Define Product Limit Rules"
						   onclick="javascript:window.location.href='${contextPath}/productlimitsruleform.html?productId=${productModel.productId}&productName=${productModel.productName}';" />
				</ec:column>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_CHARGES_READ%>">
				<ec:column  property="productId" alias="Define Product Charges Rules" title="Product Charges Rules" viewsAllowed="html" sortable="false" style="text-align:center;">
					<input type="button" class="button" style="width='90px'"
						   value="Define Product Charges Rules"
						   onclick="javascript:window.location.href='${contextPath}/p_productchargesruleform.html?productId=${productModel.productId}&productName=${productModel.productName}';" />
				</ec:column>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.MNG_PRDCT_SHARES_READ%>">
				<ec:column  property="productId" alias="Define Product Shares Rules" title="Product Shares Rules" viewsAllowed="html" sortable="false" style="text-align:center;">
					<input type="button" class="button" style="width='90px'"
						   value="Define Product Shares Rules"
						   onclick="javascript:window.location.href='${contextPath}/p_productcustomizeshare.html?productId=${productModel.productId}&productName=${productModel.productName}';" />
				</ec:column>
			</authz:authorize>

			<authz:authorize ifAnyGranted="<%=productUpdatePermissions%>">
            <%--<c:choose>
					    <c:when test="${productModel.limitApplicable}">--%>
							 <ec:column  property="productId" alias="edit" title="Edit" viewsAllowed="html" sortable="false" style="text-align:center;">
              					<input type="button" class="button" style="width='90px'"
								value="Edit"
								onclick="javascript:window.location.href='${contextPath}/productupdateform.html?productId=${productModel.productId}';" />
            				 </ec:column>
					  <%-- </c:when>
					   <c:otherwise>
							  <ec:column property="productId" alias="edit" title="Edit" viewsDenied="xls" filterable="false" sortable="false" style="text-align:center;">
              					<input type="button" class="button" style="width='90px'" value="Edit" disabled="disabled" />           				 
            				  </ec:column>	
					   </c:otherwise>
			</c:choose>--%>
    
			</authz:authorize>
             <authz:authorize ifNotGranted="<%=productUpdatePermissions%>">
             <ec:column property="productId" alias="edit" title="Edit" viewsAllowed="html" sortable="false" style="text-align:center;">
              <input type="button" class="button" style="width='90px'" value="Edit" disabled="disabled" />
            </ec:column>
			</authz:authorize>
            
			
                </ec:row>
	</ec:table>
<ajax:select source="supplierId" target="productId"
		baseUrl="${pageContext.request.contextPath}/p_refData.html"
		parameters="supplierId={supplierId},productStatus=all,rType=1,actionId=${retriveAction}" errorFunction="error"/>
<script language="javascript" type="text/javascript">
function confirmUpdateStatus(link)
{
    if (confirm('Are you sure you want to update status?')==true)
    {
      window.location.href=link;
    }
}

</script>
<script type="text/javascript" src="${contextPath}/scripts/searchFormValidator.js"></script>
</body>
</html>

