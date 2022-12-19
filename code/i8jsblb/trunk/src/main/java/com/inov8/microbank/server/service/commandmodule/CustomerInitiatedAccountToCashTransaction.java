package com.inov8.microbank.server.service.commandmodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.MiniTransactionModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.WalkinCustomerModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.IvrAuthenticationRequestQueue;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.creditmodule.DistDistCreditManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.integration.vo.AccountToCashVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.workflow.credittransfer.CreditTransferTransaction;

public class CustomerInitiatedAccountToCashTransaction extends CreditTransferTransaction {


	//  private DistDistCreditManager distDistCreditManager;
	 // private DistributorContactManager distributorContactManager;
	  private UserDeviceAccountsManager userDeviceAccountsManager;
	  private FinancialIntegrationManager financialIntegrationManager;
	  private SmartMoneyAccountManager smartMoneyAccountManager;
	  private NotificationMessageManager notificationMessageManager;
	  private ProductManager productManager;
	  private CustTransManager customerManager;
	  private CommissionManager commissionManager;
	  private ProductDispenseController productDispenseController;

	  //added by mudassir
	  private MfsAccountManager mfsAccountManager;
	  private WalkinCustomerModel recepientWalkinCustomerModel;
	  private SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel;
	  private BillPaymentProductDispenser accountToCashDispenser;
	  private MessageSource messageSource;
//	  private RetailerContactManager retailerContactManager;	

	  public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public CustomerInitiatedAccountToCashTransaction()
	  {
	  }

		private WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception{
			if (logger.isDebugEnabled())
			{
				logger.debug("Inside loadProductDispenser(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToCashTransaction...");
				logger.debug("Loading ProductDispenser...");
			}

			accountToCashDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Fetching Bill Info through Product Dispenser...");
			}
			
			wrapper = accountToCashDispenser.getBillInfo(wrapper);

