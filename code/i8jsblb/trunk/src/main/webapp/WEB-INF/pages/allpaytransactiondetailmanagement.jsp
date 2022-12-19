<!--Author: Jawwad Farooq -->
<%@include file="/common/taglibs.jsp"%>

<html>
  <head>
    
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
<style type="text/css">
</style>

<script type="text/javascript">
    function openAuditLogDetailWindow(auditLogId)
    {
         //alert("action Log id: "+actionLogId);
         newWindow=window.open('auditlogdetail.html?auditLogId='+auditLogId,'AuditLogDetail', 'width=550,height=350,menubar=no,toolbar=no,left=150,top=150,directories=no,status=yes,scrollbars=yes,resizable=yes,status=no');
         if(window.focus) newWindow.focus();
               return false;
	}
</script>
  </head>
  
  <body bgcolor="#ffffff">



<table border="0" align="left" style="border-collapse:collapse" width="100%">
<tr><td valign="top" align="left">
<c:if test="${not empty transactionCodeListViewModelList}">

<div class="eXtremeTable">
<table id="ec_tableTransactionCode"  class="tableRegion"  width="100%" border="0">
<thead>
 
<tr class="tableHeader" style="padding: 0px;" >
		<td class="tableHeader" style="text-align:left;" colspan="2" align="left">
		    Transaction ID
		</td>
	</tr>		
	
</thead>

	
	<c:forEach items="${transactionCodeListViewModelList}" var="transactionCodeListViewModelList">
	
	<tr>
	<td width="40%" class="titleRow" style=" font-weight: bold; width: 117px;">Transaction ID</td>
		<td class ="even" style="font-family:verdana;font-size: 10px;" width="65%">
			<c:out value="${transactionCodeListViewModelList.code}" /> 
		</td>
	</tr>

	<tr>
	<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Start Time</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionCodeListViewModelList.startTime}" /> </td>
	</tr>
		
	<tr>
	<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">End Time</td>
			<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionCodeListViewModelList.endTime}" /> </td>
	</tr>
		
	<tr>
	<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Status</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionCodeListViewModelList.status}" /> </td>
	</tr>
	
	<tr>
	<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Failure Reason</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionCodeListViewModelList.failureReasonIdFailureReasonModel.name}" /> </td>
		
	</tr>
	
	</c:forEach>
	
</table>
</div>
</c:if>
</br>
<c:if test="${not empty transactionListViewModel}">
<div class="eXtremeTable">
<table id="ec_tableTransaction"    class="tableRegion"  width="100%">
<thead>

<tr style="padding: 0px;">
		<td class ="tableHeader" style="text-align:left" colspan="2">Transaction
		
		</td>
	</tr>		
	</thead>

	<c:forEach items="${transactionListViewModel}" var="transactionListViewModel">
	<tr>
		<td width="40%" class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">
		Code
		
		</td>
		<td class="even" style="font-family:verdana;font-size: 10px;" width="65%">
		<c:out value="${transactionListViewModel.transactionCodeIdTransactionCodeModel.code}" /> 
		
		</td>
		
		
		</tr>

		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Agent ID</td>
		<td class="odd"  style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.mfsId}" /> </td>
		</tr>

	<tr>	
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Transaction Type</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.transactionTypeIdTransactionTypeModel.name}" /> </td>
	</tr>	
		
		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Total Amount</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;">
			<fmt:formatNumber var="ftransactionTotalAmount" type="number" maxFractionDigits="2"   value="${transactionListViewModel.totalAmount}" />
			<c:out value="${ftransactionTotalAmount}"  /> </td>
		</tr>
		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Commission Amount</td>
		<td class="even" style="font-family:verdana;font-size: 10px;">
			<fmt:formatNumber var="ftransactionTotalCommissionAmount" type="number" maxFractionDigits="2"   value="${transactionListViewModel.totalCommissionAmount}" />		
			<c:out value="${ftransactionTotalCommissionAmount}" /> 			
		</td>
		</tr>
		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Transaction Amount</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;">
			<fmt:formatNumber var="ftransactionTransactionAmount" type="number" maxFractionDigits="2"   value="${transactionListViewModel.transactionAmount}" />					
			<c:out value="${ftransactionTransactionAmount}" /> 
		</td>
		</tr>

		<tr> 
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Bank Response Code</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.bankResponseCode}" /> </td>
		</tr>

        <tr> 
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Account No.</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.bankAccountNo}" /> </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Payment Mode</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.paymentModeIdPaymentModeModel.name}" /> </td>
		</tr>

		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Distributor</td>
		
		
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.distributorIdDistributorModel.name}" /> </td>
		
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Retailer</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.retailerIdRetailerModel.name}" /> </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">From Agent Contact</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;">
		<c:out value="${transactionListViewModel.fromDistContactName}" /> </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">From Agent Contact Mobile No</td>
		<td  class="even"style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.fromDistContactMobNo}" /> </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">From Retailer Contact</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;" ><c:out value="${transactionListViewModel.fromRetContactName}" /> </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">From Retailer Contact Mobile No</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.fromRetContactMobNo}" /> </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">To Retailer Contact</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.toRetContactName}" /> </td>
		</tr>


		<tr>
		    <td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">To Retailer Contact Mobile No</td>
		    <td class="even" style="font-family:verdana;font-size: 10px;">
		        <c:out value="${transactionListViewModel.toRetContactMobNo}" />
		    </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">To Agent Contact</td>
		<td class="odd" bordercolor="silver" style="font-family:verdana;font-size: 10px;bordercolor:silver"><c:out value="${transactionListViewModel.toDistContactName}" /> </td>
		</tr>


		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">To Agent Contact Mobile No</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.toDistContactMobNo}" /> </td>
		</tr>

		
		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Notification Mobile No</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.notificationMobileNo}" /> </td>
		</tr>
		
		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Confirmation Message</td>
		<td class="even" style="font-family:verdana;font-size: 10px;">
								<c:out value="${transactionListViewModel.confirmationMessage}" /> 
								
		</td>
		</tr>

		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Sale Mobile No</td>
		<td class="odd"  style="font-family:verdana;font-size: 10px;"><c:out value="${transactionListViewModel.saleMobileNo}" /> </td>
		</tr>

			
	</c:forEach>

