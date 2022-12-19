package com.inov8.microbank.server.service.workflow.credittransfer;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.vo.AgentToAgentTransferVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */

public class RetailerToRetailerTransaction
    extends CreditTransferTransaction
{

  private RetailerContactManager retailerContactManager;
  private SmsSender smsSender;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private CommissionManager commissionManager;
  private ProductManager productManager;
  private MessageSource messageSource;
  DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
  DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
	

  public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
{
	this.smartMoneyAccountManager = smartMoneyAccountManager;
}

public RetailerToRetailerTransaction()
  {
  }

  /**
   * creditTransfer
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @todo Implement this
   *   com.inov8.microbank.server.workflow.credittransfer.CreditTransferTransaction
   *   method
   */
  protected WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws Exception
  {

	CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
	CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
	
	if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
		commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
		commissionAmounts.setFranchise1CommissionAmount(0.0d);
	}

	wrapper.setCommissionAmountsHolder(commissionAmounts);

    RetailerContactModel fromRetailerContactModel = wrapper.getFromRetailerContactModel();
    
    wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
    wrapper.getTransactionModel().setRetailerId(fromRetailerContactModel.getRetailerId());
    wrapper.getTransactionModel().setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
    wrapper.getTransactionModel().setFromRetContactId(fromRetailerContactModel.getRetailerContactId());
    wrapper.getTransactionModel().setToRetContactId(wrapper.getToRetailerContactModel().getRetailerContactId());
	wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
    
    TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
    wrapper.setTransactionDetailModel(transactionDetailModel);
    wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(transactionDetailModel);

	
    transactionDetailModel.setProductId(wrapper.getProductModel().getProductId());
    transactionDetailModel.setConsumerNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
    transactionDetailModel.setActualBillableAmount(wrapper.getTransactionAmount());
    transactionDetailModel.setSettled(true);
    transactionDetailModel.setCustomField1(wrapper.getReceivingSmartMoneyAccountModel().getSmartMoneyAccountId()+"");

    BaseWrapper baseWrapper = new BaseWrapperImpl();
    baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

	  wrapper.getTransactionDetailMasterModel().setSendingRegion(wrapper.getRetailerModel().getRegionModel().getRegionId());
	  wrapper.getTransactionDetailMasterModel().setSendingRegionName(wrapper.getRetailerModel().getRegionModel().getRegionName());

	  wrapper.getTransactionDetailMasterModel().setSenderAreaId(wrapper.getRetailerContactModel().getAreaId());
	  wrapper.getTransactionDetailMasterModel().setSenderAreaName(wrapper.getRetailerContactModel().getAreaName());

	  wrapper.getTransactionDetailMasterModel().setSenderDistributorId(wrapper.getDistributorModel().getDistributorId());
	  wrapper.getTransactionDetailMasterModel().setSenderDistributorName(wrapper.getDistributorModel().getName());
	  if (null != wrapper.getDistributorModel().getMnoId()) {
		  wrapper.getTransactionDetailMasterModel().setSenderServiceOPId(wrapper.getDistributorModel().getMnoId());
		  wrapper.getTransactionDetailMasterModel().setSenderServiceOPName(wrapper.getDistributorModel().getMnoModel().getName());
//			wrapper.getTransactionDetailMasterModel().setSenderServiceOPName(wrapper.getDistributorModel().getMnoId());
	  }

	AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

	SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel()) ;
	switchWrapper.setWorkFlowWrapper(wrapper) ;
	
	logger.info("[RetailerToRetailerTransaction] Going to transfer funds from Agent1 to Agent2. LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + wrapper.getTransactionCodeModel().getCode());
	switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;
	wrapper.setOLASwitchWrapper(switchWrapper);
	
	((AgentToAgentTransferVO) wrapper.getProductVO()).setBalance(switchWrapper.getOlavo().getFromBalanceAfterTransaction());
	switchWrapper.setAgentBalance(switchWrapper.getOlavo().getFromBalanceAfterTransaction());
	
	Double senderBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction();
	Double recipientBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction();

	ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
	
    //{0}\nTrx ID {1}\nRs.{2}\ntransferred from your {3}\nA/C to {4}\n. Fee is Rs.{5}\n incl FED\nAvl Bal is Rs.{6}
	  String brandName = null;

	  if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
		  brandName = MessageUtil.getMessage("sco.brandName");
	  }
	  else {
		  brandName = MessageUtil.getMessage("jsbl.brandName");
	  }
	
    String senderAgentSMS=this.getMessageSource().getMessage(
			"USSD.AgentToAgentTransferSenderSMS",
			new Object[] {
					brandName,
					wrapper.getTransactionCodeModel().getCode(),
					Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
					brandName,
					wrapper.getToRetailerContactAppUserModel().getMobileNo(),
					Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
					senderBalance
					},
					null);
    
	wrapper.getTransactionDetailModel().setCustomField4(senderAgentSMS);
	messageList.add(new SmsMessage(wrapper.getFromRetailerContactAppUserModel().getMobileNo(), senderAgentSMS));
    
    Double totalInclusiveCharges = CommonUtils.getDoubleOrDefaultValue(wrapper.getCommissionAmountsHolder().getInclusiveFixAmount()) + CommonUtils.getDoubleOrDefaultValue(wrapper.getCommissionAmountsHolder().getInclusivePercentAmount());

	//{0}\nTrx ID {1}\nYou have received Rs.{2}\nat {3}\non DD-MM-YY from {4}\nto {5}.\nFee Rs.{6}\nincl FED.\nBal Rs.{7}
	String recipientAgentSMS=this.getMessageSource().getMessage(
			"USSD.AgentToAgentTransferRecipientSMS",
			new Object[] {
					brandName,
					wrapper.getTransactionCodeModel().getCode(),
					Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount() - totalInclusiveCharges),
					tf.print(new LocalTime()),
					dtf.print(new DateTime()),
					wrapper.getFromRetailerContactAppUserModel().getMobileNo(),
					wrapper.getToRetailerContactAppUserModel().getMobileNo(),
					Formatter.formatDouble(totalInclusiveCharges),
					recipientBalance
					},
					null);
	
	messageList.add(new SmsMessage(wrapper.getToRetailerContactAppUserModel().getMobileNo(), recipientAgentSMS));

