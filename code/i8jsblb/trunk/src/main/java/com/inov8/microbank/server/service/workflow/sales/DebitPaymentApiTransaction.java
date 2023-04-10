package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.common.model.messagemodule.NovaAlertMessage;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productmodule.paymentservice.BookMeLog;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.dao.portal.bookmemodule.BookMeLogDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.SwitchController;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule.VeriflyManagerService;
import com.inov8.microbank.server.service.integration.dispenser.BillPaymentProductDispenser;
import com.inov8.microbank.server.service.integration.vo.AccountToAccountVO;
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.BookMeTransactionVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;
import com.inov8.microbank.server.service.productmodule.ProductDispenseController;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.stakeholdermodule.StakeholderBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.ola.integration.vo.OLAVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

public class DebitPaymentApiTransaction extends SalesTransaction {
    protected final Log log = LogFactory.getLog(getClass());
    private MessageSource messageSource;
    private DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    private DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");

    private String OLA_FROM_ACCOUNT = "OLA_FROM_ACCOUNT";
    private String OLA_TO_ACCOUNT = "OLA_TO_ACCOUNT";

    private SupplierManager supplierManager;
    private CommissionManager commissionManager;
    private ProductDispenseController productDispenseController;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private CustTransManager customerManager;
    private ProductManager productManager;
    //	private AppUserManager appUserManager;
    private VeriflyManagerService veriflyController;
    private SwitchController switchController;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private SupplierBankInfoManager supplierBankInfoManager;
    private OperatorManager operatorManager;
    private RetailerContactManager retailerContactManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private GenericDao genericDAO;
    private AbstractFinancialInstitution phoenixFinancialInstitution;
    private String rrnPrefix;
    private StakeholderBankInfoManager stakeholderBankInfoManager;
    private ActionLogManager actionLogManager = null;


    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
        // ------ Calculate the commission
        // ------------------------------------------------------

