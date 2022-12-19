package com.inov8.microbank.server.service.workflow.credittransfer;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.SwitchConstants;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.creditmodule.RetRetCreditManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureReasonManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.PhoenixFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.switchmodule.iris.model.PhoenixIntegrationMessageVO;

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

public class RsoToRetailerTransaction
    extends CreditTransferTransaction
{

  private RetRetCreditManager retRetCreditManager;
  private FailureReasonManager failureReasonManager;
  private FailureLogManager failureLogManager;
  private RetailerContactManager retailerContactManager;
  private AppUserManager appUserManager ;
  private SmsSender smsSender;
  private UserDeviceAccountsManager userDeviceAccountsManager;
  private FinancialIntegrationManager financialIntegrationManager;
  private SmartMoneyAccountManager smartMoneyAccountManager;
  private ProductDispenseController productDispenseController;
  private ProductManager productManager;
  
  BillPaymentProductDispenser billSaleProductDispenser;
  
  private MessageSource messageSource;
  DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
  DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");
  private String rrnPrefix;
	

  public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
{
	this.financialIntegrationManager = financialIntegrationManager;
}

public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
{
	this.smartMoneyAccountManager = smartMoneyAccountManager;
}

public RsoToRetailerTransaction()
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
  protected WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception
  {
    Date nowDate = new Date();

    BaseWrapper baseWrapper = new BaseWrapperImpl();
    WorkFlowWrapper workFlowWrapper = wrapper;
    TransactionModel transactionModel = workFlowWrapper.getTransactionModel();

    //To Retailer
    RetailerContactModel toRetailerContactModel = workFlowWrapper.getToRetailerContactModel();

    //From Retailer
    RetailerContactModel fromRetailerContactModel = workFlowWrapper.getFromRetailerContactModel();

    
    baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
    
    TransactionDetailModel transactionDetailModel = new TransactionDetailModel();
    wrapper.setTransactionDetailModel(transactionDetailModel);
    wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(transactionDetailModel);
	SwitchWrapper switchWrapper = new SwitchWrapperImpl();
	switchWrapper.setWorkFlowWrapper(wrapper);
	switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());

	ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
	
	//Moaziz RSO apnay Franchise account se Rs {0} agent {1} {2} ko kamyabi se muntakil kar deye hain. Tx ID\: {3} {4} {5}
	String senderAgentSMS=this.getMessageSource().getMessage(
			"USSD.RsoToAgentTransferSenderSMS",
			new Object[] {
					Formatter.formatDouble(wrapper.getTransactionModel().getTotalAmount()),//Amount Rs
					wrapper.getToRetailerContactAppUserModel().getFirstName(),//Agent First Name
					wrapper.getToRetailerContactAppUserModel().getLastName(),//Agent Last Name
					wrapper.getTransactionCodeModel().getCode(),//Trx ID
					dtf.print(new DateTime()),//Date
					tf.print(new LocalTime())//Time
					
					},
					null);
    
    transactionDetailModel.setCustomField4(senderAgentSMS);
	
	wrapper.getTransactionDetailModel().setCustomField4(senderAgentSMS);
	messageList.add(new SmsMessage(wrapper.getFromRetailerContactAppUserModel().getMobileNo(), senderAgentSMS));
    

    //RsoToAgentTransferRecipientSMS=Moaziz Retailer apkay account main Franchisee {0} nay Rs {1} kamyabi se muntakil kar deye hain. Tx ID\: {2} {3} {4}
    String recipientAgentSMS=this.getMessageSource().getMessage(
			"USSD.RsoToAgentTransferRecipientSMS",
			new Object[] {
					wrapper.getHeadRetailerAppuserModel().getFirstName(),
					wrapper.getHeadRetailerAppuserModel().getLastName(),
					Formatter.formatDouble(wrapper.getTransactionModel().getTotalAmount()),//Amount Rs
					wrapper.getTransactionCodeModel().getCode(),
					dtf.print(new DateTime()),
					tf.print(new LocalTime())
					},
					null);
	
	wrapper.getTransactionDetailModel().setCustomField4(senderAgentSMS);
	messageList.add(new SmsMessage(wrapper.getToRetailerContactAppUserModel().getMobileNo(), recipientAgentSMS));
    
	//Moaziz Franchisee apkay RSO ne RS {0} agent {1} {2} ko kamyabi se muntakil kar deye hain. Tx ID\: {3} {4} {5}
	String franchiseeSMS=this.getMessageSource().getMessage(
			"USSD.RsoToAgentTransferFranchiseSMS",
			new Object[] {
					Formatter.formatDouble(wrapper.getTransactionModel().getTotalAmount()),//Amount Rs
					wrapper.getToRetailerContactAppUserModel().getFirstName(),
					wrapper.getToRetailerContactAppUserModel().getLastName(),
					wrapper.getTransactionCodeModel().getCode(),
					dtf.print(new DateTime()),
					tf.print(new LocalTime())
					},
					null);
	
	wrapper.getTransactionDetailModel().setCustomField4(franchiseeSMS);
	messageList.add(new SmsMessage(wrapper.getHeadRetailerAppuserModel().getMobileNo(), franchiseeSMS));
	
	//Get RRN Prefix befor going for FT to use in rollback in case transactino fails.
	rrnPrefix = buildRRNPrefix();
	wrapper.getProductDispenser().doSale(wrapper);
	
    String txAmount = Formatter.formatDouble(wrapper.getTransactionAmount());
    wrapper.getTransactionModel().setConfirmationMessage(recipientAgentSMS);
    wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
    wrapper.getTransactionModel().setRetailerId(fromRetailerContactModel.getRetailerId());
    wrapper.getTransactionModel().setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
    wrapper.getTransactionModel().setFromRetContactId(fromRetailerContactModel.getRetailerContactId());
    wrapper.getTransactionModel().setToRetContactId(toRetailerContactModel.getRetailerContactId());
    wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
    
    
    transactionDetailModel.setProductId(wrapper.getProductModel().getProductId());
    transactionDetailModel.setConsumerNo(wrapper.getToRetailerContactAppUserModel().getMobileNo());
    transactionDetailModel.setActualBillableAmount(wrapper.getTransactionAmount());
    transactionDetailModel.setSettled(true);
    transactionDetailModel.setCustomField1(wrapper.getReceivingSmartMoneyAccountModel().getSmartMoneyAccountId()+"");
    transactionDetailModel.setCustomField3(SwitchConstants.CORE_BANKING_SWITCH+"");
    
    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
	txManager.saveTransaction(wrapper); //save the transaction

	wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
	workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

    return workFlowWrapper;
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

    // Check whether the From Retailer is a Head Retailer or not

//    if (!fromRetailerContactModel.getHead())
//    {
//      throw new WorkFlowException(WorkFlowErrorCodeConstants.FROMRETAILER_CONTACT_NOT_HEAD);
//    }

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

    Long distributorId = toRetailerContactModel.getRelationRetailerIdRetailerModel().getDistributorId();
    if( distributorId == null || !distributorId.equals(fromRetailerContactModel.getRelationRetailerIdRetailerModel().getDistributorId()))
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAIL_RETAIL_DIFF_DISTRIBUTOR);
    }

    Long retailerId = toRetailerContactModel.getRetailerId(); 
    if( retailerId == null || !retailerId.equals(fromRetailerContactModel.getRetailerId()))
    {
      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAIL_RETAIL_DIFF_RETAILER);
    }

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
   		throw new WorkFlowException("Recipient agent account not found.");
   	}
    

    baseWrapper.setBasePersistableModel(wrapper.getFromRetailerContactAppUserModel());
    baseWrapper = this.retailerContactManager.loadRetailerContactByAppUser(baseWrapper);
    wrapper.setFromRetailerContactModel((RetailerContactModel)baseWrapper.getBasePersistableModel());
    
