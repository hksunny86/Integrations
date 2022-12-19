package com.inov8.microbank.updatecustomername.Controller;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.*;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.Level2AccountModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.updatecustomername.facade.UpdateCustomerNameFacade;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import com.inov8.microbank.updatecustomername.vo.UpdateCustomerNameVo;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.inov8.microbank.webapp.action.portal.mfsaccountmodule.Level2AccountAuthorizationDetailController;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

public class UpdateCustomerNameAuthorizationController extends AdvanceAuthorizationFormController {

    ReferenceDataManager referenceDataManager;
    UpdateCustomerNameFacade updateCustomerNameFacade;
    private String cnic;
    private MessageSource messageSource;


    private static final Logger LOGGER = Logger.getLogger(UpdateCustomerNameAuthorizationController.class);

    public UpdateCustomerNameAuthorizationController() {
        setCommandName("actionAuthorizationModel");
        setCommandClass(ActionAuthorizationModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request) throws Exception {
        Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

        if (escalateRequest || resolveRequest) {
            ActionStatusModel actionStatusModel = new ActionStatusModel();
            ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl(actionStatusModel, "name", SortingOrder.ASC);
            referenceDataManager.getReferenceData(refDataWrapper);
            List<ActionStatusModel> actionStatusModelList;
            actionStatusModelList = refDataWrapper.getReferenceDataList();
            List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();

            for (ActionStatusModel actionStatusModel2 : actionStatusModelList) {
                if (((actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue())) && escalateRequest)
                    tempActionStatusModelList.add(actionStatusModel2);
                else if ((actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && resolveRequest)
                    tempActionStatusModelList.add(actionStatusModel2);
            }
            referenceDataMap.put("actionStatusModel", tempActionStatusModelList);

            ////// Action Authorization history////
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
            actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);

            List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;

            refDataWrapper = new ReferenceDataWrapperImpl(actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC);
            referenceDataManager.getReferenceData(refDataWrapper);

            actionAuthorizationHistoryModelList = refDataWrapper.getReferenceDataList();

            referenceDataMap.put("actionAuthorizationHistoryModelList", actionAuthorizationHistoryModelList);

            if (actionAuthorizationModel.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getCreatedById().longValue() == UserUtils.getCurrentUser().getAppUserId()) {
                boolean isAssignedBack = false;
                isAssignedBack = true;
                request.setAttribute("isAssignedBack", isAssignedBack);
            }
        }
        referenceDataMap.put("cnic", Collections.singletonList(cnic));
        return referenceDataMap;
    }

    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);