//	wrapper.getTransactionModel().setBankResponseCode("00");
	wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
	wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
    wrapper.getTransactionModel().setConfirmationMessage(recipientAgentSMS);
    wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
	
    wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
	wrapper.getTransactionDetailModel().setCustomField3(""+switchWrapper.getSwitchSwitchModel().getSwitchId());

    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
	txManager.saveTransaction(wrapper);
	
	this.settlementManager.settleCommission(commissionWrapper, wrapper);

	wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
	
    return wrapper;
  }

  /** Transfer funds from Agent1 BB Account to Agent2 BB Account
   * 
   * @param workFlowWrapper
   * @return
   * @throws Exception
   */
  private WorkFlowWrapper transferFunds(WorkFlowWrapper workFlowWrapper) throws Exception{
		AgentToAgentTransferVO agentToAgentTransferVO = (AgentToAgentTransferVO) workFlowWrapper.getProductVO();
					
		SmartMoneyAccountModel senderAgentSMA  = workFlowWrapper.getSmartMoneyAccountModel();
		SmartMoneyAccountModel recievingAgentSMA  = workFlowWrapper.getReceivingSmartMoneyAccountModel();

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(senderAgentSMA);
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		
		TransactionModel transactionModelTemp = workFlowWrapper.getTransactionModel() ;
		//Checking balance before FT
		SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
		switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
		switchWrapperTemp.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
		switchWrapperTemp.setBasePersistableModel( senderAgentSMA ) ;
		workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);

		switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, workFlowWrapper.getAccountInfoModel().getOldPin());
		switchWrapperTemp.setWorkFlowWrapper(workFlowWrapper);
		switchWrapperTemp.setTransactionTransactionModel(workFlowWrapper.getTransactionModel());
		switchWrapperTemp.setBasePersistableModel( senderAgentSMA );
		
	    switchWrapperTemp.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(""+recievingAgentSMA.getSmartMoneyAccountId());

		logger.info("[RetailerToRetailerTransaction] checking Balance for Sender SMA ID:" + senderAgentSMA.getSmartMoneyAccountId() + ". Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
		switchWrapperTemp = abstractFinancialInstitution.checkBalance(switchWrapperTemp); 
		workFlowWrapper.setTransactionModel(transactionModelTemp);
		//End checking balance before FT

		if(switchWrapperTemp != null && (switchWrapperTemp.getBalance() - workFlowWrapper.getTransactionModel().getTotalAmount() < 0)){
			throw new WorkFlowException( WorkFlowErrorCodeConstants.INSUFFICIENT_ACC_BALANCE ) ;
		}
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setBasePersistableModel(senderAgentSMA) ;
		switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
		
		switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
		switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount())));
		transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
		
		logger.info("[RetailerToRetailerTransaction] Going to transfer funds from Agent1 to Agent2. LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + workFlowWrapper.getTransactionCodeModel().getCode());

//		************************		phoenixFinancialInst.debitCreditAccount(switchWrapper);
		switchWrapper = abstractFinancialInstitution.debitCreditAccount(switchWrapper);
		workFlowWrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
		
		switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
		agentToAgentTransferVO.setBalance(switchWrapperTemp.getBalance()-workFlowWrapper.getTransactionModel().getTotalAmount());
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankResponseCode("00");
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		switchWrapper.getWorkFlowWrapper().getTransactionModel().setBankAccountNo(StringUtil.replaceString(switchWrapper.getFromAccountNo(), 5, "*"));
		switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(StringUtil.replaceString(switchWrapper.getToAccountNo(), 5, "*"));
		switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField3(""+switchWrapper.getSwitchSwitchModel().getSwitchId());
  
		return workFlowWrapper;

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
		SegmentModel segmentModel = new SegmentModel();
		segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
		
		wrapper.setSegmentModel(segmentModel);
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
	    wrapper.setTaxRegimeModel(wrapper.getFromRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of BillSaleTransaction...");
		}
		return commissionWrapper;
	}

  
  
