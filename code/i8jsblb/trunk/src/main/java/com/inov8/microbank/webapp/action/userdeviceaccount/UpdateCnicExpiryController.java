package com.inov8.microbank.webapp.action.userdeviceaccount;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.AppUserVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateCnicExpiryController extends AjaxController {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat1 = new SimpleDateFormat("MM-dd-yyyy");

    private AppUserManager appUserManager;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String error = null;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        // getting parameters from request
        Long appUserId = new Long(ServletRequestUtils.getStringParameter(request, "appUserId"));

        AppUserModel appUserModel = appUserManager.loadAppUser(appUserId);

        AppUserVO appUserVO = new AppUserVO();
        appUserVO.setMobileNo(appUserModel.getMobileNo());
        appUserVO.setUserName(appUserModel.getUsername());
        appUserVO.setAppUserId(appUserModel.getAppUserId().toString());
        if (appUserModel != null) {
            NadraIntegrationVO iVo = new NadraIntegrationVO();
            String mobileNo = appUserModel.getMobileNo();
            String cnic = appUserModel.getNic();
            String cnicIssueDate = getDate(appUserModel.getCnicIssuanceDate());
            iVo.setCnicIssuanceDate(cnicIssueDate);
            iVo.setContactNo(mobileNo);
            iVo.setCitizenNumber(cnic);
            iVo.setAreaName("Punjab");
            logger.info("Nadra Info: ");
            try {
                iVo = this.getNadraIntegrationController().getCitizenData(iVo);
                if (!iVo.getResponseCode().equals("100"))
                    throw new CommandException(iVo.getResponseDescription(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, null);
                logger.info("Nadra Verfication data for NIC: " + cnic + " Mother Name: " + iVo.getMotherName() + " Cnic Expiry: " + iVo.getCardExpire());
                appUserModel.setNicExpiryDate(dateFormat.parse(iVo.getCardExpire()));
                baseWrapper.setBasePersistableModel(appUserModel);
                appUserManager.saveOrUpdateAppUser(baseWrapper);
                buffer.append("Cnic Update SuccessFully");
            } catch (CommandException e) {
                buffer.append("Error occured on Update Customer Cnic " +e.getMessage());
            }catch (Exception ex){
                buffer.append("Error occured on Update Customer Cnic " +ex.getMessage());
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
