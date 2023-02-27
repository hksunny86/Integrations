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
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CustomerCashWithdrawalLeg2Command extends BaseCommand {
    private AppUserModel appUserModel, customerAppUserModel;
    private BaseWrapper baseWrapper;
    private String customerMobileNo, agentMobileNo, withdrawalAmount;
    private String productId, encryptionType, CNIC;
    private String commissionAmount, processingAmount, totalAmount;
    private String transactionId;
    private RetailerContactModel retailerContactModel;
    private ProductModel productModel;
    private CustomerModel customerModel;
    private Double customerBalance;
    private String encryptedOTP;
    private CommissionAmountsHolder commissionAmountsHolder;
    private WorkFlowWrapper workFlowWrapper;
    protected final Log logger = LogFactory.getLog(CustomerCashWithdrawalLeg2Command.class);

    @Override
    public void execute() throws CommandException {
        workFlowWrapper = new WorkFlowWrapperImpl();

        TransactionCodeModel transactionCodeModel = new TransactionCodeModel();
        transactionCodeModel.setCode(transactionId);
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(transactionCodeModel);


        try{
            AccountInfoModel accountInfoModel = new AccountInfoModel();
            accountInfoModel.setOldPin(""); // This PIN should never be used for validation
            workFlowWrapper.setAccountInfoModel(accountInfoModel);

            baseWrapper = commonCommandManager.loadTransactionCodeByCode(baseWrapper);
            transactionCodeModel = (TransactionCodeModel) baseWrapper.getBasePersistableModel();
            workFlowWrapper.setTransactionCodeModel(transactionCodeModel);
            workFlowWrapper.setOneTimePin(encryptedOTP);

            commonCommandManager.makeOTPValidation(workFlowWrapper);


            workFlowWrapper.setTxProcessingAmount(Double.parseDouble(processingAmount));
            workFlowWrapper.setTotalAmount(Double.parseDouble(totalAmount));
            workFlowWrapper.setTransactionAmount(Double.parseDouble(withdrawalAmount));
            workFlowWrapper.setTotalCommissionAmount(Double.parseDouble(commissionAmount));
            workFlowWrapper.setHandlerModel(handlerModel);
            workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

            String userId = ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel().getUserId();
            // Velocity validation
            BaseWrapper vWrapper = new BaseWrapperImpl();
            vWrapper.putObject(CommandConstants.VELOCITY_PRODUCT_ID, Long.parseLong(productId));
            vWrapper.putObject(CommandConstants.VELOCITY_DEVICE_TYPE_ID,Long.parseLong(deviceTypeId));
            vWrapper.putObject(CommandConstants.VELOCITY_DISTRIBUTOR_ID, retailerContactModel.getRetailerIdRetailerModel().getDistributorId());
            vWrapper.putObject(CommandConstants.VELOCITY_AGENT_TYPE, retailerContactModel.getDistributorLevelId());
            vWrapper.putObject(CommandConstants.VELOCITY_TRANSACTION_AMOUNT, Double.valueOf(withdrawalAmount));
            vWrapper.putObject(CommandConstants.VELOCITY_SEGMENT_ID, customerModel.getSegmentId());
            vWrapper.putObject(CommandConstants.VELOCITY_ACCOUNT_TYPE, retailerContactModel.getOlaCustomerAccountTypeModel().getCustomerAccountTypeId());
            vWrapper.putObject(CommandConstants.VELOCITY_CUSTOMER_ID, userId);
//            vWrapper.putObject(CommandConstants.VELOCITY_RETAILER_ID, retailerId);
            getCommonCommandManager().checkVelocityCondition(vWrapper);

            ProductVO productVo = getCommonCommandManager().loadProductVO(this.baseWrapper);
            if(productVo == null) {
                throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            workFlowWrapper.setCustomerModel(customerModel);
            workFlowWrapper.setSegmentModel(new SegmentModel());
            workFlowWrapper.getSegmentModel().setSegmentId(customerModel.getSegmentId());

            workFlowWrapper.setProductVO(productVo);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setAppUserModel(appUserModel);
            workFlowWrapper.setCustomerAppUserModel(customerAppUserModel);

            //Agent smart money account
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel agentSMAModel =  new SmartMoneyAccountModel();
            agentSMAModel.setRetailerContactId(appUserModel.getRetailerContactId());
            agentSMAModel.setActive(Boolean.TRUE);
            agentSMAModel.setDeleted(Boolean.FALSE);
            agentSMAModel.setDefAccount(Boolean.TRUE);
            searchBaseWrapper.setBasePersistableModel(agentSMAModel);

            searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

            @SuppressWarnings("rawtypes")
            CustomList smaList = searchBaseWrapper.getCustomList();
            if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
                agentSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
            }

            workFlowWrapper.setSmartMoneyAccountModel(agentSMAModel);

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

            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_CW_TX);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);

            DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
            deviceTypeModel.setDeviceTypeId(Long.parseLong(deviceTypeId));

            workFlowWrapper.setDeviceTypeModel(deviceTypeModel);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setLeg2Transaction(Boolean.TRUE); // To avoid saving of TransactionDetailMaster incase of Exception

            commonCommandManager.executeSaleCreditTransaction(workFlowWrapper);

            commissionAmountsHolder = workFlowWrapper.getCommissionAmountsHolder();

            workFlowWrapper.putObject("productTile",productModel.getName());
            commonCommandManager.sendSMS(workFlowWrapper);
            
        }catch(Exception e){
            logger.error("Command Exeution Error:", e);
            handleException(e);
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[CustomerCashWithdrawalLeg2Command.prepare] ";
        this.baseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();

        encryptedOTP = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ONE_TIME_PIN);
        deviceTypeId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);
        customerMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        withdrawalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        encryptionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        commissionAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CAMT);
        processingAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TPAM);
        totalAmount = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
        transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANSACTION_ID);

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
            RetailerContactModel retContactModel = new RetailerContactModel();
            retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(retContactModel);
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

            retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

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
            logger.error(classNMethodName + "Customer App user/retailer contact model not found: ", ex);
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        String classNMethodName = "[CustomerCashWithdrawalLeg2Command.validate] ";

        validationErrors = ValidatorWrapper.doRequired(encryptedOTP, validationErrors, "Transaction Code");
        validationErrors = ValidatorWrapper.doRequired(transactionId, validationErrors, "Transaction ID");

        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product Id");
        validationErrors = ValidatorWrapper.doRequired(encryptionType, validationErrors, "Encryption Type");

        validationErrors = ValidatorWrapper.doRequired(agentMobileNo, validationErrors, "Agent Mobile No");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");

        validationErrors = ValidatorWrapper.doRequired(withdrawalAmount, validationErrors, "Withdrawal Amount");
        validationErrors = ValidatorWrapper.doRequired(commissionAmount, validationErrors, "Commission Amount");
        validationErrors = ValidatorWrapper.doRequired(processingAmount, validationErrors, "Processing Amount");
        validationErrors = ValidatorWrapper.doRequired(totalAmount, validationErrors, "Total Amount");

        if(!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(withdrawalAmount,validationErrors,"Withdrawal Amount");
            validationErrors = ValidatorWrapper.doNumeric(commissionAmount, validationErrors, "Commission Amount");
            validationErrors = ValidatorWrapper.doNumeric(processingAmount, validationErrors, "Processing Amount");
            validationErrors = ValidatorWrapper.doNumeric(totalAmount, validationErrors, "Total Amount");
        }

        if(null== productModel) {
            logger.error(classNMethodName + "Product model not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
        }

        if(null == retailerContactModel) {
            logger.error(classNMethodName + "Retailer Contact Model is null. ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Retailer Contact not found.");
        }

        if(null == customerAppUserModel) {
            logger.error(classNMethodName + "Customer App User not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }

        if(null == customerModel) {
            logger.error(classNMethodName + "Customer model not found: ");
            validationErrors = ValidatorWrapper.addError(validationErrors, "Customer not found.");
        }

        try {
            ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(appUserModel);

            if(userValidation.hasValidationErrors()) {
                logger.error(classNMethodName + " User validation failed.");

                validationErrors = ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
            }
        } catch (FrameworkCheckedException e) {
            logger.error(classNMethodName + e.getMessage());

            validationErrors = ValidatorWrapper.addError(validationErrors, e.getMessage());
        }

        return validationErrors;
    }

    @Override
    public String response() {
        return MiniXMLUtil.createResponseXMLByParams(this.toXML());
    }

    private List<LabelValueBean> toXML(){

        List<LabelValueBean> params = new ArrayList<LabelValueBean>(20);

        params.add(new LabelValueBean(CommandFieldConstants.KEY_TRANSACTION_ID, replaceNullWithEmpty(transactionId)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_ID, replaceNullWithEmpty(transactionId)));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(customerAppUserModel.getMobileNo())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_NAME, replaceNullWithEmpty(customerAppUserModel.getFullName())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_CNIC, replaceNullWithEmpty(customerAppUserModel.getNic())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));

        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATEF, Formatter.formatDate(workFlowWrapper.getTransactionModel().getCreatedOn())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_DATE, workFlowWrapper.getTransactionModel().getCreatedOn().toString()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_TIMEF, Formatter.formatTime(workFlowWrapper.getTransactionModel().getCreatedOn())));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_BAL, "" + workFlowWrapper.getOLASwitchWrapper().getOlavo().getAgentBalanceAfterTransaction()));
        params.add(new LabelValueBean(CommandFieldConstants.KEY_FORMATED_BAL, "" + workFlowWrapper.getOLASwitchWrapper().getOlavo().getAgentBalanceAfterTransaction()));

        return params;
    }
}
