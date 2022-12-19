package com.inov8.microbank.server.service.workflow.credittransfer;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
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
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
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
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AgentRetailPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;

public class AgentRetailPaymentTransaction
extends CreditTransferTransaction
{
	private DistributorContactManager distributorContactManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private FinancialIntegrationManager financialIntegrationManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private NotificationMessageManager notificationMessageManager;
	private ProductManager productManager;
	private CommissionManager commissionManager;

	private WalkinCustomerModel recepientWalkinCustomerModel;
	private SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel;
	private MessageSource messageSource;
	
	private CustTransManager customerManager;
	private ProductDispenseController productDispenseController;
	private BillPaymentProductDispenser retailPaymentDispenser;
	private RetailerContactManager retailerContactManager;

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public AgentRetailPaymentTransaction()
	{
	}

	/**
	 * Credit transfer transaction takes place over here
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
	Exception
	{

		TransactionDetailModel txDetailModel = new TransactionDetailModel();


		CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		
		// commenting due to IVR change this.validateCommission(commissionWrapper, wrapper); // validate

		//if agent 2 is head agent, then franchise 2 commission is merged back to agent 2 and franchise commission entry is not parked in commission_transaction
		if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
			commissionAmounts.setAgent2CommissionAmount(commissionAmounts.getAgent2CommissionAmount() + commissionAmounts.getFranchise2CommissionAmount());
			commissionAmounts.setFranchise2CommissionAmount(0.0d);
		}
		
		wrapper.setCommissionAmountsHolder(commissionAmounts);

		
		AbstractFinancialInstitution olaVeriflyFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());

		olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
		AppUserModel appUserModel = wrapper.getAppUserModel();

		wrapper.setAppUserModel(wrapper.getCustomerAppUserModel());		
		olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
		
		wrapper.setAppUserModel(appUserModel);			
		
		
		if (wrapper.getIsIvrResponse() == false) {
			
			wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
			wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
			wrapper.getTransactionModel().setCreatedOn(new Date());
			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(wrapper.getProductModel());
			txDetailModel.setConsumerNo(((AgentRetailPaymentVO) wrapper.getProductVO()).getCustomerMobileNo());
			wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
			wrapper.getTransactionModel().setFromRetContactId(wrapper.getRetailerContactModel().getRetailerContactId());
			wrapper.getTransactionModel().setFromRetContactName(wrapper.getRetailerContactModel().getName());
			wrapper.getTransactionModel().setFromRetContactMobNo(wrapper.getRetailerContactModel().getMobileNo());
			
			//TODO verify which fields need to be set here
//			wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));
			//txDetailModel.setCustomField11(""+((AccountToAccountVO)wrapper.getProductVO()).getAccountNumber());
			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
			
			txDetailModel.setSettled(false);
			wrapper.setTransactionDetailModel(txDetailModel);
			wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
			wrapper.getTransactionModel().setConfirmationMessage(" _ ");
			
			// Set Handler Detail in Transaction and Transaction Detail Master
			if(wrapper.getHandlerModel() != null){
					wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
					wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
					wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
					wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
			}
			
			return super.makeIvrRequest(wrapper);
		}

		txDetailModel = wrapper.getTransactionDetailModel();

		wrapper.getTransactionModel().setTotalCommissionAmount(Math.floor(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount()));
		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());


		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		wrapper.getTransactionModel().setConfirmationMessage(" _ ");
		wrapper.getTransactionModel().setToRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
		wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
		wrapper.getTransactionModel().setNotificationMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		wrapper.getTransactionModel().setSaleMobileNo(wrapper.getTransactionModel().getCustomerMobileNo());
		wrapper.getTransactionModel().setCustomerId(wrapper.getCustomerAppUserModel().getCustomerId());
		wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.COMPLETED);
		wrapper.getTransactionModel().setToRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName());
		wrapper.getTransactionModel().setToRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		wrapper.setTransactionDetailModel(txDetailModel);
//		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
		txDetailModel.setSettled(true);

		txManager.saveTransaction(wrapper); //save the transaction

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		
		logger.info("[AgentRetailPaymentTransaction.doCreditTransfer()] Going to transfer funds. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);
		
		wrapper.setSwitchWrapper(switchWrapper); 
	    wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
	    
		
	    // Customer Account
		wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
		

		//Customer balance
	    ((AgentRetailPaymentVO)(wrapper.getProductVO())).setBalance(switchWrapper.getOlavo().getFromBalanceAfterTransaction());
		wrapper.getTransactionDetailModel().setCustomField11(switchWrapper.getOlavo().getAuthCode());
		
		String agentAccNumber = switchWrapper.getToAccountNo();
		Double customerBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction();
		Double agentBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction();
		
		if(agentBalance != null) {
			switchWrapper.setAgentBalance(agentBalance);
		}
		String brandName = null;

		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			brandName = MessageUtil.getMessage("sco.brandName");
		}
		else {
			brandName = MessageUtil.getMessage("jsbl.brandName");
		}
		
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
	        
//	        if(wrapper.getCommissionAmountsHolder().getIsInclusiveCharges()){
//	          {0}\nTrx ID {1}\nYou have successfully transferred Rs.{2}\n to Merchant {3}\nAvl Bal is Rs.{4}
				String customerSmsText = this.getMessageSource().getMessage(
	                    "ARP.CustomerSMS",
	                    new Object[] { 
	                    		brandName,
	                            wrapper.getTransactionCodeModel().getCode(),
	                            Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount()),
	                            ThreadLocalAppUser.getAppUserModel().getMobileNo(),
	                            customerBalance,
	                            }, null);
	            
				messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSmsText));

	    		if(wrapper.getHandlerModel() == null  ||
	    				(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToAgent())){

		    		//{0}\nTrx ID {1}\nYou have successfully received Rs.{2}\n from {3}\nAvl Bal is Rs.{4}
		    		String   agentSmsText = this.getMessageSource().getMessage(
		                    "ARP.AgentSMS",
		                    new Object[] {
		                    		brandName,
		                            wrapper.getTransactionCodeModel().getCode(),
		                            Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount()),
		                            wrapper.getCustomerAppUserModel().getMobileNo(),
		                            agentBalance, 
		                            },
		                            null);
		    		
		    		txDetailModel.setCustomField4(agentSmsText);
		            
		    		messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSmsText));
	    		}
	    		
	    		if(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToHandler()){
		    		String   handlerSMS = this.getMessageSource().getMessage(
		                    "ARP.AgentSMS",
		                    new Object[] {
		                    		brandName,
		                            wrapper.getTransactionCodeModel().getCode(),
		                            Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount()),
		                            ThreadLocalAppUser.getAppUserModel().getMobileNo(),
		                            agentBalance, 
		                            },
		                            null);
		    		
//		    		txDetailModel.setCustomField4(agentSmsText);
		            
		    		messageList.add(new SmsMessage(wrapper.getHandlerAppUserModel().getMobileNo(), handlerSMS));
	    		}
	    		
	    		

//	        }else{
//	          Successful Retail Payment of Rs. {0} to {1}\nCharges Rs. {2}\nNow your A/c Bal is Rs. {3}\n18/06/13 07:15 PM Transaction ID 162191649167 
//	            customerSmsText = this.getMessageSource().getMessage(
//	                    "USSD.CRPCustomerSMS",
//	                    new Object[] { 
//	                            Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount()),
//	                            ThreadLocalAppUser.getAppUserModel().getMobileNo(),
//	                            wrapper.getTransactionModel().getTotalCommissionAmount(),
//	                            customerBalance,
//	                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_TIME_FORMAT),
//	                            wrapper.getTransactionCodeModel().getCode()
//	                            }, null);
//	            
//	            
//	          Received Retail Payment of Rs. {0} from {1} \non {2} \nNow your a/c balance is {3} \nTransaction ID: {4}
//	            recipientSmsText = this.getMessageSource().getMessage(
//	                    "USSD.CRPAgentSMS",
//	                    new Object[] {
//	                            Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount()),
//	                            ThreadLocalAppUser.getAppUserModel().getMobileNo(),
//	                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_TIME_FORMAT),
//	                            agentBalance,
//	                            wrapper.getTransactionCodeModel().getCode()
//	                            },
//	                            null);
//	            
//	        }

		wrapper.getTransactionModel().setConfirmationMessage(customerSmsText);
		txDetailModel.setConsumerNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		txDetailModel.setCustomField1(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId().toString());
		txDetailModel.setCustomField2(agentAccNumber);
		txDetailModel.setCustomField3(wrapper.getSwitchWrapper().getSwitchSwitchModel().getSwitchId() + "");
		txDetailModel.setCustomField5(ThreadLocalAppUser.getAppUserModel().getMobileNo());

		txManager.saveTransaction(wrapper); //save the transaction
		wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

		logger.info("[AgentRetailPaymentTransaction.doCreditTransfer()] Going to Settle Commissions. Transaction ID: " + wrapper.getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		this.settlementManager.settleCommission(commissionWrapper, wrapper);

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

		if (wrapper.getIsIvrResponse() == false) {
			//generate new transaction code and initialize Transaction/TransctionDetailMaster objects.
			wrapper = super.doPreStart(wrapper);
		}else{
			//load existing transaction objects
			TransactionCodeModel txCodeModel = wrapper.getTransactionCodeModel();
			baseWrapper.setBasePersistableModel(txCodeModel);
			baseWrapper = txManager.loadTransactionCodeByCode(baseWrapper);
			wrapper.setTransactionCodeModel((TransactionCodeModel)baseWrapper.getBasePersistableModel());
			txManager.generateTransactionObject(wrapper);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(wrapper.getTransactionCodeModel());
			txManager.loadTransactionByTransactionCode(searchBaseWrapper);
			
			wrapper.setTransactionModel((TransactionModel)searchBaseWrapper.getBasePersistableModel());
			wrapper.setTotalAmount(wrapper.getTransactionModel().getTotalAmount());
			wrapper.setTotalCommissionAmount(wrapper.getTransactionModel().getTotalCommissionAmount());
			wrapper.setBillAmount(wrapper.getTransactionModel().getTransactionAmount());
			wrapper.setTxProcessingAmount(wrapper.getTransactionModel().getTotalCommissionAmount());
			
			List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(wrapper.getTransactionModel().getTransactionIdTransactionDetailModelList());
			if(transactionDetailModelList != null && transactionDetailModelList.size() > 0) {
				wrapper.setTransactionDetailModel(transactionDetailModelList.get(0));
			}
		}

		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(wrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

		// Populate the Customer AppUserModel and CustomerModel
		AppUserModel customerAppUserModel =new AppUserModel();
		customerAppUserModel.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);
		if(null != customerAppUserModel)
		{
			wrapper.setCustomerAppUserModel(customerAppUserModel);
			wrapper.setCustomerModel(customerAppUserModel.getCustomerIdCustomerModel());
		}

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getCustomerAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());


		// Populate the Customer OLA Smart Money Account from DB
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		sma.setCustomerId(wrapper.getCustomerModel().getCustomerId()) ;
		CustomList<SmartMoneyAccountModel> smaList = smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(sma);
		if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) 
		{
			sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
			wrapper.setOlaSmartMoneyAccountModel(sma);
		}

		
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

     
        // Populate Agent OLA Smart Money Account from DB
 		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
 		baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
 		wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

        
		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());


		// Set Handler User Device Account Model
		if(wrapper.getHandlerAppUserModel() != null && wrapper.getHandlerAppUserModel().getAppUserId() != null){			
			UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
			handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
			handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		}
			
		return wrapper;
	}


	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
	{
		wrapper = super.doPreProcess(wrapper);

		if (wrapper.getIsIvrResponse()){
			return wrapper;
		}
		
		TransactionModel txModel = wrapper.getTransactionModel();

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

		// Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		txModel.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());

		// Populate processing Bank Id
		txModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());
		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
		txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
		txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());

		wrapper.setTransactionModel(txModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of AgentRetailPaymentTransaction....");
		}

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
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
	    wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());

		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
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
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		AgentRetailPaymentVO productVO = (AgentRetailPaymentVO) workFlowWrapper.getProductVO();

		if (!Double.valueOf(Formatter.formatDouble(productVO.getTransactionAmount().doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTransactionAmount().doubleValue()))))
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

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
					transProcessingAmount += (workFlowWrapper.getTransactionAmount() * commissionRateModel.getRate()) / 100;

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

	@Override
	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper)
			throws Exception {
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper)
			throws Exception {
		return wrapper;
	}

	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
		logger.info("[AgentRetailPaymentTransaction.rollback] rollback complete..."); 

		return wrapper;
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

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setRetailerContactManager(
			RetailerContactManager retailerContactManager) {
		this.retailerContactManager = retailerContactManager;
	}

}
