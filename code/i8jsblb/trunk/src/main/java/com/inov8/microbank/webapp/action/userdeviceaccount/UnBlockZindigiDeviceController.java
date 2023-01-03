package com.inov8.microbank.webapp.action.userdeviceaccount;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.i8sb.constants.I8SBConstants;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerRequestVO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.util.HttpInvokerUtil;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.ESBAdapter;
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
import java.util.Random;

public class UnBlockZindigiDeviceController extends AjaxController {

    private ESBAdapter esbAdapter;

    private AppUserManager appUserManager;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String error = null;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        // getting parameters from request
        Long appUserId = new Long(ServletRequestUtils.getStringParameter(request, "appUserId"));
        AppUserModel appUserModel = appUserManager.loadAppUser(appUserId);
        if (appUserModel != null) {
            I8SBSwitchControllerRequestVO requestVO = new I8SBSwitchControllerRequestVO();
            I8SBSwitchControllerResponseVO responseVO = new I8SBSwitchControllerResponseVO();
            esbAdapter = new ESBAdapter();
            String transmissionDateTime = new SimpleDateFormat("yyyyMMddHHss").format(new Date());
            String stan = String.valueOf((new Random().nextInt(90000000)));
            requestVO = esbAdapter.prepareCustomerDeviceVerificationRequest(I8SBConstants.RequestType_UpdateKyc);
            requestVO.setMobileNumber(appUserModel.getMobileNo());
            SwitchWrapper sWrapper = new SwitchWrapperImpl();
            sWrapper.setI8SBSwitchControllerRequestVO(requestVO);
            sWrapper.setI8SBSwitchControllerResponseVO(responseVO);
            sWrapper = esbAdapter.makeI8SBCall(sWrapper);
            ESBAdapter.processI8sbResponseCode(sWrapper.getI8SBSwitchControllerResponseVO(), false);
            responseVO = sWrapper.getI8SBSwitchControllerRequestVO().getI8SBSwitchControllerResponseVO();
            if (!responseVO.getResponseCode().equals("I8SB-200")) {
                buffer.append("Data Not Found in Zindigi");

            } else
                buffer.append(responseVO.getDescription());

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

    public void setEsbAdapter(ESBAdapter esbAdapter) {
        this.esbAdapter = esbAdapter;
    }


}
