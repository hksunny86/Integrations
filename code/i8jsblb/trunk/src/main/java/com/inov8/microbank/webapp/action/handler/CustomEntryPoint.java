package com.inov8.microbank.webapp.action.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomEntryPoint extends LoginUrlAuthenticationEntryPoint
{
    String loginUrl="/login.jsp";
    public CustomEntryPoint(){
        super();
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
    {
        String userType="";
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
                    loginUrl = "/awlogin.jsp";
                else if(userType.equals("7"))
                    loginUrl = "/scologin.jsp";
                else
                    loginUrl = "/login.jsp";


            }
            else
            {
                loginUrl= "/login.jsp";
            }

        }
        catch(Exception e){
            e.printStackTrace();

        }
        finally {
            try {
                removeCookie.setMaxAge(0);
                response.addCookie(removeCookie);
            }catch(Exception e2){ }
        }

        return loginUrl;
    }
}
