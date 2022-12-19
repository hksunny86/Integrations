<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.inov8.microbank.common.util.CommandFieldConstants" %>
<%@include file="/common/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		
		<title>${param.PNAME}</title>
		
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

		var TXAM = document.getElementById("TXAM");

		if(!validateNumber(TXAM)) {
			return writeError("Kindly Enter Valid Amount.");
		}
		
		if(TXAM.value.length > 11) {
			return writeError("Kindly Enter Valid Amount range, 11 Digits Maximum.");
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
					<form action='<c:url value="/transferIn.aw"/>' method="post" onsubmit="return validate();">
						<table id="contents" align="center" width="100%" border="0">
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td align="center" colspan="2" id="msg">&nbsp;				
									<c:if test="${not empty errors}">
										<font color="#FF0000"> <c:out value="${errors}" /></font>
									</c:if>								
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div align="center" class="hf">Transfer In</div><br>
								</td>
							</tr>
										
							<tr>
								<td align="right" width="50%">
									<span class="label">Enter Amount:</span>					
								</td>
								<td>
									<input type="text" id="<%= CommandFieldConstants.KEY_TXAM %>" name="<%= CommandFieldConstants.KEY_TXAM %>" maxlength="5" size="11" value="${param.TXAM}" class="text">
								</td>
							</tr>
							<tr>
								<td>
								</td>
								<td>&nbsp;
									<security:token/><input class="mainbutton" type="submit" id="confirm" value="Next">
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			${responseData}
			<tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr>
		</table>
	</body>
</html>