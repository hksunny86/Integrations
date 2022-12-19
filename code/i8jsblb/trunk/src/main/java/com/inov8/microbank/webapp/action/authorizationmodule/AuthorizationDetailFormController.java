package com.inov8.microbank.webapp.action.authorizationmodule;
/*
 * Author : Hassan Javaid
 * Date   : 13-08-2014
 * Module : Action Authorization
 * Project: Mircobank
 * */

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.messagemodule.SmsMessage;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.debitcard.vo.DebitCardVO;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderService;
import com.inov8.microbank.server.service.smssendermodule.SmsSenderServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.manualadjustmentmodule.ManualAdjustmentManager;

public class AuthorizationDetailFormController extends AdvanceFormController {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationDetailFormController.class);
    private ActionAuthorizationFacade actionAuthorizationFacade;
    private UsecaseFacade usecaseFacade;
    private ReferenceDataManager referenceDataManager;
    private CommonCommandManager commonCommandManager;


    private ManualAdjustmentManager manualAdjustmentManager;

    public AuthorizationDetailFormController() {
        setCommandName("actionAuthorizationModel");
        setCommandClass(ActionAuthorizationModel.class);
    }

    @Override
    protected Map loadReferenceData(HttpServletRequest request)
            throws Exception {
        Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
        Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
        ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
        boolean isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());
        boolean isMaker = false;
        if (actionAuthorizationModel.getCreatedById().longValue() == UserUtils.getCurrentUser().getAppUserId().longValue())
            isMaker = true;

        if (escalateRequest || (AuthenticationUtil.checkRightsIfAny(PortalConstants.MNG_USECASE_UPDATE))) {
            ActionStatusModel actionStatusModel = new ActionStatusModel();
            ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl(actionStatusModel, "name", SortingOrder.ASC);
            referenceDataManager.getReferenceData(refDataWrapper);
            List<ActionStatusModel> actionStatusModelList;
            actionStatusModelList = refDataWrapper.getReferenceDataList();
            List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();

            for (ActionStatusModel actionStatusModel2 : actionStatusModelList) {

                if (actionStatusModel2.getActionStatusId().intValue() == actionAuthorizationModel.getActionStatusId().intValue())
                    tempActionStatusModelList.add(actionStatusModel2);


                if (((actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
                        || (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue())
                        && escalateRequest)) {
                    if (isValidChecker && (!isMaker))
                        tempActionStatusModelList.add(actionStatusModel2);
                } else if (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.intValue()) {
                    Long usecaseId = actionAuthorizationModel.getUsecaseId();
                    if (usecaseId.equals(PortalConstants.LINK_PAYMENT_MODE_USECASE_ID) || usecaseId.equals(PortalConstants.I8_USER_MANAGEMENT_CREATE_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.I8_USER_MANAGEMENT_UPDATE_USECASE_ID) || usecaseId.equals(PortalConstants.RETAILER_FORM_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID) || usecaseId.equals(PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID) || usecaseId.equals(PortalConstants.USER_GROUP_CREATE_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.USER_GROUP_UPDATE_USECASE_ID) || usecaseId.equals(PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.CREATE_L2_USECASE_ID) || usecaseId.equals(PortalConstants.UPDATE_L2_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.CREATE_L3_USECASE_ID) || usecaseId.equals(PortalConstants.UPDATE_USECASE) ||
                            usecaseId.equals(PortalConstants.KEY_CREATE_UPDATE_TAX_REGIME_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.WHT_EXEMPTION_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.WHT_CONFIG_USECASE_ID) ||
                            usecaseId.equals(PortalConstants.PRODUCT_CREATE_USECASE_ID) || usecaseId.equals(PortalConstants.PRODUCT_UPDATE_USECASE_ID)) {
                        if (isValidChecker && (!isMaker))
                            tempActionStatusModelList.add(actionStatusModel2);
                    }
                } else if (actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue()) {
                    if (isMaker)
                        tempActionStatusModelList.add(actionStatusModel2);
                } else if ((actionStatusModel2.getActionStatusId().intValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && (AuthenticationUtil.checkRightsIfAny(PortalConstants.MNG_USECASE_UPDATE))) {
                    if (!isMaker)
                        tempActionStatusModelList.add(actionStatusModel2);
                }
            }
            referenceDataMap.put("actionStatusModel", tempActionStatusModelList);

            ////// Action Authorization history////

            ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
            actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);

            List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;

            refDataWrapper = new ReferenceDataWrapperImpl(actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC);
            referenceDataManager.getReferenceData(refDataWrapper);

            actionAuthorizationHistoryModelList = refDataWrapper.getReferenceDataList();

            referenceDataMap.put("actionAuthorizationHistoryModelList", actionAuthorizationHistoryModelList);

            boolean isAssignedBack = false;
            if (actionAuthorizationModel.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getCreatedById().longValue() == UserUtils.getCurrentUser().getAppUserId()) {

                isAssignedBack = true;
                request.setAttribute("isAssignedBack", isAssignedBack);
            }
        }
        return referenceDataMap;
    }


    @Override
    protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {

        boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);

        if (escalateRequest || (AuthenticationUtil.checkRightsIfAny(PortalConstants.MNG_USECASE_UPDATE))) {
            Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> refDataMap = new HashMap<String, Object>();

            try {
                refDataMap = mapper.readValue(actionAuthorizationModel.getReferenceData(), new TypeReference<Map>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                throw new FrameworkCheckedException("Error occured while getting action details");
            }

            String actionId = (String) refDataMap.get(PortalConstants.KEY_ACTION_ID);
            request.setAttribute("actionId", actionId);

            String commandClassName = (String) refDataMap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS);

            String pageUrl = (String) refDataMap.get(ActionAuthorizationConstantsInterface.KEY_FORM_NAME);
            request.setAttribute("pageUrl", pageUrl);

            String newModelJson = (String) refDataMap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
            Map newModelMap = this.getFieldMap(newModelJson, commandClassName);
            request.setAttribute("newModelMap", newModelMap);

            if (actionId.equals(PortalConstants.ACTION_UPDATE.toString())) {
                String oldModelJson = (String) refDataMap.get(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING);
                if (pageUrl.contains("searchDebitCardRequest"))
                    oldModelJson = newModelJson;
                if (!StringUtil.isNullOrEmpty(oldModelJson)) {
                    Map oldModelMap = this.getFieldMap(oldModelJson, commandClassName);
                    request.setAttribute("oldModelMap", oldModelMap);
                }
            }

            return actionAuthorizationModel;
        } else
            return new ActionAuthorizationModel();
    }


    @Override
    protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object,
                                    BindException errors) throws Exception {
        return null;
    }


    @Override
    protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ModelAndView modelAndView = null;
        ActionAuthorizationModel model = (ActionAuthorizationModel) command;
        try {
            ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(model.getActionAuthorizationId());
            boolean isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());
            long currentUserId = UserUtils.getCurrentUser().getAppUserId();
            BaseWrapper baseWrapper = new BaseWrapperImpl();


            if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {

                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue() == currentUserId)) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                if (actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_REISSUENCE_USECASE_ID)) {
                    baseWrapper = populateBaseWrapper(baseWrapper, actionAuthorizationModel, model);
                    getCommonCommandManager().debitCardReissuance(baseWrapper);
                } else {
                    baseWrapper = populateBaseWrapper(baseWrapper, actionAuthorizationModel, model);
                    actionAuthorizationFacade.requestApproved(baseWrapper);
                }
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {

                if (!AuthenticationUtil.checkRightsIfAny(PortalConstants.MNG_USECASE_UPDATE)) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }

                baseWrapper = populateBaseWrapper(baseWrapper, actionAuthorizationModel, model);
                actionAuthorizationFacade.requestApproved(baseWrapper);
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());

                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }

                ///Custom Code for Mannual Adjustment- DebitBlock
            	/*if(actionAuthorizationModel.getUsecaseId().longValue()==PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID)
            	{	
            		ObjectMapper mapper = new ObjectMapper();
            		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            		baseWrapper = populateBaseWrapper(baseWrapper, actionAuthorizationModel, model);
                    String VOModelString = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
                    BasePersistableModel bpm = (BasePersistableModel) mapper.readValue(VOModelString,ManualAdjustmentVO.class);
        			baseWrapper.setBasePersistableModel(bpm);
            		ManualAdjustmentVO manualAdjustmentVO = (ManualAdjustmentVO) baseWrapper.getBasePersistableModel();
            		
            		if((manualAdjustmentVO.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) 
							|| (manualAdjustmentVO.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)))
					{
            			try {
                			manualAdjustmentManager.markAccountDebitBlockUnBlock(manualAdjustmentVO,false);
                		} catch (Exception e) {
                			e.printStackTrace();
                			throw new FrameworkCheckedException(e.getMessage());
                		}
					}	
            	}  */
                BaseWrapper tempWrapper = null;
                ObjectMapper mapper = new ObjectMapper();
                String modelClassName = null;
                String VOModelString = null;
                BasePersistableModel bpm = null;
                DebitCardVO debitCardVO = null;
                if (actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID)) {
                    tempWrapper = populateBaseWrapper(baseWrapper, actionAuthorizationModel, model);
                    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    modelClassName = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME);
                    VOModelString = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
                    bpm = (BasePersistableModel) mapper.readValue(VOModelString, Class.forName(modelClassName));
                    tempWrapper.setBasePersistableModel(bpm);
                    debitCardVO = (DebitCardVO) tempWrapper.getBasePersistableModel();
                    actionAuthorizationModel.setDebitCardPan(debitCardVO.getCardNo());
                }
                else if (actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_REISSUENCE_USECASE_ID)){
                    tempWrapper = populateBaseWrapper(baseWrapper, actionAuthorizationModel, model);
                    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    modelClassName = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME);
                    VOModelString = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
                    bpm = (BasePersistableModel) mapper.readValue(VOModelString, Class.forName(modelClassName));
                    tempWrapper.setBasePersistableModel(bpm);
                    debitCardVO = (DebitCardVO) tempWrapper.getBasePersistableModel();
                    actionAuthorizationModel.setDebitCardPan(debitCardVO.getCardNo());
                }
                actionAuthorizationFacade.actionDeniedOrCancelled(actionAuthorizationModel, model);
                if (actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID)) {
                    Method method = null;
                    Object ob = null;
                    String managerName = (String) tempWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME);
                    String methodeName = "sendSMSToUser";
                    String msgToText = MessageUtil.getMessage("debit.card.req.rejected");
                    BaseWrapper msgBaseWrapper = new BaseWrapperImpl();
                    msgBaseWrapper.setBasePersistableModel(debitCardVO);
                    msgBaseWrapper.putObject(CommandFieldConstants.KEY_SMS_MESSAGE, new SmsMessage(debitCardVO.getMobileNo(), msgToText));
                    ob = SpringContext.getBean(managerName);
                    method = ob.getClass().getMethod(methodeName, BaseWrapper.class);
                    method.invoke(ob, msgBaseWrapper);
                }
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue()
                    && (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
                    || actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))) {

                if (!(actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }

                actionAuthorizationFacade.actionDeniedOrCancelled(actionAuthorizationModel, model);

                ///Custom Code for Mannual Adjustment- DebitBlock
            	/*if(actionAuthorizationModel.getUsecaseId().longValue()==PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID)
            	{
            		ObjectMapper mapper = new ObjectMapper();
            		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            		baseWrapper = populateBaseWrapper(baseWrapper, actionAuthorizationModel, model);
                    String VOModelString = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
                    BasePersistableModel bpm = (BasePersistableModel) mapper.readValue(VOModelString,ManualAdjustmentVO.class);
        			baseWrapper.setBasePersistableModel(bpm);
            		ManualAdjustmentVO manualAdjustmentVO = (ManualAdjustmentVO) baseWrapper.getBasePersistableModel();
            		
            		if((manualAdjustmentVO.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) 
							|| (manualAdjustmentVO.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)))
					{
            			try {
                			manualAdjustmentManager.markAccountDebitBlockUnBlock(manualAdjustmentVO,false);
                		} catch (Exception e) {
                			e.printStackTrace();
                			throw new FrameworkCheckedException(e.getMessage());
                		}
					}
            	}*/
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)) {
                isValidChecker = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(), UserUtils.getCurrentUser().getAppUserId());

                if ((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }
                actionAuthorizationFacade.requestAssignedBack(actionAuthorizationModel, model);
            } else if (model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.longValue()
                    && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK)) {

                if (!(actionAuthorizationModel.getCreatedById().equals(currentUserId))) {
                    throw new FrameworkCheckedException("You are not authorized to update action status.");
                }


            } else {

                throw new FrameworkCheckedException("Invalid status marked");
            }

        } catch (FrameworkCheckedException ex) {

            ex.printStackTrace();
            request.setAttribute("message", ex.getMessage());
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        catch (WorkFlowException ex) {

            ex.printStackTrace();
            request.setAttribute("message", ex.getMessage());
            LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : " + model.getActionAuthorizationId(), ex);
            request.setAttribute("status", IssueTypeStatusConstantsInterface.FAILURE);
            return super.showForm(request, response, errors);
        }
        request.setAttribute("status", IssueTypeStatusConstantsInterface.SUCCESS);
        modelAndView = super.showForm(request, response, errors);
        return modelAndView;
    }


    private Map getFieldMap(String modelJson, String className) throws FrameworkCheckedException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        Map<Object, Object> newMap = new HashMap<>();
        try {
            map = mapper.readValue(modelJson, new TypeReference<Map>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException("Error occured while getting action details");
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = MessageUtil.getMessage(className + "." + entry.getKey());
            if (StringUtils.isEmpty(key))
                continue;
            newMap.put(key, entry.getValue());
        }

        return newMap;
    }

    private BaseWrapper populateBaseWrapper(BaseWrapper baseWrapper, ActionAuthorizationModel actionAuthorizationModel, ActionAuthorizationModel model) throws FrameworkCheckedException {
        Map referenceDatamap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Object ob = null;

        try {
            ob = mapper.readValue(actionAuthorizationModel.getReferenceData(), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkCheckedException("Unable to extract request reference data");
        }
        referenceDatamap = (HashMap) ob;

        if (null != referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING))
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING));
        if (null != referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING))
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING));
        if (null != referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS))
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS));
        if (null != referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME))
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME));
        if (null != referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME))
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME, (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME));
        if (null != referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME))
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME));
        if (null != referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_FORM_NAME))
            baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_FORM_NAME));

        if (null != referenceDatamap.get(PortalConstants.KEY_ACTION_ID)) {
            String actionId = (String) referenceDatamap.get(PortalConstants.KEY_ACTION_ID);
            baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, Long.parseLong(actionId));
        }

        if ((actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_REISSUENCE_USECASE_ID))){
            baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_TYPE, "02");

        }else if (actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID)){
            baseWrapper.putObject(CommandFieldConstants.KEY_TRANSACTION_TYPE, "01");
        }

        baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, actionAuthorizationModel.getUsecaseId());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
        baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_NEW_AUTH_MODEL, model);
        baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID, actionAuthorizationModel.getCreatedById());
        baseWrapper.putObject(PortalConstants.KEY_CREATED_ON, actionAuthorizationModel.getCreatedOn());
        baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME, actionAuthorizationModel.getCreatedByUsername());


        return baseWrapper;
    }


    public void setActionAuthorizationFacade(
            ActionAuthorizationFacade actionAuthorizationFacade) {
        this.actionAuthorizationFacade = actionAuthorizationFacade;
    }

    public void setUsecaseFacade(UsecaseFacade usecaseFacade) {
        this.usecaseFacade = usecaseFacade;
    }

    public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
        this.referenceDataManager = referenceDataManager;
    }

    public void setManualAdjustmentManager(
            ManualAdjustmentManager manualAdjustmentManager) {
        this.manualAdjustmentManager = manualAdjustmentManager;
    }

    public void setCommonCommandManager(CommonCommandManager commonCommandManager) {
        this.commonCommandManager = commonCommandManager;
    }

    private CommonCommandManager getCommonCommandManager() {
        ApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        CommonCommandManager commonCommandManager = (CommonCommandManager) webApplicationContext.getBean("commonCommandManager");
        return commonCommandManager;
    }

}