/**
 * 
 */
package com.inov8.microbank.webapp.action.ajax.smssendermodule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintsModuleConstants;
import com.inov8.microbank.server.service.complaintmodule.ComplaintManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			Jan 4, 2007
 * Creation Time: 			6:04:40 PM
 * Description:				
 */
public class ResendSmsSenderController extends AjaxController
{

    private SmsSenderService smsSenderService;
	private ComplaintManager complaintManager;

	/**
	 * 
	 */
	public ResendSmsSenderController()
	{
	}

	/* (non-Javadoc)
	 * @see com.inov8.microbank.webapp.action.ajax.AjaxController#getResponseContent(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String getResponseContent(HttpServletRequest request,
			HttpServletResponse response)
	{
		StringBuffer buffer = new StringBuffer();
		
		try
		{
			Long transactionId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getRequiredStringParameter(request, PortalConstants.KEY_TRANS_ID)));
			String encryptedTID = ServletRequestUtils.getRequiredStringParameter(request, PortalConstants.KEY_TRANS_ID);
			String commandId = ServletRequestUtils.getStringParameter(request, "commandId");
			String appUserIdParam = ServletRequestUtils.getStringParameter(request, "appUserId");
			Long appUserId = 0L;
			if(appUserIdParam != null && appUserIdParam.length()>0){
				appUserId = new Long(EncryptionUtil.decryptWithDES(appUserIdParam));
			}
			logger.info("Sending sms for Transaction Id: "+ transactionId);
			if(commandId != null ) {
				commandId = commandId.trim();
			    if(commandId.equalsIgnoreCase(CommandFieldConstants.CMD_MINI_CASHOUT)){
	                smsSenderService.resendCashWithdrawalSms(transactionId);
	            }else if(commandId.equalsIgnoreCase(CommandFieldConstants.CMD_ACCOUNT_TO_CASH)){
	                smsSenderService.resendCashPaymentSms(transactionId,false);
	            }else if(commandId.equalsIgnoreCase(CommandFieldConstants.CMD_CASH_TO_CASH_INFO)){
	                smsSenderService.resendCashPaymentSms(transactionId,true);
	            } else if(commandId.equalsIgnoreCase(CommandFieldConstants.CMD_BULK_PAYMENT)){
	                smsSenderService.resendCashPaymentSms(transactionId,null);
	            }  
			}else{
				smsSenderService.resendSms(transactionId);
			}
			
			if(appUserId > 0){
				complaintManager.createComplaint(ComplaintsModuleConstants.CATEGORY_RESEND_SMS, appUserId);					
			}			
			buffer.append("<p>")
			.append(getMessageSourceAccessor().getMessage("sms.success", request.getLocale()))
			.append("</p><script type='javascript/text'>document.getElementById('resendSms")
			.append(encryptedTID)
			.append("')")
			.append(".disabled=true;</script>");
			
		}
		catch (ServletRequestBindingException ex)
		{
			buffer.append("<p>")
			.append(getMessageSourceAccessor().getMessage("sms.failure", request.getLocale()))
			. append("</p>");
			logger.error("Error parsing request", ex);
		}
		catch(FrameworkCheckedException fEx)
		{
			buffer.append("<p>")
			.append(getMessageSourceAccessor().getMessage("sms.failure", request.getLocale()))
			. append("</p>");
			logger.error("Sms Sending failed", fEx);
		}
		catch(Exception x)
		{
			buffer.append("<p>")
			.append(getMessageSourceAccessor().getMessage("sms.failure", request.getLocale()))
			. append("</p>");
			logger.error("Sms Sending failed", x);
		}		
		return buffer.toString();
	}

	public void setSmsSenderService(SmsSenderService smsSenderService)
	{
		this.smsSenderService = smsSenderService;
	}

	public void setComplaintManager(ComplaintManager complaintManager) {
		this.complaintManager = complaintManager;
	} 

	
}
