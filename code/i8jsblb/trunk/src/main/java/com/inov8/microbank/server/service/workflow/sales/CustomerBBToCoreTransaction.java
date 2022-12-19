package com.inov8.microbank.server.service.workflow.sales;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
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
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.microbank.webapp.action.allpayweb.formbean.Product;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Date;

public class CustomerBBToCoreTransaction extends SalesTransaction {
    protected final Log log = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    SupplierManager supplierManager;
    CommissionManager commissionManager;
    private ProductManager productManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private FinancialIntegrationManager financialIntegrationManager;


    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());

        wrapper.setTaxRegimeModel(wrapper.getCustomerModel().getTaxRegimeIdTaxRegimeModel());

        commissionWrapper = this.commissionManager.calculateCommission(wrapper);

        return commissionWrapper;
    }

    public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
                CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        BBToCoreVO productVO = (BBToCoreVO) workFlowWrapper.getProductVO();

        if (!Double.valueOf(Formatter.formatDouble(productVO.getCurrentBillAmount().doubleValue())).equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getBillAmount().doubleValue())))) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
        }
        if(!workFlowWrapper.getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE)) {
            if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalCommissionAmount().doubleValue())).equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalCommissionAmount().doubleValue())))) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
            }
        }
        if(!workFlowWrapper.getDeviceTypeModel().getDeviceTypeId().equals(DeviceTypeConstantsInterface.WEB_SERVICE)) {
            if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalAmount().doubleValue())).equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalAmount().doubleValue())))) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
            }
        }
    }

    public WorkFlowWrapper settleAmount(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    public WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

        // ------------------------Validates the Customer's requirements
        // -----------------------------------
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

        if (wrapper.getSmartMoneyAccountModel() != null) {
            if (!wrapper.getSmartMoneyAccountModel().getActive()) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_INACTIVE);
            }
        } else {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUST_SMARTMONEY_NULL);
        }

        // -------------------------------- Validates the Product's requirements
        // ---------------------------------
        if (wrapper.getProductModel() != null) {
            if (!wrapper.getProductModel().getActive()) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
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
            if (!wrapper.getProductModel().getSupplierIdSupplierModel().getActive()) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NOT_ACTIVE);
            }
        } else {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.SUPPLIER_NULL);
        }

        // ------------------------- Validates the iNPUT's requirements
        if (wrapper.getBillAmount() < 0) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
        }
        if (wrapper.getTotalAmount() < 0) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
        }
        if (wrapper.getTotalCommissionAmount() < 0) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
        }
        if (wrapper.getTxProcessingAmount() < 0) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
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
            if (!wrapper.getProductModel().getServiceIdServiceModel().getActive()) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);
            }
        } else {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
        }

        return wrapper;
    }

    public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {

        TransactionDetailModel txDetailModel = new TransactionDetailModel();

        CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
        CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

        this.validateCommission(commissionWrapper, wrapper);

        wrapper.setCommissionAmountsHolder(commissionAmounts);

        AbstractFinancialInstitution olaVeriflyFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());

        //Core FT code needs to be incorporated.
        BaseWrapper _baseWrapper = new BaseWrapperImpl();
        _baseWrapper.setBasePersistableModel(new BankModel(BankConstantsInterface.ASKARI_BANK_ID));

        AbstractFinancialInstitution coreFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(_baseWrapper);

        wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
        wrapper.getTransactionModel().setCreatedOn(new Date());
        txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        txDetailModel.setConsumerNo(((BBToCoreVO) wrapper.getProductVO()).getConsumerNo());
        wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

        txDetailModel.setCustomField5("" + wrapper.getWalkInCustomerMob());
        txDetailModel.setCustomField7(wrapper.getCustomerAppUserModel().getNic());

        String coreAccNo = "" + ((BBToCoreVO) wrapper.getProductVO()).getAccountNumber();
        txDetailModel.setCustomField2(coreAccNo);
        txDetailModel.setCustomField11(coreAccNo);
        wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getWalkInCustomerMob());

        txDetailModel.setSettled(false);
        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().setConfirmationMessage(" _ ");

        BaseWrapper baseWrapper = new BaseWrapperImpl();

        baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
        double senderBalance = 0;

        logger.info("[CustomerBBToCoreTransaction.doSale] Going to debit money from Customer OLA Account." +
                " Logged in AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                " CustomerMobile No:" + ThreadLocalAppUser.getAppUserModel().getMobileNo() +
                " Trx ID:" + wrapper.getTransactionCodeModel().getCode());

        switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);

        wrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
        senderBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction();

        //set Sender BB Customer details in transaction_details table
        wrapper.getTransactionDetailModel().setCustomField1("" + wrapper.getSenderSmartMoneyAccountModel().getSmartMoneyAccountId());
        wrapper.getTransactionDetailModel().setCustomField3("" + switchWrapper.getSwitchSwitchModel().getName());
        wrapper.getTransactionDetailModel().setSettled(true);

        wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());

        txManager.saveTransaction(wrapper);
        wrapper.setSwitchWrapper(switchWrapper);
        wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());

        //Core FT code needs to be incorporated.
        SmartMoneyAccountModel smartMoneyAccountModelTemp = wrapper.getOlaSmartMoneyAccountModel();
        wrapper.setAppUserModel(wrapper.getCustomerAppUserModel());
        wrapper.setSmartMoneyAccountModel(wrapper.getOlaSmartMoneyAccountModel());

        SwitchWrapper switchWrapper2 = new SwitchWrapperImpl();
        switchWrapper2.setWorkFlowWrapper(wrapper);
        switchWrapper2.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
        switchWrapper2.setInclusiveChargesApplied(switchWrapper.getInclusiveChargesApplied()); // to be used in CreditAdvice

        logger.info("[CustomerBBToCoreTransaction.doSale] Going to credit T24 Recipient A/C." +
                " LoggedIn AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() +
                " Trx ID:" + wrapper.getTransactionCodeModel().getCode());

        switchWrapper2.setIntgTransactionTypeId(IntgTransactionTypeConstantsInterface.CREDIT_ACCOUNT_ADVICE_CORE);
        switchWrapper2 = coreFinancialInstitution.creditAccountAdvice(switchWrapper2);
        wrapper.setSmartMoneyAccountModel(smartMoneyAccountModelTemp);
        wrapper.setMiddlewareSwitchWrapper(switchWrapper2); // for day end O.F. settlement of Core FT

        this.settleAmount(wrapper); // settle all amounts to the respective stakeholders

        wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);

        String  brandName = null;
        if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
            brandName = MessageUtil.getMessage("sco.brandName");
        } else {

            brandName = MessageUtil.getMessage("jsbl.brandName");
        }

        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
        String custSMS = null;
        String maskedSenderAccNo = wrapper.getAppUserModel().getMobileNo();
        maskedSenderAccNo = maskedSenderAccNo.replaceAll("(\\d+)(\\d{2})","****$2");

        String maskedReceiverAccNo = wrapper.getTransactionDetailModel().getCustomField11();
        maskedReceiverAccNo = maskedReceiverAccNo.replaceAll("(\\d+)(\\d{2})","****$2");


