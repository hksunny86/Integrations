package com.inov8.microbank.server.service.commandmodule.minicommandmodule;
/* 
Created by IntelliJ IDEA 
@Project Name: trunk.
  @Copyright: 3/30/2022 On: 3:28 PM
  @author(Muhammad Aqeel)
*/

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.dao.customermodule.BlinkCustomerModelDAO;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.commandmodule.BaseCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class BlinkBVSVerficationCommand extends BaseCommand {
    private final Log logger = LogFactory.getLog(BlinkBVSVerficationCommand.class);
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
        BlinkCustomerModel blinkCustomerModel=new BlinkCustomerModel();
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
        BlinkCustomerModel blinkModel = new BlinkCustomerModel();
        try {
            blinkModel=commonCommandManager.getAllBlinkDataByBlinkID(blinkCustomerModelList.get(0).getBlinkCustomerId());
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        blinkModel.setMobileNo(mobileNo);
        blinkModel.setCnic(cnic);
        blinkModel.setBVS(true);
        try {
            commonCommandManager.saveAllBlinkData(blinkModel);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        CustomerModel customerModel1=new CustomerModel();
        try {
            customerModel1=commonCommandManager.getAllCustomerDataByCustomerID(customerModelList.get(0).getCustomerId());
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }
        customerModel1.setMobileNo(mobileNo);
        customerModel1.setBlinkBvs(true);
        try {
            commonCommandManager.saveOrUpdateCustomerModel(customerModel1);
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String response() {
//        return MiniXMLUtil.createMessageXML("Success");
        List<LabelValueBean> params = new ArrayList<>();
        params.add(new LabelValueBean(XMLConstants.TAG_MSG.toUpperCase(), "Account is Verified Successfully"));
        return MiniXMLUtil.createInfoResponseXMLByParams(params);
    }
}
