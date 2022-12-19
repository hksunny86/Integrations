package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
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
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.integration.vo.CashWithdrawalVO;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jawad on 09/16/2019.
 */
public class HRAToWalletTransaction extends SalesTransaction
{
    private MessageSource messageSource;
    private CommissionManager commissionManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private GenericDao genericDAO;
    private FinancialIntegrationManager financialIntegrationManager;


    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of HRAToWalletTransaction..");
        }

        wrapper.setSegmentModel(wrapper.getCustomerModel().getSegmentIdSegmentModel());

        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());


        commissionWrapper = commissionManager.calculateCommission(wrapper);

        if (logger.isDebugEnabled()) {
            logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of HRAToWalletTransaction...");
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
            logger.debug("Inside validateCommission of HRAToWalletTransaction...");
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
            logger.debug("Ending validateCommission of HRAToWalletTransaction...");
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
            logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of HRAToWalletTransaction");

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
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of HRAToWalletTransaction");

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


        wrapper.setCommissionAmountsHolder(commissionAmounts);

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel()); // Customer SMA
        AbstractFinancialInstitution abstractFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        wrapper.setCommissionAmountsHolder(commissionAmounts);
        wrapper.setCommissionWrapper(commissionWrapper);

        AppUserModel appUserModel = wrapper.getAppUserModel();
        wrapper.setAppUserModel(wrapper.getCustomerAppUserModel());

        // update workflow wrapper with required fields and make IVR request for customer verification

        wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
        wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

        wrapper.getTransactionModel().setCreatedOn(new Date());
        wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getCustomerAppUserModel().getMobileNo());

        txDetailModel.setActualBillableAmount(commissionAmounts.getTransactionAmount());
        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        txDetailModel.setConsumerNo(((CashWithdrawalVO) wrapper.getProductVO()).getCustomerMobileNo());

        txDetailModel.setSettled(false);
        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().setConfirmationMessage(" _ ");

        txDetailModel = wrapper.getTransactionDetailModel();

        wrapper.setCommissionAmountsHolder(commissionAmounts);
        wrapper.setCommissionWrapper(commissionWrapper);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

        //perform Cash withdrawal on OLA
        switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;


        wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getToAccountNo());

        wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getFromAccountNo());

        Double customerBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction(); // customer balance
        ((CashWithdrawalVO)wrapper.getProductVO()).setCustomerBalance(customerBalance);

        Double customerHRABalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction();
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

        txManager.saveTransaction(wrapper);

        settlementManager.settleCommission(commissionWrapper, wrapper);

        String brandName;
        if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){

            brandName=MessageUtil.getMessage("sco.brandName");
        }else {

            brandName= MessageUtil.getMessage("jsbl.brandName");

        }
        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
        String customerSMS=this.getMessageSource().getMessage(
                "TRANS.HRA2WALLET",
                new Object[] {
                        brandName,
                        wrapper.getTransactionCodeModel().getCode(),
                        Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                        PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                        Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionProcessingAmount()),
                        (customerHRABalance==null) ? 0.0 : customerHRABalance},
                null);

        wrapper.getTransactionDetailModel().setCustomField4(customerSMS);
        messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), customerSMS));
        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

        return wrapper;
    }

    /**
     * Update Workflow wrapper with different parameters required in further steps
     */
    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of HraCashWithdrawalTransaction....");

        BaseWrapper baseWrapper = new BaseWrapperImpl();

        wrapper = super.doPreStart(wrapper);


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
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());


        if (logger.isDebugEnabled())
            logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of HraCashWithdrawalTransaction....");

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        // prepare workflow wrapper with transaction model having all required inputs
        wrapper = super.doPreProcess(wrapper);

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

        // Smart Money Account Id is populated
        txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

        // Payment mode model of transaction is populated
        txModel.setPaymentModeId(wrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());

        txModel.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());

        // Populate processing Bank Id
        txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);
        txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());

        wrapper.setTransactionModel(txModel);

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    private Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
        if (logger.isDebugEnabled())
            logger.debug("Inside getTransactionProcessingCharges of HraCashWithdrawalTransaction....");

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
            logger.debug("Ending getTransactionProcessingCharges of HraCashWithdrawalTransaction....");

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

