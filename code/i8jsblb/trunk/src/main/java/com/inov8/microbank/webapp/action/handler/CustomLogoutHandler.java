package com.inov8.microbank.webapp.action.handler;



import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Abu Turab 25-10-2017
 */
public class CustomLogoutHandler implements LogoutHandler
{


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        String userType="";
        String logoutURL="/i8Microbank/login.jsp";
        String loggedInUserType = (String)request.getSession().getAttribute("loggedInUserType");
        Cookie removeCookie = new Cookie("loggedInUserType", "");
        removeCookie.setMaxAge(0);
        if(null != loggedInUserType && !"".equals(loggedInUserType))
        {
            userType = loggedInUserType;
        }
        else
        {
            Cookie[] cookies = request.getCookies();
            if(null != cookies) {
                for(Cookie cookie : cookies) {
                    if(cookie.getName() != null && cookie.getName().equalsIgnoreCase("loggedInUserType")) {
                        userType = cookie.getValue();
                        removeCookie = cookie;
                        break;
                    }
                }
            }
        }

        try
        {
            if(userType != null && !userType.equals(""))
            {

                    if(userType.equals("3"))
                        logoutURL = "/i8Microbank/awlogin.jsp";
                    else if(userType.equals("7"))
                        logoutURL = "/i8Microbank/scologin.jsp";
                    else
                        logoutURL = "/i8Microbank/login.jsp";


            }
            else
            {
                logoutURL= "/i8Microbank/login.jsp";
            }

        }
         catch(Exception e){
            e.printStackTrace();

        }
        finally {
             try {
                 removeCookie.setMaxAge(0);
                 response.addCookie(removeCookie);
                 response.sendRedirect(logoutURL);
             }catch(Exception e2){ }
        }

    }

}
