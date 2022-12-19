package com.inov8.microbank.webapp.action.allpayweb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.common.util.ValidatorWrapper;
import com.inov8.microbank.common.util.XMLConstants;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class ChangeAllpayPasswordController extends AbstractController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	
	public MfsWebManager getMfsWebController() {
		return mfsWebController;
	}

	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public MfsWebResponseDataPopulator getMfsWebResponseDataPopulator() {
		return mfsWebResponseDataPopulator;
	}
	public void setMfsWebResponseDataPopulator(
			MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setUserInfo(requestWrapper);

		
		String pin = requestWrapper.getParameter("PIN");
		String nPin = requestWrapper.getParameter("NPIN");
		String cPin = requestWrapper.getParameter("CPIN");
							
		if(null != requestWrapper && requestWrapper.getParameterMap().size() != 0 && 
				null != requestWrapper.getParameter(XMLConstants.ATTR_PIN.toUpperCase())) {
		
		Boolean isValid = validateRequest(requestWrapper, pin, cPin, nPin);
			
		if(!isValid) {
			return new ModelAndView ("allpay-web/changeallpaypassword");
		}

		String responseXml = mfsWebController.handleRequest(requestWrapper,CommandFieldConstants.CMD_GNI_CHG_PIN);
		
		if( MfsWebUtil.isErrorXML(responseXml) )
		{
			AllPayWebResponseDataPopulator.setDefaultParams(new AllPayRequestWrapper(requestWrapper));
			requestWrapper.setAttribute("oldPinLabel", requestWrapper.getParameter("oldPinLabel"));
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
			requestWrapper.setAttribute("ACID_TEMP", requestWrapper.getParameter("ACID_TEMP"));
			requestWrapper.setAttribute("BAID_TEMP", requestWrapper.getParameter("BAID_TEMP"));	
			return new ModelAndView ("allpay-web/changeallpaypassword");
		}
		else
		{
			mfsWebResponseDataPopulator.populateMessage(requestWrapper, responseXml);

			HttpSession session = requestWrapper.getSession( false );
			if( null != session )
			{
				Object pinChangeRequiredObj = session.getAttribute(AllPayWebConstant.PIN_CHANGE_REQUIRED.getValue());
				if( null != pinChangeRequiredObj )
				{
					Boolean pinChangeRequired = (Boolean) pinChangeRequiredObj;

					try
					{
						session.removeAttribute(AllPayWebConstant.PIN_CHANGE_REQUIRED.getValue());
					}
					catch (IllegalStateException e)
					{
						logger.error("Session already invalidated", e);
					}

					if( pinChangeRequired )
					{	

						requestWrapper.setAttribute(CommandFieldConstants.KEY_ACC_ID, requestWrapper.getParameter("ACID_TEMP"));
						requestWrapper.setAttribute(CommandFieldConstants.KEY_BANK_ID, requestWrapper.getParameter("BAID_TEMP"));						
						requestWrapper.setAttribute("message", "Your password has been changed successfully. Kindly change your PIN for the first time:");
						return new ModelAndView ("allpay-web/changePin");
					}

				}
			}

			requestWrapper.setAttribute(CommandFieldConstants.KEY_ACC_ID, requestWrapper.getParameter("ACID_TEMP"));
			requestWrapper.setAttribute(CommandFieldConstants.KEY_BANK_ID, requestWrapper.getParameter("BAID_TEMP"));	
			return new ModelAndView (AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		}
		else
		{
			requestWrapper.setAttribute("ACID_TEMP", requestWrapper.getParameter("ACID"));
			requestWrapper.setAttribute("BAID_TEMP", requestWrapper.getParameter("BAID"));
			return new ModelAndView("allpay-web/changeallpaypassword");
		}
	}

	private String  validCredentials(String password, String newPassword, String confirmPassword) {
		String errorMsg = "";
		if (newPassword.trim().length() < 8 || newPassword.trim().length() > 50){
			 errorMsg+="Password length must be between 8 and 50";
		}
		
		
		return errorMsg;
	}
	
	
	private Boolean validateRequest(AllPayRequestWrapper requestWrapper, String pin, String cPin, String nPin) {
		
		Boolean isValid = Boolean.TRUE;
		
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors = ValidatorWrapper.doRequired(pin,validationErrors,"Current Password");
		validationErrors = ValidatorWrapper.doRequired(nPin,validationErrors,"New Password");
		validationErrors = ValidatorWrapper.doRequired(cPin,validationErrors,"Confirm Password");		
		
		if((null != pin && pin.indexOf(" ")!= -1) || ((null != nPin) && nPin.indexOf(" ")!= -1) || (null != cPin && cPin.indexOf(" ")!= -1)) {
			requestWrapper.setAttribute("errors", "Blank spaces are not allowed in the password.");
			isValid = Boolean.FALSE;
		}		
		
		if(isValid && validationErrors.hasValidationErrors()) {

			requestWrapper.setAttribute("oldPinLabel", requestWrapper.getParameter("oldPinLabel"));
			requestWrapper.setAttribute("errors", validationErrors.getErrors());
			requestWrapper.setAttribute("ACID_TEMP", requestWrapper.getParameter("ACID_TEMP"));
			requestWrapper.setAttribute("BAID_TEMP", requestWrapper.getParameter("BAID_TEMP"));	
			
			isValid = Boolean.FALSE;			
		}
		
		if(isValid && (nPin.length() < 8 || nPin.length() > 50)) {
			
			requestWrapper.setAttribute("oldPinLabel", requestWrapper.getParameter("oldPinLabel"));
			requestWrapper.setAttribute("errors", "Password length must atleast bex.");
			requestWrapper.setAttribute("ACID_TEMP", requestWrapper.getParameter("ACID_TEMP"));
			requestWrapper.setAttribute("BAID_TEMP", requestWrapper.getParameter("BAID_TEMP"));
			
			isValid = Boolean.FALSE;
		}		
				
		return isValid;	
	}
	

}
