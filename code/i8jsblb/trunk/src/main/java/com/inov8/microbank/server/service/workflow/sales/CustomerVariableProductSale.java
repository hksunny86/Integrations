package com.inov8.microbank.server.service.workflow.sales;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.SupplierBankInfoModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SMSUtil;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.ola.integration.vo.OLAVO;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: i8</p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class CustomerVariableProductSale
    extends VariableProductSaleTransaction
{
  protected final Log log = LogFactory.getLog(getClass());
  private CommissionManager commissionManager;

  private ProductManager productManager;
  private CustTransManager customerManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private ProductDispenseController productDispenseController ;
  private NotificationMessageManager notificationMessageManager;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private SupplierBankInfoManager supplierBankInfoManager;
  private FinancialIntegrationManager financialIntegrationManager;

public CustomerVariableProductSale()
  {
  }

  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
      throws Exception
  {
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CustomerVariableProductSale...");
		}
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl() ;

    wrapper = super.doPreStart(wrapper);

    // Populate the Product model from DB
    
    baseWrapper.setBasePersistableModel(wrapper.getProductModel());
    baseWrapper = productManager.loadProduct(baseWrapper);
    wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
	userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
	userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
	baseWrapper.setBasePersistableModel(userDeviceAccountsModel);

	baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel)baseWrapper.getBasePersistableModel());

    wrapper.setCustomerModel( new CustomerModel() ) ;
    wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getCustomerId()) ;

    // Populate the Customer model from DB
    searchBaseWrapper.setBasePersistableModel(wrapper.getCustomerModel());

    searchBaseWrapper = customerManager.loadCustomer(searchBaseWrapper);
    wrapper.setCustomerModel((CustomerModel) searchBaseWrapper.getBasePersistableModel());

    // Populate Product's notification messages
    //--Setting instruction and success Message
    NotificationMessageModel notificationMessage = new NotificationMessageModel();
    notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
    baseWrapper.setBasePersistableModel(notificationMessage);

    baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
    wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

//    wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
//        NotificationMessageModel) baseWrapper.getBasePersistableModel());
    NotificationMessageModel successMessage = new NotificationMessageModel();
    successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
    baseWrapper.setBasePersistableModel(successMessage);
    baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
    wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());


    // Populate the Smart Money Account from DB
    baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

    baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
    wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
    wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

    // Populate Supplier Bank Info Model
    SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
    supplierBankInfoModel.setSupplierId( wrapper.getProductModel().getSupplierId() );
    supplierBankInfoModel.setPaymentModeId( wrapper.getSmartMoneyAccountModel().getPaymentModeId() );
    baseWrapper.setBasePersistableModel( supplierBankInfoModel );

    wrapper.setSupplierBankInfoModel( (SupplierBankInfoModel)this.supplierBankInfoManager.loadSupplierBankInfo( baseWrapper ).getBasePersistableModel() ) ;

    //  Populate Operator Bank Info Model
