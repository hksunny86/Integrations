package com.inov8.microbank.server.service.workflow.credittransfer;


import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ErrorCodes;
import com.inov8.microbank.common.util.ErrorLevel;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstants;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.creditmodule.DistDistCreditManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.distributormodule.DistributorManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.BulkPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;
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

public class BulkPaymentLeg2Transaction
    extends CreditTransferTransaction
{
  private DistDistCreditManager distDistCreditManager;
  private DistributorContactManager distributorContactManager;
  private SmsSender smsSender;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private NotificationMessageManager notificationMessageManager;
  private ProductManager productManager;
  private CustTransManager customerManager;
  private CommissionManager commissionManager;
  private ProductDispenseController productDispenseController;
  private DistributorManager distributorManager;
  private RetailerManager retailerManager;
  
  private MfsAccountManager mfsAccountManager;
  private WalkinCustomerModel recepientWalkinCustomerModel;
  private SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel;
  private BillPaymentProductDispenser accountToCashDispenser;
  private SettlementManager settlementManager;
  private RetailerContactManager retailerContactManager;
  
  private MessageSource messageSource;
  private AbstractFinancialInstitution phoenixFinancialInstitution ;
  DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
  DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
  private String rrnPrefix;
  

  public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)  {
	this.financialIntegrationManager = financialIntegrationManager;
  }

  public BulkPaymentLeg2Transaction(){

  }

	/*private WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside loadProductDispenser(WorkFlowWrapper wrapper) of BulkPaymentLeg2Transaction...");
			logger.debug("Loading ProductDispenser...");
		}

		accountToCashDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Fetching Bill Info through Product Dispenser...");
		}
		
		wrapper = accountToCashDispenser.getBillInfo(wrapper);

		return wrapper;
	}*/
	
  /**
   * Credit transfer transaction takes place over here
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   */
  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception
  {

	  	TransactionDetailModel txDetailModel= null;
	  
	  	logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer] Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
//	  	wrapper.getTransactionModel().setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
//	  	wrapper.getTransactionModel().setDistributorId();//TODO
	  	wrapper.getTransactionModel().setToRetContactId(wrapper.getAppUserModel().getRetailerContactId());
	  	wrapper.getTransactionModel().setToRetContactMobNo(wrapper.getAppUserModel().getMobileNo());
	  	wrapper.getTransactionModel().setToRetContactName(wrapper.getAppUserModel().getLastName() + " " + wrapper.getAppUserModel().getFirstName());
	  	wrapper.getTransactionModel().setUpdatedBy(wrapper.getAppUserModel().getAppUserId());
	  	wrapper.getTransactionModel().setUpdatedOn(new Date());
	  	wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getRecipientWalkinCustomerModel().getMobileNumber());
//	  	wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getSenderWalkinCustomerModel().getMobileNumber());
	  	wrapper.getTransactionModel().setDiscountAmount(0d);
//	  	wrapper.getTransactionModel().setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
	  	wrapper.getTransactionDetailModel().setCustomField13( "" + wrapper.getDeviceTypeModel().getDeviceTypeId());
	  	wrapper.getTransactionModel().setIssue(Boolean.FALSE);
	  	wrapper.getTransactionDetailModel().setSettled(Boolean.TRUE);
	  	wrapper.getTransactionDetailModel().setCustomField5(wrapper.getRecipientWalkinCustomerModel().getMobileNumber());
		wrapper.getTransactionDetailModel().setConsumerNo(wrapper.getTransactionDetailModel().getCustomField5());
		wrapper.getTransactionDetailModel().setActualBillableAmount(wrapper.getTransactionAmount());
		wrapper.getTransactionDetailModel().setCustomField1(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId().toString());
