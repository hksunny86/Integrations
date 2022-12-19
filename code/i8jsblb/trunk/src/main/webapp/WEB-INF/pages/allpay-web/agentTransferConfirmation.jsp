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
		
		var PIN = document.getElementById('PIN');		

		if(!validateNumber(PIN) || PIN.value.length < 4) {
			return writeError('Kindly Enter 4 Digit PIN Code.');		
		}			
		
		if(navigator.appName == 'Netscape') {		
			document.getElementById("confirm").disabled = true;
		} else {
			document.getElementById("confirm").disabled = 'disabled';
		}
		
		return true;
	}

	function goHome( )
	{
		var homeUrl = "mainmenu.aw?ACID=" + ${requestScope.ACID} + "&BAID=" + ${requestScope.BAID} + "&UID=" + ${sessionScope.UID};
		window.location.replace( homeUrl );
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
					<form method="post" action="<c:url value="/agentTransferConfirmation.aw"/>" onsubmit="return validate()">
									
						
						<table id="contents" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
						
							<tr>
								<td colspan="2">
								&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center" id='msg'>	
									<font color="#FF0000">		
									<c:if test="${not empty errors}">
											<c:out value="${errors}" />
									</c:if>	
									<br/>
									</font>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div align="center" class="hf">Agent Transfer Confirmation</div>
									<br>
								</td>
							</tr>
							<tr>
								<td width="50%" align="right"><span class="label">Sender Mobile #: </span></td>
								<td width="50%">&nbsp;&nbsp;&nbsp;<span class="value-text" id="mobileNumber"><c:out value="${requestScope.SENDER_MOBILE}"></c:out></span></td>
							</tr>
							<tr>
								<td width="50%" align="right"><span class="label">Sender Name :</span></td>
								<td width="50%">&nbsp;&nbsp;&nbsp;<span class="value-text" id='cnicCustomer'><c:out value="${requestScope.NAME}"></c:out></span></td>
							</tr>
							<tr>
								<td width="50%" align="right"><span class="label">Receiver Name :</span></td>
								<td width="50%">&nbsp;&nbsp;&nbsp;<span class="value-text"><c:out value="${requestScope.RECEIVER_NAME}"></c:out></span></td>
							</tr>
							<tr>
								<td width="50%" align="right"><span class="label">Receiver Mobile # :</span></td>
								<td width="50%">&nbsp;&nbsp;&nbsp;<span class="value-text"><c:out value="${requestScope.CDCUSTMOB}"></c:out></span></td>
							</tr>
							<tr>
								<td width="50%" align="right"><span class="label">Receiver CNIC # :</span></td>
								<td width="50%">&nbsp;&nbsp;&nbsp;<span class="value-text"><c:out value="${requestScope.RECEIVER_CNIC}"></c:out></span></td>
							</tr>
							<tr>
								<td width="50%" align="right">&nbsp;&nbsp;&nbsp;<span class="label">Amount :</span></td>
								<td width="50%">&nbsp;&nbsp;&nbsp;<span class="value-text">${requestScope.TXAM}</span></td>
							</tr>
							<tr>
								<td width="50%" align="right">
									<span style="color: #FF0000">*</span><span class="label">PIN :</span>									
								</td>
								<td width="50%"><input type="password" id="PIN" name="PIN" maxlength="4" size="4" autocomplete="off" class="text"/></td>
							</tr>
							<tr>
								<td width="50%" colspan="2" align="center"><security:token/>&nbsp;&nbsp;
									<input class="mainbutton" type="submit" id="confirm" name="confirm" value="Confirm"/>
									<input class="mainbutton" type="button" name="cancel" value="Cancel" onclick="goHome();"/>
								</td>
							</tr>
						</table>

						<input type="hidden" value="${requestScope.SENDER_MOBILE}"  name="<%= CommandFieldConstants.KEY_CSCD %>" />
						<input type="hidden" value="${requestScope.CDCUSTMOB}" name="<%= CommandFieldConstants.KEY_CD_CUSTOMER_MOBILE %>"/>
						<input type="hidden" value="${requestScope.RECEIVER_NAME}" name="RECEIVER_NAME"/>
						<input type="hidden" value="${requestScope.RECEIVER_CNIC}" name="RECEIVER_CNIC"/>						
						
						<input type="hidden" name="TXAM" value="${requestScope.TXAM}"/>
						<input type="hidden" name="TXAMF" value="${requestScope.TXAMF}"/>
						<input type="hidden" name="CUSTOMERCNIC" value="${CNIC}"/>
						<input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>
						
						<input type="hidden" name="CAMT" value="${requestScope.CAMT}"/>
						<input type="hidden" name="TPAM" value="${requestScope.TPAM}"/>
						<input type="hidden" name="MOBN" value="${requestScope.MOBN}"/>
						<input type="hidden" name="RPNAME" value="${requestScope.RPNAME}"/>
						<input type="hidden" name="TAMT" value="${requestScope.TAMT}"/>
						<input type="hidden" name="CAMTF" value="${requestScope.CAMTF}"/>
						<input type="hidden" name="TPAMF" value="${requestScope.TPAMF}"/>
						<input type="hidden" name="TAMTF" value="${requestScope.TAMTF}"/>
						<input type="hidden" name="BAMT" value="${requestScope.TAMT}"/>						
												
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