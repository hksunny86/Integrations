package com.inov8.microbank.fonepay.commands;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.microbank.server.service.workflow.sales.SalesTransaction;
import org.springframework.context.MessageSource;

import java.util.Date;
import java.util.List;

public class FonepayTransaction extends SalesTransaction {

	private MessageSource messageSource;
	private CommissionManager commissionManager;
	private UserDeviceAccountsManager userDeviceAccountsManager;
	private NotificationMessageManager notificationMessageManager;
	private GenericDao genericDAO;
	private FinancialIntegrationManager financialIntegrationManager;
	private CustTransManager customerManager;
	private TransactionReversalManager transactionReversalManager;
	
	
	public FonepayTransaction(){
		
	}
	
	
	@Override
	protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
		if (logger.isDebugEnabled()){
			logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of FonepayTransaction....");
		}
		BaseWrapper baseWrapper = new BaseWrapperImpl();		
		wrapper = super.doPreStart(wrapper);
		UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
		userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
		userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
		baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
		baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
		wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
		
		wrapper.setPaymentModeModel(new PaymentModeModel());
		wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
		
		CustomerModel custModel = new CustomerModel();
		custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
		BaseWrapper bWrapper = new BaseWrapperImpl();
		bWrapper.setBasePersistableModel(custModel);
		bWrapper = customerManager.loadCustomer(bWrapper);
		if(null != bWrapper.getBasePersistableModel()){
			custModel = (CustomerModel) bWrapper.getBasePersistableModel();
			wrapper.setCustomerModel(custModel);
			wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
		}
		
		
		if (logger.isDebugEnabled()){
			logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of FonepayTransaction....");
		}
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
		// Populate processing Bank Id
		txModel.setProcessingBankId(BankConstantsInterface.OLA_BANK_ID);
				
		wrapper.setTransactionModel(txModel);
		
