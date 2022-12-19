package com.inov8.microbank.webapp.action.userdeviceaccount;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.OlaStatusConstants;
import com.inov8.microbank.common.util.PaymentModeConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;
import org.ajaxtags.helpers.AjaxXmlBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Inov8 on 7/23/2018.
 */
public class CustomerAccountActionController extends AjaxController {

    private AppUserManager appUserManager;
    private SmartMoneyAccountManager smartMoneyAccountManager ;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;

    @Override
    public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Boolean isLockOrActive = false;
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String action = null;
        String useCaseId= request.getParameter("usecaseId");
        long lUseCaseId = Long.parseLong(useCaseId);
        if(lUseCaseId == PortalConstants.BLOCK_CUSTOMER_USECASE_ID)
            action = "BLOCK";
        else if(lUseCaseId == PortalConstants.UNBLOCK_CUSTOMER_USECASE_ID)
            action = "UN-BLOCK";
        else if(lUseCaseId == PortalConstants.REACTIVATE_CUSTOMER_USECASE_ID)
            action = "ACTIVE";
        else if(lUseCaseId == PortalConstants.DEACTIVATE_CUSTOMER_USECASE_ID)
            action = "DE-ACTIVE";
        String account = request.getParameter("account");
        Long appUserId = Long.parseLong(request.getParameter("appUserId"));
        AppUserModel appUserModel = appUserManager.loadAppUser(appUserId);

        SmartMoneyAccountModel smartMoneyAccountModel = null;

        SmartMoneyAccountModel sma = new SmartMoneyAccountModel();
        sma.setCustomerId(appUserModel.getCustomerId());
        sma.setPaymentModeId(PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
        sma.setActive(Boolean.TRUE);

        smartMoneyAccountModel = smartMoneyAccountManager.getSmartMoneyAccountByCustomerIdAndPaymentModeId(sma);

        if(action != null && account == null)
        {
            if(action.equalsIgnoreCase("BLOCK") || action.equalsIgnoreCase("UN-BLOCK"))
                isLockOrActive = true;
            else if(action.equalsIgnoreCase("ACTIVE") || action.equalsIgnoreCase("DE-ACTIVE"))
                isLockOrActive = false;

            UserDeviceAccountsModel userDeviceAccountsModel = userDeviceAccountsDAO.loadUserDeviceAccountByUserId(appUserModel.getUsername());

            if(isLockOrActive)
            {
                if(action.equalsIgnoreCase("BLOCK"))
                {
                    if(!userDeviceAccountsModel.getAccountLocked())
                        ajaxXmlBuilder.addItem("name","BLB");
                    if(smartMoneyAccountModel != null && smartMoneyAccountModel.getStatusId() !=null
                            && (smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE)
                            || smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE)))
                        ajaxXmlBuilder.addItem("name","HRA");
                    else if(userDeviceAccountsModel.getAccountLocked() && smartMoneyAccountModel != null)
                        ajaxXmlBuilder.addItem("name","NO");
                    else if(userDeviceAccountsModel.getAccountLocked())
                        ajaxXmlBuilder.addItem("name","NO-HRA");
                }
                else if(action.equalsIgnoreCase("UN-BLOCK"))
                {
                    if(userDeviceAccountsModel.getAccountLocked())
                        ajaxXmlBuilder.addItem("name","BLB");
                    if(smartMoneyAccountModel != null && smartMoneyAccountModel.getStatusId() !=null
                            && (smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE)
                            || smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED)))
                        ajaxXmlBuilder.addItem("name","HRA");
                    else if(!userDeviceAccountsModel.getAccountLocked() && smartMoneyAccountModel != null)
                        ajaxXmlBuilder.addItem("name","NO");
                    else if(!userDeviceAccountsModel.getAccountLocked())
                        ajaxXmlBuilder.addItem("name","NO-HRA");
                }
            }
            else
            {
                if(action.equalsIgnoreCase("ACTIVE"))
                {
                    if(!userDeviceAccountsModel.getAccountEnabled())
                        ajaxXmlBuilder.addItem("name","BLB");
                    if(smartMoneyAccountModel != null && smartMoneyAccountModel.getStatusId() !=null
                            && (smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE)
                            || smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED)))
                        ajaxXmlBuilder.addItem("name","HRA");
                    else if(userDeviceAccountsModel.getAccountEnabled() && smartMoneyAccountModel != null)
                        ajaxXmlBuilder.addItem("name","NO");
                    else if(userDeviceAccountsModel.getAccountEnabled())
                        ajaxXmlBuilder.addItem("name","NO-HRA");
                }
                else if(action.equalsIgnoreCase("DE-ACTIVE"))
                {
                    if(userDeviceAccountsModel.getAccountEnabled())
                        ajaxXmlBuilder.addItem("name","BLB");
                    if(smartMoneyAccountModel != null && smartMoneyAccountModel.getStatusId() !=null
                            && (smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE)
                            || smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED)))
                        ajaxXmlBuilder.addItem("name","HRA");
                    else if(!userDeviceAccountsModel.getAccountEnabled() && smartMoneyAccountModel != null)
                        ajaxXmlBuilder.addItem("name","NO");
                    else if(!userDeviceAccountsModel.getAccountEnabled())
                        ajaxXmlBuilder.addItem("name","NO-HRA");
                }
            }
        }
        else if(account != null && action != null && account.equals("HRA"))
        {
            if(action.equalsIgnoreCase("ACTIVE") || action.equalsIgnoreCase("DE-ACTIVE"))
            {
                if(smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                {
                    ajaxXmlBuilder.addItem("name","YES");
                    ajaxXmlBuilder.addItem("name",smartMoneyAccountModel.getStatusId().toString());
                }
                else
                    ajaxXmlBuilder.addItem("name","NO");
            }
            else if(action.equalsIgnoreCase("BLOCK") || action.equalsIgnoreCase("UN-BLOCK"))
            {
                if(smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE) || smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE))
                    ajaxXmlBuilder.addItem("name","NO");
                else
                    ajaxXmlBuilder.addItem("name","NO");
            }
            /*if(isLockOrActive)
            {
                if(action.equalsIgnoreCase("UN-BLOCK"))
                {
                    if(smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                        ajaxXmlBuilder.addItem("name","IN-ACTIVE");
                }
                else if(action.equalsIgnoreCase("UN-BLOCK"))
                {
                    if(smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE))
                        ajaxXmlBuilder.addItem("name","ACTIVE");
                }
            }
            else
            {
                if(action.equalsIgnoreCase("ACTIVE") || action.equalsIgnoreCase("DE-ACTIVE"))
                {
                    if(smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_BLOCKED))
                        ajaxXmlBuilder.addItem("name","BLOCKED");
                    else
                        ajaxXmlBuilder.addItem("name","NO");
                }
                else
                    ajaxXmlBuilder.addItem("name","NO");
                *//*else if(action.equalsIgnoreCase("DE-ACTIVE"))
                {
                    if(smartMoneyAccountModel.getStatusId().equals(OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE))
                        ajaxXmlBuilder.addItem("name","IN-ACTIVE");
                }*//*
            }*/
        }
        if(logger.isDebugEnabled())
        {
            logger.debug("end of getResponseContent of CustomerAccountActionController");
        }
        return ajaxXmlBuilder.toString();
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }
}
