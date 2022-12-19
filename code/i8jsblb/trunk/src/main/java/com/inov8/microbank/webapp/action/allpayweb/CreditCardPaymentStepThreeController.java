package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class CreditCardPaymentStepThreeController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator; 
	private AgentWebManager agentWebManager;

	public CreditCardPaymentStepThreeController (){
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
		/*
		Boolean isValidAmount = agentWebManager.checkProductLimit(Long.valueOf(productId), Double.valueOf(amount), requestWrapper);
		
		if(!isValidAmount) {
			nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
			return new ModelAndView (nextView);
		}
		*/
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		ThreadLocalAppUser.setAppUserModel(appUserModel);		
		createParameters(requestWrapper);
		
		String BDATE = requestWrapper.getParameter(CommandFieldConstants.KEY_BILL_DATE);
				
		if(BDATE!= null && BDATE.equalsIgnoreCase("N/A")) {
			requestWrapper.addParameter("BDATE", new String[]{null});
		}
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_AGENT_CREDIT_CARD_PAYMENT);

		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
			
			nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
			
			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
			
			if(AllPayWebConstant.INVALID_BANK_PIN.getValue().equals(errorType)) {
				
				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
				
				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
					
					nextView = "allpay-web/creditCardPaymentStepThree";
					this.createParameters(requestWrapper);
					
				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

					nextView = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
				}				
			}			
			
		} else if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {
			
			responseData = getResultData(responseXml, requestWrapper);
		}
		
		return new ModelAndView (nextView, "responseData", responseData);
	}	
	

	/**
	 * @param request
	 */
	private void createParameters(AllPayRequestWrapper requestWrapper) {

		requestWrapper.addParameter("CCNO", requestWrapper.getParameter("CCNO"));
		requestWrapper.addParameter("BDDATE", requestWrapper.getParameter("BDDATE"));
		requestWrapper.addParameter("TXAM", requestWrapper.getParameter("TXAM"));
		requestWrapper.addParameter("MOBN", requestWrapper.getParameter("MOBN"));
		requestWrapper.addParameter("PID", requestWrapper.getParameter("PID"));
		requestWrapper.addParameter("PNAME", requestWrapper.getParameter("PNAME"));
		requestWrapper.addParameter("SID", requestWrapper.getParameter("SID"));
		requestWrapper.addParameter("CCOB", requestWrapper.getParameter("CCOB"));
		requestWrapper.addParameter("TPAM", requestWrapper.getParameter("TPAM"));
		requestWrapper.addParameter("MAMTF", requestWrapper.getParameter("MAMTF"));
	
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
		String CREDIT_CARD_NO = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg/params/param[@name='"+ CommandFieldConstants.KEY_CREDIT_CARD_NO +"']/text())");
		String BILL_DUE_DATE = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg/params/param[@name='"+ CommandFieldConstants.KEY_BILL_DUE_DATE +"']/text())");
		String BILL_AMOUNT = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg/params/param[@name='"+ CommandFieldConstants.KEY_BILL_AMOUNT +"']/text())");
		String OUTSTANDING_BAL = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg/params/param[@name='"+ CommandFieldConstants.KEY_CREDIT_CARD_OUTSTANDING_BAL +"']/text())");
		String MINIMUM_AMOUNT_DUE = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg/params/param[@name='"+ CommandFieldConstants.KEY_FORMATED_MINIMUM_AMOUNT_DUE +"']/text())");
		String PROD_ID = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg/params/param[@name='"+ CommandFieldConstants.KEY_PROD_ID +"']/text())");
		String KEY_TX_CODE = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg/params/param[@name='"+ CommandFieldConstants.KEY_TX_CODE +"']/text())");
		String SID = request.getParameter("SID");
		String PNAME = request.getParameter("PNAME");
		
		responseData.put("Credit Card Number", CREDIT_CARD_NO);
		responseData.put("Bill Due Date", BILL_DUE_DATE);	
		responseData.put("Amount Paid", BILL_AMOUNT);		
		responseData.put("Amount Due", MINIMUM_AMOUNT_DUE);			
		responseData.put("Balance", OUTSTANDING_BAL);				
		
		request.setAttribute("Heading", "Bill Payment of "+request.getParameter("PNAME"));		
		request.setAttribute("TRXID", KEY_TX_CODE);
		request.setAttribute(AllPayWebConstant.PRODUCT_NAME.getValue(), PNAME);
		request.setAttribute("PRODUCT_ID", AllPayWebResponseDataPopulator.encrypt(PROD_ID));
		request.setAttribute("SID", SID);
		
		return responseData;
	}	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
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
	public void setAgentWebManager(AgentWebManager agentWebManager) {
		this.agentWebManager = agentWebManager;
	}
}
