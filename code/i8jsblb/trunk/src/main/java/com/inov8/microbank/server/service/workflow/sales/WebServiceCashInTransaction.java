package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
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
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attique on 1/16/2018.
 */
public class WebServiceCashInTransaction extends SalesTransaction {
    protected final Log log = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");


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
//	private String rrnPrefix;


    public void setNotificationMessageManager(
            NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setUserDeviceAccountsManager(
            UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public WebServiceCashInTransaction() {
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
        if (logger.isDebugEnabled())
        {
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
     * @param wrapper
     *            WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper)
            throws FrameworkCheckedException {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside validateBillInfo(WorkFlowWrapper wrapper) of CashDepositTransaction...");
        }
        BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
        wrapper =  billSaleProductDispenser.verify(wrapper);
        if(logger.isDebugEnabled())
        {
            logger.debug("Ending validateBillInfo(WorkFlowWrapper wrapper) of CashDepositTransaction...");
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

        commissionWrapper = this.commissionManager.calculateCommission(wrapper);
        return commissionWrapper;
    }


    public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Inside validateCommission of CashDepositTransaction...");
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

            logger.debug("Ending validateCommission of CashDepositTransaction...");
        }

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
            logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CashDepositTransaction");
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


        // ------------------------- Validates the iNPUT's requirements
        // -----------------------------------
        if (wrapper.getTransactionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
        }
        if (null!=wrapper.getTotalAmount() && wrapper.getTotalAmount() < 0 ) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
        }
        if (null!=wrapper.getTotalCommissionAmount() && wrapper.getTotalCommissionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
        }
        if (null!=wrapper.getTxProcessingAmount() && wrapper.getTxProcessingAmount() < 0) {

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
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CashDepositTransaction");
        }
        return wrapper;
    }

    /**
     * Method respponsible for processing ther Bill Sale transaction
     *
     * @param wrapper
     *            WorkFlowWrapper
     * @return WorkFlowWrapper
     */

    public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception
    {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Inside doSale(WorkFlowWrapper wrapper) of CashDepositTransaction..");
            }

            wrapper.getTransactionModel().setConfirmationMessage(" _ ");
            wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());


            // one coming from iPos to see if they match
            CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);

            //commissionWrapper.get
            CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
                    CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

            wrapper.setCommissionAmountsHolder(commissionAmounts);
            wrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT,commissionAmounts.getTransactionProcessingAmount());
            wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount()+commissionAmounts.getFedCommissionAmount());
            wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
            if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString().equals(FonePayConstants.APIGEE_CHANNEL)){
                wrapper.getTransactionDetailMasterModel().setTerminalId(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
                wrapper.getTransactionDetailMasterModel().setChannelId(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString());
                if(wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE) != null){
                    wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode((String) wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE));
                }
                if(wrapper.getObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME) != null){
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
            switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()-switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));
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
            sendCashDepositSMS(wrapper);

        }

        return wrapper;
    }

    private void sendCashDepositSMS(WorkFlowWrapper wrapper) throws Exception {

        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
//        Double agentBalance = ((CashInVO)wrapper.getProductVO()).getAgentBalance();

                String brandName = null;
                if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
                    brandName = MessageUtil.getMessage("sco.brandName");
                } else {
                    brandName = MessageUtil.getMessage("jsbl.brandName");
                }
        Double charges = wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
        String customerSMS= this.getMessageSource().getMessage(
                "USSD.CustomerCashDepositSMS",
                new Object[] {
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

        messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSMS));

        wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
            throws Exception {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CashDepositTransaction....");
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

        // Populate the Agent OLA Smart Money Account from DB
        String paymentMode=wrapper.getObject(CommandFieldConstants.KEY_PAYMENT_MODE).toString();
        Long paymentModeId = null;
        if(paymentMode.equals("HRA"))
            paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
        else
            paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
        sma.setPaymentModeId(paymentModeId);
        baseWrapper.setBasePersistableModel(sma);
        sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(ThreadLocalAppUser.getAppUserModel(),sma.getPaymentModeId());
        wrapper.setSmartMoneyAccountModel(sma);

        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        AppUserModel appUserModel =new AppUserModel();
        appUserModel.setMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        appUserModel = appUserManager.getAppUserModel(appUserModel);
        if(null != appUserModel)
        {
            wrapper.setCustomerAppUserModel(appUserModel);
            wrapper.setCustomerModel(appUserModel.getCustomerIdCustomerModel());
        }
        // Populate the Customer OLA Smart Money Account from DB

        sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(wrapper.getCustomerAppUserModel(),paymentModeId);
        //Commented By Sheheryaar
        /*String paymentMode=wrapper.getObject(CommandFieldConstants.KEY_PAYMENT_MODE).toString();
        sma = new SmartMoneyAccountModel();
        if(paymentMode.equals("HRA"))
            sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(wrapper.getCustomerAppUserModel(),7L);
        else
            sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(wrapper.getCustomerAppUserModel(),3L);*/

         wrapper.setOlaSmartMoneyAccountModel(sma);

        logger.info("[CashDepositTransaction.doPreStart]End loading business object models...");

        return wrapper;
    }

    /**
     * doSale
     *
     * @param workFlowWrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     * @throws FrameworkCheckedException
     */
    public WorkFlowWrapper dispenseCustomer(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
    {

        try
        {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            SmartMoneyAccountModel customerSMA = workFlowWrapper.getOlaSmartMoneyAccountModel();
            SmartMoneyAccountModel agentSMA = workFlowWrapper.getSmartMoneyAccountModel();

            if (null != customerSMA)
            {
                //Set agent smartmoneyAccount to load financialInstitution
                baseWrapper.setBasePersistableModel(agentSMA);
                AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

                SwitchWrapper switchWrapper = new SwitchWrapperImpl();

                switchWrapper.setBasePersistableModel(agentSMA) ;

                switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(""+customerSMA.getSmartMoneyAccountId()); //set customer's smart money account ID
                TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;

                logger.info("CashDepositTransaction.dispenseCustomer] Going to make FT at OLA from Agent A/C to Customer A/C." +
                        " LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                        " Trx ID:" + workFlowWrapper.getTransactionCodeModel().getCode());
                switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField2(switchWrapper.getToAccountNo());
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField3(""+switchWrapper.getSwitchSwitchModel().getSwitchId());
                workFlowWrapper.setOLASwitchWrapper(switchWrapper); // setting the switchWrapper for rollback

                // set customer balance after transaction
                Double customerBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction();


                workFlowWrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
                switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;

            }
        }
        catch (Exception e)
        {
            if(e instanceof CommandException){
                throw new WorkFlowException(e.getMessage());
            }else if(e instanceof WorkFlowException){
                throw (WorkFlowException) e;
            }else{
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_DOWN);
            }
        }
        return workFlowWrapper;

    }


    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)
            throws Exception {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CashDepositTransaction....");
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
        if(logger.isDebugEnabled())
        {
            logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of CashDepositTransaction....");
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
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside getTransactionProcessingCharges of CashDepositTransaction....");
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
            }
        }
        if(logger.isDebugEnabled())
        {
            logger.debug("Ending getTransactionProcessingCharges of CashDepositTransaction....");
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
    public WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception
    {
        logger.info("[CashDepositTransaction.rollback] complete...");
        return wrapper;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager)
    {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setRetailerContactManager(RetailerContactManager retailerContactManager)
    {
        this.retailerContactManager = retailerContactManager;
    }

    public void setGenericDAO(GenericDao genericDAO)
    {
        this.genericDAO = genericDAO;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