//    OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();
//    operatorBankInfoModel.setOperatorId( PortalConstants.REF_DATA_OPERATOR );
//    operatorBankInfoModel.setPaymentModeId( wrapper.getSmartMoneyAccountModel().getPaymentModeId() );
//    operatorBankInfoModel.setBankId( wrapper.getSmartMoneyAccountModel().getBankId() ) ;
//    baseWrapper.setBasePersistableModel( operatorBankInfoModel );
//
//    wrapper.setOperatorBankInfoModel( (OperatorBankInfoModel)this.operatorManager.getOperatorBankInfo( baseWrapper ).getBasePersistableModel() ) ;

    if(logger.isDebugEnabled())
	{
    	logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of CustomerVariableProductSale...");
	}
    return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CustomerVariableProductSale...");
		}
    wrapper = super.doPreProcess(wrapper);
    
    TransactionModel txModel = wrapper.getTransactionModel();

    txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

    //		txModel.setTransactionAmount(wrapper.getTransactionAmount());
    txModel.setTransactionAmount(wrapper.getTransactionAmount());
    txModel.setTotalAmount(wrapper.getTotalAmount());
    txModel.setTotalCommissionAmount(0d) ;
    txModel.setDiscountAmount(0d);

    // Transaction Type model of transaction is populated
    txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

    // Sets the Device type
    txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());

    // Payment mode model of transaction is populated
    txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

    // Customer model of transaction is populated
    txModel.setCustomerIdCustomerModel(wrapper.getCustomerModel());

    // Smart Money Account Id is populated
    txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

    // Customer mobile No
    txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
    txModel.setSaleMobileNo( ThreadLocalAppUser.getAppUserModel().getMobileNo() );
    
    //  Populate processing Bank Id
    txModel.setProcessingBankId( wrapper.getSmartMoneyAccountModel().getBankId() ) ;

    wrapper.setTransactionModel(txModel);
    if(logger.isDebugEnabled())
	{
    	logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CustomerVariableProductSale...");
	}
    return wrapper;
  }
  

  public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
  {
    // ------------------------Validates the Customer's requirements -----------------------------------
	  if(logger.isDebugEnabled())
		{
		  logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CustomerVariableProductSale...");
		}
	  if (wrapper.getAppUserModel() != null)
	  {
		  if ("".equals(wrapper.getAppUserModel().getMobileNo()))
		  {
		
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
		  }
	  }
	  else
	  {
		
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
	  }

	  if (wrapper.getSmartMoneyAccountModel() != null)
	  {
		  if (!wrapper.getSmartMoneyAccountModel().getActive())
		  {
		
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.
					  CUST_SMARTMONEY_INACTIVE);
		  }
		  if (wrapper.getSmartMoneyAccountModel().getChangePinRequired())
		  {
		
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.
					  CUST_SMARTMONEY_PIN_CHG_REQ);
		  }
		  if(!wrapper.getSmartMoneyAccountModel().getCustomerId().toString().equals(ThreadLocalAppUser.getAppUserModel().getCustomerId().toString()))
		  {
		
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
		  }
	  }
	  else
	  {
		
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
	  }


	  if(wrapper.getUserDeviceAccountModel() == null )
	  {
		
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
	  }

	  // ------------------------Validates the Customer's requirements -----------------------------------
	  //commented for SKMT
	  /*if ("".equals(wrapper.getCustomerAppUserModel().getMobileNo()))
	  {
		
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
	  }
	  */

	  //	--------------------------------  Validates the Product's requirements ---------------------------------
	  if ( wrapper.getProductModel() != null )
	  {
		  if ( !wrapper.getProductModel().getActive()  )
		  {
		
			  throw new WorkFlowException( WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE );
		  }
		  if(wrapper.getProductModel().getInstructionId() == null)
		  {
		
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_INSTRUCTION_FOR_PRODUCT);
		  }
		  if ( wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_VARIABLE.longValue()  )
		  {
		
			  throw new WorkFlowException( WorkFlowErrorCodeConstants.NOT_VARIABLE_PRODUCT );
		  }

	  }
	  else
	  {
		
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
	  }

	  //	-----------------------  Validates the Supplier's requirements ---------------------------------------------
	  if ( wrapper.getProductModel().getSupplierIdSupplierModel() != null )
	  {
		  if ( !wrapper.getProductModel().getSupplierIdSupplierModel().getActive()  )
		  {
		
			  throw new WorkFlowException( WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE );
		  }
	  }
	  else
	  {
		
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NULL);
	  }

	  // ----------------------- Validates the Supplier Bank Info requirements -----------------
	  if( wrapper.getSupplierBankInfoModel() == null ||
			  wrapper.getSupplierBankInfoModel().getAccountNo() == null ||
			  "".equals(wrapper.getSupplierBankInfoModel().getAccountNo()) )
	  {
		
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_MERCH_NO_NULL);
	  }
	  // ---------------------------------------------------------------------------------------

	  //  ----------------------- Validates the operator Bank Info requirements -----------------
