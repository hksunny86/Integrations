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
import com.inov8.microbank.server.dao.portal.bookmemodule.BookMeTransactionDetailDAO;
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
import com.inov8.microbank.server.service.integration.vo.BillPaymentVO;
import com.inov8.microbank.server.service.integration.vo.BookMeTransactionVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

public class BookMeTransaction extends SalesTransaction {

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
    private ESBAdapter esbAdapter;
    private BookMeLogDAO bookMeLogDAO;

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }


    //    public WorkFlowWrapper getBillInfo(WorkFlowWrapper wrapper) throws Exception {
//
//
//        BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
//        logger.debug("Fetching Bill Info through Product Dispenser...");
//        return billSaleProductDispenser.getBillInfo(wrapper);
//    }
//
//
//
//    public WorkFlowWrapper validateBillInfo(WorkFlowWrapper wrapper) throws FrameworkCheckedException {
//
//
//        BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(wrapper);
//        wrapper = billSaleProductDispenser.verify(wrapper);
//
//
//        return wrapper;
//    }


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
            logger.debug("Inside validateCommission of BookMeTransaction...");
        }
        CommissionAmountsHolder commissionHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
                CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        UserDeviceAccountsModel userDeviceAccountsModel = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();
        BillPaymentVO productVO = (BillPaymentVO) workFlowWrapper.getProductVO();

//		if (productVO.getCurrentBillAmount().doubleValue() != workFlowWrapper.getBillAmount().doubleValue())
//		{
//
//			throw new WorkFlowException(WorkFlowErrorCodeConstants.BILL_AMOUNT_NOT_MATCHED);
//		}
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

    @Override
    public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    public WorkFlowWrapper updateSupplier(WorkFlowWrapper wrapper) {
        return wrapper;
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
        transactionModel.setTransactionAmount(workFlowWrapper.getBillAmount());
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
        if (wrapper.getBillAmount() < 0) {

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
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of BookMeTransaction");
        }
        return wrapper;

    }

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper _workFlowWrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        String notificationMobileNo = _workFlowWrapper.getCustomerAppUserModel().getMobileNo();
        Integer paymentType = (Integer) _workFlowWrapper.getCustomField();

        String consumerCnic = _workFlowWrapper.getAppUserModel().getNic();

        CommissionWrapper commissionWrapper = this.calculateCommission(_workFlowWrapper);
        CommissionAmountsHolder commissionAmounts =
                (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        if (_workFlowWrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE) != null) {
            _workFlowWrapper.getTransactionDetailMasterModel().setFonepayTransactionCode((String) _workFlowWrapper.getObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE));
        }
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());

        _workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
        _workFlowWrapper.setCommissionWrapper(commissionWrapper);

        OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        //olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(_workFlowWrapper);



		/*AppUserModel appUserModel = _workFlowWrapper.getAppUserModel();
		_workFlowWrapper.setAppUserModel(_workFlowWrapper.getCustomerAppUserModel());
			*/
        //olaVeriflyFinancialInstitution.checkDebitCreditLimitOLAVO(_workFlowWrapper);
        //_workFlowWrapper.setAppUserModel(appUserModel);


//		this.validateCommission(commissionWrapper, _workFlowWrapper);

        _workFlowWrapper.setCommissionWrapper(commissionWrapper);
        _workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);


        _workFlowWrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        _workFlowWrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount() + commissionAmounts.getExclusiveFixAmount() + commissionAmounts.getExclusivePercentAmount());
        _workFlowWrapper.getTransactionModel().setCreatedOn(new Date());
        _workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.IN_PROGRESS);
        txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
        txDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
        txDetailModel.setConsumerNo(((BookMeTransactionVO) _workFlowWrapper.getProductVO()).getOrderRefId());
        _workFlowWrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

        _workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);


        _workFlowWrapper.setTransactionDetailModel(txDetailModel);
        _workFlowWrapper.getTransactionModel().setConfirmationMessage(" _ ");

        txManager.saveTransaction(_workFlowWrapper);
        //}

        String txAmount = Formatter.formatNumbers(_workFlowWrapper.getTransactionModel().getTransactionAmount());
        String seviceChargesAmount = Formatter.formatNumbers(_workFlowWrapper.getTransactionModel().getTotalAmount() - _workFlowWrapper.getTransactionModel().getTransactionAmount());

        /**
         * pull the bill information from the supplier, calculate commissions on bill and return in to iPos for the customer to accept or reject
         */

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doSale(WorkFlowWrapper wrapper) of BookMeTransaction..");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Saving Transaction in DB....");
        }

        //Added  by Maqsood for Phoenix
        SmartMoneyAccountModel agentSmartMoneyAccountModel = _workFlowWrapper.getOlaSmartMoneyAccountModel();

        SwitchWrapper switchWrapper = _workFlowWrapper.getSwitchWrapper();
        switchWrapper.setSenderCNIC(consumerCnic);

        _workFlowWrapper.setSwitchWrapper(switchWrapper);
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);

        // Disabled fetch bill info on JSBL request [22-Sep-2016]
        //_workFlowWrapper = this.getBillInfo(_workFlowWrapper);

