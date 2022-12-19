package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.ATTR_DATE;

public class ExciseAndTaxationPaymentCommand extends BaseCommand {

    private AppUserModel appUserModel, customerAppUserModel;
    private BaseWrapper baseWrapper;
    private String customerMobileNo, withdrawalAmount;
    private String productId, encryptionType;
    private String processingAmount, totalAmount;
    private ProductModel productModel;
    private CustomerModel customerModel;
    private CommissionAmountsHolder commissionAmountsHolder;
    private WorkFlowWrapper workFlowWrapper;

    private String vehicleRegNo,vehicleChesisNo,assessmentNumber,exciseAssessmentAmount;

    protected final Log logger = LogFactory.getLog(ExciseAndTaxationPaymentCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[ExciseAndTaxationPaymentCommand.prepare] ";
        this.logger.info("Start of " + classNMethodName);
        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        vehicleChesisNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_VEHICLE_CHESIS_NO);
        assessmentNumber = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_ASSESSMENT_NO);
        vehicleRegNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_VEHICLE_REG_NO);
        exciseAssessmentAmount = getCommandParameter(baseWrapper,CommandFieldConstants.KEY_ASSESSMENT_AMOUNT);
        customerMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        //withdrawalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        processingAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
        totalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel)bWrapper.getBasePersistableModel();
        }catch(Exception ex){
            logger.error(classNMethodName +" Product model not found: ", ex);
        }

        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            customerAppUserModel = new AppUserModel();
            customerAppUserModel.setMobileNo(customerMobileNo);
            customerAppUserModel = getCommonCommandManager().loadAppUserByQuery(customerMobileNo, UserTypeConstantsInterface.CUSTOMER);
            if(customerAppUserModel != null) {
                customerModel = new CustomerModel();
                customerModel.setCustomerId(customerAppUserModel.getCustomerId());
                bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(customerModel);
                bWrapper = getCommonCommandManager().loadCustomer(bWrapper);
                customerModel = (CustomerModel)bWrapper.getBasePersistableModel();
            }

        }catch(Exception ex){
            logger.error(classNMethodName + "Customer App user model not found: ", ex);
        }
        this.logger.info("End of " + classNMethodName);
    }

    @Override
    public void doValidate() throws CommandException {
        String classNMethodName = "[ExciseAndTaxationPaymentCommand.validate] ";
        this.logger.info("Start of " + classNMethodName);
        validationErrors = new ValidationErrors();
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");
        //validationErrors = ValidatorWrapper.doRequired(withdrawalAmount, validationErrors, "Withdrawal Amount");
        validationErrors = ValidatorWrapper.doRequired(processingAmount, validationErrors, "Processing Amount");
        validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");
        validationErrors = ValidatorWrapper.doRequired(assessmentNumber,validationErrors,"Assessment Number");
        validationErrors = ValidatorWrapper.doRequired(vehicleRegNo,validationErrors,"Vehicle Registration #");
        validationErrors = ValidatorWrapper.doRequired(vehicleChesisNo,validationErrors,"Vehicle Chesis #");
        validationErrors = ValidatorWrapper.doRequired(exciseAssessmentAmount,validationErrors,"Excise Assessment Amount");
        if(validationErrors != null && !validationErrors.hasValidationErrors()) {
            //validationErrors = ValidatorWrapper.doNumeric(withdrawalAmount,validationErrors,"Withdrawal Amount");
            validationErrors = ValidatorWrapper.doNumeric(processingAmount, validationErrors, "Processing Amount");
            validationErrors = ValidatorWrapper.doNumeric(totalAmount, validationErrors, "Total Amount");
            validationErrors = ValidatorWrapper.doNumeric(exciseAssessmentAmount, validationErrors, "Excise Assessment Amount");
        }
        if(null== productModel) {
            logger.error(classNMethodName + "Product model not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
        }
        if(null == customerAppUserModel) {
            logger.error(classNMethodName + "Customer App User not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }

        if(null == customerModel) {
            logger.error(classNMethodName + "Customer model not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }


        this.logger.info("End of " + classNMethodName);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        String classNMethodName = "[ExciseAndTaxationPaymentCommand.execute()] ";
        this.logger.info("Start of " + classNMethodName);
        workFlowWrapper = new WorkFlowWrapperImpl();

        try{
            TransactionModel transactionModel = new TransactionModel();
            transactionModel.setTransactionAmount(Double.valueOf(totalAmount));
            workFlowWrapper.setTransactionModel(transactionModel);
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            workFlowWrapper.setAccountInfoModel(accountInfoModel);
            workFlowWrapper.setTxProcessingAmount(Double.parseDouble(processingAmount));
            workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
            workFlowWrapper.setTransactionAmount(Double.parseDouble(totalAmount));
            workFlowWrapper.setBillAmount(Double.valueOf(totalAmount));
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
            // Velocity validation
            BaseWrapper vWrapper = new BaseWrapperImpl();
            vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
            vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(totalAmount));
            vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, customerModel.getSegmentId());
            vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, customerModel.getCustomerAccountTypeId());
            vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            getCommonCommandManager().checkVelocityCondition(vWrapper);
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID,productId);
            /*ProductVO productVo = getCommonCommandManager().loadProductVO(this.baseWrapper);
            if(productVo == null) {
                throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM,new Throwable());
            }*/
            workFlowWrapper.setCustomerModel(customerModel);
            workFlowWrapper.setSegmentModel(new SegmentModel());
            workFlowWrapper.getSegmentModel().setSegmentId(customerModel.getSegmentId());
            //workFlowWrapper.setProductVO(productVo);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setAppUserModel(customerAppUserModel);
            workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            BaseWrapper userWrapper = new BaseWrapperImpl();
            userWrapper.setBasePersistableModel(appUserModel);
            userWrapper = commonCommandManager.loadUserDeviceAccountByMobileNumber(userWrapper);
            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel((UserDeviceAccountsModel) userWrapper.getBasePersistableModel());
            workFlowWrapper.setUserDeviceAccountModel((UserDeviceAccountsModel) userWrapper.getBasePersistableModel());

            workFlowWrapper.setCustomField(0);

            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            @SuppressWarnings("rawtypes")
            CustomList smaList = searchBaseWrapper.getCustomList();
            //Customer smart money account
            searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
            customerSMAModel.setCustomerId(customerAppUserModel.getCustomerId());
            customerSMAModel.setActive(Boolean.TRUE);
            customerSMAModel.setDeleted(Boolean.FALSE);
            customerSMAModel.setDefAccount(Boolean.TRUE);
            searchBaseWrapper.setBasePersistableModel(customerSMAModel);
            searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);
            smaList = searchBaseWrapper.getCustomList();
            if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
                customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
            }
            workFlowWrapper.setOlaSmartMoneyAccountModel(customerSMAModel);
            BaseWrapper retailerWrapper = new BaseWrapperImpl();
            RetailerContactModel retailerContactModel = new RetailerContactModel();
            retailerContactModel.setPrimaryKey(appUserModel.getRetailerContactId());
            retailerWrapper.setBasePersistableModel(retailerContactModel);
            retailerWrapper = commonCommandManager.loadRetailerContact(retailerWrapper);
            retailerContactModel = (RetailerContactModel) retailerWrapper.getBasePersistableModel();
            workFlowWrapper.setFromRetailerContactModel(retailerContactModel);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            SmartMoneyAccountModel agentSMAModel =  new SmartMoneyAccountModel();
            agentSMAModel.setRetailerContactId(appUserModel.getRetailerContactId());
            agentSMAModel.setActive(Boolean.TRUE);
            agentSMAModel.setDeleted(Boolean.FALSE);
            agentSMAModel.setDefAccount(Boolean.TRUE);
            searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(agentSMAModel);
            searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);
            smaList = searchBaseWrapper.getCustomList();
            if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
                agentSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
            }
            workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);
            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.EXCISE_AND_TAXATION_TX);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));
            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            workFlowWrapper.setLeg2Transaction(Boolean.FALSE); // To avoid saving of TransactionDetailMaster incase of Exception
            workFlowWrapper.putObject(CommandFieldConstants.KEY_VEHICLE_CHESIS_NO,vehicleChesisNo);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_VEHICLE_REG_NO,vehicleRegNo);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_ASSESSMENT_NO,assessmentNumber);
            workFlowWrapper.putObject(CommandFieldConstants.KEY_ASSESSMENT_AMOUNT,exciseAssessmentAmount);
            workFlowWrapper.putObject("product",productModel);
            commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);
            commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();
            //commonCommandManager.sendSMS(workFlowWrapper);

        }catch(Exception e){
            logger.error("Command Exeution Error:", e);
            handleException(e);
        }
    }

    @Override
    public String response() {
        TransactionModel transactionModel = workFlowWrapper.getTransactionModel();
        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>(30);
        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(workFlowWrapper.getTransactionCodeModel().getCode())));
