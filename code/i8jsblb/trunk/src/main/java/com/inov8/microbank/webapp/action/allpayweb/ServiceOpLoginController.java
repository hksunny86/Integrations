package com.inov8.microbank.webapp.action.allpayweb;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.mfsweb.MfsWebManager;
import com.inov8.microbank.mfsweb.MfsWebResponseDataPopulator;
import com.inov8.microbank.server.dao.mfsmodule.UserDeviceAccountsDAO;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;
import com.inov8.microbank.webapp.action.allpayweb.formbean.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;
import java.util.List;
import java.util.Map;

/**
 * Created by Attique on 8/16/2018.
 */
public class ServiceOpLoginController extends AdvanceFormController {

    private final static Logger logger = Logger.getLogger(ServiceOpLoginController.class);

    private static final String  LOGIN_STATUS = "LOGIN_STATUS";
    private static final Integer PIN_CHANGE_REQUIRED = 0;
    private static final Integer PASSWORD_CHANGE_REQUIRED = 1;
    private static final Integer INVALID_CREDENTIAL = 2;
    private static final Integer INVALID_REQUEST_ERROR = 3;
    private static final Integer AC_NOT_LINKED = 4;
    private static final Integer SUCCESS_CASE = 5;
    private static final Integer AC_DELETED = 6;
    private static final Integer TBR_REQUIRED = 7;

    private MfsWebManager mfsWebController;
    private MfsWebResponseDataPopulator mfsWebResponseDataPopulator;
    private SmartMoneyAccountDAO smartMoneyAccountDAO;
    private UserDeviceAccountsDAO userDeviceAccountsDAO;
    private AllPayWebResponseDataPopulator allPayWebResponseDataPopulator;

    @Autowired
    private AccountControlManager accountControlManager;

    @Autowired
    private ParserService parserService;

    public ServiceOpLoginController (){
        setCommandName("object");
        setCommandClass(Object.class);
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest arg0) throws Exception {
        return new Object();
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);
        AllPayWebResponseDataPopulator.setDefaultParams(requestWrapper);

        return null;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {

        AllPayRequestWrapper requestWrapper = new AllPayRequestWrapper(request);

        ModelAndView modelAndView = new ModelAndView(getFormView());

        StringBuilder responseXml = new StringBuilder();

        Boolean viewDefined = Boolean.FALSE;

        Integer STATUS = SUCCESS_CASE;


        String UID = requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID);
        Boolean isTillBalanceRequired = Boolean.FALSE;

        AppUserModel appUserModel = allPayWebResponseDataPopulator.getAppUserModel(UID);
        ThreadLocalAppUser.setAppUserModel(appUserModel);

        if((!viewDefined)) {

            STATUS = isValidRequest(requestWrapper);

            if (STATUS == INVALID_REQUEST_ERROR) {

                viewDefined = Boolean.TRUE;
                logger.info("INVALID_REQUEST_ERROR");

                //modelAndView.setViewName(getViewName());
            }
        }

        if((!viewDefined)) {

            STATUS = initCommand(requestWrapper, responseXml);

            if (STATUS == INVALID_CREDENTIAL) {

                viewDefined = Boolean.TRUE;
                logger.info("INVALID_CREDENTIAL");
            }

            if (STATUS == TBR_REQUIRED) {

                isTillBalanceRequired = Boolean.TRUE;
                logger.info("TBR_REQUIRED");
            }





        }

        if((!viewDefined)) {
            if (appUserModel.getPasswordChangeRequired()) {

                viewDefined = Boolean.TRUE;
                request.setAttribute("PASSWORD_CHANGE_REQUIRED", true);
                modelAndView.setViewName("allpay-web/awChangepasswordform");
            }


        }

        UserDeviceAccountsModel userDeviceAccountsModel = null;

        if((!viewDefined)) {

            userDeviceAccountsModel = this.getUserDeviceAccountsModel(UID);
            viewDefined = AllPayWebUtil.checkUserIsInWrongState(userDeviceAccountsModel, requestWrapper, appUserModel, accountControlManager);
            requestWrapper.getSession().setAttribute(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNTS_MODEL, userDeviceAccountsModel);

			/* Useless check
			STATUS = isLinked(userDeviceAccountsModel, requestWrapper);

			if (STATUS == AC_DELETED || STATUS == AC_NOT_LINKED) {

				viewDefined = Boolean.TRUE;
			}
			logger.info((STATUS==AC_DELETED)? "AC_DELETED":"");
			logger.info((STATUS==AC_NOT_LINKED)? "AC_NOT_LINKED":"");
			*/
        }

        if((!viewDefined)) {

            if (isTillBalanceRequired) {

                viewDefined = Boolean.TRUE;
                modelAndView.setViewName("allpay-web/tillBalance");
                logger.info("TBR_REQUIRED");
            }
        }