//		wrapper.getTransactionDetailModel().setCustomField6(wrapper.getTransactionModel().getNotificationMobileNo());
		wrapper.getTransactionModel().setPaymentModeId(wrapper.getPaymentModeModel().getPaymentModeId());
	  	wrapper.getTransactionModel().setToRetailerId(wrapper.getRetailerContactModel().getRetailerId());
	  	wrapper.getTransactionModel().setToDistributorId(wrapper.getDistributorModel().getDistributorId());
		
		BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.setBasePersistableModel(wrapper.getTransactionModel());
		
		logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer] Saving transaction. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
		txManager.saveTransactionModel(bWrapper); //save the transaction
		
	    BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
		switchWrapper.setOlavo(new OLAVO());
		switchWrapper.getOlavo().setTransactionTypeId(TransactionTypeConstantsInterface.OLA_DEBIT);
		
		wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());
		
		//if agent is Head agent, then update agent commission (agent commission = agemtn commission + franchise commission)
		Double franchise2Commission = 0.0d;
		
		
		if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){

			logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer] Head Agent found. Loading Franchise CommissionTransactionModel. TrxDetailId:" + wrapper.getTransactionDetailModel().getTransactionDetailId() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			
			CommissionTransactionModel commissionTxModel = getCommissionTransactionModelByStakeholderIdAndTransactionId(CommissionConstantsInterface.FRANCHISE2_STAKE_HOLDER_ID, wrapper.getTransactionDetailModel().getTransactionDetailId());
			
			if (commissionTxModel != null) {
				
				logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer] Removing Franchise CommissionTrxModel and Merging TxDetailMaster Entry for CommTrxID:" + commissionTxModel.getCommissionTransactionId() + " TrxDetailId:" + wrapper.getTransactionDetailModel().getTransactionDetailId() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
				franchise2Commission = commissionTxModel.getCommissionAmount();
				
				//remove franchise entry of commissionTxModel because agent = franchise in case agent is head agent.
				commissionManager.removeCommissionTransactionModel(commissionTxModel);
			}
			
		}

		logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer] Loading CommissionTransaction of Agent 2. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
		CommissionTransactionModel commissoinTxModel = getCommissionTransactionModelByStakeholderIdAndTransactionId(CommissionConstantsInterface.AGENT2_STAKE_HOLDER_ID, wrapper.getTransactionDetailModel().getTransactionDetailId());

		CommissionAmountsHolder amountsHolder = new CommissionAmountsHolder();
		amountsHolder.setFranchise2CommissionAmount(0.0d);//Fix applied. Donot debit sundry with franchise 2 commission.
		amountsHolder.setTotalCommissionAmount(wrapper.getTransactionModel().getTotalCommissionAmount());
		
		
		//agent 2 commission can be null if commission rate is not defined for the slab in which use segment falls
		if(commissoinTxModel != null && commissoinTxModel.getCommissionAmount() != null){
			
			logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer] Merging CommissionTransaction of Franchise 2 with Agent 2. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent CommissionTransactionId: " + commissoinTxModel.getCommissionTransactionId());
			
			if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
				
				commissoinTxModel.setCommissionAmount(commissoinTxModel.getCommissionAmount() + franchise2Commission);
				amountsHolder.setAgent2CommissionAmount(commissoinTxModel.getCommissionAmount());
				wrapper.getTransactionDetailMasterModel().setAgent2Commission(commissoinTxModel.getCommissionAmount());
				wrapper.getTransactionDetailMasterModel().setFranchise2Commission(0d);
				
			}else{
				
				amountsHolder.setAgent2CommissionAmount(commissoinTxModel.getCommissionAmount());
			}
			
			logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer] Saving CommissionTransaction of Agent 2. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent CommissionTransactionId: " + commissoinTxModel.getCommissionTransactionId());
			
			//save agent 2 commissionTxModel with settled = 1 (bug 688 fix)
			commissoinTxModel.setSettled(Boolean.TRUE);		
			commissoinTxModel.setUpdatedOn(new java.util.Date());
			settlementManager.saveCommissionTransactionMoel(commissoinTxModel);
			
		}else{
			amountsHolder.setAgent2CommissionAmount(0.0D);
		}
		
		wrapper.setCommissionAmountsHolder(amountsHolder);

		logger.info("[BulkPaymentLeg2Transaction.doCreditTransfer()] Going to debit Sundry Account. Trx ID:" + wrapper.getTransactionCodeModel().getCode() + " Agent SmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		
		//Debit cash To Cash sundry account in OLA
		switchWrapper = abstractFinancialInstitution.bulkPaymentTransaction(switchWrapper);
		wrapper.setOLASwitchWrapper(switchWrapper);
		
		
		switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel()) ;
		switchWrapper.setWorkFlowWrapper(wrapper) ;
		switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
		switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);

		String senderBankAccountNo = switchWrapper.getWorkFlowWrapper().getTransactionModel().getBankAccountNo();
		
		TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
		Double balanceBeforeFT = 0d;
		try {

			switchWrapper.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
			SwitchWrapper newSwitchWrapper = phoenixFinancialInstitution.checkBalance(switchWrapper);
			balanceBeforeFT = newSwitchWrapper.getBalance();
			
		} catch (CommandException ce) {
			
			throw new CommandException(ce.getMessage() + " for your account.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
		
		}finally{
			
			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp) ;
			
		}

		/*Moving SMS before FT to avoid JMS failure causing rollback issues*/
