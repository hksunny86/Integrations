package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.command.DebitCardIssuenceInfoCommand;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.sun.mail.iap.CommandFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kohsuke.rngom.parse.host.Base;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class FeePaymentInquiryCommand extends BaseCommand {
    protected final Log logger = LogFactory.getLog(FeePaymentInquiryCommand.class);
    private String mobileNumber;
    private String cnic;
    private String productId;
    private boolean isConsumerApp = false;
    private String appId;
    private String fee;
    private CommonCommandManager commonCommandManager;
    private AppUserModel appUserModel;
    private CustomerModel customerModel;
    private ProductModel productModel;
    private SmsSender smsSender;
    private FonePayManager fonePayManager;
    private String transactionType;
    //    private GenericDao genericDao;

    private String cardFeeType;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of FeePayementInquiry.prepare()");
        this.productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        this.mobileNumber = getCommandParameter(baseWrapper, "CMOB");
        this.cnic = getCommandParameter(baseWrapper, "CNIC");
        this.appId = getCommandParameter(baseWrapper, "APPID");
        this.transactionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANSACTION_TYPE);
        this.cardFeeType=getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CARD_FEE_TYPE_ID);

        if (appId.equals("2")) {
            isConsumerApp = Boolean.TRUE;
            mobileNumber = ThreadLocalAppUser.getAppUserModel().getMobileNo();
            cnic = ThreadLocalAppUser.getAppUserModel().getNic();
        }
        try {
            BaseWrapper baseWrapper1 = new BaseWrapperImpl();
            productModel = new ProductModel();
            productModel.setProductId(Long.parseLong(productId));
            baseWrapper1.setBasePersistableModel(productModel);
            baseWrapper1 = getCommonCommandManager().loadProduct(baseWrapper1);
            productModel = (ProductModel) baseWrapper1.getBasePersistableModel();
        } catch (Exception ex) {
            logger.error("FeePayementInquiry.prepare() Product model not found: " + ex.getStackTrace());
        }
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of FeePayementInquiry.prepare()");
    }

    @Override
    public void doValidate() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuenceInfoCommand.doValidate()");
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors = ValidatorWrapper.doRequired(mobileNumber, validationErrors, "MSISDN");
        validationErrors = ValidatorWrapper.doRequired(cnic, validationErrors, "CNIC");
        boolean isValid = CommonUtils.isValidCnic(cnic);
        if (!validationErrors.hasValidationErrors()) {
            if (!isValid) {
                validationErrors.getStringBuilder().append("Invalid CNIC");
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }
        if (validationErrors.hasValidationErrors())
            throw new CommandException(validationErrors.getErrors(), ErrorCodes.VALIDATION_ERROR, ErrorLevel.HIGH, new Throwable());

        if (!validationErrors.hasValidationErrors()) {
            if (getCommonCommandManager().isCnicBlacklisted(cnic)) {
                validationErrors.getStringBuilder().append(MessageUtil.getMessage("walkinAccountBlacklisted"));
                throw new CommandException(validationErrors.getErrors(), ErrorCodes.TERMINATE_EXECUTION_FLOW, ErrorLevel.MEDIUM, new Throwable());
            }
        }

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of DebitCardIssuenceInfoCommand.doValidate()");
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return new ValidationErrors();
    }
    @Override
    public void execute() throws CommandException {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuanceInfoCommand.execute()");
        commonCommandManager = this.getCommonCommandManager();
        smsSender = this.getCommonCommandManager().getSmsSender();
        fonePayManager = this.getCommonCommandManager().getFonePayManager();
        if (isConsumerApp) {
            AppUserModel appModel = ThreadLocalAppUser.getAppUserModel();
            if (!appModel.getNic().equals(this.cnic) || !appModel.getMobileNo().equals(this.mobileNumber))
                throw new CommandException("You are not allowed to Issue Debit Card.",
                        ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
        }
        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
        try {
            Double cardFee = 0.0d;

                workFlowWrapper = getCommonCommandManager().calculateCardFee(mobileNumber, cnic, appUserModel, customerModel, productModel,
                        Long.valueOf(productId),
                        Long.valueOf(cardFeeType), Long.parseLong(deviceTypeId), null);


            if(workFlowWrapper.getCommissionAmountsHolder() != null) {
                fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
                if (workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) {
                    fee = String.valueOf(Double.valueOf(fee) + workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
                }
                else{
                    fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
                }
            }
            else{
                fee = "0.0";
            }
        } catch (CommandException ce) {

            throw new CommandException(ce.getMessage(), ce.getErrorCode(), ErrorLevel.MEDIUM, new Throwable());
        }
        catch (Exception e) {
            logger.error("Error while calculating Debit Card Issuance Charges :: " + e.getMessage(), e);
            throw new CommandException("An Error has Occurred", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        if (!isConsumerApp && !deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            String otp = CommonUtils.generateOneTimePin(5);
            String encryptedPin = EncoderUtils.encodeToSha(otp);
            try {
                fonePayManager.createMiniTransactionModel(encryptedPin, this.mobileNumber, "", CommandFieldConstants.CMD_OTP_VERIFICATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Send OTP via SMS to Customer in case of AgentMate App
            String smsText = MessageUtil.getMessage("otpSmsAccOpening", new String[]{"debit card", "card issuance", otp});
            SmsMessage smsMessage = new SmsMessage(this.mobileNumber, smsText);
            logger.info("CNIC : " + this.cnic + " Mobile # : " + this.mobileNumber + " : " + smsText);
            try {
                smsSender.send(smsMessage);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String response() {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of FeePaymentInquiryCommand.response()");
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_CNIC)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(this.cnic).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        if (fee != null && !fee.equals("0.0")) {
            strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                    .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                    .append("FEE")
                    .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                    .append(this.fee).append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE);
        }

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_MOB_NO)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(this.mobileNumber).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of FeePaymentInquiry.response()");
        return strBuilder.toString();
    }
}