public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
  {
    RetailerContactModel toRetailerContactModel = wrapper.getToRetailerContactModel();
    RetailerContactModel fromRetailerContactModel = wrapper.getFromRetailerContactModel();

    // Validates the To Retailer's requirements
    if (toRetailerContactModel != null)
    {
      if (!toRetailerContactModel.getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.TORETAILER_NOT_ACTIVE);
      }
      if (!toRetailerContactModel.getRetailerIdRetailerModel().getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.TORET_RETAILER_INACTIVE);
      }

    }
    else
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_RETAILER_AGAINST_MOBILENO);
    }

    // Validates the From Retailer's requirements
    if (fromRetailerContactModel != null)
    {
      if (!fromRetailerContactModel.getActive())
      {
        throw new WorkFlowException(WorkFlowErrorCodeConstants.FROMRETAILER_NOT_ACTIVE);
      }
    }
    else
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.FROMRETAILER_NULL);
    }

    Long retailerContactId = fromRetailerContactModel.getRetailerContactId();
    // Check whether 'to' and 'from' retailers are same
    if ( retailerContactId != null && retailerContactId.equals(toRetailerContactModel.getRetailerContactId()))
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.SAME_TO_FROM_RETAILERS);
    }

    if(wrapper.getUserDeviceAccountModel() == null )
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
    }

    //  Validates the workflowWrapper's requirements
    if ( wrapper.getTransactionAmount() <= 0 )
    {
       throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
    }

   /* Commenting out below conditions to Open Agent Fund Transfer horizontally and vertically within the same and across multiple Agent Networks
     
    Long distributorId = toRetailerContactModel.getRelationRetailerIdRetailerModel().getDistributorId();
    if( distributorId == null || !distributorId.equals(fromRetailerContactModel.getRelationRetailerIdRetailerModel().getDistributorId()))
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAIL_RETAIL_DIFF_DISTRIBUTOR);
    }

    Long retailerId = toRetailerContactModel.getRetailerId(); 
    if( retailerId == null || !retailerId.equals(fromRetailerContactModel.getRetailerId()))
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAIL_RETAIL_DIFF_RETAILER);
    }*/

    return wrapper;
  }

  public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception
  {
	  txManager.saveTransaction(wrapper);
    return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)throws Exception
  {
    wrapper = super.doPreStart(wrapper);
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
   
    
    baseWrapper.setBasePersistableModel(wrapper.getProductModel());
	baseWrapper = productManager.loadProduct(baseWrapper);
	wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());
	
	
    baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
    baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
   	wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel)baseWrapper.getBasePersistableModel());
   	baseWrapper.setBasePersistableModel(wrapper.getReceivingSmartMoneyAccountModel());
   	baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
   	if(null != baseWrapper && null != baseWrapper.getBasePersistableModel())
   	{
   		wrapper.setReceivingSmartMoneyAccountModel( (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel());
   	}
   	else
   	{
   		throw new WorkFlowException(WorkFlowErrorCodeConstants.AGENT_ACCOUNT_NOT_FOUND);
   	}
    

    baseWrapper.setBasePersistableModel(wrapper.getFromRetailerContactAppUserModel());
    baseWrapper = this.retailerContactManager.loadRetailerContactByAppUser(baseWrapper);
    wrapper.setFromRetailerContactModel((RetailerContactModel)baseWrapper.getBasePersistableModel());
    wrapper.setRetailerContactModel((RetailerContactModel)baseWrapper.getBasePersistableModel());

    AppUserModel tempAppUserModel = new AppUserModel();
    tempAppUserModel.setMobileNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
    wrapper.getToRetailerContactAppUserModel().setAppUserTypeId( UserTypeConstantsInterface.RETAILER );
    searchBaseWrapper.setBasePersistableModel(tempAppUserModel);
    wrapper.setToRetailerContactModel( retailerContactManager.findRetailerContactByMobileNumber(searchBaseWrapper) );

    UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
    userDeviceAccountsModel.setAppUserId(wrapper.getFromRetailerContactAppUserModel().getAppUserId());
    userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
    baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
    baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
    wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel)baseWrapper.getBasePersistableModel());


    return wrapper;
  }

  @Override
  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
  {
    wrapper = super.doPreProcess(wrapper);
    RetailerContactModel fromRetailerContactModel = wrapper.getFromRetailerContactModel();
    RetailerContactModel toRetailerContactModel = wrapper.getToRetailerContactModel();

    wrapper.getTransactionModel().setFromRetContactIdRetailerContactModel(fromRetailerContactModel);
    wrapper.getTransactionModel().setDistributorId(fromRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
    wrapper.getTransactionModel().setToRetContactIdRetailerContactModel(toRetailerContactModel);
    wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
    wrapper.getTransactionModel().setTotalAmount(wrapper.getTransactionAmount());
    wrapper.getTransactionModel().setTotalCommissionAmount(0D);
    wrapper.getTransactionModel().setDiscountAmount(0D);
    wrapper.getTransactionModel().setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getPrimaryKey());
    wrapper.getTransactionModel().setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
//    wrapper.getTransactionModel().setProcessingSwitchId(SwitchConstants.OLA_SWITCH);
    wrapper.getTransactionModel().setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
    wrapper.getTransactionModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
    wrapper.getTransactionModel().setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());
    wrapper.getTransactionModel().setRetailerId( fromRetailerContactModel.getRetailerId() );
	wrapper.getTransactionModel().setConfirmationMessage(" _ ");

    if( toRetailerContactModel.getRetailerContactIdAppUserModelList() != null && ((List)toRetailerContactModel.getRetailerContactIdAppUserModelList()).get(0) != null )
    {
      AppUserModel retailerAppUser = (AppUserModel)((List)toRetailerContactModel.getRetailerContactIdAppUserModelList()).get(0) ;
      wrapper.getTransactionModel().setToRetContactName(retailerAppUser.getFirstName() + " " + retailerAppUser.getLastName()) ;
    }

    wrapper.getTransactionModel().setFromRetContactMobNo(wrapper.getFromRetailerContactAppUserModel().getMobileNo());
    wrapper.getTransactionModel().setSaleMobileNo(wrapper.getFromRetailerContactAppUserModel().getMobileNo());
    wrapper.getTransactionModel().setToRetContactMobNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
    wrapper.getTransactionModel().setFromRetContactName(wrapper.getFromRetailerContactAppUserModel().getFirstName() + " " + wrapper.getFromRetailerContactAppUserModel().getLastName());
    wrapper.getTransactionModel().setToRetContactName(wrapper.getToRetailerContactAppUserModel().getFirstName() + " " + wrapper.getToRetailerContactAppUserModel().getLastName());
    wrapper.getTransactionModel().setToRetailerId(toRetailerContactModel.getRetailerId());
    wrapper.getTransactionModel().setToDistributorId(toRetailerContactModel.getRetailerIdRetailerModel().getDistributorId());
    

  return wrapper;
  }
  
  
  @Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		logger.info("[RetailerToRetailerTransaction.rollback] called..."); 
