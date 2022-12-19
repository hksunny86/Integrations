package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.fop.viewer.Command;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

//Customer HRA Cash WithDrawal;

public class HRACashWithdrawalInfoController extends AdvanceFormController {

    private static final Logger LOGGER = Logger.getLogger(HRACashWithdrawalInfoController.class);
    private static final String PRODUCT_NAME = "Product";
    private static final String DEVICE_F_ID = "dfid";
    private static final String PRODUCT_ID = "PID";

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private MfsAccountManager mfsAccountManager;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

    public HRACashWithdrawalInfoController()
    {
        setCommandClass(Object.class);
        setCommandName("object");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        requestWrapper.addParameter(PRODUCT_NAME,"HRA Cash WithDrawal");
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        return new Object();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        LOGGER.info("HRACashWithDrawalInfoController.onCreate() :: ");
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        if(!isTokenValid(httpServletRequest)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }
        String mobileNumber = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
        String PNAME = "HRA Cash WithDrawal";
        String GFID = DeviceFlowConstants.AGENT_WEB_CASH_IN.toString();
        String PID = ProductConstantsInterface.HRA_CASH_WITHDRAWAL.toString();
        String txAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TXAM);
        requestWrapper.addParameter(PRODUCT_NAME, PNAME);
        //requestWrapper.addParameter(DEVICE_F_ID, GFID);
        requestWrapper.addParameter(PRODUCT_ID, PID);
        requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID,PID);
        requestWrapper.addParameter(CommandFieldConstants.KEY_TXAM,txAmount);
        requestWrapper.addParameter(CommandFieldConstants.KEY_PAYMENT_MODE,"HRA");
        CustomerModel customerModel = null;
        String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
        String cwAmount = requestWrapper.getParameter("CW_AMOUNT");
        AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
        AppUserModel example = new AppUserModel();
        example.setMobileNo(mobileNumber);
        example.setAppUserTypeId(UserTypeConstantsInterface.CUSTOMER);
        AppUserModel customerAppUserModel = allPayWebResponseDataPopulator.getAppUserModelByQuery(example);
        if(customerAppUserModel == null)
        {
            requestWrapper.setAttribute("IS_LINKED_REQ","1");
            return new ModelAndView("allpay-web/accountOpeningWithBVS");
        }
        else if(customerAppUserModel != null)
        {
            customerModel = allPayWebResponseDataPopulator.getCommonCommandManager().getCustomerModelById(customerAppUserModel.getCustomerId());
            if(customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_0))
            {
                requestWrapper.setAttribute("IS_LINKED_REQ","1");
                requestWrapper.setAttribute("IS_UPGRADE","1");
                return new ModelAndView("allpay-web/accountOpeningWithBVS");
            }
            else if(customerModel != null && customerModel.getCustomerAccountTypeId().equals(CustomerAccountTypeConstants.LEVEL_1))
            {
                SmartMoneyAccountModel smartMoneyAccountModel = allPayWebResponseDataPopulator.getCommonCommandManager().getSmartMoneyAccountByAppUserModelAndPaymentModId(
                        customerAppUserModel,PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT);
                if(smartMoneyAccountModel == null)
                {
                    requestWrapper.setAttribute("IS_LINKED_REQ","1");
                    requestWrapper.setAttribute("IS_UPGRADE","0");
                    requestWrapper.setAttribute("CW_AMOUNT",cwAmount);
                    return new ModelAndView("allpay-web/hraaccountopeninginfo");
                }
            }
        }
        requestWrapper.addParameter(CommandFieldConstants.KEY_AGENT_MOBILE,appUserModel.getMobileNo());
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CASH_OUT_INFO);
        if(MfsWebUtil.isErrorXML(responseXML)) {
            this.mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
            return new ModelAndView(getFormView());
        }
        this.mfsWebResponseDataPopulator.populateTransactionSummary(requestWrapper, responseXML);
        this.mfsWebResponseDataPopulator.populateProductPurchase(requestWrapper, responseXML);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM, txAmount);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_MOB_NO, mobileNumber);
        requestWrapper.setAttribute("isValid","0");
        requestWrapper.setAttribute("isValidOTP","0");
        requestWrapper.setAttribute("OTP","");
        requestWrapper.setAttribute("PIN","");
        return new ModelAndView(this.getSuccessView());
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

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }
}
