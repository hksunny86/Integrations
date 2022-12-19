package com.inov8.microbank.tax.controller;

import com.inov8.framework.common.wrapper.*;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.tax.model.WHTExemptionModel;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Zeeshan on 6/28/2016.
 */
public class FetchDataByAppUserIdController  extends AjaxController {

    private static Log logger = LogFactory.getLog(FetchDataByAppUserIdController.class);
    private ActionLogManager actionLogManager;
    private CommonCommandManager commonCommandManager;


    @Override
    public String getResponseContent(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        logger.info("Executing Fetch Data By App User Id controller");
        
        String errMsg = null;
        String agentName = null;
        String agentCnic = null;

        BaseWrapper baseWrapper = new BaseWrapperImpl();
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, 3L);
        //baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, 3L);
        baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, 2L);

        ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

        AppUserModel appUserModel = new AppUserModel();
        WHTExemptionModel whtExemptionModel = new WHTExemptionModel();
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String userId = ServletRequestUtils.getRequiredStringParameter(request, "userId");
        UserDeviceAccountsModel udaModel=commonCommandManager.loadUserDeviceAccountByUserId(userId);
        if(udaModel!=null) {
            appUserModel = commonCommandManager.loadRetailerAppUserModelByAppUserId(udaModel.getAppUserId());
        }
        if(null!=appUserModel) {
            ajaxXmlBuilder.addItem("agentName", appUserModel.getFullName());
            ajaxXmlBuilder.addItem("agentCnic", appUserModel.getNic());
            ajaxXmlBuilder.addItem("errMsg", errMsg);
        }
        else
        {
            ajaxXmlBuilder.addItem("agentName", agentName);
            ajaxXmlBuilder.addItem("agentCnic", agentCnic);
            ajaxXmlBuilder.addItem("errMsg", "Retailer does not exist");
        }

            return ajaxXmlBuilder.toString();
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {this.commonCommandManager = commonCommandManager;}

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

}
