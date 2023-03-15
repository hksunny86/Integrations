package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class DebitInquiryApiCommand extends BaseCommand {
        private final Log logger = LogFactory.getLog(DebitInquiryApiCommand.class);

    private String customerMobileNo;
    private String productId;
    private AppUserModel appUserModel;
    private ProductModel productModel;
    private CommissionAmountsHolder commissionAmountsHolder;
    private String transactionAmount;
    private WorkFlowWrapper workFlowWrapper;
    private BaseWrapper baseWrapper;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of DebitInfoApiCommand.prepare()");
        }

        //********************************Request Parameters**************************************************
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        ThreadLocalAppUser.setAppUserModel(appUserModel);

        logger.info("[DebitInfoApiCommand.prepare] \nLogged In AppUserModel: " + appUserModel.getAppUserId() +
                "\n Product ID:" + productId +
                "\n deviceTypeId: " + deviceTypeId +
                "\n customerMobileNumber: " + customerMobileNo);

        if (logger.isDebugEnabled()) {
            logger.debug("End of DebitInfoApiCommand.prepare()");
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of DebitInfoApiCommand.validate()");
        }

        validationErrors = ValidatorWrapper.doRequired(customerMobileNo, validationErrors, "Customer Mobile No");

        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(customerMobileNo, validationErrors, "Customer Mobile No");
            validationErrors = ValidatorWrapper.doNumeric(transactionAmount, validationErrors, "Transaction Amount");
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of DebitInfoApiCommand.validate()");
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of DebitInfoApiCommand.execute()");
        }

        ThreadLocalAppUser.setAppUserModel(appUserModel);

        workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        CommissionWrapper commissionWrapper;
        baseWrapper = new BaseWrapperImpl();
        if (appUserModel.getCustomerId() != null) {
            try {
                ValidationErrors validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
                if (!validationErrors.hasValidationErrors()) {

                    SmartMoneyAccountModel smartMoneyAccountModel = getSmartMoneyAccountModel(appUserModel.getCustomerId());

                    if (smartMoneyAccountModel.getName() != null &&

                            smartMoneyAccountModel.getCustomerId().equals(appUserModel.getCustomerId())) {
                        productModel = new ProductModel();
                        baseWrapper.getDataMap().putAll(baseWrapper.getDataMap());
                        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
                        productModel.setProductId(Long.parseLong(productId));
                        baseWrapper.setBasePersistableModel(productModel);
                        baseWrapper = commonCommandManager.loadProduct(baseWrapper);
                        productModel = (ProductModel) baseWrapper.getBasePersistableModel();
                        if (productModel == null) {
                            throw new CommandException("Product not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }

                        workFlowWrapper.setProductModel(productModel);
                        TransactionModel transactionModel=new TransactionModel();
                        TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                        transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.DEBIT_API_TX);
                        workFlowWrapper.setProductModel(productModel);
                        workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                        workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
                        workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
                        SegmentModel segmentModel = new SegmentModel();
                        segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
                        workFlowWrapper.setSegmentModel(segmentModel);
                        workFlowWrapper.setTransactionModel(transactionModel);
                        getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                                Long.valueOf(deviceTypeId), Double.parseDouble(transactionAmount), productModel, null, workFlowWrapper.getHandlerModel());
                        transactionModel.setTransactionAmount(Double.valueOf(transactionAmount));
                        commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
                        commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                        commonCommandManager.validateBalance(appUserModel, smartMoneyAccountModel, commissionAmountsHolder.getTotalAmount(), true);

                    } else {
                        logger.error("[DebitInfoApiCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                        throw new CommandException(this.getMessageSource().getMessage("checkAccountBalanceCommand.invalidAccountDetail", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                    }
                } else {
                    logger.error("[DebitInfoApiCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
            } catch (WorkFlowException wex) {
                wex.printStackTrace();
                logger.error("[DebitInfoApiCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
            } catch (ClassCastException e) {
                logger.error("[DebitInfoApiCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (ex.getMessage() != null && !"".equals(ex.getMessage()) && ex.getMessage().indexOf("JTA") != -1) {
                    logger.error("[DebitInfoApiCommand.execute] Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                } else {
                    logger.error("[DebitInfoApiCommand.execute] Throwing Exception in Product ID: " + productId + " - Customer AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
                    throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
                }
            }
        } else {
            logger.error("[CustomerBookMeInfoCommand.execute] Throwing Exception in Product ID: " + productId + " - Agent AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(this.getMessageSource().getMessage("getSupplierInfoCommand.invalidAppUserType", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("End of CustomerBookMeInfoCommand.execute()");
        }
    }


    private SmartMoneyAccountModel getSmartMoneyAccountModel(Long customerId) throws FrameworkCheckedException {


        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        SmartMoneyAccountModel customerSMAModel = new SmartMoneyAccountModel();
        customerSMAModel.setCustomerId(appUserModel.getCustomerId());
        customerSMAModel.setActive(Boolean.TRUE);
        customerSMAModel.setDeleted(Boolean.FALSE);
        customerSMAModel.setDefAccount(Boolean.TRUE);
        searchBaseWrapper.setBasePersistableModel(customerSMAModel);

        searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

        @SuppressWarnings("rawtypes")
        CustomList smaList = searchBaseWrapper.getCustomList();
        if (smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0) {
            customerSMAModel = (SmartMoneyAccountModel) smaList.getResultsetList().get(0);
        }

        return customerSMAModel;
    }

    @Override
    public String response() {
        String response = "";
       response= toXML();
        return response;
    }

    private String toXML() {

        if (logger.isDebugEnabled()) {
            logger.debug("Start of CustomerBillPaymentsInfoCommand.toXML()");
        }

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PRODUCT_NAME, productModel.getName()));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAM, replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()).toString()));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()).toString()));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()))));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))));
        responseBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
        responseBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return responseBuilder.toString();
    }
}
