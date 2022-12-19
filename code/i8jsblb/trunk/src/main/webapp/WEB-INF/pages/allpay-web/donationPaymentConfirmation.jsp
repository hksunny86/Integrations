<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Collection Payment</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		<script type="text/javascript" src="${contextPath}/scripts/commonFormValidator.js"></script>
		
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

		
		function validatePin() {

			var code = document.getElementById('bankPin');
			if(code.value.length < 4) {
				return writeError('PIN Code length must be 4 digits');		
			}
	
			if(!validateNumber(code)) {
				return writeError('PIN Code must be numeric');		
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
		<table border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" align="center" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<html:form method="post" action="donationPaymentConfirmation.aw" onsubmit="return validatePin()" commandName="agentWebFormBean">

						<table align="center" width="100%" border="0">
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td align="center" colspan="2" id="msg" style="font-family:Arial, Helvetica, sans-serif;">
									&nbsp;
									<span class="error-msg" id="error">
										<c:if test="${not empty errors}">
											<c:out value="${errors}" />
										</c:if>					
									</span>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div align="center" class="hf" id="titleDiv">Payment for ${agentWebFormBean.productName} Confirmation</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
						</table>
						<table align="center" width="100%" border="0" id="inputTable">
							<c:if test="${agentWebFormBean.productId==2510791}">
								<tr>
									<td align="right" width="50%"><span class="label">Customer / Order Id :</span></td>
									<td width="50%" id="orderIdTd">&nbsp;&nbsp;${agentWebFormBean.consumerNumber}
										<html:hidden id='consumerNumber' path="consumerNumber"/>
									</td>
								</tr>
							</c:if>
							<tr>
								<td align="right" width="50%"><span class="label">Mobile # :</span></td>
								<td width="50%" id="mobileTd">&nbsp;&nbsp;${agentWebFormBean.walkInSenderMobileNumber}
									<html:hidden id='walkInSenderMobileNumber' path="walkInSenderMobileNumber"/>
								</td>
							</tr>
							<c:if test="${agentWebFormBean.productId!=2510798 && agentWebFormBean.productId!=2510800}">
							<tr>
								<td align="right" width="50%"><span class="label">CNIC :</span></td>
								<td width="50%" id="cnicTd">&nbsp;&nbsp;${agentWebFormBean.walkInSenderCnic}						
									<html:hidden id='walkInSenderCnic' path="walkInSenderCnic"/>	
								</td>							
							</tr>
							</c:if>
							<tr>
								<td align="right" width="50%"><span class="label">Amount : </span></td>
								<td width="50%" id="amountTd">&nbsp;&nbsp;${agentWebFormBean.amount}.0								
									<html:hidden id='amount' path="amount"/>
								</td>
							</tr>
						
							<c:if test="${agentWebFormBean.miscInfo == 5}">
						
								<tr>
									<td align="right" width="50%"><span class="label" id="chargesLabel">Charges :</span></td>
									<td width="50%" id="chargesValue">&nbsp;&nbsp; ${agentWebFormBean.commissionAmountTotal}
										<html:hidden id='commissionAmountTotal' path="commissionAmountTotal"/>
									</td>
								</tr>
							
							</c:if>
							<tr>
								<td align="right" width="50%"><span class="label">PIN :</span></td>
								<td width="50%" id="amountTd">&nbsp;&nbsp;						
									<html:password id='bankPin' path="bankPin" alt="Enter Bank PIN" autocomplete="off" maxlength="4" size="10" />	
								</td>
							</tr>
							<tr>
								<td>
									<html:hidden id='deviceTypeId' path="deviceTypeId"/>
									<html:hidden id='productId' path="productId"/>
									<html:hidden id='productName' path="productName"/>
									<html:hidden id='miscInfo' path="miscInfo"/>									
									<input type="hidden" id='SID' name='SID' value="${param.SID}"/>									
								</td>
								<td valign="top"><security:token/>&nbsp;&nbsp;<input class="mainbutton" size="7" type="submit" id="confirm" name="confirm" value="Confirm" ></td>
							</tr>
						</table>
						
					</html:form>
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