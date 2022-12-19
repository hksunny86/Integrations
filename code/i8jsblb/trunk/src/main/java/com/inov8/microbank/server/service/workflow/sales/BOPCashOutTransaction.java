package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.bispmodule.BISPManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import org.springframework.context.MessageSource;

import java.util.Date;
import java.util.List;

public class BOPCashOutTransaction extends SalesTransaction {
    private MessageSource messageSource;
    private CommissionManager commissionManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private GenericDao genericDAO;
    private FinancialIntegrationManager financialIntegrationManager;
    private BISPManager bispManager;

    private ESBAdapter esbAdapter;

    public BOPCashOutTransaction(){
    }

    /**
     * This method calls the commission module to calculate the commission on
     * this product and transaction.The wrapper should have product,payment mode
     * and principal amount that is passed onto the commission module
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */
    public CommissionWrapper calculateCommission(WorkFlowWrapper wrapper) throws Exception {
        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());
        wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());
        commissionWrapper = commissionManager.calculateCommission(wrapper);
        return commissionWrapper;
    }

    /**
     *
     * @param commissionHolder  CommissionAmountsHolder
     * @param calculatedCommissionHolder CommissionAmountsHolder
     * @throws FrameworkCheckedException
     */
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
        if(null == wrapper.getAppUserModel())
            throw new WorkFlowException(WorkFlowErrorCodeConstants.AGENT_ACCOUNT_NOT_FOUND);

        if (wrapper.getUserDeviceAccountModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);

        // Validates the Product's requirements
        if (wrapper.getProductModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);

        if (!wrapper.getProductModel().getActive())
            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NOT_ACTIVE);

        // Validates the Product's requirements
        if (wrapper.getProductModel().getServiceIdServiceModel().getServiceTypeId().longValue() != ServiceTypeConstantsInterface.SERVICE_TYPE_THIRD_PARTY_CASH_OUT.longValue()){
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

       /* if (wrapper.getTotalCommissionAmount() < 0)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TOTAL_COMM_NULL);

        if (wrapper.getTxProcessingAmount() < 0)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.TX_PROCESS_AMOUNT_NULL);*/

        // Validates the PaymentMode's requirements
        if (wrapper.getPaymentModeModel() == null || wrapper.getPaymentModeModel().getPaymentModeId() <= 0)
            throw new WorkFlowException("PaymentModeID is not supplied.");

        // ----------------------- Validates the Service's requirements
        if (wrapper.getProductModel().getServiceIdServiceModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.PRODUCT_NULL);

        if (!wrapper.getProductModel().getServiceIdServiceModel().getActive())
            throw new WorkFlowException(WorkFlowErrorCodeConstants.SERVICE_INACTIVE);

        return wrapper;
    }

    /**
     * Method responsible for processing the Cash Withdrawal transaction
     *
     * @param wrapper WorkFlowWrapper
     * @return WorkFlowWrapper
     */

    public WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception {
        if(wrapper.getObject("IS_CARD") != null && !wrapper.getObject("IS_CARD").toString().equals("0") && !wrapper.getObject("IS_CARD").toString().equals("2")){
            wrapper.getTransactionDetailMasterModel().setDebitCardNumber(EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0",
                    (String)wrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_NO)));
        }
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
        //abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
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
        wrapper.setCommissionAmountsHolder(commissionAmounts);
        wrapper.setCommissionWrapper(commissionWrapper);
        abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);
        SwitchWrapper switchWrapper = new SwitchWrapperImpl();
        switchWrapper.setWorkFlowWrapper(wrapper);
        switchWrapper.setBasePersistableModel(wrapper.getSmartMoneyAccountModel());
        //perform Cash withdrawal on OLA
        switchWrapper = abstractFinancialInstitution.transferFunds(switchWrapper) ;
        // Agent Account Number
        wrapper.getTransactionModel().setBankAccountNo(switchWrapper.getToAccountNo());
        // Customer Account Number
        wrapper.getTransactionDetailModel().setCustomField2(switchWrapper.getFromAccountNo());
        Double agentBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction(); // agent balance
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
        txManager.saveTransaction(wrapper);
        settlementManager.settleCommission(commissionWrapper, wrapper);
        return wrapper;
    }

    /**
     * Update Workflow wrapper with different parameters required in further steps
     */

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        wrapper = super.doPreStart(wrapper);
        // For Agent Retention Commission calculation - reloading customerAppUserModel (to avoid the owning Session was closed issue)
        UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
        userDeviceAccountsModel.setAppUserId(wrapper.getAppUserModel().getAppUserId());
        userDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
        baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
        baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
        wrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
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
        String thirdPartyTransactionId = (String) wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID);
        String orgThirdPartyTransactionId = (String) wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID);
        I8SBSwitchControllerRequestVO requestVo=new I8SBSwitchControllerRequestVO();
        ESBAdapter adapter = new ESBAdapter();
        requestVo=adapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_CashOut);
