package com.inov8.microbank.server.service.advancesalaryloanmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class AdvanceSalaryLoanManagerImpl implements AdvanceSalaryLoanManager {
    private AdvanceSalaryLoanDAO advanceSalaryLoanDAO;
    private CommandManager commandManager;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;

    private static final Logger LOGGER = Logger.getLogger(AdvanceSalaryLoanManagerImpl.class);

    @Override
    public List<AdvanceSalaryLoanModel> loadAllAdvanceSalaryLoanData() throws FrameworkCheckedException {
        return advanceSalaryLoanDAO.loadAllAdvanceSalaryLoan();
    }

    @Override
    public String advanceLoanDeductionCommand(AdvanceSalaryLoanModel advanceSalaryLoanModel) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        ProductModel productModel = new ProductModel();
        String response = null;
        CustomerModel customerModel = null;
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        CommissionWrapper commissionWrapper;
        sb = new StringBuilder();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel = commandManager.getCommonCommandManager().loadAppUserByMobileAndType(advanceSalaryLoanModel.getMobileNo(),UserTypeConstantsInterface.CUSTOMER.longValue());
        if (customerModel == null) {
            CustomerModel cModel = new CustomerModel();
            cModel.setCustomerId(appUserModel.getCustomerId());
            customerModel = commandManager.getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
        }
        AccountInfoModel accountInfoModel = null;
        try {
            accountInfoModel = commandManager.getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        SmartMoneyAccountModel smartMoneyAccountModel = commandManager.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel,
                PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
        Double customerBalance = null;

        try {
            customerBalance = Double.valueOf(commandManager.getCommonCommandManager().getAccountBalance(accountInfoModel, smartMoneyAccountModel));
        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getMessage());
        }
        if (productModel == null || (productModel != null && productModel.getProductId() == null)) {
            BaseWrapper baseWrapper1 = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(advanceSalaryLoanModel.getProductId());
            baseWrapper1.setBasePersistableModel(productModel);
            baseWrapper1 = commandManager.getCommonCommandManager().loadProduct(baseWrapper1);
            productModel = (ProductModel) baseWrapper1.getBasePersistableModel();
        }

        workFlowWrapper.setTransactionAmount(Double.valueOf(advanceSalaryLoanModel.getInstallmentAmount()));
        workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
        TransactionModel transactionModel = new TransactionModel();
        workFlowWrapper.setProductModel(productModel);
        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.ADVANCE_LOAN_PAYMENT_TX);
        workFlowWrapper.setProductModel(productModel);
        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(customerModel.getSegmentId());
        workFlowWrapper.setSegmentModel(segmentModel);
        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        deviceTypeModel.setDeviceTypeId(DeviceTypeConstantsInterface.WEB_SERVICE);
        workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
        workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(DeviceTypeConstantsInterface.WEB_SERVICE);
        transactionModel.setTransactionAmount(Double.valueOf(advanceSalaryLoanModel.getInstallmentAmount()));
        workFlowWrapper.setTransactionModel(transactionModel);
        workFlowWrapper.setTaxRegimeModel(customerModel.getTaxRegimeIdTaxRegimeModel());
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        commissionWrapper = commandManager.getCommonCommandManager().calculateCommission(workFlowWrapper);
        CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap()
                .get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        Double transactionAmount = commissionAmountsHolder.getTotalAmount();

        if (customerBalance < transactionAmount) {
            if (advanceSalaryLoanModel.getGracePeriodDays() >= advanceSalaryLoanModel.getGracePeriodConsumed()) {
                advanceSalaryLoanModel.setGracePeriodConsumed(advanceSalaryLoanModel.getGracePeriodConsumed() + 1);
                advanceSalaryLoanDAO.saveOrUpdate(advanceSalaryLoanModel);
            } else {
                advanceSalaryLoanModel.setIsDebitBlock(true);
                smartMoneyAccountModel.setIsDebitBlocked(true);
                smartMoneyAccountModel.setDebitBlockAmount(Double.valueOf(advanceSalaryLoanModel.getInstallmentAmount()));
                smartMoneyAccountDAO.saveOrUpdate(smartMoneyAccountModel);
                advanceSalaryLoanDAO.saveOrUpdate(advanceSalaryLoanModel);
            }
        } else {
            BaseWrapper dWrapper = new BaseWrapperImpl();
            dWrapper.setBasePersistableModel(appUserModel);
            dWrapper = commandManager.getCommonCommandManager().loadUserDeviceAccountByMobileNumber(dWrapper);
            UserDeviceAccountsModel uda = (UserDeviceAccountsModel) dWrapper.getBasePersistableModel();
            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            dWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, advanceSalaryLoanModel.getMobileNo());
            dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, advanceSalaryLoanModel.getInstallmentAmount());
            dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, advanceSalaryLoanModel.getProductId());
            dWrapper.putObject(CommandFieldConstants.KEY_TXAM, advanceSalaryLoanModel.getInstallmentAmount().toString());
            dWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
            dWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, advanceSalaryLoanModel.getMobileNo());
            dWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "MOBILE");
            dWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
            dWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
            dWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
            dWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");//Customer Initiated Transaction
            sb.append("Start of executeAdvanceLoan() in Advanceloan for Product :: ").append(advanceSalaryLoanModel.getProductId().toString());
            LOGGER.info(sb.toString());
            response = commandManager.executeCommand(dWrapper, CommandFieldConstants.KEY_CMD_ADVANCE_LOAN_PAYMENT);
            sb.append("End of executeAdvanceLoan() in Advanceloan for Product :: ").append(advanceSalaryLoanModel.getProductId().toString());
            sb.append("\n Response :: " + response);
            LOGGER.info(sb.toString());
            if (response != null) {
                advanceSalaryLoanModel.setLastPaymentDate(new Date());
                advanceSalaryLoanModel.setUpdatedOn(new Date());
                advanceSalaryLoanModel.setNoOfInstallmentPaid(advanceSalaryLoanModel.getNoOfInstallmentPaid() + 1);
                if (advanceSalaryLoanModel.getNoOfInstallment().equals(advanceSalaryLoanModel.getNoOfInstallmentPaid())) {
                    advanceSalaryLoanModel.setIsCompleted(true);
                }
                advanceSalaryLoanDAO.saveOrUpdate(advanceSalaryLoanModel);
            }
        }

        return response;

    }

    @Override
    public List<AdvanceSalaryLoanModel> loadAdvanceSalaryLoanByIsCompleted() throws FrameworkCheckedException {
        return advanceSalaryLoanDAO.loadAllAdvanceSalaryLoanByIsCompleted();
    }

    public void setAdvanceSalaryLoanDAO(AdvanceSalaryLoanDAO advanceSalaryLoanDAO) {
        this.advanceSalaryLoanDAO = advanceSalaryLoanDAO;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
}