		return wrapper;
	}
	
	
	
	@Override
	protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
		
		TransactionDetailModel txDetailModel = new TransactionDetailModel();
		
		CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
		CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
  		wrapper.setCommissionAmountsHolder(commissionAmounts);

  		wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount()+commissionAmounts.getFedCommissionAmount());
  		wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

  		wrapper.getTransactionModel().setCreatedOn(new Date());
  		wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
  		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
  		txDetailModel.setProductIdProductModel(wrapper.getProductModel());
  		//txDetailModel.setConsumerNo(((CreditAdviceVO) wrapper.getProductVO()).getMobileNo());
  		txDetailModel.setSettled(true);
  		wrapper.setTransactionDetailModel(txDetailModel);
  		wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);


  		BaseWrapper baseWrapper = new BaseWrapperImpl();
  		baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel()); // Customer SMA
  		AbstractFinancialInstitution abstractFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);

  		SwitchWrapper switchWrapper = new SwitchWrapperImpl();
  		switchWrapper.setWorkFlowWrapper(wrapper);
  		switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

  		switchWrapper.getWorkFlowWrapper().setIsIvrResponse(true);
  		
  		// perform Fonepay transaction
  		switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);
  		//**************************************************************************
  		// Customer Account Number 
  		wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
  		//--------
  		wrapper.getTransactionModel().setConfirmationMessage(" _ ");
  		wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
  		wrapper.getTransactionDetailModel().setConsumerNo(wrapper.getCustomerAppUserModel().getMobileNo());

  		wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
  		wrapper.getTransactionDetailMasterModel().setTransactionAmount(wrapper.getTransactionAmount());
		wrapper.getTransactionDetailMasterModel().setFundTransferRrn(wrapper.getObject(CommandFieldConstants.KEY_RRN).toString());
		wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode(wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE).toString());
		if(!(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString().equals(FonePayConstants.APIGEE_CHANNEL)||(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString().equals(FonePayConstants.NOVA_CHANNEL)))) {
			wrapper.getTransactionDetailMasterModel().setFonepaysettelmentType(Long.parseLong(wrapper.getObject(FonePayConstants.KEY_FONEPAY_SETTLEMENT_TYPE).toString()));
		}
		if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString().equals(FonePayConstants.APIGEE_CHANNEL)){
			wrapper.getTransactionDetailMasterModel().setTerminalId(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
            if(wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE) != null){
                wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode(wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE).toString());
            }
        }
  		// customer balance
  		
  		
  		//((CreditAdviceVO)wrapper.getProductVO()).setBalance(switchWrapper.getOlavo().getFromBalanceAfterTransaction());
  		
  		wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
  		wrapper.getTransactionDetailMasterModel().setTotalAmount(commissionAmounts.getTotalAmount());
  		wrapper.getTransactionDetailMasterModel().setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(DeviceTypeConstantsInterface.WEB_SERVICE.longValue()));
  		wrapper.getTransactionModel().setDeviceTypeId(DeviceTypeConstantsInterface.WEB_SERVICE.longValue());
  		txDetailModel.setSettled(true);
  		txManager.saveTransaction(wrapper);
  		
  		wrapper.setSwitchWrapper(switchWrapper);
  		wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
  		wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getName());

  		wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED ) ;
  		wrapper.getTransactionDetailModel().setCustomField1(""+wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
  	
  		txManager.saveTransaction(wrapper);	
  		settlementManager.settleCommission(commissionWrapper, wrapper);

		wrapper.getTransactionModel().setConfirmationMessage("-");
 		
  		return wrapper;
	}
	
	
	@Override
	protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {
		
		// Validates the Customer's requirements
				if (logger.isDebugEnabled()) 
					logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of FonepayTransaction");
				
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

							
				// Validates the PaymentMode's requirements
				if (wrapper.getPaymentModeModel() == null || wrapper.getPaymentModeModel().getPaymentModeId() <= 0)
						throw new WorkFlowException("PaymentModeID is not supplied.");

				
				// ----------------------- Validates the Service's requirements
				if (wrapper.getProductModel().getServiceIdServiceModel() == null)
					throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);

				if (!wrapper.getProductModel().getServiceIdServiceModel().getActive())
					throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);

				if (logger.isDebugEnabled())
					logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of FonepayTransaction");

				return wrapper;
	}

	

	@Override
	protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
		
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
			logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of FonepayTransaction...");
		}
		
		CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
		commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
		commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
		commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
		commissionWrapper.setProductModel(wrapper.getProductModel());
		
		///*****************************************************************************
		//******************************************************************************		
		CustomerModel cust = wrapper.getCustomerModel();
        wrapper.setTaxRegimeModel(cust.getTaxRegimeIdTaxRegimeModel());
        
        
       
        //******************************************************************************
        //******************************************************************************
		
		/*RetailerContactModel retailerContactmodel = ThreadLocalAppUser.getAppUserModel().getRetailerContactIdRetailerContactModel();
	    wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());*/
		
		commissionWrapper = this.commissionManager.calculateCommission(wrapper);
		// --------------------------------------------------------------------------------------
		if (logger.isDebugEnabled())
		{
			logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of FonepayTransaction...");
		}
		return commissionWrapper;
	}
	
	public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside validateCommission of FonepayTransaction...");
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

			logger.debug("Ending validateCommission of FonepayTransaction...");
		}

	}

	
	
	
	
	public Double getTransactionProcessingCharges(
			CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
		if(logger.isDebugEnabled())
		{
			logger.debug("Inside getTransactionProcessingCharges of FonepayTransaction....");
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
			logger.debug("Ending getTransactionProcessingCharges of FonepayTransaction....");
		}
		return transProcessingAmount;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}


	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	public CommissionManager getCommissionManager() {
		return commissionManager;
	}


	public void setCommissionManager(CommissionManager commissionManager) {
		this.commissionManager = commissionManager;
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


	public void setNotificationMessageManager(
			NotificationMessageManager notificationMessageManager) {
		this.notificationMessageManager = notificationMessageManager;
	}


	public GenericDao getGenericDAO() {
		return genericDAO;
	}


	public void setGenericDAO(GenericDao genericDAO) {
		this.genericDAO = genericDAO;
	}


	public FinancialIntegrationManager getFinancialIntegrationManager() {
		return financialIntegrationManager;
	}


	public void setFinancialIntegrationManager(
			FinancialIntegrationManager financialIntegrationManager) {
		this.financialIntegrationManager = financialIntegrationManager;
	}


	public CustTransManager getCustomerManager() {
		return customerManager;
	}


	public void setCustomerManager(CustTransManager customerManager) {
		this.customerManager = customerManager;
	}


	public TransactionReversalManager getTransactionReversalManager() {
		return transactionReversalManager;
	}


	public void setTransactionReversalManager(
			TransactionReversalManager transactionReversalManager) {
		this.transactionReversalManager = transactionReversalManager;
	}
	
	
	

}
