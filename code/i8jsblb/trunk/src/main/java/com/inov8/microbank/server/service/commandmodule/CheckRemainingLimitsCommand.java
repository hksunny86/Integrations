package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.util.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckRemainingLimitsCommand extends BaseCommand
{

    private Long limitType;

    Map<String, Object> responseMap = new HashMap<>();


    @Override
    public void prepare(BaseWrapper baseWrapper)
    {

        String _limitType = getCommandParameter(baseWrapper, "LIMIT_TYPE");
        this.limitType = StringUtil.isNullOrEmpty(_limitType) ? null : Long.parseLong(_limitType);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException
    {
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException
    {


        try {
            AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();
            Long customerAccountTypeId = null;
            Long handlerId = null;

            if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER) {
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                CustomerModel customerModel = new CustomerModel();
                customerModel.setCustomerId(appUserModel.getCustomerId());
                baseWrapper.setBasePersistableModel(customerModel);
                baseWrapper = this.getCommonCommandManager().loadCustomer(baseWrapper);
                customerModel = (CustomerModel)baseWrapper.getBasePersistableModel();
                customerAccountTypeId = customerModel.getCustomerAccountTypeId();

            /*}else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER) {
                Long retailerContactId = appUserModel.getRetailerContactId();
                RetailerContactModel retailerContactModel = getCommonCommandManager().getRetailerContactManager().findByPrimaryKey(retailerContactId);
                customerAccountTypeId = retailerContactModel.getOlaCustomerAccountTypeModelId();
                if(appUserModel.getHandlerId() != null) {
                    handlerId = appUserModel.getHandlerId();
                }*/
            } else {
                return;
            }

            responseMap = this.getCommonCommandManager().getAccountManager().getAllLimits(new Date(), customerAccountTypeId, handlerId, appUserModel.getNic(), limitType);

        } catch(Exception ex) {
            handleException(ex);
        }
    }

    @Override
    public String response()
    {
        return MiniXMLUtil.createInfoResponseXMLByParams(responseMap);

        /*if(deviceTypeId.equals(DeviceTypeConstantsInterface.USSD.toString())) {
            return getUSSDResponse();
        } else return MiniXMLUtil.createInfoResponseXMLByParams(responseMap);*/
    }

/*
    private String getUSSDResponse()
    {
        StringBuilder strBuilder = new StringBuilder();
        String creditLimits = null;
        String debitLimits = null;
        if(limitType == 1) {
            creditLimits = (String) responseMap.get(CommandFieldConstants.KEY_DAILY_CREDIT_LIMIT);
            debitLimits = (String) responseMap.get(CommandFieldConstants.KEY_DAILY_DEBIT_LIMIT);
        } else if(limitType == 2) {
            creditLimits = (String) responseMap.get(CommandFieldConstants.KEY_MONTHLY_CREDIT_LIMIT);
            debitLimits = (String) responseMap.get(CommandFieldConstants.KEY_MONTHLY_DEBIT_LIMIT);
        } else if(limitType == 3) {
            creditLimits = (String) responseMap.get(CommandFieldConstants.KEY_YEARLY_CREDIT_LIMIT);
            debitLimits = (String) responseMap.get(CommandFieldConstants.KEY_YEARLY_DEBIT_LIMIT);
        }

        strBuilder.append(this.getMessageSource().getMessage("USSD.DEBITCREDITLIMITS", new Object[]{creditLimits, debitLimits}, null));


        return strBuilder.toString();
    }
*/

}


