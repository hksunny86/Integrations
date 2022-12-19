package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

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
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.MfsWebUtil;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;

/**
 * 
 * @author Kashif Bashir
 * @since April, 2012
 * 
 */

public class PayCashCNICStepTwoController extends AdvanceFormController {
	
	private final static Log logger = LogFactory.getLog(PayCashWithDrawalController.class);
	private final String PAGE_FROM = "allpay-web/payCashCNICStepTw";
	public static final String BEAN_NAME = "payCashWithDrawalController";
	
	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator; 
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
	private AgentWebManager agentWebManager;
	
	
	public PayCashCNICStepTwoController() {
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
		
		
		if(!isTokenValid(request)) {			
//			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}
		
		String nextPage = getSuccessView();
		
		AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);						
		AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
		requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.USSD.toString());

		String bankPin = requestWrapper.getParameter(CommandFieldConstants.KEY_PIN);
		String productId = request.getParameter(CommandFieldConstants.KEY_PROD_ID);	
		String customerMobile = request.getParameter("MS_ISDN_MOBILE");
		
		logger.info("[PayCashWithDrawalController.onCreate] Product ID: " + productId + ". Mobile:" + customerMobile);

		if(AllPayWebConstant.CASH_WITHDRAWAL.getIntValue() == Integer.parseInt(productId)) {
			
			Boolean isValidCustomer = allPayWebResponseDataPopulator.isValidCustomer(requestWrapper, customerMobile);
			
			if(!isValidCustomer) {
				
				nextPage = AllPayWebConstant.GENERIC_PAGE.getValue();
			}
		}	

		TransactionCodeModel transactionCodeModel = agentWebManager.loadTransactionCodeByCode(request.getParameter(CommandFieldConstants.KEY_TX_ID));
		
		if(!AllPayWebConstant.GENERIC_PAGE.getValue().equals(nextPage)) {
			
			allPayWebResponseDataPopulator.logActionLogModel();
		
			if(!verifyPIN(bankPin, Boolean.TRUE, requestWrapper, transactionCodeModel)) {	
						
				Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);
					
				if(STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {

					nextPage = PAGE_FROM;
						
				} else if(STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

					nextPage = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
				}
				
			} else {
				
				Boolean titleFetched = fetchTitle(bankPin, Boolean.TRUE, requestWrapper, transactionCodeModel);
				
				if(!titleFetched) {
					nextPage = AllPayWebConstant.GENERIC_PAGE.getValue();					
				}
			}
		}
				
		if(!AllPayWebConstant.GENERIC_PAGE.getValue().equals(nextPage) && 
				!AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue().equals(nextPage)) {
			
			Boolean requestPopulated = agentWebManager.initTransactionParams(requestWrapper, Boolean.FALSE, transactionCodeModel);
			
			if(!requestPopulated) {
				nextPage = AllPayWebConstant.GENERIC_PAGE.getValue();
			}
		}
		
		return new ModelAndView(nextPage);
	}
	
	
	/**
	 * @param bankPin
	 * @param fetchTitle
	 * @param request
	 */
	private Boolean verifyPIN(String bankPin, Boolean fetchTitle, HttpServletRequest request, TransactionCodeModel transactionCodeModel) {

		Boolean pinVerified = Boolean.TRUE;
		
		Long productId = Long.valueOf(request.getParameter(CommandFieldConstants.KEY_PROD_ID));	
		
		ProductModel productModel = new ProductModel();
		productModel.setProductId(productId);
		
		try {
			AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
			
			if(appUserModel == null || appUserModel.getAppUserId() == null) {
			
				appUserModel = allPayWebResponseDataPopulator.getAppUserModel(request.getParameter(CommandFieldConstants.KEY_U_ID));
			}
			
			logger.info("Going Pin Verification for AppUserId  "+appUserModel.getAppUserId());

			allPayWebResponseDataPopulator.verifyPIN(appUserModel, MfsWebUtil.encryptPin(bankPin), transactionCodeModel, productModel, Boolean.FALSE);
			
		} catch (FrameworkCheckedException e) {
			
			pinVerified = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.error(e);
		}
		
		return pinVerified;
	}
	
	
	/**
	 * @param bankPin
	 * @param fetchTitle
	 * @param request
	 * @param transactionCodeModel
	 * @return
	 */
	private Boolean fetchTitle(String bankPin, Boolean fetchTitle, HttpServletRequest request, TransactionCodeModel transactionCodeModel) {

		logger.info("Agent Web > Fetch Title > APP USER " + ThreadLocalAppUser.getAppUserModel().getUsername());
		
		Boolean titleFetched = Boolean.TRUE;
		
		Long productId = Long.valueOf(request.getParameter(CommandFieldConstants.KEY_PROD_ID));
		
		ProductModel productModel = new ProductModel();
		productModel.setProductId(productId);
		
		try {
			
			allPayWebResponseDataPopulator.fetchTitle(MfsWebUtil.encryptPin(bankPin), transactionCodeModel, productModel);
			
		} catch (FrameworkCheckedException e) {
			
			titleFetched = Boolean.FALSE;
			request.setAttribute("errors", e.getMessage());
			logger.error(e);
		}
		
		return titleFetched;
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