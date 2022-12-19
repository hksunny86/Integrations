<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		
		<meta name="title" content="Change Password" />
		
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
		
		
		var oldPwd = document.forms.changePasswordForm.oldPassword.value;	
		var newPwd = document.forms.changePasswordForm.newPassword.value;
		var confirmPwd = document.forms.changePasswordForm.confirmPassword.value;

           
		   if( oldPwd == null || oldPwd  == "" ) {				   
                return writeError('Kindly Enter Old Password ');
           }
           if( newPwd == null || newPwd == "" ) {				 
                return writeError('Kindly Enter New Password ');
           }
           if( confirmPwd == null || confirmPwd == "" ) {  				
                return writeError('Kindly Enter Confirm New Password ');
           }


		 	if( document.forms.changePasswordForm.newPassword.value == document.forms.changePasswordForm.confirmPassword.value )
			{
				return validateChangePasswordListViewFormModel(document.forms.changePasswordForm);
				}	 
	 		else	
				{
				alert("Password and Confirm Password does not match.");
				document.forms.changePasswordForm.oldPassword.focus();
				return false;
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
		<c:when test="${PASSWORD_CHANGE_REQUIRED}">
			<%session.setAttribute("PASSWORD_CHANGE_REQUIRED","true");%>
		</c:when>
		<c:otherwise>
	
		<div class="vertical-left-menu ">
			<jsp:include page="agentWebMenu.jsp"></jsp:include>
		</div>
		</c:otherwise>
	</c:choose>
	
		<%-- <c:if test="${not empty status.errorMessages}">
			<div class="error-msg">
				<c:forEach var="error" items="${status.errorMessages}">
					<center><c:out  value="${error}" escapeXml="false" /></center>
					<br />
				</c:forEach>
			</div>
		</c:if>

		<c:if test="${not empty messages}">
			<div class="infoMsg" id="successMessages">
				<c:forEach var="msg" items="${messages}">
					<c:out value="${msg}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
			<c:remove var="messages" scope="session" />
		</c:if> --%>
	
	<div class="main-body-area">
					<form  id="changePasswordForm" method="post" action="<c:url value="/awChangepasswordform.html"/>" onsubmit="return validate()">
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
								<c:if test="${not empty status.errorMessages}">
									<span  class="error-msg">
										<c:forEach var="error" items="${status.errorMessages}">
											<center><c:out  value="${error}" escapeXml="false" /></center>
											<br />
										</c:forEach>
									</span>
								</c:if>
						
								<c:if test="${not empty messages}">
									<div class="infoMsg" id="successMessages">
										<c:forEach var="msg" items="${messages}">
											<c:out value="${msg}" escapeXml="false" />
											<br />
										</c:forEach>
									</div>
									<c:remove var="messages" scope="session" />
								</c:if>		
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div align="center" class="hf">Change Password</div>
								</td>
							</tr>
							<tr>
								<td align="center" colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td align="right" width="50%"><span class="label">
								<span style="color:#FF0000">*</span>
								Old Password:</span></td>
								<td width="50%">
								<!-- <input type="password" name="PIN" id="PIN" maxlength="4" autocomplete="off" class="text"/> -->
										<input type="password" id="oldPassword" name="oldPassword"  class="text" maxlength="50" value="" />
								</td>
							</tr>
							<tr>
								<td align="right"><span class="label"><span style="color:#FF0000">*</span>New Password:&nbsp;</span></td>
								<td>								
									<!-- <input type="password" name="NPIN" id="NPIN" maxlength="4" class="text"/> -->
										<input type="password" id = "newPassword" name="newPassword" class="text" maxlength="50" value="" />
								</td>
							</tr>
							<tr>	
								<td align="right"><span class="label"><span style="color:#FF0000">*</span>Confirm New Password:&nbsp;</span></td>
								<td>
									<!-- <input type="password" name="CPIN" id="CPIN" maxlength="4" class="text"/> -->
									<input type="password" id="confirmPassword" name="confirmPassword" class="text" maxlength="4000" />
								</td>
							</tr>
							<tr>
								<td align="right">
									<input type="hidden" name="ACID" value="${requestScope.ACID}"/>
									<input type="hidden" name="BAID" value="${requestScope.BAID}"/>
									<input type="hidden" name="UID" value="${requestScope.UID}"/>
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