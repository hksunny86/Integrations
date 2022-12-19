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
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.MiniXMLUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

public class BillPaymentConfirmationController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator; 
	
	public BillPaymentConfirmationController (){
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
		
		String resultData = null;
		Map<String, String> responseData = null;

		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD.toString());
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);		
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		ThreadLocalAppUser.setAppUserModel(appUserModel);		
		

		String bankPin = requestWrapper.getParameter(CommandFieldConstants.KEY_PIN);
		Boolean verifyPIN = verifyPIN(bankPin, Boolean.FALSE, requestWrapper, null);
		
		if(!verifyPIN) {
			
			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
			
			if(AllPayWebConstant.INVALID_BANK_PIN.getValue().equals(errorType)) {
				
				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
				
				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
					
					nextView = "allpay-web/billpaymentconfirmation";
					this.createParameters(requestWrapper);
					
				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

					nextView = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
				}				
			}			
			
			return new ModelAndView (nextView, "responseData", responseData);
		}
		
		
		String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
		
		if(MfsWebUtil.isErrorXML(responseXml)) {
			
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
			
			nextView = AllPayWebConstant.GENERIC_PAGE.getValue();
			
			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
			
			if(AllPayWebConstant.INVALID_BANK_PIN.getValue().equals(errorType)) {
				
				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
				
				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {
					
					nextView = "allpay-web/billpaymentconfirmation";
					this.createParameters(requestWrapper);
					
				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

					nextView = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
				}				
			}			
			
		} else if(responseXml != null && !(AllPayWebConstant.BLANK_SPACE.getValue().equals(responseXml))) {
			
			//resultData = MiniXMLUtil.getTagTextValue(responseXml, "string(/msg)");
			responseData = getResultData(responseXml, requestWrapper);
		}
		
		return new ModelAndView (nextView, "responseData", responseData);
	}	
	

	
	/**
	 * @param bankPin
	 * @param fetchTitle
	 * @param request
	 */
	private Boolean verifyPIN(String bankPin, Boolean fetchTitle, HttpServletRequest request, TransactionCodeModel transactionCodeModel) {
		/*
		Boolean pinVerified = Boolean.TRUE;

		allPayWebResponseDataPopulator.logActionLogModel();
		
		Long productId = Long.valueOf(request.getParameter(CommandFieldConstants.KEY_PROD_ID));	
		
		ProductModel productModel = new ProductModel();
		productModel.setProductId(productId);
		
		try {
			//AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
			AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(request.getParameter(CommandFieldConstants.KEY_U_ID));

			allPayWebResponseDataPopulator.verifyPIN(appUserModel, MfsWebUtil.encryptPin(bankPin), transactionCodeModel, productModel, Boolean.FALSE);
			
		} catch (FrameworkCheckedException e) {
			
			pinVerified = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.error(e);
		}
		
		return pinVerified;*/return true;
	}	
	
	
	/**
	 * @param request
	 */
	private void createParameters(HttpServletRequest request) {
		
		request.setAttribute("BDATE", request.getParameter("BDATE"));
		request.setAttribute("LBDATEF", request.getParameter("LBDATEF"));
		request.setAttribute("TAMTF", request.getParameter("TAMTF"));
		request.setAttribute("BPAID", request.getParameter("BPAID"));
		request.setAttribute("CDCUSTMOB", request.getParameter("CDCUSTMOB"));

		request.setAttribute("BAMT", request.getParameter("BAMT"));
		request.setAttribute("TAMTF", request.getParameter("TAMTF"));
		request.setAttribute("TPAMF", request.getParameter("TPAMF"));
		request.setAttribute("CAMTF", request.getParameter("CAMTF"));
		request.setAttribute("TAMT", request.getParameter("TAMT"));
		request.setAttribute("RPNAME", request.getParameter("RPNAME"));
		request.setAttribute("MOBN", request.getParameter("MOBN"));
		request.setAttribute("TPAM", request.getParameter("TPAM"));
		request.setAttribute("CAMT", request.getParameter("CAMT"));

		request.setAttribute("PNAME", request.getParameter("PNAME"));
		request.setAttribute("MOBN", request.getParameter("MOBN"));
		request.setAttribute("TXAMF", request.getParameter("TXAMF"));
		request.setAttribute("TXAM", request.getParameter("TXAM"));
		request.setAttribute("PID", request.getParameter("PID"));
		request.setAttribute("UID", request.getParameter("UID"));
		request.setAttribute("billServiceLabel", request.getParameter("billServiceLabel"));
		request.setAttribute("PNAME", request.getParameter("PNAME"));
		request.setAttribute("dfid", request.getParameter("dfid"));
		request.setAttribute("PID", request.getParameter("PID"));
		request.setAttribute("supplierId", request.getParameter("supplierId"));
		request.setAttribute("SID", request.getParameter("SID"));		
	} 
	
	/**
	 * @param response
	 * @param request
	 * @return
	 */
	private Map<String, String> getResultData(String response, HttpServletRequest request) throws XPathExpressionException {
		
		Map<String, String> responseData = new HashMap<String, String>();
		
		String CSCD = MiniXMLUtil.getTagTextValue(response, "/msg/trans/trn/@CSCD");
		String prod = MiniXMLUtil.getTagTextValue(response, "/msg/trans/trn/@prod");
		String date = MiniXMLUtil.getTagTextValue(response, "/msg/trans/trn/@date");
		String trxCode = MiniXMLUtil.getTagTextValue(response, "/msg/trans/trn/@code");
		String TAMTF = MiniXMLUtil.getTagTextValue(response, "/msg/trans/trn/@TAMTF");
		String BALF = MiniXMLUtil.getTagTextValue(response, "/msg/trans/trn/@BALF");
		
		responseData.put("Consumer Number", CSCD);
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
}
