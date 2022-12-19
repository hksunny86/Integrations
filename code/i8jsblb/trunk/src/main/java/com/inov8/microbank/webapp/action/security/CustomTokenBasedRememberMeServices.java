/**
 * 
 */
package com.inov8.microbank.webapp.action.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;


/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 17, 2007
 * Creation Time: 			10:57:31 AM
 * Description:				
 */
public class CustomTokenBasedRememberMeServices extends
		TokenBasedRememberMeServices
{
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
    {
     if(authentication!=null)
    	 super.logout(request, response, authentication);
     else
     {
    	 logger.warn("Session Already Expired. Authentication is null");
    	 cancelCookie(request,response);
     }
    }

}