/***********/// need to prepare productVO from infoCommand


        _workFlowWrapper.setSwitchWrapper(switchWrapper);

        _workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.FAILED);

        txManager.saveTransaction(_workFlowWrapper);

        //for walkin customer cash deposit
		/*if(null != _workFlowWrapper.getWalkInCustomerMob() && !"".equals(_workFlowWrapper.getWalkInCustomerMob())) {
			txDetailModel.setCustomField6(_workFlowWrapper.getWalkInCustomerMob());
		}

		if(null != _workFlowWrapper.getWalkInCustomerCNIC() && !"".equals(_workFlowWrapper.getWalkInCustomerCNIC())) {
			txDetailModel.setCustomField7(_workFlowWrapper.getWalkInCustomerCNIC());
		}*/


        txAmount = Formatter.formatDouble(_workFlowWrapper.getTransactionModel().getTransactionAmount());
        seviceChargesAmount = Formatter.formatNumbers(_workFlowWrapper.getTransactionModel().getTotalAmount() - _workFlowWrapper.getTransactionModel().getTransactionAmount());


        _workFlowWrapper.setTransactionDetailModel(txDetailModel);


        if (!_workFlowWrapper.getTransactionDetailModel().getConsumerNo().equalsIgnoreCase("")) {

            _workFlowWrapper.getTransactionModel().setConfirmationMessage(
                    SMSUtil.buildBillSaleSMS(_workFlowWrapper.getInstruction().getSmsMessageText(), _workFlowWrapper.getProductModel().getName(), txAmount, seviceChargesAmount,
                            _workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode(), _workFlowWrapper.getTransactionDetailModel().getConsumerNo()));
        } else {
            _workFlowWrapper.getTransactionModel().setConfirmationMessage(
                    SMSUtil.buildVariableProductSMS(_workFlowWrapper.getInstruction().getSmsMessageText(), txAmount, _workFlowWrapper.getTransactionModel()
                            .getTransactionCodeIdTransactionCodeModel().getCode(), _workFlowWrapper.getProductModel().getHelpLineNotificationMessageModel()
                            .getSmsMessageText()));
        }

        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
        switchWrapper.setAccountInfoModel(_workFlowWrapper.getAccountInfoModel());

        if (logger.isDebugEnabled()) {
            logger.debug("Executing Bill Sale on BillSaleProductDispenser....");
        }

        BillPaymentProductDispenser billSaleProductDispenser = (BillPaymentProductDispenser) this.productDispenseController.loadProductDispenser(_workFlowWrapper);


        //AppUserModel appUserModel = ((isCustomerTransaction) ? _workFlowWrapper.getCustomerAppUserModel() : _workFlowWrapper.getAppUserModel());

        AppUserModel appUser = _workFlowWrapper.getAppUserModel();
        _workFlowWrapper.getTransactionModel().setSaleMobileNo(appUser.getMobileNo());

        TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel();

        ThreadLocalActionLog.setActionLogId(logActionLogModel());

        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel) _workFlowWrapper.getOlaSmartMoneyAccountModel();

        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setBankId(smartMoneyAccountModel.getBankId());
        switchWrapper.setPaymentModeId(smartMoneyAccountModel.getPaymentModeId());
        switchWrapper = olaVeriflyFinancialInstitution.verifyCredentialsWithoutPin(switchWrapper);

        //Moving check balance before FT

        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.putObject(CommandFieldConstants.KEY_PIN, switchWrapper.getAccountInfoModel().getOldPin());
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
        switchWrapper.setBasePersistableModel(agentSmartMoneyAccountModel);
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        _workFlowWrapper.setSwitchWrapper(switchWrapper);
        _workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);
        _workFlowWrapper.setAccountInfoModel(switchWrapper.getAccountInfoModel());


        transactionModelTemp.setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());

        switchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp);

        _workFlowWrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());

        transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel();

        rrnPrefix = buildRRNPrefix();


        _workFlowWrapper.setSwitchWrapper(switchWrapper);

        /*
         *** 	SENDING BILL PAYMENT TO OLA BANKING.
         */
        logger.info("OLA > SENDING BILL PAYMENT TO OLA BANKING.");

        ProductModel productModel = _workFlowWrapper.getProductModel();

        _workFlowWrapper = billSaleProductDispenser.doSale(_workFlowWrapper);

        _workFlowWrapper.getDataMap().put(OLA_FROM_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getFromAccountNo());
        _workFlowWrapper.getDataMap().put(OLA_TO_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getToAccountNo());

        switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp);

        switchWrapper.getWorkFlowWrapper().setProductModel(productModel);

        //The following code is for Phoenix implementation

        switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.setBasePersistableModel(_workFlowWrapper.getOlaSmartMoneyAccountModel());

        billSaleProductDispenser.prepairSwitchWrapper(_workFlowWrapper.getProductModel().getProductId(), switchWrapper);

        switchWrapper.getWorkFlowWrapper().setTransactionModel(transactionModelTemp);
        _workFlowWrapper.setSwitchWrapper(switchWrapper);
        _workFlowWrapper.setOLASwitchWrapper(_workFlowWrapper.getSwitchWrapper());

        txDetailModel.setSettled(Boolean.TRUE);

        if (_workFlowWrapper.getFirstFTIntegrationVO() != null) {

            String bankResponseCode = _workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();

            if (bankResponseCode != null) {
                _workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
            }
        }


        SAVE_TRANSACTION:
        {

            TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();

            transactionModel.addTransactionIdTransactionDetailModel(txDetailModel);
            transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.BILL_AUTHORIZATION_SENT);
            transactionModel.setNotificationMobileNo(notificationMobileNo);

            WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
            workFlowWrapper.setTransactionModel(transactionModel);

            txManager.saveTransaction(workFlowWrapper);

            transactionModel = workFlowWrapper.getTransactionModel();

            _workFlowWrapper.setTransactionModel(transactionModel);
            _workFlowWrapper.setTransactionCodeModel(transactionModel.getTransactionCodeIdTransactionCodeModel());
        }

        this.sendSms(_workFlowWrapper, true);


        /* * * * 	SENDING BILL PAYMENT TO CORE BANKING. * * * */

        //_workFlowWrapper.setAppUserModel(appUserModel);
        _workFlowWrapper.putObject("ACTION_LOG_ID", ThreadLocalActionLog.getActionLogId());

        Boolean isInclusiveChargesIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : Boolean.TRUE;
        Long utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_BILL_POOL_T24_ACCOUNT_ID;//Change to UTILITY_BILL_POOL_T24_ACCOUNT_ID
        if (OneBillProductEnum.contains(_workFlowWrapper.getProductModel().getProductId().toString()))
            utilityBillPoolAccountId = PoolAccountConstantsInterface.UTILITY_ONE_BILL_POOL_T24_ACCOUNT_ID;

        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(new StakeholderBankInfoModel(utilityBillPoolAccountId));

        searchBaseWrapper = this.stakeholderBankInfoManager.loadStakeHolderBankInfo(searchBaseWrapper);

        StakeholderBankInfoModel utilityBillPoolT24Account = (StakeholderBankInfoModel) searchBaseWrapper.getBasePersistableModel();

        switchWrapper.setFromAccountNo(utilityBillPoolT24Account.getAccountNo());
        switchWrapper.setSenderCNIC(consumerCnic);
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setPaymentModeId(SwitchConstants.CORE_BANKING_SWITCH);

        Double amount = _workFlowWrapper.getTransactionModel().getTransactionAmount();

        if (!isInclusiveChargesIncluded) {

            amount -= (_workFlowWrapper.getCommissionAmountsHolder().getInclusiveFixAmount() + _workFlowWrapper.getCommissionAmountsHolder().getInclusivePercentAmount());
        }

        switchWrapper.setAmountPaid(Formatter.formatDouble(amount));

        TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();

