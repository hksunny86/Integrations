package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.microbank.server.service.workflow.sales.SalesTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerInitiatedAccountToAccountTransaction extends SalesTransaction  {

	protected final Log log = LogFactory.getLog(getClass());
	private MessageSource messageSource;
	SupplierManager supplierManager;
	CommissionManager commissionManager;
	private SmartMoneyAccountManager smartMoneyAccountManager;
	private CustTransManager customerManager;
	private ProductManager productManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private GenericDao genericDAO;
	private WalletSafRepoDAO walletSafRepoDAO;
	private FinancialIntegrationManager financialIntegrationManager;

	public void setGenericDAO(GenericDao genericDAO)
	{
		this.genericDAO = genericDAO;
	}

	public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager)
	{
		this.notificationMessageManager = notificationMessageManager;
	}

	public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager)
	{
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public CustomerInitiatedAccountToAccountTransaction()
	{
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
//		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue())
//		{
//			CustomerModel custModel = new CustomerModel();
//			custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
//			BaseWrapper bWrapper = new BaseWrapperImpl();
//			bWrapper.setBasePersistableModel(custModel);
//			bWrapper = customerManager.loadCustomer(bWrapper);
//			if(null != bWrapper.getBasePersistableModel())
//			{
//				custModel = (CustomerModel) bWrapper.getBasePersistableModel();
//				wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
//			}
//		}

//		already set in AccountToAccountCommand wrapper.setSegmentModel(wrapper.getCustomerModel().getSegmentIdSegmentModel());

		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
	    wrapper.setTaxRegimeModel(wrapper.getCustomerModel().getTaxRegimeIdTaxRegimeModel());
		
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
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of CustomerInitiatedAccountToAccountTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		AccountToAccountVO productVO = (AccountToAccountVO) workFlowWrapper.getProductVO();

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
		//FIXME commission rate structre changed. need to recalculate transaction processing amount to compare with xml value.
		/*if (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue())).equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue()))))
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}*/
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of CustomerInitiatedAccountToAccountTransaction...");
		}

	}

	/**
	 * This method calls the settlement module to settle the payment amounts
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper)
	{
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
	public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper)
	{
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
	public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper)
	{
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

	public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
	{

		// ------------------------Validates the Customer's requirements
		// -----------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction");
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
		if (wrapper.getUserDeviceAccountModel() == null)
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}

		if (wrapper.getSmartMoneyAccountModel() != null)
		{
			if (!wrapper.getSmartMoneyAccountModel().getActive())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
			}
			//no need to check it, on login it is already checked
/*			if (wrapper.getSmartMoneyAccountModel().getChangePinRequired())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_PIN_CHG_REQ);
			}
*/			if(null != ThreadLocalAppUser.getAppUserModel().getCustomerId()) // for CW. Threadlocal user is agent hence this if
			{
				if (!wrapper.getSmartMoneyAccountModel().getCustomerId().toString().equals(ThreadLocalAppUser.getAppUserModel().getCustomerId().toString()))
				{

					throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
				}
			}
			else
			{
//				if (!wrapper.getSmartMoneyAccountModel().getCustomerId().toString().equals(wrapper.getCustomerAppUserModel().getCustomerId().toString()))
//				{
//
//					throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
//				}
			}
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
		}

		// -------------------------------- Validates the Product's requirements
		// ---------------------------------
		if (wrapper.getProductModel() != null)
		{
			if (!wrapper.getProductModel().getActive())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
			}
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
		}

		// -------------------------------- Validates the Product's requirements ---------------------------------------------------		
		if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue())
		{
			if (!wrapper.getProductModel().getActive())
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_SERVICE_TYPE);
			}
		}

		// ----------------------- Validates the Supplier's requirements
		// ---------------------------------------------
		if (wrapper.getProductModel().getSupplierIdSupplierModel() != null)
		{
			if (!wrapper.getProductModel().getSupplierIdSupplierModel().getActive())
			{

				throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE);
			}
		}
		else
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NULL);
		}

		// ------------------------- Validates the iNPUT's requirements
		
		if (wrapper.getTotalAmount() < 0)
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
		}
		if (wrapper.getTotalCommissionAmount() < 0)
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
		}
		if (wrapper.getTxProcessingAmount() < 0)
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
		}

		// ----------------------- Validates the PaymentMode's requirements
		// --------------------------------------
		if (wrapper.getPaymentModeModel() != null)
		{
			if (wrapper.getPaymentModeModel().getPaymentModeId() <= 0)
			{

				throw new WorkFlowException("PaymentModeID is not supplied.");
			}
		}

		// ----------------------- Validates the Service's requirements
		// ---------------------------
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

		if (logger.isDebugEnabled())
		{

		}
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction");
		}
		return wrapper;
	}

	/**
	 * Method respponsible for processing ther Account To Account transaction
	 * 
	 * @param wrapper
	 *            WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception
	{
		TransactionDetailModel txDetailModel = new TransactionDetailModel();

		if (logger.isDebugEnabled())
		{
			logger.debug("Inside doSale(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction..");
		}

		CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		wrapper.setCommissionAmountsHolder(commissionAmounts);
		if(wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE) != null){
			wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode((String) wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE));
		}
		///////////////////////////		
		
		wrapper.setCommissionAmountsHolder(commissionAmounts);
		wrapper.setCommissionWrapper(commissionWrapper);	
		
		AbstractFinancialInstitution olaVeriflyFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());

		AppUserModel appUserModel = wrapper.getAppUserModel();
		
		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		txDetailModel.setConsumerNo(((AccountToAccountVO) wrapper.getProductVO()).getRecipientCustomerMobileNo());
		txDetailModel.setSettled(true);
		wrapper.setTransactionDetailModel(txDetailModel);

		wrapper.setAppUserModel(appUserModel);
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
		

		BaseWrapper baseWrapper = new BaseWrapperImpl();

		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		double senderCustomerBalance = 0 ;
		double receiverCustomerBalance = 0 ;
		double agentBalance = 0 ;
		
		logger.info("[CustomerInitiatedAccountToAccountTransaction.doSale] Going to transfer funds in OLA. Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Trx ID:" + wrapper.getTransactionCodeModel().getCode());
		
		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;

		wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());

		
		senderCustomerBalance = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getOlavo().getFromBalanceAfterTransaction()); // Sender balance
		receiverCustomerBalance = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getOlavo().getToBalanceAfterTransaction()); // Receiver balance
		Double inclusiveChargesApplied = CommonUtils.getDoubleOrDefaultValue(switchWrapper.getInclusiveChargesApplied()); //Inclusive Charges Applied

		if(wrapper.getRecipientSmartMoneyAccountModel() != null) {
			wrapper.getTransactionDetailModel().setCustomField1("" + wrapper.getRecipientSmartMoneyAccountModel().getSmartMoneyAccountId());
		}
		wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
		wrapper.getTransactionDetailModel().setCustomField3(""+switchWrapper.getSwitchSwitchModel().getSwitchId());
		
		wrapper.getTransactionDetailModel().setCustomField6(""+wrapper.getCustomerAppUserModel().getMobileNo());
		wrapper.getTransactionDetailModel().setCustomField7(""+wrapper.getCustomerAppUserModel().getNic());
		wrapper.getTransactionModel().setConfirmationMessage(" _ ");
		if(wrapper.getReceiverAppUserModel() != null) {
			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getReceiverAppUserModel().getMobileNo());
		}
		else {
			wrapper.getTransactionModel().setNotificationMobileNo(String.valueOf(wrapper.getObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE)));
		}
		wrapper.getTransactionDetailModel().setSettled(true);
		
		txManager.saveTransaction(wrapper);
		wrapper.setSwitchWrapper(switchWrapper);

		if(wrapper.getReceiverAppUserModel() == null) {
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
		}
		else{
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		}
		((AccountToAccountVO)wrapper.getProductVO()).setSenderCustomerBalance(senderCustomerBalance);
		agentBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction();
		if(switchWrapper.getOlavo().getToBalanceAfterTransaction() != null) {
			receiverCustomerBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction();
		}
		String brandName = MessageUtil.getMessage("jsbl.brandName");
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);

		if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID) != null && wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).equals("NOVA")) {
			String maskedSenderAccNo = wrapper.getAppUserModel().getMobileNo();
			maskedSenderAccNo = maskedSenderAccNo.replaceAll("(\\d+)(\\d{2})","****$2");

			if(wrapper.getReceiverAppUserModel() != null) {
				String maskedReceiverAccNo = wrapper.getReceiverAppUserModel().getMobileNo();
				maskedReceiverAccNo = maskedReceiverAccNo.replaceAll("(\\d+)(\\d{2})", "****$2");
			}

			if(wrapper.getReceiverAppUserModel() == null){
				if(wrapper.getObject(CommandFieldConstants.KEY_RECIEVER_ACCOUNT_TITLE) == null){
					String custSMS = this.getMessageSource().getMessage(
							"nova.walletToWalletNonAccHolder",
							new Object[]{
									wrapper.getTransactionCodeModel().getCode(),
									Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
									wrapper.getObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE),
									PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
									senderCustomerBalance,

//							maskedSenderAccNo,
//							wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
//							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							}, null);

					messageList.add(new SmsMessage(((AccountToAccountVO) wrapper.getProductVO()).getSenderCustomerMobileNo(), custSMS));

					wrapper.getTransactionDetailModel().setCustomField8(custSMS);
				}
				else {
					String custSMS = this.getMessageSource().getMessage(
							"nova.walletToWallet",
							new Object[]{
									wrapper.getTransactionCodeModel().getCode(),
									Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
									wrapper.getObject(CommandFieldConstants.KEY_RECIEVER_ACCOUNT_TITLE),
									wrapper.getObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE),
									PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
									senderCustomerBalance,

//							maskedSenderAccNo,
//							wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
//							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							}, null);

					messageList.add(new SmsMessage(((AccountToAccountVO) wrapper.getProductVO()).getSenderCustomerMobileNo(), custSMS));

					wrapper.getTransactionDetailModel().setCustomField8(custSMS);
				}
			}
			else {
				String custSMS = this.getMessageSource().getMessage(
						"nova.walletToWallet",
						new Object[]{
								wrapper.getTransactionCodeModel().getCode(),
								Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
								wrapper.getReceiverAppUserModel().getFirstName() + wrapper.getReceiverAppUserModel().getLastName(),
								wrapper.getReceiverAppUserModel().getMobileNo(),
								PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
								senderCustomerBalance,

//							maskedSenderAccNo,
//							wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
//							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
						}, null);

				messageList.add(new SmsMessage(((AccountToAccountVO) wrapper.getProductVO()).getSenderCustomerMobileNo(), custSMS));

				wrapper.getTransactionDetailModel().setCustomField8(custSMS);
			}

		}
		else {
			//{0}\nTrx ID {1}\nYou have sent Rs.{2}\nat {3}\non {4}\nto {5}.\nFee is Rs.{6} incl FED\nAvl Bal:Rs.{7}.
			String cust1SMS = this.getMessageSource().getMessage(
					"USSD.Customer1A2ASMS",
					new Object[]{
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							((AccountToAccountVO) wrapper.getProductVO()).getRecipientCustomerMobileNo(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
							agentBalance
					}, null);

			messageList.add(new SmsMessage(((AccountToAccountVO) wrapper.getProductVO()).getSenderCustomerMobileNo(), cust1SMS));

			wrapper.getTransactionDetailModel().setCustomField8(cust1SMS);
		}

		((AccountToAccountVO)wrapper.getProductVO()).setRecipientCustomerBalance(receiverCustomerBalance);
		switchWrapper.getWorkFlowWrapper().setP2PRecepient(false);

		//{0}\nTrx ID {1}\nYou have received Rs.{2}\nat {3}\non {4}\nfrom {5}\nAvl Bal:Rs.{6}.
		if(wrapper.getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE) && wrapper.getReceiverAppUserModel() == null){
			String cust2SMS = this.getMessageSource().getMessage(
					"W2W.Receiver.sms",
					new Object[]{
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount() - inclusiveChargesApplied),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							((AccountToAccountVO) wrapper.getProductVO()).getSenderCustomerMobileNo(),
					}, null);


			messageList.add(new SmsMessage(((AccountToAccountVO) wrapper.getProductVO()).getRecipientCustomerMobileNo(), cust2SMS));

			wrapper.getTransactionModel().setConfirmationMessage(cust2SMS);
		}
		else if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).equals("NOVA")) {
			String maskedReceiverAccNo = ((AccountToAccountVO) wrapper.getProductVO()).getRecipientCustomerMobileNo();
			maskedReceiverAccNo = maskedReceiverAccNo.replaceAll("(\\d+)(\\d{2})","****$2");

			String maskedSenderAccNo = wrapper.getAppUserModel().getMobileNo();
			maskedSenderAccNo = maskedSenderAccNo.replaceAll("(\\d+)(\\d{2})","****$2");

				String cust2SMS = this.getMessageSource().getMessage(
					"nova.W2WReceivingCustomer",
					new Object[]{
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
							wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
							wrapper.getAppUserModel().getMobileNo(),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							receiverCustomerBalance,

//							wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
//                            maskedSenderAccNo,
//							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
//							receiverCustomerBalance,

					}, null);


			messageList.add(new SmsMessage(((AccountToAccountVO) wrapper.getProductVO()).getRecipientCustomerMobileNo(), cust2SMS));

			wrapper.getTransactionModel().setConfirmationMessage(cust2SMS);
		}
		else {
			String cust2SMS = this.getMessageSource().getMessage(
					"USSD.Customer2A2ASMS",
					new Object[]{
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount() - inclusiveChargesApplied),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							((AccountToAccountVO) wrapper.getProductVO()).getSenderCustomerMobileNo(),
							receiverCustomerBalance
					}, null);


			messageList.add(new SmsMessage(((AccountToAccountVO) wrapper.getProductVO()).getRecipientCustomerMobileNo(), cust2SMS));

			wrapper.getTransactionModel().setConfirmationMessage(cust2SMS);
		}
		txManager.saveTransaction(wrapper);
		wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

		this.settlementManager.settleCommission(commissionWrapper, wrapper);

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception
	{

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		//comment by tayyab
		//if (wrapper.getIsIvrResponse() == false) {
			//generate new transaction code and initialize Transaction/TransctionDetailMaster objects.
			wrapper = super.doPreStart(wrapper);
	//	}
			/*else{
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
		} */
			
		// Populate the Product model from DB
		baseWrapper.setBasePersistableModel(wrapper.getProductModel());
		baseWrapper = productManager.loadProduct(baseWrapper);
		wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

		wrapper.setCustomerModel(new CustomerModel());
		wrapper.getCustomerModel().setCustomerId(((AccountToAccountVO)wrapper.getProductVO()).getSenderCustomerId());

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

		// Populate the SENDER Customer model from DB
		baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
		baseWrapper = customerManager.loadCustomer(baseWrapper);
		wrapper.setCustomerModel((CustomerModel) baseWrapper.getBasePersistableModel());

		// Populate the Agent Smart Money Account from DB
		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
	/*	sma.setRetailerContactId( ThreadLocalAppUser.getAppUserModel().getCustomerId() ) ;
		baseWrapper.setBasePersistableModel(sma);
		baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
		sma = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();*/
		


		// Populate Retailer Contact model from DB
