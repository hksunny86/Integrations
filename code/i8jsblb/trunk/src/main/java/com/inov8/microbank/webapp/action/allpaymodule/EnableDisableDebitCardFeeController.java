package com.inov8.microbank.webapp.action.allpaymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.HandlerModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.server.service.handlermodule.HandlerManager;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.userdeviceaccount.UserDeviceAccountListViewManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EnableDisableDebitCardFeeController  extends AjaxController {

    private UserDeviceAccountListViewManager userDeviceAccountListViewManager;
    private RetailerContactManager retailerContactManager;
    private HandlerManager handlerManager;
    private AppUserManager appUserManager;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer buffer = new StringBuffer();
        try{
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            // getting parameters from request
            Long appUserId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "appUserId")));
            String mfsId = new String(ServletRequestUtils.getStringParameter(request, "mfsId"));
            Boolean isEnabled = new Boolean(ServletRequestUtils.getStringParameter(request, "isEnabledDebitCardFee"));
            Long retailerContactId = null;
            Long handlerId = null;

            AppUserModel appUserModel = new AppUserModel();
            appUserModel.setAppUserId(appUserId);

            baseWrapper.setBasePersistableModel(appUserModel);
            appUserModel = (AppUserModel) appUserManager.loadAppUser(baseWrapper).getBasePersistableModel();
            baseWrapper.putObject("appUserTypeId", appUserModel.getAppUserTypeId());
            // putting log information into wrapper for further used

            if(appUserModel.getAppUserTypeId().longValue() == 3){
                retailerContactId = new Long(ServletRequestUtils.getStringParameter(request, "retailerContactId"));
            }else{
                handlerId = new Long(ServletRequestUtils.getStringParameter(request, "handlerId"));
            }
            if(isEnabled == false){
                isEnabled = true;
            }else{
                isEnabled = false;
            }

            RetailerContactModel retailerModel = null;
            HandlerModel handlerModel = null;
            if(null != retailerContactId && appUserModel.getAppUserTypeId().longValue() == 3){
                retailerModel = new RetailerContactModel();
                retailerModel = retailerContactManager.loadRetailerContactByRetailerContactId(retailerContactId);
                if(null==retailerModel){
                    throw new FrameworkCheckedException("Retailer contact not found.");
                }
                retailerModel.setIsDebitCardFeeEnabled(isEnabled);
                baseWrapper.setBasePersistableModel(retailerModel);
                retailerModel = retailerContactManager.saveOrUpdateRetailerContactModel(retailerModel);
                if(isEnabled == true){
                    buffer.append("Debit Card Fee has been enabled");
                }else{
                    buffer.append("Debit Card Fee has been Disabled");
                }

            }else if(null != handlerId && appUserModel.getAppUserTypeId().longValue() == 12){
                handlerModel = new HandlerModel();
                handlerModel.setHandlerId(handlerId);
                baseWrapper.setBasePersistableModel(handlerModel);
                handlerModel = (HandlerModel) handlerManager.loadHandler(baseWrapper).getBasePersistableModel();
                if(null==handlerModel){
                    throw new FrameworkCheckedException("Handler not found.");
                }

                handlerModel.setIsDebitCardFeeEnabled(isEnabled);
                baseWrapper.setBasePersistableModel(handlerModel);
                handlerManager.createOrUpdateHandler(baseWrapper);
                if(isEnabled == true){
                    buffer.append("Debit Card Fee has been enabled");
                }else{
                    buffer.append("Debit Card Fee has been Disabled");
                }
            }


        }
        catch(FrameworkCheckedException ex){
            ex.printStackTrace();
            buffer.append("Operation cannot be performed at the moment");
        }
        return buffer.toString();
    }

    public void setUserDeviceAccountListViewManager(UserDeviceAccountListViewManager userDeviceAccountListViewManager) {
        this.userDeviceAccountListViewManager = userDeviceAccountListViewManager;
    }

    public void setRetailerContactManager(RetailerContactManager retailerContactManager) {
        this.retailerContactManager = retailerContactManager;
    }

    public void setHandlerManager(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }
}
