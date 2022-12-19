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
	
		var code = document.getElementById('PIN');

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
		<table cellpadding="0" cellspacing="0" height="100%" width="85%" align="center" class="container">
			<tr height="95px">
				<td>
					<jsp:include page="header.jsp"></jsp:include>
				</td>
			</tr>
			<tr id="contents" valign="top">
				<td>
					<form name="billpaymentform" method="post" action='<c:url value="/transferInConfirmation.aw"/>' onsubmit="return validate()">
						<table align="center" width="100%" border="0">
							<tr>
								<td colspan="2" align="center">&nbsp;<br></td>
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
									<div align="center" class="hf">${requestScope.NAME} Confirmation<input type='hidden' name='PID' value='${requestScope.PID}'/></div><br>	
								</td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">From Account Number (Core) :</span>								
								</td>
								<td>${requestScope.ACCNO}<input type='hidden' name='ACCNO' value='${requestScope.ACCNO}'/></td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">To Account Number (Branchless) :</span>								
								</td>
								<td>${requestScope.BBACCNO}<input type='hidden' name='BBACCNO' value='${requestScope.BBACCNO}'/></td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">Amount :</span>								
								</td>
								<td>${((requestScope.TXAM) - (requestScope.CAMT))}<input type='hidden' name='TXAM' value='${requestScope.TXAM}'/></td>
							</tr>
							<tr>
								<td align="right" width="50%">
									<span class="label">Commission/Tax Amount :</span>								
								</td>
								<td>${requestScope.CAMT}<input type='hidden' name='CAMT' value='${requestScope.CAMT}'/></td>
							</tr>
						
							<tr>
								<td align="right" width="50%"><span style="color: #FF0000">*</span><span class="label">PIN :</span></td>
								<td><input type="password" maxlength="4" size="12" id="PIN" name="PIN" value="" autocomplete="off"/></td>
							</tr>
							<tr>
								<td align="right"></td>
								<td><security:token/><input name="next" class="mainbutton" type="submit" id="confirm" value="Next" /></td>
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