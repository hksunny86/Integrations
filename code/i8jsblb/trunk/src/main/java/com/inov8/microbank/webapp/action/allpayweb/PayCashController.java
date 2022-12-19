package com.inov8.microbank.webapp.action.allpayweb;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MiniTransactionStateConstant;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

/**
 * 
 * @author Kashif Bashir
 * @since April, 2012
 * 
 */

public class PayCashController extends AdvanceFormController {

	private MfsWebManager mfsWebController;
	private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
	private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

	private final static Log logger = LogFactory.getLog(PayCashController.class);
	private final String PAGE_FROM = "allpay-web/payCashStepOne";
	public final String CASH_WITHDRAW = "Cash Withdraw";
	public final String ACCOUNT_TO_CASH = "Account To Cash";
	public final String CASH_TO_CASH = "Cash Transfer";
	public final String BULK_PAYMENT_WITHDRAW = "Bulk Payment Withdrawal";
	public final String TRANSACTION_TYPE = "TRANSACTION_TYPE";

	private AgentWebManager agentWebManager;
	
	public PayCashController(){
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
		
		logger.info("[PayCasyController.onCreate] Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		
		if(!isTokenValid(request)) {			
			return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
		}		
		
		String nextPage = getSuccessView();
		
		String transactionId = request.getParameter(CommandFieldConstants.KEY_TX_ID);

		TransactionCodeModel transactionCodeModel = loadTransactionCodeByCode(transactionId);
			
		if(transactionCodeModel != null) {

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			TransactionDetailMasterModel model = new TransactionDetailMasterModel();
			model.setTransactionCode(transactionId);
			searchBaseWrapper.setBasePersistableModel(model);
			model = (TransactionDetailMasterModel) getCommonCommandManager().loadTransactionDetailMaster(searchBaseWrapper).getBasePersistableModel();

			MiniTransactionModel miniTransactionModel = loadMiniTransactionModelByTrId(transactionCodeModel, request);

			if(!isExpired(miniTransactionModel,model)) {
			
				logger.info("[PayCashController.onCreate] validating Transaction ID: " + transactionCodeModel.getCode());

				String transactionType = getTransactionType(miniTransactionModel,model);
				request.setAttribute(TRANSACTION_TYPE, transactionType);
				
				CASH_WITHDRAW_CASE_VALIDATION : {
				
					if(CASH_WITHDRAW.equals(transactionType)) {
						
						String uid = request.getParameter(CommandFieldConstants.KEY_U_ID);
						
						if(!isSameAgentCW(miniTransactionModel.getSmsText(), uid)) {
	
							nextPage = PAGE_FROM;				
							request.setAttribute("errors", "This transaction cannot be carried out by this agent.");	
						}
					}
				}
				
				agentWebManager.setTransactionParameter(transactionId, request, miniTransactionModel,model);
				
			} else {				
				nextPage = PAGE_FROM;				
				request.setAttribute("errors", "This transaction is either already claimed or expired.");				
			}
			
		} else {			
			nextPage = PAGE_FROM;			
			request.setAttribute("errors", "Invalid Transaction ID "+transactionId);
		}
		
		return new ModelAndView(nextPage);
	}

	
	private Boolean isSameAgentCW(String smsText, String UID) {
		
		Boolean isSameAgent = Boolean.FALSE;
		
		AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
		
		if(appUserModel == null) {
			
			appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);	
		}		
		
		if(smsText != null && !smsText.isEmpty()) {
			
			String[] array = smsText.split(" ");
			
			if(array != null && array.length>0) {
			
				String agent2MobileNumber = array[0];
				
				if(appUserModel.getMobileNo().equals(agent2MobileNumber)) {
					
					isSameAgent = Boolean.TRUE;
				}
			}
		}
		
		return isSameAgent;
	}
	
	
	protected static Boolean isExpired(MiniTransactionModel miniTransactionModel,TransactionDetailMasterModel tdm) {
		
		Boolean isExpired = Boolean.TRUE;
		
		if(miniTransactionModel != null && miniTransactionModel.getMiniTransactionStateId() != null) {
						
			if(miniTransactionModel.getMiniTransactionStateId().longValue() == MiniTransactionStateConstant.PIN_SENT.longValue()) {
			
				isExpired = Boolean.FALSE;
			}
		}
		else if(tdm != null && tdm.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.IN_PROGRESS))
		{
			isExpired = Boolean.FALSE;
		}
		
