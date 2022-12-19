package com.inov8.microbank.server.service.workflow.credittransfer;


import com.inov8.framework.common.exception.FrameworkCheckedException;
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
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AgentRetailPaymentVO;
import com.inov8.microbank.server.service.integration.vo.RetailPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.workflow.sales.SalesTransaction;
import com.inov8.ola.integration.vo.OLAVO;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class CustomerRetailPaymentTransaction
extends SalesTransaction
{
	private DistributorContactManager distributorContactManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private FinancialIntegrationManager financialIntegrationManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private NotificationMessageManager notificationMessageManager;
	private ProductManager productManager;
	private CustTransManager customerManager;
	private RetailerManager retailerManager;
	private CommissionManager commissionManager;
	private ProductDispenseController productDispenseController;

	private WalkinCustomerModel recepientWalkinCustomerModel;
	private SmartMoneyAccountModel recepientWalkinSmartMoneyAccountModel;
	private BillPaymentProductDispenser retailPaymentDispenser;
	private MessageSource messageSource;
	private RetailerContactManager retailerContactManager;
	

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public CustomerRetailPaymentTransaction()
	{
	}

	private WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside loadProductDispenser(WorkFlowWrapper wrapper) of CustomerRetailPaymentTransaction...");
			logger.debug("Loading ProductDispenser...");
		}

		retailPaymentDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);

		if (logger.isDebugEnabled())
		{
			logger.debug("Fetching Bill Info through Product Dispenser...");
		}

		wrapper = retailPaymentDispenser.getBillInfo(wrapper);

		return wrapper;
	}

	/**
	 * Credit transfer transaction takes place over here
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

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
		

		if( ThreadLocalAppUser.getAppUserModel().getMobileNo().equals(wrapper.getRetailerAppUserModel().getMobileNo() ))
		{
			throw new FrameworkCheckedException( "Own account transfer is not allowed." ) ;
		}
		
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {


		TransactionDetailModel txDetailModel = new TransactionDetailModel();


		CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		wrapper.setCommissionAmountsHolder(commissionAmounts);

		SmartMoneyAccountModel agentSMA=new SmartMoneyAccountModel();
		agentSMA=this.smartMoneyAccountManager.getSMAccountByRetailer(wrapper.getRetailerAppUserModel().getRetailerContactIdRetailerContactModel());
		if(agentSMA!=null) {
			wrapper.setReceivingSmartMoneyAccountModel(agentSMA);
		}

		wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
		wrapper.getTransactionModel().setCreatedOn(new Date());
		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		//txDetailModel.setConsumerNo(((AgentRetailPaymentVO) wrapper.getProductVO()).getConsumerNo());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		wrapper.getTransactionModel().setFromRetContactId(wrapper.getRetailerContactModel().getRetailerContactId());
		wrapper.getTransactionModel().setFromRetContactName(wrapper.getRetailerContactModel().getName());



		txDetailModel.setSettled(false);
		wrapper.setTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().setConfirmationMessage(" _ ");

		//return super.makeIvrRequest(wrapper);


		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		wrapper.getTransactionModel().setConfirmationMessage(" _ ");
		wrapper.getTransactionModel().setToRetContactId(wrapper.getReceiverAppUserModel().getRetailerContactId());
		wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
		wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
		wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		wrapper.getTransactionModel().setSaleMobileNo(wrapper.getTransactionModel().getCustomerMobileNo());
		wrapper.getTransactionModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());
		wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.COMPLETED);
		wrapper.getTransactionModel().setToRetContactName(wrapper.getReceiverAppUserModel().getFirstName() + " " + wrapper.getReceiverAppUserModel().getLastName());
		wrapper.getTransactionModel().setToRetContactMobNo(wrapper.getReceiverAppUserModel().getMobileNo());
		wrapper.setTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
		txDetailModel.setSettled(true);

		txManager.saveTransaction(wrapper); //save the transaction

		logger.info("[CustomerRetailPaymentTransaction.doCreditTransfer()] Going to Settle Commissions. Transaction ID: " + wrapper.getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		this.settlementManager.settleCommission(commissionWrapper, wrapper);

//************** BB Agents Changes - start
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);

		wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
		//txDetailModel.setCustomField11(""+((AgentRetailPaymentVO)wrapper.getProductVO()).getAccountNumber());
		wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getReceiverAppUserModel().getMobileNo());
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
		Double inclusiveCharges=CommonUtils.getDoubleOrDefaultValue(switchWrapper.getInclusiveChargesApplied());


		String brandName = null;
		if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
			brandName = MessageUtil.getMessage("sco.brandName");
		} else {
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
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
						Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
						wrapper.getReceiverAppUserModel().getMobileNo(),
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
							((wrapper.getTransactionModel().getTransactionAmount())-inclusiveCharges),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							wrapper.getCustomerAppUserModel().getMobileNo(),
							agentBalance,
					},
					null);

			txDetailModel.setCustomField4(agentSmsText);

			messageList.add(new SmsMessage(wrapper.getReceiverAppUserModel().getMobileNo(), agentSmsText));
		}

		wrapper.getTransactionModel().setConfirmationMessage(customerSmsText);
		txDetailModel.setCustomField1(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId().toString());
		txDetailModel.setCustomField2(switchWrapper.getFromAccountNo());
		txDetailModel.setCustomField3(wrapper.getOLASwitchWrapper().getSwitchSwitchModel().getSwitchId() + "");

		txManager.saveTransaction(wrapper); //save the transaction
		wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

		logger.info("[AgentRetailPaymentTransaction.doCreditTransfer()] Going to Settle Commissions. Transaction ID: " + wrapper.getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		this.settlementManager.settleCommission(commissionWrapper, wrapper);

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

			//generate new trasnaction code and initialize Transaction/TransctionDetailMaster objects.
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
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

		wrapper.setCustomerModel(new CustomerModel());
		wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());


		// Populate the Customer model from DB
		baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
		baseWrapper=customerManager.loadCustomer(baseWrapper);
		wrapper.setCustomerModel((CustomerModel) baseWrapper.getBasePersistableModel());

		// Populate customer Smart Money Account from DB
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
		wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

		wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());

		//Populate Recipient Agent AppUserModel..
		AgentRetailPaymentVO p2PVO = (AgentRetailPaymentVO) wrapper.getProductVO();
		AppUserModel agentAppUser = new AppUserModel();
		agentAppUser.setMobileNo(wrapper.getRetailerAppUserModel().getMobileNo());
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setBasePersistableModel(agentAppUser);
        appUserManager.searchAppUserByMobile(searchBaseWrapper);
		
        if (null != searchBaseWrapper.getCustomList() && null !=searchBaseWrapper.getCustomList().getResultsetList()
				&& searchBaseWrapper.getCustomList().getResultsetList().size() > 0) {
        	agentAppUser = (AppUserModel) searchBaseWrapper.getCustomList().getResultsetList().get(0);
        	wrapper.setReceiverAppUserModel(agentAppUser);
			wrapper.setRetailerAppUserModel(agentAppUser);
		}
        
     		
        if(agentAppUser.getRetailerContactId() != null){
            // Populate Agent OLA Smart Money Account from DB
     		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
     		sma.setRetailerContactId(agentAppUser.getRetailerContactId()) ;
     		baseWrapper.setBasePersistableModel(sma);
     		baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
     		wrapper.setRecipientSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

        	//populate retailer model from DB
         	RetailerContactModel retailerContactModel = new RetailerContactModel();
			retailerContactModel.setRetailerContactId(agentAppUser.getRetailerContactId());
			baseWrapper.setBasePersistableModel(retailerContactModel);
			baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
			retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
			wrapper.setRetailerContactModel(retailerContactModel);
        }
        
		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		return wrapper;
	}


	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)throws Exception
	{
		wrapper = super.doPreProcess(wrapper);

		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		txModel.setTransactionAmount(wrapper.getBillAmount());
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
		txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());

		// Populate processing Bank Id
		txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
		txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
		txModel.setFromRetContactMobNo(wrapper.getRetailerContactModel().getMobileNo());
		txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());

		wrapper.setTransactionModel(txModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CustomerRetailPaymentTransaction....");
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
				wrapper.setCustomerModel(custModel);
				wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
			}
		}



		//--------------------------------------------------------------



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
		try{
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			wrapper.getTransactionModel().setTransactionId(null);
			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
			txManager.saveTransaction(wrapper);
		}catch(Exception ex){
			logger.error("[CustomerRetailPaymentTransaction.rollback] Unable to save Transaction details in case of rollback: \n"+ ex.getStackTrace());
		}
		
		if(null != wrapper.getOLASwitchWrapper()){
			logger.info("[CustomerRetailPaymentTransaction.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
			abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,1));
			if(null != wrapper.getOlaSwitchWrapper_2()){
				abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,2));
			}
			if(null != wrapper.getOlaSwitchWrapper_3()){
				abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,3));
			}
			if(null != wrapper.getOlaSwitchWrapper_4()){
				abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,4));
			}
		}
		logger.info("[CustomerRetailPaymentTransaction.rollback] rollback complete..."); 

		return wrapper;
	}

	/*
	 * This method swaps sender and recipient OLA Accounts for reversal FT
	 */
	private SwitchWrapper swapAccounts(WorkFlowWrapper wrapper,int ftOrder) throws Exception{
		
		SwitchWrapper olaSwitchWrapper = wrapper.getOLASwitchWrapper();
		
		if(ftOrder == 2){
			olaSwitchWrapper = wrapper.getOlaSwitchWrapper_2();
		}else if(ftOrder == 3){
			olaSwitchWrapper = wrapper.getOlaSwitchWrapper_3();
		}else if(ftOrder == 4){
			olaSwitchWrapper = wrapper.getOlaSwitchWrapper_4();
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
