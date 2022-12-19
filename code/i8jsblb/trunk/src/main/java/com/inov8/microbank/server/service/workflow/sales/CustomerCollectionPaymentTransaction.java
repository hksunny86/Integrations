package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Omar Butt on 1/9/2018.
 */
public class CustomerCollectionPaymentTransaction extends SalesTransaction {

    protected final Log log = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    private CommissionManager commissionManager;
    private CustTransManager customerManager;
    private ProductManager productManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private ActionLogManager actionLogManager = null;
    private ESBAdapter esbAdapter;
    private TransactionReversalManager transactionReversalManager;


    private DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
    private DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");


    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
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
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());

        wrapper.setTaxRegimeModel(wrapper.getCustomerModel().getTaxRegimeIdTaxRegimeModel());

        commissionWrapper = this.commissionManager.calculateCommission(wrapper);

        return commissionWrapper;
    }

    @Override
    public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {

        BaseWrapper baseWrapper = new BaseWrapperImpl();

        _workFlowWrapper = super.doPreStart(_workFlowWrapper);

        // Populate the Product model from DB
        baseWrapper.setBasePersistableModel(_workFlowWrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        _workFlowWrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

        // For Agent Retention Commission calculation - reloading customerAppUserModel (to avoid the owning Session was closed issue)
        AppUserModel customerAppUserModel = new AppUserModel();
        customerAppUserModel.setMobileNo(_workFlowWrapper.getCustomerAppUserModel().getMobileNo());
        customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);

        if(null != customerAppUserModel){
            _workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
            _workFlowWrapper.setCustomerModel(customerAppUserModel.getCustomerIdCustomerModel());
        }

        NotificationMessageModel notificationMessage = new NotificationMessageModel();
        notificationMessage.setNotificationMessageId(_workFlowWrapper.getProductModel().getInstructionId());
        baseWrapper.setBasePersistableModel(notificationMessage);
        baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
        _workFlowWrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

        NotificationMessageModel successMessage = new NotificationMessageModel();
        successMessage.setNotificationMessageId(_workFlowWrapper.getProductModel().getSuccessMessageId());
        baseWrapper.setBasePersistableModel(successMessage);
        baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
        _workFlowWrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(_workFlowWrapper.getAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);

        _workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        _workFlowWrapper.setPaymentModeModel(new PaymentModeModel());
        _workFlowWrapper.getPaymentModeModel().setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

        return _workFlowWrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
        workFlowWrapper = super.doPreProcess(workFlowWrapper);

        TransactionModel transactionModel = workFlowWrapper.getTransactionModel();

        transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        transactionModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
        transactionModel.setProcessingBankId(workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());
        transactionModel.setTransactionAmount(workFlowWrapper.getBillAmount());
        transactionModel.setTotalAmount(workFlowWrapper.getBillAmount());
        transactionModel.setTotalCommissionAmount(0d);
        transactionModel.setDiscountAmount(0d);
        transactionModel.setTransactionTypeIdTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
        transactionModel.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
        transactionModel.setPaymentModeId(workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
        transactionModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        transactionModel.setSmartMoneyAccountId(workFlowWrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
        transactionModel.setMfsId(workFlowWrapper.getUserDeviceAccountModel().getUserId());

        workFlowWrapper.setTransactionModel(transactionModel);

        return workFlowWrapper;
    }

    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

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

        if (wrapper.getOlaSmartMoneyAccountModel() != null)
        {
            if (!wrapper.getOlaSmartMoneyAccountModel().getActive())
            {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
            }
        }
        else
        {
            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
        }

        // -------------------------------- Validates the Product's requirements
        // ---------------------------------
        if (wrapper.getProductModel() != null) {
            if (!wrapper.getProductModel().getActive()) {

                throw new WorkFlowException(
                        WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
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

        if (wrapper.getBillAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
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

        return wrapper;

    }

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper _workFlowWrapper) throws Exception {

        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        String notificationMobileNo = _workFlowWrapper.getCustomerAppUserModel().getMobileNo();
        TransactionCodeModel transactionCodeModel = _workFlowWrapper.getTransactionCodeModel();
        TransactionDetailMasterModel transactionDetailMasterModel = _workFlowWrapper.getTransactionDetailMasterModel();
        //Commenting TDM check as we are checking challan status in bill_status in Customer/CollectionPaymentCommand and info
        /*
        String workFlowConsumerNo;
        workFlowConsumerNo = (((UtilityBillVO) _workFlowWrapper.getProductVO()).getConsumerNo());

        if (workFlowConsumerNo != null && _workFlowWrapper.getProductModel().getProductCode()!=null ) {
            long count = 0;
            logger.info("CollectionPaymentTransaction:doSale:Validating Challan for consumerNo : "+workFlowConsumerNo+" productCode : "+_workFlowWrapper.getProductModel().getProductCode());
            count = transactionDetailMasterManager.getPaidChallan(workFlowConsumerNo,_workFlowWrapper.getProductModel().getProductCode());
            if(count > 0){
                if(SupplierProcessingStatusConstants.COMPLETED.equals(count))
                    throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.03"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
                else if(SupplierProcessingStatusConstants.PROCESSING.equals(count))
                    throw new CommandException("Transaction is already Processing.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
                else if(SupplierProcessingStatusConstants.IN_PROGRESS.equals(count))
                    throw new CommandException("Transaction is already in Progress.",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);
            }
        }*/
        if(transactionDetailMasterModel != null && transactionCodeModel != null)
        {
            transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.PROCESSING);
            transactionDetailMasterModel.setProcessingStatusName(SupplierProcessingStatusConstants.PROCESSING_NAME);
            transactionDetailMasterModel.setTransactionCode(transactionCodeModel.getCode());
            transactionDetailMasterModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
            transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
            transactionDetailMasterModel = (TransactionDetailMasterModel) baseWrapper.getBasePersistableModel();
            _workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
        }
        CommissionWrapper commissionWrapper = this.calculateCommission(_workFlowWrapper);
        CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

        _workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
        _workFlowWrapper.setCommissionWrapper(commissionWrapper);

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());

        OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        _workFlowWrapper.setCommissionWrapper(commissionWrapper);
        _workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);

        _workFlowWrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        _workFlowWrapper.getTransactionModel().setCreatedOn(new Date());
        _workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);

        txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
        txDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
        txDetailModel.setConsumerNo(((UtilityBillVO) _workFlowWrapper.getProductVO()).getConsumerNo());
        txDetailModel.setSettled(Boolean.TRUE);

        _workFlowWrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
        _workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
        _workFlowWrapper.getTransactionModel().setSaleMobileNo(notificationMobileNo);

        _workFlowWrapper.setTransactionDetailModel(txDetailModel);
        _workFlowWrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
        _workFlowWrapper.getTransactionModel().setConfirmationMessage(" _ ");

        txManager.saveTransaction(_workFlowWrapper);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
        switchWrapper.getWorkFlowWrapper().setIsIvrResponse(true);

        olaVeriflyFinancialInstitution.customercollectionPayment(switchWrapper);
        _workFlowWrapper.setOLASwitchWrapper(switchWrapper);

        this.sendSms(_workFlowWrapper);

        settlementManager.settleCommission(commissionWrapper, _workFlowWrapper);

        txManager.saveTransaction(_workFlowWrapper); // save the transaction



        Date creditAccountAdviceDateTime = new Date();

        MiddlewareAdviceVO middlewareAdviceVO = new MiddlewareAdviceVO();
        middlewareAdviceVO.setAccountNo1(notificationMobileNo);
        middlewareAdviceVO.setAccountNo2(switchWrapper.getToAccountNo());
        middlewareAdviceVO.setTransactionAmount(switchWrapper.getTransactionAmount().toString());
        middlewareAdviceVO.setMicrobankTransactionCode(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode());
        middlewareAdviceVO.setConsumerNo(_workFlowWrapper.getTransactionDetailModel().getConsumerNo());
        middlewareAdviceVO.setStan(String.valueOf((100000 + new Random().nextInt(900000)))+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        middlewareAdviceVO.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
        middlewareAdviceVO.setMicrobankTransactionCodeId(switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getTransactionCodeId());

//        middlewareAdviceVO.setStan(_workFlowWrapper.get);
//        middlewareAdviceVO.setRetrievalReferenceNumber(this.getRetrievalReferenceNumber(middlewareAdviceVO.getStan()));
        middlewareAdviceVO.setRequestTime(creditAccountAdviceDateTime);
        middlewareAdviceVO.setDateTimeLocalTransaction(creditAccountAdviceDateTime);
        middlewareAdviceVO.setTransmissionTime(creditAccountAdviceDateTime);
        middlewareAdviceVO.setAdviceType(CoreAdviceUtil.CHALLAN_COLLECTION_ADVICE);

        //Product model is null in case of manual adjustment
        if(switchWrapper.getWorkFlowWrapper().getProductModel() != null){
            middlewareAdviceVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        }

        if( switchWrapper.getTransactionTransactionModel() != null && switchWrapper.getTransactionTransactionModel().getTransactionId() != null){
            middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", switchWrapper.getTransactionTransactionModel().getTransactionId());
        }

        transactionReversalManager.makeCoreCreditAdvice(middlewareAdviceVO);

/*
        /*/
/***************************************************
        // Calling ESBAdaptor for Collection Payment - start
        /*/
/***************************************************
        String billAmount = Formatter.formatDouble(_workFlowWrapper.getBillAmount());
        String consumerNo = _workFlowWrapper.getTransactionDetailModel().getConsumerNo();
        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareCollectionPaymentRequest(consumerNo,billAmount);
        SwitchWrapper i8sbSwitchWrapper = new SwitchWrapperImpl();
        i8sbSwitchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

        i8sbSwitchWrapper = this.esbAdapter.makeI8SBCall(i8sbSwitchWrapper);

        I8SBSwitchControllerResponseVO responseVO = i8sbSwitchWrapper.getI8SBSwitchControllerResponseVO();

        ESBAdapter.processI8sbResponseCode(responseVO, false); //throws WorkFlowException incase of error response code
*/

        //Calling ESBAdaptor for Collection Payment - end

        return _workFlowWrapper;

    }

    private void sendSms(WorkFlowWrapper _workFlowWrapper) throws FrameworkCheckedException {

        String customerMsgString = null;
        Object[] customerSMSParam = null;

/*
        String brandName = MessageUtil.getMessage("jsbl.brandName");
        String trxCode = _workFlowWrapper.getTransactionCodeModel().getCode();
        String totalAmount = Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTotalAmount());
        String customerBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
        String productName = _workFlowWrapper.getProductModel().getName();
        String consumer = _workFlowWrapper.getTransactionDetailModel().getConsumerNo();
        String date = dtf.print(new DateTime());
        String time = tf.print(new LocalTime());
        customerSMSParam = new Object[] {brandName,trxCode,productName,consumer,totalAmount,time,date,customerBalance};
         */
        String trxCode = _workFlowWrapper.getTransactionCodeModel().getCode();
        String totalAmount = Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTotalAmount());
        String customerCharges = Formatter.formatDouble(_workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges());
        String customerBalance = Formatter.formatDouble(_workFlowWrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
        String productName = _workFlowWrapper.getProductModel().getName();
        String date = dtf.print(new DateTime());
        String time = tf.print(new LocalTime());

        customerMsgString = "collection.payment.customer";
        customerSMSParam = new Object[] {trxCode,totalAmount,productName,date,time,customerCharges,customerBalance};

        String customerSMS = this.getMessageSource().getMessage(customerMsgString, customerSMSParam, null);

        SmsMessage customerSMSMessage = new SmsMessage(_workFlowWrapper.getCustomerAppUserModel().getMobileNo(), customerSMS);

        _workFlowWrapper.getTransactionDetailModel().setCustomField8(customerSMS);
        _workFlowWrapper.getTransactionModel().setNotificationMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());
        _workFlowWrapper.getTransactionModel().setConfirmationMessage(customerSMS);

        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
        messageList.add(customerSMSMessage);

        _workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
    }

    protected Long logActionLogModel() {

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setAppUserIdAppUserModel(ThreadLocalAppUser.getAppUserModel());
        actionLogModel.setActionStatusId(PortalConstants.ACTION_STATUS_END);
        actionLogModel.setEndTime(new Timestamp(new Date().getTime()));

        BaseWrapper baseWrapperActionLog = new BaseWrapperImpl();

        baseWrapperActionLog.setBasePersistableModel(actionLogModel);

        try {

            baseWrapperActionLog = actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapperActionLog);

        } catch (FrameworkCheckedException e) {
            logger.error(e.getLocalizedMessage());
        }

        if(baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {

            actionLogModel = (ActionLogModel) baseWrapperActionLog.getBasePersistableModel();
        }

        return actionLogModel.getActionLogId();
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
    public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws Exception {
        return workFlowWrapper;
    }

//	DEPENDANCY INJECTION (IOC)
    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)	{
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }
    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }
    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }
    public void setCustomerManager(CustTransManager customerManager) {
        this.customerManager = customerManager;
    }
    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }
    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }


    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }


}