/*		if(wrapper.getFromRetailerContactModel() == null){                                     //comment by tayyab
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		    RetailerContactModel retailerContact = new RetailerContactModel();
		    retailerContact.setRetailerContactId( ThreadLocalAppUser.getAppUserModel().getRetailerContactId() );
		    searchBaseWrapper.setBasePersistableModel( retailerContact );
		    searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
		    wrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());
		}*/
		
//		SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
//		sma.setCustomerId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
//		CustomList<SmartMoneyAccountModel> smaList = smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(sma);
//		if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) {
//			sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
//	
//			wrapper.setSmartMoneyAccountModel(sma);
//			wrapper.setPaymentModeModel(new PaymentModeModel());
//			wrapper.getPaymentModeModel().setPaymentModeId(sma.getPaymentModeId());
//		}
		

		// Populate the RECEIVER AppUser from DB
		if(wrapper.getReceiverAppUserModel() != null) {
			AppUserModel receiverAppUserModel = new AppUserModel();
			receiverAppUserModel.setMobileNo(wrapper.getReceiverAppUserModel().getMobileNo());
			receiverAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
			receiverAppUserModel = appUserManager.getAppUserModel(receiverAppUserModel);
			if (null != receiverAppUserModel) {
				wrapper.setReceiverAppUserModel(receiverAppUserModel);
			}


			// Populate the RECEIVER Customer model from DB
			CustomerModel recipientCustomerModel = new CustomerModel();
			recipientCustomerModel.setCustomerId(receiverAppUserModel.getCustomerId());
			baseWrapper.setBasePersistableModel(recipientCustomerModel);
			baseWrapper = customerManager.loadCustomer(baseWrapper);
			wrapper.setRecipientCustomerModel((CustomerModel) baseWrapper.getBasePersistableModel());
		}
		
		// Populate the SENDER Customer AppUserModel                                                 //comment by tayyab
