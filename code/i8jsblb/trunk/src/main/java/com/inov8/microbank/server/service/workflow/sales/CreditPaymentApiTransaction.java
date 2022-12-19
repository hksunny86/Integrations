package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
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
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreditPaymentApiTransaction extends SalesTransaction {

    protected final Log log = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");


    SupplierManager supplierManager;

    CommissionManager commissionManager;

    private ProductDispenseController productDispenseController;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private CustTransManager customerManager;
    private ProductManager productManager;
    private VeriflyManagerService veriflyController;
    private SwitchController switchController;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private SupplierBankInfoManager supplierBankInfoManager;
    private OperatorManager operatorManager;
    private RetailerContactManager retailerContactManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private GenericDao genericDAO;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private TransactionReversalManager transactionReversalManager;

//	private String rrnPrefix;


    public void setNotificationMessageManager(
            NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setUserDeviceAccountsManager(
            UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public CreditPaymentApiTransaction() {
    }

    /**
     * Pulls the bill information from the supplier system
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside getBillInfo(WorkFlowWrapper wrapper) of CashDepositTransaction...");
            logger.debug("Loading ProductDispenser...");
        }
        BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
        logger.debug("Fetching Bill Info through Product Dispenser...");
        wrapper = billSaleProductDispenser.getBillInfo(wrapper);


        return wrapper;
    }

    /**
     * Validate input from the user against the information pulled from the
     * supplier
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper)
            throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of CashDepositTransaction...");
        }
        BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
        wrapper = billSaleProductDispenser.verify(wrapper);
        if (logger.isDebugEnabled()) {
            logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of CashDepositTransaction...");
        }

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
    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
        // ------ Calculate the commission
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());
        if (null != wrapper.getCustomerModel()) {
            SegmentModel segmentModel = new SegmentModel();
            segmentModel = wrapper.getCustomerModel().getSegmentIdSegmentModel();
            wrapper.setSegmentModel(segmentModel);
        }

        commissionWrapper = this.commissionManager.calculateCommission(wrapper);
        return commissionWrapper;
    }


    public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside validateCommission of CashDepositTransaction...");
        }
        CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
                CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
        CashInVO productVO = (CashInVO) workFlowWrapper.getProductVO();

        if (productVO.getTransactionAmount().doubleValue() != workFlowWrapper.getTransactionAmount().doubleValue()) {

            throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
        }
        if (commissionHolder.getTotalCommissionAmount().doubleValue() != workFlowWrapper.getTotalCommissionAmount().doubleValue()) {

            throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
        }
        if (commissionHolder.getTotalAmount().doubleValue() != workFlowWrapper.getTotalAmount().doubleValue()) {

            throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
        }
        if (null != userDeviceAccountsModel && userDeviceAccountsModel.getCommissioned()) {
            if (commissionHolder.getTotalAmount().doubleValue() < workFlowWrapper.getDiscountAmount()) {

                throw new WorkFlowException(WorkFlowErrorCodeConstants.DISCOUNT_AMOUNT_EXCEEDS_PRICE);
            }
        }

        if (this.getTransactionProcessingCharges(commissionWrapper, workFlowWrapper).doubleValue() != workFlowWrapper.getTxProcessingAmount().doubleValue()) {

            throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NOT_MATCHED);
        }
        if (logger.isDebugEnabled()) {

            logger.debug("Ending validateCommission of CreditPaymentApiTransaction...");
        }

    }

    /**
     * This method calls the settlement module to settle the payment amounts
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

        // ------------------------Validates the Customer's requirements
        // -----------------------------------
        if (logger.isDebugEnabled()) {
            logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CreditPaymentApiTransaction");
        }
        if (wrapper.getAppUserModel() != null) {
            if ("".equals(wrapper.getAppUserModel().getMobileNo())) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
            }
        } else {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
        }
        if (wrapper.getUserDeviceAccountModel() == null) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
        }

        if (wrapper.getOlaSmartMoneyAccountModel() != null) {
            if (!wrapper.getOlaSmartMoneyAccountModel().getActive()) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
            }
        } else {
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
            if (!wrapper.getProductModel().getActive()) {
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


        // ------------------------- Validates the iNPUT's requirements
        // -----------------------------------
        if (wrapper.getTransactionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
        }
        if (null != wrapper.getTotalAmount() && wrapper.getTotalAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
        }
        if (null != wrapper.getTotalCommissionAmount() && wrapper.getTotalCommissionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
        }
        if (null != wrapper.getTxProcessingAmount() && wrapper.getTxProcessingAmount() < 0) {

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

        if (logger.isDebugEnabled()) {
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CreditPaymentApiTransaction");
        }
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        {
            if (logger.isDebugEnabled()) {
                logger.debug("Inside doSale(WorkFlowWrapper wrapper) of CreditPaymentApiTransaction..");
            }

            wrapper.getTransactionModel().setConfirmationMessage(" _ ");
            wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());


            // one coming from iPos to see if they match
            CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);

            //commissionWrapper.get
            CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
                    CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

            wrapper.setCommissionAmountsHolder(commissionAmounts);
            wrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, commissionAmounts.getTransactionProcessingAmount());
            wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
            wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
            if (wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString().equals(FonePayConstants.APIGEE_CHANNEL)) {
                wrapper.getTransactionDetailMasterModel().setTerminalId(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
                wrapper.getTransactionDetailMasterModel().setChannelId(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString());
                if (wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE) != null) {
                    wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode((String) wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE));
                }
                if (wrapper.getObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME) != null) {
                    wrapper.getTransactionDetailMasterModel().setExternalProductName((String) wrapper.getObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME));
                }
            }
            txDetailModel.setActualBillableAmount(commissionAmounts.getTransactionAmount());
            txDetailModel.setProductIdProductModel(wrapper.getProductModel());
            txDetailModel.setConsumerNo(wrapper.getCustomerAppUserModel().getMobileNo());
            wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

            txDetailModel.setSettled(false);

            wrapper.setTransactionDetailModel(txDetailModel);
            wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
            txManager.saveTransaction(wrapper); // save the transaction

            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setWorkFlowWrapper(wrapper);
            switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());

            wrapper = dispenseCustomer(wrapper);
            switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
            /*            switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);*/
            switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
            switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount() - switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
            switchWrapper.setTransactionTypeId(wrapper.getTransactionTypeModel().getTransactionTypeId());
            wrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(wrapper.getOLASwitchWrapper().getSwitchSwitchModel());
            wrapper.getTransactionModel().setBankAccountNo(wrapper.getOLASwitchWrapper().getFromAccountNo());

            wrapper.getTransactionModel().setCustomerMobileNo(null);

/*            if(wrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction() == null) {
                wrapper.getOLASwitchWrapper().getOlavo().setFromBalanceAfterTransaction(0D);
            }

            switchWrapper.setAgentBalance(wrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction());*/
            wrapper.setSwitchWrapper(switchWrapper);

            txDetailModel.setSettled(true);
            wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
            txManager.saveTransaction(wrapper); // save the transaction

            this.settlementManager.settleCommission(commissionWrapper, wrapper);
            //*****************************************************************
            //****  Update status of from 'Pushed to SAF' to 'Successful'  ****
            //*****************************************************************
                transactionReversalManager.updateIBFTStatus(wrapper.getObject(CommandFieldConstants.KEY_STAN).toString(),
                        (Date) wrapper.getObject(CommandFieldConstants.KEY_TX_DATE),
                        PortalConstants.IBFT_STATUS_SUCCESS,
                        wrapper.getTransactionCodeModel().getCode());


            sendCashDepositSMS(wrapper);

        }

        return wrapper;
    }

    private void sendCashDepositSMS(WorkFlowWrapper wrapper) throws Exception {

        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
//        Double agentBalance = ((CashInVO)wrapper.getProductVO()).getAgentBalance();
        String customerSMS = "";
        if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID) != null && wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).equals("NOVA")) {
            String pNames = MessageUtil.getMessage("nova.Transfer.product.ids");
            List<String> pItems = Arrays.asList(pNames.split("\\s*,\\s*"));

            String otherProductNames = MessageUtil.getMessage("nova.Receiver.Transfer.product.ids");
            List<String> otherProductItems = Arrays.asList(otherProductNames.split("\\s*,\\s*"));

            if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CASH_BACK)) {
//                String senderAccNumber = wrapper.getOLASwitchWrapper().getFromAccountNo();
                customerSMS = this.getMessageSource().getMessage(
                        "nova.CashBackSMS",
                        new Object[] {
                                wrapper.getTransactionCodeModel().getCode(),
                                Formatter.formatDouble(wrapper.getTransactionAmount()),
                                tf.print(new LocalTime()),
                                dtf.print(new DateTime()),
                                Formatter.formatDouble(wrapper.getOLASwitchWrapper().getOlavo().getToBalanceAfterTransaction())
                        },
                        null);

                messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSMS));

                wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
                wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
            }

            if (pItems.contains(wrapper.getProductModel().getProductId().toString())) {
                customerSMS = this.getMessageSource().getMessage(
                        "USSD.CustomerCashDepositSMS",
                        new Object[]{
                                Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                                wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
                        },
                        null);

                messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSMS));

                wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
                wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
            }

            if (otherProductItems.contains(wrapper.getProductModel().getProductId().toString())) {
                customerSMS = this.getMessageSource().getMessage(
                        "nova.ReceiverSMS",
                        new Object[]{
                                wrapper.getTransactionCodeModel().getCode(),
                                Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                                wrapper.getProductModel().getName(),
                                tf.print(new LocalTime()),
                                Formatter.formatDouble(wrapper.getOLASwitchWrapper().getOlavo().getToBalanceAfterTransaction())
                        },
                        null);

                messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSMS));

                wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
                wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
            }

        }
        else {
            customerSMS = "";
            String brandName = null;
            if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
                brandName = MessageUtil.getMessage("sco.brandName");
            } else {
                brandName = MessageUtil.getMessage("jsbl.brandName");
            }
            if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CORE_TO_WALLET_MB)) {
                String senderAccNumber = wrapper.getOLASwitchWrapper().getFromAccountNo();
                customerSMS = this.getMessageSource().getMessage(
                        "coreToWallet.recipient.sms",
                        new Object[] {
                                wrapper.getTransactionCodeModel().getCode(),
                                Formatter.formatDouble(wrapper.getTransactionAmount()),
                                tf.print(new LocalTime()),
                                dtf.print(new DateTime()),
                                senderAccNumber,
                                Formatter.formatDouble(wrapper.getOLASwitchWrapper().getOlavo().getToBalanceAfterTransaction())
                        },
                        null);
            }
            else {
                Double charges = wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
                customerSMS = this.getMessageSource().getMessage(
                        "USSD.CustomerCashDepositSMS",
                        new Object[]{
                                brandName,
                                wrapper.getTransactionCodeModel().getCode(),
                                Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                                tf.print(new LocalTime()),
                                dtf.print(new DateTime()),
                                brandName,
                                Formatter.formatDouble(charges),
                                Formatter.formatDouble(wrapper.getOLASwitchWrapper().getOlavo().getToBalanceAfterTransaction())
                        },
                        null);
            }
            messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSMS));

            wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
            wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
        }
    }

    @Override
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper) {
        return wrapper;
    }


    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
            throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CreditPaymentApiTransaction....");
        }

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        wrapper = super.doPreStart(wrapper);

        // Populate the Product model from DB
        baseWrapper.setBasePersistableModel(wrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

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

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        Long paymentModeId = null;
        paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
        sma.setPaymentModeId(paymentModeId);
        baseWrapper.setBasePersistableModel(sma);
        sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(ThreadLocalAppUser.getAppUserModel(), sma.getPaymentModeId());
        wrapper.setSmartMoneyAccountModel(sma);

        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        appUserModel = appUserManager.getAppUserModel(appUserModel);
        if (null != appUserModel) {
            wrapper.setCustomerAppUserModel(appUserModel);
            wrapper.setCustomerModel(appUserModel.getCustomerIdCustomerModel());
        }
        // Populate the Customer OLA Smart Money Account from DB

        sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(wrapper.getCustomerAppUserModel(), paymentModeId);
        //Commented By Sheheryaar
        /*String paymentMode=wrapper.getObject(CommandFieldConstants.KEY_PAYMENT_MODE).toString();
        sma = new SmartMoneyAccountModel();
        if(paymentMode.equals("HRA"))
            sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(wrapper.getCustomerAppUserModel(),7L);
        else
            sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(wrapper.getCustomerAppUserModel(),3L);*/

        wrapper.setOlaSmartMoneyAccountModel(sma);

        logger.info("[CreditPaymentApiTransaction.doPreStart]End loading business object models...");

        return wrapper;
    }


    public WorkFlowWrapper dispenseCustomer(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {

        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            SmartMoneyAccountModel customerSMA = workFlowWrapper.getOlaSmartMoneyAccountModel();
            SmartMoneyAccountModel agentSMA = workFlowWrapper.getSmartMoneyAccountModel();

            if (null != customerSMA) {
                //Set agent smartmoneyAccount to load financialInstitution
                baseWrapper.setBasePersistableModel(agentSMA);
                AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

                SwitchWrapper switchWrapper = new SwitchWrapperImpl();

                switchWrapper.setBasePersistableModel(agentSMA);

                switchWrapper.setWorkFlowWrapper(workFlowWrapper);
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1("" + customerSMA.getSmartMoneyAccountId()); //set customer's smart money account ID
                TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel();

                logger.info("CreditPaymentApiTransaction.dispenseCustomer] Going to make FT at OLA from Agent A/C to Customer A/C." +
                        " LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                        " Trx ID:" + workFlowWrapper.getTransactionCodeModel().getCode());
                switchWrapper = abstractFinancialInstitution.creditPayment(switchWrapper);
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField3("" + switchWrapper.getSwitchSwitchModel().getSwitchId());
                workFlowWrapper.setOLASwitchWrapper(switchWrapper); // setting the switchWrapper for rollback

                // set customer balance after transaction
                Double customerBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction();


                workFlowWrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
                switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp);

            }
        } catch (Exception e) {
            if (e instanceof CommandException) {
                throw new WorkFlowException(e.getMessage());
            } else if (e instanceof WorkFlowException) {
                throw (WorkFlowException) e;
            } else {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
            }
        }
        return workFlowWrapper;

    }


    protected WorkFlowWrapper doPreEnd(WorkFlowWrapper wrapper) throws Exception {
        updateTransactionDetailMasterForTransaction(wrapper);

        Boolean isIvrResp = wrapper.getIsIvrResponse();

        if ((isIvrResp == null || !isIvrResp) && wrapper.getTransactionModel().getSupProcessingStatusId().longValue() == SupplierProcessingStatusConstants.IVR_VALIDATION_PENDING.longValue()) {
            return wrapper;
        }

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        settlementManager.prepareDataForDayEndSettlement(wrapper);
//        Long utilityBillPoolAccountId = null;
//        List<StakeholderBankInfoModel> stakeholderBankInfoModelList = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(wrapper.getProductModel().getProductId(), "OF_SET");
//        if (stakeholderBankInfoModelList != null) {
//            utilityBillPoolAccountId = Long.valueOf(stakeholderBankInfoModelList.get(0).getStakeholderBankInfoId());
//        }
//        prepareAndSaveSettlementTransactionRDV(wrapper.getTransactionModel().getTransactionId(),
//                wrapper.getProductModel().getProductId(),
//                wrapper.getSwitchWrapper().getTransactionAmount(),
//                PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
//                utilityBillPoolAccountId);

        return wrapper;
    }


    private void prepareAndSaveSettlementTransactionRDV(Long transactionId, Long productId, Double amount, Long fromAccountInfoId, Long toAccountInfoId) throws Exception {

        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
        SettlementTransactionModel settlementModel = new SettlementTransactionModel();
        settlementModel.setTransactionID(transactionId);
        settlementModel.setProductID(productId);
        settlementModel.setCreatedBy(appUserModel.getAppUserId());
        settlementModel.setUpdatedBy(appUserModel.getAppUserId());
        settlementModel.setCreatedOn(new Date());
        settlementModel.setUpdatedOn(new Date());
        settlementModel.setStatus(0L);
        settlementModel.setFromBankInfoID(fromAccountInfoId);
        settlementModel.setToBankInfoID(toAccountInfoId);
        settlementModel.setAmount(amount);

        this.settlementManager.saveSettlementTransactionModel(settlementModel);

    }





    private void updateTransactionDetailMasterForTransaction(WorkFlowWrapper wrapper) throws Exception {

        logger.info("[TransactionProcessor.updateTransactionDetailMasterForTransaction] Updating Transaction Detail Master.");
        wrapper = this.prepareTrxDetailMasterModelForAgentNetworkChange(wrapper);
        TransactionDetailMasterModel txDetailMaster = wrapper.getTransactionDetailMasterModel();
        if (wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID) != null && !wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).equals(""))
            txDetailMaster.setTerminalId((String) wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID));
        // Skip in case of Commission Settlement Transaction type
        if (wrapper.getTransactionTypeModel() != null & wrapper.getTransactionTypeModel().getTransactionTypeId().longValue() != TransactionTypeConstantsInterface.COMMISSION_SETTLEMENT) {

            //if :new.transaction_type_id <> 19 then
            if (wrapper.getProductModel() != null && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CASH_TRANSFER
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.IBFT
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.WEB_SERVICE_CASH_IN) {

                txDetailMaster.setMfsId("");
                txDetailMaster.setSaleMobileNo("");

                if (UtilityCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                        InternetCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId())) ||
                        NadraCompanyEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                        || OtherThanOneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))
                        || OneBillProductEnum.contains(String.valueOf(wrapper.getProductModel().getProductId()))) {

                    txDetailMaster.setSenderCnic(wrapper.getCustomerAppUserModel().getNic());
                    txDetailMaster.setSaleMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());

                } else if (wrapper.getTransactionModel().getSaleMobileNo() != null) {
                    Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER};
                    AppUserModel appUser = appUserManager.loadAppUserByMobileAndType(wrapper.getTransactionModel().getSaleMobileNo(), appUserTypeIds);

                    if (appUser != null) {
                        txDetailMaster.setSenderCnic("");
                    }
                }

                if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.longValue()) {

                    txDetailMaster.setSenderCnic(wrapper.getWalkInCustomerCNIC());
                }
                if (wrapper.getAppUserModel() != null &&
                        (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX) || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.VIRTUAL_CARD_PAYMENT_TX))) {
                    txDetailMaster.setMfsId("");
                    txDetailMaster.setSenderCnic("");
                    txDetailMaster.setSaleMobileNo("");
                }

                if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());

                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CNIC).toString());
                } else if (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX)) {
                    txDetailMaster.setRecipientMfsId("");
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE) != null)
                        txDetailMaster.setRecipientMobileNo(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
                }
            }

            //if :new.transaction_type_id not in (19, 20,27) then
            if (wrapper.getTransactionTypeModel() != null && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.CASH_TO_CASH
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.ACCOUNT_TO_CASH_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.BB_TO_CORE_ACCOUNT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.CUSTOMER_INITIATED_BB_TO_CORE_ACCOUNT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.SENDER_REDEEM_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.CUSTOMER_COLLECTION_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.COLLECTION_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.SENDER_REDEEM_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.DONATION_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX
                    && wrapper.getTransactionTypeModel().getTransactionTypeId() != TransactionTypeConstantsInterface.VIRTUAL_CARD_PAYMENT_TX
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_CASH_OUT_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.PROOF_OF_LIFE_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.ADVANCE_SALARY_LOAN_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.AGENT_IBFT_TX)
                    && !wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.BOP_CARD_ISSUANCE_REISSUANCE_TX)
                    ) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionModel().getNotificationMobileNo());

                if (wrapper.getTransactionModel().getNotificationMobileNo() != null) {
                    Long[] appUserTypeIds = {UserTypeConstantsInterface.CUSTOMER, UserTypeConstantsInterface.RETAILER};
                    AppUserModel appUser = appUserManager.loadAppUserByMobileAndType(wrapper.getTransactionModel().getNotificationMobileNo(), appUserTypeIds);

                    if (appUser != null) {
                        txDetailMaster.setRecipientCnic(appUser.getNic());
                        UserDeviceAccountsModel userDeviceModel = null;
                        for (UserDeviceAccountsModel model : appUser.getAppUserIdUserDeviceAccountsModelList()) {
                            userDeviceModel = model;
                            break;
                        }

                        if (userDeviceModel != null) {
                            txDetailMaster.setRecipientMfsId(userDeviceModel.getUserId());
                        }
                    }
                }
            }

            txDetailMaster.setTransactionId(wrapper.getTransactionModel().getTransactionId());
            txDetailMaster.setAuthorizationCode(wrapper.getTransactionModel().getBankResponseCode());
            if (!StringUtil.isNullOrEmpty(wrapper.getTransactionModel().getBankAccountNo())) {
                txDetailMaster.setBankAccountNo(wrapper.getTransactionModel().getBankAccountNo());
                txDetailMaster.setBankAccountNoLastFive(StringUtil.getLastFiveDigitsFromAccountNo(wrapper.getTransactionModel().getBankAccountNo()));
            }
            txDetailMaster.setUpdatedOn(wrapper.getTransactionModel().getUpdatedOn());
            txDetailMaster.setPaymentModeId(wrapper.getTransactionModel().getPaymentModeId());
            if (wrapper.getTransactionModel().getPaymentModeId() != null) {
                txDetailMaster.setPaymentMode(PaymentModeConstantsInterface.paymentModeConstantsMap.get(wrapper.getTransactionModel().getPaymentModeId()));
            }
            txDetailMaster.setBankId(wrapper.getTransactionModel().getProcessingBankId());
            txDetailMaster.setBankName(wrapper.getTransactionModel().getProcessingBankIdBankModel().getName());
            txDetailMaster.setTransactionAmount(wrapper.getTransactionModel().getTransactionAmount());
            txDetailMaster.setTotalAmount(wrapper.getTransactionModel().getTotalAmount());
            txDetailMaster.setMfsId("");
            txDetailMaster.setSupProcessingStatusId(wrapper.getTransactionModel().getSupProcessingStatusId());
            txDetailMaster.setProcessingStatusName(SupplierProcessingStatusConstants.processingStatusNamesMap.get(wrapper.getTransactionModel().getSupProcessingStatusId()));
            txDetailMaster.setSenderDeviceTypeId(wrapper.getTransactionModel().getDeviceTypeId());
            if (wrapper.getTransactionModel().getDeviceTypeId() != null) {
                txDetailMaster.setDeviceType(DeviceTypeConstantsInterface.DEVICE_TYPES_MAP.get(wrapper.getTransactionModel().getDeviceTypeId()));
            }

            if (wrapper.getTransactionModel().getSmartMoneyAccountId() != null) {
                txDetailMaster.setSenderAccountNick(transactionDetailMasterManager.loadSmartMoneyAccountNickBySmartMoneyAccountId(wrapper.getTransactionModel().getSmartMoneyAccountId()));
            }

            if (wrapper.getTransactionModel().getFromRetContactId() != null) {
                txDetailMaster.setAgent1Id(transactionDetailMasterManager.loadAgentId(wrapper.getTransactionModel().getFromRetContactId()));
            }

            if (wrapper.getTransactionModel().getToRetContactId() != null &&
                    wrapper.getProductModel() != null &&
                    wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.AGENT_TO_AGENT_TRANSFER &&
                    wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.RETAIL_PAYMENT) {

                txDetailMaster.setAgent2Id(transactionDetailMasterManager.loadAgentId(wrapper.getTransactionModel().getToRetContactId()));
            }
            if (wrapper.getTransactionModel().getFromRetContactId() != null) {
                txDetailMaster.setAgent1MobileNo(transactionDetailMasterManager.getAgentMobileByRetailerContactId(wrapper.getTransactionModel().getFromRetContactId()));
            }

        }

        ServiceModel serviceModel = wrapper.getProductModel().getServiceIdServiceModel();
        if (serviceModel != null) {
            txDetailMaster.setServiceId(serviceModel.getServiceId());
            txDetailMaster.setServiceName(serviceModel.getName());
        }

        //Transaction Detail Fields updated
        if (wrapper.getProductModel() != null && wrapper.getProductModel().getProductId().longValue() != 2510734L) {//Only God knows why this product id is used. but i m following it :(

//		  IF :new.product_id in (2510791,2510795,2510797) THEN
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.APOTHECARE
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ML_TRANSFER_TO_RETAILER
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ML_TRANSFER_TO_CUSTOMER) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField6());
            }

