package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class AdvanceSalaryLoanInfoCommand extends BaseCommand {
    private String customerCNIC, customerMobileNo;
    private String productId;
    BaseWrapper baseWrapper;
    private ProductModel productModel;
    private AppUserModel appUserModel;
    private String withdrawalAmount;
    private CommissionAmountsHolder commissionAmountsHolder;
    private DeviceTypeModel deviceTypeModel;
    private String ThirdPartyRRN = null;

    private final Log logger = LogFactory.getLog(AdvanceSalaryLoanInfoCommand.class);
    private String logClassName = "AdvanceSalaryLoanInfoCommand";

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[AdvanceSalaryLoanInfoCommand.prepare] ";
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        customerCNIC = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        productId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        withdrawalAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TX_AMOUNT);
        deviceTypeModel = new DeviceTypeModel(com.inov8.ola.util.StringUtil.isNullOrEmpty(deviceTypeId) ? DeviceTypeConstantsInterface.USSD : Long.valueOf(deviceTypeId));
        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel)bWrapper.getBasePersistableModel();
            this.baseWrapper=baseWrapper;
        }

        catch(Exception ex){
            logger.error(classNMethodName +" Product model not found: " + ex.getStackTrace());
        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(productId,validationErrors,"Product");
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId,validationErrors,"Device Type");
        validationErrors = ValidatorWrapper.doRequired(customerCNIC,validationErrors,"Customer CNIC");
        validationErrors = ValidatorWrapper.doRequired(customerMobileNo,validationErrors,"Customer Mobile");
        validationErrors = ValidatorWrapper.doRequired(withdrawalAmount,validationErrors,"Amount");

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

        CommissionWrapper commissionWrapper;
        String classNMethodName = "[ThirdPartyBalanceInquiryCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId + " Customer CNIC:" + customerCNIC ;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        baseWrapper = new BaseWrapperImpl();

        appUserModel = ThreadLocalAppUser.getAppUserModel();
        try {
            ValidationErrors validationErrors = commonCommandManager.checkActiveAppUser(appUserModel);
            if (!validationErrors.hasValidationErrors()) {
                //Check User Device Accounts health
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.MOBILE);
                baseWrapper.setBasePersistableModel(appUserModel);
//                validationErrors = getCommonCommandManager().checkCustomerCredentials(baseWrapper);

                if (validationErrors.hasValidationErrors()) {
                    throw new CommandException(validationErrors.getErrors(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }

                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                SmartMoneyAccountModel customerSMAModel =  new SmartMoneyAccountModel();
                customerSMAModel.setCustomerId(appUserModel.getCustomerId());
                customerSMAModel.setActive(Boolean.TRUE);
                customerSMAModel.setDeleted(Boolean.FALSE);
                customerSMAModel.setDefAccount(Boolean.TRUE);
                searchBaseWrapper.setBasePersistableModel(customerSMAModel);

                searchBaseWrapper = getCommonCommandManager().loadSMAExactMatch(searchBaseWrapper);

                CustomList smaList = searchBaseWrapper.getCustomList();
                if(smaList != null && smaList.getResultsetList() != null && smaList.getResultsetList().size() > 0){
                    customerSMAModel = (SmartMoneyAccountModel)smaList.getResultsetList().get(0);
                }

                baseWrapper.setBasePersistableModel(customerSMAModel);
                validationErrors = commonCommandManager.checkSmartMoneyAccount(baseWrapper);

                if(validationErrors.hasValidationErrors()){
                    logger.error("["+logClassName+".execute] Throwing Exception in Product ID: " +  productId + " - Logged in AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId()+ " Mobile No:" + customerMobileNo);
                    throw new CommandException(validationErrors.getErrors(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                }

                TransactionModel transactionModel = new TransactionModel();

                workFlowWrapper.setSmartMoneyAccountModel(customerSMAModel);
                workFlowWrapper.setProductModel(productModel);

                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
//                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX);
                workFlowWrapper.setProductModel(productModel);
                workFlowWrapper.setTransactionTypeModel(transactionTypeModel);
                workFlowWrapper.setDeviceTypeModel(deviceTypeModel);

//                workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(Long.valueOf(deviceTypeId));
                SegmentModel segmentModel = new SegmentModel();
                segmentModel.setSegmentId(CommissionConstantsInterface.DEFAULT_SEGMENT_ID);
                workFlowWrapper.setSegmentModel(segmentModel);

                workFlowWrapper.setTransactionModel(transactionModel);

                getCommonCommandManager().checkProductLimit(null, productModel.getProductId(), appUserModel.getMobileNo(),
                        Long.valueOf(deviceTypeId), Double.parseDouble(withdrawalAmount), productModel, null, workFlowWrapper.getHandlerModel());

                transactionModel.setTransactionAmount(Double.valueOf(withdrawalAmount));
                commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
                commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);

                ESBAdapter adapter = new ESBAdapter();
                I8SBSwitchControllerRequestVO requestVO=new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO=new I8SBSwitchControllerResponseVO();

                requestVO = ESBAdapter.prepareRequestVoForAppInSnap(I8SBConstants.RequestType_LoanManagement);

                requestVO.setTransactionId(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
                requestVO.setConsumerNickName(appUserModel.getFirstName());
                requestVO.setFatherName(appUserModel.getLastName());
                requestVO.setAccountNumber(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",customerMobileNo));
                requestVO.setCNIC(customerCNIC);
                requestVO.setAmount(withdrawalAmount);
                requestVO.setRRN(requestVO.getSTAN() + requestVO.getTransmissionDateAndTime());
                requestVO.setThirdPartyChannelId("FCLoan");

                ThirdPartyRRN = requestVO.getRRN();
                SwitchWrapper sWrapper=new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);

                sWrapper=adapter.makeI8SBCall(sWrapper);

                ESBAdapter.processI8sbResponseCodeForEOBI(sWrapper.getI8SBSwitchControllerResponseVO(),true);

            }
        }
        catch (CommandException e) {
            logger.error(genericExceptionMessage + e.getMessage());
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CommandException(this.getMessageSource().getMessage("command.unexpectedError", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
        }

    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_DEVICE_TYPE_ID, "5"));
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_CNIC, customerCNIC));
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_PROD_ID, productId));
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_THIRD_PARTY_RRN, ThirdPartyRRN));
        if(commissionAmountsHolder != null) {
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_COMM_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_COMM_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalCommissionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_PROCESS_AMNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TX_PROCESS_AMNT, Formatter.formatNumbers(commissionAmountsHolder.getTransactionProcessingAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TOTAL_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATED_TOTAL_AMOUNT, Formatter.formatNumbers(commissionAmountsHolder.getTotalAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TX_AMOUNT, ""+replaceNullWithZero(commissionAmountsHolder.getTransactionAmount())));
            strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_FORMATTED_TX_AMOUNT, ""+Formatter.formatNumbers(commissionAmountsHolder.getTransactionAmount())));
        }
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return strBuilder.toString();

    }
}
