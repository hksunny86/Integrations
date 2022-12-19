package com.inov8.microbank.server.service.workflow.credittransfer;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.util.*;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.vo.AccountToCashVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;

/**
 *
 * <p>Title: DistributorToDistributorTransaction</p>
 *
 * <p>Description: Class implements the logic for credit transfer between a distributor contact
 * to another distributor contact </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class AccountToCashLeg2Transaction
    extends CreditTransferTransaction
{
  private DistributorContactManager distributorContactManager;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private NotificationMessageManager notificationMessageManager;
  private ProductManager productManager;
  private CustTransManager customerManager;
  private CommissionManager commissionManager;

  //added by mudassir
  private MfsAccountManager mfsAccountManager;
  private WalkinCustomerModel recepientWalkinCustomerModel;
  private SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel;
  private RetailerContactManager retailerContactManager;
  private RetailerManager retailerManager;
  
  private MessageSource messageSource;
  private AbstractFinancialInstitution phoenixFinancialInstitution ;
  DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
  DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
  private String rrnPrefix;
  

  public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

  public AccountToCashLeg2Transaction(){

  }

  /**
   * Credit transfer transaction takes place over here
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception
  {

	  
	  	wrapper.getTransactionModel().setToRetailerId(wrapper.getRetailerContactModel().getRetailerId());
	  	wrapper.getTransactionModel().setToDistributorId(wrapper.getRetailerModel().getDistributorId());
	  	wrapper.getTransactionModel().setToRetContactId(wrapper.getAppUserModel().getRetailerContactId());
	  	wrapper.getTransactionModel().setToRetContactMobNo(wrapper.getAppUserModel().getMobileNo());
	  	wrapper.getTransactionModel().setToRetContactName(wrapper.getAppUserModel().getLastName() + " " + wrapper.getAppUserModel().getFirstName());
	  	wrapper.getTransactionModel().setUpdatedBy(wrapper.getAppUserModel().getAppUserId());
	  	wrapper.getTransactionModel().setUpdatedOn(new Date());
	  	wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getTransactionModel().getCustomerMobileNo());//customer mobile number
	  	wrapper.getTransactionModel().setDiscountAmount(0d);
	  	 wrapper.getTransactionDetailModel().setCustomField13( "" + wrapper.getDeviceTypeModel().getDeviceTypeId());

	  	wrapper.getTransactionDetailModel().setSettled(Boolean.TRUE);
		wrapper.getTransactionDetailModel().setConsumerNo(wrapper.getTransactionDetailModel().getCustomField5());
		wrapper.getTransactionDetailModel().setActualBillableAmount(wrapper.getTransactionAmount());
		
//		BaseWrapper bWrapper = new BaseWrapperImpl();
//		bWrapper.setBasePersistableModel(wrapper.getTransactionModel());
		txManager.transactionRequiresNewTransaction(wrapper); //save the transaction
		
		
	    BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		
		wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());
		
		CommissionAmountsHolder amountsHolder = new CommissionAmountsHolder();
		Double franchise2Commission = 0.0d;
		CommissionTransactionModel frnCommTxModel = getCommissionTransactionModelByStakeholderIdAndTransactionId(CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID, wrapper.getTransactionDetailModel().getTransactionDetailId());
		
		if(frnCommTxModel != null){
			franchise2Commission = frnCommTxModel.getCommissionAmount();
			amountsHolder.setFranchise2CommissionAmount(franchise2Commission);
		}
		
		//if agent is Head agent, then update agent commission (agent commission = agemtn commission + franchise commission)
		if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){

			logger.info("[AccountToCashLeg2Transaction.doCreditTransfer()] Head Agent found. Removing CommissionTrxModel for CommTrxID:" + frnCommTxModel.getCommissionTransactionId() + " TrxDetailId:" + wrapper.getTransactionDetailModel().getTransactionDetailId() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

			//remove franchise entry of commissionTxModel because agent = franchise in case agent is head agent.
			commissionManager.removeCommissionTransactionModel(frnCommTxModel);
		}else if(franchise2Commission > 0.0d){
			//save franchise 2 commissionTxModel with settled = 1, Posted = 0
			frnCommTxModel.setSettled(Boolean.TRUE);
			frnCommTxModel.setUpdatedOn(new java.util.Date());
			frnCommTxModel.setPosted(Boolean.FALSE);
			settlementManager.saveCommissionTransactionMoel(frnCommTxModel);
		}
		
		CommissionTransactionModel commissoinTxModel = getCommissionTransactionModelByStakeholderIdAndTransactionId(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID, wrapper.getTransactionDetailModel().getTransactionDetailId());
		
		//agent 2 commission can be null if commission rate is not defined for the slab in which use segment falls
		if(commissoinTxModel != null && commissoinTxModel.getCommissionAmount() != null){
			
			if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
				
				commissoinTxModel.setCommissionAmount(commissoinTxModel.getCommissionAmount() + franchise2Commission);
				amountsHolder.setAgent2CommissionAmount(commissoinTxModel.getCommissionAmount());
				amountsHolder.setFranchise2CommissionAmount(0.0d);
			}else{
				
				amountsHolder.setAgent2CommissionAmount(commissoinTxModel.getCommissionAmount());
			}
			
			//save agent 2 commissionTxModel with settled = 1, Posted = 0 (bug 688 fix)
			commissoinTxModel.setSettled(Boolean.TRUE);
			commissoinTxModel.setUpdatedOn(new java.util.Date());
			commissoinTxModel.setPosted(Boolean.FALSE);
			settlementManager.saveCommissionTransactionMoel(commissoinTxModel);
			
		}else{
			amountsHolder.setAgent2CommissionAmount(0.0D);
		}
		
		wrapper.setCommissionAmountsHolder(amountsHolder);
		
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// Agent SMA
		switchWrapper.setFtOrder(1);
		switchWrapper.setIsAccountToCashLeg2(true);
		
		logger.info("[AccountToCashLeg2Transaction.doCreditTransfer()] Going to transfer funds from Acc-to-Cash Sundry to Agent Account. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		switchWrapper = abstractFinancialInstitution.debitCreditAccount(switchWrapper);
		
		wrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
	    wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());

		//agent balance
		Double agentBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction();
		String agentAccNo = switchWrapper.getToAccountNo();

		if(wrapper.getCommissionAmountsHolder().getFranchise2CommissionAmount() > 0.0d){
			SwitchWrapper switchWrapper2 = new SwitchWrapperImpl();
			switchWrapper2.setWorkFlowWrapper(wrapper);
			switchWrapper2.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// Agent SMA
			switchWrapper2.setFtOrder(2);
			switchWrapper2.setIsAccountToCashLeg2(true);
			switchWrapper2.setSkipAccountInfoLoading(true);
			
			logger.info("[AccountToCashLeg2Transaction.doCreditTransfer()] Going to transfer funds from Acc-to-Cash Sundry to Franchise Account. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			switchWrapper2 = abstractFinancialInstitution.debitCreditAccount(switchWrapper2);
			wrapper.setOlaSwitchWrapper_2(switchWrapper2); //setting the switchWrapper for rollback
		}
		
		((AccountToCashVO) (wrapper.getProductVO())).setBalance(agentBalance);

	    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		wrapper.getTransactionDetailModel().setCustomField1(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId().toString());
		wrapper.getTransactionDetailModel().setCustomField2(StringUtil.replaceString(agentAccNo, 5, "*"));
		wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());
		
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);

		String agentSMS=this.getMessageSource().getMessage(
				"USSD.AgentA2CSMS",
				new Object[] {
						Formatter.formatDouble(wrapper.getTransactionAmount()),
						wrapper.getRecipientWalkinCustomerModel().getCnic(),
						dtf.print(new DateTime()),
						tf.print(new LocalTime()),
						wrapper.getTransactionCodeModel().getCode()
						},
						null);

		messageList.add(new SmsMessage(wrapper.getTransactionModel().getToRetContactMobNo(), agentSMS));

		String customerSMS= this.getMessageSource().getMessage(
				"USSD.CustomerA2CLeg2SMS",
				new Object[] {
						wrapper.getRecipientWalkinCustomerModel().getCnic(),
						Formatter.formatDouble(wrapper.getTransactionAmount()),
						dtf.print(new DateTime()),
						tf.print(new LocalTime()),
						wrapper.getTransactionCodeModel().getCode()
						},
						null);
		
		wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
		
		messageList.add(new SmsMessage(wrapper.getTransactionModel().getCustomerMobileNo(), customerSMS));

		String walkinCustomerSMS= this.getMessageSource().getMessage(
				"USSD.walkinCustomerA2CSMS",
				new Object[] {
						Formatter.formatDouble(wrapper.getTransactionAmount()),
						wrapper.getSenderAppUserModel().getMobileNo(),
						dtf.print(new DateTime()),
						tf.print(new LocalTime()),
						wrapper.getTransactionCodeModel().getCode()
						},
						null);

		messageList.add(new SmsMessage(wrapper.getRecipientWalkinCustomerModel().getMobileNumber(), walkinCustomerSMS));
		
		wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
		wrapper.getTransactionDetailModel().setCustomField10(walkinCustomerSMS);
		
	    txManager.saveTransaction(wrapper); //save the transaction

	    wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
	    
    return wrapper;
    
  }

  /**
   * doValidate
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   */
protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
      Exception
  {

    DistributorContactModel toDistributorContactModel = wrapper.getToDistributorContactModel();

    if(wrapper.getUserDeviceAccountModel() == null )
	{
		throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
	}

    //  Validates the workflowWrapper's requirements
    if ( wrapper.getTransactionAmount() <= 0 )
    {
       throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
    }
    return wrapper;
  }

  public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
    // Prepares the Transaction Wrapper object and sends it to the Transaction module( TransactionManager ) for
    // saving that transaction in the database

