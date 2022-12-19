<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Change PIN</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/extremecomponents.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery.navgoco.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/demo.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.cookie.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.navgoco.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/agentWebMenu.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/highlight.pack.js"></script>
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
		var NPIN = document.getElementById('NPIN');
		var CPIN = document.getElementById('CPIN');

		if(!validateNumber(PIN) || PIN.value.length < 4) {
			return writeError('Kindly enter 4 digit existing PIN Code.');		
		}		

		if(NPIN.value.length < 4) {
			return writeError('New PIN Code length must be 4 Digits');		
		}

		if(!validateNumber(NPIN)) {
			return writeError('New PIN Code Must be Numeric');		
		}			

		if(CPIN.value.length < 4) {
			return writeError('Confirm New PIN Code length must be 4 Digits');		
		}

		if(!validateNumber(CPIN)) {
			return writeError('Confirm New PIN Code Must be Numeric');		
		}	

		if(CPIN.value != NPIN.value) {
			return writeError('New PIN and Confirm PIN should have same Number');	
		}

		if(CPIN.value == PIN.value) {
			//return writeError('New PIN and Existing PIN Code should not be same');	
		}
		
		if(navigator.appName == 'Netscape') {		
			document.getElementById("confirm").disabled = true;
		} else {
			document.getElementById("confirm").disabled = 'disabled';
		}
		
		return true;
	}
</script>
		<style type="text/css">
			* {
				box-sizing: border-box;
			}

			.header {
				border: 1px solid red;
				padding: 15px;
			}

			.vertical-left-menu {
				width: 20%;
				float: left;
				padding: 15px;
				overflow-y: scroll;
				height: 500px;

			}

			.main-body-area {
				width: 80%;
				float: left;
				padding: 15px;
				height: 450px;

			}
		</style>
	</head>
	<body oncontextmenu="return false">
	<div style="height:95px%">
		<jsp:include page="header.jsp"></jsp:include>
	</div>
	<c:choose>
	<c:when test="${PIN_CHANGE_REQUIRED}">
		<%session.setAttribute("PIN_CHANGE_REQUIRED","true");%>
	</c:when>
	<c:otherwise>

	<div class="vertical-left-menu ">
		<jsp:include page="agentWebMenu.jsp"></jsp:include>
	</div>
	</c:otherwise>
	</c:choose>
	<div class="main-body-area">
		<table align="center" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			
			<tr id="contents" valign="top">
				<td>
					<form method="post" action="<c:url value="/changePin.aw"/>" onsubmit="return validate()">
						<table align="center" width="100%" border="0">
							<tr>
								<td align="center" colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td align="center" colspan="2" id="msg">
								&nbsp;
								<c:if test="${not empty errors}">
									<span class="error-msg">
										<c:out value="${errors}" />
									</span>
								</c:if>				
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div align="center" class="hf">Change MPIN</div>
								</td>
							</tr>
							<tr>
								<td align="center" colspan="2">&nbsp;</td>
							</tr>
							<c:if test="${IS_MIGRATED eq null}">
								<tr>
									<td align="right" width="50%"><span class="label">Existing MPIN:</span></td>
									<td width="50%"><input type="password" name="PIN" id="PIN" maxlength="4" autocomplete="off" class="text"/></td>
								</tr>
							</c:if>
							<tr>
								<td align="right"><span class="label">New MPIN:</span></td>
								<td><input type="password" name="NPIN" id="NPIN" maxlength="4" class="text"/></td>
							</tr>
							<tr>	
								<td align="right"><span class="label">Confirm New MPIN:</span></td>
								<td><input type="password" name="CPIN" id="CPIN" maxlength="4" class="text"/></td>
							</tr>
							<tr>
								<td align="right">
									<input type="hidden" name="ACID" value="${requestScope.ACID}"/>
									<input type="hidden" name="BAID" value="${requestScope.BAID}"/>
									<input type="hidden" name="UID" value="${requestScope.UID}"/>
									<input type="hidden" name="IS_MIGRATED" value="${IS_MIGRATED}"/>
								</td>
								<td><security:token/><input class="mainbutton" type="submit" id="confirm" name="confirm" value="Change" class="mainbutton"></td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			<%-- <tr valign="bottom">
				<td>
					<jsp:include page="footer.jsp"></jsp:include>
				</td>
			</tr> --%>
		</table>
	</div>
	</body>
</html>