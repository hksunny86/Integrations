
/*<script type="text/javascript">*/
/* This function is used to set cookies */
function setCookie(name,value,expires,path,domain,secure) {
  document.cookie = name + "=" + escape (value) +
    ((expires) ? "; expires=" + expires.toGMTString() : "") +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : "");
}

/* This function is used to get cookies */
function getCookie(name) {
	var prefix = name + "=" 
	var start = document.cookie.indexOf(prefix) 

	if (start==-1) {
		return null;
	}
	
	var end = document.cookie.indexOf(";", start+prefix.length) 
	if (end==-1) {
		end=document.cookie.length;
	}

	var value=document.cookie.substring(start+prefix.length, end) 
	return unescape(value);
}

/* This function is used to delete cookies */
function deleteCookie(name,path,domain) {
  if (getCookie(name)) {
    document.cookie = name + "=" +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      "; expires=Thu, 01-Jan-70 00:00:01 GMT";
  }
}
try
{
    /*if (getCookie("username") != null) {
        $("j_username").value = getCookie("username");
        $("j_password").value = getCookie("password");
        $("rememberMe").checked = false;
        $("j_password").focus();
    } else {
        $("j_username").focus();
    }*/
    $("j_username").value ="";
    $("j_password").value ="";
    $("j_username").focus();
}catch(e){}    
    function saveUsername(theForm) {
        var expires = new Date();
        expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.
        setCookie("username",theForm.j_username.value,expires,"<c:url value="/"/>");
    }

    function saveUser(theForm) {
      if($("rememberMe").checked){		 
	        var expires = new Date();
	        expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.
	        setCookie("username",theForm.j_username.value,expires,"<c:url value="/"/>");
	        setCookie("password",theForm.j_password.value,expires,"<c:url value="/"/>");
        }else{
	        deleteCookie("username","<c:url value="/"/>",'');
        }      
    }

    
    function validateForm(form) {                                                               
        return required(form); 
    } 
    
    function passwordHint() {
        if ($("j_username").value.length == 0) {
            alert("The <fmt:message key="+label.username+"/> field must be filled in to get a password hint sent to you.");
            $("j_username").focus();
        } else {
            location.href="<c:url value=/passwordHint.html?username=" + $("j_username").value+"/>";     
        }
    }
    
    function required () { 
    	message = "";
    	fldToFocus = "";
	    if ($("j_username").value.length == 0) 
	    {
	    	message += "\nUser Name is a required field";
	    	fldToFocus = "j_username";
	    }
	    if ($("j_password").value.length == 0) 
	    {
	    	message += "\nPassword is a required field";
	    	if (fldToFocus.length == 0)
	    		fldToFocus = "j_password";
	    }
        if ($("j_captcha").value.length == 0)
        {
            message += "\ncaptcha is a required field";
            if (fldToFocus.length == 0)
                fldToFocus = "j_captcha";
        }

        if ($("j_captcha").value.length > 5)
        {
            message += "\nMax 5 characters are allowed in captcha";
            if (fldToFocus.length == 0)
                fldToFocus = "j_captcha";
        }
		   if(message.length>0)
		   {
		       	alert(message);
		       	$(fldToFocus).focus();
		       	return false;
		   }
		   return true;
    /*
        this.aa = new Array("j_username", "<fmt:message key="errors.required"><fmt:param><fmt:message key="label.username"/></fmt:param></fmt:message>", new Function ("varName", " return this[varName];"));
        this.ab = new Array("j_password", "<fmt:message key="errors.required"><fmt:param><fmt:message key="label.password"/></fmt:param></fmt:message>", new Function ("varName", " return this[varName];"));
        */
        
    } 
