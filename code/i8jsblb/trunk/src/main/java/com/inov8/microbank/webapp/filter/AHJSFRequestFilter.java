package com.inov8.microbank.webapp.filter;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.util.Constants;
import com.inov8.microbank.common.util.JSFContext;
import com.inov8.microbank.common.util.SessionBeanObjects;

public class AHJSFRequestFilter implements Filter {
	private static final Log logger= LogFactory.getLog(AHJSFRequestFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request=(HttpServletRequest)req;
		HttpServletResponse response=(HttpServletResponse)res;
		HttpSession session= request.getSession();
		
		if(session.isNew())
		{
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			logger.info("Session expired");
			return;
		}
		
		if(session.getAttribute("TargetUrl") != null && !session.getAttribute("TargetUrl").equals(""))
		{
			String uri = (String)session.getAttribute("TargetUrl");
			if(uri.endsWith("/addRegion.jsf") || uri.endsWith("addAreaLevel.jsf") || uri.endsWith("addAgentLevel.jsf") || uri.endsWith("addAreaName.jsf"))
			{
				response.sendRedirect(request.getContextPath()+"/home.html");
			}
			session.removeAttribute("TargetUrl");
		}
		
		if(request.getParameter("postBack") != null)		
		{
			SessionBeanObjects.removeAllSessionObjects(request);	
		}
		
		chain.doFilter(req, res);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
