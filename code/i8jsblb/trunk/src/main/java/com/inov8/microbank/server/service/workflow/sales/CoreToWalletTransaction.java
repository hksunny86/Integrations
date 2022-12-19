package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.integration.vo.CreditAdviceVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CoreToWalletTransaction extends SalesTransaction {

	private MessageSource messageSource;
	private CommissionManager commissionManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private GenericDao genericDAO;
	private FinancialIntegrationManager financialIntegrationManager;
	private CustTransManager customerManager;
	private TransactionReversalManager transactionReversalManager;

	public CoreToWalletTransaction(){
	}

	/**
	 * Update Workflow wrapper with different parameters required in further steps
	 */
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of InitialDepositTransaction....");

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		wrapper = super.doPreStart(wrapper);

		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		
		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()){
			CustomerModel custModel = new CustomerModel();
			custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
			BaseWrapper bWrapper = new BaseWrapperImpl();
			bWrapper.setBasePersistableModel(custModel);
			bWrapper = customerManager.loadCustomer(bWrapper);
			if(null != bWrapper.getBasePersistableModel()){
				custModel = (CustomerModel) bWrapper.getBasePersistableModel();
				wrapper.setCustomerModel(custModel);
				wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
				wrapper.setTaxRegimeModel(custModel.getTaxRegimeIdTaxRegimeModel());
			}
		}else{
			SegmentModel segmentModel = new SegmentModel();
			segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
			wrapper.setSegmentModel(segmentModel);
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of InitialDepositTransaction....");

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
		
		wrapper = super.doPreProcess(wrapper);

		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTotalAmount(0d);
		txModel.setTotalCommissionAmount(0d);
		txModel.setDiscountAmount(0d);

		// Transaction Type model of transaction is populated
		txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());

		// Sets the Device type
		txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

		// Smart Money Account Id is populated
		txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

		// Payment mode model of transaction is populated
		txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

//		txModel.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());

		// Populate processing Bank Id
		txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);

//		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
		
		if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER.longValue()){
			txModel.setToRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		}
//		txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
//		txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
//		txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		
		wrapper.setTransactionModel(txModel);
		
		return wrapper;
	}

	/**
	 * Method responsible for processing the Initial Deposit transaction
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
		TransactionDetailModel txDetailModel = new TransactionDetailModel();

		CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
		if (null != wrapper.getFromRetailerContactModel() && wrapper.getFromRetailerContactModel().getHead()) {
			commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
			commissionAmounts.setFranchise1CommissionAmount(0.0d);
		}
		
		/* Check removed as it never executed ( no record against productDeviceFlow.DeviceTypeId = 13)
		ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
		productDeviceFlowModel.setProductId(wrapper.getProductModel().getPrimaryKey());
		productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.BANKING_MIDDLEWARE );
		
		List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

		if( list != null && list.size() > 0 )
		{
			productDeviceFlowModel = list.get(0);	
			wrapper.setProductDeviceFlowModel(productDeviceFlowModel);
		}
		
		if ( productDeviceFlowModel != null && productDeviceFlowModel.getDeviceFlowId() != null && 
				productDeviceFlowModel.getDeviceFlowId().longValue() != 17 )
		{
			this.validateCommission(commissionWrapper, wrapper); // validate
		}
		*/

		wrapper.setCommissionAmountsHolder(commissionAmounts);

		wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

		wrapper.getTransactionModel().setCreatedOn(new Date());
		wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		txDetailModel.setConsumerNo(((CreditAdviceVO) wrapper.getProductVO()).getMobileNo());
		txDetailModel.setSettled(false);
		wrapper.setTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);


		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel()); // Customer SMA
		AbstractFinancialInstitution abstractFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

		switchWrapper.getWorkFlowWrapper().setIsIvrResponse(true);

		// perform Initial Deposit
		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);

		// Agent Account Number 
		wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
		wrapper.getTransactionModel().setConfirmationMessage(" _ ");
		wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
		wrapper.getTransactionDetailModel().setConsumerNo(wrapper.getCustomerAppUserModel().getMobileNo());
		// Customer Account Number
		wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
		wrapper.getTransactionDetailMasterModel().setTransactionAmount(wrapper.getTransactionAmount());
		// customer balance
		((CreditAdviceVO) wrapper.getProductVO()).setBalance(switchWrapper.getOlavo().getToBalanceAfterTransaction());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
		wrapper.getTransactionDetailMasterModel().setTotalAmount(commissionAmounts.getTotalAmount());
		wrapper.getTransactionDetailMasterModel().setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.longValue()));
		wrapper.getTransactionModel().setDeviceTypeId(DeviceTypeConstantsInterface.BANKING_MIDDLEWARE.longValue());
		txDetailModel.setSettled(true);
		txManager.saveTransaction(wrapper);

