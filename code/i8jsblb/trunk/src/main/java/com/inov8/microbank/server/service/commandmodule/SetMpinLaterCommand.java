package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class SetMpinLaterCommand extends BaseCommand{

    protected String mobileNo, cnic;
    protected String isSetMpinLater;
    protected AppUserModel appUserModel;
    private Boolean isError=false;

    protected final Log logger = LogFactory.getLog(SetMpinLaterCommand.class);

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        logger.info ("Start of SetMpinLaterCommand.prepare()");
        appUserModel = ThreadLocalAppUser.getAppUserModel();
        deviceTypeId = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CUSTOMER_MOBILE);
        cnic = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_CNIC);

        isSetMpinLater = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_IS_SET_MPIN_LATER);

        try {
            appUserModel = this.getCommonCommandManager().loadAppUserByCnicAndMobileAndAppUserType(mobileNo, cnic, UserTypeConstantsInterface.CUSTOMER);
            if(appUserModel!=null){
                ThreadLocalAppUser.setAppUserModel(appUserModel);
            }
            else{
                isError=true;
            }
        }
        catch (Exception e){
            logger.error("[SetMpinLaterCommand.prepare] Unable to Load AppUserModel by mobile: " + mobileNo ,e);

        }
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        validationErrors = ValidatorWrapper.doNumeric(mobileNo, validationErrors, "Mobile No");
        if(isError)
            throw new CommandException("No Customer found against given Cnic",ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM,new Throwable());
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        logger.info ("Start of SetMpinLaterCommand.execute()");
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(new CustomerModel(appUserModel.getCustomerId()));
        try {
            this.getCommonCommandManager().loadCustomer(baseWrapper);
            CustomerModel customerModel = (CustomerModel) baseWrapper.getBasePersistableModel();
            customerModel.setIsSetMpinLater(Long.valueOf(isSetMpinLater));

            this.getCommonCommandManager().saveOrUpdateCustomerModel(customerModel);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_DEVICE_TYPE_ID)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(this.deviceTypeId).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return strBuilder.toString();

    }
}
