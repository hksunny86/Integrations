package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.integration.middleware.controller.NadraIntegrationController;
import com.inov8.integration.vo.NadraIntegrationVO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.webapp.action.allpayweb.formbean.AgentWebFormBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.inov8.microbank.webapp.action.allpayweb.AllPayWebResponseDataPopulator.isTokenValid;

public abstract class AgentWebFormController extends AdvanceFormController
{

    protected AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;
    protected MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    protected MfsWebManager mfsWebController;
    NadraIntegrationVO iVo = new NadraIntegrationVO();

    public AgentWebFormController() {
    }

    /* (non-Javadoc)
     * @see com.inov8.framework.webapp.action.AdvanceFormController#loadFormBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        AgentWebFormBean agentWebFormBean = new AgentWebFormBean();
        agentWebFormBean.setDeviceTypeId(DeviceTypeConstantsInterface.WEB);

        return agentWebFormBean;
    }


    /* (non-Javadoc)
     * @see com.inov8.framework.webapp.action.AdvanceFormController#loadReferenceData(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);
        return new HashMap();
    }


    /* (non-Javadoc)
     * @see com.inov8.framework.webapp.action.AdvanceFormController#onUpdate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model, BindException exception) throws Exception {

        return onCreate(httpServletRequest, httpServletResponse, model, exception);
    }


    /* (non-Javadoc)
     * @see com.inov8.framework.webapp.action.AdvanceFormController#onCreate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object model, BindException exception) throws Exception {

        long start = System.currentTimeMillis();

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(httpServletRequest);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        if (!isTokenValid(httpServletRequest)) {

            return new ModelAndView(AllPayWebConstant.GENERIC_PAGE.getValue());
        }

        AppUserModel appUserModel = (AppUserModel) httpServletRequest.getAttribute(CommandFieldConstants.KEY_APP_USER);

        if (appUserModel == null) {

            String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
            appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
            ThreadLocalAppUser.setAppUserModel(appUserModel);
        }

        ModelAndView modelAndView = new ModelAndView(getFormView());

        try {

            modelAndView = verfyPin(requestWrapper, httpServletResponse, model, appUserModel, exception);

        } finally {

            ThreadLocalAccountInfo.remove();
            ThreadLocalAppUser.remove();
            ThreadLocalUserDeviceAccounts.remove();

            logger.info("\n\nTransaction via AgentWeb Took :" + ((System.currentTimeMillis() - start) / 1000) + "Second");
        }

        return modelAndView;
    }


    /**
     * @param httpServletRequest
     * @param httpServletResponse
     * @param model
     * @param exception
     * @return
     * @throws Exception
     */
    protected ModelAndView verfyPin(AllPayRequestWrapper httpServletRequest, HttpServletResponse httpServletResponse, Object model, AppUserModel appUserModel, BindException exception) throws Exception {

        String plainPin = httpServletRequest.getParameter(CommandFieldConstants.KEY_PIN);

        String responseXML = "";

        String nextView = getSuccessView();

        ModelAndView modelAndView = new ModelAndView();

        if (!StringUtil.isNullOrEmpty(plainPin)) {

        	if(httpServletRequest.getParameter("FINGER_TEMPLATE") != null && httpServletRequest.getParameter("FINGER_TEMPLATE") != "" ){

                String areaName = allPayWebResponseDataPopulator.getAreaName(appUserModel);

	            iVo.setAreaName(areaName);
	            iVo.setCitizenNumber(httpServletRequest.getParameter("SWCNIC"));
	            iVo.setContactNo(httpServletRequest.getParameter("SWMOB"));
	            iVo.setFingerIndex(httpServletRequest.getParameter("FINGER_INDEX"));
	            iVo.setFingerTemplate(httpServletRequest.getParameter("FINGER_TEMPLATE"));
	            iVo.setSecondaryCitizenNumber(httpServletRequest.getParameter("RWCNIC"));
                iVo.setSecondaryContactNo(httpServletRequest.getParameter("RWMOB"));
	            iVo.setTemplateType("ISO_19794_2");
	            try {
	                iVo = this.getNadraIntegrationController().fingerPrintVerification(iVo);
	                if(null == iVo.getResponseCode() || !iVo.getResponseCode().equals("100")) {
	                    responseXML = MiniXMLUtil.createMessageXML("No record found in NADRA!");
	                    modelAndView.setViewName("allpay-web/genericblanknotificationscreen");
	                    httpServletRequest.setAttribute("errors",iVo.getResponseDescription());
	                    return modelAndView;
	                }
	            }catch(Exception e){
	                responseXML = MiniXMLUtil.createMessageXML("Error while connecting NADRA integeration!");
	                modelAndView.setViewName("allpay-web/genericblanknotificationscreen");
	                httpServletRequest.setAttribute("errors", "Error while connecting NADRA integeration!");
	                return modelAndView;
	            }
        	}
            Integer pinTryCount = 0;

            if (httpServletRequest.getSession().getAttribute("INVALID_BANK_PIN_COUNT") != null) {

                pinTryCount = (Integer) httpServletRequest.getSession().getAttribute("INVALID_BANK_PIN_COUNT");
            }

            httpServletRequest.addParameter("PIN_RETRY_COUNT", String.valueOf(pinTryCount));

            responseXML = mfsWebController.handleRequest(httpServletRequest, CommandFieldConstants.CMD_VERIFY_PIN);
        }
        else
        {
            nextView="allpay-web/case2CashStepOneInfo";
            httpServletRequest.setAttribute("ISRECBVS","1");
        }

        Boolean hasError = MfsWebUtil.isErrorXML(responseXML) || httpServletRequest.getAttribute("errors") != null;

        mfsWebResponseDataPopulator.populateMessage(httpServletRequest, responseXML);

        if (hasError) {
            populatePreviousScreen(httpServletRequest);
            nextView = checkError(httpServletRequest, responseXML);

        } else {

            UserDeviceAccountsModel userDeviceAccountsModel = (UserDeviceAccountsModel) httpServletRequest.getSession().getAttribute(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNTS_MODEL);
            ThreadLocalUserDeviceAccounts.setUserDeviceAccountsModel(userDeviceAccountsModel);

            ThreadLocalAppUser.setAppUserModel(appUserModel);

            httpServletRequest.addParameter(CommandFieldConstants.KEY_AGENT_MOB_NO, appUserModel.getMobileNo());
            String plainParameterPin = httpServletRequest.getParameter(CommandFieldConstants.KEY_ONE_TIME_PIN);

            if(null != plainParameterPin && !"".equals(plainParameterPin)) {
                httpServletRequest.addParameter(CommandFieldConstants.KEY_ONE_TIME_PIN, plainParameterPin/*EncryptionUtil.encryptWithAES(XMLConstants.AES_ENCRYPTION_KEY, plainParameterPin)*/);
            }
            httpServletRequest.addParameter(CommandFieldConstants.KEY_RECEIVER_CITY, iVo.getAreaName());
            responseXML = runCommand(httpServletRequest, httpServletResponse, model, exception);

            Boolean _hasError = MfsWebUtil.isErrorXML(responseXML) || httpServletRequest.getAttribute("errors") != null;

            if (_hasError) {

                nextView = checkError(httpServletRequest, responseXML);
            }
        }

        populateMessage(httpServletRequest, responseXML);

        modelAndView.setViewName(nextView);

        return modelAndView;
    }