//		BaseWrapper _baseWrapper = new BaseWrapperImpl();
//		_baseWrapper.setBasePersistableModel(new BankModel(BankConstantsInterface.ASKARI_BANK_ID));
//		AbstractFinancialInstitution coreFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(_baseWrapper);
//		SmartMoneyAccountModel smartMoneyAccountModelTemp = wrapper.getOlaSmartMoneyAccountModel();
//
//		SwitchWrapper switchWrapper2 = new SwitchWrapperImpl();
//		switchWrapper2.setWorkFlowWrapper(wrapper);
//		switchWrapper2.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
//
//		logger.info("[coreToWalletTransaction.doSale] Going to credit T24 Recipient A/C." +
//				" LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
//				" Trx ID:" + wrapper.getTransactionCodeModel().getCode());
//
//		switchWrapper2.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
//		switchWrapper2 = coreFinancialInstitution.creditAccountAdvice(switchWrapper2);
//		wrapper.setSmartMoneyAccountModel(smartMoneyAccountModelTemp);
//		wrapper.setMiddlewareSwitchWrapper(switchWrapper2);
//
//		this.settleAmount(wrapper); // settle all amounts to the respective stakeholders

		wrapper.setSwitchWrapper(switchWrapper);
		wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getName());

		wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
		wrapper.getTransactionDetailModel().setCustomField1("" + wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());

		//SMS should be sent by openCustomerAccountCommand

		txManager.saveTransaction(wrapper);
		settlementManager.settleCommission(commissionWrapper, wrapper);

		//*****************************************************************
		//****  Update status of from 'Pushed to SAF' to 'Successful'  ****
		//*****************************************************************
		transactionReversalManager.updateIBFTStatus(wrapper.getObject(CommandFieldConstants.KEY_STAN).toString(),
				(Date) wrapper.getObject(CommandFieldConstants.KEY_TX_DATE),
				PortalConstants.IBFT_STATUS_SUCCESS,
				wrapper.getTransactionCodeModel().getCode());

		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(1);

		String brandName = null;

		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			brandName = MessageUtil.getMessage("sco.brandName");
		}
		else {
			brandName = MessageUtil.getMessage("jsbl.brandName");
		}
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");
		Double balance = switchWrapper.getOlavo().getToBalanceAfterTransaction();
		String senderAccNumber = ObjectUtils.toString(wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER_SENDER), "");
		String bankId = ObjectUtils.toString(wrapper.getObject(CommandFieldConstants.KEY_BANK_ID), "");
		String bankName="";
		if (bankId != null && bankId != ""){
			bankName = ObjectUtils.toString(BankEnum.getBankName(Long.parseLong(bankId)), "");
		}

		String ibftSMS = this.getMessageSource().getMessage(
				"coreToWallet.recipient.sms",
				new Object[] {
						wrapper.getTransactionCodeModel().getCode(),
						Formatter.formatDouble(wrapper.getTransactionAmount()),
						tf.print(new LocalTime()),
						dtf.print(new DateTime()),
						senderAccNumber,
						Formatter.formatDouble(balance),
						},
						null);
		
		messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), ibftSMS));
		
		wrapper.getTransactionModel().setConfirmationMessage(ibftSMS);

		wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
		
		return wrapper;
	}
	


	/**
	 * This method is responsible for inserting the data into the transaction
	 * tables
	 * 
	 * @param wrapper  WorkFlowWrapper
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
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 * @throws Exception
	 */

	public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

		// Validates the Customer's requirements
		if (logger.isDebugEnabled()) 
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of InitialDepositTransaction");
		
		if(null == wrapper.getAppUserModel())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.AGENT_ACCOUNT_NOT_FOUND);
		
		if (wrapper.getCustomerAppUserModel() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
		
		if("".equals(wrapper.getCustomerAppUserModel().getMobileNo())) 
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);

		if (wrapper.getUserDeviceAccountModel() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);

		if (wrapper.getOlaSmartMoneyAccountModel() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
		
		if (!wrapper.getOlaSmartMoneyAccountModel().getActive())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
			
		if (wrapper.getOlaSmartMoneyAccountModel().getChangePinRequired())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_PIN_CHG_REQ);

		if(null != ThreadLocalAppUser.getAppUserModel().getCustomerId()) {  // for Thread local user is agent hence this if
			if (!wrapper.getOlaSmartMoneyAccountModel().getCustomerId().toString().equals(ThreadLocalAppUser.getAppUserModel().getCustomerId().toString()))
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		if(wrapper.getAppUserModel().getAppUserTypeId() == UserTypeConstantsInterface.CUSTOMER.longValue()){			
			if (!wrapper.getOlaSmartMoneyAccountModel().getCustomerId().toString().equals(wrapper.getCustomerAppUserModel().getCustomerId().toString()))
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		

		// Validates the Product's requirements
		if (wrapper.getProductModel() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);

		if (!wrapper.getProductModel().getActive())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
		
		
		// Validates the Product's requirements 		
		if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue()){
			if (!wrapper.getProductModel().getActive())
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_SERVICE_TYPE);
		}

		// Validates the Supplier's requirements
		if (wrapper.getProductModel().getSupplierIdSupplierModel() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NULL);

		if (!wrapper.getProductModel().getSupplierIdSupplierModel().getActive())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE);

		
		//  Validates the iNPUT's requirements
		if (wrapper.getTransactionAmount() < 0)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_MODEL_NULL);

		/*if (wrapper.getTotalAmount() < 0)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);

		if (wrapper.getTotalCommissionAmount() < 0)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);

		if (wrapper.getTxProcessingAmount() < 0)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
*/
		
		// Validates the PaymentMode's requirements
		if (wrapper.getPaymentModeModel() == null || wrapper.getPaymentModeModel().getPaymentModeId() <= 0)
				throw new WorkFlowException("PaymentModeID is not supplied.");

		
		// ----------------------- Validates the Service's requirements
		if (wrapper.getProductModel().getServiceIdServiceModel() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);

		if (!wrapper.getProductModel().getServiceIdServiceModel().getActive())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);

		if (logger.isDebugEnabled())
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of InitialDepositTransaction");

		return wrapper;
	}
	
	public Double getTransactionProcessingCharges(
			CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of CnicToBBAccountTransaction....");
		}
		Double transProcessingAmount = 0D;

		List<CommissionRateModel> resultSetList = (List) commissionWrapper
				.getCommissionWrapperHashMap().get(
						CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) 
		{
			if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue() ) 
			{
				if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
					transProcessingAmount += commissionRateModel.getRate();
				else
					transProcessingAmount += (workFlowWrapper.getTransactionAmount() * commissionRateModel.getRate()) / 100;
				
				//transProcessingAmount += commissionRateModel.getRate();
				//System.out.println( "  !!!!!!!!!!!!!!!!!!1 " + transProcessingAmount );
			}
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("Ending getTransactionProcessingCharges of CnicToBBAccountTransaction....");
		}
		return transProcessingAmount;
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
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of CnicToBBAccountTransaction...");
		}
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
		
