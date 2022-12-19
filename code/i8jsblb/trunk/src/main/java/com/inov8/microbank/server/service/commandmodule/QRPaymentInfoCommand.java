package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Omar Butt on 12/20/2017
 */
public class QRPaymentInfoCommand extends BaseCommand {

    private static final Log logger = LogFactory.getLog(QRPaymentInfoCommand.class);

    private String merchantId;
    private String trxAmount = null;
    private AppUserModel appUserModel = null;
    private String qrString;
    private I8SBSwitchControllerResponseVO responseVO = null;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        merchantId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MERCHANT_ID);
        trxAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
        qrString = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_QR_STRING);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(merchantId, validationErrors, "Merchant Id");
        validationErrors = ValidatorWrapper.doRequired(trxAmount, validationErrors, "Transaction Amount");

        if(!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(trxAmount, validationErrors, "Transaction Amount");
        }

        appUserModel = ThreadLocalAppUser.getAppUserModel();
        if(appUserModel == null){
            logger.error("QRPaymentInfoCommand.prepare() -> ThreadLocalAppUser is null... Throwing general exception...");
            throw new CommandException(CommandConstants.GENERAL_ERROR_MSG, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH);
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        try {

            logger.info("QRPaymentInfoCommand called. loggedInAppUserId:"+appUserModel.getAppUserId() +
                    ", Cust Mob:" + appUserModel.getMobileNo() + ", merchantId:" + merchantId +
                    ", qrString:" + qrString +  ", trxAmount:"+trxAmount);

            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
            requestVO.setUserId(appUserModel.getUsername());
            requestVO.setCNIC(appUserModel.getNic());
            requestVO.setMerchantId(merchantId);
            requestVO.setMerchantQRCode(qrString);
            requestVO.setTransactionAmount(trxAmount);
            requestVO.setBankId(BankConstantsInterface.I8SB_BANK_ID);
            requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
            requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
            requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_FONEPAY);
            requestVO.setRequestType(I8SBConstants.RequestType_MPASS_MerchantDetail);

            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

            switchWrapper = commonCommandManager.makeI8SBCall(switchWrapper);

            responseVO = switchWrapper.getI8SBSwitchControllerResponseVO();
            ESBAdapter.processQRResponseCode(responseVO, true);

            logger.info(String.format("Successfully validated merchant from FoneyPay for Merchant ID:(%s) Name: (%s)", merchantId, responseVO.getMerchantName()));

        }catch (FrameworkCheckedException ex) {
            logger.error("[QRPaymentInfoCommand.execute] Throwing Exception for Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(ex.getMessage(), (ex.getErrorCode() == 0) ? ErrorCodes.COMMAND_EXECUTION_ERROR : ex.getErrorCode(), ErrorLevel.MEDIUM, ex);
        }catch (WorkFlowException wex) {
            logger.error("[QRPaymentInfoCommand.execute] Throwing Exception Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
        }catch (Exception ex) {
            logger.error("[QRPaymentInfoCommand.execute] Throwing Exception for Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(CommandConstants.GENERAL_ERROR_MSG, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, ex);
        }
    }

    @Override
    public String response() {
       return toXML();
    }

    private String toXML() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_MERCHANT_ID, responseVO.getMerchantId()));
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_MERCHANT_NAME, replaceNullWithEmpty(responseVO.getMerchantName())));
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMT, trxAmount));
        strBuilder.append(MiniXMLUtil.createXMLParameterTag(CommandFieldConstants.KEY_TAMTF, Formatter.formatNumbers(Double.parseDouble(trxAmount))));

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH).append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);
        return strBuilder.toString();
    }

}