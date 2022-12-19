package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
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

public class AccountOpeningWithBVSController extends AdvanceFormController {

    private static final String PRODUCT_NAME = "Product";
    private static final String DEVICE_F_ID = "dfid";
    private static final String PRODUCT_ID = "PID";

    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;

    public AccountOpeningWithBVSController()
    {
        setCommandName("object");
        setCommandClass(MfsAccountModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        requestWrapper.addParameter(PRODUCT_NAME,"Account Opening");
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        return new MfsAccountModel();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        if(!isTokenValid(httpServletRequest)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }

        String mobileNumber = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
        String cNic= requestWrapper.getParameter(CommandFieldConstants.KEY_CNIC).trim();
        String PNAME = ProductConstantsInterface.ACCOUNT_OPENING_NAME;
        String GFID = DeviceFlowConstants.ACCOUNT_OPENING.toString();
        String PID = ProductConstantsInterface.ACCOUNT_OPENING.toString();
        String isLinkedRequest = requestWrapper.getParameter("IS_LINKED_REQ");
        requestWrapper.addParameter(PRODUCT_NAME, PNAME);
        requestWrapper.addParameter(DEVICE_F_ID, GFID);
        requestWrapper.addParameter(PRODUCT_ID, PID);

        logger.info("[AccountOpeningWithBVSController.onCreate] Validating Customer. Mobile No:" + mobileNumber + " Product ID:" + PID);

        String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
        AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
        ThreadLocalAppUser.setAppUserModel(appUserModel);
        requestWrapper.addParameter(PortalConstants.IS_BVS_ENABLE, "1");
        String initialDeposit = requestWrapper.getParameter("initialAmount");
        String cwAmount = requestWrapper.getParameter("CW_AMOUNT");
        String isUpgrade = requestWrapper.getParameter("IS_UPGRADE");
        String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_OPEN_CUSTOMER_L0_ACCOUNT_INQUIRY);

        Boolean isErrorXML = MfsWebUtil.isErrorXML(responseXML);
        if(isErrorXML) {
            mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
            requestWrapper.addParameter(PRODUCT_NAME, Encryption.encrypt(PNAME));
            requestWrapper.addParameter(DEVICE_F_ID, Encryption.encrypt(GFID));
            requestWrapper.addParameter(PRODUCT_ID, Encryption.encrypt(PID));
            requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNumber);
            requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC, cNic);
            requestWrapper.setAttribute("IS_UPGRADE",isUpgrade);
            requestWrapper.setAttribute("IS_LINKED_REQ",isLinkedRequest);
            return new ModelAndView(getFormView());
        }
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_MOBILE, mobileNumber);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC, cNic);
        requestWrapper.setAttribute("IS_LINKED_REQ",isLinkedRequest);
        requestWrapper.setAttribute("isValid","0");
        requestWrapper.setAttribute("otpValid","0");
        requestWrapper.setAttribute("mpinValid","0");
        requestWrapper.setAttribute("nadraValid","0");
        return new ModelAndView(getSuccessView());
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