			return wrapper;
		}
		
	  /**
	   * Credit transfer transaction takes place over here
	   * @param wrapper WorkFlowWrapper
	   * @return WorkFlowWrapper
	   */
	  public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws Exception
	  {
		    wrapper = this.getBillInfo(wrapper); // 11-Dec-2014: By Omar Butt: Enabled to check walk-in customer credit limits
			
		    TransactionDetailModel txDetailModel = new TransactionDetailModel();

		  
		  	CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
			CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
					CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			// commenting due to IVR change this.validateCommission(commissionWrapper, wrapper); // validate

			wrapper.setCommissionAmountsHolder(commissionAmounts);
			
			
			
			
			AbstractFinancialInstitution olaVeriflyFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());

			//olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
			AppUserModel appUserModel = wrapper.getAppUserModel();

			wrapper.setAppUserModel(wrapper.getCustomerAppUserModel());		
		//	olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
			
			wrapper.setAppUserModel(appUserModel);			
			
			txDetailModel.setCustomField9(wrapper.getRecipientWalkinCustomerModel().getCnic());
		    txDetailModel.setCustomField5(wrapper.getRecipientWalkinCustomerModel().getMobileNumber());
			wrapper.getTransactionModel().setSaleMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(wrapper.getProductModel());
			txDetailModel.setConsumerNo(((AccountToCashVO) wrapper.getProductVO()).getConsumerNo());
			wrapper.getTransactionModel().setConfirmationMessage(" _ ");
			
			wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
			wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);

			this.createMiniTransactionModel(wrapper);//Save Mini Transaction

			wrapper.getTransactionModel().setTotalCommissionAmount(Math.floor(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount()));
			wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(wrapper.getProductModel());
			wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
			
			
		    BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());
			
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
		    wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
		    txDetailModel.setSettled(false);
		    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.IN_PROGRESS);
			
			wrapper.setTransactionDetailModel(txDetailModel);

			txManager.saveTransaction(wrapper); //save the transaction
			
			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(wrapper);
			switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// customer SMA
			
			logger.info("[CustomerInitiatedAccountToCashTransaction.doCreditTransfer()] Going to transfer funds from Customer Account to Acc-to-Cash Sundry Account. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
			switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);
			
			wrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
		    wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
			wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());

			double customerBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction(); // customer balance

			// this amount is to be disbursed to customer at the time of account opening
			wrapper.getTransactionDetailModel().setActualBillableAmount(commissionAmounts.getTransactionAmount() - switchWrapper.getInclusiveChargesApplied());
			
			wrapper.getTransactionDetailModel().setCustomField11(switchWrapper.getOlavo().getAuthCode());
			ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
			String brandName = MessageUtil.getMessage("jsbl.brandName");

			//FT of Rs. {0},\n to CNIC ({1})\nCharges\: Rs. {2}nA/C Balance is\: {3}\n on {4} {5}\n Tx ID\: {6}
			String customerSmsText = this.getMessageSource().getMessage(
					"USSD.CustomerAcctToCashSMS",
					new Object[] { 
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
							wrapper.getRecipientWalkinCustomerModel().getCnic(),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
							wrapper.getOneTimePin(),customerBalance
							}, null);
			
			messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSmsText));
			
			String recipientSmsText = this.getMessageSource().getMessage(
					"USSD.WalkinCustomerA2CLeg1SMS",
					new Object[] {
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getTransactionDetailModel().getActualBillableAmount()),
							wrapper.getCustomerAppUserModel().getMobileNo(),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							brandName,
							},
							null);
			
			messageList.add(new SmsMessage(wrapper.getRecipientWalkinCustomerModel().getMobileNumber(), recipientSmsText));
			wrapper.getTransactionModel().setConfirmationMessage(recipientSmsText);
			wrapper.getTransactionDetailModel().setCustomField10(recipientSmsText);
			wrapper.getTransactionDetailModel().setCustomField8(customerSmsText.substring(0, customerSmsText.length()-5)+" *****"); //masked as code was being saved in transaction_detail's custom_field8

			switchWrapper = new SwitchWrapperImpl();
			switchWrapper.setWorkFlowWrapper(wrapper) ;
			switchWrapper = abstractFinancialInstitution.saveWalkinCustomerLedgerEntry(switchWrapper);
			wrapper.setOlaSwitchWrapper_3(switchWrapper);

			txManager.saveTransaction(wrapper); //save the transaction
			wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
			
			logger.info("[CustomerInitiatedAccountToCashTransaction.doCreditTransfer()] Going to Settle Commissions. Transaction ID: " + wrapper.getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
			this.settlementManager.settleCommission(commissionWrapper, wrapper);

		    return wrapper;
	    
	  }

	  private void createMiniTransactionModel(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

			 try {
				
				TransactionCodeModel transactionCodeModel = workFlowWrapper.getTransactionCodeModel();
				MiniTransactionModel miniTransactionModel = workFlowWrapper.getMiniTransactionModel();
				miniTransactionModel.setTransactionCodeIdTransactionCodeModel(transactionCodeModel);
				miniTransactionModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
				miniTransactionModel.setTimeDate(new Date()) ;
		
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.setBasePersistableModel( miniTransactionModel ) ;
				baseWrapper = txManager.saveMiniTransaction(baseWrapper) ;

			 } catch (Exception e) {
				 e.printStackTrace();
				 throw new WorkFlowException(e.getMessage());
			 }
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

	    txManager.saveTransaction(wrapper);

	    return wrapper;
	  }

/*	  public void setDistDistCreditManager(DistDistCreditManager
	                                       distDistCreditManager)
	  {
	    this.distDistCreditManager = distDistCreditManager;
	  }*/


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
		  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		 // if (wrapper.getIsIvrResponse() == false) {
				//generate new trasnaction code and initialize Transaction/TransctionDetailMaster objects.
				wrapper = super.doPreStart(wrapper);
