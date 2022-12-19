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

public class UBPServiceStepThreeController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator; 
	private AgentWebManager agentWebManager;

	public UBPServiceStepThreeController (){
		setCommandName("object");
	    setCommandClass(Object.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {

		return new Object();
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {	

		String nextView = getSuccessView();
		
		Map<String, String> responseData = null;

		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}

		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
		String productId = requestWrapper.getParameter(CommandFieldConstants.KEY_PROD_ID);
		String amount = requestWrapper.getParameter(CommandFieldConstants.KEY_AMOUNT);
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
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD.toString());
		
		String BDATE = requestWrapper.getParameter(CommandFieldConstants.KEY_BILL_DATE);
				
		if(BDATE!= null && BDATE.equalsIgnoreCase("N/A")) {
			requestWrapper.addParameter("BDATE", new String[]{null});
		}

		logger.info("UBPServiceStepThreeController.REQUEST.BDATE = -"+requestWrapper.getParameter(CommandFieldConstants.KEY_BILL_DATE)+"-");
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
		
		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
			
			nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
			
			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
			
			if(AllPayWebConstant.INVALID_BANK_PIN.getValue().equals(errorType)) {
				
				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
				
				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
					
					nextView = "allpay-web/ubpServiceStepThree";
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
	private void createParameters(AllPayRequestWrapper request) {

//		request.addParameter(CommandFieldConstants.KEY_TOTAL_AMOUNT, "0.0");
//		request.addParameter(CommandFieldConstants.KEY_COMM_AMOUNT, "0.0");
//		request.addParameter(CommandFieldConstants.KEY_BILL_AMOUNT, "0.0");
		
//		request.setAttribute("BDATE", request.getParameter("BDATE"));
//		request.setAttribute("LBDATEF", request.getParameter("LBDATEF"));
//		request.setAttribute("TAMTF", request.getParameter("TAMTF"));
//		request.setAttribute("BPAID", request.getParameter("BPAID"));
//		request.setAttribute("CDCUSTMOB", request.getParameter("CDCUSTMOB"));
//
//		request.setAttribute("BAMT", request.getParameter("BAMT"));
//		request.setAttribute("TAMTF", request.getParameter("TAMTF"));
//		request.setAttribute("TPAMF", request.getParameter("TPAMF"));
//		request.setAttribute("CAMTF", request.getParameter("CAMTF"));
//		request.setAttribute("TAMT", request.getParameter("TAMT"));
//		request.setAttribute("RPNAME", request.getParameter("RPNAME"));
//		request.setAttribute("MOBN", request.getParameter("MOBN"));
//		request.setAttribute("TPAM", request.getParameter("TPAM"));
//		request.setAttribute("CAMT", request.getParameter("CAMT"));
//
//		request.setAttribute("PNAME", request.getParameter("PNAME"));
//		request.setAttribute("MOBN", request.getParameter("MOBN"));
//		request.setAttribute("TXAMF", request.getParameter("TXAMF"));
//		request.setAttribute("TXAM", request.getParameter("TXAM"));
//		request.setAttribute("PID", request.getParameter("PID"));
//		request.setAttribute("UID", request.getParameter("UID"));
//		request.setAttribute("billServiceLabel", request.getParameter("billServiceLabel"));
//		request.setAttribute("PNAME", request.getParameter("PNAME"));
//		request.setAttribute("dfid", request.getParameter("dfid"));
//		request.setAttribute("PID", request.getParameter("PID"));
//		request.setAttribute("supplierId", request.getParameter("supplierId"));
//		request.setAttribute("SID", request.getParameter("SID"));		
	} 
	
	/**
	 * @param response
	 * @param request
	 * @return
	 * @throws XPathExpressionException 
	 */
	private Map<String, String> getResultData(String responseXml, HttpServletRequest request) throws XPathExpressionException {
		
		Map<String, String> responseData = new HashMap<String, String>();

		String MDN = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@CSCD");
		String prod = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@prod");
		String date = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@date");
		String trxCode = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@code");
		String TAMTF = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@TAMTF");
		String BALF = MiniXMLUtil.getTagTextValue(responseXml, "/msg/trans/trn/@BALF");
		
		responseData.put("Consumer Number / MDN", MDN);
		responseData.put("Dated", date);				
		responseData.put("Tx ID", trxCode);				
		responseData.put("Amount Paid", TAMTF);			
		responseData.put("Balance", BALF);				
		request.setAttribute("Heading", "Bill Payment of "+prod);		
		request.setAttribute("TRXID", trxCode);
		request.setAttribute("PID", request.getParameter("PID"));	
		
		request.setAttribute(AllPayWebConstant.PRODUCT_NAME.getValue(), prod);
		request.setAttribute("PRODUCT_ID", AllPayWebResponseDataPopulator.encrypt(request.getParameter("PID")));
		request.setAttribute("SID", request.getParameter("SID"));
		
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
