package com.inov8.microbank.server.service.workflow.sales;

/**
 * Project Name: 			Microbank	
 * @author 					Jawwad Farooq
 * Creation Date: 			February 2008  			
 * Description:				
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CustomerModel;
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
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.common.util.ServiceTypeConstantsInterface;
import com.inov8.microbank.common.util.SmsSender;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstantsInterface;
import com.inov8.microbank.common.util.ThreadLocalAppUser;
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
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchModuleManager;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.dispenser.ProductDispenser;
import com.inov8.microbank.server.service.integration.vo.CreditCardPaymentVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.ola.integration.vo.OLAVO;

public class CreditCardBillSaleTransaction extends SalesTransaction
{

    protected final Log log = LogFactory.getLog(getClass());

    SupplierManager supplierManager;

    CommissionManager commissionManager;
    
    private SwitchModuleManager switchModuleManager;

    SettlementManager settlementManager;

    private ProductDispenseController productDispenseController;

    private SmartMoneyAccountManager smartMoneyAccountManager;

    private CustTransManager customerManager;

    private SmsSender smsSender;

    private ProductManager productManager;

    BillPaymentProductDispenser billSaleProductDispenser;

    private UserDeviceAccountsManager userDeviceAccountsManager;

    private FinancialIntegrationManager financialIntegrationManager;

    private MessageSource messageSource;

    
    
    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager)
    {
	this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public CreditCardBillSaleTransaction()
    {
    }

    /**
     * Pulls the bill information from the supplier system
     * 
     * @param wrapper
     *                WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception{
    	
    	billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
	
    	wrapper = billSaleProductDispenser.getBillInfo(wrapper);

    	return wrapper;
    }

    /**
     * Validate input from the user against the information pulled from the
     * supplier
     * 
     * @param wrapper
     *                WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper) throws FrameworkCheckedException
    {
	if (logger.isDebugEnabled())
	{
	    logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of BillSaleTransaction...");
	}
	wrapper = billSaleProductDispenser.verify(wrapper);
	if (logger.isDebugEnabled())
	{
	    logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of BillSaleTransaction...");
	}

	return wrapper;

    }

    /**
     * This method calls the commission module to calculate the commission
     * on this product and transaction.The wrapper should have
     * product,payment mode and principal amount that is passed onto the
     * commission module
     * 
     * @param wrapper
     *                WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception
    {
//    	if(ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()){
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
//		}else{
//			SegmentModel segmentModel = new SegmentModel();
//			segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
//			wrapper.setSegmentModel(segmentModel);
//		}
    	
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
	
		return commissionWrapper;
    }

    /**
     * 
     * @param commissionHolder
     *                CommissionAmountsHolder
     * @param calculatedCommissionHolder
     *                CommissionAmountsHolder
     * @throws FrameworkCheckedException
     */
    public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException{
		CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
		CreditCardPaymentVO productVO = (CreditCardPaymentVO) workFlowWrapper.getProductVO();
	
		if (productVO.getCurrentBillAmount().doubleValue() != workFlowWrapper.getBillAmount().doubleValue()){
		    throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
		}
		if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue()){
		    throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
		}
		if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue()){
		    throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
		}
		if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue()){
		    throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
		}

    }

    /**
     * This method calls the settlement module to settle the payment amounts
     * 
     * @param wrapper
     *                WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper)
    {
	return wrapper;
    }

    /**
     * This method is responsible for inserting the data into the
     * transaction tables
     * 
     * @param wrapper
     *                WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper)
    {
	return wrapper;
    }

    /**
     * This method is responsible for updating the supplier with the
     * information for example updating LESCO system with all the bill
     * collections
     * 
     * @param wrapper
     *                WorkFlowWrapper
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
     *                WorkFlowWrapper
     * @return WorkFlowWrapper
     * @throws Exception
     */

    public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception
    {

	// ------------------------Validates the Customer's requirements
	// -----------------------------------
	if (logger.isDebugEnabled())
	{
	    logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of BillSaleTransaction");
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
			if (wrapper.getSmartMoneyAccountModel().getChangePinRequired())
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_PIN_CHG_REQ);
			}
			if (!wrapper.getSmartMoneyAccountModel().getCustomerId().toString().equals(
					ThreadLocalAppUser.getAppUserModel().getCustomerId().toString()))
			{
				throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
			}
			if (wrapper.getSmartMoneyAccountModel().getPaymentModeId().longValue() == PaymentModeConstantsInterface.CREDIT_CARD
					.longValue())
			{
				throw new WorkFlowException( WorkFlowErrorCodeConstants.PHOENIX_INVALID_PAYMENT_MODE_CREDIT_CARD );
			}
			if (wrapper.getSmartMoneyAccountModel().getPaymentModeId().longValue() == PaymentModeConstantsInterface.DEBIT_CARD
					.longValue())
			{
				throw new WorkFlowException( WorkFlowErrorCodeConstants.PHOENIX_INVALID_PAYMENT_MODE_DEBIT_CARD );
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

	// -------------------------------- Validates the Product's requirements
	// ---------------------------------------------------
	if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT
		.longValue())
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

	// ----------------------- Validates the Supplier Bank Info requirements
	// -----------------
