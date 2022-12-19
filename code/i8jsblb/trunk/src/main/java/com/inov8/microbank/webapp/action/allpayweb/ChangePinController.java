package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.USSDXMLUtils;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.sun.org.apache.xml.internal.security.utils.EncryptionConstants;

/**
 * 
 * @author Kashif Bashir
 * @since April, 2012
 * 
 */

public class ChangePinController extends AdvanceFormController {

	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
		
	private final static Log logger = LogFactory.getLog(PayCashWithDrawalController.class);
	
	private final String PAGE_FROM = "allpay-web/changePin";
	
	private final String PARAM_CURRENT_PIN = "PIN";
	private final String PARAM_NEW_PIN = "NPIN";
	private final String PARAM_CONFIRM_PIN = "CPIN";
	
	public ChangePinController() {
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		return null;
	}	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {

		logger.info("onUpdate(...)");
		
		return onCreate(request, response, model, exception);
	}
	
	
	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception {
		
		logger.info("onCreate(...)");
		String nextView = PAGE_FROM;
		if(!isTokenValid(request)) {
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}		
		
		String pin = (String) request.getParameter(PARAM_CURRENT_PIN);
		String nPin = (String) request.getParameter(PARAM_NEW_PIN);	
		String cPin = (String) request.getParameter(PARAM_CONFIRM_PIN);
		String isMigrated = null;
		if(request.getParameter("IS_MIGRATED") != null)
			isMigrated = (String)request.getParameter("IS_MIGRATED");
		Boolean isValidate = validate(request, pin, nPin, cPin);
		
		if(isValidate) {
			
			AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
			
			AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
			requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
			requestWrapper.addParameter(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");

			String responseXML = null;
			if(isMigrated != null && isMigrated.equals("1"))
				responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_MIGRATED_PIN_CHG);
			else
				responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_VF_PIN_CHG);

			if(MfsWebUtil.isErrorXML(responseXML)) {
				
				mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);	
						
				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
				
				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
					
					nextView = getFormView();
					
				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

					nextView = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
				}

				String isPinChangeReq = (String)request.getSession().getAttribute("PIN_CHANGE_REQUIRED");
				if(null != isPinChangeReq && !"".equals(isPinChangeReq))
				{
					request.setAttribute("PIN_CHANGE_REQUIRED", Boolean.TRUE);
				}
				if(isMigrated != null && isMigrated.equals("1"))
					requestWrapper.setAttribute("IS_MIGRATED",isMigrated);
								
			} else {
			
				mfsWebResponseDataPopulator.populateMessage(requestWrapper, responseXML);	
				nextView = getSuccessView();
				
			}
		}
		
		return new ModelAndView(nextView);
	}	
	
	
	/**
	 * @param request
	 * @param pin
	 * @param nPin
	 * @param cPin
	 * @return
	 */
	Boolean validate(HttpServletRequest request, String pin, String nPin, String cPin) {
		
		Boolean validate = Boolean.TRUE;
		Integer index = 0;
		StringBuilder errors = new StringBuilder();
		if(request.getParameter("IS_MIGRATED") == null)
		{
			if((null != pin && pin.indexOf(" ")!= -1) || ((null != nPin) && nPin.indexOf(" ")!= -1) || (null != cPin && cPin.indexOf(" ")!= -1)) {
				errors.append((++index).toString() +" Blank spaces are not allowed in the Pin(s).\n");
				validate = Boolean.FALSE;
			}
			if (pin.trim().length() < 4 ){
				errors.append((++index).toString() +" : Pin length must be 4 Digits.\n");
				validate = Boolean.FALSE;
			}
			if (!isNumericNumber(pin)){
				errors.append((++index).toString() +" : Pin must be 4 Digits.\n");
				validate = Boolean.FALSE;
			}
		}
		if(((null != nPin) && nPin.indexOf(" ")!= -1) || (null != cPin && cPin.indexOf(" ")!= -1)) {
			errors.append((++index).toString() +" Blank spaces are not allowed in the Pin(s).\n");			
			validate = Boolean.FALSE;
		}

		if (nPin.trim().length() < 4 ){
			errors.append((++index).toString() +" : New Pin length must be 4 Digits.\n");
			validate = Boolean.FALSE;
		}
		if (cPin.trim().length() < 4 ){
			errors.append((++index).toString() +" : Confirm New Pin length must be 4 Digits.\n");
			validate = Boolean.FALSE;
		}
		
		if(!cPin.equals(nPin)) {
			errors.append((++index).toString() +" : Confirm and New Pin Numbers are not same.\n");
			validate = Boolean.FALSE;
		}

		if (!isNumericNumber(nPin)){
			errors.append((++index).toString() +" : New Pin must be 4 Digits.\n");
			validate = Boolean.FALSE;
		}
		if (!isNumericNumber(cPin)){
			errors.append((++index).toString() +" : Confirm New Pin must be 4 Digits.\n");
			validate = Boolean.FALSE;
		}
		
		if(!validate) {
			
			request.setAttribute("errors", errors.toString());
		}
		
		return validate;
	}

	public static boolean isNumericNumber(String string){
		  
		  if(string != null && !string.trim().equals("")) {		  
			  return string.matches("^\\d+$");
		  }
		  
		  return false;
	  }		
	
	/**
	 * @param agentMobile
	 * @param pin
	 * @param transactionCode
	 * @param customerMobile
	 * @return
	 */
	private String getResponseXML(String agentMobile, String pin, String transactionCode, String customerMobile) {
		
		String responseXML = 
			USSDXMLUtils.prepareXmlMessage("44", "UssdApp", "kanne!", "4", agentMobile, "PC "+ pin +" "+ transactionCode +" "+ customerMobile, "50000");
		
		String reqTimeNode = "<param name="+'"'+"reqTime"+'"'+">"+System.currentTimeMillis()+"</param>";
		
		StringBuilder xmlResponse = new StringBuilder(responseXML);
		xmlResponse.insert(xmlResponse.indexOf("</params>"), reqTimeNode);	
		
		return xmlResponse.toString();
	}
	
	
	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
	
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}	
}