//    txManager.saveTransaction(wrapper);

    return wrapper;
  }

  protected WorkFlowWrapper doCreditTransferStart(WorkFlowWrapper wrapper)
  {
    return wrapper;
  }

  protected WorkFlowWrapper doCreditTransferEnd(WorkFlowWrapper wrapper)
  {
    return wrapper;
  }

  /**
   * Populates the transaction object with all the necessary data to save it in the db.
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)throws Exception
  {
	  BaseWrapper baseWrapper = new BaseWrapperImpl();

	  // Populate the Product model from DB
	  baseWrapper.setBasePersistableModel(wrapper.getProductModel());
	  baseWrapper = productManager.loadProduct(baseWrapper);
	  wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

	  UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
	  userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
	  userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
	  baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
	  baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
	  wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

	  wrapper.setCustomerModel(new CustomerModel());
	  wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getRetailerContactId());

	  // --Setting instruction and success Message
	  NotificationMessageModel notificationMessage = new NotificationMessageModel();
	  notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
	  baseWrapper.setBasePersistableModel(notificationMessage);
	  baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
	  wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

	  // wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
	  // NotificationMessageModel) baseWrapper.getBasePersistableModel());
	  NotificationMessageModel successMessage = new NotificationMessageModel();
	  successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
	  baseWrapper.setBasePersistableModel(successMessage);
	  baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
	  wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

	  // Populate the Retailer contact model from DB
	  baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
	  baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
	  wrapper.setRetailerContactModel((RetailerContactModel) baseWrapper.getBasePersistableModel());

	  // Populate the Retailer model from DB
	  RetailerModel retailerModel = new RetailerModel();
	  retailerModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
	  baseWrapper.setBasePersistableModel(retailerModel);
	  baseWrapper = retailerManager.loadRetailer(baseWrapper);
	  wrapper.setRetailerModel((RetailerModel) baseWrapper.getBasePersistableModel());

	  // Populate the Agent Smart Money Account from DB
	  baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
	  baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
	  wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

	  wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());

	  wrapper.setPaymentModeModel(new PaymentModeModel());
	  wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

	  wrapper.setPaymentModeModel(new PaymentModeModel());
	  wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

	  // Added by mudassir - Populate the walk in customer Smart Money Account from DB
	  WalkinCustomerModel walkinCustomerModel = mfsAccountManager.getWalkinCustomerModel(wrapper.getRecipientWalkinCustomerModel());
	  wrapper.setRecipientWalkinCustomerModel(walkinCustomerModel);

	  SmartMoneyAccountModel smartMoneyAccountModel = wrapper.getRecipientWalkinSmartMoneyAccountModel();
	  smartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
	  //		smartMoneyAccountModel.setWalkinCustomerIdWalkinCustomerModel(walkinCustomerModel);
	  SmartMoneyAccountModel walkinRecipeientSMA = mfsAccountManager.getSmartMoneyAccount(smartMoneyAccountModel);

	  wrapper.setRecipientWalkinSmartMoneyAccountModel(walkinRecipeientSMA);

	  // Populate the Sender Customer Smart Money Account from DB
	  SmartMoneyAccountModel senderSmartMoneyAccountModel = wrapper.getSenderSmartMoneyAccountModel();
	  senderSmartMoneyAccountModel.setCustomerId(wrapper.getTransactionModel().getCustomerId());
	  smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
	  baseWrapper.setBasePersistableModel(senderSmartMoneyAccountModel);
	  baseWrapper = smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
	  wrapper.setSenderSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

	  /*AppUserModel senderAppUserModel =  wrapper.getSenderAppUserModel();
		senderAppUserModel.setCustomerId(wrapper.getTransactionModel().getCustomerId());
		baseWrapper.setBasePersistableModel(senderAppUserModel);
		wrapper.setSenderAppUserModel(appUserManager.getAppUserModel(senderAppUserModel));
	   */
		
    return wrapper;
  }
  

  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
	  wrapper = super.doPreProcess(wrapper);

		if (logger.isDebugEnabled())
		{
			logger.debug("Strat/End doPreProcess(WorkFlowWrapper wrapper) of AccountToCashLeg2Transaction....");
		}

    return wrapper;
  }

	@Override
	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper)
			throws Exception {
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper)
			throws Exception {
		// TODO Auto-generated method stub
		return wrapper;
	}

	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		String code = wrapper.getTransactionCodeModel().getCode();
		logger.info("[AccountToCashLeg2Transaction.rollback] Rolling back A2C Leg2 transaction with  ID: " + code);
		try{
//			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
//			wrapper.getTransactionModel().setTransactionId(null);
//			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
			txManager.saveTransaction(wrapper);
		}catch(Exception ex){
			logger.error("Unable to save A2C Leg2 Transaction ("+code+") details in case of rollback: \n"+ ex.getStackTrace());
		}
		
		if(null != wrapper.getOLASwitchWrapper()){
			logger.info("[AccountToCashTransaction.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
			abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,1));
			if(null != wrapper.getOlaSwitchWrapper_2()){
				abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,2));
			}
		}
		logger.info("[AccountToCashLeg2Transaction.rollback] rollback complete..."); 
		
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

	private Double checkBalance(WorkFlowWrapper workFlowWrapper, SwitchWrapper switchWrapper, AbstractFinancialInstitution financialInstitution) throws FrameworkCheckedException, Exception {

		SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();

		switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC,	ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
		switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
		switchWrapperTemp.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
		switchWrapperTemp.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());
		switchWrapperTemp.setIsAccountToCashLeg2(true);
		
		workFlowWrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
