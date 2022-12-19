package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
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
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;


public class AgentCashDepositTransaction extends SalesTransaction {

    protected final Log log = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf =  DateTimeFormat.forPattern("h:mm a");

    private SupplierManager supplierManager;
    private CommissionManager commissionManager;

    private ProductDispenseController productDispenseController;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private ProductManager productManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private RetailerContactManager retailerContactManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private AppUserManager appUserManager;


    public AgentCashDepositTransaction() {
    }

    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception{
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());
        RetailerContactModel retailerContactmodel = wrapper.getFromRetailerContactModel();
        wrapper.setTaxRegimeModel(retailerContactmodel.getTaxRegimeIdTaxRegimeModel());

        commissionWrapper = this.commissionManager.calculateCommission(wrapper);

        return commissionWrapper;

    }


    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of AgentCashDepositTransaction");
        }
        if (wrapper.getFromRetailerContactModel() != null)
        {
            if (!wrapper.getFromRetailerContactModel().getActive())
            {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NOT_ACTIVE);
            }
        }
        else
        {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.RETAILER_CONTACT_NULL);
        }

        if (wrapper.getRetailerAppUserModel() != null)
        {
            if ("".equals(wrapper.getRetailerAppUserModel().getMobileNo()))
            {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.AGENT_MOBILENO_NOT_SUPPLIED);
            }
        }
        else
        {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.AGENT_MODEL_NULL);
        }
        if (wrapper.getUserDeviceAccountModel() == null)
        {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
        }
        if (wrapper.getSmartMoneyAccountModel() != null)
        {
            if (!wrapper.getSmartMoneyAccountModel().getActive())
            {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.AGENT_SMARTMONEY_INACTIVE);
            }
        }
        else
        {
            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.AGENT_SMARTMONEY_NULL);
        }

        if (wrapper.getProductModel() != null) {
            if (!wrapper.getProductModel().getActive()) {

                throw new WorkFlowException(
                        WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);
            }
        } else {

            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);
        }

        if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_BILL_PAYMENT.longValue()) {
            if (!wrapper.getProductModel().getActive())
            {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_SERVICE_TYPE);
            }
        }

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

        if (wrapper.getTransactionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
        }
        if (wrapper.getTotalAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
        }
        if (wrapper.getTotalCommissionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
        }
        if (wrapper.getTxProcessingAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
        }

        if (wrapper.getPaymentModeModel() != null) {
            if (wrapper.getPaymentModeModel().getPaymentModeId() <= 0) {

                throw new WorkFlowException("PaymentModeID is not supplied.");
            }
        }

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
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of AgentCashDepositTransaction");
        }

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
//        BaseWrapper baseWrapper = new BaseWrapperImpl();
//        AbstractFinancialInstitution abstractFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        if (logger.isDebugEnabled())
        {
            logger.debug("Inside doSale(WorkFlowWrapper wrapper) of AgentCashDepositTransaction..");
        }

        wrapper.getTransactionModel().setConfirmationMessage(" _ ");
        wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getRetailerAppUserModel().getMobileNo());
        wrapper.getTransactionModel().setDiscountAmount(wrapper.getDiscountAmount().doubleValue());
        wrapper.getTransactionModel().setTotalAmount(wrapper.getTotalAmount());
        wrapper.getTransactionModel().setTransactionAmount(wrapper.getTransactionAmount());
        wrapper.getTransactionModel().setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
        wrapper.getTransactionModel().setPaymentModeId(wrapper.getPaymentModeModel().getPaymentModeId());
        wrapper.getFromRetailerContactModel().setOlaCustomerAccountTypeModelId(wrapper.getFromRetailerContactModel().getOlaCustomerAccountTypeModelId());
        wrapper.getTransactionModel().setTransactionTypeId(wrapper.getTransactionTypeModel().getTransactionTypeId());
        CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
        CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap()
                .get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

        if(null != wrapper.getFromRetailerContactModel() && wrapper.getFromRetailerContactModel().getHead()){
            commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
            commissionAmounts.setFranchise1CommissionAmount(0.0d);
        }

        wrapper.setCommissionAmountsHolder(commissionAmounts);

        wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount()+commissionAmounts.getFedCommissionAmount());
        wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());

        txDetailModel.setActualBillableAmount(commissionAmounts.getTransactionAmount());
        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        txDetailModel.setConsumerNo(wrapper.getRetailerAppUserModel().getMobileNo());
//        wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

        txDetailModel.setSettled(false);

        if(wrapper.getHandlerModel() != null){
            wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
            wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
        }

        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
        txManager.saveTransaction(wrapper); // save the transaction

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
        switchWrapper.setCommissionAppUserModel(wrapper.getAppUserModel());

        wrapper = dispenseAgent(wrapper);

        switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
