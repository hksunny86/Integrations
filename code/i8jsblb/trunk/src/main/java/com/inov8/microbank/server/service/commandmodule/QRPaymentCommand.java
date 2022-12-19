package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;

import static com.inov8.microbank.common.util.XMLConstants.*;

/**
 * Created by Omar Butt on 12/20/2017
 */
public class QRPaymentCommand extends BaseCommand {

    private static final Log logger = LogFactory.getLog(QRPaymentCommand.class);

    private String merchantId = null;
    private String merchantName = null;
    private String trxAmount = null;
    private AppUserModel appUserModel = null;
    private I8SBSwitchControllerResponseVO responseVO = null;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        merchantId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MERCHANT_ID);
        merchantName = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MERCHANT_NAME);
        trxAmount = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_TAMT);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(deviceTypeId, validationErrors, "Device Type");
        validationErrors = ValidatorWrapper.doRequired(merchantId, validationErrors, "Merchant Id");
        validationErrors = ValidatorWrapper.doRequired(merchantName, validationErrors, "Merchant Name");
        validationErrors = ValidatorWrapper.doRequired(trxAmount, validationErrors, "Transaction Amount");

        if(!validationErrors.hasValidationErrors()) {
            validationErrors = ValidatorWrapper.doNumeric(trxAmount, validationErrors, "Transaction Amount");
        }

        appUserModel = ThreadLocalAppUser.getAppUserModel();
        if(appUserModel == null){
            logger.error("QRPaymentCommand.prepare() -> ThreadLocalAppUser is null... Throwing general exception...");
            throw new CommandException(CommandConstants.GENERAL_ERROR_MSG, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH);
        }

        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        try {

            logger.info("QRPaymentCommand called. loggedInAppUserId:"+appUserModel.getAppUserId() +
                    ", Cust Mob:" + appUserModel.getMobileNo() + ", merchantId:" + merchantId +
                    ", merchantName:" + merchantName + ", trxAmount:"+trxAmount);

            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
            requestVO.setUserId(appUserModel.getUsername());
            requestVO.setMerchantId(merchantId);
            requestVO.setMerchantName(merchantName);
            requestVO.setFirstName(appUserModel.getFirstName());
            requestVO.setLastName(appUserModel.getLastName());
            requestVO.setCNIC(appUserModel.getNic());
            requestVO.setMobilePhone(appUserModel.getMobileNo());
            requestVO.setTransactionAmount(trxAmount);

            requestVO.setBankId(BankConstantsInterface.I8SB_BANK_ID);
            requestVO.setI8sbClientID(I8SBConstants.I8SB_Client_ID_JSBL);
            requestVO.setI8sbClientTerminalID(I8SBConstants.I8SB_Client_Terminal_ID_BLB);
            requestVO.setI8sbChannelID(I8SBConstants.I8SB_Channel_ID_FONEPAY);
            requestVO.setRequestType(I8SBConstants.RequestType_MPASS_Payment);

            requestVO.setEmail( loadCustomerEmail() );

            SwitchWrapper switchWrapper = new SwitchWrapperImpl();
            switchWrapper.setI8SBSwitchControllerRequestVO(requestVO);

            switchWrapper = commonCommandManager.makeI8SBCall(switchWrapper);

            responseVO = switchWrapper.getI8SBSwitchControllerResponseVO();
            ESBAdapter.processQRResponseCode(responseVO, false);

            logger.info(String.format("QR Transaction Successful... Customer mob: " + appUserModel.getMobileNo() + " against merchant ID:", merchantId));

        }catch (FrameworkCheckedException ex) {
            logger.error("[QRPaymentCommand.execute] Throwing Exception for Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(ex.getMessage(), (ex.getErrorCode() == 0) ? ErrorCodes.COMMAND_EXECUTION_ERROR : ex.getErrorCode(), ErrorLevel.MEDIUM, ex);
        }catch (WorkFlowException wex) {
            logger.error("[QRPaymentCommand.execute] Throwing Exception Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(wex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, wex);
        }catch (Exception ex) {
            logger.error("[QRPaymentCommand.execute] Throwing Exception for Logged In AppUserID:" + ThreadLocalAppUser.getAppUserModel().getAppUserId());
            throw new CommandException(CommandConstants.GENERAL_ERROR_MSG, ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.HIGH, ex);
        }
    }

    private String loadCustomerEmail(){
        String email = "";
        try {
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            CustomerModel customerModel = new CustomerModel();
            customerModel.setCustomerId(appUserModel.getCustomerId());
            baseWrapper.setBasePersistableModel(customerModel);
            this.commonCommandManager.loadCustomer(baseWrapper);
            customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
            email = customerModel.getEmail();
        }catch(Exception e){
            logger.error("Error while QRPaymentCommand.loadCustomerEmail()...", e);
        }
        return email;
    }

    @Override
    public String response() {
        return toXML();
    }

    private String toXML() {
        Date trxDate = new Date();

        ArrayList<LabelValueBean> params = new ArrayList<LabelValueBean>();

        params.add(new LabelValueBean(ATTR_TRXID,replaceNullWithEmpty(responseVO.getTranCode())));
        params.add(new LabelValueBean(ATTR_DATE, replaceNullWithEmpty(trxDate+"")));
        params.add(new LabelValueBean(ATTR_DATEF, PortalDateUtils.formatDate(trxDate,PortalDateUtils.LONG_DATE_FORMAT)));
        params.add(new LabelValueBean(ATTR_TIMEF, Formatter.formatTime(trxDate)));
        params.add(new LabelValueBean(ATTR_PROD, replaceNullWithEmpty("MPASS Merchant Transaction")));
        params.add(new LabelValueBean(ATTR_MNAME, replaceNullWithEmpty(merchantName)));
        params.add(new LabelValueBean(ATTR_TXAM, replaceNullWithEmpty(trxAmount)));
        params.add(new LabelValueBean(ATTR_TXAMF, Formatter.formatNumbers(Double.parseDouble(trxAmount))));

        return MiniXMLUtil.createResponseXMLByParams(params);
    }

}