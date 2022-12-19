package com.inov8.microbank.server.service.workflow.sales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
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
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.CreditCardPaymentVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.ola.integration.vo.OLAVO;

public class AgentCreditCardPaymentTransaction extends SalesTransaction {
	
	protected final Log log = LogFactory.getLog(getClass());
	private MessageSource messageSource;
	DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
	DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");


	CommissionManager commissionManager;
	SettlementManager settlementManager;
	private ProductDispenseController productDispenseController;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private SmsSender smsSender;
	private ProductManager productManager;
	BillPaymentProductDispenser billSaleProductDispenser;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private RetailerContactManager retailerContactManager;	
	private FinancialIntegrationManager financialIntegrationManager;


	public void setNotificationMessageManager(
			NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}

	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public AgentCreditCardPaymentTransaction() {
	}

	/**
	 * Pulls the bill information from the supplier system
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception
	{
//		if (logger.isDebugEnabled())
//		{
//			logger.debug("Inside getBillInfo(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction...");
//			logger.debug("Loading ProductDispenser...");
//		}
		billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
//		logger.debug("Fetching Bill Info through Product Dispenser...");
		wrapper = billSaleProductDispenser.getBillInfo(wrapper);

		return wrapper;
	}

	/**
	 * Validate input from the user against the information pulled from the
	 * supplier
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper)
			throws FrameworkCheckedException {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction...");
		}
		wrapper =  billSaleProductDispenser.verify(wrapper);
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction...");
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
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction...");
		}
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
		else
		{
			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
			wrapper.setSegmentModel(segmentModel);
		}
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		if(null != wrapper.getCustomerModel()){
			SegmentModel segmentModel=new SegmentModel();
			segmentModel=wrapper.getCustomerModel().getSegmentIdSegmentModel();
			wrapper.setSegmentModel(segmentModel);
		}
		
		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction...");
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
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		CreditCardPaymentVO productVO = (CreditCardPaymentVO) workFlowWrapper.getProductVO();

		if (productVO.getCurrentBillAmount().doubleValue() != workFlowWrapper.getBillAmount().doubleValue()){
			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		}
		if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue()){
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue())
		{
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue()){
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
//		if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()){
//			if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount()){
//				throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
//			}
//		}
		
	}

	/**
	 * This method calls the settlement module to settle the payment amounts
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	/**
	 * This method is responsible for inserting the data into the transaction
	 * tables
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	/**
	 * This method is responsible for updating the supplier with the information
	 * for example updating LESCO system with all the bill collections
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper) {
		return wrapper;
	}

	/**
	 * Validates the user input
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws Exception
	 */

	public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

		// ------------------------Validates the Customer's requirements
		// -----------------------------------
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction");
		}
		
//		 --------------------- Validates the Retailer's requirements -----------------------------
	    if (wrapper.getFromRetailerContactModel() != null)
	    {
	      if (!wrapper.getFromRetailerContactModel().getActive())
	      {
	        throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NOT_ACTIVE);
	      }
