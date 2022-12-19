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
		
		function validate() {
			
			writeError('');
			
			var consumerNumber = document.getElementById('consumerNumber');
			var walkInSenderMobileNumber = document.getElementById('walkInSenderMobileNumber');
			var amountTotal = document.getElementById('amount');
			var walkInSenderCnic = document.getElementById('walkInSenderCnic');			
						
			if(consumerNumber != null) {

				if(consumerNumber.value == '') {

					return writeError('Kindly Enter Customer / Order ID.');
					
				} else {
					
					if(!isAlphanumeric(consumerNumber)) {
						
						return writeError('Kindly Enter Valid Customer / Order ID.');
					}					
				}
				
			}
			
			if(!validateNumber(walkInSenderMobileNumber) || walkInSenderMobileNumber.value.length<11) {
				
				return writeError('Kindly Enter 11 Digit Mobile Number.');
				
			} else if((walkInSenderMobileNumber.value+0)==0) {

				return writeError('Kindly Enter Valid 11 Digit Mobile Number.');
			}

			if((walkInSenderMobileNumber.value.substring(0,2) != '03')) {
				return writeError("Mobile # must start with 03.");
			}
			
			if(!validateNumber(amountTotal) || (amountTotal.value+0 == 0)) {
			
				return writeError('Kindly Enter Valid Amount.');	
			}
			
			if(walkInSenderCnic != null && !validateNumber(walkInSenderCnic) || walkInSenderCnic.value.length != 13) {
				
				return writeError('Kindly Enter 13 Digit CNIC.');
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
					<html:form onsubmit="return validate()" method="post" action="donationPayment.aw" commandName="agentWebFormBean">

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
									<div align="center" class="hf" id="titleDiv">Payment for ${agentWebFormBean.productName}</div>
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
									<td width="50%" id="consumerNumberTd">&nbsp;
									
									<html:input id='consumerNumber' path="consumerNumber" maxlength="10" size="16" />
								</tr>
							</c:if>
							<tr>
								<td align="right" width="50%"><span class="label">Mobile # :</span></td>
								<td width="50%" id="mobileTd">&nbsp;
									<html:input id='walkInSenderMobileNumber' path="walkInSenderMobileNumber" maxlength="11" size="16"/>
							</tr>
							<c:if test="${agentWebFormBean.productId!=2510798 && agentWebFormBean.productId!=2510800}">
							<tr>
								<td align="right" width="50%"><span class="label">CNIC :</span></td>
								<td width="50%" id="walkInSenderCnicTd">&nbsp;								
									<html:input id='walkInSenderCnic' path="walkInSenderCnic" maxlength="13" size="16"/>								
							</tr>
							</c:if>
							<tr>
								<td align="right" width="50%"><span class="label">Amount :</span></td>
								<td width="50%" id="amountTd">&nbsp;								
									<html:input id='amount' path="amount" maxlength="6" size="16"/>
							</tr>
							<tr id="next">
								<td align="right" width="50%"></td>
								<td width="50%">&nbsp;<input type="submit" value="Next"/></td>
							</tr>
						</table>
						 
						<table align="center" width="100%" border="0" style="visibility: collapse;" id="confirmTable">
						
							<tr>
								<td>
						
									<html:hidden id='deviceTypeId' path="deviceTypeId"/>
									<html:hidden id='productId' path="productId"/>
									<html:hidden id='productName' path="productName"/>									
									<input type="hidden" id='SID' name='SID' value="${param.SID}"/>
									
								</td>
								<td valign="top" align="left"><security:token/>&nbsp;<input class="mainbutton" size="7" type="submit" id="confirm" name="confirm" value="Submit" ></td>
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