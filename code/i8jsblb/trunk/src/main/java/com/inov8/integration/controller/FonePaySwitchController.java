package com.inov8.integration.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.vo.CardType;
import com.inov8.integration.vo.CatalogList;
import com.inov8.integration.vo.SegmentList;
import com.inov8.integration.webservice.controller.WebServiceSwitchController;
import com.inov8.integration.webservice.optasiaVO.*;
import com.inov8.integration.webservice.vo.*;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.MiniStatementListViewModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.util.DebitCardUtill;
import com.inov8.microbank.debitcard.vo.DebitCardReversalVO;
import com.inov8.microbank.fonepay.common.*;
import com.inov8.microbank.fonepay.model.EcofinSubAgentModel;
import com.inov8.microbank.fonepay.model.VirtualCardModel;
import com.inov8.microbank.fonepay.service.FonePayManager;
import com.inov8.microbank.hra.airtimetopup.model.HRARemitanceInfoModel;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO;
import com.inov8.microbank.server.dao.productmodule.ProductCatalogDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.dao.securitymodule.EcofinSubAgentDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDetailMasterDAO;
import com.inov8.microbank.server.facade.ReversalAdviceQueingPreProcessor;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountClosureFacade;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingBlinkCustomerDAO;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.devicemodule.DeviceTypeCommandManager;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionDetailMasterManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import com.inov8.microbank.updatecustomername.dao.UpdateCustomerNameDAO;
import com.inov8.microbank.updatecustomername.facade.UpdateCustomerNameFacade;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator;
import com.inov8.ola.server.service.accountholder.AccountHolderManager;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.web.context.ContextLoader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;

public class FonePaySwitchController implements WebServiceSwitchController {
    protected final Log logger = LogFactory.getLog(getClass());
    protected CommonCommandManager commonCommandManager;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private MfsAccountClosureFacade mfsAccountClosureFacade;
    private MfsAccountManager mfsAccountManager;
    private SmartMoneyAccountManager smartMoneyAccountManager;
    private AccountHolderManager accountHolderManager;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;
    private MessageSource messageSource;
    private ProductCatalogDAO catalogDAO;
    private ClsPendingBlinkCustomerDAO clsPendingBlinkCustomerDAO;
    private ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO;
    private LimitManager limitManager;
    private UpdateCustomerNameFacade updateCustomerNameFacade;
    private BlinkCustomerModelDAO blinkCustomerModelDAO;
    private UpdateCustomerNameDAO updateCustomerNameDAO;
    private AppUserDAO appUserDAO;
    private CustomerDAO customerDAO;
    private EcofinSubAgentDAO ecofinSubAgentDAO;
    private CustomerPictureDAO customerPictureDAO;
    //    private IBFTSwitchController ibftSwitchController;
    private ESBAdapter esbAdapter;
    //    private IBFTRetryAdviceDAO ibftRetryAdviceDAO;
    private ReversalAdviceQueingPreProcessor reversalAdviceQueingPreProcessor;

    private TransactionReversalManager transactionReversalManager;
    private TransactionDetailMasterDAO transactionDetailMasterDAO;
    private TransactionDetailMasterManager transactionDetailMasterManager;


    private boolean defaultUserLogin() {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        String username = "0010787";

        String password = "6tBH5Et3C3b9p7Xzr1YVIQ==";
        boolean result = false;
        try {
            DeviceTypeCommandModel deviceTypeCommandModel = new DeviceTypeCommandModel();
            deviceTypeCommandModel.setDeviceTypeId(DeviceTypeConstantsInterface.BANKING_MIDDLEWARE);
            deviceTypeCommandModel.setCommandId(Long.valueOf("126"));
            baseWrapper.setBasePersistableModel(deviceTypeCommandModel);
            CustomList<DeviceTypeCommandModel> customList = getDeviceTypeCommandManager().loadDeviceTypeCommand(baseWrapper);
            if (null != customList) {
                List<DeviceTypeCommandModel> list = customList.getResultsetList();
                if (list.isEmpty()) {
                    this.logger.error("[defaultUserLogin] Unable to Load Device Type Command - ErrorCode:16");
                } else {
                    deviceTypeCommandModel = (DeviceTypeCommandModel) list.get(0);

                    baseWrapper.putObject("DTID", DeviceTypeConstantsInterface.BANKING_MIDDLEWARE);
                    baseWrapper.putObject("UID", username);
                    baseWrapper.putObject("PIN", password);
                    getCommandManager().executeCommand(baseWrapper, "25");

                    AppUserModel appUserModel = getAllPayWebResponseDataPopulator().getAppUserModel(username);
                    baseWrapper.putObject("DTID", DeviceTypeConstantsInterface.BANKING_MIDDLEWARE);
                    baseWrapper.setBasePersistableModel(appUserModel);
                    ValidationErrors validationErrors = getCommonCommandManager().checkUserCredentials(baseWrapper);
                    if (validationErrors.hasValidationErrors()) {
                        this.logger.error("[defaultUserLogin] Invalid credentials for default User. Error:" + validationErrors.getErrors());
                    } else {
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
            this.logger.error("[defaultUserLogin] Exception occurred while trying to Login default User. Details: ", e);
        }
        return result;
    }

    @Override
    public WebServiceVO paymentInquiry(WebServiceVO webServiceVO) {

        String cnicNo = "";// webServiceVO.getCnicNo();
        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getTransactionAmount();
        String dateTime = webServiceVO.getDateTime();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String isOtpReq = webServiceVO.getReserved1();
        AppUserModel appUserModel = new AppUserModel();
        String charges = "";
        String messageType = "Payment";
        String cmdId = CommandFieldConstants.CMD_FONEPAY_INQUIRY;
        BaseWrapper bWrapper = new BaseWrapperImpl();

        logger.info("[FonePay paymentInquiry] [CNIC:" + cnicNo + ", Mobile:" + mobileNo + ", RRN:" + rrn + ", DateTime:" + dateTime + ", Trx Amount:" + trxnAmount + "]");

        FonePayLogModel fonePayLogModel = null;
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_PAYMENT_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            webServiceVO.setCnicNo(appUserModel.getNic());
            cnicNo = appUserModel.getNic();

            bWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
            bWrapper.putObject(CommandFieldConstants.KEY_CNIC, cnicNo);
            bWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, trxnAmount);
            bWrapper.putObject(CommandFieldConstants.KEY_DATE, dateTime);
            bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

            if (!CommonUtils.isTwoDecimalPlacesAmount(trxnAmount)) {
                webServiceVO.setResponseCode(FonePayResponseCodes.TRXN_AMOUNT_DECIMAL_PLACES);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.TRXN_AMOUNT_DECIMAL_PLACES));
            } else {
                webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            }

            charges = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_FONEPAY_INQUIRY);

            if (isOtpReq != null && isOtpReq.equals("02")) {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                logger.info("The plain otp is " + otp);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                webServiceVO.setOtpPin(otp);
                getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
            }

//    		if(FonePayResponseCodes.SUCCESS_RESPONSE_CODE.equals(webServiceVO.getResponseCode())){
//    			charges = getCommandManager().executeCommand(bWrapper , CommandFieldConstants.CMD_FONEPAY_INQUIRY);
//                String cmdId=CommandFieldConstants.CMD_FONEPAY_PAYMENT,messageType="Payment";
//                ///Generate OTP and store in MiniTransaction
//                String otp = CommonUtils.generateOneTimePin(5);
//                logger.info("The plain otp is " + otp);
//                String encryptedPin = EncoderUtils.encodeToSha(otp);
//                getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(),cmdId);
//                webServiceVO.setOtpPin(otp);
//
//                getFonePayManager().sendOtpSms(otp,messageType,webServiceVO.getMobileNo());
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setCharges(charges);//check
//    		}else{
//    			webServiceVO.setResponseCode(webServiceVO.getResponseCode());
//    			webServiceVO.setResponseCodeDescription(webServiceVO.getResponseCodeDescription());
//    		}

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.paymentInquiry] Command Exception occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());

        } catch (Exception ex) {
            logger.error("[FonePaySwitchController.paymentInquiry] Error occured: " + ex.getMessage(), ex);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }

        logger.info("FonepayPaymentInfoCommand => " + "Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO paymentRequest(WebServiceVO webServiceVO) {
        ActionLogModel actionLogModel = new ActionLogModel();
        this.actionLogBeforeStart(null, null, null, null);
        AppUserModel appUserModel = null;

        String accountNo1 = webServiceVO.getAccountNo1();
        String mobileNo = "";//webServiceVO.getMobileNo();
        String dateTime = webServiceVO.getDateTime();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String amount = webServiceVO.getTransactionAmount();
        String charges = webServiceVO.getCharges();
        String transactionType = webServiceVO.getTransactionType();
        String mpin = webServiceVO.getMobilePin();
        String terminalId = webServiceVO.getTerminalId();
        String paymentType = webServiceVO.getPaymentType();
        String vcn = "";//webServiceVO.getCardNo();
        String enteredVcExpiry = webServiceVO.getCardExpiry();
        String settelmentType = webServiceVO.getSettlementType();
        String isValidationRequired = webServiceVO.getReserved1();


        logger.info("[FonePay paymentRequest] [accountNo1:" + accountNo1 + ", paymentType:" + paymentType + ", RRN:" + rrn + ", DateTime:" + dateTime + ", Trx Amount:" + amount + ", Trx Type:" + transactionType + "]");

        BaseWrapper idWrapper = new BaseWrapperImpl();

        idWrapper.putObject(CommandFieldConstants.KEY_DATE, dateTime);
        idWrapper.putObject(CommandFieldConstants.KEY_RRN, rrn);
        idWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, amount);
        idWrapper.putObject(CommandFieldConstants.KEY_CAMT, charges);
        idWrapper.putObject(FonePayConstants.TRANSACTION_TYPE, transactionType);
        idWrapper.putObject(FonePayConstants.PAYMENT_TYPE, paymentType);
        idWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, "5");
        idWrapper.putObject(CommandFieldConstants.KEY_PIN, mpin);
        idWrapper.putObject(FonePayConstants.VIRTUAL_CARD_EXPIRY, enteredVcExpiry);
        idWrapper.putObject("ENCT", EncryptionUtil.ENCRYPTION_TYPE_AES);
        idWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getMicrobankTransactionCode());
        idWrapper.putObject(FonePayConstants.KEY_FONEPAY_SETTLEMENT_TYPE, settelmentType);
        idWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());

        // New Wrapper for Pin Command
        BaseWrapper pinWrapper = new BaseWrapperImpl();
        pinWrapper.putObject("ENCT", EncryptionUtil.ENCRYPTION_TYPE_AES);
        pinWrapper.putObject(CommandFieldConstants.KEY_PIN, mpin);
        pinWrapper.putObject("DTID", DeviceTypeConstantsInterface.ALL_PAY);
        pinWrapper.putObject("PIN_RETRY_COUNT", 0);
        pinWrapper.putObject("ISWEBSERVICE", "1");

        //**************************************************************
        String existingVcExpiry = "";
        FonePayLogModel fonePayLogModel = null;


        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
        fonePayMessageVO.setValidateWebServiceEnabled(true);
        fonePayMessageVO.setValidateFonePayEnabled(true);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            if (!CommonUtils.isTwoDecimalPlacesAmount(amount)) {
                webServiceVO.setResponseCode(FonePayResponseCodes.TRXN_AMOUNT_DECIMAL_PLACES);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.TRXN_AMOUNT_DECIMAL_PLACES));
                return webServiceVO;
            }

            VirtualCardModel virtualCardModel = null;
            boolean isCardPayment = false;
            String paymentTypeForLog = FonePayConstants.REQ_PAYMENT_FONEPAY;

            if ((paymentType != null) && paymentType.equals(FonePayConstants.VIRTUAL_CARD_PAYMENT)) {
                isCardPayment = true;
                vcn = accountNo1;
                webServiceVO.setCardNo(vcn);
                idWrapper.putObject(FonePayConstants.VIRTUAL_CARD_NO, vcn);
                paymentTypeForLog = FonePayConstants.REQ_PAYMENT_CARD;
            } else {
                webServiceVO.setMobileNo(accountNo1);
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, paymentTypeForLog);

            if (isCardPayment) {
                VirtualCardModel vcModel = new VirtualCardModel();
                vcModel.setCardNo(vcn);
                SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
                searchBaseWrapper.setBasePersistableModel(vcModel);
                List<VirtualCardModel> list = null;
                searchBaseWrapper = getFonePayManager().loadVirtualCard(searchBaseWrapper);
                list = searchBaseWrapper.getCustomList().getResultsetList();
                if (list != null && list.size() > 0) {
                    virtualCardModel = list.get(0);
                    mobileNo = virtualCardModel.getMobileNo();
                } else {
                    logger.error("Invalid Card No. No record found against Card No:" + accountNo1);
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CARD_NOT_FOUND);
                    return webServiceVO;
                }
            } else {
                mobileNo = accountNo1;
            }

            fonePayMessageVO.setMobileNo(mobileNo);
            fonePayMessageVO.setCardNo(vcn);
//            fonePayMessageVO.setMpin(webServiceVO.getMobilePin());

            fonePayMessageVO.setValidateDiscripent(true);
            if (!(webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL))) {
                if (!(paymentType.equals(FonePayConstants.AGENT_SETTLEMENT_PAYMENT))) {
                    fonePayMessageVO = getFonePayManager().makevalidateExistingCustomer(fonePayMessageVO);
                } else {
                    fonePayMessageVO = getFonePayManager().makevalidateAgent(fonePayMessageVO);
                }
            } else {
                idWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
                fonePayMessageVO.setValidateFonePayEnabled(false);
                fonePayMessageVO = getFonePayManager().makevalidateExistingCustomer(fonePayMessageVO);
            }
            // If User is OK then go to execute command
            if (FonePayResponseCodes.SUCCESS_RESPONSE_CODE.equals(fonePayMessageVO.getResponseCode())) {
                appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNo);
                if (appUserModel != null) {
                    logger.debug("[FonepayPaymentCommand.execute] AppUserModel loader wil AppUserId:" + appUserModel.getAppUserId());
                    ThreadLocalAppUser.setAppUserModel(appUserModel);
                }

//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
                webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;

                idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, appUserModel.getMobileNo());
                idWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());

                //Virtual Card Transaction Type
                if (isCardPayment) {
                    boolean cardExpiredFlag = false;

                    existingVcExpiry = virtualCardModel.getCardExpiry();
                    //validate Card's Expiry date
                    if (!existingVcExpiry.equals(enteredVcExpiry)) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CARD_EXPIRY_INCORRECT);
                    }

                    if (checkExpiryDate(enteredVcExpiry)) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CARD_IS_EXPIRED);
                    }

                    if (virtualCardModel.getIsBlocked()) {
                        return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CARD_IS_BLOCKED);
                    }

                    // If every thing is ok, then execute command.
                    String transactionCode = getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_FONEPAY_PAYMENT);
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    webServiceVO.setMicrobankTransactionCode(transactionCode);
                } else if (!(paymentType.equals(FonePayConstants.AGENT_SETTLEMENT_PAYMENT))) {
                    // WebService Payment Transaction
                    if (!(webServiceVO.getChannelId().equals(FonePayConstants.APIGEE_CHANNEL) || webServiceVO.getChannelId().equals(FonePayConstants.NOVA_CHANNEL))) {
                        FonePayMessageVO fonPayMessageVO = getFonePayManager().validateMPINRetryCount(webServiceVO, pinWrapper);//getCommandManager().executeCommand(pinWrapper,CommandFieldConstants.CMD_VERIFY_PIN);
                        if (fonPayMessageVO.getResponseCode() == FonePayResponseCodes.SUCCESS_RESPONSE_CODE) {
                            String transactionCode = getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_FONEPAY_PAYMENT);
                            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                            webServiceVO.setMicrobankTransactionCode(transactionCode);
                        } else {
                            webServiceVO.setResponseCode(fonPayMessageVO.getResponseCode());
                            FonePayUtils.prepareErrorResponse(webServiceVO, webServiceVO.getResponseCode());
                        }
                    } else {
                        BaseWrapper bWrapper = new BaseWrapperImpl();
//                            bWrapper.putObject(CommandFieldConstants.KEY_PIN,webServiceVO.getMobilePin());
                        this.userValidation(webServiceVO, CommandFieldConstants.CMD_FONEPAY_INQUIRY);
                        if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                            return webServiceVO;
//                            if(isValidationRequired != null && isValidationRequired.equals("02")) {
//                                this.validateOTP(webServiceVO, CommandFieldConstants.CMD_FONEPAY_INQUIRY);
//                            }
//                            bWrapper.putObject(CommandFieldConstants.KEY_MOB_NO,webServiceVO.getMobileNo());
//                            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,webServiceVO.getChannelId());
//                            bWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID,CommandFieldConstants.CMD_FONEPAY_INQUIRY);
//                            String resp= getCommandManager().executeCommand(bWrapper,CommandFieldConstants.CMD_OTP_VERIFICATION);//getCommandManager().executeCommand(pinWrapper,CommandFieldConstants.CMD_VERIFY_PIN);
                        idWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
                        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                        String transactionCode = getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_FONEPAY_PAYMENT);
                        webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                        webServiceVO.setMicrobankTransactionCode(transactionCode);
                    }

                } else {
                    String transactionCode = getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_FONEPAY_SETTLEMENT);
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    webServiceVO.setMicrobankTransactionCode(transactionCode);
                }
            }
            //If User is not ok , then set response code and message.
            else {

                webServiceVO.setResponseCode(fonePayMessageVO.getResponseCode());
                webServiceVO.setResponseCodeDescription(fonePayMessageVO.getResponseCodeDescription());
            }

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                }
            } else {
                logger.error("[FonePaySwitchController.paymentRequest] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            logger.error("[FonePaySwitchController.paymentRequest] Command Exception occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());

            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
            } else if (ex instanceof CommandException) {
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            }
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }

        this.actionLogAfterEnd(actionLogModel);
        logger.info("FonepayPaymentCommand => " + "Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }


    public WebServiceVO paymentReversal(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Payment Reversal");
            if ((webServiceVO.getMicrobankTransactionCode() == null) || ("".equals(webServiceVO.getMicrobankTransactionCode()))) {
                this.logger.error("[FonePaySwitchController.paymentReversal] Error occured: Transaction Code not supplied ");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, "56");
            } else {
                webServiceVO = getFonePayManager().makeTransactionReversal(webServiceVO);
                if ((webServiceVO.getResponseCode() == null) || (webServiceVO.getResponseCode() == "00")) {
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Success For Payment Reversal");
                }
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
            } else {

                this.logger.error("[FonePaySwitchController.paymentReversal] Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                webServiceVO.setResponseCode("10");
            }

        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }

    public WebServiceVO otpVerification(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        ActionLogModel actionLogModel = new ActionLogModel();
        try {
            actionLogBeforeStart(null, null, null, null);
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Verify OTP");
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject("PIN", ThirdPartyEncryptionUtil.encryptWithAES("65412399991212FF65412399991212FF", webServiceVO.getOtpPin()));
            baseWrapper.putObject("MOBN", webServiceVO.getMobileNo());
            baseWrapper.putObject("ACTION", "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.CMD_OTP_VERIFICATION);
            getCommandManager().executeCommand(baseWrapper, "182");
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
        } catch (CommandException e) {
            if (e.getErrorCode() == 0L) {
                FonePayUtils.prepareErrorResponse(webServiceVO, "00");
            } else if (e.getErrorCode() == 9023L) {
                FonePayUtils.prepareErrorResponse(webServiceVO, "58".toString());
            } else if (e.getErrorCode() == 9029L) {
                FonePayUtils.prepareErrorResponse(webServiceVO, "59".toString());
            } else {
                this.logger.error("[FonePaySwitchController.verifyOTP] Command Exception Error occured:" + e.getMessage(), e);
                if (e.getMessage() != "") {
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                    webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                } else
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, "10");
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

            this.logger.error("[FonePaySwitchController.verifyOTP] Error occured:" + e.getMessage(), e);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            webServiceVO.setResponseCode("10");
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        this.logger.info("[FonePaySwitchController.verifyOTP] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    public WebServiceVO cardTagging(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        ActionLogModel actionLogModel = new ActionLogModel();
        try {
            actionLogBeforeStart(PortalConstants.ACTION_CREATE, null, null, null);
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Virtual Card Tagging");
            webServiceVO = getFonePayManager().makeVirtualCardTagging(webServiceVO);
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.virtualCardTagging] Error occured: " + e.getMessage(), e);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, "10");
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        return webServiceVO;
    }

    private ActionLogModel actionLogBeforeStart(Long actionId, Long usecaseId, Long commandId, String referenceId) {
        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setActionId(actionId);
        actionLogModel.setUsecaseId(usecaseId);
        actionLogModel.setCommandId(commandId);
        actionLogModel.setCustomField1(referenceId);
        actionLogModel.setActionStatusId(ActionStatusConstantsInterface.START_PROCESSING);
        actionLogModel.setDeviceTypeId(DeviceTypeConstantsInterface.WEB_SERVICE);
        actionLogModel.setStartTime(new Timestamp(new Date().getTime()));
        actionLogModel.setAppUserId(Long.valueOf(4L));

        actionLogModel = insertActionLogRequiresNewTransaction(actionLogModel);
        if (actionLogModel.getActionLogId() != null) {
            ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
        }
        return actionLogModel;
    }

    private void actionLogAfterEnd(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            if ((actionLogModel != null) && (actionLogModel.getActionLogId() != null)) {
                actionLogModel.setActionStatusId(ActionStatusConstantsInterface.END_PROCESSING);
                actionLogModel.setEndTime(new Timestamp(new Date().getTime()));
                baseWrapper.setBasePersistableModel(actionLogModel);
                getActionLogManager().createOrUpdateActionLog(baseWrapper);
            }
        } catch (Exception e) {
            this.logger.error("FonePaySwitchController.actionLogAfterEnd - Error occured: ", e);
        }
    }

    private ActionLogModel insertActionLogRequiresNewTransaction(ActionLogModel actionLogModel) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(actionLogModel);
        try {
            baseWrapper = getActionLogManager().createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
            actionLogModel = (ActionLogModel) baseWrapper.getBasePersistableModel();
        } catch (Exception ex) {
            this.logger.error("[FonePaySwitchController]Exception occurred while insertActionLogRequiresNewTransaction() ", ex);
        }
        return actionLogModel;
    }

    public WebServiceVO accountLinkDelink(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String dateTime = webServiceVO.getDateTime();
        String transactionType = webServiceVO.getTransactionType();
        String isValidationRequired = webServiceVO.getReserved1();
        String messageType = "Account Link-DeLink";
        AppUserModel appUserModel = new AppUserModel();

        String currCmdId = CommandFieldConstants.KEY_CMD_ACC_LINK;
        if ((!StringUtil.isNullOrEmpty(transactionType)) && (transactionType.equals("02"))) {
            currCmdId = CommandFieldConstants.KEY_CMD_ACC_DELINK;
        }
        String reqType = "Account Link";
        if ((!StringUtil.isNullOrEmpty(transactionType)) && (transactionType.equals("02"))) {
            reqType = "Account DeLink";
        }
        this.logger.info("[FonePay " + reqType + "] CNIC:" + cnic + ", Mobile:" + mobileNumber + ", RRN:" + rrn + ", DateTime:" + dateTime + ", TransactionType:" + transactionType + "]");
        try {
          /*  webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;*/
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, reqType);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null, null, mobileNumber);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            CustomerModel customerModel = new CustomerModel();
            customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());


            this.userValidation(webServiceVO, currCmdId);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            webServiceVO = getFonePayManager().makeLinkDelinkAccount(webServiceVO);
            actionLogAfterEnd(actionLogModel);

            OlaCustomerAccountTypeModel ola = new OlaCustomerAccountTypeModel();
            ola.setActive(true);
            ola.setIsCustomerAccountType(true);
            ola = getCommonCommandManager().loadCustomerAccountTypeModelById(customerModel.getCustomerAccountTypeId());

            webServiceVO.setAccountType(String.valueOf(ola.getName()));
            webServiceVO.setAccountTitle(appUserModel.getFirstName() + " " + appUserModel.getLastName());
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9010) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MPIN_CHANGE_REQ.toString());
            } else if (e.getErrorCode() == 9015) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_ACCOUNT_CREDENTIALS_EXP.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else {
                logger.error("[FonePaySwitchController.billPayment] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController " + reqType + "] Error occured: " + e.getMessage(), e);
            webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, "10");
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("[FonePaySwitchController " + reqType + "] (In End) Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

    public WebServiceVO verifyAccount(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String dateTime = webServiceVO.getDateTime();
        String transactionType = webServiceVO.getTransactionType();
        String otpReqType = webServiceVO.getReserved2();
        String reqType = "Verify New Customer";
        if ((!StringUtil.isNullOrEmpty(transactionType)) && (transactionType.equals("02"))) {
            reqType = "Verify Existing Customer";
        }
        this.logger.info("[FonePay Verify Account] [CNIC:" + cnic + ", Mobile:" + mobileNumber + ", RRN:" + rrn + ", DateTime:" + dateTime + ", TransactionType:" + transactionType + "]");
        try {
           /* webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;*/
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, reqType);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, mobileNumber);
            if (transactionType.equals("02")) {
                webServiceVO = getFonePayManager().makeExistingCustomerVerification(webServiceVO);
            } else if (transactionType.equals("01")) {
                webServiceVO = getFonePayManager().verifyNewCustomer(webServiceVO);
            }
            actionLogAfterEnd(actionLogModel);
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.verifyAccount] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }


            webServiceVO.setAccountTitle(null);
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("[FonePaySwitchController.verifyAccount] (In End) Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

    private WebServiceVO validateRRN(WebServiceVO webServiceVO) {
        String responseCode = FonePayResponseCodes.INVALID_REQUEST;
        String description = "Your Request cannot be processed at the moment.Please try again later.";
        try {
            if (webServiceVO.getRetrievalReferenceNumber() == null || webServiceVO.getRetrievalReferenceNumber().equals("")) {
                responseCode = FonePayResponseCodes.APIGEE_RRN_ALREADY_EXISTS.toString();
                description = FonePayResponseCodes.APIGEE_RRN_ALREADY_EXISTS_DESCRIPTION;
            } else {
                if (getFonePayManager().validateApiGeeRRN(webServiceVO)) {
                    responseCode = FonePayResponseCodes.SUCCESS_RESPONSE_CODE;
                    description = FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION;
                } else {
                    responseCode = FonePayResponseCodes.APIGEE_RRN_ALREADY_EXISTS.toString();
                    description = FonePayResponseCodes.APIGEE_RRN_ALREADY_EXISTS_DESCRIPTION;
                }
            }
        } catch (Exception ex) {
            logger.error("Error While Validating APIGEE RRN :: " + ex.getMessage(), ex);
            responseCode = FonePayResponseCodes.GENERAL_ERROR;
        } finally {
            webServiceVO.setResponseCode(responseCode);
            webServiceVO.setResponseCodeDescription(description);
        }
        return webServiceVO;
    }

    private WebServiceVO validateAmaAccountType(WebServiceVO webServiceVO, AppUserModel appUserModel) {
        String responseCode = FonePayResponseCodes.INVALID_REQUEST;
        String description = "Your Request cannot be processed at the moment.Please try again later.";
        try {
            if (webServiceVO.getChannelId() == null || webServiceVO.getChannelId().equals("")) {
                responseCode = FonePayResponseCodes.INVALID_REQUEST;
                description = "FonePayResponseCodes.Your Request cannot be processed at the moment.Please try again later.";
            } else {
                CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
                Long customerAccTypeId = customerModel.getCustomerAccountTypeId();
                if (webServiceVO.getChannelId().equals(MessageUtil.getMessage("BLINK.CUSTOMER.CHANNEL.ID"))) {
                    if (!customerAccTypeId.equals(MessageUtil.getMessage("BLINK.CUSTOMER.ACCOUNT.TYPE.ID"))) {
                        responseCode = FonePayResponseCodes.SUCCESS_RESPONSE_CODE;
                        description = FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION;
                    } else {
                        responseCode = FonePayResponseCodes.INVALID_AMOUNT;
                        description = "Your Request cannot be processed at the moment.Please try again later.";
                    }
                } else {
                    responseCode = FonePayResponseCodes.SUCCESS_RESPONSE_CODE;
                    description = FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION;
                }
            }
        } catch (Exception ex) {
            logger.error("Error While Validating APIGEE RRN :: " + ex.getMessage(), ex);
            responseCode = FonePayResponseCodes.GENERAL_ERROR;
        } finally {
            webServiceVO.setResponseCode(responseCode);
            webServiceVO.setResponseCodeDescription(description);
        }
        return webServiceVO;
    }

    public WebServiceVO accountOpening(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String dateTime = webServiceVO.getDateTime();
        //Below parameter are set to change cnic issuance Date format change  for api dd-mm-yyyy to yyyy-mm-dd
        webServiceVO.setReserved10(String.valueOf(DeviceTypeConstantsInterface.WEB_SERVICE));

        this.logger.info("[FonePay Open Account] [CNIC:" + cnic + ", Mobile:" + mobileNumber + ", RRN:" + rrn + ", DateTime:" + dateTime + "]");
        try {
//            webServiceVO = this.validateRRN(webServiceVO);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;
            if (webServiceVO.getReserved4() != null && webServiceVO.getReserved4().equals("1")) {
                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Minor Account Opening");

            } else {
                fonePayLogModel=new FonePayLogModel();
                fonePayLogModel.setCnic(webServiceVO.getCnicNo());
                fonePayLogModel.setMobile_no(webServiceVO.getMobileNo());
                fonePayLogModel.setResponse_code(webServiceVO.getResponseCode());
                fonePayLogModel.setResponse_description(webServiceVO.getResponseCodeDescription());
                fonePayLogModel.setRrn(webServiceVO.getRetrievalReferenceNumber());
                fonePayLogModel.setTransactionId(webServiceVO.getTransactionId());
                Date date = new Date();
                Timestamp ts_now = new Timestamp(date.getTime());
                fonePayLogModel.setCreated_on(ts_now);
                fonePayLogModel.setUpdated_on(ts_now);
//                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Account Opening");
            }
            AppUserModel webServiceAppUser = new AppUserModel();
            webServiceAppUser.setAppUserId(Long.valueOf(4L));
            ThreadLocalAppUser.setAppUserModel(webServiceAppUser);

            webServiceAppUser = getCommonCommandManager().loadAppUserByCnicAndType(cnic);
            if (webServiceAppUser != null) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST);
            }
            if (this.getCommonCommandManager().isCnicBlacklisted(cnic)) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
            }

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());

            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L), Long.valueOf(Long.parseLong("121")), mobileNumber);

            int pendingTransactionCount = getCommonCommandManager().countCustomerPendingTrx(cnic);
            if (webServiceVO.getChannelId().equalsIgnoreCase(MessageUtil.getMessage("EcofinChannelId"))) {

                EcofinSubAgentModel ecofinSubAgent = ecofinSubAgentDAO.findByPrimaryKey(Long.valueOf(webServiceVO.getReserved3()));

                if (ecofinSubAgent == null) {
                    this.logger.error("Ecofin Sub Agent Error:");
                }
            }
//            AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(MessageUtil.getMessage("EcofinMobileNumberR"), UserTypeConstantsInterface.RETAILER);
//            if (appUserModel != null) {
            if (pendingTransactionCount > 0) {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
            } else {
                pendingTransactionCount = getCommonCommandManager().countCustomerPendingTrxByMobile(mobileNumber);
                if (pendingTransactionCount > 0) {
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
                } else {
                    webServiceVO = getFonePayManager().createCustomer(webServiceVO, false);
                }
            }

            if (webServiceVO.getReserved7() != null && !webServiceVO.getReserved7().equals("")) {
                webServiceVO.setResponseCode(webServiceVO.getReserved7());
            }
//            }
            actionLogAfterEnd(actionLogModel);
            if (webServiceVO.getReserved7() != null && !webServiceVO.getReserved7().equals("")) {
                webServiceVO.setResponseCode(webServiceVO.getReserved7());
            }


        } catch (CommandException ex) {
            if (ex.getErrorCode() == 9098) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_EMAIL_ADDRESS_ALREADY_EXISTS);
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.accountOpening] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.verifyAccount] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
                webServiceVO.setAccountTitle(null);
            }
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("[FonePaySwitchController.accountOpening] (In End) Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

    public WebServiceVO conventionalAccountOpening(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String dateTime = webServiceVO.getDateTime();


        this.logger.info("[FonePay conventionalAccountOpening] [CNIC:" + cnic + ", Mobile:" + mobileNumber + ", RRN:" + rrn + ", DateTime:" + dateTime + "]");
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Conventional Account Opening");

            AppUserModel webServiceAppUser = new AppUserModel();
            webServiceAppUser.setAppUserId(Long.valueOf(4L));
            ThreadLocalAppUser.setAppUserModel(webServiceAppUser);

            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L), Long.valueOf(Long.parseLong("121")), mobileNumber);

            int pendingTransactionCount = getCommonCommandManager().countCustomerPendingTrx(cnic);
            if (pendingTransactionCount > 0) {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
            } else {
                pendingTransactionCount = getCommonCommandManager().countCustomerPendingTrxByMobile(mobileNumber);
                if (pendingTransactionCount > 0) {
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
                } else {

                    webServiceVO = getFonePayManager().createCustomer(webServiceVO, false);
                }
            }

            actionLogAfterEnd(actionLogModel);
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.conventionalAccountOpening] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.verifyAccount] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
                webServiceVO.setAccountTitle(null);
            }
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("[FonePaySwitchController.conventionalAccountOpening] (In End) Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

    public WebServiceVO setCardStatus(WebServiceVO webServiceVO) {
        this.logger.info("[FonePaySwitchController.setCardStatus] Start:: ");
        String transactionType = webServiceVO.getTransactionType();
        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        String reqType;
        if (transactionType.equals("01")) {
            reqType = "Virtual Card Activation";
        } else {
            if (transactionType.equals("02")) {
                reqType = "Virtual Card De-Activation";
            } else {
                reqType = "Virtual Card Delete";
            }
        }
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, reqType);
            webServiceVO = getFonePayManager().updateCardInfo(webServiceVO);
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.setCardStatus] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, "10");
            }
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("[FonePaySwitchController.setCardStatus] (In End) Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

  /*private boolean checkExpiryDate(String expiry)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
    boolean isExpired = false;
    try
    {
      Date expiryDate = sdf.parse(expiry);
      Date currentDate = new Date();
      if ((expiryDate.getYear() < currentDate.getYear()) && (expiryDate.getMonth() > currentDate.getMonth())) {
        isExpired = true;
      } else if ((expiryDate.getYear() < currentDate.getYear()) && (expiryDate.getMonth() < currentDate.getMonth())) {
        isExpired = true;
      } else if ((expiryDate.getYear() < currentDate.getYear()) && (expiryDate.getMonth() == currentDate.getMonth())) {
        isExpired = true;
      } else if ((expiryDate.getYear() == currentDate.getYear()) && (expiryDate.getMonth() < currentDate.getMonth())) {
        isExpired = true;
      } else {
        isExpired = false;
      }
    }
    catch (ParseException e)
    {
      this.logger.info("[FonePaySwitchController.checkExpiryDate] (In End) Message: " + e.getMessage());
    }
    return isExpired;
  }*/

    public FonePayManager getFonePayManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (FonePayManager) applicationContext.getBean("fonePayFacade");
    }

    public CommandManager getCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommandManager) applicationContext.getBean("cmdManager");
    }

    public DeviceTypeCommandManager getDeviceTypeCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (DeviceTypeCommandManager) applicationContext.getBean("deviceTypeCommandManager");
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public ActionLogManager getActionLogManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (ActionLogManager) applicationContext.getBean("actionLogManager");
    }

    public AllPayWebResponseDataPopulator getAllPayWebResponseDataPopulator() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (AllPayWebResponseDataPopulator) applicationContext.getBean("allPayWebResponseDataPopulator");
    }

    public TransactionReversalManager getTransactionReversalManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (TransactionReversalManager) applicationContext.getBean("transactionReversalManager");
    }

    @Override
    public WebServiceVO generateOTP(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.generateOTP] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        ValidationErrors errors = new ValidationErrors();
        String req = "", cmdId = "", messageType = "";

        if (webServiceVO.getOtpPurpose().equals(FonePayConstants.FONEPAY_BALANCE_INQUIRY)) {
            req = FonePayConstants.REQ_BALANCE_INQUIRY_INFO;
            cmdId = CommandFieldConstants.CMD_CHK_ACC_BAL;
            messageType = "Balance Inquiry";
        } else if (webServiceVO.getOtpPurpose().equals(FonePayConstants.FONEPAY_MINI_STATMENT)) {
            req = FonePayConstants.REQ_MINI_STATMENT_INFO;
            cmdId = CommandFieldConstants.CMD_MINISTATEMENT_AGENT;
            messageType = "Mini Statment";
        } else if (webServiceVO.getOtpPurpose().equals(FonePayConstants.FONEPAY_WALLET_TO_WALLET)) {
            req = FonePayConstants.REQ_WALLET_TO_WALLET_INFO;
            cmdId = CommandFieldConstants.CMD_MINISTATEMENT_AGENT;
            messageType = "Mini Statment";
        } else {
            webServiceVO.setResponseCode(FonePayResponseCodes.INVALID_REQUEST);
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_REQUEST);
            logger.info("[FonePaySwitchController.generateOTP]  Response Code: " + webServiceVO.getResponseCode() + " Bad Request Aurguments");
            return webServiceVO;
        }

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, req);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            webServiceVO.setCnicNo(appUserModel.getNic());
            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if (webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                logger.info("The plain otp is " + otp);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                webServiceVO.setOtpPin(otp);

                getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());

                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.generateOTP] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
            }

        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.balance InquiryInfo] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    private WebServiceVO validateOTP(WebServiceVO webServiceVO, String validationForCommandId) throws Exception {
        BaseWrapper idWrapper = new BaseWrapperImpl();
        idWrapper.putObject(CommandFieldConstants.KEY_PIN, ThirdPartyEncryptionUtil.encryptWithAES("65412399991212FF65412399991212FF", webServiceVO.getOtpPin()));
        if (validationForCommandId.equals("202")) {
            idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getSenderMobileNumber());
        } else {
            idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        }
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        idWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, validationForCommandId);
        String response = getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_OTP_VERIFICATION);
        if (MfsWebUtil.isErrorXML(response)) {
            return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
        }
        webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        return webServiceVO;
    }

    private WebServiceVO validateMPIN(WebServiceVO webServiceVO) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
        baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
        String mPin = EncryptionUtil.encryptWithAES("682ede816988e58fb6d057d9d85605e0", webServiceVO.getMobilePin());
        baseWrapper.putObject(CommandFieldConstants.KEY_PIN, mPin);
        baseWrapper.putObject(CommandFieldConstants.KEY_PIN_RETRY_COUNT, "0");
        String response = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_VERIFY_PIN);
        if (MfsWebUtil.isErrorXML(response)) {
            return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
        }
        webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        return webServiceVO;
    }

    @Override
    public WebServiceVO balanceInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.Inquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        Boolean isAtmChannel = Boolean.FALSE;
        String fee = null;
        Long productId = null;
        DebitCardModel debitCardModel = new DebitCardModel();
        try {
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            ActionLogModel actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, Long.valueOf(CommandFieldConstants.CMD_CHK_ACC_BAL), webServiceVO.getMobileNo());
            String terminalid = null;
            if (webServiceVO.getTerminalId() != null)
                terminalid = webServiceVO.getTerminalId();
            if (terminalid != null && terminalid.equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                isAtmChannel = Boolean.TRUE;
            } else {
                webServiceVO = this.validateRRN(webServiceVO);
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }


            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_BALANCE_INQUIRY);
            if (terminalid != null && terminalid.equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByCardNumber(webServiceVO.getCardNo());
                if (debitCardModel != null) {
                    DebitCardUtill.verifyDebitCard(webServiceVO, debitCardModel);
                    if (!webServiceVO.getResponseCode().equals("00"))
                        return webServiceVO;
                    webServiceVO.setMobileNo(debitCardModel.getMobileNo());
                }
            }
            appUserModel = commonCommandManager.getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel == null) {
                logger.info("[FonePaySwitchController.Inquiry] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }
            CustomerModel customerModel = new CustomerModel();
            try {
                customerModel = commonCommandManager.getCustomerModelById(appUserModel.getCustomerId());
            } catch (CommandException e) {
                e.printStackTrace();
            }

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
//            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            ThreadLocalAppUser.setAppUserModel(appUserModel);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            if (!isAtmChannel) {
                this.userValidation(webServiceVO, CommandFieldConstants.CMD_CHK_ACC_BAL);
                if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }
            Long customerId;
            if (appUserModel.getCustomerId() != null)
                customerId = appUserModel.getCustomerId();
            else
                customerId = appUserModel.getAppUserId();

            Long paymentModeId;
            if (webServiceVO.getPaymentMode() != null && !webServiceVO.getPaymentMode().equals("") && webServiceVO.getPaymentMode().equals("HRA"))
                paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
            else
                paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
            AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(customerId, paymentModeId);
            SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, paymentModeId);
            if (isAtmChannel) {
                if (webServiceVO.getChannelId().equals("60373300000") || webServiceVO.getChannelId().equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                    logger.info("[FonePaySwitchController.ATMBalanceInquiry ON-US] Channel id# :: " + webServiceVO.getChannelId());

                    String balance = getCommonCommandManager().getAccountBalance(accountInfoModel, smartMoneyAccountModel);
                    webServiceVO.setBalance(balance);
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

                } else {
                    if (webServiceVO.getCurrencyCode().equals("586")) {
                        logger.info("[FonePaySwitchController.ATMBalanceInquiry OFF-US] Channel id# :: " + webServiceVO.getChannelId());

                        productId = ProductConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US;
                        WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                        workFlowWrapper = this.getCommonCommandManager().calculateDebitCardFeeForAPI(webServiceVO.getMobileNo(), webServiceVO.getCnicNo(), null, null, null,
                                productId, CardConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US, smartMoneyAccountModel.getCardProdId(), DeviceTypeConstantsInterface.MOBILE, debitCardModel);
                        fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
                        logger.info("[FonePaySwitchController.ATMBalanceInquiry OFF-US FEE] Fee :: " + fee);
                    } else {

                        if (customerModel != null && !customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                            logger.info("[FonePaySwitchController.Internation ATMBalanceInquiry OFF-US Not Allowed For L1 and L0] Channel id# :: " + webServiceVO.getChannelId());
                            fee = "0.0";
                        } else {

                            logger.info("[FonePaySwitchController.Internation ATMBalanceInquiry OFF-US] Channel id# :: " + webServiceVO.getChannelId());

                            productId = ProductConstantsInterface.INTERNATIONAL_BALANCE_INQUIRY_OFF_US;
                            WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                            workFlowWrapper = this.getCommonCommandManager().calculateDebitCardFeeForAPI(webServiceVO.getMobileNo(), webServiceVO.getCnicNo(), null, null, null,
                                    productId, CardConstantsInterface.INTERNATIONAL_ATM_BALANCE_INQUIRY_OFF_US, smartMoneyAccountModel.getCardProdId(), DeviceTypeConstantsInterface.MOBILE, debitCardModel);
                            fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
                            logger.info("[FonePaySwitchController.International ATMBalanceInquiry OFF-US FEE] Fee :: " + fee);
                        }
                    }
                    if (fee != null && !fee.equals("") && !fee.equals("0.0")) {
                        StringBuilder sb = new StringBuilder();
                        sb = new StringBuilder();
                        BaseWrapper dWrapper = new BaseWrapperImpl();
                        dWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
                        dWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, fee.toString());
                        dWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId.toString());
                        dWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ATM.toString());
                        dWrapper.putObject(CommandFieldConstants.KEY_TXAM, fee.toString());
                        dWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
                        dWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
                        dWrapper.putObject(CommandFieldConstants.KEY_TAMT, fee);
                        dWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, "MOBILE");
                        dWrapper.putObject(CommandFieldConstants.KEY_STAN, "");
                        dWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "");
                        dWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "2");//Customer Initiated Transaction
                        sb.append("Start of executeIssuanceFee() in AtmBalanceInquiryOffUs for Product :: ").append(productId.toString());
                        sb.append(" \nand Mobile # :: " + webServiceVO.getMobileNo() + " and CNIC :: " + webServiceVO.getCnicNo() + " and FeeType :: " + CardConstantsInterface.ATM_BALANCE_INQUIRY_OFF_US.toString()).append(" at Time ::" + new Date());
                        logger.info(sb.toString());
                        String response = null;
                        response = this.getCommandManager().executeCommand(dWrapper, CommandFieldConstants.CMD_DEBIT_CARD_CW);
                        logger.info("[FonePaySwitchController.ATMBalanceInquiry OFF-US] Response :: " + response);

                        String balance = MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.TRAN_BAL_NODEREF);
                        webServiceVO.setBalance(balance);
                        webServiceVO.setResponseCode("00");
                        webServiceVO.setResponseCodeDescription("Successfull");

                    } else if (!webServiceVO.getCurrencyCode().equals("586") && customerModel != null && !customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK) && fee.equals("0.0")) {
                        webServiceVO.setResponseCode("79");
                        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.INTERNATIONAL_TRANSACTION_NOT_ALLOWED_DESCRIPTION);
                    } else {
                        String balance = getCommonCommandManager().getAccountBalance(accountInfoModel, smartMoneyAccountModel);
                        webServiceVO.setBalance(balance);
                        webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

                    }
                }
            } else {
                String balance = getCommonCommandManager().getAccountBalance(accountInfoModel, smartMoneyAccountModel);
                webServiceVO.setBalance(balance);
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);


            }       // webServiceVO=getFonePayManager().saveBalnceInquiryInfo(webServiceVO);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9010) {
                FonePayUtils.prepareErrorResponse(webServiceVO, String.valueOf(FonePayResponseCodes.INVALID_PIN));
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else {
                logger.error("[FonePaySwitchController.verifyOTP] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.Inquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {

                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalActionLog.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.balance Inquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    private WebServiceVO userValidation(WebServiceVO webServiceVO, String validationForCommandId) throws Exception {

        String isValidationRequired = webServiceVO.getReserved1();
        String terminalId = webServiceVO.getTerminalId();
        if(validationForCommandId.equals(CommandFieldConstants.CMD_CASH_OUT_INFO) || validationForCommandId.equals(CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND)){
            if(isValidationRequired != null && isValidationRequired.equals("3")) {
                webServiceVO.setMobileNo(webServiceVO.getAgentMobileNumber());
            }
            else {
                webServiceVO.setMobileNo(webServiceVO.getConsumerMobileNo());
            }
        }
        if (isValidationRequired != null && isValidationRequired.equals("02") && terminalId != null && !terminalId.equals(FonePayConstants.DEBIT_CARD_CHANNEL) && validationForCommandId != null) {
            this.validateOTP(webServiceVO, validationForCommandId);
            return webServiceVO;
        } else if (isValidationRequired != null && isValidationRequired.equals("03") && !terminalId.equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
            this.validateMPIN(webServiceVO);
            return webServiceVO;
        }
        webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        return webServiceVO;
    }

    @Override
    public WebServiceVO miniStatement(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.miniStatement] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        Boolean isAtmChannel = Boolean.FALSE;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            String terminalid = null;
            if (webServiceVO.getTerminalId() != null)
                terminalid = webServiceVO.getTerminalId();
            if (terminalid != null && terminalid.equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                isAtmChannel = Boolean.TRUE;
            } else {
                webServiceVO = this.validateRRN(webServiceVO);
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_MINI_STATMENT);
            if (webServiceVO.getTerminalId().equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                DebitCardModel debitCardModel = getCommonCommandManager().getDebitCardModelDao().getDebitCardModelByCardNumber(webServiceVO.getCardNo());
                webServiceVO = DebitCardUtill.verifyDebitCard(webServiceVO, debitCardModel);
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
                webServiceVO.setMobileNo(debitCardModel.getMobileNo());
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;


            if (!isAtmChannel) {
                this.userValidation(webServiceVO, CommandFieldConstants.CMD_MINISTATEMENT_AGENT);
                if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }
            webServiceVO.setCnicNo(appUserModel.getNic());
            webServiceVO.setPaymentMode("");
            webServiceVO = getFonePayManager().getMiniStatment(webServiceVO);
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9010) {
                FonePayUtils.prepareErrorResponse(webServiceVO, String.valueOf(FonePayResponseCodes.INVALID_PIN));
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else {
                logger.error("[FonePaySwitchController.miniStatement] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.miniStatement] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {

                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            logger.info("[FonePaySwitchController.miniStatement] (In End) Response Code: " + webServiceVO.getResponseCode());
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }

    private boolean checkExpiryDate(String expiry) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        boolean isExpired = false;
        try {
            Date expiryDate = sdf.parse(expiry);
            Date currentDate = new Date();

            if ((expiryDate.getYear() < currentDate.getYear()) && (expiryDate.getMonth() > currentDate.getMonth())) {
                isExpired = true;
            } else if ((expiryDate.getYear() < currentDate.getYear()) && (expiryDate.getMonth() < currentDate.getMonth())) {
                isExpired = true;
            } else if ((expiryDate.getYear() < currentDate.getYear()) && (expiryDate.getMonth() == currentDate.getMonth())) {
                isExpired = true;
            } else if ((expiryDate.getYear() == currentDate.getYear()) && (expiryDate.getMonth() < currentDate.getMonth())) {
                isExpired = true;
            } else {
                isExpired = false;
            }

        } catch (ParseException e) {
            logger.info("[FonePaySwitchController.checkExpiryDate] (In End) Message: " + e.getMessage());
        }
        return isExpired;
    }

    @Override
    public WebServiceVO billPayment(WebServiceVO webServiceVO) {

        String isOtpReq = webServiceVO.getReserved1();
    /*if(null!=webServiceVO.getReserved1() && !"".equals(webServiceVO.getReserved1()))
        isOtpReq=webServiceVO.getReserved1();*/
        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getBillAmount();
        String PID = webServiceVO.getProductID();
        String consumerNo = webServiceVO.getConsumerNo();
        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
        String commissionAmount = webServiceVO.getCommissionAmount();
        String charges = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String terminalId;


        BaseWrapper idWrapper = new BaseWrapperImpl();
        idWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        idWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS_INQUIRY);
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_BILL_PAYMENT);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            }

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            this.userValidation(webServiceVO, CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS_INQUIRY);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            String cmdId = CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS;
            String messageType = "Bill Payment";

            this.logger.info("[FonePay billPayment] [Mobile:" + mobileNo + ", PID:" + PID + ", consumerNO:" + consumerNo + ", Trx Amount:" + trxnAmount + "]");

            bWrapper.putObject("CMOB", mobileNo);
            bWrapper.putObject("TPAM", txProcessingAmount);
            bWrapper.putObject("CAMT", commissionAmount);
            bWrapper.putObject("BAMT", trxnAmount);
            bWrapper.putObject("CSCD", consumerNo);
            bWrapper.putObject("PID", PID);
            bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            charges = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS);
            String transactionAmount = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_AMOUNT_NODEREF);
            String totalAmount = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
            String commision = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
            String transactionCode = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_ID_NODEREF);
            webServiceVO.setResponseContentXML(charges);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
            webServiceVO.setTotalAmount(totalAmount);
            webServiceVO.setTransactionAmount(transactionAmount);
            webServiceVO.setCommissionAmount(commision);
            webServiceVO.setTransactionId(transactionCode);

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());
            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else if (e.getMessage().equals("Your account is debit blocked.")) {
                    webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                } else {
                    logger.error("[FonePaySwitchController.billPayment] Command Exception Error occured:" + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.billPayment] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.billPayment] Error occured: " + e.getMessage(), e);
            if (e.getMessage().equals("Your account is debit blocked.")) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEBIT_BLOCKED);
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CustomerBillPaymentCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO billPaymentInquiry(WebServiceVO webServiceVO) {
        String isValidationRequired = webServiceVO.getReserved1();
            /*if(null!=webServiceVO.getReserved1() && !"".equals(webServiceVO.getReserved1()))
                isOtpReq=webServiceVO.getReserved1();*/
        String paymentType = webServiceVO.getPaymentType();
        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getBillAmount();
        String PID = webServiceVO.getProductID();
        String consumerNo = webServiceVO.getConsumerNo();
        String xml = "";
        BaseWrapper bWrapper = new BaseWrapperImpl();
        String cmdId = CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS_INQUIRY;
        String messageType = "Bill Payment";

        this.logger.info("[FonePay billPaymentInquiry] [Mobile:" + mobileNo + ", PID:" + PID + ", consumerNO:" + consumerNo + ", Trx Amount:" + trxnAmount + "]");

        bWrapper.putObject("CMOB", mobileNo);
        bWrapper.putObject("PMTTYPE", paymentType);
        bWrapper.putObject("BAMT", trxnAmount);
        bWrapper.putObject("CSCD", consumerNo);
        bWrapper.putObject("PID", PID);
        bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
        bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());

        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

//        if(webServiceVO.getBillAmount()==null || webServiceVO.getBillAmount().equals("") || Double.valueOf(webServiceVO.getBillAmount()) <= 0 ){
//            return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
//        }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_BILL_PAYMENT_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS_INQUIRY);

                //Parsing of xml to object
                try {
                    InputStream is = new ByteArrayInputStream(xml.toString().getBytes(StandardCharsets.UTF_8.name()));// BufferedInputStream((InputStream) strBuilder)
                    JAXBContext jaxbContext = JAXBContext.newInstance("com.inov8.microbank.common.util");
                    Unmarshaller jaxbunMarshaller = jaxbContext.createUnmarshaller();
                    CommandResponseXML tc = (CommandResponseXML) jaxbunMarshaller.unmarshal(new BufferedInputStream(is));

                    String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);
                    webServiceVO.setCommissionAmount(charges);

                    if (tc != null && tc.getParamList() != null && tc.getParamList().size() > 0) {
                        Iterator<CommandResponseXML.Param> it = tc.getParamList().iterator();
                        while (it.hasNext()) {
                            CommandResponseXML.Param param = it.next();

                            if (param.getName().equals(CommandFieldConstants.KEY_CUSTOMER_NAME)) {
                                webServiceVO.setProductName(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_TOTAL_AMOUNT)) {
                                webServiceVO.setTotalAmount(param.getValue());
                            } else if (param.getName().equals("AFTER_DUE_DATE")) {
                                webServiceVO.setLateBillAmount(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER)) {
                                webServiceVO.setConsumerNo(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_BILL_AMOUNT)) {
                                webServiceVO.setBillAmount(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_LATE_BILL_AMT)) {
                                webServiceVO.setLateBillAmount(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_BILL_PAID)) {
                                webServiceVO.setBillPaid(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.CMD_AGNETMATE_DUEDATER)) {
                                webServiceVO.setDueDate(param.getValue());
                            } else if (param.getName().equals("ISOVERDUE")) {
                                webServiceVO.setOverDue(param.getValue());
                            }
                        }
                    }
                } catch (JAXBException e) {
                    throw new RuntimeException(e);
                }

                if (isValidationRequired != null && isValidationRequired.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }
        } catch (CommandException e) {
            if (e.getErrorCode() == 131) {
                webServiceVO.setResponseCode(FonePayResponseCodes.BILL_ALREADY_PAID);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else if (e.getErrorCode() == 132) {
                webServiceVO.setResponseCode(FonePayResponseCodes.REFERENCE_NUMBER_BLOCKED);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else if (e.getErrorCode() == 134) {
                webServiceVO.setResponseCode(FonePayResponseCodes.RDV_DOWN);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else if (e.getErrorCode() == 135) {
                webServiceVO.setResponseCode(FonePayResponseCodes.CONSUMER_NUMBER_INVALID);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else {
                this.logger.error("[FonePaySwitchController.billPaymentInquiry] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (WorkFlowException wfe) {
            webServiceVO.setResponseCode(wfe.getErrorCode());
            webServiceVO.setResponseCodeDescription(wfe.getMessage());
        } catch (Exception ex) {
            this.logger.error("[FonePaySwitchController.billPaymentInquiry] Error occured: " + ex.getMessage(), ex);
            if (ex instanceof NumberFormatException) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PRODUCT_NOT_FOUND.toString());
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            }
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CustomerBillPaymentInfoCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO cashIn(WebServiceVO webServiceVO) {
        // TODO Auto-generated method stub
        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getTransactionAmount();
        String consumerNo = webServiceVO.getConsumerNo();
        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
        String commissionAmount = webServiceVO.getCommissionAmount();
        String charges = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L), Long.valueOf(Long.parseLong("121")), webServiceVO.getMobileNo());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            String cmdId = CommandFieldConstants.CMD_CUSTOMER_BILL_PAYMENTS;
            String messageType = "Bill Payment";
            this.logger.info("[FonePay CashIn]");
            bWrapper.putObject("CMOB", mobileNo);
            bWrapper.putObject("TPAM", txProcessingAmount);
            bWrapper.putObject("CAMT", commissionAmount);
            bWrapper.putObject("TAMT", trxnAmount);
            bWrapper.putObject("TXAM", trxnAmount);
            bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.WEB_SERVICE_CASH_IN);
            bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
            bWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            bWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, webServiceVO.getPaymentMode());
            bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME, webServiceVO.getPaymentMode());
            charges = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_WEB_SERVICE_CASH_IN_COMMAND);
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(charges);
            //Save Customer Remitance Info
            HRARemitanceInfoModel remitanceInfoModel = new HRARemitanceInfoModel();
            remitanceInfoModel.setActive(Boolean.TRUE);
            remitanceInfoModel.setAmountCashedIn(Double.parseDouble(trxnAmount));
            remitanceInfoModel.setCreatedOn(new Date());
            remitanceInfoModel.setMobileNo(webServiceVO.getMobileNo());
            remitanceInfoModel.setPaymentMode(webServiceVO.getPaymentMode());
            remitanceInfoModel.setUpdatedOn(new Date());
            remitanceInfoModel.setRrn(webServiceVO.getRetrievalReferenceNumber());
            remitanceInfoModel.setTerminalId(webServiceVO.getTerminalId());
            remitanceInfoModel.setReqDateTime(new Timestamp(System.currentTimeMillis()));
            getCommonCommandManager().getRemitanceInfoDao().saveOrUpdate(remitanceInfoModel);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());
            } else {
                logger.error("[FonePaySwitchController.CashIn] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.CashIn] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CashIn => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO TitleFetch(WebServiceVO webServiceVO) {

        String mobileNo = webServiceVO.getMobileNo();
        String paymentMode = "";
        if (null != webServiceVO.getPaymentMode() && !webServiceVO.getPaymentMode().equals(""))
            paymentMode = webServiceVO.getPaymentMode();
        List customerDetailList;
        Double dailyDebitConsumed = 0.0, monthlyDebitConsumed = 0.0, yearlyDebitConsumed = 0.0;
        Double dailyCreditConsumed = 0.0, monthlyCreditConsumed = 0.0, yearlyCreditConsumed = 0.0;
        Double maxDebitLimit, maxCreditLimit;
        List<String> remainingLimits;
        Long customerAccountType = CustomerAccountTypeConstants.HRA;
        Long paymentModeId = 7L;
        if (paymentMode.equals("")) {
            paymentMode = "0";
            paymentModeId = 3L;
        }

        String charges = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel example = new AppUserModel();
        example.setMobileNo(mobileNo);
        example.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().getAppUserModel(example);
        CustomerModel customerModel = new CustomerModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L), Long.valueOf(Long.parseLong("121")), webServiceVO.getMobileNo());
            if (webServiceVO.getTerminalId().equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                DebitCardModel model = getCommonCommandManager().getDebitCardModelDao().getDebitCardModelByCardNumber(webServiceVO.getCardNo());
                webServiceVO = DebitCardUtill.verifyDebitCard(webServiceVO, model);
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
                webServiceVO.setMobileNo(model.getMobileNo());
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);

            if (appUserModel == null)
                throw new CommandException("No customer exists against this Mobile No.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());

            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
                if (paymentMode.equals("0")) {
                    /*CustomerModel cModel=new CustomerModel();
                    cModel.setCustomerId(appUserModel.getCustomerId());*/
                    customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
                    customerAccountType = customerModel.getCustomerAccountTypeId();
                }

            }
            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            SmartMoneyAccountModel smartMoneyAccountModel = this.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, paymentModeId);
            if (smartMoneyAccountModel == null && !paymentMode.equals("1"))
                throw new CommandException("Account is closed.", 14, ErrorLevel.MEDIUM, new Throwable());
            if (smartMoneyAccountModel == null && paymentMode.equals("1"))
                throw new CommandException("HRA Account does not exist.", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            else if (smartMoneyAccountModel != null && paymentMode.equals("1") && smartMoneyAccountModel.getStatusId() != null
                    && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED)) {
                throw new CommandException("Account is Blocked.", 17, ErrorLevel.MEDIUM, new Throwable());
            } else if (smartMoneyAccountModel != null && paymentMode.equals("1") && smartMoneyAccountModel.getStatusId() != null
                    && smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE)) {
                throw new CommandException("Account is not Active.", 13, ErrorLevel.MEDIUM, new Throwable());
            }
            Long statusId = OlaStatusConstants.ACCOUNT_STATUS_ACTIVE;
            AccountModel accountModel = null;
            accountModel = getCommonCommandManager().getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(), customerAccountType, statusId);
            if (accountModel != null) {

                Long accountId = accountModel.getAccountId();
                Date currentDate = new Date();
                Calendar cal = GregorianCalendar.getInstance();
                Date startDate;
                //daily debit consumed
                dailyDebitConsumed = getCommonCommandManager().getDailyConsumedBalance(accountId, TransactionTypeConstants.DEBIT, currentDate, null);
                //daily credit consumed
                dailyCreditConsumed = getCommonCommandManager().getDailyConsumedBalance(accountId, TransactionTypeConstants.CREDIT, currentDate, null);
                cal.setTime(new Date());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                startDate = cal.getTime();

                monthlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
                //monthly credit consumed
                monthlyCreditConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);
                //yearly debit consumed
                cal.setTime(new Date());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.MONTH, 0);
                startDate = cal.getTime();

                yearlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
                //yearly credit consumed
                yearlyCreditConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);

                remainingLimits = calculateLimits(dailyDebitConsumed, dailyCreditConsumed, monthlyDebitConsumed, monthlyCreditConsumed,
                        yearlyDebitConsumed, yearlyCreditConsumed, customerAccountType);

                webServiceVO.setCnicNo(appUserModel.getNic());
                webServiceVO.setBalance(accountModel.getBalance());
                webServiceVO.setRemainingDebitLimit(remainingLimits.get(1) + "," + remainingLimits.get(3) + "," + remainingLimits.get(5));
                webServiceVO.setRemainingCreditLimit(remainingLimits.get(0) + "," + remainingLimits.get(2) + "," + remainingLimits.get(4));
                webServiceVO.setConsumerName(appUserModel.getFirstName() + " " + appUserModel.getLastName());
                webServiceVO.setAccountTitle(accountModel.getAccountNumber());
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            }


        } catch (CommandException ce) {
            this.logger.error("[FonePaySwitchController.TitleFetch] Error occured: " + ce.getMessage(), ce);
            webServiceVO.setResponseCode(String.valueOf(ce.getErrorCode()));
            webServiceVO.setResponseCodeDescription(ce.getMessage());
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.TitleFetch] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }


        return webServiceVO;
    }

    @Override
    public WebServiceVO cashInAgent(WebServiceVO webServiceVO) {
        String mobileNumber = webServiceVO.getAgentMobileNumber();
        String dateTime = webServiceVO.getDateTime();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String channelID = webServiceVO.getChannelId();
        String terminalId = webServiceVO.getTerminalId();
        String txAmount = webServiceVO.getTransactionAmount();
        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
        String commissionAmount = webServiceVO.getCommissionAmount();
//        String cnic = webServiceVO.getCnicNo();
        String charges = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getAgentMobileNumber(), UserTypeConstantsInterface.RETAILER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

//            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
//            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_AGENT_CASH_IN);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null, 245L, webServiceVO.getAgentMobileNumber());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getAgentMobileNumber(), UserTypeConstantsInterface.RETAILER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
            this.logger.info("[FonePay CashIn]");
            bWrapper.putObject("AMOB", mobileNumber);
            bWrapper.putObject("DATE", dateTime);
            bWrapper.putObject("RRN", rrn);
            bWrapper.putObject("CHANNELID", channelID);
            bWrapper.putObject("TERMINAL_ID", terminalId);
            bWrapper.putObject("TPAM", 0);
            bWrapper.putObject("CAMT", 0);
//            bWrapper.putObject("CNIC", cnic);
            bWrapper.putObject("TAMT", txAmount);
            bWrapper.putObject("TXAM", txAmount);
            bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.AGENT_CASH_DEPOSIT);
            bWrapper.putObject("DTID", DeviceTypeConstantsInterface.ALL_PAY);
            bWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            bWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, webServiceVO.getPaymentMode());
            bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());

            charges = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_AGENT_CASH_DEPOSIT);
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(charges);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED.toString());
            } else {
                logger.error("[FonePaySwitchController.CashIn] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.CashIn] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        }

        this.logger.info("CashIn => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getAgentMobileNumber() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO cashOutInquiry(WebServiceVO webServiceVO) {
        String isOtpReq = "1";
            /*if(null!=webServiceVO.getReserved1() && !"".equals(webServiceVO.getReserved1()))
                isOtpReq=webServiceVO.getReserved1();*/
        String mobileNo = webServiceVO.getMobileNo();
        String dateTime = webServiceVO.getDateTime();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String channelId = webServiceVO.getChannelId();
        String terminalId = webServiceVO.getTerminalId();
        String txAmount = webServiceVO.getTransactionAmount();
//        String PID = webServiceVO.getProductID();
//        String consumerNo = webServiceVO.getConsumerNo();
        String xml = "";
        BaseWrapper bWrapper = new BaseWrapperImpl();
        String cmdId = CommandFieldConstants.CMD_CASH_OUT_INFO;
        String messageType = "Cash Out";

        this.logger.info("[FonePay cashOutInquiry] [Mobile:" + mobileNo + ", Trx Amount:" + txAmount + "]");

        bWrapper.putObject("CMOB", mobileNo);
        bWrapper.putObject("DATE", dateTime);
        bWrapper.putObject("RRN", rrn);
        bWrapper.putObject("CHANNELID", channelId);
        bWrapper.putObject("TERMINALID", terminalId);
        bWrapper.putObject("TXAM", txAmount);
//        bWrapper.putObject("CSCD", consumerNo);
//        bWrapper.putObject("PID", PID);
        bWrapper.putObject("DTID", DeviceTypeConstantsInterface.ALL_PAY);
        bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());

        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_OUT_INFO);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CASH_OUT_INFO);

                if (isOtpReq.equals("1")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }
        } catch (CommandException e) {


            this.logger.error("[FonePaySwitchController.cashOutInquiry] Command Exception occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
            webServiceVO.setResponseCodeDescription(e.getMessage());
        } catch (Exception ex) {
            this.logger.error("[FonePaySwitchController.cashOutInquiry] Error occured: " + ex.getMessage(), ex);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CashWithdrawalCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());


        return webServiceVO;
    }


    @Override
    public WebServiceVO cashOut(WebServiceVO webServiceVO) {
        String isOtpReq = "1";
    /*if(null!=webServiceVO.getReserved1() && !"".equals(webServiceVO.getReserved1()))
        isOtpReq=webServiceVO.getReserved1();*/
        String transactionDateTime = webServiceVO.getTransactionDateTime();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String channelId = webServiceVO.getChannelId();
        String terminalId = webServiceVO.getTerminalId();
        String custMobileNumber = webServiceVO.getMobileNo();
        String agentMobileNumber = webServiceVO.getAgentMobileNumber();
        String txAmount = webServiceVO.getTransactionAmount();
//        String PID = webServiceVO.getProductID();
//        String consumerNo = webServiceVO.getConsumerNo();
        String mpin = webServiceVO.getMobilePin();
        String otp = webServiceVO.getOtpPin();
//        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
//        String commissionAmount = webServiceVO.getCommissionAmount();
        String charges = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();

        BaseWrapper idWrapper = new BaseWrapperImpl();
        idWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        idWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.CMD_CASH_OUT_INFO);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_OUT);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (isOtpReq.equals("1")) {
                getCommandManager().executeCommand(idWrapper, CommandFieldConstants.CMD_OTP_VERIFICATION);
            }
            String cmdId = CommandFieldConstants.CMD_CASH_OUT;
            String messageType = "Cash Out";

            this.logger.info("[FonePay cashOut] [Mobile:" + custMobileNumber + ", Trx Amount:" + txAmount + "]");

            bWrapper.putObject("DATE", transactionDateTime);
            bWrapper.putObject("RRN", rrn);
            bWrapper.putObject("CHANNELID", channelId);
            bWrapper.putObject("TERMINALID", terminalId);
            bWrapper.putObject("CMOB", custMobileNumber);
            bWrapper.putObject("AMOB", agentMobileNumber);
            bWrapper.putObject("MANUAL_OTPIN", mpin);
            bWrapper.putObject("OTPIN", otp);
//            bWrapper.putObject("TPAM", txProcessingAmount);
//            bWrapper.putObject("CAMT", commissionAmount);
            bWrapper.putObject("TAMT", txAmount);
            bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
//            bWrapper.putObject("CSCD", consumerNo);
//            bWrapper.putObject("PID", PID);
            bWrapper.putObject("DTID", DeviceTypeConstantsInterface.ALL_PAY);
            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());

            charges = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CASH_OUT);
            webServiceVO.setResponseContentXML(charges);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.cashout] Command Exception Error occured:" + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.billPayment] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.billPaymentInquiry] Error occured: " + e.getMessage(), e);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("cashout => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());


        return webServiceVO;
    }

    @Override
    public WebServiceVO mpinRegistration(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = null;
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L),
                    Long.valueOf(Long.parseLong(CommandFieldConstants.CMD_MIGRATED_PIN_CHG)),
                    webServiceVO.getMobileNo());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            if (!webServiceVO.getMobilePin().equals(webServiceVO.getConfirmMpin())) {
                webServiceVO.setResponseCode(FonePayResponseCodes.PIN_MISMATCHED);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.PIN_MISMATCHED));
                return webServiceVO;
            }

            /*Boolean testing = Boolean.TRUE;
            if(testing){
                this.wallet2WalletInquiry(webServiceVO);
                if(webServiceVO.getResponseCode().equals("00")){
                    this.wallet2WalletPayment(webServiceVO);
                }
                return webServiceVO;
            }*/
//            this.resetPin(webServiceVO);
//            if (webServiceVO.getResponseCode().equals("00")) {
            BaseWrapper bWrapper = new BaseWrapperImpl();
            this.logger.info("Third Party MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            bWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, webServiceVO.getMobilePin());
            bWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, webServiceVO.getConfirmMpin());
            bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            bWrapper.putObject("IS_FORCEFUL", "1");
            String response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_MIGRATED_PIN_CHG);
            if (MfsWebUtil.isErrorXML(response)) {
                return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
            }
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(response);

        } catch (Exception ex) {
            logger.error("Error Occurred while MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO mpinChange(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = null;
        ActionLogModel actionLogModel = null;
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN);
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null,
                    Long.valueOf(Long.parseLong(CommandFieldConstants.CMD_VERIFLY_PIN_CHANGE)),
                    webServiceVO.getMobileNo());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            if (webServiceVO.getOldMpin().equals(webServiceVO.getMobilePin())) {
                webServiceVO.setResponseCode(FonePayResponseCodes.SAME_PIN);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.SAME_PIN));
                return webServiceVO;
            }

            if (!webServiceVO.getMobilePin().equals(webServiceVO.getConfirmMpin())) {
                webServiceVO.setResponseCode(FonePayResponseCodes.PIN_MISMATCHED);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.PIN_MISMATCHED));
                return webServiceVO;
            }

            this.logger.info("Third Party MPIN Change Request for Mobile # :: " + webServiceVO.getMobileNo());
//            this.resetPin(webServiceVO);
//            if (webServiceVO.getResponseCode().equals("00")) {
            //this.userValidation(webServiceVO,null);
            /*if(webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;*/
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            bWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOldMpin());
            bWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, webServiceVO.getMobilePin());
            bWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, webServiceVO.getConfirmMpin());
            bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            String response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_VERIFLY_PIN_CHANGE);


            if (MfsWebUtil.isErrorXML(response)) {
                return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
            }
            String responseCode = MiniXMLUtil.getTagTextValue(response, "//params/param[@name='CODE']");
            if (responseCode != null && !responseCode.equals("") && !responseCode.equals("0")) {
                webServiceVO.setResponseCode(responseCode);
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_OLD_PIN);
                return webServiceVO;
            } else {
                String names = MessageUtil.getMessage("mpinchange.channel.name");
                List<String> items = Arrays.asList(names.split("\\s*,\\s*"));
                if (items.contains(webServiceVO.getChannelId())) {
                    webServiceVO.setLoginPin(EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, webServiceVO.getMobilePin()));
                    webServiceVO.setReserved2("0");
                    this.loginPin(webServiceVO);
                    if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                        return webServiceVO;
                }
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                webServiceVO.setResponseContentXML(response);


            }
//            }
        } catch (CommandException ex) {
            logger.error("Error Occurred while MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);

        } catch (Exception ex) {
            logger.error("Error Occurred while MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        return webServiceVO;
    }

    private BaseWrapper prepareNadraRequest(AppUserModel appUserModel, WebServiceVO webServiceVO) throws Exception {
        CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject("IS_UPGRADE", "1");
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, appUserModel.getMobileNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, appUserModel.getNic());
        baseWrapper.putObject(CommandFieldConstants.KEY_FINGER_INDEX, webServiceVO.getFingerIndex());
        baseWrapper.putObject(CommandFieldConstants.KEY_FINGER_TEMPLATE, webServiceVO.getFingerTemplate());
        baseWrapper.putObject(CommandFieldConstants.KEY_TEMPLATE_TYPE, webServiceVO.getTemplateType());
        baseWrapper.putObject(CommandFieldConstants.KEY_SENDER_CITY, customerModel.getTaxRegimeIdTaxRegimeModel().getName());
        return baseWrapper;
    }

    private Map<String, Map<String, String>> prepareNADRADataIfRequired(String retXML, String curCmd, String cnic) throws Exception {
        Map<String, Map<String, String>> inputMap = new HashMap<>();
        if (curCmd != null && curCmd.equals("181")) { //Customer NADRA VERIFCATION
            Map<String, String> map = new HashMap<String, String>();
            //Example
            map.put("CDOB", MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.CDOB_NODEREF));
            map.put(CommandFieldConstants.KEY_CNIC_EXPIRY, MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.CNIC_EXP_NODEREF));
            map.put(CommandFieldConstants.KEY_PRESENT_ADDR, MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.PRESENT_ADDR_NODEREF));
            map.put(CommandFieldConstants.KEY_PERMANENT_ADDR, MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.PERMANENT_ADDR_NODEREF));
            map.put("GENDER", MiniXMLUtil.getTagTextValue(retXML, "GENDER"));
            //
            map.put("CNAME", MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.CNAME_NODEREF));
            map.put("PERMANENT_ADDR", MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.PERMANENT_ADDR_NODEREF));
            map.put("MOTHER_MAIDEN", MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.MOTHER_MAIDEN_NODEREF));
            map.put("PRESENT_ADDR", MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.PRESENT_ADDR_NODEREF));
            map.put("BIRTH_PLACE", MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.BIRTH_PLACE_NODEREF));
            inputMap.put(cnic, map);
        }
        return inputMap;
    }

    private WebServiceVO parseXMLForTransactions(String retXML, String curCmd, WebServiceVO webServiceVO) throws Exception {
        if (!MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.SERVICE_CHARGES_NODEREF).equals("")) {
            webServiceVO.setTransactionProcessingAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.SERVICE_CHARGES_NODEREF));
        } else {
            webServiceVO.setTransactionProcessingAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF));
        }
        if (!MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.CAMT_NODEREF).equals("")) {
            webServiceVO.setCommissionAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.CAMT_NODEREF));
        } else {
            webServiceVO.setCommissionAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF));
        }
        if (!MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TAMT_NODEREF).equals("")) {
            webServiceVO.setTotalAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TAMT_NODEREF));
        } else {
            webServiceVO.setTotalAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF));
        }
        webServiceVO.setRecieverAccountTilte(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.RECACCTITLE_NODEREF));
        if (!MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TXAM_NODEREF).equals("")) {
            webServiceVO.setTransactionAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TXAM_NODEREF));
        } else {
            webServiceVO.setTransactionAmount(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TRANS_AMOUNT_NODEREF));
        }
        if (!MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TRANS_ID_NODEREF).equals(""))
            webServiceVO.setTransactionId(MiniXMLUtil.getTagTextValue(retXML, MiniXMLUtil.TRANS_ID_NODEREF));
        return webServiceVO;
    }

    @Override
    public WebServiceVO accountStatusChange(WebServiceVO webServiceVO) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        String isValidationRequired = webServiceVO.getReserved1();
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
//      String acType = webServiceVO.getAccountType();
//      String paymentModeId = webServiceVO.getPaymentMode();
        String accountStatus = webServiceVO.getAccountStatus();
        boolean isLockUnlock = false;
        Long usecaseId = null;
//      Long actionId = null;
        String action = null;

        if (accountStatus.equals("01")) {
            usecaseId = PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID;
            action = "ACTIVE";
        } else if (accountStatus.equals("02")) {
            usecaseId = PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID;
            action = "DE-ACTIVE";
        } else if (accountStatus.equals("03")) {
            usecaseId = PortalConstants.BLOCK_CUSTOMER_USECASE_ID;
            isLockUnlock = true;
            action = "BLOCK";
        } else if (accountStatus.equals("04")) {
            isLockUnlock = true;
            usecaseId = PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID;
            action = "UN-BLOCK";
        } else if (accountStatus.equals("05")) {

            try {
                baseWrapper = new BaseWrapperImpl();
                AppUserModel appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper.putObject("isClosedSettled", false);

                Long paymentModeId = this.getPaymentModeId(String.valueOf(CustomerAccountTypeConstants.LEVEL_0), accountStatus, null, null, null);
                SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel, paymentModeId);
                UserDeviceAccountsModel uda = getCommonCommandManager().getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(appUserModel.getAppUserId());
//              baseWrapper.setBasePersistableModel(smartMoneyAccountModel);

                AccountInfoModel accountInfoModel = null;
                if (smartMoneyAccountModel != null) {
                    accountInfoModel = getCommonCommandManager().getAccountInfoModel(smartMoneyAccountModel.getCustomerId(), smartMoneyAccountModel.getName());
                }
                if (accountInfoModel != null) {
                    accountInfoModel.setActive(Boolean.FALSE);
                    baseWrapper.putObject("accountInfoModel", accountInfoModel);
                }
                CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
                Long customerAccountType = customerModel.getCustomerAccountTypeId();
                Long statusId = OlaStatusConstants.ACCOUNT_STATUS_ACTIVE;
                AccountModel accountModel = null;
                accountModel = getCommonCommandManager().getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(), customerAccountType, statusId);

                if (accountModel != null) {
                    if (accountModel.getAccountHolderId() != null) {
                        BaseWrapper accountHolderWrapper = new BaseWrapperImpl();
                        AccountHolderModel acHolderModel = new AccountHolderModel();
                        acHolderModel.setAccountHolderId(accountModel.getAccountHolderId());
                        accountHolderWrapper.setBasePersistableModel(acHolderModel);
                        accountHolderWrapper = this.accountHolderManager.loadAccountHolder(accountHolderWrapper);

                        AccountHolderModel accountHolderModel = (AccountHolderModel) accountHolderWrapper.getBasePersistableModel();
                        if (accountHolderModel != null) {
                            accountHolderModel.setActive(Boolean.FALSE);
                            baseWrapper.putObject("accountHolderModel", accountHolderModel);

                        }
                    }
                    accountModel.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_CLOSED);
                    baseWrapper.putObject("accountModel", accountModel);

                }
                UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
                userDeviceAccountsModel.setAppUserId(uda.getAppUserId());
                baseWrapper.putObject("smartMoneyAccountModel", smartMoneyAccountModel);
                baseWrapper.putObject("paymentModeId", paymentModeId);
                baseWrapper.putObject("mfsId", uda.getUserId());


//              Boolean isClosedSetteled = (Boolean) baseWrapper.getObject("isClosedSettled");
//              smartMoneyAccountModel.setActive(Boolean.FALSE);
//              smartMoneyAccountDAO.updateSmartMoneyAccountModelToCloseAccount(smartMoneyAccountModel,isClosedSetteled);

                mfsAccountClosureFacade.makeCustomerAccountClosed(baseWrapper);


                baseWrapper.putObject("isClosedSettled", true);

                mfsAccountClosureFacade.makeCustomerAccountClosed(baseWrapper);

                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            } catch (FrameworkCheckedException e) {
                e.printStackTrace();
                if (e.getErrorCode() == 151) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.BAL_NOT_ZERO);
                } else
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return webServiceVO;
        }

        try {
            if (isValidationRequired != null && isValidationRequired.equals("03")) {
                this.validateMPIN(webServiceVO);
                return webServiceVO;
            }

            baseWrapper = new BaseWrapperImpl();
            AppUserModel appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(mobileNumber);
            Long paymentModeId = this.getPaymentModeId(String.valueOf(CustomerAccountTypeConstants.LEVEL_0), accountStatus, null, null, null);
            SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel, paymentModeId);
//            if(smartMoneyAc0countModel == null && (isAgent || isHandler))
//            {
//                paymentModeId = PaymentModeConstantsInterface.CORE_BANKING_ACCOUNT;
//                smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel,paymentModeId);
//            }

            if (action != null && action.equalsIgnoreCase("ACTIVE") && smartMoneyAccountModel == null)
                smartMoneyAccountModel = smartMoneyAccountManager.getInActiveSMA(appUserModel, paymentModeId, OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);

            if (smartMoneyAccountModel != null)
                smartMoneyAccountModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_WARM);

            UserDeviceAccountsModel uda = getCommonCommandManager().getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(appUserModel.getAppUserId());
            baseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            UserDeviceAccountsModel userDeviceAccountsModel = new UserDeviceAccountsModel();
            userDeviceAccountsModel.setAppUserId(uda.getAppUserId());
            CustomerModel customerModel = new CustomerModel();
            customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, usecaseId);
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, 1L);
            baseWrapper.putObject("isLockUnlock", new Boolean(isLockUnlock));
            baseWrapper.putObject("accountStatus", accountStatus);
            baseWrapper.putObject("mfsId", uda.getUserId());
            baseWrapper.putObject("paymentModeId", paymentModeId);
            baseWrapper.putObject("userDeviceAccountsModel", userDeviceAccountsModel);
            baseWrapper.putObject("appUserModel", appUserModel);
            baseWrapper.putObject("acType", customerModel.getCustomerAccountTypeId().toString());
            baseWrapper.putObject("action", action);

            mfsAccountManager.activateDeactivateMfsAccount(baseWrapper);

//            mfsAccountClosureFacade.makeCustomerAccountClosed(baseWrapper);

            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

        } catch (Exception e) {
            e.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        }

        return webServiceVO;

    }

    private Long getPaymentModeId(String acType, String action, Boolean isAgent, Boolean isHandler, Long id) {
        Long paymentModeId = null;
        if (action == null && acType == null && (isAgent || isHandler) && id != null)
            paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
        else if (action != null && acType != null && acType.equals("HRA"))
            paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
        else
            paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

        return paymentModeId;
    }

    @Override
    public WebServiceVO upgradeAccount(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = null;
        ActionLogModel actionLogModel = null;
        CustomerModel customerModel = new CustomerModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Upgrade L1 Account Request");
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null,
                    Long.valueOf(Long.parseLong(CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT)),
                    webServiceVO.getMobileNo());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
            }
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT_INQUIRY);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                String response = null;
                if (!webServiceVO.getReserved2().equals("1")) {
                    BaseWrapper bWrapper = this.prepareNadraRequest(appUserModel, webServiceVO);
                    response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CUSTOMER_NADRA_VERIFICATION);
                    if (MfsWebUtil.isErrorXML(response)) {
                        logger.info("CustomerNadraVerificationCommand response for Mobile # :: " + appUserModel.getMobileNo() + "\n" + response);
                        return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                    }
//            response = "<msg id=\"181\"><params><param name=\"DTID\">8</param><param name=\"CREG_STATE_ID\">null</param><param name=\"CMOB\">03088008099</param>" +
//                                "<param name=\"CNIC\">3430114376879</param><param name=\"BIRTH_PLACE\">حافظ آباد,حافظ آباد</param><param name=\"RESP\">100</param>" +
//                                "<param name=\"CNAME\">شہریار نواز</param><param name=\"MOTHER_MAIDEN\"> پروین</param><param name=\"CNIC_EXP\">2028-01-20</param>" +
//                                "<param name=\"CDOB\">1992-04-13</param><param name=\"CNIC_STATUS\">2028-01-20</param>" +
//                                "<param name=\"PRESENT_ADDR\">\u202Eڈاک\u202A \u202Cخانہ\u202A،\u202A،\u202A \u202Cتحصیل\u202A \u202Cوضلع\u202A \u202Cحافظ\u202A \u202Cآباد\u202C</param>" +
//                                "<param name=\"PERMANENT_ADDR\">\u202Eڈاک\u202A \u202Cخانہ\u202A،\u202Aہ،\u202A \u202Cتحصیل\u202A \u202Cوضلع\u202A \u202Cحافظ\u202A \u202Cآباد\u202C</param>" +
//                                "<param name=\"RTIMAGES\">1</param><param name=\"MUAOR\">1</param><param name=\"IDR\">1</param></params></msg>";
                    Map<String, Map<String, String>> nadraMap = prepareNADRADataIfRequired(response, "181", appUserModel.getNic());
                    //A/C Opening
                    bWrapper = new BaseWrapperImpl();
                    bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
                    bWrapper.putObject("CMOB", appUserModel.getMobileNo());
                    bWrapper.putObject("CNIC", appUserModel.getNic());
                    bWrapper.putObject(CommandFieldConstants.KEY_BIRTH_PLACE, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_BIRTH_PLACE).toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNAME, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CNAME).toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_MOTHER_MAIDEN, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_MOTHER_MAIDEN).toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_CDOB, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CDOB).toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNIC_EXPIRY, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CNIC_EXPIRY).toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_PRESENT_ADDR).toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_PERMANENT_ADDR).toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_ACC_TITLE, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CNAME).toString());
                    bWrapper.putObject("GENDER", nadraMap.get(appUserModel.getNic()).get("GENDER").toString());
                    bWrapper.putObject("IS_CNIC_SEEN", "1");
                    bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, "2510763");
                    bWrapper.putObject("IS_BVS_ACCOUNT", "1");
                    bWrapper.putObject(CommandFieldConstants.KEY_CUST_ACC_TYPE, CustomerAccountTypeConstants.LEVEL_1);
                    response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT);

                } else {
                    BaseWrapper bWrapper = new BaseWrapperImpl();
                    bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
                    bWrapper.putObject("CMOB", appUserModel.getMobileNo());
                    bWrapper.putObject("CNIC", appUserModel.getNic());
                    bWrapper.putObject(CommandFieldConstants.KEY_BIRTH_PLACE, customerModel.getBirthPlace());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNAME, customerModel.getName());
                    bWrapper.putObject(CommandFieldConstants.KEY_MOTHER_MAIDEN, appUserModel.getMotherMaidenName());
                    bWrapper.putObject(CommandFieldConstants.KEY_CDOB, appUserModel.getDob());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNIC_EXPIRY, appUserModel.getNicExpiryDate());
                    bWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, appUserModel.getAddress1());
                    bWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, appUserModel.getAddress1());
                    bWrapper.putObject(CommandFieldConstants.KEY_ACC_TITLE, customerModel.getName());
                    bWrapper.putObject("GENDER", customerModel.getGender());
                    bWrapper.putObject("IS_CNIC_SEEN", "1");
                    bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, "2510763");
                    bWrapper.putObject("IS_BVS_ACCOUNT", "1");
                    bWrapper.putObject(CommandFieldConstants.KEY_CUST_ACC_TYPE, CustomerAccountTypeConstants.LEVEL_1);
                    response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT);

                }
                logger.info("OpenCustomerL0AccountCommand response for Mobile #:: " + appUserModel.getMobileNo() + "\n" + response);
                if (MfsWebUtil.isErrorXML(response)) {
                    return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                }

                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription("Account Upgraded Successfully");
                webServiceVO.setResponseContentXML(response);
            }
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else {
                logger.error("[FonePaySwitchController.upgradeAccount] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            logger.error("Error Occurred while Upgrade Account for Mobile # :: " + webServiceVO.getMobileNo());
            logger.error("[FonePaySwitchController.upgradeAccount] Error occured: " + ex.getMessage(), ex);
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO walletToWalletPaymentInquiry(WebServiceVO webServiceVO) {
        return this.wallet2WalletInquiry(webServiceVO);
    }

    @Override
    public WebServiceVO walletToWalletPayment(WebServiceVO webServiceVO) {
        return wallet2WalletPayment(webServiceVO);
    }

    private WebServiceVO updateAccountStatus(WebServiceVO webServiceVO) throws Exception {
        return webServiceVO;
    }

    private WebServiceVO upgradeAcInquiry(WebServiceVO webServiceVO) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        String pinParam = webServiceVO.getReserved1();
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject("IS_PIN_VERIFY", pinParam);
        baseWrapper.putObject("IS_RECEIVE_CASH", "0");
        baseWrapper.putObject("IS_HRA", "0");
        baseWrapper.putObject("IS_UPGRADE", "1");
        baseWrapper.putObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED, webServiceVO.getReserved1());
        String response = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT_INQUIRY);
        if (MfsWebUtil.isErrorXML(response)) {
            return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
        }
        webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
        return webServiceVO;
    }

    @Override
    public WebServiceVO upgradeAccountInquiry(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = null;
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                upgradeAcInquiry(webServiceVO);
            }
        } catch (Exception ex) {
            logger.error("Error while Performing Upgrade Account Inquiry for Mobile # :: " + webServiceVO.getMobileNo()
                    + "\n" + ex.getMessage());
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO cashInInquiry(WebServiceVO webServiceVO) {
        // TODO Auto-generated method stub
        logger.info("[FonePaySwitchController.miniStatement] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) < 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN_INFO);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            webServiceVO.setCnicNo(appUserModel.getNic());
            //cnicNo=appUserModel.getNic();
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            }
            webServiceVO = getFonePayManager().makeCashIninquiry(webServiceVO);


        } catch (CommandException e) {
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.miniStatement] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.verifyAccount] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.miniStatement] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    private List<String> calculateLimits(Double dailyDebitConsumed, Double dailyCreditConsumed, Double monthlyDebitConsumed, Double monthlyCreditConsumed,
                                         Double yearlyDebitConsumed, Double yearlyCreditConsumed, Long customerAccountTypeId) throws FrameworkCheckedException {
        NumberFormat formatter = new DecimalFormat("#0.00");
        List<LimitModel> limits = getCommonCommandManager().getLimitsByCustomerAccountType(customerAccountTypeId);
        List<String> remainingLimits = new ArrayList<>();

        Double remainingDailyCreditLimit = 0.0;
        Double remainingDailyDebitLimit = 0.0;
        Double remainingMonthlyCreditLimit = 0.0;
        Double remainingMonthlyDebitLimit = 0.0;
        Double remainingYearlyCreditLimit = 0.0;
        Double remainingYearlyDebitLimit = 0.0;

        for (LimitModel limitModel : limits) {
            if (limitModel.getLimitTypeId().equals(LimitTypeConstants.DAILY)) {
                if (limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)) {
                    remainingDailyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyCreditConsumed;
                    remainingDailyCreditLimit = remainingDailyCreditLimit < 0 ? 0 : remainingDailyCreditLimit;
                } else {
                    remainingDailyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyDebitConsumed;
                    remainingDailyDebitLimit = (remainingDailyDebitLimit < 0 ? 0 : remainingDailyDebitLimit);
                }
            } else if (limitModel.getLimitTypeId().equals(LimitTypeConstants.MONTHLY)) {
                if (limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)) {
                    remainingMonthlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyCreditConsumed;
                    remainingMonthlyCreditLimit = remainingMonthlyCreditLimit < 0 ? 0 : remainingMonthlyCreditLimit;
                } else {
                    remainingMonthlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyDebitConsumed;
                    remainingMonthlyDebitLimit = remainingMonthlyDebitLimit < 0 ? 0 : remainingMonthlyDebitLimit;
                }
            } else if (limitModel.getLimitTypeId().equals(LimitTypeConstants.YEARLY)) {
                if (limitModel.getTransactionTypeId().equals(TransactionTypeConstants.CREDIT)) {
                    remainingYearlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyCreditConsumed;
                    remainingYearlyCreditLimit = remainingYearlyCreditLimit < 0 ? 0 : remainingYearlyCreditLimit;
                } else {
                    remainingYearlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyDebitConsumed;
                    remainingYearlyDebitLimit = remainingYearlyDebitLimit < 0 ? 0 : remainingYearlyDebitLimit;
                }
            }
        }
       /* if(remainingDailyDebitLimit<remainingMonthlyDebitLimit && remainingDailyDebitLimit<remainingYearlyDebitLimit){
            remainingLimits.add(formatter.format(remainingDailyDebitLimit));
        }else if(remainingMonthlyDebitLimit<remainingDailyDebitLimit && remainingMonthlyDebitLimit<remainingYearlyDebitLimit){
            remainingLimits.add(formatter.format(remainingMonthlyDebitLimit));
        }else{
            remainingLimits.add(formatter.format(remainingYearlyDebitLimit));
        }

        if(remainingDailyCreditLimit<remainingMonthlyCreditLimit && remainingDailyCreditLimit<remainingYearlyCreditLimit){
            remainingLimits.add(formatter.format(remainingDailyCreditLimit));
        }else if(remainingMonthlyCreditLimit<remainingDailyCreditLimit && remainingMonthlyCreditLimit<remainingYearlyCreditLimit){
            remainingLimits.add(formatter.format(remainingMonthlyCreditLimit));
        }else{
            remainingLimits.add(formatter.format(remainingYearlyCreditLimit));
        }*/
        remainingLimits.add(formatter.format(remainingDailyCreditLimit));
        remainingLimits.add(formatter.format(remainingDailyDebitLimit));
        remainingLimits.add(formatter.format(remainingMonthlyCreditLimit));
        remainingLimits.add(formatter.format(remainingMonthlyDebitLimit));
        remainingLimits.add(formatter.format(remainingYearlyCreditLimit));
        remainingLimits.add(formatter.format(remainingYearlyDebitLimit));
        return remainingLimits;
    }


    private List<String> calculateBlinkCustomerLimits(Double dailyDebitConsumed, Double dailyCreditConsumed, Double monthlyDebitConsumed, Double monthlyCreditConsumed,
                                                      Double yearlyDebitConsumed, Double yearlyCreditConsumed, Long customerAccountTypeId, Long customerId) throws FrameworkCheckedException {
        NumberFormat formatter = new DecimalFormat("#0.00");
        List<BlinkCustomerLimitModel> limits = limitManager.getBlinkCustomerLimitByTransactionTypeByCustomerId(customerId);
        List<String> remainingLimits = new ArrayList<>();

        Double remainingDailyCreditLimit = 0.0;
        Double remainingDailyDebitLimit = 0.0;
        Double remainingMonthlyCreditLimit = 0.0;
        Double remainingMonthlyDebitLimit = 0.0;
        Double remainingYearlyCreditLimit = 0.0;
        Double remainingYearlyDebitLimit = 0.0;

        for (BlinkCustomerLimitModel limitModel : limits) {
            if (limitModel.getLimitType().equals(LimitTypeConstants.DAILY)) {
                if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                    remainingDailyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyCreditConsumed;
                    remainingDailyCreditLimit = remainingDailyCreditLimit < 0 ? 0 : remainingDailyCreditLimit;
                } else {
                    remainingDailyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - dailyDebitConsumed;
                    remainingDailyDebitLimit = (remainingDailyDebitLimit < 0 ? 0 : remainingDailyDebitLimit);
                }
            } else if (limitModel.getLimitType().equals(LimitTypeConstants.MONTHLY)) {
                if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                    remainingMonthlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyCreditConsumed;
                    remainingMonthlyCreditLimit = remainingMonthlyCreditLimit < 0 ? 0 : remainingMonthlyCreditLimit;
                } else {
                    remainingMonthlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - monthlyDebitConsumed;
                    remainingMonthlyDebitLimit = remainingMonthlyDebitLimit < 0 ? 0 : remainingMonthlyDebitLimit;
                }
            } else if (limitModel.getLimitType().equals(LimitTypeConstants.YEARLY)) {
                if (limitModel.getTransactionType().equals(TransactionTypeConstants.CREDIT)) {
                    remainingYearlyCreditLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyCreditConsumed;
                    remainingYearlyCreditLimit = remainingYearlyCreditLimit < 0 ? 0 : remainingYearlyCreditLimit;
                } else {
                    remainingYearlyDebitLimit = (limitModel.getMaximum() == null ? 0.0 : limitModel.getMaximum()) - yearlyDebitConsumed;
                    remainingYearlyDebitLimit = remainingYearlyDebitLimit < 0 ? 0 : remainingYearlyDebitLimit;
                }
            }
        }

        remainingLimits.add(formatter.format(remainingDailyCreditLimit));
        remainingLimits.add(formatter.format(remainingDailyDebitLimit));
        remainingLimits.add(formatter.format(remainingMonthlyCreditLimit));
        remainingLimits.add(formatter.format(remainingMonthlyDebitLimit));
        remainingLimits.add(formatter.format(remainingYearlyCreditLimit));
        remainingLimits.add(formatter.format(remainingYearlyDebitLimit));
        return remainingLimits;
    }


    private WebServiceVO wallet2WalletInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.wallet2WalletInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        String isOtpReq = webServiceVO.getReserved1();
        AppUserModel appUserModel = null;
        String messageType = "Wallet To Wallet Transaction";
        String cmdId = CommandFieldConstants.KEY_CMD_WALLET2WALLETINFO;

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTotalAmount() == null || Double.valueOf(webServiceVO.getTotalAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.WALLET_2_WALLET_INFO);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_ACT_CI);
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTotalAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTotalAmount());
            String response = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2WALLETINFO);
            if (MfsWebUtil.isErrorXML(response)) {
                return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
            }
            logger.info("For Mobile # :: " + webServiceVO.getMobileNo() + " Wallet_To_Wallet Inquiry Response ::\n" + response);
            this.parseXMLForTransactions(response, CommandFieldConstants.KEY_CMD_WALLET2WALLETINFO.toString(), webServiceVO);
          /*if(webServiceVO.getOtpPurpose() != null && webServiceVO.getOtpPurpose().equals("03")){
              this.generateOTP(webServiceVO);
          }*/
            if (isOtpReq != null && isOtpReq.equals("02")) {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                logger.info("The plain otp is " + otp);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                webServiceVO.setOtpPin(otp);
                getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
            }

            String recieverTitle = MiniXMLUtil.getTagTextValue(response, MiniXMLUtil.RECACCTITLE_NODEREF);
            webServiceVO.setRecieverAccountTilte(recieverTitle);
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(response);
        } catch (Exception ex) {
            if (ex.getMessage().equals("Your account is debit blocked.")) {
                webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            } else {
                logger.error("Error while Performing Wallet_To_Wallet Inquiry for Mobile # :: " + webServiceVO.getMobileNo()
                        + "\n" + ex.getMessage());
                webServiceVO.setResponseCodeDescription(ex.getMessage());
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        return webServiceVO;
    }

    private WebServiceVO wallet2WalletPayment(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.wallet2WalletPayment] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = null;
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTotalAmount() == null || Double.valueOf(webServiceVO.getTotalAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.WALLET_2_WALLET);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            // Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            this.userValidation(webServiceVO, CommandFieldConstants.KEY_CMD_WALLET2WALLETINFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            this.checkBalance(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_ACT_CI);
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTotalAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTotalAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_CAMT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            String response = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2WALLETCMD);
            if (MfsWebUtil.isErrorXML(response)) {
                return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
            }
            logger.info("For Mobile # :: " + webServiceVO.getMobileNo() + " Wallet_To_Wallet Inquiry Response ::\n" + response);
            this.parseXMLForTransactions(response, CommandFieldConstants.KEY_CMD_WALLET2WALLETINFO.toString(), webServiceVO);
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(response);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8090) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.RECIPIENT_MOBILE_NUMBER.toString());
            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());
            } else if (e.getErrorCode() == 9024) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.BB_WALLET_NOT_REGISTERED.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.wallet2WalletPayment] Command Exception Error occured:" + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.wallet2WalletPayment] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Error while Performing Wallet_To_Wallet Payment for Mobile # :: " + webServiceVO.getMobileNo()
                    + "\n" + ex.getMessage());
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        return webServiceVO;
    }

    private WebServiceVO checkBalance(WebServiceVO webServiceVO) {

        String trxAmount = webServiceVO.getTotalAmount();
        String mobileNo = webServiceVO.getMobileNo();
        AppUserModel example = new AppUserModel();
        example.setMobileNo(mobileNo);
        example.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().getAppUserModel(example);

        Long customerId = null;
        if (appUserModel.getCustomerId() != null)
            customerId = appUserModel.getCustomerId();
        else
            customerId = appUserModel.getAppUserId();

        try {
            SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
            sma.setCustomerId(appUserModel.getCustomerId());
            sma.setActive(true);
            sma.setAccountClosedUnsetteled(0L);
            SmartMoneyAccountModel sma1 = getCommonCommandManager().getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);

            AccountInfoModel model = getCommonCommandManager().getAccountInfoModel(customerId, sma1.getName());
            Double accountBalance = Double.valueOf(getCommonCommandManager().getAccountBalance(model, sma1));

            if (accountBalance < Double.valueOf(trxAmount)) {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.LOW_BALANCE);
                return webServiceVO;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO ibftTitleFetch(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.ibftTitleFetch] Start:: ");
        String isValidationRequired = webServiceVO.getReserved1();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "IBFT Title Fetch";
        String cmdId = CommandFieldConstants.KEY_CMD_BB2COREINFO;

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, CommandFieldConstants.KEY_CMD_BB2COREINFO);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getSenderMobileNumber(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CUSTOMER_BB_TO_IBFT);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getSenderMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_CC_FROM_BANK_IMD, "603733");
            baseWrapper.putObject(CommandFieldConstants.KEY_CC_TO_BANK_IMD, webServiceVO.getDestinationBankImd());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getDestinationAccountNumber());//destinationaccountnumber
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());//transactionamount
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BB2COREINFO);

            String COREACTL = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CORE_ACT_TITLE_NODEREF);
            String beneBankName = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BENE_BANK_NAME_NODEREF);
            String beneBranchName = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BENE_BRANCH_NAME_NODEREF);
            String beneIBAN = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BENE_IBAN_NODEREF);
            String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);

            if (isValidationRequired != null && isValidationRequired.equals("02")) {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                logger.info("The plain otp is " + otp);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getSenderMobileNumber(), webServiceVO.getChannelId(), cmdId);
                webServiceVO.setOtpPin(otp);
                getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getSenderMobileNumber());
            }
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setSenderMobileNumber(webServiceVO.getSenderMobileNumber());
            webServiceVO.setSenderAccountTitle(appUserModel.getFirstName() + " " + appUserModel.getLastName());
            webServiceVO.setRecieverAccountTilte(COREACTL);
            webServiceVO.setRecieverAccountNumber(webServiceVO.getDestinationAccountNumber());
            webServiceVO.setTotalAmount(webServiceVO.getTransactionAmount());
            webServiceVO.setCharges(charges);
            webServiceVO.setBenificieryIban(beneIBAN);
            webServiceVO.setBankName(beneBankName);
            webServiceVO.setBranchName(beneBranchName);
            webServiceVO.setSourceBankImd("603733");
            webServiceVO.setDestinationBankImd(webServiceVO.getDestinationBankImd());
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");

        }
//        catch (CommandException e) {
//            e.printStackTrace();
//            this.logger.error("[FonePaySwitchController.ibftTitleFetch] Command Exception occured: " + e.getMessage(), e);
//            webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//        }
        catch (CommandException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 9051) {
                webServiceVO.setResponseCode("20");
                webServiceVO.setResponseCodeDescription("Transaction Timeout. Please try again");
            } else if (e.getErrorCode() == 9053) {
                webServiceVO.setResponseCode("154");
                webServiceVO.setResponseCodeDescription("The entered Account# is invalid. Please retry with a valid Account#.");
            } else if (e.getMessage().equals("Your account is debit blocked.")) {
                webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex.getMessage().equals("Your account is debit blocked.")) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEBIT_BLOCKED);
            } else {
                this.logger.error("[FonePaySwitchController.ibftTitleFetch] Error occured: " + ex.getMessage(), ex);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            }
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CustomerBBToCoreInfoCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO ibftAdvice(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.ibftAdvice] Start:: ");
        String isValidationRequired = webServiceVO.getReserved1();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getSenderMobileNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.KEY_CMD_BB2COREINFO);
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, CommandFieldConstants.KEY_CMD_BBTOCORECMD);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getSenderMobileNumber(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.KEY_CMD_BB2COREINFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CUSTOMER_BB_TO_IBFT);
            baseWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getMobilePin());
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getSenderMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_CC_FROM_BANK_IMD, "603733");
            baseWrapper.putObject(CommandFieldConstants.KEY_CC_TO_BANK_IMD, webServiceVO.getDestinationBankImd());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getDestinationAccountNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
//            baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE,webServiceVO.getPurposeOfPayment());
            baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE, webServiceVO.getPurposeOfPayment());
            baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_SENDER, webServiceVO.getSenderAccountTitle());
            baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, webServiceVO.getRecieverAccountTilte());
            baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_BANK_NAME, webServiceVO.getBankName());
            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_BRANCH_NAME, webServiceVO.getBranchName());
            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_IBAN, webServiceVO.getBenificieryIban());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTransactionAmount());
            baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());

            xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BBTOCORECMD);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
            webServiceVO.setDestinationBankImd(webServiceVO.getDestinationBankImd());
            webServiceVO.setDestinationAccountNumber(webServiceVO.getDestinationAccountNumber());
            String transactionCode = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
            webServiceVO.setTransactionId(transactionCode);
        } catch (CommandException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8090) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.RECIPIENT_MOBILE_NUMBER.toString());
            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8059) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE.toString());
            } else if (e.getErrorCode() == 9045) {
                this.logger.error("[FonePaySwitchController.ibftAdvice] Command Exception occured: " + e.getMessage(), e);
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MEMBER_BANK_NOT_FOUND.toString());
            } else if (e.getErrorCode() == 9052) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.TRXN_AMOUNT_GREATER_THAN_LIMIT.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    this.logger.error("[FonePaySwitchController.ibftAdvice] Command Exception occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                this.logger.error("[FonePaySwitchController.ibftAdvice] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.ibftAdvice] Error occured: " + ex.getMessage(), ex);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CustomerBBToCoreCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO challanPayment(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.challanPayment] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_CHALLAN_PAYMENT_INFO;
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "Challan Payment";
        Long appUserTypeId = 2L;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay challanPayment] [Mobile:" + mobileNo + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, webServiceVO.getChallanNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_BILL_AMOUNT, webServiceVO.getBillAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, webServiceVO.getCommissionAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTotalAmount());

        ProductModel productModel = new ProductModel();
        productModel.setProductCode(webServiceVO.getProductCode());
        baseWrapper.setBasePersistableModel(productModel);
        try {
            productModel = getCommonCommandManager().loadProductByProductCodeAndAppUserTypeId(webServiceVO.getProductCode(), appUserTypeId);
            if (productModel != null) {
                baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productModel.getProductId());

            } else {
                throw new CommandException("No Product Found", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        } catch (FrameworkCheckedException e) {
            logger.error("[FonePaySwitchController.ChallanPament] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
            return webServiceVO;
        }

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CHALLAN_PAYMENT);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_CHALLAN_PAYMENT_INFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CHALLAN_PAYMENT);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CHALLAN_PAYMENT);
                String challanAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_BILL_AMOUNT_NODEREF);
                String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                String dueDate = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.DUEDATEF_NODEREF);
                String transactionCode = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setTransactionId(transactionCode);
                webServiceVO.setChallanAmount(challanAmount);
                webServiceVO.setTotalAmount(totalAmount);
                webServiceVO.setTransactionDateTime(dueDate);


            }

        } catch (CommandException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8090) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.RECIPIENT_MOBILE_NUMBER.toString());
            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8059) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE.toString());
            } else if (e.getErrorCode() == 9045) {
                this.logger.error("[FonePaySwitchController.ibftAdvice] Command Exception occured: " + e.getMessage(), e);
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MEMBER_BANK_NOT_FOUND.toString());
            } else if (e.getErrorCode() == 9052) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.TRXN_AMOUNT_GREATER_THAN_LIMIT.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    this.logger.error("[FonePaySwitchController.ChallanPayment] Command Exception occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                this.logger.error("[FonePaySwitchController.ChallanPayment] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.ChallanPayment] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.ChallanPayment] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.ChallanPayment] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;


    }

    @Override
    public WebServiceVO challanPaymentInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.challanPaymentInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_CHALLAN_PAYMENT_INFO;
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "Challan Payment Issuance";
        Long appUserTypeId = 2L;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay challanPaymentInquiry] [Mobile:" + mobileNo + ", Cnic:" + cnic + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, webServiceVO.getChallanNumber());

        ProductModel productModel = new ProductModel();
        productModel.setProductCode(webServiceVO.getProductCode());
        baseWrapper.setBasePersistableModel(productModel);
        try {
            productModel = getCommonCommandManager().loadProductByProductCodeAndAppUserTypeId(webServiceVO.getProductCode(), appUserTypeId);
            if (productModel != null) {
                baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productModel.getProductId());

            } else {
                throw new CommandException("No Product Found", ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
            }
        } catch (FrameworkCheckedException e) {
            logger.error("[FonePaySwitchController.ChallanPamentInquiry] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
            return webServiceVO;

        }

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CHALLAN_PAYMENT_INQUIRY);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CHALLAN_PAYMENT_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CHALLAN_PAYMENT_INFO);
                String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);

                String status = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BILL_PAID_STATUS_NODEREF);
                String billPaid = (status.equalsIgnoreCase("0") ? "U" : "P");
                String challanAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BILL_AMOUNT_NODEREF);
                String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMTF_NODEREF);
                String dueDate = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.DUEDATEF_NODEREF);
                String lateBillAount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.LATE_BILL_AMT_NODEREF);
                if (isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setChallanAmount(challanAmount);
                webServiceVO.setDueDate(dueDate);
                webServiceVO.setLateBillAmount(lateBillAount);
                webServiceVO.setCharges(charges);
                webServiceVO.setTotalAmount(totalAmount);
                webServiceVO.setStatus(billPaid);

            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.challanPaymentInquiry] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.challanPaymentInquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.challanPaymentInquiry] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.challanPaymentInquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;

    }

    @Override
    public WebServiceVO walletToCnic(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.walletToCnic] Start:: ");
        String isValidationRequired = webServiceVO.getPinType();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "Wallet To CNIC";
        String cmdId = CommandFieldConstants.KEY_CMD_WALLET2CNICCMD;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.WALLET_2_CNIC);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            this.userValidation(webServiceVO, CommandFieldConstants.KEY_CMD_WALLET2CNICINFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_CASH_CI);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
            baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_R_W_CNIC, webServiceVO.getReceiverCNIC());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTransactionAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE, webServiceVO.getPurposeOfPayment());

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2CNICCMD);

                String trxId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
                String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
                String trxAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);

                webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                webServiceVO.setDateTime(webServiceVO.getDateTime());
                webServiceVO.setTransactionId(trxId);
                webServiceVO.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
//            webServiceVO.setReceiverCnic(webServiceVO.getReceiverCnic());
                webServiceVO.setTransactionAmount(webServiceVO.getTransactionAmount());
                webServiceVO.setCommissionAmount(charges);
                webServiceVO.setTotalAmount(trxAmount);
            }
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    this.logger.error("[FonePaySwitchController.walletToCnic] Command Exception occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                this.logger.error("[FonePaySwitchController.walletToCnic] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.walletToCnic] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("walletToCnic => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }


    @Override
    public WebServiceVO walletToCnicInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.walletToCnicInquiry] Start:: ");
        String isValidationRequired = webServiceVO.getPinType();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "Wallet To CNIC Inquiry";
        String cmdId = CommandFieldConstants.KEY_CMD_WALLET2CNICINFO;

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.WALLET_2_CNIC_INFO);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.ACT_TO_CASH_CI);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
            baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_R_W_CNIC, webServiceVO.getReceiverCNIC());
            baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, webServiceVO.getTransactionAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE, webServiceVO.getPurposeOfPayment());

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_WALLET2CNICINFO);

                String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);
                String trxAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMTF_NODEREF);

                if (isValidationRequired != null && isValidationRequired.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }

                webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                webServiceVO.setDateTime(webServiceVO.getDateTime());
                webServiceVO.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
                webServiceVO.setReceiverCNIC(webServiceVO.getReceiverCNIC());
                webServiceVO.setTransactionAmount(webServiceVO.getTransactionAmount());
                webServiceVO.setCommissionAmount(charges);
                webServiceVO.setTotalAmount(trxAmount);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (ex.getMessage().equals("Your account is debit blocked.")) {
                webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            } else {
                this.logger.error("[FonePaySwitchController.walletToCnicInquiry] Error occured: " + ex.getMessage(), ex);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            }
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("walletToCnicInquiry => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO hraToWallet(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.hraToWallet] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String PID = ProductConstantsInterface.HRA_TO_WALLET_TRANSACTION.toString();
        String cmdId = CommandFieldConstants.CMD_HRA_TO_WALLET;
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "HRA To Wallet";
        Long appUserTypeId = 2L;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay hraToWallet] [Mobile:" + mobileNo + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, webServiceVO.getTransactionAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_CAMT, "0");
        baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
        baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTransactionAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
        baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, PID);


        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_TO_WALLET);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_HRA_TO_WALLET_INQUIRY);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_TO_WALLET);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_HRA_TO_WALLET);
                String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_AMOUNT_NODEREF);
                String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
                String transactionCode = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setTransactionId(transactionCode);
                webServiceVO.setTransactionAmount(transactionAmount);
                webServiceVO.setTotalAmount(totalAmount);
                webServiceVO.setCommissionAmount(charges);


            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.HraToWallet] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.HraToWallet] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.HraToWallet] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.HraToWallet] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO hraToWalletInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.hraToWalletInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String PID = ProductConstantsInterface.HRA_TO_WALLET_TRANSACTION.toString();
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_HRA_TO_WALLET_INQUIRY;
        String messageType = "HRA TO WALLET";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay hraToWalletInquiry] [Mobile:" + mobileNo + ", PID:" + PID + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, PID);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_TO_WALLET_INQUIRY);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_TO_WALLET_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_HRA_TO_WALLET_INQUIRY);
                String transactionProcessingAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);
                String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TXAMF_NODEREF);
                String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMTF_NODEREF);


                if (isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setTransactionAmount(transactionAmount);
                webServiceVO.setCommissionAmount(transactionProcessingAmount);
                webServiceVO.setTotalAmount(totalAmount);

            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.hraToWalletInquiry] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.hraToWalletInquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.hraToWalletInquiry] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.hraToWalletInquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;


    }

    @Override
    public WebServiceVO walletToCore(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.walletToCore] Start:: ");
        String isValidationRequired = webServiceVO.getPinType();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "Wallet To Core";
        String cmdId = CommandFieldConstants.KEY_CMD_BBTOCORECMD;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getSenderMobileNumber());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.WALLET_2_CORE);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getSenderMobileNumber(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.KEY_CMD_BB2COREINFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getSenderMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
            baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getRecieverAccountNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, webServiceVO.getTransactionAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTransactionAmount());

//            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
//            if ("00".equals(webServiceVO.getResponseCode())) {
            xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BBTOCORECMD);

            String trxId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
            String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
            String receiverAccountTitle = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CORE_ACT_TITLE_NODEREF);
            String trxAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);

            webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setDateTime(webServiceVO.getDateTime());
            webServiceVO.setTransactionId(trxId);
            webServiceVO.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
            webServiceVO.setRecieverAccountNumber(webServiceVO.getRecieverAccountNumber());
            webServiceVO.setRecieverAccountTilte(receiverAccountTitle);
            webServiceVO.setTransactionAmount(webServiceVO.getTransactionAmount());
            webServiceVO.setCommissionAmount(charges);
            webServiceVO.setTotalAmount(trxAmount);

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    this.logger.error("[FonePaySwitchController.walletToCore] Command Exception occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                this.logger.error("[FonePaySwitchController.walletToCore] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.walletToCore] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("walletToCore => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO walletToCoreInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.walletToCoreInquiry] Start:: ");
        String isValidationRequired = webServiceVO.getPinType();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "Wallet To Core Inquiry";
        String cmdId = CommandFieldConstants.KEY_CMD_BB2COREINFO;

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.WALLET_2_CORE);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CUSTOMER_BB_TO_CORE_ACCOUNT);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
            baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getRecieverAccountNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, webServiceVO.getTransactionAmount());

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {

                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BB2COREINFO);

                String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);
                String trxAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMTF_NODEREF);
                String receiverAccountTitle = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CORE_ACT_TITLE_NODEREF);

                if (isValidationRequired != null && isValidationRequired.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }

                webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                webServiceVO.setDateTime(webServiceVO.getDateTime());
                webServiceVO.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
                webServiceVO.setRecieverAccountNumber(webServiceVO.getRecieverAccountNumber());
                webServiceVO.setRecieverAccountTilte(receiverAccountTitle);
                webServiceVO.setTransactionAmount(webServiceVO.getTransactionAmount());
                webServiceVO.setCommissionAmount(charges);
                webServiceVO.setTotalAmount(trxAmount);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.walletToCoreInquiry] Error occured: " + ex.getMessage(), ex);
            if (ex.getMessage().equals("Your account is debit blocked.")) {
                webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            }
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("walletToCoreInquiry => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }


    @Override
    public WebServiceVO fundWalletToCoreInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.FundwalletToCoreInquiry] Start:: ");
        String isValidationRequired = webServiceVO.getPinType();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "Fund Wallet To Core Inquiry";
        String cmdId = CommandFieldConstants.KEY_CMD_BB2COREINFO;

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.FUND_WALLET_2_CORE_INFO);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.RELIEF_FUND_PRODUCT);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            baseWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
            baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getRecieverAccountNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, webServiceVO.getTransactionAmount());

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {

                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BB2COREINFO);

                String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);
                String trxAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMTF_NODEREF);
                String receiverAccountTitle = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CORE_ACT_TITLE_NODEREF);

                if (isValidationRequired != null && isValidationRequired.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }

                webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                webServiceVO.setDateTime(webServiceVO.getDateTime());
                webServiceVO.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
                webServiceVO.setRecieverAccountNumber(webServiceVO.getRecieverAccountNumber());
                webServiceVO.setRecieverAccountTilte(receiverAccountTitle);
                webServiceVO.setTransactionAmount(webServiceVO.getTransactionAmount());
                webServiceVO.setCommissionAmount(charges);
                webServiceVO.setTotalAmount(trxAmount);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.FundwalletToCoreInquiry] Error occured: " + ex.getMessage(), ex);
            if (ex.getMessage().equals("Your account is debit blocked.")) {
                webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            }
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("walletToCoreInquiry => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;

    }

    @Override
    public WebServiceVO fundWalletToCore(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.FundwalletToCore] Start:: ");
        String isValidationRequired = webServiceVO.getPinType();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "Fund Wallet To Core";
        String cmdId = CommandFieldConstants.KEY_CMD_BBTOCORECMD;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getSenderMobileNumber());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.FUND_WALLET_2_CORE);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getSenderMobileNumber(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.KEY_CMD_BB2COREINFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.RELIEF_FUND_PRODUCT);
            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getSenderMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
            baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            baseWrapper.putObject(CommandFieldConstants.KEY_RECEIVING_CUSTOMER_MOBILE, webServiceVO.getReceiverMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getRecieverAccountNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_TXAM, webServiceVO.getTransactionAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTransactionAmount());

//            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
//            if ("00".equals(webServiceVO.getResponseCode())) {
            xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.KEY_CMD_BBTOCORECMD);

            String trxId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
            String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
            String receiverAccountTitle = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CORE_ACT_TITLE_NODEREF);
            String trxAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);

            webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setDateTime(webServiceVO.getDateTime());
            webServiceVO.setTransactionId(trxId);
            webServiceVO.setReceiverMobileNumber(webServiceVO.getReceiverMobileNumber());
            webServiceVO.setRecieverAccountNumber(webServiceVO.getRecieverAccountNumber());
            webServiceVO.setRecieverAccountTilte(receiverAccountTitle);
            webServiceVO.setTransactionAmount(webServiceVO.getTransactionAmount());
            webServiceVO.setCommissionAmount(charges);
            webServiceVO.setTotalAmount(trxAmount);

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    this.logger.error("[FonePaySwitchController.walletToCore] Command Exception occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                this.logger.error("[FonePaySwitchController.walletToCore] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.walletToCore] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("walletToCore => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO debitCardIssuance(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.debitCardIssuance] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        String PID = "";
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        if (webServiceVO.getTransactionType().equals("02")) {
            PID = ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE.toString();

        } else {
            PID = ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE.toString();
        }
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_DEBIT_CARD_ISSUANCE;
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "Debit Card Issuance";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay debitCardIssuance] [Mobile:" + mobileNo + ", PID:" + PID + ", Cnic:" + cnic + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CTYPE, webServiceVO.getCardTypeId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, "5");
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject("CARD_DESCRIPTION", webServiceVO.getCardDescription());
        baseWrapper.putObject("MAILING_ADDRESS", webServiceVO.getMailingAddress());
        baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_TYPE, transactionType);
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_CARD_ISSUANCE);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_DEBIT_CARD_ISSUANCE_INFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_CARD_ISSUANCE);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_CARD_ISSUANCE);
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");

            }

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.debitcardissuance] Command Exception Error occured:" + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.debitcardissuance] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.debitcardissuance] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.debitcardissuance] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.debitcardissuance] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO debitCardIssuanceInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.debitCardIssuanceInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String PID = "";
        CardProdCodeModel cardTypeModel = new CardProdCodeModel();
        List<CardProdCodeModel> list;

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        String mobileNo = webServiceVO.getMobileNo();
        if (webServiceVO.getTransactionType().equals("02")) {
            PID = ProductConstantsInterface.DEBIT_CARD_RE_ISSUANCE.toString();

        } else {
            PID = ProductConstantsInterface.CUSTOMER_DEBIT_CARD_ISSUANCE.toString();
        }
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_DEBIT_CARD_ISSUANCE_INFO;
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "Debit Card Issuance";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay debitCardIssuanceInquiry] [Mobile:" + mobileNo + ", PID:" + PID + ", Cnic:" + cnic + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CTYPE, webServiceVO.getCardTypeId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, PID);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_TYPE, transactionType);
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_CARD_ISSUANCE_INQUIRY);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
//below line comment because already save in DB
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_CARD_ISSUANCE_INQUIRY);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_CARD_ISSUANCE_INFO);
                String fee = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.FEE_NODEREF);


                if (isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                list = getCommonCommandManager().getCardTypeDao().findByExample(cardTypeModel, null, null, exampleConfigHolderModel).getResultsetList();
                List<CardType> cardTypeList = new ArrayList<>();
                for (CardProdCodeModel model : list) {
                    CardType cardType = new CardType();
                    cardType.setCardProductTypeId(String.valueOf(model.getCardProductCodeId()));
                    cardType.setCardName(model.getCardProductName());
                    cardTypeList.add(cardType);
                }


                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setCharges(fee);
                webServiceVO.setCardTypes(cardTypeList);
            }

        } catch (CommandException e) {
            if (e.getErrorCode() == 9097) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEBIT_CARD_ALREADY_EXISTS.toString());
            } else {
                logger.error("[FonePaySwitchController.debitcardissuanceinquiry] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.debitcardissuanceinquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.debitcardissuanceinquiry] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.debitcardissuanceinquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO hraRegistration(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.hraRegistration] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_OPEN_HRA_ACCOUNT_PAYMENT;
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "HRA Registration";
        Long appUserTypeId = 2L;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay hraRegistration] [Mobile:" + mobileNo + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_NAME, webServiceVO.getFirstName());
        baseWrapper.putObject(CommandFieldConstants.KEY_CDOB, webServiceVO.getDateOfBirth());
        baseWrapper.putObject("FATHER_HUSBND_NAME", webServiceVO.getFatherHusbandName());
        baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE, webServiceVO.getPurposeOfPayment());
        baseWrapper.putObject(CommandFieldConstants.KEY_OCCUPATION, webServiceVO.getOccupation());
        baseWrapper.putObject("Kin_MOB_NO", webServiceVO.getKINMobileNumber());
        baseWrapper.putObject("SOI", webServiceVO.getSourceOfIncome());
        baseWrapper.putObject("KIN_NAME", webServiceVO.getKINName());
        baseWrapper.putObject("KIN_CNIC", webServiceVO.getKINCNIC());
        baseWrapper.putObject("KIN_RELATIONSHIP", webServiceVO.getKINRelation());

        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_CSCD, webServiceVO.getChallanNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_BILL_AMOUNT, webServiceVO.getBillAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, webServiceVO.getCommissionAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTotalAmount());
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_REGISTRATION);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_HRA_REGISTRATION_INQUIRY);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_REGISTRATION);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_OPEN_HRA_ACCOUNT_PAYMENT);
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.hraRegistration] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.hraRegistration] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.hraRegistration] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.hraRegistration] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;

    }

    @Override
    public WebServiceVO hraRegistrationInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.hraRegistrationInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_HRA_REGISTRATION_INQUIRY;
        String appId = "2";
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "HRA Registration Inquiry";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay hraRegistrationInquiry] [Mobile:" + mobileNo + ", Cnic:" + cnic + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_REGISTRATION_INQUIRY);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_REGISTRATION_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_HRA_REGISTRATION_INQUIRY);
                String cDOB = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CDOB_NODEREF);
                String name = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CNAME_NODEREF);
                String fatherName = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.FNAME_NODEREF);


                if (isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setDateOfBirth(cDOB);
                webServiceVO.setFirstName(name);
                webServiceVO.setLastName(fatherName);
            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.hraRegistrationInquiry] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.hraRegistrationInquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.hraRegistrationInquiry] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.hraRegistrationInquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO debitInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.debitInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_DEBIT_INQUIRY_API;
        String appId = "2";
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "Debit Inquiry";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay debitInquiry] [Mobile:" + mobileNo + "]");
        CustomerModel customerModel = new CustomerModel();

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
        if(webServiceVO.getProductID().equals("xtraCash")) {
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.LOAN_XTRA_CASH_REPAYMENT);
        }
        else{
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, webServiceVO.getProductID());
        }

        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_INQUIRY);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            if (appUserModel != null) {
                customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }
            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                String channelId = MessageUtil.getMessage("merchantCamping.channel.ids");
                List<String> channelIds = Arrays.asList(channelId.split("\\s*,\\s*"));
                if (channelIds.contains(webServiceVO.getChannelId())) {
                    I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                    I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                    requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionInquiry);
                    requestVO.setUserId(String.valueOf(appUserModel.getAppUserId()));
                    requestVO.setCNIC(appUserModel.getNic());
                    requestVO.setCardNumber("");
                    requestVO.setMerchantName(webServiceVO.getTerminalId());
                    requestVO.setSTAN(webServiceVO.getReserved2());
                    requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
                    requestVO.setMobileNumber(appUserModel.getMobileNo());
                    requestVO.setTransactionAmount(webServiceVO.getTransactionAmount());
                    requestVO.setTransactionCodeDesc("POS PURCHASE");
                    requestVO.setTransactionType("M");
                    requestVO.setSegmentId(String.valueOf(customerModel.getSegmentId()));

                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                    if (!responseVO.getResponseCode().equals("I8SB-200")) {
                        webServiceVO.setResponseCode("65");
                        webServiceVO.setResponseCodeDescription("Transaction Rejected");
                    } else {
                        xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_INQUIRY_API);
                        String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CAMTF_NODEREF);
                        String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TXAMF_NODEREF);
                        String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMT_NODEREF);


                        if (isopt.equals("02")) {
                            ///Generate OTP and store in MiniTransaction
                            String otp = CommonUtils.generateOneTimePin(5);
                            logger.info("The plain otp is " + otp);
                            String encryptedPin = EncoderUtils.encodeToSha(otp);
                            getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                            webServiceVO.setOtpPin(otp);
                            getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                        }
                        webServiceVO.setResponseContentXML(xml);
                        webServiceVO.setResponseCode("00");
                        webServiceVO.setResponseCodeDescription("Successful");
                        webServiceVO.setCommissionAmount(charges);
                        webServiceVO.setTransactionAmount(transactionAmount);
                        webServiceVO.setTotalAmount(totalAmount);
                    }
                } else {


                    xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_INQUIRY_API);
                    String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CAMTF_NODEREF);
                    String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TXAMF_NODEREF);
                    String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMT_NODEREF);


                    if (isopt.equals("02")) {
                        ///Generate OTP and store in MiniTransaction
                        String otp = CommonUtils.generateOneTimePin(5);
                        logger.info("The plain otp is " + otp);
                        String encryptedPin = EncoderUtils.encodeToSha(otp);
                        getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                        webServiceVO.setOtpPin(otp);
                        getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                    }
                    webServiceVO.setResponseContentXML(xml);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successful");
                    webServiceVO.setCommissionAmount(charges);
                    webServiceVO.setTransactionAmount(transactionAmount);
                    webServiceVO.setTotalAmount(totalAmount);
                }
            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.debitInquiry] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            if (e.getMessage().equals("Your account is debit blocked.")) {
                webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else {
                webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.debitInquiry] Error occured: " + e.getMessage(), e);
            if (e.getMessage().equals("Your account is debit blocked.")) {
                webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else {
                if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                    this.logger.error("[FonePaySwitchController.debitInquiry] Error occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                    if (e instanceof NullPointerException
                            || e instanceof HibernateException
                            || e instanceof SQLException
                            || e instanceof DataAccessException
                            || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                        logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                        webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                    }
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.debitInquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;

    }

    @Override
    public WebServiceVO debit(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.debitPaymentApi] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_DEBIT_PAYMENT_API;
        String appId = "2";
        Double charges;
        Double tranAmount;
        String merchantTransactionAmount = webServiceVO.getTransactionAmount();
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "Debit Payment";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay DebitPayment] [Mobile:" + mobileNo + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
        if(webServiceVO.getProductID().equals("xtraCash")) {
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.LOAN_XTRA_CASH_REPAYMENT);
        }
        else{
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, webServiceVO.getProductID());
        }

        baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
        baseWrapper.putObject(CommandFieldConstants.KEY_PROFIT_KEY, webServiceVO.getReserved3());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_4, webServiceVO.getReserved4());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_5, webServiceVO.getReserved5());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_8, webServiceVO.getReserved8());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_9, webServiceVO.getReserved9());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_10, webServiceVO.getReserved10());

        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_PAYMENT);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_DEBIT_INQUIRY_API);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_PAYMENT);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }
            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_DEBIT_PAYMENT_API);
                String transactionAmount = (MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_AMOUNT_NODEREF));
                String transactionCode = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
                String balanceAfterTransaction = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_BAL_NODEREF);

                if (xml != null && webServiceVO.getProductID().equals("10245364")) {
                    DebitCardReversalVO debitCardReversalVO = new DebitCardReversalVO();
//                    debitCardReversalVO.setRrn(webServiceVO.getRetrievalReferenceNumber());
                    debitCardReversalVO.setCardPan(webServiceVO.getMobileNo());
                    debitCardReversalVO.setOriginalStan(webServiceVO.getRetrievalReferenceNumber());
                    debitCardReversalVO.setReversalAmount(Double.valueOf(webServiceVO.getTransactionAmount()));
                    debitCardReversalVO.setRetryCount(0L);
                    debitCardReversalVO.setTransactionCodeId(Long.valueOf(transactionCode));
                    debitCardReversalVO.setTransactionCode(transactionCode);
                    debitCardReversalVO.setReversalRequestTime(webServiceVO.getDateTime());
                    debitCardReversalVO.setAdviceType(CoreAdviceUtil.ADVICE_TYPE_REVERSAL);
                    debitCardReversalVO.setProductId(Long.valueOf(webServiceVO.getProductID()));
                    this.loadAndForwardCoreAdviceToQueue(debitCardReversalVO);

                    if (transactionAmount.contains(",")) {
                        tranAmount = Double.parseDouble(transactionAmount.replace(",", ""));
                    } else {
                        tranAmount = Double.valueOf(transactionAmount);
                    }
                    String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                    charges = Double.valueOf(MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF));
                    if (charges == null || charges.equals(0.0)) {
                        charges = Double.valueOf(MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF));
                        tranAmount = tranAmount - charges;

                    }

                    webServiceVO.setResponseContentXML(xml);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successfull");
                    webServiceVO.setCommissionAmount(String.valueOf(charges));
                    webServiceVO.setTransactionAmount(String.valueOf(tranAmount));
                    webServiceVO.setTotalAmount(totalAmount);
                    webServiceVO.setTransactionId(transactionCode);
                } else {
                    if (transactionAmount.contains(",")) {
                        tranAmount = Double.parseDouble(transactionAmount.replace(",", ""));
                    } else {
                        tranAmount = Double.valueOf(transactionAmount);
                    }
                    String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                    charges = Double.valueOf(MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF));
                    if (charges == null || charges.equals(0.0)) {
                        charges = Double.valueOf(MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF));
                        tranAmount = tranAmount - charges;

                    }


                    webServiceVO.setResponseContentXML(xml);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successfull");
                    webServiceVO.setCommissionAmount(String.valueOf(charges));
                    webServiceVO.setTransactionAmount(String.valueOf(tranAmount));
                    webServiceVO.setTotalAmount(totalAmount);
                    webServiceVO.setTransactionId(transactionCode);
                }
                String channelId = MessageUtil.getMessage("merchantCamping.channel.ids");
                List<String> channelIds = Arrays.asList(channelId.split("\\s*,\\s*"));
                if (channelIds.contains(webServiceVO.getChannelId())) {
                    I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                    I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                    requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                    requestVO.setUserId(String.valueOf(appUserModel.getAppUserId()));
                    requestVO.setTransactionCode(transactionCode);
                    requestVO.setAvailableBalance(balanceAfterTransaction);
                    requestVO.setMobileNumber(appUserModel.getMobileNo());
                    requestVO.setTransactionDate(String.valueOf(new Date()));
                    requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
                    requestVO.setSTAN(webServiceVO.getReserved2());
                    requestVO.setTransactionAmount(merchantTransactionAmount);
                    requestVO.setTransactionType("M");
                    requestVO.setStatus("S");
                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
                }
            }

        } catch (CommandException e) {
            String channelId = MessageUtil.getMessage("merchantCamping.channel.ids");
            List<String> channelIds = Arrays.asList(channelId.split("\\s*,\\s*"));
            if (channelIds.contains(webServiceVO.getChannelId())) {
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                requestVO.setUserId(String.valueOf(appUserModel.getAppUserId()));
                requestVO.setTransactionCode("");
                requestVO.setAvailableBalance("");
                requestVO.setMobileNumber(appUserModel.getMobileNo());
                requestVO.setTransactionDate(String.valueOf(new Date()));
                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
                requestVO.setSTAN(webServiceVO.getReserved2());
                requestVO.setTransactionAmount(merchantTransactionAmount);
                requestVO.setTransactionType("M");
                requestVO.setStatus("U");
                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

            }
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.") ||
                        e.getMessage().equals("Request cannot be processed. Insufficient account balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.debitPayment] Error occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.debitPayment] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.debitPayment] Error occured: " + e.getMessage(), e);
            String channelId = MessageUtil.getMessage("merchantCamping.channel.ids");
            List<String> channelIds = Arrays.asList(channelId.split("\\s*,\\s*"));
            if (channelIds.contains(webServiceVO.getChannelId())) {
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.prepareMerchantCampingRequest(I8SBConstants.RequestType_TransactionStatus);
                requestVO.setUserId(String.valueOf(appUserModel.getAppUserId()));
                requestVO.setTransactionCode("");
                requestVO.setAvailableBalance("");
                requestVO.setMobileNumber(appUserModel.getMobileNo());
                requestVO.setTransactionDate(String.valueOf(new Date()));
                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
                requestVO.setSTAN(webServiceVO.getReserved2());
                requestVO.setStatus("U");
                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
            }
//            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
//                this.logger.error("[FonePaySwitchController.debitPayment] Error occured: " + e.getMessage(), e);
//                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
//                webServiceVO.setResponseCodeDescription(e.getMessage());
//                if (e instanceof NullPointerException
//                        || e instanceof HibernateException
//                        || e instanceof SQLException
//                        || e instanceof DataAccessException
//                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {
//
//                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
//                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
//                }


            this.logger.error("[FonePaySwitchController.DebitPayment] Error occured: " + e.getMessage(), e);
            if (e.getMessage().equals("Your account is debit blocked.")) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEBIT_BLOCKED);
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());

            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.debitPayment] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;

    }


    @Override
    public WebServiceVO agentBillPaymentInquiry(WebServiceVO webServiceVO) {
        String isopt = webServiceVO.getPinType();
        String paymentType = webServiceVO.getPaymentType();
        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getBillAmount();
        String PID = webServiceVO.getProductID();
        String consumerNo = webServiceVO.getConsumerNo();
        String xml = "";
        BaseWrapper bWrapper = new BaseWrapperImpl();
        String cmdId = CommandFieldConstants.CMD_ALLPAY_BILL_INFO;
        String messageType = "Bill Payment";

        this.logger.info("[FonePay billPaymentInquiry] [Mobile:" + mobileNo + ", PID:" + PID + ", consumerNO:" + consumerNo + ", Trx Amount:" + trxnAmount + "]");

        bWrapper.putObject("CMOB", mobileNo);
        bWrapper.putObject("PMTTYPE", paymentType);
        bWrapper.putObject("BAMT", trxnAmount);
        bWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNo);
        bWrapper.putObject("PID", PID);
        bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
        bWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE, "1");
        bWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_AGENT_MOBILE_NUMBER, webServiceVO.getAgentMobileNumber());
        bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());

        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
        fonePayMessageVO.setMobileNo(webServiceVO.getAgentMobileNumber());
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_BILL_PAYMENT_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getAgentMobileNumber(), UserTypeConstantsInterface.RETAILER);
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

//            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
//            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
//                return webServiceVO;
//            }

            fonePayMessageVO = getFonePayManager().makevalidateAgent(fonePayMessageVO);
            if ("00".equals(fonePayMessageVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_INFO);

                //Parsing of xml to object
                try {
                    InputStream is = new ByteArrayInputStream(xml.toString().getBytes(StandardCharsets.UTF_8.name()));// BufferedInputStream((InputStream) strBuilder)
                    JAXBContext jaxbContext = JAXBContext.newInstance("com.inov8.microbank.common.util");
                    Unmarshaller jaxbunMarshaller = jaxbContext.createUnmarshaller();
                    CommandResponseXML tc = (CommandResponseXML) jaxbunMarshaller.unmarshal(new BufferedInputStream(is));

                    if (tc != null && tc.getParamList() != null && tc.getParamList().size() > 0) {
                        Iterator<CommandResponseXML.Param> it = tc.getParamList().iterator();
                        while (it.hasNext()) {
                            CommandResponseXML.Param param = it.next();

                            if (param.getName().equals(CommandFieldConstants.KEY_PESSENGER_NAME)) {
                                webServiceVO.setDueDate(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_TOTAL_AMOUNT)) {
                                webServiceVO.setTotalAmount(param.getValue());
                            } else if (param.getName().equals("AFTER_DUE_DATE")) {
                                webServiceVO.setLateBillAmount(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER)) {
                                webServiceVO.setConsumerNo(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_BILL_AMOUNT)) {
                                webServiceVO.setBillAmount(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_LATE_BILL_AMT)) {
                                webServiceVO.setLateBillAmount(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.KEY_BILL_PAID)) {
                                webServiceVO.setBillPaid(param.getValue());
                            } else if (param.getName().equals(CommandFieldConstants.CMD_AGNETMATE_DUEDATER)) {
                                webServiceVO.setDueDate(param.getValue());
                            } else if (param.getName().equals("ISOVERDUE")) {
                                webServiceVO.setOverDue(param.getValue());
                            }
                        }
                    }
                } catch (JAXBException e) {
                    throw new RuntimeException(e);
                }

                if (isopt != null && isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }
        } catch (CommandException e) {
            if (e.getErrorCode() == 131) {
                webServiceVO.setResponseCode(FonePayResponseCodes.BILL_ALREADY_PAID);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else if (e.getErrorCode() == 132) {
                webServiceVO.setResponseCode(FonePayResponseCodes.REFERENCE_NUMBER_BLOCKED);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else if (e.getErrorCode() == 134) {
                webServiceVO.setResponseCode(FonePayResponseCodes.RDV_DOWN);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else if (e.getErrorCode() == 135) {
                webServiceVO.setResponseCode(FonePayResponseCodes.CONSUMER_NUMBER_INVALID);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else {
                this.logger.error("[FonePaySwitchController.billPaymentInquiry] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (WorkFlowException wfe) {
            webServiceVO.setResponseCode(wfe.getErrorCode());
            webServiceVO.setResponseCodeDescription(wfe.getMessage());
        } catch (Exception ex) {
            this.logger.error("[FonePaySwitchController.billPaymentInquiry] Error occured: " + ex.getMessage(), ex);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CustomerBillPaymentInfoCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }


    @Override
    public WebServiceVO agentBillPayment(WebServiceVO webServiceVO) {

        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getBillAmount();
        String PID = webServiceVO.getProductID();
        String consumerNo = webServiceVO.getConsumerNo();
        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
        String commissionAmount = webServiceVO.getCommissionAmount();
        String charges = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String terminalId;


        BaseWrapper idWrapper = new BaseWrapperImpl();
        idWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        idWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_BILL_PAYMENT);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getAgentMobileNumber(), UserTypeConstantsInterface.RETAILER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            }
////               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
//            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
//                return webServiceVO;
//            }

            this.userValidation(webServiceVO, CommandFieldConstants.CMD_ALLPAY_BILL_INFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            String cmdId = CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT;
            String messageType = "Bill Payment";

            this.logger.info("[FonePay billPayment] [Mobile:" + mobileNo + ", PID:" + PID + ", consumerNO:" + consumerNo + ", Trx Amount:" + trxnAmount + "]");

            bWrapper.putObject("CMOB", mobileNo);
            bWrapper.putObject("TPAM", txProcessingAmount);
            bWrapper.putObject("CAMT", commissionAmount);
            bWrapper.putObject(CommandFieldConstants.KEY_TXAM, trxnAmount);
            bWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNo);
            bWrapper.putObject("PID", PID);
            bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            bWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_AGENT_MOBILE_NUMBER, webServiceVO.getAgentMobileNumber());
            bWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE, "1");
            charges = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
            String transactionAmount = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_TOTAL_AMT_NODEREF);
            String totalAmount = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
            String commision = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
            String transactionCode = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_CODE_NODEREF);
            webServiceVO.setResponseContentXML(charges);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
            webServiceVO.setTotalAmount(totalAmount);
            webServiceVO.setTransactionAmount(transactionAmount);
            webServiceVO.setCommissionAmount(commision);
            webServiceVO.setTransactionId(transactionCode);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else if (e.getMessage().equals("Your account is debit blocked.")) {
                    webServiceVO.setResponseCode(FonePayResponseCodes.DEBIT_BLOCKED);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                } else {
                    logger.error("[FonePaySwitchController.billPayment] Error occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.billPayment] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.billPayment] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("AgentBillPaymentCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO creditInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.creditInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_CREDIT_INQUIRY_API;
        String appId = "2";
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "Debit Inquiry";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay creditInquiry] [Mobile:" + mobileNo + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
        if(webServiceVO.getProductID().equals("xtraCash")) {
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.LOAN_XTRA_CASH);
        }
        else{
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, webServiceVO.getProductID());
        }
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CREDIT_INQUIRY);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
            //               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CREDIT_INQUIRY_API);
                String charges = null;
                charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);
                webServiceVO.setReserved3("ExclusiveCharges");
                if (charges == null || charges.equals("0.00")) {
                    charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CAMTF_NODEREF);
                    if (charges == null || charges.equals("0.00")) {
                        webServiceVO.setReserved3("");
                    } else {
                        webServiceVO.setReserved3("InclusiveCharges");
                    }
                }
                String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TXAMF_NODEREF);
                String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMT_NODEREF);


                if (isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setCommissionAmount(charges);
                webServiceVO.setTransactionAmount(transactionAmount);
                webServiceVO.setTotalAmount(totalAmount);
            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.creditInquiry] Error occured: " + e.getMessage(), e);
            if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else {
                    logger.error("[FonePaySwitchController.creditPayment] Error occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            }
            else {
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.creditInquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.creditInquiry] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.creditInquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO credit(WebServiceVO webServiceVO) {

        logger.info("[FonePaySwitchController.creditPaymentApi] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_DEBIT_PAYMENT_API;
        String appId = "2";
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "credit Payment";
//        Long productId = Long.valueOf(webServiceVO.getProductID());
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay creditPayment] [Mobile:" + mobileNo + "]");
        AdvanceSalaryLoanModel advanceSalaryLoanModel = new AdvanceSalaryLoanModel();


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
        if(webServiceVO.getProductID().equals("xtraCash")) {
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.LOAN_XTRA_CASH);
        }
        else{
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, webServiceVO.getProductID());
        }

        baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_2, webServiceVO.getReserved2());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_3, webServiceVO.getReserved3());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_4, webServiceVO.getReserved4());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_5, webServiceVO.getReserved5());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_6, webServiceVO.getReserved6());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_7, webServiceVO.getReserved7());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_8, webServiceVO.getReserved8());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_9, webServiceVO.getReserved9());
        baseWrapper.putObject(CommandFieldConstants.KEY_RESERVED_10, webServiceVO.getReserved10());
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CREDIT_PAYMENT);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_CREDIT_INQUIRY_API);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CREDIT_PAYMENT);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            {
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }
            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
//                if (productId.equals(ProductConstantsInterface.CORE_TO_WALLET_MB)) {
//                MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
//                middlewareMessageVO.setAccountNo1("0000000000");
//                middlewareMessageVO.setAccountNo2(webServiceVO.getMobileNo());
//                middlewareMessageVO.setStan(webServiceVO.getReserved2());
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
//                Date dt = formatter.parse(webServiceVO.getDateTime());
//                middlewareMessageVO.setRequestTime(dt);
//                middlewareMessageVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
//                middlewareMessageVO.setTransactionAmount(webServiceVO.getTransactionAmount());
//                middlewareMessageVO.setProductId(Long.parseLong(webServiceVO.getProductID()));
//                middlewareMessageVO = this.creditPaymentAdvice(middlewareMessageVO);
//                if (middlewareMessageVO.getResponseCode().equals("00")) {
//                    webServiceVO.setResponseCode("00");
//                    webServiceVO.setResponseCodeDescription("Successfull");
//                    webServiceVO.setTransactionAmount(middlewareMessageVO.getTransactionAmount());
//
//                }
//                }
//                below code
//                else {
                if (webServiceVO.getProductID().equals(MessageUtil.getMessage("advanceSalary"))) {
                    advanceSalaryLoanModel = getCommonCommandManager().getAdvanceSalaryLoanDAO().loadAdvanceSalaryLoanByMobileNumber(appUserModel.getMobileNo());
                    if (advanceSalaryLoanModel != null) {
                        webServiceVO.setResponseCode("163");
                        webServiceVO.setResponseCodeDescription("LOAN Already Disburse");
                    } else {
                        xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CREDIT_PAYMENT_API);
                        String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_AMOUNT_NODEREF);
                        String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                        String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
                        String transactionCode = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
                        webServiceVO.setResponseContentXML(xml);
                        webServiceVO.setResponseCode("00");
                        webServiceVO.setResponseCodeDescription("Successfull");
                        webServiceVO.setCommissionAmount(charges);
                        webServiceVO.setTransactionAmount(transactionAmount);
                        webServiceVO.setTotalAmount(totalAmount);
                        webServiceVO.setTransactionId(transactionCode);
                    }
                } else {
                    xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CREDIT_PAYMENT_API);
                    String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_AMOUNT_NODEREF);
                    String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                    String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
                    String transactionCode = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
                    webServiceVO.setResponseContentXML(xml);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successfull");
                    webServiceVO.setCommissionAmount(charges);
                    webServiceVO.setTransactionAmount(transactionAmount);
                    webServiceVO.setTotalAmount(totalAmount);
                    webServiceVO.setTransactionId(transactionCode);
                }
            }

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.creditPayment] Error occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }

            } else {
                logger.error("[FonePaySwitchController.creditPayment] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.creditPayment] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.creditPayment] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.creditPayment] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;

    }

    @Override
    public WebServiceVO hraCashWithDrawlInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.creditInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        AppUserModel appUserModel1 = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_CASH_OUT_INFO;
        String productId = ProductConstantsInterface.HRA_CASH_WITHDRAWAL.toString();
        String appId = "2";
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "HRA CashWithdrawal Inquiry";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay HRA CashWithdrawal] [Mobile:" + mobileNo + "]");


        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
        fonePayMessageVO.setMobileNo(webServiceVO.getAgentMobileNumber());
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, webServiceVO.getAgentMobileNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, productId);
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        baseWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, "1");


//        baseWrapper.putObject(CommandFieldConstants.);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_CASHWITHDRAWAL_PAYMENT_INQUIRY);

            appUserModel1 = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel1)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_CASHWITHDRAWAL_PAYMENT_INQUIRY);
            appUserModel1 = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel1 != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel1);
                baseWrapper.setBasePersistableModel(appUserModel1);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel1.getNic());
            }


            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getAgentMobileNumber(), UserTypeConstantsInterface.RETAILER);
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }

            fonePayMessageVO = getFonePayManager().makevalidateAgent(fonePayMessageVO);


            if ("00".equals(fonePayMessageVO.getResponseCode()) && "00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CASH_OUT_INFO);
                String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.SERVICE_CHARGES_NODEREF);
                String transactionAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TXAMF_NODEREF);
                String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMT_NODEREF);
                cnic = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CNIC_NODEREF);


                if (isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
                webServiceVO.setCommissionAmount(charges);
                webServiceVO.setTransactionAmount(transactionAmount);
                webServiceVO.setTotalAmount(totalAmount);
                webServiceVO.setCnicNo(cnic);

            }

        } catch (CommandException e) {
            logger.error("[FonePaySwitchController.HRA CashWithdrawal] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
            webServiceVO.setResponseCodeDescription(e.getMessage());
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.HRA CashWithdrawal] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.HRA CashWithdrawal] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.HRA CashWithdrawal] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO hraCashWithDrawl(WebServiceVO webServiceVO) {
        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getBillAmount();
        String productId = ProductConstantsInterface.HRA_CASH_WITHDRAWAL.toString();
        String consumerNo = webServiceVO.getConsumerNo();
        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
        String commissionAmount = webServiceVO.getCommissionAmount();
        String charges = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String terminalId;


        BaseWrapper idWrapper = new BaseWrapperImpl();
        idWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        idWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT);
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_HRA_CASHWITHDRAWAL_PAYMENT);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getAgentMobileNumber(), UserTypeConstantsInterface.RETAILER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                bWrapper.setBasePersistableModel(appUserModel);
                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            }

            this.userValidation(webServiceVO, CommandFieldConstants.CMD_CASH_OUT_INFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            String cmdId = CommandFieldConstants.CMD_ALLPAY_BILL_PAYMENT;
            String messageType = "HRA CashWithdrawal";

            this.logger.info("[FonePay HRA CashWithdrawal] [Mobile:" + mobileNo + ", PID:" + productId + ", consumerNO:" + consumerNo + ", Trx Amount:" + trxnAmount + "]");

            bWrapper.putObject("CMOB", mobileNo);
            bWrapper.putObject("TPAM", txProcessingAmount);
            bWrapper.putObject("CAMT", commissionAmount);
            bWrapper.putObject(CommandFieldConstants.KEY_TXAM, trxnAmount);
            bWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_CONSUMER_NUMBER, consumerNo);
            bWrapper.putObject("PID", productId);
            bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
            bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
            bWrapper.putObject(CommandFieldConstants.CMD_AGNETMATE_AGENT_MOBILE_NUMBER, webServiceVO.getAgentMobileNumber());
            bWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_TYPE, "1");
            bWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
//            bWrapper.putObject("ENCT", EncryptionUtil.ENCRYPTION_TYPE_AES);
            bWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
//            bWrapper.putObject(CommandFieldConstants.KEY_PIN,agentMpin);
            bWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, "0");
            bWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, "0");
            bWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTransactionAmount());
            charges = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.KEY_CMD_HRA_CASH_WITHDRAWAL);
            String transactionAmount = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_AMOUNT_NODEREF);
            String totalAmount = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
            String commision = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF);
            String transactionCode = MiniXMLUtil.getTagTextValue(charges, MiniXMLUtil.TRANS_ID_NODEREF);
            webServiceVO.setResponseContentXML(charges);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
            webServiceVO.setTotalAmount(totalAmount);
            webServiceVO.setTransactionAmount(transactionAmount);
            webServiceVO.setCommissionAmount(commision);
            webServiceVO.setTransactionId(transactionCode);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.HRA CashWithdrawal] Command Exception Error occured:" + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.HRA CashWithdrawal] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.HRA CashWithdrawal] Error occured: " + e.getMessage(), e);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("HRA CashWithdrawal => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO accountAuthentication(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.accountAuthentication] Start:: ");
        String mobileNo = webServiceVO.getMobileNo();
        String consumerNo = webServiceVO.getConsumerNo();
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        CustomerModel customerModel = new CustomerModel();
        BaseWrapper idWrapper = new BaseWrapperImpl();
        SegmentModel segmentModel = null;
        Double accountBalance = 0.0d;

        Double dailyDebitConsumed = 0.0d, dailyCreditConsumed = 0.0d, monthlyDebitConsumed = 0.0d,
                monthlyCreditConsumed = 0.0d, yearlyDebitConsumed = 0.0d, yearlyCreditConsumed = 0.0d;
        List<String> remainingLimits;
        idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = this.getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Login Authentication");
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            webServiceVO = this.getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                if (appUserModel != null) {
                    ThreadLocalAppUser.setAppUserModel(appUserModel);
                    bWrapper.setBasePersistableModel(appUserModel);
                    bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                    customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

                    if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                        if (appUserModel.getRegistrationStateId().equals(RegistrationStateConstants.BLINK_PENDING)) {
                            throw new CommandException("Customer is in Blink-Pending state. Full Registration Required",
                                    ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, new Throwable());
                        }
                    }
                    SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
                    sma.setCustomerId(appUserModel.getCustomerId());
                    sma.setActive(true);
                    sma.setAccountClosedUnsetteled(0L);
                    SmartMoneyAccountModel sma1 = getCommonCommandManager().getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);
                    segmentModel = getCommonCommandManager().getSegmentDao().findByPrimaryKey(customerModel.getSegmentId());
                    AccountInfoModel model = getCommonCommandManager().getAccountInfoModel(appUserModel.getCustomerId(), sma1.getName());
                    accountBalance = Double.valueOf(getCommonCommandManager().getAccountBalance(model, sma1));
                    uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                    ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                }
                int counter = uda.getLoginAttemptCount();
                String pin = EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", uda.getPin());

                if (!pin.equals(webServiceVO.getMobilePin())) {
                    ++counter;
                    uda.setLoginAttemptCount(counter);
                    baseWrapper.setBasePersistableModel(uda);
                    this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);
                    if (uda.getLoginAttemptCount() == 3) {
                        uda.setAccountLocked(true);
                        baseWrapper.setBasePersistableModel(uda);
                        this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);
                        appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_WARM);
                        baseWrapper.setBasePersistableModel(appUserModel);
                        this.getCommonCommandManager().updateAppUser(baseWrapper);
                    }

                    webServiceVO.setResponseCode("10");
                    webServiceVO.setResponseCodeDescription("User/PIN Invalid");
                }
                if (webServiceVO.getResponseCode() != null && webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                    uda.setLoginAttemptCount(new Integer(0));
                    baseWrapper.setBasePersistableModel(uda);
                    this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successful");
                    webServiceVO.setAccountTitle(appUserModel.getFirstName() + " " + appUserModel.getLastName());
                    if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                        webServiceVO.setAccountType("L0");
                        webServiceVO.setIsBVSAccount("0");
                    } else if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1)) {
                        webServiceVO.setAccountType("L1");
                        webServiceVO.setIsBVSAccount("1");
                    } else if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                        webServiceVO.setAccountType("blink");
                        if (customerModel.getBlinkBvs().equals(true)){
                            webServiceVO.setReserved3("1");
                        }else {
                            webServiceVO.setReserved3("0");
                        }
                    } else if (customerModel.getCustomerAccountTypeId().equals(56L)) {
                        webServiceVO.setAccountType("Zindigi Ultra");
                    }

                    if (customerModel.getIban() != null) {
                        webServiceVO.setBenificieryIban(customerModel.getIban());
                    } else {
                        webServiceVO.setBenificieryIban("");
                    }
                    webServiceVO.setReserved2(segmentModel.getName());
                    webServiceVO.setBalance(String.valueOf(accountBalance));
                    return webServiceVO;
                } else {
                    return webServiceVO;
                }

            }
        } catch (CommandException e) {

            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else {
                logger.error("[FonePaySwitchController.verify Mpin] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.MpinVerification] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.MpinVerification] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO loginPin(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.loginPin] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String loginPin = webServiceVO.getLoginPin();

        try {
            if (webServiceVO.getReserved2().equals("0")) {

            } else {
                webServiceVO = this.validateRRN(webServiceVO);
            }
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_lOGIN_PIN);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                uda = this.getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());

                BaseWrapper baseWrapper = new BaseWrapperImpl();

                String password = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, loginPin);
                appUserModel.setPassword(password);
                uda.setPin(password);
                uda.setLoginAttemptCount(new Integer(0));

                baseWrapper.setBasePersistableModel(appUserModel);
                this.getCommonCommandManager().updateAppUser(baseWrapper);

                baseWrapper.setBasePersistableModel(uda);
                this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);

                String names = MessageUtil.getMessage("mpinchange.channel.name");
                List<String> items = Arrays.asList(names.split("\\s*,\\s*"));
                if (!items.contains(webServiceVO.getChannelId())) {
                    String customerSMS = this.getMessageSource().getMessage("loginSmsViaAPI", null, null);
                    String userName = webServiceVO.getMobileNo();
                    baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(userName, customerSMS));
                    getCommonCommandManager().sendSMSToUser(baseWrapper);
                }
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.loginPin] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("loginPin => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO loginPinChange(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.loginPinChange] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String oldLoginPin = webServiceVO.getOldLoginPin();
        oldLoginPin = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, oldLoginPin);//EncoderUtils.encodeToSha(randomPin);

        String newLoginPin = webServiceVO.getNewLoginPin();
        newLoginPin = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, newLoginPin);//EncoderUtils.encodeToSha(randomPin);

        String confirmLoginPin = webServiceVO.getConfirmLoginPin();
        confirmLoginPin = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, confirmLoginPin);//EncoderUtils.encodeToSha(randomPin);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_LOGIN_PIN_CHANGE);
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null, null, webServiceVO.getMobileNo());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                uda = this.getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());
                String pin = EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", uda.getPin());
                if (!pin.equals(webServiceVO.getOldLoginPin())) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_LOGIN_PIN);
                    webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.INVALID_LOGIN_PIN));
                    this.logger.info("loginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
                    return webServiceVO;
                }

                if (webServiceVO.getOldLoginPin().equals(webServiceVO.getNewLoginPin())) { //params for login pin to be added.. i.e. old new etc
                    webServiceVO.setResponseCode(FonePayResponseCodes.SAME_PIN);
                    webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.SAME_PIN));
                    this.logger.info("loginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
                    return webServiceVO;
                }

                if (!webServiceVO.getNewLoginPin().equals(webServiceVO.getConfirmLoginPin())) { //params for login pin to be added.. i.e. old new etc
                    webServiceVO.setResponseCode(FonePayResponseCodes.PIN_MISMATCHED);
                    webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.PIN_MISMATCHED));
                    this.logger.info("loginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
                    return webServiceVO;
                }

                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
                bWrapper.putObject(CommandFieldConstants.KEY_PIN, oldLoginPin);
                bWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, newLoginPin);
                bWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, confirmLoginPin);
                bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
                String response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_LOGIN_PIN_CHANGE);
                if (MfsWebUtil.isErrorXML(response)) {
                    return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                }

                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                webServiceVO.setResponseContentXML(response);
            }
        } catch (CommandException ex) {
            logger.error("Error Occurred while changing Login Pin for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);

        } catch (Exception ex) {
            logger.error("Error Occurred while changing Login Pin for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("loginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    public WebServiceVO resetPin(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.resetPin] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String loginPin = webServiceVO.getNewLoginPin();


        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
//            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_RESET_LOGIN_PIN);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
//                return webServiceVO;
//            }

            if (!webServiceVO.getNewLoginPin().equals(webServiceVO.getConfirmLoginPin())) { //params for login pin to be added.. i.e. old new etc
                webServiceVO.setResponseCode(FonePayResponseCodes.PIN_MISMATCHED);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.PIN_MISMATCHED));
                this.logger.info("resetPin => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
                return webServiceVO;
            }

            webServiceVO = getFonePayManager().makevalidateCustomerForResetPinAPI(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                uda = this.getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());

                BaseWrapper baseWrapper = new BaseWrapperImpl();

                String password = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, loginPin);
                appUserModel.setPassword(password);
                appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                appUserModel.setUpdatedOn(new Date());
                uda.setPin(password);
                uda.setAccountLocked(false);
                uda.setComments("");
                uda.setUpdatedOn(new Date());
                uda.setLoginAttemptCount(new Integer(0));

                baseWrapper.setBasePersistableModel(appUserModel);
                this.getCommonCommandManager().updateAppUser(baseWrapper);

                baseWrapper.setBasePersistableModel(uda);
                this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);

//                String customerSMS = this.getMessageSource().getMessage("resetLoginSmsViaAPI", null, null);
                String userName = webServiceVO.getMobileNo();

//                baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(userName, customerSMS));
//                getCommonCommandManager().sendSMSToUser(baseWrapper);

                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.resetPin] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("resetPin => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());

        return webServiceVO;
    }

    @Override
    public WebServiceVO smsGeneration(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.smsGeneration] Start:: ");
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        String mobileNumber = webServiceVO.getMobileNo();
        String msgText = webServiceVO.getMessage();

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNumber);
            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, msgText);
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(mobileNumber, msgText));
            baseWrapper.putObject(CommandFieldConstants.KEY_NETWORK, webServiceVO.getReserved2());
//          if(webServiceVO.getReserved1().equals("0")){
            getCommonCommandManager().sendSMSToUser(baseWrapper);
//          }else {
//              getFonePayManager().sendSMSToUser(baseWrapper);
//          }
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");

        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.smsGeneration] Error occured: " + e.getMessage(), e);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        }
        this.logger.info("smsGeneration => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;

    }


    @Override
    public WebServiceVO advanceLoanSalary(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.advanceLoanSalary] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_ADVANCE_SALARY_LOAN);
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null, null, webServiceVO.getMobileNo());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);


            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                AdvanceSalaryLoanModel advanceSalaryLoanModel = new AdvanceSalaryLoanModel();
                advanceSalaryLoanModel.setCnic(webServiceVO.getCnicNo());
                advanceSalaryLoanModel.setLoanAmount(Long.valueOf(webServiceVO.getLoanAmount()));
                advanceSalaryLoanModel.setNoOfInstallment(Long.valueOf(webServiceVO.getNumberOfInstallments()));
                advanceSalaryLoanModel.setEarlyPaymentCharges(Long.valueOf(webServiceVO.getEarlyPaymentCharges()));
                advanceSalaryLoanModel.setLatePaymentCharges(Long.valueOf(webServiceVO.getLatePaymentCharges()));
                advanceSalaryLoanModel.setInstallmentAmount(Long.valueOf(webServiceVO.getInstallmentAmount()));
                advanceSalaryLoanModel.setMobileNo(webServiceVO.getMobileNo());
                advanceSalaryLoanModel.setProductId(Long.valueOf(webServiceVO.getProductID()));
                advanceSalaryLoanModel.setGracePeriodDays(Long.valueOf(webServiceVO.getGracePeriod()));
                advanceSalaryLoanModel.setCreatedOn(new Date());
                advanceSalaryLoanModel.setUpdatedBy(1L);
                advanceSalaryLoanModel.setUpdatedOn(new Date());
                advanceSalaryLoanModel.setCreatedBy(1L);

                getCommonCommandManager().saveOrUpdateAdvanceSalaryLoan(advanceSalaryLoanModel);

                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

            }
        } catch (CommandException ex) {
            logger.error("Error Occurred while Advance Salary Loan for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);

        } catch (Exception ex) {
            logger.error("Error Occurred while Advance Salary Loan for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("advanceSalaryLoan => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }


    @Override
    public WebServiceVO agentAccountLogin(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentAccountAuthentication] Start:: ");
        List<String> remainingLimits;
        String mobileNo = webServiceVO.getMobileNo();
        //      String consumerNo = webServiceVO.getConsumerNo();
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        BaseWrapper idWrapper = new BaseWrapperImpl();
        Double dailyDebitConsumed = 0.0, monthlyDebitConsumed = 0.0, yearlyDebitConsumed = 0.0;
        Double dailyCreditConsumed = 0.0, monthlyCreditConsumed = 0.0, yearlyCreditConsumed = 0.0;
        idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_LOGIN_AUTHENTICATION);

            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentAccountAuthentication] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();

            fonePayMessageVO.setMobileNo(appUserModel.getMobileNo());
            fonePayMessageVO = getFonePayManager().makevalidateAgent(fonePayMessageVO);

            if ("00".equals(webServiceVO.getResponseCode())) {

                if (appUserModel != null) {
                    ThreadLocalAppUser.setAppUserModel(appUserModel);
                    bWrapper.setBasePersistableModel(appUserModel);
                    bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                    uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                    ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                }

                if (uda.getLoginAttemptCount() == 3) {
                    webServiceVO.setResponseCode("10");
                    webServiceVO.setResponseCodeDescription("Your account has been blocked due to invalid attempts");
                    this.logger.info("agentAccountAuthentication => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Agent Id : " + webServiceVO.getAgentId() + ",  CNIC : " + webServiceVO.getCnicNo()); //+ ", Amount : " + webServiceVO.getTransactionAmount());
                    return webServiceVO;
                }

                int counter = uda.getLoginAttemptCount();
                String pin = EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", uda.getPin());
                if (!pin.equals(webServiceVO.getLoginPin())) {
                    counter++;
                    uda.setLoginAttemptCount(counter);
                    baseWrapper.setBasePersistableModel(uda);
                    this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);

                    if (uda.getLoginAttemptCount() == 3) {
                        uda.setAccountLocked(true);
                        baseWrapper.setBasePersistableModel(uda);
                        this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);

                        appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_WARM);
                        baseWrapper.setBasePersistableModel(appUserModel);
                        this.getCommonCommandManager().updateAppUser(baseWrapper);
                    }

                    webServiceVO.setResponseCode(FonePayResponseCodes.INVALID_LOGIN_PIN);
                    webServiceVO.setResponseCodeDescription("Invalid PIN");
                } else {
                    RetailerContactModel retailerContactModel = new RetailerContactModel();
                    retailerContactModel.setRetailerContactId(appUserModel.getRetailerContactId());
                    baseWrapper.setBasePersistableModel(retailerContactModel);
                    baseWrapper = getCommonCommandManager().loadRetailerContact(baseWrapper);
                    retailerContactModel = (RetailerContactModel) baseWrapper.getBasePersistableModel();
                    RetailerModel retailerModel = loadRetailerModel(retailerContactModel.getRetailerId());

                    Long statusId = OlaStatusConstants.ACCOUNT_STATUS_ACTIVE;
                    AccountModel accountModel = null;
                    accountModel = getCommonCommandManager().getAccountModelByCnicAndCustomerAccountTypeAndStatusId(appUserModel.getNic(), CustomerAccountTypeConstants.RETAILER, statusId);
                    if (accountModel != null) {

                        Long accountId = accountModel.getAccountId();
                        Date currentDate = new Date();
                        Calendar cal = GregorianCalendar.getInstance();
                        Date startDate;
                        //daily debit consumed
                        dailyDebitConsumed = getCommonCommandManager().getDailyConsumedBalance(accountId, TransactionTypeConstants.DEBIT, currentDate, null);
                        //daily credit consumed
                        dailyCreditConsumed = getCommonCommandManager().getDailyConsumedBalance(accountId, TransactionTypeConstants.CREDIT, currentDate, null);
                        cal.setTime(new Date());
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = cal.getTime();

                        monthlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
                        //monthly credit consumed
                        monthlyCreditConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);
                        //yearly debit consumed
                        cal.setTime(new Date());
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        cal.set(Calendar.MONTH, 0);
                        startDate = cal.getTime();

                        yearlyDebitConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.DEBIT, startDate, currentDate);
                        //yearly credit consumed
                        yearlyCreditConsumed = getCommonCommandManager().getConsumedBalanceByDateRange(accountId, TransactionTypeConstants.CREDIT, startDate, currentDate);
                        remainingLimits = calculateLimits(dailyDebitConsumed, dailyCreditConsumed, monthlyDebitConsumed, monthlyCreditConsumed,
                                yearlyDebitConsumed, yearlyCreditConsumed, CustomerAccountTypeConstants.RETAILER);

                        uda.setLoginAttemptCount(new Integer(0));
                        baseWrapper.setBasePersistableModel(uda);
                        this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);
                        webServiceVO.setResponseCode("00");
                        webServiceVO.setResponseCodeDescription("Successful");
//                        webServiceVO.setDailyCreditLimit(remainingLimits.get(0));
//                        webServiceVO.setDailyDebitLimit(remainingLimits.get(1));
//                        webServiceVO.setMonthlyCreditLimit(remainingLimits.get(2));
//                        webServiceVO.setMonthlyDebitLimit(remainingLimits.get(3));
//                        webServiceVO.setAccountTitle(appUserModel.getFirstName() + " " + appUserModel.getLastName());
//                        webServiceVO.setAgentNetwork(String.valueOf(retailerModel.getDistributorId()));


                    }
                }
            }

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else {
                logger.error("[FonePaySwitchController.agentAccountAuthentication] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.agentAccountAuthentication] Error occured: " + e.getMessage(), e);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("agentAccountAuthentication => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentLoginPinGeneration(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentLoginPin] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String loginPin = webServiceVO.getLoginPin();

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getAgentMobileNumber());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_lOGIN_PIN);
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentLoginPin] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
            fonePayMessageVO.setMobileNo(appUserModel.getMobileNo());
            fonePayMessageVO = getFonePayManager().makevalidateAgent(fonePayMessageVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                //   uda = this.getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());

                BaseWrapper baseWrapper = new BaseWrapperImpl();

                String password = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, loginPin);
                appUserModel.setPassword(password);
                uda.setPin(password);
                uda.setLoginAttemptCount(new Integer(0));

                baseWrapper.setBasePersistableModel(appUserModel);
                this.getCommonCommandManager().updateAppUser(baseWrapper);

                baseWrapper.setBasePersistableModel(uda);
                this.getCommonCommandManager().updateUserDeviceAccounts(baseWrapper);

                String customerSMS = this.getMessageSource().getMessage("loginSmsViaAPI", null, null);
                String userName = webServiceVO.getAgentMobileNumber();

                baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(userName, customerSMS));
                getCommonCommandManager().sendSMSToUser(baseWrapper);

                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.loginPin] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("agentLoginPin => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentLoginPinReset(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentLoginPinChange] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String oldLoginPin = webServiceVO.getOldLoginPin();
        oldLoginPin = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, oldLoginPin);//EncoderUtils.encodeToSha(randomPin);

        String newLoginPin = webServiceVO.getNewLoginPin();
        newLoginPin = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, newLoginPin);//EncoderUtils.encodeToSha(randomPin);

        String confirmLoginPin = webServiceVO.getConfirmLoginPin();
        confirmLoginPin = com.inov8.microbank.common.util.EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, confirmLoginPin);//EncoderUtils.encodeToSha(randomPin);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_LOGIN_PIN_CHANGE);
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null, null, webServiceVO.getAgentMobileNumber());
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentLoginPinChange] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
            fonePayMessageVO.setMobileNo(appUserModel.getMobileNo());
            fonePayMessageVO = getFonePayManager().makevalidateAgent(fonePayMessageVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                //        uda = this.getCommonCommandManager().loadUserDeviceAccountByUserId(appUserModel.getUsername());
                String pin = EncryptionUtil.decryptWithAES("682ede816988e58fb6d057d9d85605e0", uda.getPin());
                if (!pin.equals(webServiceVO.getOldLoginPin())) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_LOGIN_PIN);
                    webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.INVALID_LOGIN_PIN));
                    this.logger.info("agentLoginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
                    return webServiceVO;
                }

                if (webServiceVO.getOldLoginPin().equals(webServiceVO.getNewLoginPin())) { //params for login pin to be added.. i.e. old new etc
                    webServiceVO.setResponseCode(FonePayResponseCodes.SAME_PIN);
                    webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.SAME_PIN));
                    this.logger.info("agentLoginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
                    return webServiceVO;
                }

                if (!webServiceVO.getNewLoginPin().equals(webServiceVO.getConfirmLoginPin())) { //params for login pin to be added.. i.e. old new etc
                    webServiceVO.setResponseCode(FonePayResponseCodes.PIN_MISMATCHED);
                    webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.PIN_MISMATCHED));
                    this.logger.info("agentLoginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
                    return webServiceVO;
                }

                BaseWrapper bWrapper = new BaseWrapperImpl();
                bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
                bWrapper.putObject(CommandFieldConstants.KEY_PIN, oldLoginPin);
                bWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, newLoginPin);
                bWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, confirmLoginPin);
                bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
                String response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_LOGIN_PIN_CHANGE);
                if (MfsWebUtil.isErrorXML(response)) {
                    return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                }

                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                webServiceVO.setResponseContentXML(response);
            }
        } catch (CommandException ex) {
            logger.error("Error Occurred while changing Login Pin for Agent Id :: " + webServiceVO.getAgentId());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);

        } catch (Exception ex) {
            logger.error("Error Occurred while changing Login Pin for Agent Id :: " + webServiceVO.getAgentId());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("loginPinChange => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentMpinGeneration(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = null;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L),
                    Long.valueOf(Long.parseLong(CommandFieldConstants.CMD_MIGRATED_PIN_CHG)),
                    webServiceVO.getMobileNo());
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentMpinGeneration] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            if (!webServiceVO.getMobilePin().equals(webServiceVO.getConfirmMpin())) {
                webServiceVO.setResponseCode(FonePayResponseCodes.PIN_MISMATCHED);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.PIN_MISMATCHED));
                return webServiceVO;
            }

            BaseWrapper bWrapper = new BaseWrapperImpl();
            this.logger.info("Third Party MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            bWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, webServiceVO.getMobilePin());
            bWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, webServiceVO.getConfirmMpin());
            bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
            bWrapper.putObject("IS_FORCEFUL", "1");
            String response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_MIGRATED_PIN_CHG);
            if (MfsWebUtil.isErrorXML(response)) {
                return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
            }
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(response);
        } catch (Exception ex) {
            logger.error("Error Occurred while MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentMpinReset(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = null;
        ActionLogModel actionLogModel = null;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN);
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null,
                    Long.valueOf(Long.parseLong(CommandFieldConstants.CMD_VERIFLY_PIN_CHANGE)),
                    webServiceVO.getMobileNo());
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentMpinReset] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            if (webServiceVO.getOldMpin().equals(webServiceVO.getMobilePin())) {
                webServiceVO.setResponseCode(FonePayResponseCodes.SAME_PIN);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.SAME_PIN));
                return webServiceVO;
            }

            if (!webServiceVO.getMobilePin().equals(webServiceVO.getConfirmMpin())) {
                webServiceVO.setResponseCode(FonePayResponseCodes.PIN_MISMATCHED);
                webServiceVO.setResponseCodeDescription(MessageUtil.getMessage("fonepay.error." + FonePayResponseCodes.PIN_MISMATCHED));
                return webServiceVO;
            }

            this.logger.info("Third Party MPIN Change Request for Mobile # :: " + webServiceVO.getMobileNo());
            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            bWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOldMpin());
            bWrapper.putObject(CommandFieldConstants.KEY_NEW_PIN, webServiceVO.getMobilePin());
            bWrapper.putObject(CommandFieldConstants.KEY_CONF_PIN, webServiceVO.getConfirmMpin());
            bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
            String response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_VERIFLY_PIN_CHANGE);
            if (MfsWebUtil.isErrorXML(response)) {
                return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
            }
            String responseCode = MiniXMLUtil.getTagTextValue(response, "//params/param[@name='CODE']");
            if (responseCode != null && !responseCode.equals("") && !responseCode.equals("0")) {
                webServiceVO.setResponseCode(responseCode);
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_OLD_PIN);
                return webServiceVO;
            }
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(response);
        } catch (CommandException ex) {
            logger.error("Error Occurred while MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);

        } catch (Exception ex) {
            logger.error("Error Occurred while MPIN Registration for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentBalanceInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentBalanceInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        Boolean isAtmChannel = Boolean.FALSE;
        try {
            CommonCommandManager commonCommandManager = this.getCommonCommandManager();
            ActionLogModel actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, Long.valueOf(CommandFieldConstants.CMD_CHK_ACC_BAL), webServiceVO.getAgentMobileNumber());
            String terminalid = null;
            if (webServiceVO.getTerminalId() != null)
                terminalid = webServiceVO.getTerminalId();
            if (terminalid != null && terminalid.equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                isAtmChannel = Boolean.TRUE;
            } else {
                webServiceVO = this.validateRRN(webServiceVO);
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }


            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_BALANCE_INQUIRY);
            if (terminalid != null && terminalid.equals(FonePayConstants.DEBIT_CARD_CHANNEL)) {
                DebitCardModel debitCardModel = commonCommandManager.getDebitCardModelDao().getDebitCardModelByCardNumber(webServiceVO.getCardNo());
                if (debitCardModel != null) {
                    DebitCardUtill.verifyDebitCard(webServiceVO, debitCardModel);
                    if (!webServiceVO.getResponseCode().equals("00"))
                        return webServiceVO;
                    webServiceVO.setMobileNo(debitCardModel.getMobileNo());
                }
            }
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentBalanceInquiry] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = commonCommandManager.getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (appUserModel == null) {
                logger.info("[FonePaySwitchController.agentBalanceInquiry] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            if (!isAtmChannel) {
                this.userValidation(webServiceVO, CommandFieldConstants.CMD_CHK_ACC_BAL);
                if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }
            Long agentId;
            if (appUserModel.getCustomerId() != null)
                agentId = appUserModel.getCustomerId();
            else
                agentId = appUserModel.getAppUserId();

            Long paymentModeId;
            paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

            AccountInfoModel accountInfoModel = commonCommandManager.getAccountInfoModel(agentId, paymentModeId);
            SmartMoneyAccountModel smartMoneyAccountModel = commonCommandManager.getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, paymentModeId);
            // webServiceVO=getFonePayManager().getOlaBalance(webServiceVO);
            String balance = getCommonCommandManager().getAccountBalance(accountInfoModel, smartMoneyAccountModel);
            webServiceVO.setBalance(balance);
            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            // webServiceVO=getFonePayManager().saveBalnceInquiryInfo(webServiceVO);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9010) {
                FonePayUtils.prepareErrorResponse(webServiceVO, String.valueOf(FonePayResponseCodes.INVALID_PIN));
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                }
            } else {
                logger.error("[FonePaySwitchController.verifyOTP] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.agentBalanceInquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {

                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalActionLog.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.Agent Balance Inquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentIbftInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentIbftInquiry] Start:: ");
        String isValidationRequired = webServiceVO.getReserved1();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String xml = "";
        String messageType = "Agent IBFT Inquiry";
        String cmdId = CommandFieldConstants.WALLET_TO_CORE_INQUIRY;

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, CommandFieldConstants.WALLET_TO_CORE_INQUIRY);
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentIbftInquiry] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.AGENT_BB_TO_IBFT);
            baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE, webServiceVO.getPurposeOfPayment());
            baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, webServiceVO.getMobileNo());
//            baseWrapper.putObject(CommandFieldConstants.KEY_BB_ACC_ID, webServiceVO.getBranchlessAccountId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TO_BANK_IMD, webServiceVO.getBankId());
            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_BANK_NAME, webServiceVO.getBankName());
//            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_BRANCH_NAME, webServiceVO.getBranchName());
//            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_IBAN, webServiceVO.getBenificieryIban());
//            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_TITLE, webServiceVO.getAccountTitle());
//            baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT,webServiceVO.getCharges());
//            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTotalAmount());
//            baseWrapper.putObject(CommandFieldConstants.KEY_TX_PROCESS_AMNT, webServiceVO.getTransactionProcessingAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getCoreAccountId());//destinationaccountnumber
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());//transactionamount
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.WALLET_TO_CORE_INQUIRY);

            String COREACTL = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CORE_ACCOUNT_TITLE_NODEREF);
            String branchlessBankId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BRANCHLESS_BANK_ACT_NODEREF);
            String beneBranchName = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BENE_BRANCH_NAME_NODEREF);
            String beneIBAN = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BENE_IBAN_NODEREF);
            String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF);
            String tranAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TXAM_NODEREF);
            String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TAMT_NODEREF);
            String tranProccessingAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_PROCESS_AMN_NODEREF);

            if (isValidationRequired != null && isValidationRequired.equals("02")) {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                logger.info("The plain otp is " + otp);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                webServiceVO.setOtpPin(otp);
                getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
            }
            webServiceVO.setResponseContentXML(xml);
//            webServiceVO.setMobileNo(webServiceVO.getMobileNo());
//            webServiceVO.setSenderAccountTitle(appUserModel.getFirstName() + " " + appUserModel.getLastName());
            webServiceVO.setCoreAccountId(webServiceVO.getCoreAccountId());
            webServiceVO.setAccountTitle(COREACTL);
//            webServiceVO.setRecieverAccountNumber(webServiceVO.getDestinationAccountNumber());
            webServiceVO.setTransactionProcessingAmount(tranProccessingAmount);
            webServiceVO.setTransactionAmount(tranAmount);
            webServiceVO.setTotalAmount(totalAmount);
            webServiceVO.setCommissionAmount(charges);
            webServiceVO.setBenificieryIban(beneIBAN);
            webServiceVO.setBankName(webServiceVO.getBankName());
            webServiceVO.setBranchName(beneBranchName);
            webServiceVO.setBankId(webServiceVO.getBankId());
            webServiceVO.setBranchlessAccountId(branchlessBankId);
//            webServiceVO.setDestinationBankImd(webServiceVO.getDestinationBankImd());
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");

        } catch (CommandException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 9051) {
                webServiceVO.setResponseCode("20");
                webServiceVO.setResponseCodeDescription("Transaction Timeout. Please try again");
            } else if (e.getErrorCode() == 9053) {
                webServiceVO.setResponseCode("154");
                webServiceVO.setResponseCodeDescription("The entered Account # is invalid. Please retry with a valid Account #.");
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.agentIbftInquiry] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("WalletToCoreInfoCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentIbftPayment(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentIbftPayment] Start:: ");
        String isValidationRequired = webServiceVO.getReserved1();
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.WALLET_TO_CORE_INQUIRY);
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, CommandFieldConstants.WALLET_TO_CORE_PAYMENT);
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentIbftPayment] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.WALLET_TO_CORE_INQUIRY);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
            baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.AGENT_BB_TO_IBFT);
            baseWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getMobilePin());
//            baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getSenderMobileNumber());
            baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, webServiceVO.getAgentMobileNumber());
//            baseWrapper.putObject(CommandFieldConstants.KEY_CC_FROM_BANK_IMD, "603733");
            baseWrapper.putObject(CommandFieldConstants.KEY_TO_BANK_IMD, webServiceVO.getBankId());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_NO, webServiceVO.getCoreAccountId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE, webServiceVO.getPurposeOfPayment());
//            baseWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE_CODE, webServiceVO.getPurposeOfPayment());
            baseWrapper.putObject(CommandFieldConstants.KEY_CORE_ACC_TITLE, webServiceVO.getAccountTitle());
//            baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_TITLE_AGNETMATE, webServiceVO.getRecieverAccountTilte());
            baseWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_BANK_NAME, webServiceVO.getBankName());
            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_BRANCH_NAME, webServiceVO.getBranchName());
            baseWrapper.putObject(CommandFieldConstants.KEY_BENE_IBAN, webServiceVO.getBenificieryIban());
//            baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
//            baseWrapper.putObject(CommandFieldConstants.KEY_COMM_AMOUNT, "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
            baseWrapper.putObject(CommandFieldConstants.KEY_TOTAL_AMOUNT, webServiceVO.getTotalAmount());
            baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());

            xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.WALLET_TO_CORE_PAYMENT);

//            String charges = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF);
            String transId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
            String tranProccessingAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_PROCESS_AMN_NODEREF);


            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
            webServiceVO.setAgentMobileNumber(webServiceVO.getAgentMobileNumber());
            webServiceVO.setBankName(webServiceVO.getBankName());
            webServiceVO.setBranchName(webServiceVO.getBranchName());
            webServiceVO.setBenificieryIban(webServiceVO.getBenificieryIban());
//            webServiceVO.setCommissionAmount(charges);
            webServiceVO.setCoreAccountId(webServiceVO.getCoreAccountId());
            webServiceVO.setAccountTitle(webServiceVO.getAccountTitle());
            webServiceVO.setTransactionId(transId);
            webServiceVO.setTransactionProcessingAmount(tranProccessingAmount);
            webServiceVO.setTransactionAmount(webServiceVO.getTransactionAmount());
            webServiceVO.setTotalAmount(webServiceVO.getTotalAmount());

        } catch (CommandException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 8062) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    this.logger.error("[FonePaySwitchController.agentIbftPayment] Command Exception occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }

            } else if (e.getErrorCode() == 8064) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8090) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.RECIPIENT_MOBILE_NUMBER.toString());
            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED.toString());
            } else if (e.getErrorCode() == 8059) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE.toString());
            } else if (e.getErrorCode() == 9045) {
                this.logger.error("[FonePaySwitchController.agentIbftPayment] Command Exception occured: " + e.getMessage(), e);
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MEMBER_BANK_NOT_FOUND.toString());
            } else if (e.getErrorCode() == 9052) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.TRXN_AMOUNT_GREATER_THAN_LIMIT.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else {
                this.logger.error("[FonePaySwitchController.agentIbftPayment] Command Exception occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.agentIbftPayment] Error occured: " + ex.getMessage(), ex);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("WalletToCorePaymentCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO agentCashDepositInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentCashDepositInquiry] Start:: ");
        String xml = "";
        String isValidationRequired = webServiceVO.getReserved1();
        FonePayLogModel fonePayLogModel = null;
        AppUserModel customerAppUserModel = new AppUserModel();
        AppUserModel agentAppUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String messageType = "Agent Cash Deposit Inquiry";
        String cmdId = CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND;

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) < 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            if (!(webServiceVO.getProductID()).equals(ProductConstantsInterface.CASH_DEPOSIT.toString())) {
                logger.info("[FonePaySwitchController.agentCashDepositInquiry] Product ID is Incorrect :: " + webServiceVO.getProductID());
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription("Product ID is Incorrect.");
                return webServiceVO;
            }
//            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (webServiceVO.getChannelId().equalsIgnoreCase(MessageUtil.getMessage("EcofinChannelId"))) {
                EcofinSubAgentModel ecofinSubAgent = ecofinSubAgentDAO.findByPrimaryKey(Long.valueOf(webServiceVO.getReserved3()));

                if (ecofinSubAgent == null) {
                    this.logger.error("Ecofin Sub Agent Error:");
                }
            }
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());

            if (uda == null) {
                logger.info("[FonePaySwitchController.agentCashDepositInquiry] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }

            customerAppUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (customerAppUserModel == null) {
                logger.info("[FonePaySwitchController.agentCashDepositInquiry] Customer Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Customer Not Found.");
                return webServiceVO;
            }

            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, customerAppUserModel)) {
                return webServiceVO;
            }

            agentAppUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            String agentMobileNo = agentAppUserModel.getMobileNo();
            String agentPhoneNo = webServiceVO.getAgentMobileNumber();
//            String agentPhoneNo = MessageUtil.getMessage("EcofinMobileNumber");
            if (!agentMobileNo.equals(agentPhoneNo)) {
                logger.info("[FonePaySwitchController.agentCashDepositInquiry] Agent Mobile # :: " + webServiceVO.getAgentMobileNumber() + " is incorrect.");
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Mobile No is Incorrect.");
                return webServiceVO;
            } else if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, agentAppUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN_INFO);

            bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
            bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CASH_DEPOSIT);
            bWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, webServiceVO.getAgentMobileNumber());
            bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
            bWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());

            xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND);

            String commission_amount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF);
            String proccessing_amount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_PROCESS_AMN_NODEREF);
            String total_amount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
            String name = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.NAME_NODEREF);
            String cnic = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CNIC_NODEREF);

            if (isValidationRequired != null && isValidationRequired.equals("02")) {
                ///Generate OTP and store in MiniTransaction
                String otp = CommonUtils.generateOneTimePin(5);
                logger.info("The plain otp is " + otp);
                String encryptedPin = EncoderUtils.encodeToSha(otp);
                getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                webServiceVO.setOtpPin(otp);
                getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
            }

            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
            webServiceVO.setMobileNo(webServiceVO.getMobileNo());
            webServiceVO.setCommissionAmount(commission_amount);
            webServiceVO.setConsumerName(name);
            webServiceVO.setCnicNo(cnic);
            webServiceVO.setTransactionProcessingAmount(proccessing_amount);
            webServiceVO.setTotalAmount(total_amount);


        } catch (CommandException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 9051) {
                webServiceVO.setResponseCode("20");
                webServiceVO.setResponseCodeDescription("Transaction Timeout. Please try again");
            } else if (e.getErrorCode() == 9053) {
                webServiceVO.setResponseCode("154");
                webServiceVO.setResponseCodeDescription("The entered Account # is invalid. Please retry with a valid Account #.");
            } else if (e.getErrorCode() == 9001) {
                if (e.getMessage().equals("Upgrade Customer Account From L0 to L1.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                } else if (e.getMessage().equals("Insufficient customer balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    webServiceVO.setResponseCode(FonePayResponseCodes.TRXN_AMOUNT_LESSER_THAN_LIMIT);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else if (e.getErrorCode() == 9052) {
                webServiceVO.setResponseCode(FonePayResponseCodes.TRXN_AMOUNT_GREATER_THAN_LIMIT);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.agentCashDepositInquiry] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.verifyAccount] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.agentCashDepositInquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }


    @Override
    public WebServiceVO agentCashDepositPayment(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.agentCashDepositPayment] Start:: ");
        String mobileNo = webServiceVO.getMobileNo();
        String trxnAmount = webServiceVO.getTransactionAmount();
        String consumerNo = webServiceVO.getConsumerNo();
        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
        String commissionAmount = webServiceVO.getCommissionAmount();
        String PID = webServiceVO.getProductID();
        String charges = "";
        String xml = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        bWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getOtpPin());
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_IN);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L), Long.valueOf(Long.parseLong("121")), webServiceVO.getAgentMobileNumber());
            if (webServiceVO.getChannelId().equalsIgnoreCase(MessageUtil.getMessage("EcofinChannelId"))) {
                EcofinSubAgentModel ecofinSubAgent = ecofinSubAgentDAO.findByPrimaryKey(Long.valueOf(webServiceVO.getReserved3()));
                if (ecofinSubAgent == null) {
                    this.logger.error("Ecofin Sub Agent Not Found:");
                }
            }
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentCashDepositPayment] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }
            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());

            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            this.userValidation(webServiceVO, CommandFieldConstants.CASH_DEPOSIT_INFO_COMMAND);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            String response = null;
            if (webServiceVO.getReserved4().equals("1")) {
                AppUserModel customerAppUserModel = new AppUserModel();
                customerAppUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
                if (customerAppUserModel == null) {
                    logger.info("[FonePaySwitchController.agentCashDepositInquiry] Customer Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                    webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                    webServiceVO.setResponseCodeDescription("Customer Not Found.");
                    return webServiceVO;
                }

                bWrapper = this.prepareNadraRequest(customerAppUserModel, webServiceVO);
                bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, PID);
                bWrapper.putObject(CommandFieldConstants.KEY_REMITTANCE_TYPE, "MONEY_TRANSFER_RECEIVE");
                response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CUSTOMER_NADRA_VERIFICATION);
                if (MfsWebUtil.isErrorXML(response)) {
                    logger.info("CustomerNadraVerificationCommand response for Mobile # :: " + appUserModel.getMobileNo() + "\n" + response);
                    return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                }

                bWrapper.putObject("CMOB", mobileNo);
                bWrapper.putObject("TPAM", txProcessingAmount);
                bWrapper.putObject("CAMT", commissionAmount);
                bWrapper.putObject("TAMT", trxnAmount);
                bWrapper.putObject("TXAM", trxnAmount);
                bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, PID);
                bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
                bWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
                bWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, webServiceVO.getAgentMobileNumber());
                bWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getMobilePin());
                bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
                bWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, webServiceVO.getPaymentMode());
                bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
                bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
                bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME, webServiceVO.getPaymentMode());

                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CASH_DEPOSIT);
            } else {
                bWrapper.putObject("CMOB", mobileNo);
                bWrapper.putObject("TPAM", txProcessingAmount);
                bWrapper.putObject("CAMT", commissionAmount);
                bWrapper.putObject("TAMT", trxnAmount);
                bWrapper.putObject("TXAM", trxnAmount);
                bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, ProductConstantsInterface.CASH_DEPOSIT);
                bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
                bWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
                bWrapper.putObject(CommandFieldConstants.KEY_AGENT_MOBILE, webServiceVO.getAgentMobileNumber());
                bWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getMobilePin());
                bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
                bWrapper.putObject(CommandFieldConstants.KEY_PAYMENT_MODE, webServiceVO.getPaymentMode());
                bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
                bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
                bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_PRODUCT_NAME, webServiceVO.getPaymentMode());

                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CASH_DEPOSIT);
            }
            String transId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);
            String balance = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BALANCE_AMOUNT_NODEREF);
            String transProccessingAmt = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_PROCESS_AMN_NODEREF);
            String transCommisssionAmt = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF);
            String totalAmt = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);

            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setTransactionId(transId);
            webServiceVO.setBalance(balance);
            webServiceVO.setTransactionProcessingAmount(transProccessingAmt);
            webServiceVO.setCommissionAmount(transCommisssionAmt);
            webServiceVO.setTotalAmount(totalAmt);
        } catch (CommandException e) {
            if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.agentCashDepositPayment] Command Exception Error occured:" + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());
            } else if (e.getErrorCode() == 111) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.FINGER_DOES_NOT_EXIT.toString());
            } else if (e.getErrorCode() == 118) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.NADRA_FINGER_EXAUST_ERROR.toString());
            } else if (e.getErrorCode() == 120) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_INPUT_FINGER_TEMPLETE.toString());
            } else if (e.getErrorCode() == 121) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.FINGER_PRINT_NOT_MATCHED.toString());
            } else if (e.getErrorCode() == 122) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_FINGER_INDEX.toString());
            } else if (e.getErrorCode() == 123) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_FINGER_TEMPLETE_TYPE.toString());
            } else {
                logger.error("[FonePaySwitchController.agentCashDepositPayment] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.agentCashDepositPayment] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("Agent Cash Deposit Payment => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }


    @Override
    public WebServiceVO agentCashWithdrawalInquiry(WebServiceVO webServiceVO) {
//        String isOtpReq = "1";
        String mobileNo = webServiceVO.getMobileNo();
        String dateTime = webServiceVO.getDateTime();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String channelId = webServiceVO.getChannelId();
        String terminalId = webServiceVO.getTerminalId();
        String txAmount = webServiceVO.getTransactionAmount();
        String PID = webServiceVO.getProductID();
        String agentmobileno = webServiceVO.getAgentMobileNumber();
        String xml = "";
        BaseWrapper bWrapper = new BaseWrapperImpl();
        String cmdId = CommandFieldConstants.CMD_CASH_OUT_INFO;
        String messageType = "Agent Cash Out";

        this.logger.info("[FonePay agentCashWithdrawalInquiry] [Mobile:" + mobileNo + ", Trx Amount:" + txAmount + "]");

        bWrapper.putObject("CMOB", mobileNo);
        bWrapper.putObject("DATE", dateTime);
        bWrapper.putObject("RRN", rrn);
        bWrapper.putObject("CHANNELID", channelId);
        bWrapper.putObject("TERMINALID", terminalId);
        bWrapper.putObject("TXAM", txAmount);
        bWrapper.putObject("AMOB", agentmobileno);
        bWrapper.putObject("PID", PID);
        bWrapper.putObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED, webServiceVO.getIsOtpRequest());
        bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);

        FonePayMessageVO fonePayMessageVO = new FonePayMessageVO();
        FonePayLogModel fonePayLogModel = null;
        AppUserModel agentAppUserModel = new AppUserModel();
        AppUserModel customerAppUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }

            if (!(webServiceVO.getProductID()).equals(ProductConstantsInterface.CASH_WITHDRAWAL.toString())) {
                logger.info("[FonePaySwitchController.agentCashWithdrawalInquiry] Product ID is Incorrect :: " + webServiceVO.getProductID());
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription("Product ID is Incorrect.");
                return webServiceVO;
            }
            if (webServiceVO.getChannelId().equalsIgnoreCase(MessageUtil.getMessage("EcofinChannelId"))) {
                EcofinSubAgentModel ecofinSubAgent = ecofinSubAgentDAO.findByPrimaryKey(Long.valueOf(webServiceVO.getReserved3()));

                if (ecofinSubAgent == null) {
                    this.logger.error("Ecofin Sub Agent Error:");
                }
            }
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());

            if (uda == null) {
                logger.info("[FonePaySwitchController.agentCashWithdrawalInquiry] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }


            customerAppUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (customerAppUserModel == null) {
                logger.info("[FonePaySwitchController.agentCashWithdrawalInquiry] Customer Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Customer Not Found.");
                return webServiceVO;
            }

            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, customerAppUserModel)) {
                return webServiceVO;
            }

            agentAppUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            String agentMobileNo = agentAppUserModel.getMobileNo();
            String agentPhoneNo = webServiceVO.getAgentMobileNumber();
//            String agentPhoneNo = MessageUtil.getMessage("EcofinAgentId");
            if (!agentMobileNo.equals(agentPhoneNo)) {
                logger.info("[FonePaySwitchController.agentCashWithdrawalInquiry] Agent Mobile # :: " + webServiceVO.getAgentMobileNumber() + " is incorrect.");
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Mobile No is Incorrect.");
                return webServiceVO;
            } else if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, agentAppUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_OUT_INFO);

            fonePayMessageVO.setMobileNo(webServiceVO.getMobileNo());
            fonePayMessageVO = getFonePayManager().makevalidateAgent(fonePayMessageVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CASH_OUT_INFO);

                String commission_amount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_COMMISSION_CHARGES_NODEREF);
                String proccessing_amount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_PROCESS_AMN_NODEREF);
                String total_amount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                String name = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.CNAME_NODEREF);

                webServiceVO.setConsumerName(name);
                webServiceVO.setCommissionAmount(commission_amount);
                webServiceVO.setTransactionProcessingAmount(proccessing_amount);
                webServiceVO.setTotalAmount(total_amount);

//                if (isOtpReq.equals("1")) {
//                    ///Generate OTP and store in MiniTransaction
//                    String otp = CommonUtils.generateOneTimePin(5);
//                    logger.info("The plain otp is " + otp);
//                    String encryptedPin = EncoderUtils.encodeToSha(otp);
//                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
//                    webServiceVO.setOtpPin(otp);
//                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
//                }
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");
            }
        } catch (CommandException e) {
            this.logger.error("[FonePaySwitchController.agentCashWithdrawalInquiry] Command Exception occured: " + e.getMessage(), e);
            if (e.getErrorCode() == 9001) {
                if (e.getMessage().equals("Upgrade Customer Account From L0 to L1.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                } else if (e.getMessage().equals("Insufficient customer balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    webServiceVO.setResponseCode(FonePayResponseCodes.TRXN_AMOUNT_LESSER_THAN_LIMIT);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else if (e.getErrorCode() == 9052) {
                webServiceVO.setResponseCode(FonePayResponseCodes.TRXN_AMOUNT_GREATER_THAN_LIMIT);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            } else {
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception ex) {
            this.logger.error("[FonePaySwitchController.agentCashWithdrawalInquiry] Error occured: " + ex.getMessage(), ex);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("CashWithdrawalInfoCommand => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());

        return webServiceVO;
    }

    @Override
    public WebServiceVO agentCashWithdrawalPayment(WebServiceVO webServiceVO) {
        String encrypType = "1";
    /*if(null!=webServiceVO.getReserved1() && !"".equals(webServiceVO.getReserved1()))
        isOtpReq=webServiceVO.getReserved1();*/
        String transactionDateTime = webServiceVO.getTransactionDateTime();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String channelId = webServiceVO.getChannelId();
        String terminalId = webServiceVO.getTerminalId();
        String custMobileNumber = webServiceVO.getMobileNo();
        String agentMobileNumber = webServiceVO.getAgentMobileNumber();
        String cnic = webServiceVO.getCnicNo();
        String tAmount = webServiceVO.getTotalAmount();
        String txAmount = webServiceVO.getTransactionAmount();
        String mpin = webServiceVO.getMobilePin();
        String PID = webServiceVO.getProductID();
        String otp = webServiceVO.getOtpPin();
        String tranID = webServiceVO.getTransactionId();
        String txProcessingAmount = webServiceVO.getTransactionProcessingAmount();
        String commissionAmount = webServiceVO.getCommissionAmount();
        String xml = "";
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        BaseWrapper bWrapper = new BaseWrapperImpl();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();

        BaseWrapper idWrapper = new BaseWrapperImpl();
//        idWrapper.putObject(CommandFieldConstants.KEY_PIN, webServiceVO.getMobilePin());
        idWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, webServiceVO.getMobileNo());
        idWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        idWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.CMD_CASH_OUT_INFO);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (webServiceVO.getTransactionAmount() == null || Double.valueOf(webServiceVO.getTransactionAmount()) <= 0) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);
            }
            if (webServiceVO.getChannelId().equalsIgnoreCase(MessageUtil.getMessage("EcofinChannelId"))) {
                EcofinSubAgentModel ecofinSubAgent = ecofinSubAgentDAO.findByPrimaryKey(Long.valueOf(webServiceVO.getReserved3()));

                if (ecofinSubAgent == null) {
                    this.logger.error("Ecofin Sub Agent Error:");
                }

            }
            uda = getCommonCommandManager().loadUserDeviceAccountByUserId(webServiceVO.getAgentId());
            if (uda == null) {
                logger.info("[FonePaySwitchController.agentCashWithdrawalPayment] Agent Not Found against the Agent Id :: " + webServiceVO.getAgentId());
                webServiceVO.setResponseCode(FonePayResponseCodes.AGENT_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Agent Not Found.");
                return webServiceVO;
            }

            appUserModel = getCommonCommandManager().getAppUserManager().loadRetailerAppUserModelByAppUserId(uda.getAppUserId());
            if (!getCommonCommandManager().checkActiveAgentAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_CASH_OUT);

//            if (appUserModel != null) {
//                ThreadLocalAppUser.setAppUserModel(appUserModel);
//                bWrapper.setBasePersistableModel(appUserModel);
//                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
//                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
//                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
//            }


            this.userValidation(webServiceVO, CommandFieldConstants.CMD_CASH_OUT_INFO);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            this.logger.info("[FonePay agentCashWithdrawalPayment] [Mobile:" + custMobileNumber + ", Trx Amount:" + txAmount + "]");

            String response = null;
            if (webServiceVO.getReserved4().equals("1")) {
                AppUserModel customerAppUserModel = new AppUserModel();
                customerAppUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
                if (customerAppUserModel == null) {
                    logger.info("[FonePaySwitchController.agentCashDepositInquiry] Customer Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                    webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                    webServiceVO.setResponseCodeDescription("Customer Not Found.");
                    return webServiceVO;
                }

                bWrapper = this.prepareNadraRequest(customerAppUserModel, webServiceVO);
                bWrapper.putObject("PID", PID);
                bWrapper.putObject(CommandFieldConstants.KEY_REMITTANCE_TYPE, "MONEY_TRANSFER_SEND");
                response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CUSTOMER_NADRA_VERIFICATION);
                if (MfsWebUtil.isErrorXML(response)) {
                    logger.info("CustomerNadraVerificationCommand response for Mobile # :: " + appUserModel.getMobileNo() + "\n" + response);
                    return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                }
                bWrapper.putObject("DATE", transactionDateTime);
                bWrapper.putObject("RRN", rrn);
                bWrapper.putObject("CHANNELID", channelId);
                bWrapper.putObject("TERMINALID", terminalId);
                bWrapper.putObject("CMOB", custMobileNumber);
                bWrapper.putObject("AMOB", agentMobileNumber);
                bWrapper.putObject("PIN", mpin);
                bWrapper.putObject("OTPIN", (ThirdPartyEncryptionUtil.encryptWithAES(XMLConstants.THIRD_PARTY_ENCRYPTION_KEY, otp)));
                bWrapper.putObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED, "1");
                bWrapper.putObject("TPAM", txProcessingAmount);
                bWrapper.putObject("CAMT", commissionAmount);
                bWrapper.putObject("TXAM", txAmount);
                bWrapper.putObject("CNIC", cnic);
                bWrapper.putObject("TAMT", tAmount);
                bWrapper.putObject("ENCT", encrypType);
                bWrapper.putObject("ID", tranID);
                bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
                bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
//            bWrapper.putObject("CSCD", consumerNo);
                bWrapper.putObject("PID", PID);
                bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
                bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());

                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CASH_OUT);
            } else {
                bWrapper.putObject("DATE", transactionDateTime);
                bWrapper.putObject("RRN", rrn);
                bWrapper.putObject("CHANNELID", channelId);
                bWrapper.putObject("TERMINALID", terminalId);
                bWrapper.putObject("CMOB", custMobileNumber);
                bWrapper.putObject("AMOB", agentMobileNumber);
                bWrapper.putObject("PIN", mpin);
                bWrapper.putObject("OTPIN", (ThirdPartyEncryptionUtil.encryptWithAES(XMLConstants.THIRD_PARTY_ENCRYPTION_KEY, otp)));
                bWrapper.putObject(CommandFieldConstants.KEY_IS_OTP_REQUIRED, "1");
                bWrapper.putObject("TPAM", txProcessingAmount);
                bWrapper.putObject("CAMT", commissionAmount);
                bWrapper.putObject("TXAM", txAmount);
                bWrapper.putObject("CNIC", cnic);
                bWrapper.putObject("TAMT", tAmount);
                bWrapper.putObject("ENCT", encrypType);
                bWrapper.putObject("ID", tranID);
                bWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
                bWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
//            bWrapper.putObject("CSCD", consumerNo);
                bWrapper.putObject("PID", PID);
                bWrapper.putObject("DTID", DeviceTypeConstantsInterface.WEB_SERVICE);
                bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());

                xml = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CASH_OUT);
            }
            String transId = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRX_ID_NODEREF);
            String balance = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.BALANCE_AMOUNT_NODEREF);
            webServiceVO.setResponseContentXML(xml);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");
            webServiceVO.setTransactionId(transId);
            webServiceVO.setBalance(balance);

        } catch (CommandException e) {
            if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.agentCashWithdrawalPayment] Command Exception Error occured:" + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 8063) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
            } else if (e.getErrorCode() == 8061) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
            } else if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 111) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.FINGER_DOES_NOT_EXIT.toString());
            } else if (e.getErrorCode() == 118) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.NADRA_FINGER_EXAUST_ERROR.toString());
            } else if (e.getErrorCode() == 120) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_INPUT_FINGER_TEMPLETE.toString());
            } else if (e.getErrorCode() == 121) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.FINGER_PRINT_NOT_MATCHED.toString());
            } else if (e.getErrorCode() == 122) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_FINGER_INDEX.toString());
            } else if (e.getErrorCode() == 123) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_FINGER_TEMPLETE_TYPE.toString());
            } else {
                logger.error("[FonePaySwitchController.agentCashWithdrawalPayment] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.agentCashWithdrawalPayment] Error occured: " + e.getMessage(), e);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("agentCashWithdrawalPayment => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());


        return webServiceVO;
    }


    @Override
    public WebServiceVO mpinVerification(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.mPin Verification] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_MPIN_Verification);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {

                this.validateMPIN(webServiceVO);

                if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
            }
        } catch (CommandException e) {

            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9010) {
                FonePayUtils.prepareErrorResponse(webServiceVO, String.valueOf(FonePayResponseCodes.INVALID_PIN));
            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                }
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else {
                logger.error("[FonePaySwitchController.verify Mpin] Command Exception Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.MpinVerification] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.MpinVerification] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }

    public List<SegmentModel> fetchSegment() {
        List<SegmentModel> list;
        this.commonCommandManager = getCommonCommandManager();
        SegmentModel segmentModel = new SegmentModel();
        segmentModel.setIsActive(true);
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        list = commonCommandManager.getSegmentDao().findByExample(segmentModel, null, null, exampleConfigHolderModel).getResultsetList();
        return list;
    }

    @Override
    public WebServiceVO listSegments(WebServiceVO webServiceVO) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        List<SegmentModel> result;
        try {

            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
//            baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_ID, webServiceVO.getAgentId());
            result = fetchSegment();
            List<SegmentList> list = new ArrayList<>();
            for (SegmentModel model : result) {
                SegmentList segmentList = new SegmentList();
                segmentList.setSegmentId(String.valueOf(model.getSegmentId()));
                segmentList.setSegmentName(model.getName());
                list.add(segmentList);
            }
            webServiceVO.setSegmentNames(list);
//           webServiceVO.setResponseContentXML(result.toString());
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");

        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.segmentsList] Error occured: " + e.getMessage(), e);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        }
        this.logger.info("segmentsList => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO listCatalogs(WebServiceVO webServiceVO) {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        List<ProductCatalogModel> result;
        ProductCatalogModel catalogModel = new ProductCatalogModel();
        try {

            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
            baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
//            baseWrapper.putObject(CommandFieldConstants.KEY_AGENT_ID, webServiceVO.getAgentId());
            result = (List<ProductCatalogModel>) this.catalogDAO.fetchProductCatalog(catalogModel);
            List<CatalogList> list = new ArrayList<>();
            for (ProductCatalogModel model : result) {
                CatalogList catalogList = new CatalogList();
                catalogList.setCatalogId(String.valueOf(model.getProductCatalogId()));
                catalogList.setCatalogName(model.getName());
                list.add(catalogList);
            }
            webServiceVO.setCatalogNames(list);
//           webServiceVO.setResponseContentXML(result.toString());
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("Successful");

        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.listCatalogs] Error occured: " + e.getMessage(), e);

            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        }
        this.logger.info("listCatalogs => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription() + ",  Mobile No : " + webServiceVO.getMobileNo() + ",  CNIC : " + webServiceVO.getCnicNo() + ", Amount : " + webServiceVO.getTransactionAmount());
        return webServiceVO;
    }

    @Override
    public WebServiceVO l2AccountOpening(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        String mobileNumber = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String dateTime = webServiceVO.getDateTime();
        //Below parameter are set to change cnic issuance Date format change  for api dd-mm-yyyy to yyyy-mm-dd
        webServiceVO.setReserved10(String.valueOf(DeviceTypeConstantsInterface.WEB_SERVICE));

        this.logger.info("[FonePay L2 Open Account] [CNIC:" + cnic + ", Mobile:" + mobileNumber + ", RRN:" + rrn + ", DateTime:" + dateTime + "]");
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_ACCOUNT_OPENING_L2);

            AppUserModel webServiceAppUser = new AppUserModel();
            webServiceAppUser.setAppUserId(Long.valueOf(4L));
            ThreadLocalAppUser.setAppUserModel(webServiceAppUser);

            webServiceAppUser = getCommonCommandManager().loadAppUserByCnicAndType(cnic);
            if (webServiceAppUser != null) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST);
            }
            if (this.getCommonCommandManager().isCnicBlacklisted(cnic)) {
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
            }

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());

            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, Long.valueOf(1002L), Long.valueOf(Long.parseLong("121")), mobileNumber);

            int pendingTransactionCount = getCommonCommandManager().countCustomerPendingTrx(cnic);
            if (pendingTransactionCount > 0) {
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
            } else {
                pendingTransactionCount = getCommonCommandManager().countCustomerPendingTrxByMobile(mobileNumber);
                if (pendingTransactionCount > 0) {
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUST_HAS_PEND_TRXNS);
                } else {
                    webServiceVO = getFonePayManager().createL2Customer(webServiceVO, false);
                }
            }

//            if(!webServiceVO.getReserved8().equals("")) {
//                webServiceVO.setResponseCodeDescription(webServiceVO.getReserved8());
//            }
            actionLogAfterEnd(actionLogModel);
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.accountOpening] Error occured: " + e.getMessage(), e);
//            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
            this.logger.error("[FonePaySwitchController.verifyAccount] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
//                }
                webServiceVO.setAccountTitle(null);
            }
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("[FonePaySwitchController.accountOpening] (In End) Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

    @Override
    public WebServiceVO l2AccountUpgrade(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.l2AccountUpgrade] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        AppUserModel appUserModel = null;
        ActionLogModel actionLogModel = null;
        CustomerModel customerModel = new CustomerModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_L2_UPGRADE);
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null,
                    Long.parseLong(CommandFieldConstants.CMD_UPGRADE_L2_ACCOUNT),
                    webServiceVO.getMobileNo());

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
            }
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            this.userValidation(webServiceVO, CommandFieldConstants.CMD_UPGRADE_L2_ACCOUNT);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                String response = null;
                BaseWrapper bWrapper = new BaseWrapperImpl();

                if (customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0)) {
                    if (!webServiceVO.getReserved2().equals("1")) {
                        bWrapper = this.prepareNadraRequest(appUserModel, webServiceVO);
                        response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CUSTOMER_NADRA_VERIFICATION);
                        if (MfsWebUtil.isErrorXML(response)) {
                            logger.info("CustomerNadraVerificationCommand response for Mobile # :: " + appUserModel.getMobileNo() + "\n" + response);
                            return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                        }
//            response = "<msg id=\"181\"><params><param name=\"DTID\">8</param><param name=\"CREG_STATE_ID\">null</param><param name=\"CMOB\">03088008099</param>" +
//                                "<param name=\"CNIC\">3430114376879</param><param name=\"BIRTH_PLACE\">حافظ آباد,حافظ آباد</param><param name=\"RESP\">100</param>" +
//                                "<param name=\"CNAME\">شہریار نواز</param><param name=\"MOTHER_MAIDEN\"> پروین</param><param name=\"CNIC_EXP\">2028-01-20</param>" +
//                                "<param name=\"CDOB\">1992-04-13</param><param name=\"CNIC_STATUS\">2028-01-20</param>" +
//                                "<param name=\"PRESENT_ADDR\">\u202Eڈاک\u202A \u202Cخانہ\u202A،\u202A،\u202A \u202Cتحصیل\u202A \u202Cوضلع\u202A \u202Cحافظ\u202A \u202Cآباد\u202C</param>" +
//                                "<param name=\"PERMANENT_ADDR\">\u202Eڈاک\u202A \u202Cخانہ\u202A،\u202Aہ،\u202A \u202Cتحصیل\u202A \u202Cوضلع\u202A \u202Cحافظ\u202A \u202Cآباد\u202C</param>" +
//                                "<param name=\"RTIMAGES\">1</param><param name=\"MUAOR\">1</param><param name=\"IDR\">1</param></params></msg>";
                        Map<String, Map<String, String>> nadraMap = prepareNADRADataIfRequired(response, "181", appUserModel.getNic());
                        //A/C Opening
                        bWrapper = new BaseWrapperImpl();
                        bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
                        bWrapper.putObject("CMOB", appUserModel.getMobileNo());
                        bWrapper.putObject("CNIC", appUserModel.getNic());
                        bWrapper.putObject(CommandFieldConstants.KEY_BIRTH_PLACE, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_BIRTH_PLACE).toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNAME, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CNAME).toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_MOTHER_MAIDEN, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_MOTHER_MAIDEN).toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_CDOB, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CDOB).toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNIC_EXPIRY, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CNIC_EXPIRY).toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_PRESENT_ADDR).toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_PERMANENT_ADDR).toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_ACC_TITLE, nadraMap.get(appUserModel.getNic()).get(CommandFieldConstants.KEY_CNAME).toString());
                        bWrapper.putObject("GENDER", nadraMap.get(appUserModel.getNic()).get("GENDER").toString());
                        bWrapper.putObject("IS_CNIC_SEEN", "1");
//                        bWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, "2510763");
                        bWrapper.putObject("IS_BVS_ACCOUNT", "1");
                        bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                        bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
                        bWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE, webServiceVO.getPurposeOfAccount());
                        bWrapper.putObject("INCOME_SOURCE", webServiceVO.getSourceOfIncome());
                        bWrapper.putObject("EXPECTED_MONTHLY_TURNOVER", webServiceVO.getExpectedMonthlyTurnover());
                        bWrapper.putObject("NEXT_OF_KIN", webServiceVO.getNextOfKin());
                        bWrapper.putObject("SIGNATURE_PHOTO_FLAG", webServiceVO.getReserved6());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNIC_FRONT_PHOTO, webServiceVO.getCnicFrontPhoto());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNIC_BACK_PHOTO, webServiceVO.getCnicBackPhoto());
                        bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_PHOTO, webServiceVO.getCustomerPhoto());
                        bWrapper.putObject(CommandFieldConstants.KEY_LATITUDE, webServiceVO.getLatitude());
                        bWrapper.putObject(CommandFieldConstants.KEY_LONGITUDE, webServiceVO.getLongitude());
                        bWrapper.putObject(CommandFieldConstants.KEY_CUST_ACC_TYPE, CustomerAccountTypeConstants.LEVEL_0);
                        bWrapper.putObject("INCOME_SOURCE_PIC", webServiceVO.getSourceOfIncomePic());
                        bWrapper.putObject("PROOF_OF_PROFESSION", webServiceVO.getReserved3());
                        bWrapper.putObject("DUAL_NATIONALITY", webServiceVO.getReserved4());
                        bWrapper.putObject("US_CITIZEN", webServiceVO.getReserved5());
                        response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_UPGRADE_L2_ACCOUNT);

                    } else {
                        //A/C Opening
                        bWrapper = new BaseWrapperImpl();
                        bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
                        bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
                        bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
                        bWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
                        bWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
                        bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                        bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
//                bWrapper.putObject(CommandFieldConstants.KEY_FINGER_INDEX, webServiceVO.getFingerIndex());
//                bWrapper.putObject(CommandFieldConstants.KEY_FINGER_TEMPLATE, webServiceVO.getFingerTemplate());
//                bWrapper.putObject(CommandFieldConstants.KEY_TEMPLATE_TYPE, webServiceVO.getTemplateType());
                        bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_NAME, webServiceVO.getConsumerName());
                        bWrapper.putObject("FATHER_HUSBND_NAME", webServiceVO.getFatherHusbandName());
                        bWrapper.putObject("GENDER", webServiceVO.getGender());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNIC_ISSUE_DATE, webServiceVO.getCnicIssuanceDate());
                        bWrapper.putObject(CommandFieldConstants.KEY_CDOB, webServiceVO.getDateOfBirth());
                        bWrapper.putObject(CommandFieldConstants.KEY_BIRTH_PLACE, webServiceVO.getBirthPlace());
                        bWrapper.putObject(CommandFieldConstants.KEY_MOTHER_MAIDEN, webServiceVO.getMotherMaiden());
                        bWrapper.putObject(CommandFieldConstants.KEY_EMAIL_ADDRESS, webServiceVO.getEmailAddress());
                        bWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, webServiceVO.getMailingAddress());
                        bWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, webServiceVO.getPermanentAddress());
                        bWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE, webServiceVO.getPurposeOfAccount());
                        bWrapper.putObject("INCOME_SOURCE", webServiceVO.getSourceOfIncome());
                        bWrapper.putObject("EXPECTED_MONTHLY_TURNOVER", webServiceVO.getExpectedMonthlyTurnover());
                        bWrapper.putObject("NEXT_OF_KIN", webServiceVO.getNextOfKin());
                        bWrapper.putObject("SIGNATURE_PHOTO_FLAG", webServiceVO.getReserved6());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNIC_FRONT_PHOTO, webServiceVO.getCnicFrontPhoto());
                        bWrapper.putObject(CommandFieldConstants.KEY_CNIC_BACK_PHOTO, webServiceVO.getCnicBackPhoto());
                        bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_PHOTO, webServiceVO.getCustomerPhoto());
                        bWrapper.putObject(CommandFieldConstants.KEY_LATITUDE, webServiceVO.getLatitude());
                        bWrapper.putObject(CommandFieldConstants.KEY_LONGITUDE, webServiceVO.getLongitude());
                        bWrapper.putObject("IS_BVS_ACCOUNT", "1");
                        bWrapper.putObject("INCOME_SOURCE_PIC", webServiceVO.getSourceOfIncomePic());
                        bWrapper.putObject("PROOF_OF_PROFESSION", webServiceVO.getReserved3());
                        bWrapper.putObject("DUAL_NATIONALITY", webServiceVO.getReserved4());
                        bWrapper.putObject("US_CITIZEN", webServiceVO.getReserved5());
                        bWrapper.putObject(CommandFieldConstants.KEY_CUST_ACC_TYPE, CustomerAccountTypeConstants.LEVEL_0);
                        response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_UPGRADE_L2_ACCOUNT);
                    }
                } else {
                    bWrapper = new BaseWrapperImpl();
                    bWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE.toString());
                    bWrapper.putObject(CommandFieldConstants.KEY_ENCRYPTION_TYPE, "1");
                    bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, webServiceVO.getMobileNo());
                    bWrapper.putObject(CommandFieldConstants.KEY_DATE, webServiceVO.getDateTime());
                    bWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
                    bWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
                    bWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
//                bWrapper.putObject(CommandFieldConstants.KEY_FINGER_INDEX, webServiceVO.getFingerIndex());
//                bWrapper.putObject(CommandFieldConstants.KEY_FINGER_TEMPLATE, webServiceVO.getFingerTemplate());
//                bWrapper.putObject(CommandFieldConstants.KEY_TEMPLATE_TYPE, webServiceVO.getTemplateType());
                    bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_NAME, webServiceVO.getConsumerName());
                    bWrapper.putObject("FATHER_HUSBND_NAME", webServiceVO.getFatherHusbandName());
                    bWrapper.putObject("GENDER", webServiceVO.getGender());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNIC_ISSUE_DATE, webServiceVO.getCnicIssuanceDate());
                    bWrapper.putObject(CommandFieldConstants.KEY_CDOB, webServiceVO.getDateOfBirth());
                    bWrapper.putObject(CommandFieldConstants.KEY_BIRTH_PLACE, webServiceVO.getBirthPlace());
                    bWrapper.putObject(CommandFieldConstants.KEY_MOTHER_MAIDEN, webServiceVO.getMotherMaiden());
                    bWrapper.putObject(CommandFieldConstants.KEY_EMAIL_ADDRESS, webServiceVO.getEmailAddress());
                    bWrapper.putObject(CommandFieldConstants.KEY_PRESENT_ADDR, webServiceVO.getMailingAddress());
                    bWrapper.putObject(CommandFieldConstants.KEY_PERMANENT_ADDR, webServiceVO.getPermanentAddress());
                    bWrapper.putObject(CommandFieldConstants.KEY_TRANS_PURPOSE, webServiceVO.getPurposeOfAccount());
                    bWrapper.putObject("INCOME_SOURCE", webServiceVO.getSourceOfIncome());
                    bWrapper.putObject("EXPECTED_MONTHLY_TURNOVER", webServiceVO.getExpectedMonthlyTurnover());
                    bWrapper.putObject("NEXT_OF_KIN", webServiceVO.getNextOfKin());
                    bWrapper.putObject("SIGNATURE_PHOTO_FLAG", webServiceVO.getReserved6());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNIC_FRONT_PHOTO, webServiceVO.getCnicFrontPhoto());
                    bWrapper.putObject(CommandFieldConstants.KEY_CNIC_BACK_PHOTO, webServiceVO.getCnicBackPhoto());
                    bWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_PHOTO, webServiceVO.getCustomerPhoto());
                    bWrapper.putObject(CommandFieldConstants.KEY_LATITUDE, webServiceVO.getLatitude());
                    bWrapper.putObject(CommandFieldConstants.KEY_LONGITUDE, webServiceVO.getLongitude());
                    bWrapper.putObject("INCOME_SOURCE_PIC", webServiceVO.getSourceOfIncomePic());
                    bWrapper.putObject("PROOF_OF_PROFESSION", webServiceVO.getReserved3());
                    bWrapper.putObject("DUAL_NATIONALITY", webServiceVO.getReserved4());
                    bWrapper.putObject("US_CITIZEN", webServiceVO.getReserved5());
//                    bWrapper.putObject("IS_BVS_ACCOUNT", "1");
                    bWrapper.putObject(CommandFieldConstants.KEY_CUST_ACC_TYPE, CustomerAccountTypeConstants.LEVEL_1);
                    response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_UPGRADE_L2_ACCOUNT);
                }
                logger.info("UpgradeL2AccountCommand response for Mobile #:: " + appUserModel.getMobileNo() + "\n" + response);
                if (MfsWebUtil.isErrorXML(response)) {
                    return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                }
                BlinkCustomerModel bcm = new BlinkCustomerModel();
                bcm = this.getCommonCommandManager().loadBlinkCustomerByMobileAndAccUpdate(webServiceVO.getMobileNo(), 1L);
                if (bcm != null) {
                    String customerSMS = this.getMessageSource().getMessage("smsCommand.blinkCustomer_tracking_id", new Object[]{Long.toString(bcm.getBlinkCustomerId())}, null);
                    BaseWrapper baseWrapper = new BaseWrapperImpl();
                    baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(webServiceVO.getMobileNo(), customerSMS));
                    getCommonCommandManager().sendSMSToUser(baseWrapper);

                }
                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription("Request has been submitted successfully");
                webServiceVO.setResponseContentXML(response);
            }
        } catch (CommandException ex) {
            if (ex.getErrorCode() == 1024L) {
                logger.error("Error Occurred while Upgrade Account for Mobile # :: " + webServiceVO.getMobileNo());
                logger.error("[FonePaySwitchController.upgradeAccount] Error occured: " + ex.getMessage(), ex);
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.BLINK_CUSTOMER_DATA_ALREADY_EXISTS);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            } else {
                logger.error("Error Occurred while Upgrade Account for Mobile # :: " + webServiceVO.getMobileNo());
                logger.error("[FonePaySwitchController.upgradeAccount] Error occured: " + ex.getMessage(), ex);
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(ex.getMessage());
            }
        } catch (Exception ex) {
            logger.error("Error Occurred while Upgrade Account for Mobile # :: " + webServiceVO.getMobileNo());
            logger.error("[FonePaySwitchController.upgradeAccount] Error occured: " + ex.getMessage(), ex);
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
            logger.info("[FonePaySwitchController.l2AccountUpgrade] End:: ");
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO accountDetail(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.Account Detail Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        CustomerModel customerModel = new CustomerModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String loginPin = webServiceVO.getLoginPin();

        try {
            if (webServiceVO.getReserved2().equals("0")) {

            } else {
                webServiceVO = this.validateRRN(webServiceVO);
            }
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Account Detail");
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                customerModel.setCustomerId(appUserModel.getCustomerId());
                baseWrapper.setBasePersistableModel(customerModel);
                baseWrapper = mfsAccountManager.loadCustomer(baseWrapper);
                customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
                customerModel.setStrockTrading(webServiceVO.getStockTrading());
                customerModel.setMutualFunds(webServiceVO.getMutulaFunds());
                this.getCommonCommandManager().saveOrUpdateCustomerModel(customerModel);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successful");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.Account Detail] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);

        }
        this.logger.info("Account Detail => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO customerNameUpdate(WebServiceVO webServiceVO) {

        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = null;
        UpdateCustomerNameModel updateCustomerNameModel = null;

        try {

            webServiceVO = this.validateRRN(webServiceVO);

            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Update Customer Name");
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel == null) {
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {

                updateCustomerNameModel = new UpdateCustomerNameModel();
                updateCustomerNameModel = updateCustomerNameDAO.getCustomerNameAndNameUpdate(appUserModel.getNic(), false);
                if (updateCustomerNameModel == null) {
                    updateCustomerNameModel = new UpdateCustomerNameModel();

                    updateCustomerNameModel.setUpdated(false);
                    updateCustomerNameModel.setFirstName(webServiceVO.getFirstName());
                    updateCustomerNameModel.setLastName(webServiceVO.getLastName());
                    updateCustomerNameModel.setCnic(appUserModel.getNic());
                    updateCustomerNameModel.setMobileNo(webServiceVO.getMobileNo());
                    updateCustomerNameModel.setNadraName(appUserModel.getFullName());
                    updateCustomerNameModel.setCreatedOn(new Date());
                    updateCustomerNameModel.setCreadtedBy(1L);
                    BaseWrapper baseWrapper = new BaseWrapperImpl();
                    baseWrapper.setBasePersistableModel(updateCustomerNameModel);
                    updateCustomerNameFacade.saveOrUpdateCustomerName(baseWrapper);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successful");

//                    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
//                    ArrayList<SmsMessage> messageList = new ArrayList<>();
//                    messageList.add(new SmsMessage(appUserModel.getMobileNo(),
//                            MessageUtil.getMessage("update.customer.name.intimation")));
//
//                    workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
//                    getCommonCommandManager().sendSMS(workFlowWrapper);
                    String customerSMS = this.getMessageSource().getMessage("update.customer.name.request.received", null, null);
                    baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(webServiceVO.getMobileNo(), customerSMS));
                    getCommonCommandManager().sendSMSToUser(baseWrapper);
                } else {
                    webServiceVO.setResponseCode("179");
                    webServiceVO.setResponseCodeDescription("Request Already Exist");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.Update Customer Name] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
            actionLogAfterEnd(actionLogModel);
        }
        this.logger.info("Update Customer Name => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }


    @Override
    public WebServiceVO clsStatusUpdate(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.CLS Status Update:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        CustomerModel customerModel = new CustomerModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        String loginPin = webServiceVO.getLoginPin();

        try {
            if (webServiceVO.getReserved2().equals("0")) {

            } else {
                webServiceVO = this.validateRRN(webServiceVO);
            }
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            actionLogModel = this.actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, webServiceVO.getMobileNo());
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "Cls Status Update");
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);

            if (appUserModel == null) {
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }
            //            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
//                return webServiceVO;
//            }

//            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel = new ClsPendingBlinkCustomerModel();
                clsPendingBlinkCustomerModel.setMobileNo(appUserModel.getMobileNo());
                clsPendingBlinkCustomerModel.setCaseID(webServiceVO.getCaseId());
                clsPendingBlinkCustomerModel = clsPendingBlinkCustomerDAO.loadExistingPendingAccountOpeningSafRepo(clsPendingBlinkCustomerModel);
                if (clsPendingBlinkCustomerModel != null) {
                    clsPendingBlinkCustomerModel.setCaseStatus(webServiceVO.getCaseStatus());
                    clsPendingBlinkCustomerModel.setCaseID(webServiceVO.getCaseId());
                    clsPendingBlinkCustomerModel.setClsComments(webServiceVO.getClsComment());
                    clsPendingBlinkCustomerModel.setClsBotStatus(1);
                    clsPendingBlinkCustomerDAO.saveOrUpdate(clsPendingBlinkCustomerModel);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successful");
                } else {
                    ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = new ClsPendingAccountOpeningModel();
                    clsPendingAccountOpeningModel.setMobileNo(appUserModel.getMobileNo());
                    clsPendingAccountOpeningModel.setCaseID(webServiceVO.getCaseId());

                    clsPendingAccountOpeningModel = clsPendingAccountOpeningDAO.loadExistingPendingAccountOpeningSafRepo(clsPendingAccountOpeningModel);
                    clsPendingAccountOpeningModel.setCaseStatus(webServiceVO.getCaseStatus());
                    clsPendingAccountOpeningModel.setCaseID(webServiceVO.getCaseId());
                    clsPendingAccountOpeningModel.setClsComments(webServiceVO.getClsComment());
                    clsPendingAccountOpeningModel.setClsBotStatus(1);
                    clsPendingAccountOpeningDAO.saveOrUpdate(clsPendingAccountOpeningModel);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successful");
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.Account Detail] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);

        }
        this.logger.info("Account Detail => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }

    @Override
    public WebServiceVO blinkAccountVerificationInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.blinkAccountVerificationInquiry");

        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
        FonePayLogModel fonePayLogModel = null;
        blinkCustomerModel.setMobileNo(mobileNo);
        blinkCustomerModel.setCnic(cnic);
        List<BlinkCustomerModel> blinkCustomerModelList = new ArrayList<BlinkCustomerModel>();

        blinkCustomerModelList = blinkCustomerModelDAO.findByExample(blinkCustomerModel).getResultsetList();
        AppUserModel appUserModel = appUserDAO.loadAppUserByCNIC(webServiceVO.getCnicNo());
        if (appUserModel == null) {
            webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            webServiceVO.setResponseCodeDescription("User Not Found.");
            return webServiceVO;
        }
        CustomerModel customerModel = new CustomerModel();
        customerModel.setCustomerId(appUserModel.getCustomerId());
        try {
            customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
        } catch (CommandException e) {
            e.printStackTrace();
        }
        if (blinkCustomerModelList.isEmpty() || blinkCustomerModelList.get(0) == null) {
            webServiceVO.setResponseCodeDescription("No Blink Customer Found Against This CNIC and Mobile Number");
            webServiceVO.setResponseCode("172");
            return webServiceVO;
        } else {
            if (blinkCustomerModelList.get(0).getRegistrationStatus().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED.toString()) && blinkCustomerModelList.get(0).getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                try {
                    webServiceVO = this.validateRRN(webServiceVO);
                    if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                        return webServiceVO;
                    fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "BlinkAccountVerificationInquiry");
                    if (blinkCustomerModelList.get(0).getBVS() != null && blinkCustomerModelList.get(0).getBVS().equals(true)) {

                        webServiceVO.setResponseCode("173");
                        webServiceVO.setResponseCodeDescription("Blink Account Already Bvs Verified");
                    } else {
                        webServiceVO.setResponseCode("00");
                        webServiceVO.setResponseCodeDescription("Successful");
                        webServiceVO.setAccountType(customerModel.getTypeOfAccount());
                        webServiceVO.setCustomerRegState(blinkCustomerModelList.get(0).getRegistrationStateName());
                        webServiceVO.setAccountTitle(blinkCustomerModelList.get(0).getConsumerName());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.logger.error("[FonePaySwitchController.Blink Account Verification Detail] Error occured: " + ex.getMessage(), ex);
                    webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                    webServiceVO.setResponseCodeDescription(ex.getMessage());
                    if (ex instanceof NullPointerException
                            || ex instanceof HibernateException
                            || ex instanceof SQLException
                            || ex instanceof DataAccessException
                            || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                        logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                        webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                    }
                }

            } else {
                webServiceVO.setResponseCodeDescription("Blink Registration State in not Approved or Account type is not Blink in Blink Customer");
                webServiceVO.setResponseCode("175");
                return webServiceVO;
            }
        }

        return webServiceVO;
    }


    @Override
    public WebServiceVO blinkAccountVerification(WebServiceVO webServiceVO) {
        logger.info("blinkAccountVerification Request Received");
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        BlinkCustomerModel blinkCustomerModel = new BlinkCustomerModel();
        FonePayLogModel fonePayLogModel = null;
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        blinkCustomerModel.setMobileNo(mobileNo);
        blinkCustomerModel.setCnic(cnic);
        BaseWrapper bWrapper = new BaseWrapperImpl();
        List<BlinkCustomerModel> blinkCustomerModelList = new ArrayList<BlinkCustomerModel>();
        blinkCustomerModelList = blinkCustomerModelDAO.findByExample(blinkCustomerModel).getResultsetList();
        AppUserModel appUserModel = appUserDAO.loadAppUserByCNIC(cnic);
        if (appUserModel == null) {
            logger.info("No Customer Found");
            webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
            webServiceVO.setResponseCodeDescription("User Not Found.");
            return webServiceVO;
        }
        CustomerModel customerModel = new CustomerModel();
        customerModel.setCustomerId(appUserModel.getCustomerId());
        try {
            customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
            if (!(customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK) || customerModel.getCustomerAccountTypeId().equals(56L))) {
                webServiceVO.setResponseCodeDescription("No Ultra and Zindig Ultra Customer Found Against This CNIC and Mobile Number");
                webServiceVO.setResponseCode("172");
                return webServiceVO;
            } else {
                webServiceVO = this.validateRRN(webServiceVO);
                if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                    return webServiceVO;
                fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "BlinkAccountVerification");
                if ("00".equals(webServiceVO.getResponseCode())) {
                    String response = null;
                    if (!webServiceVO.getReserved2().equals("1")) {
                        ThreadLocalAppUser.setAppUserModel(appUserModel);
                        bWrapper = this.prepareNadraRequest(appUserModel, webServiceVO);
                        response = getCommandManager().executeCommand(bWrapper, CommandFieldConstants.CMD_CUSTOMER_NADRA_VERIFICATION);
                        if (MfsWebUtil.isErrorXML(response)) {
                            logger.info("CustomerNadraVerificationCommand response for Mobile # :: " + appUserModel.getMobileNo() + "\n" + response);
                            return mfsWebResponseDataPopulator.populateErrorMessagesForOpenAPI(webServiceVO, response);
                        } else {

                            if (!blinkCustomerModelList.isEmpty()) {


                                BlinkCustomerModel blinkModel = new BlinkCustomerModel();
                                blinkModel = blinkCustomerModelDAO.loadBlinkCustomerModelByBlinkCustomerId(blinkCustomerModelList.get(0).getBlinkCustomerId());
                                bWrapper.setBasePersistableModel(appUserModel);
                                logger.info("load UserDeviceAccount against appUserId" + appUserModel.getAppUserId());
                                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                                Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
                                logger.info("account Info Model against customerID" + customerModel.getCustomerId() + "and payement mode Id" + paymentModeId);
                                AccountInfoModel accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerModel.getCustomerId(), paymentModeId);
                                SmartMoneyAccountModel smartMoneyAccountModel = null;
                                smartMoneyAccountModel = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, paymentModeId);
                                if (uda.getAccountLocked()) {
                                    if (blinkModel.getClsResponseCode() == null || !blinkModel.getClsResponseCode().equals("True Match-Compliance")) {
                                        uda.setAccountLocked(false);
                                        uda.setUpdatedOn(new Date());
                                        bWrapper.setBasePersistableModel(uda);
                                        this.getCommonCommandManager().updateUserDeviceAccounts(bWrapper);
                                        appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                                        appUserModel.setUpdatedOn(new Date());
                                        bWrapper.setBasePersistableModel(appUserModel);
                                        this.getCommonCommandManager().updateAppUser(bWrapper);
                                    }
                                }
                                Long stateId = appUserModel.getAccountStateId();
                                if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {
                                    appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                                    appUserModel.setPrevRegistrationStateId(appUserModel.getRegistrationStateId());
                                    appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                    appUserModel.setDormancyRemovedOn(new Date());
                                    appUserModel.setUpdatedOn(new Date());
                                    bWrapper.setBasePersistableModel(appUserModel);
                                    this.getCommonCommandManager().updateAppUser(bWrapper);
                                }
                                if (!(smartMoneyAccountModel.getIsDebitBlocked() == null) && smartMoneyAccountModel.getIsDebitBlocked().equals(true)) {
                                    smartMoneyAccountModel.setIsDebitBlocked(false);
                                    smartMoneyAccountModel.setDebitBlockAmount(0.0);
                                    smartMoneyAccountModel.setUpdatedOn(new Date());
                                    bWrapper.setBasePersistableModel(smartMoneyAccountModel);
                                    this.getCommonCommandManager().updateSmartMoneyAccount(bWrapper);
                                }
                                logger.info("Blink Bvs Successfully Verified");
                                blinkModel.setMobileNo(mobileNo);
                                blinkModel.setCnic(cnic);
                                blinkModel.setBVS(true);
                                blinkCustomerModelDAO.saveOrUpdate(blinkModel);
                                CustomerModel customerModel1 = new CustomerModel();
                                customerModel1 = customerDAO.loadCustomerModelByCustomerId(customerModel.getCustomerId());
                                customerModel1.setMobileNo(mobileNo);
                                customerModel1.setBlinkBvs(true);
                                customerDAO.saveOrUpdate(customerModel1);
                                webServiceVO.setResponseCodeDescription("Successful");
                                webServiceVO.setResponseCode("00");
                            }else {
                                bWrapper.setBasePersistableModel(appUserModel);
                                logger.info("load UserDeviceAccount against appUserId" + appUserModel.getAppUserId());
                                bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                                uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                                Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
                                logger.info("account Info Model against customerID" + customerModel.getCustomerId() + "and payement mode Id" + paymentModeId);
                                AccountInfoModel accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerModel.getCustomerId(), paymentModeId);
                                SmartMoneyAccountModel smartMoneyAccountModel = null;
                                smartMoneyAccountModel = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, paymentModeId);
                                Long stateId = appUserModel.getAccountStateId();
                                if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {
                                    appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                                    appUserModel.setPrevRegistrationStateId(appUserModel.getRegistrationStateId());
                                    appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                    appUserModel.setDormancyRemovedOn(new Date());
                                    appUserModel.setUpdatedOn(new Date());
                                    bWrapper.setBasePersistableModel(appUserModel);
                                    this.getCommonCommandManager().updateAppUser(bWrapper);
                                }
                                if (!(smartMoneyAccountModel.getIsDebitBlocked() == null) && smartMoneyAccountModel.getIsDebitBlocked().equals(true)) {
                                    smartMoneyAccountModel.setIsDebitBlocked(false);
                                    smartMoneyAccountModel.setDebitBlockAmount(0.0);
                                    smartMoneyAccountModel.setUpdatedOn(new Date());
                                    bWrapper.setBasePersistableModel(smartMoneyAccountModel);
                                    this.getCommonCommandManager().updateSmartMoneyAccount(bWrapper);
                                }
                                CustomerModel customerModel1 = new CustomerModel();
                                customerModel1 = customerDAO.loadCustomerModelByCustomerId(customerModel.getCustomerId());
                                customerModel1.setMobileNo(mobileNo);
                                customerModel1.setBlinkBvs(true);
                                customerDAO.saveOrUpdate(customerModel1);
                                webServiceVO.setResponseCodeDescription("Successful");
                                webServiceVO.setResponseCode("00");
                            }

                        }
                    } else {

                        if(!blinkCustomerModelList.isEmpty()) {
                            BlinkCustomerModel blinkModel = new BlinkCustomerModel();
                            blinkModel = blinkCustomerModelDAO.loadBlinkCustomerModelByBlinkCustomerId(blinkCustomerModelList.get(0).getBlinkCustomerId());
                            bWrapper.setBasePersistableModel(appUserModel);
                            logger.info("load UserDeviceAccount against appUserId" + appUserModel.getAppUserId());
                            bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                            uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                            Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
                            logger.info("account Info Model against customerID" + customerModel.getCustomerId() + "and payement mode Id" + paymentModeId);
                            AccountInfoModel accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerModel.getCustomerId(), paymentModeId);
                            SmartMoneyAccountModel smartMoneyAccountModel = null;
                            smartMoneyAccountModel = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, paymentModeId);
                            if (uda.getAccountLocked()) {
                                if (blinkModel.getClsResponseCode() == null || !blinkModel.getClsResponseCode().equals("True Match-Compliance")) {
                                    uda.setAccountLocked(false);
                                    uda.setUpdatedOn(new Date());
                                    bWrapper.setBasePersistableModel(uda);
                                    this.getCommonCommandManager().updateUserDeviceAccounts(bWrapper);
                                    appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                                    appUserModel.setUpdatedOn(new Date());
                                    bWrapper.setBasePersistableModel(appUserModel);
                                    this.getCommonCommandManager().updateAppUser(bWrapper);

                                }
                            }
                            Long stateId = appUserModel.getAccountStateId();
                            if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {
                                appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                                appUserModel.setPrevRegistrationStateId(appUserModel.getRegistrationStateId());
                                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                appUserModel.setUpdatedOn(new Date());
                                bWrapper.setBasePersistableModel(appUserModel);
                                this.getCommonCommandManager().updateAppUser(bWrapper);
                            }
                            if (!(smartMoneyAccountModel.getIsDebitBlocked() == null) && smartMoneyAccountModel.getIsDebitBlocked().equals(true)) {
                                smartMoneyAccountModel.setIsDebitBlocked(false);
                                smartMoneyAccountModel.setDebitBlockAmount(0.0);
                                smartMoneyAccountModel.setUpdatedOn(new Date());
                                bWrapper.setBasePersistableModel(smartMoneyAccountModel);
                                this.getCommonCommandManager().updateSmartMoneyAccount(bWrapper);
                            }
                            logger.info("Blink Bvs Successfully Verified");
                            blinkModel.setMobileNo(mobileNo);
                            blinkModel.setCnic(cnic);
                            blinkModel.setBVS(true);
                            blinkCustomerModelDAO.saveOrUpdate(blinkModel);
                            CustomerModel customerModel1 = new CustomerModel();
                            customerModel1 = customerDAO.loadCustomerModelByCustomerId(customerModel.getCustomerId());
                            customerModel1.setMobileNo(mobileNo);
                            customerModel1.setBlinkBvs(true);
                            customerDAO.saveOrUpdate(customerModel1);
                            webServiceVO.setResponseCodeDescription("Successful");
                            webServiceVO.setResponseCode("00");
                        }else {
                            bWrapper.setBasePersistableModel(appUserModel);
                            logger.info("load UserDeviceAccount against appUserId" + appUserModel.getAppUserId());
                            bWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(bWrapper);
                            uda = (UserDeviceAccountsModel) bWrapper.getBasePersistableModel();
                            Long paymentModeId = PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;
                            logger.info("account Info Model against customerID" + customerModel.getCustomerId() + "and payement mode Id" + paymentModeId);
                            AccountInfoModel accountInfoModel = getCommonCommandManager().getAccountInfoModel(customerModel.getCustomerId(), paymentModeId);
                            SmartMoneyAccountModel smartMoneyAccountModel = null;
                            smartMoneyAccountModel = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(appUserModel, paymentModeId);
                            Long stateId = appUserModel.getAccountStateId();
                            if (stateId.equals(AccountStateConstantsInterface.ACCOUNT_STATE_DORMANT)) {
                                appUserModel.setAccountStateId(AccountStateConstants.ACCOUNT_STATE_COLD);
                                appUserModel.setPrevRegistrationStateId(appUserModel.getRegistrationStateId());
                                appUserModel.setRegistrationStateId(RegistrationStateConstants.VERIFIED);
                                appUserModel.setDormancyRemovedOn(new Date());
                                appUserModel.setUpdatedOn(new Date());
                                bWrapper.setBasePersistableModel(appUserModel);
                                this.getCommonCommandManager().updateAppUser(bWrapper);
                            }
                            if (!(smartMoneyAccountModel.getIsDebitBlocked() == null) && smartMoneyAccountModel.getIsDebitBlocked().equals(true)) {
                                smartMoneyAccountModel.setIsDebitBlocked(false);
                                smartMoneyAccountModel.setDebitBlockAmount(0.0);
                                smartMoneyAccountModel.setUpdatedOn(new Date());
                                bWrapper.setBasePersistableModel(smartMoneyAccountModel);
                                this.getCommonCommandManager().updateSmartMoneyAccount(bWrapper);
                            }
                            CustomerModel customerModel1 = new CustomerModel();
                            customerModel1 = customerDAO.loadCustomerModelByCustomerId(customerModel.getCustomerId());
                            customerModel1.setMobileNo(mobileNo);
                            customerModel1.setBlinkBvs(true);
                            customerDAO.saveOrUpdate(customerModel1);
                            webServiceVO.setResponseCodeDescription("Successful");
                            webServiceVO.setResponseCode("00");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.logger.error("[FonePaySwitchController.Blink Bvs Detail] Error occured: " + ex.getMessage(), ex);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(ex.getMessage());
            if (ex instanceof NullPointerException
                    || ex instanceof HibernateException
                    || ex instanceof SQLException
                    || ex instanceof DataAccessException
                    || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }
        }
        return webServiceVO;
    }



    @Override
    public WebServiceVO debitCardStatusVerification(WebServiceVO webServiceVO) {

        logger.info("[FonePaySwitchController.debitCardIssuanceInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String transactionType = webServiceVO.getTransactionType();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay DebitCardStatus] [Mobile:" + mobileNo + ", Cnic:" + cnic + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "DebitCardStatusVerification");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_DEBIT_CARD_ISSUANCE_INQUIRY);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                List<DebitCardModel> list = null;
                try {
                    list = getCommonCommandManager().getDebitCardModelDao().getDebitCardModelByMobileAndNIC(mobileNo, cnic);
                } catch (FrameworkCheckedException e) {
                    e.printStackTrace();
                }
                if (list != null && !list.isEmpty()) {
                    DebitCardModel debitCardModel = list.get(0);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successful");
                    if (debitCardModel.getReissuance() != null && debitCardModel.getReissuance().equals("1")) {
                        if (debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_INTITATED)) {
                            webServiceVO.setCardDescription("Request Received" + " " + debitCardModel.getReissuanceRequestDate());

                        } else if (debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)) {
                            webServiceVO.setCardDescription("Printing" + " " + debitCardModel.getInProgressDate());
                        } else if (debitCardModel.getReIssuanceStatus().equals(CardConstantsInterface.CARD_STATUS_ACTIVE)) {
                            webServiceVO.setCardDescription("Printing" + " " + debitCardModel.getInProgressDate());
                        }

                    } else {
                        if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_APPROVED)) {
                            webServiceVO.setCardDescription("Approved" + " " + debitCardModel.getApprovedOn());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_HOT)) {
                            webServiceVO.setCardDescription("Hot" + " " + debitCardModel.getHotOn());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_IN_PROCESS)) {
                            webServiceVO.setCardDescription("Printing" + " " + debitCardModel.getInProgressDate());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_REJECTED)) {
                            webServiceVO.setCardDescription("Rejected" + " " + debitCardModel.getDeniedOn());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_INTITATED)) {
                            webServiceVO.setCardDescription("Request Received" + " " + debitCardModel.getCreatedOn());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_DE_ACTIVATED)) {
                            webServiceVO.setCardDescription("De-Activated" + " " + debitCardModel.getDeActiveOn());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_BLOCKED)) {
                            webServiceVO.setCardDescription("Block" + " " + debitCardModel.getUpdatedOn());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_PENDING)) {
                            webServiceVO.setCardDescription("Pending" + " " + debitCardModel.getUpdatedOn());
                        } else if (debitCardModel.getCardStatusId().equals(CardConstantsInterface.CARD_STATUS_ACTIVE)) {
                            webServiceVO.setCardDescription("Dispatched" + " " + debitCardModel.getActiveOn());
                        }
                    }

                } else {
                    webServiceVO.setResponseCode(FonePayResponseCodes.CARD_NOT_FOUND);
                    webServiceVO.setResponseCodeDescription("Card Not Found");
                }

            }
        } catch (CommandException e) {
            if (e.getErrorCode() == 9097) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEBIT_CARD_ALREADY_EXISTS.toString());
            } else {
                logger.error("[FonePaySwitchController.DebitCardStatus Verification] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.DebitCardStatus Verification] Error occured: " + e.getMessage(), e);
            if (StringUtil.isNullOrEmpty(webServiceVO.getResponseCode())) {
                this.logger.error("[FonePaySwitchController.DebitCardStatus Verification] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                if (e instanceof NullPointerException
                        || e instanceof HibernateException
                        || e instanceof SQLException
                        || e instanceof DataAccessException
                        || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                    logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                    webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
                }
            }
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }


    @Override
    public WebServiceVO advanceLoanPaymentSettlement(WebServiceVO webServiceVO) {

        logger.info("[FonePaySwitchController.advanceLoanSalary] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        AdvanceSalaryLoanModel advanceSalaryLoanModel = new AdvanceSalaryLoanModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.REQ_ADVANCE_SALARY_LOAN);
            actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_CREATE, null, null, webServiceVO.getMobileNo());
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);


            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);

            if ("00".equals(webServiceVO.getResponseCode())) {
                advanceSalaryLoanModel = getCommonCommandManager().getAdvanceSalaryLoanDAO().loadAdvanceSalaryLoanByMobileNumber(appUserModel.getMobileNo());
                if (advanceSalaryLoanModel != null) {
                    advanceSalaryLoanModel.setIsCompleted(true);
                    advanceSalaryLoanModel.setUpdatedOn(new Date());
                    getCommonCommandManager().getAdvanceSalaryLoanDAO().saveOrUpdate(advanceSalaryLoanModel);
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);

                } else {
                    webServiceVO.setResponseCode(FonePayResponseCodes.LOAN_DATA);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.LOAN_DATA_NOT_FOUND);

                }

            }
        } catch (CommandException ex) {
            logger.error("Error Occurred while Advance Salary Loan for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);

        } catch (Exception ex) {
            logger.error("Error Occurred while Advance Salary Loan for Mobile # :: " + webServiceVO.getMobileNo());
            ex.printStackTrace();
            FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR);
        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("advanceSalaryLoan => Response Code : " + webServiceVO.getResponseCode() + ", Description : " + webServiceVO.getResponseCodeDescription());
        return webServiceVO;
    }


    @Override
    public WebServiceVO feePaymentInquiry(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.feePaymentInquiry] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String PID = "";
        CardProdCodeModel cardTypeModel = new CardProdCodeModel();
        List<CardProdCodeModel> list;

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        String mobileNo = webServiceVO.getMobileNo();
        PID = webServiceVO.getProductID();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_FEE_PAYMENT_INQUIRY;


        String transactionType = webServiceVO.getTransactionType();
        String messageType = "FeePaymentInquiry";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay feePaymentInquiry] [Mobile:" + mobileNo + ", PID:" + PID + ", Cnic:" + cnic + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_CNIC, webServiceVO.getCnicNo());
        baseWrapper.putObject(CommandFieldConstants.KEY_RRN, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, PID);
        baseWrapper.putObject(CommandFieldConstants.KEY_CARD_FEE_TYPE_ID, webServiceVO.getCardTypeId());
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.FEE_PAYMENT_INFO);

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }


            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_FEE_PAYMENT_INQUIRY);
                String fee = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.FEE_NODEREF);


                if (isopt.equals("02")) {
                    ///Generate OTP and store in MiniTransaction
                    String otp = CommonUtils.generateOneTimePin(5);
                    logger.info("The plain otp is " + otp);
                    String encryptedPin = EncoderUtils.encodeToSha(otp);
                    getFonePayManager().createMiniTransactionModel(encryptedPin, webServiceVO.getMobileNo(), webServiceVO.getChannelId(), cmdId);
                    webServiceVO.setOtpPin(otp);
                    getFonePayManager().sendOtpSms(otp, messageType, webServiceVO.getMobileNo());
                }

                if (fee != null && !fee.equals("") && !fee.equals("0.0")) {
                    webServiceVO.setResponseContentXML(xml);
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successful");
                    webServiceVO.setCharges(fee);
                } else {
                    webServiceVO.setResponseCode("182");
                    webServiceVO.setResponseCodeDescription("No Fee Configure Against Card Fee Type And Product");
                }
            }

        } catch (CommandException e) {
            if (e.getErrorCode() == 9097) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEBIT_CARD_ALREADY_EXISTS.toString());
            } else {
                logger.error("[FonePaySwitchController.FeePaymentInquiry] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode(String.valueOf(e.getErrorCode()));
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.FeePaymentInquiry] Error occured: " + e.getMessage(), e);
            this.logger.error("[FonePaySwitchController.FeePaymentInquiry] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.FeePyamentInquiry] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO feePayment(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.feePaymenttApi] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        String isopt = webServiceVO.getPinType();
        String cmdId = CommandFieldConstants.CMD_FEE_PAYMENT;
        String appId = "2";
        Double charges;
        Double tranAmount;
        String transactionType = webServiceVO.getTransactionType();
        String messageType = "fee Payment";
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay feePayment] [Mobile:" + mobileNo + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNo);
        baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID, webServiceVO.getChannelId());
        baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, webServiceVO.getTransactionAmount());
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
        baseWrapper.putObject(CommandFieldConstants.KEY_TPAM, "0");
        baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, webServiceVO.getProductID());
        baseWrapper.putObject(CommandFieldConstants.KEY_TAMT, webServiceVO.getTransactionAmount());
        baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, webServiceVO.getRetrievalReferenceNumber());
        baseWrapper.putObject(CommandFieldConstants.KEY_TERMINAL_ID, webServiceVO.getTerminalId());
        baseWrapper.putObject(CommandFieldConstants.KEY_STAN, webServiceVO.getReserved2());
        baseWrapper.putObject(CommandFieldConstants.KEY_APP_ID, appId);
        baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);

        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, FonePayConstants.FEE_PAYMENT);

            Double cardFee = 0.0d;
            WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();


            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            this.userValidation(webServiceVO, CommandFieldConstants.CMD_FEE_PAYMENT_INQUIRY);
            if (webServiceVO.getResponseCode() != null && !webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;

            if (appUserModel != null) {
                ThreadLocalAppUser.setAppUserModel(appUserModel);
                baseWrapper.setBasePersistableModel(appUserModel);
                baseWrapper = getCommonCommandManager().loadUserDeviceAccountByMobileNumber(baseWrapper);
                uda = (UserDeviceAccountsModel) baseWrapper.getBasePersistableModel();
                ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
                webServiceVO.setCnicNo(appUserModel.getNic());
            }
//               Restrict BLINK Account Transaction FROM AMA validateAmaAccountType
            webServiceVO = this.validateAmaAccountType(webServiceVO, appUserModel);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE)) {
                return webServiceVO;
            }
            workFlowWrapper = getCommonCommandManager().calculateCardFee(webServiceVO.getMobileNo(), cnic, appUserModel, null, null,
                    Long.valueOf(webServiceVO.getProductID()),
                    Long.valueOf(webServiceVO.getCardTypeId()), Long.parseLong(String.valueOf(DeviceTypeConstantsInterface.WEB_SERVICE)), null);
            String fee = String.valueOf(workFlowWrapper.getCommissionAmountsHolder().getTransactionAmount());
            logger.info("[FonePaySwitchController.FeePayment  FEE] Fee :: " + fee);

            if (!Double.valueOf(webServiceVO.getTransactionAmount()).equals(Double.valueOf(fee))) {
                webServiceVO.setResponseCode(FonePayResponseCodes.INVALID_AMOUNT);
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_AMOUNT);

            }

            webServiceVO = getFonePayManager().makevalidateCustomer(webServiceVO);
            if ("00".equals(webServiceVO.getResponseCode())) {
                xml = getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_FEE_PAYMENT);
                String transactionAmount = (MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_AMOUNT_NODEREF));
                String totalAmount = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRAN_TOTAL_AMT_NODEREF);
                charges = Double.valueOf(MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_SERVICE_CHARGES_NODEREF));
                String transactionCode = MiniXMLUtil.getTagTextValue(xml, MiniXMLUtil.TRANS_ID_NODEREF);


                webServiceVO.setResponseContentXML(xml);
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successfull");
                webServiceVO.setCommissionAmount(String.valueOf(charges));
                webServiceVO.setTransactionAmount(transactionAmount);
                webServiceVO.setTotalAmount(totalAmount);
                webServiceVO.setTransactionId(transactionCode);
            }

        } catch (CommandException e) {
            if (e.getErrorCode() == 9023) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_INVALID.toString());
            } else if (e.getErrorCode() == 9029) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DEVICE_OTP_EXPIRED.toString());
            } else if (e.getErrorCode() == 9000) {
                FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.PIN_IS_NUMERIC.toString());

            } else if (e.getErrorCode() == 9001L) {
                if (e.getMessage().equals("Per day limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per month limit of Sender exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_DEBIT_LIMIT_BUSTED.toString());
                } else if (e.getMessage().equals("Per day limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.DAILY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Per month limit of Recipient exceeded.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.MONTHLY_CREDIT_LIMIT_BUSTED);
                } else if (e.getMessage().equals("Incorrect MPIN, Please retry.\n")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
                } else if (e.getMessage().equals("Transaction cannot be processed due to insufficient balance.")) {
                    FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INSUFFICIENT_ACC_BALANCE);
                } else {
                    logger.error("[FonePaySwitchController.FeePayment] Error occured: " + e.getMessage(), e);
                    webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                    webServiceVO.setResponseCodeDescription(e.getMessage());
                }
            } else {
                logger.error("[FonePaySwitchController.FeePayment] Error occured: " + e.getMessage(), e);
                webServiceVO.setResponseCode(FonePayResponseCodes.COMMAND_GENERAL_EXCEPTION);
                webServiceVO.setResponseCodeDescription(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.FeePayment] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.FeePayment] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.FeePayment] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO updateMinorAccount(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.UpdateMinorAccount] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        String mobileNo = webServiceVO.getMobileNo();
        String cnic = webServiceVO.getCnicNo();
        UserDeviceAccountsModel uda = new UserDeviceAccountsModel();
        this.logger.info("[FonePay UpdateMinorAccount] [Mobile:" + mobileNo + ", Cnic:" + cnic + "]");


        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "UpdateMinorAccount");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }

            this.minorUpdate(webServiceVO);


        } catch (Exception e) {
            logger.error("[FonePaySwitchController.UpdateMinorAccount] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.UpdateMinorAccount] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.UpdateMinorAccount] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO verifyLoginAccount(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = null;
        String mobileNumber = webServiceVO.getMobileNo();
        String rrn = webServiceVO.getRetrievalReferenceNumber();
        String dateTime = webServiceVO.getDateTime();
        String transactionType = webServiceVO.getTransactionType();
        String otpReqType = webServiceVO.getReserved2();
        String reqType = "Verify Login Account Customer";

        this.logger.info("[FonePay Verify Account] [Mobile:" + mobileNumber + ", RRN:" + rrn + ", DateTime:" + dateTime + "]");
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, reqType);
            ActionLogModel actionLogModel = actionLogBeforeStart(PortalConstants.ACTION_RETRIEVE, null, null, mobileNumber);

            webServiceVO = getFonePayManager().verifyLoginCustomer(webServiceVO);

            actionLogAfterEnd(actionLogModel);
        } catch (Exception e) {
            this.logger.error("[FonePaySwitchController.verifyAccount] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }


            webServiceVO.setAccountTitle(null);
        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        this.logger.info("[FonePaySwitchController.verifyAccount] (In End) Response Code: " + webServiceVO.getResponseCode());

        return webServiceVO;
    }

    @Override
    public WebServiceVO minorFatherBvsVerification(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.minorFatherBvsVerification] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "MinorFatherBVSVerfication");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                return webServiceVO;
            }
            if (appUserModel != null) {
                this.minorFatherBVSUpdate(webServiceVO, appUserModel.getCustomerId());
            } else {
                logger.info("[FonePaySwitchController.Inquiry] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.minorFatherBvsVerification] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.minorFatherBvsVerification] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.minorFatherBvsVerification] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO checqueBookStatus(WebServiceVO webServiceVO) {
        FonePayLogModel fonePayLogModel = new FonePayLogModel();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "ChecqueBookStatus");
            if ((webServiceVO.getMicrobankTransactionCode() == null) || ("".equals(webServiceVO.getMicrobankTransactionCode()))) {
                this.logger.error("[FonePaySwitchController.ChecquBookStatus] Error occured: Transaction Code not supplied ");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, "56");
            } else {
                webServiceVO = getFonePayManager().makeChecqueBookStatus(webServiceVO);
                if ((webServiceVO.getResponseCode() == null) || (webServiceVO.getResponseCode() == "00")) {
                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Success For Checque Book Status");
                }
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.INVALID_PIN.toString());
            } else {

                this.logger.error("[FonePaySwitchController.paymentReversal] Error occured:" + e.getMessage(), e);
                webServiceVO.setResponseCodeDescription(e.getMessage());
                webServiceVO.setResponseCode("10");
            }

        } finally {
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        return webServiceVO;
    }

    @Override
    public WebServiceVO cnicTo256(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.cnicTo256] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
//            webServiceVO = this.validateRRN(webServiceVO);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "CnicTo256");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null){
                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }
                webServiceVO.setShaCnic(appUserModel.getNic());
                webServiceVO.setMobileNo(appUserModel.getMobileNo());
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successfull");
            }
            else {
                logger.info("[FonePaySwitchController.cnicTo256] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.cnicTo256] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.cnicTo256] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.cnicTo256] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO transactionStatus(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.transactionStatus] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
//            webServiceVO = this.validateRRN(webServiceVO);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "transactionStatus");

//            if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
//                return webServiceVO;
//            }

            TransactionDetailMasterModel tdm = getTransactionReversalManager().loadTDMbyThridPartyRRN(webServiceVO.getThirdPartyTransactionId());
            if(tdm != null){
                webServiceVO.setTransactionId(tdm.getTransactionCode());
                webServiceVO.setStatus(tdm.getProcessingStatusName());
                webServiceVO.setTransactionAmount(String.valueOf(tdm.getTransactionAmount()));
                webServiceVO.setTotalAmount(String.valueOf(tdm.getTotalAmount()));
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successfull");
            }
            else {
                logger.info("[FonePaySwitchController.transactionStatus] Transaction Not Found against the RRN # :: " + webServiceVO.getRetrievalReferenceNumber());
                webServiceVO.setResponseCode(FonePayResponseCodes.TRANSACTION_CODE_NOT_AVALIBLE);
                webServiceVO.setResponseCodeDescription("Transaction Not Found.");
                return webServiceVO;
            }
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.transactionStatus] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.transactionStatus] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.transactionStatus] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO profileStatus(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.profileStatus] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "profileStatus");

//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null){
                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }

                CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
                webServiceVO.setWalletType(customerModel.getCustomerAccountTypeIdCustomerAccountTypeModel().getName());
                webServiceVO.setWalletStatus(String.valueOf(appUserModel.getRegistrationStateModel().getName()));
                webServiceVO.setTaxRegime(customerModel.getTaxRegimeIdTaxRegimeModel().getTaxRegimeCode());

                SmartMoneyAccountModel sma = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId
                        (appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);

                webServiceVO.setLienStatus(String.valueOf(sma.getIsOptasiaDebitBlocked()));
                webServiceVO.setShaCnic(appUserModel.getNic());
                webServiceVO.setResponseCode("00");
                webServiceVO.setResponseCodeDescription("Successfull");
            }
            else {
                logger.info("[FonePaySwitchController.profileStatus] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.profileStatus] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.profileStatus] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.profileStatus] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO lienStatus(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.lienStatus] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "lienStatus");

//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }

                SmartMoneyAccountModel sma = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId
                        (appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
                if (sma != null) {
                    sma.setIsDebitBlocked(true);
                    sma.setIsOptasiaDebitBlocked(true);
                    sma.setDebitBlockAmount(9999999999d);
                    sma.setDebitBlockReason("Debit Blocked By Optasia");
                    smartMoneyAccountDAO.saveOrUpdate(sma);

                    webServiceVO.setResponseCode("00");
                    webServiceVO.setResponseCodeDescription("Successfull");
                }
                else{
                    logger.info("[FonePaySwitchController.lienStatus] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                    webServiceVO.setResponseCode(FonePayResponseCodes.SMA_NOT_LOADED);
                    webServiceVO.setResponseCodeDescription("No Data Found in Smart Money Account");
                    return webServiceVO;
                }
            }
            else {
                logger.info("[FonePaySwitchController.lienStatus] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.lienStatus] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.lienStatus] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.lienStatus] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO initiateLoan(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.initiateLoan] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";
        Date currentDate = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "initiateLoan");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }

                CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();

                TasdeeqDataModel tasdeeqDataModel = getCommonCommandManager().loadTasdeeqDataModelByMobile(webServiceVO.getMobileNo());
                if(tasdeeqDataModel != null){
                    int daysToBePassed = Days.daysBetween(new DateTime(tasdeeqDataModel.getCreatedOn().getTime()), new DateTime(currentDate.getTime())).getDays();
                    if(daysToBePassed < 60){
                        //response from db
                        webServiceVO.setReportDate(tasdeeqDataModel.getReportDate());
                        webServiceVO.setReportTime(tasdeeqDataModel.getReportTime());
                        webServiceVO.setName(tasdeeqDataModel.getName());
                        webServiceVO.setCnicNo(tasdeeqDataModel.getCnic());
                        webServiceVO.setCity(tasdeeqDataModel.getCity());
                        webServiceVO.setNoOfActiveAccounts(tasdeeqDataModel.getNoOfActiveAccounts());
                        webServiceVO.setTotalOutstandingBalance(String.valueOf(tasdeeqDataModel.getTotalOutstandingBalance()));
                        webServiceVO.setDateOfBirth(tasdeeqDataModel.getDob());
                        webServiceVO.setPlus3024m(tasdeeqDataModel.getPlus3024M());
                        webServiceVO.setPlus6024m(tasdeeqDataModel.getPlus6024M());
                        webServiceVO.setPlus9024m(tasdeeqDataModel.getPlus9024M());
                        webServiceVO.setPlus12024m(tasdeeqDataModel.getPlus12024M());
                        webServiceVO.setPlus15024m(tasdeeqDataModel.getPlus1504M());
                        webServiceVO.setPlus18024m(tasdeeqDataModel.getPlus18024M());
                        webServiceVO.setWriteOff(tasdeeqDataModel.getWriteOff());
                        webServiceVO.setRefNo(tasdeeqDataModel.getReferenceNumber());
                    }
                    else{
                        requestVO = ESBAdapter.ecibData(I8SBConstants.RequestType_Tasdeeq_CustomAnalytics); //Request type ecibdata api
                        requestVO.setCNIC(appUserModel.getNic());
                        requestVO.setFullName(appUserModel.getFirstName() + "" + appUserModel.getLastName());
                        requestVO.setDob(dateFormat1.format(appUserModel.getDob()));
                        requestVO.setCity(appUserModel.getCity());
                        requestVO.setAmount(webServiceVO.getAmount());
                        requestVO.setGenderCode(customerModel.getGender());
                        requestVO.setAddress(appUserModel.getAddress1());
                        requestVO.setFatherName(customerModel.getFatherHusbandName());

                        SwitchWrapper sWrapper = new SwitchWrapperImpl();
                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                        if (!responseVO.getResponseCode().equals("I8SB-200")) {
                            webServiceVO.setResponseCode("65");
                            webServiceVO.setResponseCodeDescription("No success response from I8SB");
                            return webServiceVO;
                        }
                        else{
                            tasdeeqDataModel = new TasdeeqDataModel();
                            tasdeeqDataModel.setReportDate(responseVO.getDate());
                            tasdeeqDataModel.setReportTime(responseVO.getReportTime());
                            tasdeeqDataModel.setName(responseVO.getName());
                            tasdeeqDataModel.setCnic(responseVO.getCNIC());
                            tasdeeqDataModel.setCity(responseVO.getCity());
                            tasdeeqDataModel.setNoOfActiveAccounts(responseVO.getNoOfActiveAccounts());
                            tasdeeqDataModel.setDob(responseVO.getDob());
                            tasdeeqDataModel.setPlus3024M(responseVO.getPlus3024m());
                            tasdeeqDataModel.setPlus6024M(responseVO.getPlus6024m());
                            tasdeeqDataModel.setPlus9024M(responseVO.getPlus9024m());
                            tasdeeqDataModel.setPlus12024M(responseVO.getPlus12024m());
                            tasdeeqDataModel.setPlus1504M(responseVO.getPlus15024m());
                            tasdeeqDataModel.setPlus18024M(responseVO.getPlus18024m());
                            tasdeeqDataModel.setWriteOff(responseVO.getWriteOff());
                            tasdeeqDataModel.setReferenceNumber(responseVO.getRefNo());
                            tasdeeqDataModel.setValidStatus("1");
                            tasdeeqDataModel.setCreatedOn(new Date());
                            tasdeeqDataModel.setUpdatedOn(new Date());
                            tasdeeqDataModel.setCreatedBy(String.valueOf(UserUtils.getCurrentUser().getAppUserId()));
                            tasdeeqDataModel.setUpdatedBy(String.valueOf(UserUtils.getCurrentUser().getAppUserId()));

                            getCommonCommandManager().saveOrUpdateTasdeeqDataModel(tasdeeqDataModel);

                            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                        }
                    }
                }
                else {
                    requestVO = ESBAdapter.ecibData(I8SBConstants.RequestType_Tasdeeq_CustomAnalytics); //Request type ecibdata api
                    requestVO.setCNIC(appUserModel.getNic());
                    requestVO.setFullName(appUserModel.getFirstName() + " " + appUserModel.getLastName());
                    requestVO.setDob(dateFormat2.format(appUserModel.getDob()));
                    requestVO.setCity(customerModel.getBirthPlace());
                    requestVO.setAmount(webServiceVO.getAmount());
                    requestVO.setGenderCode(customerModel.getGender());
                    requestVO.setAddress(appUserModel.getAddress1());
                    requestVO.setFatherName(customerModel.getFatherHusbandName());

                    SwitchWrapper sWrapper = new SwitchWrapperImpl();
                    sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                    sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                    sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                    ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                    responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                    if (!responseVO.getResponseCode().equals("I8SB-200")) {
                        webServiceVO.setResponseCode("65");
                        webServiceVO.setResponseCodeDescription("No success response from I8SB");
                        return webServiceVO;
                    } else {
                        tasdeeqDataModel = new TasdeeqDataModel();
                        tasdeeqDataModel.setReportDate(responseVO.getDate());
                        tasdeeqDataModel.setReportTime(responseVO.getReportTime());
                        tasdeeqDataModel.setName(responseVO.getName());
                        tasdeeqDataModel.setCnic(responseVO.getCNIC());
                        tasdeeqDataModel.setCity(responseVO.getCity());
                        tasdeeqDataModel.setMobileNo(webServiceVO.getMobileNo());
                        tasdeeqDataModel.setNoOfActiveAccounts(responseVO.getNoOfActiveAccounts());
                        tasdeeqDataModel.setDob(responseVO.getDob());
                        tasdeeqDataModel.setPlus3024M(responseVO.getPlus3024m());
                        tasdeeqDataModel.setPlus6024M(responseVO.getPlus6024m());
                        tasdeeqDataModel.setPlus9024M(responseVO.getPlus9024m());
                        tasdeeqDataModel.setPlus12024M(responseVO.getPlus12024m());
                        tasdeeqDataModel.setPlus1504M(responseVO.getPlus15024m());
                        tasdeeqDataModel.setPlus18024M(responseVO.getPlus18024m());
                        tasdeeqDataModel.setWriteOff(responseVO.getWriteOff());
                        tasdeeqDataModel.setValidStatus("1");
                        tasdeeqDataModel.setReferenceNumber(responseVO.getRefNo());
                        tasdeeqDataModel.setCreatedOn(new Date());
                        tasdeeqDataModel.setUpdatedOn(new Date());
                        tasdeeqDataModel.setCreatedBy(String.valueOf(UserUtils.getCurrentUser().getAppUserId()));
                        tasdeeqDataModel.setUpdatedBy(String.valueOf(UserUtils.getCurrentUser().getAppUserId()));

                        getCommonCommandManager().saveOrUpdateTasdeeqDataModel(tasdeeqDataModel);

                        webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                        webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                    }
                }

                requestVO = new I8SBSwitchControllerRequestVO();
                responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.offerListForCommodity(I8SBConstants.RequestType_OPTASIA_OfferListForCommodity); //Request type offer list for commodity api
                requestVO.setIdentityType("customerIdentity");
                requestVO.setIdentityValue(appUserModel.getShaNic());
                requestVO.setOrigSource("mobileApp");
                requestVO.setCommodityType(webServiceVO.getCommodityType());
//                requestVO.setSTAN(webServiceVO.getReserved2());
//                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
                requestVO.setFilterType(webServiceVO.getFilterType());
                requestVO.setSourceRequestId("EXT123");
                requestVO.setOfferName(webServiceVO.getOfferName());
                requestVO.setAmount(webServiceVO.getAmount());
                //tax regime in additional info
                requestVO.setFed(customerModel.getTaxRegimeIdTaxRegimeModel().getTaxRegimeCode());

                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    webServiceVO.setResponseCode("65");
                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
                    return webServiceVO;
                }
                else{
                    webServiceVO.setIdentityValue(appUserModel.getShaNic());
                    webServiceVO.setIdentityType(webServiceVO.getMobileNo());
                    webServiceVO.setOrigSource("mobileApp");
                    webServiceVO.setReceivedTimestamp(responseVO.getReceivedTimestamp());
                    ArrayList<?> data = new ArrayList<>();

                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("EligibilityStatus");
                    if (data != null) {
                        List<?> eligibilityStatusList = data;
                        for (int i=0; i<eligibilityStatusList.size(); i++) {
                            EligibilityStatus eligibilityStatus = new EligibilityStatus();

                            eligibilityStatus.setEligible(((EligibilityStatus) eligibilityStatusList.get(i)).getEligible());
                            eligibilityStatus.setEligibilityStatus(((EligibilityStatus) eligibilityStatusList.get(i)).getEligibilityStatus());

                            List<EligibilityStatus> eligibilityStatusList1 = new ArrayList<>();
                            eligibilityStatusList1.add(eligibilityStatus);

                            webServiceVO.setEligibilityStatusList(eligibilityStatusList1);
                        }
                    }

                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("OutstandingStatus");
                    if (data != null) {
                        List<?> outstandingList = data;
                        for (int i=0; i<outstandingList.size(); i++) {

                            OutstandingStatus outstandingStatus = new OutstandingStatus();
                            List<OutstandingStatus> outstandingStatusList = new ArrayList<>();

                            outstandingStatus.setCurrencyCode(((OutstandingStatus)outstandingList.get(i)).getCurrencyCode());
                            outstandingStatus.setAvailableCreditLimit(((OutstandingStatus)outstandingList.get(i)).getAvailableCreditLimit());
                            outstandingStatus.setDynamicCreditLimit(((OutstandingStatus)outstandingList.get(i)).getDynamicCreditLimit());
                            outstandingStatus.setNumOutstandingLoans(((OutstandingStatus)outstandingList.get(i)).getNumOutstandingLoans());
                            outstandingStatus.setTotalGross(((OutstandingStatus)outstandingList.get(i)).getTotalGross());
                            outstandingStatus.setTotalPrincipal(((OutstandingStatus)outstandingList.get(i)).getTotalPrincipal());
                            outstandingStatus.setTotalSetupFees(((OutstandingStatus)outstandingList.get(i)).getTotalSetupFees());
                            outstandingStatus.setTotalInterest(((OutstandingStatus)outstandingList.get(i)).getTotalInterest());
                            outstandingStatus.setTotalInterestVAT(((OutstandingStatus)outstandingList.get(i)).getTotalInterestVAT());
                            outstandingStatus.setTotalCharges(((OutstandingStatus)outstandingList.get(i)).getTotalCharges());
                            outstandingStatus.setTotalChargesVAT(((OutstandingStatus)outstandingList.get(i)).getTotalChargesVAT());
                            outstandingStatus.setTotalPendingLoans(((OutstandingStatus)outstandingList.get(i)).getTotalPendingLoans());
                            outstandingStatus.setTotalPendingRecoveries(((OutstandingStatus)outstandingList.get(i)).getTotalPendingRecoveries());

                            outstandingStatusList.add(outstandingStatus);
                            webServiceVO.setOutstandingStatusList(outstandingStatusList);

                        }
                    }

                    List<?> interestList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("Interest");
                    if (data != null) {
                        interestList = data;
                        for (int i=0; i<interestList.size(); i++) {

                            Interest interest = new Interest();
                            List<Interest> interestList1 = new ArrayList<>();

                            interest.setInterestName(((Interest) interestList.get(i)).getInterestName());
                            interest.setInterestType(((Interest) interestList.get(i)).getInterestType());
                            interest.setInterestValue(((Interest) interestList.get(i)).getInterestValue());
                            interest.setInterestVAT(((Interest) interestList.get(i)).getInterestVAT());
                            interest.setDaysOffset(((Interest) interestList.get(i)).getDaysOffset());
                            interest.setInterval(((Interest) interestList.get(i)).getInterval());

                            interestList1.add(interest);
                            webServiceVO.setInterestList(interestList1);

                        }
                    }

                    List<?> oneOffChargesList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("OneOffCharges");
                    if (data != null) {
                        oneOffChargesList = data;
                        for (int i=0; i<oneOffChargesList.size(); i++) {

                            OneOffCharges oneOffCharges = new OneOffCharges();
                            List<OneOffCharges> oneOffCharges1 = new ArrayList<>();


                            oneOffCharges.setChargeName(((OneOffCharges) oneOffChargesList.get(i)).getChargeName());
                            oneOffCharges.setChargeType(((OneOffCharges) oneOffChargesList.get(i)).getChargeType());
                            oneOffCharges.setChargeValue(String.valueOf(((OneOffCharges) oneOffChargesList.get(i)).getChargeValue()));
                            oneOffCharges.setChargeVAT(String.valueOf(((OneOffCharges) oneOffChargesList.get(i)).getChargeVAT()));
                            oneOffCharges.setDaysOffset(String.valueOf(((OneOffCharges) oneOffChargesList.get(i)).getDaysOffset()));


                            oneOffCharges1.add(oneOffCharges);
                            webServiceVO.setOneOffChargesList(oneOffCharges1);

                        }
                    }

                    List<?> recurringChargesList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("RecurringCharges");
                    if (data != null) {
                        recurringChargesList = data;
                        for (int i=0; i<recurringChargesList.size(); i++) {

                            RecurringCharges recurringCharges = new RecurringCharges();
                            List<RecurringCharges> recurringCharges1 = new ArrayList<>();


                            recurringCharges.setChargeName(((RecurringCharges) recurringChargesList.get(i)).getChargeName());
                            recurringCharges.setChargeType(((RecurringCharges) recurringChargesList.get(i)).getChargeType());
                            recurringCharges.setChargeValue(String.valueOf(((RecurringCharges) recurringChargesList.get(i)).getChargeValue()));
                            recurringCharges.setChargeVAT(String.valueOf(((RecurringCharges) recurringChargesList.get(i)).getChargeVAT()));
                            recurringCharges.setInterval(String.valueOf(((RecurringCharges) recurringChargesList.get(i)).getDaysOffset()));


                            recurringCharges1.add(recurringCharges);
                            webServiceVO.setRecurringChargesList(recurringCharges1);

                        }
                    }

                    List<?> maturityDetailsList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("MaturityDetails");
                    if (data != null) {
                        maturityDetailsList = data;
                        for (int i=0; i<maturityDetailsList.size(); i++) {

                            MaturityDetails maturityDetails = new MaturityDetails();
                            List<MaturityDetails> maturityDetailsList1 = new ArrayList<>();

//                            loanOffers.setOfferClass(((LoanOffers)loanOffersList.get(i)).getOfferClass());
                            maturityDetails.setMaturityDuration(((MaturityDetails)maturityDetailsList.get(i)).getMaturityDuration());

                            maturityDetails.setInterestList((List<Interest>) interestList);
                            maturityDetails.setRecurringChargesList((List<RecurringCharges>) recurringChargesList);
                            maturityDetails.setOneOffChargesList((List<OneOffCharges>) oneOffChargesList);

                            maturityDetailsList1.add(maturityDetails);
                            webServiceVO.setMaturityDetailsList(maturityDetailsList1);
                        }
                    }

                    List<?> loanOffersList = new ArrayList<>();

                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("LoanOffers");
                    if (data != null) {
                        loanOffersList = data;
                        for (int i=0; i<loanOffersList.size(); i++) {
                            LoanOffers loanOffers = new LoanOffers();

                            loanOffers.setOfferClass(((LoanOffers)loanOffersList.get(i)).getOfferClass());
                            loanOffers.setOfferName(((LoanOffers) loanOffersList.get(i)).getOfferName());
                            loanOffers.setCurrencyCode(((LoanOffers) loanOffersList.get(i)).getCurrencyCode());
                            loanOffers.setPrincipalFrom(((LoanOffers) loanOffersList.get(i)).getPrincipalFrom());
                            loanOffers.setPrincipalTo(((LoanOffers) loanOffersList.get(i)).getPrincipalTo());
                            loanOffers.setSetupFees(((LoanOffers) loanOffersList.get(i)).getSetupFees());
                            loanOffers.setCommodityType(((LoanOffers) loanOffersList.get(i)).getCommodityType());
                            loanOffers.setLoanPlanId(((LoanOffers) loanOffersList.get(i)).getLoanPlanId());
                            loanOffers.setLoanPlanName(((LoanOffers) loanOffersList.get(i)).getLoanPlanName());

                            List<LoanOffers> loanOffersList1 = new ArrayList<>();

                            loanOffers.setMaturityDetailsList(webServiceVO.getMaturityDetailsList());

                            loanOffersList1.add(loanOffers);

                            webServiceVO.setLoanOffersList(loanOffersList1);

                        }
                    }

                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("LoanOffersByLoanProductGroup");
                    if (data != null) {
                        List<?> loanOffersByLoanProductGroupList = data;
                        for (int i=0; i<loanOffersByLoanProductGroupList.size(); i++) {

                            LoanOffersByLoanProductGroup loanOffersByLoanProductGroup = new LoanOffersByLoanProductGroup();

                            loanOffersByLoanProductGroup.setLoanProductGroup(((LoanOffersByLoanProductGroup) loanOffersByLoanProductGroupList.get(i)).getLoanProductGroup());


                            List<LoanOffersByLoanProductGroup> loanOffersByLoanProductGroups = new ArrayList<>();

                            loanOffersByLoanProductGroup.setLoanOffersList(webServiceVO.getLoanOffersList());

                            loanOffersByLoanProductGroups.add(loanOffersByLoanProductGroup);

                            webServiceVO.setLoanOffersByLoanProductGroupList(loanOffersByLoanProductGroups);

                        }
                    }

                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                }
            }
            else {
                logger.info("[FonePaySwitchController.initiateLoan] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.initiateLoan] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.initiateLoan] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(((WorkFlowException) e).getErrorCode());
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.initiateLoan] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO selectLoanOffer(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.selectLoanOffer] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "selectLoanOffer");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }

                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.loanOffer(I8SBConstants.RequestType_OPTASIA_PROJECTION);
                requestVO.setIdentityType("customerIdentity");
                requestVO.setIdentityValue(appUserModel.getShaNic());
                requestVO.setOrigSource("mobileApp");
                requestVO.setOfferName(webServiceVO.getOfferName());
//                requestVO.setSTAN(webServiceVO.getReserved2());
//                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
                requestVO.setAmount(webServiceVO.getLoanAmount());
                requestVO.setUpToPeriod(webServiceVO.getUpToPeriod());

                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    webServiceVO.setResponseCode("65");
                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
                }
                else{
                    webServiceVO.setIdentityType("customerIdentity");
                    webServiceVO.setIdentityValue(appUserModel.getNic());
                    webServiceVO.setOrigSource("mobileApp");
                    webServiceVO.setReceivedTimestamp(responseVO.getReceivedTimestamp());
                    List<LoanOffers> loanOffersList = new ArrayList<>();
                    LoanOffers loanOffers = new LoanOffers();
                    loanOffers.setOfferName(responseVO.getOfferName());
                    loanOffers.setOfferClass(responseVO.getOfferClass());
                    loanOffers.setAdvanceOfferId(responseVO.getAdvanceOfferId());
                    loanOffers.setCurrencyCode(responseVO.getCurrencyCode());
                    loanOffers.setPrincipalFrom(responseVO.getPrincipalFrom());
                    loanOffers.setPrincipalTo(responseVO.getPrincipalTo());
                    loanOffers.setPrincipalAmount(responseVO.getPrincipalAmount());
                    loanOffers.setSetupFees(responseVO.getSetupFees());
                    loanOffers.setCommodityType(responseVO.getCommodityType());
                    loanOffers.setLoanPlanId(responseVO.getLoanPlanId());
                    loanOffers.setLoanPlanName(responseVO.getLoanPlanName());

                    MaturityDetails maturityDetails = new MaturityDetails();

                    maturityDetails.setMaturityDuration(responseVO.getMaturityDuration());

                    List<MaturityDetails> maturityDetailsList = new ArrayList<>();

                    ArrayList<?> data = new ArrayList<>();

                    List<?> oneOffChargesList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("OneOffCharges");
                    if (data != null) {
                        oneOffChargesList = data;
                        List<OneOffCharges> oneOffCharges1 = new ArrayList<>();
                        for (int i=0; i<oneOffChargesList.size(); i++) {

                            OneOffCharges oneOffCharges = new OneOffCharges();


                            oneOffCharges.setChargeName(((OneOffCharges) oneOffChargesList.get(i)).getChargeName());
                            oneOffCharges.setChargeType(((OneOffCharges) oneOffChargesList.get(i)).getChargeType());
                            oneOffCharges.setChargeValue(String.valueOf(((OneOffCharges) oneOffChargesList.get(i)).getChargeValue()));
                            oneOffCharges.setChargeVAT(String.valueOf(((OneOffCharges) oneOffChargesList.get(i)).getChargeVAT()));
                            oneOffCharges.setDaysOffset(String.valueOf(((OneOffCharges) oneOffChargesList.get(i)).getDaysOffset()));

                            oneOffCharges1.add(oneOffCharges);
                            webServiceVO.setOneOffChargesList(oneOffCharges1);

                        }
                    }

                    List<?> recurringChargesList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("RecurringCharges");
                    if (data != null) {
                        recurringChargesList = data;
                        List<RecurringCharges> recurringCharges1 = new ArrayList<>();
                        for (int i=0; i<recurringChargesList.size(); i++) {

                            RecurringCharges recurringCharges = new RecurringCharges();


                            recurringCharges.setChargeName(((RecurringCharges) recurringChargesList.get(i)).getChargeName());
                            recurringCharges.setChargeType(((RecurringCharges) recurringChargesList.get(i)).getChargeType());
                            recurringCharges.setChargeValue(String.valueOf(((RecurringCharges) recurringChargesList.get(i)).getChargeValue()));
                            recurringCharges.setChargeVAT(String.valueOf(((RecurringCharges) recurringChargesList.get(i)).getChargeVAT()));
                            recurringCharges.setInterval(String.valueOf(((RecurringCharges) recurringChargesList.get(i)).getDaysOffset()));

                            recurringCharges1.add(recurringCharges);
                            webServiceVO.setRecurringChargesList(recurringCharges1);

                        }
                    }

                    maturityDetails.setOneOffChargesList(webServiceVO.getOneOffChargesList());
                    maturityDetails.setRecurringChargesList(webServiceVO.getRecurringChargesList());
                    maturityDetailsList.add(maturityDetails);

                    webServiceVO.setMaturityDetailsList(maturityDetailsList);

                    loanOffers.setMaturityDetailsList(maturityDetailsList);

//                    maturityDetailsList.add(maturityDetails);

                    loanOffersList.add(loanOffers);
                    webServiceVO.setLoanOffersList(loanOffersList);
//                    List<Interest> interestList = new ArrayList<>();
//                    Interest interest = new Interest();
//                    interest.setInterestName(responseVO.getInterestName());
//                    interest.setInterestType(responseVO.getInterestType());
//                    interest.setInterestValue(responseVO.getInterestValue());
//                    interest.setInterestVAT(responseVO.getInterestVAT());
//                    interest.setDaysOffset(responseVO.getDaysOffset());
//                    interest.setInterval(responseVO.getInterval());
//
//                    interestList.add(interest);
//
//
//                    interestList.add(interest);
//                    maturityDetails.setInterestList(interestList);

//                    List<OneOffCharges> oneOffChargesList = new ArrayList<>();
//                    OneOffCharges oneOffCharges = new OneOffCharges();
//                    oneOffCharges.setChargeName(responseVO.getChargeName());
//                    oneOffCharges.setChargeType(responseVO.getChargeType());
//                    oneOffCharges.setChargeValue(responseVO.getChargeValue());
//                    oneOffCharges.setChargeVAT(responseVO.getChargeVAT());
//                    oneOffCharges.setDaysOffset(responseVO.getDaysOffset());
//                    oneOffChargesList.add(oneOffCharges);



                    List<?> chargeAdjustmentList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("ChargeAdjustments");
                    if (data != null) {
                        chargeAdjustmentList = data;
                        List<ChargeAdjustments> chargeAdjustmentList1 = new ArrayList<>();
                        for (int i=0; i<chargeAdjustmentList.size(); i++) {
                            ChargeAdjustments chargeAdjustments = new ChargeAdjustments();

                            chargeAdjustments.setGross(((ChargeAdjustments) chargeAdjustmentList.get(i)).getGross());
                            chargeAdjustments.setNet(((ChargeAdjustments) chargeAdjustmentList.get(i)).getNet());
                            chargeAdjustments.setVat(((ChargeAdjustments) chargeAdjustmentList.get(i)).getVat());
                            chargeAdjustments.setName(((ChargeAdjustments) chargeAdjustmentList.get(i)).getName());


                            chargeAdjustmentList1.add(chargeAdjustments);

                            webServiceVO.setChargeAdjustmentsList(chargeAdjustmentList1);
                        }
                    }

                    List<?> interestAdjustmentList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("InterestAdjustment");
                    if (data != null) {
                        interestAdjustmentList = data;
                        List<InterestAdjustment> interestAdjustmentList1 = new ArrayList<>();
                        for (int i=0; i<interestAdjustmentList.size(); i++) {
                            InterestAdjustment interestAdjustment = new InterestAdjustment();

                            interestAdjustment.setGross(((InterestAdjustment) interestAdjustmentList.get(i)).getGross());
                            interestAdjustment.setNet(((InterestAdjustment) interestAdjustmentList.get(i)).getNet());
                            interestAdjustment.setVat(((InterestAdjustment) interestAdjustmentList.get(i)).getVat());


                            interestAdjustmentList1.add(interestAdjustment);

                            webServiceVO.setInterestAdjustmentsList(interestAdjustmentList1);
                        }
                    }

//                    List<?> milestonesList = new ArrayList<>();
//                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("Milestones");
//                    if (data != null) {
//                        milestonesList = data;
//                        List<Milestones> milestonesList1 = new ArrayList<>();
//                        for (int i=0; i<milestonesList.size(); i++) {
//                            Milestones milestones = new Milestones();
//
//                            milestones.setDayOfLoan(((Milestones) milestonesList.get(i)).getDayOfLoan());
//                            milestones.setDate(((Milestones) milestonesList.get(i)).getDate());
//                            milestones.setPrincipal(((Milestones) milestonesList.get(i)).getPrincipal());
//                            milestones.setTotalExpenses(((Milestones) milestonesList.get(i)).getTotalExpenses());
//                            milestones.setTotalGross(((Milestones) milestonesList.get(i)).getTotalGross());
//                            milestones.setTotalInterest(((Milestones) milestonesList.get(i)).getTotalInterest());
//                            milestones.setTotalInterestVAT(((Milestones) milestonesList.get(i)).getTotalInterestVAT());
//                            milestones.setTotalCharges(((Milestones) milestonesList.get(i)).getTotalCharges());
//                            milestones.setTotalChargesVAT(((Milestones) milestonesList.get(i)).getTotalChargesVAT());
//
//                            milestones.setInterestAdjustmentList(webServiceVO.getInterestAdjustmentsList());
//                            milestones.setChargeAdjustmentsList(webServiceVO.getChargeAdjustmentsList());
//
//                            milestonesList1.add(milestones);
//
//                            webServiceVO.setMilestonesList(milestonesList1);
//                        }
//                    }

                    List<?> totalOneOffChargesList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("TotalOneOffCharges");
                    if (data != null) {
                        totalOneOffChargesList = data;
                        List<TotalOneOffCharges> totalOneOffChargesList1 = new ArrayList<>();
                        for (int i=0; i<totalOneOffChargesList.size(); i++) {
                            TotalOneOffCharges totalOneOffCharges = new TotalOneOffCharges();

                            totalOneOffCharges.setCharge(((TotalOneOffCharges) totalOneOffChargesList.get(i)).getCharge());
                            totalOneOffCharges.setChargeName(((TotalOneOffCharges) totalOneOffChargesList.get(i)).getChargeName());
                            totalOneOffCharges.setChargeVAT(((TotalOneOffCharges) totalOneOffChargesList.get(i)).getChargeVAT());

                            totalOneOffChargesList1.add(totalOneOffCharges);

                            webServiceVO.setTotalOneOffChargesList(totalOneOffChargesList1);
                        }
                    }

                    List<?> totalRecurringChargesList = new ArrayList<>();
                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("TotalRecurringCharges");
                    if (data != null) {
                        totalRecurringChargesList = data;
                        List<TotalRecurringCharge> totalRecurringChargesList1 = new ArrayList<>();
                        for (int i=0; i<totalOneOffChargesList.size(); i++) {
                            TotalRecurringCharge totalRecurringCharge = new TotalRecurringCharge();

                            totalRecurringCharge.setCharge(((TotalRecurringCharge) totalRecurringChargesList.get(i)).getCharge());
                            totalRecurringCharge.setChargeName(((TotalRecurringCharge) totalRecurringChargesList.get(i)).getChargeName());
                            totalRecurringCharge.setChargeVAT(((TotalRecurringCharge) totalRecurringChargesList.get(i)).getChargeVAT());

                            totalRecurringChargesList1.add(totalRecurringCharge);

                            webServiceVO.setTotalRecurringChargeList(totalRecurringChargesList1);
                        }
                    }

                    data = (ArrayList<?>) responseVO.getCollectionOfList().get("Projections");
                    if (data != null) {
                        List<?> projectionsList = data;
                        List<PeriodsProjection> periodsProjectionList = new ArrayList<>();
                        for (int i=0; i<projectionsList.size(); i++) {
                            PeriodsProjection periodsProjection = new PeriodsProjection();

                            periodsProjection.setPeriodIndex(((PeriodsProjection) projectionsList.get(i)).getPeriodIndex());
                            periodsProjection.setPeriodType(((PeriodsProjection) projectionsList.get(i)).getPeriodType());
                            periodsProjection.setPeriodStartTimemp(((PeriodsProjection) projectionsList.get(i)).getPeriodStartTimemp());
                            periodsProjection.setPeriodEndTimestamp(((PeriodsProjection) projectionsList.get(i)).getPeriodEndTimestamp());
                            periodsProjection.setPeriodStartDayOfLoanIndex(((PeriodsProjection) projectionsList.get(i)).getPeriodStartDayOfLoanIndex());
                            periodsProjection.setPeriodEndDayOfLoanIndex(((PeriodsProjection) projectionsList.get(i)).getPeriodEndDayOfLoanIndex());
                            periodsProjection.setPrincipal(((PeriodsProjection) projectionsList.get(i)).getPrincipal());
                            periodsProjection.setTotalExpenses(((PeriodsProjection) projectionsList.get(i)).getTotalExpenses());
                            periodsProjection.setTotalGross(((PeriodsProjection) projectionsList.get(i)).getTotalGross());
                            periodsProjection.setTotalInterest(((PeriodsProjection) projectionsList.get(i)).getTotalInterest());
                            periodsProjection.setTotalInterestVAT(((PeriodsProjection) projectionsList.get(i)).getTotalInterestVAT());
                            periodsProjection.setTotalCharges(((PeriodsProjection) projectionsList.get(i)).getTotalCharges());
                            periodsProjection.setTotalChargesVAT(((PeriodsProjection) projectionsList.get(i)).getTotalChargesVAT());

                            periodsProjection.setMilestonesList(((PeriodsProjection) projectionsList.get(i)).getMilestonesList());
                            periodsProjection.setTotalOneOffChargesList(webServiceVO.getTotalOneOffChargesList());
//                            periodsProjection.setMilestonesList(webServiceVO.getMilestonesList());
                            periodsProjection.setTotalRecurringCharges(webServiceVO.getTotalRecurringChargeList());
                            periodsProjectionList.add(periodsProjection);

                            webServiceVO.setPeriodsProjectionList(periodsProjectionList);
                        }
                    }

                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                }
            }
            else {
                logger.info("[FonePaySwitchController.selectLoanOffer] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.selectLoanOffer] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.selectLoanOffer] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(((WorkFlowException) e).getErrorCode());
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.selectLoanOffer] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO selectLoan(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.selectLoan] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "selectLoan");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }
                CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.loans(I8SBConstants.RequestType_OPTASIA_LOANOFFER);
                requestVO.setIdentityType("customerIdentity");
                requestVO.setIdentityValue(appUserModel.getShaNic());
                requestVO.setOrigSource("mobileApp");
                requestVO.setSourceRequestId(webServiceVO.getRetrievalReferenceNumber());
                requestVO.setOfferName(webServiceVO.getOfferName());
                requestVO.setAmount(webServiceVO.getAmount());
//                requestVO.setFed(customerModel.getTaxRegimeIdTaxRegimeModel().getTaxRegimeCode());
//                requestVO.setMerchantId(webServiceVO.getMerchantId());
//                requestVO.setLoanPurpose(webServiceVO.getLoanPurpose());

                ArrayList additionalInfolist = new ArrayList<>();

                AdditionalInfo additionalInfo = new AdditionalInfo();
                additionalInfo.setInfoKey("loanPurpose");
                additionalInfo.setInfoValue(webServiceVO.getLoanPurpose());
                additionalInfolist.add(additionalInfo);

                additionalInfo = new AdditionalInfo();
                additionalInfo.setInfoKey("merchantId");
                additionalInfo.setInfoValue(webServiceVO.getMerchantId());
                additionalInfolist.add(additionalInfo);

                additionalInfo = new AdditionalInfo();
                additionalInfo.setInfoKey("FED");
                additionalInfo.setInfoValue(customerModel.getTaxRegimeIdTaxRegimeModel().getTaxRegimeCode());
                additionalInfolist.add(additionalInfo);

                requestVO.setAdditionalInfoList(additionalInfolist);

                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    webServiceVO.setResponseCode("65");
                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
                }
                else{
                    TransactionDetailMasterModel tdm = getTransactionReversalManager().loadCurrentTDMbyMobileNumber
                            (webServiceVO.getMobileNo(), String.valueOf(ProductConstantsInterface.LOAN_XTRA_CASH));

                    if(tdm != null){
                        webServiceVO.setTransactionId(tdm.getTransactionCode());
                        webServiceVO.setLoanAmount(String.valueOf(tdm.getTransactionAmount()));
                        webServiceVO.setProcessingFee(String.valueOf(tdm.getExclusiveCharges()));
                        webServiceVO.setTotalAmount(String.valueOf(tdm.getTotalAmount()));
                    }
                    webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());

                    webServiceVO.setStatus(responseVO.getCode());
                    webServiceVO.setMessage(responseVO.getMessage());
                    webServiceVO.setPrimaryLoanId(responseVO.getPrimaryLoanId());
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                }
            }
            else {
                logger.info("[FonePaySwitchController.selectLoan] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.selectLoan] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.selectLoan] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(((WorkFlowException) e).getErrorCode());
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.selectLoan] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO payLoan(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.payloan] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "payloan");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.loans(I8SBConstants.RequestType_OPTASIA_OUTSTANDING);
                requestVO.setIdentityType("customerIdentity");
                requestVO.setIdentityValue(appUserModel.getShaNic());
                requestVO.setOrigSource("mobileApp");

                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    webServiceVO.setResponseCode("65");
                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
                }
                else{
                    webServiceVO.setProcessingFee(responseVO.getTotalCharges());
                    webServiceVO.setProductID(String.valueOf(ProductConstantsInterface.LOAN_XTRA_CASH_REPAYMENT));
                    webServiceVO.setTransactionAmount(webServiceVO.getAmount());
                    this.debit(webServiceVO);
                    if(webServiceVO.getResponseCode().equals("00")) {

//                        Double outStandingAmount = Double.valueOf(responseVO.getTotalGross());
//                        Double payAmount = Double.valueOf(webServiceVO.getAmount());

//                        Double remainingAmount = outStandingAmount - payAmount;

//                        SmartMoneyAccountModel sma = getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId
//                                (appUserModel, PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
//                        if (sma != null) {
//                            if (remainingAmount > 0) {
//                                sma.setIsDebitBlocked(true);
//                                sma.setDebitBlockAmount(remainingAmount);
//                                sma.setIsOptasiaDebitBlocked(true);
//                                sma.setDebitBlockReason("Debit Blocked By Optasia");
//                                sma.setUpdatedOn(new Date());
//                                smartMoneyAccountDAO.saveOrUpdate(sma);
//                            }
//                        }

                        requestVO = new I8SBSwitchControllerRequestVO();
                        responseVO = new I8SBSwitchControllerResponseVO();
                        requestVO = ESBAdapter.loans(I8SBConstants.RequestType_OPTASIA_PAYLOAN);
                        requestVO.setIdentityType("customerIdentity");
                        requestVO.setIdentityValue(appUserModel.getShaNic());
                        requestVO.setOrigSource("mobileApp");
                        requestVO.setSourceRequestId(webServiceVO.getRetrievalReferenceNumber());
                        requestVO.setAmount(webServiceVO.getAmount());
                        requestVO.setCurrencyCode(webServiceVO.getCurrencyCode());
                        requestVO.setReason(webServiceVO.getReason());

                        sWrapper = new SwitchWrapperImpl();
                        sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                        sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                        sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                        ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                        responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                        if (!responseVO.getResponseCode().equals("I8SB-200")) {
                            webServiceVO.setResponseCode("65");
                            webServiceVO.setResponseCodeDescription("No success response from I8SB");
                        }
                        else {
//                            TransactionDetailMasterModel tdm = getTransactionReversalManager().loadTDMbyProductId
//                                    (webServiceVO.getMobileNo(), String.valueOf(ProductConstantsInterface.LOAN_XTRA_CASH));
//                            if (tdm != null) {
//                                webServiceVO.setTransactionId(tdm.getTransactionCode());
//                            } else {
//                                webServiceVO.setTransactionId("");
//                            }

                            webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
                            webServiceVO.setLoanAmount(webServiceVO.getLoanAmount());
                            webServiceVO.setTotalAmount(webServiceVO.getTotalAmount());
                            webServiceVO.setTransactionId(webServiceVO.getTransactionId());
                            webServiceVO.setStatus(responseVO.getCode());
                            webServiceVO.setMessage(responseVO.getMessage());
                            webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                            webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                        }
                    }
                    else{
                        webServiceVO.setResponseCode(webServiceVO.getResponseCode());
                        webServiceVO.setResponseCodeDescription(webServiceVO.getResponseCodeDescription());
                    }
                }
            }
            else {
                logger.info("[FonePaySwitchController.payloan] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.payloan] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.payloan] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(((WorkFlowException) e).getErrorCode());
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.payloan] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

//    @Override
//    public WebServiceVO repayLoan(WebServiceVO webServiceVO) {
//        logger.info("[FonePaySwitchController.repayLoan] Start:: ");
//        FonePayLogModel fonePayLogModel = null;
//        ActionLogModel actionLogModel = null;
//        AppUserModel appUserModel = new AppUserModel();
//        String xml = "";
//
//        BaseWrapper baseWrapper = new BaseWrapperImpl();
//        try {
//            webServiceVO = this.validateRRN(webServiceVO);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "repayLoan");
//
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
//            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
//                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
//                    return webServiceVO;
//                }
//
//                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//                requestVO = ESBAdapter.offerListForCommodity(I8SBConstants.RequestType_OPTASIA_OfferListForCommodity); //Request type offer list for commodity api
//                requestVO.setIdentityType("customerIdentity");
//                requestVO.setIdentityValue(webServiceVO.getShaCnic());
//                requestVO.setOrigSource("mobileApp");
//                requestVO.setCommodityType(webServiceVO.getCommodityType());
////                requestVO.setSTAN(webServiceVO.getReserved2());
////                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
//                requestVO.setFilterType(webServiceVO.getFilterType());
//
//                SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                    webServiceVO.setResponseCode("65");
//                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
//                    return webServiceVO;
//                }
//                else{
//                    webServiceVO.setOfferName(responseVO.getOfferName());
//                    webServiceVO.setTotalGross(responseVO.getTotalGross());
//                }
//
//                requestVO = new I8SBSwitchControllerRequestVO();
//                responseVO = new I8SBSwitchControllerResponseVO();
//                requestVO = ESBAdapter.offerListForCommodity(I8SBConstants.RequestType_OPTASIA_OUTSTANDING); //Request type offer list for commodity api
//                requestVO.setIdentityType("customerIdentity");
//                requestVO.setIdentityValue(webServiceVO.getShaCnic());
//                requestVO.setOrigSource("mobileApp");
//                requestVO.setCommodityType("CASH");
////                requestVO.setSTAN(webServiceVO.getReserved2());
////                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
//
//                sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                    webServiceVO.setResponseCode("65");
//                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
//                    return webServiceVO;
//                }
//
//                requestVO = new I8SBSwitchControllerRequestVO();
//                responseVO = new I8SBSwitchControllerResponseVO();
//                requestVO = ESBAdapter.repayment(I8SBConstants.RequestType_OPTASIA_LOANS);
//                requestVO.setIdentityType("customerIdentity");
//                requestVO.setIdentityValue(webServiceVO.getShaCnic());
//                requestVO.setOrigSource("mobileApp");
//                requestVO.setFilterCommodityType("CASH");
//                requestVO.setSTAN(webServiceVO.getReserved2());
//                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
//                requestVO.setFilterLoanState("OPEN/ALL");
//
//                sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                    webServiceVO.setResponseCode("65");
//                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
//                }
//
//                requestVO = new I8SBSwitchControllerRequestVO();
//                responseVO = new I8SBSwitchControllerResponseVO();
//                requestVO = ESBAdapter.repayment(I8SBConstants.RequestType_OPTASIA_REPAYLOAN);
//                requestVO.setIdentityType("customerIdentity");
//                requestVO.setIdentityValue(webServiceVO.getShaCnic());
//                requestVO.setOrigSource("mobileApp");
//                requestVO.setCommodityType("CASH");
//                requestVO.setSTAN(webServiceVO.getReserved2());
//                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
//                requestVO.setReason("userIntiated/serviceInitiated");
//
//                sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                    webServiceVO.setResponseCode("65");
//                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
//                }
//            }
//            else {
//                logger.info("[FonePaySwitchController.repayLoan] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
//                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
//                webServiceVO.setResponseCodeDescription("User Not Found.");
//                return webServiceVO;
//            }
//
//        } catch (Exception e) {
//            logger.error("[FonePaySwitchController.repayLoan] Error occured: " + e.getMessage(), e);
//
//            this.logger.error("[FonePaySwitchController.selectLoan] Error occured: " + e.getMessage(), e);
//            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            if (e instanceof NullPointerException
//                    || e instanceof HibernateException
//                    || e instanceof SQLException
//                    || e instanceof DataAccessException
//                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {
//
//                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
//                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
//            }
//
//        } finally {
//            ThreadLocalAppUser.remove();
//            ThreadLocalUserDeviceAccounts.remove();
//            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
//        }
//        logger.info("[FonePaySwitchController.repayLoan] (In End) Response Code: " + webServiceVO.getResponseCode());
//        return webServiceVO;
//    }

//    @Override
//    public WebServiceVO getLoanSummary(WebServiceVO webServiceVO) {
//        logger.info("[FonePaySwitchController.getLoanSummary] Start:: ");
//        FonePayLogModel fonePayLogModel = null;
//        ActionLogModel actionLogModel = null;
//        AppUserModel appUserModel = new AppUserModel();
//        String xml = "";
//
//        BaseWrapper baseWrapper = new BaseWrapperImpl();
//        try {
//            webServiceVO = this.validateRRN(webServiceVO);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "getLoanSummary");
//
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
//            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
//                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
//                    return webServiceVO;
//                }
//                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//                requestVO = ESBAdapter.loanSummary(I8SBConstants.RequestType_OPTASIA_LOANSUMMARY);
//                requestVO.setIdentityType("customerIdentity");
//                requestVO.setIdentityValue(webServiceVO.getShaCnic());
//                requestVO.setOrigSource("mobileApp");
////                requestVO.setSTAN(webServiceVO.getReserved2());
////                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
//
//                SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                    webServiceVO.setResponseCode("65");
//                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
//                }
//                else{
//
//                }
//            }
//            else {
//                logger.info("[FonePaySwitchController.getLoanSummary] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
//                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
//                webServiceVO.setResponseCodeDescription("User Not Found.");
//                return webServiceVO;
//            }
//
//        } catch (Exception e) {
//            logger.error("[FonePaySwitchController.getLoanSummary] Error occured: " + e.getMessage(), e);
//
//            this.logger.error("[FonePaySwitchController.getLoanSummary] Error occured: " + e.getMessage(), e);
//            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            if (e instanceof NullPointerException
//                    || e instanceof HibernateException
//                    || e instanceof SQLException
//                    || e instanceof DataAccessException
//                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {
//
//                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
//                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
//            }
//
//        } finally {
//            ThreadLocalAppUser.remove();
//            ThreadLocalUserDeviceAccounts.remove();
//            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
//        }
//        logger.info("[FonePaySwitchController.getLoanSummary] (In End) Response Code: " + webServiceVO.getResponseCode());
//        return webServiceVO;
//    }

//    @Override
//    public WebServiceVO payLoan(WebServiceVO webServiceVO) {
//        logger.info("[FonePaySwitchController.payLoan] Start:: ");
//        FonePayLogModel fonePayLogModel = null;
//        ActionLogModel actionLogModel = null;
//        AppUserModel appUserModel = new AppUserModel();
//        String xml = "";
//
//        BaseWrapper baseWrapper = new BaseWrapperImpl();
//        try {
//            webServiceVO = this.validateRRN(webServiceVO);
//            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
//                return webServiceVO;
//            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "payLoan");
//
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
//            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
//                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
//                    return webServiceVO;
//                }
//                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
//                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
//                requestVO = ESBAdapter.payLoan(I8SBConstants.RequestType_OPTASIA_PAYLOAN);
//                requestVO.setUserId(String.valueOf(appUserModel.getAppUserId()));
//                requestVO.setCNIC(appUserModel.getNic());
//                requestVO.setCardNumber("");
//                requestVO.setMerchantName(webServiceVO.getTerminalId());
//                requestVO.setSTAN(webServiceVO.getReserved2());
//                requestVO.setRRN(webServiceVO.getRetrievalReferenceNumber());
//                requestVO.setMobileNumber(appUserModel.getMobileNo());
//                requestVO.setTransactionAmount(webServiceVO.getTransactionAmount());
//                requestVO.setTransactionCodeDesc("POS PURCHASE");
//                requestVO.setTransactionType("M");
////                requestVO.setSegmentId(String.valueOf(customerModel.getSegmentId()));
//
//                SwitchWrapper sWrapper = new SwitchWrapperImpl();
//                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
//                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
//                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
//                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
//
//                if (!responseVO.getResponseCode().equals("I8SB-200")) {
//                    webServiceVO.setResponseCode("65");
//                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
//                }
//            }
//            else {
//                logger.info("[FonePaySwitchController.payLoan] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
//                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
//                webServiceVO.setResponseCodeDescription("User Not Found.");
//                return webServiceVO;
//            }
//
//        } catch (Exception e) {
//            logger.error("[FonePaySwitchController.payLoan] Error occured: " + e.getMessage(), e);
//
//            this.logger.error("[FonePaySwitchController.payLoan] Error occured: " + e.getMessage(), e);
//            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
//            webServiceVO.setResponseCodeDescription(e.getMessage());
//            if (e instanceof NullPointerException
//                    || e instanceof HibernateException
//                    || e instanceof SQLException
//                    || e instanceof DataAccessException
//                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {
//
//                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
//                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
//            }
//
//        } finally {
//            ThreadLocalAppUser.remove();
//            ThreadLocalUserDeviceAccounts.remove();
//            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
//        }
//        logger.info("[FonePaySwitchController.payLoan] (In End) Response Code: " + webServiceVO.getResponseCode());
//        return webServiceVO;
//    }

    @Override
    public WebServiceVO outstandingLoanStatus(WebServiceVO webServiceVO) {
        //call opatsia api loans
        logger.info("[FonePaySwitchController.outstandingLoanStatus] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "outstandingLoanStatus");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }
                I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
                I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
                requestVO = ESBAdapter.loans(I8SBConstants.RequestType_OPTASIA_LOANS);
                requestVO.setIdentityType("customerIdentity");
                requestVO.setIdentityValue(appUserModel.getShaNic());
                requestVO.setOrigSource("mobileApp");
                requestVO.setFilterCommodityType("CASH");
                requestVO.setFilterLoanState("OPEN");

                SwitchWrapper sWrapper = new SwitchWrapperImpl();
                sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
                sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
                sWrapper = esbAdapter.makeI8SBCall(sWrapper);
                ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
                responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();

                if (!responseVO.getResponseCode().equals("I8SB-200")) {
                    webServiceVO.setResponseCode("65");
                    webServiceVO.setResponseCodeDescription("No success response from I8SB");
                }
                else{
                    new ArrayList();
                    ArrayList<?> data = (ArrayList)responseVO.getCollectionOfList().get("Plan");
                    ArrayList repaymentList;
                    int i;
                    ArrayList repaymentList1;
                    if (data != null) {
                        repaymentList = data;

                        for(i = 0; i < repaymentList.size(); ++i) {
                            Plan plan = new Plan();
                            plan.setCurrentPeriod(((Plan)repaymentList.get(i)).getCurrentPeriod());
                            plan.setDaysLeftInPeriod(((Plan)repaymentList.get(i)).getDaysLeftInPeriod());
                            plan.setNextPeriod(((Plan)repaymentList.get(i)).getNextPeriod());
                            repaymentList1 = new ArrayList();
                            repaymentList1.add(plan);
                            webServiceVO.setPlanList(repaymentList1);
                        }
                    }

                    data = (ArrayList)responseVO.getCollectionOfList().get("Events");
                    ArrayList eventsList;
                    ArrayList eventsList1;

                    if (data != null) {
                        eventsList = data;
                        for (i = 0; i < eventsList.size(); ++i) {

                            Events events = new Events();
                            events.setEventType(((Events) eventsList.get(i)).getEventType());
                            events.setEventTypeDetails(((Events) eventsList.get(i)).getEventTypeDetails());
                            events.setEventTypeStatus(((Events) eventsList.get(i)).getEventTypeStatus());
                            events.setEventTransactionId(((Events) eventsList.get(i)).getEventTransactionId());
                            events.setThirdPartyTransactionId(((Events) eventsList.get(i)).getThirdPartyTransactionId());
                            events.setEventReason(((Events) eventsList.get(i)).getEventReason());
                            events.setEventReasonDetails(((Events) eventsList.get(i)).getEventReasonDetails());
                            events.setPeriod(((Events) eventsList.get(i)).getPeriod());
                            events.setPeriodIndex(((Events) eventsList.get(i)).getPeriodIndex());
                            events.setPeriodExpirationTimestamp(((Events) eventsList.get(i)).getPeriodExpirationTimestamp());
                            events.setPrincipalAdjustment(((Events) eventsList.get(i)).getPrincipalAdjustment());
                            events.setPrincipalBefore(((Events) eventsList.get(i)).getPrincipalBefore());
                            events.setPrincipalAfter(((Events) eventsList.get(i)).getPrincipalAfter());
                            events.setSetupFeesAdjustment(((Events) eventsList.get(i)).getSetupFeesAdjustment());
                            events.setSetupFeesBefore(((Events) eventsList.get(i)).getSetupFeesBefore());
                            events.setSetupFeesAfter(((Events) eventsList.get(i)).getSetupFeesAfter());
                            events.setInterestAdjustment(((Events) eventsList.get(i)).getInterestAdjustment());
                            events.setInterestAdjustmentVAT(((Events) eventsList.get(i)).getInterestAdjustmentVAT());
                            events.setInterestBefore(((Events) eventsList.get(i)).getInterestBefore());
                            events.setInterestAfter(((Events) eventsList.get(i)).getInterestAfter());
                            events.setTotalChargesAdjustment(((Events) eventsList.get(i)).getTotalChargesAdjustment());
                            events.setTotalChargesAdjustmentVAT(((Events) eventsList.get(i)).getTotalChargesAdjustmentVAT());
                            events.setTotalChargesBefore(((Events) eventsList.get(i)).getTotalChargesBefore());
                            events.setTotalChargesAfter(((Events) eventsList.get(i)).getTotalChargesAfter());
                            events.setEventTimestamp(((Events) eventsList.get(i)).getEventTimestamp());
                            events.setReceptionTimestamp(((Events) eventsList.get(i)).getReceptionTimestamp());
                            events.setProcessingTimestamp(((Events) eventsList.get(i)).getProcessingTimestamp());
                            events.setRemoteRequestId(((Events) eventsList.get(i)).getRemoteRequestId());
                            events.setSourceRequestId(((Events) eventsList.get(i)).getSourceRequestId());
                            events.setOfferName(((Events) eventsList.get(i)).getOfferName());
                            events.setCommodityType(((Events) eventsList.get(i)).getCommodityType());
                            events.setCurrencyCode(((Events) eventsList.get(i)).getCurrencyCode());
                            events.setPrincipalAmount(((Events) eventsList.get(i)).getPrincipalAmount());
                            events.setSetupFees(((Events) eventsList.get(i)).getSetupFees());
                            events.setLoanProductGroup(((Events) eventsList.get(i)).getLoanProductGroup());
                            events.setLoanPlanId(((Events) eventsList.get(i)).getLoanPlanId());
                            events.setLoanPlanName(((Events) eventsList.get(i)).getLoanPlanName());
                            events.setMaturityDetails(((Events) eventsList.get(i)).getMaturityDetails());
//                            events.setProjectSpecific(((Events) eventsList.get(i)).getProjectSpecific());
                            events.setLoanReason(((Events) eventsList.get(i)).getLoanReason());
                            events.setLoanReasonDetails(((Events) eventsList.get(i)).getLoanReasonDetails());
                            eventsList1 = new ArrayList();
                            eventsList1.add(events);
                            webServiceVO.setEventsList(eventsList1);
                        }
                    }

                    data = (ArrayList)responseVO.getCollectionOfList().get("Outstanding");
                    if (data != null) {
                        repaymentList = data;

                        for(i = 0; i < repaymentList.size(); ++i) {
                            OutstandingStatus outstandingStatus = new OutstandingStatus();
                            repaymentList1 = new ArrayList();
                            outstandingStatus.setCurrencyCode(((OutstandingStatus)repaymentList.get(i)).getCurrencyCode());
                            outstandingStatus.setNumOutstandingLoans(((OutstandingStatus)repaymentList.get(i)).getNumOutstandingLoans());
                            outstandingStatus.setTotalGross(((OutstandingStatus)repaymentList.get(i)).getTotalGross());
                            outstandingStatus.setTotalPrincipal(((OutstandingStatus)repaymentList.get(i)).getTotalPrincipal());
                            outstandingStatus.setTotalSetupFees(((OutstandingStatus)repaymentList.get(i)).getTotalSetupFees());
                            outstandingStatus.setTotalInterest(((OutstandingStatus)repaymentList.get(i)).getTotalInterest());
                            outstandingStatus.setTotalInterestVAT(((OutstandingStatus)repaymentList.get(i)).getTotalInterestVAT());
                            outstandingStatus.setTotalCharges(((OutstandingStatus)repaymentList.get(i)).getTotalCharges());
                            outstandingStatus.setTotalChargesVAT(((OutstandingStatus)repaymentList.get(i)).getTotalChargesVAT());
                            outstandingStatus.setTotalPendingLoans(((OutstandingStatus)repaymentList.get(i)).getTotalPendingLoans());
                            outstandingStatus.setTotalPendingRecoveries(((OutstandingStatus)repaymentList.get(i)).getTotalPendingRecoveries());
                            repaymentList1.add(outstandingStatus);
                            webServiceVO.setOutstandingStatusList(repaymentList1);
                        }
                    }

                    new ArrayList();
                    data = (ArrayList)responseVO.getCollectionOfList().get("Repayment");
                    if (data != null) {
                        repaymentList = data;

                        for(i = 0; i < repaymentList.size(); ++i) {
                            Repayment repayment = new Repayment();
                            repayment.setRepaymentsCount(((Repayment)repaymentList.get(i)).getRepaymentsCount());
                            repayment.setGross(((Repayment)repaymentList.get(i)).getGross());
                            repayment.setPrincipal(((Repayment)repaymentList.get(i)).getPrincipal());
                            repayment.setSetupFees(((Repayment)repaymentList.get(i)).getSetupFees());
                            repayment.setInterest(((Repayment)repaymentList.get(i)).getInterest());
                            repayment.setInterestVAT(((Repayment)repaymentList.get(i)).getInterestVAT());
                            repayment.setCharges(((Repayment)repaymentList.get(i)).getCharges());
                            repayment.setChargesVAT(((Repayment)repaymentList.get(i)).getChargesVAT());
                            repaymentList1 = new ArrayList();
                            repaymentList1.add(repayment);
                            webServiceVO.setRepaymentList(repaymentList1);
                        }
                    }

                    data = (ArrayList)responseVO.getCollectionOfList().get("Report");
                    ArrayList loanOffersList1;
                    ArrayList loanOffersList;
//                    int i;
                    if (data != null) {
                        loanOffersList = data;

                        for(i = 0; i < loanOffersList.size(); ++i) {
                            Report report = new Report();
                            report.setRepaymentList(webServiceVO.getRepaymentList());
                            report.setOutstandingStatusList(webServiceVO.getOutstandingStatusList());
                            report.setPlanList(webServiceVO.getPlanList());
                            loanOffersList1 = new ArrayList();
                            loanOffersList1.add(report);
                            webServiceVO.setReportList(loanOffersList1);
                        }
                    }

                    new ArrayList();
                    data = (ArrayList)responseVO.getCollectionOfList().get("LoanOffers");
                    if (data != null) {
                        loanOffersList = data;

                        for(i = 0; i < loanOffersList.size(); ++i) {
                            LoanOffers loanOffers = new LoanOffers();
                            loanOffers.setOfferName(((LoanOffers)loanOffersList.get(i)).getOfferName());
                            loanOffers.setAdvanceOfferId(((LoanOffers)loanOffersList.get(i)).getAdvanceOfferId());
                            loanOffers.setCommodityType(((LoanOffers)loanOffersList.get(i)).getCommodityType());
                            loanOffers.setCurrencyCode(((LoanOffers)loanOffersList.get(i)).getCurrencyCode());
                            loanOffers.setSetupFees(((LoanOffers)loanOffersList.get(i)).getSetupFees());
                            loanOffers.setLoanPlanId(((LoanOffers)loanOffersList.get(i)).getLoanPlanId());
                            loanOffers.setLoanPlanName(((LoanOffers)loanOffersList.get(i)).getLoanPlanName());
                            loanOffers.setLoanProductGroup(((LoanOffers)loanOffersList.get(i)).getLoanProductGroup());
                            loanOffers.setPrincipalAmount(((LoanOffers)loanOffersList.get(i)).getPrincipalAmount());
                            loanOffers.setMaturityDetailsList(((LoanOffers)loanOffersList.get(i)).getMaturityDetailsList());
                            loanOffersList1 = new ArrayList();
                            loanOffersList1.add(loanOffers);
                            webServiceVO.setLoanOffersList(loanOffersList1);
                        }
                    }

                    data = (ArrayList)responseVO.getCollectionOfList().get("Loan");
                    ArrayList loansPerStateList1;
                    ArrayList loansPerStateList;
//                    int i;
                    if (data != null) {
                        loansPerStateList = data;

                        for(i = 0; i < loansPerStateList.size(); ++i) {
                            Loan loan = new Loan();
                            loan.setLoanId(((Loan)loansPerStateList.get(i)).getLoanId());
                            loan.setInternalLoanId(((Loan)loansPerStateList.get(i)).getInternalLoanId());
                            loan.setLoanState(((Loan)loansPerStateList.get(i)).getLoanState());
                            loan.setLoanTimestamp(((Loan)loansPerStateList.get(i)).getLoanTimestamp());
                            loan.setLoanReason(((Loan)loansPerStateList.get(i)).getLoanReason());
                            loan.setLoanReasonDetails(((Loan)loansPerStateList.get(i)).getLoanReasonDetails());
                            loansPerStateList1 = new ArrayList();
                            loan.setLoanOffersList(webServiceVO.getLoanOffersList());
                            loansPerStateList1.add(loan);
                            webServiceVO.setLoanList(loansPerStateList1);
                        }
                    }

                    data = (ArrayList)responseVO.getCollectionOfList().get("Loans");
                    if (data != null) {
                        loansPerStateList = data;

                        for(i = 0; i < loansPerStateList.size(); ++i) {
                            Loans loans = new Loans();
                            loans.setLoanList(webServiceVO.getLoanList());
                            loansPerStateList1 = new ArrayList();
                            loans.setLoanList(webServiceVO.getLoanList());
                            loans.setReportList(webServiceVO.getReportList());
                            loans.setEventsList(webServiceVO.getEventsList());
//                            loans.setOutstandingStatusList(webServiceVO.getOutstandingStatusList());
//                            loans.setPlanList(webServiceVO.getPlanList());
                            loansPerStateList1.add(loans);
                            webServiceVO.setLoansList(loansPerStateList1);
                        }
                    }

                    data = (ArrayList)responseVO.getCollectionOfList().get("LoansPerState");
                    if (data != null) {
                        loansPerStateList = data;

                        for(i = 0; i < loansPerStateList.size(); ++i) {
                            LoansPerState loansPerState1 = new LoansPerState();
                            loansPerState1.setLoanState(((LoansPerState)loansPerStateList.get(i)).getLoanState());
                            loansPerStateList1 = new ArrayList();
                            loansPerState1.setLoansList(webServiceVO.getLoansList());
                            loansPerStateList1.add(loansPerState1);
                            webServiceVO.setLoansPerStates(loansPerStateList1);
                        }
                    }

                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                }
            }
            else {
                logger.info("[FonePaySwitchController.outstandingLoanStatus] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.outstandingLoanStatus] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.outstandingLoanStatus] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(((WorkFlowException) e).getErrorCode());
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.outstandingLoanStatus] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO getOutstandingLoan(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO loanPlan(WebServiceVO webServiceVO) {
        return null;
    }

    @Override
    public WebServiceVO loanHistory(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.loanHistory] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "loanHistory");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }
                List<TransactionDetailMasterModel> toBeProcessedList = null;
                List<History> historyList = new ArrayList<>();

                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                Calendar startCalendar = Calendar.getInstance();

                Date dateStr = format.parse(webServiceVO.getFromDate()) ;
                Date endStr = format.parse(webServiceVO.getToDate());

//                dateStr = startCalendar.setTime(webServiceVO.getFromDate());

                toBeProcessedList = transactionDetailMasterDAO.loadTDMbyMobileandDateRange
                        (webServiceVO.getMobileNo(), dateStr, endStr, String.valueOf(ProductConstantsInterface.LOAN_XTRA_CASH));

                if(toBeProcessedList != null && !toBeProcessedList.isEmpty()){
                    for (TransactionDetailMasterModel l1 : toBeProcessedList) {
                        History h1 = new History();
                        h1.setTitle(l1.getProductName().toString());
                        if(l1.getSaleMobileNo() != null){
                            h1.setStatus("Repaid");
                        }
                        else{
                            h1.setStatus("Avail");
                        }
                        h1.setAmount(l1.getTotalAmount().toString());
                        h1.setDateTime(String.valueOf(l1.getCreatedOn()));
                        historyList.add(h1);
                    }

                    webServiceVO.setHistoryList(historyList);
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                }
                else{
                    logger.info("[FonePaySwitchController.transactionStatus] Transaction Not Found against the RRN # :: " + webServiceVO.getRetrievalReferenceNumber());
                    webServiceVO.setResponseCode(FonePayResponseCodes.TRANSACTION_CODE_NOT_AVALIBLE);
                    webServiceVO.setResponseCodeDescription("Transaction Not Found.");
                }
            }
            else {
                logger.info("[FonePaySwitchController.loanHistory] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.loanHistory] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.loanHistory] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.loanHistory] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO transactionActive(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.transactionActive] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "transactionActive");

            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
//            appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByCnic256(webServiceVO.getShaCnic());
            if(appUserModel != null) {
//                webServiceVO.setCnicNo(appUserModel.getNic());
                if (!getCommonCommandManager().checkActiveAppUserForOpenAPI(webServiceVO, appUserModel)) {
                    return webServiceVO;
                }
                TransactionDetailMasterModel tdm = getTransactionReversalManager().loadTDMbyMobileNumber
                        (webServiceVO.getMobileNo(), String.valueOf(ProductConstantsInterface.LOAN_XTRA_CASH));
                if(tdm != null){
                    webServiceVO.setStatus("Active");
                    webServiceVO.setStatusFlag(true);
                }
                else{
                    webServiceVO.setStatus("In-Active");
                    webServiceVO.setStatusFlag(false);
                }

                webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
            }
            else {
                logger.info("[FonePaySwitchController.transactionActive] User Not Found against the Mobile # :: " + webServiceVO.getMobileNo());
                webServiceVO.setResponseCode(FonePayResponseCodes.CUSTOMER_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("User Not Found.");
                return webServiceVO;
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.transactionActive] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.transactionActive] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.transactionActive] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO loanCallBack(WebServiceVO webServiceVO) {
        logger.info("[FonePaySwitchController.loanCallBack] Start:: ");
        FonePayLogModel fonePayLogModel = null;
        ActionLogModel actionLogModel = null;
        AppUserModel appUserModel = new AppUserModel();
        String xml = "";

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        try {
            webServiceVO = this.validateRRN(webServiceVO);
            if (!webServiceVO.getResponseCode().equals(FonePayResponseCodes.SUCCESS_RESPONSE_CODE))
                return webServiceVO;
            fonePayLogModel = getFonePayManager().saveFonePayIntegrationLogModel(webServiceVO, "loanCallBack");

            logger.info("[FonePaySwitchController.loanCallBack] Checking tdm against internalLoanId: " + webServiceVO.getInternalLoanId());
            TransactionDetailMasterModel tdm = getTransactionReversalManager().loadTDMbyReserved2(webServiceVO.getInternalLoanId());
            if(tdm != null){
                if(tdm.getSupProcessingStatusId().equals(SupplierProcessingStatusConstants.COMPLETED)){
                    webServiceVO.setResponseCode(FonePayResponseCodes.SUCCESS_RESPONSE_CODE);
                    webServiceVO.setResponseCodeDescription(FonePayResponseCodes.SUCCESS_RESPONSE_DESCRIPTION);
                }
                else{
                    webServiceVO.setResponseCode("166");
                    webServiceVO.setResponseCodeDescription("Transaction Status Failed");
                }
            }
            else{
                logger.info("[FonePaySwitchController.loanCallBack] data not found against internalLoanId: " + webServiceVO.getInternalLoanId());
                webServiceVO.setResponseCode(FonePayResponseCodes.TRANSACTION_NOT_FOUND);
                webServiceVO.setResponseCodeDescription("Transaction Not Found");
            }

        } catch (Exception e) {
            logger.error("[FonePaySwitchController.loanCallBack] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.loanCallBack] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        } finally {
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();
            getFonePayManager().updateFonePayIntegrationLogModel(fonePayLogModel, webServiceVO);
        }
        logger.info("[FonePaySwitchController.loanCallBack] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

    @Override
    public WebServiceVO simpleAccountOpening(WebServiceVO webServiceVO) {
        try {
            AppUserModel appUserModel = getCommonCommandManager().loadAppUserByCnicAndType(webServiceVO.getCnicNo());
            if(appUserModel != null){
                String cnic = appUserModel.getNic();
                appUserModel = null;
                appUserModel = getCommonCommandManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo());
                if(appUserModel != null){
                    logger.info("FonePaySwitchController.simpleAccountOpening(): Account already exit with mobile no: "+appUserModel.getMobileNo()+" and CNIC: "+appUserModel.getNic());
                    return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_MOBILE_ALREADY_EXIST_AS_CUSTOMER);
                }
                logger.info("FonePaySwitchController.simpleAccountOpening(): Account already exit with CNIC: "+cnic);
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_ALREADY_EXIST_AS_CUSTOMER);
            }
            if (this.getCommonCommandManager().isCnicBlacklisted(webServiceVO.getCnicNo()))
                return FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.CUSTOMER_CNIC_BLACKLISTED);
            webServiceVO = getFonePayManager().simpleAccountOpening(webServiceVO);
            return webServiceVO;
        } catch (Exception e) {
            logger.error("[FonePaySwitchController.simpleAccountOpening] Error occured: " + e.getMessage(), e);

            this.logger.error("[FonePaySwitchController.simpleAccountOpening] Error occured: " + e.getMessage(), e);
            webServiceVO.setResponseCode(FonePayResponseCodes.GENERAL_ERROR);
            webServiceVO.setResponseCodeDescription(e.getMessage());
            if (e instanceof NullPointerException
                    || e instanceof HibernateException
                    || e instanceof SQLException
                    || e instanceof DataAccessException
                    || (e.getMessage() != null && e.getMessage().indexOf("Exception") != -1)) {

                logger.error("Converting Exception (" + e.getClass() + ") to generic error message...");
                webServiceVO = FonePayUtils.prepareErrorResponse(webServiceVO, FonePayResponseCodes.GENERAL_ERROR.toString());
            }

        }
        logger.info("[FonePaySwitchController.simpleAccountOpening] (In End) Response Code: " + webServiceVO.getResponseCode());
        return webServiceVO;
    }

//    @Override
//    public WebServiceVO loanStatus(WebServiceVO webServiceVO) {
//        return null;
//    }

//    @Override
//    public WebServiceVO loanCallBack(WebServiceVO webServiceVO) {
//        return null;
//    }

//    @Override
//    public WebServiceVO offerListForCommodity(WebServiceVO webServiceVO) {
//        return null;
//    }

//    @Override
//    public WebServiceVO customerAnalytics(WebServiceVO webServiceVO) {
//        return null;
//    }


    public WebServiceVO minorUpdate(WebServiceVO webServiceVO) throws Exception {
        ArrayList<CustomerPictureModel> arrayCustomerPictures = new ArrayList<CustomerPictureModel>();
        Date nowDate = new Date();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel = getCommonCommandManager().getAppUserManager().loadAppUserByMobileAndType(webServiceVO.getMobileNo(), UserTypeConstantsInterface.CUSTOMER);
        if (StringUtils.isNotEmpty(webServiceVO.getMinorCustomerPic()) && webServiceVO.getMinorCustomerPic() != null) {
            addMinorCustomerPicture(PictureTypeConstants.CUSTOMER_PHOTO, arrayCustomerPictures, nowDate, webServiceVO.getMinorCustomerPic(), appUserModel.getCustomerId());
        }
        if (StringUtils.isNotEmpty(webServiceVO.getbFormPic()) && webServiceVO.getbFormPic() != null) {
            addMinorCustomerPicture(PictureTypeConstants.B_FORM_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getbFormPic(), appUserModel.getCustomerId());
        }
        if (StringUtils.isNotEmpty(webServiceVO.getParentCnicPic()) && webServiceVO.getParentCnicPic() != null) {
            addMinorCustomerPicture(PictureTypeConstants.PARENT_CNIC_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getParentCnicPic(), appUserModel.getCustomerId());
        }
        if (StringUtils.isNotEmpty(webServiceVO.getSnicPic()) && webServiceVO.getSnicPic() != null) {
            addMinorCustomerPicture(PictureTypeConstants.ID_FRONT_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getSnicPic(), appUserModel.getCustomerId());
        }
        if (StringUtils.isNotEmpty(webServiceVO.getsNicBackPic()) && webServiceVO.getsNicBackPic() != null) {
            addMinorCustomerPicture(PictureTypeConstants.ID_BACK_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getsNicBackPic(), appUserModel.getCustomerId());
        }
        if (StringUtils.isNotEmpty(webServiceVO.getParentNicBackPic()) && webServiceVO.getParentNicBackPic() != null) {
            addMinorCustomerPicture(PictureTypeConstants.PARENT_CNIC_BACK_SNAPSHOT, arrayCustomerPictures, nowDate, webServiceVO.getParentNicBackPic(), appUserModel.getCustomerId());
        }

        appUserModel.setRegistrationStateId(RegistrationStateConstants.DISCREPANT_REQUEST_RECEIVED);
        for (CustomerPictureModel customerPictureModel : arrayCustomerPictures) {
            customerPictureModel.setCustomerId(appUserModel.getCustomerId());
        }


        this.getCommonCommandManager().saveOrUpdateCustomerPictureModel(arrayCustomerPictures);
        appUserDAO.saveOrUpdate(appUserModel);
        webServiceVO.setResponseCode("00");
        webServiceVO.setResponseCodeDescription("Successfull");
        webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        return webServiceVO;
    }

    public WebServiceVO minorFatherBVSUpdate(WebServiceVO webServiceVO, long customerId) throws Exception {
        CustomerModel customerModel = new CustomerModel();
        customerModel = getCommonCommandManager().getCustomerModelById(customerId);

        if (customerModel.getFatherMotherMobileNo().equals(webServiceVO.getFatherMotherMobileNumber()) &&
                customerModel.getFatherCnicNo().equals(webServiceVO.getFatherCnic())) {
            customerModel.setBvs(true);
            customerModel.setUpdatedOn(new Date());
            customerDAO.saveOrUpdate(customerModel);
            webServiceVO.setResponseCode("00");
            webServiceVO.setResponseCodeDescription("BVS successful");
            webServiceVO.setRetrievalReferenceNumber(webServiceVO.getRetrievalReferenceNumber());
        } else {
            webServiceVO.setResponseCode("170");
            webServiceVO.setResponseCodeDescription("Father Mobile Number/Cnic is not valid");
        }
        return webServiceVO;
    }

    private void addMinorCustomerPicture(Long pictureTypeId, ArrayList<CustomerPictureModel> arrayCustomerPictures, Date nowDate, String pictureAsString, Long customerId) throws IOException, CommandException {
        CustomerPictureModel customerPictureModel = new CustomerPictureModel();
        try {
            customerPictureModel = customerPictureDAO.getCustomerPictureByTypeId(pictureTypeId, customerId);

//            if (customerPictureModel != null) {
//                pictureAsString = pictureAsString.replace(" ", "+");
//                byte[] imageFileBytes = org.apache.commons.codec.binary.Base64.decodeBase64(pictureAsString.getBytes());
//
//                customerPictureModel.setPicture(imageFileBytes);
//                customerPictureModel.setPictureTypeId(pictureTypeId);
//                customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
//                customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
//                customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
//                customerPictureModel.setCreatedOn(new Date());
//                customerPictureModel.setDiscrepant(false);
//                customerPictureModel.setUpdatedOn(new Date());
//                customerPictureDAO.saveOrUpdate(customerPictureModel);
//            } else {
            customerPictureModel = new CustomerPictureModel();
            pictureAsString = pictureAsString.replace(" ", "+");
            byte[] imageFileBytes = org.apache.commons.codec.binary.Base64.decodeBase64(pictureAsString.getBytes());

            customerPictureModel.setPicture(imageFileBytes);
            customerPictureModel.setPictureTypeId(pictureTypeId);
            customerPictureModel.setApplicantTypeId(ApplicantTypeConstants.APPLICANT_TYPE_1);
            customerPictureModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            customerPictureModel.setCreatedOn(new Date());
            customerPictureModel.setUpdatedOn(new Date());
            customerPictureModel.setDiscrepant(false);
            customerPictureModel = (CustomerPictureModel) customerPictureDAO.saveOrUpdate(customerPictureModel);


//            }
        } catch (Exception e) {
            logger.error("Unable to decode image bytes...", e);
            throw new CommandException(MessageUtil.getMessage("newMfsAccount.unknown"), ErrorCodes.FILE_NOT_FOUND, ErrorLevel.MEDIUM);
        }

        if (arrayCustomerPictures != null) {
            arrayCustomerPictures.add(customerPictureModel);
        }
    }

    public RetailerModel loadRetailerModel(Long retailerId) throws CommandException {

        RetailerModel retailerModel = new RetailerModel();
        retailerModel.setRetailerId(retailerId);
        BaseWrapper baseWrapper = new BaseWrapperImpl();

        baseWrapper.setBasePersistableModel(retailerModel);

        try {

            baseWrapper = getCommonCommandManager().loadRetailer(baseWrapper);

        } catch (FrameworkCheckedException e) {

            throw new CommandException(e.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, e);
        }

        return (baseWrapper != null ? ((RetailerModel) baseWrapper.getBasePersistableModel()) : null);
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }

    public void setMfsAccountClosureFacade(MfsAccountClosureFacade mfsAccountClosureFacade) {
        this.mfsAccountClosureFacade = mfsAccountClosureFacade;
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setAccountHolderManager(AccountHolderManager accountHolderManager) {
        this.accountHolderManager = accountHolderManager;
    }

    public void setLimitManager(LimitManager limitManager) {
        this.limitManager = limitManager;
    }

    public void setCatalogDAO(ProductCatalogDAO catalogDAO) {
        this.catalogDAO = catalogDAO;
    }

    public void setUpdateCustomerNameFacade(UpdateCustomerNameFacade updateCustomerNameFacade) {
        this.updateCustomerNameFacade = updateCustomerNameFacade;
    }

    public void setClsPendingBlinkCustomerDAO(ClsPendingBlinkCustomerDAO clsPendingBlinkCustomerDAO) {
        this.clsPendingBlinkCustomerDAO = clsPendingBlinkCustomerDAO;
    }

    public void setBlinkCustomerModelDAO(BlinkCustomerModelDAO blinkCustomerModelDAO) {
        this.blinkCustomerModelDAO = blinkCustomerModelDAO;
    }

    public void setUpdateCustomerNameDAO(UpdateCustomerNameDAO updateCustomerNameDAO) {
        this.updateCustomerNameDAO = updateCustomerNameDAO;
    }

    public void setAppUserDAO(AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setClsPendingAccountOpeningDAO(ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO) {
        this.clsPendingAccountOpeningDAO = clsPendingAccountOpeningDAO;
    }

    public void setEcofinSubAgentDAO(EcofinSubAgentDAO ecofinSubAgentDAO) {
        this.ecofinSubAgentDAO = ecofinSubAgentDAO;
    }

    public void setCustomerPictureDAO(CustomerPictureDAO customerPictureDAO) {
        this.customerPictureDAO = customerPictureDAO;
    }

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }

    private void loadAndForwardCoreAdviceToQueue(final Object workFlowWrapper) throws InterruptedException {
        reversalAdviceQueingPreProcessor.startProcessing(workFlowWrapper);
    }

    public void setReversalAdviceQueingPreProcessor(ReversalAdviceQueingPreProcessor reversalAdviceQueingPreProcessor) {
        this.reversalAdviceQueingPreProcessor = reversalAdviceQueingPreProcessor;
    }

    public void setTransactionDetailMasterDAO(TransactionDetailMasterDAO transactionDetailMasterDAO) {
        this.transactionDetailMasterDAO = transactionDetailMasterDAO;
    }
    public void setTransactionDetailMasterManager(TransactionDetailMasterManager transactionDetailMasterManager) {
        this.transactionDetailMasterManager = transactionDetailMasterManager;
    }
}