    /**
     * @param httpServletRequest
     * @param responseXML
     */
    private void populateMessage(AllPayRequestWrapper httpServletRequest, String responseXML) {

        mfsWebResponseDataPopulator.populateMessage(httpServletRequest, responseXML);

        if (MfsWebUtil.isErrorXML(responseXML)) {

            mfsWebResponseDataPopulator.populateErrorMessages(httpServletRequest, responseXML);
        }
    }


    /**
     * @param requestWrapper
     * @param responseXML
     * @return
     * @throws Exception
     */
    private String checkError(AllPayRequestWrapper requestWrapper, String responseXML) throws Exception {

        mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXML);

        String errorType = getErrorType(requestWrapper);

        String viewName = getFormView();

        /*if (AllPayWebConstant.INVALID_BANK_PIN.getValue().equals(errorType)) {

            Integer STATUS = allPayWebResponseDataPopulator.isValidBankPinTryCount(requestWrapper);

            if (STATUS == allPayWebResponseDataPopulator.INVALID_BANK_PIN) {

                viewName = getFormView();

            } else if (STATUS == allPayWebResponseDataPopulator.BLOCKED_BANK_PIN) {

                viewName = AllPayWebConstant.GENERIC_PAGE_NO_FOOTER.getValue();
            }

        } else if (AllPayWebConstant.LIMIT_EXCEED.getValue().equals(errorType)) {

            viewName = AllPayWebConstant.GENERIC_PAGE.getValue();

        } else {

            viewName = AllPayWebConstant.GENERIC_PAGE.getValue();
        }*/

        requestWrapper.setAttribute(AllPayWebConstant.HEADING.getValue(), "Error(s)");

        populatePreviousScreen(requestWrapper);

        return viewName;
    }


    /**
     * @param requestWrapper
     * @return
     */
    protected String getErrorType(AllPayRequestWrapper requestWrapper) {

        String errorType = allPayWebResponseDataPopulator.getErrorType(requestWrapper);

        return errorType;
    }

    /**
     * @param httpServletRequest
     * @param httpServletResponse
     * @param model
     * @param exception
     * @return
     * @throws Exception
     */
    protected abstract String runCommand(AllPayRequestWrapper httpServletRequest, HttpServletResponse httpServletResponse, Object model, BindException exception) throws Exception;

    protected void populatePreviousScreen(AllPayRequestWrapper httpServletRequest) {
        final Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        for (String key : parameterMap.keySet()) {
            httpServletRequest.setAttribute(key, httpServletRequest.getParameter(key));
        }
    }

    private NadraIntegrationController getNadraIntegrationController() {
        return HttpInvokerUtil.getHttpInvokerFactoryBean(NadraIntegrationController.class,
                MessageUtil.getMessage("NadraIntegrationURL"));
    }

}