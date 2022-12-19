package com.inov8.microbank.webapp.action.portal.partnergroupmodule;
/*
 * Author : Hassan Javaid
 * Date   : 23-09-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.PartnerGroupDetailModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.UserGroupReferenceDataModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerPermissionViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.UserPermissionWrapper;
import com.inov8.microbank.common.model.portal.usermanagementmodule.UserManagementModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class PartnerGroupAuthorizationDetailController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( PartnerGroupAuthorizationDetailController.class );
	private ReferenceDataManager referenceDataManager;
	private PartnerGroupManager partnerGroupManager;
		
	public PartnerGroupAuthorizationDetailController() {
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
			
			refDataWrapper = new ReferenceDataWrapperImpl( actionAuthorizationHistoryModel, "escalationLevel", SortingOrder.ASC );
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
			UserGroupReferenceDataModel userGroupReferenceDataModel = (UserGroupReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			request.setAttribute("groupName",userGroupReferenceDataModel.getName());

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
				throw new FrameworkCheckedException("partnerGroupModule.notAuthorized");
			}
			
			long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(actionAuthorizationModel.getUsecaseId(),actionAuthorizationModel.getEscalationLevel());
			if(nextAuthorizationLevel<1){
				
				
				XStream xstream = new XStream();
				UserGroupReferenceDataModel userGroupReferenceDataModel = (UserGroupReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
					
				baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
				baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
				baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
				
				if (usecaseModel.getUsecaseId().longValue()==PortalConstants.USER_GROUP_CREATE_USECASE_ID){
					
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

					
					PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
							
					partnerGroupModel.setPartnerId(userGroupReferenceDataModel.getPartnerId());
					partnerGroupModel.setName(userGroupReferenceDataModel.getName());
					partnerGroupModel.setDescription(userGroupReferenceDataModel.getDescription());
					partnerGroupModel.setActive(userGroupReferenceDataModel.getActive() == null ? false : userGroupReferenceDataModel.getActive());
					partnerGroupModel.setEditable(true);
					partnerGroupModel.setEmail(userGroupReferenceDataModel.getEmail());
					partnerGroupModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupModel.setCreatedOn(new Date());
					partnerGroupModel.setUpdatedOn(new Date());
					Set<Long> permission = new HashSet<Long>();
									
					Iterator<UserPermissionWrapper> itr = userGroupReferenceDataModel.getUserPermissionList().iterator();
					while(itr.hasNext())
					{
						UserPermissionWrapper up = itr.next();
						PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
						partnerGroupDetailModel.setUserPermissionId(up.getPermissionId());
						partnerGroupDetailModel.setReadAllowed(up.isReadAllowed());
						partnerGroupDetailModel.setUpdateAllowed(up.isUpdateAllowed());
						partnerGroupDetailModel.setDeleteAllowed(up.isDeleteAllowed());
						partnerGroupDetailModel.setCreateAllowed(up.isCreateAllowed());
						partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
						partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
						partnerGroupDetailModel.setCreatedOn(new Date());
						partnerGroupDetailModel.setUpdatedOn(new Date());
						
						if(permission.add(partnerGroupDetailModel.getUserPermissionId()));
						{
							partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);
						}
					}				
					
					PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
					partnerPermissionViewModel.setPartnerId(partnerGroupModel.getPartnerId());
					partnerPermissionViewModel.setIsDefault(true);//Load Default permissions like home page & change password
					SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
					searchBaseWrapper.setBasePersistableModel(partnerPermissionViewModel);
					searchBaseWrapper = this.partnerGroupManager.searchDefaultPartnerPermission(searchBaseWrapper);
					Iterator<PartnerPermissionViewModel> itrDP = searchBaseWrapper.getCustomList().getResultsetList().iterator();
					while(itrDP.hasNext())
					{
						PartnerPermissionViewModel ppModel = itrDP.next();
						PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
						partnerGroupDetailModel.setUserPermissionId(ppModel.getUserPermissionId());
						partnerGroupDetailModel.setReadAllowed(ppModel.getReadAvailable());
						partnerGroupDetailModel.setUpdateAllowed(ppModel.getUpdateAvailable());
						partnerGroupDetailModel.setDeleteAllowed(ppModel.getDeleteAvailable());
						partnerGroupDetailModel.setCreateAllowed(ppModel.getCreateAvailable());
						partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
						partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
						partnerGroupDetailModel.setCreatedOn(new Date());
						partnerGroupDetailModel.setUpdatedOn(new Date());
						
						if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
						{
							partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
						}	
					}
					
					baseWrapper.setBasePersistableModel(partnerGroupModel);
					baseWrapper = this.partnerGroupManager.createPartnerGroup(baseWrapper);
					
				}
				else
				{
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_UPDATE_USECASE_ID));
					
					PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
					partnerGroupModel.setPartnerGroupId(userGroupReferenceDataModel.getPartnerGroupId());
					baseWrapper.setBasePersistableModel(partnerGroupModel);
					baseWrapper = this.partnerGroupManager.loadPartnerGroup(baseWrapper);
					partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
								
					partnerGroupModel.setDescription(userGroupReferenceDataModel.getDescription());
					partnerGroupModel.setActive(userGroupReferenceDataModel.getActive() == null ? false : userGroupReferenceDataModel.getActive());
					partnerGroupModel.setEmail(userGroupReferenceDataModel.getEmail());
					partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());		
					partnerGroupModel.setUpdatedOn(new Date());
											
					Iterator<PartnerGroupDetailModel> itr = partnerGroupModel.getPartnerGroupIdPartnerGroupDetailModelList().iterator();		
					List<UserPermissionWrapper> upList = userGroupReferenceDataModel.getUserPermissionList();
					while(itr.hasNext())
					{
						PartnerGroupDetailModel partnerGroupDetailModel = itr.next();	
					
						for(int i = 0; i < upList.size(); i++)
						{
							UserPermissionWrapper up = upList.get(i);
							if(partnerGroupDetailModel.getUserPermissionId().equals(up.getPermissionId()))
							{					
								partnerGroupDetailModel.setReadAllowed(up.isReadAllowed());
								partnerGroupDetailModel.setUpdateAllowed(up.isUpdateAllowed());
								partnerGroupDetailModel.setDeleteAllowed(up.isDeleteAllowed());
								partnerGroupDetailModel.setCreateAllowed(up.isCreateAllowed());
								partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
								partnerGroupDetailModel.setUpdatedOn(new Date());
								break;
							}
						}
						
					}
					
					baseWrapper.setBasePersistableModel(partnerGroupModel);
					baseWrapper = this.partnerGroupManager.updatePartnerGroup(baseWrapper);
				}

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
				throw new FrameworkCheckedException("partnerGroupModule.notAuthorized");
			}	
			actionDeniedOrCancelled(actionAuthorizationModel, model,request);
		}
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_CANCELLED.longValue() 
				&& (actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)
				|| actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK))){
			
			if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("partnerGroupModule.notAuthorized");
			}
			actionDeniedOrCancelled(actionAuthorizationModel,model,request);
		}
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK.longValue() 
				&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL)){
			isValidChecker  = usecaseFacade.isCheckerAtLevel(actionAuthorizationModel.getUsecaseId(), actionAuthorizationModel.getEscalationLevel(),UserUtils.getCurrentUser().getAppUserId());

			if((!isValidChecker) || (actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("partnerGroupModule.notAuthorized");
			}
			requestAssignedBack(actionAuthorizationModel,model,request);
		} 
		else if(model.getActionStatusId().longValue()==ActionAuthorizationConstantsInterface.ACTION_STATUS_RE_SUBMIT.longValue()
				&& actionAuthorizationModel.getActionStatusId().equals(ActionAuthorizationConstantsInterface.ACTION_STATUS_ASIGNED_BACK)){
			
			if(!(actionAuthorizationModel.getCreatedById().equals(currentUserId))){
				throw new FrameworkCheckedException("partnerGroupModule.notAuthorized");
			}
			
			
		
		}
		else{
			
			throw new FrameworkCheckedException("Invalid status marked");
		}
		
	}
	catch (FrameworkCheckedException ex)
	{	
		if(ex.getMessage().equals(super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale())))
    	{
    		request.setAttribute("message", super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale()));
    	}
    	else if("Partnergroupcannotbedeletedbecausegroupisnotempty".equals(ex.getMessage())){
    		request.setAttribute("message", super.getText("appUserPartnerGroup.Partnergroupcannotbedeletedbecausegroupisnotempty", request.getLocale()));
    	}
    	else if ("ConstraintViolationException".equals(ex.getMessage()) || "DataIntegrityViolationException".equals(ex.getMessage()))
		{
    		request.setAttribute("message", super.getText("partnerGroupModule.partnerGroupNotUnique", request.getLocale()));
		}
    	else if ("partnerGroupModule.notAuthorized".equals(ex.getMessage()))
		{
    		request.setAttribute("message", super.getText("partnerGroupModule.notAuthorized", request.getLocale()));
		
		}
		else
		{
			this.saveMessage(request, super.getText("partnerGroupModule.recordUnSaveSuccessful", request.getLocale()));
		}
			
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
		//request.setAttribute("message", ex.getMessage());
		request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
    	return super.showForm(request, response, errors);
	}catch (Exception ex)
	{	
		this.saveMessage(request, MessageUtil.getMessage("6075"));	
		LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
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
			UserGroupReferenceDataModel userGroupReferenceDataModel = (UserGroupReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
				
			baseWrapper.putObject(PortalConstants.KEY_APP_USER_ID,actionAuthorizationModel.getCreatedById());
			baseWrapper.putObject(PortalConstants.KEY_CREATED_ON,actionAuthorizationModel.getCreatedOn());
			baseWrapper.putObject(PortalConstants.KEY_APPUSER_USERNAME,actionAuthorizationModel.getCreatedByUsername());
			
			if (usecaseModel.getUsecaseId().longValue()==PortalConstants.USER_GROUP_CREATE_USECASE_ID){
				
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

				
				PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
						
				partnerGroupModel.setPartnerId(userGroupReferenceDataModel.getPartnerId());
				partnerGroupModel.setName(userGroupReferenceDataModel.getName());
				partnerGroupModel.setDescription(userGroupReferenceDataModel.getDescription());
				partnerGroupModel.setActive(userGroupReferenceDataModel.getActive() == null ? false : userGroupReferenceDataModel.getActive());
				partnerGroupModel.setEditable(true);
				partnerGroupModel.setEmail(userGroupReferenceDataModel.getEmail());
				partnerGroupModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				partnerGroupModel.setCreatedOn(new Date());
				partnerGroupModel.setUpdatedOn(new Date());
				Set<Long> permission = new HashSet<Long>();
								
				Iterator<UserPermissionWrapper> itr = userGroupReferenceDataModel.getUserPermissionList().iterator();
				while(itr.hasNext())
				{
					UserPermissionWrapper up = itr.next();
					PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
					partnerGroupDetailModel.setUserPermissionId(up.getPermissionId());
					partnerGroupDetailModel.setReadAllowed(up.isReadAllowed());
					partnerGroupDetailModel.setUpdateAllowed(up.isUpdateAllowed());
					partnerGroupDetailModel.setDeleteAllowed(up.isDeleteAllowed());
					partnerGroupDetailModel.setCreateAllowed(up.isCreateAllowed());
					partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupDetailModel.setCreatedOn(new Date());
					partnerGroupDetailModel.setUpdatedOn(new Date());
					
					if(permission.add(partnerGroupDetailModel.getUserPermissionId()));
					{
						partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);
					}
				}				
				
				PartnerPermissionViewModel partnerPermissionViewModel = new PartnerPermissionViewModel();
				partnerPermissionViewModel.setPartnerId(partnerGroupModel.getPartnerId());
				partnerPermissionViewModel.setIsDefault(true);//Load Default permissions like home page & change password
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				searchBaseWrapper.setBasePersistableModel(partnerPermissionViewModel);
				searchBaseWrapper = this.partnerGroupManager.searchDefaultPartnerPermission(searchBaseWrapper);
				Iterator<PartnerPermissionViewModel> itrDP = searchBaseWrapper.getCustomList().getResultsetList().iterator();
				while(itrDP.hasNext())
				{
					PartnerPermissionViewModel ppModel = itrDP.next();
					PartnerGroupDetailModel partnerGroupDetailModel = new PartnerGroupDetailModel();
					partnerGroupDetailModel.setUserPermissionId(ppModel.getUserPermissionId());
					partnerGroupDetailModel.setReadAllowed(ppModel.getReadAvailable());
					partnerGroupDetailModel.setUpdateAllowed(ppModel.getUpdateAvailable());
					partnerGroupDetailModel.setDeleteAllowed(ppModel.getDeleteAvailable());
					partnerGroupDetailModel.setCreateAllowed(ppModel.getCreateAvailable());
					partnerGroupDetailModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupDetailModel.setCreatedOn(new Date());
					partnerGroupDetailModel.setUpdatedOn(new Date());
					
					if(permission.add(partnerGroupDetailModel.getUserPermissionId()))
					{
						partnerGroupModel.addPartnerGroupIdPartnerGroupDetailModel(partnerGroupDetailModel);//add default permissions like home page and change password
					}	
				}
				
				baseWrapper.setBasePersistableModel(partnerGroupModel);
				baseWrapper = this.partnerGroupManager.createPartnerGroup(baseWrapper);
				
			}
			else
			{
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_UPDATE_USECASE_ID));
				
				PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
				partnerGroupModel.setPartnerGroupId(userGroupReferenceDataModel.getPartnerGroupId());
				baseWrapper.setBasePersistableModel(partnerGroupModel);
				baseWrapper = this.partnerGroupManager.loadPartnerGroup(baseWrapper);
				partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
							
				partnerGroupModel.setDescription(userGroupReferenceDataModel.getDescription());
				partnerGroupModel.setActive(userGroupReferenceDataModel.getActive() == null ? false : userGroupReferenceDataModel.getActive());
				partnerGroupModel.setEmail(userGroupReferenceDataModel.getEmail());
				partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());		
				partnerGroupModel.setUpdatedOn(new Date());
										
				Iterator<PartnerGroupDetailModel> itr = partnerGroupModel.getPartnerGroupIdPartnerGroupDetailModelList().iterator();		
				List<UserPermissionWrapper> upList = userGroupReferenceDataModel.getUserPermissionList();
				while(itr.hasNext())
				{
					PartnerGroupDetailModel partnerGroupDetailModel = itr.next();	
				
					for(int i = 0; i < upList.size(); i++)
					{
						UserPermissionWrapper up = upList.get(i);
						if(partnerGroupDetailModel.getUserPermissionId().equals(up.getPermissionId()))
						{					
							partnerGroupDetailModel.setReadAllowed(up.isReadAllowed());
							partnerGroupDetailModel.setUpdateAllowed(up.isUpdateAllowed());
							partnerGroupDetailModel.setDeleteAllowed(up.isDeleteAllowed());
							partnerGroupDetailModel.setCreateAllowed(up.isCreateAllowed());
							partnerGroupDetailModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
							partnerGroupDetailModel.setUpdatedOn(new Date());
							break;
						}
					}
					
				}
				
				baseWrapper.setBasePersistableModel(partnerGroupModel);
				baseWrapper = this.partnerGroupManager.updatePartnerGroup(baseWrapper);
			}
	
			resolveWithIntimation(actionAuthorizationModel,model, usecaseModel,request);
			
		}
		catch (FrameworkCheckedException ex)
		{			
			if(ex.getMessage().equals(super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale())))
	    	{
	    		request.setAttribute("message", super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale()));
	    	}
	    	else if("Partnergroupcannotbedeletedbecausegroupisnotempty".equals(ex.getMessage())){
	    		request.setAttribute("message", super.getText("appUserPartnerGroup.Partnergroupcannotbedeletedbecausegroupisnotempty", request.getLocale()));
	    	}
	    	else if ("ConstraintViolationException".equals(ex.getMessage()) || "DataIntegrityViolationException".equals(ex.getMessage()))
			{
	    		request.setAttribute("message", super.getText("partnerGroupModule.partnerGroupNotUnique", request.getLocale()));
			}
			else
			{
				this.saveMessage(request, super.getText("partnerGroupModule.recordUnSaveSuccessful", request.getLocale()));
			}
			
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			//request.setAttribute("message", ex.getMessage());
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}catch (Exception ex)
		{	
			this.saveMessage(request, MessageUtil.getMessage("6075"));	
			LOGGER.error("Exception occured while Action Authorization on Action Authorization ID : "+model.getActionAuthorizationId(), ex);
			request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	    	return super.showForm(request, response, errors);
		}
		request.setAttribute("status",IssueTypeStatusConstantsInterface.SUCCESS);
	    modelAndView = super.showForm(request, response, errors);	
		return modelAndView;	
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}
}
