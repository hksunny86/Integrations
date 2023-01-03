package com.inov8.microbank.webapp.action.userdeviceaccount;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.demographics.model.DemographicsModel;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnBlockJSWalletDeviceController extends AjaxController {


    private AppUserManager appUserManager;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String error = null;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        // getting parameters from request
        Long appUserId = new Long(ServletRequestUtils.getStringParameter(request, "appUserId"));
        DemographicsModel demographicsModel = new DemographicsModel();
        AppUserModel appUserModel = appUserManager.loadAppUser(appUserId);
        if (appUserModel != null) {
            demographicsModel = this.getCommonCommandManager().getDemographicsManager().loadDemographics(appUserModel.getAppUserId());

            if (demographicsModel != null) {
                demographicsModel.setLocked(false);
                demographicsModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
                demographicsModel.setUpdatedOn(new Date());
                this.getCommonCommandManager().getDemographicsManager().saveOrUpdate(demographicsModel);
                buffer.append("Device UnBlock SuccessFully");
            } else {
                buffer.append("Data Not Found in Demographics");
            }
        } else {
            buffer.append("Customer Not Found");
        }


        return buffer.toString();
    }


    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    private NadraIntegrationController getNadraIntegrationController() {
        return HttpInvokerUtil.getHttpInvokerFactoryBean(NadraIntegrationController.class,
                MessageUtil.getMessage("NadraIntegrationURL"));
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public String getDate(Date issaunceDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(issaunceDate);

        return strDate;
    }


}
