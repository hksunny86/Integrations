package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

public class HRAAcOpeningConfirmationController extends AdvanceFormController {

    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;

    public HRAAcOpeningConfirmationController()
    {
        setCommandName("object");
        setCommandClass(MfsAccountModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        return new MfsAccountModel();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        String isLinkedRequest = requestWrapper.getParameter("IS_LINKED_REQ");
        if(!isTokenValid(httpServletRequest)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }
        String customerName = requestWrapper.getParameter("CNAME");
        String fatherName = requestWrapper.getParameter("FNAME");
        String customerDOB = requestWrapper.getParameter("CDOB");
        AppUserModel appUserModel = UserUtils.getCurrentUser();
        UserDeviceAccountsModel uda = allPayWebResponseDataPopulator.getUserDeviceAccountsDAO().findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        requestWrapper.addParameter(CommandFieldConstants.KEY_AGENT_MOBILE, UserUtils.getCurrentUser().getMobileNo());
        requestWrapper.addParameter(CommandFieldConstants.KEY_ENCRYPTION_TYPE,"1");
        requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
        AllPayRequestWrapper tempWrapper = requestWrapper;
        Integer pinRetryCount = 0;
        if(requestWrapper.getParameter("PIN_RETRY_COUNT") != null && !requestWrapper.getParameter("PIN_RETRY_COUNT").equals(""))
            pinRetryCount = Integer.parseInt(requestWrapper.getParameter("PIN_RETRY_COUNT"));
        requestWrapper.addParameter("ENCT","1");
        requestWrapper.addParameter("PIN_RETRY_COUNT",pinRetryCount.toString());
        String mpinValid = requestWrapper.getParameter("mpinValid");
        requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC,requestWrapper.getParameter("CNIC"));
        String responseXML = null;
        Boolean isErrorXML = false;
        if(mpinValid.equals("0"))
            responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_VERIFY_PIN);
        if(responseXML != null)
            isErrorXML = MfsWebUtil.isErrorXML(responseXML);
        if(isErrorXML)
        {
            pinRetryCount +=1;
            mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_MOBILE, requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE));
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC, requestWrapper.getParameter(CommandFieldConstants.KEY_CNIC));
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_NAME,customerName);
            requestWrapper.setAttribute("FNAME",fatherName);
            requestWrapper.setAttribute("CDOB",customerDOB);
            requestWrapper.setAttribute("isValid","0");
            requestWrapper.addParameter("PIN_RETRY_COUNT",pinRetryCount.toString());
            requestWrapper.setAttribute("mpinValid","0");
            requestWrapper.setAttribute("otpValid","0");
            requestWrapper.setAttribute("IS_LINKED_REQ",isLinkedRequest);
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return new ModelAndView("allpay-web/hraaccountopeningconfirmation");
        }
        requestWrapper.setAttribute("mpinValid","1");
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        responseXML = mfsWebController.handleRequest(requestWrapper,CommandFieldConstants.CMD_OPEN_HRA_ACCOUNT_PAYMENT);
        isErrorXML = MfsWebUtil.isErrorXML(responseXML);
        if(isErrorXML) {
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return new ModelAndView(getFormView());
        }
        if(isLinkedRequest != null && isLinkedRequest.equals("1"))
        {
            ThreadLocalAppUser.setAppUserModel(appUserModel);
            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
            String cwAmount = tempWrapper.getParameter("CW_AMOUNT");
            requestWrapper.addParameter(CommandFieldConstants.KEY_HRA_LINKED_REQUEST,"2");
            requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID,ProductConstantsInterface.CASH_WITHDRAWAL.toString());
            requestWrapper.addParameter(CommandFieldConstants.KEY_TXAM,tempWrapper.getParameter("CW_AMOUNT"));
            requestWrapper.addParameter(CommandFieldConstants.KEY_PAYMENT_MODE,"HRA");
            responseXML = mfsWebController.handleRequest(requestWrapper,CommandFieldConstants.CMD_CASH_OUT_INFO);
            isErrorXML = MfsWebUtil.isErrorXML(responseXML);
            if(isErrorXML) {
                return new ModelAndView("allpay-web/customerHraCashWithdrawalInfo");
            }
            requestWrapper.setAttribute("isValidOTP","0");
            requestWrapper.setAttribute("isValid","0");
            requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM, cwAmount);
            requestWrapper.addParameter("isValidOTP","0");
            requestWrapper.addParameter("isValid","0");
            return new ModelAndView("allpay-web/hraCashWithDrawalConfirmation");
        }
        return new ModelAndView("allpay-web/welcomescreen");
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }

    public void setMfsWebController(MfsWebManager mfsWebController) {
        this.mfsWebController = mfsWebController;
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }
}