//	  if( wrapper.getOperatorBankInfoModel() == null )
//	  {
//		  
//		  throw new WorkFlowException(WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
//	  }
	  // ---------------------------------------------------------------------------------------


	  //  Validates the workflowWrapper's requirements
	  if ( wrapper.getTransactionAmount() <= 0 )
	  {
		  
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
	  }

	  // ----------------------- Validates the Service's requirements ---------------------------
	  if (wrapper.getProductModel().getServiceIdServiceModel() != null)
	  {
		  if (!wrapper.getProductModel().getServiceIdServiceModel().getActive())
		  {
			
			  throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);
		  }
	  }
	  else
	  {
		  
		  throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
	  }

	  if(logger.isDebugEnabled())
	  {
		  logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CustomerVariableProductSale...");
	  }
	  return wrapper;
  }

  /**
   * doVariableProductSale
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   *
   */
  public WorkFlowWrapper doVariableProductSale(WorkFlowWrapper wrapper) throws Exception {

    TransactionDetailModel txDetailModel = new TransactionDetailModel();

// ------ Gets the reference of the Supplier's implementation class ----------------------
//      SupplierWrapper supplierWrapper = new SupplierWrapperImpl();
//      supplierWrapper.setProductTypeProductModel(wrapper.getProductModel());
//      Supplier supplier = SupplierFactory.getSupplier(supplierWrapper);

      
      ProductDispenser productDispense = this.productDispenseController.loadProductDispenser( wrapper ) ;
      wrapper.setProductDispenser(productDispense);

//****wrapper = productDispense.verify(wrapper);

      CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
      commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
      commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
      commissionWrapper.setProductModel(wrapper.getProductModel());
      
      RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	  wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
      
      commissionWrapper = this.commissionManager.calculateCommission(wrapper);

      // -------- Validate commission ---------------------------------------------------------
      CommissionAmountsHolder productCommission = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

      txDetailModel.setProductTopupAmount( productCommission.getTotalAmount() - productCommission.getTotalCommissionAmount() );
      txDetailModel.setProductIdProductModel(wrapper.getProductModel());
      wrapper.getTransactionModel().setNotificationMobileNo( wrapper.getCustomerAppUserModel().getMobileNo() );
      txDetailModel.setConsumerNo(((BillPaymentVO) wrapper.getProductVO()).getConsumerNo());
      
      wrapper.setCommissionAmountsHolder(productCommission);
      
      wrapper.getTransactionModel().setTotalCommissionAmount(productCommission.getTotalCommissionAmount());
      wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
      wrapper.getTransactionModel().setTotalAmount(productCommission.getTotalAmount());
      wrapper.getTransactionModel().setConfirmationMessage(" _ ");

      txDetailModel.setSettled(false);
      wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
      wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));
      wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
      wrapper.setTransactionDetailModel(txDetailModel);
      
      txManager.saveTransaction(wrapper);

      this.settlementManager.settleCommission(commissionWrapper, wrapper);
	
      BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		if(wrapper.getUserDeviceAccountModel().getCommissioned()){
	    	switchWrapper.setDiscountAmount(wrapper.getDiscountAmount());
	    	switchWrapper.setCommissioned(true);
	    }
		
//**************** BB Agents Changes - Start
		switchWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());// customer SMA
		switchWrapper.setFtOrder(1);
		
		logger.info("[CustomerVariableProductSale.doVariableProductSale] Going to transfer funds from Customer Account to Topup Pool Account. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		switchWrapper = abstractFinancialInstitution.debitCreditAccount(switchWrapper);
		
		wrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
	    wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
	    
	    Double customerBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction(); // customer balance
		
	    switchWrapper.setBalance(customerBalance);
	    wrapper.setSwitchWrapper(switchWrapper);
	    
	    if(wrapper.getDeviceTypeModel().getDeviceTypeId().longValue() != DeviceTypeConstantsInterface.ALL_PAY.longValue()){
	    	
	    	if(null != wrapper.getCustomerAppUserModel().getMobileNo() && !"".equals(wrapper.getCustomerAppUserModel().getMobileNo())){
	    		if( wrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD ){
	    			wrapper.getTransactionModel().setConfirmationMessage( "You have received top up of\nRs. " + wrapper.getTransactionAmount() + " from\n"
	    						+ wrapper.getAppUserModel().getMobileNo()
	    						+ " ("+ThreadLocalAppUser.getAppUserModel().getFirstName()+" "+ThreadLocalAppUser.getAppUserModel().getLastName()+")\non "
	    						+ PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT)+" "+PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT)+"\nTx ID: " + wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() + "\n*Standard Charges apply." ) ;
    			  
    			  smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage() ));
    			  
    			  wrapper.getTransactionDetailModel().setCustomField4("Transaction Successful\n\nRs. " + Formatter.formatDouble(wrapper.getTransactionAmount())
    					  + " Zong top up sent to\n" + wrapper.getCustomerAppUserModel().getMobileNo() 
    					  + "\n\nTx ID:" +  wrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode()
    					  + "\nDate Time: " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT) 
    					  + " " + PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT) + "\n\nYour Balance is: Rs. " + Formatter.formatDouble(customerBalance)+"\n*Standard Charges apply.");
    			  
    			  this.smsSender.send(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), wrapper.getTransactionDetailModel().getCustomField4()));

    		  }else{
    			  if( wrapper.getCustomerAppUserModel().getMobileNo().trim().equals(wrapper.getAppUserModel().getMobileNo())){
        			  smsSender.sendDelayed(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(),wrapper.getTransactionModel().getConfirmationMessage()));
        		  }else{
        			  smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(),wrapper.getTransactionModel().getConfirmationMessage()));
        		  }
    		  }
    	  }
	    }

	    wrapper = productDispense.doSale( wrapper ) ;
