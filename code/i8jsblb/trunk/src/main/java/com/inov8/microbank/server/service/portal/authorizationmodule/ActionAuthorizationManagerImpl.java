package com.inov8.microbank.server.service.portal.authorizationmodule;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inov8.integration.controller.IBFTSwitchController;
import com.inov8.integration.vo.MiddlewareMessageVO;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.transactionreversal.TransactionReversalManager;
import org.apache.commons.validator.GenericValidator;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.BasePersistableModel;
import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.EmailServiceSendFailureException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthPictureModelDAO;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthorizationModelDAO;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class ActionAuthorizationManagerImpl implements ActionAuthorizationManager {
	
	private ActionAuthorizationModelDAO actionAuthorizationModelDAO;
	private ActionAuthPictureModelDAO	actionAuthPictureModelDAO;
	private ActionAuthorizationHistoryManager actionAuthorizationHistoryManager;
	private JmsProducer jmsProducer;
	private UsecaseFacade usecaseFacade;
	private ActionLogManager actionLogManager;
	private TransactionReversalManager transactionReversalManager;
	private IBFTSwitchController ibftSwitchController;

	@Override
	public SearchBaseWrapper search(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		searchBaseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.KEY_LIST_AUTH_RQST_USECASE_ID);
		ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,null);
		CustomList<ActionAuthorizationModel> customList = this.actionAuthorizationModelDAO.search(searchBaseWrapper);
		searchBaseWrapper.setCustomList(customList);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,actionLogModel);
		return searchBaseWrapper;
	}

	@Override
	public SearchBaseWrapper searchMyRequests(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		searchBaseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.KEY_MY_AUTH_RQST_USECASE_ID);
		ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,null);
		CustomList<ActionAuthorizationModel> customList = this.actionAuthorizationModelDAO.searchMyRequests(searchBaseWrapper);
		searchBaseWrapper.setCustomList(customList);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,null,actionLogModel);
		return searchBaseWrapper;
	}

	@Override
	public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate((ActionAuthorizationModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		return baseWrapper;
		
	}
	@Override
	public ActionAuthorizationModel load(Long actionAuthorizationId ) throws FrameworkCheckedException {
		return actionAuthorizationModelDAO.findByPrimaryKey(actionAuthorizationId);	
	}
	
	@Override
	public ActionAuthPictureModel getActionAuthPictureModelByTypeId(Long actionAuthorizationId, Long pictureTypeId)
																					throws FrameworkCheckedException
	{
		ActionAuthPictureModel actionAuthPictureModel = new ActionAuthPictureModel();
		actionAuthPictureModel.setActionAuthorizationId(actionAuthorizationId);
		actionAuthPictureModel.setPictureTypeId(pictureTypeId);
		CustomList<ActionAuthPictureModel> customList=actionAuthPictureModelDAO.findByExample(actionAuthPictureModel);
		if(null!=customList.getResultsetList() && customList.getResultsetList().size()>0)
			return customList.getResultsetList().get(0);
		else
			return null;
	}
	@Override
	public ActionAuthPictureModel saveOrUpdate(ActionAuthPictureModel actionAuthPictureModel) throws FrameworkCheckedException {
		
		return actionAuthPictureModelDAO.saveOrUpdate(actionAuthPictureModel);
	}
	
	@Override
	public SearchBaseWrapper searchConflictedAuthorizationRequests(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		CustomList<ActionAuthorizationModel> customList = this.actionAuthorizationModelDAO.searchConflictedAuthorizationRequests(searchBaseWrapper);
		searchBaseWrapper.setCustomList(customList);
		return searchBaseWrapper;
	}

	@Override
	public CustomList<ActionAuthorizationModel> checkExistingRequest(ActionAuthorizationModel actionAuthorizationModel)throws FrameworkCheckedException {
		
		return this.actionAuthorizationModelDAO.checkExistingRequest(actionAuthorizationModel);
	}
	
	
	@Override
	public String loadAuthorizationVOJson(HttpServletRequest req) throws Exception{
		Long actionAuthorizationId = ServletRequestUtils.getLongParameter(req,"authId");
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(req, "isReSubmit",false);
		boolean isNew = ServletRequestUtils.getBooleanParameter(req, "isNew",false);
		boolean isReadOnly = ServletRequestUtils.getBooleanParameter(req, "isReadOnly",false);
		ActionAuthorizationModel actionAuthorizationModel = this.load(actionAuthorizationId);
		
		if((actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()) && isReSubmit){
			throw new FrameworkCheckedException("illegal operation performed");
		}
 	
		Map referenceDatamap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		
		Object ob;
		ob = mapper.readValue(actionAuthorizationModel.getReferenceData(), Class.forName(HashMap.class.getName()));
		
		referenceDatamap = (HashMap) ob;
		String modelJsonString = null;
		
		if(isReadOnly){
			if(isNew)
				modelJsonString = (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
			else
				modelJsonString = (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING);
						
		}
		else
			modelJsonString = (String) referenceDatamap.get(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);

		return modelJsonString;
	}  
	
	//****************************************************************************************************************
	
	@SuppressWarnings("unchecked")
	@Override
	public void requestApproved(BaseWrapper baseWrapper) throws FrameworkCheckedException {


		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String VOModelString = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING);
		String modelClassName =  (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME);
		String managerName = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME);
		String methodeName = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME);
		Object ob = null;
		BasePersistableModel bpm;
		Class manager =null;
		Method method =null;
		
		try 
		{
			bpm = (BasePersistableModel) mapper.readValue(VOModelString,Class.forName(modelClassName));
			baseWrapper.setBasePersistableModel(bpm);
			ob= SpringContext.getBean(managerName);			
			method = ob.getClass().getMethod(methodeName, BaseWrapper.class);
			method.invoke(ob, baseWrapper);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException|NoSuchMethodException 
				|SecurityException|ClassNotFoundException|IOException e) {
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getCause().getMessage());
		}
			
	}
	
	
	@Override
	public void actionDeniedOrCancelled(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException{
		BaseWrapper modelWrapper = new BaseWrapperImpl();
		modelWrapper.setBasePersistableModel(commandModel);
		modelWrapper.putObject(PortalConstants.KEY_USECASE_ID,commandModel.getUsecaseId());
		modelWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		ActionLogModel actionLogModel = actionLogManager.getActionLogModelByActionAuthId(actionAuthorizationModel.getActionAuthorizationId());
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		actionAuthorizationModel.setActionStatusId(commandModel.getActionStatusId());
		actionAuthorizationModel.setCheckedById(UserUtils.getCurrentUser().getAppUserId());
		actionAuthorizationModel.setCheckedOn(new Date());
		actionAuthorizationModel.setComments(commandModel.getComments());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);
		
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel(commandModel.getActionStatusIdActionStatusModel(), UserUtils.getCurrentUser(), 
				actionAuthorizationModel, actionAuthorizationModel.getEscalationLevel(), new Date(), commandModel.getCheckerComments(), null,null);
		
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);

		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);

		if(actionLogModel != null)
			actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,actionLogModel);

		if((null!=actionAuthorizationModel.getCreatedByAppUserModel().getEmail()) && GenericValidator.isEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail()))
			sendNotificationEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail(),actionAuthorizationModel);

		if((actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_ISSUENCE_USECASE_ID)||actionAuthorizationModel.getUsecaseId().equals(PortalConstants.KEY_DEBIT_CARD_REISSUENCE_USECASE_ID)) && actionAuthorizationModel.getTransactionCode() != null)
		{
			TransactionDetailMasterModel tempModel = new TransactionDetailMasterModel();
			tempModel.setTransactionCode(actionAuthorizationModel.getTransactionCode());
			BaseWrapper wrapper = new BaseWrapperImpl();
			wrapper.setBasePersistableModel(tempModel);
			try {
				tempModel = transactionReversalManager.loadTDMForReversal(wrapper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//
			if(tempModel != null) {
				MiddlewareMessageVO middlewareMessageVO = new MiddlewareMessageVO();
				middlewareMessageVO.setProductId(tempModel.getProductId());
				middlewareMessageVO.setReserved1(tempModel.getTransactionCodeId().toString());
				middlewareMessageVO.setPAN(actionAuthorizationModel.getDebitCardPan());
				ibftSwitchController.cashWithDrawalReversal(middlewareMessageVO);
				System.out.println("End of Transaction Reversal");
			}
		}
	}
	
	
	
	
	
	
	
	
	private void sendNotificationEmail(String recipients,ActionAuthorizationModel model) throws FrameworkCheckedException{
		EmailMessage emailMessage = new EmailMessage();
		StringBuilder notificationtext= new StringBuilder();
		String detailUrl=null;
		
		emailMessage.setRecepients(recipients.split(";"));	
		
		emailMessage.setSubject(MessageUtil.getMessage("actionAuthorization.emailSubject",new String[]{model.getActionAuthorizationId().toString()}));
		
		long actionStatusId = model.getActionStatusId().longValue();
		if(actionStatusId == ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.longValue()) 
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionPending",new String[]{model.getEscalationLevel().toString()}));
		else if(actionStatusId == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() || 
				actionStatusId == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED.longValue())
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionApproved",null));
		else if(actionStatusId == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue())
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionDenied",new String[]{model.getEscalationLevel().toString()}));
		else if(actionStatusId == ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.longValue())
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionResolved",null));
		else if(actionStatusId == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue())
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.actionAssignedBack",null));

		if(model.getUsecaseId().longValue()==PortalConstants.ONE_TIME_PIN_RESET_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.RETAILER_FORM_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.RETAILER_FORM_UPDATE_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.MFS_ACCOUNT_CREATE_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.MFS_ACCOUNT_UPDATE_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.CREATE_L2_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.UPDATE_L2_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.CREATE_L3_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.UPDATE_L3_USECASE_ID
			||model.getUsecaseId().longValue()==PortalConstants.UPDATE_USECASE)
		{
			detailUrl = AuthoirzationDetailEnum.getUrlByUsecaseId(model.getUsecaseId());
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.oldUrl",new String[]{MessageUtil.getPortalLink(),detailUrl})+model.getActionAuthorizationId().toString());
		}
		else
		{
			notificationtext.append(MessageUtil.getMessage("actionAuthorization.emailText.url",new String[]{MessageUtil.getPortalLink()})+model.getActionAuthorizationId().toString());
		}
		
		emailMessage.setText(notificationtext.toString());
		
		try{
			jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
		}catch(EmailServiceSendFailureException esx){
			throw new FrameworkCheckedException(esx.getMessage());
		}
	}
	
	
	@Override
	public void requestAssignedBack(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK);
		actionAuthorizationModel.setEscalationLevel(new Long(0));
		
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);
		
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel(commandModel.getActionStatusIdActionStatusModel(), UserUtils.getCurrentUser(), actionAuthorizationModel,
				commandModel.getEscalationLevel(), new Date(), commandModel.getCheckerComments(),null,null);
	
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
		
		if((null!=actionAuthorizationModel.getCreatedByAppUserModel().getEmail()) && GenericValidator.isEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail()))
			sendNotificationEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail(),actionAuthorizationModel);			
	}	
	
	
	@Override
	public Boolean performAuthorization(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		ActionLogModel actionLogModel = new ActionLogModel();
		UsecaseModel usecaseModel=null;
		ActionAuthorizationModel actionAuthorizationModel=null;
		String refDataMapStr = "";
		Long usecaseId = (Long) baseWrapper.getObject(PortalConstants.KEY_USECASE_ID);
		Long actionAuthorizationId = (Long) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_ACTION_AUTH_ID);	
		String reqReferenceId = (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_REQ_REF_ID);
		String initiatorComments = (String)baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_INITIATOR_COMMENTS);
		
		Boolean resubmitRequest = false;
		
		if(null!=actionAuthorizationId){
			actionAuthorizationModel = this.load(actionAuthorizationId);
			usecaseModel = actionAuthorizationModel.getUsecaseIdUsecaseModel();
		}
		else{
			usecaseModel = usecaseFacade.loadUsecase(usecaseId);
		}
		
		if(usecaseModel.getIsAuthorizationEnable() 
				&& (null==actionAuthorizationModel || 
						(null!=actionAuthorizationModel && actionAuthorizationModel.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){


			if(baseWrapper.getObject(PortalConstants.KEY_USECASE_ID).equals(1093L)) {
				refDataMapStr = String.valueOf(baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING));
			}
			else {
				refDataMapStr = getRefDataMapString(baseWrapper);
			}
			actionLogModel.setUsecaseId(usecaseId);
			baseWrapper.putObject("actionLogModel",actionLogModel);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
			if(nextAuthorizationLevel.intValue()<0){
				baseWrapper = performActionWithAllIntimationLevels(nextAuthorizationLevel,initiatorComments, refDataMapStr, usecaseModel,actionAuthorizationModel);
				actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper.getBasePersistableModel();
				actionAuthorizationId = actionAuthorizationModel.getActionAuthorizationId();
				baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG,
						"Action is authorized successfully. Changes are saved against reference Action ID : "+actionAuthorizationId);
				actionLogModel = (ActionLogModel) baseWrapper.getObject("actionLogModel");
				actionLogModel.setOutputXml("Action is authorized successfully. Changes are saved against reference Action ID : "+actionAuthorizationId.toString());
				actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
				return true;
			}
			else
			{
				if(null!=actionAuthorizationModel)//As authorization model will be either Null or With Assigned back Status in this block
					resubmitRequest = true;
				Long segmentId = null;
				String transactionCode = null;
				if(baseWrapper.getObject("segmentId") != null)
					segmentId = (Long) baseWrapper.getObject("segmentId");
				if(baseWrapper.getObject(CommandFieldConstants.KEY_TRANSACTION_ID) != null)
					transactionCode = (String) baseWrapper.getObject(CommandFieldConstants.KEY_TRANSACTION_ID);
				BaseWrapper modelWrapper = createAuthorizationRequest(nextAuthorizationLevel,initiatorComments,
						refDataMapStr,usecaseId,reqReferenceId,resubmitRequest,actionAuthorizationModel,segmentId,transactionCode);
				actionAuthorizationModel = (ActionAuthorizationModel) modelWrapper.getBasePersistableModel();
				actionAuthorizationId = actionAuthorizationModel.getActionAuthorizationId();
				baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationId);
				actionLogModel = (ActionLogModel) modelWrapper.getObject("actionLogModel");
				actionLogModel.setOutputXml("Action is pending for approval against reference Action ID : "+actionAuthorizationId.toString());
				actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,actionLogModel);
				baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG,
						"Action is pending for approval against reference Action ID : "+actionAuthorizationId.toString());
				
				
				///Custom Code for Mannual Adjustment- DebitBlock
			/*	if(usecaseId.longValue()==PortalConstants.MANUAL_ADJUSTMENT_USECASE_ID)
				{
					ManualAdjustmentVO manualAdjustmentVO = (ManualAdjustmentVO) baseWrapper.getBasePersistableModel();
					
					if((manualAdjustmentVO.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_BB)) 
							|| (manualAdjustmentVO.getAdjustmentType().equals(ManualAdjustmentTypeConstants.BB_TO_CORE)))
					{
						try {
							manualAdjustmentManager.markAccountDebitBlockUnBlock(manualAdjustmentVO,true);
						} catch (Exception e) {
							e.printStackTrace();
							throw new FrameworkCheckedException(e.getMessage());
						}
					}
				}*/
				return false;
			}
			
		}
		else if(null!=actionAuthorizationModel && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
			ActionAuthorizationModel newAuthModel = (ActionAuthorizationModel) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_NEW_AUTH_MODEL);

			if(newAuthModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED)){
				resolveWithIntimation(actionAuthorizationModel, newAuthModel, usecaseModel);
				return true;
			}
			
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,actionAuthorizationModel.getEscalationLevel());
			
			if(nextAuthorizationLevel.intValue()<0){
				
				if(actionAuthorizationModel.getEscalationLevel().intValue()< usecaseModel.getEscalationLevels().intValue()){
					approvedWithIntimationLevelsNext(actionAuthorizationModel,newAuthModel, usecaseModel);
				}
				else
				{
					approvedAtMaxLevel(actionAuthorizationModel, newAuthModel);
				}
				return true;
			}
			else
			{
				escalateToNextLevel(actionAuthorizationModel,newAuthModel, nextAuthorizationLevel, usecaseModel.getUsecaseId());
				return false;
			}
		}
		else
			return true;
		
	} 
	
	
	
	private String getRefDataMapString(BaseWrapper baseWrapper) throws FrameworkCheckedException{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		String mapstr =null;
		if(baseWrapper.getObject(PortalConstants.KEY_USECASE_ID).equals(1093L)){
			map.put(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING));
		}
		else {
			Long actionId = (Long) baseWrapper.getObject(PortalConstants.KEY_ACTION_ID);
			map.put(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_STRING));
			if (actionId.equals(PortalConstants.ACTION_UPDATE))
				map.put(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_OLD_MODEL_STRING));
			map.put(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS));
			map.put(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MODEL_CLASS_QUALIFIED_NAME));
			map.put(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_MANAGER_BEAN_NAME));
			map.put(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_METHODE_NAME));
			map.put(ActionAuthorizationConstantsInterface.KEY_FORM_NAME, (String) baseWrapper.getObject(ActionAuthorizationConstantsInterface.KEY_FORM_NAME));
			map.put(PortalConstants.KEY_ACTION_ID, actionId.toString());
		}
		try
		{
			mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			mapstr = mapper.writeValueAsString(map);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new FrameworkCheckedException(e.getMessage());
		} 
		
		return mapstr;
	}
	
	private BaseWrapper performActionWithAllIntimationLevels(Long nextAuthorizationLevel,String comments,String refDataModelString,UsecaseModel usecaseModel,ActionAuthorizationModel authorizationModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),usecaseModel.getEscalationLevels());
		List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
		StringBuilder checkers = new StringBuilder();

		for (LevelCheckerModel levelCheckerModel : checkerList) {
			checkers.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername());
			checkers.append(",");
		}
		checkers.deleteCharAt(checkers.lastIndexOf(","));
		
		ActionAuthorizationModel actionAuthorizationModel;
		if(null!=authorizationModel)
			actionAuthorizationModel = authorizationModel;
		else
			actionAuthorizationModel = new ActionAuthorizationModel();
		actionAuthorizationModel.setEscalationLevel(usecaseModel.getEscalationLevels());
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
		actionAuthorizationModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationModel.setCreatedOn(new Date());
		actionAuthorizationModel.setUsecaseId(usecaseModel.getUsecaseId());
		actionAuthorizationModel.setComments(comments);
		actionAuthorizationModel.setReferenceData(refDataModelString);
		actionAuthorizationModel.setIntimatedOn(new Date());
		actionAuthorizationModel.setIntimatedTo(checkers.toString());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		ActionLogModel actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,null);
		baseWrapper.putObject("actionLogModel",actionLogModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
							
		for(long i =1;i<=usecaseModel.getEscalationLevels();i++){
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());	
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			
			// Setting Checker Names/emails
			usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),i);
			checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){	
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}
				
			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());

			BaseWrapper authHistoryWrapper = new BaseWrapperImpl();
			authHistoryWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			authHistoryWrapper = actionAuthorizationHistoryManager.saveOrUpdate(authHistoryWrapper);
			
			// Sending Email Notification to all checkers
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel);
		}
		return baseWrapper;
	}
	
	
	private BaseWrapper createAuthorizationRequest(Long nextAuthorizationLevel,String comments,String refDataModelString,Long usecaseId,
												   String reqReferenceId,boolean resubmitRequest,ActionAuthorizationModel authorizationModel,
												   Long segmentId,String microBankTransactionCode) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		List<ActionAuthorizationModel> existingRequest;
		
		ActionAuthorizationModel actionAuthorizationModel;
		if(null!=authorizationModel)
			actionAuthorizationModel = authorizationModel;
		else
			actionAuthorizationModel = new ActionAuthorizationModel();

		actionAuthorizationModel.setTransactionCode(microBankTransactionCode);
		actionAuthorizationModel.setCreatedOn(new Date());
		actionAuthorizationModel.setSegmentId(segmentId);
		actionAuthorizationModel.setEscalationLevel(nextAuthorizationLevel);
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		actionAuthorizationModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationModel.setUsecaseId(usecaseId);
		actionAuthorizationModel.setComments(comments);
		actionAuthorizationModel.setReferenceData(refDataModelString);
		actionAuthorizationModel.setReferenceId(reqReferenceId);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,usecaseId);

		if(null!=reqReferenceId){
			existingRequest = actionAuthorizationModelDAO.checkExistingRequest(actionAuthorizationModel).getResultsetList();

			if(!existingRequest.isEmpty() && !resubmitRequest)
			{
				baseWrapper.putObject(ActionAuthorizationConstantsInterface.KEY_AUTHORIZATION_MSG,
						"Action authorization request already exist with  Action ID:" +existingRequest.get(0).getActionAuthorizationId().toString());
				/*actionLogModel.setOutputXml("Action authorization request already exist with  Action ID : "+ existingRequest.get(0).getActionAuthorizationId().toString());
				actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);*/
				throw new FrameworkCheckedException("Action authorization request already exist with  Action ID : "+ existingRequest.get(0).getActionAuthorizationId().toString());
			}
		}
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setCustomField11(reqReferenceId);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		baseWrapper.putObject("actionLogModel",actionLogModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		Boolean isAuthHistory = Boolean.FALSE;
		for(long i =1;i<nextAuthorizationLevel;i++){
			isAuthHistory = Boolean.TRUE;
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel(null, null, actionAuthorizationModel, i, null,null, new Date(),null);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			
			// Setting Checker Names
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,i);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}
				
			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());

			BaseWrapper modelWrapper = new BaseWrapperImpl();
			modelWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			modelWrapper = actionAuthorizationHistoryManager.saveOrUpdate(modelWrapper);
			
			// Sending Email Notification to all checkers
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel);
		}
		if(!isAuthHistory)
		{
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel(null, null, actionAuthorizationModel, 1L, null,null, new Date(),null);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);

			// Setting Checker Names
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,1L);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}

			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());

			BaseWrapper modelWrapper = new BaseWrapperImpl();
			modelWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			modelWrapper = actionAuthorizationHistoryManager.saveOrUpdate(modelWrapper);
		}
		///Send Email to Current Level Checkers
		
		if(nextAuthorizationLevel>0){
		
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,nextAuthorizationLevel);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}		
			}
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel);
		}
		///End Send Notification Email to Current Level Checkers
		
		return baseWrapper;
	}
	
	private void resolveWithIntimation(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,UsecaseModel usecaseModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,usecaseModel.getUsecaseId());
		baseWrapper.setBasePersistableModel(commandModel);
		ActionLogModel actionLogModel = actionLogManager.getActionLogModelByActionAuthId(actionAuthorizationModel.getActionAuthorizationId());//.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,null);
		UsecaseLevelModel usecaseLevelModel= null;
		List<LevelCheckerModel> checkerList = null;
		StringBuilder checkers = null;
		
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());
		
		// Setting Checker Names
		usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),actionAuthorizationModel.getEscalationLevel());
		checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
		checkers = new StringBuilder();
		for (LevelCheckerModel levelCheckerModel : checkerList) {
			checkers.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername());
			checkers.append(",");
		}
		checkers.deleteCharAt(checkers.lastIndexOf(","));
		actionAuthorizationHistoryModel.setIntimatedTo(checkers.toString());
		actionAuthorizationHistoryModel.setIntimatedOn(new Date());
		
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
		
		actionAuthorizationModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED);
		actionAuthorizationModel.setCheckedById(UserUtils.getCurrentUser().getAppUserId());
		actionAuthorizationModel.setCheckedOn(new Date());
		actionAuthorizationModel.setIntimatedOn(new Date());
		
		// Setting Checker Names
		usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),usecaseModel.getEscalationLevels());
		checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
		checkers = new StringBuilder();
		for (LevelCheckerModel levelCheckerModel : checkerList) {
			checkers.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername());
			checkers.append(",");
		}
		checkers.deleteCharAt(checkers.lastIndexOf(","));
		
		actionAuthorizationModel.setIntimatedTo(checkers.toString());
		
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);	
	  			
		for(long i =commandModel.getEscalationLevel()+1;i<=usecaseModel.getEscalationLevels();i++){
			actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED);
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());
			
			// Setting Checker Names
			usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),i);
			checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
					emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());
			

			BaseWrapper modelWrapper = new BaseWrapperImpl();
			modelWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			modelWrapper = actionAuthorizationHistoryManager.saveOrUpdate(modelWrapper);
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel);
		}
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
	}
	
	private void approvedWithIntimationLevelsNext(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,UsecaseModel usecaseModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,actionAuthorizationModel.getUsecaseId());
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		ActionLogModel actionLogModel = actionLogManager.getActionLogModelByActionAuthId(actionAuthorizationModel.getActionAuthorizationId());//prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,null);
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());
		
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
		
		actionAuthorizationModel.setEscalationLevel(usecaseModel.getEscalationLevels());
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
		actionAuthorizationModel.setIntimatedOn(new Date());
		
		//TODO reduce DB calls, List<LevelCheckerModel> getLevelCheckerModelList(usecaseId,  escalationLevel)
		// Setting Checker Names
		UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),usecaseModel.getEscalationLevels());
		List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
		StringBuilder checkers = new StringBuilder();
		for (LevelCheckerModel levelCheckerModel : checkerList) {
			checkers.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
		}
		checkers.deleteCharAt(checkers.lastIndexOf(","));
		
		actionAuthorizationModel.setIntimatedTo(checkers.toString());
		
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);	
	  			
		for(long i =commandModel.getEscalationLevel()+1;i<=usecaseModel.getEscalationLevels();i++){
			actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());
			
			// Setting Checker Names
			usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),i);
			checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}
				
			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());
			

			BaseWrapper modelWrapper = new BaseWrapperImpl();
			modelWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			modelWrapper = actionAuthorizationHistoryManager.saveOrUpdate(modelWrapper);
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel);
		}
		if(actionLogModel != null)
			actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
	}
	
	
	private void approvedAtMaxLevel(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,actionAuthorizationModel.getUsecaseId());
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		ActionLogModel actionLogModel = actionLogManager.getActionLogModelByActionAuthId(actionAuthorizationModel.getActionAuthorizationId());//prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,null);

		actionAuthorizationModel.setActionStatusId(commandModel.getActionStatusId());
		actionAuthorizationModel.setCheckedById(UserUtils.getCurrentUser().getAppUserId());
		actionAuthorizationModel.setCheckedOn(new Date());
		actionAuthorizationModel.setCheckerComments(commandModel.getCheckerComments());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);
		
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());
		//
		BaseWrapper modelWrapper = new BaseWrapperImpl();
		modelWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		modelWrapper = actionAuthorizationHistoryManager.saveOrUpdate(modelWrapper);
		//
		if(actionLogModel != null)
		{
			actionLogModel.setOutputXml("Action is authorized successfully. Changes are saved against reference Action ID : "+actionAuthorizationModel.getActionAuthorizationId().toString());
			actionLogModel = actionLogManager.completeActionLog(actionLogModel);//actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		}
	}
	
	private void escalateToNextLevel(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,
			long nextAuthorizationLevel,Long usecaseId) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,actionAuthorizationModel.getUsecaseId());
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		ActionLogModel actionLogModel = actionLogManager.getActionLogModelByActionAuthId(actionAuthorizationModel.getActionAuthorizationId());//prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,null);
		//TODO preffer parameterized constructor where possible
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());

		BaseWrapper modelWrapper =new BaseWrapperImpl();
		modelWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		modelWrapper = actionAuthorizationHistoryManager.saveOrUpdate(modelWrapper);
		
		//TODO prepare a prototype and make clones in a loop
		for(long i =commandModel.getEscalationLevel()+1;i<nextAuthorizationLevel;i++){
			actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());
			
			// Setting Checker Names
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,i);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder usernames = new StringBuilder();
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				usernames.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername()+",");
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}
				
			}
			usernames.deleteCharAt(usernames.lastIndexOf(","));
			
			actionAuthorizationHistoryModel.setIntimatedTo(usernames.toString());

			modelWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			modelWrapper = actionAuthorizationHistoryManager.saveOrUpdate(modelWrapper);
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel);
		}
					
		actionAuthorizationModel.setEscalationLevel(nextAuthorizationLevel);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionAuthorizationModel = actionAuthorizationModelDAO.saveOrUpdate(actionAuthorizationModel);
		
		///Send Email to Current Level Checkers
		
		if(nextAuthorizationLevel>0){
		
			UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseId,nextAuthorizationLevel);
			List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
			StringBuilder emailRecipients = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : checkerList) {
				//TODO use local variable 
				//TODO use reverse conditions to reduce nesting
				if( (null!=(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail())) && (!levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail().isEmpty())){
					if(GenericValidator.isEmail(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()))
						emailRecipients.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getEmail()+";");
				}		
			}
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel);
		}
		///End Send Notification Email to Current Level Checkers
		if(actionLogModel != null)
			actionLogModel = actionLogManager.completeActionLog(actionLogModel);
	}
	
	
	//****************************************************************************************************************
	
	public void setActionAuthorizationModelDAO(ActionAuthorizationModelDAO actionAuthorizationModelDAO) {
		this.actionAuthorizationModelDAO = actionAuthorizationModelDAO;
	}
	public void setActionAuthPictureModelDAO(ActionAuthPictureModelDAO actionAuthPictureModelDAO) {
		this.actionAuthPictureModelDAO = actionAuthPictureModelDAO;
	}
	public void setActionAuthorizationHistoryManager(
			ActionAuthorizationHistoryManager actionAuthorizationHistoryManager) {
		this.actionAuthorizationHistoryManager = actionAuthorizationHistoryManager;
	}
	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}
	public void setUsecaseFacade(UsecaseFacade usecaseFacade) {
		this.usecaseFacade = usecaseFacade;
	}


	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setTransactionReversalManager(TransactionReversalManager transactionReversalManager) {
		this.transactionReversalManager = transactionReversalManager;
	}

	public void setIbftSwitchController(IBFTSwitchController ibftSwitchController) {
		this.ibftSwitchController = ibftSwitchController;
	}
}