        if (escalateRequest || resolveRequest) {
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            XStream xstream = new XStream();
            UpdateCustomerNameVo updateCustomerNameVo = (UpdateCustomerNameVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            cnic = updateCustomerNameVo.getCnic();
            request.setAttribute("updateCustomerNameVo", updateCustomerNameVo);
            //End Populating authorization pictures
            AppUserModel appUserModel = getCommonCommandManager().loadAppUserByCnicAndType(updateCustomerNameVo.getCnic());
            request.setAttribute("appUserModel", appUserModel);


            return actionAuthorizationModel;
        } else
            return new ActionAuthorizationModel();
    }

    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return null;
    }

    @Override
    protected ModelAndView onEscalate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;
        try {
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            boolean isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());
            long currentUserId = UserUtils.getCurrentUser().getAppUserId();

            UsecaseModel usecaseModel = usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());

            if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue() == currentUserId)) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }

                long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel());
                if (nextAuthorizationLevel < 1) {

                    XStream xstream = new XStream();
                    UpdateCustomerNameVo updateCustomerNameVo = (UpdateCustomerNameVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());

                    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
                    baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID));
                    baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, actionAuthorizationModel.getCreatedById());
                    baseWrapper.putObject(PortalConstants.KEY_CREATED_ON, actionAuthorizationModel.getCreatedOn());
                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, actionAuthorizationModel.getCreatedByUsername());
                    updateCustomerNameVo.setActionStatus(model.getActionStatusId().longValue());
                    updateCustomerNameFacade.updateCustomer(baseWrapper, updateCustomerNameVo);

                    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
                    ArrayList<SmsMessage> messageList = new ArrayList<>();
                    messageList.add(new SmsMessage(updateCustomerNameVo.getMobileNo(),
                            MessageUtil.getMessage("update.customer.name.approved")));

                    workFlowWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGES, messageList);
                    getCommonCommandManager().sendSMS(workFlowWrapper);

                    if (actionAuthorizationModel.getEscalationLevel().intValue() < usecaseModel.getEscalationLevels().intValue()) {
                        approvedWithIntimationLevelsNext(actionAuthorizationModel, model, usecaseModel, request);
                    } else {
                        approvedAtMaxLevel(actionAuthorizationModel, model);
                    }

                } else {
                    escalateToNextLevel(actionAuthorizationModel, model, nextAuthorizationLevel, usecaseModel.getUsecaseId(), request);
                }
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                XStream xstream = new XStream();
                UpdateCustomerNameVo updateCustomerNameVo = (UpdateCustomerNameVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());
                baseWrapper = new BaseWrapperImpl();
                UpdateCustomerNameModel updateCustomerNameModel = new UpdateCustomerNameModel();
                updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(updateCustomerNameVo.getCnic());
                updateCustomerNameModel.setUpdated(true);
                updateCustomerNameModel.setUpdateCustomerNameId(updateCustomerNameVo.getUpdateCustomerNameId());
                updateCustomerNameModel.setUpdatedOn(new Date());
                updateCustomerNameModel.setActionStatusId(model.getActionStatusId().longValue());
                baseWrapper.setBasePersistableModel(updateCustomerNameModel);
                updateCustomerNameFacade.saveOrUpdateCustomerName(baseWrapper);
                String customerSMS = this.getMessageSource().getMessage("update.customer.name.request.Rejected", null, null);
                baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(updateCustomerNameVo.getMobileNo(), customerSMS));
                getCommonCommandManager().sendSMSToUser(baseWrapper);
                actionDeniedOrCancelled(actionAuthorizationModel, model, request);
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue()
                    && (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
                    || actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))) {

                if (!(actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                XStream xstream = new XStream();
                UpdateCustomerNameVo updateCustomerNameVo = (UpdateCustomerNameVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());
                baseWrapper = new BaseWrapperImpl();
                UpdateCustomerNameModel updateCustomerNameModel = new UpdateCustomerNameModel();
                updateCustomerNameModel = updateCustomerNameFacade.getUpdateCustomer(updateCustomerNameVo.getCnic());
                updateCustomerNameModel.setUpdated(true);
                updateCustomerNameModel.setUpdateCustomerNameId(updateCustomerNameVo.getUpdateCustomerNameId());
                updateCustomerNameModel.setActionStatusId(model.getActionStatusId().longValue());
                updateCustomerNameModel.setUpdatedOn(new Date());
                baseWrapper.setBasePersistableModel(updateCustomerNameModel);
                updateCustomerNameFacade.saveOrUpdateCustomerName(baseWrapper);
                String customerSMS = this.getMessageSource().getMessage("update.customer.name.request.Rejected", null, null);
                baseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(updateCustomerNameVo.getMobileNo(), customerSMS));
                getCommonCommandManager().sendSMSToUser(baseWrapper);
                actionDeniedOrCancelled(actionAuthorizationModel, model, request);
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());

                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                requestAssignedBack(actionAuthorizationModel, model, request);
            } else {

                throw new FrameworkCheckedException("Invalid status marked");
            }
        } catch (FrameworkCheckedException ex) {
            if ("MobileNumUniqueException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale()));
            } else if ("NICUniqueException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.nicNotUnique", request.getLocale()));
            } else if ("AccountOpeningCommissionException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.commission.failed", request.getLocale()));
            } else {
                if (null != ex.getMessage())
                    request.setAttribute("message", ex.getMessage().replaceAll("<br>", ""));
                else
                    request.setAttribute("message", MessageUtil.getMessage("6075"));
            }
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        } catch (Exception ex) {
            request.setAttribute("message", MessageUtil.getMessage("6075"));
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        request.setAttribute("status", IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(request, response, errors);
        return modelAndView;
    }

    @Override
    protected ModelAndView onResolve(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;
        try {
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            UsecaseModel usecaseModel = usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
            XStream xstream = new XStream();
            UpdateCustomerNameVo updateCustomerNameVo = (UpdateCustomerNameVo) xstream.fromXML(actionAuthorizationModel.getReferenceData());

            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_CUSTOMER_NAME_USECASE_ID));
            baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, actionAuthorizationModel.getCreatedById());
            baseWrapper.putObject(PortalConstants.KEY_CREATED_ON, actionAuthorizationModel.getCreatedOn());
            baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, actionAuthorizationModel.getCreatedByUsername());
            updateCustomerNameVo.setActionStatus(model.getActionStatusId().longValue());
            updateCustomerNameFacade.updateCustomer(baseWrapper, updateCustomerNameVo);
            resolveWithIntimation(actionAuthorizationModel, model, usecaseModel, request);
        } catch (FrameworkCheckedException ex) {
            if ("MobileNumUniqueException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale()));
            } else if ("NICUniqueException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.nicNotUnique", request.getLocale()));
            } else if ("AccountOpeningCommissionException".equals(ex.getMessage())) {
                request.setAttribute("message", super.getText("newMfsAccount.commission.failed", request.getLocale()));
            } else {
                if (null != ex.getMessage())
                    request.setAttribute("message", ex.getMessage().replaceAll("<br>", ""));
                else
                    request.setAttribute("message", MessageUtil.getMessage("6075"));
            }
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        } catch (Exception ex) {
            request.setAttribute("message", MessageUtil.getMessage("6075"));
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        request.setAttribute("status", IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(request, response, errors);
        return modelAndView;
    }

    @Override
    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setUpdateCustomerNameFacade(UpdateCustomerNameFacade updateCustomerNameFacade) {
        this.updateCustomerNameFacade = updateCustomerNameFacade;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
