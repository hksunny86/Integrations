package com.inov8.microbank.webapp.action.ajax.smssendermodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * Created By    : Naseer Ullah <br>
 * Creation Date : Jul 13, 2013 1:58:53 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class ResendSmsStrategyController extends AjaxController
{
    private SmsSenderService smsSenderService;
	private ComplaintManager complaintManager;

	public ResendSmsStrategyController()
	{
	}

    @Override
    public String getResponseContent( HttpServletRequest request, HttpServletResponse response )
    {
		StringBuilder responseBuilder = new StringBuilder();

		try
		{
			Long transactionId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getRequiredStringParameter(request, PortalConstants.KEY_TRANS_ID)));
			String resendSmsStrategy = ServletRequestUtils.getRequiredStringParameter( request, "resendSmsStrategy" );
			String appUserIdParam = ServletRequestUtils.getStringParameter(request, "appUserId");
			//String appUserIdParam = "3733343438";
			Long appUserId = 0L;
			if(appUserIdParam != null && appUserIdParam.length()>0){
				appUserId = new Long(EncryptionUtil.decryptWithDES(appUserIdParam));
			}

			logger.info("Sending sms for Transaction Id: "+ transactionId);
			smsSenderService.resendSmsUsingStrategy( transactionId, resendSmsStrategy );

			if(appUserId > 0)
			{
				complaintManager.createComplaint( ComplaintsModuleConstants.CATEGORY_RESEND_SMS, appUserId );					
			}
			responseBuilder.append( getMessageSourceAccessor().getMessage( "sms.success", request.getLocale() ) );
		}
		catch(ServletRequestBindingException ex)
		{
		    logger.error("Error parsing request", ex);
			responseBuilder.append(getMessageSourceAccessor().getMessage( "sms.failure", request.getLocale() ) );
		}
		catch(Exception ex)
		{
			logger.error("Sms Sendiong failed", ex);
			responseBuilder.append(getMessageSourceAccessor().getMessage( "sms.failure", request.getLocale() ) );
		}

		return responseBuilder.toString();
	}

	public void setSmsSenderService(SmsSenderService smsSenderService)
	{
		this.smsSenderService = smsSenderService;
	}

	public void setComplaintManager(ComplaintManager complaintManager)
	{
		this.complaintManager = complaintManager;
	} 

}