        //*************************************************************************************************************************
        if (ThreadLocalAppUser.getAppUserModel().getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER.longValue()) {
            CustomerModel custModel = new CustomerModel();
            custModel.setCustomerId(ThreadLocalAppUser.getAppUserModel().getCustomerId());
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(custModel);
            bWrapper = customerManager.loadCustomer(bWrapper);
            if (null != bWrapper.getBasePersistableModel()) {
                custModel = (CustomerModel) bWrapper.getBasePersistableModel();
                wrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
            }
        } else {
            SegmentModel segmentModel = new SegmentModel();
            segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
            wrapper.setSegmentModel(segmentModel);
        }
        //***********************************************************************************************
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());

		/*Double transacAmount = wrapper.getTransactionAmount();
		TransactionModel model = new TransactionModel();
		model.setTransactionAmount(transacAmount);
		wrapper.setTransactionModel(model);*/

        //wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
        wrapper.setTaxRegimeModel(wrapper.getCustomerModel().getTaxRegimeIdTaxRegimeModel());

        commissionWrapper = this.commissionManager.calculateCommission(wrapper);
        // --------------------------------------------------------------------------------------

        return commissionWrapper;
    }

    public void validateCommission(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) throws FrameworkCheckedException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside validateCommission of DebitPaymentTransaction...");
        }
        CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
                CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

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

    }

    public Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {


        Double transProcessingAmount = 0D;

        List<CommissionRateModel> resultSetList = (List) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_RATE_LIST);

        for (CommissionRateModel commissionRateModel : resultSetList) {

            if (commissionRateModel.getCommissionReasonId().longValue() == CommissionReasonConstants.ALLPAY_SERVICE_CHARGE.longValue()) {

                if (commissionRateModel.getCommissionTypeId().longValue() == CommissionConstantsInterface.FIXED_COMMISSION.longValue()) {

                    transProcessingAmount += commissionRateModel.getRate();
                } else {
                    transProcessingAmount += (workFlowWrapper.getBillAmount() * commissionRateModel.getRate()) / 100;
                }
            }
        }


        return transProcessingAmount;
    }


    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

        _workFlowWrapper = super.doPreStart(_workFlowWrapper);

        // Populate the Product model from DB
        baseWrapper.setBasePersistableModel(_workFlowWrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        _workFlowWrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());


        if (_workFlowWrapper.getCustomerAppUserModel() != null) {

            AppUserModel appUserModel = _workFlowWrapper.getCustomerAppUserModel();
            BaseWrapper _baseWrapper = new BaseWrapperImpl();
            SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();
            smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setActive(Boolean.TRUE);
            smartMoneyAccountModel.setDeleted(Boolean.FALSE);
            _baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            _baseWrapper = this.smartMoneyAccountManager.searchSmartMoneyAccount(_baseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel) _baseWrapper.getBasePersistableModel();

            _workFlowWrapper.putObject("CUSTOMER_SmartMoneyAccountModel", smartMoneyAccountModel);


            // For Agent Retention Commission calculation - reloading customerAppUserModel (to avoid the owning Session was closed issue)
            AppUserModel customerAppUserModel = new AppUserModel();
            customerAppUserModel.setMobileNo(_workFlowWrapper.getCustomerAppUserModel().getMobileNo());
            customerAppUserModel.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
            customerAppUserModel = appUserManager.getAppUserModel(customerAppUserModel);

            if (null != customerAppUserModel) {
                _workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
                _workFlowWrapper.setCustomerModel(customerAppUserModel.getCustomerIdCustomerModel());
            }
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

        // Populate Supplier Bank Info Model

        SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
        supplierBankInfoModel.setSupplierId(_workFlowWrapper.getProductModel().getSupplierId());

        supplierBankInfoModel = this.supplierBankInfoManager.getSupplierBankInfoModel(supplierBankInfoModel);

        _workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModel);

        OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();

        if (supplierBankInfoModel != null) {
            // Populate Operator's Paying Bank Info Model
            operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
            operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
            operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
            baseWrapper.setBasePersistableModel(operatorBankInfoModel);
            _workFlowWrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
        }

        // Populate Operator Bank Info Model

        operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
        operatorBankInfoModel.setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
        operatorBankInfoModel.setBankId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());
        baseWrapper.setBasePersistableModel(operatorBankInfoModel);
        _workFlowWrapper.setOperatorBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());

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
        transactionModel.setTransactionAmount(workFlowWrapper.getTransactionAmount());
        transactionModel.setTotalAmount(workFlowWrapper.getBillAmount());
        transactionModel.setTotalCommissionAmount(0d);
        transactionModel.setDiscountAmount(0d);
        transactionModel.setTransactionTypeIdTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
        transactionModel.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
        transactionModel.setPaymentModeId(workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
        transactionModel.setCustomerMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());

        Long smartMoneyAccountId = null;

        SmartMoneyAccountModel customerSmartMoneyAccountModel = (SmartMoneyAccountModel) workFlowWrapper.getObject("CUSTOMER_SmartMoneyAccountModel");
        smartMoneyAccountId = customerSmartMoneyAccountModel.getSmartMoneyAccountId();

        transactionModel.setSmartMoneyAccountId(smartMoneyAccountId);
        String mfsId = null;

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(workFlowWrapper.getCustomerAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        userDeviceAccountsModel = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
        mfsId = userDeviceAccountsModel.getUserId();

        transactionModel.setMfsId(mfsId);

        workFlowWrapper.setTransactionModel(transactionModel);

        return workFlowWrapper;
    }


    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {

        if (wrapper.getAppUserModel() != null) {
            if ("".equals(wrapper.getAppUserModel().getMobileNo())) {
                throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MOBILENO_NOT_SUPPLIED);
            }
        } else {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);
        }
		/*if (wrapper.getUserDeviceAccountModel() == null)
		{
			throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
		}*/

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


        if (wrapper.getOperatorPayingBankInfoModel() == null) {

//			throw new WorkFlowException(WorkFlowErrorCodeConstants.OPERATOR_BANK_INFO_NULL);
        }
        // ---------------------------------------------------------------------------------------

        // ------------------------- Validates the iNPUT's requirements
        // -----------------------------------

        ////***************************************************************************
        ////***************************************************************************
        ////***************************************************************************
        if (wrapper.getTransactionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
        }
		/*if (wrapper.getTotalAmount() < 0) {

			throw new WorkFlowException(
					WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NULL);
		}*/
        if (wrapper.getTotalCommissionAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);
        }
        if (wrapper.getTxProcessingAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);
        }

        ////***************************************************************************
        ////***************************************************************************
        ////***************************************************************************
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

        }
        if (logger.isDebugEnabled()) {
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of DebitPaymentTransaction");
        }
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        String notificationMobileNo = wrapper.getCustomerAppUserModel().getMobileNo();
        Integer paymentType = (Integer) wrapper.getCustomField();

        String consumerCnic = wrapper.getAppUserModel().getNic();

        CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
        CommissionAmountsHolder commissionAmounts =
                (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        if (wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE) != null) {
            wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode((String) wrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE));
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());

        wrapper.setCommissionAmountsHolder(commissionAmounts);
        wrapper.setCommissionWrapper(commissionWrapper);

        OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) financialIntegrationManager.loadFinancialInstitution(baseWrapper);


        wrapper.setCommissionWrapper(commissionWrapper);
        wrapper.setCommissionAmountsHolder(commissionAmounts);


        wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
        wrapper.getTransactionModel().setCreatedOn(new Date());
        wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
        txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        txDetailModel.setConsumerNo("");
        wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

        wrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);


        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().setConfirmationMessage(" _ ");

        txManager.saveTransaction(wrapper);
        //}

        String txAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTransactionAmount());
        String seviceChargesAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());

        /**
         * pull the bill information from the supplier, calculate commissions on bill and return in to iPos for the customer to accept or reject
         */

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doSale(WorkFlowWrapper wrapper) of DebitPaymentApiTransaction..");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Saving Transaction in DB....");
        }

        //Added  by Maqsood for Phoenix
        SmartMoneyAccountModel agentSmartMoneyAccountModel = wrapper.getOlaSmartMoneyAccountModel();

        SwitchWrapper switchWrapper = wrapper.getSwitchWrapper();
        switchWrapper.setSenderCNIC(consumerCnic);

        wrapper.setSwitchWrapper(switchWrapper);
        switchWrapper.setWorkFlowWrapper(wrapper);


        wrapper.setSwitchWrapper(switchWrapper);

        wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);

        txManager.saveTransaction(wrapper);


        txAmount = Formatter.formatDouble(wrapper.getTransactionModel().getTransactionAmount());
        seviceChargesAmount = Formatter.formatNumbers(wrapper.getTransactionModel().getTotalAmount() - wrapper.getTransactionModel().getTransactionAmount());


        wrapper.setTransactionDetailModel(txDetailModel);
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
        switchWrapper.setAccountInfoModel(wrapper.getAccountInfoModel());

        AppUserModel appUser = wrapper.getAppUserModel();
        wrapper.getTransactionModel().setSaleMobileNo(appUser.getMobileNo());

        TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel();

        ThreadLocalActionLog.setActionLogId(logActionLogModel());

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) wrapper.getOlaSmartMoneyAccountModel();

        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        switchWrapper = olaVeriflyFinancialInstitution.verifyCredentialsWithoutPin(switchWrapper);

        //Moving check balance before FT

        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getAccountInfoModel().getOldPin());
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
        switchWrapper.setBasePersistableModel(agentSmartMoneyAccountModel);
        switchWrapper.setWorkFlowWrapper(wrapper);
        wrapper.setSwitchWrapper(switchWrapper);
        wrapper.getTransactionModel().setCustomerMobileNo(null);
        wrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());
        wrapper.setSwitchWrapper(switchWrapper);

        /*
         *** 	SENDING BILL PAYMENT TO OLA BANKING.
         */
        logger.info("OLA > SENDING DEBIT PAYMENT TO OLA BANKING.");

        ProductModel productModel = wrapper.getProductModel();

        switchWrapper = olaVeriflyFinancialInstitution.debitPayment(switchWrapper);
        wrapper.getDataMap().put(OLA_FROM_ACCOUNT, wrapper.getSwitchWrapper().getFromAccountNo());
        wrapper.getDataMap().put(OLA_TO_ACCOUNT, wrapper.getSwitchWrapper().getToAccountNo());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp);
        switchWrapper.getWorkFlowWrapper().setProductModel(productModel);
        switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.setBasePersistableModel(wrapper.getOlaSmartMoneyAccountModel());
        switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp);
        wrapper.setSwitchWrapper(switchWrapper);
        wrapper.setOLASwitchWrapper(wrapper.getSwitchWrapper());

        txDetailModel.setSettled(Boolean.TRUE);

        if (wrapper.getFirstFTIntegrationVO() != null) {

            String bankResponseCode = wrapper.getFirstFTIntegrationVO().getResponseCode();

            if (bankResponseCode != null) {
                wrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
            }
        }


        SAVE_TRANSACTION:
        {

            TransactionModel transactionModel = wrapper.getTransactionModel();

            transactionModel.addTransactionIdTransactionDetailModel(txDetailModel);
            transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.BILL_AUTHORIZATION_SENT);
            transactionModel.setNotificationMobileNo(notificationMobileNo);
            wrapper.getTransactionModel().setBankAccountNo(wrapper.getOLASwitchWrapper().getFromAccountNo());


            WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
            workFlowWrapper.setTransactionModel(transactionModel);

            txManager.saveTransaction(workFlowWrapper);

            transactionModel = workFlowWrapper.getTransactionModel();

            wrapper.setTransactionModel(transactionModel);
            wrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
        }

        this.sendSms(wrapper, true);


        /* * * * 	SENDING BILL PAYMENT TO CORE BANKING. * * * */

        //_workFlowWrapper.setAppUserModel(appUserModel);
        wrapper.putObject("ACTION_LOG_ID", ThreadLocalActionLog.getActionLogId());

        Boolean isInclusiveChargesIncluded = wrapper.getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : Boolean.TRUE;
        Long utilityBillPoolAccountId = PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID;//Change to UTILITY_BILL_POOL_T24_ACCOUNT_ID
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(new StakeholderBankInfoModel(utilityBillPoolAccountId));

        searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);
        StakeholderBankInfoModel utilityBillPoolT24Account = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();
        switchWrapper.setFromAccountNo(utilityBillPoolT24Account.getAccountNo());
        switchWrapper.setSenderCNIC(consumerCnic);
        switchWrapper.setWorkFlowWrapper(wrapper);
        Double amount = wrapper.getTransactionModel().getTransactionAmount();

        if (!isInclusiveChargesIncluded) {

            amount -= (wrapper.getCommissionAmountsHolder().getInclusiveFixAmount() + wrapper.getCommissionAmountsHolder().getInclusivePercentAmount());
        }