//        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, wrapper.getRetailerAppUserModel().getNic());
        switchWrapper.setTransactionAmount(Double.parseDouble(Formatter.formatDouble(switchWrapper.getWorkFlowWrapper().getTransactionModel().getTotalAmount()-switchWrapper.getWorkFlowWrapper().getCommissionAmountsHolder().getAgent1CommissionAmount())));

        wrapper.getTransactionModel().setProcessingSwitchIdSwitchModel(wrapper.getOLASwitchWrapper().getSwitchSwitchModel());
        wrapper.getTransactionModel().setBankAccountNo(wrapper.getOLASwitchWrapper().getFromAccountNo());

        if(wrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction() == null) {
            wrapper.getOLASwitchWrapper().getOlavo().setFromBalanceAfterTransaction(0D);
        }

        switchWrapper.setAgentBalance(wrapper.getOLASwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
        wrapper.setSwitchWrapper(switchWrapper);

        txDetailModel.setSettled(true);
        wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
        txManager.saveTransaction(wrapper); // save the transaction


        this.settlementManager.settleCommission(commissionWrapper, wrapper);
        return wrapper;

    }
    public WorkFlowWrapper dispenseAgent(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
    {
        try
        {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            SmartMoneyAccountModel agentSMA = workFlowWrapper.getSmartMoneyAccountModel();

            //Set agent smartmoneyAccount to load financialInstitution
            baseWrapper.setBasePersistableModel(agentSMA);
            AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setBasePersistableModel(agentSMA) ;
            switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
            switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;
            // set agent balance after transaction
            Double agentBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction();

            workFlowWrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());

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
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
            throws Exception {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of AgentCashDepositTransaction....");
        }

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        wrapper = super.doPreStart(wrapper);
        //Populate Product Model
        baseWrapper.setBasePersistableModel(wrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());


        // Populate Retailer Contact
        if(wrapper.getFromRetailerContactModel() == null){
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            RetailerContactModel retailerContact = new RetailerContactModel();
            retailerContact.setRetailerContactId( wrapper.getFromRetailerContactAppUserModel().getRetailerContactId() );
            searchBaseWrapper.setBasePersistableModel( retailerContact );
            searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
            wrapper.setFromRetailerContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());
        }

        // Populate Handler's Retailer Contact model from DB
        if(wrapper.getHandlerModel() != null){
            // Populate the Handler OLA Smart Money Account from DB
            SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.setHandlerSMAModel(sma);

            // Set Handler User Device Account Model
            UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
            handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
            handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
            baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
            baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
            wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        }

        // --Setting instruction and success Message
        NotificationMessageModel notificationMessage = new NotificationMessageModel();
        notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
        baseWrapper.setBasePersistableModel(notificationMessage);
        baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
        wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getRetailerAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

        // Populate the Agent OLA Smart Money Account from DB
        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setRetailerContactId( wrapper.getRetailerAppUserModel().getRetailerContactId() ) ;
        baseWrapper.setBasePersistableModel(sma);
        baseWrapper = smartMoneyAccountManager.loadOLASmartMoneyAccount(baseWrapper);
        wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        AppUserModel appUserModel =new AppUserModel();
        appUserModel.setMobileNo(wrapper.getRetailerAppUserModel().getMobileNo());
        appUserModel.setAppUserTypeId(UserTypeConstantsInterface.RETAILER);
//        appUserModel = appUserManager.getAppUserModel(appUserModel);
        appUserModel = wrapper.getRetailerAppUserModel();
        if(null != appUserModel)
        {
            wrapper.setRetailerAppUserModel(appUserModel);
            wrapper.setRetailerContactModel(appUserModel.getRetailerContactIdRetailerContactModel());
        }

        RetailerModel retailerModel = new RetailerModel();
        retailerModel.setRetailerId(UserTypeConstantsInterface.RETAILER);
        retailerModel = wrapper.getRetailerModel();
        if(null!=retailerModel){
            wrapper.setRetailerModel(retailerModel);
        }

        logger.info("[AgentCashDepositTransaction.doPreStart]End loading business object models...");

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)
            throws Exception {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of AgentCashDepositTransaction....");
        }
        wrapper = super.doPreProcess(wrapper);

        TransactionModel txModel = wrapper.getTransactionModel();

        txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
        txModel.setRetailerId(wrapper.getFromRetailerContactModel().getRetailerId());
        txModel.setDistributorId(wrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);

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
        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel()
                .getPaymentModeId());

        wrapper.setTransactionModel(txModel);
        if(logger.isDebugEnabled())
        {
            logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of AgentCashDepositTransaction....");
        }

        return wrapper;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setSupplierManager(SupplierManager supplierManager) {
        this.supplierManager = supplierManager;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public void setProductDispenseController(ProductDispenseController productDispenseController) {
        this.productDispenseController = productDispenseController;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }
}