//		wrapper.setSwitchWrapper(switchWrapper);

		txDetailModel.setSettled(true);
		wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		txManager.saveTransaction(wrapper);

		
//**************** BB Agents Changes - End
	    
//		logger.info("[CustomerVariableProductSale.doVariableProductSale] Going to Debit Customr Account. Logged in AppuserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
//		switchWrapper = abstractFinancialInstitution.transaction(switchWrapper);
//		switchWrapper.setBasePersistableModel( wrapper.getSmartMoneyAccountModel() ) ;
//		wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());
//		wrapper.setOLASwitchWrapper(switchWrapper);
		
//		SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
//		
//		switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
//		switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
//		switchWrapperTemp.setWorkFlowWrapper(wrapper);
//		switchWrapperTemp.setTransactionTransactionModel(wrapper.getTransactionModel());
//		switchWrapperTemp.setBasePersistableModel( wrapper.getSmartMoneyAccountModel() ) ;
//		
//		logger.info("[CustomerVariableProductSale.doVariableProductSale] Checking Customr Account Balance. Logged in AppuserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
//		
//		switchWrapperTemp = abstractFinancialInstitution.checkBalance(switchWrapperTemp);
//		
		//////////////////////////////////////////////////////////////////////////////////////////////
		
//		SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
//		StakeholderBankInfoModel customerPoolBankInfoModel = new StakeholderBankInfoModel();
//		customerPoolBankInfoModel.setPrimaryKey(PoolAccountConstantsInterface.CUSTOMER_POOL_ACCOUNT_ID);
//		searchBaseWrapper.setBasePersistableModel(customerPoolBankInfoModel);
//		customerPoolBankInfoModel = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper).getBasePersistableModel();
		
//		StakeholderBankInfoModel topUpPool = new StakeholderBankInfoModel();
//		topUpPool.setPrimaryKey(PoolAccountConstantsInterface.TOPUP_POOL_ID);
//		sbWrapper.setBasePersistableModel(topUpPool);
//		topUpPool = (StakeholderBankInfoModel)stakeholderBankInfoManager.loadStakeHolderBankInfo(sbWrapper).getBasePersistableModel();
		
//		TransactionModel tempTransaction = wrapper.getTransactionModel();
		
//		SwitchWrapper pSwitchWrapper = new SwitchWrapperImpl();
//		pSwitchWrapper.setTransactionTransactionModel(switchWrapper.getWorkFlowWrapper().getTransactionModel());
//		pSwitchWrapper.setBankId(50110L);
//		pSwitchWrapper.setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
//		pSwitchWrapper.setFromAccountNo(customerPoolBankInfoModel.getAccountNo());
//		pSwitchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		pSwitchWrapper.setFromCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//		pSwitchWrapper.setToAccountNo(topUpPool.getAccountNo());
//		pSwitchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//		pSwitchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTransactionAmount())));
//		pSwitchWrapper.setIntegrationMessageVO(new PhoenixIntegrationMessageVO());
//		pSwitchWrapper.setWorkFlowWrapper(wrapper);
//		pSwitchWrapper.setBasePersistableModel(switchWrapper.getWorkFlowWrapper().getSmartMoneyAccountModel());