		return isExpired;
	}
	
	
	private CommonCommandManager commonCommandManager = null;
	private CommonCommandManager getCommonCommandManager() {
		
		if(commonCommandManager == null) {
			   commonCommandManager = 
				   (CommonCommandManager) getWebApplicationContext().getBean("commonCommandManager");
		}
		
		return commonCommandManager;
	}
	
	
	/**
	 * @param transactionId
	 * @return
	 */
	protected TransactionCodeModel loadTransactionCodeByCode(String transactionId) {
		
		TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
		transactionCodeModel.setCode(transactionId);	
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(transactionCodeModel);
		
		try {
			
			baseWrapper = getCommonCommandManager().loadTransactionCodeByCode(baseWrapper);
			
			if(baseWrapper != null && baseWrapper.getBasePersistableModel() != null) {
				   
				return ((TransactionCodeModel)baseWrapper.getBasePersistableModel());	
			}
			
		} catch (FrameworkCheckedException e) {
			logger.error(e);
		}   
		
		return null;
	}
	
	
	/**
	 * @param transactionCodeModel
	 * @param request
	 * @return
	 */
	protected MiniTransactionModel loadMiniTransactionModelByTrId(TransactionCodeModel transactionCodeModel, HttpServletRequest request) {
		
		MiniTransactionModel miniTransactionModel = null;
		   
		try {			   
			   MiniTransactionModel miniTrModel = new MiniTransactionModel();
			   miniTrModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
				   
			   SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
			   wrapper.setBasePersistableModel(miniTrModel);
			   wrapper = commonCommandManager.loadMiniTransaction(wrapper);
				   
			   if(wrapper.getCustomList() != null && 
					   wrapper.getCustomList().getResultsetList() != null && 
					   	wrapper.getCustomList().getResultsetList().size() > 0) {
				
				miniTransactionModel = (MiniTransactionModel)wrapper.getCustomList().getResultsetList().get(0);			   
			   }
		   
		} catch (FrameworkCheckedException e) {
			logger.error(e);
			request.setAttribute("errors", e.getLocalizedMessage());
		}
		
		return miniTransactionModel;
	}
	
	
	protected TransactionDetailModel loadTransactionDetailModel(TransactionCodeModel transactionCodeModel, HttpServletRequest request) {
		
		TransactionDetailModel transactionDetailModel = null;
		   
		try {
			
			TransactionModel transactionModel = getTransactionModel(transactionCodeModel);
			
			if(transactionModel != null) {
				
				SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
				wrapper.setBasePersistableModel(transactionModel);
				commonCommandManager.loadTransactionDetail(wrapper);
				transactionDetailModel = (TransactionDetailModel) wrapper.getBasePersistableModel();
			}
		   
		} catch (FrameworkCheckedException e) {
			logger.error(e);
			request.setAttribute("errors", e.getLocalizedMessage());
		}
		
		return transactionDetailModel;
	}
	
	
	protected TransactionModel getTransactionModel(TransactionCodeModel transactionCodeModel) {
		
		TransactionModel transactionModel = null;
		
		SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
		wrapper.setBasePersistableModel(transactionCodeModel);
		
		try {
			
			wrapper = commonCommandManager.loadTransactionByTransactionCode(wrapper);
			transactionModel = (TransactionModel) wrapper.getBasePersistableModel();
			
		} catch (FrameworkCheckedException e) {
			logger.error(e);
		}
		
		return transactionModel;
	}

	
	/**
	 * @param transactionId
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	protected void setTransactionParameter(String transactionId, HttpServletRequest request, MiniTransactionModel _miniTransactionModel) throws FrameworkCheckedException {
			
		MiniTransactionModel miniTransactionModel = null;
		
		if(_miniTransactionModel == null) {
			
			TransactionCodeModel transactionCodeModel = loadTransactionCodeByCode(transactionId);
			
			if(transactionCodeModel != null) {
				
				miniTransactionModel =	loadMiniTransactionModelByTrId(transactionCodeModel, request);	
			}
			
		} else {
			
			miniTransactionModel = _miniTransactionModel;
		}

		if(miniTransactionModel != null) {

			String transactionType = getTransactionType(miniTransactionModel,null);
			
			if(transactionType.equals(CASH_WITHDRAW)) {
				
				updateCWRequestParameter(miniTransactionModel, request);
				
			} else if(transactionType.equals(ACCOUNT_TO_CASH)) {
				
				updateA2CRequestParameter(miniTransactionModel, request);
				
			} else if(transactionType.equals(CASH_TO_CASH)) {
				
				updateC2CRequestParameter(miniTransactionModel, request);
			}
		}
	}
	
	/**
	 * @param transactionId
	 * @param request
	 * @throws FrameworkCheckedException
	 */
	private void updateCWRequestParameter(MiniTransactionModel miniTransactionModel, HttpServletRequest request) throws FrameworkCheckedException {
			
		request.setAttribute(CommandFieldConstants.KEY_TX_ID, miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
		request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(miniTransactionModel.getCreatedOn().getTime()));
		request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(miniTransactionModel.getCreatedOn().getTime()));
		request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(miniTransactionModel.getBAMT().doubleValue()));
		request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
		request.setAttribute("TOTAL_AMOUNT", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTAMT().doubleValue())));
		request.setAttribute("MS_ISDN_MOBILE", miniTransactionModel.getMobileNo());
		request.setAttribute(CommandFieldConstants.KEY_PROD_ID, 50006);		
		
		
		AppUserModel cusModel = new AppUserModel();
		cusModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		cusModel.setMobileNo(miniTransactionModel.getMobileNo());
		SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		sBaseWrapper.setBasePersistableModel(cusModel);
		sBaseWrapper = commonCommandManager.searchAppUserByExample(sBaseWrapper);
		
		
			if (sBaseWrapper != null && sBaseWrapper.getBasePersistableModel()!= null) {			
		
				cusModel = (AppUserModel) sBaseWrapper.getBasePersistableModel();
				
				if(cusModel.getFirstName().equalsIgnoreCase(cusModel.getLastName())) {
					
					request.setAttribute("CUSTOMER_NAME", cusModel.getFirstName());
					
				} else {
					
					request.setAttribute("CUSTOMER_NAME", cusModel.getFirstName() + " " + cusModel.getLastName());
				}			
			}
		}
	
	
	private void updateA2CRequestParameter(MiniTransactionModel miniTransactionModel, HttpServletRequest request) throws FrameworkCheckedException {
	    
	    request.setAttribute(CommandFieldConstants.KEY_TX_ID, miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
	    request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(miniTransactionModel.getBAMT().doubleValue()));
	    request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
	    request.setAttribute("TOTAL_AMOUNT", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTAMT().doubleValue())));
	    request.setAttribute("MS_ISDN_MOBILE", miniTransactionModel.getMobileNo());
		request.setAttribute("CUSTOMER_NAME", "Walkin Customer");
		
		TransactionDetailModel transactionDetailModel = 
								loadTransactionDetailModel(miniTransactionModel.getTransactionCodeIdTransactionCodeModel(), request);
		
		if(transactionDetailModel != null) {
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN, transactionDetailModel.getCustomField5());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN, transactionDetailModel.getCustomField6());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC, transactionDetailModel.getCustomField7());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC, transactionDetailModel.getCustomField9());
			request.setAttribute(CommandFieldConstants.KEY_PROD_ID, transactionDetailModel.getProductId());
		}	
	}
	
	
	private void updateC2CRequestParameter(MiniTransactionModel miniTransactionModel, HttpServletRequest request) throws FrameworkCheckedException {
	    
	    request.setAttribute(CommandFieldConstants.KEY_TX_ID, miniTransactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
	    request.setAttribute("TRANSACTION_DATE", DateTimeFormat.forPattern("dd/MM/yyyy").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_TIME", DateTimeFormat.forPattern("h:mm a").print(miniTransactionModel.getCreatedOn().getTime()));
	    request.setAttribute("TRANSACTION_AMOUNT", Formatter.formatDouble(miniTransactionModel.getBAMT().doubleValue()));
	    request.setAttribute("DEDUCTION_AMOUNT", Formatter.formatDouble( miniTransactionModel.getTPAM().doubleValue()));
	    request.setAttribute("TOTAL_AMOUNT", Double.parseDouble( Formatter.formatDouble( miniTransactionModel.getTAMT().doubleValue())));
	    request.setAttribute("MS_ISDN_MOBILE", miniTransactionModel.getMobileNo());
		request.setAttribute("CUSTOMER_NAME", "Walkin Customer");
		
		TransactionDetailModel transactionDetailModel = 
								loadTransactionDetailModel(miniTransactionModel.getTransactionCodeIdTransactionCodeModel(), request);
		
		if(transactionDetailModel != null) {
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_MSISDN, transactionDetailModel.getCustomField5());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_MSISDN, transactionDetailModel.getCustomField6());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_SENDER_CNIC, transactionDetailModel.getCustomField7());
			request.setAttribute(CommandFieldConstants.KEY_WALKIN_RECEIVER_CNIC, transactionDetailModel.getCustomField9());
			request.setAttribute(CommandFieldConstants.KEY_PROD_ID, transactionDetailModel.getProductId());
		}			
	}
	
	
	/**
	 * @param miniTransactionModel
	 * @return
	 */
	private String getTransactionType(MiniTransactionModel miniTransactionModel,TransactionDetailMasterModel tdm) {

	    String transactionType = null;
	    
	    if(miniTransactionModel != null && miniTransactionModel.getCommandId() != null) {
	    	
	    	if(String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_ACCOUNT_TO_CASH)) {
	    	
	    		transactionType = ACCOUNT_TO_CASH;
	    	}
	    	
	    	if(String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_CASH_TO_CASH_INFO)) {
	    	
	    		transactionType = CASH_TO_CASH;
	    	}
	    	
	    	if(String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_MINI_CASHOUT)) {
	    	
	    		transactionType = CASH_WITHDRAW;
	    	}
	    	
	    	if(String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_BULK_PAYMENT)) {
	    	
	    		transactionType = BULK_PAYMENT_WITHDRAW;
	    	}

			if(String.valueOf(miniTransactionModel.getCommandId()).equals(CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_REQUEST)) {

				transactionType = CASH_WITHDRAW;
			}
	    }
	    else if(tdm != null)
		{
			if(tdm.getProductId().equals(50011L))
				transactionType = CASH_TO_CASH;
		}
	    
	    return transactionType;
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
