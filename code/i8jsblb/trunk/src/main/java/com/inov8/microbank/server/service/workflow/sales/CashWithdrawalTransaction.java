package com.inov8.microbank.server.service.workflow.sales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inov8.microbank.common.model.*;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.common.util.CommissionReasonConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.Formatter;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
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
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.CashWithdrawalVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;

public class CashWithdrawalTransaction extends SalesTransaction{

	private MessageSource messageSource;
	private CommissionManager commissionManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private GenericDao genericDAO;
	private FinancialIntegrationManager financialIntegrationManager;
	
	public CashWithdrawalTransaction(){
	}

	/**
	 * This method calls the commission module to calculate the commission on
	 * this product and transaction.The wrapper should have product,payment mode
	 * and principal amount that is passed onto the commission module
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of CashWithdrawalTransaction...");
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
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of CashWithdrawalTransaction...");
		}
		return commissionWrapper;
	}

	/**
	 * 
	 * @param commissionHolder  CommissionAmountsHolder
	 * @param calculatedCommissionHolder CommissionAmountsHolder
	 * @throws FrameworkCheckedException
	 */
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside validateCommission of CashWithdrawalTransaction...");
		}
		
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTransactionAmount().doubleValue()))
				.equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTransactionAmount().doubleValue())))) {

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalCommissionAmount().doubleValue()))
				.equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalCommissionAmount().doubleValue())))) {

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalAmount().doubleValue()))
				.equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalAmount().doubleValue())))) {

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		
		if (!Double.valueOf(Formatter.formatDouble(this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue()))
				.equals( Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTxProcessingAmount().doubleValue())))) {

			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Ending validateCommission of CashWithdrawalTransaction...");
		}
	}

	/**
	 * This method calls the settlement module to settle the payment amounts
	 * 
	 * @param wrapper  WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */
	public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
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
			logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CashWithdrawalTransaction");
		
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

		if(null != ThreadLocalAppUser.getAppUserModel().getCustomerId()) {  // for CW. Threadlocal user is agent hence this if
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

		if (wrapper.getTotalAmount() < 0)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);

		if (wrapper.getTotalCommissionAmount() < 0)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);

		if (wrapper.getTxProcessingAmount() < 0)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);

		
		// Validates the PaymentMode's requirements
		if (wrapper.getPaymentModeModel() == null || wrapper.getPaymentModeModel().getPaymentModeId() <= 0)
				throw new WorkFlowException("PaymentModeID is not supplied.");

		
		// ----------------------- Validates the Service's requirements
		if (wrapper.getProductModel().getServiceIdServiceModel() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);

		if (!wrapper.getProductModel().getServiceIdServiceModel().getActive())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);

		if (logger.isDebugEnabled())
			logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CashWithdrawalTransaction");

		return wrapper;
	}

	/**
	 * Method responsible for processing the Cash Withdrawal transaction
	 * 
	 * @param wrapper WorkFlowWrapper
	 * @return WorkFlowWrapper
	 */

	public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
		TransactionDetailModel txDetailModel = new TransactionDetailModel();

		// calculate and set commissions to transaction & transaction details model 
		CommissionWrapper commissionWrapper = calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
				CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

		//validateCommission(commissionWrapper, wrapper); // validate commission against the one calculated against the bill and the one coming from iPos

		//if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
		if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
			commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
			commissionAmounts.setFranchise1CommissionAmount(0.0d);
		}
		
		wrapper.setCommissionAmountsHolder(commissionAmounts);

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel()); // Customer SMA
		AbstractFinancialInstitution abstractFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);
			
		wrapper.setCommissionAmountsHolder(commissionAmounts);
		wrapper.setCommissionWrapper(commissionWrapper);		

		
		abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);

		AppUserModel appUserModel = wrapper.getAppUserModel();
		wrapper.setAppUserModel(wrapper.getCustomerAppUserModel());
		
		abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
		wrapper.setAppUserModel(appUserModel);
	
		
		// update workflow wrapper with required fields and make IVR request for customer verification
		if (!wrapper.getIsIvrResponse() && !wrapper.getUSSDCashWithdrawal()
				&& wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED) != null && !wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED).equals("1")) {
			wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
			wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
			wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

			wrapper.getTransactionModel().setCreatedOn(new Date());
			wrapper.getTransactionModel().setFromRetContactId(wrapper.getRetailerContactModel().getRetailerContactId());
			wrapper.getTransactionModel().setFromRetContactName(wrapper.getRetailerContactModel().getName());
			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
			
			txDetailModel.setActualBillableAmount(commissionAmounts.getTransactionAmount());
			txDetailModel.setProductIdProductModel(wrapper.getProductModel());
			txDetailModel.setConsumerNo(((CashWithdrawalVO) wrapper.getProductVO()).getCustomerMobileNo());
			
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

		wrapper.setCommissionAmountsHolder(commissionAmounts);
		wrapper.setCommissionWrapper(commissionWrapper);
		abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
		
		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
		switchWrapper.setWorkFlowWrapper(wrapper);
		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

		//perform Cash withdrawal on OLA
		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;
		
		// Agent Account Number
		wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getToAccountNo());

		if(wrapper.getUSSDCashWithdrawal() || (wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED) != null
				&& wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED).equals("1"))) {
			txDetailModel = new TransactionDetailModel();
			txDetailModel.setProductId(wrapper.getProductModel().getProductId());
			wrapper.setTransactionDetailModel(txDetailModel);

			wrapper.getTransactionModel().setConfirmationMessage(" _ ");
			wrapper.getTransactionModel().setToRetContactId(wrapper.getAppUserModel().getRetailerContactId());
			wrapper.getTransactionModel().setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
			wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
			wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
			wrapper.getTransactionModel().setSaleMobileNo(wrapper.getTransactionModel().getCustomerMobileNo());
			wrapper.getTransactionModel().setCustomerId(wrapper.getCustomerAppUserModel().getCustomerId());
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.COMPLETED);
			wrapper.getTransactionModel().setToRetContactName(wrapper.getCustomerAppUserModel().getFirstName() + " " + wrapper.getCustomerAppUserModel().getLastName());
			wrapper.getTransactionModel().setToRetContactMobNo(wrapper.getCustomerAppUserModel().getMobileNo());
			wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
		}
		wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getFromAccountNo());


		// Customer Account Number

					
		double customerBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction(); // balance
		((CashWithdrawalVO)wrapper.getProductVO()).setCustomerBalance(customerBalance);
		
		Double agentBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction(); // agent balance
		((CashWithdrawalVO)wrapper.getProductVO()).setAgentBalance(agentBalance);
		
		txDetailModel.setSettled(true);
		txManager.saveTransaction(wrapper);

		wrapper.setSwitchWrapper(switchWrapper);
		wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
		wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());
		
		ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
		productDeviceFlowModel.setProductId(wrapper.getProductModel().getPrimaryKey());
		productDeviceFlowModel.setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );

		List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

		if( list != null && list.size() > 0 ) {
			productDeviceFlowModel = list.get(0);	
			wrapper.setProductDeviceFlowModel(productDeviceFlowModel);
		}

		// settle all amounts to the respective stakeholders
		this.settleAmount(wrapper); 

		wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED ) ;
		wrapper.getTransactionDetailModel().setCustomField1(""+wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
		
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);

		String brandName=null;

		if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
			 brandName = MessageUtil.getMessage("sco.brandName");
		}else {


			brandName = MessageUtil.getMessage("jsbl.brandName");
		}
		
		if(wrapper.getHandlerModel() == null  ||
				(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToAgent())){

			String agentSMS=this.getMessageSource().getMessage(
					"USSD.AgentPayCashSMS",
					new Object[] { 
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
							(agentBalance==null) ? 0.0 : agentBalance},
							null);
			
			wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
			messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));
		}
		
		if(wrapper.getHandlerModel() != null && wrapper.getHandlerModel().getSmsToHandler()){

			String handlerSMS=this.getMessageSource().getMessage(
					"USSD.AgentPayCashSMS",
					new Object[] { 
							brandName,
							wrapper.getTransactionCodeModel().getCode(),
							Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
							PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
							Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
							(agentBalance==null) ? 0.0 : agentBalance},
							null);
			
//			wrapper.getTransactionDetailModel().setCustomField4(handlerSMS);
			messageList.add(new SmsMessage(wrapper.getHandlerAppUserModel().getMobileNo(), handlerSMS));
		}
		//send sms to customer

		String customerSms = this.getMessageSource().getMessage(
				"USSD.CustomerPayCashSMS",
				new Object[] {
						brandName,
						wrapper.getTransactionCodeModel().getCode(),
						Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
						brandName,
						Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
						Formatter.formatNumbers(customerBalance) 
						}, null);

		messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSms));

		wrapper.getTransactionModel().setConfirmationMessage(customerSms);

		txManager.saveTransaction(wrapper);	
		
		settlementManager.settleCommission(commissionWrapper, wrapper);

		wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
		
		return wrapper;
	}
	
	/**
	 * Update Workflow wrapper with different parameters required in further steps
	 */
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CashWithdrawalTransaction....");

		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		if (!wrapper.getIsIvrResponse()) {
			//generate new transaction code and initialize Transaction/TransctionDetailMaster objects.
			wrapper = super.doPreStart(wrapper);
		}
		else if(wrapper.getIsIvrResponse() &&
				wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED)  != null && wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED).equals("1")){
			logger.info("Check to Bypass IVR and TRANSACTION Id Vlidation for OTP Based Cash Out.");
			wrapper = super.doPreStart(wrapper);
		}
		else{
			//load existing transaction objects after IVR response
			TransactionCodeModel txCodeModel = wrapper.getTransactionCodeModel();
			baseWrapper.setBasePersistableModel(txCodeModel);
			baseWrapper = txManager.loadTransactionCodeByCode(baseWrapper);
			wrapper.setTransactionCodeModel((TransactionCodeModel)baseWrapper.getBasePersistableModel());
			txManager.generateTransactionObject(wrapper);
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			searchBaseWrapper.setBasePersistableModel(wrapper.getTransactionCodeModel());
			txManager.loadTransactionByTransactionCode(searchBaseWrapper);
			
			TransactionModel transactionModel = (TransactionModel)searchBaseWrapper.getBasePersistableModel();
			wrapper.setTransactionModel(transactionModel);
			wrapper.setTotalAmount(transactionModel.getTotalAmount());
			wrapper.setTotalCommissionAmount(transactionModel.getTotalCommissionAmount());
			wrapper.setTransactionAmount(transactionModel.getTransactionAmount());
			wrapper.setTxProcessingAmount(transactionModel.getTotalCommissionAmount());
			
			List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(transactionModel.getTransactionIdTransactionDetailModelList());
			if(transactionDetailModelList != null && transactionDetailModelList.size() > 0) {
				wrapper.setTransactionDetailModel(transactionDetailModelList.get(0));
			}
		}

		
		// For Agent Retention Commission calculation - reloading customerAppUserModel (to avoid the owning Session was closed issue)
		AppUserModel customerAppUserModel = new AppUserModel();
		customerAppUserModel.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
		customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
		customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);
		if(null != customerAppUserModel){
			wrapper.setCustomerAppUserModel(customerAppUserModel);
			wrapper.setCustomerModel(customerAppUserModel.getCustomerIdCustomerModel());
		}
		
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		//Setting Customer's User ID in Transaction Detail Master Model
		wrapper.getTransactionDetailMasterModel().setRecipientMfsId(wrapper.getUserDeviceAccountModel().getUserId());

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
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of CashWithdrawalTransaction....");

		return wrapper;
	}

	@Override
	protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
		// prepare workflow wrapper with transaction model having all required inputs
		wrapper = super.doPreProcess(wrapper);
		// agent assisted customer transaction need customer verification through IVR
		// in case of IVR response no need to perform pre-process steps
		if (wrapper.getIsIvrResponse() && wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED) != null
				&& !wrapper.getObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED).equals("1"))
			return wrapper;
		
		TransactionModel txModel = wrapper.getTransactionModel();

		txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());

		txModel.setTransactionAmount(wrapper.getTransactionAmount());
		txModel.setTotalAmount(wrapper.getTotalAmount());
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

	@Override
	protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception {
		return wrapper;
	}

	private Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		if (logger.isDebugEnabled())
			logger.debug("Inside getTransactionProcessingCharges of CashWithdrawalTransaction....");

		Double transProcessingAmount = 0D;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

		for (CommissionRateModel commissionRateModel : resultSetList) {
			if (commissionRateModel.getCommissionReasonId().longValue() != CommissionReasonConstants.EXCLUSIVE_CHARGES.longValue())
				continue;

			if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
				transProcessingAmount += commissionRateModel.getRate();
			else
				transProcessingAmount += (workFlowWrapper.getTransactionAmount() * commissionRateModel.getRate()) / 100;
		}
		
		if (logger.isDebugEnabled())
			logger.debug("Ending getTransactionProcessingCharges of CashWithdrawalTransaction....");

		return transProcessingAmount;
	}

	@Override
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
