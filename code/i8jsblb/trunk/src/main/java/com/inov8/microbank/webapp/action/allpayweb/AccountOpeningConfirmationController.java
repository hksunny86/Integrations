package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

public class AccountOpeningConfirmationController extends AdvanceFormController {

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

    public AccountOpeningConfirmationController()
    {
        setCommandName("mfsAccountModel");
        setCommandClass(MfsAccountModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        MfsAccountModel mfsAccountModel = new MfsAccountModel();
        requestWrapper.setAttribute("mfsAccountModel", mfsAccountModel);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        Map referenceDataMap = new HashMap();

        referenceDataMap.put("mfsAccountModel", mfsAccountModel);

        return referenceDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        MfsAccountModel mfsAccountModel = new MfsAccountModel();
        mfsAccountModel.setFingerIndex("1");
        return mfsAccountModel;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        if(!isTokenValid(httpServletRequest)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }
        String isLinkedRequest = requestWrapper.getParameter("IS_LINKED_REQ");
        String initialDeposit = requestWrapper.getParameter("initialAmount");
        MfsAccountModel mfsAccountModel = (MfsAccountModel) o;
        String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
        AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
        UserDeviceAccountsModel uda = allPayWebResponseDataPopulator.getUserDeviceAccountsDAO().findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        String fingerIndex = httpServletRequest.getParameter("fingerIndex");
        String cNic = requestWrapper.getParameter(CommandFieldConstants.KEY_CNIC).trim();
        String mobileNo = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
        String otp = requestWrapper.getParameter("OTP");
        String otpValid = requestWrapper.getParameter("otpValid");
        String mpinValid = requestWrapper.getParameter("mpinValid");
        if(fingerIndex != null && !fingerIndex.equals(""))
            mfsAccountModel.setFingerIndex(fingerIndex);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC,cNic);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNo);
        Integer pinRetryCount = 0;
        if(requestWrapper.getParameter("PIN_RETRY_COUNT") != null && !requestWrapper.getParameter("PIN_RETRY_COUNT").equals(""))
            pinRetryCount = Integer.parseInt(requestWrapper.getParameter("PIN_RETRY_COUNT"));
        requestWrapper.addParameter("ENCT","1");
        requestWrapper.addParameter("PIN_RETRY_COUNT",pinRetryCount.toString());
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNo);
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
            this.prepareErrorResponse(requestWrapper,responseXML);
            requestWrapper.addParameter("PIN_RETRY_COUNT",pinRetryCount.toString());
            requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC,cNic);
            requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNo);
            requestWrapper.addParameter("OTP",otp);
            requestWrapper.setAttribute("mpinValid","0");
            requestWrapper.setAttribute("otpValid","0");
            requestWrapper.setAttribute("IS_LINKED_REQ",isLinkedRequest);
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return new ModelAndView(getFormView());
        }
        requestWrapper.setAttribute("mpinValid","1");
        requestWrapper.addParameter("PIN",otp);
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        requestWrapper.addParameter(CommandFieldConstants.KEY_MOB_NO,mobileNo);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CURR_COMMAND_ID, CommandFieldConstants.CMD_OTP_VERIFICATION);
        if(otpValid.equals("0"))
            responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_OTP_VERIFICATION);
        if(responseXML != null)
            isErrorXML = MfsWebUtil.isErrorXML(responseXML);
        if(isErrorXML)
        {
            this.prepareErrorResponse(requestWrapper,responseXML);
            requestWrapper.addParameter("OTP",otp);
            requestWrapper.setAttribute("otpValid","0");
            requestWrapper.setAttribute("IS_LINKED_REQ",isLinkedRequest);
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return new ModelAndView(getFormView());
        }
        requestWrapper.setAttribute("otpValid","1");
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        requestWrapper.addParameter(CommandFieldConstants.KEY_FINGER_INDEX,fingerIndex);
        requestWrapper.addParameter(CommandFieldConstants.KEY_FINGER_TEMPLATE,mfsAccountModel.getThumbImpressionPic());
        requestWrapper.addParameter(CommandFieldConstants.KEY_TEMPLATE_TYPE,"1");
        requestWrapper.addParameter("IS_UPGRADE","1");
        logger.info("[AccountOpeningWithBVSConfirmationController.onCreate] Validating Customer BVS Against Mobile No:" + mobileNo + " CNIC:" + cNic);
        requestWrapper.addParameter(PortalConstants.IS_BVS_ENABLE, "1");
        responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CUSTOMER_NADRA_VERIFICATION);
       /* responseXML = "<msg id=\"181\"><params><param name=\"DTID\">8</param><param name=\"CREG_STATE_ID\">null</param><param name=\"CMOB\">03088008099</param>" +
                "<param name=\"CNIC\">3430114376879</param><param name=\"BIRTH_PLACE\">حافظ آباد,حافظ آباد</param><param name=\"RESP\">100</param>" +
                "<param name=\"CNAME\">شہریار نواز</param><param name=\"MOTHER_MAIDEN\"> پروین</param><param name=\"CNIC_EXP\">2028-01-20</param>" +
                "<param name=\"CDOB\">1992-04-13</param><param name=\"CNIC_STATUS\">2028-01-20</param>" +
                "<param name=\"PRESENT_ADDR\">\u202Eڈاک\u202A \u202Cخانہ\u202A،\u202A،\u202A \u202Cتحصیل\u202A \u202Cوضلع\u202A \u202Cحافظ\u202A \u202Cآباد\u202C</param>" +
                "<param name=\"PERMANENT_ADDR\">\u202Eڈاک\u202A \u202Cخانہ\u202A،\u202Aہ،\u202A \u202Cتحصیل\u202A \u202Cوضلع\u202A \u202Cحافظ\u202A \u202Cآباد\u202C</param>" +
                "<param name=\"RTIMAGES\">1</param><param name=\"MUAOR\">1</param><param name=\"IDR\">1</param></params></msg>";*/
        isErrorXML = MfsWebUtil.isErrorXML(responseXML);
        if(isErrorXML) {
            this.prepareErrorResponse(requestWrapper,responseXML);
            requestWrapper.setAttribute("IS_LINKED_REQ",isLinkedRequest);
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return new ModelAndView(getFormView());
        }
        requestWrapper.setAttribute("nadraValid","1");
        requestWrapper.setAttribute("OTP",otp);
        mfsWebResponseDataPopulator.populateProductPurchase(requestWrapper,responseXML);
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        this.prepareWrapperForAccountOpening(requestWrapper);
        requestWrapper.addParameter(CommandFieldConstants.KEY_AGENT_MOBILE,appUserModel.getMobileNo());
        if(initialDeposit != null && !initialDeposit.equals("") && Double.parseDouble(initialDeposit) > 0)
        {
            requestWrapper.addParameter("DEPOSIT_AMT_FLAG","1");
            requestWrapper.addParameter("DEPOSIT_AMT",initialDeposit);
        }
        responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT);
        isErrorXML = MfsWebUtil.isErrorXML(responseXML);
        if(isErrorXML) {
            this.prepareErrorResponse(requestWrapper,responseXML);
            requestWrapper.setAttribute("IS_LINKED_REQ",isLinkedRequest);
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return new ModelAndView(getFormView());
        }
        mfsWebResponseDataPopulator.populateMessage(requestWrapper,responseXML);
        prepareWrapperForAccountOpening(requestWrapper);
        if(isLinkedRequest != null && isLinkedRequest.equals("1"))
        {
            requestWrapper.setAttribute("IS_LINKED_REQ","1");
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return new ModelAndView("allpay-web/hraaccountopeninginfo");
        }
        requestWrapper.setAttribute("isValid","1");
        return new ModelAndView("allpay-web/accountOpeningWithBVSConfirmation");
    }

    private void prepareWrapperForAccountOpening(AllPayRequestWrapper requestWrapper)
    {
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_NAME,(String) requestWrapper.getAttribute(CommandFieldConstants.KEY_CUSTOMER_NAME));
        requestWrapper.addParameter(CommandFieldConstants.KEY_MOTHER_MAIDEN,(String) requestWrapper.getAttribute(CommandFieldConstants.KEY_MOTHER_MAIDEN));
        requestWrapper.addParameter(CommandFieldConstants.KEY_BIRTH_PLACE,(String) requestWrapper.getAttribute(CommandFieldConstants.KEY_BIRTH_PLACE));
        requestWrapper.addParameter("CDOB",(String) requestWrapper.getAttribute("CDOB"));
        requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC_EXPIRY,(String) requestWrapper.getAttribute(CommandFieldConstants.KEY_CNIC_EXPIRY));
        requestWrapper.addParameter(CommandFieldConstants.KEY_PRESENT_ADDR,(String) requestWrapper.getAttribute(CommandFieldConstants.KEY_PRESENT_ADDR));
        requestWrapper.addParameter(CommandFieldConstants.KEY_PERMANENT_ADDR,(String) requestWrapper.getAttribute(CommandFieldConstants.KEY_PERMANENT_ADDR));
        requestWrapper.addParameter("IS_BVS_ACCOUNT","1");
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUST_ACC_TYPE, Long.toString(CustomerAccountTypeConstants.LEVEL_1));
        requestWrapper.addParameter("GENDER",(String) requestWrapper.getAttribute("GENDER"));
    }

    private void prepareErrorResponse(AllPayRequestWrapper requestWrapper, String responseXML)
    {
        mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_MOBILE, requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE));
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC, requestWrapper.getParameter(CommandFieldConstants.KEY_CNIC));
        requestWrapper.setAttribute("isValid","0");
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    public void setMfsWebController(MfsWebManager mfsWebController) {
        this.mfsWebController = mfsWebController;
    }

    public void setMfsWebResponseDataPopulator(MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }
}
