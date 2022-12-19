package com.inov8.microbank.server.service.commandmodule.minicommandmodule;
/* 
Created by IntelliJ IDEA 
@Project Name: trunk.
  @Copyright: 3/30/2022 On: 12:26 PM
  @author(Muhammad Aqeel)
*/

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.fonepay.common.FonePayResponseCodes;
import com.inov8.microbank.fonepay.common.FonePayUtils;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.commandmodule.AllPayBillInfoCommand;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared.util.xml.XmlUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class BlinkBVSVerificationInquiryCommand extends BaseCommand {
    private final Log logger = LogFactory.getLog(BlinkBVSVerificationInquiryCommand.class);
    String mobileNo;
    String cnic;
    private BlinkCustomerModel blinkCustomerModel;
    private List<BlinkCustomerModel> blinkCustomerModelList;
    private List<CustomerModel> customerModelList;
    @Override
    public void prepare(BaseWrapper baseWrapper) {
        mobileNo = this.getCommandParameter(baseWrapper, CommandFieldConstants.KEY_MOB_NO);
        cnic=this.getCommandParameter(baseWrapper,CommandFieldConstants.KEY_CNIC);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return null;
    }

    @Override
    public void execute() throws CommandException {
        blinkCustomerModel=new BlinkCustomerModel();
        blinkCustomerModel.setMobileNo(mobileNo);
        blinkCustomerModel.setCnic(cnic);
        blinkCustomerModelList = new ArrayList<BlinkCustomerModel>();
        try {
            blinkCustomerModelList=commonCommandManager.getAllBlinkData(blinkCustomerModel);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        AppUserModel appUserModel = null;
        try {
            appUserModel = commonCommandManager.loadAppUserByCNIC(cnic);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        CustomerModel customerModel=new CustomerModel();
        customerModel.setMobileNo(mobileNo);
        try {
            customerModelList=commonCommandManager.getAllCustomerData(customerModel);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        if (blinkCustomerModelList.get(0).getRegistrationStatus().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED.toString()) && blinkCustomerModelList.get(0).getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                if (appUserModel.getRegistrationStateId().equals(BlinkCustomerRegistrationStateConstantsInterface.APPROVED) && customerModelList.get(0).getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.BLINK)) {
                    try {
                        logger.info("Blink Registration State Approved or Account type Blink");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        this.logger.error("[BlinkBVSVerificationInquiryCommand.Execute] Error occured: " + ex.getMessage(), ex);
                        if (ex instanceof NullPointerException
                                || ex instanceof HibernateException
                                || ex instanceof SQLException
                                || ex instanceof DataAccessException
                                || (ex.getMessage() != null && ex.getMessage().indexOf("Exception") != -1)) {

                            logger.error("Converting Exception (" + ex.getClass() + ") to generic error message...");
                        }
                    }
                }
            }
            else{
               logger.error("Blink Registration State in not Approved or Account type in not Blink");
            }

    }

    @Override
    public String response() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE)

                .append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_ACC_TYPE)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(customerModelList.get(0).getTypeOfAccount())
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_REG_STATE)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(blinkCustomerModelList.get(0).getRegistrationStateName())
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE)
                .append(ATTR_ACC_TITLE)
                .append(TAG_SYMBOL_EQUAL)
                .append(TAG_SYMBOL_QUOTE)
                .append(blinkCustomerModelList.get(0).getConsumerName())
                .append(TAG_SYMBOL_QUOTE)
                .append(TAG_SYMBOL_CLOSE)
                .append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);

        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);


        return strBuilder.toString();
    }
}