//        params.add(new LabelValueBean(CommandFieldConstants.KEY_VEHICLE_REG_NO, vehicleRegNo));
//        params.add(new LabelValueBean(CommandFieldConstants.KEY_ASSESSMENT_AMOUNT, exciseAssessmentAmount));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_ASSESSMENT_AMOUNT, Formatter.formatNumbers(Double.parseDouble(exciseAssessmentAmount))));
        params.add(new LabelValueBean(ATTR_CAMT, replaceNullWithEmpty(workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges() + "")));
        params.add(new LabelValueBean(ATTR_CAMTF, Formatter.formatNumbers(workFlowWrapper.getTransactionDetailMasterModel().getExclusiveCharges())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_AGENT_MOBILE, appUserModel.getMobileNo() ));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, customerAppUserModel.getNic()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT, replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_PRODUCT_NAME,  replaceNullWithEmpty(productModel.getName())));
        params.add(new LabelValueBean(ATTR_TAMT, replaceNullWithEmpty(transactionModel.getTotalAmount()+"")));
        params.add(new LabelValueBean(ATTR_TAMTF, Formatter.formatNumbers(transactionModel.getTotalAmount())));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(transactionModel.getCreatedOn(),PortalDateUtils.SHORT_DATE_FORMAT_2)));
        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(transactionModel.getTransactionAmount()+"")));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(transactionModel.getTransactionAmount())));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(transactionModel.getCreatedOn())));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(transactionModel.getCreatedOn()+"")));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_VEHICLE_REG_NO, vehicleRegNo));
        params.add(new LabelValueBean(ATTR_BALF, Formatter.formatNumbers(workFlowWrapper.getSwitchWrapper().getOlavo().getToBalanceAfterTransaction())));
        return MiniXMLUtil.createResponseXMLByParams(params);
    }
}
