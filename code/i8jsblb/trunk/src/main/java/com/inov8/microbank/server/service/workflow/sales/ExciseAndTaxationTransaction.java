package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import com.inov8.microbank.server.service.integration.vo.UtilityBillVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.inov8.microbank.common.util.StringUtil.buildRRNPrefix;

public class ExciseAndTaxationTransaction extends SalesTransaction {

    protected final Log log = LogFactory.getLog(getClass());
    private String OLA_FROM_ACCOUNT = "OLA_FROM_ACCOUNT";
    private String OLA_TO_ACCOUNT = "OLA_TO_ACCOUNT";

    private CommissionManager commissionManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private GenericDao genericDAO;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private SupplierBankInfoManager supplierBankInfoManager;
    private TransactionReversalManager transactionReversalManager;

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper _workFlowWrapper) throws Exception {
        Long startTime = System.currentTimeMillis();
        _workFlowWrapper = super.doPreStart(_workFlowWrapper);
        DistributorModel distributorModel = new DistributorModel();
        distributorModel.setDistributorId(_workFlowWrapper.getFromRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        _workFlowWrapper.setDistributorModel(distributorModel);
        _workFlowWrapper.setPaymentModeModel(new PaymentModeModel());
        _workFlowWrapper.getPaymentModeModel().setPaymentModeId(_workFlowWrapper.getOlaSmartMoneyAccountModel().getPaymentModeId());
        //_workFlowWrapper.getPaymentModeModel().setPaymentModeId(_workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
        // Populate Supplier Bank Info Model
        SupplierBankInfoModel supplierBankInfoModel = new SupplierBankInfoModel();
        supplierBankInfoModel.setSupplierId(_workFlowWrapper.getProductModel().getSupplierId());
        supplierBankInfoModel = this.supplierBankInfoManager.getSupplierBankInfoModel(supplierBankInfoModel);
        _workFlowWrapper.setSupplierBankInfoModel(supplierBankInfoModel);

        /*OperatorBankInfoModel operatorBankInfoModel = new OperatorBankInfoModel();

        if (supplierBankInfoModel != null) {
            // Populate Operator's Paying Bank Info Model
            operatorBankInfoModel.setOperatorId(PortalConstants.REF_DATA_OPERATOR);
            operatorBankInfoModel.setPaymentModeId(supplierBankInfoModel.getPaymentModeId());
            operatorBankInfoModel.setBankId(supplierBankInfoModel.getBankId());
            baseWrapper.setBasePersistableModel(operatorBankInfoModel);
            _workFlowWrapper.setOperatorPayingBankInfoModel((OperatorBankInfoModel) this.operatorManager.getOperatorBankInfo(baseWrapper).getBasePersistableModel());
        }*/
        // Populate Handler's Retailer Contact model from DB
        /*if(_workFlowWrapper.getHandlerModel() != null){
            searchBaseWrapper = new SearchBaseWrapperImpl();
            RetailerContactModel retailerContact = new RetailerContactModel();
            retailerContact.setRetailerContactId( _workFlowWrapper.getHandlerModel().getRetailerContactId() );
            searchBaseWrapper.setBasePersistableModel( retailerContact );
            searchBaseWrapper = retailerContactManager.loadRetailerContact(searchBaseWrapper);
            _workFlowWrapper.setHandlerRetContactModel((RetailerContactModel)searchBaseWrapper.getBasePersistableModel());

            // Populate the Handler OLA Smart Money Account from DB
            SmartMoneyAccountModel sma = smartMoneyAccountManager.getSMAccountByHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
            _workFlowWrapper.setHandlerSMAModel(sma);

            // Set Handler User Device Account Model
            UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
            handlerUserDeviceAccountsModel.setAppUserId(_workFlowWrapper.getHandlerAppUserModel().getAppUserId());
            handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
            baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
            baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
            _workFlowWrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        }*/
        TransactionModel trxnModel = _workFlowWrapper.getTransactionModel();
        if(trxnModel != null){
            trxnModel.setNotificationMobileNo(_workFlowWrapper.getTransactionModel().getNotificationMobileNo());
            _workFlowWrapper.setTransactionModel(trxnModel);
        }

        logger.info("\t**** Ending doPreStart(WorkFlowWrapper _workFlowWrapper) of ExiseAndTaxationTransaction **** " +
                "\nTotal execution Time :: " + (System.currentTimeMillis() - startTime));

        return _workFlowWrapper;

    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper workFlowWrapper) throws Exception {
        Long startTime = System.currentTimeMillis();
        workFlowWrapper = super.doPreProcess(workFlowWrapper);
        TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
        transactionModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        transactionModel.setRetailerId(  workFlowWrapper.getFromRetailerContactModel().getRetailerId());
        transactionModel.setProcessingBankId(workFlowWrapper.getSmartMoneyAccountModel().getBankId());
        transactionModel.setTransactionAmount(workFlowWrapper.getBillAmount());
        transactionModel.setTotalAmount(workFlowWrapper.getBillAmount());
        transactionModel.setTotalCommissionAmount(0d);
        transactionModel.setDiscountAmount(0d);
        transactionModel.setTransactionTypeIdTransactionTypeModel(workFlowWrapper.getTransactionTypeModel());
        transactionModel.setDeviceTypeId(workFlowWrapper.getDeviceTypeModel().getDeviceTypeId());
        transactionModel.setPaymentModeId(workFlowWrapper.getSmartMoneyAccountModel().getPaymentModeId());
        transactionModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        transactionModel.setNotificationMobileNo(workFlowWrapper.getTransactionModel().getNotificationMobileNo());
        Long smartMoneyAccountId = null;
        SmartMoneyAccountModel agentmartMoneyAccountModel = (SmartMoneyAccountModel) workFlowWrapper.getSmartMoneyAccountModel();
        smartMoneyAccountId = agentmartMoneyAccountModel.getSmartMoneyAccountId();

        transactionModel.setSmartMoneyAccountId(smartMoneyAccountId);

        //As always agent would perform transactions so logged-in user's mfsId would be used.
        String mfsId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
        transactionModel.setMfsId(mfsId);
        workFlowWrapper.setTransactionModel(transactionModel);
        logger.info("\t**** Ending doPreProcess(WorkFlowWrapper _workFlowWrapper) of ExiseAndTaxationTransaction **** " +
                "\nTotal execution Time :: " + (System.currentTimeMillis() - startTime));
        return workFlowWrapper;
    }

    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {
        Long startTime = System.currentTimeMillis();
        //--------------------- Validates the Retailer's requirements -----------------------------
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
        if (wrapper.getUserDeviceAccountModel() == null)
        {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
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

        // ----------------------- Validates the Supplier's requirements-------------------------
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
        if (wrapper.getBillAmount() < 0) {

            throw new WorkFlowException(
                    WorkFlowErrorCodeConstants.BILL_AMOUNT_NULL);
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

        logger.info("\t**** Ending doValidate(WorkFlowWrapper _workFlowWrapper) of ExiseAndTaxationTransaction **** " +
                "\nTotal execution Time :: " + (System.currentTimeMillis() - startTime));logger.info("Ending doValidate(WorkFlowWrapper wrapper) of CollectionPaymentTransaction..");

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper _workFlowWrapper) throws Exception {
        String notificationMobileNo = _workFlowWrapper.getCustomerAppUserModel().getMobileNo();//_workFlowWrapper.getTransactionModel().getNotificationMobileNo();
        TransactionCodeModel transactionCodeModel = _workFlowWrapper.getTransactionCodeModel();
        TransactionDetailMasterModel transactionDetailMasterModel = _workFlowWrapper.getTransactionDetailMasterModel();
        if(transactionDetailMasterModel != null && transactionCodeModel != null)
        {
            Double exciseAssessmentAmount = Double.valueOf((String)_workFlowWrapper.getObject(CommandFieldConstants.KEY_ASSESSMENT_AMOUNT));
            transactionDetailMasterModel.setSupProcessingStatusId(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH);
            transactionDetailMasterModel.setProcessingStatusName("3rd Party Authorization Pending");
            transactionDetailMasterModel.setTransactionCode(transactionCodeModel.getCode());
            transactionDetailMasterModel.setTransactionCodeId(transactionCodeModel.getTransactionCodeId());
            transactionDetailMasterModel.setVehicleChesisNo((String)_workFlowWrapper.getObject(CommandFieldConstants.KEY_VEHICLE_CHESIS_NO));
            transactionDetailMasterModel.setVehicleRegNo((String)_workFlowWrapper.getObject(CommandFieldConstants.KEY_VEHICLE_REG_NO));
            transactionDetailMasterModel.setExciseAssessmentNumber((String)_workFlowWrapper.getObject(CommandFieldConstants.KEY_ASSESSMENT_NO));
            transactionDetailMasterModel.setExciseAssessmentAmount(exciseAssessmentAmount);
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.setBasePersistableModel(transactionDetailMasterModel);
            transactionDetailMasterManager.saveTransactionDetailMasterRequiresNewTransaction(baseWrapper);
            _workFlowWrapper.setTransactionDetailMasterModel(transactionDetailMasterModel);
        }
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        Integer paymentType = (Integer) _workFlowWrapper.getCustomField();
        _workFlowWrapper.setTaxRegimeModel(_workFlowWrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
        CommissionWrapper commissionWrapper = this.commissionManager.calculateCommission(_workFlowWrapper);
        CommissionAmountsHolder commissionAmounts =
                (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
        _workFlowWrapper.setCommissionAmountsHolder(commissionAmounts);
        _workFlowWrapper.setCommissionWrapper(commissionWrapper);
        _workFlowWrapper.putObject("misc", paymentType.intValue() == 1 ? Boolean.TRUE : Boolean.FALSE);

        OLAVeriflyFinancialInstitutionImpl olaVeriflyFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        _workFlowWrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        _workFlowWrapper.getTransactionModel().setCreatedOn(new Date());
        _workFlowWrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.PENDING_ACTION_AUTH);
        txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
        txDetailModel.setProductIdProductModel(_workFlowWrapper.getProductModel());
        //txDetailModel.setConsumerNo(((UtilityBillVO) _workFlowWrapper.getProductVO()).getConsumerNo());
        _workFlowWrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
        _workFlowWrapper.setRetailerContactModel(_workFlowWrapper.getFromRetailerContactModel());
        //_workFlowWrapper.getTransactionModel().setFromRetContactId(_workFlowWrapper.getFromRetailerContactModel().getRetailerContactId());
        _workFlowWrapper.getTransactionModel().setNotificationMobileNo(notificationMobileNo);
        _workFlowWrapper.getTransactionDetailMasterModel().setRecipientMobileNo(notificationMobileNo); //TRB March 12, 2018 //VISIBLE SOLUTION (1103277) - Handlers Reporting
        // Set Handler Detail in Transaction and Transaction Detail Master
        /*if(_workFlowWrapper.getHandlerModel() != null){
            _workFlowWrapper.getTransactionModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
            _workFlowWrapper.getTransactionModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
            _workFlowWrapper.getTransactionDetailMasterModel().setHandlerId(_workFlowWrapper.getHandlerModel().getHandlerId());
            _workFlowWrapper.getTransactionDetailMasterModel().setHandlerMfsId(_workFlowWrapper.getHandlerUserDeviceAccountModel().getUserId());
        }*/
        txDetailModel.setSettled(Boolean.FALSE);
        _workFlowWrapper.setTransactionDetailModel(txDetailModel);
        _workFlowWrapper.getTransactionModel().setConfirmationMessage(" _ ");
        logger.info("Saving Transaction in DB....");
        txManager.saveTransaction(_workFlowWrapper);
        //------------------------------------------------------------------------------------
        txDetailModel.setSettled(Boolean.FALSE);
        _workFlowWrapper.setTransactionDetailModel(txDetailModel);
        SmartMoneyAccountModel agentSmartMoneyAccountModel = _workFlowWrapper.getSmartMoneyAccountModel();
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        _workFlowWrapper.setSwitchWrapper(switchWrapper);
        _workFlowWrapper.getTransactionModel().setSaleMobileNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        TransactionModel transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
        //ThreadLocalActionLog.setActionLogId(logActionLogModel());
        SmartMoneyAccountModel smartMoneyAccountModel = (SmartMoneyAccountModel)_workFlowWrapper.getSmartMoneyAccountModel();
        switchWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        switchWrapper.setBankId( smartMoneyAccountModel.getBankId() );
        switchWrapper.setPaymentModeId( smartMoneyAccountModel.getPaymentModeId() );
        AccountInfoModel accountInfoModel = null;
        if(switchWrapper.getWorkFlowWrapper().getAccountInfoModel() != null)
            accountInfoModel = switchWrapper.getWorkFlowWrapper().getAccountInfoModel();
        else
            switchWrapper = olaVeriflyFinancialInstitution.verifyCredentialsWithoutPin(switchWrapper);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.putObject(CommandFieldConstants.KEY_PIN, accountInfoModel.getOldPin());
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        switchWrapper.setTransactionTransactionModel(_workFlowWrapper.getTransactionModel());
        switchWrapper.setBasePersistableModel( agentSmartMoneyAccountModel ) ;
        switchWrapper.setWorkFlowWrapper(_workFlowWrapper);
        _workFlowWrapper.setSwitchWrapper(switchWrapper);
        _workFlowWrapper.getTransactionModel().setCustomerMobileNo(null);
        _workFlowWrapper.setAccountInfoModel(accountInfoModel);
        transactionModelTemp.setProcessingSwitchIdSwitchModel(switchWrapper.getSwitchSwitchModel());
        switchWrapper.setWorkFlowWrapper(switchWrapper.getWorkFlowWrapper());
        switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
        _workFlowWrapper.getTransactionModel().setBankAccountNo(switchWrapper.getFromAccountNo());
        transactionModelTemp = switchWrapper.getWorkFlowWrapper().getTransactionModel() ;
        ProductModel productModel = _workFlowWrapper.getProductModel();
        //_workFlowWrapper = billSaleProductDispenser.doSale(_workFlowWrapper);
        /*_workFlowWrapper.getDataMap().put(OLA_FROM_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getFromAccountNo());
        _workFlowWrapper.getDataMap().put(OLA_TO_ACCOUNT, _workFlowWrapper.getSwitchWrapper().getToAccountNo());*/
        switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
        switchWrapper.getWorkFlowWrapper().setProductModel(productModel);
        //The following code is for Phoenix implementation
        switchWrapper.setCurrencyCode(PhoenixConstantsInterface.DEFAULT_CURRENCY_TYPE);
        switchWrapper.setFromAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.setToAccountType(PhoenixConstantsInterface.CURRENT_ACCOUNT_TYPE);
        switchWrapper.putObject(CommandFieldConstants.KEY_CNIC, ThreadLocalAppUser.getAppUserModel().getNic());
        switchWrapper.setBasePersistableModel(_workFlowWrapper.getSmartMoneyAccountModel());
        switchWrapper.getWorkFlowWrapper().setTransactionModel( transactionModelTemp ) ;
        _workFlowWrapper.setSwitchWrapper(switchWrapper);
        _workFlowWrapper.setOLASwitchWrapper(_workFlowWrapper.getSwitchWrapper());
        //txDetailModel.setSettled(Boolean.TRUE);
        TransactionModel transactionModel = _workFlowWrapper.getTransactionModel();
        transactionModel.addTransactionIdTransactionDetailModel(txDetailModel);
        _workFlowWrapper.setTransactionModel(transactionModel);
        txManager.saveTransaction(_workFlowWrapper);
        _workFlowWrapper.setAppUserModel(ThreadLocalAppUser.getAppUserModel());
        _workFlowWrapper.putObject("ACTION_LOG_ID", ThreadLocalActionLog.getActionLogId());
        Boolean isInclusiveChargesIncluded = _workFlowWrapper.getProductModel().getInclChargesCheck();
        isInclusiveChargesIncluded = isInclusiveChargesIncluded == null ? Boolean.FALSE : Boolean.TRUE;
        //
        olaVeriflyFinancialInstitution.customerExciseTaxPayment(switchWrapper);
        //
        _workFlowWrapper.setOLASwitchWrapper(switchWrapper);
        double amount = _workFlowWrapper.getTransactionModel().getTransactionAmount();
        if(!isInclusiveChargesIncluded) {
            amount -= (_workFlowWrapper.getCommissionAmountsHolder().getInclusiveFixAmount() + _workFlowWrapper.getCommissionAmountsHolder().getInclusivePercentAmount());
        }
        switchWrapper.setAmountPaid(Formatter.formatDouble(amount));
        transactionModel = _workFlowWrapper.getTransactionModel();
        _workFlowWrapper.setMiddlewareSwitchWrapper(switchWrapper); // for day end O.F. settlement of Core FT
        _workFlowWrapper.setTransactionModel(transactionModel);
        SETTLE_COMMISSION_BLOCK : {
            settlementManager.settleCommission(commissionWrapper, _workFlowWrapper);
        }
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
        middlewareAdviceVO.setRequestTime(creditAccountAdviceDateTime);
        middlewareAdviceVO.setDateTimeLocalTransaction(creditAccountAdviceDateTime);
        middlewareAdviceVO.setTransmissionTime(creditAccountAdviceDateTime);
        middlewareAdviceVO.setAdviceType(CoreAdviceUtil.AGENT_EXCISE_TAX_ADVICE);
        middlewareAdviceVO.setVehicleRegNumber(transactionDetailMasterModel.getVehicleRegNo());
        middlewareAdviceVO.setVehicleChesisNumber(transactionDetailMasterModel.getVehicleChesisNo());
        middlewareAdviceVO.setExciseAssessmentNumber(transactionDetailMasterModel.getExciseAssessmentNumber());
        middlewareAdviceVO.setExciseAssessmentTotalAmount(transactionDetailMasterModel.getExciseAssessmentAmount().toString());

        //Product model is null in case of manual adjustment
        if(switchWrapper.getWorkFlowWrapper().getProductModel() != null){
            middlewareAdviceVO.setProductId(switchWrapper.getWorkFlowWrapper().getProductModel().getProductId());
        }

        if( switchWrapper.getTransactionTransactionModel() != null && switchWrapper.getTransactionTransactionModel().getTransactionId() != null){
            middlewareAdviceVO.getDataMap().put("TRANSACTION_ID", switchWrapper.getTransactionTransactionModel().getTransactionId());
        }
        logger.info("Going to make Core Advice against the Vehcile_Reg_No: " + transactionDetailMasterModel.getVehicleRegNo()
                + " and Chesis #: " + transactionDetailMasterModel.getVehicleChesisNo() + " and Assessment #: " + transactionDetailMasterModel.getExciseAssessmentNumber());
        transactionReversalManager.makeCoreCreditAdvice(middlewareAdviceVO);
        //transactionReversalManager.makeCoreAdvice(null,null);
        //_workFlowWrapper.setToRetailerContactModel(_workFlowWrapper.getFromRetailerContactModel());
        //_workFlowWrapper.setFromRetailerContactModel(null);
        return _workFlowWrapper;
    }

    @Override
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public void setSupplierBankInfoManager(SupplierBankInfoManager supplierBankInfoManager) {
        this.supplierBankInfoManager = supplierBankInfoManager;
    }

    public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
        this.transactionReversalManager = transactionReversalManager;
    }
}
