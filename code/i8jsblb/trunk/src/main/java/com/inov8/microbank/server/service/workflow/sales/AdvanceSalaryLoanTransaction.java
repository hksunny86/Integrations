package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.schedulemodule.ScheduleLoanPaymentModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.schedulemodule.ScheduleBillPaymentDao;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.OLAVeriflyFinancialInstitutionImpl;
import com.inov8.microbank.server.service.integration.vo.BBToCoreVO;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

import java.util.Date;

public class AdvanceSalaryLoanTransaction extends SalesTransaction {
    protected final Log log = LogFactory.getLog(getClass());
    SupplierManager supplierManager;
    CommissionManager commissionManager;
    private MessageSource messageSource;
    private ProductManager productManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private ScheduleBillPaymentDao scheduleBillPaymentDao;

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        logger.info("AdvanceSalaryLoanTransaction.doPreProcess()");
        wrapper = super.doPreProcess(wrapper);
        TransactionModel txModel = wrapper.getTransactionModel();
        txModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
        txModel.setTransactionAmount(wrapper.getTransactionAmount());
        txModel.setTotalAmount(wrapper.getTotalAmount());
        txModel.setTotalCommissionAmount(0d);
        txModel.setDiscountAmount(0d);
        txModel.setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());
        txModel.setDeviceTypeId(wrapper.getDeviceTypeModel().getDeviceTypeId());
        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
        txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
        txModel.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());
        txModel.setProcessingBankId(BankConstantsInterface.OLA_BANK_ID);
        txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
//        txModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
//        txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
//        txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
//        txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        wrapper.setTransactionModel(txModel);
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
        userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

        return wrapper;
    }
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

        if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTransactionAmount().doubleValue())).equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getBillAmount().doubleValue())))) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_MATCHED);
        }
        if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalCommissionAmount().doubleValue())).equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalCommissionAmount().doubleValue())))) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NOT_MATCHED);
        }
        if (!Double.valueOf(Formatter.formatDouble(commissionHolder.getTotalAmount().doubleValue())).equals(Double.valueOf(Formatter.formatDouble(workFlowWrapper.getTotalAmount().doubleValue())))) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_AMOUNT_NOT_MATCHED);
        }

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

    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();

        CommissionWrapper commissionWrapper = this.calculateCommission(wrapper);
        CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

//        this.validateCommission(commissionWrapper, wrapper);

        wrapper.setCommissionAmountsHolder(commissionAmounts);

        AbstractFinancialInstitution olaVeriflyFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitutionByClassName(OLAVeriflyFinancialInstitutionImpl.class.getName());

        wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
        wrapper.getTransactionModel().setCreatedOn(new Date());
        txDetailModel.setActualBillableAmount(commissionAmounts.getBillingOrganizationAmount());
        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());

        txDetailModel.setCustomField5("" + wrapper.getWalkInCustomerMob());
        txDetailModel.setCustomField7(wrapper.getAppUserModel().getNic());

        wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getAppUserModel().getMobileNo());

        txDetailModel.setSettled(false);
        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().setConfirmationMessage(" _ ");

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

        AbstractFinancialInstitution abstractFinancialInstitution = this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);

        switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

        switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);
        //set Sender BB Customer details in transaction_details table
        wrapper.getTransactionDetailModel().setCustomField1("" + wrapper.getSenderSmartMoneyAccountModel().getSmartMoneyAccountId());
        wrapper.getTransactionDetailModel().setCustomField3("" + switchWrapper.getSwitchSwitchModel().getName());
        wrapper.getTransactionDetailModel().setSettled(true);

        txManager.saveTransaction(wrapper);
        wrapper.setSwitchWrapper(switchWrapper);
        wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());

        wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED ) ;
        wrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_ID, wrapper.getTransactionModel().getTransactionId());

        ScheduleLoanPaymentModel scheduleLoanPaymentModel = (ScheduleLoanPaymentModel) wrapper.getScheduleLoanPaymentModel();
        wrapper.setScheduleLoanPaymentModel(scheduleLoanPaymentModel);
        scheduleBillPaymentDao.saveOrUpdateScheduleLoanRequest(wrapper);

        txManager.saveTransaction(wrapper);
        settlementManager.settleCommission(commissionWrapper, wrapper);

        return wrapper;
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
    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }
    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setScheduleBillPaymentDao(ScheduleBillPaymentDao scheduleBillPaymentDao) {
        this.scheduleBillPaymentDao = scheduleBillPaymentDao;
    }
}
