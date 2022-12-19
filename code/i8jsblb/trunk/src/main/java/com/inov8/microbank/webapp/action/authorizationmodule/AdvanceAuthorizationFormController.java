package com.inov8.microbank.webapp.action.authorizationmodule;
/*
 * Author : Hassan Javaid
 * Date   : 13-06-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTime;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.messagemodule.EmailMessage;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormController;
import com.inov8.microbank.common.exception.EmailServiceSendFailureException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.AuthoirzationDetailEnum;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.jms.JmsProducer;
import com.inov8.microbank.server.service.portal.authorizationmodule.ActionAuthorizationHistoryManager;

abstract public class AdvanceAuthorizationFormController extends BaseFormController
{
	/**
	 * Default parameter triggering the delete. This will turn-off validation.
	 * Note: binding is still performed.
	 */
	private static final String PARAM_DELETE = "_delete";

	private String deleteParamKey = PARAM_DELETE;

	private boolean customHandler = false;
	
	protected UsecaseFacade usecaseFacade;
	protected ActionAuthorizationFacade actionAuthorizationFacade;
	protected ActionAuthorizationHistoryManager actionAuthorizationHistoryManager;
	protected ReferenceDataManager referenceDataManager;
	protected JmsProducer jmsProducer;
	protected ActionLogManager actionLogManager;
	
	//private static TokenProcessor token = TokenProcessor.getInstance();
	

	public AdvanceAuthorizationFormController()
	{
	}

	@Override
	protected final Map referenceData(HttpServletRequest request) throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("Inside referenceData method of AdvanceAuthorizationFormController");
		}

		return this.loadReferenceData(request);
	}

	protected abstract Map loadReferenceData(HttpServletRequest request) throws Exception;

	@Override
	protected final Object formBackingObject(HttpServletRequest request) throws Exception
	{
		if (log.isDebugEnabled())
		{
			log.debug("formBackingObject of AdvanceAuthorizationFormController is called");
		}
		if (!super.isFormSubmission(request))
		{
			log.debug("Form loading case identified inside formBackingObject of AdvanceAuthorizationFormController");
			return this.loadFormBackingObject(request);
		}
		else
		{
			log.debug("Form submission case identified inside formBackingObject of AdvanceAuthorizationFormController");
			return super.formBackingObject(request);
		}
	}

	protected abstract Object loadFormBackingObject(HttpServletRequest request) throws Exception;

	/*
	 * PLEASE USE HIBERNATE RELATIONS (MANY TO ONE) INSTEAD OF THIS @Override
	 * protected ModelAndView showForm(HttpServletRequest request,
	 * HttpServletResponse response, BindException errors, Map controlModel)
	 * throws Exception { ModelAndView mv =
	 * super.showForm(request,response,errors,controlModel); Object command =
	 * mv.getModel().get(this.getCommandName()); Map popupDataMap =
	 * this.loadPopupData(command); if(popupDataMap != null) {
	 * mv.getModel().putAll(popupDataMap); } return mv; } protected Map
	 * loadPopupData(Object command) throws Exception { return null; }
	 */

	@Override
	public final ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception
	{
		
		
		if (log.isDebugEnabled())
		{
			log.debug("Inside onSubmit method of AdvanceAuthorizationFormController");
		}
		if (true == this.customHandler)
		{
			log.debug("Custom Handler is defined in AdvanceAuthorizationFormController.");
			return this.onCustomHandler(request, response, command, errors);
		}

		if (ServletRequestUtils.getStringParameter(request, this.getDeleteParamKey()) != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Delete case identified inside onSubmit method of AdvanceAuthorizationFormController");
			}
			return this.onDelete(request, response, command, errors);
		}
		else if (ServletRequestUtils.getBooleanParameter(request, "isUpdate", false))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Update case identified inside onSubmit method of AdvanceAuthorizationFormController");
			}
			Boolean isParentUsecase=false;
			Long usecaseId = null;
			if (ServletRequestUtils.getBooleanParameter(request, "isManageUsecase", false)){
				usecaseId = PortalConstants.UPDATE_USECASE;
				isParentUsecase =  ServletRequestUtils.getBooleanParameter(request, "isParentUsecase", false);/// is set on in case of Update Usecase
			}	
			else 
			{	
				usecaseId= ServletRequestUtils.getLongParameter(request,"usecaseId");
				isParentUsecase =false;
			}	
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			
			if((usecaseModel.getIsAuthorizationEnable() || ServletRequestUtils.getBooleanParameter(request, "resubmitRequest", false))
					&& !isParentUsecase && (usecaseId.longValue()!=PortalConstants.BULK_MANUAL_ADJUSTMENT_USECASE_ID && usecaseId.longValue() != PortalConstants.BULK_AUTO_REVERSAL_USECASE_ID)){
				return this.onAuthorization(request, response, command, errors);
			}	
			else
				return this.onUpdate(request, response, command, errors);				
					
		}
		else if (ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false)){
			return this.onResolve(request, response, command, errors);
		}
		else if (ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false)){
			return this.onEscalate(request, response, command, errors);
		}
		else
		{	
			Boolean isParentUsecase= false;
			Long usecaseId = null;
			if (ServletRequestUtils.getBooleanParameter(request, "isManageUsecase", false)){
				usecaseId = PortalConstants.UPDATE_USECASE;
				isParentUsecase =  ServletRequestUtils.getBooleanParameter(request, "isParentUsecase", false);/// is set on in case of Update Usecase
			}	
			else 
			{	
				usecaseId= ServletRequestUtils.getLongParameter(request,"usecaseId");
				isParentUsecase = false;
			}	
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
						
			if(!UserUtils.getCurrentUser().getAppUserTypeId().equals(3L) && !isParentUsecase 
					&& (usecaseModel.getIsAuthorizationEnable() || ServletRequestUtils.getBooleanParameter(request, "resubmitRequest", false)))		
				return this.onAuthorization(request, response, command, errors);
			else
				return this.onCreate(request, response, command, errors);			
		}
	}

	
	protected  ModelAndView onEscalate(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception{return null;}
	protected ModelAndView onResolve(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception{return null;}
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception{return null;}
	
	protected ModelAndView onDelete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception
	{
		throw new RuntimeException(
				"Delete Operation is not Supported. If you do want to implement delete functionality please override onDelete method of AdvanceAuthorizationFormController");
	}

	public final ModelAndView onCustomHandler(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception
	{
		throw new RuntimeException(
				"Custom handling is not supported. If you do want to implement custom handler functionality please override onCustomHandler method of AdvanceAuthorizationFormController");
	}

	protected abstract ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception;

	protected abstract ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object command,
			BindException errors) throws Exception;


	/**
	 * This methods turns off validation if the request has the parameter with
	 * name "_delete"
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param command
	 *            Object
	 * @throws Exception
	 */
	@Override
	protected void onBind(HttpServletRequest request, Object command) throws Exception
	{
		// if the user is being deleted, turn off validation
		if (log.isDebugEnabled())
		{
			log.debug("Inside onBind method of AdvanceAuthorizationFormController");
		}
		if (request.getParameter(this.getDeleteParamKey()) != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Delete Case identified turning off validation on binding.");
			}

			super.setValidateOnBinding(false);
		}
		else
		{
			if (log.isDebugEnabled())
			{
				log.debug("Turning on validation on binding");
			}

			super.setValidateOnBinding(true);
		}
	}
	
	protected void sendNotificationEmail(String recipients,ActionAuthorizationModel model,HttpServletRequest request) throws FrameworkCheckedException{
		EmailMessage emailMessage = new EmailMessage();
		StringBuilder notificationtext= new StringBuilder();
		String detailUrl=null;
		
		emailMessage.setRecepients(recipients.split(";"));
		emailMessage.setSubject(getText("actionAuthorization.emailSubject",model.getActionAuthorizationId().toString(),request.getLocale()));
		
		if(model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.longValue()) 
			notificationtext.append(getText("actionAuthorization.emailText.actionPending",model.getEscalationLevel().toString(),request.getLocale()));
		else if(model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() || 
				model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED.longValue())
			notificationtext.append(getText("actionAuthorization.emailText.actionApproved",request.getLocale()));
		else if(model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.longValue())
			notificationtext.append(getText("actionAuthorization.emailText.actionDenied",model.getEscalationLevel().toString(),request.getLocale()));
		else if(model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.longValue())
			notificationtext.append(getText("actionAuthorization.emailText.actionResolved",request.getLocale()));
		else if(model.getActionStatusId().longValue() == ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue())
			notificationtext.append(getText("actionAuthorization.emailText.actionAssignedBack",request.getLocale()));
		
		detailUrl = AuthoirzationDetailEnum.getUrlByUsecaseId(model.getUsecaseId());
		
		notificationtext.append(getText("actionAuthorization.emailText.url",detailUrl,request.getLocale())+model.getActionAuthorizationId().toString());
		emailMessage.setText(notificationtext.toString());
		
		try{
			jmsProducer.produce(emailMessage, DestinationConstants.EMAIL_DESTINATION);
		}catch(EmailServiceSendFailureException esx){
			throw new FrameworkCheckedException(esx.getMessage());
		}
	}
	protected void escalateToNextLevel(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,long nextAuthorizationLevel,Long usecaseId,HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());
		actionAuthorizationHistoryModel.setStartTime(new Timestamp(new Date().getTime()));

		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
		
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
													
			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
					
		actionAuthorizationModel.setEscalationLevel(nextAuthorizationLevel);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);
		
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
			sendNotificationEmail(emailRecipients.toString(),(ActionAuthorizationModel) baseWrapper.getBasePersistableModel(),request);
		}
		///End Send Notification Email to Current Level Checkers
	}
	
	protected void approvedAtMaxLevel(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
		actionAuthorizationModel.setActionStatusId(commandModel.getActionStatusId());
		actionAuthorizationModel.setCheckedById(UserUtils.getCurrentUser().getAppUserId());
		actionAuthorizationModel.setCheckedOn(new Date());
		actionAuthorizationModel.setCheckerComments(commandModel.getCheckerComments());
		if(actionAuthorizationModel.getUsecaseId().equals(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID)){
			actionAuthorizationModel.setbFormPicCheckerComments(commandModel.getbFormPicCheckerComments());
			actionAuthorizationModel.setCustPicCheckerComments(commandModel.getCustPicCheckerComments());
			actionAuthorizationModel.setpNicPicCheckerComments(commandModel.getpNicPicCheckerComments());
			actionAuthorizationModel.setNicBackPicCheckerComments(commandModel.getNicBackPicCheckerComments());
			actionAuthorizationModel.setNicFrontPicCheckerComments(commandModel.getNicFrontPicCheckerComments());
			actionAuthorizationModel.setpNicBackPicCheckerComments(commandModel.getpNicBackPicCheckerComments());
		}
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);
		actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper.getBasePersistableModel();
		
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
	}
	
	protected void approvedWithIntimationLevelsNext(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,UsecaseModel usecaseModel,HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
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
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);	
	  			
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
			
								
			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
	}
	protected void resolveWithIntimation(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,UsecaseModel usecaseModel,HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
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
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);	
	  			
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
			
								
			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
	}
	protected Long createAuthorizationRequest(Long nextAuthorizationLevel,String comments,String refDataModelString,String oldRefDataModelString,Long usecaseId,String referenceId,boolean resubmitRequest,Long actionAuthorizationId,HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		List<ActionAuthorizationModel> existingRequest;
		
		ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
		if(null!=actionAuthorizationId){
			actionAuthorizationModel.setActionAuthorizationId(actionAuthorizationId);
		}	
		actionAuthorizationModel.setCreatedOn(new Date());
		actionAuthorizationModel.setEscalationLevel(nextAuthorizationLevel);
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		actionAuthorizationModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationModel.setUsecaseId(usecaseId);
		actionAuthorizationModel.setComments(comments);
		actionAuthorizationModel.setReferenceData(refDataModelString);
		actionAuthorizationModel.setOldReferenceData(oldRefDataModelString);
		actionAuthorizationModel.setReferenceId(referenceId);
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setInputXml(refDataModelString);
		actionLogModel.setCustomField11(referenceId);

		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,usecaseId);
		if(!StringUtil.isNullOrEmpty(referenceId)){
			existingRequest = actionAuthorizationFacade.checkExistingRequest(actionAuthorizationModel).getResultsetList();
			if(existingRequest != null && !existingRequest.isEmpty() && !resubmitRequest)
			{
				/*actionLogModel.setOutputXml("Action authorization request already exist with  Action ID : "+ existingRequest.get(0).getActionAuthorizationId().toString());
				actionLogModel.setActionAuthorizationId(existingRequest.get(0).getActionAuthorizationId());
				actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);*/
			}
			if(!existingRequest.isEmpty() && !resubmitRequest)
				throw new FrameworkCheckedException("Action authorization request already exist with  Action ID : "+ existingRequest.get(0).getActionAuthorizationId().toString());
		}

		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);
		actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper.getBasePersistableModel();
		Boolean isAuthHistory = Boolean.FALSE;

		for(long i =1;i<nextAuthorizationLevel;i++){
			isAuthHistory = Boolean.TRUE;
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());				
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			actionAuthorizationHistoryModel.setCheckerComments(comments);
			actionAuthorizationHistoryModel.setStartTime(new Timestamp(new Date().getTime()));
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
								
			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
			
			// Sending Email Notification to all checkers
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
		if(!isAuthHistory)
		{
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());
			actionAuthorizationHistoryModel.setEscalationLevel(1L);
			actionAuthorizationHistoryModel.setCheckerComments(comments);
			actionAuthorizationHistoryModel.setStartTime(new Timestamp(new Date().getTime()));
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

			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
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
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
		///End Send Notification Email to Current Level Checkers
		BaseWrapper modelWrapper = new BaseWrapperImpl();
		modelWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionLogModel.setOutputXml("Action is pending for approval against reference Action ID : "+actionAuthorizationModel.getActionAuthorizationId().toString());
		modelWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,actionLogModel);
		return actionAuthorizationModel.getActionAuthorizationId();
	}
	
	protected Long performActionWithAllIntimationLevels(Long nextAuthorizationLevel,String comments,String refDataModelString,String oldRefDataModelString,UsecaseModel usecaseModel,Long actionAuthorizationId, HttpServletRequest request) throws FrameworkCheckedException{
		ActionLogModel actionLogModel = new ActionLogModel();
		actionLogModel.setInputXml(refDataModelString);
		actionLogModel.setUsecaseId(usecaseModel.getUsecaseId());
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		UsecaseLevelModel usecaseLevelModel= usecaseFacade.findUsecaseLevel(usecaseModel.getUsecaseId(),usecaseModel.getEscalationLevels());
		List<LevelCheckerModel> checkerList = usecaseFacade.getLevelCheckerModelList(usecaseLevelModel.getUsecaseLevelId());
		StringBuilder checkers = new StringBuilder();

		for (LevelCheckerModel levelCheckerModel : checkerList) {
			checkers.append(levelCheckerModel.getRelationCheckerIdAppUserModel().getUsername());
			checkers.append(",");
		}
		checkers.deleteCharAt(checkers.lastIndexOf(","));
		
		ActionAuthorizationModel actionAuthorizationModel = new ActionAuthorizationModel();
		if(null!=actionAuthorizationId)
			actionAuthorizationModel.setActionAuthorizationId(actionAuthorizationId);
		actionAuthorizationModel.setEscalationLevel(usecaseModel.getEscalationLevels());
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
		actionAuthorizationModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationModel.setCreatedOn(new Date());
		actionAuthorizationModel.setUsecaseId(usecaseModel.getUsecaseId());
		actionAuthorizationModel.setComments(comments);
		actionAuthorizationModel.setReferenceData(refDataModelString);
		actionAuthorizationModel.setOldReferenceData(oldRefDataModelString);
		actionAuthorizationModel.setIntimatedOn(new Date());
		actionAuthorizationModel.setIntimatedTo(checkers.toString());
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);
		actionAuthorizationModel = (ActionAuthorizationModel) baseWrapper.getBasePersistableModel();
							
		for(long i =1;i<=usecaseModel.getEscalationLevels();i++){
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			actionAuthorizationHistoryModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED_INTIMATED);
			actionAuthorizationHistoryModel.setIntimatedOn(new Date());	
			actionAuthorizationHistoryModel.setEscalationLevel(i);
			actionAuthorizationHistoryModel.setCheckerComments(comments);
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
								
			baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
			baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
			
			// Sending Email Notification to all checkers
			
			sendNotificationEmail(emailRecipients.toString(),actionAuthorizationModel,request);
		}
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		actionLogModel.setOutputXml("Action is authorized successfully. Changes are saved against refernce Action ID : " + actionAuthorizationId);
		actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,baseWrapper,actionLogModel);
		return actionAuthorizationModel.getActionAuthorizationId();
	}
	
	protected void actionDeniedOrCancelled(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper modelWrapper = new BaseWrapperImpl();
		modelWrapper.setBasePersistableModel(actionAuthorizationModel);
		modelWrapper.putObject(PortalConstants.KEY_USECASE_ID,actionAuthorizationModel.getUsecaseId());
		modelWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID,actionAuthorizationModel.getActionAuthorizationId());
		ActionLogModel actionLogModel = actionLogManager.getActionLogModelByActionAuthId(actionAuthorizationModel.getActionAuthorizationId());//prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,null);
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_AUTH_ID, actionAuthorizationModel.getActionAuthorizationId());
		actionAuthorizationModel.setActionStatusId(commandModel.getActionStatusId());
		actionAuthorizationModel.setCheckedById(UserUtils.getCurrentUser().getAppUserId());
		actionAuthorizationModel.setCheckedOn(new Date());
		actionAuthorizationModel.setComments(commandModel.getComments());
		if(actionAuthorizationModel.getUsecaseId().equals(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID)){
			actionAuthorizationModel.setbFormPicCheckerComments(commandModel.getbFormPicCheckerComments());
			actionAuthorizationModel.setCustPicCheckerComments(commandModel.getCustPicCheckerComments());
			actionAuthorizationModel.setpNicPicCheckerComments(commandModel.getpNicPicCheckerComments());
			actionAuthorizationModel.setNicBackPicCheckerComments(commandModel.getNicBackPicCheckerComments());
			actionAuthorizationModel.setNicFrontPicCheckerComments(commandModel.getNicFrontPicCheckerComments());
			actionAuthorizationModel.setpNicBackPicCheckerComments(commandModel.getpNicBackPicCheckerComments());
		}
		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);
		modelWrapper.setBasePersistableModel(actionAuthorizationModel);
		
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(actionAuthorizationModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());
		
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);

		if(actionLogModel != null)
			actionLogModel = actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(null,modelWrapper,actionLogModel);

		if((null!=actionAuthorizationModel.getCreatedByAppUserModel().getEmail()) && GenericValidator.isEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail()))
			sendNotificationEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail(),actionAuthorizationModel,request);	
	}
	
	protected void requestAssignedBack(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel,HttpServletRequest request) throws FrameworkCheckedException{
		BaseWrapper baseWrapper= new BaseWrapperImpl();
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK);
		actionAuthorizationModel.setEscalationLevel(new Long(0));

		if(actionAuthorizationModel.getUsecaseId().equals(PortalConstants.MFS_MINOR_ACCOUNT_UPDATE_USECASE_ID)){
			actionAuthorizationModel.setbFormPicCheckerComments(commandModel.getbFormPicCheckerComments());
			actionAuthorizationModel.setCustPicCheckerComments(commandModel.getCustPicCheckerComments());
			actionAuthorizationModel.setpNicPicCheckerComments(commandModel.getpNicPicCheckerComments());
			actionAuthorizationModel.setNicBackPicCheckerComments(commandModel.getNicBackPicCheckerComments());
			actionAuthorizationModel.setNicFrontPicCheckerComments(commandModel.getNicFrontPicCheckerComments());
			actionAuthorizationModel.setpNicBackPicCheckerComments(commandModel.getpNicBackPicCheckerComments());
		}

		baseWrapper.setBasePersistableModel(actionAuthorizationModel);
		baseWrapper = actionAuthorizationFacade.saveOrUpdate(baseWrapper);

		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel= new ActionAuthorizationHistoryModel();
		actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
		actionAuthorizationHistoryModel.setActionStatusIdActionStatusModel(commandModel.getActionStatusIdActionStatusModel());
		actionAuthorizationHistoryModel.setCheckedOn(new Date());
		actionAuthorizationHistoryModel.setCheckedByAppUserModel(UserUtils.getCurrentUser());
		actionAuthorizationHistoryModel.setEscalationLevel(commandModel.getEscalationLevel());
		actionAuthorizationHistoryModel.setCheckerComments(commandModel.getCheckerComments());
		
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		baseWrapper = actionAuthorizationHistoryManager.saveOrUpdate(baseWrapper);
		
		if((null!=actionAuthorizationModel.getCreatedByAppUserModel().getEmail()) && GenericValidator.isEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail()))
			sendNotificationEmail(actionAuthorizationModel.getCreatedByAppUserModel().getEmail(),actionAuthorizationModel,request);			
	}

	public String getDeleteParamKey()
	{
		return deleteParamKey;
	}

	public boolean isCustomHandler()
	{
		return customHandler;
	}

	public void setDeleteParamKey(String deleteParamKey)
	{
		this.deleteParamKey = deleteParamKey;
	}

	public void setCustomHandler(boolean customHandler)
	{
		this.customHandler = customHandler;
	}
	
	public void setUsecaseFacade(UsecaseFacade usecaseFacade) {
		this.usecaseFacade = usecaseFacade;
	}
	public void setActionAuthorizationFacade(ActionAuthorizationFacade actionAuthorizationFacade) {
		this.actionAuthorizationFacade = actionAuthorizationFacade;
	}
	public void setActionAuthorizationHistoryManager(
			ActionAuthorizationHistoryManager actionAuthorizationHistoryManager) {
		this.actionAuthorizationHistoryManager = actionAuthorizationHistoryManager;
	}
	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}
	public void setJmsProducer(JmsProducer jmsProducer) {
		this.jmsProducer = jmsProducer;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}
}
