package com.inov8.microbank.webapp.action.allpaymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class EnableDisableAgentUSSDController extends AjaxController {
    private UserDeviceAccountListViewManager userDeviceAccountListViewManager;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    private RetailerContactManager retailerContactManager;
    private HandlerManager handlerManager;
    private AppUserManager appUserManager;
    private CustomerDAO customerDAO;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer buffer = new StringBuffer();
        try{
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            // getting parameters from request
            Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "appUserId")));
            UserDeviceAccountsModel uda = userDeviceAccountListViewManager.findUserDeviceByAppUserId(appUserId);
            if(uda != null)
            {
                UserDeviceAccountsModel isAlreadyExistUDA = userDeviceAccountsDAO.findUserDeviceAccountByAppUserIdAndDeviceTypeId(appUserId,DeviceTypeConstantsInterface.USSD);
                if(isAlreadyExistUDA == null)
                {
                    UserDeviceAccountsModel ussdModel = (UserDeviceAccountsModel) uda.clone();
                    ussdModel.setUserDeviceAccountsId(null);
                    ussdModel.setDeviceTypeId(DeviceTypeConstantsInterface.USSD);
                    ussdModel.setCreatedOn(new Date());
                    ussdModel.setUpdatedOn(new Date());
                    ussdModel.setCreatedBy(UserUtils.getCurrentUser().getAppUserId());
                    ussdModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                    userDeviceAccountsDAO.saveOrUpdate(ussdModel);
                }
            }
            Boolean isEnabled = new Boolean(ServletRequestUtils.getStringParameter(request, "isEnabled"));
            Boolean updatedEnabled = null;
            if(isEnabled)
                updatedEnabled = Boolean.FALSE;
            else
                updatedEnabled = Boolean.TRUE;
            Long retailerContactId = null;
            RetailerContactModel retailerModel = null;
            HandlerModel handlerModel = null;
            Long handlerId = null;
            AppUserModel appUserModel = new AppUserModel();
            appUserModel.setAppUserId(appUserId);
            baseWrapper.setBasePersistableModel(appUserModel);
            appUserModel = (AppUserModel) appUserManager.loadAppUser(baseWrapper).getBasePersistableModel();
            baseWrapper.putObject("appUserTypeId", appUserModel.getAppUserTypeId());
            // putting log information into wrapper for further used
            if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.RETAILER){
                retailerContactId = new Long(ServletRequestUtils.getStringParameter(request, "retailerContactId"));
                retailerModel = retailerContactManager.loadRetailerContactByRetailerContactId(retailerContactId);
                if(null==retailerModel){
                    throw new FrameworkCheckedException("Retailer contact not found.");
                }
                retailerModel.setAgentUssdEnabled(updatedEnabled);
                baseWrapper.setBasePersistableModel(retailerModel);
                retailerModel = retailerContactManager.saveOrUpdateRetailerContactModel(retailerModel);
                if(isEnabled == true){
                    buffer.append("Agent USSD has been enabled.");
                }else{
                    buffer.append("Agent USSD has been Disabled.");
                }
            }
            else if(null != handlerId && appUserModel.getAppUserTypeId().longValue() == 12){
                handlerModel = new HandlerModel();
                handlerModel.setHandlerId(handlerId);
                baseWrapper.setBasePersistableModel(handlerModel);
                handlerModel = (HandlerModel) handlerManager.loadHandler(baseWrapper).getBasePersistableModel();
                if(null==handlerModel){
                    throw new FrameworkCheckedException("Handler not found.");
                }
                handlerModel.setAgentUssdEnabled(updatedEnabled);
                baseWrapper.setBasePersistableModel(handlerModel);
                handlerManager.createOrUpdateHandler(baseWrapper);
                if(isEnabled == true){
                    buffer.append("Agent USSD has been enabled.");
                }else{
                    buffer.append("Agent USSD has been enabled.");
                }
            }
            else if(appUserModel.getAppUserTypeId().longValue() == UserTypeConstantsInterface.CUSTOMER){
                CustomerModel customerModel = appUserManager.getCustomerModelByPK(appUserModel.getCustomerId());
                customerModel.setCustomerUSSDEnabled(updatedEnabled);
                customerModel.setUpdatedOn(new Date());
                customerModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                customerDAO.saveOrUpdate(customerModel);
                if(isEnabled == true){
                    buffer.append("Customer USSD has been Enabled.");
                }else{
                    buffer.append("Customer USSD has been Disabled.");
                }
            }
        }
        catch(FrameworkCheckedException ex){
            ex.printStackTrace();
            buffer.append("Operation cannot be performed at the moment");
        }
        return buffer.toString();
    }

    public void setUserDeviceAccountListViewManager(
            UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
        this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setRetailerContactManager(
            RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

    public void setHandlerManager(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}