        if((!viewDefined)) {

            STATUS = isPinChangeRequired(responseXml.toString(), requestWrapper);
//			STATUS = isPinChangeRequired(userDeviceAccountsModel, requestWrapper);

            if (STATUS == PIN_CHANGE_REQUIRED) {

                viewDefined = Boolean.TRUE;
                modelAndView.setViewName("allpay-web/changePin");
                request.setAttribute("PIN_CHANGE_REQUIRED", Boolean.TRUE);
                logger.info("PIN_CHANGE_REQUIRED");
            }
        }


        if((!viewDefined)) {

            if (STATUS == SUCCESS_CASE) {

                modelAndView.setViewName(getSuccessView());
                logger.info("SUCCESS_CASE");
            }
        }

        if(STATUS == SUCCESS_CASE || STATUS == PIN_CHANGE_REQUIRED || STATUS == TBR_REQUIRED || isTillBalanceRequired || appUserModel.getPasswordChangeRequired()) {

            requestWrapper.setAttribute(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALLPAY_WEB);
            mfsWebResponseDataPopulator.populateAllPayLoginResponse(requestWrapper, responseXml.toString());
            setSessionData(requestWrapper, appUserModel);

            HttpSession httpSession = request.getSession();
            httpSession = setAppUserModelToThreadLocalFromSession(httpSession);
            final String catalogXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_UPD_CAT);
            httpSession = setAppUserModelToThreadLocalFromSession(httpSession);
            final List<Category> categories = this.parserService.parseCateogry(catalogXml);
            //remove unimplemented transaction for time being from agentWeb by turab 11-27-2017
            categories.remove(new Category(19));
            categories.remove(new Category(4));
            categories.remove(new Category(17));
            categories.remove(new Category(2));
            categories.remove(new Category(1));
            categories.remove(new Category(21));
            //end of remove unimplemented transaction for time being from agentWeb by turab 11-27-2017

