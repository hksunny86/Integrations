package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.NovaAlertMessage;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DebitCardCashWithdrawlTransaction extends SalesTransaction {
    protected final Log log = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");
    SupplierManager supplierManager;
    CommissionManager commissionManager;

    private SmartMoneyAccountManager smartMoneyAccountManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private GenericDao genericDAO;
    private UserDeviceAccountsManager userDeviceAccountsManager;

    public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {
        if (wrapper.getCustomerAppUserModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);

        if ("".equals(wrapper.getCustomerAppUserModel().getMobileNo()))
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);

        if (wrapper.getUserDeviceAccountModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);

        if (wrapper.getOlaSmartMoneyAccountModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);

        if (!wrapper.getOlaSmartMoneyAccountModel().getActive())
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);

        if (wrapper.getDeviceTypeModel() != null && !wrapper.getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.ATM)) {
            if (wrapper.getOlaSmartMoneyAccountModel().getChangePinRequired())
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_PIN_CHG_REQ);
        }

        if (!wrapper.getOlaSmartMoneyAccountModel().getCustomerId().toString().equals(wrapper.getCustomerAppUserModel().getCustomerId().toString()))
            throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);


        // Validates the Product's requirements
        if (wrapper.getProductModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);

        if (!wrapper.getProductModel().getActive())
            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);


        // Validates the Product's requirements
        if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue()) {
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

        return wrapper;
    }

    public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        CommissionWrapper commissionWrapper = calculateCommission(wrapper);

        CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        wrapper.setCommissionAmountsHolder(commissionAmounts);
        wrapper.setCommissionWrapper(commissionWrapper);

        try {
            getCommonCommandManager().validateBalance(wrapper.getAppUserModel(), wrapper.getOlaSmartMoneyAccountModel(), wrapper.getCommissionAmountsHolder().getTotalAmount(),
                    true);
        }
        catch (Exception ex){
            ex.printStackTrace();
            logger.error("[DebitCardCashWithdrawlTransaction.doSale] Exception occured while validating balance " + ex.getStackTrace());
            throw new WorkFlowException(WorkFlowErrorCodeConstants.DEBIT_BLOCKED);
        }
        TransactionDetailModel txDetailModel = new TransactionDetailModel();

        wrapper.getTransactionModel().setConfirmationMessage(" _ ");
        wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        CommissionAmountsHolder commissionAmountsHolder = wrapper.getCommissionAmountsHolder();
        ProductDeviceFlowListViewModel productDeviceFlowModel = new ProductDeviceFlowListViewModel();
        productDeviceFlowModel.setProductId(wrapper.getProductModel().getPrimaryKey());
        productDeviceFlowModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);

        List<ProductDeviceFlowListViewModel> list = this.genericDAO.findEntityByExample(productDeviceFlowModel, null);

        if (list != null && list.size() > 0) {
            productDeviceFlowModel = list.get(0);
            wrapper.setProductDeviceFlowModel(productDeviceFlowModel);
        }

        wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmountsHolder.getTotalCommissionAmount() + commissionAmountsHolder.getFedCommissionAmount());
        txDetailModel.setActualBillableAmount(commissionAmountsHolder.getTransactionAmount());
        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        txDetailModel.setConsumerNo(wrapper.getCustomerAppUserModel().getMobileNo());
        wrapper.getTransactionModel().setTotalAmount(commissionAmountsHolder.getTotalAmount());
        if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE))
            if (ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel() != null) {
                wrapper.getTransactionModel().setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
            }
        txDetailModel.setSettled(false);

        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
