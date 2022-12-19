package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

public class CollectionPaymentTransaction extends SalesTransaction {

	protected final Log log = LogFactory.getLog(getClass());
	private MessageSource messageSource;
	private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	
	private String OLA_FROM_ACCOUNT = "OLA_FROM_ACCOUNT";
	private String OLA_TO_ACCOUNT = "OLA_TO_ACCOUNT";
	
	
	private SupplierManager supplierManager;
	private CommissionManager commissionManager;
	private ProductDispenseController productDispenseController;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private ProductManager productManager;
//	private AppUserManager appUserManager;
	private VeriflyManagerService veriflyController;
	private SwitchController switchController;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private SupplierBankInfoManager supplierBankInfoManager;
	private OperatorManager operatorManager;
	private RetailerContactManager retailerContactManager;	
	private FinancialIntegrationManager financialIntegrationManager;
	private GenericDao genericDAO;
	private AbstractFinancialInstitution phoenixFinancialInstitution;
	private String rrnPrefix;
	private StakeholderBankInfoManager stakeholderBankInfoManager;
	private ActionLogManager actionLogManager = null;
	private TransactionReversalManager transactionReversalManager;
	
	
	
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {
	
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CollectionPaymentTransaction....");
		}
		String notificationMobileNo = _workFlowWrapper.getTransactionModel().getNotificationMobileNo();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		//generate new trasnaction code and initialize Transaction/TransctionDetailMaster objects.
		_workFlowWrapper = super.doPreStart(_workFlowWrapper);
		
	
		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		_workFlowWrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());
		
		// Populate Retailer Contact model from DB
		searchBaseWrapper = new SearchBaseWrapperImpl();
	    RetailerContactModel retailerContactModel = new RetailerContactModel();
	    retailerContactModel.setRetailerContactId( _workFlowWrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
	    searchBaseWrapper.setBasePersistableModel(retailerContactModel);
	    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
	    retailerContactModel = (RetailerContactModel) searchBaseWrapper.getBasePersistableModel();
	    _workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
	    	     
	    DistributorModel distributorModel = new DistributorModel();
	    distributorModel.setDistributorId(retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
	    _workFlowWrapper.setDistributorModel(distributorModel);
	    
		NotificationMessageModel notificationMessage = new NotificationMessageModel();
		notificationMessage.setNotificationMessageId(_workFlowWrapper.getProductModel().getInstructionId());
		baseWrapper.setBasePersistableModel(notificationMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		_workFlowWrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		NotificationMessageModel successMessage = new NotificationMessageModel();
		successMessage.setNotificationMessageId(_workFlowWrapper.getProductModel().getSuccessMessageId());
		baseWrapper.setBasePersistableModel(successMessage);
		baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		_workFlowWrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(_workFlowWrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		
		_workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		_workFlowWrapper.setPaymentModeModel(new PaymentModeModel());
		//_workFlowWrapper.getPaymentModeModel().setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
		_workFlowWrapper.getPaymentModeModel().setPaymentModeId(_workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Populate Supplier Bank Info Model
		
		SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
		supplierBankInfoModel.setSupplierId(_workFlowWrapper.getProductModel().getSupplierId());

		supplierBankInfoModel = this.supplierBankInfoManager.getSupplierBankInfoModel(supplierBankInfoModel);

		_workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModel);

		OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();

		if (supplierBankInfoModel != null) {
			// Populate Operator's Paying Bank Info Model
			operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
			operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
			operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
			baseWrapper.setBasePersistableModel(operatorBankInfoModel);
			_workFlowWrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		}


		// Populate Handler's Retailer Contact model from DB
		if(_workFlowWrapper.getHandlerModel() != null){
			searchBaseWrapper = new SearchBaseWrapperImpl();
			RetailerContactModel retailerContact = new RetailerContactModel();
			retailerContact.setRetailerContactId( _workFlowWrapper.getHandlerModel().getRetailerContactId() );
			searchBaseWrapper.setBasePersistableModel( retailerContact );
			searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
			_workFlowWrapper.setHandlerRetContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());

			// Populate the Handler OLA Smart Money Account from DB
			SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
			_workFlowWrapper.setHandlerSMAModel(sma);

			// Set Handler User Device Account Model
			UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
			handlerUserDeviceAccountsModel.setAppUserId(_workFlowWrapper.getHandlerAppUserModel().getAppUserId());
			handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			_workFlowWrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		}
		TransactionModel trxnModel = _workFlowWrapper.getTransactionModel();
		if(trxnModel != null){
			trxnModel.setNotificationMobileNo(notificationMobileNo);			
			_workFlowWrapper.setTransactionModel(trxnModel);	
		}
		
		logger.info("Ending doPreStart(WorkFlowWrapper _workFlowWrapper) of CollectionPaymentTransaction....");
		
		return _workFlowWrapper;
	
	}
	
	@Override
	protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper)
			throws Exception {
		
		//--------------------- Validates the Retailer's requirements -----------------------------
	    if (wrapper.getFromRetailerContactModel() != null)
	    {
		      if (!wrapper.getFromRetailerContactModel().getActive())
		      {
		        throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NOT_ACTIVE);
		      }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NULL);
	    }
	    
	    
		if (wrapper.getUserDeviceAccountModel() == null)
		{
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}

		// -------------------------------- Validates the Product's requirements
		// ---------------------------------
		if (wrapper.getProductModel() != null) {
			if (!wrapper.getProductModel().getActive()) {
		
				throw new WorkFlowException(
						WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
			}
		} else {
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}
		
		// -------------------------------- Validates the Product's requirements ---------------------------------------------------		
		if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue()) {
			if (!wrapper.getProductModel().getActive()) 
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_SERVICE_TYPE);
			}
		}

		// ----------------------- Validates the Supplier's requirements-------------------------
		if (wrapper.getProductModel().getSupplierIdSupplierModel() != null) {
			if (!wrapper.getProductModel().getSupplierIdSupplierModel()
					.getActive()) {
		
				throw new WorkFlowException(
						WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE);
			}
		} else {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.SUPPLIER_NULL);
		}


		// ------------------------- Validates the iNPUT's requirements
		if (wrapper.getBillAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
		}
		if (wrapper.getTotalCommissionAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
		}
		if (wrapper.getTxProcessingAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
		}


		// ----------------------- Validates the PaymentMode's requirements
		// --------------------------------------
		if (wrapper.getPaymentModeModel() != null) {
			if (wrapper.getPaymentModeModel().getPaymentModeId() <= 0) {
		
				throw new WorkFlowException("PaymentModeID is not supplied.");
			}
		}

		// ----------------------- Validates the Service's requirements
		// ---------------------------
		if (wrapper.getProductModel().getServiceIdServiceModel() != null) {
			if (!wrapper.getProductModel().getServiceIdServiceModel()
					.getActive()) {
		
				throw new WorkFlowException(
						WorkFlowErrorCodeConstants.SERVICE_INACTIVE);
			}
		} else {
		
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}
		
		
		logger.info("Ending doValidate(WorkFlowWrapper wrapper) of CollectionPaymentTransaction..");
		
		return wrapper;
	}
	
	
	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
	
		workFlowWrapper = super.doPreProcess(workFlowWrapper);
		
		TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
		transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		transactionModel.setRetailerId(  workFlowWrapper.getRetailerContactModel().getRetailerId());	
		transactionModel.setProcessingBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
		transactionModel.setTransactionAmount(workFlowWrapper.getBillAmount());
		transactionModel.setTotalAmount(workFlowWrapper.getBillAmount());
		transactionModel.setTotalCommissionAmount(0d);
		transactionModel.setDiscountAmount(0d);
		transactionModel.setTransactionTypeIdTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
		transactionModel.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
		transactionModel.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
		transactionModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		transactionModel.setNotificationMobileNo(workFlowWrapper.getTransactionModel().getNotificationMobileNo());
		Long smartMoneyAccountId = null;
		SmartMoneyAccountModel agentmartMoneyAccountModel = (SmartMoneyAccountModel) workFlowWrapper.getSmartMoneyAccountModel();
		smartMoneyAccountId = agentmartMoneyAccountModel.getSmartMoneyAccountId();
		
		transactionModel.setSmartMoneyAccountId(smartMoneyAccountId);
	
		//As always agent would perform transactions so logged-in user's mfsId would be used.
		String mfsId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
		transactionModel.setMfsId(mfsId);
		workFlowWrapper.setTransactionModel(transactionModel);		
		
		return workFlowWrapper;
	}
	
	
	@Override
	protected WorkFlowWrapper doSale(WorkFlowWrapper _workFlowWrapper) throws Exception {
		//Customer Mobile Number has been set in Custom Field in Command
		String notificationMobileNo = _workFlowWrapper.getTransactionModel().getNotificationMobileNo();
		TransactionCodeModel transactionCodeModel = _workFlowWrapper.getTransactionCodeModel();
		TransactionDetailMasterModel transactionDetailMasterModel = _workFlowWrapper.getTransactionDetailMasterModel();
        //Commenting TDM check as we are checking challan status in bill_status in Customer/CollectionPaymentCommand and info
        /*
		String workFlowConsumerNo;
        workFlowConsumerNo = (((UtilityBillVO) _workFlowWrapper.getProductVO()).getConsumerNo());
        if (workFlowConsumerNo != null && _workFlowWrapper.getProductModel().getProductCode()!=null ) {
            long count = 0;
            logger.info("CollectionPaymentTransaction:doSale:Validating Challan for consumerNo : "+workFlowConsumerNo+" productCode : "+_workFlowWrapper.getProductModel().getProductCode());
            count = transactionDetailMasterManager.getPaidChallan(workFlowConsumerNo,_workFlowWrapper.getProductModel().getProductCode());
            if(count > 0){
                if(SupplierProcessingStatusConstants.COMPLETED.equals(count))
                    throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.03"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
                else if(SupplierProcessingStatusConstants.PROCESSING.equals(count))
                    throw new CommandException("Transaction is already Processing.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
                else if(SupplierProcessingStatusConstants.IN_PROGRESS.equals(count))
                    throw new CommandException("Transaction is already in Progress.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
            }
        }*/
        if(transactionDetailMasterModel != null && transactionCodeModel != null)
		{
			transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.PROCESSING);
			transactionDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.PROCESSING_NAME);
			transactionDetailMasterModel.setTransactionCode(transactionCodeModel.getCode());
			transactionDetailMasterModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
			transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
			_workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
		}
		TransactionDetailModel txDetailModel = new TransactionDetailModel();
		Integer paymentType = (Integer) _workFlowWrapper.getCustomField();
		CommissionWrapper commissionWrapper = this.calculateCommission(_workFlowWrapper);
		CommissionAmountsHolder commissionAmounts = 
				(CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
	
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());		
		
		_workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
		_workFlowWrapper.setCommissionWrapper(commissionWrapper);		
		_workFlowWrapper.putObject("misc", paymentType.intValue() == 1 ? Boolean.TRUE : Boolean.FALSE);
		
		OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) financialIntegrationManager.loadFinancialInstitution(baseWrapper);	
		//olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(_workFlowWrapper);
		//------------------------------------------------------------------------------------
		_workFlowWrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
		_workFlowWrapper.getTransactionModel().setCreatedOn(new Date());
		_workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		txDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
		txDetailModel.setConsumerNo(((UtilityBillVO) _workFlowWrapper.getProductVO()).getConsumerNo());
		_workFlowWrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		_workFlowWrapper.getTransactionModel().setFromRetContactId(_workFlowWrapper.getRetailerContactModel().getRetailerContactId());
		_workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
		_workFlowWrapper.getTransactionDetailMasterModel().setRecipientMobileNo(notificationMobileNo); //TRB March 12, 2018 //VISIBLE SOLUTION (1103277) - Handlers Reporting
		// Set Handler Detail in Transaction and Transaction Detail Master
		if(_workFlowWrapper.getHandlerModel() != null){
			_workFlowWrapper.getTransactionModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
			_workFlowWrapper.getTransactionModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
			_workFlowWrapper.getTransactionDetailMasterModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
			_workFlowWrapper.getTransactionDetailMasterModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
		}

		txDetailModel.setSettled(Boolean.FALSE);
		_workFlowWrapper.setTransactionDetailModel(txDetailModel);
		_workFlowWrapper.getTransactionModel().setConfirmationMessage(" _ ");
		
		logger.info("Saving Transaction in DB....");
		txManager.saveTransaction(_workFlowWrapper);	
		
		//------------------------------------------------------------------------------------
		txDetailModel.setSettled(Boolean.FALSE);
		_workFlowWrapper.setTransactionDetailModel(txDetailModel);
		
		/*if (!_workFlowWrapper.getTransactionDetailModel().getConsumerNo().equalsIgnoreCase("")) {

			_workFlowWrapper.getTransactionModel().setConfirmationMessage(
					SMSUtil.buildBillSaleSMS(_workFlowWrapper.getInstruction().getSmsMessageText(), _workFlowWrapper.getProductModel().getName(), txAmount, seviceChargesAmount,
							_workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), _workFlowWrapper.getTransactionDetailModel().getConsumerNo()));
		} else {
				_workFlowWrapper.getTransactionModel().setConfirmationMessage(
						SMSUtil.buildVariableProductSMS(_workFlowWrapper.getInstruction().getSmsMessageText(), txAmount, _workFlowWrapper.getTransactionModel()
								.getTransactionCodeIdTransactionCodeModel().getCode(), _workFlowWrapper.getProductModel().getHelpLineNotificationMessageModel()
								.getSmsMessageText()));
		}*/
		SmartMoneyAccountModel agentSmartMoneyAccountModel = _workFlowWrapper.getSmartMoneyAccountModel();
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		
		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		
		_workFlowWrapper.getTransactionModel().setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		
		TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;		

		ThreadLocalActionLog.setActionLogId(logActionLogModel());

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)_workFlowWrapper.getSmartMoneyAccountModel();
        
        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setBankId( smartMoneyAccountModel.getBankId() );
        switchWrapper.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() );
        AccountInfoModel accountInfoModel = null;
        if(switchWrapper.getWorkFlowWrapper().getAccountInfoModel() != null)
			accountInfoModel = switchWrapper.getWorkFlowWrapper().getAccountInfoModel();
        else
       	switchWrapper = olaVeriflyFinancialInstitution.verifyCredentialsWithoutPin(switchWrapper);
			
        	
  		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
  		switchWrapper.putObject(CommandFieldConstants.KEY_PIN, accountInfoModel.getOldPin());
  		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
  		switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
  		switchWrapper.setBasePersistableModel( agentSmartMoneyAccountModel ) ;
  		switchWrapper.setWorkFlowWrapper(_workFlowWrapper);			
  		_workFlowWrapper.setSwitchWrapper(switchWrapper);
  		_workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);
  		_workFlowWrapper.setAccountInfoModel(accountInfoModel);

  		logger.info("[CollectionPaymentTransaction.dosale]Checking Agent Balance. Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
  						
  		transactionModelTemp.setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
  			
  		switchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

  		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;

  		_workFlowWrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
  				
  		transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;	
  			
  		rrnPrefix = buildRRNPrefix();		
  		
  		ProductModel productModel = _workFlowWrapper.getProductModel();
						
		//_workFlowWrapper = billSaleProductDispenser.doSale(_workFlowWrapper);
			
		_workFlowWrapper.getDataMap().put(OLA_FROM_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getFromAccountNo());
		_workFlowWrapper.getDataMap().put(OLA_TO_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getToAccountNo());
			
		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
			
		switchWrapper.getWorkFlowWrapper().setProductModel(productModel);
			
		//The following code is for Phoenix implementation
			
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());

		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
		_workFlowWrapper.setSwitchWrapper(switchWrapper);
		_workFlowWrapper.setOLASwitchWrapper(_workFlowWrapper.getSwitchWrapper());
		
		txDetailModel.setSettled(Boolean.TRUE);
			TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();

			transactionModel.addTransactionIdTransactionDetailModel(txDetailModel);
			_workFlowWrapper.setTransactionModel(transactionModel);
		txManager.saveTransaction(_workFlowWrapper);
		_workFlowWrapper.setAppUserModel(ThreadLocalAppUser.getAppUserModel());
		_workFlowWrapper.putObject("ACTION_LOG_ID", ThreadLocalActionLog.getActionLogId());
		
		Boolean isInclusiveChargesIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
		isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : Boolean.TRUE;
		
		
		//**********************************************************************
				//**********************************************************************
				 olaVeriflyFinancialInstitution.agentCollectionPayment(switchWrapper);
			        _workFlowWrapper.setOLASwitchWrapper(switchWrapper);

			    //**********************************************************************
			    //**********************************************************************
				
				this.sendSms(_workFlowWrapper);

		double amount = _workFlowWrapper.getTransactionModel().getTransactionAmount();

		if(!isInclusiveChargesIncluded) {
			
			amount -= (_workFlowWrapper.getCommissionAmountsHolder().getInclusiveFixAmount() + _workFlowWrapper.getCommissionAmountsHolder().getInclusivePercentAmount());
		}
		
        switchWrapper.setAmountPaid(Formatter.formatDouble(amount));
		
		transactionModel = _workFlowWrapper.getTransactionModel();
			
		
			
		//switchWrapper = phoenixFinancialInstitution.pushBillPayment(switchWrapper);
		
		
		_workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
		_workFlowWrapper.setTransactionModel(transactionModel);			
			
		SETTLE_COMMISSION_BLOCK : {
	
			settlementManager.settleCommission(commissionWrapper, _workFlowWrapper);			
		}
			
		transactionModel.setCustomerMobileNo(notificationMobileNo);
		transactionModel.setNotificationMobileNo(notificationMobileNo);
		transactionModel.setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED);
		_workFlowWrapper.setTransactionModel(transactionModel);
			
		txManager.saveTransaction(_workFlowWrapper); // save the transaction

		/*logger.info("Validating Challan after saving transaction");
		count = transactionDetailMasterManager.getPaidChallan(transactionDetailMasterModel.getConsumerNo(),_workFlowWrapper.getProductModel().getProductId().toString());
		if(count > 0){
			if(SupplierProcessingStatusConstants.COMPLETED.equals(count))
				throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.03"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			else if(SupplierProcessingStatusConstants.PROCESSING.equals(count))
				throw new CommandException("Transaction is already Processing.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
			else if(SupplierProcessingStatusConstants.IN_PROGRESS.equals(count))
				throw new CommandException("Transaction is already in Progress.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
		}*/

		Date creditAccountAdviceDateTime = new Date();

		MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
		middlewareAdviceVO.setAccountNo1(notificationMobileNo);
		middlewareAdviceVO.setAccountNo2(switchWrapper.getToAccountNo());
		middlewareAdviceVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
		middlewareAdviceVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
		middlewareAdviceVO.setConsumerNo(_workFlowWrapper.getTransactionDetailModel().getConsumerNo());
		middlewareAdviceVO.setStan(String.valueOf((100000 + new Random().nextInt(900000)))+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

		middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
		middlewareAdviceVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

//        middlewareAdviceVO.setStan(_workFlowWrapper.get);
//        middlewareAdviceVO.setRetrievalReferenceNumber(this.getRetrievalReferenceNumber(middlewareAdviceVO.getStan()));
		middlewareAdviceVO.setRequestTime(creditAccountAdviceDateTime);
		middlewareAdviceVO.setDateTimeLocalTransaction(creditAccountAdviceDateTime);
		middlewareAdviceVO.setTransmissionTime(creditAccountAdviceDateTime);
		middlewareAdviceVO.setAdviceType(CoreAdviceUtil.CHALLAN_COLLECTION_ADVICE);

		//Product model is null in case of manual adjustment
		if(switchWrapper.getWorkFlowWrapper().getProductModel() != null){
			middlewareAdviceVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
		}

		if( switchWrapper.getTransactionTransactionModel() != null && switchWrapper.getTransactionTransactionModel().getTransactionId() != null){
			middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", switchWrapper.getTransactionTransactionModel().getTransactionId());
		}

		transactionReversalManager.makeCoreCreditAdvice(middlewareAdviceVO);



		return _workFlowWrapper;
	}

	
	
	private void sendSms(WorkFlowWrapper _workFlowWrapper) throws FrameworkCheckedException {
		
			
		String agentMsgString = null;
		String customerMsgString = null;
		Object[] agentSMSParam = null;
		Object[] customerSMSParam = null;
		String customerBalance = "";
		String agentBalance = "";

		String brandName =null;
		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			brandName= MessageUtil.getMessage("sco.brandName");
		}else {

			brandName = MessageUtil.getMessage("jsbl.brandName");
		}
		String trxCode = _workFlowWrapper.getTransactionCodeModel().getCode();
		String totalAmount = Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTotalAmount());
		String customerCharges = Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount());