//		workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);

		switchWrapperTemp = financialInstitution.checkBalanceWithoutPin(switchWrapperTemp);

		return switchWrapperTemp.getBalance();
	}

  
  
  /**
	 * This method calls the commission module to calculate the commission on
	 * this product and transaction.The wrapper should have product,payment mode
	 * and principal amount that is passed onto the commission module
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception
	{
		// ------ Calculate the commission
		// ------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		//----------------------Maqsood Shahzad -----------------------
		
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
		{
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(custModel);
			bWrapper = customerManager.loadCustomer(bWrapper);
			if(null != bWrapper.getBasePersistableModel())
			{
				custModel = (CustomerModel) bWrapper.getBasePersistableModel();
				wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
			}
		}
		
		
		
		//--------------------------------------------------------------
		
		
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		return commissionWrapper;
	}

	/**
	 * 
	 * @param commissionHolder
	 *            CommissionAmountsHolder
	 * @param calculatedCommissionHolder
	 *            CommissionAmountsHolder
	 * @throws FrameworkCheckedException
	 */
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		 java.text.DecimalFormat fourDForm = new java.text.DecimalFormat("#.####");
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of BillSaleTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		AccountToCashVO productVO = (AccountToCashVO) workFlowWrapper.getProductVO();

		if (!Double.valueOf(Formatter.formatDouble(productVO.getCurrentBillAmount().doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getBillAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalCommissionAmount().doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalCommissionAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalAmount().doubleValue())).equals(  Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalAmount().doubleValue()))))
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

		if (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of BillSaleTransaction...");
		}

	}
	
	private CommissionTransactionModel getCommissionTransactionModelByStakeholderIdAndTransactionId(Long commStakeholderId, Long TransactionDetailId) throws FrameworkCheckedException{
		
		CommissionTransactionModel commissoinTxModel = new CommissionTransactionModel();
		commissoinTxModel.setCommissionStakeholderId(commStakeholderId);
		commissoinTxModel.setTransactionDetailId(TransactionDetailId);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(commissoinTxModel);
		searchBaseWrapper = commissionManager.getCommissionTransactionModel(searchBaseWrapper);
		
		commissoinTxModel = (CommissionTransactionModel)searchBaseWrapper.getBasePersistableModel();
	
		return commissoinTxModel;
	}

	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of BillSaleTransaction....");
		}
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList)
		{
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.EXCLUSIVE_CHARGES.longValue())
			{
				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
					transProcessingAmount += commissionRateModel.getRate();
				else
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;

				//transProcessingAmount += commissionRateModel.getRate();
				//System.out.println( "  !!!!!!!!!!!!!!!!!!1 " + transProcessingAmount );
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending getTransactionProcessingCharges of BillSaleTransaction....");
		}
		return transProcessingAmount;
	}

  public void setDistributorContactManager(DistributorContactManager distributorContactManager)
  {
    this.distributorContactManager = distributorContactManager;
  }