//    RetailerContactModel headRetailerAppUserModel = retailerContactManager.findHeadRetailerContactModelByRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());
//    wrapper.setHeadRetailerContactModel(headRetailerAppUserModel);                     TODO come back later to make it work for better performance 

   /* RetailerContactModel headRetailerContactModel = new RetailerContactModel();
    headRetailerContactModel.setRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());
    headRetailerContactModel.setHead(Boolean.TRUE);
    searchBaseWrapper.setBasePersistableModel(headRetailerContactModel);
    searchBaseWrapper = retailerContactManager.searchRetailerContact(searchBaseWrapper);
//    ExampleConfigHolderModel exampleHolder = new ExampleConfigHolderModel();
//    exampleHolder.setMatchMode(MatchMode.EXACT); 
    wrapper.setHeadRetailerContactModel(searchBaseWrapper.getBasePersistableModel());*/
    
    //Load Franchisee Data
    BaseWrapper headRetailerBaseWrapper = retailerContactManager.loadHeadRetailerContactAppUser(wrapper.getFromRetailerContactModel().getRetailerId());
    if(headRetailerBaseWrapper.getObject("appUserModel") != null && headRetailerBaseWrapper.getObject("userDeviceAccountsModel") != null ){
//    	 ReteilerContactModelbaseWrapper.getObject("headRetailerContactModel");
    	AppUserModel headRetailerAppUserModel = (AppUserModel)headRetailerBaseWrapper.getObject("appUserModel");
    	wrapper.setHeadRetailerAppuserModel(headRetailerAppUserModel);
    }else{
    	throw new WorkFlowException("Franchise Account is not valid.\n");
    }
    
    /*SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
    smartMoneyAccountModel.setRetailerContactId(headRetailerAppUserModel.getRetailerContactId());
    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
    baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
    smartMoneyAccountModel = (SmartMoneyAccountModel)baseWrapper.getBasePersistableModel();
    wrapper.setHeadRetailerSmaModel(smartMoneyAccountModel);
   	wrapper.setHeadRetailerAppuserModel(headRetailerAppUserModel);*/
    
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
    wrapper.getTransactionModel().setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
    wrapper.getTransactionModel().setProcessingSwitchId(SwitchConstants.CORE_BANKING_SWITCH);
    wrapper.getTransactionModel().setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
    wrapper.getTransactionModel().setPaymentModeId(PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT);
    wrapper.getTransactionModel().setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());
    wrapper.getTransactionModel().setRetailerId( fromRetailerContactModel.getRetailerId() );

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
		
		String code = wrapper.getTransactionCodeModel().getCode();
		logger.error("[RsoToRetailerTransaction.rollback] Rolling back transaction with  ID: " + code);
		if(null != wrapper.getFirstFTIntegrationVO())
		{
			
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(PhoenixFinancialInstitutionImpl.class.getName());

			String rrn = wrapper.getFirstFTIntegrationVO().getRetrievalReferenceNumber();
			rrn = rrnPrefix + rrn;
			rrnPrefix = null;
			logger.error("[RsoToRetailerTransaction.rollback] FT was done so trying to roll back with RRN  : "+ rrn +" and trx code : "+code);
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
			
			logger.error("[RsoToRetailerTransaction.rollback] Hitting Phoenix to rollback with RRN : "+rrn+" and trx code : "+code);
			switchWrapper = abstractFinancialInstitution.reverseFundTransfer(switchWrapper);
			
		}
		
		return wrapper;
	}

  

  public void setRetRetCreditManager(RetRetCreditManager retRetCreditManager)
  {
    this.retRetCreditManager = retRetCreditManager;
  }

//  public void setTransactionManager(TransactionManager transactionManager)
//  {
//    this.transactionManager = transactionManager;
//  }

  public void setFailureReasonManager(FailureReasonManager failureReasonManager)
  {
    this.failureReasonManager = failureReasonManager;
  }

  public void setFailureLogManager(FailureLogManager
                                   failureLogManager)
  {
    this.failureLogManager = failureLogManager;
  }

public void setRetailerContactManager(
		RetailerContactManager retailerContactManager) {
	this.retailerContactManager = retailerContactManager;
}

  public void setAppUserManager(AppUserManager appUserManager)
  {
    this.appUserManager = appUserManager;
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

public void setProductDispenseController(
		ProductDispenseController productDispenseController) {
	this.productDispenseController = productDispenseController;
}

public void setProductManager(ProductManager productManager) {
	this.productManager = productManager;
}

public void setBillSaleProductDispenser(
		BillPaymentProductDispenser billSaleProductDispenser) {
	this.billSaleProductDispenser = billSaleProductDispenser;
}

public void setMessageSource(MessageSource messageSource) {
	this.messageSource = messageSource;
}

public MessageSource getMessageSource() {
	return messageSource;
}


}
