package com.inov8.microbank.server.service.workflow.credittransfer;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.inov8.microbank.common.util.*;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.MessageSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
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
import com.inov8.microbank.server.service.integration.vo.AccountToCashVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;
import com.inov8.microbank.server.service.settlementmodule.SettlementManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.ola.integration.vo.OLAVO;

/**
 * <p>Title: SenderRredeemTransaction</p>
 * <p>
 * <p>Description: Class implements the logic for Sender Redeem Transaction.
 * </p>
 * <p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>
 * <p>Company: Inov8</p>
 *
 * @author Abu Turab
 * @version 1.0
 */

public class SenderRedeemTransaction
        extends CreditTransferTransaction {
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private FinancialIntegrationManager financialIntegrationManager;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private NotificationMessageManager notificationMessageManager;
    private ProductManager productManager;
    private CommissionManager commissionManager;
    private RetailerContactManager retailerContactManager;
    private RetailerManager retailerManager;

    private MessageSource messageSource;
    DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
    DateTimeFormatter tf = DateTimeFormat.forPattern("h:mm a");


    public void setFinancialIntegrationManager(FinancialIntegrationManager financialIntegrationManager) {
        this.financialIntegrationManager = financialIntegrationManager;
    }

    public SenderRedeemTransaction() {

    }

    /**
     * Credit transfer transaction takes place over here
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
            Exception {


        wrapper.getTransactionModel().setToRetailerId(wrapper.getRetailerContactModel().getRetailerId());
        wrapper.getTransactionModel().setToDistributorId(wrapper.getRetailerModel().getDistributorId());
        wrapper.getTransactionModel().setToRetContactId(wrapper.getAppUserModel().getRetailerContactId());
        wrapper.getTransactionModel().setToRetContactMobNo(wrapper.getAppUserModel().getMobileNo());
        wrapper.getTransactionModel().setToRetContactName(wrapper.getAppUserModel().getLastName() + " " + wrapper.getAppUserModel().getFirstName());
        wrapper.getTransactionModel().setUpdatedBy(wrapper.getAppUserModel().getAppUserId());
        wrapper.getTransactionModel().setUpdatedOn(new Date());
        wrapper.getTransactionModel().setNotificationMobileNo(wrapper.getWalkInCustomerMob());//customer mobile number
        wrapper.getTransactionModel().setDiscountAmount(0d);
        wrapper.getTransactionDetailModel().setCustomField13("" + wrapper.getDeviceTypeModel().getDeviceTypeId());

        wrapper.getTransactionDetailModel().setSettled(Boolean.TRUE);
        wrapper.getTransactionDetailModel().setConsumerNo(wrapper.getTransactionDetailModel().getCustomField5());
        wrapper.getTransactionDetailModel().setActualBillableAmount(wrapper.getTransactionAmount());

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());

        wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());

        Integer redemptionType = wrapper.getTransactionDetailMasterModel().getRedemptionType();
        logger.info("Transaction ID: " + wrapper.getTransactionCodeModel().getCode() + " -> redemptionType: " + redemptionType);

        boolean settleCommission = false;
        if (redemptionType == null) {
            logger.error("Redemption Type missing in Transaction Detail Master for Transaction ID:" + wrapper.getTransactionCodeModel().getCode());
            throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
        } else if (redemptionType.intValue() == TransactionReversalConstants.REDEMPTION_TYPE_PARTIAL) {
            settleCommission = true;
        }

        CommissionAmountsHolder commissionAmountsHolder = commissionManager.loadCommissionDetailsUnsettled(wrapper.getTransactionDetailMasterModel().getTransactionDetailId());
        wrapper.setCommissionAmountsHolder(commissionAmountsHolder);

        if (settleCommission) {
            this.commissionManager.makeAgent2CommissionSettlement(wrapper);
            settlementManager.updateCommissionTransactionSettled(wrapper.getTransactionDetailMasterModel().getTransactionDetailId());
        }


        OLAVeriflyFinancialInstitutionImpl olaFinancialInstitution = (OLAVeriflyFinancialInstitutionImpl) this.financialIntegrationManager.loadFinancialInstitution(baseWrapper);

        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());// Agent SMA

        logger.info("[SenderRedeemTransaction.performLeg2()] Going to transfer funds from Redemption GL to Agent Account. Transaction ID: " + switchWrapper.getWorkFlowWrapper().getTransactionCodeModel().getCode() + " SenderSmartMoneyAccountId: " + wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());
//		switchWrapper = olaFinancialInstitution.reverseP2PTransaction(switchWrapper);
        olaFinancialInstitution.performLeg2(switchWrapper, ReasonConstants.SENDER_REDEEM,
                TransactionConstantsInterface.SENDER_REDEEM_CATEGORY_ID,
                settleCommission);

        wrapper.setOLASwitchWrapper(switchWrapper); //setting the switchWrapper for rollback
        wrapper.getTransactionModel().setProcessingSwitchId(switchWrapper.getSwitchSwitchModel().getSwitchId());

        //agent balance
        Double agentBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction();
        String agentAccNo = switchWrapper.getToAccountNo();
        Double inclusiveChargesApplied = switchWrapper.getInclusiveChargesApplied();

		/*wrapper.getMiniTransactionModel().setMiniTransactionStateId(MiniTransactionStateConstant.COMPLETED);*/
        wrapper.getTransactionModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);
        wrapper.getTransactionDetailMasterModel().setSupProcessingStatusId(SupplierProcessingStatusConstants.REVERSE_COMPLETED);


        wrapper.getTransactionDetailModel().setCustomField1(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId().toString());
        wrapper.getTransactionDetailModel().setCustomField2(agentAccNo);
        wrapper.getTransactionDetailModel().setCustomField3(switchWrapper.getSwitchSwitchModel().getSwitchId().toString());

        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);

        String brandName = null;

        if (UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L)) {
            brandName = MessageUtil.getMessage("sco.brandName");
        } else {
            brandName = MessageUtil.getMessage("jsbl.brandName");
        }

        Double effectiveAmount = 0D;
        if (settleCommission) {
            //Partial Redemption
            effectiveAmount = wrapper.getTransactionDetailMasterModel().getTransactionAmount() - inclusiveChargesApplied;
        } else {
            //Full Redemption
            effectiveAmount = wrapper.getTransactionDetailMasterModel().getTransactionAmount() + commissionAmountsHolder.getTotalCommissionAmountUnsettled() - wrapper.getTransactionDetailMasterModel().getInclusiveCharges();
        }


        wrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, effectiveAmount); // Amount to be displayed at AgentMate final notification screen

        //smsCommand.cash2cash.sender.redeem={0}\nTrx Id {1}\nRs. {2} received as Reversal Redeem, net of fees and taxes, at {3} on {4}.
        String customerSms = this.getMessageSource().getMessage(
                "smsCommand.cash2cash.sender.redeem",
                new Object[]{
                        brandName,
                        wrapper.getTransactionCodeModel().getCode(),
                        Formatter.formatDouble(effectiveAmount),
                        dtf.print(new DateTime()),
                        tf.print(new LocalTime()),
                },
                null);

        messageList.add(new SmsMessage(wrapper.getWalkInCustomerMob(), customerSms));
		
		/*Brand Name
		 Amount of Rs. xxxxx.xx withdrawn on dd/mm/yyyy 00:00 AM,
		 Tx ID: xxxxxxxxxxxx*/
        String agentSms = this.getMessageSource().getMessage(
                "smsCommand.sender.redeem.agentsms",
                new Object[]{
                        brandName,
                        Formatter.formatDouble(effectiveAmount),
                        dtf.print(new DateTime()),
                        tf.print(new LocalTime()),
                        wrapper.getTransactionCodeModel().getCode(),
                },
                null);

        messageList.add(new SmsMessage(wrapper.getTransactionModel().getToRetContactMobNo(), agentSms));

        txManager.saveTransaction(wrapper); //save the transaction
	    /*BaseWrapper miniBaseWrapper = new BaseWrapperImpl();
	    miniBaseWrapper.setBasePersistableModel(wrapper.getMiniTransactionModel());
	    txManager.saveMiniTransaction(miniBaseWrapper); //save mini transaction*/

        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);

        return wrapper;

    }

    /**
     * doValidate
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     * @throws Exception
     */
    protected WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
            Exception {

        DistributorContactModel toDistributorContactModel = wrapper.getToDistributorContactModel();

        if (wrapper.getUserDeviceAccountModel() == null) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);
        }

        //  Validates the workflowWrapper's requirements
        if (wrapper.getTransactionAmount() <= 0) {
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED);
        }
        return wrapper;
    }

    public WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
            Exception {
        // Prepares the Transaction Wrapper object and sends it to the Transaction module( TransactionManager ) for
        // saving that transaction in the database

//    txManager.saveTransaction(wrapper);

        return wrapper;
    }

    protected WorkFlowWrapper doCreditTransferStart(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    protected WorkFlowWrapper doCreditTransferEnd(WorkFlowWrapper wrapper) {
        return wrapper;
    }

    /**
     * Populates the transaction object with all the necessary data to save it in the db.
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
        TransactionModel txModel = new TransactionModel();
        txModel.setTransactionCodeId(wrapper.getTransactionCodeModel().getTransactionCodeId());
        sBaseWrapper.setBasePersistableModel(wrapper.getTransactionCodeModel());
        sBaseWrapper = txManager.loadTransactionByTransactionCode(sBaseWrapper);
        txModel = (TransactionModel) sBaseWrapper.getBasePersistableModel();
        wrapper.setTransactionModel(txModel);
        wrapper.setTransactionCodeModel(txModel.getTransactionCodeIdTransactionCodeModel());
        for (Iterator<TransactionDetailModel> iterator = txModel.getTransactionIdTransactionDetailModelList().iterator(); iterator.hasNext(); ) {
            TransactionDetailModel txDetailModel = (TransactionDetailModel) iterator.next();
            wrapper.setTransactionDetailModel(txDetailModel);
        }

        // Populate the Product model from DB
        baseWrapper.setBasePersistableModel(wrapper.getProductModel());
        baseWrapper = productManager.loadProduct(baseWrapper);
        wrapper.setProductModel((ProductModel) baseWrapper.getBasePersistableModel());

        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());

        //commented as it is not needed - turab 13-5-2015
	  /*wrapper.setCustomerModel(new CustomerModel());
	  wrapper.getCustomerModel().setCustomerId(wrapper.getAppUserModel().getRetailerContactId());
	   */
        // --Setting instruction and success Message
        NotificationMessageModel notificationMessage = new NotificationMessageModel();
        notificationMessage.setNotificationMessageId(wrapper.getProductModel().getInstructionId());
        baseWrapper.setBasePersistableModel(notificationMessage);
        baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
        wrapper.setInstruction((NotificationMessageModel) baseWrapper.getBasePersistableModel());

        // wrapper.getProductModel().setInstructionIdNotificationMessageModel( (
        // NotificationMessageModel) baseWrapper.getBasePersistableModel());
        NotificationMessageModel successMessage = new NotificationMessageModel();
        successMessage.setNotificationMessageId(wrapper.getProductModel().getSuccessMessageId());
        baseWrapper.setBasePersistableModel(successMessage);
        baseWrapper = this.notificationMessageManager.loadNotificationMessage(baseWrapper);
        wrapper.setSuccessMessage((NotificationMessageModel) baseWrapper.getBasePersistableModel());

        // Populate the Retailer contact model from DB
        //commented as it is not needed - turab 13-5-2015
	 /* baseWrapper.setBasePersistableModel(wrapper.getCustomerModel());
	  baseWrapper = retailerContactManager.loadRetailerContact(baseWrapper);
	  wrapper.setRetailerContactModel((RetailerContactModel) baseWrapper.getBasePersistableModel());
	  */
        // Populate the Retailer model from DB
        RetailerModel retailerModel = new RetailerModel();
        retailerModel.setRetailerId(wrapper.getRetailerContactModel().getRetailerId());
        baseWrapper.setBasePersistableModel(retailerModel);
        baseWrapper = retailerManager.loadRetailer(baseWrapper);
        wrapper.setRetailerModel((RetailerModel) baseWrapper.getBasePersistableModel());

        // Populate the Agent Smart Money Account from DB
        baseWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
        baseWrapper = smartMoneyAccountManager.loadSmartMoneyAccount(baseWrapper);
        wrapper.setSmartMoneyAccountModel((SmartMoneyAccountModel) baseWrapper.getBasePersistableModel());

        wrapper.setOlaSmartMoneyAccountModel(wrapper.getSmartMoneyAccountModel());

        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        wrapper.setPaymentModeModel(new PaymentModeModel());
        wrapper.getPaymentModeModel().setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        return wrapper;
    }


    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        wrapper = super.doPreProcess(wrapper);

        if (logger.isDebugEnabled()) {
            logger.debug("Strat/End doPreProcess(WorkFlowWrapper wrapper) of SenderRedeemTransaction....");
        }

        return wrapper;
    }

    public void setUserDeviceAccountsManager(
            UserDeviceAccountsManager userDeviceAccountsManager) {
        this.userDeviceAccountsManager = userDeviceAccountsManager;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }


    public void setNotificationMessageManager(NotificationMessageManager notificationMessageManager) {
        this.notificationMessageManager = notificationMessageManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void setCommissionManager(CommissionManager commissionManager) {
        this.commissionManager = commissionManager;
    }


    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

    public void setSettlementManager(SettlementManager settlementManager) {
        this.settlementManager = settlementManager;
    }

    public void setTransactionDetailMasterManager(
            TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }

}