</table>
</div>
</c:if>
</br>



<c:if test="${not empty transactionDetailListViewModel}">
<div class="eXtremeTable">
<table id="ec_tableTransactionDetails"  class="tableRegion"  width="100%">

<thead>

<tr style="padding: 0px;" >
		<td class ="tableHeader" style="text-align:left" colspan="2" >Transaction Detail
		</td>
	</tr>		
	
</thead>

	
	<c:forEach items="${transactionDetailListViewModel}" var="transactionDetailListViewModel">
	
	<tr>
		<td width="40%" class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product</td>
		<td class="even" style="font-family:verdana;font-size: 10px;" width="65%"><c:out value="${transactionDetailListViewModel.productIdProductModel.name}"/> </td>
	</tr>

	<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product Unit Price</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;">
		<fmt:formatNumber var="ftransactiondProductUnitPrice" type="number" maxFractionDigits="2"   value="${transactionDetailListViewModel.productUnitPrice}" />		
		<c:out value="${ftransactiondProductUnitPrice}"/> </td>
	</tr>


	<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Consumer No</td>
		<td class="even" style="font-family:verdana;font-size: 10px; border: medium; border-color: black;"><c:out value="${transactionDetailListViewModel.consumerNo}"/> </td>
	</tr>


	<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product Topup Amount</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;">
		
		<fmt:formatNumber var="ftransactionDproductTopupAmount" type="number" maxFractionDigits="2"   value="${transactionDetailListViewModel.productTopupAmount}" />				
		<c:out value="${ftransactionDproductTopupAmount}"/>
		
		</td>
	</tr>


	<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Actual Billable Amount</td>
		<td class="even" style="font-family:verdana;font-size: 10px;">

		<fmt:formatNumber var="ftransactionDactualBillableAmount" type="number" maxFractionDigits="2"   value="${transactionDetailListViewModel.actualBillableAmount}" />
		<c:out value="${ftransactionDactualBillableAmount}"/> 
		
		</td>
	</tr>
	
	<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Settled</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;" ><c:if test="${transactionDetailListViewModel.settled}">True</c:if><c:if test="${!transactionDetailListViewModel.settled}">False</c:if> </td>
	</tr>
	
<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product Cost Price</td>
		<td class="even" style="font-family:verdana;font-size: 10px;">
			<fmt:formatNumber var="ftransactionDproductCostPrice" type="number" maxFractionDigits="2"   value="${transactionDetailListViewModel.productCostPrice}" />		
			<c:out value="${ftransactionDproductCostPrice}"/>
		</td>
	</tr>


		
		
	</c:forEach>
	
</table>
</div>
</c:if>
</br>

<c:if test="${not empty commissionTransactionListViewModel}">
<div class="eXtremeTable">
<table id="ec_tableCommissionTransaction"  class="tableRegion"  width="100%">

