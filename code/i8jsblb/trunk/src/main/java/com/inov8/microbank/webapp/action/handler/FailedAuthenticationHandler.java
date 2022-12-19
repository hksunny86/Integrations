package com.inov8.microbank.webapp.action.handler;

import com.inov8.microbank.common.util.UserUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Abu Turab 25-10-2017
 */
public class FailedAuthenticationHandler extends SimpleUrlAuthenticationFailureHandler
{
    public FailedAuthenticationHandler(){
        super();
    }
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
    {
        logger.error(exception);
        //Turab:Security:User enumeration is handled
        if(exception instanceof UsernameNotFoundException)
            this.saveException(request, new AuthenticationCredentialsNotFoundException("Bad Credentials"));
        else
            this.saveException(request, exception);

        String userType="";
        String loggedInUserType = (String)request.getSession().getAttribute("loggedInUserType");
        if(null != loggedInUserType && !"".equals(loggedInUserType))
        {
            userType = loggedInUserType;
        }
        else
        {
            userType = UserUtils.getCookieValue("loggedInUserType", request);
        }

        if(userType != null && !userType.equals(""))
        {
            if(userType.equals("3"))
            {
                if((request.getHeader("referer").contains("awlogin")))
                    this.redirectStrategy.sendRedirect(request, response,"/awlogin.jsp?error=true");
                else
                    this.redirectStrategy.sendRedirect(request, response,"/awscologin.jsp?error=true");
            }
            else if(userType.equals("7"))
                this.redirectStrategy.sendRedirect(request, response,"/scologin.jsp?error=true");
            else
                this.redirectStrategy.sendRedirect(request, response,"/login.jsp?error=true");

        }
        else
        {
            this.redirectStrategy.sendRedirect(request, response,"/login.jsp?error=true");
        }

    }
}
