package com.inov8.microbank.webapp.action.userdeviceaccount;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.vo.account.SmartMoneyAccountVO;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inov8 on 5/31/2018.
 */
public class RemoveDormancyFormController extends AdvanceFormController {

    private MfsAccountManager mfsAccountManager;
    private AppUserManager appUserManager;
    private SmartMoneyAccountManager smartMoneyAccountManager ;
    private String appUserId;

    public RemoveDormancyFormController()
    {
        setCommandName("appUserModel");
        setCommandClass(AppUserModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
        ArrayList<String> list = new ArrayList();

        AppUserModel appUserModel1 = appUserManager.loadAppUser(Long.parseLong(appUserId));

        if(appUserModel1 != null && appUserModel1.getRegistrationStateId() != null && appUserModel1.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT))
            list.add("BLB");

        Long paymentModeId = PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
        SmartMoneyAccountModel smartMoneyAccountModel=smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel1,paymentModeId);

        if(smartMoneyAccountModel == null)
            smartMoneyAccountModel = smartMoneyAccountManager.getInActiveSMA(appUserModel1,paymentModeId,OlaStatusConstants.ACCOUNT_STATUS_IN_ACTIVE);

        if(smartMoneyAccountModel != null && smartMoneyAccountModel.getRegistrationStateId() != null && smartMoneyAccountModel.getRegistrationStateId().equals(RegistrationStateConstantsInterface.DORMANT))
            list.add("HRA");

        Map referenceDataMap = new HashMap();

        referenceDataMap.put("accountTypeList", list);

        return referenceDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        appUserId = ServletRequestUtils.getStringParameter(httpServletRequest,"appUserId");
        String customerId = ServletRequestUtils.getStringParameter(httpServletRequest,"customerId");
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = new AppUserModel();

        if (null != appUserId)
        {
            appUserModel.setAppUserId(Long.valueOf(EncryptionUtil.decryptForAppUserId( appUserId)));
            baseWrapper.setBasePersistableModel(appUserModel);
            baseWrapper = this.mfsAccountManager.searchAppUserByPrimaryKey(baseWrapper);
            appUserModel = (AppUserModel) baseWrapper.getBasePersistableModel();
        }

        if (null != customerId)
        {
            appUserModel.setCustomerId(Long.valueOf(customerId));
        }

