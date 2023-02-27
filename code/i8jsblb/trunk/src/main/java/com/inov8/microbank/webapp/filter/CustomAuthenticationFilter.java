/**
 *
 */
package com.inov8.microbank.webapp.filter;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.partnermodule.PartnerIpAddressDAO;
import com.inov8.microbank.server.dao.usergroupmodule.PartnerDAO;
import com.inov8.microbank.server.service.AllpayModule.AllpayRetailerAccountManager;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Project Name: microbank
 *
 * @author Imran Sarwar Creation Date: Jan 15, 2007 Creation Time: 2:33:17 PM
 *         Description:
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	private PartnerDAO partnerDAO = null;
	private PartnerIpAddressDAO partnerIpAddressDAO = null;
	private AppUserManager appUserManager=null;
	private RetailerContactManager retailerContactManager;
	private HandlerManager handlerManager;
	private AllpayRetailerAccountManager allpayRetailerAccountManager;
	private AccountControlManager accountControlManager;
	String loggedInUserType="";

	public CustomAuthenticationFilter()
	{
		super();
	}

	private Boolean validPortalAccess(HttpServletRequest request,AppUserModel user)
	{
		Boolean isValid = Boolean.TRUE;
		try{
			if(loggedInUserType.equals("6"))
			{
				if(!user.getAppUserTypeId().toString().equals(loggedInUserType))
					isValid = Boolean.FALSE;
				else if(user.getMnoId() != null && !user.getMnoId().equals(50027L))
					isValid = Boolean.FALSE;
			}
			else if(loggedInUserType.equals("7"))
			{
				if(!user.getAppUserTypeId().toString().equals(loggedInUserType))
					isValid = Boolean.FALSE;
				else if(user.getMnoId() != null && !user.getMnoId().equals(50028L))
					isValid = Boolean.FALSE;
			}
			else if(loggedInUserType.equals("3"))
			{
				if(user.getAppUserTypeId().toString().equals(UserTypeConstantsInterface.HANDLER.toString()))
				{
					loggedInUserType = UserTypeConstantsInterface.HANDLER.toString();
					Boolean isAgentWebEnabled = handlerManager.isAgentWebEnabled(user.getHandlerId());
					Long retailerContactId = user.getHandlerIdHandlerModel().getRetailerContactId();
					RetailerContactModel retailerContactModel = retailerContactManager.loadRetailerContactByRetailerContactId(retailerContactId);
					if(request.getHeader("referer").contains("awlogin") && isAgentWebEnabled && retailerContactModel.getIsAgentWebEnabled())
					{
						if(user.getMnoId() != null && !user.getMnoId().equals(50027L))
							isValid = Boolean.FALSE;
					}
					else if(request.getHeader("referer").contains("awscologin") && isAgentWebEnabled && retailerContactModel.getIsAgentWebEnabled())
					{
						if(user.getMnoId() != null && !user.getMnoId().equals(50028L))
							isValid = Boolean.FALSE;
					}
				}
				else{
					if(!user.getAppUserTypeId().toString().equals(loggedInUserType))
						isValid = Boolean.FALSE;
					if(request.getHeader("referer").contains("awlogin"))
					{
						if(user.getMnoId() != null && !user.getMnoId().equals(50027L))
							isValid = Boolean.FALSE;
					}
					else if(request.getHeader("referer").contains("awscologin"))
					{
						if(user.getMnoId() != null && !user.getMnoId().equals(50028L))
							isValid = Boolean.FALSE;
					}
				}
			}
		}
		catch (Exception ex)
		{
			isValid = Boolean.FALSE;
		}
		return isValid;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		String captcha_input =  request.getParameter("j_captcha");
		String captcha_answer =  request.getParameter("_captchaanswer");

		if(captcha_answer != null && !captcha_input.equals(captcha_answer))
		{
//			throw new BadCredentialsException("captcha does not match.");
		}
		HttpSession session=(HttpSession) request.getSession();
		loggedInUserType = (String)session.getAttribute("loggedInUserType");
		Cookie cookie = UserUtils.prepareCookieUserType(loggedInUserType);
		response.addCookie(cookie);
		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
		if( savedRequest != null )
		{
			session.setAttribute("TargetUrl", savedRequest.getRedirectUrl());
		}

		validateCSRFToken(request);

		////Check for Account Closed
		String username=request.getParameter("j_username");
		AppUserModel user=null;
		if(null!=username)
			user= appUserManager.getUserByUsername(username);
		else
			user= UserUtils.getCurrentUser();

		if(null != user  && (null!= user.getAccountClosedUnsettled())){
			if(user.getAccountClosedUnsettled())
				throw new BadCredentialsException("Your account has closed. Please contact your system administrator");
		}

		if(null != user && (null != loggedInUserType))
		{
			if(!validPortalAccess(request,user))
				throw new BadCredentialsException("You are not authorized to access this portal.");
			if(loggedInUserType.equals("3")|| loggedInUserType.equals("12"))
			{
				if((request.getHeader("referer").contains("awlogin")) || (request.getHeader("referer").contains("awscologin"))
						&& ( user.getAppUserTypeId().toString().equals("3") || user.getAppUserTypeId().toString().equals("12")))
				{
					
					if(user.getAppUserTypeId().toString().equals("3")){
						
					
						Long retailerContactId = user.getRetailerContactId();
						RetailerContactModel rtlrCntctModel = new RetailerContactModel();
						Boolean isEnabled = false;
						try {
							rtlrCntctModel= retailerContactManager.loadRetailerContactByRetailerContactId(retailerContactId);
							if(null != rtlrCntctModel){
								isEnabled = rtlrCntctModel.getIsAgentWebEnabled();
								if(isEnabled == false){
									throw new BadCredentialsException("Agent Web Portal is not enabled for you.");
								}
							}
							
						} catch (FrameworkCheckedException e) {
							
							logger.error("FrameWorkCheckedException while Loading RetailerContactByRetailerContactId :: " + e.getMessage() + " Exception :: " + e);
						}
					}else if(user.getAppUserTypeId().toString().equals("12")){
						
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						Long handlerId = user.getHandlerId();
						HandlerModel handlerModel = new HandlerModel();
						
						handlerModel.setHandlerId(handlerId);
						
						baseWrapper.setBasePersistableModel(handlerModel);
						Boolean isEnabled = false;
						try {
							
							handlerModel = (HandlerModel) handlerManager.loadHandler(baseWrapper).getBasePersistableModel();
							if(null != handlerModel){
								isEnabled = handlerModel.getIsAgentWebEnabled();
								if(isEnabled == false){
									throw new BadCredentialsException("Agent Web Portal is not enabled for you.");
								}
							}
						} catch (FrameworkCheckedException e) {
							// TODO Auto-generated catch block
							logger.error("Error while loading Handler in CustomAuthenticationFilter :: " + e.getMessage() + " Exception :: " + e);
						}
						
					}

				}
				else
					throw new BadCredentialsException("You are not authorized to access this portal");
			}
			/*if(!user.getAppUserTypeId().toString().equals(loggedInUserType))
			{
				throw new BadCredentialsException("You are not authorized to access this portal");
			}*/
		}

		//*********************************************************************************************
		//							General check if CNIC is blacklisted							 //
		//*********************************************************************************************
		Boolean blackListedflag = false;
		/*AppUserModel usr = null;
		usr= UserUtils.getCurrentUser();*/
		
		String cnic = user==null?null :user.getNic();
		if(cnic !=null){
			if(accountControlManager.isCnicBlacklisted(cnic)) {
				blackListedflag = true;
				throw new BadCredentialsException(MessageUtil.getMessage("LoginCommand.accountBlacklisted", new Object[]{cnic}));
			}
		}

		//**********************************************************************************************
		if(user != null && user.getAppUserTypeId() != null && user.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER){
			try {
				UserDeviceAccountsModel userDeviceAccountsModel = this.allpayRetailerAccountManager.getUserDeviceAccountsModel(String.valueOf(user.getAppUserId()));
				if(userDeviceAccountsModel != null && !userDeviceAccountsModel.getAccountEnabled()){
					throw new BadCredentialsException("Your account has been deactivated");
				}
				if(userDeviceAccountsModel != null && userDeviceAccountsModel.getAccountLocked()){
					throw new BadCredentialsException(MessageUtil.getMessage("LoginCommand.accountLocked"));
				}
			} catch (FrameworkCheckedException ex) {
				ex.printStackTrace();
			}

		}

		Authentication authentication = null;
		try {
			//Requesting ACEGI to authenticate current user
			authentication = super.attemptAuthentication(request,response);
			SecurityContextHolder.getContext().setAuthentication( authentication );
			session.setAttribute( UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username );
		}
		catch(AuthenticationException authenticationException) {
			if(authenticationException instanceof UsernameNotFoundException) {
				throw new BadCredentialsException("Bad credentials");
			}
			if(authenticationException instanceof BadCredentialsException) {
				//on N no. of attempts, expire his/her credentials by setting flag to true.
				//After that administrator has to reset his/her password as well as this flag
				try {
					checkLoginAttempts(request);
				}
				catch(AuthenticationException ae) {
					throw ae;
				}
			}
			if(authenticationException instanceof SessionAuthenticationException)
			{
				throw new BadCredentialsException("You are already logged in at a different location.");
			}
			throw authenticationException;
		}



		boolean isAccessAllowed = true;
		try {
			if(authentication != null) {
				//Recording the login time in db and reinitialize the loginAttempt counter on successfull authentication.
				try {
					AppUserModel appUser = null;
					Object principalObj = authentication.getPrincipal();
					if(principalObj instanceof AppUserModel){
						appUser = (AppUserModel)principalObj;
						Timestamp currentTime = new Timestamp(System.currentTimeMillis());
						currentTime.setNanos(0);
						appUser.setLastLoginAttemptTime(currentTime);
						appUser.setLastLoginTime(currentTime); // added by Turab
						appUser.setLoginAttemptCount(0);
						BaseWrapper baseWrapper = new BaseWrapperImpl();
						baseWrapper.setBasePersistableModel(appUser);
						appUserManager.saveOrUpdateAppUser(baseWrapper);
						cookie = UserUtils.prepareCookieUserType(appUser.getAppUserTypeId().toString());
						response.addCookie(cookie);
					}
				}
				catch(FrameworkCheckedException ex) {
					ex.printStackTrace();
				}
				//applying further security; for the time being its source ip level security
				isAccessAllowed = this.isAccessAllowedFromIP(request.getRemoteAddr(),authentication);
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		if(!isAccessAllowed) {
			SecurityContextHolder.getContext().setAuthentication(null);
			throw new BadCredentialsException("Requests from this IP is not allowed");
		}
		return authentication;
	}
	private void checkLoginAttempts(HttpServletRequest request) throws AuthenticationException {
		try {
			ReloadableResourceBundleMessageSource msgSrc = new ReloadableResourceBundleMessageSource();
			msgSrc.setBasename("portals");

			long allowedLoginAttemptsTime = 0;//in seconds
			long allowedLoginAttempts = 0;

			try {
				String allowedLoginAttemptsTimeObj = msgSrc.getMessage("allowedTimeOfLoginAttempts",null,null);
				if(allowedLoginAttemptsTimeObj != null) {
					allowedLoginAttemptsTime = Long.valueOf(allowedLoginAttemptsTimeObj);
				}

				String allowedLoginAttemptsObj = msgSrc.getMessage("allowedNoOfLoginAttempts",null,null);
				if(allowedLoginAttemptsObj != null) {
					allowedLoginAttempts = Long.valueOf(allowedLoginAttemptsObj);
				}
			}
			catch(NoSuchMessageException e) {
				//it shows there is no property set for login attempts so bypass this check
				return;
			}

			//Getting the User who is not authenticated
			String userName = (String)request.getParameter("j_username");
			AppUserModel appUser = appUserManager.getUserByUsername(userName);

			if(appUser.getCredentialsExpired().booleanValue()) {
				//if user's credentials has already been expired then simply throw exception to user
				throw new CredentialsExpiredException("User credentials have expired");
			}

			// checking if either login count or login attempt time threshold reaches
			boolean thresholdReached = false;

			Timestamp lastLoginAttemptTime = appUser.getLastLoginAttemptTime();
			Integer loginAttemptCount = appUser.getLoginAttemptCount();
			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			currentTime.setNanos(0);
			if(lastLoginAttemptTime == null) {
				lastLoginAttemptTime = currentTime;
			}
			if(loginAttemptCount == null) {
				loginAttemptCount = 0;
			}

			long firstAttemptTime = lastLoginAttemptTime.getTime();
			long currentAttemptTime = currentTime.getTime();

			if((currentAttemptTime - firstAttemptTime)/1000 <= allowedLoginAttemptsTime)
			{
				if(loginAttemptCount.intValue()+1 >= allowedLoginAttempts) {
					thresholdReached = true;
				}
			}
			else
			{
				//reset the counter
				appUser.setLoginAttemptCount(0);
				if(0 >= allowedLoginAttempts) {
					thresholdReached = true;
				}
			}

			//if user reaches the threshold level of login attempts then his/her credentials must expired.
			if(thresholdReached) {
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(appUser);
				appUser.setLastLoginAttemptTime(currentTime);
				appUser.setLoginAttemptCount(0);
				appUser.setCredentialsExpired(true);
				appUserManager.saveOrUpdateAppUser(baseWrapper);

				//Modifying at this stage the exception to credentials expired exception
				throw new BadCredentialsException("Maximum number of login retries reached");
			}
			else {
				//update the counter and loginAttempt time
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(appUser);
				appUser.setLastLoginAttemptTime(currentTime);
				Integer loginCount = appUser.getLoginAttemptCount();
				if(loginCount == null) {
					loginCount = 0;
				}
				appUser.setLoginAttemptCount(loginCount+1);
				appUserManager.saveOrUpdateAppUser(baseWrapper);
			}

		}
		catch(FrameworkCheckedException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response,
											 FilterChain chain, Authentication authResult ) throws IOException, ServletException
	{
		//Check for Retailer-RSO
	/*	AppUserModel user = (AppUserModel)authentication.getPrincipal();

			if(null!=user.getRetailerContactId()){
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				RetailerContactModel retailerContactModel=new RetailerContactModel();
				retailerContactModel.setRetailerContactId(user.getRetailerContactId());
				searchBaseWrapper.setBasePersistableModel(retailerContactModel);
				try {
					searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
				} catch (FrameworkCheckedException e) {
					e.printStackTrace();
					throw new IOException(e.getMessage());
				}
				retailerContactModel= (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
				if(null!=retailerContactModel && retailerContactModel.getRso()){
					throw new BadCredentialsException("Access Restricted !, Please contact your system administrator");
				}
			}
    	*/
		request.getSession(false).setAttribute(PortalConstants.KEY_LOGO, UserUtils.getImage());
		super.successfulAuthentication(request, response, chain, authResult);

		//Turab:Security:Duplicate Cookies issue handled
		String sessionId = request.getSession().getId();
		String remoteIp = UserUtils.getClientIpAddress(request);
		request.getSession(false).setAttribute(sessionId, remoteIp);
	}

	private boolean isAccessAllowedFromIP(String ip, Authentication authentication)
			throws AccessDeniedException
	{
		boolean isAccessAllowed=true;
		AppUserModel appUser = null;

		//Getting AppUserModel of successfully logged in user
		Object principalObj = authentication.getPrincipal();
		if(principalObj instanceof AppUserModel){
			appUser = (AppUserModel)principalObj;
		}

		//currently we are only applying security for Bank Users
		if(appUser != null && appUser.getAppUserTypeId().compareTo(UserTypeConstantsInterface.BANK) == 0)
		{
			//Getting partner id of bank
			Long bankId = appUser.getBankUserIdBankUserModel().getBankId();
			ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
			exampleHolder.setEnableLike(Boolean.FALSE);
			PartnerModel tmpPartnerModel = new PartnerModel();
			tmpPartnerModel.setBankId(bankId);
			tmpPartnerModel.setAppUserTypeId(UserTypeConstantsInterface.BANK);
			CustomList<PartnerModel> partnerModelList = partnerDAO.findByExample(tmpPartnerModel,null,null,exampleHolder);
			tmpPartnerModel = partnerModelList.getResultsetList().get(0);
			Long partnerId = tmpPartnerModel.getPartnerId();

			//Now get IP Addresses List of this partner
			PartnerIpAddressModel tmpPartnerIpAddressModel = new PartnerIpAddressModel();
			tmpPartnerIpAddressModel.setPartnerId(partnerId);
			CustomList<PartnerIpAddressModel> partnerIpAddressModelList = partnerIpAddressDAO.findByExample(tmpPartnerIpAddressModel,null,null,exampleHolder);

			//Now applying ip level security if any
			if(partnerIpAddressModelList != null) {
				List<PartnerIpAddressModel> partnerIpsList = partnerIpAddressModelList.getResultsetList();
				if(partnerIpsList != null && partnerIpsList.size() > 0) {
					isAccessAllowed=false;//Now, client must conform to the ip list

					for(PartnerIpAddressModel partnerIp : partnerIpsList) {
						String subnetMask = partnerIp.getSubnetMask();
						String allowedNetwork = partnerIp.getNetwork();
						String[] ipOctets = ip.split("\\.");
						String[] subnetMaskOctets = subnetMask.split("\\.");
						String clientNetwork = "";

						//subnetMask and ip both have same no. of octets
						for(int i=0; i < subnetMaskOctets.length; i++) {
							int octetValue = Integer.parseInt(subnetMaskOctets[i]);
							octetValue =(int) (octetValue & Integer.parseInt(ipOctets[i]));
							clientNetwork =clientNetwork + octetValue;
							if(i < subnetMaskOctets.length-1) {
								clientNetwork += ".";
							}
						}
						if(allowedNetwork.equals(clientNetwork)) {
							isAccessAllowed=true;
							break;
						}
					}
				}
			}
		}

		return isAccessAllowed;
	}

	private void validateCSRFToken(HttpServletRequest request) throws AuthenticationException {
		String tokehHiddenValue = StringUtils.defaultIfEmpty(request.getParameter("_csrfckinput"),"");
		logger.info("Validating Login Request against possible CSRF attacks. Token: " + tokehHiddenValue);
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for (Cookie cookie : cookies ) {
				String cookieName = cookie.getName();
				if (cookieName.equals("_csrfcookie")){
					String cookieToken = StringUtils.defaultIfEmpty(cookie.getValue(), "");
					boolean sameToken = cookieToken.equals(tokehHiddenValue);
					logger.info("\nCookie Token: " + cookieToken + "\nRequest Token:" + tokehHiddenValue);
					if (! cookieToken.equals(tokehHiddenValue)){
						SecurityContextHolder.getContext().setAuthentication(null);
						throw new BadCredentialsException("Invalid login details");
					}
				}
			}
		}
	}

	public void setPartnerDAO(PartnerDAO partnerDAO) {
		this.partnerDAO = partnerDAO;
	}
	public void setPartnerIpAddressDAO(PartnerIpAddressDAO partnerIpAddressDAO) {
		this.partnerIpAddressDAO = partnerIpAddressDAO;
	}
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}
	public void setAllpayRetailerAccountManager(
			AllpayRetailerAccountManager allpayRetailerAccountManager) {
		this.allpayRetailerAccountManager = allpayRetailerAccountManager;
	}
	public void setAccountControlManager(AccountControlManager accountControlManager) {
		this.accountControlManager = accountControlManager;
	}

	
	
	public void setHandlerManager(HandlerManager handlerManager) {
		this.handlerManager = handlerManager;
	}

}
