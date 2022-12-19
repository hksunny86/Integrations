package com.inov8.microbank.account.controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.account.vo.DebitBlockVo;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.service.smartmoneymodule.SmartMoneyAccountManager;
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

public class DebitBlockController extends AdvanceFormController {

    private SmartMoneyAccountManager smartMoneyAccountManager;
    private ActionAuthorizationFacade actionAuthorizationFacade;

    public DebitBlockController() {
        setCommandName("debitBlockVo");
        setCommandClass(DebitBlockVo.class);
    }


    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        return null;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

        if (escalateRequest || resolveRequest) {
            ObjectMapper mapper = new ObjectMapper();
            String modelJsonString = actionAuthorizationFacade.loadAuthorizationVOJson(request);
            DebitBlockVo debitBlockRefData = mapper.readValue(modelJsonString,DebitBlockVo.class);

            request.setAttribute("mfsId", debitBlockRefData.getMfsId());
            request.setAttribute("isAgent", debitBlockRefData.getIsAgent());
            request.setAttribute("mobileNo", debitBlockRefData.getMobileNo());
            request.setAttribute("debitBlockAmount", debitBlockRefData.getAmount());
            return debitBlockRefData;
        } else
        {
            DebitBlockVo debitBlockVo = new DebitBlockVo();
            String strAgent = ServletRequestUtils.getStringParameter(request, "isAgent");
            debitBlockVo.setIsAgent(strAgent == null ? false : Boolean.parseBoolean(strAgent));
            return debitBlockVo ;
        }
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws Exception {
        ModelAndView modelAndView = null;
        Long appUserId = null;
        String encryptedAppUserId;
        DebitBlockVo debitBlockVo = (DebitBlockVo) o;
        BaseWrapper baseWrapper=new BaseWrapperImpl();

        Boolean isDebitBlock = ServletRequestUtils.getRequiredBooleanParameter(request, "isDebitBlocked");


        try {
            encryptedAppUserId=debitBlockVo.getEncryptedAppUserId();
            appUserId = new Long(encryptedAppUserId);;
            debitBlockVo.setAppUserId(appUserId);
            if (null != appUserId) {

                debitBlockVo.setAppUserId(appUserId);
                debitBlockVo.setIsDebitBlocked(isDebitBlock);
                baseWrapper.setBasePersistableModel(debitBlockVo);
                populateAuthenticationParams(baseWrapper, request, debitBlockVo);
                baseWrapper = this.smartMoneyAccountManager.updateDebitBlockWithAuthorization(baseWrapper);

            }

        } catch (FrameworkCheckedException ex) {
            request.setAttribute("message", ex.getMessage());
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, e);
        }

        Map<String, String> map = new HashMap<String, String>();
        String message = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
        if(StringUtil.isNullOrEmpty(message)){
            if(!debitBlockVo.getIsDebitBlocked()){
                map.put("message", "Record updated sucessfully. You have been debit unblocked by "+ debitBlockVo.getNoOfMonths() + " Months");
            }
            else {
                map.put("message", "Record updated sucessfully");
            }
        }else{
            map.put("message", message);
        }
        map.put("status", IssueTypeStatusConstantsInterface.SUCCESS);

        modelAndView = new ModelAndView(this.getSuccessView(), map);
        return modelAndView;
    }

    @Override
    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {

        DebitBlockVo debitBlockVo = (DebitBlockVo) model;
        ObjectMapper mapper = new ObjectMapper();
        Long actionAuthorizationId =null;
        Long usecaseId = null;
        try
        {
            actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
            usecaseId = ServletRequestUtils.getLongParameter(req, "usecaseId");
        } catch (ServletRequestBindingException e1) {
            e1.printStackTrace();
        }

        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
        mapper.setDateFormat(df);

        String modelJsonString = null;
        try {

            modelJsonString = mapper.writeValueAsString(debitBlockVo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }


        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, MessageUtil.getMessage("DebitBlockVo.methodName"));
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, DebitBlockVo.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, DebitBlockVo.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME, MessageUtil.getMessage("DebitBlockVo.Manager"));
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID, debitBlockVo.getEncryptedAppUserId());

        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,usecaseId);
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, this.getFormView());

    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    public void setActionAuthorizationFacade(ActionAuthorizationFacade actionAuthorizationFacade) {
        this.actionAuthorizationFacade = actionAuthorizationFacade;
    }
}
