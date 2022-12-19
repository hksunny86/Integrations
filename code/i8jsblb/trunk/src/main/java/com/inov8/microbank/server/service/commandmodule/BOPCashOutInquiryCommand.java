package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class BOPCashOutInquiryCommand extends BaseCommand {

    private final Log logger = LogFactory.getLog(BOPCashOutInquiryCommand.class);

    private String customerMobileNo;
    private String cNic;
    private String customerCardNo;
    private String isCard;
    private String customerSegmentId;
    private String transactionAmount;
    private String productId;

    private AppUserModel appUserModel;
    private UserDeviceAccountsModel userDeviceAccountsModel;
    private ProductModel productModel;
    private BaseWrapper baseWrapper;
    private CommissionAmountsHolder commissionAmountsHolder;
    private I8SBSwitchControllerResponseVO responseVO;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        String classNMethodName = "[BOPCashOutInquiryCommand.prepare] ";
        logger.info("Start of " + classNMethodName);
        responseVO = new I8SBSwitchControllerResponseVO();
        customerMobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        cNic = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CNIC);
        customerCardNo = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_DEBIT_CARD_NO);
        isCard = this.getCommandParameter(baseWrapper,"IS_CARD");
        customerSegmentId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE);
        transactionAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AMOUNT);
        productId = this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_PROD_ID);
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        try{
            BaseWrapper bWrapper = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            bWrapper.setBasePersistableModel(productModel);
            bWrapper = getCommonCommandManager().loadProduct(bWrapper);
            productModel = (ProductModel) bWrapper.getBasePersistableModel();
        }
        catch(Exception ex){
            ex.printStackTrace();
            logger.error(classNMethodName +" Product model not found: ");
        }
        if(productId.equals(ProductConstantsInterface.BOP_CASH_OUT_COVID_19.toString()))
            customerSegmentId = productModel.getProductCode();
        logger.info("End of " + classNMethodName);
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
        CommissionWrapper commissionWrapper;

        String classNMethodName = "[ThirdPartyBalanceInquiryCommand.execute] ";
        String exceptionMessage = "Exception occurred ";
        String inputParams = "AppUserID: " + ThreadLocalAppUser.getAppUserModel().getAppUserId() + " Product ID: " +  productId
                + " Customer Mobile #: " + customerMobileNo
                + " Customer NIC #: " + cNic
                + " Customer Card #: " + customerCardNo ;
        String genericExceptionMessage = classNMethodName + exceptionMessage + inputParams;
        logger.info(classNMethodName + "\n" + inputParams);
        ValidationErrors validationErrors;
        if(appUserModel.getRetailerContactId() != null) {
            try {
                validationErrors = getCommonCommandManager().checkActiveAppUser(appUserModel);
                if (validationErrors.hasValidationErrors()) {
                    throw new Exception("Agent Is not in correct state.");
                }
                userDeviceAccountsModel = commonCommandManager.getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(appUserModel.getAppUserId());
                TransactionModel transactionModel = new TransactionModel();
                Long pimInfoId = productModel.getProductIntgModuleInfoId();
                Long piVOId = productModel.getProductIntgVoId();
                if (pimInfoId == null || "".equals(pimInfoId) || piVOId == null || "".equals(piVOId)) {
                    logger.error(classNMethodName + " Unable to load Product VO. " + inputParams + exceptionMessage);
                    throw new CommandException(this.getMessageSource().getMessage("billPaymentCommand.productVoLoadedError", null, null), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                }
                workFlowWrapper.setProductModel(productModel);
                logger.info(classNMethodName + inputParams);
                TransactionTypeModel transactionTypeModel = new TransactionTypeModel();
                transactionTypeModel.setTransactionTypeId(TransactionTypeConstantsInterface.CASH_WITHDRAWAL_TX);
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
                baseWrapper=new BaseWrapperImpl();
                RetailerContactModel retailerContactModel = new RetailerContactModel();
                retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
                baseWrapper.setBasePersistableModel(retailerContactModel);
                baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
                retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
                workFlowWrapper.setRetailerContactModel(retailerContactModel);
                workFlowWrapper.setTaxRegimeModel(retailerContactModel.getTaxRegimeIdTaxRegimeModel());
                //Third Party Call
                ESBAdapter adapter =new ESBAdapter();
                I8SBSwitchControllerRequestVO requestVO = ESBAdapter.prepareRequestVoForBOP(I8SBConstants.RequestType_BOP_CashOutInquiry);
                requestVO.setCNIC(cNic);
                requestVO.setMobileNumber(customerMobileNo);
                requestVO.setAccountNumber(EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0",customerCardNo));
                requestVO.setSegmentId(customerSegmentId);
                requestVO.setTransactionAmount(transactionAmount);
                SwitchWrapper sWrapper=new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = adapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(),false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                //
            }
            catch (Exception ex){
                ex.printStackTrace();
                logger.error(genericExceptionMessage);
                throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
            }
        }
    }

    @Override
    public String response() {
        StringBuilder responseXML = new StringBuilder();
        responseXML.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CNIC, cNic));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_IS_BVS_REQUIRED, responseVO.getStatusFlag()));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TRANSACTION_ID, responseVO.getTransactionId()));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_THIRD_PARTY_CUST_SEGMENT_CODE, customerSegmentId));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_MOBILE, customerMobileNo));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_DEBIT_CARD_NO, customerCardNo));
        responseXML.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_CUSTOMER_NAME, responseVO.getAccountTitle()));
        responseXML.append(MiniXMLUtil.createXMLParameterTag("IS_CARD", isCard));
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
