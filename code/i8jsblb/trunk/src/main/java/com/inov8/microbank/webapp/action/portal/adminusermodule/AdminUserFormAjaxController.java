package com.inov8.microbank.webapp.action.portal.adminusermodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Forgot Verifly Change Pin</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Basit Mehr
 * @author imran.sarwar
 * @version 1.0
 */
public class AdminUserFormAjaxController extends AjaxController
 {
	private UserManagementManager	userManagementManager;

	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		
		//getting parameters from request
		Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request,"appUserId")));
		String notifyViaSMS = ServletRequestUtils.getStringParameter(request,PortalConstants.KEY_NOTIFY_BY_SMS);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_NOTIFY_BY_SMS, notifyViaSMS);

		baseWrapper.putObject("appUserId", appUserId);
			
		if(notifyViaSMS != null && notifyViaSMS.equals("true")){
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.RESET_AGENT_PASSWORD_PORTAL_USECASE_ID);
			baseWrapper = this.userManagementManager.changePasswordBySMSEmail(baseWrapper);
		}else{ // via email
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.PG_ADMIN_RESET_PASSWORD_USECASE_ID);
			baseWrapper = this.userManagementManager.changePasswordBySMSEmail(baseWrapper);
		}
			
		String errorMessage = baseWrapper.getObject("errorMessage")!=null?baseWrapper.getObject("errorMessage").toString():"";
		
		if(errorMessage.trim().length() > 0)
		{
			buffer.append(errorMessage);				
		}
		else
		{
			if(notifyViaSMS != null && notifyViaSMS.equals("true")){
				buffer.append("Password is changed and SMS is sent to User.");
			}else{
				//buffer.append(getMessageSourceAccessor().getMessage("forgotveriflypinmodule.new.pin.genrated", request.getLocale()));
				buffer.append("Password is changed and Email is sent to User.");
			}
		}
		
		return buffer.toString();
	}


	/**
	 * @param userManagementManager the userManagementManager to set
	 */
	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}



	/*	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//getting parameters from request
		Long appUserId = ServletRequestUtils.getLongParameter(request,"appUserId");
		Long smartMoneyAccountId = ServletRequestUtils.getLongParameter(request,"smartMoneyAccountId");

		//getting log information from the request
		Long useCaseId = ServletRequestUtils.getLongParameter(request,PortalConstants.KEY_USECASE_ID);
		Long actionId = ServletRequestUtils.getLongParameter(request,PortalConstants.KEY_ACTION_ID);		

		//putting log information into wrapper for further used
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject("appUserId", appUserId);
		baseWrapper.putObject("smartMoneyAccountId", smartMoneyAccountId);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
			
		this.forgotVeriflyPinManager.changeVeriflyPin(baseWrapper);
			
		String errorMessage = baseWrapper.getObject("errorMessage")!=null?baseWrapper.getObject("errorMessage").toString():"";
	
		if(errorMessage.trim().length() > 0){
			ControllerUtils.saveMessage(request, errorMessage);				
		}else{
			ControllerUtils.saveMessage(request, getMessageSourceAccessor().getMessage("forgotveriflypinmodule.new.pin.genrated", request.getLocale()));
		}
		
		return new ModelAndView(new RedirectView("p_forgotveriflypinmanagement.html"));
		
	}
*/

}