<thead> 
<tr style="padding: 0px;" >
		<td class ="tableHeader" style="text-align:left" colspan="2" >Commission Transaction ................
		</td>
	</tr>		
	
</thead>

	
	<c:forEach items="${commissionTransactionListViewModel}" var="commissionTransactionListViewModel">
	
	<tr>
		<td width="40%" class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product</td>
		<td class="even" style="font-family:verdana;font-size: 10px;" width="65%"><c:out value="${commissionTransactionListViewModel.productIdProductModel.name}"/> </td>
	</tr>

	<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product Unit Price</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;" >
			<fmt:formatNumber var="fcommissionproductUnitPrice" type="number" maxFractionDigits="2"   value="${commissionTransactionListViewModel.productUnitPrice}" />						
			<c:out value="${fcommissionproductUnitPrice}"/> 
		</td>
	</tr>
	

	<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product Topup Amount</td>
		<td class="even" style="font-family:verdana;font-size: 10px;">
			<fmt:formatNumber var="fcommissionproductTopupAmount" type="number" maxFractionDigits="2"   value="${commissionTransactionListViewModel.productTopupAmount}" />				
			<c:out value="${fcommissionproductTopupAmount}"/>
		</td>
	</tr>

	
<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Payment Mode</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${commissionTransactionListViewModel.paymentModeIdPaymentModeModel.name}" /> </td>
		</tr>

<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Commission Type</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${commissionTransactionListViewModel.commissionTypeIdCommissionTypeModel.name}" /> </td>
		</tr>
<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Commission Amount</td>
		<td class="even" style="font-family:verdana;font-size: 10px;">
			<fmt:formatNumber var="fcommissionAmount" type="number" maxFractionDigits="2"   value="${commissionTransactionListViewModel.commissionAmount}" />
			<c:out value="${fcommissionAmount}"/>
		</td>
<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Stakeholder Bank</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${commissionTransactionListViewModel.stakeholderBankIdStakeholderBankInfoModel.name}" /> </td>
		</tr>

		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Dispense Type</td>
		<td class="even" style="font-family:verdana;font-size: 10px;"><c:out value="${commissionTransactionListViewModel.dispenseTypeIdDispenseTypeModel.name}" /> </td>
		</tr>
		
		<tr>
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Commission Stake Holder</td>
		<td class="odd" style="font-family:verdana;font-size: 10px;"><c:out value="${commissionTransactionListViewModel.commissionStakeholderIdCommissionStakeholderModel.name}" /> </td>
		</tr>

	<tr> 
		<td class="titleRow" style="text-align: left; font-weight: bold; width: 117px;">Product Cost Price</td>
		<td class="even" style="font-family:verdana;font-size: 10px;">
			<fmt:formatNumber var="fcommissionproductCostPrice" type="number" maxFractionDigits="2"   value="${commissionTransactionListViewModel.productCostPrice}" />
			<c:out value="${fcommissionproductCostPrice}"/>
		</td>
	</tr>

		<tr>
		<td>
		</td>
		</tr>
		<tr>
		<td>
		</td>
		</tr>
		
	</c:forEach>
	
</table>
</div>
</c:if>
<c:if test="${not empty auditLogListViewModelList}">
<div class="eXtremeTable">
<table id="ec_tableAuditLog" class="tableRegion" align="left" width="40%" border="0">



<thead>
<tr style="padding: 0px;" >
		<td class ="tableHeader" style="text-align:left" colspan="2" >Audit Log
		</td>
	</tr>		
	
</thead>

	
	
	<c:forEach items="${auditLogListViewModelList}" var="auditLogListViewModelList" varStatus="x">
	<c:set var="rowNum" value="even"/>
	   				<c:if test="${x.count%2!=0}">
	   					<c:set var="rowNum" value="odd" scope="page"/>
	   				</c:if>
		<tr class="${rowNum}">
		
		<td class="linktext">
		<a class="linktext" href="${pageContext.request.contextPath}/auditlogdetail.html?auditLogId=${auditLogListViewModelList.auditLogId}"onclick="return openAuditLogDetailWindow(${auditLogListViewModelList.auditLogId})">
			<span class="linktext">
			<c:out value="${auditLogListViewModelList.auditLogId}"/> 
			</span>
			</a>
		</td>
	</tr>

	</c:forEach>	



</table>

</div>
</c:if>
</td></tr>
<tr><td align="center">
			<input type ="button" class="button" class="button" value = "Close Window" onclick = "window.close();"/>
	   
</td></tr>
</table>

</body>
</html>