//Removed FT from here because same operation is done from Topup Scheduler at Day end for all the transactions of the day
//		logger.info("[CustomerVariableProductSale.doVariableProductSale] Going to Debit Credit Phoenix Accounts. Logged in AppuserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
//		pSwitchWrapper = phoenixFinancialInstitution.debitCreditAccount(pSwitchWrapper);
//		wrapper.setTransactionModel(tempTransaction);
//      this.settlementManager.settleBankPayment(commissionWrapper, wrapper);


      // ---------- Calls the Verifly Module to verify the Pin -------------------
/*      VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl() ;
      AccountInfoModel accountInfoModel = wrapper.getAccountInfoModel() ;
      LogModel logModel = new LogModel();
      logModel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
      logModel.setCreatedBy(UserUtils.getCurrentUser().getUsername());
      VeriflyManager veriflyManager = this.veriflyController.getVeriflyMgrByAccountId( wrapper.getSmartMoneyAccountModel() ) ;

      accountInfoModel.setCustomerId( wrapper.getCustomerModel().getCustomerId() );
      accountInfoModel.setAccountNick( wrapper.getSmartMoneyAccountModel().getName() ) ;

      logModel.setTransactionCodeId( wrapper.getTransactionModel().getTransactionCodeId() );

      veriflyBaseWrapper.setAccountInfoModel( accountInfoModel ) ;
      veriflyBaseWrapper.setLogModel( logModel ) ;

      veriflyBaseWrapper = veriflyManager.verifyPIN(veriflyBaseWrapper);

      if( null != veriflyBaseWrapper.getErrorMessage() && !veriflyBaseWrapper.getErrorMessage().equalsIgnoreCase("") )
                  throw new WorkFlowException( WorkFlowErrorCodeConstants.PIN_NOT_VERIFIED );
      // -------------------------------------------------------------------------

      // ---------- Calls the switch module --------------------------------------
      SwitchWrapper switchWrapper = new SwitchWrapperImpl() ;
      switchWrapper.setAccountInfoModel( veriflyBaseWrapper.getAccountInfoModel() );
      switchWrapper.setTransactionTransactionModel( wrapper.getTransactionModel() );
      switchWrapper.setBankId( wrapper.getSmartMoneyAccountModel().getBankId() );
      switchWrapper.setPaymentModeId( wrapper.getSmartMoneyAccountModel().getPaymentModeId() );
      switchWrapper.setVeriflyBaseWrapper( veriflyBaseWrapper );
      switchWrapper.setWorkFlowWrapper( wrapper );

      switchWrapper = this.switchController.transaction( switchWrapper, commissionWrapper ) ;
      // -------------------------------------------------------------------------
*/

    return wrapper;
  }
  
  @Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		String code = wrapper.getTransactionCodeModel().getCode();
		logger.info("[CustomerVariableProductSale.rollback] Rolling back Top Up transaction with ID: " + code);
		try{
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			wrapper.getTransactionModel().setTransactionId(null);
			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
			txManager.saveTransaction(wrapper);
		}catch(Exception ex){
			logger.error("Unable to save Customer Top Up Transaction details in case of rollback: \n"+ ex.getStackTrace());
		}
		
		if(null != wrapper.getOLASwitchWrapper()){
			logger.info("[CustomerVariableProductSale.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
			abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,1));
		    
			if(null != wrapper.getSwitchWrapper() && null != wrapper.getProductVO() && null != wrapper.getProductVO().getResponseCode() && wrapper.getProductVO().getResponseCode().equals("0000")){
		    	ProductDispenser productDispense = this.productDispenseController.loadProductDispenser( wrapper ) ;
		    	productDispense.rollback(wrapper);
		    }

		}
	  
		return wrapper;
	}

	/*
	 * This method swaps sender and recipient OLA Accounts for reversal FT
	 */
	private SwitchWrapper swapAccounts(WorkFlowWrapper wrapper,int ftOrder) throws Exception{
		
		SwitchWrapper olaSwitchWrapper = wrapper.getOLASwitchWrapper();
		
		if(ftOrder == 2){
			olaSwitchWrapper = wrapper.getOlaSwitchWrapper_2();
		}
		
		OLAVO olaVO = olaSwitchWrapper.getOlavo();
		String fromAccountNo = olaSwitchWrapper.getFromAccountNo();
		String toAccountNo = olaSwitchWrapper.getToAccountNo();
		olaSwitchWrapper.setFromAccountNo(toAccountNo);
		olaSwitchWrapper.setToAccountNo(fromAccountNo);
		olaVO.setPayingAccNo(toAccountNo);
		olaVO.setReceivingAccNo(fromAccountNo);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setOlavo(olaVO);
		switchWrapper.setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
		switchWrapper.setBankId(wrapper.getOlaSmartMoneyAccountModel().getBankId());
		switchWrapper.setWorkFlowWrapper(wrapper);

		return switchWrapper;
	}
	

  public void validateCommission(CommissionAmountsHolder commissionHolder,
                                 CommissionAmountsHolder calculatedCommissionHolder,
                                 WorkFlowWrapper workFlowWrapper) throws Exception
  {
	if(logger.isDebugEnabled())
	{
		  logger.debug("Inside validateCommission of CustomerVariableProductSale...");
	}
    if (commissionHolder.getTotalCommissionAmount().doubleValue() !=
        calculatedCommissionHolder.getTotalCommissionAmount().doubleValue())
    {
     
      throw new Exception(
          "Calculated commission and the one from external interface, dont matched");
    }
    if(logger.isDebugEnabled())
	{
    	logger.debug("Ending validateCommission of CustomerVariableProductSale...");
	}
  }

  public void setCommissionManager(CommissionManager commissionManager)
  {
    this.commissionManager = commissionManager;
  }

  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setCustomerManager(CustTransManager customerManager)
  {
    this.customerManager = customerManager;
  }

  public void setSmartMoneyAccountManager(SmartMoneyAccountManager
                                          smartMoneyAccountManager)
  {
    this.smartMoneyAccountManager = smartMoneyAccountManager;
  }

  public void setProductDispenseController(ProductDispenseController
                                           productDispenseController)
  {
    this.productDispenseController = productDispenseController;
  }

  public void setNotificationMessageManager(NotificationMessageManager
                                            notificationMessageManager)
  {
    this.notificationMessageManager = notificationMessageManager;
  }

  public void setSupplierBankInfoManager(SupplierBankInfoManager
                                         supplierBankInfoManager)
  {
    this.supplierBankInfoManager = supplierBankInfoManager;
  }

  protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
    return wrapper;
  }
  
  public void setUserDeviceAccountsManager(
  		UserDeviceAccountsManager userDeviceAccountsManager) {
  	this.userDeviceAccountsManager = userDeviceAccountsManager;
  }