//        if(thirdPartyTransactionId != null && thirdPartyTransactionId.equals(""))
//            thirdPartyTransactionId = requestVo.getRRN();
//        wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID,thirdPartyTransactionId);
//        wrapper=this.prepareSafRepoCashOutModel(wrapper);
        requestVo.setConsumerNumber(wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER).toString());
        requestVo.setOtp((String)wrapper.getObject(CommandFieldConstants.KEY_OTP));
        requestVo.setTransactionAmount(String.valueOf(wrapper.getTransactionAmount()));
//        if(orgThirdPartyTransactionId != null && !orgThirdPartyTransactionId.equals(""))
//            requestVo.setTransactionId(thirdPartyTransactionId);
        requestVo.setTransmissionDateAndTime(String.valueOf( new Date()));
        requestVo.setSenderMobile(wrapper.getAppUserModel().getMobileNo());
        requestVo.setCNIC(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
        requestVo.setMobileNumber(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
        requestVo.setAgentId(wrapper.getUserDeviceAccountModel().getUserId());
        requestVo.setMobilePhone(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
        requestVo.setProductCode(wrapper.getProductModel().getProductCode());
        requestVo.setTerminalID(wrapper.getUserDeviceAccountModel().getUserId());
        requestVo.setSessionId(wrapper.getObject(CommandFieldConstants.KEY_SESSION_ID).toString());
        requestVo.setSessionIdNadra((String)wrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID));
        requestVo.setSegmentId((String)wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE));
        requestVo.setAccountNumber((String)wrapper.getObject(CommandFieldConstants.KEY_DEBIT_CARD_NO));
        requestVo.setTransactionType("W");
        requestVo.setAgentLocation(wrapper.getTransactionCodeModel().getCode());
        requestVo.setAreaName("SINDH");
        String bvsConfigId="";
        if(null!=wrapper.getObject( CommandFieldConstants.KEY_IS_BVS_REQUIRED))
            bvsConfigId = wrapper.getObject( CommandFieldConstants.KEY_IS_BVS_REQUIRED).toString();

        if(bvsConfigId.equals("1")){
            requestVo.setFingerIndex(wrapper.getObject(CommandFieldConstants.KEY_FINGER_INDEX).toString());
            requestVo.setTempeleteType(wrapper.getObject(CommandFieldConstants.KEY_TEMPLATE_TYPE).toString());
            requestVo.setFingerTemplete(wrapper.getObject(CommandFieldConstants.KEY_FINGER_TEMPLATE).toString());
        }
        I8SBSwitchControllerResponseVO responseVO=new I8SBSwitchControllerResponseVO();
        SwitchWrapper sWrapper=new SwitchWrapperImpl();

        if(thirdPartyTransactionId != null && thirdPartyTransactionId.equals(""))
            thirdPartyTransactionId = requestVo.getRRN();
        wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID,thirdPartyTransactionId);
//        wrapper=this.prepareSafRepoCashOutModel(wrapper);
        if(orgThirdPartyTransactionId != null && !orgThirdPartyTransactionId.equals(""))
            requestVo.setTransactionId(thirdPartyTransactionId);

        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        sWrapper.setI8SBSwitchControllerRequestVO(requestVo);
        sWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
        wrapper.putObject("IS_CALL_SENT","1");
        sWrapper=esbAdapter.makeI8SBCall(sWrapper);

//        if(thirdPartyTransactionId != null && thirdPartyTransactionId.equals(""))
//            thirdPartyTransactionId = sWrapper.getI8SBSwitchControllerResponseVO().getTransactionId();
        wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID,sWrapper.getI8SBSwitchControllerResponseVO().getTransactionId());
        logger.info("Saving data in SAF Repo Cash Out");
        wrapper=this.prepareSafRepoCashOutModel(wrapper);
        logger.info("Data Saved in SAF Repo Cash Out");
//        if(orgThirdPartyTransactionId != null && !orgThirdPartyTransactionId.equals(""))
//            requestVo.setTransactionId(thirdPartyTransactionId);

        if(null!=sWrapper.getI8SBSwitchControllerResponseVO().getResponseCode()) {
            responseVO=sWrapper.getI8SBSwitchControllerResponseVO();
            String responseCode = responseVO.getResponseCode();
            String errorMessage = null;
            if(responseCode.equals("122") || responseCode.equals("121"))
            {
                errorMessage = responseVO.getDescription() + "," + responseVO.getFingerIndex();
                wrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID,responseVO.getSessionId());
                wrapper.putObject("IS_NADRA_ERROR",responseCode);
                throw new CommandException(errorMessage, Long.valueOf(responseCode),ErrorLevel.MEDIUM,null);
            }

