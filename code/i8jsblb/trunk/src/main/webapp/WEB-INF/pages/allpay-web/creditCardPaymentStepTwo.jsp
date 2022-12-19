<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Bill Payment</title>
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
	
	function isValidFormat(mobileNumber) {
		
		return (mobileNumber.substring(0,2) == '03') ;
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
		
		var AMT = document.getElementById('AMT');	
		var CDCUSTMOB = document.getElementById('CDCUSTMOB');	

		if(AMT.value == '') {
			return writeError('Bill Amount is required.');	
		}
		
		if(!validateNumber(AMT)) {
			return writeError('Bill Amount must be Numeric.');	
		}
		
		if(CDCUSTMOB.value == '') {
			return writeError('Customer Mobile # is required.');	
		}
		if(!validateNumber(CDCUSTMOB) || CDCUSTMOB.value.length != 11) {
			return writeError('Customer Mobile # must be 11 Digit.');	
		}
		if(!isValidFormat(CDCUSTMOB.value)) {
			return writeError('Customer Mobile # format must be 03XXXXXXXXX.');	
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
		<table cellpadding="0" cellspacing="0" height="100%" width="85%" align="center" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form name="billpaymentform" method="post" action='<c:url value="/creditCardPaymentStepTwo.aw"/>' onsubmit="return validate()">
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
									<div align="center" class="hf">${param.PNAME} Payment</div><br>
								</td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">Credit Card Number :</span>
								</td>
								<td>&nbsp;&nbsp;&nbsp;${param.CCNO}</td>
							</tr>												

							<tr>
								<td align="right" width="50%"><span class="label">Bill Amount :</span></td>
								<td>&nbsp;&nbsp;&nbsp;${MAMTF}</td>
							</tr>
							<tr>
								<td align="right" width="50%"><span class="label">Bill Due Date :</span></td>
								<td>&nbsp;&nbsp;&nbsp;${BDDATE}</td>
							</tr>
							<tr>
								<td align="right" width="50%"><span class="label">Outstanding Balance:</span></td>
								<td>&nbsp;&nbsp;&nbsp;${CCOB}</td>
							</tr>							
							<tr>
								<td align="right" width="50%">
									<span style="color: #FF0000">*</span><span class="label">Amount :</span></td>
								<td><input type="text" id="TXAM" name="TXAM" maxlength="20" size="11" 
										style="-wap-input-format: '*N'; -wap-input-required: true" class="text" /></td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span style="color: #FF0000">*</span><span class="label">Customer Mobile # :</span></td>
								<td><input type="text" id="MOBN" name="MOBN" maxlength="11" size="11" 
										style="-wap-input-format: '*N'; -wap-input-required: true" class="text" /></td>
							</tr>
							<tr>
								<td align="right">
									<input type="hidden" name='SID' value="${param.SID}"/>
									<input type="hidden" name="PID" value="${param.PID}"/>
									<input type='hidden' name='CCNO' value='${param.CCNO}'>
									<input type='hidden' name='PNAME' value='${param.PNAME}'/>
									<input type="hidden" name="CAMT" value="${param.CAMT}"/>
									<input type='hidden' name='BDDATE' value='${BDDATE}'>
									<input type='hidden' name='CCOB' value='${CCOB}'>
									<input type="hidden" name="MAMTF" value="${MAMTF}"/><br>	
									<input type="hidden" name="TPAM" value="${TPAM}"/><br>	
								</td>
								<td><security:token/>&nbsp;&nbsp;&nbsp;<input name="next" class="mainbutton" type="submit" id="confirm" value="Next" /></td>
							</tr>
						</table>				
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