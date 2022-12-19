package com.inov8.microbank.webapp.action.portal.mfsaccountmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsAccountModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MfsDebitCardModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.mfsaccountmodule.MfsAccountManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class MfsDebitCardAuthorizationDetailController extends AdvanceAuthorizationFormController {

    private static final Logger LOGGER = Logger.getLogger( MfsDebitCardAuthorizationDetailController.class );
    private MfsAccountManager mfsAccountManager;
    private ReferenceDataManager referenceDataManager;


    public MfsDebitCardAuthorizationDetailController() {
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
            MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
            request.setAttribute("mfsDebitCardModel", mfsDebitCardModel);

            MfsDebitCardModel currentMfsDebitCardModel = populateCurrentInfoModel(mfsDebitCardModel.getMobileNo());
            request.setAttribute("currentMfsDebitCardModel",currentMfsDebitCardModel);
            return actionAuthorizationModel;
        }

        else
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
    protected ModelAndView onEscalate(HttpServletRequest request,HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;

        try{
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            boolean isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());
            long currentUserId= UserUtils.getCurrentUser().getAppUserId();

            UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());

            if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue() == currentUserId)) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }

                long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel());
                if (nextAuthorizationLevel < 1) {
                    XStream xstream = new XStream();
                    MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

                    baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsDebitCardModel.getActionId());
                    baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsDebitCardModel.getUsecaseId());
                    baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
                    baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
                    baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());

                    baseWrapper.putObject(MfsDebitCardModel.MFS_DEBIT_CARD_MODEL_KEY, mfsDebitCardModel);

                    MfsDebitCardModel oldMfsDebitCardModel = populateCurrentInfoModel(mfsDebitCardModel.getMobileNo());
