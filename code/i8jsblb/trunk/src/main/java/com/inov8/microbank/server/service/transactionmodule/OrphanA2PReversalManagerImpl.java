package com.inov8.microbank.server.service.transactionmodule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.XMLUtil;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.exception.WorkFlowExceptionTranslator;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.ActionStatusConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ReasonConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.TransactionConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.util.XPathConstants;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.thoughtworks.xstream.XStream;


public class OrphanA2PReversalManagerImpl implements OrphanA2PReversalManager{
	private static Logger logger = Logger.getLogger(OrphanA2PReversalManagerImpl.class);

	private AppUserManager appUserManager;
	private TransactionModuleManager transactionManager;
	
	@Autowired
	private FinancialIntegrationManager financialIntegrationManager;
	@Autowired
	private SmartMoneyAccountManager smartMoneyAccountManager;
	@Autowired
	private TransactionDetailMasterManager transactionDetailMasterManager;
	@Autowired
	private WorkFlowExceptionTranslator workflowExceptionTranslator;
	@Autowired
	private SettlementManager settlementManager;
	@Autowired
	private ActionLogManager actionLogManager;
	@Autowired
	private CommissionManager commissionManager;
	@Autowired
	private UserDeviceAccountsManager userDeviceAccountsManager;
	@Autowired
	private TransactionDetailMasterDAO transactionDetailMasterDAO;

	
	public WorkFlowWrapper makeAccountToCashReversal(TransactionReversalVo txReversalVo) throws FrameworkCheckedException{

		String transactionCode = txReversalVo.getTransactionCode();
		logger.info("[OrphanA2PReversalManagerImpl.makeAccountToCashReversal] Going to settle transaction of customer CNIC by transaction Id : "+transactionCode);
		
		try{
			TransactionDetailMasterModel txDetailMasterModel = new TransactionDetailMasterModel();
			txDetailMasterModel.setTransactionCode(transactionCode);
	        CustomList<TransactionDetailMasterModel> customList = this.transactionDetailMasterDAO.findByExample(txDetailMasterModel,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
	        
	        if(customList != null && customList.getResultsetList() != null && customList.getResultsetList().size() > 0 ){
	        	txDetailMasterModel = customList.getResultsetList().get(0) ;
	        }
	        
	        if(txDetailMasterModel == null || txDetailMasterModel.getSupProcessingStatusId() == null){
				logger.error("Unable to load TransactionDetailMaster for Transaction ID:"+transactionCode);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
			}
	        /*if(txDetailMasterModel.getMfsId() == null){
				logger.error("TransactionDetailMaster.MfsId is null for Transaction ID:"+transactionCode);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
			}*/
	        if(SupplierProcessingStatusConstants.REVERSED.longValue() != txDetailMasterModel.getSupProcessingStatusId()){
				logger.error("Transaction ID:" + transactionCode + " - SupProcessingStatusId is not "+SupplierProcessingStatusConstants.REVERSED+" in TransactionDetailMaster.");
				throw new FrameworkCheckedException(MessageUtil.getMessage("A2P_TRX_INVALID_STATUS"));
			}
			
			Integer redemptionType = txDetailMasterModel.getRedemptionType();
			/*if(redemptionType == null){
				logger.error("Redemption Type missing in TransactionDetailMaster for Transaction ID:"+txDetailMasterModel.getTransactionCode());
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
			}*/

	        
	        //ActionLog Start
			ActionLogModel actionLogModel = new ActionLogModel();
			XStream xstream = new XStream();
			String xml = xstream.toXML("");
			actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.BULK_DISBURSEMENT);
			actionLogModel.setInputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogBeforeStart(actionLogModel);

			
			//Load Customer AppUserModel 
			AppUserModel customerAppUserModel = new AppUserModel();
			customerAppUserModel.setMobileNo(txDetailMasterModel.getSaleMobileNo());
			customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);
			
			if(customerAppUserModel == null || customerAppUserModel.getAppUserId() == null){
				logger.error("Unable to load customer info for Transaction ID:"+transactionCode);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
			}
			
			CustomerModel customerModel = customerAppUserModel.getCustomerIdCustomerModel();

			//Customer SMA
			SmartMoneyAccountModel customerSMA = new SmartMoneyAccountModel();
			customerSMA.setCustomerId(customerAppUserModel.getCustomerId());
			CustomList<SmartMoneyAccountModel> smaList = smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(customerSMA);
			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) {
				customerSMA = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
			}else{
				logger.error("Unable to load Customer Smart Money Account Info against Transaction ID:"+transactionCode);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
			}
			
