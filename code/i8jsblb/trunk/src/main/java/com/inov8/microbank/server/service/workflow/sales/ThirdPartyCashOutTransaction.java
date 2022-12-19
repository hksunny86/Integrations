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
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.productdeviceflowmodule.ProductDeviceFlowListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.bispcustnadraverification.BISPCustNadraVerificationDAO;
import com.inov8.microbank.server.service.bispmodule.BISPManager;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.financialintegrationmodule.FinancialIntegrationManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.integration.vo.CashWithdrawalVO;
import com.inov8.microbank.server.service.messagemodule.NotificationMessageManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.mfsmodule.UserDeviceAccountsManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Attique on 9/4/2018.
 */
public class ThirdPartyCashOutTransaction extends SalesTransaction {

    private MessageSource messageSource;
    private CommissionManager commissionManager;
    private UserDeviceAccountsManager userDeviceAccountsManager;
    private NotificationMessageManager notificationMessageManager;
    private GenericDao genericDAO;
    private FinancialIntegrationManager financialIntegrationManager;
    private BISPManager bispManager;

    private ESBAdapter esbAdapter;

    public ThirdPartyCashOutTransaction(){
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
        if (logger.isDebugEnabled()) {
            logger.debug("Inside calculateCommission(WorkFlowWrapper wrapper) of CashWithdrawalTransaction...");
        }

        // wrapper.setSegmentModel(new SegmentModel());

        CommissionWrapper commissionWrapper = new CommissionWrapperImpl();
        commissionWrapper.setPaymentModeModel(wrapper.getTransactionModel().getPaymentModeIdPaymentModeModel());
        commissionWrapper.setTransactionModel(wrapper.getTransactionModel());
        commissionWrapper.setTransactionTypeModel(wrapper.getTransactionTypeModel());
        commissionWrapper.setProductModel(wrapper.getProductModel());

        wrapper.setTaxRegimeModel(wrapper.getRetailerContactModel().getTaxRegimeIdTaxRegimeModel());

        commissionWrapper = commissionManager.calculateCommission(wrapper);

        if (logger.isDebugEnabled()) {
            logger.debug("Ending calculateCommission(WorkFlowWrapper wrapper) of CashWithdrawalTransaction...");
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
            logger.debug("Inside validateCommission of CashWithdrawalTransaction...");
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
            logger.debug("Ending validateCommission of CashWithdrawalTransaction...");
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
            logger.debug("Inside doValidate(WorkFlowWrapper wrapper) of CashWithdrawalTransaction");

        if(null == wrapper.getAppUserModel())
            throw new WorkFlowException(WorkFlowErrorCodeConstants.AGENT_ACCOUNT_NOT_FOUND);

       /* if (wrapper.getCustomerAppUserModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.CUSTOMER_MODEL_NULL);*/


        if (wrapper.getUserDeviceAccountModel() == null)
            throw new WorkFlowException(WorkFlowErrorCodeConstants.NO_DEVICE_ACCOUNT_FOR_USER);



        if(null != ThreadLocalAppUser.getAppUserModel().getCustomerId()) {  // for CW. Threadlocal user is agent hence this if
            if (!wrapper.getOlaSmartMoneyAccountModel().getCustomerId().toString().equals(ThreadLocalAppUser.getAppUserModel().getCustomerId().toString()))
                throw new WorkFlowException(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT);
        }


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
            logger.debug("Ending doValidate(WorkFlowWrapper wrapper) of CashWithdrawalTransaction");

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

        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
        wrapper.setSegmentModel(segmentModel);
        // calculate and set commissions to transaction & transaction details model
        CommissionWrapper commissionWrapper = calculateCommission(wrapper);
        CommissionAmountsHolder commissionAmounts = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(
                CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

        //validateCommission(commissionWrapper, wrapper); // validate commission against the one calculated against the bill and the one coming from iPos

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
/*        wrapper.setAppUserModel(wrapper.getCustomerAppUserModel());

        abstractFinancialInstitution.checkDebitCreditLimitOLAVO(wrapper);*/
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

        // Set Handler Detail in Transaction and Transaction Detail Master
        if(wrapper.getHandlerModel() != null){
            wrapper.getTransactionModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.getTransactionModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
            wrapper.getTransactionDetailMasterModel().setHandlerId(wrapper.getHandlerModel().getHandlerId());
            wrapper.getTransactionDetailMasterModel().setHandlerMfsId(wrapper.getHandlerUserDeviceAccountModel().getUserId());
        }


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

/*
        double customerBalance = switchWrapper.getOlavo().getFromBalanceAfterTransaction(); // balance
        ((CashWithdrawalVO)wrapper.getProductVO()).setCustomerBalance(customerBalance);
*/

        Double agentBalance = switchWrapper.getOlavo().getToBalanceAfterTransaction(); // agent balance
        /*        ((CashWithdrawalVO)wrapper.getProductVO()).setAgentBalance(agentBalance);*/

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
        //wrapper.getTransactionDetailModel().setCustomField1(""+wrapper.getOlaSmartMoneyAccountModel().getSmartMoneyAccountId());

        ArrayList<SmsMessage> messageList = new ArrayList<SmsMessage>(0);
        String brandName=null;

        if(UserUtils.getCurrentUser().getMnoId()!=null && UserUtils.getCurrentUser().getMnoId().equals(50028L)){
            brandName = MessageUtil.getMessage("sco.brandName");
        }else {


            brandName = MessageUtil.getMessage("jsbl.brandName");
        }

        String agentSMS=this.getMessageSource().getMessage(
                "USSD.AgentThirdPArtyPayCashSMS",
                new Object[] {
                        brandName,
                        wrapper.getTransactionCodeModel().getCode(),
                        Formatter.formatNumbers(wrapper.getCommissionAmountsHolder().getTransactionAmount()),
                        wrapper.getProductModel().getName(),
                        PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_TIME_FORMAT),
                        PortalDateUtils.formatDate(wrapper.getTransactionModel().getCreatedOn(), PortalDateUtils.SHORT_DATE_FORMAT),
                        (agentBalance==null) ? 0.0 : agentBalance},
                null);

        wrapper.getTransactionDetailModel().setCustomField4(agentSMS);
        messageList.add(new SmsMessage(wrapper.getAppUserModel().getMobileNo(), agentSMS));




        txManager.saveTransaction(wrapper);

        settlementManager.settleCommission(commissionWrapper, wrapper);

        wrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
//wrapper.getCustomerAppUserModel().getAppUserTypeId();
        return wrapper;
    }

    /**
     * Update Workflow wrapper with different parameters required in further steps
     */

    private Boolean isNumeric(String value){
        String numerics = "0123456789.";
        value = value.trim();
        Boolean isValid = Boolean.TRUE;
        char c;
        for(int i = 0; i < value.length(); i++)
        {
            c = value.charAt(i);
            String.valueOf(c);

            if(numerics.indexOf(String.valueOf(c)) == -1)
            {
                isValid = Boolean.FALSE;
                break;
            }
        }
        return isValid;
    }

    private WorkFlowWrapper validateNIFQAndMinutaieValue(WorkFlowWrapper wrapper) throws Exception{
        String minutiaeCount = (String) wrapper.getObject(CommandFieldConstants.KEY_NADRA_MINUTIAE_COUNT);
        String nIfq = (String) wrapper.getObject(CommandFieldConstants.KEY_NADRA_NIFQ);
        wrapper.getTransactionDetailMasterModel().setnIfq(nIfq);
        wrapper.getTransactionDetailMasterModel().setMintaieCount(minutiaeCount);
        if(minutiaeCount != null && !minutiaeCount.equals("")){
            if((!isNumeric(minutiaeCount)))
            {
                wrapper.putObject("IS_NADRA_ERROR", ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE.toString());
                throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.134",null,null),ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE,
                        ErrorLevel.MEDIUM,new Throwable());
            }
            else if(Integer.parseInt(minutiaeCount) > 255)
            {
                wrapper.putObject("IS_NADRA_ERROR", ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE.toString());
                throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.134",null,null),ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE,
                        ErrorLevel.MEDIUM,new Throwable());
            }
            else if(Integer.parseInt(minutiaeCount) < 13){
                wrapper.putObject("IS_NADRA_ERROR", ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE.toString());
                throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.135",null,null),ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE,
                        ErrorLevel.MEDIUM,new Throwable());
            }
        }
        if(nIfq != null && !nIfq.equals("")){
            if(!isNumeric(nIfq))
            {
                wrapper.putObject("IS_NADRA_ERROR", ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE.toString());
                throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.133",null,null),ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE,
                        ErrorLevel.MEDIUM,new Throwable());
            }
            else if(Integer.parseInt(nIfq) > 3)
            {
                wrapper.putObject("IS_NADRA_ERROR", ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE.toString());
                throw new CommandException(MessageUtil.getMessage("i8sb.response.payment.136",null,null),ErrorCodes.INVALID_NIFQ_MINUTIAE_VALUE,
                        ErrorLevel.MEDIUM,new Throwable());
            }
        }
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreStart(WorkFlowWrapper wrapper) throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("Inside doPreStart(WorkFlowWrapper wrapper) of CashWithdrawalTransaction....");

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



        // Set Handler User Device Account Model
        if(wrapper.getHandlerAppUserModel() != null && wrapper.getHandlerAppUserModel().getAppUserId() != null){
            UserDeviceAccountsModel handlerUserDeviceAccountsModel = new UserDeviceAccountsModel();
            handlerUserDeviceAccountsModel.setAppUserId(wrapper.getHandlerAppUserModel().getAppUserId());
            handlerUserDeviceAccountsModel.setDeviceTypeId(DeviceTypeConstantsInterface.ALL_PAY);
            baseWrapper.setBasePersistableModel(handlerUserDeviceAccountsModel);
            baseWrapper = this.userDeviceAccountsManager.loadUserDeviceAccount(baseWrapper);
            wrapper.setHandlerUserDeviceAccountModel((UserDeviceAccountsModel) baseWrapper.getBasePersistableModel());
        }
        Boolean skipForLocalTesting = (Boolean) wrapper.getObject("skipForLocalTesting");
        wrapper=this.prepareSafRepoCashOutModel(wrapper);
        this.validateNIFQAndMinutaieValue(wrapper);
        I8SBSwitchControllerRequestVO requestVo=new I8SBSwitchControllerRequestVO();
        if(wrapper.getObject( CommandFieldConstants.KEY_PRODUCT_ID).equals(ProductConstantsInterface.EOBI_CASH_OUT_ID)) {
            /*requestVo = esbAdapter.prepareRequestVoForApiGee(I8SBConstants.RequestType_EOBI_CashWithdrawal);
            requestVo.setAmount(String.valueOf( wrapper.getTransactionAmount()));
                requestVo.setOtp(wrapper.getObject(CommandFieldConstants.KEY_OTP).toString());*/
        }
        else
            requestVo=esbAdapter.prepareRequestVoForApiGee(I8SBConstants.RequestType_CashWithdrawal);
        requestVo.setConsumerNumber(wrapper.getObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER).toString());
        requestVo.setTransactionAmount(String.valueOf(wrapper.getTransactionAmount()));
        String thirdPartyTransactionId = (String) wrapper.getObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID);
        if(thirdPartyTransactionId != null && thirdPartyTransactionId.equals(""))
            thirdPartyTransactionId = requestVo.getRRN();
        wrapper.getSafRepoCashOutModel().setTransactionId(thirdPartyTransactionId);
        requestVo.setTransactionId(thirdPartyTransactionId);
        requestVo.setTransmissionDateAndTime(String.valueOf( new Date()));
        requestVo.setSenderMobile(wrapper.getAppUserModel().getMobileNo());
        requestVo.setAgentId(wrapper.getUserDeviceAccountModel().getUserId());
        requestVo.setCNIC(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
        requestVo.setMobilePhone(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE).toString());
        requestVo.setProductCode(wrapper.getProductModel().getProductCode());
        requestVo.setTerminalID(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
        requestVo.setSessionId(wrapper.getObject(CommandFieldConstants.KEY_SESSION_ID).toString());
        requestVo.setSessionIdNadra((String)wrapper.getObject(CommandFieldConstants.KEY_NADRA_SESSION_ID));
        requestVo.setWalletRequired((String)wrapper.getObject(CommandFieldConstants.KEY_BAFL_WALLET));
        requestVo.setWalletAccountId((String)wrapper.getObject(CommandFieldConstants.KEY_BAFL_WALLET_ACCOUNT_ID));
        requestVo.setImeiNumber((String) wrapper.getObject(CommandFieldConstants.KEY_MOBILE_IMEI_NUMBER));
        requestVo.setMacAddress((String) wrapper.getObject(CommandFieldConstants.KEY_MAC_ADDRESS));
        requestVo.setIpAddress((String) wrapper.getObject(CommandFieldConstants.KEY_IP_ADDRESS));
        requestVo.setLatitude((String) wrapper.getObject(CommandFieldConstants.KEY_LATITUDE));
        requestVo.setLongitude((String) wrapper.getObject(CommandFieldConstants.KEY_LONGITUDE));
        String transactionType = null;
        String bAflExists = null;
        if(wrapper.getObject(CommandFieldConstants.KEY_BAFL_WALLET) != null)
            bAflExists = (String) wrapper.getObject(CommandFieldConstants.KEY_BAFL_WALLET);
        if(bAflExists != null && bAflExists.equals("Y"))
            transactionType = "T";
        else
            transactionType = "W";
        requestVo.setTransactionType(transactionType);
        requestVo.setReserved1("");
        requestVo.setAreaName("SINDH");
        String bvsConfigId="";
        if(null!=wrapper.getObject( CommandFieldConstants.KEY_IS_BVS_REQUIRED))
            bvsConfigId = wrapper.getObject( CommandFieldConstants.KEY_IS_BVS_REQUIRED).toString();

        if(bvsConfigId.equals("2")){
            requestVo.setFingerIndex(wrapper.getObject(CommandFieldConstants.KEY_FINGER_INDEX).toString());
            requestVo.setTempeleteType(wrapper.getObject(CommandFieldConstants.KEY_TEMPLATE_TYPE).toString());
            requestVo.setFingerTemplete(wrapper.getObject(CommandFieldConstants.KEY_FINGER_TEMPLATE).toString());
        }
        requestVo.setMinutiaeCount((String)wrapper.getObject(CommandFieldConstants.KEY_NADRA_MINUTIAE_COUNT));
        requestVo.setNfiq((String)wrapper.getObject(CommandFieldConstants.KEY_NADRA_NIFQ));
        I8SBSwitchControllerResponseVO responseVO=new I8SBSwitchControllerResponseVO();
        SwitchWrapper sWrapper=new SwitchWrapperImpl();
        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
        sWrapper.setI8SBSwitchControllerRequestVO(requestVo);
        sWrapper.setTransactionTransactionModel(wrapper.getTransactionModel());
        if(!skipForLocalTesting)
            wrapper.getSafRepoCashOutModel().setTransactionId(requestVo.getTransactionId());
        wrapper.putObject("IS_CALL_SENT","1");
        if(!skipForLocalTesting)
            sWrapper=esbAdapter.makeI8SBCall(sWrapper);

        responseVO = sWrapper.getI8SBSwitchControllerResponseVO();
        //
        if (responseVO == null && !skipForLocalTesting) {
            this.logger.error("ResponseVO is Null");
            ESBAdapter.processI8sbResponseCode(responseVO, false);
        } else if (responseVO != null && responseVO.getResponseCode() == null  && !skipForLocalTesting) {
            this.logger.error("Response Code is null.");
            ESBAdapter.processI8sbResponseCode(responseVO, false);
        }
        if(skipForLocalTesting)
        {
            String responseCode = MessageUtil.getMessage("BISP.MOCK.RESPONSE.CODE");
            if(responseCode.equals(""))
                responseCode = "I8SB-500";
            sWrapper.getI8SBSwitchControllerResponseVO().setResponseCode(responseCode);
            sWrapper.getI8SBSwitchControllerResponseVO().setConsumerNumber("1000567467");
        }
        if(null!=sWrapper.getI8SBSwitchControllerResponseVO().getResponseCode()) {

            if(wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.EOBI_CASH_OUT)){
                wrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE,sWrapper.getI8SBSwitchControllerResponseVO().getCustomerAccNumber());
            }
            else
                wrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE,sWrapper.getI8SBSwitchControllerResponseVO().getConsumerNumber());
            if (isNadraError(sWrapper.getI8SBSwitchControllerResponseVO().getResponseCode()).equals(Boolean.TRUE)) {
                wrapper.putObject("IS_NADRA_ERROR", responseVO.getResponseCode());
                wrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID, sWrapper.getI8SBSwitchControllerResponseVO().getI8SBSwitchControllerResponseVOList().get(1).getSessionIdNadra());
                wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID,thirdPartyTransactionId);
            }
            if(!skipForLocalTesting)
            {
                /*wrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID, sWrapper.getI8SBSwitchControllerResponseVO().getI8SBSwitchControllerResponseVOList().get(1).getSessionIdNadra());
                wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID,thirdPartyTransactionId);*/
            }
            else
                wrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID,"1004356789");
        }
        if(!wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.EOBI_CASH_OUT))
        {
            wrapper.putObject("I8SB_RESPONSE_CODE",sWrapper.getI8SBSwitchControllerResponseVO().getResponseCode());
            String errorMessage = bispManager.saveOrUpdateBVSEntryRequiresNewTransaction(wrapper.getUserDeviceAccountModel(),wrapper.getAppUserModel()
                    ,(String)wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC),sWrapper,wrapper.getTransactionCodeModel().getCode());
            if(errorMessage != null)
            {
                String responseCode = sWrapper.getI8SBSwitchControllerResponseVO().getResponseCode();
                if(responseCode != null && responseCode.equals("I8SB-500"))
                {
                    responseCode = "500";
                    wrapper.putObject("IS_NADRA_ERROR", ErrorCodes.THIRD_PARTY_TRANSACTION_TIME_OUT.toString());
                }
                throw new CommandException(errorMessage, Long.valueOf(responseCode),ErrorLevel.MEDIUM,null);
            }
            ESBAdapter.processI8sbResponseCode(responseVO,false);
            wrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRX_INTERNAL_ERROR,"1");
        }
        if(wrapper.getProductModel().getProductId().equals(ProductConstantsInterface.EOBI_CASH_OUT)){
            esbAdapter.processI8sbResponseCodeForEOBI(sWrapper.getI8SBSwitchControllerResponseVO(),true);
            wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode(sWrapper.getI8SBSwitchControllerResponseVO().getSessionId());
        }
        else
            wrapper.getTransactionDetailMasterModel().setFonepayTransactionCode(requestVo.getTransactionId());

        if (logger.isDebugEnabled())
            logger.debug("Ending doPreStart(WorkFlowWrapper wrapper) of CashWithdrawalTransaction....");
        return wrapper;
    }

    @Override
    protected WorkFlowWrapper doPreProcess(WorkFlowWrapper wrapper) throws Exception {
        // prepare workflow wrapper with transaction model having all required inputs
        wrapper = super.doPreProcess(wrapper);
        // agent assisted customer transaction need customer verification through IVR
        // in case of IVR response no need to perform pre-process steps

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
        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        // Smart Money Account Id is populated
        txModel.setSmartMoneyAccountId(wrapper.getSmartMoneyAccountModel().getSmartMoneyAccountId());

        // Payment mode model of transaction is populated
        txModel.setPaymentModeId(wrapper.getSmartMoneyAccountModel().getPaymentModeId());

        txModel.setSaleMobileNo(wrapper.getAppUserModel().getMobileNo());

        // Populate processing Bank Id
        txModel.setProcessingBankId(BankConstantsInterface.ASKARI_BANK_ID);

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

    private Double getTransactionProcessingCharges(CommissionWrapper commissionWrapper, WorkFlowWrapper workFlowWrapper) {
        if (logger.isDebugEnabled())
            logger.debug("Inside getTransactionProcessingCharges of CashWithdrawalTransaction....");

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
            logger.debug("Ending getTransactionProcessingCharges of CashWithdrawalTransaction....");

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
        if(wrapper.getObject("IS_CALL_SENT").equals("1") && (null==wrapper.getObject("IS_NADRA_ERROR") || !wrapper.getObject("IS_NADRA_ERROR").equals("1"))) {
            if(wrapper.getSafRepoCashOutModel() != null)
            {
                wrapper.getSafRepoCashOutModel().setCreatedOn(new Date());
                wrapper.getSafRepoCashOutModel().setUpdatedOn(new Date());
                genericDAO.createEntity(wrapper.getSafRepoCashOutModel());
            }
        }
        return wrapper;
    }

    public WorkFlowWrapper prepareSafRepoCashOutModel(WorkFlowWrapper wrapper){

        wrapper.setSafRepoCashOutModel(new SafRepoCashOutModel());
//        wrapper.getSafRepoCashOutModel().setSegmentId(wrapper.getSegmentModel().getSegmentId());
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
        wrapper.getSafRepoCashOutModel().setTransactionCode(wrapper.getTransactionCodeModel().getCode());
        wrapper.getSafRepoCashOutModel().setCustomerCnic(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
        wrapper.getSafRepoCashOutModel().setCustomerAccountNumber(wrapper.getObject(CommandFieldConstants.KEY_CUSTOMER_CNIC).toString());
        wrapper.getSafRepoCashOutModel().setTerminalId(wrapper.getObject(CommandFieldConstants.KEY_TERMINAL_ID).toString());
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
                errorCode.equals("111") || errorCode.equals("118") || errorCode.equals("119") || errorCode.equals("120") || errorCode.equals("121") || errorCode.equals("122") || errorCode.equals("123") || errorCode.equals("124")
                || errorCode.equals("131"))
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