/*

        // Set Handler Detail in Transaction and Transaction Detail Master
        if(wrapper.getHandlerModel() != null && wrapper.getHandlerUserDeviceAccountModel() != null){
            wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
            wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
        }
*/

        txManager.saveTransaction(wrapper); // save the transaction

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());
        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
        if (wrapper.getSmartMoneyAccountModel() != null)
            switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
        else
            switchWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());

        //perform Cash withdrawal on OLA
        switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);

        wrapper.setOLASwitchWrapper(switchWrapper);
        // Agent Account Number
        if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE))
            wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getToAccountNo());

        // Customer Account Number
        wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getFromAccountNo());

        wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());
        wrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(wrapper.getOLASwitchWrapper().getSwitchSwitchModel());
        wrapper.getTransactionDetailModel().setCustomField1("" + wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId()); //set customer's smart money account ID
        wrapper.getTransactionDetailModel().setCustomField3("" + switchWrapper.getSwitchSwitchModel().getSwitchId());
        wrapper.getTransactionDetailModel().setCustomField13("" + wrapper.getDeviceTypeModel().getDeviceTypeId());
        if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)||wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)||wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL))
            wrapper.getTransactionModel().setBankAccountNo(wrapper.getOLASwitchWrapper().getFromAccountNo());

        wrapper.getTransactionModel().setCustomerMobileNo(null);

        if (wrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction() == null) {
            wrapper.getOLASwitchWrapper().getOlavo().setFromBalanceAfterTransaction(0D);
        }

        if (wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.INTERNATIONAL_DEBIT_CARD_CASH_WITHDRAWAL_OFF_US) ||
                wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.International_POS_DEBIT_CARD_CASH_WITHDRAWAL)){
            wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getAccountInfoModel().getAccountNo());
        }

            switchWrapper.setAgentBalance(wrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
        wrapper.setSwitchWrapper(switchWrapper);

        txDetailModel.setSettled(true);
        wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
        this.settlementManager.settleCommission(wrapper.getCommissionWrapper(), wrapper);
        sendSMS(wrapper);

        txManager.saveTransaction(wrapper);
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    private void sendSMS(WorkFlowWrapper wrapper) throws Exception {
        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
        ArrayList<NovaAlertMessage> messageList2 = new ArrayList<NovaAlertMessage>(0);

        Boolean thirdPartyCheck = wrapper.getProductModel().getInclChargesCheck();
        Double totalAmount = 0.0d;
        if (thirdPartyCheck == null) {
            thirdPartyCheck = false;
        }
        Double txAmount = wrapper.getCommissionAmountsHolder().getTransactionAmount();
        wrapper.setInclChargesCheck(thirdPartyCheck);
        Double billableAmount = thirdPartyCheck ? txAmount : txAmount - wrapper.getTransactionDetailMasterModel().getInclusiveCharges();
        Map<String, Double> amounts = CommonUtils.amountsWithIncChargesAdjustment(wrapper);
        Double charges = amounts.get(CommandFieldConstants.KEY_INCC_EXCC);

        if (wrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().size() == 0) {
            totalAmount = wrapper.getCommissionAmountsHolder().getTransactionAmount();
        } else {
            totalAmount = wrapper.getCommissionAmountsHolder().getTransactionAmount() + wrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID);
        }
        if (thirdPartyCheck) {
            charges = wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount();
        }
        Object[] customerParams = new Object[]{Formatter.formatNumbers(txAmount), wrapper.getCustomerAppUserModel().getMobileNo(), charges, dtf.print(new DateTime()), tf.print(new LocalTime())};
        String smsInputParam = "CUSTOMER_WITHDRAWAL_DEBIT_CARD";
        ProductModel productModel = wrapper.getProductModel();
        if (productModel != null && (productModel.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ISSUANCE)
                || productModel.getProductId().equals(ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE)
                || productModel.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_ANNUAL_FEE)
                || productModel.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE)
                || productModel.getProductId().equals(ProductConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US)
                || productModel.getProductId().equals(ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US))) {

            smsInputParam = "CUSTOMER_DEBIT_CARD_FEE_DEDUCTION_WITH_EXCLUSIVE_FED";
            String cardProductName = "";
            if (wrapper.getSwitchWrapper().getSmartMoneyAccountModel().getCardTypeProdIdCardProdCodeModel() != null) {
                cardProductName = wrapper.getSwitchWrapper().getSmartMoneyAccountModel().getCardTypeProdIdCardProdCodeModel().getCardProductName();
            }

            if (wrapper.getSwitchWrapper().getSmartMoneyAccountModel().getCustomerId() != null) {
                if ((wrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || wrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) &&
                        wrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID) > 0.0) {
                    customerParams = new Object[]{Formatter.formatNumbers(txAmount), productModel.getName(),
                            cardProductName, totalAmount};
                } else {
                    smsInputParam = "CUSTOMER_DEBIT_CARD_FEE_DEDUCTION";
                    customerParams = new Object[]{Formatter.formatNumbers(txAmount), productModel.getName(),
                            cardProductName};
                }
            } else {
                if ((wrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || wrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) &&
                        wrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID) > 0.0) {
                    customerParams = new Object[]{Formatter.formatNumbers(txAmount), productModel.getName(),
                            wrapper.getSwitchWrapper().getWorkFlowWrapper().getOlaSmartMoneyAccountModel().getCardTypeProdIdCardProdCodeModel().getCardProductName(), totalAmount};
                } else {
                    smsInputParam = "CUSTOMER_DEBIT_CARD_FEE_DEDUCTION";
                    customerParams = new Object[]{Formatter.formatNumbers(txAmount), productModel.getName(),
                            wrapper.getSwitchWrapper().getWorkFlowWrapper().getOlaSmartMoneyAccountModel().getCardTypeProdIdCardProdCodeModel().getCardProductName()};
                }
            }
        }
        if(productModel != null && productModel.getProductId().equals(ProductConstantsInterface.DEBIT_CARD_CASH_WITHDRAWAL_OFF_US)){
            smsInputParam = "ATM_CASH_WITHDRAWAL";
            customerParams = new Object[]{Formatter.formatNumbers(txAmount), tf.print(new LocalTime()), dtf.print(new DateTime()),
                    charges, wrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction(), wrapper.getTransactionCodeModel().getCode()};
        }
        if(productModel != null && productModel.getProductId().equals(ProductConstantsInterface.POS_DEBIT_CARD_CASH_WITHDRAWAL)){
            smsInputParam = "POS_PURCHASE";
            customerParams = new Object[]{Formatter.formatNumbers(txAmount), wrapper.getCardAcceptorDetails(), tf.print(new LocalTime()), dtf.print(new DateTime()),
                    charges, wrapper.getTransactionDetailMasterModel().getWht(), wrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction(),
                    wrapper.getTransactionCodeModel().getCode()};
        }

        String customerSMS = this.messageSource.getMessage(smsInputParam, customerParams, null);
        messageList.add(new SmsMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSMS));
        messageList2.add(new NovaAlertMessage(wrapper.getCustomerAppUserModel().getMobileNo(), customerSMS,"","","",""));
        wrapper.getTransactionModel().setConfirmationMessage(customerSMS);
        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
        wrapper.putObject(CommandFieldConstants.KEY_NOVA_ALERT_SMS_MESSAGES,messageList2);
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
        wrapper = super.doPreStart(wrapper);
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getCustomerAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        //Setting Customer's User ID in Transaction Detail Master Model
        wrapper.getTransactionDetailMasterModel().setRecipientMfsId(wrapper.getUserDeviceAccountModel().getUserId());

        // Set Handler User Device Account Model
        /*if(wrapper.getHandlerAppUserModel() != null && wrapper.getHandlerAppUserModel().getAppUserId() != null && wrapper.getHandlerModel() != null){
            SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.setHandlerSMAModel(sma);

            UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
            handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
            handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
            baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
            baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
            wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        }
*/
        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

        logger.info("[CustomerDebitCardCashWithdrawalTransaction.doPreStart]End loading business object models...");

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>Inside doPreProcess(WorkFlowWrapper wrapper) of DebitCardCashWithdrawlTransaction>>>>>>>>>>>>>>>>>>>>>>>>");
        TransactionModel txModel = wrapper.getTransactionModel();
        Long appUserTypeId = ThreadLocalAppUser.getAppUserModel().getAppUserTypeId();
        if (appUserTypeId.equals(UserTypeConstantsInterface.RETAILER)) {
            txModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
            txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
            txModel.setFromRetContactName(ThreadLocalAppUser.getAppUserModel().getFirstName() + " " + ThreadLocalAppUser.getAppUserModel().getLastName());
            txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        }
        wrapper = super.doPreProcess(wrapper);
        txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setSmartMoneyAccountId(wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());
        txModel.setProcessingBankId(BankConstantsInterface.OLA_BANK_ID);
        txModel.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        if (ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel() != null) {
            txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
        }
        txModel.setTransactionAmount(wrapper.getTransactionAmount());
        txModel.setTotalAmount(wrapper.getTotalAmount());
        txModel.setTotalCommissionAmount(0d);
        txModel.setDiscountAmount(0d);

        txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());
        txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
        txModel.setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
        txModel.setCustomerMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        if (appUserTypeId.equals(UserTypeConstantsInterface.CUSTOMER))
            txModel.setSaleMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());
        else
            txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        wrapper.setTransactionModel(txModel);
        logger.info(">>>>>>>>>>>>>>>>>>>>>Ending doPreProcess(WorkFlowWrapper wrapper) of DebitCardCashWithdrawalTransaction>>>>>>>>>>>>>>>>>>>>");
        if (logger.isDebugEnabled()) {
            logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of DebitCardCashWithdrawlTransaction....");
        }

        return wrapper;
    }


    public void setSupplierManager(SupplierManager supplierManager) {
        this.supplierManager = supplierManager;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
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

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    @Override
    protected WorkFlowWrapper doPostEnd(WorkFlowWrapper wrapper) {
        return super.doPostEnd(wrapper);
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
            logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of DebitCardCashWithdrawalTransaction...");
        }
        wrapper.setSegmentModel(wrapper.getCustomerModel().getSegmentIdSegmentModel());

        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());

//        wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());

        commissionWrapper = commissionManager.calculateCommission(wrapper);

        if (logger.isDebugEnabled()) {
            logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of DebitCardCashWithdrawalTransaction...");
        }
        return commissionWrapper;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}
