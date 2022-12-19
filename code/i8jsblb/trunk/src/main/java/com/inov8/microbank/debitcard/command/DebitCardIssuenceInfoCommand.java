package com.inov8.microbank.debitcard.command;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.verifly.common.constants.CardTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class DebitCardIssuenceInfoCommand extends BaseCommand {

    protected final Log logger = LogFactory.getLog(DebitCardIssuenceInfoCommand.class);
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
    private String cardTypeId;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuenceInfoCommand.prepare()");
        this.productId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_PROD_ID);
        this.mobileNumber = getCommandParameter(baseWrapper, "CMOB");
        this.cnic = getCommandParameter(baseWrapper, "CNIC");
        this.appId = getCommandParameter(baseWrapper, "APPID");
        this.transactionType = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TRANSACTION_TYPE);
        this.cardTypeId = getCommandParameter(baseWrapper, "CTYPE");

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
            logger.error("DebitCardIssuenceInfoCommand.prepare() Product model not found: " + ex.getStackTrace());
        }
        if (this.logger.isDebugEnabled())
            this.logger.debug("End of DebitCardIssuenceInfoCommand.prepare()");
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

        validateCustomer();
        try {
//            Double cardFee = 0.0d;
            if (!isConsumerApp && !deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                RetailerContactModel retailerContactModel = null;
                BaseWrapper retailerWrapper = new BaseWrapperImpl();
                RetailerContactModel model = new RetailerContactModel();
                model.setPrimaryKey(ThreadLocalAppUser.getAppUserModel().getRetailerContactId());
                retailerWrapper.setBasePersistableModel(model);
                retailerWrapper = commonCommandManager.loadRetailerContact(retailerWrapper);
                retailerContactModel = (RetailerContactModel) retailerWrapper.getBasePersistableModel();
                if (retailerContactModel != null && retailerContactModel.getIsDebitCardFeeEnabled() != null && retailerContactModel.getIsDebitCardFeeEnabled()) {
                    workFlowWrapper = getCommonCommandManager().calculateDebitCardFee(mobileNumber, cnic, appUserModel, customerModel, productModel,
                            ProductConstantsInterface.DEBIT_CARD_ISSUANCE,
                            CardConstantsInterface.CARD_FEE_TYPE_ISSUANCE, Long.parseLong(deviceTypeId), null);
                }
            } else if (isConsumerApp) {
                workFlowWrapper = getCommonCommandManager().calculateDebitCardFee(mobileNumber, cnic, appUserModel, customerModel, productModel,
                        ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE,
                        CardConstantsInterface.CARD_FEE_TYPE_ISSUANCE, Long.parseLong(deviceTypeId), null);
            } else {
//                if (transactionType.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
//                    workFlowWrapper = getCommonCommandManager().calculateDebitCardFeeForAPI(mobileNumber, cnic, appUserModel, customerModel, productModel,
//                            ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE,
//                            CardConstantsInterface.CARD_FEE_TYPE_RE_ISSUANCE, Long.valueOf(cardTypeId), Long.parseLong(deviceTypeId), null);
//                } else {
//                    workFlowWrapper = getCommonCommandManager().calculateDebitCardFeeForAPI(mobileNumber, cnic, appUserModel, customerModel, productModel,
//                            ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE,
//                            CardConstantsInterface.CARD_FEE_TYPE_ISSUANCE, Long.valueOf(cardTypeId), Long.parseLong(deviceTypeId), null);
//                }
            }
            if (workFlowWrapper.getCommissionAmountsHolder() != null) {
                fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
                if (workFlowWrapper.getCommissionAmountsHolder().getExclusivePercentAmount() > 0.0 || workFlowWrapper.getCommissionAmountsHolder().getExclusiveFixAmount() > 0.0) {
                    fee = String.valueOf(Double.valueOf(fee) + workFlowWrapper.getCommissionAmountsHolder().getStakeholderCommissionsMap().get(CommissionConstantsInterface.FED_STAKE_HOLDER_ID));
                } else {
                    fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
                }
            } else {
                fee = "0.0";
            }
        } catch (CommandException ce) {
//            if (ce.getErrorCode() == 9044L && appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.CLSPENDING)) {
//
//                DebitCardPendingSafRepo model1 = new DebitCardPendingSafRepo();
//                model1.setMobileNo(appUserModel.getMobileNo());
//                model1.setCnic(appUserModel.getNic());
//                model1.setProductId(Long.valueOf(ProductConstantsInterface.DEBIT_CARD_ISSUANCE));
//                model1.setIsCompleted("1");
//                try {
//                    model1 = this.getCommonCommandManager().loadDebitCardSafRepo(model1);
//                } catch (FrameworkCheckedException e) {
//                    e.printStackTrace();
//                }
//
//                if (model1 != null) {
//                    throw new CommandException("Request Already Exist With Mobile Number", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM);
//                }
//
//                DebitCardPendingSafRepo debitCardPendingSafRepo = new DebitCardPendingSafRepo();
//                debitCardPendingSafRepo.setMobileNo(appUserModel.getMobileNo());
//                debitCardPendingSafRepo.setCnic(appUserModel.getNic());
//                debitCardPendingSafRepo.setRegistrationStateId(appUserModel.getRegistrationStateId());
//                debitCardPendingSafRepo.setProductId(ProductConstantsInterface.DEBIT_CARD_ISSUANCE);
//                debitCardPendingSafRepo.setAppId(appId);
//                debitCardPendingSafRepo.setEmail("");
//                debitCardPendingSafRepo.setCardDescription("null");
//                debitCardPendingSafRepo.setDebitCardRegectionReason(ce.getMessage());
//                debitCardPendingSafRepo.setCreatedBy(null);
//                debitCardPendingSafRepo.setUpdatedBy(null);
//                debitCardPendingSafRepo.setCreatedOn(new Date());
//                debitCardPendingSafRepo.setUpdatedOn(new Date());
//                debitCardPendingSafRepo.setIsCompleted(String.valueOf(1));
//                debitCardPendingSafRepo.setCardTypeId(null);
//                debitCardPendingSafRepo.setAgentId(null);
//                debitCardPendingSafRepo.setSegmentId(null);
//                debitCardPendingSafRepo.setDeviceTypeId(deviceTypeId);
//                debitCardPendingSafRepo = commonCommandManager.debitCardPendingSafRepo(debitCardPendingSafRepo);
//                if (debitCardPendingSafRepo == null) {
//                    logger.info("Exception occured on saving data in debitCardPendingSafRepo :");
//                }
//            }
            throw new CommandException(ce.getMessage(), ce.getErrorCode(), ErrorLevel.MEDIUM, new Throwable());
        } catch (Exception e) {
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

    private String validateCustomerData(AppUserModel appUserModel, CustomerModel cModel) {
        String errorMsg = null;
        if (appUserModel.getMotherMaidenName() == null || appUserModel.getMotherMaidenName().equals(""))
            appUserModel.setMotherMaidenName("Mother");
//            errorMsg = "Customer Mother Name is Missing.";
        else if (appUserModel.getNicExpiryDate() == null || appUserModel.getNicExpiryDate().equals(""))
            errorMsg = "Customer NIC Expiry Date is Missing.";
        else if (appUserModel.getFirstName() == null || appUserModel.getFirstName().equals("") || appUserModel.getLastName() == null || appUserModel.getLastName().equals(""))
            errorMsg = "Customer Name is Missing.";
        else if (appUserModel.getDob() == null || appUserModel.getDob().equals(""))
            errorMsg = "Customer Date of Birth is Missing.";

        if (errorMsg != null)
            errorMsg += "Please contact call center to update your information in Wallet account and then process card request again.";
        return errorMsg;
    }

    private String validateCustomerAccount(AppUserModel appUserModel, UserDeviceAccountsModel uda) {
        String errorMsg = null;
        if ((appUserModel.getAccountClosedSettled() != null && appUserModel.getAccountClosedSettled())
                || (appUserModel.getAccountClosedUnsettled() != null && appUserModel.getAccountClosedUnsettled()))
            errorMsg = "Account has been Closed.";
        else if (uda.getAccountLocked())
            errorMsg = "Account is Blocked.";
        else if (!uda.getAccountEnabled())
            errorMsg = "Account is Deactivated.";
        else if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT))
            errorMsg = "Account is in Dormant State.";
        else if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.RQST_RCVD))
            errorMsg = "Account is in Request Received State.";
        else if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.BULK_RQST_RCVD))
            errorMsg = "Account is in Bulk Request Received State.";
