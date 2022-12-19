<%@ include file="/common/taglibs.jsp"%>

<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
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
<HEAD>
<title>Agent Web ::: Login Area</title>
<meta http-equiv="Page-Enter" content="blendTrans(Duration=0.2)">
<meta http-equiv="Page-Exit" content="blendTrans(Duration=0.2)">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<link rel='stylesheet' href="styles/main_login_askari.css" type='text/css'>
<script language="javascript" type="text/javascript">


</script>
</HEAD>
<BODY onload="javascript:$('j_username').focus();">


	<div id="logo_login"></div>

    <div id="box_bg">
        <form method="post" id="loginForm" action="<c:url value="/j_spring_security_check"/>" onsubmit="return validateForm(this)">
        <h1>AgentWeb Sign In</h1>

        <div id="content">
            
            <!-- Social Buttons -->
            <div class="social">

				<div id="errorMsg" class ="error" <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message == null || SPRING_SECURITY_LAST_EXCEPTION.message eq ''}"> style="display:none;"</c:if>>
  					<p><b>Error!</b>
  					<c:choose>
  						<c:when test="${SPRING_SECURITY_LAST_EXCEPTION.class.name ne 'org.springframework.security.authentication.AuthenticationServiceException'}">
			  				<c:set var="isMsgDisplayed" value="0"/>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'Bad credentials'}">
			  					Login Name or Password is incorrect, please try again
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'User account is locked'}">
				  				Your account has been locked or you do not have rights to proceed. Please contact your system administrator
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'User credentials have expired'}">
				  				Your account has expired. Please contact your system administrator
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'Maximum number of login retries reached'}">
				  				Your portal account has been locked. Maximum number of login retries reached. Please contact your system administrator
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'User account has expired'}">
				  				Your account has expired. Please contact your system administrator
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'User is disabled'}">
				  				Your account has disabled. Please contact your system administrator
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'Requests from this IP is not allowed'}">
				  				Login failed. You are not allowed to access the portal from this network. Please contact your system administrator
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'User account is not verified'}">
				  				Your account is not verified. Please contact your system administrator
				  				<c:set var="isMsgDisplayed" value="1"/>
			  				</c:if>
							<c:if test="${isMsgDisplayed eq '0'}">
								<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
			  				</c:if>
  						</c:when>
  						<c:otherwise>
  							Service unavailable. Please try again.
  						</c:otherwise>
  					</c:choose>
  					</p>
  				</div>
            </div>
			<%session.setAttribute( "loggedInUserType", "3" );%>
            <!-- Login Fields -->
            <div id="login">Sign in using your registered account:<br/>
            	<input name="j_username" id="j_username" maxlength="18" type="text" tabindex="1" class="login user"/>
	            <input name="j_password" id="j_password" maxlength="18" type="password" tabindex="2" class="login password"/>
            </div>

			<input name="_csrfckinput" type="hidden" value='<c:out value='${_csrToken}'/>'/>
            
            <!-- Green Button -->
            <input name="loginButton" type="submit" value="Login" tabindex="4" class="button" />
            <!-- Checkbox -->
            <div class="checkbox">
    	        <li>
        		    <fieldset>
			            <![if !IE | (gte IE 8)]><legend id="title2" class="desc"></legend><![endif]>
            			<!--[if lt IE 8]><label id="title2" class="desc"></label><![endif]-->
			            <!-- <div>
				            <span>
					            <input id="rememberMe" name="rememberMe" type="checkbox" class="field checkbox" value="First Choice" tabindex="3"/>
					            <label class="choice" for="rememberMe">Remember Me</label>
				            </span>
			            </div> -->
            		</fieldset>
	            </li>
            </div>
        
        </div>
        </form>
    </div>

	<div id="login-footer">
<%--	    Your Leading Mobile Banking Partner<br />&copy; 2014 - Engineered and Powered by Inov8 Limited.--%>
	    <div class="logo_inov8"><a href="" target="_blank"></a></div>
	</div>

<script type="text/javascript" src="<c:url value='/scripts/login.js'/>"></script>
</BODY>
</HTML>
