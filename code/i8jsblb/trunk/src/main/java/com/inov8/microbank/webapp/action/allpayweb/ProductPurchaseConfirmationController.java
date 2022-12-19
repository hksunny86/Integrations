package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.allpayweb.formbean.AgentWebFormBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

public class ProductPurchaseConfirmationController extends AdvanceFormController {
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	
	private static final String PRODUCT_NAME = "Product";
	private static final String DEVICE_F_ID = "dfid";
	private static final String PRODUCT_ID = "PID";	
	
	public ProductPurchaseConfirmationController (){
		setCommandName("agentWebFormBean");
	    setCommandClass(AgentWebFormBean.class);
	}
	@Override
	protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
		AgentWebFormBean agentWebFormBean = new AgentWebFormBean();
		agentWebFormBean.setDeviceTypeId(Long.valueOf(8));

		return agentWebFormBean;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request) throws Exception {
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		return null;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {

		ModelAndView modelAndView = new ModelAndView( getSuccessView() );
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
		String mobileNumber = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
		String cNic = requestWrapper.getParameter(CommandFieldConstants.KEY_CNIC).trim();
		String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
		AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
		UserDeviceAccountsModel uda = allPayWebResponseDataPopulator.getUserDeviceAccountsDAO().findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
		String trxProcAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TX_PROCESS_AMNT);
		String totalAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TOTAL_AMOUNT);
		String commAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_COMM_AMOUNT);
		String PID = ProductConstantsInterface.CASH_DEPOSIT.toString();//requestWrapper.getParameter(PRODUCT_ID);

		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}

		Integer pinRetryCount = 0;
		if(requestWrapper.getParameter("PIN_RETRY_COUNT") != null && !requestWrapper.getParameter("PIN_RETRY_COUNT").equals(""))
			pinRetryCount = Integer.parseInt(requestWrapper.getParameter("PIN_RETRY_COUNT"));
		requestWrapper.addParameter("ENCT","1");
		requestWrapper.addParameter("PIN_RETRY_COUNT",pinRetryCount.toString());
		String pin = requestWrapper.getParameter("PIN");
		requestWrapper.addParameter("PIN",pin);
		requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
		requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC,requestWrapper.getParameter("CNIC"));
		String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_VERIFY_PIN);

		if(MfsWebUtil.isErrorXML(responseXML) ) {

			pinRetryCount +=1;
			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
			requestWrapper.setAttribute("PIN_RETRY_COUNT",pinRetryCount.toString());
			requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC,cNic);
			requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_MOBILE,requestWrapper.getParameter("CMOB"));
			requestWrapper.setAttribute("TAMT",totalAmount);
			requestWrapper.setAttribute(CommandFieldConstants.KEY_COMM_AMOUNT,commAmount);
			requestWrapper.setAttribute(CommandFieldConstants.KEY_TX_PROCESS_AMNT,trxProcAmount);
			if(responseXML.contains("Incorrect"))
				return new ModelAndView(getFormView());

			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
			logger.info("errorType "+errorType);

			if( AllPayWebConstant.INVALID_BANK_PIN.getValue().equals( errorType ) ) {

				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);

				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {

					modelAndView.setViewName(getFormView());

				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

					modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue());
				}

			} else if( AllPayWebConstant.LIMIT_EXCEED.getValue().equals( errorType ) ) {

				modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());

			} else {

				modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());
			}
			requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC,cNic);
			requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");

			return modelAndView;

		}

		requestWrapper.addParameter(PRODUCT_ID, PID);
		requestWrapper.addParameter(CommandFieldConstants.KEY_AGENT_MOBILE,appUserModel.getMobileNo());
		ThreadLocalAppUser.setAppUserModel(appUserModel);
		ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
		responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CASH_DEPOSIT);

		if(MfsWebUtil.isErrorXML(responseXML) ) {
			
			allPayWebResponseDataPopulator.populateAccountHolderInfo(requestWrapper);
			allPayWebResponseDataPopulator.populateTransactionInfo(requestWrapper);

			mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);

			String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);			
			logger.info("errorType "+errorType);

			modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());
			requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
			}
		else {
			/*mfsWebResponseDataPopulator.populateTransactionSummary(requestWrapper, responseXML);
			requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Receive Cash Transaction Summary");*/
		}
		mfsWebResponseDataPopulator.populateProductPurchase(requestWrapper,responseXML);
		requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC,cNic);
		requestWrapper.setAttribute("isValid","1");
		requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Cash Deposited Successfully.");
		return modelAndView;
	}
	
	
	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
		return onCreate(request, response, model, exception);
	}

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
	public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
		this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
	}
}