//		  IF CRS.SUPPLIER_ID=50063 or :new.product_id in (2510793,2510794,2510796) THEN
            ProductModel productModel = transactionDetailMasterManager.getProductModelByProductId(wrapper.getTransactionDetailModel().getProductId());
//            if (wrapper.getProductModel().getSupplierId().longValue() == SupplierConstants.TransReportPhonixCSRViewSupplierID
//                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.APOTHECARE_PAYMENT
//                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT_PAYMENT
//                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA_PAYMENT) {

                txDetailMaster.setConsumerNo(wrapper.getTransactionDetailModel().getConsumerNo());
                if (wrapper.getAppUserModel() != null) {
                    txDetailMaster.setRecipientMfsId(wrapper.getUserDeviceAccountModel().getUserId());
                    txDetailMaster.setRecipientCnic(wrapper.getAppUserModel().getNic());
                    txDetailMaster.setRecipientMobileNo(wrapper.getAppUserModel().getMobileNo());
                }
//            }

            //In order to show challan number on transaction report in case of challan payment
            if (wrapper.getProductModel().getServiceId().longValue() == ServiceConstantsInterface.COLLECTION_PAYMENT) {
                txDetailMaster.setConsumerNo(wrapper.getTransactionDetailModel().getConsumerNo());
            }
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.HRA_CASH_WITHDRAWAL) {
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT) {

                txDetailMaster.setSenderBVS(wrapper.isSenderBvs());

            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BISP_CASH_OUT ||
                    wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BISP_CASH_OUT_WALLET) {
                txDetailMaster.setSenderBVS(wrapper.isSenderBvs());
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.THIRD_PARTY_ACCOUNT_OPENING) {
                txDetailMaster.setReceiverBVS(Boolean.TRUE);
            }
            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.PROOF_OF_LIFE) {
                txDetailMaster.setReceiverBVS(Boolean.TRUE);
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BOP_CASH_OUT) {
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT) {
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionDetailModel().getCustomField6());
                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_DEPOSIT) {

                txDetailMaster.setCashDepositorCnic(wrapper.getTransactionDetailModel().getCustomField7());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CASH_TRANSFER) {

                txDetailMaster.setMfsId(null);
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionDetailModel().getCustomField6());
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());
                txDetailMaster.setRecipientCnic(wrapper.getTransactionDetailModel().getCustomField9());
                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setRecipientMfsId(null);

                if (wrapper.isLeg2Transaction())
                    txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
                else
                    txDetailMaster.setSenderBVS(wrapper.isSenderBvs());

            }

            if (wrapper.getProductModel().getServiceId() == ServiceConstantsInterface.BULK_DISB_NON_ACC_HOLDER.longValue()) {

                txDetailMaster.setMfsId(null);
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());
                txDetailMaster.setRecipientCnic(wrapper.getTransactionDetailModel().getCustomField9());
                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setRecipientMfsId(null);

            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACCOUNT_TO_CASH || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_CASH_CI) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setRecipientMfsId(null);
                txDetailMaster.setRecipientCnic(wrapper.getTransactionDetailModel().getCustomField9());
                txDetailMaster.setReceiverBVS(wrapper.isReceiverBvs());
            }


            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.ACT_TO_ACT) {

                txDetailMaster.setRecipientMobileNo(wrapper.getReceiverAppUserModel().getMobileNo());
            }

            if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.BB_TO_CORE_ACCOUNT) {

                txDetailMaster.setRecipientMobileNo(wrapper.getTransactionDetailModel().getCustomField5());
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionModel().getNotificationMobileNo());
                txDetailMaster.setSenderCnic(wrapper.getTransactionDetailModel().getCustomField7());

            }

            long productId = wrapper.getProductModel().getProductId().longValue();

            if (productId == ProductConstantsInterface.TELLER_CASH_OUT ||
                    productId == ProductConstantsInterface.TELLER_WALK_IN_CASH_IN ||
                    productId == ProductConstantsInterface.TELLER_ACCOUNT_HOLDER_CASH_IN) {

                TransactionModel transactionModel = wrapper.getTransactionModel();
                TransactionDetailModel transactionDetailModel = wrapper.getTransactionDetailModel();

                txDetailMaster.setRecipientMobileNo(transactionDetailModel.getCustomField8());
                txDetailMaster.setRecipientCnic(transactionDetailModel.getCustomField5());
                txDetailMaster.setSaleMobileNo(transactionDetailModel.getCustomField6());
                txDetailMaster.setSenderCnic(transactionDetailModel.getCustomField7());
                txDetailMaster.setRecipientAccountNo(transactionDetailModel.getCustomField2());
                txDetailMaster.setTellerAppUserId(wrapper.getAppUserModel().getAppUserId());
            }

            txDetailMaster.setTransactionDetailId(wrapper.getTransactionDetailModel().getTransactionDetailId());
            txDetailMaster.setRecipientBankAccountNo(wrapper.getTransactionDetailModel().getCustomField1());
            txDetailMaster.setProductId(wrapper.getTransactionDetailModel().getProductId());

            if (productModel != null && productModel.getProductId() != null) {
                txDetailMaster.setProductName(productModel.getName());
                txDetailMaster.setProductCode(productModel.getProductCode());
                txDetailMaster.setBillType(productModel.getBillType());
                txDetailMaster.setSupplierId(productModel.getSupplierId());
                txDetailMaster.setSupplierName(productModel.getSupplierIdSupplierModel().getName());
            }

            if (wrapper.getTransactionDetailModel().getCustomField13() != null) {
                txDetailMaster.setRecipientDeviceTypeId(Long.valueOf(wrapper.getTransactionDetailModel().getCustomField13()));
                String deviceTypeName = transactionDetailMasterManager.getDeviceTypeNameById(Long.valueOf(wrapper.getTransactionDetailModel().getCustomField13()));
                txDetailMaster.setRecipientDeviceType(deviceTypeName);
            }

            if (wrapper.getTransactionDetailModel().getCustomField1() != null
                    && productId != ProductConstantsInterface.BB_TO_CORE_ACCOUNT) {
                txDetailMaster.setRecipientAccountNick(transactionDetailMasterManager.loadSmartMoneyAccountNickBySmartMoneyAccountId(Long.valueOf(wrapper.getTransactionDetailModel().getCustomField1())));
            }

            if (wrapper.getTransactionDetailModel().getCustomField2() != null &&
                    wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CASH_TRANSFER) {

                txDetailMaster.setRecipientAccountNo(wrapper.getTransactionDetailModel().getCustomField2());
            }

            // setting segmentId to be used for Velocity_Stats calculation
            if (wrapper.getSegmentModel() != null &&
                    wrapper.getSegmentModel().getSegmentId() != null) {

                txDetailMaster.setSegmentId(wrapper.getSegmentModel().getSegmentId());
            }


        }

        logger.info("[TransactionProcessor.updateTransactionDetailMasterForTransaction] Saving transaction detail master. PK: " + txDetailMaster.getPk() + " Trx Code:" + wrapper.getTransactionDetailMasterModel().getTransactionCode() + " Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());


        transactionDetailMasterManager.saveTransactionDetailMaster(txDetailMaster);

    }

    private WorkFlowWrapper prepareTrxDetailMasterModelForAgentNetworkChange(WorkFlowWrapper wrapper) throws Exception {
        logger.info("[TransactionProcessor.prepareTrxDetailMasterModelForAgentNetworkChange] Updating Transaction Detail Master.");
        Boolean isLeg2Trx = wrapper.isLeg2Transaction();
        RetailerContactModel senderRetailerContactModel = null;
        RetailerContactModel receiverRetailerContactModel = null;
        RetailerModel senderRetailerModel = null;
        RetailerModel receiverRetailerModel = null;
        DistributorModel senderDistributorModel = null;
        DistributorModel receiverDistributorModel = null;
        senderRetailerContactModel = wrapper.getToRetailerContactModel();
        if (senderRetailerContactModel != null && isLeg2Trx) {
            senderRetailerContactModel = appUserManager.getRetailerContactModelById(senderRetailerContactModel.getRetailerContactId());
            senderRetailerModel = appUserManager.getRetailerModelByRetailerIdForTrx(senderRetailerContactModel.getRetailerId());
            if (senderRetailerModel != null)
                senderDistributorModel = appUserManager.findDistributorModelByIdForTrx(senderRetailerModel.getDistributorId());
        }
        receiverRetailerContactModel = wrapper.getFromRetailerContactModel();
        if (receiverRetailerContactModel != null && !isLeg2Trx) {
            receiverRetailerContactModel = appUserManager.getRetailerContactModelById(receiverRetailerContactModel.getRetailerContactId());
            receiverRetailerModel = appUserManager.getRetailerModelByRetailerIdForTrx(receiverRetailerContactModel.getRetailerId());
            if (receiverRetailerModel != null)
                receiverDistributorModel = appUserManager.findDistributorModelByIdForTrx(receiverRetailerModel.getDistributorId());
        }
        if (isLeg2Trx) {
            wrapper = updateTrxDetailMasterForLeg2(wrapper, senderRetailerContactModel, senderRetailerModel, senderDistributorModel);
            wrapper = updateTrxDetailMasterForLeg1(wrapper, receiverRetailerContactModel, receiverRetailerModel, receiverDistributorModel);
        } else
            wrapper = updateTrxDetailMasterForLeg1(wrapper, receiverRetailerContactModel, receiverRetailerModel, receiverDistributorModel);
        return wrapper;
    }


    private WorkFlowWrapper updateTrxDetailMasterForLeg1(WorkFlowWrapper wrapper, RetailerContactModel retailerContactModel, RetailerModel retailerModel,
                                                         DistributorModel distributorModel) throws Exception {
        TransactionDetailMasterModel txDetailMaster = wrapper.getTransactionDetailMasterModel();
        AreaModel areaModel = null;
        RegionModel regionModel = null;
        DistributorLevelModel distributorLevelModel = null;
        if (retailerContactModel != null) {
            distributorLevelModel = retailerContactModel.getRelationDistributorLevelModel();
            txDetailMaster.setSenderRetailerContactId(retailerContactModel.getRetailerContactId());
            txDetailMaster.setSenderRetailerContactName(retailerContactModel.getName());
            if (retailerContactModel.getParentRetailerContactModel() != null)
                txDetailMaster.setSenderParentRetailerContactId(retailerContactModel.getParentRetailerContactModel().getParentRetailerContactModelId());
        }
        if (retailerModel != null) {
            regionModel = retailerModel.getRegionModel();
            areaModel = retailerModel.getRelationAreaIdAreaModel();
        }
        if (retailerModel != null) {
            txDetailMaster.setSenderRetailerId(retailerModel.getRetailerId());
            txDetailMaster.setSenderRetailerName(retailerModel.getName());
        }
        if (distributorModel != null) {
            txDetailMaster.setSenderDistributorId(retailerModel.getDistributorId());
            txDetailMaster.setSenderDistributorName(distributorModel.getName());
            txDetailMaster.setSenderServiceOPId(distributorModel.getMnoId());
        }
        if (regionModel != null) {
            txDetailMaster.setSendingRegion(regionModel.getRegionId());
            txDetailMaster.setSendingRegionName(regionModel.getRegionName());
        }
        if (areaModel != null) {
            txDetailMaster.setSenderAreaId(areaModel.getAreaId());
            txDetailMaster.setSenderAreaName(areaModel.getName());
        }
        if (distributorLevelModel != null) {
            txDetailMaster.setSenderDistributorLevelId(distributorLevelModel.getDistributorLevelId());
            txDetailMaster.setSenderDistributorLevelName(distributorLevelModel.getName());
        }
        wrapper.setTransactionDetailMasterModel(txDetailMaster);
        return wrapper;
    }

    private WorkFlowWrapper updateTrxDetailMasterForLeg2(WorkFlowWrapper wrapper, RetailerContactModel retailerContactModel, RetailerModel retailerModel,
                                                         DistributorModel distributorModel) throws Exception {
        TransactionDetailMasterModel txDetailMaster = wrapper.getTransactionDetailMasterModel();
        AreaModel areaModel = null;
        RegionModel regionModel = null;
        DistributorLevelModel distributorLevelModel = null;
        if (retailerContactModel != null) {
            distributorLevelModel = retailerContactModel.getRelationDistributorLevelModel();
            txDetailMaster.setRecipientRetailerContactId(retailerContactModel.getRetailerContactId());
            txDetailMaster.setRecipientRetailerContactName(retailerContactModel.getName());
            if (retailerContactModel.getParentRetailerContactModel() != null)
                txDetailMaster.setReceiverParentRetailerContactId(retailerContactModel.getParentRetailerContactModel().getParentRetailerContactModelId());
        }
        if (retailerModel != null) {
            regionModel = retailerModel.getRegionModel();
            areaModel = retailerModel.getRelationAreaIdAreaModel();
        }
        if (retailerModel != null) {
            txDetailMaster.setRecipientRetailerId(retailerModel.getRetailerId());
            txDetailMaster.setRecipientRetailerName(retailerModel.getName());
        }
        if (distributorModel != null) {
            txDetailMaster.setReceiverDistributorId(retailerModel.getDistributorId());
            txDetailMaster.setReceiverDistributorName(distributorModel.getName());
            txDetailMaster.setReceiverServiceOPId(distributorModel.getMnoId());
        }
        if (regionModel != null) {
            txDetailMaster.setReceivingRegion(regionModel.getRegionId());
            txDetailMaster.setReceivingRegionName(regionModel.getRegionName());
        }
        if (areaModel != null) {
            txDetailMaster.setReceiverAreaId(areaModel.getAreaId());
            txDetailMaster.setReceiverAreaName(areaModel.getName());
        }
        if (distributorLevelModel != null) {
            txDetailMaster.setRecipientDistributorLevelId(distributorLevelModel.getDistributorLevelId());
            txDetailMaster.setRecipientDistributorLevelName(distributorLevelModel.getName());
        }
        wrapper.setTransactionDetailMasterModel(txDetailMaster);
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)
            throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CreditPaymentApiTransaction....");
        }
        wrapper = super.doPreProcess(wrapper);

        TransactionModel txModel = wrapper.getTransactionModel();

        txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
        // txModel.setDistributorId(wrapper.getCustomerAppUserModel().getDistributorContactIdDistributorContactModel().getDistributorId());
        txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
        txModel.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        txModel.setTransactionAmount(wrapper.getTransactionAmount());
        txModel.setTotalAmount(wrapper.getTotalAmount());
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

        // Customer mobile No
        txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel()
                .getMobileNo());
        txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel()
                .getMobileNo());
        txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());

        wrapper.setTransactionModel(txModel);
        if (logger.isDebugEnabled()) {
            logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CreditPaymentApiTransaction....");
        }

        return wrapper;
    }

    public void setSupplierManager(SupplierManager supplierManager) {
        this.supplierManager = supplierManager;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
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

    public void setSwitchController(SwitchController switchController) {
        this.switchController = switchController;
    }

    public void setVeriflyController(VeriflyManagerService veriflyController) {
        this.veriflyController = veriflyController;
    }

    public void setSupplierBankInfoManager(
            SupplierBankInfoManager supplierBankInfoManager) {
        this.supplierBankInfoManager = supplierBankInfoManager;
    }

    public Double getTransactionProcessingCharges(
            CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside getTransactionProcessingCharges of CreditPaymentApiTransaction....");
        }
        Double transProcessingAmount = 0D;

        List<CommissionRateModel> resultSetList = (List) commissionWrapper
                .getCommissionWrapperHashMap().get(
                        CommissionConstantsInterface.COMMISSION_RATE_LIST);

        for (CommissionRateModel commissionRateModel : resultSetList) {
            if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue()) {
                if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue())
                    transProcessingAmount += commissionRateModel.getRate();
                else
                    transProcessingAmount += (workFlowWrapper.getTransactionAmount() * commissionRateModel.getRate()) / 100;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Ending getTransactionProcessingCharges of CreditPaymentApiTransaction....");
        }
        return transProcessingAmount;
    }

    public void setOperatorManager(OperatorManager operatorManager) {
        this.operatorManager = operatorManager;
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
    public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception {
        logger.info("[CreditPaymentApiTransaction.rollback] complete...");
        return wrapper;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    public void setTransactionReversalManager(
            TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }
}