/*			}else{
				//load existing transaction objects
				TransactionCodeModel txCodeModel = wrapper.getTransactionCodeModel();
				baseWrapper.setBasePersistableModel(txCodeModel);
				baseWrapper = txManager.loadTransactionCodeByCode(baseWrapper);
				wrapper.setTransactionCodeModel((TransactionCodeModel)baseWrapper.getBasePersistableModel());
				txManager.generateTransactionObject(wrapper);
				searchBaseWrapper.setBasePersistableModel(wrapper.getTransactionCodeModel());
				txManager.loadTransactionByTransactionCode(searchBaseWrapper);
				
				wrapper.setTransactionModel((TransactionModel)searchBaseWrapper.getBasePersistableModel());
				wrapper.setTotalAmount(wrapper.getTransactionModel().getTotalAmount());
				wrapper.setTotalCommissionAmount(wrapper.getTransactionModel().getTotalCommissionAmount());
				wrapper.setBillAmount(wrapper.getTransactionModel().getTransactionAmount());
				wrapper.setTransactionAmount(wrapper.getTransactionModel().getTransactionAmount());
				wrapper.setTxProcessingAmount(wrapper.getTransactionModel().getTotalCommissionAmount());
				
				List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(wrapper.getTransactionModel().getTransactionIdTransactionDetailModelList());
				if(transactionDetailModelList != null && transactionDetailModelList.size() > 0) {
					wrapper.setTransactionDetailModel(transactionDetailModelList.get(0));
				}
			}
*/
		  // Populate the Product model from DB
		  baseWrapper.setBasePersistableModel(wrapper.getProductModel());
		  baseWrapper = productManager.loadProduct(baseWrapper);
		  wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

		  //Load Customer AppUser 
/*		  AppUserModel customerAppUserModel =new AppUserModel();
		  customerAppUserModel.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		  customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		  customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);
		  wrapper.setCustomerAppUserModel(customerAppUserModel);*/

		  //Sender Customer DeviceAccountsModel
		  UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		  userDeviceAccountsModel.setAppUserId(wrapper.getCustomerAppUserModel().getAppUserId());
		  userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		  baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		  baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		  wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

		  // --Setting instruction and success Message
		  NotificationMessageModel notificationMessage = new NotificationMessageModel();
		  notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
		  baseWrapper.setBasePersistableModel(notificationMessage);
		  baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		  wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		  NotificationMessageModel successMessage = new NotificationMessageModel();
		  successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
		  baseWrapper.setBasePersistableModel(successMessage);
		  baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
		  wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

		  // Populate the Customer model from DB
		  wrapper.setCustomerModel(new CustomerModel());
		  wrapper.getCustomerModel().setCustomerId(wrapper.getCustomerAppUserModel().getCustomerId());
		  baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
		  baseWrapper = customerManager.loadCustomer(baseWrapper);
		  wrapper.setCustomerModel((CustomerModel) baseWrapper.getBasePersistableModel());

		  // Populate Customer Smart Money Account from DB
		  SmartMoneyAccountModel customerSma = new SmartMoneyAccountModel();
		  customerSma.setCustomerId(wrapper.getCustomerAppUserModel().getCustomerId());
		  baseWrapper.setBasePersistableModel(customerSma);
		  baseWrapper = smartMoneyAccountManager.searchSmartMoneyAccount(baseWrapper);
		  wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
		  wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

		  wrapper.setPaymentModeModel(new PaymentModeModel());
		  wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

		  wrapper.setPaymentModeModel(new PaymentModeModel());
		  wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

		  // Added by mudassir - Populate the walk in customer Smart Money Account from DB
		  WalkinCustomerModel walkinCustomerModel = mfsAccountManager.getWalkinCustomerModel(wrapper.getRecipientWalkinCustomerModel());
		  wrapper.setRecipientWalkinCustomerModel(walkinCustomerModel);

		  SmartMoneyAccountModel smartMoneyAccountModel = wrapper.getRecipientWalkinSmartMoneyAccountModel();
		  smartMoneyAccountModel.setWalkinCustomerModel(walkinCustomerModel);
		  SmartMoneyAccountModel walkinRecipeientSMA = mfsAccountManager.getSmartMoneyAccount(smartMoneyAccountModel);

		  wrapper.setRecipientWalkinSmartMoneyAccountModel(walkinRecipeientSMA);

		  // Populate Retailer Contact model from DB
/*		  if(wrapper.getFromRetailerContactModel() == null){
			  searchBaseWrapper = new SearchBaseWrapperImpl();
			  RetailerContactModel retailerContact = new RetailerContactModel();
			  retailerContact.setRetailerContactId( ThreadLocalAppUser.getAppUserModel().getRetailerContactId() );
			  searchBaseWrapper.setBasePersistableModel( retailerContact );
			  searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
			  wrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());
		  }*/
		  
		// Set Handler User Device Account Model
