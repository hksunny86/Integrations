package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

//Customer HRA Cash WithDrawal;

public class  CashOutInfoController extends AdvanceFormController {

    private static final Logger LOGGER = Logger.getLogger(CashOutInfoController.class);
    private static final String PRODUCT_NAME = "Product";
    private static final String DEVICE_F_ID = "dfid";
    private static final String PRODUCT_ID = "PID";

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private MfsAccountManager mfsAccountManager;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

    public CashOutInfoController()
    {
        setCommandClass(Object.class);
        setCommandName("object");
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {


        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
////        requestWrapper.addParameter(PRODUCT_NAME,"Cash Out (By Transaction ID)");
////        requestWrapper.addParameter(PRODUCT_NAME,"customerCashOutInfo.aw?PID=0");
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

//        Long productId = null;
//        String productIdParam = httpServletRequest.getParameter("PID");
//        if(!StringUtils.isEmpty(productIdParam)) {
//            if(StringUtils.isNumeric(productIdParam)) {
//                productId = Long.valueOf(productIdParam);
//            }

//        if(productIdParam.equals("1")) {
//            requestWrapper.setAttribute("PID", productIdParam);
//            requestWrapper.setAttribute("LABEL", "Cash Out(By Transaction)");
//        }
//        else
//            requestWrapper.setAttribute("LABEL","Cash Out (By IVR)");
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
        LOGGER.info("CustomerCahOutInfo.onCreate() :: ");
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        if(!isTokenValid(httpServletRequest)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }
        //String mobileNumber = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
        String PNAME = "Cash Out (By Transaction Id)";
        String transactionID = requestWrapper.getParameter(CommandFieldConstants.KEY_TRANSACTION_ID ).trim();
        String PID = ProductConstantsInterface.CASH_WITHDRAWAL.toString();
        String txAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TXAM);



        String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
        AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);

        requestWrapper.addParameter(PRODUCT_NAME, PNAME);
        requestWrapper.addParameter(CommandFieldConstants.KEY_AGENT_MOBILE,appUserModel.getMobileNo());
        requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID,PID);
        requestWrapper.addParameter(CommandFieldConstants.KEY_TXAM,txAmount);
        requestWrapper.addParameter(CommandFieldConstants.KEY_TRANSACTION_ID,transactionID);
        requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID,DeviceTypeConstantsInterface.ALLPAY_WEB.toString());

        ThreadLocalAppUser.setAppUserModel(appUserModel);

        String responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_LEG2_INFO);

        this.mfsWebResponseDataPopulator.populateTransactionSummary(requestWrapper, responseXML);
        if(MfsWebUtil.isErrorXML(responseXML)) {
            this.mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
            /*requestWrapper.addParameter(PRODUCT_NAME, Encryption.encrypt(PNAME));
            requestWrapper.addParameter(DEVICE_F_ID, Encryption.encrypt(GFID));
            requestWrapper.addParameter(PRODUCT_ID, Encryption.encrypt(PID));*/
            return new ModelAndView(getFormView());
        }
        this.mfsWebResponseDataPopulator.populateProductPurchase(requestWrapper, responseXML);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_NAME, (String)requestWrapper.getAttribute("NAME"));
        requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM, txAmount);
        requestWrapper.addParameter(CommandFieldConstants.KEY_TRANSACTION_ID,transactionID);

        requestWrapper.setAttribute("isValid","0");
        requestWrapper.setAttribute("isValidOTP","0");
        requestWrapper.setAttribute("OTP","");
        requestWrapper.setAttribute("PIN","");
        return new ModelAndView(this.getSuccessView());
    }

    @Override
    protected  ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
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
