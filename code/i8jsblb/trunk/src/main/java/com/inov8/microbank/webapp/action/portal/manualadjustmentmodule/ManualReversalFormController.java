package com.inov8.microbank.webapp.action.portal.manualadjustmentmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.BBAccountsViewModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.vo.transactionreversal.ManualReversalVO;
import com.inov8.microbank.server.facade.portal.commissionmodule.CommissionTransactionViewFacade;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualReversalManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.EncryptionUtil;
import com.inov8.ola.util.TransactionTypeConstants;
import com.thoughtworks.xstream.XStream;

public class ManualReversalFormController extends AdvanceAuthorizationFormController {
    
	@Autowired
	private ManualReversalManager manualReversalFacade;
	@Autowired
    private ReferenceDataManager referenceDataManager;
	@Autowired
	private TransactionDetailMasterManager transactionDetailMasterManager;
	@Autowired
	private CommissionTransactionViewFacade commissionTransactionViewFacade;
	@Autowired
	private PortalOlaFacade portalOlaFacade;
	@Autowired
	private WorkFlowExceptionTranslator workflowExceptionTranslator;
	@Autowired
	private ManualAdjustmentManager manualAdjustmentManager;

	public ManualReversalFormController() {
		setCommandName("manualReversalVO");
		setCommandClass(ManualReversalVO.class);
	}
	
	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest req) throws Exception {
		Map<String, Object> referenceDataMap = new HashMap<String, Object>(0);
	    return referenceDataMap;
	}
	
	
	@Override
	protected Object loadFormBackingObject(HttpServletRequest req) throws Exception {
		
		String trxCode = ServletRequestUtils.getStringParameter(req, "trxid");
		Integer adjustmentType = ServletRequestUtils.getIntParameter(req, "adjtype");
		
		ManualReversalVO manualReversalVO = new ManualReversalVO();
		manualReversalVO.setTransactionCode(trxCode);
		manualReversalVO.setAdjustmentType(adjustmentType);
		boolean isReversal = (adjustmentType != null && adjustmentType == 1) ? true : false;
		
		if(!StringUtil.isNullOrEmpty(trxCode)){
			logger.info("Going to load details against transactionCode:"+trxCode);
			
			TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
			transactionDetailMasterModel.setTransactionCode(trxCode);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
			this.transactionDetailMasterManager.loadTransactionDetailMasterModel(baseWrapper);
			transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
			if(transactionDetailMasterModel == null || transactionDetailMasterModel.getTransactionId() == null){
				super.saveMessage(req, "Invalid transaction ID");
				manualReversalVO.setTransactionCode("");
				return manualReversalVO;
			}
			prepareVOFromMasterModel(manualReversalVO, transactionDetailMasterModel);
			
			CommissionTransactionViewModel commissionTransactionViewModel = new CommissionTransactionViewModel();
			commissionTransactionViewModel.setTransactionId(trxCode);
			List<CommissionTransactionViewModel> list = this.commissionTransactionViewFacade.searchCommissionTransactionView(commissionTransactionViewModel);
			manualReversalVO.setCommissionTransactionList(list);
			
			BbStatementAllViewModel bbStatementAllViewModel = new BbStatementAllViewModel();
			bbStatementAllViewModel.setTransactionCode(trxCode);
			LinkedHashMap<String,SortingOrder> sortingMap = new LinkedHashMap<String,SortingOrder>();
			sortingMap.put("category", SortingOrder.ASC);
			sortingMap.put("ledgerId", SortingOrder.ASC);
			SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
			sbWrapper.setSortingOrderMap(sortingMap); 
			sbWrapper.setBasePersistableModel(bbStatementAllViewModel);
			List<BbStatementAllViewModel>  settlementBBStatementList =  portalOlaFacade.searchBbStatementAllView(sbWrapper).getResultsetList();
			if(null!=settlementBBStatementList && settlementBBStatementList.size()>0){
				for (BbStatementAllViewModel modelInList : settlementBBStatementList) {
					modelInList.setAccountNumber(EncryptionUtil.decryptAccountNo( modelInList.getAccountNumber()));
					boolean isDebit = (modelInList.getDebitAmount() != null && modelInList.getDebitAmount() > 0) ? true : false;
					if(isDebit){
						modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getDebitAmount()) : "");
						modelInList.setCreditAmount(null);
						modelInList.setTransactionType(isReversal ? TransactionTypeConstants.CREDIT : null); // reverse the action for reversal only
					}else{
						modelInList.setAmountStr(isReversal ? Formatter.formatDouble(modelInList.getCreditAmount()) : "");
						modelInList.setDebitAmount(null);
						modelInList.setTransactionType(isReversal ? TransactionTypeConstants.DEBIT : null); // reverse the action for reversal only
					}
				}
			}
			manualReversalVO.setFundTransferEntryList(settlementBBStatementList);
			
		}
		
		return manualReversalVO;
	}
	
	@Override
	protected ModelAndView onCreate(HttpServletRequest req, HttpServletResponse res, Object model, BindException errors) throws Exception {
		ManualReversalVO manualReversalVO = (ManualReversalVO) model;
		String oldTrxCode = manualReversalVO.getTransactionCode();
		
		try{
			if( ! this.validateManualReversalForm(manualReversalVO, req)){
				return super.showForm(req, res, errors);
			}
			this.prepareInitiatorDetails(manualReversalVO);
			manualReversalFacade.makeReversal(manualReversalVO);
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			String defaultErrorMessage = super.getText("manualadjustment.add.failure", req.getLocale());
			String errorMessage = translateToProperErrorMessage(ex, defaultErrorMessage);
			logger.error("Translated Error message: "+ errorMessage);
			manualReversalVO.setTransactionCode(oldTrxCode);
			super.saveMessage(req, errorMessage);
			return super.showForm(req, res, errors);
		}
		
		super.saveMessage(req, super.getText("manualreversal.add.success", req.getLocale()));
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_manualreversal.html"));
		return modelAndView;
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest req, HttpServletResponse res, Object obj, BindException errors) throws Exception {
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_manualreversal.html?actionId=2"));
		return modelAndView;
	}
	
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest req, HttpServletResponse res,Object command, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		ManualReversalVO manualReversalVO = (ManualReversalVO) command;
		String oldTrxCode = manualReversalVO.getTransactionCode();
		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(req, "resubmitRequest",false);
		Long usecaseId= ServletRequestUtils.getLongParameter(req, "usecaseId");
		
		Long actionAuthorizationId = null;
		if(resubmitRequest){
			actionAuthorizationId=ServletRequestUtils.getLongParameter(req, "actionAuthorizationId");
		}
		
		try{
			if( ! validateManualReversalForm(manualReversalVO, req)){
				return super.showForm(req, res, errors);
			}
			this.prepareInitiatorDetails(manualReversalVO);

			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(manualReversalVO);
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
			String referenceId = StringUtil.isNullOrEmpty(manualReversalVO.getTransactionCode()) ? "" : manualReversalVO.getTransactionCode();
			String makerComments = StringUtil.isNullOrEmpty(manualReversalVO.getComments()) ? "" : manualReversalVO.getComments();
			
			if(nextAuthorizationLevel.intValue()<1){
					
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.TRANSACTION_REVERSAL_USECASE_ID));
				
				this.manualReversalFacade.makeReversal(manualReversalVO/*, actionAuthorizationId*/);
				
				String msg = super.getText("manualreversal.add.success", req.getLocale());
				this.saveMessage(req, msg);
					
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,makerComments, refDataModelString,null,usecaseModel,actionAuthorizationId,req);
				this.saveMessage(req,"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
			}else{
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,makerComments, refDataModelString,null,usecaseModel.getUsecaseId(),referenceId,resubmitRequest,actionAuthorizationId,req);
				this.saveMessage(req,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
			}
		
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			String defaultErrorMessage = super.getText("manualadjustment.add.failure", req.getLocale());
			String errorMessage = translateToProperErrorMessage(ex, defaultErrorMessage);
			logger.error("Translated Error message: "+ errorMessage);
			manualReversalVO.setTransactionCode(oldTrxCode);
			super.saveMessage(req, errorMessage);
			return super.showForm(req, res, errors);
		}
		
		ModelAndView modelAndView = new ModelAndView( new RedirectView("p_manualreversal.html"));
		return modelAndView;
	}
	
	private Double formatAmount(Double amount){
		if(amount == null){
			return 0.0D;
		}else{
			String amt = Formatter.formatDouble(amount);
			return Double.parseDouble(amt);
		}
	}

	private Double parseAmount(String amountStr){
		if(StringUtil.isNullOrEmpty(amountStr)){
			return 0.0D;
		}else{
			return Double.parseDouble(amountStr);
		}
	}

	private void prepareInitiatorDetails(ManualReversalVO manualReversalVO){
		String firstName = UserUtils.getCurrentUser().getFirstName();
        String lastName = UserUtils.getCurrentUser().getLastName();
        lastName = ((StringUtil.isNullOrEmpty(lastName)) ? "" : " " + lastName);
        Long appUserId = UserUtils.getCurrentUser().getAppUserId();
        
        manualReversalVO.setInitiatorAppUserId(appUserId);
        manualReversalVO.setInitiatorName(firstName + lastName);
	}

	private void prepareVOFromMasterModel(ManualReversalVO manualReversalVO, TransactionDetailMasterModel transactionDetailMasterModel){
		
		manualReversalVO.setExclusiveCharges(transactionDetailMasterModel.getExclusiveCharges());
		manualReversalVO.setInclusiveCharges(transactionDetailMasterModel.getInclusiveCharges());
		manualReversalVO.setProductId(transactionDetailMasterModel.getProductId());
		manualReversalVO.setProductName(transactionDetailMasterModel.getProductName());
		manualReversalVO.setSupProcessingStatusId(transactionDetailMasterModel.getSupProcessingStatusId());
		manualReversalVO.setSupProcessingStatusName(transactionDetailMasterModel.getProcessingStatusName());
		manualReversalVO.setTransactionAmount(transactionDetailMasterModel.getTransactionAmount());
		manualReversalVO.setTransactionDate(transactionDetailMasterModel.getCreatedOn());
		
	}
	
	private boolean validateManualReversalForm(ManualReversalVO manualReversalVO, HttpServletRequest req){
		
		Integer adjustmentType = manualReversalVO.getAdjustmentType();
		if(adjustmentType == null || adjustmentType.intValue() == 0){
			logger.error("Adjustment Type empty");
			super.saveMessage(req, super.getText("manualreversal.error.empty.adj.type", req.getLocale()));
			return false;
		}
		
		if(adjustmentType.intValue() == 1 && StringUtil.isNullOrEmpty(manualReversalVO.getTransactionCode()) ){ // Reversal
			logger.error("Transaction ID missing");
			super.saveMessage(req, super.getText("manualreversal.error.empty.trx.id", req.getLocale()));
			return false;
		}
		
		if(!StringUtil.isNullOrEmpty(manualReversalVO.getTransactionCode())){
			TransactionDetailMasterModel tdmModel = null;
			try {
				tdmModel = this.manualReversalFacade.getTransactionDetailMasterModel(manualReversalVO.getTransactionCode());
			} catch (FrameworkCheckedException e) {
				logger.error("Exception while loading TransactionDetailMasterModel for trx ID:"+manualReversalVO.getTransactionCode());
			}
			if(tdmModel == null){
				logger.error("Invalid Trx ID provided:"+manualReversalVO.getTransactionCode());
				super.saveMessage(req, super.getText("manualreversal.error.transaction.id", req.getLocale()));
				return false;
			}
			
			if(SupplierProcessingStatusConstants.REVERSE_COMPLETED.longValue() == tdmModel.getSupProcessingStatusId() && adjustmentType.intValue() == 1){
				logger.error("Transaction status is already Reverse Completed for trx ID:"+manualReversalVO.getTransactionCode());
				super.saveMessage(req, super.getText("manualreversal.error.already.reversed", req.getLocale()));
				return false;
			}
			
			// 08-MAR-2017 check commented on request of JSBL
			/*if(adjustmentType.intValue() == 1 && 
					(tdmModel.getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH || 
						tdmModel.getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER ||
						tdmModel.getProductId().longValue() == ProductConstantsInterface.BULK_PAYMENT
							) && (tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.COMPLETED ||
							tdmModel.getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.REVERSE_COMPLETED)){
				
				logger.error("Transaction reversal is not allowed for " + tdmModel.getProductName() +  "  with status completed / reversed-completed");
				super.saveMessage(req,"Transaction reversal is not allowed for " + tdmModel.getProductName() + " with status completed / reversed-completed ");
				return false;
			}*/
			
		}
		
		
		List<BbStatementAllViewModel> entryList = manualReversalVO.getFundTransferEntryList();
		if(entryList==null || entryList.size() == 0){
			logger.error("Entry list is empty");
			super.saveMessage(req, super.getText("manualreversal.error.empty.list", req.getLocale()));
			return false;
		}
		
		double totalDebit = 0;
		double totalCredit = 0;
		double fmtAmount = 0;
		
		for(BbStatementAllViewModel entry: entryList){
			BBAccountsViewModel model = new BBAccountsViewModel();
			if(StringUtil.isNullOrEmpty(entry.getAccountNumber())){
				logger.error("Account Number is empty");
				super.saveMessage(req, super.getText("manualreversal.error.empty.account.no", req.getLocale()));
				return false;
			}else{
				try {
					model.setAccountNumber(EncryptionUtil.encryptWithDES(entry.getAccountNumber()));
					model = manualAdjustmentManager.getBBAccountsViewModel(model);
					if(model !=null){
						if(model.getTitle() == null || model.getTitle().equals("null") || model.getTitle().trim().equals("")){
							logger.error("Account does not exist against the  account number : " + entry.getAccountNumber());
							super.saveMessage(req, "Account does not exist against the account number : " + entry.getAccountNumber());
							return false;
						}else if(model.getAccountTypeId() != null && model.getAccountTypeId() == CustomerAccountTypeConstants.SETTLEMENT){
							logger.info("Settlement Acc Type loaded against accNumber:"+entry.getAccountNumber()+" ... so SKIPPING account status/active check");
						}else{
							if( model.getIsActive()== null || !model.getIsActive() ){
								logger.error("Account is not active against the account number : " + entry.getAccountNumber());
								super.saveMessage(req, "Account is not active against the account number : " + entry.getAccountNumber());
								return false;
							}
						if(model.getStatusId() == null || model.getStatusId().longValue() != 1){
								logger.error("Account Status is not active against the account number : " + entry.getAccountNumber() );
								super.saveMessage(req, "Account Status is not active against the account number : " + entry.getAccountNumber());
								return false;
						
							}
						if (model.getAcState() != null && model.getAcState().equalsIgnoreCase("CLOSED")){
								logger.error("Account is CLOSED against the account number : " + entry.getAccountNumber());
								super.saveMessage(req, "Account is CLOSED against the account number : " + entry.getAccountNumber());
								return false;
						
							}
						}
						
						}
					
					else{
						logger.error(entry.getAccountNumber() + "Account Number does not exist");
						super.saveMessage(req, super.getText(entry.getAccountNumber() + "manualreversal.error.notexist.account.no", req.getLocale()));
						return false;
					}
				}	
			 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					super.saveMessage(req,"Account no is not valid");
					return false;
				}
			}
			if(StringUtil.isNullOrEmpty(entry.getAmountStr())){
				logger.error("Amount is empty");
				super.saveMessage(req, super.getText("manualreversal.error.empty.amount", req.getLocale()));
				return false;
			}else{
				try{
					entry.setAmount(parseAmount(entry.getAmountStr()));
				}catch(NumberFormatException e){
					logger.error("Manual Reversal: Invalid Amount:"+entry.getAmountStr(),e);
					super.saveMessage(req, super.getText("manualreversal.error.amount.invalid", req.getLocale()));
					return false;
				}
				fmtAmount = formatAmount(entry.getAmount());
				entry.setAmount(fmtAmount);
				if(fmtAmount < 0.01D){
					logger.error("Manual Reversal: Amount cannot be less than 0.01");
					super.saveMessage(req, super.getText("manualreversal.error.amount.lower", req.getLocale()));
					return false;
				}
				if(fmtAmount > 999999999999.99D){
					logger.error("Manual Reversal: Amount cannot be greater than 999999999999.99");
					super.saveMessage(req, super.getText("manualreversal.error.amount.upper", req.getLocale()));
					return false;
				}
			}
			if(entry.getTransactionType() == null){
				logger.error("Transaction type is empty");
				super.saveMessage(req, super.getText("manualreversal.error.empty.trx.type", req.getLocale()));
				return false;
			}
			
			if(entry.getTransactionType().longValue() == TransactionTypeConstants.DEBIT){
				totalDebit += fmtAmount;
			}else{
				totalCredit += fmtAmount;
			}
		}
		
		if(totalDebit == 0){
			logger.error("Total debit amount is 0");
			super.saveMessage(req, super.getText("manualreversal.error.amount.debit", req.getLocale()));
			return false;
		}else if(totalCredit == 0){
			logger.error("Total credit amount is 0");
			super.saveMessage(req, super.getText("manualreversal.error.amount.credit", req.getLocale()));
			return false;
		}else if(formatAmount(totalDebit).longValue() != formatAmount(totalCredit).longValue()){
			logger.error("Total debit and credit amount is not equal... Total Debit:"+totalDebit+" Total Credit:"+totalCredit);
			super.saveMessage(req, super.getText("manualreversal.error.amount.not.equal", req.getLocale()));
			return false;
		}
		
		manualReversalVO.setTotalAmount(totalDebit);
		
		return true;
		
	}

	private String translateToProperErrorMessage(Exception e, String defaultError){
		String errorMessage = StringUtil.isNullOrEmpty(e.getMessage()) ? defaultError : e.getMessage();
		if(StringUtil.isFailureReasonId(e.getMessage())){
			WorkFlowException wfEx = this.workflowExceptionTranslator.translateWorkFlowException(new WorkFlowException(e.getMessage()),FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
			errorMessage = wfEx.getMessage();
		}		
		return errorMessage;
	}
	
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}

	public void setCommissionTransactionViewFacade(CommissionTransactionViewFacade commissionTransactionViewFacade) {
		this.commissionTransactionViewFacade = commissionTransactionViewFacade;
	}

	public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
		this.portalOlaFacade = portalOlaFacade;
	}

	public void setManualReversalFacade(ManualReversalManager manualReversalFacade) {
		this.manualReversalFacade = manualReversalFacade;
	}

	public void setWorkflowExceptionTranslator(WorkFlowExceptionTranslator workflowExceptionTranslator) {
		this.workflowExceptionTranslator = workflowExceptionTranslator;
	}

	public void setManualAdjustmentManager(
			ManualAdjustmentManager manualAdjustmentManager) {
		this.manualAdjustmentManager = manualAdjustmentManager;
	}

}