			BaseWrapper bWrapper = new BaseWrapperImpl();

			//Customer UserDeviceAccounts
			UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
			userDeviceAccountsModel.setAppUserId(customerAppUserModel.getAppUserId());
			userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
			bWrapper.setBasePersistableModel(userDeviceAccountsModel);
			bWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(bWrapper);
			UserDeviceAccountsModel customerUDAModel = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
			if(customerUDAModel == null || customerUDAModel.getUserDeviceAccountsId() == null){
				logger.error("Unable to load Customer User Device Account Info against Transaction ID:"+transactionCode);
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
			}
			
			ThreadLocalAppUser.setAppUserModel(UserUtils.getCurrentUser());
			
			logger.info("****** Going to transfer Funds from Redemption A/C to Customer A/C for TransactionID:"+transactionCode);
			WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
			workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
			workFlowWrapper.setSmartMoneyAccountModel(customerSMA);
			workFlowWrapper.setAccountInfoModel(new AccountInfoModel());
			workFlowWrapper.setOlaSmartMoneyAccountModel(customerSMA);
			workFlowWrapper.setCustomerModel(customerModel);
			workFlowWrapper.setTransactionDetailMasterModel(txDetailMasterModel);
			workFlowWrapper.setAppUserModel(ThreadLocalAppUser.getAppUserModel());

			ProductModel productModel = new ProductModel();
			productModel.setProductId(txDetailMasterModel.getProductId());
			workFlowWrapper.setProductModel(productModel);
			
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	        TransactionModel transactionModel = new TransactionModel();
	        transactionModel.setTransactionId(txDetailMasterModel.getTransactionId());
	        searchBaseWrapper.setBasePersistableModel(transactionModel);
	        searchBaseWrapper = this.transactionManager.loadTransaction(searchBaseWrapper);    

	        if(searchBaseWrapper.getBasePersistableModel() != null){
	        	transactionModel = (TransactionModel) searchBaseWrapper.getBasePersistableModel();
	        	workFlowWrapper.setTransactionModel(transactionModel);
	        	workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
		        List<TransactionDetailModel> txdetails = (List<TransactionDetailModel>) transactionModel.getTransactionIdTransactionDetailModelList();
		        if(txdetails != null && !txdetails.isEmpty()){
		        	workFlowWrapper.setTransactionDetailModel(txdetails.get(0));
		        }else{
		        	logger.error("[OrphanA2PReversalManagerImpl.makeAccountToCashReversal] Unable to load TransactionDetailModel for transactionId:"+txDetailMasterModel.getTransactionId());
					throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
		        }
	        }else{
	        	logger.error("[OrphanA2PReversalManagerImpl.makeAccountToCashReversal] Unable to load TransactionModel for transactionId:"+txDetailMasterModel.getTransactionId());
				throw new FrameworkCheckedException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
	        }
			
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(workFlowWrapper);
			switchWrapper.setBasePersistableModel(customerSMA);
	        switchWrapper.setBankId( txDetailMasterModel.getBankId() );
	        switchWrapper.setPaymentModeId( txDetailMasterModel.getPaymentModeId() );

			try{
				workFlowWrapper.setCurrentSupProcessingStatusId(txDetailMasterModel.getSupProcessingStatusId());
				workFlowWrapper.setLeg2Transaction(true);
				
				CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetailsUnsettled(txDetailMasterModel.getTransactionDetailId());
				workFlowWrapper.setCommissionAmountsHolder(commissionAmountsHolder);
				
				settlementManager.updateCommissionTransactionSettled(txDetailMasterModel.getTransactionDetailId());
				
				boolean settleCommission = (txReversalVo.getRedemptionType() != null && txReversalVo.getRedemptionType() == 2) ? true : false;
				
			    BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel(customerSMA);

				OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

				switchWrapper = olaFinancialInstitution.performLeg2(switchWrapper,
						ReasonConstants.SENDER_REDEEM,
						TransactionConstantsInterface.SENDER_REDEEM_CATEGORY_ID,
						settleCommission);

				workFlowWrapper.setOLASwitchWrapper(switchWrapper);
			}catch(WorkFlowException ex){
				throw translateException(ex);
			}

