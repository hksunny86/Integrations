package com.inov8.microbank.server.service.workflow.sales;

import java.util.Date;
import java.util.List;

import org.springframework.context.MessageSource;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
import com.inov8.microbank.common.util.ThreadLocalUserDeviceAccounts;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;

public class InitialDepositTransaction extends SalesTransaction {

	private MessageSource messageSource;
	private CommissionManager commissionManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private GenericDao genericDAO;
	private FinancialIntegrationManager financialIntegrationManager;
	
	public InitialDepositTransaction(){
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
		
		// Setting instruction and success Message
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

		txModel.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());

		// Populate processing Bank Id
		txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);

		txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
		txModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
		txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
		txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
		txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
		
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

		/*@SuppressWarnings("unused")
		boolean isBvsAccount =  (Boolean) wrapper.getObject("isBvsAccount");*/
		
		Boolean isBvsAccountObj =  (Boolean) wrapper.getObject("isBvsAccount");
		boolean isBvsAccount = (isBvsAccountObj == null) ? false : isBvsAccountObj;
		
		
		// calculate and set commissions to transaction & transaction details model 
		CommissionWrapper commissionWrapper = calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
		if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
			commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
			commissionAmounts.setFranchise1CommissionAmount(0.0d);
		}
		
		wrapper.setTxProcessingAmount(commissionAmounts.getTransactionProcessingAmount());
		wrapper.setCommissionAmountsHolder(commissionAmounts);
		wrapper.getTransactionModel().setConfirmationMessage(" _ ");
		wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

		wrapper.getTransactionModel().setCreatedOn(new Date());
		wrapper.getTransactionModel().setFromRetContactId(wrapper.getRetailerContactModel().getRetailerContactId());
		wrapper.getTransactionModel().setFromRetContactName(wrapper.getRetailerContactModel().getName());
		wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		wrapper.getTransactionModel().setCustomerId(wrapper.getCustomerAppUserModel().getCustomerId());
		
		txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
		txDetailModel.setConsumerNo(((CashInVO) wrapper.getProductVO()).getCustomerMobileNo());

		txDetailModel.setSettled(false);
		wrapper.setTransactionDetailModel(txDetailModel);
		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);

		// Set Handler Detail in Transaction and Transaction Detail Master
		if(wrapper.getHandlerModel() != null){
				wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
				wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
				wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
				wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
		}

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel()); // Customer SMA
		AbstractFinancialInstitution abstractFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

		// perform Initial Deposit
		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);
		
		// Agent Account Number 
		wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
		
		// Customer Account Number
		wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
		
		// customer balance
		((CashInVO)wrapper.getProductVO()).setCustomerBalance(switchWrapper.getOlavo().getToBalanceAfterTransaction());
		
		// agent balance
		((CashInVO)wrapper.getProductVO()).setAgentBalance(switchWrapper.getOlavo().getFromBalanceAfterTransaction());
		
		txDetailModel.setSettled(true);
		txManager.saveTransaction(wrapper);

		wrapper.setSwitchWrapper(switchWrapper);
		wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getName());
		
		ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
		productDeviceFlowModel.setProductId(wrapper.getProductModel().getPrimaryKey());
		productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );

		List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

		if( list != null && list.size() > 0 ) {
			productDeviceFlowModel = list.get(0);	
			wrapper.setProductDeviceFlowModel(productDeviceFlowModel);
		}

		wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED ) ;
		wrapper.getTransactionDetailModel().setCustomField1(""+wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());

		//SMS should be sent by openCustomerAccountCommand
	
		txManager.saveTransaction(wrapper);	
		settlementManager.settleCommission(commissionWrapper, wrapper);

		return wrapper;
	}
	
	/**
	 * This method calls the commission module to calculate the commission on
	 * this product and transaction.The wrapper should have product,payment mode
	 * and principal amount that is passed onto the commission module
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	private CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of InitialDepositTransaction...");
		}

		wrapper.setSegmentModel(wrapper.getCustomerModel().getSegmentIdSegmentModel());
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
	    wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
		
		commissionWrapper = commissionManager.calculateCommission(wrapper);

		if (logger.isDebugEnabled()) {
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of InitialDepositTransaction...");
		}
		return commissionWrapper;
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
		else if (!wrapper.getOlaSmartMoneyAccountModel().getCustomerId().toString().equals(wrapper.getCustomerAppUserModel().getCustomerId().toString()))
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
		

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
}
