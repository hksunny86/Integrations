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
			
				var CNIC = document.getElementById('CNIC');

				if(CNIC.value.length < 13) {
					return writeError('CNIC length must be 13 Digits');		
				}

				if(!validateNumber(CNIC)) {
					return writeError('CNIC Must be Numeric');		
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
					<form method="post" action="<c:url value="/payCashCNICStepOne.aw"/>" onsubmit="return validate()">
						<input type="hidden" name="PID" value="${param.PID}"/>
						<input type="hidden" name="dfid" value="${param.dfid}"/>
						<input type="hidden" name="TXAM" value="${param.TXAM}"/>
						<input type="hidden" name="TXAMF" value="${TXAMF}"/>
						<input type="hidden" name="MOBN" value="${MOBN}"/>			
						<input type="hidden" name="PNAME" value="${param.PNAME}"/>
						<input type="hidden" name="reqTime" value="<%=System.currentTimeMillis()%>"/>				
						<input type="hidden" name="CAMT" value="${requestScope.CAMT}"/>
						<input type="hidden" name="TPAM" value="${requestScope.TPAM}"/>
						<input type="hidden" name="MOBN" value="${requestScope.MOBN}"/>
						<input type="hidden" name="RPNAME" value="${requestScope.RPNAME}"/>
						<input type="hidden" name="TAMT" value="${requestScope.TAMT}"/>
						<input type="hidden" name="CAMTF" value="${requestScope.CAMTF}"/>
						<input type="hidden" name="TPAMF" value="${requestScope.TPAMF}"/>
						<input type="hidden" name="TAMTF" value="${requestScope.TAMTF}"/>
						<input type="hidden" name="BAMT" value="${TXAM}"/>			
						
						<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td colspan="2">&nbsp;
								
								</td>
							 </tr>
							<tr>
								<td align="center" colspan="2" id="msg">
									&nbsp;
									<c:if test="${not empty errors}">
										<font color="#FF0000">
											<c:out value="${errors}" />
											<br/>
										</font>
									</c:if>					
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div align="center" class="hf">Pay Cash To CNIC (Step 1)</div>
								</td>
							</tr>
							<tr>
								<td colspan="2">&nbsp;
								
								</td>
							 </tr>
							<tr>
								<td align="right" width="50%"><span class="label">CNIC:</span><span style="color: #FF0000">*</span></td>
								<td>
								<input type="text" name="<%= CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC %>" id="<%= CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC %>" maxlength="13" size="13" class="text"/>
								</td>
							</tr>
							<tr>
								<td align="right"></td>
								<td><security:token/>&nbsp;&nbsp;<input class="mainbutton" type="submit" id="confirm" name="confirm" value="Details"></td>
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