			String brandName = MessageUtil.getMessage("jsbl.brandName");
			ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(3);
			double customerBalance	= switchWrapper.getOlavo().getToBalanceAfterTransaction();
			Double reverseAmount = (Double) switchWrapper.getObject(CommandFieldConstants.KEY_TX_AMOUNT);
			
			String customerSms = MessageUtil.getMessage(
					"A2P_TRX_REVERSAL_CUSTOMER_SMS",
					new Object[]{
							brandName,
							Formatter.formatDouble(reverseAmount),
							workFlowWrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(customerBalance)
					});
			
			messageList.add(new SmsMessage(workFlowWrapper.getCustomerAppUserModel().getMobileNo(), customerSms));
			workFlowWrapper.getTransactionDetailModel().setCustomField10(customerSms);
			
			workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
			
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			
			transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);
			baseWrapper.setBasePersistableModel(transactionModel);
			transactionManager.updateTransaction(baseWrapper);
						
			txDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);
			txDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.REVERSE_COMPLETED_NAME);
			txDetailMasterModel.setRedemptionType(txReversalVo.getRedemptionType());
			txDetailMasterModel.setUpdatedOn(new Date());
			
			transactionDetailMasterManager.saveTransactionDetailMaster(txDetailMasterModel);
			
			settlementManager.prepareDataForDayEndSettlement(workFlowWrapper);

			//ActionLog End
			actionLogModel.setAppUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
			actionLogModel.setOutputXml(XMLUtil.replaceElementsUsingXPath(xml,XPathConstants.actionLogInputXMLLocationSteps));
			this.actionLogAfterEnd(actionLogModel);

			logger.info("[OrphanA2PReversalManagerImpl.makeAccountToCashReversal] Successful completion...");
			
			return workFlowWrapper;

		}catch (Exception e){
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage(),e);
		}
		
	}
	
	private WorkFlowException translateException(WorkFlowException ex){
		String errorMessage = ex.getMessage();
		WorkFlowException outputException = null;
		try{
			if(errorMessage != null && errorMessage.length() == 4 && StringUtil.isInteger(errorMessage)){
				outputException = this.workflowExceptionTranslator.translateWorkFlowException(ex,this.workflowExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
			}
		}catch(Exception e){
			logger.error("Exception while translating the exception...",e);
		}
		
		if(outputException != null){
			return outputException;
		}else{
			return ex;
		}

	}
	
	
	private void actionLogBeforeStart(ActionLogModel actionLogModel) {

		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
		actionLogModel.setStartTime(new Timestamp(new java.util.Date().getTime()));
		actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
		if (actionLogModel.getActionLogId() != null) {
			ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
		}
	}

	private void actionLogAfterEnd(ActionLogModel actionLogModel) {
		actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
		actionLogModel.setEndTime(new Timestamp(new java.util.Date().getTime()));
		insertActionLogRequiresNewTransaction(actionLogModel);
	}
	
	private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(actionLogModel);
		try {
			baseWrapper = this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
		} catch (Exception ex) {
			logger.error("Exception occurred while processing", ex);

		}
		return actionLogModel;
	}
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}
	
	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setTransactionManager(TransactionModuleManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
		this.transactionDetailMasterManager = transactionDetailMasterManager;
	}
	
	public void setWorkflowExceptionTranslator(WorkFlowExceptionTranslator workflowExceptionTranslator) {
		this.workflowExceptionTranslator = workflowExceptionTranslator;
	}
	
	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}
	
	public void setCommissionManager(CommissionManager commissionManager){
		this.commissionManager = commissionManager;
	}
	
	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager){
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}
	
	public void setTransactionDetailMasterDAO(TransactionDetailMasterDAO transactionDetailMasterDAO){
		this.transactionDetailMasterDAO = transactionDetailMasterDAO;
	}
	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager){
		this.financialIntegrationManager = financialIntegrationManager;
	}

}