/*		AppUserModel senderAppUserModel =new AppUserModel();
		senderAppUserModel.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		senderAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		senderAppUserModel = appUserManager.getAppUserModel(senderAppUserModel);
		if(null != senderAppUserModel){
			wrapper.setCustomerAppUserModel(senderAppUserModel);
		}*/

		//SENDER Customer User Device Accounts Model
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getCustomerAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

		sma = new SmartMoneyAccountModel();
		sma.setCustomerId(wrapper.getCustomerAppUserModel().getCustomerId());
		sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
		CustomList<SmartMoneyAccountModel> smaList = smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(sma);
		if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) {
			sma = ((SmartMoneyAccountModel)smaList.getResultsetList().get(0)) ;
			wrapper.setOlaSmartMoneyAccountModel(sma);
			
			wrapper.setSmartMoneyAccountModel(sma);
			wrapper.setPaymentModeModel(new PaymentModeModel());
			wrapper.getPaymentModeModel().setPaymentModeId(sma.getPaymentModeId());
		}

		if(wrapper.getReceiverAppUserModel() != null) {
			sma = new SmartMoneyAccountModel();
			sma.setCustomerId(wrapper.getReceiverAppUserModel().getCustomerId());
			sma.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
			smaList = smartMoneyAccountManager.loadCustomerSmartMoneyAccountByHQL(sma);
			if (null != smaList && null != smaList.getResultsetList() && smaList.getResultsetList().size() > 0) {
				sma = ((SmartMoneyAccountModel) smaList.getResultsetList().get(0));
				wrapper.setRecipientSmartMoneyAccountModel(sma);
			}
		}
		// Set Handler User Device Account Model
