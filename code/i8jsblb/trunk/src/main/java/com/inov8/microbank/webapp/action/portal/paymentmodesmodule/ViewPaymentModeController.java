package com.inov8.microbank.webapp.action.portal.paymentmodesmodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.server.service.portal.paymentmodesmodule.ViewPaymentModeManager;


public class ViewPaymentModeController extends AbstractController
{
	  private ViewPaymentModeManager viewPaymentModesManager;

	  
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
    	SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    	
    	searchBaseWrapper=this.viewPaymentModesManager.viewAvailablePaymentModeList(searchBaseWrapper);
    	
    	return new ModelAndView("p_availablepaymentmodes", "paymentModeList", searchBaseWrapper.getCustomList().getResultsetList());
    	
	
	}


	public void setViewPaymentModesManager(	ViewPaymentModeManager viewPaymentModeManager) {
		this.viewPaymentModesManager = viewPaymentModeManager;
	}
	



}
 