package com.inov8.microbank.webapp.action.portal.usecasemodule;
/*
 * Author : Hassan Javaid
 * Date   : 21-01-2015
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.UsecaseLevelRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.UsecaseReferenceDataModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class UsecaseAuthorizationFormController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( UsecaseAuthorizationFormController.class );
		
	private UserManagementManager	userManagementManager;
	
	public UsecaseAuthorizationFormController() {
		setCommandName("actionAuthorizationModel");
		setCommandClass(ActionAuthorizationModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);
		
		if (escalateRequest || resolveRequest){				
			ActionStatusModel actionStatusModel = new ActionStatusModel();
			ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( actionStatusModel, "name", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );
			List<ActionStatusModel> actionStatusModelList;
			actionStatusModelList=refDataWrapper.getReferenceDataList();
			List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();
			
			for (ActionStatusModel actionStatusModel2 :  actionStatusModelList) {
				if(((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.intValue())
						||(actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL.intValue()) 
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.intValue())																		
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVAL_DENIED.intValue()) 
						//|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.intValue())
						|| (actionStatusModel2.getActionStatusId().intValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.intValue()))
						&& escalateRequest )
					tempActionStatusModelList.add(actionStatusModel2);
				else if((actionStatusModel2.getActionStatusId().intValue()== ActionAuthorizationConstantsInterface.ACTION_STATUS_RESOLVED.intValue()) && resolveRequest)
					tempActionStatusModelList.add(actionStatusModel2);
			}		
			referenceDataMap.put( "actionStatusModel", tempActionStatusModelList);
			
			////// Action Authorization history////
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = new ActionAuthorizationHistoryModel();
			actionAuthorizationHistoryModel.setActionAthorizationIdActionAuthorizationModel(actionAuthorizationModel);
			
			List<ActionAuthorizationHistoryModel> actionAuthorizationHistoryModelList;
			
			refDataWrapper = new ReferenceDataWrapperImpl( actionAuthorizationHistoryModel, "actionAuthHistoryId", SortingOrder.ASC );
			referenceDataManager.getReferenceData( refDataWrapper );
			
			actionAuthorizationHistoryModelList=refDataWrapper.getReferenceDataList();
			
			referenceDataMap.put( "actionAuthorizationHistoryModelList",actionAuthorizationHistoryModelList );
			
			if(actionAuthorizationModel.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue()
					&& actionAuthorizationModel.getCreatedById().longValue()== UserUtils.getCurrentUser().getAppUserId()){
				boolean isAssignedBack=false;
				isAssignedBack=true;
				request.setAttribute( "isAssignedBack",isAssignedBack );
			}
		}
		return referenceDataMap;		
	}


	@Override
	protected Object loadFormBackingObject(HttpServletRequest request) throws Exception {
		
		boolean escalateRequest = ServletRequestUtils.getBooleanParameter(request, "escalateRequest", false);
		boolean resolveRequest = ServletRequestUtils.getBooleanParameter(request, "resolveRequest", false);
		
		if (escalateRequest || resolveRequest){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			XStream xstream = new XStream();
			UsecaseReferenceDataModel usecaseModel = (UsecaseReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("usecaseModel",usecaseModel);
			
			
			/// Current usecase Model
			UsecaseReferenceDataModel oldUsecaseReferenceDataModel = (UsecaseReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getOldReferenceData());					
			request.setAttribute("currentUsecaseModel",oldUsecaseReferenceDataModel);
			/// Populating Current usecase Model
			
			/// Setting User Names of Checker
			
			
			if(usecaseModel.getEscalationLevels()>0){	
				String[] levelCheckerNames = new String[10];
				for(int i=0;i<usecaseModel.getEscalationLevels();i++ ){
					StringBuilder names = new StringBuilder();
					String[] levelCheckerIds =  usecaseModel.getLevelcheckers()[i].split(",");	
					for(int j=0; j< levelCheckerIds.length;j++){			
						BaseWrapper baseWrapper= new BaseWrapperImpl();
						AppUserModel appUserModel = new AppUserModel();
						appUserModel.setAppUserId(Long.parseLong(levelCheckerIds[j]));
						baseWrapper.setBasePersistableModel(appUserModel);
						appUserModel= (AppUserModel) userManagementManager.searchAppUserByPrimaryKey(baseWrapper).getBasePersistableModel();
						if(null!=appUserModel){
							if(j>0)
								names=names.append(",") ;
							names=names.append(appUserModel.getUsername()) ;
						}
					}
					levelCheckerNames[i]=names.toString();
				}  
				usecaseModel.setLevelcheckers(levelCheckerNames);
			}
			///End Setting User Names of Checker
						
			return actionAuthorizationModel;
		}
		else 
			return new ActionAuthorizationModel();
	}


	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException errors) throws Exception
	{
		return null;		
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object,
			BindException errors) throws Exception
	{
				
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
		if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_APPROVED.longValue() && actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().longValue()==currentUserId)){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			
			long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(),actionAuthorizationModel.getEscalationLevel());
			if(nextAuthorizationLevel<1){
				
				XStream xstream = new XStream();
				UsecaseReferenceDataModel usecaseReferenceDataModel = (UsecaseReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.UPDATE_USECASE);
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				
				UsecaseModel usecaseModelCommand = populateUsecaseModel(usecaseReferenceDataModel);
				usecaseModelCommand.setUpdatedBy(actionAuthorizationModel.getCreatedById());
				usecaseModelCommand.setUpdatedOn(actionAuthorizationModel.getCreatedOn());
				List<UsecaseLevelModel> usecaseLevelModelList = new ArrayList<UsecaseLevelModel>(); 
				//Populate Bank Users Map
				
				Map<String,LevelCheckerModel> levelCheckerModelMap = new LinkedHashMap();
				List<AppUserModel> appUserModelList = null;
				AppUserModel appUserModel = new AppUserModel();
				appUserModel.setAppUserTypeId(6L); // For loading Bank AppUserModels
				appUserModel.setAccountClosedSettled(false);
				appUserModel.setAccountClosedUnsettled(false);
				 
				ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(appUserModel, "username", SortingOrder.ASC);
				referenceDataManager.getReferenceData(referenceDataWrapper);
				  
				 
			  if (referenceDataWrapper.getReferenceDataList() != null)
			  {
				  appUserModelList = referenceDataWrapper.getReferenceDataList();
			  }
			  LevelCheckerModel	tempLevelCheckerModel;
			  
			  for (AppUserModel temAppUserModel : appUserModelList) {
				  
				  tempLevelCheckerModel= new LevelCheckerModel();
				  tempLevelCheckerModel.setCheckerIdAppUserModel(temAppUserModel);
				  levelCheckerModelMap.put(tempLevelCheckerModel.getCheckerIdAppUserModel().getAppUserId().toString(), tempLevelCheckerModel);
			  }
				
				//End Populate Bank Users Map 
				
					baseWrapper.putObject("checkerMap",(Serializable) levelCheckerModelMap);
							
					//UsecaseModel
					//usecaseModelCommand.setUpdatedOn(new Date());
					//usecaseModelCommand.setUpdatedByAppUserModel(UserUtils.getCurrentUser());	
					usecaseModelCommand.setIsAuthorizationEnable((Boolean) (usecaseModelCommand.getIsAuthorizationEnable() == null ? false : usecaseModelCommand.getIsAuthorizationEnable()));
					baseWrapper.setBasePersistableModel(usecaseModelCommand);
						
					//Adjust Usecase levels 
					
					
					for (UsecaseLevelModel usecaseLevelModel: usecaseModelCommand.getUsecaseIdLevelModelList()) {
						if(usecaseLevelModel.getLevelNo()<=usecaseModelCommand.getEscalationLevels())
							usecaseLevelModelList.add(usecaseLevelModel);
					}
					
					usecaseModelCommand.setUsecaseIdLevelModelList(usecaseLevelModelList);
					baseWrapper = this.usecaseFacade.saveOrUpdateUsecase(baseWrapper);
						
				
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
			isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}	
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
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.longValue()
				&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK)){
			
			if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("You are not authorized to update action status.");
			}
			
			
		
		}
		else{
			
			throw new FrameworkCheckedException("Invalid status marked");
		}
		
	}
	catch (Exception ex)
	{			
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		request.setAttribute("message",MessageUtil.getMessage("6075"));
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);  	
	}
	request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
    modelAndView = super.showForm(request, response, errors);
    return modelAndView; 
	}
	

	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		
		return null;
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
			UsecaseReferenceDataModel usecaseReferenceDataModel = (UsecaseReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID,PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID,PortalConstants.UPDATE_USECASE);
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());

			UsecaseModel usecaseModelCommand = populateUsecaseModel(usecaseReferenceDataModel);
			usecaseModelCommand.setUpdatedBy(actionAuthorizationModel.getCreatedById());
			usecaseModelCommand.setUpdatedOn(actionAuthorizationModel.getCreatedOn());
			List<UsecaseLevelModel> usecaseLevelModelList = new ArrayList<UsecaseLevelModel>(); 
			//Populate Bank Users Map
			
			Map<String,LevelCheckerModel> levelCheckerModelMap = new LinkedHashMap();
			List<AppUserModel> appUserModelList = null;
			AppUserModel appUserModel = new AppUserModel();
			appUserModel.setAppUserTypeId(6L); // For loading Bank AppUserModels
			appUserModel.setAccountClosedSettled(false);
			appUserModel.setAccountClosedUnsettled(false);
			 
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(appUserModel, "username", SortingOrder.ASC);
			referenceDataManager.getReferenceData(referenceDataWrapper);
			  
			 
		  if (referenceDataWrapper.getReferenceDataList() != null)
		  {
			  appUserModelList = referenceDataWrapper.getReferenceDataList();
		  }
		  LevelCheckerModel	tempLevelCheckerModel;
		  
		  for (AppUserModel temAppUserModel : appUserModelList) {
			  
			  tempLevelCheckerModel= new LevelCheckerModel();
			  tempLevelCheckerModel.setCheckerIdAppUserModel(temAppUserModel);
			  levelCheckerModelMap.put(tempLevelCheckerModel.getCheckerIdAppUserModel().getAppUserId().toString(), tempLevelCheckerModel);
		  }
			
			//End Populate Bank Users Map 
			
			baseWrapper.putObject("checkerMap",(Serializable) levelCheckerModelMap);
					
			//UsecaseModel
			//usecaseModelCommand.setUpdatedOn(new Date());
			//usecaseModelCommand.setUpdatedByAppUserModel(UserUtils.getCurrentUser());	
			usecaseModelCommand.setIsAuthorizationEnable((Boolean) (usecaseModelCommand.getIsAuthorizationEnable() == null ? false : usecaseModelCommand.getIsAuthorizationEnable()));
			baseWrapper.setBasePersistableModel(usecaseModelCommand);
				
			//Adjust Usecase levels 
			
			
			for (UsecaseLevelModel usecaseLevelModel: usecaseModelCommand.getUsecaseIdLevelModelList()) {
				if(usecaseLevelModel.getLevelNo()<=usecaseModelCommand.getEscalationLevels())
					usecaseLevelModelList.add(usecaseLevelModel);
			}
			
			usecaseModelCommand.setUsecaseIdLevelModelList(usecaseLevelModelList);
			baseWrapper = this.usecaseFacade.saveOrUpdateUsecase(baseWrapper);
					
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel,request);
			
		}
		catch (Exception ex)
		{	
			
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
			request.setAttribute("message",MessageUtil.getMessage("6075"));
	    	return super.showForm(request, response, errors);
		}
		request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
	    modelAndView = super.showForm(request, response, errors);	
		return modelAndView;	
	}
		
	private UsecaseModel populateUsecaseModel(UsecaseReferenceDataModel refDataModel) throws Exception{
		
		UsecaseModel usecaseModel = new UsecaseModel();
		
		usecaseModel.setComments(refDataModel.getComments());
		usecaseModel.setDescription(refDataModel.getDescription());
		usecaseModel.setEscalationLevels(refDataModel.getEscalationLevels());
		usecaseModel.setIsAuthorizationEnable(refDataModel.getIsAuthorizationEnable());
		usecaseModel.setLevelcheckers(refDataModel.getLevelcheckers());
		usecaseModel.setName(refDataModel.getName());
		usecaseModel.setUsecaseId(refDataModel.getUsecaseId());
		usecaseModel.setVersionNo(refDataModel.getVersionNo());
		
		usecaseModel.setCreatedBy(refDataModel.getCreatedBy());
		usecaseModel.setCreatedOn(refDataModel.getCreatedOn());
		usecaseModel.setUpdatedBy(refDataModel.getUpdatedBy());
		usecaseModel.setUpdatedOn(refDataModel.getUpdatedOn());
				
		if(refDataModel.getEscalationLevels()>0){
			
			for (UsecaseLevelRefDataModel usecaseLevelRefDataModel : refDataModel.getUsecaseIdLevelModelList()) {
				
				UsecaseLevelModel usecaseLevelModel = new UsecaseLevelModel();
				usecaseLevelModel.setIntimateOnly(usecaseLevelRefDataModel.getIntimateOnly());
				usecaseLevelModel.setLevelNo(usecaseLevelRefDataModel.getLevelNo());
				usecaseLevelModel.setUsecaseId(usecaseLevelRefDataModel.getUsecaseId());
				usecaseLevelModel.setUsecaseLevelId(usecaseLevelRefDataModel.getUsecaseLevelId());
				
				usecaseModel.getUsecaseIdLevelModelList().add(usecaseLevelModel);
			}
		}	
		return usecaseModel;
	}
	
	
	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}	
}
