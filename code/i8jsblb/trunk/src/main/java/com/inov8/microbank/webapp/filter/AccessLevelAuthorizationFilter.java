/**
 * 
 */
package com.inov8.microbank.webapp.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;

/**
 * Project Name: microbank
 * @author Imran Sarwar 
 * Creation Date: Jun 12, 2007 
 * Creation Time: 12:11:12 PM 
 * Description: Filters request for AccessLevelAuthorization, Look for the current action in the request 
 */
public class AccessLevelAuthorizationFilter implements Filter, Serializable
{

	private static final Log logger = LogFactory.getLog(AccessLevelAuthorizationFilter.class);

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1182713604974603028L;
	
	/**
	 * If true then if the action parameter is not in the request even then allow access. If it is set to false
	 * then it will check for the presence of action parameter and determine the access level authorization.
	 * Defatult value is false. You can set this parameter in web.xml or in security.xml if the filter is defined 
	 * in acegi context 
	 */
	private boolean doAllowMissingAction = false;

	public AccessLevelAuthorizationFilter()
	{
		super();
		logger.info("<<<<< AccessLevel Authorization Filter initialized... >>>>>");
	}

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException
	{
		boolean isAllowed = false;
		logger.info("<<< Filtering request to determine the Access level of the current User >>>");
		String currentActionParam = request.getParameter(PortalConstants.KEY_ACTION_ID);
		Long currentAction = null;
		
		if(isDoAllowMissingAction() && StringUtil.isNullOrEmpty(currentActionParam))
		{
		    logger.info("<<< Allowing request with verification as doAllowMissingAction=true >>>");
			isAllowed = true;
		}
		else
		{
			try
			{
				if(currentActionParam != null) 
				{
					currentAction = Long.parseLong(currentActionParam);
				}
				
				logger.info("<<< Current Action: "+ UserUtils.getActionName(currentAction));

				// logger.info("<<< Filtering request to determine the Access level of the current User
				// >>>");
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if (auth != null)
				{
					// logger.info("<<< Current User: "+ auth.getName() + ">>>");
//					System.out.println("<<< Current User: " + auth.getName() + ">>>");
//					System.out.println("<<< Current Class: " + auth.getClass() + ">>>");
					AppUserModel user = null;
					Object principal = auth.getPrincipal();
//					System.out.println("<<< Current Principal: " + principal + ">>>");
					if (principal instanceof AppUserModel)
					{
						user = (AppUserModel) principal;
						if (user != null)
						{
							//isAllowed = UserUtils.isAccessAllowed(currentAction, user);
						    isAllowed = true;
						    logger.info("<<< Allow Request: "+ isAllowed);
	/*
							Set<AppUserAppRoleModel> set = user.getAppUserAppRoleModels();
							System.out.println("<<< Size of the list: " + set.size() + ">>>");
							Iterator<AppUserAppRoleModel> iter = set.iterator();
							while (iter.hasNext())
							{
								AppUserAppRoleModel appUserAppRole = (AppUserAppRoleModel) iter.next();
								System.out.println("\n----------------------\n Role: "
										+ appUserAppRole.getAppRoleModel().getName() + "\n User Name: "
										+ appUserAppRole.getAppUserModel().getUsername() + "\n Access Level: "
										+ appUserAppRole.getAccessLevelModel().getAccessLevelName()
										+ "\n-------------------------");
							}
	*/
						}
					}else
						isAllowed = true;
						
				}
			}
			catch (NumberFormatException e) 
			{
			    logger.error("<<< Action Param is not a valid number: ", e);
			}
			catch(Exception ex)
			{
			    logger.error( ex.getMessage(), ex );
			}
		}

		if(isAllowed)
			filterChain.doFilter(request, response);
		else
			throw new AccessDeniedException("Access to the required resource is not granted");
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
		doAllowMissingAction = Boolean.parseBoolean(filterConfig.getInitParameter("doAllowMissingAction"));
	}

	/**
	 * @return the doAllowMissingAction
	 */
	public boolean isDoAllowMissingAction()
	{
		return doAllowMissingAction;
	}

	/**
	 * @param doAllowMissingAction the doAllowMissingAction to set
	 */
	public void setDoAllowMissingAction(boolean doAllowMissingAction)
	{
		this.doAllowMissingAction = doAllowMissingAction;
		System.out.println("<<< doAllowMissingAction = "+ this.doAllowMissingAction +" >>>");
	}

}