        httpServletRequest.setAttribute("message", "form is loaded successfully");
        return appUserModel;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String error = null;
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = null;
        ModelAndView modelAndView = null;
        String authMessage = null;
        try{
            // getting parameters from request
            Long appUserId = new Long(ServletRequestUtils.getStringParameter(httpServletRequest, "appUserId"));
            String comments = ServletRequestUtils.getStringParameter(httpServletRequest, "closingComments");
            String acType= ServletRequestUtils.getStringParameter(httpServletRequest, "acType");
            Long paymentModeId=null;

            appUserModel=appUserManager.loadAppUser(appUserId);
            appUserModel.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
            appUserModel.setClosingComments(comments);
            appUserModel.setDormancyRemovedBy(UserUtils.getCurrentUser().getAppUserId());
            appUserModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
            appUserModel.setUpdatedOn(new Date());

            if(acType.equals("HRA"))
                paymentModeId= PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT;
            else
                paymentModeId= PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT;

            /*AppUserVO appUserVO = new AppUserVO();
            appUserVO.setMobileNo(appUserModel.getMobileNo());
            appUserVO.setUserName(appUserModel.getUsername());
            appUserVO.setAppUserId(appUserModel.getAppUserId().toString());

            if(null!=appUserModel.getAccountStateId()){
                appUserVO.setAccountState(appUserModel.getAccountStateId().toString());
            }*/

            SmartMoneyAccountModel smartMoneyAccountModel = smartMoneyAccountManager.loadSmartMoneyAccountModel(appUserModel,paymentModeId);
            if(smartMoneyAccountModel == null && paymentModeId == PaymentModeConstantsInterface.HOME_REMMITTANCE_ACCOUNT ) {
                //return super.showForm(httpServletRequest, httpServletResponse, e);
                throw new FrameworkCheckedException("notExists");
            }
            if(paymentModeId == PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT)
            {
                smartMoneyAccountModel.setRegistrationStateId(appUserModel.getRegistrationStateId());
                smartMoneyAccountModel.setPreviousRegStateId(appUserModel.getPrevRegistrationStateId());
            }
            SmartMoneyAccountVO smartMoneyAccountVO = this.convertModelToVO(smartMoneyAccountModel);
            //
            /*baseWrapper.putObject("paymentModeId",paymentModeId);
            baseWrapper.putObject("smartMoneyAccountModel",smartMoneyAccountModel);*/
            //
            if(paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
                baseWrapper.putObject("appUserModel", appUserModel);
            baseWrapper.putObject("useCaseId", PortalConstants.RESTORE_FROM_DORMANCY_USECASE_ID);
            baseWrapper.setBasePersistableModel(smartMoneyAccountVO);
            this.populateAuthenticationParams(baseWrapper, httpServletRequest, smartMoneyAccountVO);
            baseWrapper = smartMoneyAccountManager.updateSmartMoneyAccountDormancyWithAuthorization(baseWrapper);
            authMessage = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
            if(authMessage != null && !authMessage.equals(""))
                throw new FrameworkCheckedException("newRequest");
            //appUserManager.updateAppUserWithAuthorization(baseWrapper);

        }
        catch(Exception exp){
            exp.printStackTrace();
            if(exp != null){
                if(exp.getMessage().contains("newRequest")){
                    httpServletRequest.setAttribute("message",authMessage);
                }
                else if(exp.getMessage().contains("Action authorization request already exist with  Action ID")){
                    httpServletRequest.setAttribute("message",exp.getMessage());
                    // super.saveMessage(httpServletRequest,"authorization request already exist with  Action ID");
                }
                else if("notExists".equalsIgnoreCase(exp.getMessage()))
                {
                    httpServletRequest.setAttribute("message","HRA Account does not exist");
                    //super.saveMessage(httpServletRequest,"HRA Account does not exist");
                }
                else{
                    //httpServletRequest.setAttribute("message","An error has occurered. Please try again later");
                    super.saveMessage(httpServletRequest,"An error has occurered. Please try again later");
                }
            }else{
                //httpServletRequest.setAttribute("message","An error has occurered. Please try again later");
                super.saveMessage(httpServletRequest,"An error has occurered. Please try again later");
            }
            httpServletRequest.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(httpServletRequest, httpServletResponse, e);
        }

        /*String msg = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG);
        if (null == msg) {
            buffer.append(MessageUtil.getMessage("genericUpdateSuccessMessage"));
        }else{
            buffer.append(msg.toString());
        }*/
        Map<String, String> map = new HashMap<String, String>();
        map.put("message","success");
        httpServletRequest.setAttribute("status", "success");
        modelAndView = new ModelAndView(this.getSuccessView()+"?status=success&appUserId="+appUserModel.getAppUserId(),map);
        return modelAndView;
    }