/*********************************************************************************************************************************************/
		// Amount of Rs. {0},\n withdrawn on {1} {2}\nCharges Rs. {3}\n Tx ID\: {4}
		
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
		
		String agentSMS=this.getMessageSource().getMessage(
				"USSD.AgentBulkPaymentLeg2SMS",
				new Object[] {
						Formatter.formatDouble(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTotalCommissionAmount()),
						dtf.print(new DateTime()),
						tf.print(new LocalTime()),
						Formatter.formatDouble(amountsHolder.getTotalCommissionAmount()),
						wrapper.getTransactionCodeModel().getCode()
						},
						null);

		wrapper.getTransactionDetailModel().setCustomField4(agentSMS);

		messageList.add(new SmsMessage(wrapper.getTransactionModel().getToRetContactMobNo(), agentSMS));

		String totalCommissionAmt =Formatter.formatDouble(amountsHolder.getTotalCommissionAmount());
		if (StringUtils.isNotEmpty(wrapper.getTransactionDetailModel().getCustomField14()) && wrapper.getTransactionDetailModel().getCustomField14().equals(PaymentModeConstants.PDMA_TYPE_ID)) {
			totalCommissionAmt = "0";
		}
		
		//Received Amount of Rs. {0}\nfrom Agent ({1})\nCharges Deducted: {2}\nAgent mobile number {3}\non {4} {5},\nTx ID {6}
		String walkinCustomerSMS= this.getMessageSource().getMessage(
				"USSD.ReceiverBulkPaymentLeg2SMS",
				new Object[] {
						Formatter.formatDouble(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTotalCommissionAmount()),
						wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
						totalCommissionAmt,
						wrapper.getAppUserModel().getMobileNo(),						
						dtf.print(new DateTime()),
						tf.print(new LocalTime()),
						wrapper.getTransactionCodeModel().getCode()
						},
						null);
		
		wrapper.getTransactionDetailModel().setCustomField10(walkinCustomerSMS);
		
		messageList.add(new SmsMessage(wrapper.getTransactionDetailModel().getCustomField5(), walkinCustomerSMS));

		