//        else if (!appUserModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.VERIFIED)) {
//            errorMsg = "Your Account is not in Approved State.";
//        }
        return errorMsg;
    }

    private void validateCustomer() throws CommandException {
        logger.info("*** Validating Customer for Debit Card Issuance ***");
        String errorMessage = "No Customer exists against the given Mobile # and CNIC.";
        AppUserModel criteriaModel = new AppUserModel();
        UsecaseModel usecaseModel = new UsecaseModel();
        criteriaModel.setNic(cnic);
        criteriaModel.setMobileNo(mobileNumber);
        criteriaModel.setRegistrationStateId(RegistrationStateConstantsInterface.VERIFIED);
        Long[] registrationStateIds = {RegistrationStateConstants.VERIFIED, RegistrationStateConstants.CLSPENDING};
        try {
            appUserModel = commonCommandManager.getAppUserManager().getAppUserWithRegistrationStates(mobileNumber, cnic, registrationStateIds);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        if (appUserModel == null)
            throw new CommandException(errorMessage, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        Boolean isBlackListed = commonCommandManager.isCnicBlacklisted(appUserModel.getNic());
        if (isBlackListed)
            throw new CommandException("Customer CNIC is BlackListed.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        if (transactionType.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            usecaseModel = commonCommandManager.getUseCaseDAO().findByPrimaryKey(PortalConstants.KEY_DEBIT_CARD_REISSUENCE_USECASE_ID);
        } else {
            usecaseModel = commonCommandManager.getUseCaseDAO().findByPrimaryKey(PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID);
        }


//        if (usecaseModel != null && !usecaseModel.getIsAuthorizationEnable())
//            throw new CommandException("Please Enable Action Authorization against the Debit Card Issuance UseCase.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
//        List<ActionAuthorizationModel> existingReqList = null;
//        ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
//        actionAuthorizationModel.setReferenceId(appUserModel.getAppUserId().toString());
//        if (deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()) && transactionType.equals("02")) {
//            actionAuthorizationModel.setUsecaseId(PortalConstants.KEY_DEBIT_CARD_REISSUENCE_USECASE_ID);
//        } else {
//            actionAuthorizationModel.setUsecaseId(PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID);
//        }
//        try {
//            existingReqList = commonCommandManager.getActionAuthorizationFacade().checkExistingRequest(actionAuthorizationModel).getResultsetList();
////            existingReqList = commonCommandManager.getActionAuthorizationFacade().checkExistingRequestByUseCaseId(actionAuthorizationModel).getResultsetList();
//        } catch (FrameworkCheckedException e) {
//            e.printStackTrace();
//            throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
//        }
//        if (existingReqList != null && !existingReqList.isEmpty())
//            throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        UserDeviceAccountsModel userDeviceAccountsModel = null;
        try {
            userDeviceAccountsModel = this.commonCommandManager.loadUserDeviceAccountByUserId(appUserModel.getUsername());
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        CustomerModel cModel = new CustomerModel();
        cModel.setCustomerId(appUserModel.getCustomerId());
        customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
        if (customerModel == null)
            throw new CommandException(errorMessage, ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
        if (!deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            if (customerModel != null && !(customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1) || customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)))
                throw new CommandException(MessageUtil.getMessage("upgrade.customer.L1.account"), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
        }
        errorMessage = validateCustomerAccount(appUserModel, userDeviceAccountsModel);
        if (!StringUtil.isNullOrEmpty(errorMessage))
            throw new CommandException(errorMessage, ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
        errorMessage = validateCustomerData(appUserModel, customerModel);
        if (!StringUtil.isNullOrEmpty(errorMessage))
            throw new CommandException(errorMessage, ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
        List<DebitCardModel> list = null;
        try {
            list = commonCommandManager.getDebitCardModelDao().getDebitCardModelByMobileAndNIC(mobileNumber, cnic);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        if (list != null && !list.isEmpty()) {
            DebitCardModel debitCardModel = list.get(0);


            if (debitCardModel.getCardStatusId() != null
                    && (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_APPROVED) || debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_ACTIVE)) && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString()) && transactionType.equals("02")) {
                if (debitCardModel.getReIssuanceStatus() != null
                        && (debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)
                        || debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_APPROVED)
                        || debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_PENDING)
                        || debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_INTITATED))) {
                    throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"),
                            ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
                } else {
                    logger.info("*** Customer Validation Successful for Debit Card Re-Issuance ***");
                }
            } else if (debitCardModel.getCardStatusId() != null && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_REJECTED)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_APPROVED)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_PENDING)
                    && !debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_INTITATED)) {
                throw new CommandException(MessageUtil.getMessage("debit.card.already.linked"), ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
            } else if (debitCardModel.getCardStatusId() != null
                    && (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)
                    || debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_APPROVED)
                    || debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_PENDING)
                    || debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_INTITATED))) {
                throw new CommandException(MessageUtil.getMessage("debit.card.req.in.process"),
                        ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());
            }