//              if( wrapper.getFromRetailerContactModel().getBalance().doubleValue() < wrapper.getTransactionAmount().doubleValue() )
//              {
//                throw new WorkFlowException(WorkFlowErrorCodeConstants.INSUFFICIENT_RETAILER_BALANCE);
//              }
	    }
	    else
	    {
	      throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NULL);
	    }

		if (wrapper.getAppUserModel() != null){
			if ("".equals(wrapper.getAppUserModel().getMobileNo())){
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
			}
		}else{
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
		}
		
		if (wrapper.getUserDeviceAccountModel() == null){
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}

		if (wrapper.getOlaSmartMoneyAccountModel() != null) {
			if (!wrapper.getOlaSmartMoneyAccountModel().getActive()) {
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
			}			
		}else{
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
		}

		// -------------------------------- Validates the Product's requirements
		// ---------------------------------
		if (wrapper.getProductModel() != null) {
			if (!wrapper.getProductModel().getActive()) {
				throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
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

		// ----------------------- Validates the Supplier's requirements
		// ---------------------------------------------
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

		// ----------------------- Validates the Supplier Bank Info requirements
		// -----------------
//		if (wrapper.getSupplierBankInfoModel() == null
//				|| wrapper.getSupplierBankInfoModel().getAccountNo() == null
//				|| "".equals(wrapper.getSupplierBankInfoModel().getAccountNo())) {
//		
//			logger.error("Either SupplierBankInfo or account No is null");
//			throw new WorkFlowException(
//					WorkFlowErrorCodeConstants.SUPPLIER_MERCH_NO_NULL);
//		}
		// ---------------------------------------------------------------------------------------

		// ----------------------- Validates the operator Bank Info requirements
		// -----------------
//		if (wrapper.getOperatorBankInfoModel() == null) {
//		
//			throw new WorkFlowException(
//					WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
//		}
//		if (wrapper.getOperatorPayingBankInfoModel() == null) {
//		
//			throw new WorkFlowException(
//					WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
//		}
		// ---------------------------------------------------------------------------------------

		// ------------------------- Validates the iNPUT's requirements
		// -----------------------------------
		if (wrapper.getBillAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
		}
		if (wrapper.getTotalAmount() < 0) {
		
			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
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
		
		if(logger.isDebugEnabled())
		{
			
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction");
		}
		return wrapper;
	}

	/**
	 * Method responsible for processing Agent Credit Card Bill Payment transaction
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception
	{
		TransactionDetailModel txDetailModel = new TransactionDetailModel();
		String txAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount());
		String seviceChargesAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());

		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Inside doSale(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction..");
			}
			
			wrapper.getTransactionModel().setConfirmationMessage(" _ ");
			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getWalkInCustomerMob());
			
			wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.FAILED );
			
			if(null != wrapper.getWalkInCustomerMob() && !"".equals(wrapper.getWalkInCustomerMob())){
				txDetailModel.setCustomField6(wrapper.getWalkInCustomerMob());
			}
			if(null != wrapper.getWalkInCustomerCNIC() && !"".equals(wrapper.getWalkInCustomerCNIC())){
				txDetailModel.setCustomField7(wrapper.getWalkInCustomerCNIC());
			}
			
			wrapper = this.getBillInfo(wrapper);
//			this.validateBillInfo(wrapper); //validate bill info against the one coming from iPos to see if they match

			CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
			CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
			this.validateCommission(commissionWrapper, wrapper);
			
			//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
			if(null != wrapper.getFromRetailerContactModel() && wrapper.getFromRetailerContactModel().getHead()){
				commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
				commissionAmounts.setFranchise1CommissionAmount(0.0d);
			}
			
			wrapper.setCommissionAmountsHolder(commissionAmounts);

			wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount()+commissionAmounts.getFedCommissionAmount());
			wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

			txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
			txDetailModel.setProductIdProductModel(wrapper.getProductModel());
//			txDetailModel.setConsumerNo(((BillPaymentVO) wrapper.getProductVO()).getConsumerNo());
			wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

//			wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));
			
			txAmount = Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount());
			seviceChargesAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());

			txDetailModel.setSettled(false);
			wrapper.setTransactionDetailModel(txDetailModel);
			wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
			
			txManager.saveTransaction(wrapper);

			this.settlementManager.settleCommission(commissionWrapper, wrapper);
			
			txDetailModel.setSettled(true);

			
		    BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

			SwitchWrapper olaSwitchWrapper = new SwitchWrapperImpl();
			olaSwitchWrapper.setWorkFlowWrapper(wrapper);
			olaSwitchWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());// Agent SMA
			olaSwitchWrapper.setFtOrder(1);
			
			logger.info("[AgentCreditCardPaymentTransaction.doSale()] Going to transfer funds from Agent Account to Credit Card Pool Account. Transaction ID: " + olaSwitchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
			olaSwitchWrapper = abstractFinancialInstitution.debitCreditAccount(olaSwitchWrapper);
			
			wrapper.setOLASwitchWrapper(olaSwitchWrapper); //setting the olaSwitchWrapper for rollback
		    wrapper.getTransactionModel().setProcessingSwitchId(olaSwitchWrapper.getSwitchSwitchModel().getSwitchId());

			double agentBalance = olaSwitchWrapper.getOlavo().getFromBalanceAfterTransaction(); // Agent balance
			
			
//			BaseWrapper baseWrapper = new BaseWrapperImpl();
			//Added  by Maqsood for Phoenix
//			SmartMoneyAccountModel sma = wrapper.getOlaSmartMoneyAccountModel();
//			SmartMoneyAccountModel agentSmartMoneyAccountModel = sma;
			
//			baseWrapper.setBasePersistableModel(sma);
//			wrapper.setSmartMoneyAccountModel(sma);
//			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

//			TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
			
//			double balance = 0 ;
//			transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
			
			//Moving check balance before FT
			
//			SwitchWrapper switchWrapperTemp = new SwitchWrapperImpl();
//			switchWrapperTemp.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
//			switchWrapperTemp.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getWorkFlowWrapper().getAccountInfoModel().getOldPin());
//			switchWrapperTemp.setWorkFlowWrapper(wrapper);
//			switchWrapperTemp.setTransactionTransactionModel(wrapper.getTransactionModel());
//			switchWrapperTemp.setBasePersistableModel( agentSmartMoneyAccountModel ) ;
//			
//			switchWrapperTemp = abstractFinancialInstitution.checkBalance(switchWrapperTemp);
//			balance = switchWrapperTemp.getBalance() ;
			
//			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
//			already set in OLAVeriflyFinancialInstitutionImpl.debitCreditAccount()
//			wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(switchWrapperTemp.getAccountInfoModel().getAccountNo(), 5, "*"));

			ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
			
			//Moving SMS before Bill Payment to avoid any JMS related issue 22nd May 2013
			String agentSMS=this.getMessageSource().getMessage(
					"USSD.AgentCreditCardPayment.AgentSMS",
					new Object[] {
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
						    StringUtil.replaceString(((CreditCardPaymentVO)wrapper.getProductVO()).getCardNumber(), 5, "*"),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							wrapper.getTransactionCodeModel().getCode()
							},null);
			wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
			
			messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));
			
			String customerSMS=this.getMessageSource().getMessage(
					"USSD.AgentCreditCardPayment.CustomerSMS",
					new Object[] {
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTotalAmount()),
						    StringUtil.replaceString(((CreditCardPaymentVO)wrapper.getProductVO()).getCardNumber(), 5, "*"),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							wrapper.getTransactionCodeModel().getCode()
							},null);
			
			messageList.add(new SmsMessage(wrapper.getWalkInCustomerMob(), customerSMS));
			
			wrapper.getTransactionDetailModel().setCustomField8(customerSMS);
			wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
			
//			SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//			switchWrapper.setWorkFlowWrapper(wrapper);
//			switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());

//			transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
			
//			switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
			
			//The following code is for Phoenix implementation
//			switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
//			switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//			switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//			switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
//			switchWrapper.setBasePersistableModel(sma);
//			switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()-switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
			
//			wrapper.setSwitchWrapper(switchWrapper);
//			if(null != wrapper.getProductModel() && wrapper.getProductModel().getProductId() != 50002L) //setting account info model in case of bill payment
//			{
//				wrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());
//			}
			
//			if(wrapper.getFirstFTIntegrationVO() != null) {
//				String bankResponseCode = wrapper.getFirstFTIntegrationVO().getResponseCode();
//				if(bankResponseCode != null) {
//					wrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
//				}
//			}
			
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
			
			txManager.saveTransaction(wrapper);		
			
			wrapper = billSaleProductDispenser.doSale(wrapper);
			
			wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);	
			
		}
		
		if (logger.isDebugEnabled()){
			logger.debug("Ending doSale of AgentCreditCardPaymentTransaction.");
		}

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
			throws Exception {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction....");
		}
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		wrapper = super.doPreStart(wrapper);

		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(wrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());
		
		// Populate Retailer Contact model from DB
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    RetailerContactModel retailerContact = new RetailerContactModel();
	    retailerContact.setRetailerContactId( wrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
	    searchBaseWrapper.setBasePersistableModel( retailerContact );
	    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
	    wrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());


//		wrapper.setCustomerModel(new CustomerModel());
//		wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getCustomerId());

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

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		
		// Populate the OLA Smart Money Account from DB
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
		sma.setRetailerContactId( ThreadLocalAppUser.getAppUserModel().getRetailerContactId() ) ;
		baseWrapper.setBasePersistableModel(sma);
		baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
		wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
		wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

		// Populate Supplier Bank Info Model
//		SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
//		supplierBankInfoModel.setSupplierId(wrapper.getProductModel().getSupplierId());
//		supplierBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
//		baseWrapper.setBasePersistableModel(supplierBankInfoModel);
//		supplierBankInfoModel = (SupplierBankInfoModel) this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper).getBasePersistableModel();
//		wrapper.setSupplierBankInfoModel(supplierBankInfoModel);
//
//		OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();
//
//		if (supplierBankInfoModel != null)
//		{
//			// Populate Operator's Paying Bank Info Model
//			operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
//			operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
//			operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
//			baseWrapper.setBasePersistableModel(operatorBankInfoModel);
//			wrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
//		}
//
//		// Populate Operator Bank Info Model
//		// OperatorBankInfoModel operatorBankInfoModel = new
//		// OperatorBankInfoModel();
//		operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
//		operatorBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
//		operatorBankInfoModel.setBankId(wrapper.getSmartMoneyAccountModel().getBankId());
//		baseWrapper.setBasePersistableModel(operatorBankInfoModel);
//		wrapper.setOperatorBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
		
		logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction....");
		
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)
			throws Exception {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction....");
		}
		wrapper = super.doPreProcess(wrapper);

		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		txModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;
		txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
		txModel.setRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());
		txModel.setDistributorId(wrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		txModel.setProcessingBankId(wrapper.getOlaSmartMoneyAccountModel().getBankId());
		txModel.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		
		
		txModel.setTransactionAmount(wrapper.getBillAmount());
		txModel.setTotalAmount(wrapper.getBillAmount());
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper
				.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel()
				.getPaymentModeId());

		// Customer model of transaction is populated
//		txModel.setCustomerIdCustomerModel(wrapper.getCustomerModel());

		// Customer mobile No
		txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel()
				.getMobileNo());
		txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel()
				.getMobileNo());
		txModel.setMfsId(/*wrapper.getUserDeviceAccountModel().getUserId()*/ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());

		wrapper.setTransactionModel(txModel);
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of AgentCreditCardPaymentTransaction....");
		}

		return wrapper;
	}


	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}

	public void setSettlementManager(SettlementManager settlementManager) {
		this.settlementManager = settlementManager;
	}

	public void setCustomerManager(CustTransManager customerManager) {
		this.customerManager = customerManager;
	}

	public void setProductDispenseController(
			ProductDispenseController productDispenseController) {
		this.productDispenseController = productDispenseController;
	}

	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}

	public void setSmartMoneyAccountManager(
			SmartMoneyAccountManager smartMoneyAccountManager) {
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setSmsSender(SmsSender smsSender) {
		this.smsSender = smsSender;
	}

	public Double getTransactionProcessingCharges(
			CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of AgentCreditCardPaymentTransaction....");
		}
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) 
		{
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue() ) 
			{
				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
					transProcessingAmount += commissionRateModel.getRate();
				else
					transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
				
				//transProcessingAmount += commissionRateModel.getRate();
				//System.out.println( "  !!!!!!!!!!!!!!!!!!1 " + transProcessingAmount );
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getTransactionProcessingCharges of AgentCreditCardPaymentTransaction....");
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
		logger.info("[AgentCreditCardPaymentTransaction.rollback] Rolling back Agent Credit Card Bill Payment transaction with  ID: " + code);
		try{
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			wrapper.getTransactionModel().setTransactionId(null);
			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
			txManager.saveTransaction(wrapper);
		}catch(Exception ex){
			logger.error("Unable to save Agent Credit Card Bill Payment Transaction details in case of rollback: \n"+ ex.getStackTrace());
		}
		
		if(null != wrapper.getOLASwitchWrapper()){
			logger.info("[AgentCreditCardPaymentTransaction.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
			AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
			abstractFinancialInstitution.rollback(this.swapAccounts(wrapper,1));
			
			if(null != wrapper.getSwitchWrapper() 
					&& null != wrapper.getSwitchWrapper().getIntegrationMessageVO() 
					&& null != wrapper.getSwitchWrapper().getIntegrationMessageVO().getResponseCode()
					&& wrapper.getSwitchWrapper().getIntegrationMessageVO().getResponseCode().equals("00")){
				
		    	ProductDispenser productDispense = this.productDispenseController.loadProductDispenser( wrapper ) ;
		    	productDispense.rollback(wrapper);
		    }
		}
		logger.info("[AgentCreditCardPaymentTransaction.rollback] rollback complete..."); 
		
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

	
	public static String buildRRNPrefix() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String transdatetime = format.format(now);
		return transdatetime;
	}
	
	
	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public void setRetailerContactManager(RetailerContactManager retailerContactManager)
	{
		this.retailerContactManager = retailerContactManager;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


}