public DistributorContactManager getDistributorContactManager() {
	return distributorContactManager;
}

public void setUserDeviceAccountsManager(
		UserDeviceAccountsManager userDeviceAccountsManager) {
	this.userDeviceAccountsManager = userDeviceAccountsManager;
}

public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
{
	this.smartMoneyAccountManager = smartMoneyAccountManager;
}

public void setCustomerManager(CustTransManager customerManager)
{
	this.customerManager = customerManager;
}

public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager)
{
	this.notificationMessageManager = notificationMessageManager;
}

public void setProductManager(ProductManager productManager)
{
	this.productManager = productManager;
}

public void setCommissionManager(CommissionManager commissionManager)
{
	this.commissionManager = commissionManager;
}

public WalkinCustomerModel getRecepientWalkinCustomerModel() {
	return recepientWalkinCustomerModel;
}

public void setRecepientWalkinCustomerModel(WalkinCustomerModel recepientWalkinCustomerModel) {
	this.recepientWalkinCustomerModel = recepientWalkinCustomerModel;
}

public SmartMoneyAccountModel getRecepientWalkinSmartMoneyAccountModel() {
	return recepientWalkinSmartMoneyAccountModel;
}

public void setRecepientWalkinSmartMoneyAccountModel(SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel) {
	this.recepientWalkinSmartMoneyAccountModel = recepientWalkinSmartMoneyAccountModel;
}

public void setMfsAccountManager(MfsAccountManager mfsAccountManager)
{
	this.mfsAccountManager = mfsAccountManager;
}

public MessageSource getMessageSource() {
	return messageSource;
}

public void setMessageSource(MessageSource messageSource) {
	this.messageSource = messageSource;
}

public void setRetailerContactManager(RetailerContactManager retailerContactManager)
{
  this.retailerContactManager = retailerContactManager;
}

public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
	this.phoenixFinancialInstitution = phoenixFinancialInstitution;
}

public void setRetailerManager(RetailerManager retailerManager) {
	this.retailerManager = retailerManager;
}

}
