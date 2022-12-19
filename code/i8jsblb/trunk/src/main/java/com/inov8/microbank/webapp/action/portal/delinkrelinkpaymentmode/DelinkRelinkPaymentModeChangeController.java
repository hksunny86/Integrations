package com.inov8.microbank.webapp.action.portal.delinkrelinkpaymentmode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.microbank.server.service.portal.delinkrelinkpaymentmode.DelinkRelinkPaymentModeManager;

/**
 * @deprecated - This has been deprecated in favor of com.inov8.microbank.webapp.action.portal.delinkrelinkpaymentmode.DelinkRelinkPaymentModeChangeAjaxController 
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Feb 6, 2007
 * Creation Time: 			5:16:54 PM
 * Description:
 */
 
public class DelinkRelinkPaymentModeChangeController extends AbstractController {
	
	private DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/*
		//getting parameters from request
		Long appUserId = ServletRequestUtils.getLongParameter(request,"appUserId");
		Long smartMoneyAccountId = ServletRequestUtils.getLongParameter(request,"smartMoneyAccountId");
		String linkMode = ServletRequestUtils.getStringParameter(request,"linkMode");

		//getting log information from the request
		Long useCaseId = ServletRequestUtils.getLongParameter(request,PortalConstants.KEY_USECASE_ID);
		Long actionId = ServletRequestUtils.getLongParameter(request,PortalConstants.KEY_ACTION_ID);		

		//putting log information into wrapper for further used
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject("appUserId", appUserId);
		baseWrapper.putObject("smartMoneyAccountId", smartMoneyAccountId);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
		baseWrapper.putObject("linkMode", linkMode);
		
		
			
		this.delinkRelinkPaymentModeManager.delinkRelinkVeriflyPin(baseWrapper);
			
		String errorMessage = baseWrapper.getObject("errorMessage")!=null?baseWrapper.getObject("errorMessage").toString():"";
		
		if(errorMessage.trim().length() > 0){
			ControllerUtils.saveMessage(request, errorMessage);				
		}else{
			if(linkMode.equals("reLink") ){
				ControllerUtils.saveMessage(request, "Account has been activate successfully");
			}else{
				ControllerUtils.saveMessage(request, "Account has been deactivate successfully");
			}
		}
		*/
		return new ModelAndView(new RedirectView("p_delinkrelinkpaymentmodemanagement.html"));
		
	}

	
	
	
	public void setDelinkRelinkPaymentModeManager(
			DelinkRelinkPaymentModeManager delinkRelinkPaymentModeManager) {
		this.delinkRelinkPaymentModeManager = delinkRelinkPaymentModeManager;
	}
}
