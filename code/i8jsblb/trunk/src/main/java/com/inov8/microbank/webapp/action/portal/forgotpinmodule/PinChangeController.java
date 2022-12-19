package com.inov8.microbank.webapp.action.portal.forgotpinmodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.IvrAuthenticationRequestQueue;
import com.inov8.microbank.server.service.portal.forgotpinmodule.ForgotpinManager;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import com.inov8.microbank.webapp.action.ajax.AjaxController;


/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Feb 15, 2007
 * Creation Time: 			1:59:01 PM
 * Description:				
 */
public class PinChangeController extends AjaxController
{
	  private ForgotpinManager forgotpinManager;
	  private IvrAuthenticationRequestQueue ivrAuthenticationRequestQueueSender;

	  
		@Override
		public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			
			String mobileNo = ServletRequestUtils.getStringParameter(request, "mobileNo");
			
			
			IvrRequestDTO ivrDTO = new IvrRequestDTO();
			ivrDTO.setCustomerMobileNo(mobileNo);
			ivrDTO.setRetryCount(0);
			ivrDTO.setProductId(new Long(CommandFieldConstants.CMD_CHANGE_PIN_IVR));
			ivrAuthenticationRequestQueueSender.sentAuthenticationRequest(ivrDTO);
			
			
			return "An IVR call is being routed to the customer for PIN change";
			
			
			
			
		}


	

	public void setForgotpinManager(ForgotpinManager forgotpinManager) {
		this.forgotpinManager = forgotpinManager;
	}




	public void setIvrAuthenticationRequestQueueSender(IvrAuthenticationRequestQueue ivrAuthenticationRequestQueueSender) {
		this.ivrAuthenticationRequestQueueSender = ivrAuthenticationRequestQueueSender;
	}

	
	

	
	
}
 