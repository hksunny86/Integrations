<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Change Password</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/aw/aw-contents-height-setter.js"></script>			
<script language="javascript">
	
	function validate() {

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
					<form method="post" action="<c:url value="/changeallpaypassword.aw"/>" id="mfspinchangeform" onsubmit="return validate()">
						<input type="hidden" name ="UID" value="${param.UID}">
						<c:if test="${not empty oldPinLabel}">			
						<input type="hidden" name ="oldPinLabel" value="${oldPinLabel}">
						</c:if>
						<table width="100%" border="0">
						<tr>
							<td colspan="3">
								<div align="center" class="hf">Change Password</div>
							</td>
						</tr>
						<c:if test="${not empty oldPinLabel}">
						<tr>
						  	<td align="center" colspan="3">
						  		&nbsp;
						  		<c:out value="${oldPinLabel}" />
					  		</td>
				  		</tr>
				  		</c:if>
						  <tr>
						  	<td align="center" colspan="3">
						  		&nbsp;
						  		<c:if test="${not empty errors}">
									<span class="error-msg">
										<c:out value="${errors}" />
									</span>
								</c:if>
						  	</td>
						  </tr>
						  <tr>
							<td align="right" width="50%">
								<span class="label">
									Enter Current Password:
								</span>
							</td>
							<td width="5%">
								<input type="password"  name="PIN" id="PIN" maxlength="50" style="-wap-input-format:'50M'" size="14" style="-wap-input-required: true" autocomplete="off" class="text">
							</td>
							<td width="45%">&nbsp;</td>
						  </tr>
						  <tr>
							<td align="right">
								<span class="label">Enter New Password:</span>
							</td>
							<td>
								<input type="password" name="NPIN"  id="NPIN" maxlength="50" style="-wap-input-format:'50M'" size="14" style="-wap-input-required: true" autocomplete="off" class="text"></td>
							<td align="left">
								<I style="padding-left:10px;">[Password needs to be at least 8 characters long]</I>
							</td>
						  </tr>
						  <tr>
							<td align="right"><span class="label">Re-enter New Password:</span></td>
							<td><input type="password" name="CPIN" id="CPIN"  maxlength="50" style="-wap-input-format:'50M'" size="14" style="-wap-input-required: true" autocomplete="off" class="text"></td>
							<td>&nbsp;</td>
						  </tr>
						  <tr>
							<td colspan="3">
							&nbsp;
							</td>
						 </tr>
						  <tr>
							<td>&nbsp;</td>
							<td><input class="mainbutton" size="14" type="submit" id="confirm" name="Submit" value="Change"></td>
							<td>&nbsp;</td>
						  </tr>
						</table>											
						
						<input type="hidden" id="APID" name="APID" value="${sessionScope.APID}" />
						<input type="hidden" id="UID" name="UID" value="${requestScope.UID}" />
												
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
