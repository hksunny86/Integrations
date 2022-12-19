package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.ThirdPartyAcOpeningDAO;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import org.springframework.context.MessageSource;

import java.util.Date;

public class ProofOfLifeTransaction extends SalesTransaction {

    private MessageSource messageSource;
    private CommissionManager commissionManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private GenericDao genericDAO;
    private FinancialIntegrationManager financialIntegrationManager;
    private ThirdPartyAcOpeningDAO thirdPartyAcOpeningDAO;
    private ESBAdapter esbAdapter;

    public ProofOfLifeTransaction(){
    }
    @Override
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        logger.info("ProofOfLifeTransaction.doPreProcess()");
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
        txModel.setProcessingBankId(BankConstantsInterface.BOP_BANK_ID);
        txModel.setMfsId(ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId());
        txModel.setFromRetContactId(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
        txModel.setFromRetContactMobNo(ThreadLocalAppUser.getAppUserModel().getMobileNo());
        txModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
        txModel.setDistributorId(wrapper.getRetailerContactModel().getRetailerIdRetailerModel().getDistributorId());
        wrapper.setTransactionModel(txModel);
        return wrapper;
    }

    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
        logger.info("ProofOfLifeTransaction.calculateCommission()");
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());
        wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
        commissionWrapper = commissionManager.calculateCommission(wrapper);
        return commissionWrapper;
    }
    @Override
    protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        TransactionDetailModel txDetailModel = new TransactionDetailModel();
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        wrapper.setSegmentModel(segmentModel);
        // calculate and set commissions to transaction & transaction details model
        CommissionWrapper commissionWrapper = calculateCommission(wrapper);
        CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
        //if agent 1 is head agent, then franchise 1 commission is merged back to agent 1 and franchise commission entry is not parked in commission_transaction
        if(null != wrapper.getRetailerContactModel() && wrapper.getRetailerContactModel().getHead()){
            commissionAmounts.setAgent1CommissionAmount(commissionAmounts.getAgent1CommissionAmount() + commissionAmounts.getFranchise1CommissionAmount());
            commissionAmounts.setFranchise1CommissionAmount(0.0d);
        }
        wrapper.setCommissionAmountsHolder(commissionAmounts);
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel()); // Customer SMA
        AbstractFinancialInstitution abstractFinancialInstitution = financialIntegrationManager.loadFinancialInstitution(baseWrapper);
        wrapper.setCommissionAmountsHolder(commissionAmounts);
        wrapper.setCommissionWrapper(commissionWrapper);
        abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
        AppUserModel appUserModel = wrapper.getAppUserModel();
        wrapper.setAppUserModel(appUserModel);
        wrapper.getTransactionModel().setTotalCommissionAmount(commissionAmounts.getTotalCommissionAmount() + commissionAmounts.getFedCommissionAmount());
        wrapper.getTransactionModel().setTransactionAmount(commissionAmounts.getTransactionAmount());
        wrapper.getTransactionModel().setTotalAmount(commissionAmounts.getTotalAmount());
        wrapper.getTransactionModel().setCreatedOn(new Date());
        wrapper.getTransactionModel().setFromRetContactId(wrapper.getRetailerContactModel().getRetailerContactId());
        wrapper.getTransactionModel().setFromRetContactName(wrapper.getRetailerContactModel().getName());
        wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getAppUserModel().getMobileNo());
        txDetailModel.setActualBillableAmount(commissionAmounts.getTransactionAmount());
        txDetailModel.setProductIdProductModel(wrapper.getProductModel());
        txDetailModel.setSettled(false);
        wrapper.setTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().addTransactionIdTransactionDetailModel(txDetailModel);
        wrapper.getTransactionModel().setConfirmationMessage(" _ ");
        txDetailModel = wrapper.getTransactionDetailModel();
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
        //perform Cash withdrawal on OLA
        switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper);
        // Agent Account Number
        wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getToAccountNo());
        // Customer Account Number
        wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getFromAccountNo());
        txDetailModel.setSettled(true);
        txManager.saveTransaction(wrapper);
        wrapper.setSwitchWrapper(switchWrapper);
        wrapper.getTransactionModel().setSupProcessingStatusId( SupplierProcessingStatusConstants.COMPLETED ) ;
        txManager.saveTransaction(wrapper);
        settlementManager.settleCommission(commissionWrapper, wrapper);
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        wrapper = super.doPreStart(wrapper);
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
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());
        I8SBSwitchControllerRequestVO requestVo=new I8SBSwitchControllerRequestVO();
        I8SBSwitchControllerResponseVO responseVO=new I8SBSwitchControllerResponseVO();
        SwitchWrapper sWrapper = (SwitchWrapper) wrapper.getObject("SWITCH_WRAPPER");
        sWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
        wrapper.putObject("IS_CALL_SENT","1");
        sWrapper=esbAdapter.makeI8SBCall(sWrapper);
        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

        if(responseVO != null && responseVO.getResponseCode() != null) {
            String responseCode = responseVO.getResponseCode();
            String errorMessage = null;
            if (responseCode.equals("122") || responseCode.equals("121")) {
                errorMessage = responseVO.getDescription() + "," + responseVO.getFingerIndex();
                wrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID, responseVO.getSessionId());
                wrapper.putObject("IS_NADRA_ERROR", responseCode);
                throw new CommandException(errorMessage, Long.valueOf(responseCode), ErrorLevel.MEDIUM, null);
            }

            ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
        }
        else{
            logger.error("[ESBAdapter.processI8sbResponseCode] i8sb responseVO is null. throwing general error");
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG);
        }

        wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode(responseVO.getTransactionId());

        return wrapper;
    }
    @Override
    protected WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }

    public void setUserDeviceAccountsManager(UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setGenericDAO(GenericDao genericDAO) {
        this.genericDAO = genericDAO;
    }

    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }
}