//        logger.info("Middleware > SENDING BILL PAYMENT TO Core Integration for Authorization of Bill By Queue. \n From : "+switchWrapper.getFromAccountNo()+"\n To :"+switchWrapper.getToAccountNo());
//
//        switchWrapper = phoenixFinancialInstitution.pushBillPayment(switchWrapper);
//        _workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
//        _workFlowWrapper.setTransactionModel(transactionModel);

        SETTLE_COMMISSION_BLOCK:
        {
            //Going to settle commissions using SettlementManager
            settlementManager.settleCommission(commissionWrapper, _workFlowWrapper);
        }


//        if(_workFlowWrapper.getFirstFTIntegrationVO() != null) {
//
//            String bankResponseCode = _workFlowWrapper.getFirstFTIntegrationVO().getResponseCode();
//
//            if(bankResponseCode != null) {
//                _workFlowWrapper.getTransactionModel().setBankResponseCode(bankResponseCode);
//            }
//        }

        transactionModel.setCustomerMobileNo(notificationMobileNo);
        transactionModel.setNotificationMobileNo(notificationMobileNo);
        transactionModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.COMPLETED);
        _workFlowWrapper.setTransactionModel(transactionModel);

        txManager.saveTransaction(_workFlowWrapper); // save the transaction
        I8SBSwitchControllerRequestVO requestVo = new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
        I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForJsBookMe(I8SBConstants.RequestType_BookMeIPN);
        BookMeTransactionVO bookMeTransactionVO = (BookMeTransactionVO) _workFlowWrapper.getProductVO();
        requestVO.setOrderRefId(bookMeTransactionVO.getOrderRefId());
        requestVO.setType(bookMeTransactionVO.getServiceType());
        requestVO.setMobileNumber(bookMeTransactionVO.getCustomerMobileNo());
        requestVO.setAmount(String.valueOf(bookMeTransactionVO.getTransactionAmount()));
        requestVO.setStatus("confirm");
        requestVo.setPartnerId(switchWrapper.getTransactionTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode());


        BookMeLog bookMeLog=new BookMeLog();
        bookMeLog.setBookMeStatus(bookMeTransactionVO.getBookMeStatus());
        bookMeLog.setTransactionAmount( bookMeTransactionVO.getTransactionAmount().longValue() ); ;
        bookMeLog.setCustomerName( bookMeTransactionVO.getCustomerName() );
        bookMeLog.setMicrobankTxCode( _workFlowWrapper.getTransactionModel().getTransactionCodeIdTransactionCodeModel().getCode() ) ;
        bookMeLog.setPaidAmount( _workFlowWrapper.getTransactionModel().getTransactionAmount().longValue() ) ;
        bookMeLog.setTransactionProcessingAmount(bookMeTransactionVO.getTransactionProcessingAmount());
        bookMeLog.setServiceProviderName(bookMeTransactionVO.getServiceProviderName());
        bookMeLog.setOrderRefId(bookMeTransactionVO.getOrderRefId());
        bookMeLog.setServiceType(bookMeTransactionVO.getServiceType());
        bookMeLog.setTotalAmount(bookMeTransactionVO.getTotalAmount());
        bookMeLog.setBillAmount(bookMeTransactionVO.getTransactionAmount());
        bookMeLog.setPhoneNo(bookMeTransactionVO.getCustomerMobileNo());
        bookMeLog.setBaseFare(bookMeTransactionVO.getBaseFare());
        bookMeLog.setTax(bookMeTransactionVO.getTax());
        bookMeLog.setFee(bookMeTransactionVO.getFee());
        bookMeLog.setDiscount(bookMeTransactionVO.getDiscount());
        bookMeLog.setPaidDate( new Date() ) ;
        bookMeLog.setBookMeCustomerCnic(bookMeTransactionVO.getBookMeCustomerCnic());
        bookMeLog.setBookMeCustomerEmail(bookMeTransactionVO.getBookMeCustomerEmail());
        bookMeLog.setBookMeCustomerMobileNo(bookMeTransactionVO.getBookMeCustomerMobileNo());
        bookMeLog.setBookMeCustomerName(bookMeTransactionVO.getBookMeCustomerName());

        _workFlowWrapper.setBasePersistableModel(bookMeLog);
        txManager.bookMeTransactionRequiresNewTransaction(_workFlowWrapper);