//            if(debitCardModel.getReissuance() != null && debitCardModel.getReissuance().equals("1")){
//                throw new CommandException("Debit Card Re-issuance request already exists", ErrorCodes.DEBIT_CARD_REISSUANCE_EXISTS, ErrorLevel.MEDIUM, new Throwable());
//            }


        } else if (transactionType.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
            throw new CommandException(MessageUtil.getMessage("Please.Issue.You.Debit.Card.First"), ErrorCodes.INVALID_INPUT, ErrorLevel.MEDIUM, new Throwable());


        } else {
            if (transactionType.equals("02") && deviceTypeId.equals(DeviceTypeConstantsInterface.WEB_SERVICE.toString())) {
                logger.info("*** Customer Validation Successful for Debit Card Re-Issuance ***");
            } else {

                logger.info("*** Customer Validation Successful for Debit Card Issuance ***");
            }
        }
    }

    @Override
    public String response() {
        if (this.logger.isDebugEnabled())
            this.logger.debug("Start of DebitCardIssuanceInfoCommand.response()");
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

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_SEGMENT_ID)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(customerModel.getSegmentId()).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        if (this.logger.isDebugEnabled())
            this.logger.debug("End of DebitCardIssuanceInfoCommand.response()");
        return strBuilder.toString();
    }
//
//    public void setGenericDao(GenericDao genericDao) {
//        this.genericDao = genericDao;
//    }
}
