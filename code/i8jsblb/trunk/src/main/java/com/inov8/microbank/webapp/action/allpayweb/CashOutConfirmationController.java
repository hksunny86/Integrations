package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.allpayweb.formbean.AgentWebFormBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

public class CashOutConfirmationController extends AdvanceFormController {

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

    private static final String PRODUCT_ID = "PID";

    public CashOutConfirmationController()
    {
        setCommandClass(AgentWebFormBean.class);
        setCommandName("agentWebFormBean");
    }


    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        AgentWebFormBean agentWebFormBean = new AgentWebFormBean();
        agentWebFormBean.setDeviceTypeId(DeviceTypeConstantsInterface.ALLPAY_WEB);

        return agentWebFormBean;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        ModelAndView modelAndView = new ModelAndView( getSuccessView() );
        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        requestWrapper.addParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB.toString());
        String mobileNumber = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE).trim();
        String cNic = requestWrapper.getParameter(CommandFieldConstants.KEY_CNIC).trim();
        String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
        String OTP = requestWrapper.getParameter(CommandFieldConstants.KEY_ONE_TIME_PIN);
        String PIN = requestWrapper.getParameter(CommandFieldConstants.KEY_PIN);
        AppUserModel appUserModel = ThreadLocalAppUser.getAppUserModel();//allPayWebResponseDataPopulator.getAppUserModel(UID);
        UserDeviceAccountsModel uda = allPayWebResponseDataPopulator.getUserDeviceAccountsDAO().findUserDeviceAccountByAppUserId(appUserModel.getAppUserId());
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        String trxAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TXAM);
        String totalAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TOTAL_AMOUNT);
        String commAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_COMM_AMOUNT);
        String trxProcessingAmt = requestWrapper.getParameter(CommandFieldConstants.KEY_TX_PROCESS_AMNT);
        String cName = requestWrapper.getParameter(CommandFieldConstants.KEY_CUSTOMER_NAME);
        String TXID = requestWrapper.getParameter(CommandFieldConstants.KEY_TRANSACTION_ID);
        String PID = ProductConstantsInterface.CASH_WITHDRAWAL.toString();//requestWrapper.getParameter(PRODUCT_ID);
        if(!isTokenValid(httpServletRequest)) {
            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }
        String isOTPValidated = requestWrapper.getParameter("OTPIN");
        Integer pinRetryCount = 0;
        if(requestWrapper.getParameter("PIN_RETRY_COUNT") != null && !requestWrapper.getParameter("PIN_RETRY_COUNT").equals(""))
            pinRetryCount = Integer.parseInt(requestWrapper.getParameter("PIN_RETRY_COUNT"));
        requestWrapper.addParameter(CommandFieldConstants.KEY_PIN,OTP);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC,cNic);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CURR_COMMAND_ID,CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_REQUEST);
        requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID,PID);
        requestWrapper.addParameter(CommandFieldConstants.KEY_MOB_NO,mobileNumber);
        requestWrapper.addParameter(CommandFieldConstants.KEY_ONE_TIME_PIN,EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY,isOTPValidated));
        String responseXML = null;

        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);
        requestWrapper.addParameter("ENCT","1");
        requestWrapper.addParameter("PIN_RETRY_COUNT",pinRetryCount.toString());
        requestWrapper.addParameter(CommandFieldConstants.KEY_PIN,PIN);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC,cNic);
        responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_VERIFY_PIN);

        if(responseXML != null && MfsWebUtil.isErrorXML(responseXML) ) {
            pinRetryCount +=1;
            requestWrapper.setAttribute("isValid","0");
            mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
            requestWrapper.setAttribute("PIN_RETRY_COUNT",pinRetryCount.toString());

            requestWrapper.setAttribute("isValid","0");
            requestWrapper.setAttribute("isValidOTP","0");
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC,cNic);
            requestWrapper.addParameter(CommandFieldConstants.KEY_PIN,PIN);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM,trxAmount);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_TOTAL_AMOUNT,totalAmount);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_COMM_AMOUNT,commAmount);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_NAME,cName);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_TX_PROCESS_AMNT,trxProcessingAmt);
            requestWrapper.addParameter(CommandFieldConstants.KEY_ONE_TIME_PIN,EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY,isOTPValidated));
            requestWrapper.setAttribute("PIN","");
//            requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC,cNic);
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
                return modelAndView;
        }
        requestWrapper.addParameter(PRODUCT_ID, PID);
        requestWrapper.addParameter(CommandFieldConstants.KEY_AGENT_MOBILE,appUserModel.getMobileNo());