//	if (wrapper.getSupplierBankInfoModel() == null || wrapper.getSupplierBankInfoModel().getAccountNo() == null
//		|| "".equals(wrapper.getSupplierBankInfoModel().getAccountNo()))
//	{
//
//	    throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_MERCH_NO_NULL);
//	}
	// ---------------------------------------------------------------------------------------

	// ------------------------- Validates the iNPUT's requirements
	// -----------------------------------
////	if (wrapper.getBillAmount() < 0)
////	{
////
////	    throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
////	}
//	if (wrapper.getTotalAmount() < 0)
//	{
//
//	    throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
//	}
//	if (wrapper.getTotalCommissionAmount() < 0)
//	{
//
//	    throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
//	}
//	if (wrapper.getTxProcessingAmount() < 0)
//	{
//
//	    throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
//	}
//
	// ----------------------- Validates the PaymentMode's requirements
	// --------------------------------------
//	if (wrapper.getPaymentModeModel() != null)
//	{
//	    if (wrapper.getPaymentModeModel().getPaymentModeId() <= 0)
//	    {
//
//		throw new WorkFlowException("PaymentModeID is not supplied.");
//	    }
//	}

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
	    logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of BillSaleTransaction");
	}
	return wrapper;
    }

    /**
     * Method responsible for processing the Credit card Bill Sale transaction
     * 
     * @param wrapper
     *                WorkFlowWrapper
     * @return WorkFlowWrapper
     */

    public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception{
	    wrapper = this.getBillInfo(wrapper);
	    this.validateBillInfo(wrapper);
	     
	    CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
	    CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
	    this.validateCommission(commissionWrapper, wrapper);
	    
	    wrapper.setCommissionAmountsHolder(commissionAmounts);

	    TransactionDetailModel txDetailModel = new TransactionDetailModel();
	    txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
	    txDetailModel.setProductIdProductModel(wrapper.getProductModel());
	    txDetailModel.setSettled(Boolean.FALSE);
//	    txDetailModel.setConsumerNo(((BillPaymentVO) wrapper.getProductVO()).getConsumerNo());

	    wrapper.setTransactionDetailModel(txDetailModel);
	    wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
	    
	    wrapper.getTransactionModel().setConfirmationMessage(" _ ");

	    wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount());
	    wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
	    wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
	    wrapper.getTransactionModel().setBankAccountNo(StringUtil.replaceString(wrapper.getCustomerAccount().getNumber(), 5, "*"));
	    wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
	    wrapper.getTransactionModel().setCreatedOn(new Date());

	    txManager.saveTransaction(wrapper);

	    this.settlementManager.settleCommission(commissionWrapper, wrapper);

	    
	    BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
		AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

		SwitchWrapper olaSwitchWrapper = new SwitchWrapperImpl();
		olaSwitchWrapper.setWorkFlowWrapper(wrapper);
		olaSwitchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// customer SMA
		olaSwitchWrapper.setFtOrder(1);
		
		logger.info("[AccountToCashTransaction.doCreditTransfer()] Going to transfer funds from Customer Account to Credit Card Pool Account. Transaction ID: " + olaSwitchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
		olaSwitchWrapper = abstractFinancialInstitution.debitCreditAccount(olaSwitchWrapper);
		
		wrapper.setOLASwitchWrapper(olaSwitchWrapper); //setting the olaSwitchWrapper for rollback
	    wrapper.getTransactionModel().setProcessingSwitchId(olaSwitchWrapper.getSwitchSwitchModel().getSwitchId());

		double customerBalance = olaSwitchWrapper.getOlavo().getFromBalanceAfterTransaction(); // customer balance
		wrapper.getOLASwitchWrapper().setBalance(customerBalance);
		
//		((CreditCardPaymentVO)(wrapper.getProductVO())).setBalance(customerBalance);
	    
//	    String txAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount());
//	    String seviceChargesAmount=Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());
//	    
//	    if (!wrapper.getTransactionDetailModel().getConsumerNo().equalsIgnoreCase("")){
//	    	wrapper.getTransactionModel().setConfirmationMessage(
//	    			SMSUtil.buildBillSaleSMS(wrapper.getInstruction().getSmsMessageText(), wrapper
//	    					.getProductModel().getName(), txAmount, seviceChargesAmount,wrapper.getTransactionModel()
//	    					.getTransactionCodeIdTransactionCodeModel().getCode(), wrapper
//	    					.getTransactionDetailModel().getConsumerNo()));
//
//	    }else{
//	    	wrapper.getTransactionModel().setConfirmationMessage(
//	    			SMSUtil.buildVariableProductSMS(wrapper.getInstruction().getSmsMessageText(), txAmount, wrapper
//	    					.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), wrapper
//	    					.getProductModel().getHelpLineNotificationMessageModel().getSmsMessageText()));
//	    }
	    
	    

	    
	    txDetailModel.setSettled(true);
	    txManager.saveTransaction(wrapper);
	    
		ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
	    
		String custSMS = this.getMessageSource().getMessage("USSD.CustomerCreditCardPaymentSMS",
				new Object[] { 
						Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
					    StringUtil.replaceString(((CreditCardPaymentVO)wrapper.getProductVO()).getCardNumber(), 5, "*"),
						PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
						wrapper.getTransactionCodeModel().getCode(),
						customerBalance
				}, null);

		wrapper.getTransactionModel().setConfirmationMessage(custSMS);

		messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), custSMS));

	    
