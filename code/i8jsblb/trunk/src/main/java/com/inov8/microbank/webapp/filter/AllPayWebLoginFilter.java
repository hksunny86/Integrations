
package com.inov8.microbank.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.webapp.action.allpayweb.LoginController;

/**
 * @author Jawwad Farooq
 * Creation Time: May 6, 2006 15:02:18 PM
 */
public class AllPayWebLoginFilter implements Filter {

	protected final Log logger = LogFactory.getLog(getClass());
	
	public void destroy() {
		logger.info("AllPayWebLoginFilter.destroy");
	}

	
	public void doFilter(ServletRequest servletRequest, ServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		
		AppUserModel appUserModel = (AppUserModel) httpServletRequest.getSession(Boolean.TRUE).getAttribute(CommandFieldConstants.KEY_APP_USER);
		
		httpServletRequest.setAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB);
		
		Integer STATUS_CASE = new LoginController().isValidRequest(httpServletRequest);
		
		if((appUserModel == null || STATUS_CASE.intValue() == 3) && !httpServletRequest.getRequestURI().endsWith("agentweblogin.aw")) {
			
			//httpServletRequest.getRequestDispatcher("awlogin.gw").forward(httpServletRequest, httpServletResponse);
			httpServletRequest.getRequestDispatcher("/awlogin.html").forward(httpServletRequest, httpServletResponse);
			return ;
		}
		
		filterChain.doFilter(httpServletRequest, httpServletResponse);	 
	}

	
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("AllPayWebLoginFilter.init");
	}

}