//OTP RETRY
        Integer otpRetryCount = 0;
        if(requestWrapper.getParameter("OTP_RETRY_COUNT") != null && !requestWrapper.getParameter("OTP_RETRY_COUNT").equals(""))
            pinRetryCount = Integer.parseInt(requestWrapper.getParameter("OTP_RETRY_COUNT"));
        requestWrapper.addParameter(CommandFieldConstants.KEY_PIN,PIN);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CNIC,cNic);
        requestWrapper.addParameter(CommandFieldConstants.KEY_CURR_COMMAND_ID,CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_REQUEST);
        requestWrapper.addParameter(CommandFieldConstants.KEY_PROD_ID,PID);
        requestWrapper.addParameter(CommandFieldConstants.KEY_MOB_NO,mobileNumber);
        requestWrapper.getParameter(CommandFieldConstants.KEY_PIN);

        ThreadLocalAppUser.setAppUserModel(appUserModel);
        ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(uda);

        requestWrapper.addParameter("ENCT","1");
        requestWrapper.addParameter("OTP_RETRY_COUNT",otpRetryCount.toString());
        String deviceTypeId = requestWrapper.getParameter(CommandFieldConstants.KEY_DEVICE_TYPE_ID);
        String encryptionType = requestWrapper.getParameter(CommandFieldConstants.KEY_ENCRYPTION_TYPE);
        String commissionAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_CAMT);
        String processingAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TPAM);
        String tAmount = requestWrapper.getParameter(CommandFieldConstants.KEY_TAMT);
        String agentMobileNo= requestWrapper.getParameter(CommandFieldConstants.KEY_AGENT_MOBILE);

        requestWrapper.addParameter(CommandFieldConstants.KEY_PIN,PIN);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_ONE_TIME_PIN,OTP);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_AGENT_MOBILE,agentMobileNo);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID,deviceTypeId);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_PROD_ID,PID);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM,trxAmount);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_ENCRYPTION_TYPE,encryptionType);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CAMT,commissionAmount);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_TPAM,processingAmount);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_TAMT,tAmount);
        requestWrapper.addParameter(CommandFieldConstants.KEY_TRANSACTION_ID,TXID);


        responseXML = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_CUSTOMER_CASH_WITHDRAWAL_LEG2);
        if(responseXML != null && MfsWebUtil.isErrorXML(responseXML) ) {
            otpRetryCount +=1;
            requestWrapper.setAttribute("isValid","0");
            mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);
            requestWrapper.setAttribute("OTP_RETRY_COUNT",otpRetryCount.toString());

            requestWrapper.setAttribute("isValidOTP","0");
            requestWrapper.setAttribute("isComplete","0");
//            allPayWebResponseDataPopulator.populateAccountHolderInfo(requestWrapper);
//            allPayWebResponseDataPopulator.populateTransactionInfo(requestWrapper);

            requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_MOBILE,mobileNumber);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC,cNic);
            requestWrapper.addParameter(CommandFieldConstants.KEY_PIN,PIN);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_TXAM,trxAmount);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_TOTAL_AMOUNT,totalAmount);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_COMM_AMOUNT,commAmount);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_NAME,cName);
            requestWrapper.setAttribute(CommandFieldConstants.KEY_TX_PROCESS_AMNT,trxProcessingAmt);
            requestWrapper.addParameter(CommandFieldConstants.KEY_ONE_TIME_PIN,EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY,isOTPValidated));
            requestWrapper.setAttribute("PIN","");
//            String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);
//            logger.error("errorType "+errorType);
//            modelAndView.setViewName(AllPayWebConstant.GENERIC_PAGE.getValue());
            requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");
            return modelAndView;
        }
        mfsWebResponseDataPopulator.populateProductPurchase(requestWrapper,responseXML);
        String charges = requestWrapper.getParameter("TPAM");
        requestWrapper.setAttribute(CommandFieldConstants.KEY_TRANSACTION_ID,TXID);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CNIC,cNic);
        requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Cash Successfully.");
        requestWrapper.setAttribute("isComplete","1");
        requestWrapper.setAttribute(PRODUCT_ID, PID);
        requestWrapper.setAttribute(CommandFieldConstants.KEY_CUSTOMER_NAME,cName);
        requestWrapper.setAttribute("TPAM",charges);
        return modelAndView;
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
