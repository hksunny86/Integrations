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
import com.inov8.microbank.server.service.integration.vo.ProductVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class HRAToWalletInfoCommand extends BaseCommand {

    private String amount;
    private String productId;
    private String customerMobileNo;
    private AppUserModel customerAppUserModel;
    private CommonCommandManager commonCommandManager;
    private ProductModel productModel;
    private CommissionAmountsHolder commissionAmountsHolder;
    private CommissionWrapper commissionWrapper;
    private CustomerModel customerModel;
    protected final Log logger = LogFactory.getLog(HRAToWalletInfoCommand.class);

    public void prepare(BaseWrapper baseWrapper) {
        customerAppUserModel = ThreadLocalAppUser.getAppUserModel();
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        amount  = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_TX_AMOUNT);
        customerMobileNo = customerAppUserModel.getMobileNo();
        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel)bWrapper.getBasePersistableModel();

            if(customerAppUserModel != null) {
                customerModel = new CustomerModel();
                customerModel.setCustomerId(customerAppUserModel.getCustomerId());
                bWrapper = new BaseWrapperImpl();
                bWrapper.setBasePersistableModel(customerModel);
                bWrapper = getCommonCommandManager().loadCustomer(bWrapper);
                customerModel = (CustomerModel)bWrapper.getBasePersistableModel();
            }
        }
        catch(Exception ex){

        }
    }

    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Customer Mobile No");
        validationErrors = ValidatorWrapper.doRequired(amount,validationErrors,"Amount");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product Id");
        if(!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(amount,validationErrors,"Amount");
        }

        return validationErrors;
    }

    public void execute() throws CommandException {

        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        BaseWrapper baseWrapperTemp = new BaseWrapperImpl();
        String classNMethodName = "[CashWithdrwalInfoCommand.execute] ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Mobile No:" + customerMobileNo ;
        String exceptionMessage = "Exception occurred ";
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;

        try {


            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
            customerSMAModel.setCustomerId(customerAppUserModel.getCustomerId());
            customerSMAModel.setActive(Boolean.TRUE);
            customerSMAModel.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);

            searchBaseWrapper.setBasePersistableModel(customerSMAModel);
            searchBaseWrapper = getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);

            CustomList smaList = searchBaseWrapper.getCustomList();
            if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0)
                customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
            if(customerSMAModel.getSmartMoneyAccountId() == null)
                throw new CommandException("HRA Code does not exist",ErrorCodes.NO_HRA_EXISTS, ErrorLevel.MEDIUM,new Throwable());
            baseWrapperTemp.setBasePersistableModel(customerSMAModel);
            validationErrors = getCommonCommandManager().checkSmartMoneyAccount(baseWrapperTemp);
            if(validationErrors.hasValidationErrors()) {
                //logger.error(genericExceptionMessage);
                throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);
            TransactionModel transactionModel = new TransactionModel();
            // add to baseWrapperTemp so populate vo
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, deviceTypeId);
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerAppUserModel.getMobileNo());
            baseWrapperTemp.putObject(CommandFieldConstants.KEY_TAMT, amount);

            Long pimInfoId = productModel.getProductIntgModuleInfoId();
            Long piVOId = productModel.getProductIntgVoId();
            if(pimInfoId == null || "".equals(pimInfoId) || piVOId == null || "".equals(piVOId)){
               // logger.error(classNMethodName + " Unable to load Product VO. " + inputParams + exceptionMessage);
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
            transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX);
            workFlowWrapper.setProductModel(productModel);
            workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
            workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
            workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
            transactionModel.setTransactionAmount(Double.valueOf(amount));
            workFlowWrapper.setTransactionModel(transactionModel);
            workFlowWrapper.setSegmentModel(new SegmentModel());
            workFlowWrapper.getSegmentModel().setSegmentId(customerModel.getSegmentId());

            commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
            commissionAmountsHolder = (CommissionAmountsHolder)commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

            getCommonCommandManager().validateBalance(customerAppUserModel, customerSMAModel , commissionAmountsHolder.getTotalAmount(), true);

            getCommonCommandManager().checkCustomerBalanceForHra(customerMobileNo, commissionAmountsHolder.getTotalAmount());

        }
        catch (FrameworkCheckedException e) {
            logger.error(genericExceptionMessage + e.getMessage());
            throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
        }
        catch(WorkFlowException we) {
            logger.error(genericExceptionMessage + we.getMessage());
            throw new CommandException(we.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,we);
        }
        catch (Exception e) {
            logger.error(genericExceptionMessage + e.getMessage());
            throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
        }

    }

    public String response() {
        StringBuilder responseXML = new StringBuilder();
        responseXML.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);

        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));

        if(customerAppUserModel != null){
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_NAME, customerAppUserModel.getFirstName() +" "+ customerAppUserModel.getLastName()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, customerAppUserModel.getNic()));
        }

        if(commissionAmountsHolder != null) {
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAM, replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TXAMF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTransactionAmount()))));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, replaceNullWithZero(commissionAmountsHolder.getTotalAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTotalAmount()))));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMT, replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CAMTF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount()))));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAM, replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()).toString()));
            responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TPAMF, Formatter.formatNumbers(replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount()))));
        }
        responseXML.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return responseXML.toString();
    }
}