//	    if (!(wrapper.getCustomerAppUserModel().getMobileNo().trim().equals(wrapper.getAppUserModel().getMobileNo()))){
//	    	smsSender.send(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
//	    }else{
//	    	smsSender.sendDelayed(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), wrapper.getTransactionModel().getConfirmationMessage()));
//	    }
//	    this.settleAmount(wrapper); // settle all amounts to the respective stakeholders
	    
	    wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstantsInterface.COMPLETED);
	    txManager.saveTransaction(wrapper); // save the transaction
	    
	    wrapper = billSaleProductDispenser.doSale(wrapper);

	    wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
		
	    return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception
    {
	if (logger.isDebugEnabled())
	{
	    logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of BillSaleTransaction....");
	}

	BaseWrapper baseWrapper = new BaseWrapperImpl();
//	SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

	wrapper = super.doPreStart(wrapper);
	
	//Populate the Smart Money Account from DB
	baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
	baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
	wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
	wrapper.setOlaSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());
	
//	SwitchWrapper switchWrapper = new SwitchWrapperImpl();
//	switchWrapper.setBankId(wrapper.getSmartMoneyAccountModel().getBankId());
//	switchWrapper.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
//	switchWrapper = this.switchModuleManager.getSwitchClassPath(switchWrapper);
//	wrapper.setSwitchWrapper(switchWrapper);	
	
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

	// --Setting instruction and success Message
//	NotificationMessageModel notificationMessage = new NotificationMessageModel();
//	notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
//	baseWrapper.setBasePersistableModel(notificationMessage);
//	baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
//	wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

	// wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
	// NotificationMessageModel) baseWrapper.getBasePersistableModel());
//	NotificationMessageModel successMessage = new NotificationMessageModel();
//	successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
//	baseWrapper.setBasePersistableModel(successMessage);
//	baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
//	wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

	// Populate the Customer model from DB
	baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
	baseWrapper = customerManager.loadCustomer(baseWrapper);
	wrapper.setCustomerModel((CustomerModel) baseWrapper.getBasePersistableModel());

	

	wrapper.setPaymentModeModel(new PaymentModeModel());
	wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

	// Populate Supplier Bank Info Model
