<!--Author: Rizwan-ur-Rehman -->

<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head>
<meta name="decorator" content="decorator">
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css"/>
      <meta name="title" content="AllPay Commission Rate"/>
      <script type="text/javascript" src="<c:url value='/scripts/activatedeactivate.js'/>"></script>
	<script type="text/javascript">
		function actdeact(request)
		{
			isOperationSuccessful(request);
		}
	</script>

   </head>
   <body bgcolor="#ffffff">
   <div align="right"><a href="AllpayCommissionRatesForm.html" class="linktext"> Add AllPay Commission Rate </a><br/></div>
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
      items="commissionRateList"
      var = "commissionRateList"
      retrieveRowsCallback="limit"
      filterRowsCallback="limit"
      sortRowsCallback="limit"
      action="${pageContext.request.contextPath}/AllpayCommissionRatesManagement.html"
      title="">
         	<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLS_READ%>">
				<ec:exportXls fileName="AllPay Commission Rate.xls" tooltip="Export Excel"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_XLSX_READ%>">
				<ec:exportXlsx fileName="AllPay Commission Rate.xlsx" tooltip="Export Excel" />
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_PDF_READ%>">
				<ec:exportPdf view="com.inov8.microbank.common.util.CustomPdfView" headerBackgroundColor="#b6c2da"
				headerTitle="AllPay Commission Rate" fileName="AllPay Commission Rate.pdf" tooltip="Export PDF"/>
			</authz:authorize>
			<authz:authorize ifAnyGranted="<%=PortalConstants.PERMS_EXPORT_CSV_READ%>">
				<ec:exportCsv fileName="AllPay Commission Rate.csv" tooltip="Export CSV"></ec:exportCsv>
			</authz:authorize>
         <ec:row>
           <ec:column property="productName" title="Product"/>
           <ec:column property="nationalDistributor" title="National Agent "/>
           <ec:column property="distributor" title="Distributor"/>
           <ec:column property="retailer" title="Retailer"/>
           <ec:column property="reasonName" title="Commission Reason"/>
           <ec:column property="fromDate" title="From Date" cell="date" format="dd/MM/yyyy"/>
           <ec:column property="todate" title="To Date" cell="date" format="dd/MM/yyyy"/>
           <ec:column property="nationalDistributorRate" title="National Agent Rate"/>
           <ec:column property="distributorRate" title="Agent Rate"/>
           <ec:column property="retailerRate" title="Retailer Rate"/>
           

           <ec:column  property="allpayCommissionRateId"  title="Edit" viewsDenied="xls" filterable="false" sortable="false">
             <a href ="${pageContext.request.contextPath}/AllpayCommissionRatesForm.html?allpayCommissionRateId=${commissionRateList.allpayCommissionRateId}">
             Edit
             </a>
           </ec:column>
           <ec:column  property="allpayCommissionRateId" title="Deactivate" filterable="false" sortable="false" viewsDenied="xls">
             <%-- <a href="javascript:confirmUpdateStatus('${pageContext.request.contextPath}/bankmanagement.html?&_setActivate=${!bankModel.active}&bankId=${bankModel.bankId}');"><c:if test="${bankModel.active}">Deactivate</c:if><c:if test="${!bankModel.active}">Activate</c:if></a> --%>
             <tags:activatedeactivate 
			id="${commissionRateList.allpayCommissionRateId}" 
			model="com.inov8.microbank.common.model.AllpayCommissionRateModel" 
			property="active"
			propertyValue="${commissionRateList.active}"
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