    @Override
    protected void populateAuthenticationParams(BaseWrapper baseWrapper, HttpServletRequest req, Object model) throws FrameworkCheckedException {

        SmartMoneyAccountVO smartMoneyAccountVO = (SmartMoneyAccountVO) model;
        try {
            Long appUserId = new Long(ServletRequestUtils.getStringParameter(req, "appUserId"));
            smartMoneyAccountVO.setAppUserId(appUserId);
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        Long actionAuthorizationId =null;
        SmartMoneyAccountVO  oldsmartMoneyAccountVO = new SmartMoneyAccountVO();

        DateFormat df = new SimpleDateFormat(PortalDateUtils.SHORT_DATE_FORMAT_2);
        mapper.setDateFormat(df);

        String modelJsonString = null;
        String initialModelJsonString = null;

        try {
            oldsmartMoneyAccountVO.setStatusId(smartMoneyAccountVO.getStatusId());
            oldsmartMoneyAccountVO.setDormancyUpdatedOn(smartMoneyAccountVO.getDormancyUpdatedOn());
            oldsmartMoneyAccountVO.setSmartMoneyAccountId(smartMoneyAccountVO.getSmartMoneyAccountId());

            modelJsonString = mapper.writeValueAsString(smartMoneyAccountVO);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException(MessageUtil.getMessage(ActionAuthorizationConstantsInterface.REF_DATA_EXCEPTION_MSG));
        }

        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, initialModelJsonString);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, "updateSmartMoneyAccountDormancyWithAuthorization");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, SmartMoneyAccountVO.class.getSimpleName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, SmartMoneyAccountVO.class.getName());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, modelJsonString);
        baseWrapper.putObject("managerName","smartMoneyAccountManager");
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID,smartMoneyAccountVO.getSmartMoneyAccountId().toString());


        baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, baseWrapper.getObject("useCaseId"));
        baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, this.getFormView());
    }
    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    public void setAppUserManager(AppUserManager appUserManager) {
        this.appUserManager = appUserManager;
    }

    public void setSmartMoneyAccountManager(SmartMoneyAccountManager smartMoneyAccountManager) {
        this.smartMoneyAccountManager = smartMoneyAccountManager;
    }

    private SmartMoneyAccountVO convertModelToVO(SmartMoneyAccountModel smartMoneyAccountModel)
    {
        SmartMoneyAccountVO smartMoneyAccountVO = new SmartMoneyAccountVO();
        //
        smartMoneyAccountVO.setSmartMoneyAccountId(smartMoneyAccountModel.getSmartMoneyAccountId());
        smartMoneyAccountVO.setBankId(smartMoneyAccountModel.getRelationBankIdBankModel().getBankId());
        smartMoneyAccountVO.setPaymentModeId(smartMoneyAccountModel.getPaymentModeIdPaymentModeModel().getPaymentModeId());
        smartMoneyAccountVO.setCustomerId(smartMoneyAccountModel.getCustomerIdCustomerModel().getCustomerId());
        smartMoneyAccountVO.setCreatedByAppUserId(smartMoneyAccountModel.getCreatedByAppUserModel().getAppUserId());
        smartMoneyAccountVO.setUpdatedByAppUserId(UserUtils.getCurrentUser().getAppUserId());

        smartMoneyAccountVO.setName(smartMoneyAccountModel.getName());
        smartMoneyAccountVO.setStatusId(OlaStatusConstants.ACCOUNT_STATUS_ACTIVE);
        smartMoneyAccountVO.setActive(true);
        smartMoneyAccountVO.setDefAccount(smartMoneyAccountModel.getDefAccount());
        smartMoneyAccountVO.setChangePinRequired(smartMoneyAccountModel.getChangePinRequired());
        smartMoneyAccountVO.setDormancyUpdatedOn(new Date());
        smartMoneyAccountVO.setCREATED_ON(smartMoneyAccountModel.getCreatedOn());
        smartMoneyAccountVO.setUpdatedOn(new Date());
        smartMoneyAccountVO.setVersionNo(smartMoneyAccountModel.getVersionNo());
        smartMoneyAccountVO.setDeleted(smartMoneyAccountModel.getDeleted());
        smartMoneyAccountVO.setPrevRegistrationStateId(smartMoneyAccountModel.getRegistrationStateId());
        smartMoneyAccountVO.setRegistrationStateId(smartMoneyAccountModel.getPreviousRegStateId());
        smartMoneyAccountVO.setAccountStateId(AccountStateConstantsInterface.ACCOUNT_STATE_COLD);
        return smartMoneyAccountVO;
    }
}
