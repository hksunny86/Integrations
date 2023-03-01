package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.integration.dispenser.CashInProductDispenser;
import com.inov8.microbank.server.service.integration.vo.CashInVO;
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.integration.vo.OLAVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;
import static org.apache.commons.logging.LogFactory.*;

public class CreditInquiryApiCommand extends BaseCommand {
    private final Log logger = getLog(CreditInquiryApiCommand.class);
    protected AppUserModel appUserModel;
    protected String productId;
    protected BaseWrapper preparedBaseWrapper;

    protected UserDeviceAccountsModel userDeviceAccountsModel;
    ProductModel productModel;
    BaseWrapper baseWrapper;
    String successMessage;
    CommissionAmountsHolder commissionAmountsHolder;
    CashInVO p;

    protected String customerMobileNumber;
    protected String customerCNIC;
    protected String customerName;
    String txAmount;
    CustomerModel customerModel;


    @Override
    public void prepare(BaseWrapper baseWrapper) {
        preparedBaseWrapper = baseWrapper;
        appUserModel = ThreadLocalAppUser.getAppUserModel();

        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        customerMobileNumber = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        txAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        try {

            BaseWrapper bWrapper = new BaseWrapperImpl();
            CustomerModel cModel = new CustomerModel();
            cModel.setCustomerId(appUserModel.getCustomerId());
            bWrapper.setBasePersistableModel(cModel);
            customerModel = getCommonCommandManager().loadCustomer(cModel);

        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);

        }
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId, validationErrors, "Product");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNumber, validationErrors, "Mobile No");
        validationErrors = ValidatorWrapper.doRequired(txAmount, validationErrors, "Amount");

        if (!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(productId, validationErrors, "Product");
            validationErrors = ValidatorWrapper.doInteger(deviceTypeId, validationErrors, "Device Type");
            validationErrors = ValidatorWrapper.doNumeric(txAmount, validationErrors, "Amount");
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        if (logger.isDebugEnabled()) {
            logger.debug("Start of CreditInquiryApiCommand.execute()");
        }
        logger.info("Start of CreditInquiryApiCommand.execute()");

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        workFlowWrapper.setHandlerModel(handlerModel);
        workFlowWrapper.setHandlerAppUserModel(handlerAppUserModel);

        CommissionWrapper commissionWrapper;
        baseWrapper = new BaseWrapperImpl();

        try {
            productModel = new ProductModel();
            TransactionModel transactionModel = null;
            productModel.setProductId(Long.parseLong(productId));
            baseWrapper.setBasePersistableModel(productModel);
            baseWrapper = commonCommandManager.loadProduct(baseWrapper);
            productModel = (ProductModel) baseWrapper.getBasePersistableModel();
            if (productModel == null) {
                throw new CommandException("Product not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
//            if (productModel.getProductIntgModuleInfoId() != null && !"".equals(productModel.getProductIntgModuleInfoId()) && productModel.getProductIntgVoId() != null && !"".equals(productModel.getProductIntgVoId())) {
//                ProductVO productVO = commonCommandManager.loadProductVO(preparedBaseWrapper);
//                if (productVO == null) {
//                    throw new CommandException("ProductVo is not loaded", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
//                }
//
//                logger.info("productVo populating");
//                productVO.populateVO(productVO, preparedBaseWrapper);

                workFlowWrapper.setProductModel(productModel);

//To calculate the commission

                transactionModel = new TransactionModel();
                transactionModel.setTransactionAmount(Double.valueOf(txAmount));

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.BILL_SALE_TX);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setCustomerModel(appUserModel.getCustomerIdCustomerModel());
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setTransactionModel(transactionModel);
//                workFlowWrapper.setSmartMoneyAccountModel(smartMoneyAccountModel);

                //pulling the customer segment for commission module changes
                long segmentId = this.getCommonCommandManager().getCustomerSegmentIdByMobileNo(customerMobileNumber);
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(segmentId);
                workFlowWrapper.setSegmentModel(segmentModel);
                workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
                workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));

                commonCommandManager.checkProductLimit(segmentId, productModel.getProductId(), appUserModel.getMobileNo(), Long.valueOf(deviceTypeId), transactionModel.getTransactionAmount(), productModel, null, workFlowWrapper.getHandlerModel());

                workFlowWrapper.setTaxRegimeModel((TaxRegimeModel) customerModel.getTaxRegimeIdTaxRegimeModel().clone());
                commissionWrapper = commonCommandManager.calculateCommission(workFlowWrapper);
                commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                userDeviceAccountsModel = (UserDeviceAccountsModel) ThreadLocalUserDeviceAccounts.getUserDeviceAccountsModel();

//                p = cashInVO;
//            }

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

        if (logger.isDebugEnabled()) {
            logger.debug("End of CreditInquiryApiCommand.execute()");
        }
    }

    @Override
    public String response() {
        return toXML();
    }

    private String toXML() {
        StringBuilder strBuilder = new StringBuilder();
        if (commissionAmountsHolder != null) {
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, replaceNullWithEmpty(customerMobileNumber)));
//            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_NAME, replaceNullWithEmpty(customerName)));
//            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, replaceNullWithEmpty(customerCNIC)));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, "" + Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "" + replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, "" + replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        }

        return strBuilder.toString();
    }

}