//		try{
//			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
//			wrapper.getTransactionModel().setTransactionId(null);
//			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
//			txManager.saveTransaction(wrapper);
//		}catch(Exception ex){
//			logger.error("[RetailerToRetailerTransaction.rollback] Unable to save Transaction details in case of rollback: \n"+ ex.getStackTrace());
//		}
//		
//		if(null != wrapper.getOLASwitchWrapper()){
//			logger.info("[RetailerToRetailerTransaction.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
//			BaseWrapper baseWrapper = new BaseWrapperImpl();
//			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
//			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
//			
//			SmartMoneyAccountModel senderSMA = wrapper.getSmartMoneyAccountModel();
//			SmartMoneyAccountModel recipientSMA = wrapper.getReceivingSmartMoneyAccountModel();
//			wrapper.setSmartMoneyAccountModel(recipientSMA);
//			wrapper.setRecipientSmartMoneyAccountModel(senderSMA);
//			wrapper.setOlaSmartMoneyAccountModel(senderSMA);
//			
//			abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,true));
//		}
		logger.info("[RetailerToRetailerTransaction.rollback] rollback complete..."); 

		return wrapper;
	}

	/*
	 * This method swaps sender and recipient OLA Accounts for reversal FT
	 */
	private SwitchWrapper swapAccounts(WorkFlowWrapper wrapper,boolean isFirstFT) throws Exception{
		
		SwitchWrapper olaSwitchWrapper = isFirstFT?wrapper.getOLASwitchWrapper():wrapper.getOlaSwitchWrapper_2();
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

public void setRetailerContactManager(
		RetailerContactManager retailerContactManager) {
	this.retailerContactManager = retailerContactManager;
}


  public void setSmsSender(SmsSender smsSender)
 {
   this.smsSender = smsSender;
 }

  public void setUserDeviceAccountsManager(UserDeviceAccountsManager
                                           userDeviceAccountsManager)
  {
    this.userDeviceAccountsManager = userDeviceAccountsManager;
  }


public void setProductManager(ProductManager productManager) {
	this.productManager = productManager;
}

public void setMessageSource(MessageSource messageSource) {
	this.messageSource = messageSource;
}

public MessageSource getMessageSource() {
	return messageSource;
}

public void setCommissionManager(CommissionManager commissionManager)
{
	this.commissionManager = commissionManager;
}

}