//            if (isNadraError(sWrapper.getI8SBSwitchControllerResponseVO().getResponseCode()).equals(Boolean.TRUE)) {
//                wrapper.putObject("IS_NADRA_ERROR", responseVO.getResponseCode());
//                wrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID, sWrapper.getI8SBSwitchControllerResponseVO().getI8SBSwitchControllerResponseVOList().get(1).getSessionIdNadra());
//            }
//            wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID,thirdPartyTransactionId);
        }
        responseVO = sWrapper.getI8SBSwitchControllerResponseVO();
        if( responseVO != null
                && responseVO.getResponseCode() != null)
        {
            wrapper.putObject("I8SB_RESPONSE_CODE",responseVO.getResponseCode());
            String errorMessage = bispManager.saveOrUpdateBVSEntryRequiresNewTransaction(wrapper.getUserDeviceAccountModel(),wrapper.getAppUserModel()
                    ,(String)wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC),sWrapper,wrapper.getTransactionCodeModel().getCode());
            if(errorMessage != null)
            {
                String responseCode = responseVO.getResponseCode();
                if(responseCode.equals("I8SB-500"))
                    responseCode = "500";
                if(errorMessage ==null || (errorMessage != null && errorMessage.equals("")))
                    errorMessage = WorkFlowErrorCodeConstants.GENERAL_ERROR_MSG;
                throw new CommandException(errorMessage, Long.valueOf(responseCode),ErrorLevel.MEDIUM,null);
            }
        }
        esbAdapter.processI8sbResponseCode(responseVO,true);
        wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRX_INTERNAL_ERROR,"1");
        wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode(responseVO.getTransactionId());

        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
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

    @Override
    protected WorkFlowWrapper doEnd(WorkFlowWrapper wrapper) throws Exception {
        return wrapper;
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
        if(wrapper.getObject("IS_CALL_SENT").equals("1") && (null==wrapper.getObject("IS_NADRA_ERROR") || !wrapper.getObject("IS_NADRA_ERROR").equals("1"))) {
            if(wrapper.getSafRepoCashOutModel() != null)
            {
                genericDAO.createEntity(wrapper.getSafRepoCashOutModel());
            }
        }
        return wrapper;
    }

    public WorkFlowWrapper prepareSafRepoCashOutModel(WorkFlowWrapper wrapper){

        wrapper.setSafRepoCashOutModel(new SafRepoCashOutModel());
        wrapper.getSafRepoCashOutModel().setTransactionTypeIdTransactionTypeModel(wrapper.getTransactionTypeModel());
        wrapper.getSafRepoCashOutModel().setCreatedBy(2L);
        wrapper.getSafRepoCashOutModel().setUpdatedBy(2L);
        wrapper.getSafRepoCashOutModel().setAgentAccountNumber(wrapper.getAppUserModel().getMobileNo());
        wrapper.getSafRepoCashOutModel().setProductId(wrapper.getProductModel().getProductId());
        wrapper.getSafRepoCashOutModel().setTransactionAmount(wrapper.getTransactionAmount());
        wrapper.getSafRepoCashOutModel().setTransactionStatus(wrapper.getTransactionDetailMasterModel().getProcessingStatusName());
        wrapper.getSafRepoCashOutModel().setTransactionTime(new Date());
        wrapper.getSafRepoCashOutModel().setStatus(PortalConstants.SAF_STATUS_INITIATED);
        wrapper.getSafRepoCashOutModel().setProjectCode(wrapper.getProductModel().getProductCode());
        logger.info("Saving JS Trx Id: " + wrapper.getTransactionCodeModel().getCode());
        wrapper.getSafRepoCashOutModel().setTransactionCode(wrapper.getTransactionCodeModel().getCode());
        logger.info("Saving BOP Trx Id: " + wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID));
        wrapper.getSafRepoCashOutModel().setTransactionId((String)wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID));
//        wrapper.getSafRepoCashOutModel().setTerminalId(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
        wrapper.getSafRepoCashOutModel().setSellerCode(wrapper.getUserDeviceAccountModel().getUserId());
        wrapper.getSafRepoCashOutModel().setSessionId(wrapper.getObject(CommandFieldConstants.KEY_SESSION_ID).toString());
        wrapper.getSafRepoCashOutModel().setCreatedOn(new Date());
        wrapper.getSafRepoCashOutModel().setUpdatedOn(new Date());
        genericDAO.createEntity(wrapper.getSafRepoCashOutModel());
        return wrapper;
    }

    private Boolean isNadraError(String errorCode){
        Boolean response=false;
        if(errorCode.equals("040") || errorCode.equals("044") || errorCode.equals("047") || errorCode.equals("048") || errorCode.equals("049") || errorCode.equals("050") || errorCode.equals("103") ||
                errorCode.equals("111") || errorCode.equals("118") || errorCode.equals("119") || errorCode.equals("120") || errorCode.equals("121") || errorCode.equals("122") || errorCode.equals("123") || errorCode.equals("124"))
            response=true;
        return response;
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


    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    public void setBispManager(BISPManager bispManager) {
        this.bispManager = bispManager;
    }
}