@Override
protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception
{
	// TODO Auto-generated method stub
	return wrapper;
}

@Override
protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception
{
	// TODO Auto-generated method stub
	return wrapper;
}

@Override
protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper)throws Exception
{
	
	String sms = SMSUtil.buildVariableProductSMSForUser(wrapper);
	
	
	
	if(wrapper.getDeviceTypeModel().getDeviceTypeId().longValue() != DeviceTypeConstantsInterface.ALL_PAY.longValue())
    {
		 if( wrapper.getDeviceTypeModel().getDeviceTypeId() == DeviceTypeConstantsInterface.USSD )
		  {
		  }
		  else
			  this.smsSender.sendDelayed(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), sms));
    }
	
	return wrapper;
	
}

@Override
protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper)throws Exception
{
//	if (wrapper.getFinancialTransactionsMileStones().getIsCommissionsSettled().size() != 0)
//		{
//			try
//			{
//				this.settlementManager.rollbackCommissionSettlement(wrapper);
//			}
//			catch (Exception e)
//			{
//				log.warn("Exception occured during Commission Settlement rollback : " + e.getStackTrace());
//			}
//		}
//		if (wrapper.getFinancialTransactionsMileStones().isCustomerBankAccountDebitted())
//		{
//			try
//			{
//				this.settlementManager.rollbackBankPayment(wrapper);
//			}
//			catch (Exception e)
//			{
//				log.warn("Exception occured during Bank Settlement rollback : " + e.getStackTrace());
//			}
//		}
//		if (wrapper.getFinancialTransactionsMileStones().isProductDispensed())
//		{
//			try
//			{
//				wrapper.getProductDispenser().rollback(wrapper);
//			}
//			catch (Exception e)
//			{
//				log.warn("Exception occured during Product Dispense rollback : " + e.getStackTrace());
//			}
//		}

	return wrapper;
}

public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

}
