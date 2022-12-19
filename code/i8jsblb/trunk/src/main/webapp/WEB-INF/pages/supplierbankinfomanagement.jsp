<!--Author: Jalil-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="Supplier Bank"/>
      <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>

   </head>
   <body bgcolor="#ffffff">
      <div align="right">
        <a href="supplierbankinfoform.html" class="linktext">
          Add Supplier Bank Info </a><br>&nbsp;&nbsp;
      </div>
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
      <ec:table
      items="supplierBankInfoModelList"
      var = "supplierBankInfoModel"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/supplierbankinfomanagement.html"
      title="">
         	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="Supplier Bank.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="Supplier Bank.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="Supplier Bank" fileName="Supplier Bank.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="Supplier Bank.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         <ec:row>
           <ec:column property="name" title="Supplier Bank"/>
           <ec:column property="supplierName" title="Supplier"/>
           <ec:column property="bankName" title="Bank"/>
           <ec:column property="paymentModeName" title="Payment Mode"/>
            <ec:column property="accountNo" title="Account No"/>
           


            <ec:column  property="supplierBankInfoId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false">
             <a href ="${pageContext.request.contextPath}/supplierbankinfoform.html?supplierBankInfoId=${supplierBankInfoModel.supplierBankInfoId}">
             Edit
             </a>
           </ec:column>
           
           
           <ec:column  property="supplierBankInfoId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
  			  <tags:activatedeactivate id="${supplierBankInfoModel.supplierBankInfoId}"
						model="com.inov8.microbank.common.model.SupplierBankInfoModel"
						property="active" 
						propertyValue="${supplierBankInfoModel.active}"
						callback="actdeact" error="defaultError" />
    
 			 </ec:column>  
           
         </ec:row>
      </ec:table>

 
   </body>
</html>