//Params To be Added
        SwitchWrapper sWrapper = new SwitchWrapperImpl();
        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        _workFlowWrapper.putObject("SWITCH_WRAPPER", sWrapper);
        sWrapper = (SwitchWrapper) _workFlowWrapper.getObject("SWITCH_WRAPPER");
        sWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
        _workFlowWrapper.putObject("IS_CALL_SENT", "1");
        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

        if (responseVO != null && responseVO.getResponseCode() != null) {
            String responseCode = responseVO.getResponseCode();
            String errorMessage = null;
            if (responseCode.equals("422") || responseCode.equals("500") || responseCode.equals("I8SB-400")) {
                if (responseCode.equals("I8SB-400"))
                    responseCode = "400";
                if (responseVO.getError() != null) {
                    errorMessage = responseVO.getError();
                    bookMeTransactionVO.setBookMeStatus(errorMessage);
                    bookMeLog.setBookMeStatus(bookMeTransactionVO.getBookMeStatus());
                    baseWrapper.setBasePersistableModel(bookMeLog);
                    txManager.bookMeTransactionRequiresNewTransaction(_workFlowWrapper);
                } else {
                    errorMessage = responseVO.getDescription();
                    bookMeTransactionVO.setBookMeStatus(errorMessage);
                    bookMeLog.setBookMeStatus(bookMeTransactionVO.getBookMeStatus());
                    baseWrapper.setBasePersistableModel(bookMeLog);
                    txManager.bookMeTransactionRequiresNewTransaction(_workFlowWrapper);
                }

                throw new CommandException(errorMessage, Long.valueOf(responseCode), ErrorLevel.MEDIUM, null);
            }
            if (sWrapper.getI8SBSwitchControllerResponseVO().getResponseCode().equals("I8SB-500")) {
                sWrapper.getI8SBSwitchControllerResponseVO().setResponseCode("500");
                sWrapper.getI8SBSwitchControllerResponseVO().setStatus("Pending");

                bookMeTransactionVO.setBookMeStatus(sWrapper.getI8SBSwitchControllerResponseVO().getStatus());
                bookMeLog.setBookMeStatus(bookMeTransactionVO.getBookMeStatus());
                baseWrapper.setBasePersistableModel(bookMeLog);
                txManager.bookMeTransactionRequiresNewTransaction(_workFlowWrapper);
                ESBAdapter.processI8sbResponseCodeForBookMe(sWrapper.getI8SBSwitchControllerResponseVO(), false);
            } else {
                bookMeTransactionVO.setBookMeStatus("Completed");
                bookMeLog.setBookMeStatus(bookMeTransactionVO.getBookMeStatus());
                baseWrapper.setBasePersistableModel(bookMeLog);
                txManager.bookMeTransactionRequiresNewTransaction(_workFlowWrapper);
                ESBAdapter.processI8sbResponseCodeForBookMe(sWrapper.getI8SBSwitchControllerResponseVO(), false);
            }
        } else {
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb responseVO is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        }


        if (logger.isDebugEnabled()) {

            logger.debug("Ending doSale of BookMeTransaction.");
        }


        return _workFlowWrapper;

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


            customerMsgString = "ubp.paybyaccount.customer";
            customerSMSParam = new Object[]{brandName, trxCode, productName, consumer, totalAmount, charges, time, date, customerBalance};


        } else if (categoryId == POST_PAID.longValue()) {


            customerMsgString = "ubp.paybyaccount.postpaid.customer";//{0}\nTrx ID: {1}\nYou have successfully paid your bill for {2} {3}\nRs.{4}\nfrom {5} agent at {6}\non {7}\nAvl Bal: Rs.{8}
            customerSMSParam = new Object[]{brandName, trxCode, productName, consumer, totalAmount, charges, brandName, time, date, customerBalance};

        } else if (categoryId == PRE_PAID.longValue()) {


            customerMsgString = "ubp.paybyaccount.prepaid.customer";
            customerSMSParam = new Object[]{brandName, trxCode, totalAmount, consumer, time, date, customerBalance};

        }

        String customerSMS = this.getMessageSource().getMessage(customerMsgString, customerSMSParam, null);

        SmsMessage customerSMSMessage = new SmsMessage(_workFlowWrapper.getCustomerAppUserModel().getMobileNo(), customerSMS);

        _workFlowWrapper.getTransactionDetailModel().setCustomField8(customerSMS);
        _workFlowWrapper.getTransactionModel().setNotificationMobileNo(_workFlowWrapper.getAppUserModel().getMobileNo());//todo
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


//	DEPENDANCY INJECTION (IOC)

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

    public void setBookMeLogDAO(BookMeLogDAO bookMeLogDAO) {
        this.bookMeLogDAO = bookMeLogDAO;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}