/*		if(wrapper.getHandlerAppUserModel() != null && wrapper.getHandlerAppUserModel().getAppUserId() != null){			 //comment by tayyab
			UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
			handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
			handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
			baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
			baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
			wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		}*/

		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction....");
		}
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
	{
		logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction....");
		
		wrapper = super.doPreProcess(wrapper);
		
	/*	if (wrapper.getIsIvrResponse())                                //comment by tayyab
			return wrapper;*/
		
		TransactionModel txModel = wrapper.getTransactionModel();
		
		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTotalAmount(wrapper.getTransactionAmount());
		/*txModel.setRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());                                           //comment by tayyab
		txModel.setDistributorId(wrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		txModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName() ) ;*/
		
		
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Sender Customer model of transaction is populated
		txModel.setCustomerIdCustomerModel(new CustomerModel());
		txModel.getCustomerIdCustomerModel().setCustomerId(wrapper.getCustomerModel().getCustomerId());
		
		//Sender Customer Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		txModel.setCustomerMobileNo(((AccountToAccountVO)wrapper.getProductVO()).getSenderCustomerMobileNo());
		txModel.setSaleMobileNo(((AccountToAccountVO)wrapper.getProductVO()).getSenderCustomerMobileNo());

		// Populate processing Bank Id
		txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
		txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
		
		wrapper.setTransactionModel(txModel);
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CustomerInitiatedAccountToAccountTransaction....");
		}

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception
	{
		return wrapper;
	}

	public void setSupplierManager(SupplierManager supplierManager)
	{
		this.supplierManager = supplierManager;
	}

	public void setCommissionManager(CommissionManager commissionManager)
	{
		this.commissionManager = commissionManager;
	}

	public void setCustomerManager(CustTransManager customerManager)
	{
		this.customerManager = customerManager;
	}

	public void setProductManager(ProductManager productManager)
	{
		this.productManager = productManager;
	}

	public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager)
	{
		this.smartMoneyAccountManager = smartMoneyAccountManager;
	}

	public void setSmsSender(SmsSender smsSender)
	{
		this.smsSender = smsSender;
	}

	public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of CustomerInitiatedAccountToAccountTransaction....");
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
			logger.debug("Ending getTransactionProcessingCharges of CustomerInitiatedAccountToAccountTransaction....");
		}
		return transProcessingAmount;
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
	protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper) throws Exception
	{
		return wrapper;
	}
	
	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
	{
//		logger.info("[AccountToAccountTransaction.rollback] rollback called..."); 
//		try{
//			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
//			wrapper.getTransactionModel().setTransactionId(null);
//			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
//			txManager.saveTransaction(wrapper);
//		}catch(Exception ex){
//			logger.error("Unable to save Transaction details in case of rollback: \n"+ ex.getStackTrace());
//		}
//		
		logger.info("[CustomerInitiatedAccountToAccountTransaction.rollback] rollback complete..."); 
		return wrapper;
	}
	
	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
	{
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setWalletSafRepoDAO(WalletSafRepoDAO walletSafRepoDAO) {
		this.walletSafRepoDAO = walletSafRepoDAO;
	}
/*	public void setRetailerContactManager(RetailerContactManager retailerContactManager)
	{
		this.retailerContactManager = retailerContactManager;
	}*/


}
