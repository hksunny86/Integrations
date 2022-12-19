package com.inov8.microbank.updatecustomername.Controller;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.updatecustomername.dao.UpdateCustomerNameDAO;
import com.inov8.microbank.updatecustomername.facade.UpdateCustomerNameFacade;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import com.inov8.microbank.updatecustomername.vo.UpdateCustomerNameVo;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class UpdateCustomerNameDetailController extends AdvanceAuthorizationFormController {
    UpdateCustomerNameFacade updateCustomerNameFacade;
    private String cnic;
    private MessageSource messageSource;
    private CustTransManager custTransManager;


    public UpdateCustomerNameDetailController() {
        setCommandName("updateCustomerNameVo");
        setCommandClass(UpdateCustomerNameVo.class);
    }

    @Override
    protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        UpdateCustomerNameVo updateCustomerNameVo = (UpdateCustomerNameVo) command;
        updateCustomerNameVo.setCnic(cnic);
        ModelAndView modelAndView = null;
        boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest", false);
        Long actionAuthorizationId = null;
        if (resubmitRequest)
            actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
        try {

            XStream xstream = new XStream();

            String refDataModelString = xstream.toXML(updateCustomerNameVo);

            UsecaseModel usecaseModels = usecaseFacade.loadUsecase(new Long(PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID));

            Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(new Long(PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID), new Long(0));

            if (nextAuthorizationLevel.intValue() < 1) {
                Long appUserId = ServletRequestUtils.getLongParameter(request, "appUserId");
                AppUserModel appUserModel = getCommonCommandManager().getAppUserManager().loadAppUser(appUserId);
                if (appUserModel != null) {
                    CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());

                    BaseWrapper baseWrapper = new BaseWrapperImpl();
                    appUserModel.setFirstName(updateCustomerNameVo.getFirstName());
                    appUserModel.setLastName(updateCustomerNameVo.getLastName());
                    appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    appUserModel.setUpdatedOn(new Date());
                    baseWrapper.setBasePersistableModel(appUserModel);
                    getCommonCommandManager().getAppUserManager().saveOrUpdateAppUser(baseWrapper);
                    baseWrapper = new BaseWrapperImpl();
                    customerModel.setName(updateCustomerNameVo.getFirstName() + " " + updateCustomerNameVo.getLastName());
                    customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
                    customerModel.setUpdatedOn(new Date());
                    baseWrapper.setBasePersistableModel(customerModel);

                    baseWrapper = new BaseWrapperImpl();
                    UpdateCustomerNameModel updateCustomerNameModel = new UpdateCustomerNameModel();
                    updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(updateCustomerNameVo.getCnic());

                    updateCustomerNameModel.setUpdated(true);

                    updateCustomerNameModel.setUpdatedOn(new Date());
                    updateCustomerNameModel.setUpdateCustomerNameId(updateCustomerNameVo.getUpdateCustomerNameId());
                    updateCustomerNameModel.setNadraName(updateCustomerNameVo.getFirstName() + " " + updateCustomerNameVo.getLastName());

                    baseWrapper.setBasePersistableModel(updateCustomerNameModel);

                    updateCustomerNameFacade.saveOrUpdateCustomerName(baseWrapper);

                    String customerSMS = this.getMessageSource().getMessage("update.customer.name.request.Update", null, null);
                    baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(appUserModel.getMobileNo(), customerSMS));
                    getCommonCommandManager().sendSMSToUser(baseWrapper);

                    actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel, "", refDataModelString, null, usecaseModels, actionAuthorizationId, request);
                    this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale())
                            + "Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
                    modelAndView = new ModelAndView(new RedirectView("home.html"));
                }
            } else {

                actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel, "", refDataModelString, null, new Long(PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID), updateCustomerNameVo.getMobileNo(), resubmitRequest, actionAuthorizationId, request);
                this.saveMessage(request, "Action is pending for approval against reference Action ID : " + actionAuthorizationId);
                BaseWrapper baseWrapper = new BaseWrapperImpl();
                UpdateCustomerNameModel updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(cnic);
                updateCustomerNameModel.setUpdateCustomerNameId(updateCustomerNameVo.getUpdateCustomerNameId());
                updateCustomerNameModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
                updateCustomerNameModel.setUpdatedOn(new Date());
                baseWrapper.setBasePersistableModel(updateCustomerNameModel);
                updateCustomerNameFacade.saveOrUpdateCustomerName(baseWrapper);
                modelAndView = new ModelAndView(new RedirectView("home.html"));

            }

        } catch (Exception e) {
            this.saveMessage(request, MessageUtil.getMessage("6075"));
            return super.showForm(request, response, errors);

        }


        return modelAndView;
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception {
        Map referenceDataMap = new HashMap();
        AppUserModel appUserModel = getCommonCommandManager().getAppUserModelByCNIC(cnic);
        if (appUserModel != null)
            referenceDataMap.put("appUserId", appUserModel.getAppUserId());
        referenceDataMap.put("middleName", appUserModel.getMiddleName());

        return referenceDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
        boolean isReSubmit = ServletRequestUtils.getBooleanParameter(request, "isReSubmit", false);
        if (isReSubmit) {
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "authId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
            cnic = ServletRequestUtils.getStringParameter(request, "cnic");
            UpdateCustomerNameModel updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(cnic);
            XStream xstream = new XStream();
            UpdateCustomerNameVo updateCustomerNameVo = (UpdateCustomerNameVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());

            updateCustomerNameVo.setCnic(updateCustomerNameModel.getCnic());
            updateCustomerNameVo.setMobileNo(updateCustomerNameModel.getMobileNo());
        }

        cnic = ServletRequestUtils.getStringParameter(request, "cnic");
        UpdateCustomerNameModel updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(cnic);

        UpdateCustomerNameVo updateCustomerNameVo = new UpdateCustomerNameVo();

        updateCustomerNameVo.setFirstName(updateCustomerNameModel.getFirstName());
        updateCustomerNameVo.setLastName(updateCustomerNameModel.getLastName());
        updateCustomerNameVo.setCnic(updateCustomerNameModel.getCnic());
        updateCustomerNameVo.setMobileNo(updateCustomerNameModel.getMobileNo());
        updateCustomerNameVo.setUpdateCustomerNameId(updateCustomerNameModel.getUpdateCustomerNameId());
        updateCustomerNameVo.setUpdated(updateCustomerNameModel.getUpdated());
        updateCustomerNameVo.setNadraName(updateCustomerNameModel.getNadraName());
        return updateCustomerNameVo;
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        cnic = ServletRequestUtils.getStringParameter(request, "cnic");
        BaseWrapper baseWrapper = null;
        AppUserModel appUserModel = getCommonCommandManager().loadAppUserByCnicAndType(cnic);
        if (appUserModel != null) {
            CustomerModel customerModel = getCommonCommandManager().getCustomerModelById(appUserModel.getCustomerId());
            UpdateCustomerNameModel updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(cnic);

             baseWrapper = new BaseWrapperImpl();
            appUserModel.setFirstName(updateCustomerNameModel.getFirstName());
            appUserModel.setLastName(updateCustomerNameModel.getLastName());
            appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            appUserModel.setUpdatedOn(new Date());
            baseWrapper.setBasePersistableModel(appUserModel);
            getCommonCommandManager().getAppUserManager().saveOrUpdateAppUser(baseWrapper);

            baseWrapper = new BaseWrapperImpl();
            customerModel.setName(updateCustomerNameModel.getFirstName() + " " + updateCustomerNameModel.getLastName());
            customerModel.setUpdatedOn(new Date());
            customerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
            baseWrapper.setBasePersistableModel(customerModel);
            baseWrapper = this.custTransManager.saveOrUpdate(baseWrapper);
            baseWrapper = new BaseWrapperImpl();

            updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(updateCustomerNameModel.getCnic());

            updateCustomerNameModel.setUpdated(true);
            updateCustomerNameModel.setUpdateCustomerNameId(updateCustomerNameModel.getUpdateCustomerNameId());
            updateCustomerNameModel.setUpdatedOn(new Date());
            updateCustomerNameModel.setNadraName(updateCustomerNameModel.getFirstName() + " " + updateCustomerNameModel.getLastName());


            baseWrapper.setBasePersistableModel(updateCustomerNameModel);

            updateCustomerNameFacade.saveOrUpdateCustomerName(baseWrapper);
            String customerSMS = this.getMessageSource().getMessage("update.customer.name.request.Updated", null, null);
            baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(appUserModel.getMobileNo(), customerSMS));
            getCommonCommandManager().sendSMSToUser(baseWrapper);
        }

        this.saveMessage(request, super.getText("newMfsAccount.recordUpdateSuccessful", request.getLocale()));
        ModelAndView modelAndView = new ModelAndView(new RedirectView("home.html"));

        return modelAndView;    }

    public void setUpdateCustomerNameFacade(UpdateCustomerNameFacade updateCustomerNameFacade) {
        this.updateCustomerNameFacade = updateCustomerNameFacade;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setCustTransManager(CustTransManager custTransManager) {
        this.custTransManager = custTransManager;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }
}
