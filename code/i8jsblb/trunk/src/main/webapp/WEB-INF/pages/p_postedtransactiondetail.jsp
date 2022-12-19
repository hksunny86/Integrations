<%@include file="/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

   <head>  
      <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/commonFormValidator.js"></script>
       
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css">
      <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

     <meta name="title" content=""/>

</head>

   <body bgcolor="#ffffff" >
   
      <spring:bind path="transactionLogModel.*">
	  <c:if test="${not empty status.errorMessages}">
	    <div class="errorMsg">
	      <c:forEach var="error" items="${status.errorMessages}">
	        <c:out value="${error}" escapeXml="false"/>
	        <br/>
	      </c:forEach>
	    </div>
	  </c:if>
	</spring:bind>
	
	<c:if test="${not empty messages}">
	    <div class="infoMsg" id="successMessages">
	    	<script>window.opener.location = 'p_postedtransactionmanagement.html?actionId=2&_formSubmit=true'</script>
	        <c:forEach var="msg" items="${messages}">
	            <c:out value="${msg}" escapeXml="false"/><br/>
	        </c:forEach>
	    </div>
	    <c:remove var="messages" scope="session"/>
	</c:if>
	  
<div class="eXtremeTable">
  		<table class="tableRegion" width="100%">
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Transaction Date</td>
		   		<td colspan="4"> 
		   			<fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss a" value="${transactionLogModel.transactionDate}"/>
				</td>
			</tr>
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Type</td>
		   		<td colspan="4"> 
		   			${transactionLogModel.displayTransactionType}
				</td>
			</tr>
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">RRN</td>
		   		<td colspan="4"> 
		   			${transactionLogModel.displayRRN}
				</td>
			</tr>
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">From Account #</td>
		   		<td colspan="4"> 
		   			${transactionLogModel.fromAccount}
				</td>
			</tr>
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">To Account #</td>
		   		<td colspan="4"> 
		   			${transactionLogModel.toAccount}
				</td>
			</tr>			
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Amount</td>
		   		<td colspan="4"> 
		   			${transactionLogModel.amount}
				</td>
			</tr>	
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Response Code</td>
		   		<td colspan="4"> 
					${transactionLogModel.responseCode}
				</td>
			</tr>			
	   		<tr>
				<td class="titleRow" style="width: 185px; font-weight: bold">Status</td>
		   		<td colspan="4"> 
					${transactionLogModel.displayStatus}
				</td>
			</tr>
			<c:if test="${not empty transactionLogModel.reversalTransactionList}">

					   		<tr>
								<td class="tableHeader" style="font-weight: bold; align:center" colspan="10">Auto Reversal History</td>
						   	</tr>
							<tr>
	   							<td width="185" class="tableHeader" style="width:125px;">Transaction Date</td>
			   					<td width="60" class="tableHeader" style="text-align: center; width:125px;">Type</td>
			   					<td width="88" class="tableHeader" style="text-align: center; width:125px;">RRN</td>
			   					<td width="88" class="tableHeader" >From Account #</td>
			   					<td width="250" class="tableHeader" >To Account #</td>
			   					<td width="88" class="tableHeader" >Amount</td>
			   					<td width="88" class="tableHeader" >Response Code</td>
			   					<td width="88" class="tableHeader" >Rectification Status</td>
							</tr>		

							<c:forEach items="${transactionLogModel.reversalTransactionList}" var="reversalHistoryModel"  varStatus="x">
								<c:set var="rowCssClass" value="even"/>
				   				<c:if test="${x.count%2!=0}">
			   						<c:set var="rowCssClass" value="odd" scope="page"/>
		   						</c:if>
					   			<tr class="${rowCssClass}" onmouseover="this.className='highlight'"  onmouseout="this.className='${rowCssClass}'">
		   							<td style="width: 125px;">
										<fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss a" value="${reversalHistoryModel.transactionDate}"/>
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${reversalHistoryModel.displayTransactionType}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${reversalHistoryModel.displayRRN}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${reversalHistoryModel.fromAccount}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${reversalHistoryModel.toAccount}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${reversalHistoryModel.amount}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${reversalHistoryModel.responseCode}
				   					</td>
				   					<td style="text-align: center; width:125px;">
										${reversalHistoryModel.displayStatus}
				   					</td>
				   				</tr>
			   				</c:forEach>
			   	</c:if>			
		</table>
</div>
      
<script language="javascript" type="text/javascript">
	function closeWindow(){
		window.close();
	}
</script>
   
   </body>
</html>