//	SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
//	supplierBankInfoModel.setSupplierId(wrapper.getProductModel().getSupplierId());
//	supplierBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
//	baseWrapper.setBasePersistableModel(supplierBankInfoModel);
//	supplierBankInfoModel = (SupplierBankInfoModel) this.supplierBankInfoManager.loadSupplierBankInfo(baseWrapper)
//		.getBasePersistableModel();
//	wrapper.setSupplierBankInfoModel(supplierBankInfoModel);

//	OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();
//
//	if (supplierBankInfoModel != null)
//	{
//	    // Populate Operator's Paying Bank Info Model
//	    operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
//	    operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
//	    operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
//	    baseWrapper.setBasePersistableModel(operatorBankInfoModel);
//	    wrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(
//		    baseWrapper).getBasePersistableModel());
//	}

//	operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
//	operatorBankInfoModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
//	operatorBankInfoModel.setBankId(wrapper.getSmartMoneyAccountModel().getBankId());
//	baseWrapper.setBasePersistableModel(operatorBankInfoModel);
//	wrapper.setOperatorBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
	if (logger.isDebugEnabled())
	{
	    logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of BillSaleTransaction....");
	}
	return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception
    {
	if (logger.isDebugEnabled())
	{
	    logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of BillSaleTransaction....");
	}
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

	txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

	// Customer model of transaction is populated
	txModel.setCustomerIdCustomerModel(wrapper.getCustomerModel());

	// Smart Money Account Id is populated
	txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

	// Payment mode model of transaction is populated
	txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

	// Customer mobile No
	txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
	txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
	txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());

	// Populate processing Bank Id
	txModel.setProcessingBankId(wrapper.getSmartMoneyAccountModel().getBankId());

	wrapper.setTransactionModel(txModel);
	if (logger.isDebugEnabled())
	{
	    logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of BillSaleTransaction....");
	}

	return wrapper;
    }

    @Override
    protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception{
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

    public void setSettlementManager(SettlementManager settlementManager)
    {
	this.settlementManager = settlementManager;
    }

    public void setCustomerManager(CustTransManager customerManager)
    {
	this.customerManager = customerManager;
    }

    public void setProductDispenseController(ProductDispenseController productDispenseController)
    {
	this.productDispenseController = productDispenseController;
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
	    logger.debug("Inside getTransactionProcessingCharges of BillSaleTransaction....");
	}
	Double transProcessingAmount = 0D;

	List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(
		CommissionConstantsInterface.COMMISSION_RATE_LIST);

	for (CommissionRateModel commissionRateModel : resultSetList)
	{
		if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.EXCLUSIVE_CHARGES ) 
	    {
		if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION
			.longValue())
		    transProcessingAmount += commissionRateModel.getRate();
		else
		    transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
	    }
	}
	if (logger.isDebugEnabled())
	{
	    logger.debug("Ending getTransactionProcessingCharges of BillSaleTransaction....");
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
	public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception{
		
		String code = wrapper.getTransactionCodeModel().getCode();
		logger.info("[CreditCardBillSaleTransaction.rollback] Rolling back Credit Card Bill Payment transaction with  ID: " + code);
		try{
			wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
			wrapper.getTransactionModel().setTransactionId(null);
			wrapper.getTransactionDetailModel().setTransactionDetailId(null);
			txManager.saveTransaction(wrapper);
		}catch(Exception ex){
			logger.error("Unable to save Credit Card Bill Payment Transaction details in case of rollback: \n"+ ex.getStackTrace());
		}
		
		if(null != wrapper.getOLASwitchWrapper()){
			logger.info("[CreditCardBillSaleTransaction.rollback] performing reversal entries in ledger. Trx ID: "+wrapper.getTransactionCodeModel().getCode()); 
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
		logger.info("[CreditCardBillSaleTransaction.rollback] rollback complete..."); 
		
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

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
    {
	this.financialIntegrationManager = financialIntegrationManager;
    }

	
	public void setSwitchModuleManager(SwitchModuleManager switchModuleManager)
	{
		this.switchModuleManager = switchModuleManager;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
