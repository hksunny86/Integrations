package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.EncoderUtils;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class ForgotPinInfoCommand extends BaseCommand {

    protected AppUserModel appUserModel;
    protected String mobileNo;
    protected String deviceTypeId;
    String veriflyErrorMessage;
    boolean errorMessagesFlag;
    UserDeviceAccountsModel userDeviceAccountsModel;
    protected String encryption_type;
    private Boolean isError=false;

    protected final Log logger = LogFactory.getLog(ForgotPinInfoCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        if(logger.isDebugEnabled())
        {
            logger.debug("Start of ForgotPinInfoCommand.prepare()");
        }
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        try {
            appUserModel=(AppUserModel) this.getCommonCommandManager().loadAppUserByMobileAndType(mobileNo, UserTypeConstantsInterface.CUSTOMER);
            if(appUserModel!=null){
                ThreadLocalAppUser.setAppUserModel(appUserModel);
            }
            else{
                isError=true;
            }


        }
        catch (Exception e){
            logger.error("[ForgotPinInfoCommand.prepare] Unable to Load AppUserModel by mobile: " + mobileNo ,e);

        }

        if(logger.isDebugEnabled())
        {
            logger.debug("End of ForgotPinInfoCommand.prepare()");
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
        if(isError)
            throw new CommandException("No Customer found against given Mobile Number",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();



        logger.info("\n\nForgotPinInfoCommand");
        logger.info(
                "\nApp User Id = "+appUserModel.getAppUserId()
                        +"\nAppUser Type Id "+ appUserModel.getAppUserTypeId()
                        +"\nAppUser Retailer Id "+appUserModel.getRetailerContactId()
                        +"\nAppUser Handler Id "+appUserModel.getHandlerId()
                        +"\nAppUser Customer Id "+appUserModel.getCustomerId()
        );

        try
        {
            //Is CNIC Blacklisted - Validation
            if(getCommonCommandManager().isCnicBlacklisted(appUserModel.getNic())){
                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountBlacklisted", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            //Account Closed - Validation
            if(appUserModel.getAccountClosedUnsettled() || appUserModel.getAccountClosedSettled()){
                throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountClosed", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            // Smart Money Account - Validation
            SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
            smartMoneyAccountModel.setCustomerId(appUserModel.getCustomerId());
            smartMoneyAccountModel.setPaymentModeId(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT);
            smartMoneyAccountModel.setActive(Boolean.TRUE);
            searchBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);

            searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
            smartMoneyAccountModel = (SmartMoneyAccountModel)searchBaseWrapper.getCustomList().getResultsetList().get(0);

            if(smartMoneyAccountModel == null){
                throw new CommandException(this.getMessageSource().getMessage("phoenix.trans.02", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }else if(!smartMoneyAccountModel.getActive()){
                throw new CommandException(this.getMessageSource().getMessage("6058", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }

            //UserDeviceAccount - Validation
            UserDeviceAccountsModel userDeviceAccountModel = new UserDeviceAccountsModel();
            userDeviceAccountModel.setAppUserId(appUserModel.getAppUserId());
            userDeviceAccountModel.setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

            searchBaseWrapper = new SearchBaseWrapperImpl();
            searchBaseWrapper.setBasePersistableModel(userDeviceAccountModel);
            //searchBaseWrapper = checkLogin(searchBaseWrapper);
            CustomList<UserDeviceAccountsModel> customList = this.getCommonCommandManager().loadUserDeviceAccounts(searchBaseWrapper).getCustomList();//.userDeviceAccountsDAO.findByExample(userDeviceAccountModel,null,null, PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
            List<UserDeviceAccountsModel> list = customList.getResultsetList();
            if(list != null && list.size() > 0){
                userDeviceAccountModel = list.get(0);
                if(userDeviceAccountModel.getUserId() != null){
                    boolean messageSeparatorFlag = false;
                    if(userDeviceAccountModel.getAccountExpired()){
                        throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountExpired", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                    }else if(userDeviceAccountModel.getAccountLocked()){
                        throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountLocked", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                    }else if(userDeviceAccountModel.getCredentialsExpired()){
                        throw new CommandException(this.getMessageSource().getMessage("LoginCommand.credentialsExpired", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                    }else if(!userDeviceAccountModel.getAccountEnabled()){
                        throw new CommandException(this.getMessageSource().getMessage("LoginCommand.accountDisabled", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                    }
                }else{
                    throw new CommandException(this.getMessageSource().getMessage("phoenix.trans.02", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
                }
            }else{
                throw new CommandException(this.getMessageSource().getMessage("phoenix.trans.02", null,null),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }
            ///Generate OTP and store in MiniTransaction
            String otp = CommonUtils.generateOneTimePin(5);
            logger.info("The plain otp is "+otp);
            String encryptedPin = EncoderUtils.encodeToSha(otp);
            try {
                this.getCommonCommandManager().getFonePayManager().createMiniTransactionModel(encryptedPin, mobileNo,"",CommandFieldConstants.CMD_OTP_VERIFICATION);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Send OTP via SMS to Customer
            String smsText = MessageUtil.getMessage("otpSms", new String[]{"Reset Login Pin", otp});
            SmsMessage smsMessage = new SmsMessage(mobileNo, smsText);
            try {
                this.getCommonCommandManager().getSmsSender().send(smsMessage);
            } catch (FrameworkCheckedException e) {

                e.printStackTrace();
                throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,ex);
        }



    }

    @Override
    public String response() {
        return toXML();
    }

    private String toXML()
    {
        if(logger.isDebugEnabled())
        {
            logger.debug("Start of ForgotPinInfoCommand.toXML()");
        }
        StringBuilder strBuilder = new StringBuilder();


            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAMS)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_NAME)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append("DTID")
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE);
                strBuilder.append("5");
            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAM)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_PARAMS)
                    .append(TAG_SYMBOL_CLOSE);


        if(logger.isDebugEnabled())
        {
            logger.debug("End of ForgotPinInfoCommand.toXML()");
        }
        return strBuilder.toString();
    }

    private SmartMoneyAccountModel getSmartMoneyAccountModel(SmartMoneyAccountModel _smartMoneyAccountModel) throws FrameworkCheckedException {

        SmartMoneyAccountModel smartMoneyAccountModel = null;
        SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
        searchBaseWrapper.setBasePersistableModel(_smartMoneyAccountModel);
        searchBaseWrapper = this.getCommonCommandManager().loadSmartMoneyAccount(searchBaseWrapper);
        List<SmartMoneyAccountModel> smartMoneyAccounts = searchBaseWrapper.getCustomList().getResultsetList() ;

        if( smartMoneyAccounts.size() == 1) {
            smartMoneyAccountModel = (SmartMoneyAccountModel)smartMoneyAccounts.get(0) ;
        }

        return smartMoneyAccountModel;
    }

}
