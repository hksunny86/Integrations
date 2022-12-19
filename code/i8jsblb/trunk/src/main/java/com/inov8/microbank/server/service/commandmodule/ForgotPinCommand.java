package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.financialintegrationmodule.AbstractFinancialInstitution;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.verifly.common.constants.VeriflyConstants;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapper;
import com.inov8.verifly.common.wrapper.VeriflyBaseWrapperImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class ForgotPinCommand extends BaseCommand {


    protected AppUserModel appUserModel;
    protected String mobileNo;
    protected String deviceTypeId;
    String veriflyErrorMessage;
    boolean errorMessagesFlag;
    UserDeviceAccountsModel userDeviceAccountsModel;
    protected String encryption_type;
    protected String newPin;
    protected String confirmPin;
    //protected String otp;

    protected final Log logger = LogFactory.getLog(ForgotPinCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {

        if(logger.isDebugEnabled())
        {
            logger.debug("Start of ForgotPinCommand.prepare()");
        }
        //appUserModel = ThreadLocalAppUser.getAppUserModel();
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        encryption_type = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        try {
            appUserModel=(AppUserModel) this.getCommonCommandManager().loadAppUserByMobileAndType(mobileNo, UserTypeConstantsInterface.CUSTOMER);
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        }
        catch (Exception e){
            logger.error("[ForgotPinCommand.prepare] Unable to Load AppUserModel by mobile: " + mobileNo ,e);
        }

        //otp = this.getCommandParameter(baseWrapper,"OTP");
        newPin = this.getCommandParameter(baseWrapper, "NMPIN");
//        newPin = EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY,newPin);
        newPin = StringUtil.replaceSpacesWithPlus(newPin);
        confirmPin = this.getCommandParameter(baseWrapper, "CMPIN");
//        confirmPin = EncryptionUtil.decryptWithAES(XMLConstants.AES_ENCRYPTION_KEY,confirmPin);
        confirmPin = StringUtil.replaceSpacesWithPlus(confirmPin);
        if(logger.isDebugEnabled())
        {
            logger.debug("End of ForgotPinCommand.prepare()");
        }

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doRequired(mobileNo, validationErrors, "Mobile No");
        //validationErrors = ValidatorWrapper.doRequired(otp, validationErrors, "OTP");
        validationErrors = ValidatorWrapper.doRequired(newPin, validationErrors, "Pin");
        validationErrors = ValidatorWrapper.doRequired(confirmPin, validationErrors, "Confirm Pin");
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {

        CommonCommandManager commonCommandManager = this.getCommonCommandManager();
        VeriflyBaseWrapper veriflyBaseWrapper = new VeriflyBaseWrapperImpl();
        SmartMoneyAccountModel smartMoneyAccountModel =  new SmartMoneyAccountModel();


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

            BaseWrapper baseWrapper = new BaseWrapperImpl();
            /*baseWrapper.putObject(CommandFieldConstants.KEY_PIN, otp);
            baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, mobileNo);
            baseWrapper.putObject("ACTION", "0");
            baseWrapper.putObject(CommandFieldConstants.KEY_CHANNEL_ID,"");
            baseWrapper.putObject(CommandFieldConstants.KEY_CURR_COMMAND_ID,CommandFieldConstants.CMD_OTP_VERIFICATION);
            getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_OTP_VERIFICATION);*/

            BaseWrapper bWrapper = new BaseWrapperImpl();
            bWrapper.setBasePersistableModel(smartMoneyAccountModel);

            AbstractFinancialInstitution abstractFinancialInstitution = this.commonCommandManager.loadFinancialInstitution(bWrapper);
            AccountInfoModel accountInfo = abstractFinancialInstitution.getAccountInfoModelBySmartMoneyAccount(smartMoneyAccountModel, appUserModel.getCustomerId(), null);
            veriflyBaseWrapper.setAccountInfoModel(accountInfo);
            LogModel logmodel = new LogModel();
            if(UserUtils.getCurrentUser() != null){
                logmodel.setCreatdByUserId(UserUtils.getCurrentUser().getAppUserId());
                logmodel.setCreatedBy(UserUtils.getCurrentUser().getFirstName());
            }
            else{
                logmodel.setCreatdByUserId(ThreadLocalAppUser.getAppUserModel().getAppUserId());
                logmodel.setCreatedBy(ThreadLocalAppUser.getAppUserModel().getFirstName());
            }
            veriflyBaseWrapper.setLogModel(logmodel);
            veriflyBaseWrapper.setBasePersistableModel(smartMoneyAccountModel);
            veriflyBaseWrapper.putObject(VeriflyConstants.IVR_USER_PIN, newPin);
            veriflyBaseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.WEB);


            baseWrapper.putObject("appUserTypeId", appUserModel.getAppUserTypeId());
            // putting log information into wrapper for further used

            baseWrapper.putObject("appUserId", Long.valueOf(appUserModel.getAppUserId()));
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE );
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.KEY_UPDATE_CUST_LOGIN_PIN_USECASE_ID);
            baseWrapper.putObject(CommandFieldConstants.KEY_PIN,newPin);
            userDeviceAccountsModel= this.getCommonCommandManager().getUserDeviceAccountListViewManager().findUserDeviceByAppUserId(Long.valueOf(appUserModel.getAppUserId()));
            if(null==userDeviceAccountsModel){
                throw new CommandException("user device Account not found",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
            }
            baseWrapper.setBasePersistableModel(userDeviceAccountsModel);
            this.getCommonCommandManager().getUserDeviceAccountListViewManager().changeUserDeviceAccountForgotPin(baseWrapper);



            /*try{
                veriflyBaseWrapper = abstractFinancialInstitution.resetPin(veriflyBaseWrapper);
            }catch(Exception e){
                e.printStackTrace();
                throw new CommandException(e.getMessage(),ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,e);
            }*/

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
            logger.debug("Start of ForgotPinCommand.toXML()");
        }
        StringBuilder strBuilder = new StringBuilder();


            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_MESGS)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_MESG)
                    .append(TAG_SYMBOL_SPACE)
                    .append(ATTR_LEVEL)
                    .append(TAG_SYMBOL_EQUAL)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(ATTR_LEVEL_ONE)
                    .append(TAG_SYMBOL_QUOTE)
                    .append(TAG_SYMBOL_CLOSE);
                strBuilder.append("Password has been changed successfully");
            strBuilder.append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_MESG)
                    .append(TAG_SYMBOL_CLOSE)
                    .append(TAG_SYMBOL_OPEN)
                    .append(TAG_SYMBOL_SLASH)
                    .append(TAG_MESGS)
                    .append(TAG_SYMBOL_CLOSE);


        if(logger.isDebugEnabled())
        {
            logger.debug("End of ForgotPinCommand.toXML()");
        }
        return strBuilder.toString();
    }

}
