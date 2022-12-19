package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
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
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.util.List;

/**
 * Created by Jawad on 05/16/2019.
 */
public class POSRefundTransaction extends SalesTransaction
{

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


    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

        // ------------------------Validates the Customer's requirements
        // -----------------------------------
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of POSRefund");
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
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of POSRefund Transaction");
        }
        return wrapper;

    }

    protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Inside doSale(WorkFlowWrapper wrapper) of POS Refund..");
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

//            if(wrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).toString().equals(FonePayConstants.APIGEE_CHANNEL)){
//                wrapper.getTransactionDetailMasterModel().setTerminalId(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
//            }
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

        }

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper)
            throws Exception {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of POSRefund Transaction....");
        }

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        wrapper = super.doPreStart(wrapper);

        // Populate the Product model from DB
        baseWrapper.setBasePersistableModel(wrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());


        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(wrapper.getUserDeviceAccountModel());

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setCustomerId( ThreadLocalAppUser.getAppUserModel().getCustomerId() );
        baseWrapper.setBasePersistableModel(sma);
        sma = smartMoneyAccountManager.loadSmartMoneyAccountModel(ThreadLocalAppUser.getAppUserModel());
        wrapper.setSmartMoneyAccountModel(sma);
        wrapper.setOlaSmartMoneyAccountModel(sma);


        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
        logger.info("[POSRefund Transaction.doPreStart]End loading business object models...");

        return wrapper;
    }

    public WorkFlowWrapper dispenseCustomer(WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException
    {

        try
        {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            SmartMoneyAccountModel customerSMA = workFlowWrapper.getOlaSmartMoneyAccountModel();


            if (null != customerSMA)
            {
                //Set agent smartmoneyAccount to load financialInstitution
                baseWrapper.setBasePersistableModel(customerSMA);
                AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

                SwitchWrapper switchWrapper = new SwitchWrapperImpl();

                switchWrapper.setBasePersistableModel(customerSMA) ;

                switchWrapper.setWorkFlowWrapper(workFlowWrapper) ;
                switchWrapper.getWorkFlowWrapper().getTransactionDetailModel().setCustomField1(""+customerSMA.getSmartMoneyAccountId()); //set customer's smart money account ID
                TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;

                logger.info("POS Refund Transaction.dispenseCustomer] Going to make FT to Customer A/C." +
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

    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

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
            logger.debug("Inside validateCommission of POSRefund Transaction...");
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

            logger.debug("Ending validateCommission of POSRefund Transaction...");
        }

    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper)
            throws Exception {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside doPreProcess(WorkFlowWrapper wrapper) of POSRefund Transaction....");
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
            logger.debug("Ending doPreProcess(WorkFlowWrapper wrapper) of POSRefund Transaction....");
        }

        return wrapper;
    }

    public Double getTransactionProcessingCharges(
            CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
        if(logger.isDebugEnabled())
        {
            logger.debug("Inside getTransactionProcessingCharges of POSRefund Transaction..");
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
            logger.debug("Ending getTransactionProcessingCharges of POS Refund Transaction...");
        }
        return transProcessingAmount;
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



    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setDtf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    public void setTf(DateTimeFormatter tf) {
        this.tf = tf;
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

    public void setCustomerManager(CustTransManager customerManager) {
        this.customerManager = customerManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setVeriflyController(VeriflyManagerService veriflyController) {
        this.veriflyController = veriflyController;
    }

    public void setSwitchController(SwitchController switchController) {
        this.switchController = switchController;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
        this.supplierBankInfoManager = supplierBankInfoManager;
    }

    public void setOperatorManager(OperatorManager operatorManager) {
        this.operatorManager = operatorManager;
    }

    public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }
}