//		{0}\nTrx ID {1}\nYou have transferred Rs.{2}\nat {3}\non {4}\ninto JSBL A/C {5}\nAvl Bal is Rs.{6}.
        if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID) != null &&
                (wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).equals("NOVA") &&
                        wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT))) {
            custSMS = this.getMessageSource().getMessage(
                    "nova.walletToIBFT",
                    new Object[]{
                            wrapper.getTransactionCodeModel().getCode(),
                            Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                            wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE),
                            wrapper.getObject("BANK_SHORT_NAME"),
                            maskedReceiverAccNo,
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                            senderBalance,

                    }, null);

//            messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), custSMS));

            wrapper.getTransactionModel().setConfirmationMessage(custSMS);
        }
          else if(wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)) {
            custSMS = this.getMessageSource().getMessage(
                    "CustomerBBToT24SMS",
                    new Object[]{
                            maskedSenderAccNo,
                            wrapper.getAppUserModel().getFirstName() + " " + wrapper.getAppUserModel().getLastName(),
                            Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                            maskedReceiverAccNo,//{1}
                            wrapper.getCustomerAccount().getTitleOfTheAccount(),
                            wrapper.getObject("BANK_SHORT_NAME"),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
                            senderBalance,


                    }, null);
        }
        else if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID) != null &&
                (wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).equals("NOVA") &&
                        wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT))){
            custSMS = this.getMessageSource().getMessage(
                    "nova.walletToCore",
                    new Object[]{
                            wrapper.getTransactionCodeModel().getCode(),
                            Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                            maskedReceiverAccNo,
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                            senderBalance,
                    }, null);

//            messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), custSMS));

            wrapper.getTransactionModel().setConfirmationMessage(custSMS);
        }
        else{
             custSMS = this.getMessageSource().getMessage(
                    "CustomerWalletToT24SMS",
                    new Object[]{
                            brandName,
                            wrapper.getTransactionCodeModel().getCode(),
                            Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
                            Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
                            wrapper.getTransactionDetailModel().getCustomField11(),//{1}
                            senderBalance
                    }, null);
        }

        messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), custSMS));

        wrapper.getTransactionModel().setConfirmationMessage(custSMS);

        if(wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.CUSTOMER_BB_TO_IBFT)) {
            String receiverSMS = this.getMessageSource().getMessage(
                    "wallet.RecipientBBToT24SMS",
                    new Object[]{
                            wrapper.getTransactionCodeModel().getCode(),
                            Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
                            wrapper.getObject("BANK_SHORT_NAME"),
                            wrapper.getTransactionDetailModel().getCustomField11(),
                            wrapper.getSenderAppUserModel(). getMobileNo(),
                    }, null);

            messageList.add(new SmsMessage(wrapper.getTransactionDetailModel().getCustomField5(), receiverSMS));
        }
        else if (!StringUtil.isNullOrEmpty(wrapper.getTransactionDetailModel().getCustomField5())) {
            //{0}\nTrx ID {1}\nYou have received Rs. {2} at {3} on {4} into JSBL A/c {5} from {6}
            String receiverSMS = this.getMessageSource().getMessage(
                    "RecipientBBToT24SMS",
                    new Object[]{
                            wrapper.getTransactionCodeModel().getCode(),
                            Formatter.formatDouble(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                            PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
                            wrapper.getTransactionDetailModel().getCustomField11(),
                            wrapper.getSenderAppUserModel(). getMobileNo(),
                    }, null);

            messageList.add(new SmsMessage(wrapper.getTransactionDetailModel().getCustomField5(), receiverSMS));
        }

        txManager.saveTransaction(wrapper);
        this.settlementManager.settleCommission(commissionWrapper, wrapper);

        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {

        wrapper = super.doPreStart(wrapper);

//        //load existing transaction objects
//        wrapper.setTotalAmount(wrapper.getTransactionModel().getTotalAmount());
//        wrapper.setTotalCommissionAmount(wrapper.getTransactionModel().getTotalCommissionAmount());
//        wrapper.setBillAmount(wrapper.getTransactionModel().getTransactionAmount());
//        wrapper.setTxProcessingAmount(wrapper.getTransactionModel().getTotalCommissionAmount());
//
//        List<TransactionDetailModel> transactionDetailModelList = new ArrayList<TransactionDetailModel>(wrapper.getTransactionModel().getTransactionIdTransactionDetailModelList());
//        if(transactionDetailModelList != null && transactionDetailModelList.size() > 0) {
//            wrapper.setTransactionDetailModel(transactionDetailModelList.get(0));
//        }

        // Populate the Product model from DB
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(wrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        //SENDER Customer User Device Accounts Model
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getCustomerAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of CustomerBBToCoreTransaction....");
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
        txModel.setCustomerIdCustomerModel(new CustomerModel());
        txModel.getCustomerIdCustomerModel().setCustomerId(wrapper.getCustomerModel().getCustomerId());

        // Smart Money Account Id is populated
        txModel.setSmartMoneyAccountId(wrapper.getSenderSmartMoneyAccountModel().getSmartMoneyAccountId());

        // Payment mode model of transaction is populated
        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        txModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        txModel.setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        txModel.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());

        // Populate processing Bank Id
        txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
        wrapper.setTransactionModel(txModel);

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;

    }

    public void setSupplierManager(SupplierManager supplierManager) {
        this.supplierManager = supplierManager;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
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
        logger.info("[CustomerBBToCoreTransaction.rollback] rollback called...");
        return wrapper;
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

}