//		RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
//	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of CnicToBBAccountTransaction...");
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
			logger.debug("Inside validateCommission of CnicToBBAccountTransaction...");
		}
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
		CashInVO productVO = (CashInVO) workFlowWrapper.getProductVO();

		if (productVO.getTransactionAmount().doubleValue() != workFlowWrapper.getTransactionAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		}
		if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue())
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

		if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue())
		{

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
		if (logger.isDebugEnabled())
		{

			logger.debug("Ending validateCommission of CnicToBBAccountTransaction...");
		}

	}
	public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
		return wrapper;
	}
	
	@Override
	protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}

	protected WorkFlowWrapper doPostRollback(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreRollback(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doRollback(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}
	
	@Override
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}
	
	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
	}

	public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setGenericDAO(GenericDao genericDAO){
		this.genericDAO = genericDAO;
	}

	public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager){
		this.notificationMessageManager = notificationMessageManager;
	}
	
	public UserDeviceAccountsManager getUserDeviceAccountsManager() {
		return userDeviceAccountsManager;
	}

	public void setUserDeviceAccountsManager(
			UserDeviceAccountsManager userDeviceAccountsManager) {
		this.userDeviceAccountsManager = userDeviceAccountsManager;
	}

	public NotificationMessageManager getNotificationMessageManager() {
		return notificationMessageManager;
	}

	public FinancialIntegrationManager getFinancialIntegrationManager() {
		return financialIntegrationManager;
	}

	public CustTransManager getCustomerManager() {
		return customerManager;
	}

	public void setCustomerManager(CustTransManager customerManager) {
		this.customerManager = customerManager;
	}

	public CommissionManager getCommissionManager() {
		return commissionManager;
	}

	public void setTransactionReversalManager(
			TransactionReversalManager transactionReversalManager) {
		this.transactionReversalManager = transactionReversalManager;
	}
}
