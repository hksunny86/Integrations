package com.inov8.microbank.webapp.filter;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ForceChangePasswordFilter implements Filter{

	private AppUserManager appUserManager ;
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
						
		AppUserModel appUser = null;
		
		if(UserUtils.getCurrentUser() != null) {
			appUser = appUserManager.getUser(UserUtils.getCurrentUser().getAppUserId().toString());
		}
		
			if((appUser != null && appUser.getPasswordChangeRequired() != null 
					&& appUser.getPasswordChangeRequired().booleanValue()==true) && (appUser.getAppUserTypeId().longValue() != 3L &&  appUser.getAppUserTypeId().longValue() != 12L)) {
				List messages = new ArrayList();
				String message = "You need to change your password before proceeding";
				messages.add(message);
				//we added following check to remove cyclic call issue of changepasswordform
				if(!((HttpServletRequest)request).getRequestURI().contains("changepasswordform")
						&& !((HttpServletRequest)request).getRequestURI().contains("validator")) {				
					((HttpServletRequest)request).getSession().setAttribute("messages",messages );
					((HttpServletRequest)request).getRequestDispatcher("changepasswordform.html").forward(request, response);
				}
				else if(((HttpServletRequest)request).getRequestURI().contains("changepasswordform") ||
						((HttpServletRequest)request).getRequestURI().contains("validator")) {
					filterChain.doFilter(request, response);			
					appUser = appUserManager.getUser(UserUtils.getCurrentUser().getAppUserId().toString());
					if(appUser != null && appUser.getPasswordChangeRequired() != null 
							&& appUser.getPasswordChangeRequired().booleanValue()==true) {
						((HttpServletRequest)request).getSession().setAttribute("messages",messages );
					}	
					else {
						Object messagesObj = ((HttpServletRequest)request).getSession().getAttribute("messages" );
						if(messagesObj != null) {
							List list = (List)messagesObj;
							list.remove(message);
						}
					}
				}
				else {
					filterChain.doFilter(request, response);
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}
