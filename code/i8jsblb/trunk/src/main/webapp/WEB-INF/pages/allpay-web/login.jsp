<%@include file="/common/taglibs.jsp"%>
<%--
Turab:Security:Pragma No-cache
Pragma and Cache-Control Headers are set in responce in order to prevent Browser Clients from caching the response that is send back to them from server.
Following scriptlet should be added on every page of the application, so in Microbank we need to add this scriptlet to header.jsp and login.jsp pages.
--%>
<%
	HttpServletResponse httpServletResponse = (HttpServletResponse) pageContext.getResponse();
	httpServletResponse.setHeader("Cache-Control", "no-cache");
	httpServletResponse.setHeader("Pragma", "No-cache");
	long currentTime = System.currentTimeMillis();
	httpServletResponse.setDateHeader("Expires", currentTime + 3000L);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Branchless Banking Web Application</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awstyle_new.css" type="text/css">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/awlogin.css" type="text/css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.min.js"></script>


<script language="javascript">

	$(document).ready(
		function()
		{
			adjustScreenHeight();
		/* 	if( parent.name == null || parent.name != "BB_Agent_Application" )
			{
				$('#contents').remove();
				alert('You cannot access Agent Web Application directly.');
				setInterval(
					function()
				    {
				        window.close();
				    },
				    3000);
			}
			else  */
			
			if( ${not empty param.UID and empty errors} )
			{
				$('#loginform').submit();
			}
		}

	);	
	
		function adjustScreenHeight()
		{
			var containerMargin = parseInt( $('.container').css('margin-top'), 10 ) + parseInt( $('.container').css('margin-bottom'), 10 );
			var topHeaderHeight = $('#header_portal').height();
			var windowHeight = $(window).height();
			$('#contents').height( windowHeight - topHeaderHeight - containerMargin );
		}	
	
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
		
		var UID = document.getElementById("UID");
		var AMOUNT = document.getElementById("AMOUNT");
		var flag = true;
		
		if(!validateNumber(UID) && (UID.value == "" || UID.value == null)) {
			flag = writeError('Kindly Enter 7 Digit User ID.');	
			return flag;
		}	

		/*
		if(!validateNumber(AMOUNT) && (AMOUNT.value == "" || AMOUNT.value == null)) {
			flag = writeError('Kindly Enter Amount.');
			return flag;		
		}
		*/			
		/*
		if(flag && navigator.appName == 'Netscape') {		
			document.getElementById("confirm").disabled = true;
		} else if(flag && navigator.appName != 'Netscape') {
			document.getElementById("confirm").disabled = 'disabled';
		}	
		*/
		return flag;
	}

	function closeWindow() {

		setInterval(
				function()
			    {
			        window.close();
			    },
			    3000);
	}
	
	function afterLogout() {
        opener.location.reload();
		setInterval(
				function()
			    {
					writeError("Closing Window...");
					closeWindow();
			    },
			    3000);
	}

</script>		
	</head>
	<body oncontextmenu="return false;">
		<table align="center" border="0" cellpadding="0" cellspacing="0" height="100%" width="85%" class="container">
			<tr height="95px">
				<td>
					<div id="header_portal">
                        <div class="logo_portal"></div>
                        <div class="banner_right_top"></div>
                    </div>
				</td>
			</tr>

			<tr id="contents" valign="top">
				<td>
					<form id="loginform" name="loginform" method="post" action="agentweblogin.aw" onsubmit="return validate();">
						<input type="hidden" name="isUpdate" id="isUpdate" value="true" />
			
						<table id="contents" width="100%" border="0" align="center">
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									&nbsp;
									<span class="error-msg" id="msg">
						                <c:if test="${not empty errors}">
											<c:out value="${errors}" />
											
											<script language="javascript">
												afterLogout();	
											</script>											
											
										</c:if>
						            </span>
								</td>
							</tr>
						
							<c:if test="${param.UID != null}">
												
								<tr>
									<td align="right" width="50%">
										<%-- <span class="label">Enter Amount (In Hand):</span> --%>
									</td>	
									<td align="left" width="50%">
										<%-- <input name="AMOUNT" id="AMOUNT" type="text" value="" size="12" maxlength="7" style="-wap-input-format:'NNNNNNNNNNNN'" style="-wap-input-required: true"/> --%>
										<input name="UID" id="UID" type="hidden" value="${param.UID}" size="12" maxlength="7" style="-wap-input-format:'NNNNNNNNNNNN'"	style="-wap-input-required: true"/>
									</td>								
								</tr>
								
								<tr height="10%"><td colspan="2">&nbsp;</td></tr>
								<tr>
									<td colspan="2" align="center">&nbsp;<img src="images/aw/waiting.gif" /></td>
								</tr>
							</c:if>

							<c:if test="${param.UID == null}">	
								<script language="javascript">
									afterLogout(null);	
								</script>
							</c:if>							
							
						</table>
						
					</form>
				</td>
			</tr>			
		</table>
	</body>
</html>
