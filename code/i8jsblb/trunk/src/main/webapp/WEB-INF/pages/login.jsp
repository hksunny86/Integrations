<%@ include file="/common/taglibs.jsp"%>

<!--Title: i8Microbank-->
<!--Description: Backened Application for POS terminal-->
<!--Author: Naseer Ullah-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title>i 8 M i c r o b a n k ::: Login Area</title>
<meta http-equiv="Page-Enter" content="blendTrans(Duration=0.2)">
<meta http-equiv="Page-Exit" content="blendTrans(Duration=0.2)">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
<link rel='stylesheet' href="styles/main_login_askari.css" type='text/css'>
<script language="javascript" type="text/javascript">
if((top.location+'').indexOf("/login.jsp") == -1) top.location=self.document.location;

function defineTabID() {
    var iPageTabID = sessionStorage.getItem("tabID");
    if (iPageTabID == null) {
        var iLocalTabID = localStorage.getItem("tabID");
        var iPageTabID = (iLocalTabID == null) ? 1 : Number(iLocalTabID) + 1;
        localStorage.setItem("tabID", iPageTabID);
        sessionStorage.setItem("tabID", iPageTabID);
        document.getElementById("tabvalue").setAttribute('value', iPageTabID);
        //alert("Tab id is ::: " + iPageTabID);
    }
    else {
        document.getElementById("tabvalue").setAttribute('value', iPageTabID);
        //alert("Tab id is ::: " + iPageTabID+" AQ");
    }
}
if (performance.navigation.type == performance.navigation.TYPE_RELOAD) {
    defineTabID();
    console.info("Page is reload")
} else {
    console.info( "page is not reloaded");
}

</script>
</HEAD>
<BODY onload="javascript:$('j_username').focus();">


	<div id="logo_login"></div>

    <div id="box_bg">
        <form method="post" id="loginForm" action="<c:url value="/j_spring_security_check"/>" onsubmit="return validateForm(this)">
        <h1>Sign In</h1>

        <div id="content">
            
            <!-- Social Buttons -->
            <div class="social">

				<div id="errorMsg" class="error" <c:if test="${param.logoutReason == null}"> style="display:none;" </c:if>>
					<c:choose>
						<c:when test="${param.logoutReason eq 'concurrentlogin'}">
							Either You are logged in at a different location or Access rights have been modified.
						</c:when>
						<c:otherwise>${param.logoutReason}</c:otherwise>
					</c:choose>
				</div>

				<div id="errorMsg" class ="error" <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message == null || SPRING_SECURITY_LAST_EXCEPTION.message eq ''}"> style="display:none;"</c:if>>
  					<p><b>Error!</b>
  					<c:choose>
  						<c:when test="${SPRING_SECURITY_LAST_EXCEPTION.class.name ne 'org.springframework.security.authentication.AuthenticationServiceException'}">
			  				<c:set var="isMsgDisplayed" value="0"/>
			  				<c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'Bad Credentials'}">
			  					Error! Bad Credentials
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
			<%session.setAttribute( "loggedInUserType", "6" );%>
            <!-- Login Fields -->
            <div id="login" onmousemove="defineTabID()">Sign in using your registered account:<br/>
                <input name="j_username" id="j_username" maxlength="18" autocomplete="off" type="text" tabindex="1" class="login user"/>
                <input name="j_password" id="j_password" maxlength="18" autocomplete="off" type="password" tabindex="2" class="login password"/>
                <c:if test="${not empty captchaEnc}">
                    <img src="data:image/png;base64,${captchaEnc}" class="captchaimg"/></br>
                    <input type='text' name='j_captcha' id="j_captcha" onchange="defineTabID()" maxlength="5" autocomplete="off" tabindex="3" class = "login captcha" />
                </c:if>
            </div>

			<input name="_csrfckinput" type="hidden" value='<c:out value='${_csrToken}'/>'/>
			<input name="_captchaanswer" type="hidden" value='<c:out value='${captcha}'/>'/>

            <!-- Green Button -->
            <input name="loginButton" type="submit" value="Login" tabindex="4" class="button" />
            <input type="hidden" id="tabvalue" value="AQ" onclick="defineTabID()"/>

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
	    Your Leading Mobile Banking Partner<br />&copy; 2014 - Engineered and Powered by Inov8 Limited.
	    <div class="logo_inov8"><a href="http://www.inov8.com.pk" target="_blank"></a></div>
	</div>

<script type="text/javascript" src="<c:url value='/scripts/login.js'/>"></script>
</BODY>
</HTML>
