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
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.TreeMap;

public class CustomerCashWithdrawalLeg2InfoCommand extends BaseCommand {

    private String transactionId;
    private String txAmount;
    private String productId;
    private AppUserModel appUserModel;
    private ProductModel productModel;
    private String agentMobileNo;
    private RetailerContactModel retailerContactModel;

    private String customerMobileNo;
    private AppUserModel customerAppUserModel;
    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
    private CommissionAmountsHolder commissionAmountsHolder;

    protected final Log logger = LogFactory.getLog(CashWithdrawalInfoCommand.class);


    @Override
    public void execute() throws CommandException {
        ValidationErrors errors;
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);
        String classNMethodName = "[CustomerCashWithdrawalLeg2InfoCommand.execute] ";
        BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
        CommissionWrapper commissionWrapper;

        try {
            if(appUserModel.getRetailerContactId() == null && appUserModel.getHandlerId() == null) {
                throw new CommandException(MessageUtil.getMessage("command.invalidAppUserType"), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH);
            }

            loadMiniTransactionModel();

            //-----------------------  Validating Customer...  -------------------------------
            //baseWrapper.putObject(CommandFieldConstants.KEY_LABEL, "Customer (" + customerMobileNo + ")");
            customerAppUserModel = commonCommandManager.loadAppUserByQuery(customerMobileNo, UserTypeConstantsInterface.CUSTOMER);
            if(customerAppUserModel == null){
                throw new CommandException(MessageUtil.getMessage("22012"), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH);
            }

            else if(customerAppUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
                CustomerModel customerModel = new CustomerModel();
                try {
                    customerModel = commonCommandManager.getCustomerModelById(customerAppUserModel.getCustomerId());
                } catch (CommandException e) {
                    e.printStackTrace();
                }

                if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                    logger.error("[CashDepositInfoCommand.execute] Upgrade Account L0 to L1 to perform Transaction.: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerMobileNo);
                    throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

                }
            }

            ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(customerAppUserModel);

            if(userValidation.hasValidationErrors()) {
                logger.error(classNMethodName + " Customer validation failed");
                throw new CommandException(validationErrors.getErrors(),Long.valueOf(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT),ErrorLevel.MEDIUM,new Throwable());
            }

            //check if Customer is Blacklisted
            if (this.getCommonCommandManager().isCnicBlacklisted(customerAppUserModel.getNic())) {
                throw new CommandException(MessageUtil.getMessage("blackListed.customer"),Long.valueOf(WorkFlowErrorCodeConstants.INVALID_CUSTOMER_ACCOUNT),ErrorLevel.MEDIUM,new Throwable());
            }

            //Check User Device Accounts health
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
            baseWrapper.setBasePersistableModel(customerAppUserModel);
            userValidation = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

            if(userValidation.hasValidationErrors()) {
                logger.error(classNMethodName + " User Device Accounts failed");
                throw new CommandException(userValidation.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }


            loadCustomerSegment(workFlowWrapper, customerAppUserModel.getCustomerId());

            //Product Limit Check
            getCommonCommandManager().checkProductLimit(workFlowWrapper.getSegmentModel().getSegmentId(), productModel.getProductId(), appUserModel.getMobileNo(),
                    Long.valueOf(deviceTypeId), Double.parseDouble(txAmount), productModel, null, workFlowWrapper.getHandlerModel());


            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
            customerSMAModel.setCustomerId(customerAppUserModel.getCustomerId());
            searchBaseWrapper.setBasePersistableModel(customerSMAModel);

            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

            @SuppressWarnings("rawtypes")
            CustomList smaList = searchBaseWrapper.getCustomList();
            if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
                customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
            }

            baseWrapperTemp.setBasePersistableModel(customerSMAModel);
            validationErrors = getCommonCommandManager().checkSmartMoneyAccount(baseWrapperTemp);

            if(validationErrors.hasValidationErrors()) {
                throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            if(!customerSMAModel.getCustomerId().toString().equals(customerAppUserModel.getCustomerId().toString())) {
                logger.error(classNMethodName + " Invalid Smart Money account for customerId:"+customerAppUserModel.getCustomerId());
                throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);

            TransactionModel transactionModel = new TransactionModel();

            // add to baseWrapperTemp so populate vo
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo);
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, agentMobileNo);
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_TAMT, txAmount);

            Long pimInfoId = productModel.getProductIntgModuleInfoId();
            Long piVOId = productModel.getProductIntgVoId();

            if(pimInfoId == null || "".equals(pimInfoId) || piVOId == null || "".equals(piVOId)){
                logger.error(classNMethodName + " Unable to load Product VO. ");
                throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            ProductVO productVO = getCommonCommandManager().loadProductVO(baseWrapperTemp);
            if(productVO == null) {
                throw new CommandException("ProductVo is not loaded",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            productVO.populateVO(productVO, baseWrapperTemp);

            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setProductVO(productVO);

            TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CUSTOMER_INITIATED_CW_TX);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setRetailerContactModel(retailerContactModel);
            workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
            workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));

            transactionModel.setTransactionAmount(Double.valueOf(txAmount));
            workFlowWrapper.setTransactionModel(transactionModel);

            workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());

            commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
            commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
            getCommonCommandManager().checkCustomerBalance(customerMobileNo, commissionAmountsHolder.getTotalAmount());

        }catch(Exception ex){
            handleException(ex);
        }
    }

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[CustomerCashWithdrawalLeg2InfoCommand.prepare] ";

        appUserModel = ThreadLocalAppUser.getAppUserModel();

        transactionId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANSACTION_ID);
        txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TXAM);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        agentMobileNo = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOBILE);

        try{
            RetailerContactModel retContactModel = new RetailerContactModel();
            retContactModel.setRetailerContactId(appUserModel.getRetailerContactId());

            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(retContactModel);
            bWrapper = getCommonCommandManager().loadRetailerContact(bWrapper);
            retailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();
        }catch(Exception ex){
            logger.error(classNMethodName + "Retailer contact model not found: " + ex.getStackTrace());
        }

        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel)bWrapper.getBasePersistableModel();
        }catch(Exception ex){
            logger.error(classNMethodName +" Product model not found: " + ex.getStackTrace());
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        String classNMethodName = "[CustomerCashWithdrawalLeg2InfoCommand.validate] ";

        validationErrors = ValidatorWrapper.doRequired(agentMobileNo,validationErrors,"Agent Mobile No");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product Id");
        validationErrors = ValidatorWrapper.doRequired(transactionId, validationErrors, "Transaction Id");
        validationErrors = ValidatorWrapper.doRequired(txAmount, validationErrors, "Transaction Amount");

        if(!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(txAmount,validationErrors,"Withdrawal Amount");
        }

        if(!validationErrors.hasValidationErrors()) {
            try {
                ValidationErrors userValidation = getCommonCommandManager().checkActiveAppUser(appUserModel);

                if (userValidation.hasValidationErrors()) {
                    logger.error(classNMethodName + " Agent validation failed.");
                    validationErrors = ValidatorWrapper.addError(validationErrors, userValidation.getErrors());
                }
            } catch (FrameworkCheckedException e) {
                logger.error(classNMethodName + e.getMessage());
                validationErrors = ValidatorWrapper.addError(validationErrors, e.getMessage());
            }
        }

        if(!validationErrors.hasValidationErrors()) {
            if (null == retailerContactModel) {
                logger.error("[CashWithdrawalInfoCommand.execute] Retailer Contact Model is null. Logged In Agent-AppUserId:" + appUserModel.getAppUserId());
                validationErrors = ValidatorWrapper.addError(validationErrors, "Retailer Contact not found.");
            }
        }

        if(!validationErrors.hasValidationErrors()) {
            if (null == productModel) {
                logger.error(classNMethodName + "Product model not found: against productId:" + productId);
                validationErrors = ValidatorWrapper.addError(validationErrors, "Product not found.");
            }
        }

        return validationErrors;
    }

    @Override
    public String response() {
        return MiniXMLUtil.createInfoResponseXMLByParams(toXML());
    }

    private Map<String, Object> toXML(){
        Map<String, Object> paramMap = new TreeMap();
        if(commissionAmountsHolder != null){
            paramMap.put(CommandFieldConstants.KEY_TRANSACTION_ID, transactionId);
            paramMap.put(CommandFieldConstants.KEY_TX_ID, transactionId);
            paramMap.put(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo);
            paramMap.put(CommandFieldConstants.KEY_NAME, replaceNullWithEmpty(customerAppUserModel.getFullName()));
            paramMap.put(CommandFieldConstants.KEY_CNIC, replaceNullWithEmpty(customerAppUserModel.getNic()));
            paramMap.put(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()));
            paramMap.put(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+ Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount()));
            paramMap.put(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()));
            paramMap.put(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount()));
            paramMap.put(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()));
            paramMap.put(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount()));
            paramMap.put(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount()));
            paramMap.put(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount()));
        }

        return paramMap;
    }


    protected void loadMiniTransactionModel() throws FrameworkCheckedException {

        MiniTransactionModel miniModel = commonCommandManager.loadMiniTransactionByTransactionCode(transactionId);
        if(miniModel == null){
            throw new CommandException(MessageUtil.getMessage("MINI.INVALID"), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH);
        }else{
            Long stateId = miniModel.getMiniTransactionStateId();
            if(stateId.longValue() == MiniTransactionStateConstant.PIN_SENT) {
                if(commonCommandManager.getMiniTransactionManager().updateIfExpired(miniModel, productModel.getProductId())){
                    stateId = MiniTransactionStateConstant.OT_PIN_EXPIRED;
                }
            }

            if(stateId.longValue() == MiniTransactionStateConstant.OT_PIN_EXPIRED) {
                throw new CommandException(MessageUtil.getMessage("MINI.ExpiredPIN"), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH);
            }else if(stateId.longValue() != MiniTransactionStateConstant.PIN_SENT) {
                throw new CommandException(MessageUtil.getMessage("MINI.TransactionClosed"), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.HIGH);
            }else{
                customerMobileNo = miniModel.getMobileNo();
            }
        }
        workFlowWrapper.setMiniTransactionModel(miniModel);
    }

    private void loadCustomerSegment(WorkFlowWrapper workFlowWrapper, Long customerId) throws Exception{
        CustomerModel custModel = new CustomerModel();
        custModel.setCustomerId(customerId);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        bWrapper.setBasePersistableModel(custModel);
        bWrapper = this.getCommonCommandManager().loadCustomer(bWrapper);
        if(null != bWrapper.getBasePersistableModel()) {
            custModel = (CustomerModel) bWrapper.getBasePersistableModel();
            workFlowWrapper.setSegmentModel(custModel.getSegmentIdSegmentModel());
        }
    }

}
