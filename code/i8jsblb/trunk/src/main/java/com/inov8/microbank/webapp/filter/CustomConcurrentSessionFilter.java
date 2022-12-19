package com.inov8.microbank.webapp.filter;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.UserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Abu Turab 25-10-2017
 */
public class CustomConcurrentSessionFilter extends GenericFilterBean
{
	private SessionRegistry sessionRegistry;
	private String expiredUrl;
	private LogoutHandler[] handlers;
	private RedirectStrategy redirectStrategy;

	/** @deprecated */
	public CustomConcurrentSessionFilter() {
		this.handlers = new LogoutHandler[]{new SecurityContextLogoutHandler()};
		this.redirectStrategy = new DefaultRedirectStrategy();
	}

	public CustomConcurrentSessionFilter(SessionRegistry sessionRegistry) {
		this(sessionRegistry, (String)null);
	}

	public CustomConcurrentSessionFilter(SessionRegistry sessionRegistry, String expiredUrl) {
		this.handlers = new LogoutHandler[]{new SecurityContextLogoutHandler()};
		this.redirectStrategy = new DefaultRedirectStrategy();
		this.sessionRegistry = sessionRegistry;
		this.expiredUrl = expiredUrl;
	}

	public void afterPropertiesSet() {
		Assert.notNull(this.sessionRegistry, "SessionRegistry required");
		Assert.isTrue(this.expiredUrl == null || UrlUtils.isValidRedirectUrl(this.expiredUrl), this.expiredUrl + " isn't a valid redirect URL");
	}

	private Boolean validateRequestIpAndUser(ServletRequest request, AppUserModel appUserModel)
	{
		Boolean isValidReq = Boolean.TRUE;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String currentSessionId = httpServletRequest.getSession().getId();
		String remoteIpAddress = UserUtils.getClientIpAddress(httpServletRequest);
		String sessionIp = (String) httpServletRequest.getSession().getAttribute(currentSessionId);
		//sessionIp = "58.27.201.77";
		Boolean isIpValid = Boolean.TRUE;
		if(!remoteIpAddress.equals(sessionIp))
			isIpValid = Boolean.FALSE;

		Boolean isAppUser = Boolean.TRUE;
		String sessionUserName = (String) httpServletRequest.getSession(false).getAttribute("j_username");
		String requestedUserName = appUserModel.getUsername();

		if((!isAppUser || !isIpValid ) || !sessionUserName.equals(requestedUserName))
			isValidReq = Boolean.FALSE;

		return isValidReq;
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			HttpSession session = request.getSession(false);
			if (session != null) {
				SessionInformation info = this.sessionRegistry.getSessionInformation(session.getId());
				if (info != null) {
					Object obj = info.getPrincipal();
					AppUserModel appUserModel = null;
					if (obj instanceof AppUserModel)
						appUserModel = (AppUserModel) obj;
					if (info.isExpired() || (appUserModel != null && !validateRequestIpAndUser(req, appUserModel))) {
						this.doLogout(request, response);
						String targetUrl = this.determineExpiredUrl(request, info);
						if (targetUrl != null) {
							this.redirectStrategy.sendRedirect(request, response, targetUrl);
							return;
						}

						response.getWriter().print("This session has been expired (possibly due to multiple concurrent logins being attempted as the same user).");
						response.flushBuffer();
						return;
					}

					this.sessionRegistry.refreshLastRequest(info.getSessionId());
				}
			}

			chain.doFilter(request, response);
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	protected String determineExpiredUrl(HttpServletRequest request, SessionInformation info) {

		String expiredUrl="/login.jsp?logoutReason=concurrentlogin";

		String loggedInUserType = (String)request.getSession().getAttribute("loggedInUserType");
		if(loggedInUserType != null && !"".equals(loggedInUserType))
		{
			if(loggedInUserType.equals("3"))
			{
				expiredUrl = "/awlogin.jsp?logoutReason=concurrentlogin";
			}

			if(loggedInUserType.equals("7"))
			{
				expiredUrl = "/scologin.jsp?logoutReason=concurrentlogin";
			}
		}
		else
		{
			Cookie[] cookies = request.getCookies();
			if(null != cookies) {
				loggedInUserType = UserUtils.getCookieValue("loggedInUserType", request);
			}

			if(loggedInUserType.equals("3"))
			{
				expiredUrl = "/awlogin.jsp?logoutReason=concurrentlogin";
			}
			if(loggedInUserType.equals("7"))
			{
				expiredUrl = "/scologin.jsp?logoutReason=concurrentlogin";
			}

		}

		this.expiredUrl = expiredUrl;

		return this.expiredUrl;
	}

	private void doLogout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LogoutHandler[] arr$ = this.handlers;
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			LogoutHandler handler = arr$[i$];
			handler.logout(request, response, auth);
		}

	}

	/** @deprecated */
	@Deprecated
	public void setExpiredUrl(String expiredUrl) {
		this.expiredUrl = expiredUrl;
	}

	/** @deprecated */
	@Deprecated
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public void setLogoutHandlers(LogoutHandler[] handlers) {
		Assert.notNull(handlers);
		this.handlers = handlers;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}
