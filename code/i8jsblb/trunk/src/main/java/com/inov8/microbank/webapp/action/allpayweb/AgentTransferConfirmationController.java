package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.transactionmodule.FetchTransactionListViewModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

/**
 * 
 * @author Kashif Bashir
 * @since November, 2012
 * 
 */

public class AgentTransferConfirmationController extends AdvanceFormController {

	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator = null;
	
	private final static Log logger = LogFactory.getLog(AgentTransferConfirmationController.class);
	private final String PAGE_FROM = getFormView();
	


	public AgentTransferConfirmationController() {
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
		
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
	
		return onCreate(request, response, model, exception);
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object commandObject, BindException exception) throws Exception {
		
		if(!isTokenValid(request)) {		
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}		
		
		long start = System.currentTimeMillis();
		
		AllPayRequestWrapper requestWrapper = initializeRequest(request);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD.toString());
		
		String nextView = getSuccessView();		
		
		String bankPin = requestWrapper.getParameter(CommandFieldConstants.KEY_PIN);						
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		
		Map<String, String> responseData = null;

		allPayWebResponseDataPopulator.logActionLogModel();
		
		Boolean verifyPIN = verifyPIN(appUserModel, bankPin, Boolean.FALSE, requestWrapper);
		
		if(!verifyPIN) {	
			
			Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
				
			if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {

				nextView = getFormView();
				
				updateRequestForPreviousScreen(requestWrapper);
					
			} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

				nextView = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
			}
			
			return new ModelAndView(nextView);
			
		}
// Fetch title skipped for BB Agents
// else {
//			Boolean titleFetched = fetchTitle(bankPin, requestWrapper);
//			
//			if(!titleFetched) {
//				
//				return new ModelAndView(AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue());				
//			}
//		}			
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_R2R_TRANS);
		
		logger.info("[AgentTransferConfirmationController.onCreate] Compleded transaction for Logged in AppUserID:" + appUserModel.getAppUserId());
		
		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);	
			
			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
			
			if(AllPayWebConstant.LIMIT_EXCEED.getValue().equals(errorType)) {	
				
				nextView = AllPayWebConstant.GENERIC_PAGE.getValue();		
				
			} else if(AllPayWebConstant.BLANK_SPACE.getValue().equals(errorType)) {	
				
				nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
				
			} else if(AllPayWebConstant.SERVICE_UNAVAILABLE.getValue().equals(errorType)) {	
				
				nextView = AllPayWebConstant.GENERIC_PAGE.getValue();					
			}

		} else if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {
			
			mfsWebResponseDataPopulator.populateTransactionSummary(requestWrapper, responseXml);
			responseData = updateRequest(responseXml, requestWrapper);	
			nextView = getSuccessView();		
		}	

		return new ModelAndView(nextView, "responseData", responseData);
	}
						
	
	/**
	 * @param bankPin
	 * @param fetchTitle
	 * @param request
	 */
	private Boolean verifyPIN(AppUserModel appUserModel, String bankPin, Boolean fetchTitle, HttpServletRequest request) {

		Boolean pinVerified = Boolean.TRUE;
		
		try {

			logger.info("[AgentTransferConfirmationController.verifyPin] Verifying Pin for AppUserID:" + appUserModel.getAppUserId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			allPayWebResponseDataPopulator.verifyPIN(appUserModel, MfsWebUtil.encryptPin(bankPin), null, null, fetchTitle);
			
		} catch (FrameworkCheckedException e) {
			
			pinVerified = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.info("[AgentTransferConfirmationController.verifyPin] Exceptino occured in Verifying Pin for AppUserID:" + appUserModel.getAppUserId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());

		}
		
		return pinVerified;
	}	
	
	
	private Boolean fetchTitle(String bankPin, HttpServletRequest request) {

		logger.info("Agent Web > Fetch Title > APP USER " + ThreadLocalAppUser.getAppUserModel().getUsername());
		
		Boolean titleFetched = Boolean.TRUE;
		
		Long productId = Long.valueOf(request.getParameter(CommandFieldConstants.KEY_PROD_ID));
		
		ProductModel productModel = new ProductModel();
		productModel.setProductId(productId);
		
		try {
			
			allPayWebResponseDataPopulator.fetchTitle(MfsWebUtil.encryptPin(bankPin), null, productModel);
			
		} catch (FrameworkCheckedException e) {
			
			titleFetched = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.error(e);
		}
		
		return titleFetched;
	}
	
	private AllPayRequestWrapper initializeRequest(HttpServletRequest request) {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(DeviceTypeConstantsInterface.ALLPAY_WEB));
		requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, "50013");
		
		return requestWrapper;
	}
	
	
	protected Map<String, String> updateRequest(String responseXml, AllPayRequestWrapper requestWrapper) {

		Map<String, String> responseData = new HashMap<String, String>();
		
		FetchTransactionListViewModel transactionModel = (FetchTransactionListViewModel) requestWrapper.getAttribute("FetchTransactionListViewModel");
		
		if(transactionModel != null) {

			requestWrapper.setAttribute("Heading", "Fund Transferred to Agent ");
			requestWrapper.setAttribute("Name", "'"+requestWrapper.getParameter("RECEIVER_NAME")+".'");
			responseData.put("Recipient Agent Name", requestWrapper.getParameter("RECEIVER_NAME"));
			responseData.put("Recipient Agent Mobile #", requestWrapper.getParameter("CDCUSTMOB"));
			responseData.put("Recipient Agent CNIC #", requestWrapper.getParameter("RECEIVER_CNIC"));
			responseData.put("Amount", String.valueOf(transactionModel.getTranAmount()));
			responseData.put("Transaction ID", transactionModel.getTranCode());
			responseData.put("Dated", transactionModel.getTransactionDateTime());
			responseData.put("Charges/Tax", "0.00");
			
		} else {
			requestWrapper.setAttribute("Heading", "Agent Transfer Failed.");}

		return responseData;
	}	
	

	private void updateRequestForPreviousScreen(AllPayRequestWrapper requestWrapper) throws Exception {
		
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(DeviceTypeConstantsInterface.ALLPAY_WEB));
		requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, "50013");

		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.AGENT_TO_AGENT_INFO);

		AgentTransferController agentTransferController = ((AgentTransferController) this.getWebApplicationContext().getBean("agentTransferController"));
		agentTransferController.updateRequest(requestWrapper, responseXml);	
	}	
	
	
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}		
	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}	
	public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
		this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
	}
}