/*			if(wrapper.getHandlerAppUserModel() != null && wrapper.getHandlerAppUserModel().getAppUserId() != null){			
				UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
				handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
				handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
				baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
				baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
				wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
			}*/

		  
	    return wrapper;
	  }
	  

	  @Override
	  protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
	  {
		  wrapper = super.doPreProcess(wrapper);
		  
		  /*if (wrapper.getIsIvrResponse() == false) {*/

				TransactionModel txModel = wrapper.getTransactionModel();
		
				txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		
				txModel.setTransactionAmount(wrapper.getBillAmount());
				txModel.setTotalAmount(wrapper.getTransactionAmount());
				//txModel.setRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());
			//	txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
			//	txModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;
				txModel.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
				
				txModel.setTotalCommissionAmount(0d);
				txModel.setDiscountAmount(0d);
		
				// Transaction Type model of transaction is populated
				txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());
				
				txModel.setCustomerIdCustomerModel(new CustomerModel());
				txModel.getCustomerIdCustomerModel().setCustomerId(wrapper.getCustomerModel().getCustomerId());

				// Sets the Device type
				txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
				txModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		
				// Smart Money Account Id is populated
				txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
		
				// Payment mode model of transaction is populated
				txModel.setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
				txModel.setNotificationMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
				txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
		
				// Populate processing Bank Id
				txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);

				wrapper.setTransactionModel(txModel);
		//  }
			
			if (logger.isDebugEnabled())
			{
				logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToCashTransaction....");
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
//	    transactionModel.setDistributorIdDistributorModel(wrapper.getFromDistributorContactModel().getDistributorIdDistributorModel());
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
				logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToCashTransaction...");
			}
			
			wrapper.setSegmentModel(wrapper.getCustomerModel().getSegmentIdSegmentModel());
			
			CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
			commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
			commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
			commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
			commissionWrapper.setProductModel(wrapper.getProductModel());
		    wrapper.setTaxRegimeModel(wrapper.getCustomerModel().getTaxRegimeIdTaxRegimeModel());
			commissionWrapper = this.commissionManager.calculateCommission(wrapper);
			// --------------------------------------------------------------------------------------
			if (logger.isDebugEnabled())
			{
				logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToCashTransaction...");
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
			if (logger.isDebugEnabled())
			{
				logger.debug("Inside validateCommission of CustomerInitiatedAccountToCashTransaction...");
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

			//FIXME commission rate structre changed. need to recalculate transaction processing amount to compare with xml value.
			/*if (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue()))))
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
			}*/
			if (logger.isDebugEnabled())
			{

				logger.debug("Ending validateCommission of CustomerInitiatedAccountToCashTransaction...");
			}

		}

		public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Inside getTransactionProcessingCharges of CustomerInitiatedAccountToCashTransaction....");
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
				}
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("Ending getTransactionProcessingCharges of CustomerInitiatedAccountToCashTransaction....");
			}
			return transProcessingAmount;
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
			logger.info("[CustomerInitiatedAccountToCashTransaction.rollback] Rolling back A2C transaction with  ID: " + code);
			/*try{
				wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
				wrapper.getTransactionModel().setTransactionId(null);
				wrapper.getTransactionDetailModel().setTransactionDetailId(null);
				txManager.saveTransaction(wrapper);
			}catch(Exception ex){
				logger.error("Unable to save A2C Transaction details in case of rollback: \n"+ ex.getStackTrace());
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
			logger.info("[AccountToCashTransaction.rollback] rollback complete..."); */
			
			return wrapper;
		}
		
		
/*	  public void setDistributorContactManager(DistributorContactManager distributorContactManager)
	  {
	    this.distributorContactManager = distributorContactManager;
	  }*/

/*	public DistributorContactManager getDistributorContactManager() {
		return distributorContactManager;
	}*/

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

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setIvrRequestHandler(IvrRequestHandler ivrRequestHandler) {
		this.ivrRequestHandler = ivrRequestHandler;
	}

	public IvrAuthenticationRequestQueue getIvrAuthenticationRequestQueueSender() {
		return ivrAuthenticationRequestQueueSender;
	}

	public void setIvrAuthenticationRequestQueueSender(
			IvrAuthenticationRequestQueue ivrAuthenticationRequestQueueSender) {
		this.ivrAuthenticationRequestQueueSender = ivrAuthenticationRequestQueueSender;
	}
/*	public void setRetailerContactManager(RetailerContactManager retailerContactManager)
	{
		this.retailerContactManager = retailerContactManager;
	}*/


}