/*********************************************************************************************************************************************/
		
		rrnPrefix = buildRRNPrefix();
		
		try{
			//make FT at Phoenix
			switchWrapper = phoenixFinancialInstitution.debitCreditAccount(switchWrapper) ;
			
			//Update balance after transaction in VO for using in agent SMS.
			((BulkPaymentVO) (wrapper.getProductVO())).setBalance(balanceBeforeFT + switchWrapper.getTransactionAmount());

		}finally{
			
			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp) ;
			
		}
		
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(senderBankAccountNo); //Bug # 995
		wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());
		wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());//put agent account number//TODO
	    wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());
	    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
	    wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
		
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
		logger.error("[BulkPaymentLeg2Transaction.doCreditTransfer] LoggRolling back transaction with  ID: " + code);
		if(null != wrapper.getFirstFTIntegrationVO())
		{
			
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(PhoenixFinancialInstitutionImpl.class.getName());

			String rrn = wrapper.getFirstFTIntegrationVO().getRetrievalReferenceNumber();
			rrn = rrnPrefix + rrn;
			rrnPrefix = null;
			logger.error("[BulkPaymentLeg2Transaction.doCreditTransfer] FT was done so trying to roll back with RRN  : "+ rrn +" and trx code : "+code);
			PhoenixIntegrationMessageVO phoenixIntegrationMessageVO = new PhoenixIntegrationMessageVO();
			phoenixIntegrationMessageVO.setRetrievalReferenceNumber( rrn );
			phoenixIntegrationMessageVO.setChannelId("MR0001");
			
			SwitchWrapper switchWrapper = wrapper.getFirstFTSwitchWrapper();
			switchWrapper.setIntegrationMessageVO(phoenixIntegrationMessageVO);
			String fromAccount = new String(switchWrapper.getFromAccountNo());
			String toAccount = new String(switchWrapper.getToAccountNo());
			//reverse accounts for posted transaction report...
			switchWrapper.setFromAccountNo(toAccount);
			switchWrapper.setToAccountNo(fromAccount);
			
			logger.error("[BulkPaymentLeg2Transaction.doCreditTransfer] Hitting Phoenix to rollback with RRN : "+rrn+" and trx code : "+code);
			switchWrapper = abstractFinancialInstitution.reverseFundTransfer(switchWrapper);
			
		}
		
		if(null != wrapper.getOLASwitchWrapper()){//sundry account was debited earlier. Time to credit sundry.
			logger.error("[CashToCashTransaction.rollback] OLA Sundry Account was Debited. so trying to rollback. Transaction ID:" + code + ". OLA Auth Code:" + wrapper.getOLASwitchWrapper().getOlavo().getAuthCode());
			AbstractFinancialInstitution olaFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());
			olaFinancialInstitution.rollback(wrapper.getOLASwitchWrapper());
		}
		
		return wrapper;
	}
	

  
  
  private Double checkBalance(WorkFlowWrapper workFlowWrapper, SwitchWrapper switchWrapper, 
			AbstractFinancialInstitution financialInstitution) throws FrameworkCheckedException, Exception {

	SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();

	switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC,	ThreadLocalAppUser.getAppUserModel().getNic());
	switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
	switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
	switchWrapperTemp.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
	switchWrapperTemp.setBasePersistableModel(workFlowWrapper.getSmartMoneyAccountModel());

	if (workFlowWrapper.getProductModel().getProductId() == 50011L) {
		workFlowWrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
		workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);

		switchWrapperTemp = financialInstitution.checkBalanceWithoutPin(switchWrapperTemp);
	}

	return switchWrapperTemp.getBalance();
}

  /**
   * doValidate
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   */
  protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