//
//                    String mailingAddress  = String.valueOf(request.getAttribute("mailingAddressId"));
//                    baseWrapper.putObject("mailingAddressId", mailingAddress);

                    baseWrapper = this.mfsAccountManager.updateMfsDebitCard(baseWrapper);

                    actionAuthorizationModel.setReferenceData(xstream.toXML(oldMfsDebitCardModel));

                    if(actionAuthorizationModel.getEscalationLevel().intValue()< usecaseModel.getEscalationLevels().intValue()){
                        approvedWithIntimationLevelsNext(actionAuthorizationModel,model, usecaseModel,request);
                    }
                    else
                    {
                        approvedAtMaxLevel(actionAuthorizationModel, model);
                    }
                }
                else
                {
                    escalateToNextLevel(actionAuthorizationModel,model, nextAuthorizationLevel, usecaseModel.getUsecaseId(),request);
                }
            }
            else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){

                if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                XStream xstream = new XStream();
                MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

//                DebitCardRequestsViewModel debitCardRequestsViewModel = getCommonCommandManager().
//                        getDebitCardRequestsViewModelByAppUserId(mfsDebitCardModel.getAppUserId(), mfsDebitCardModel.getMobileNo());
                DebitCardModel debitCardModel = getCommonCommandManager().getDebitCardModelDao().
                        getDebitCardModelByCardNumber(mfsDebitCardModel.getCardNumber());

                debitCardModel.setCheckedById(UserUtils.getCurrentUser().getAppUserId());
                debitCardModel.setCheckedByName(UserUtils.getCurrentUser().getFirstName());
                debitCardModel.setUpdatedOn(new Date());
                getCommonCommandManager().getDebitCardModelDao().saveOrUpdate(debitCardModel);
                actionDeniedOrCancelled(actionAuthorizationModel, model,request);
            }
            else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue()
                    && (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
                    || actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){

                if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                actionDeniedOrCancelled(actionAuthorizationModel,model,request);
            }
            else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
                isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

                if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                requestAssignedBack(actionAuthorizationModel,model,request);
            }
            else{

                throw new FrameworkCheckedException("Invalid status marked");
            }
        }
        catch (FrameworkCheckedException ex)
        {
            if("MobileNumUniqueException".equals(ex.getMessage())){
                request.setAttribute("message",super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
            }else if("NICUniqueException".equals(ex.getMessage())){
                request.setAttribute("message",super.getText("newMfsAccount.nicNotUnique", request.getLocale() ));
            }else if("AccountOpeningCommissionException".equals(ex.getMessage())){
                request.setAttribute("message",super.getText("newMfsAccount.commission.failed", request.getLocale() ));
            }else{
                if(null!=ex.getMessage())
                    request.setAttribute("message",ex.getMessage().replaceAll("<br>",""));
                else
                    request.setAttribute("message", MessageUtil.getMessage("6075"));
            }

            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        catch (Exception ex){
            request.setAttribute("message",MessageUtil.getMessage("6075"));
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
            request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(request, response, errors);
        return modelAndView;
    }

    @Override
    protected ModelAndView onResolve(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;

        try{
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            UsecaseModel usecaseModel= usecaseFacade.loadUsecase(actionAuthorizationModel.getUsecaseId());
            BaseWrapper baseWrapper = new BaseWrapperImpl();
            baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());

            XStream xstream = new XStream();
            MfsDebitCardModel mfsDebitCardModel = (MfsDebitCardModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());

            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, mfsDebitCardModel.getActionId());
            baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, mfsDebitCardModel.getUsecaseId());
            baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
            baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
            baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());

            baseWrapper.putObject(MfsDebitCardModel.MFS_DEBIT_CARD_MODEL_KEY, mfsDebitCardModel);

            //setting current Data for history
            MfsDebitCardModel oldMfsDebitCardModel = this.populateCurrentInfoModel(mfsDebitCardModel.getMobileNo());

            baseWrapper = this.mfsAccountManager.updateMfsDebitCard(baseWrapper);

            actionAuthorizationModel.setReferenceData(xstream.toXML(oldMfsDebitCardModel));

            resolveWithIntimation(actionAuthorizationModel,model, usecaseModel,request);

        }
        catch (FrameworkCheckedException ex)
        {
            if("MobileNumUniqueException".equals(ex.getMessage())){
                request.setAttribute("message",super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
            }else if("NICUniqueException".equals(ex.getMessage())){
                request.setAttribute("message",super.getText("newMfsAccount.nicNotUnique", request.getLocale() ));
            }else if("AccountOpeningCommissionException".equals(ex.getMessage())){
                request.setAttribute("message",super.getText("newMfsAccount.commission.failed", request.getLocale() ));
            }else{
                if(null!=ex.getMessage())
                    request.setAttribute("message",ex.getMessage().replaceAll("<br>",""));
                else
                    request.setAttribute("message",MessageUtil.getMessage("6075"));
            }

            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
            request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }

        catch (Exception ex){
            request.setAttribute("message",MessageUtil.getMessage("6075"));
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
            request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(request, response, errors);
        return modelAndView;
    }

        private MfsDebitCardModel populateCurrentInfoModel(String mobileNo) throws Exception {
        BaseWrapper baseWrapper = new BaseWrapperImpl();
        AppUserModel appUserModel = new AppUserModel();
        appUserModel.setMobileNo(mobileNo);
        appUserModel = this.mfsAccountManager.getAppUserModelByMobileNumber(mobileNo);

        DebitCardRequestsViewModel debitCardRequestsViewModel = getCommonCommandManager().getDebitCardRequestsViewModelByAppUserId
                (appUserModel.getAppUserId(), appUserModel.getMobileNo());

        MfsDebitCardModel mfsDebitCardModel = new MfsDebitCardModel();
        mfsDebitCardModel.setEmbossingName(debitCardRequestsViewModel.getEmbossingName());
        mfsDebitCardModel.setNadraName(debitCardRequestsViewModel.getNadraName());
        mfsDebitCardModel.setCnic(debitCardRequestsViewModel.getCnic());
        mfsDebitCardModel.setMobileNo(debitCardRequestsViewModel.getMobileNo());
        mfsDebitCardModel.setMailingAddress(debitCardRequestsViewModel.getMailingAddress());
        mfsDebitCardModel.setCardNumber(debitCardRequestsViewModel.getCardNumber());
        mfsDebitCardModel.setCardStatusId(debitCardRequestsViewModel.getCardStatusId());
        mfsDebitCardModel.setCardProductCodeId(debitCardRequestsViewModel.getCardProductCodeId());
        mfsDebitCardModel.setCardStateId(debitCardRequestsViewModel.getCardStateId());
        mfsDebitCardModel.setSegmentName(debitCardRequestsViewModel.getSegmentName());
        mfsDebitCardModel.setChannelName(debitCardRequestsViewModel.getChannelName());
        mfsDebitCardModel.setCardProductType(debitCardRequestsViewModel.getCardProductType());
        mfsDebitCardModel.setCardTypeCode(debitCardRequestsViewModel.getCardTypeCode());
        mfsDebitCardModel.setMailingAddressId(debitCardRequestsViewModel.getMailingAddressId());

        return mfsDebitCardModel;
    }

    public CommonCommandManager getCommonCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommonCommandManager) applicationContext.getBean("commonCommandManager");
    }

    public void setMfsAccountManager(MfsAccountManager mfsAccountManager) {
        this.mfsAccountManager = mfsAccountManager;
    }

    @Override
    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }
}