//		agentBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchW'rapper().getOlavo().getAgentBalanceAfterTransaction());
		if(_workFlowWrapper.getCustomerAppUserModel() != null) {
			agentBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchWrapper().getOlavo().getAgentBalanceAfterTransaction());
			customerBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
		}
		else{
			customerBalance = "0";
			agentBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
		}
		String productName = _workFlowWrapper.getProductModel().getName();
		String consumer = _workFlowWrapper.getTransactionDetailModel().getConsumerNo();
		String notificationNo = _workFlowWrapper.getTransactionModel().getNotificationMobileNo();
		String date = dtf.print(new DateTime());
		String time = tf.print(new LocalTime());
				
		agentMsgString = "ubp.challan.agent";
		customerMsgString = "collection.payment.customer";
		agentSMSParam = new Object[] {brandName,trxCode,productName,consumer,notificationNo,totalAmount,time,date,agentBalance};
		customerSMSParam = new Object[] {trxCode,totalAmount,productName,date,time,customerCharges,customerBalance};
		
		String agentSMS = this.getMessageSource().getMessage(agentMsgString, agentSMSParam, null);			
		String customerSMS = this.getMessageSource().getMessage(customerMsgString, customerSMSParam, null);
		
		SmsMessage agentSMSMessage = new SmsMessage(_workFlowWrapper.getAppUserModel().getMobileNo(), agentSMS);
		SmsMessage customerSMSMessage = new SmsMessage(_workFlowWrapper.getTransactionModel().getNotificationMobileNo(), customerSMS);
			
		_workFlowWrapper.getTransactionDetailModel().setCustomField4(agentSMS);			
		_workFlowWrapper.getTransactionDetailModel().setCustomField8(customerSMS);
		_workFlowWrapper.getTransactionModel().setNotificationMobileNo(_workFlowWrapper.getTransactionModel().getNotificationMobileNo());//todo
		_workFlowWrapper.getTransactionModel().setConfirmationMessage(customerSMS);
			
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
		messageList.add(agentSMSMessage);
		messageList.add(customerSMSMessage);
		
		if(_workFlowWrapper.getHandlerModel() != null && _workFlowWrapper.getHandlerModel().getSmsToHandler()) {
			messageList.add(new SmsMessage(_workFlowWrapper.getHandlerAppUserModel().getMobileNo(), agentSMS));
		}
		
		_workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
		
			
		
	}

	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
		//***********************************************************************************************
			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
			wrapper.setSegmentModel(segmentModel);
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
	    wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------

		return commissionWrapper;
	}
	
	
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of CustomerBillPaymentTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		//BillPaymentVO productVO = (BillPaymentVO) workFlowWrapper.getProductVO();

		if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned())
		{
			if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
			}
		}

		if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}

	}
	
	
	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		
		
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) {
			
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue()) {

				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue()) {
				
					transProcessingAmount += commissionRateModel.getRate();
				} else {
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
				}
			}
		}
		

		
		return transProcessingAmount;
	}
	
	protected Long logActionLogModel() {
		
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
		actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
		actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
	
		BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

		baseWrapperActionLog.setBasePersistableModel(actionLogModel);
		
		try {
		
			baseWrapperActionLog = actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);
			
		} catch (FrameworkCheckedException e) {
			logger.error(e.getLocalizedMessage());
		}
		
		if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {
			
			actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();			
		}
		
		return actionLogModel.getActionLogId();
	}


	@Override
	protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	public void setSupplierManager(SupplierManager supplierManager) {
		this.supplierManager = supplierManager;
	}


	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}


	public void setProductDispenseController(
			ProductDispenseController productDispenseController) {
		this.productDispenseController = productDispenseController;
	}


	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}


	public void setCustomerManager(CustTransManager customerManager) {
		this.customerManager = customerManager;
	}


	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}


	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}


	public void setNotificationMessageManager(
			NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}


	public void setSupplierBankInfoManager(
			SupplierBankInfoManager supplierBankInfoManager) {
		this.supplierBankInfoManager = supplierBankInfoManager;
	}


	public void setOperatorManager(OperatorManager operatorManager) {
		this.operatorManager = operatorManager;
	}


	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}


	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}


	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}


	public void setPhoenixFinancialInstitution(
			AbstractFinancialInstitution phoenixFinancialInstitution) {
		this.phoenixFinancialInstitution = phoenixFinancialInstitution;
	}


	public void setStakeholderBankInfoManager(
			StakeholderBankInfoManager stakeholderBankInfoManager) {
		this.stakeholderBankInfoManager = stakeholderBankInfoManager;
	}


	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}


	public void setVeriflyController(VeriflyManagerService veriflyController) {
		this.veriflyController = veriflyController;
	}


	public void setSwitchController(SwitchController switchController) {
		this.switchController = switchController;
	}


	public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
		this.transactionReversalManager = transactionReversalManager;
	}
	
	
	
	
	

}
