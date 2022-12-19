<%@page	import="com.inov8.microbank.common.util.CommandFieldConstants,com.inov8.microbank.common.util.UtilityCompanyEnum" %>
<%@include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Fund Transfer Receipt</title>

<script language="javascript">

	function printAndHide() {
		
		var print = document.getElementById('print');
		print.style.visibility = 'hidden';
		window.print(); 
	}

</script>

</head>

<body style="font-family: Courier,yArial">

<fieldset style="width:95%;border-color: black;">

<table width="100%" border="0">   
      <tr>
        <td>&nbsp;</td>
        <td align="right"">
        	<div id="print">
        		<a href="#" onclick="printAndHide()">Print</a>
        	</div>
        </td>
      </tr>
      <tr>
        <td><b>TRANSACTION RECEIPT </b></td>
        <td colspan="2" rowspan="0"><img src="images/aw/logo-js-jsbank.png" align="right"/></td>
      </tr>
      <tr>
        <td>Transaction Type:</td>
        <td> ${requestScope.TRANSACTION_TYPE}</td>
      </tr>
      <tr>
        <td>Transaction ID:</td> 
        <td>${requestScope.TRXID}</td>
      </tr>
      <tr>
        <td>Agent ID:</td> 
        <td>${sessionScope.UID}</td>
      </tr>
      <tr>
        <td>Dated : </td>
        <td colspan="2">${requestScope.TRANSACTION_DATE}  ${requestScope.TRANSACTION_TIME}</td>
      </tr>
      <c:if test="${requestScope.TXCD == 'BILL_PRODUCT'}">
	      <tr>
	        <td>Consumer #. </td>
	        <td colspan="2">${requestScope.CONSUMER_NUMBER}</td>
	      </tr>
      </c:if>
      
		<c:choose>
			<c:when test="${requestScope.PID == '50006' || requestScope.TXCD == 'BILL_PRODUCT' || requestScope.PID == '50002'}">
		      <tr>
		        <td width="50%">Customer Mobile #.</td>
		        <td colspan="2">${requestScope.CUSTOMER_MOBILE}</td>
		      </tr>	
			</c:when>
			<c:otherwise>      
			  <c:if test="${(requestScope.PID=='50011' &&  requestScope.LEG=='1')}">	
		      <tr>
		        <td width="50%">Sender CNIC.</td>
		        <td colspan="2">${requestScope.WALKIN_SENDER_CNIC}</td>
		      </tr>	      
		      <tr>
		        <td>Sender Mobile No. </td>
		        <td colspan="2">${requestScope.WALKIN_SENDER_MSISDN}</td>
		      </tr>
		      </c:if>
		      <tr>
		        <td>Receiver CNIC.</td>
		        <td colspan="2">${requestScope.WALKIN_REC_CNIC}</td>
		      </tr>
		      <tr>
		        <td>Receiver Mobile No</td>
		        <td colspan="2">${requestScope.WALKIN_REC_MSISDN}</td>
		      </tr>	
			</c:otherwise>
		</c:choose>
      <tr>
        <td>Transaction Amount (Rs.) </td>
        <td colspan="2">${requestScope.TRANSACTION_AMOUNT}</td>
      </tr>
      <c:if test="${requestScope.PID=='50002' || (requestScope.PID=='50010' &&  requestScope.LEG=='1') || (requestScope.PID=='50011' &&  requestScope.LEG=='1') || (requestScope.PID=='2510798' &&  requestScope.LEG=='2') || (requestScope.PID=='2510800' &&  requestScope.LEG=='2')}">
	      <tr>
	        <td>Charges (Rs.) </td>
	        <td colspan="2">${requestScope.DEDUCTION_AMOUNT}</td>
	      </tr>
	      <tr>
	        <td>Total From Customer (Rs.) </td>
	        <td colspan="2">${requestScope.TOTAL_AMOUNT}</td>
	      </tr>
      </c:if>
      <c:if test="${requestScope.PID=='50006'}">
	      <tr>
	        <td colspan="2">*Charges Applied</td>
	      </tr>
      </c:if>
      <tr>
        <td>&nbsp;</td>
        <td></td>
      </tr>
      <tr>
        <td align="left"><u>Customer Signature/Thumb</u> </td>
        <td align="left"><u>Agent Signature/Stamp</u> </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>   
    </table>
</fieldset>    
</body>
</html>