//
        switchWrapper.setAmountPaid(Formatter.formatDouble(amount));

        TransactionModel transactionModel = wrapper.getTransactionModel();


        SETTLE_COMMISSION_BLOCK:
        {
            //Going to settle commissions using SettlementManager
            settlementManager.settleCommission(commissionWrapper, wrapper);
        }

        transactionModel.setCustomerMobileNo(notificationMobileNo);
        transactionModel.setNotificationMobileNo(notificationMobileNo);
        transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
        wrapper.setTransactionModel(transactionModel);
        txManager.saveTransaction(wrapper); // save the transaction
        return wrapper;

    }

    private void sendSms(WorkFlowWrapper _workFlowWrapper, Boolean isPayByAccount) throws FrameworkCheckedException {


        String customerMsgString = null;
        Object[] customerSMSParam = null;

        String brandName = null;
        if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
            brandName = MessageUtil.getMessage("sco.brandName");
        } else {

            brandName = MessageUtil.getMessage("jsbl.brandName");
        }
        String trxCode = _workFlowWrapper.getTransactionCodeModel().getCode();
        String totalAmount = Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTotalAmount());
        String customerBalance = Formatter.formatDouble(_workFlowWrapper.getSwitchWrapper().getOlavo().getFromBalanceAfterTransaction());
        String productName = _workFlowWrapper.getProductModel().getName();
        String charges = Formatter.formatDouble(_workFlowWrapper.getCommissionAmountsHolder().getTransactionProcessingAmount());
        String consumer = _workFlowWrapper.getTransactionDetailModel().getConsumerNo();
        String date = dtf.print(new DateTime());
        String time = tf.print(new LocalTime());

        Long categoryId = 0L;
        Long POST_PAID = 5L;
        Long PRE_PAID = 6L;

        if (_workFlowWrapper.getProductModel().getCategoryId() != null) {
            categoryId = _workFlowWrapper.getProductModel().getCategoryId();
        }

        if (categoryId != POST_PAID.longValue() && categoryId != PRE_PAID.longValue()) {
            if(_workFlowWrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID) != null && _workFlowWrapper.getObject(CommandFieldConstants.KEY_CHANNEL_ID).equals("NOVA")) {
                String names = MessageUtil.getMessage("nova.subscriptions.product.ids");
                List<String> items = Arrays.asList(names.split("\\s*,\\s*"));

//                String pNames = MessageUtil.getMessage("nova.Transfer.product.ids");
//                List<String> pItems = Arrays.asList(pNames.split("\\s*,\\s*"));

                String iNames = MessageUtil.getMessage("nova.investment.product.ids");
                List<String> iItems = Arrays.asList(iNames.split("\\s*,\\s*"));

                String vNames = MessageUtil.getMessage("nova.voucher.product.ids");
                List<String> vItems = Arrays.asList(vNames.split("\\s*,\\s*"));

                if (items.contains(_workFlowWrapper.getProductModel().getProductId().toString())) {
                    customerMsgString = "nova.subscriptions.msg";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
                    customerSMSParam = new Object[]{trxCode, productName, totalAmount, date, customerBalance};
                }

//                if (pItems.contains(_workFlowWrapper.getProductModel().getProductId().toString())) {
//                    customerMsgString = "nova.transfer.products.msg";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
//                    customerSMSParam = new Object[]{totalAmount, _workFlowWrapper.getAppUserModel().getFirstName()+ " "+ _workFlowWrapper.getAppUserModel().getLastName()};
//                }

                if (iItems.contains(_workFlowWrapper.getProductModel().getProductId().toString())) {
                    customerMsgString = "nova.investment.products.msg";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
                    customerSMSParam = new Object[]{trxCode, productName, totalAmount, date, customerBalance};
                }

                if (vItems.contains(_workFlowWrapper.getProductModel().getProductId().toString())) {
                    customerMsgString = "nova.voucher.products.msg";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
                    customerSMSParam = new Object[]{trxCode, productName, totalAmount, date, customerBalance};
                }

                if(_workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.STOCK_PURCHASED)){
                    customerMsgString = "nova.stock.product.msg";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
                    customerSMSParam = new Object[]{brandName, trxCode, totalAmount, time, date, customerBalance};
                }
                if (_workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.LOAN_XTRA_CASH_REPAYMENT)){
                    customerMsgString = "advanceCashPaid.SMS";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
                    customerSMSParam = new Object[]{brandName, trxCode, productName, totalAmount, time, date, charges, customerBalance};
                }

            }
            else {
                if(_workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.STOCK_PURCHASED)){
                    customerMsgString = "nova.stock.product.msg";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
                    customerSMSParam = new Object[]{brandName, trxCode, totalAmount, time, date, customerBalance};
                }
                else if (_workFlowWrapper.getProductModel().getProductId().equals(ProductConstantsInterface.LOAN_XTRA_CASH_REPAYMENT)){
                    customerMsgString = "advanceCashPaid.SMS";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
                    customerSMSParam = new Object[]{brandName, trxCode, productName, totalAmount, time, date, charges, customerBalance};
                }
                else {

                    customerMsgString = "ubp.paybyaccount.customer";
                    customerSMSParam = new Object[]{brandName, trxCode, productName, consumer, totalAmount, charges, time, date, customerBalance};
                }
            }

        } else if (categoryId == POST_PAID.longValue()) {


            customerMsgString = "ubp.paybyaccount.postpaid.customer";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
            customerSMSParam = new Object[]{brandName, trxCode, productName, consumer, totalAmount, charges, brandName, time, date, customerBalance};

        } else if (categoryId == PRE_PAID.longValue()) {


            customerMsgString = "ubp.paybyaccount.prepaid.customer";
            customerSMSParam = new Object[]{brandName, trxCode, totalAmount, consumer, time, date, customerBalance};

        }

        if(customerMsgString != null) {
            String customerSMS = this.getMessageSource().getMessage(customerMsgString, customerSMSParam, null);

            SmsMessage customerSMSMessage = new SmsMessage(_workFlowWrapper.getCustomerAppUserModel().getMobileNo(), customerSMS);
            NovaAlertMessage customerNovaAlertSMSMessage = new NovaAlertMessage(_workFlowWrapper.getCustomerAppUserModel().getMobileNo(), customerSMS,"","","","");


            _workFlowWrapper.getTransactionDetailModel().setCustomField8(customerSMS);
            _workFlowWrapper.getTransactionModel().setNotificationMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());//todo
            _workFlowWrapper.getTransactionModel().setConfirmationMessage(customerSMS);

            ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
            ArrayList<NovaAlertMessage> messageList2 = new ArrayList<NovaAlertMessage>(0);
            messageList.add(customerSMSMessage);
            messageList2.add(customerNovaAlertSMSMessage);


            _workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
            _workFlowWrapper.putObject(CommandFieldConstants.KEY_NOVA_ALERT_SMS_MESSAGES, messageList2);

        }
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

        if (baseWrapperActionLog != null && baseWrapperActionLog.getBasePersistableModel() != null) {

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
    protected WorkFlowWrapper doPreEnd(WorkFlowWrapper wrapper) throws Exception {
        updateTransactionDetailMasterForTransaction(wrapper);
        settlementManager.prepareDataForDayEndSettlement(wrapper);
        Long utilityBillPoolAccountId = null;
        String names = MessageUtil.getMessage("product.ids");
        List<String> items = Arrays.asList(names.split("\\s*,\\s*"));
        String cardProductsNames=MessageUtil.getMessage("cardPayment.product.ids");
        List<String> cardProductItems = Arrays.asList(cardProductsNames.split("\\s*,\\s*"));
        if (items.contains(wrapper.getProductModel().getProductId().toString())) {
            utilityBillPoolAccountId=PoolAccountConstantsInterface.OF_SETTLEMENT_IFT_POOL_ACCOUNT;
        }else if (cardProductItems.contains(wrapper.getProductModel().getProductId().toString())){
            utilityBillPoolAccountId=PoolAccountConstantsInterface.CARD_PAYMENT_OF_ACCOUNT_ID;
        }
        else {
            List<StakeholderBankInfoModel> stakeholderBankInfoModelList = this.stakeholderBankInfoManager.getStakeholderBankInfoForProductandAccountType(wrapper.getProductModel().getProductId(), "OF_SET");
            if (stakeholderBankInfoModelList != null) {
                utilityBillPoolAccountId = Long.valueOf(stakeholderBankInfoModelList.get(0).getStakeholderBankInfoId());
            }
        }
        prepareAndSaveSettlementTransactionRDV(wrapper.getTransactionModel().getTransactionId(),
                wrapper.getProductModel().getProductId(),
                wrapper.getSwitchWrapper().getTransactionAmount(),
                PoolAccountConstantsInterface.JSBL_OF_SETTLEMENT_POOL_ACCOUNT_ID,
                utilityBillPoolAccountId);

        return wrapper;
    }


    private void updateTransactionDetailMasterForTransaction(WorkFlowWrapper wrapper) throws Exception {
        logger.info("[TransactionProcessor.updateTransactionDetailMasterForTransaction] Updating Transaction Detail Master.");
        wrapper = this.prepareTrxDetailMasterModelForAgentNetworkChange(wrapper);
        TransactionDetailMasterModel txDetailMaster = wrapper.getTransactionDetailMasterModel();
        txDetailMaster.setRecipientAccountNo(wrapper.getRecipientAccountNo());
        if (wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID) != null && !wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).equals(""))
            txDetailMaster.setTerminalId((String) wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID));
        // Skip in case of Commission Settlement Transaction type
        if (wrapper.getTransactionTypeModel() != null & wrapper.getTransactionTypeModel().getTransactionTypeId().longValue() != TransactionTypeConstantsInterface.COMMISSION_SETTLEMENT) {

            //if :new.transaction_type_id <> 19 then
            if (wrapper.getProductModel() != null && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CASH_TRANSFER
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.CNIC_TO_CORE_ACCOUNT
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.IBFT
                    && wrapper.getProductModel().getProductId().longValue() != ProductConstantsInterface.WEB_SERVICE_CASH_IN) {

                txDetailMaster.setMfsId(wrapper.getTransactionModel().getMfsId());
                txDetailMaster.setSaleMobileNo(wrapper.getTransactionModel().getSaleMobileNo());

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
                        txDetailMaster.setSenderCnic(appUser.getNic());
                    }
                }

                if (wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.CNIC_TO_BB_ACCOUNT.longValue()) {

                    txDetailMaster.setSenderCnic(wrapper.getWalkInCustomerCNIC());
                }
                if (wrapper.getAppUserModel() != null &&
                        (wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.WEB_SERVICE_PAYMENT_TX) || wrapper.getTransactionTypeModel().getTransactionTypeId().equals(TransactionTypeConstantsInterface.VIRTUAL_CARD_PAYMENT_TX))) {
                    txDetailMaster.setMfsId(wrapper.getUserDeviceAccountModel().getUserId());
                    txDetailMaster.setSenderCnic(wrapper.getAppUserModel().getNic());
                    txDetailMaster.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());
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
                    if (wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC) != null)
                        txDetailMaster.setRecipientCnic(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
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
            txDetailMaster.setMfsId(wrapper.getTransactionModel().getMfsId());
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
            if (wrapper.getProductModel().getSupplierId().longValue() == SupplierConstants.TransReportPhonixCSRViewSupplierID
           || wrapper.getProductModel().getSupplierId().longValue() == SupplierConstants.BRANCHLESS_BANKING_SUPPLIER
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.APOTHECARE_PAYMENT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_ZAKAT_PAYMENT
                    || wrapper.getProductModel().getProductId().longValue() == ProductConstantsInterface.DAWAT_E_ISLAMI_SADQA_PAYMENT) {

                txDetailMaster.setConsumerNo(wrapper.getTransactionDetailModel().getConsumerNo());
                txDetailMaster.setRecipientMobileNo(null);
                txDetailMaster.setRecipientMfsId(null);
                txDetailMaster.setRecipientCnic(null);

            }

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

    @Override
    public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws Exception {

        MARK_TRANSACTION_FAILED:
        {

            workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);
            workFlowWrapper.getTransactionModel().setTransactionId(null);
//			workFlowWrapper.getTransactionDetailModel().setTransactionDetailId(null);
            workFlowWrapper.getTransactionModel().setConfirmationMessage(" ");
//			txManager.saveTransaction(workFlowWrapper);
        }


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(workFlowWrapper.getOlaSmartMoneyAccountModel());

        OLAVeriflyFinancialInstitutionImpl financialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        String olaFromAccount = (String) workFlowWrapper.getDataMap().get(OLA_FROM_ACCOUNT);
        String olaToAccount = (String) workFlowWrapper.getDataMap().get(OLA_TO_ACCOUNT);

        if (!StringUtil.isNullOrEmpty(olaFromAccount) && !StringUtil.isNullOrEmpty(olaToAccount)) {

            OLAVO olaVO = workFlowWrapper.getSwitchWrapper().getOlavo();
            olaVO.setPayingAccNo(olaToAccount);
            olaVO.setReceivingAccNo(olaFromAccount);

            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
            switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
            switchWrapper.setBasePersistableModel(workFlowWrapper.getOlaSmartMoneyAccountModel());
            switchWrapper.setBankId(workFlowWrapper.getOlaSmartMoneyAccountModel().getBankId());
            switchWrapper.setTransactionAmount(workFlowWrapper.getBillAmount());
            switchWrapper.setWorkFlowWrapper(workFlowWrapper);
            switchWrapper.setFromAccountNo(olaToAccount);
            switchWrapper.setToAccountNo(olaFromAccount);
            switchWrapper.setOlavo(olaVO);

            financialInstitution.rollback(switchWrapper);
        }

        return workFlowWrapper;
    }


    @Override
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper) {
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

    public void setPhoenixFinancialInstitution(AbstractFinancialInstitution phoenixFinancialInstitution) {
        this.phoenixFinancialInstitution = phoenixFinancialInstitution;
    }

    public void setStakeholderBankInfoManager(StakeholderBankInfoManager stakeholderBankInfoManager) {
        this.stakeholderBankInfoManager = stakeholderBankInfoManager;
    }

    public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
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

    public void setProductDispenseController(ProductDispenseController productDispenseController) {
        this.productDispenseController = productDispenseController;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setSwitchController(SwitchController switchController) {
        this.switchController = switchController;
    }

    public void setVeriflyController(VeriflyManagerService veriflyController) {
        this.veriflyController = veriflyController;
    }

    public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
        this.supplierBankInfoManager = supplierBankInfoManager;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    public void setOperatorManager(OperatorManager operatorManager) {
        this.operatorManager = operatorManager;
    }
}
