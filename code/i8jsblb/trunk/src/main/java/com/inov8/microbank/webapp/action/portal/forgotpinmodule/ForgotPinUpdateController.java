package com.inov8.microbank.webapp.action.portal.forgotpinmodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.portal.forgotpinmodule.ForgotpinManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;


/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Feb 15, 2007
 * Creation Time: 			1:59:01 PM
 * Description:				
 */
public class ForgotPinUpdateController extends AjaxController
{
	  private ForgotpinManager forgotpinManager;

	  
		@Override
		public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			
			Long id = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "appUserId")));
			
			StringBuffer buffer = new StringBuffer();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			UserDeviceAccountsModel userDeviceModel = new UserDeviceAccountsModel();
			userDeviceModel.setAppUserId(id);
			baseWrapper.setBasePersistableModel(userDeviceModel);
//	    	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, new Long(request.getParameter(PortalConstants.KEY_ACTION_ID)));
	    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(request.getParameter(PortalConstants.KEY_USECASE_ID)));
	    	baseWrapper=this.forgotpinManager.updateForgotPin(baseWrapper);
	    	buffer.append(getMessageSourceAccessor().getMessage("forgotpinmodule.new.pin.genrated", request.getLocale()));
			return buffer.toString();
		}

	  
	  
/*	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		Long flagforgotpin=ServletRequestUtils.getLongParameter(request,"forgotpinflag");		
		Long id = ServletRequestUtils.getLongParameter(request, "appUserId");
		
		
		
		if (null != id)
	    {
	    	
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			UserDeviceAccountsModel userDeviceModel = new UserDeviceAccountsModel();
			userDeviceModel.setAppUserId(id);
			baseWrapper.setBasePersistableModel(userDeviceModel);
	    	baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, new Long(request.getParameter(PortalConstants.KEY_ACTION_ID)));
	    	baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(request.getParameter(PortalConstants.KEY_USECASE_ID)));
	    	//baseWrapper.putObject("appUserId",id);
	    	baseWrapper=this.forgotpinManager.updateForgotPin(baseWrapper);
	    	    		    	    	
	    	ControllerUtils.saveMessage(request, getMessageSourceAccessor().getMessage("forgotpinmodule.new.pin.genrated", request.getLocale()));
	    	
	    	if(null != flagforgotpin)
	    	{	
	    		return new ModelAndView(new RedirectView("p_pgforgotpin.html"));
	    	}
	    	return new ModelAndView(new RedirectView("p_mnoforgotpin.html"));
	    
	    }
	     
	    	return new ModelAndView(new RedirectView("p_mnoforgotpin.html")); 
	}
*/
	

	public void setForgotpinManager(ForgotpinManager forgotpinManager) {
		this.forgotpinManager = forgotpinManager;
	}

	
	

	
	
}
 