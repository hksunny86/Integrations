package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class AgentCashDepositInfoCommand extends BaseCommand {

    protected String productId;
    protected BaseWrapper preparedBaseWrapper;
    protected UserDeviceAccountsModel userDeviceAccountsModel;

    ProductModel productModel;
    BaseWrapper baseWrapper;
    String successMessage;
    CommissionAmountsHolder commissionAmountsHolder;

    protected String agentMobileNumber;
    protected String agentCNIC;
    String txAmount;
    RetailerContactModel fromRetailerContactModel;
    AppUserModel agentAppUserModel;
    protected String customerMobileNumber;

    protected final Log logger = LogFactory.getLog(AgentCashDepositInfoCommand.class);
    DateTimeFormatter dtf =  DateTimeFormat.forPattern("dd/MM/yyyy");

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        preparedBaseWrapper = baseWrapper;
        agentMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_MOB_NO);
        Long[] appUserTypeIds = {UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};

        try {
            agentAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(agentMobileNumber, appUserTypeIds);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }

        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        agentCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);

        try{
            RetailerContactModel retContactModel = new RetailerContactModel();
            retContactModel.setRetailerContactId(agentAppUserModel.getRetailerContactId());

            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(retContactModel);
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            bWrapper = commonCommandManager.loadRetailerContact(bWrapper);

            this.fromRetailerContactModel = (RetailerContactModel) bWrapper.getBasePersistableModel();

        }catch(Exception ex){
            logger.error("[AgentCashDepositInfoCommand.prepare] Unable to load RetailerContact info... " + ex.getStackTrace());
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(agentMobileNumber,validationErrors,"Mobile No");
        validationErrors = ValidatorWrapper.doRequired(txAmount,validationErrors,"Amount");

        if(!validationErrors.hasValidationErrors()){
            validationErrors = ValidatorWrapper.doNumeric(productId,validationErrors,"Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Amount");
        }
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        if(logger.isDebugEnabled())
        {
            logger.debug("Start of AgentCashDepositInfoCommand.execute()");
        }

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        CommissionWrapper commissionWrapper;
        baseWrapper = new BaseWrapperImpl();

        if(agentAppUserModel.getRetailerContactId() != null) {
            try {
                Long[] appUserTypeIds = {UserTypeConstantsInterface.RETAILER, UserTypeConstantsInterface.HANDLER};
                AppUserModel agentAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(agentMobileNumber, appUserTypeIds);
                AppUserModel customerAppUserModel = getCommonCommandManager().loadAppUserByMobileAndType(customerMobileNumber, appUserTypeIds);
                //Validate Agent
                ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(agentAppUserModel);

                if (!validationErrors.hasValidationErrors()) {
                    SmartMoneyAccountModel smartMoneyAccountModel = new SmartMoneyAccountModel();

                    smartMoneyAccountModel.setRetailerContactId(agentAppUserModel.getRetailerContactId());
                    smartMoneyAccountModel.setDeleted(false);
                    baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

                    baseWrapper = commonCommandManager.loadOLASmartMoneyAccount(baseWrapper);
                    smartMoneyAccountModel = (SmartMoneyAccountModel) baseWrapper.getBasePersistableModel();

                    baseWrapper.putObject(CommandFieldConstants.KEY_HANDLER_MODEL, handlerModel);
                    validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

                    if (!validationErrors.hasValidationErrors()) {
                        if (smartMoneyAccountModel.getName() != null) {
                            if (smartMoneyAccountModel.getRetailerContactId().toString().equals(agentAppUserModel.getRetailerContactId().toString())) {

                                productModel = new ProductModel();
                                TransactionModel transactionModel = null;
                                productModel.setProductId(Long.parseLong(productId));
                                baseWrapper.setBasePersistableModel(productModel);
                                baseWrapper = commonCommandManager.loadProduct(baseWrapper);
                                productModel = (ProductModel) baseWrapper.getBasePersistableModel();

                                if (productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) {

                                    workFlowWrapper.setProductModel(productModel);
                                    agentCNIC = agentAppUserModel.getNic();

//									To calculate the commission
                                    transactionModel = new TransactionModel();
                                    transactionModel.setTransactionAmount(Double.valueOf(txAmount));

                                    TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                                    transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BILL_SALE_TX);
                                    workFlowWrapper.setProductModel(productModel);
                                    workFlowWrapper.setRetailerContactModel(fromRetailerContactModel);
                                    workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                                    workFlowWrapper.setTransactionModel(transactionModel);
                                    workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);
                                    workFlowWrapper.setFromRetailerContactAppUserModel(agentAppUserModel);
                                    workFlowWrapper.setSegmentModel(new SegmentModel());

                                    workFlowWrapper.setTaxRegimeModel((TaxRegimeModel) fromRetailerContactModel.getTaxRegimeIdTaxRegimeModel().clone());
                                    commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
                                    commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                                    userDeviceAccountsModel = (UserDeviceAccountsModel) ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

                                }
                                CustomerModel customerModel = new CustomerModel();
                                try {
                                    customerModel = commonCommandManager.getCustomerModelById(customerAppUserModel.getCustomerId());
                                } catch (CommandException e) {
                                    e.printStackTrace();
                                }
                                if (customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                                    logger.error("[CashDepositInfoCommand.execute] Upgrade Account L0 to L1 to perform Transaction.: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Mobile No:" + customerMobileNumber);
                                    throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());

                                }
                            } else {
                                throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountId", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                            }
                        } else {
                            throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }
                    } else {
                        throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }
                } else {
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
            } catch (CommandException e) {
                logger.error(e.getMessage());
                throw e;
            } catch (WorkFlowException wex) {
                throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
            } catch (ClassCastException e) {
                throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
            } catch (Exception ex) {
                if (ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1) {
                    throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                } else {
                    throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                }
            }
        }
        else
        {
            throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        }
        if(logger.isDebugEnabled())
        {
            logger.debug("End of AgentCashDepositInfoCommand.execute()");
        }
    }

    @Override
    public String response() { return toXML(); }

    private String toXML(){
        StringBuilder strBuilder = new StringBuilder();
        if(commissionAmountsHolder != null){
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(agentMobileNumber)));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, replaceNullWithEmpty(agentCNIC)));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        }

        return strBuilder.toString();
    }
}
