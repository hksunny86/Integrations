package com.inov8.microbank.account.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.account.service.AccountControlManager;
import com.inov8.microbank.account.vo.BlacklistMarkingVo;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.service.bulkdisbursements.CustomerPendingTrxManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Malik on 8/22/2016.
 */
public class MarkUnmarkBlacklistedController extends AdvanceFormController
{

        private AccountControlManager accountControlManager;
        private ActionAuthorizationFacade actionAuthorizationFacade;
    	private MfsAccountManager	mfsAccountManager;
        private AppUserManager appUserManager;
    	private CustomerPendingTrxManager	customerPendingTrxManager;

        public MarkUnmarkBlacklistedController() {
            setCommandName("blacklistMarkingVo");
            setCommandClass(BlacklistMarkingVo.class);
        }

        @Override
        protected Map loadReferenceData(HttpServletRequest request)
                throws Exception {
            return null;
        }

        @Override
        protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

            boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
            boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

                if (escalateRequest || resolveRequest) {
                    ObjectMapper mapper = new ObjectMapper();
                    String modelJsonString = actionAuthorizationFacade.loadAuthorizationVOJson(request);
                    BlacklistMarkingVo activateDeactivateMfsAccountRefDataModel = mapper.readValue(modelJsonString,BlacklistMarkingVo.class);

                    request.setAttribute("mfsId", activateDeactivateMfsAccountRefDataModel.getMfsId());
                    request.setAttribute("isAgent", activateDeactivateMfsAccountRefDataModel.getIsAgent());
                    request.setAttribute("blacklisted", activateDeactivateMfsAccountRefDataModel.getBlacklisted());
                    return activateDeactivateMfsAccountRefDataModel;
                } else
                return new BlacklistMarkingVo();
        }


        @Override
        protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
                                        BindException bindException) throws Exception {
            return null;
        }

        @Override
        protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
                                        BindException errors) throws Exception {
            ModelAndView modelAndView = null;
            Long appUserId = null;
            String encryptedAppUserId;
            BlacklistMarkingVo blacklistMarkingVo = (BlacklistMarkingVo) object;
            BaseWrapper baseWrapper=new BaseWrapperImpl();

            try {
                encryptedAppUserId=blacklistMarkingVo.getEncryptedAppUserId();
                appUserId = new Long(EncryptionUtil.decryptForAppUserId( encryptedAppUserId));
                blacklistMarkingVo.setAppUserId(appUserId);
                if (null != appUserId) {

                    baseWrapper.setBasePersistableModel(blacklistMarkingVo);

                    populateAuthenticationParams(baseWrapper, request, blacklistMarkingVo);
                    baseWrapper = this.accountControlManager.markUnmarkBlacklistedWithAuthorization(baseWrapper);

                }
            } catch (FrameworkCheckedException ex) {
                request.setAttribute("message", ex.getMessage());
                request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
                return super.showForm(request, response, errors);
            }

            Map<String, String> map = new HashMap<String, String>();
            String message = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
            if(StringUtil.isNullOrEmpty(message)){
                map.put("message", MessageUtil.getMessage("genericUpdateSuccessMessage"));
            }else{
                map.put("message", message);
            }
            map.put("status", IssueTypeStatusConstantsInterface.SUCCESS);

            modelAndView = new ModelAndView(this.getSuccessView(), map);
            return modelAndView;
        }

    
        @Override
        protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {

            BlacklistMarkingVo blacklistMarkingVo = (BlacklistMarkingVo) model;
            ObjectMapper mapper = new ObjectMapper();
            Long actionAuthorizationId =null;
            try
            {
                actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
            } catch (ServletRequestBindingException e1) {
                e1.printStackTrace();
            }

            DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
            mapper.setDateFormat(df);

            String modelJsonString = null;
            try {

                modelJsonString = mapper.writeValueAsString(blacklistMarkingVo);
            } catch (IOException e) {
                e.printStackTrace();
                throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
            }


            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, MessageUtil.getMessage("BlacklistMarkingVo.methodName"));
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, BlacklistMarkingVo.class.getSimpleName());
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, BlacklistMarkingVo.class.getName());
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME, MessageUtil.getMessage("BlacklistMarkingVo.Manager"));
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID, blacklistMarkingVo.getEncryptedAppUserId());

            baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,blacklistMarkingVo.getUsecaseId() );
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_INITIATOR_COMMENTS,blacklistMarkingVo.getComments());
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, this.getFormView());

        }

    public void setAccountControlManager(AccountControlManager accountControlManager)
        {
            this.accountControlManager = accountControlManager;
        }

		public void setAppUserManager(AppUserManager appUserManager) {
			this.appUserManager = appUserManager;
		}

		public void setCustomerPendingTrxManager(
				CustomerPendingTrxManager customerPendingTrxManager) {
			this.customerPendingTrxManager = customerPendingTrxManager;
		}

		public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
			this.mfsAccountManager = mfsAccountManager;
		}
        
        
}