            requestWrapper.getSession().setAttribute("categories", categories);
        }

        logger.info("*************************************************************");
        logger.info("Agent Web Login for appUserModel = " + appUserModel);
        logger.info("Status = " + STATUS);
        logger.info("Next View = " + modelAndView.getViewName());
        logger.info("*************************************************************");

        return modelAndView;
    }


    /**
     * @param requestWrapper
     * @param _responseXml
     * @return
     */
    private Integer initCommand(AllPayRequestWrapper requestWrapper, StringBuilder _responseXml) {

        Integer STATUS = SUCCESS_CASE;

        try {

            String responseXml = mfsWebController.handleRequest(requestWrapper, CommandFieldConstants.CMD_ALL_PAY_LOGIN);

            _responseXml.append(responseXml);

            if( AllPayWebUtil.isErrorXML(responseXml)) {

                STATUS = INVALID_CREDENTIAL;

                mfsWebResponseDataPopulator.populateErrorMessages(requestWrapper, responseXml);
            }

            if(! AllPayWebUtil.isValidBankAccount(responseXml)) {

                requestWrapper.setAttribute("errors", "Your account might not be linked, deactivated or deleted. Kindly contact your service provider.");

                STATUS = AC_NOT_LINKED;
            }

            if(STATUS == SUCCESS_CASE) {

                Boolean isTllBalanceRequired = isTllBalanceRequired(responseXml);

                if(isTllBalanceRequired) {
                    STATUS = TBR_REQUIRED;
                }
            }

        } catch (Exception e) {

            logger.equals(e);
        }

        return STATUS;
    }


    /**
     * @param xmlData
     * @return
     */
    private Boolean isTllBalanceRequired(String xmlData) {

        String tbrValue = "0";

        try {

            tbrValue = MiniXMLUtil.getTagTextValue(xmlData, "/msg/params/param");

        } catch (XPathExpressionException e) {

            logger.equals(e);
        }

        return (tbrValue.equals("1") ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see com.inov8.framework.webapp.action.AdvanceFormController#onUpdate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object model, BindException exception) throws Exception {
        return onCreate(request, response, model, exception);
    }


    /**
     * @param UIDa
     * @return
     */
/*	private Integer isLinked(UserDeviceAccountsModel userDeviceAccountsModel, HttpServletRequest requestWrapper) throws FrameworkCheckedException {

		Integer status = SUCCESS_CASE;

		if(userDeviceAccountsModel != null) {

			AppUserModel appUserIdAppUserModel = userDeviceAccountsModel.getAppUserIdAppUserModel();

			if(appUserIdAppUserModel != null && UserTypeConstantsInterface.RETAILER.longValue() == appUserIdAppUserModel.getAppUserTypeId()) {

				RetailerContactModel retailerContactModel = appUserIdAppUserModel.getRetailerContactIdRetailerContactModel();
				Long retailerContactId = retailerContactModel.getRetailerContactId();

				SmartMoneyAccountModel exampleInstance = new SmartMoneyAccountModel();
				exampleInstance.setRetailerContactId(retailerContactId);
				exampleInstance.setDeleted(Boolean.FALSE);

				CustomList<SmartMoneyAccountModel> _customList = smartMoneyAccountDAO.findByExample(exampleInstance, null);

				if(_customList != null && _customList.getResultsetList()!= null && !_customList.getResultsetList().isEmpty()) {

					exampleInstance = _customList.getResultsetList().get(0);

					if(exampleInstance != null && !exampleInstance.getActive()) {
						status = AC_NOT_LINKED;
						requestWrapper.setAttribute("errors", "You do not have any Core Banking Account Linked.");
					} else {
						requestWrapper.getSession().setAttribute(CommandFieldConstants.KEY_USER_DEVICE_ACCOUNTS_MODEL, userDeviceAccountsModel);
					}
				} else {
					status = AC_DELETED;
					requestWrapper.setAttribute("errors", "Your Account has been Deleted.");
				}
			}
		}

		return status;
	}
*/

    /**
     * @param responseXml
     * @param requestWrapper
     * @return
     */
    private Integer isPasswordChangeRequired(String responseXml, AllPayRequestWrapper requestWrapper) {

        if(AllPayWebUtil.isPasswordChangeRequired(responseXml)) {

            requestWrapper.setAttribute("oldPinLabel", "Please enter Password to be changed for the first time:");
            setSessionData(requestWrapper, ThreadLocalAppUser.getAppUserModel());

            return PASSWORD_CHANGE_REQUIRED;
        }

        return SUCCESS_CASE;
    }


    /**
     * @param responseXml
     * @param requestWrapper
     * @return
     */
    private Integer isPinChangeRequired(String responseXml, AllPayRequestWrapper requestWrapper) {

        if(AllPayWebUtil.isPinChangeRequired(responseXml)) {

            requestWrapper.setAttribute("oldPinLabel", "Please enter Password to be changed for the first time:");

            requestWrapper.getSession().setAttribute(AllPayWebConstant.PIN_CHANGE_REQUIRED.getValue(), Boolean.TRUE );
            requestWrapper.getSession().setAttribute( "message", "Please change your PIN for the first time." );
            requestWrapper.getSession().setAttribute( "APID", requestWrapper.getParameter("UID") );

            return PIN_CHANGE_REQUIRED;
        }

        return SUCCESS_CASE;
    }


    /**
     * @param userDeviceAccountsModel
     * @param requestWrapper
     * @return
     */
    private Integer isPinChangeRequired(UserDeviceAccountsModel userDeviceAccountsModel, AllPayRequestWrapper requestWrapper) {

        if(userDeviceAccountsModel != null && userDeviceAccountsModel.getPinChangeRequired()) {

            requestWrapper.setAttribute("oldPinLabel", "Please enter Password to be changed for the first time:");

            requestWrapper.getSession().setAttribute(AllPayWebConstant.PIN_CHANGE_REQUIRED.getValue(), Boolean.TRUE );
            requestWrapper.getSession().setAttribute( "message", "Please change your PIN for the first time." );
            requestWrapper.getSession().setAttribute( "APID", requestWrapper.getParameter(CommandFieldConstants.KEY_U_ID));

            return PIN_CHANGE_REQUIRED;
        }

        return SUCCESS_CASE;
    }


    /**
     * @param request
     */
    public void setSessionData(HttpServletRequest request, AppUserModel appUserModel) {

        logger.info("setSessionData HttpServletRequest ...");

        String ACID = request.getParameter(CommandFieldConstants.KEY_ACC_ID);
        String BAID = request.getParameter(CommandFieldConstants.KEY_BANK_ID);
        String UID = request.getParameter(CommandFieldConstants.KEY_U_ID);

        if(!AllPayWebResponseDataPopulator.isValidString(ACID)) {
            ACID = (String) request.getAttribute(CommandFieldConstants.KEY_ACC_ID);
        }
        if(!AllPayWebResponseDataPopulator.isValidString(BAID)) {

            Long _BAID = (Long) request.getAttribute(CommandFieldConstants.KEY_BANK_ID);

            if(_BAID != null) {
                BAID = String.valueOf(_BAID);
            }
        }
        if(!AllPayWebResponseDataPopulator.isValidString(UID)) {
            UID = (String) request.getAttribute(CommandFieldConstants.KEY_U_ID);
        }

        logger.info(CommandFieldConstants.KEY_ACC_ID +" = " +ACID);
        logger.info(CommandFieldConstants.KEY_BANK_ID +" = " +BAID);
        logger.info(CommandFieldConstants.KEY_U_ID +" = " +UID);
        logger.info(CommandFieldConstants.KEY_ALLPAY_ID +" = " +UID);

        HttpSession httpSession = request.getSession();

        httpSession.setAttribute(CommandFieldConstants.KEY_ACC_ID, ACID);
        httpSession.setAttribute(CommandFieldConstants.KEY_BANK_ID, BAID);
        httpSession.setAttribute(CommandFieldConstants.KEY_U_ID, UID);
        httpSession.setAttribute(CommandFieldConstants.KEY_ALLPAY_ID, UID);

        httpSession.setAttribute(CommandFieldConstants.KEY_APP_USER, appUserModel);

        httpSession.setAttribute("USTY", request.getAttribute("USTY")) ;
    }


    /**
     * @param UID
     * @return
     */
    private UserDeviceAccountsModel getUserDeviceAccountsModel(String UID) {

        UserDeviceAccountsModel example = new UserDeviceAccountsModel();
        example.setUserId(UID);

        CustomList<UserDeviceAccountsModel> customList = userDeviceAccountsDAO.findByExample(example,null,null,PortalConstants.EXACT_CONFIG_HOLDER_MODEL);

        UserDeviceAccountsModel userDeviceAccountsModel = null;

        if(customList != null && customList.getResultsetList()!= null && !customList.getResultsetList().isEmpty()) {

            userDeviceAccountsModel = customList.getResultsetList().get(0);
        }

        return userDeviceAccountsModel;
    }


    /**
     * @param requestWrapper
     * @return
     * @desc In future, this method should move in AllPayWebLoginFilter
     */
    public Integer isValidRequest(HttpServletRequest requestWrapper) {


        Integer STATUS_CASE = SUCCESS_CASE;

        if(requestWrapper == null){
            logger.info("AW isValidRequest" );
            STATUS_CASE = INVALID_REQUEST_ERROR;
            return STATUS_CASE;
        }

        HttpSession httpSession = requestWrapper.getSession(Boolean.FALSE);

        String agentLastName = (String) httpSession.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);

        SessionInformation sessionInformation = getSessionRegistry().getSessionInformation(httpSession.getId());

        if(sessionInformation == null || sessionInformation.isExpired() || !AllPayWebResponseDataPopulator.isValidString(agentLastName)) {

            requestWrapper.setAttribute("errors", "Portal Session has been Expired, Please re-login on Portal");
            httpSession.setAttribute(CommandFieldConstants.KEY_U_ID, null);
            STATUS_CASE = INVALID_REQUEST_ERROR;

            logger.info("AW isValidRequest("+agentLastName+") sessionInformation = "+ (sessionInformation==null?null:sessionInformation.isExpired()));
        }

        return STATUS_CASE;
    }


    /**
     * @return SessionRegistryImpl
     * @desc In future, this method should move in AllPayWebLoginFilter
     */
    private SessionRegistryImpl sessionRegistry = null;

    public SessionRegistryImpl getSessionRegistry() {

        if(sessionRegistry == null) {

            sessionRegistry = (SessionRegistryImpl) allPayWebResponseDataPopulator.getBean("sessionRegistry");
        }

        return sessionRegistry;
    }


    public MfsWebManager getMfsWebController() {
        return mfsWebController;
    }

    public void setMfsWebController(MfsWebManager mfsWebController) {
        this.mfsWebController = mfsWebController;
    }
    public MfsWebResponseDataPopulator getMfsWebResponseDataPopulator() {
        return mfsWebResponseDataPopulator;
    }
    public void setMfsWebResponseDataPopulator(
            MfsWebResponseDataPopulator mfsWebResponseDataPopulator) {
        this.mfsWebResponseDataPopulator = mfsWebResponseDataPopulator;
    }

    public void setSmartMoneyAccountDAO(SmartMoneyAccountDAO smartMoneyAccountDAO) {
        this.smartMoneyAccountDAO = smartMoneyAccountDAO;
    }
    public void setUserDeviceAccountsDAO(UserDeviceAccountsDAO userDeviceAccountsDAO) {
        this.userDeviceAccountsDAO = userDeviceAccountsDAO;
    }

    public void setAllPayWebResponseDataPopulator(AllPayWebResponseDataPopulator allPayWebResponseDataPopulator) {
        this.allPayWebResponseDataPopulator = allPayWebResponseDataPopulator;
    }

    public void setParserService(ParserService parserService)
    {
        this.parserService = parserService;
    }

    private HttpSession setAppUserModelToThreadLocalFromSession(HttpSession httpSession)
    {

        ThreadLocalAppUser.setAppUserModel((AppUserModel) httpSession.getAttribute(CommandFieldConstants.KEY_APP_USER));
        return httpSession;
    }
}
