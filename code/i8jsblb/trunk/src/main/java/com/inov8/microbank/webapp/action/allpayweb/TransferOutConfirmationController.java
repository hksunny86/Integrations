package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.AllPayWebUtil;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionProductEnum;
import com.inov8.microbank.mfsweb.MfsWebManager;

public class TransferOutConfirmationController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator; 
	private AgentWebManager agentWebManager;

	public TransferOutConfirmationController () {
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {

		return new Object();
	}

	
	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		return null;
	}

	
	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {	

		String nextView = getSuccessView();
		
		Map<String, String> responseData = null;

		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD.toString());
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}

		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
		String productId = requestWrapper.getParameter(CommandFieldConstants.KEY_PROD_ID);
		String amount = requestWrapper.getParameter(CommandFieldConstants.KEY_TXAM);
		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);

		String bankPin = requestWrapper.getParameter(CommandFieldConstants.KEY_PIN);	
		
		ThreadLocalAppUser.setAppUserModel(appUserModel);			

		allPayWebResponseDataPopulator.logActionLogModel();

		Boolean verifyPIN = verifyPIN(appUserModel, bankPin, Boolean.FALSE, Long.valueOf(productId), requestWrapper);
		
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
		
		String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_TRANSFER_OUT);
		
		Boolean hasError = AllPayWebUtil.isErrorXML(responseXML);
		
		if(!hasError) {
		
			responseData = getResultData(responseXML, requestWrapper);
			
		} else {

			String errors = MiniXMLUtil.getTagTextValue(responseXML, "/msg/errors/error/text()");
			
			nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
			requestWrapper.setAttribute("errors", errors);
			requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Transfer Out");	
		}
		
		return new ModelAndView (nextView, "responseData", responseData);
	}	
	


	/**
	 * @param bankPin
	 * @param fetchTitle
	 * @param request
	 */
	private Boolean verifyPIN(AppUserModel appUserModel, String bankPin, Boolean fetchTitle, Long productId, HttpServletRequest request) {
	
		Boolean pinVerified = Boolean.TRUE;
		
		try {
	
			logger.info("[TransferOutConfirmationController.verifyPin] Verifying Pin for AppUserID:" + appUserModel.getAppUserId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
			
			ProductModel productModel = new ProductModel();
			productModel.setProductId(productId);
			
			allPayWebResponseDataPopulator.verifyPIN(appUserModel, MfsWebUtil.encryptPin(bankPin), null, productModel, fetchTitle);
			
		} catch (FrameworkCheckedException e) {
			
			pinVerified = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.info("[TransferOutConfirmationController.verifyPin] Exceptino occured in Verifying Pin for AppUserID:" + appUserModel.getAppUserId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + ". Exception Msg:" + e.getMessage());
	
		}
		
		return pinVerified;
	}	
	

	private void updateRequestForPreviousScreen(AllPayRequestWrapper requestWrapper) throws Exception {
		
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, String.valueOf(DeviceTypeConstantsInterface.ALLPAY_WEB));
		requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID, String.valueOf(TransactionProductEnum.TRANSACTION_OUT_PRODUCT.getProductId()));

		requestWrapper.setAttribute(CommandFieldConstants.KEY_U_ID, requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));
		requestWrapper.setAttribute(CommandFieldConstants.KEY_PROD_ID, requestWrapper.getParameter(CommandFieldConstants.KEY_PROD_ID));
		requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM, requestWrapper.getParameter(CommandFieldConstants.KEY_TXAM));
		requestWrapper.setAttribute(CommandFieldConstants.KEY_CAMT, requestWrapper.getParameter(CommandFieldConstants.KEY_CAMT));
		requestWrapper.setAttribute(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB, requestWrapper.getParameter(CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB));
		requestWrapper.setAttribute(CommandFieldConstants.KEY_ACCOUNT_NUMBER, requestWrapper.getParameter(CommandFieldConstants.KEY_ACCOUNT_NUMBER));
		requestWrapper.setAttribute(CommandFieldConstants.KEY_NAME, "Transfer Out");
		
		requestWrapper.toString();
	}	
	
	
	/**
	 * @param response
	 * @param request
	 * @return
	 * @throws XPathExpressionException 
	 */
	private Map<String, String> getResultData(String responseXml, HttpServletRequest request) throws XPathExpressionException {
		
		Map<String, String> responseData = new HashMap<String, String>();		

		String PID = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='"+ CommandFieldConstants.KEY_PROD_ID +"']/text()");
		String productName = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='"+ CommandFieldConstants.KEY_NAME +"']/text()");
		String bbAccountNumber = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='"+ CommandFieldConstants.KEY_ACCOUNT_NUMBER_BB +"']/text()");
		String coreAccountNumber = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='"+ CommandFieldConstants.KEY_ACCOUNT_NUMBER +"']/text()");
		String trxAmount = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='"+ CommandFieldConstants.KEY_TXAM +"']/text()");
		String commission = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='"+ CommandFieldConstants.KEY_CAMT +"']/text()");
		String code = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='"+ CommandFieldConstants.KEY_TX_CODE +"']/text()");
		String coreBalance = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='CORE_BALANCE']/text()");
		String olaBalance = MiniXMLUtil.getTagTextValue(responseXml, "/msg/param[@name='OLA_BALANCE']/text()");
		
		responseData.put("Branchless Bank Account #", bbAccountNumber);
		responseData.put("Core Bank Account #", coreAccountNumber);					
		responseData.put("Amount ", trxAmount);								
		responseData.put("Commission ", commission);	
		
		responseData.put("Transaction Code", code);	
//		responseData.put("Balance [Core Account]", coreBalance);	
		responseData.put("Balance [Branchless Account]", olaBalance);			
		request.setAttribute("TRXID", code);		
		
		request.setAttribute("Heading", productName);	
		request.setAttribute("PID", PID);	
		
		return responseData;
	}	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}

	public void setMfsWebController(MfsWebManager mfsWebController) {
		this.mfsWebController = mfsWebController;
	}
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
	public void setAgentWebManager(AgentWebManager agentWebManager) {
		this.agentWebManager = agentWebManager;
	}
}