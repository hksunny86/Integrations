package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class BOPCashOutCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(BOPCashOutCommand.class);

    private String customerMobileNo;
    private String cNic;
    private String customerCardNo;
    private String isCard;
    private String customerSegmentId;
    private String transactionAmount,transactionProcessingAmount;
    private String productId;
    private String isBvsRequired;
    private String fingerIndex, templateType, fingerTemplate, nadraSessionId, thirdPartyTransactionId,terminalId,sessionId;
    private String otp;

    private AppUserModel appUserModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private ProductModel productModel;
    private BaseWrapper baseWrapper;
    private CommissionAmountsHolder commissionAmountsHolder;
    private I8SBSwitchControllerResponseVO responseVO;
    private TransactionModel transactionModel;
    private RetailerContactModel retailerContactModel;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[BOPCashOutCommand.prepare] ";
        responseVO = new I8SBSwitchControllerResponseVO();
        otp = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_OTP);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        cNic = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CNIC);
        customerCardNo = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DEBIT_CARD_NO);
        isCard = this.getCommandParameter(baseWrapper,"IS_CARD");
        customerSegmentId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
        transactionProcessingAmount = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TPAM);
        productId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_PROD_ID);
        nadraSessionId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_NADRA_SESSION_ID);
        thirdPartyTransactionId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID);
        terminalId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TERMINAL_ID);
        sessionId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_SESSION_ID);
        isBvsRequired = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_IS_BVS_REQUIRED);
        if (isBvsRequired.equals("1")) {
            fingerIndex = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_INDEX);
            templateType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TEMPLATE_TYPE);
            fingerTemplate = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_FINGER_TEMPLATE);
        }
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel) bWrapper.getBasePersistableModel();

            RetailerContactModel retContactModel = new RetailerContactModel();
            retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(retContactModel);
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

            retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
        }

        catch(Exception ex){
            ex.printStackTrace();
            logger.error(classNMethodName +" Product model not found: ");
        }
        if(productId.equals(ProductConstantsInterface.BOP_CASH_OUT_COVID_19.toString()))
            customerSegmentId = productModel.getProductCode();
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if(isCard.equals("0"))
        {
            validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Mobile No");
            validationErrors = ValidatorWrapper.doRequired(customerSegmentId,validationErrors,"Segment");
        }
        else if(isCard.equals("2")){
            validationErrors = ValidatorWrapper.doRequired(cNic,validationErrors,"Customer NIC ");
        }
        else
            validationErrors = ValidatorWrapper.doRequired(customerCardNo,validationErrors,"Card No");

        validationErrors = ValidatorWrapper.doRequired(transactionAmount,validationErrors,"Amount");
        if(validationErrors.hasValidationErrors())
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH,new Throwable());
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        String classNMethodName = "[BOPCashOutCommand.execute] ";
        String exceptionMessage = "Exception Occurred for Logged in AppUser";
        String inputParams = " ID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " + productId
                + " Mobile No:" + customerMobileNo
                + " NIC #: " + cNic;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        try{
            workFlowWrapper.setTransactionAmount(Double.parseDouble(transactionAmount));
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
            // check product limit
            getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                    Long.valueOf(deviceTypeId), Double.parseDouble(transactionAmount), productModel, null, workFlowWrapper.getHandlerModel());

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();

            // Velocity validation
            BaseWrapper vWrapper = new BaseWrapperImpl();
            vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID, Long.parseLong(deviceTypeId));
            vWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
            vWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
            vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(transactionAmount));
            vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
            vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
            vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            getCommonCommandManager().checkVelocityCondition(vWrapper);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setAppUserModel(appUserModel);
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel agentSMAModel = new SmartMoneyAccountModel();
            agentSMAModel.setRetailerContactId(appUserModel.getRetailerContactId());
            agentSMAModel.setActive(Boolean.TRUE);
            searchBaseWrapper.setBasePersistableModel(agentSMAModel);
            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            @SuppressWarnings("rawtypes")
            CustomList smaList = searchBaseWrapper.getCustomList();
            if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
                agentSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
            }
            workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);
            AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(appUserModel.getAppUserId(), agentSMAModel.getName());
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BOP_CASH_OUT_TX);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            if (isBvsRequired.equals("0"))
                workFlowWrapper.putObject(CommandFieldConstants.KEY_OTP, otp);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, terminalId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_SESSION_ID, sessionId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER, customerMobileNo);
            if (isBvsRequired.equals("1")) {
                workFlowWrapper.putObject(CommandFieldConstants.KEY_FINGER_INDEX, fingerIndex);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_TEMPLATE_TYPE, templateType);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_FINGER_TEMPLATE, fingerTemplate);
                workFlowWrapper.putObject(CommandFieldConstants.KEY_IS_BVS_REQUIRED,isBvsRequired);
            }
            workFlowWrapper.putObject(CommandFieldConstants.KEY_PRODUCT_ID, productId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_NADRA_SESSION_ID, nadraSessionId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_TRANSACTION_ID, thirdPartyTransactionId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE,customerSegmentId);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_DEBIT_CARD_NO,EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",customerCardNo));
            workFlowWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_CNIC,cNic);
            workFlowWrapper.putObject("IS_CARD",isCard);
            if(isBvsRequired.equals("1")){
                workFlowWrapper.setReceiverBvs(Boolean.TRUE);
            }
//            workFlowWrapper.setReceiverBvs(Boolean.parseBoolean(isBvsRequired));
            logger.info(classNMethodName + " Going to execute Transaction flow. " + inputParams);
            workFlowWrapper = getCommonCommandManager().executeSaleCreditTransaction(workFlowWrapper);
            transactionModel = workFlowWrapper.getTransactionModel();
            productModel = workFlowWrapper.getProductModel();
            //
            getCommonCommandManager().sendSMS(workFlowWrapper);
        }
        catch (Exception ex){
            ex.printStackTrace();
            logger.error(genericExceptionMessage);
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }
    }

    @Override
    public String response() {
        List<LabelValueBean> lvbs = new ArrayList<LabelValueBean>();
        lvbs.add(new LabelValueBean("IS_CARD", isCard));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, cNic));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DEBIT_CARD_NO, customerCardNo));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, transactionModel.getCreatedOn().toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, productModel.getName()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, transactionModel.getTransactionCodeIdTransactionCodeModel().getCode()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAM, replaceNullWithZero(transactionModel.getTransactionAmount()).toString()));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(transactionModel.getTransactionAmount()))));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMT, replaceNullWithEmpty(transactionModel.getTotalCommissionAmount().toString())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(transactionModel.getTotalCommissionAmount())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAM, transactionProcessingAmount));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(Double.parseDouble(transactionProcessingAmount))));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMT, String.valueOf(transactionModel.getTotalAmount().doubleValue())));
        lvbs.add(new LabelValueBean(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        return MiniXMLUtil.createResponseXMLByParams(lvbs);
    }
}