//    this.populateTransactionObject(wrapper);
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

    return wrapper;
  }

  public void setDistDistCreditManager(DistDistCreditManager
                                       distDistCreditManager)
  {
    this.distDistCreditManager = distDistCreditManager;
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

	  // Populate the RetailerContact model from DB
	  baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
	  baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
	  wrapper.setRetailerContactModel((RetailerContactModel) baseWrapper.getBasePersistableModel());

	  // Populate the RetailerModel from DB
	  baseWrapper.setBasePersistableModel(wrapper.getRetailerContactModel().getRetailerIdRetailerModel());
	  baseWrapper = retailerManager.loadRetailer(baseWrapper);
	  wrapper.setRetailerModel((RetailerModel) baseWrapper.getBasePersistableModel());

	  
	  // Populate the DistributorModel from DB
	  baseWrapper.setBasePersistableModel(wrapper.getRetailerModel().getDistributorIdDistributorModel());
	  baseWrapper = distributorManager.loadDistributor(baseWrapper);
	  wrapper.setDistributorModel((DistributorModel) baseWrapper.getBasePersistableModel());

	  // Populate the Agent Smart Money Account from DB
	  baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
	  baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
	  wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

	  wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());
	  
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
	  senderSmartMoneyAccountModel.setRetailerContactId(wrapper.getAppUserModel().getRetailerContactId());
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

		/*TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTotalAmount(wrapper.getTransactionAmount());
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
//		if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId().longValue() == 50006L)
//		{
			txModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
//		}
//		else
//		{
//			txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
//		}

//		// Customer model of transaction is populated
//		if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId().longValue() == 50000L)
//		{
//			txModel.setCustomerIdCustomerModel(new CustomerModel());
//			txModel.getCustomerIdCustomerModel().setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
//		}
		

		// Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Customer mobile No
//		if(wrapper.getProductModel().getProductId().longValue() != 50006L)
//		{
			txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
//		}
		
//		txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
//		if(wrapper.getProductModel().getProductId().longValue() == 50000L)
//		{
//			txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
//		}
//		else
//		{
			txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
//		}

		// Populate processing Bank Id
		txModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
//		txModel.setSaleMobileNo(wrapper.getCustomerModel().getMobileNo());
		if(wrapper.getProductModel().getProductId().longValue() == 50006L)
		{
			txModel.setToRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
			txModel.setToRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
			txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
			txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		}
		
		wrapper.setTransactionModel(txModel);*/
		if (logger.isDebugEnabled())
		{
			logger.debug("Strat/End doPreProcess(WorkFlowWrapper wrapper) of AccountToCashLeg2Transaction....");
		}

    /*wrapper = super.doPreProcess(wrapper);
    TransactionModel transactionModel = wrapper.getTransactionModel();

    transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
    transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
    transactionModel.setTransactionAmount(wrapper.getTransactionAmount());
    transactionModel.setTotalAmount(wrapper.getTransactionAmount());
    transactionModel.setTotalCommissionAmount(0d);
    transactionModel.setDiscountAmount(0d);
    transactionModel.setFromDistContactId(wrapper.getFromDistributorContactModel().getDistributorContactId());
    transactionModel.setToDistContactId(wrapper.getToDistributorContactModel().getDistributorContactId());
//    transactionModel.setDistributorIdDistributorModel(wrapper.getFromDistributorContactModel().getDistributorIdDistributorModel());
    transactionModel.setDistributorNmContactId(wrapper.getDistributorNmContactModel().getDistributorContactId());

    transactionModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
    transactionModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
    transactionModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

    transactionModel.setFromDistContactMobNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
    transactionModel.setSaleMobileNo(wrapper.getFromDistributorContactAppUserModel().getMobileNo());
    transactionModel.setToDistContactMobNo(wrapper.getToDistributorContactAppUserModel().getMobileNo());
    transactionModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getPrimaryKey());
    transactionModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
    transactionModel.setFromDistContactName(wrapper.getFromDistributorContactAppUserModel().getFirstName()+ " "+ wrapper.getFromDistributorContactAppUserModel().getLastName());

    if( wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList() != null && ((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) != null )
    {
      AppUserModel toDistAppUser = (AppUserModel)((List)wrapper.getToDistributorContactModel().getDistributorContactIdAppUserModelList()).get(0) ;
      wrapper.getTransactionModel().setToDistContactName(toDistAppUser.getFirstName() + " " + toDistAppUser.getLastName()) ;
    }

    transactionModel.setDistributorId( wrapper.getFromDistributorContactModel().getDistributorId() );
*/
    return wrapper;
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
		BulkPaymentVO productVO = (BulkPaymentVO) workFlowWrapper.getProductVO();

		/*if (!Double.valueOf(Formatter.formatDouble(productVO.getCurrentBillAmount().doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getBillAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		}*/
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



  public void setDistributorContactManager(DistributorContactManager distributorContactManager)
  {
    this.distributorContactManager = distributorContactManager;
  }

  public void setSmsSender(SmsSender smsSender)
  {
    this.smsSender = smsSender;
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

public void setProductDispenseController(ProductDispenseController productDispenseController) {
	this.productDispenseController = productDispenseController;
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

public void setSettlementManager(SettlementManager settlementManager)
{
	this.settlementManager = settlementManager;
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

public DistributorManager getDistributorManager() {
	return distributorManager;
}

public void setDistributorManager(DistributorManager distributorManager) {
	this.distributorManager = distributorManager;
}

public RetailerManager getRetailerManager() {
	return retailerManager;
}

public void setRetailerManager(RetailerManager retailerManager) {
	this.retailerManager = retailerManager;
}

}
