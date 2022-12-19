<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page	import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Pay Cash</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
			
<script language="javascript">

	function writeError(message) {
		var msg = document.getElementById('msg');
		msg.innerHTML = message;
		msg.className = 'error-msg';
		return false;
	}
	
	
	function validateNumber(field){
	
		if(field.value==""){
			return false; 
		}
		
		var charpos = field.value.search("[^0-9]"); 
		if(charpos >= 0) { 
			return false; 
		} 
		return true;
	}
	
	function validate() {
		
		var PIN = document.getElementById('PIN');	

		if(!validateNumber(PIN) || PIN.value.length < 4) {
			return writeError('Kindly Enter 4 Digit PIN.');		
		}
		
		if(navigator.appName == 'Netscape') {	
			document.getElementById("confirm").disabled = true;
		} else {
			document.getElementById("confirm").disabled = 'disabled';
		}
		
		return true;
	}
</script>			
			
	</head>
	<body oncontextmenu="return false">
		<table align="center" bgcolor="white" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form method="post" action="<c:url value="/payCashTransactionCodeVerification.aw"/>" onsubmit="return validate()">			
						
						<table align="center" width="100%" border="0">
							<tr>
								<td colspan="2" align="center">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td align="center" colspan="2" id='msg'>
									&nbsp;
									<c:if test="${not empty errors}">
										<font color="#FF0000"> <c:out value="${errors}" />
										</font>									
									</c:if>								
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<div align="center" class="hf">Pay Cash To CNIC (Step 2) Confirmation</div><br>
								</td>
							</tr>
							<tr>
								<td align="right" width="50%"><span class="label">Customer :</span></td>
								<td>
									<span class="value-text">Walkin Customer</span>
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">CNIC :</span></td>
								<td>
									<span class="value-text">${requestScope.transactionDetailModel.customField9}</span>					
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">Phone/Mobile # :</span></td>
								<td>
									<span class="value-text">${requestScope.transactionDetailModel.customField5}</span>					
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">Date :</span></td>
								<td><span class="value-text">${requestScope.transactionDetailModel.transactionIdTransactionModel.createdOn}</span></td>
							</tr>
							<tr>
								<td align="right"><span class="label">Amount :</span></td>
								<td><span class="value-text">${requestScope.transactionDetailModel.actualBillableAmount}</span></td>
							</tr>
							<tr>
								<td align="right"><span class="label">Recipient Mobile # :</span></td>
								<td>
									<span class="value-text"><input type="text" name="<%= CommandFieldConstants.KEY_REC_MSISDN %>" maxlength="11" size="11" class="text"/></span>					
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label">PIN :</span><span style="color: #FF0000">*</span></td>
								<td><input type="password" id="<%= CommandFieldConstants.KEY_PIN %>" name="<%= CommandFieldConstants.KEY_PIN %>" size="11" maxlength="4" class="text"/></td>
							</tr>
							<tr>
								<td align="right"></td>
								<td><security:token/><input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/></td>
							</tr>
						</table>

						<input type="hidden" name="<%= CommandFieldConstants.KEY_PROD_ID %>" value="2510801"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_TX_ID %>" value="${requestScope.transactionDetailModel.transactionIdTransactionModel.transactionCodeIdTransactionCodeModel.code}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN %>" value="${requestScope.transactionDetailModel.customField5}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_MOB_NO %>" value="${requestScope.transactionDetailModel.customField5}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_TX_AMOUNT %>" value="${requestScope.transactionDetailModel.actualBillableAmount}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_COMM_AMOUNT %>" value="${requestScope.transactionDetailModel.transactionIdTransactionModel.totalCommissionAmount}"/>
						<input type="hidden" name="<%= CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC %>" value="${requestScope.transactionDetailModel.customField9}"/>

						
					</form>
				</td>
			</tr>
			<tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr>
		</table>
	</body>
</html>