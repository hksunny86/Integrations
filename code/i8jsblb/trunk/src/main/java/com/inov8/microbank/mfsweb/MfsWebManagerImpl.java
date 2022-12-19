package com.inov8.microbank.mfsweb;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			April 17, 2009  			
 * Description:				
 */


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.util.XMLUtil;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MfsRequestWrapper;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAccountInfo;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.mfs.MfsRequestHandler;
import com.inov8.microbank.mfs.jme.messaging.parser.MfsWebResponseParser;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebConstant;

public class MfsWebManagerImpl implements MfsWebManager	
{
	/** Logger for this class */
	protected final Log	logger	= LogFactory.getLog(getClass());

	private MfsRequestHandler	requestHandler	= null;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator = null ;
	private AppUserManager appUserManager;
	
	
	
	
	/**
	 * @param request
	 * @param commandId
	 * @return
	 */
	private String getRequestXML(HttpServletRequest request, String commandId) {
		System.out.println();
		String requestXML = (String) request.getAttribute(AllPayWebConstant.EXTERNAL_XML.getValue());
		
		if(requestXML == null || AllPayWebConstant.BLANK_SPACE.getValue().equals(requestXML)) {
			
			requestXML = MfsWebUtil.buildRequestXML(request, commandId);	
			
		} else {
			
			request.removeAttribute(AllPayWebConstant.EXTERNAL_XML.getValue());	
		}		
		
		return requestXML;
	}
	
	
	
	public void setAppUserManager(AppUserManager appUserManager)
	{
		this.appUserManager = appUserManager;
	}

	public String handleRequest(HttpServletRequest request, String commandId) throws Exception
	{
		return this.handleRequest(request, commandId, true);		
	}
	

	
	public String handleRequest(HttpServletRequest request, String commandId, boolean removeFromThreadLocal) throws Exception
	{
//		String commandId = request.getParameter("id");
		
		logger.info("----------- MWallet WEB Controller called -----------------------");
		ThreadLocalAppUser.getAppUserModel();
		MfsRequestWrapper sessionWrapper = (MfsRequestWrapper) request.getSession(false).getAttribute(MfsRequestWrapper.KEY_MFS_REQUEST_MAP);
		if(null == sessionWrapper)
		{
			if(logger.isDebugEnabled())
				logger.debug(">>>>>>>>> SessionWrapper is null, creating and storing in HttpSession");
			sessionWrapper = new MfsRequestWrapper();
			request.getSession(false).setAttribute(MfsRequestWrapper.KEY_MFS_REQUEST_MAP, sessionWrapper);
		}
		
		String xml = getRequestXML(request, commandId);	
		
		logger.debug("[MfsWebManagerImpl.handleRequest] Request XML : " + xml );
		
		
		// if xml is null or empty then fall back to get the data from servlet strem directly
		if(xml == null || "".equals(xml))
		{
			logger.info("Request Parameter not found. Trying to read data from the servlet stream");
		}
		
		if(logger.isDebugEnabled())
			logger.debug("Request: XML" + XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogInputXMLLocationSteps));

		try
		{
			xml = requestHandler.handleRequest(xml, sessionWrapper);
			
			logger.debug("[MfsWebManamgerImpl.handleRequest] Request XML : " + xml );
			
			
			new MfsWebResponseParser().parse(xml, request) ;
			
			
//			// In case of Login Command, add the Bank information in request parameter
//			if( commandId.equals(CommandFieldConstants.CMD_MFS_LOGIN) )
//				mfsWebResponseDataPopulator.populateBankInfo(request, xml);
//			

			if(logger.isDebugEnabled())
				logger.debug("Response: XML" + XMLUtil.replaceElementsUsingXPath(xml, XPathConstants.actionLogOutputXMLLocationSteps));
			
			 
			// For login only when this call returns AppUserModel should be in
			// the ThreadLocal just put it on session
			AppUserModel userModel = ThreadLocalAppUser.getAppUserModel();
			UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();	
			if (userModel != null)
			{
				HttpSession session = request.getSession(true);
				session.setAttribute(CommandFieldConstants.KEY_APP_USER, userModel);
				if(userDeviceAccountsModel != null)
				{
					session.setAttribute(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNTS_MODEL, userDeviceAccountsModel);
				}
			}
//			writeResponse(response, xml);
		}
		finally
		{
			if( removeFromThreadLocal )
			{
//				logger.debug("[MfsWebManagerImpl] Going to Remove ThreadLocalAccountInfo objects.");
				// we are done now remove the appUserModel from ThreadLocal
				ThreadLocalAppUser.remove();
				ThreadLocalActionLog.remove();
				ThreadLocalUserDeviceAccounts.remove();
				ThreadLocalAccountInfo.remove();
			}
		}
		return xml;
	}
	
	
	public void logoutUser( HttpServletRequest request )
	{
		if(request.getSession(false) != null)
		{
			request.getSession(false).invalidate();
		}
	}
	
	
	public void getBankInfoFromLoginXML( HttpServletRequest request, String loginXML )
	{
		mfsWebResponseDataPopulator.populateBankInfo(request, loginXML);		
	}
	
//	public void getAccountsByBankId( HttpServletRequest request, String loginXML )
//	{
//		Long bankId = Long.parseLong(request.getParameter("bankId"));
//		
//		mfsWebResponseDataPopulator.populateAccountsByBankId(request, loginXML, bankId);
//		
//		
//	}
	
	public void getSuppliersInfo( HttpServletRequest request, String loginXML )
	{
		mfsWebResponseDataPopulator.populateSuppliersInfo(request, loginXML);		
	}
	
	
	public void getServicesInfo( HttpServletRequest request, String loginXML )
	{
		mfsWebResponseDataPopulator.populateServicesInfo(request, loginXML);		
	}
	

	public MfsRequestHandler getRequestHandler()
	{
		return requestHandler;
	}

	public void setRequestHandler(MfsRequestHandler requestHandler)
	{
		this.requestHandler = requestHandler;
	}


	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator)
	{
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}

}