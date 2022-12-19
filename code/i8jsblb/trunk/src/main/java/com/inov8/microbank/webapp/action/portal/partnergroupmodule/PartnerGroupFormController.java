package com.inov8.microbank.webapp.action.portal.partnergroupmodule;

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
import com.inov8.microbank.common.model.AppUserTypeModel;
import com.inov8.microbank.common.model.PartnerGroupDetailModel;
import com.inov8.microbank.common.model.PartnerGroupModel;
import com.inov8.microbank.common.model.PartnerModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.UserGroupReferenceDataModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.AppUserPartnerViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerGroupPermissionListViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerPermissionViewModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.UserPermissionWrapper;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.partnergroupmodule.PartnerGroupManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class PartnerGroupFormController extends AdvanceAuthorizationFormController{

	
	private ReferenceDataManager referenceDataManager;
	private PartnerGroupManager partnerGroupManager;
	
	public PartnerGroupFormController() {
		setCommandName("partnerGroupListViewModel");
		setCommandClass(PartnerGroupPermissionListViewModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception {
		
		Long id = ServletRequestUtils.getLongParameter(httpServletRequest,"partnerGroupId");
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(httpServletRequest, "isReSubmit",false);
		boolean isReadOnly = ServletRequestUtils.getBooleanParameter(httpServletRequest, "isReadOnly",false);
		boolean isNew = ServletRequestUtils.getBooleanParameter(httpServletRequest, "isNew",false);
		PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel = new PartnerGroupPermissionListViewModel();
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		UserGroupReferenceDataModel userGroupReferenceDataModel = null;
				
		/// Added for Resubmit Authorization Request 
		if(isReSubmit || isReadOnly){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(httpServletRequest,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if((actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()) && isReSubmit){
				throw new FrameworkCheckedException("illegal operation performed");
			}
	 	
			XStream xstream = new XStream();
			
			if(isNew)
				userGroupReferenceDataModel = (UserGroupReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			else
				userGroupReferenceDataModel = (UserGroupReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getOldReferenceData());

			
			id = userGroupReferenceDataModel.getPartnerGroupId();
			
			httpServletRequest.setAttribute("partnerGroupId", userGroupReferenceDataModel.getPartnerGroupId());
		}
		///End Added for Resubmit Authorization Request
		
		if (null != id) {// update case.
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}							
			partnerGroupPermissionListViewModel.setPartnerGroupId(id);
		
			baseWrapper.setBasePersistableModel(partnerGroupPermissionListViewModel);
			baseWrapper = this.partnerGroupManager
					.loadPartnerGroupView(baseWrapper);
			partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel) baseWrapper
					.getBasePersistableModel();
					
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}
		}			
			
			AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					appUserTypeModel, "name", SortingOrder.ASC);

			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (FrameworkCheckedException ex1) {
				ex1.getStackTrace();
			}
			List<AppUserTypeModel> appUserTypeModelList = null;
			List<AppUserTypeModel> filteredAppUserTypeModelList = new ArrayList<AppUserTypeModel>();
			if (referenceDataWrapper.getReferenceDataList() != null) {
				appUserTypeModelList = referenceDataWrapper.getReferenceDataList();
				// hardcoded for pilot
				for(AppUserTypeModel tModel:appUserTypeModelList){
					if(tModel.getName().equalsIgnoreCase("Bank") 
							|| tModel.getName().equalsIgnoreCase("Retailer") 
							|| tModel.getName().equalsIgnoreCase("Operator")
							|| tModel.getName().equalsIgnoreCase("Service Operator")
					        || tModel.getName().equalsIgnoreCase("Supplier")){
						filteredAppUserTypeModelList.add(tModel);
					}
				}
				
			}

			
			
			PartnerModel partnerModel = new PartnerModel();
			if (filteredAppUserTypeModelList!=null && filteredAppUserTypeModelList.size()>0)
			{
				if((id != null) || isReSubmit)//-- update case
				{
					partnerModel.setAppUserTypeId(partnerGroupPermissionListViewModel.getAppUserTypeId());
				}else
				{//-- create case
					partnerModel.setAppUserTypeId(filteredAppUserTypeModelList.get(0).getAppUserTypeId());
				}
			}
			partnerModel.setActive(true);

			referenceDataWrapper = new ReferenceDataWrapperImpl(
					partnerModel, "name", SortingOrder.ASC);

			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (FrameworkCheckedException ex1) {
				ex1.getStackTrace();
			}
			List<PartnerModel> partnerModelList = null;
			if (referenceDataWrapper.getReferenceDataList() != null) {
				partnerModelList = referenceDataWrapper.getReferenceDataList();
			}
			httpServletRequest.setAttribute("appUserTypeModelList", filteredAppUserTypeModelList);
			httpServletRequest.setAttribute("partnerModelList", partnerModelList);	
			
			if(id == null)//-- create case.
			{
				if(!UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.INOV8))
				{
					 Object partnerId = httpServletRequest.getSession(true).getAttribute(PortalConstants.KEY_PARTNER_ID);
					 if(partnerId != null)
					 {
						 partnerGroupPermissionListViewModel.setPartnerId((Long)partnerId); 
					 }else
					 {
						 BaseWrapper baseWrapperAUP = new BaseWrapperImpl();
						 AppUserPartnerViewModel  appUserPartnerViewModel = new AppUserPartnerViewModel();
						 appUserPartnerViewModel.setAppUserId(UserUtils.getCurrentUser().getAppUserId());
						 baseWrapperAUP.setBasePersistableModel(appUserPartnerViewModel);
						 baseWrapperAUP = this.partnerGroupManager.loadAppUserPartner(baseWrapperAUP);
						 appUserPartnerViewModel = (AppUserPartnerViewModel)baseWrapperAUP.getBasePersistableModel();
						 partnerGroupPermissionListViewModel.setPartnerId(appUserPartnerViewModel.getPartnerId());
						 httpServletRequest.getSession(true).setAttribute(PortalConstants.KEY_PARTNER_ID, appUserPartnerViewModel.getPartnerId());
					 }
				}else
				{
					partnerGroupPermissionListViewModel.setPartnerId(partnerModelList.get(0).getPartnerId());
				}
				baseWrapper.setBasePersistableModel(partnerGroupPermissionListViewModel);
				baseWrapper = this.partnerGroupManager
						.loadPartnerGroupView(baseWrapper);
				partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel) baseWrapper
						.getBasePersistableModel();
			}
			/// Added for Resubmit Authorization Request 
			if(isReSubmit || isReadOnly){
				
				if(null == id){//donot load permissions in edit user group scenario, these are pre loaded     
					List<UserPermissionWrapper> userPermissionWrapperList = this.partnerGroupManager.loadPartnerPermission(userGroupReferenceDataModel.getPartnerId());
					partnerGroupPermissionListViewModel.setUserPermissionList(userPermissionWrapperList);
	
					partnerGroupPermissionListViewModel.setPartnerId(userGroupReferenceDataModel.getPartnerId());
					partnerGroupPermissionListViewModel.setPartnerName(userGroupReferenceDataModel.getPartnerName());
					partnerGroupPermissionListViewModel.setAppUserTypeId(userGroupReferenceDataModel.getAppUserTypeId());
					partnerGroupPermissionListViewModel.setAppUserTypeName(userGroupReferenceDataModel.getAppUserTypeName());
				}
				
				for (UserPermissionWrapper userPermissionWrapper : partnerGroupPermissionListViewModel.getUserPermissionList()) {
					
					for (UserPermissionWrapper userPermissionWrapper2 : userGroupReferenceDataModel.getUserPermissionList()) {
						if(userPermissionWrapper.getPermissionId()==userPermissionWrapper2.getPermissionId()){
							userPermissionWrapper.setCreateAllowed(userPermissionWrapper2.isCreateAvailable());
							userPermissionWrapper.setDeleteAllowed(userPermissionWrapper2.isDeleteAllowed());
							userPermissionWrapper.setUpdateAllowed(userPermissionWrapper2.isUpdateAllowed());
							userPermissionWrapper.setReadAllowed(userPermissionWrapper2.isReadAllowed());
							break;
						}
					} 
					
				}
				
				///Populate from reference data Model
				partnerGroupPermissionListViewModel.setName(userGroupReferenceDataModel.getName());
				partnerGroupPermissionListViewModel.setActive(userGroupReferenceDataModel.getActive());
				partnerGroupPermissionListViewModel.setDescription(userGroupReferenceDataModel.getDescription());
				partnerGroupPermissionListViewModel.setEditable(userGroupReferenceDataModel.getEditable());
				partnerGroupPermissionListViewModel.setEmail(userGroupReferenceDataModel.getEmail());
				
				
			}
			///End Added for Resubmit Authorization Request	
		return partnerGroupPermissionListViewModel;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception {
		Map referenceDataMap = new HashMap();
		List<PartnerModel> partnerModelList = (List<PartnerModel>)httpServletRequest.getAttribute("partnerModelList");
		List<AppUserTypeModel> appUserTypeModelList = (List<AppUserTypeModel>)httpServletRequest.getAttribute("appUserTypeModelList");
		List<AppUserTypeModel> filteredAppUserTypeModelList = new ArrayList<AppUserTypeModel>();
		
		if(partnerModelList == null || appUserTypeModelList == null)
		{
			AppUserTypeModel appUserTypeModel = new AppUserTypeModel();
			ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
					appUserTypeModel, "name", SortingOrder.ASC);

			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (FrameworkCheckedException ex1) {
				ex1.getStackTrace();
			}
			
			if (referenceDataWrapper.getReferenceDataList() != null) {
				appUserTypeModelList = referenceDataWrapper.getReferenceDataList();
				// hardcoded for pilot
				for(AppUserTypeModel tModel:appUserTypeModelList){
					if(tModel.getName().equalsIgnoreCase("Bank") 
							|| tModel.getName().equalsIgnoreCase("Retailer") 
							|| tModel.getName().equalsIgnoreCase("Operator")
							|| tModel.getName().equalsIgnoreCase("Service Operator")){
						filteredAppUserTypeModelList.add(tModel);
					}
				}
			}

			
			
			PartnerModel partnerModel = new PartnerModel();
			if (filteredAppUserTypeModelList!=null && filteredAppUserTypeModelList.size()>0)
			{
				if(httpServletRequest.getAttribute("AppUserTypeId") != null)
				{
					partnerModel.setAppUserTypeId((Long)httpServletRequest.getAttribute("AppUserTypeId"));
				}else
				{
					partnerModel.setAppUserTypeId(filteredAppUserTypeModelList.get(0).getAppUserTypeId());
				}
			}
			partnerModel.setActive(true);

			referenceDataWrapper = new ReferenceDataWrapperImpl(
					partnerModel, "name", SortingOrder.ASC);

			try {
				referenceDataManager.getReferenceData(referenceDataWrapper);
			} catch (FrameworkCheckedException ex1) {
				ex1.getStackTrace();
			}
			
			if (referenceDataWrapper.getReferenceDataList() != null) {
				partnerModelList = referenceDataWrapper.getReferenceDataList();
			}
		}else{
			filteredAppUserTypeModelList = appUserTypeModelList;
		}
		
		referenceDataMap.put("appUserTypeModelList", filteredAppUserTypeModelList);
		referenceDataMap.put("partnerModelList", partnerModelList);

		return referenceDataMap;
		

	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest request, HttpServletResponse response, Object object, BindException erorrs) throws Exception {
				
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

		
		PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel)object;
				
		partnerGroupModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());
		partnerGroupModel.setName(partnerGroupPermissionListViewModel.getName());
		partnerGroupModel.setDescription(partnerGroupPermissionListViewModel.getDescription());
		partnerGroupModel.setActive(partnerGroupPermissionListViewModel.getActive() == null ? false : partnerGroupPermissionListViewModel.getActive());
		partnerGroupModel.setEditable(true);
		partnerGroupModel.setEmail(partnerGroupPermissionListViewModel.getEmail());
		partnerGroupModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
		partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		partnerGroupModel.setCreatedOn(new Date());
		partnerGroupModel.setUpdatedOn(new Date());
		Set<Long> permission = new HashSet<Long>();
						
		Iterator<UserPermissionWrapper> itr = partnerGroupPermissionListViewModel.getUserPermissionList().iterator();
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
		try
		{
			baseWrapper = this.partnerGroupManager.createPartnerGroup(baseWrapper);
		}catch(FrameworkCheckedException fce)
		{			
			request.setAttribute("AppUserTypeId", partnerGroupPermissionListViewModel.getAppUserTypeId());
			if ("ConstraintViolationException".equals(fce.getMessage()) || "DataIntegrityViolationException".equals(fce.getMessage()))
			{
				this.saveMessage(request, super.getText("partnerGroupModule.partnerGroupNotUnique", request.getLocale()));
			}else
			{
				this.saveMessage(request, super.getText("partnerGroupModule.recordUnSaveSuccessful", request.getLocale()));
			}
			return super.showForm(request, response, erorrs);
//	        return new ModelAndView("redirect:p_partnergroupform.html?actionId=1");//super.showForm(request, response, erorrs);
		}catch(Exception fce)
		{			
			fce.printStackTrace();
			request.setAttribute("AppUserTypeId", partnerGroupPermissionListViewModel.getAppUserTypeId());
			this.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response, erorrs);
		}
	    
        this.saveMessage(request, super.getText("partnerGroupModule.recordSaveSuccessful", request.getLocale()));
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;         	
	      
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest request, HttpServletResponse response, Object object, BindException erorrs) throws Exception {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();		
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_UPDATE_USECASE_ID));

		PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel)object;
		Long id = ServletRequestUtils.getLongParameter(request,"partnerGroupId");
			
		try {	
		PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
		partnerGroupModel.setPartnerGroupId(id);
		baseWrapper.setBasePersistableModel(partnerGroupModel);
		baseWrapper = this.partnerGroupManager.loadPartnerGroup(baseWrapper);
		partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
					
		partnerGroupModel.setDescription(partnerGroupPermissionListViewModel.getDescription());
		partnerGroupModel.setActive(partnerGroupPermissionListViewModel.getActive() == null ? false : partnerGroupPermissionListViewModel.getActive());
		partnerGroupModel.setEmail(partnerGroupPermissionListViewModel.getEmail());
		partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());		
		partnerGroupModel.setUpdatedOn(new Date());
								
		Iterator<PartnerGroupDetailModel> itr = partnerGroupModel.getPartnerGroupIdPartnerGroupDetailModelList().iterator();		
		List<UserPermissionWrapper> upList = partnerGroupPermissionListViewModel.getUserPermissionList();
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
	    try
	    {	    	
	    	baseWrapper = this.partnerGroupManager.updatePartnerGroup(baseWrapper);
	    }catch(FrameworkCheckedException fce)
	    {	    	
	    	String str = request.getParameter("p_name");
	    	partnerGroupPermissionListViewModel.setName(str);
			str = request.getParameter("p_appUserTypeId");
			partnerGroupPermissionListViewModel.setAppUserTypeId(Long.valueOf(str));
			str = request.getParameter("p_partnerId");
			partnerGroupPermissionListViewModel.setPartnerId(Long.valueOf(str));
			request.setAttribute("AppUserTypeId", partnerGroupPermissionListViewModel.getAppUserTypeId());
	    	if(fce.getMessage().equals(super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale())))
	    	{
	    		//bug 2135,2136 resolved
	    		partnerGroupPermissionListViewModel.setActive(true);//set group status to active because can't deactivated
	    		this.saveMessage(request, super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale()));
	    	}else
	    	{
	    		this.saveMessage(request, super.getText("partnerGroupModule.recordUnUpdateSuccessful", request.getLocale()));
	    	}
	        
	        return super.showForm(request, response, erorrs);
	    }
	    
        this.saveMessage(request, super.getText("partnerGroupModule.recordUpdateSuccessful", request.getLocale()));
        ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
        return modelAndView;
	    
	    
		} catch (FrameworkCheckedException ex) {

			if("Partnergroupcannotbedeletedbecausegroupisnotempty".equals(ex.getMessage())){
				super.saveMessage(request, super.getText("appUserPartnerGroup.Partnergroupcannotbedeletedbecausegroupisnotempty", request.getLocale() ));
				return super.showForm(request, response,
						erorrs);
			}
			
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			super.saveMessage(request, MessageUtil.getMessage("6075"));
			return super.showForm(request, response,erorrs);
		}
	}
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel)command;
		Long id = null; 
		if(ServletRequestUtils.getBooleanParameter(request, "isUpdate", false))
			id = ServletRequestUtils.getLongParameter(request,"partnerGroupId");
		
		boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest",false);
		Long usecaseId= ServletRequestUtils.getLongParameter(request, "usecaseId");
		
		Long actionAuthorizationId = null;
		if(resubmitRequest)
			actionAuthorizationId=ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
		///Populate reference data Model
		UserGroupReferenceDataModel userGroupReferenceDataModel = populateReferenceDataModel(partnerGroupPermissionListViewModel);
		
		///End Populate reference data Model
		
		try
		{								
			XStream xstream = new XStream();
			String refDataModelString= xstream.toXML(userGroupReferenceDataModel);
			String oldRefDataModelString = null;
			
			UsecaseModel usecaseModel = usecaseFacade.loadUsecase(usecaseId);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(usecaseId,new Long(0));
			
			if (usecaseId.longValue()!=PortalConstants.USER_GROUP_CREATE_USECASE_ID){
				//Saving Current Data
				UserGroupReferenceDataModel oldUserGroupReferenceDataModel = populateOldReferenceData(id);
				oldRefDataModelString= xstream.toXML(oldUserGroupReferenceDataModel);
			}
			
			if(nextAuthorizationLevel.intValue()<1){
				
				if (usecaseId.longValue()==PortalConstants.USER_GROUP_CREATE_USECASE_ID){
					
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.USER_GROUP_CREATE_USECASE_ID));

					
					PartnerGroupModel partnerGroupModel = new PartnerGroupModel();
							
					partnerGroupModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());
					partnerGroupModel.setName(partnerGroupPermissionListViewModel.getName());
					partnerGroupModel.setDescription(partnerGroupPermissionListViewModel.getDescription());
					partnerGroupModel.setActive(partnerGroupPermissionListViewModel.getActive() == null ? false : partnerGroupPermissionListViewModel.getActive());
					partnerGroupModel.setEditable(true);
					partnerGroupModel.setEmail(partnerGroupPermissionListViewModel.getEmail());
					partnerGroupModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
					partnerGroupModel.setCreatedOn(new Date());
					partnerGroupModel.setUpdatedOn(new Date());
					Set<Long> permission = new HashSet<Long>();
									
					Iterator<UserPermissionWrapper> itr = partnerGroupPermissionListViewModel.getUserPermissionList().iterator();
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
					partnerGroupModel.setPartnerGroupId(id);
					baseWrapper.setBasePersistableModel(partnerGroupModel);
					baseWrapper = this.partnerGroupManager.loadPartnerGroup(baseWrapper);
					partnerGroupModel = (PartnerGroupModel) baseWrapper.getBasePersistableModel();
								
					partnerGroupModel.setDescription(partnerGroupPermissionListViewModel.getDescription());
					partnerGroupModel.setActive(partnerGroupPermissionListViewModel.getActive() == null ? false : partnerGroupPermissionListViewModel.getActive());
					partnerGroupModel.setEmail(partnerGroupPermissionListViewModel.getEmail());
					partnerGroupModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());		
					partnerGroupModel.setUpdatedOn(new Date());
											
					Iterator<PartnerGroupDetailModel> itr = partnerGroupModel.getPartnerGroupIdPartnerGroupDetailModelList().iterator();		
					List<UserPermissionWrapper> upList = partnerGroupPermissionListViewModel.getUserPermissionList();
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
				
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,"", refDataModelString,oldRefDataModelString,usecaseModel,actionAuthorizationId,request);
				this.saveMessage(request,"Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId);
			}
			else
			{	
				partnerGroupManager.validatePartnerGroup(partnerGroupPermissionListViewModel);
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,"", refDataModelString,oldRefDataModelString,usecaseModel.getUsecaseId(),partnerGroupPermissionListViewModel.getName(),resubmitRequest,actionAuthorizationId,request);
				this.saveMessage(request,"Action is pending for approval against reference Action ID : "+actionAuthorizationId);
			}
		
		}
		catch (FrameworkCheckedException ex)
		{	
			String str = request.getParameter("p_name");
	    	partnerGroupPermissionListViewModel.setName(str);
			str = request.getParameter("p_appUserTypeId");
			if(null!=str)
				partnerGroupPermissionListViewModel.setAppUserTypeId(Long.valueOf(str));
			str = request.getParameter("p_partnerId");
			if(null!=str)
				partnerGroupPermissionListViewModel.setPartnerId(Long.valueOf(str));
			request.setAttribute("AppUserTypeId", partnerGroupPermissionListViewModel.getAppUserTypeId());
	    	if(ex.getMessage().equals(super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale())))
	    	{
	    		//bug 2135,2136 resolved
	    		partnerGroupPermissionListViewModel.setActive(true);//set group status to active because can't deactivated
	    		this.saveMessage(request, super.getText("partnerGroupModule.partnerGroupCantBeDeactive", request.getLocale()));
	    	}
	    	else if("Partnergroupcannotbedeletedbecausegroupisnotempty".equals(ex.getMessage())){
				this.saveMessage(request, super.getText("appUserPartnerGroup.Partnergroupcannotbedeletedbecausegroupisnotempty", request.getLocale() ));	
			}
	    	else if ("ConstraintViolationException".equals(ex.getMessage()) || "DataIntegrityViolationException".equals(ex.getMessage()))
			{
				this.saveMessage(request, super.getText("partnerGroupModule.partnerGroupNotUnique", request.getLocale()));
			}
			else if(ex.getMessage().contains("already exist")){
				this.saveMessage(request, "Action authorization request with same Group Name is already exist.");
			}
			else
			{
				this.saveMessage(request, super.getText("partnerGroupModule.recordUnSaveSuccessful", request.getLocale()));
			}
	    	if(resubmitRequest)
				return new ModelAndView("redirect:p_partnergroupform.html?isReSubmit=true&authId="+actionAuthorizationId+"&actionId=1"); 
			else
				return super.showForm(request, response, errors);
//				super.showForm(request, errors, "p_partnergroupmanagement");
	       			
		}catch (Exception ex)
		{	
			String str = request.getParameter("p_name");
	    	partnerGroupPermissionListViewModel.setName(str);
			str = request.getParameter("p_appUserTypeId");
			if(null!=str)
				partnerGroupPermissionListViewModel.setAppUserTypeId(Long.valueOf(str));
			str = request.getParameter("p_partnerId");
			if(null!=str)
				partnerGroupPermissionListViewModel.setPartnerId(Long.valueOf(str));
			request.setAttribute("AppUserTypeId", partnerGroupPermissionListViewModel.getAppUserTypeId());
	    	
			this.saveMessage(request,MessageUtil.getMessage("6075"));
			
	    	if(resubmitRequest)
				return new ModelAndView("redirect:p_partnergroupform.html?isReSubmit=true&authId="+actionAuthorizationId+"&actionId=1"); 
			else
				return super.showForm(request, response, errors);	       			
		}
		return new ModelAndView(getSuccessView());
	}
	
	private UserGroupReferenceDataModel populateReferenceDataModel(PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel){
		UserGroupReferenceDataModel userGroupReferenceDataModel = new UserGroupReferenceDataModel();
		userGroupReferenceDataModel.setActive(partnerGroupPermissionListViewModel.getActive());
		userGroupReferenceDataModel.setAppUserTypeId(partnerGroupPermissionListViewModel.getAppUserTypeId());
		userGroupReferenceDataModel.setAppUserTypeName(partnerGroupPermissionListViewModel.getAppUserTypeName());
		userGroupReferenceDataModel.setDescription(partnerGroupPermissionListViewModel.getDescription());
		userGroupReferenceDataModel.setEditable(partnerGroupPermissionListViewModel.getEditable());
		userGroupReferenceDataModel.setEmail(partnerGroupPermissionListViewModel.getEmail());
		userGroupReferenceDataModel.setName(partnerGroupPermissionListViewModel.getName());
		userGroupReferenceDataModel.setPartnerGroupId(partnerGroupPermissionListViewModel.getPartnerGroupId());
		userGroupReferenceDataModel.setPartnerId(partnerGroupPermissionListViewModel.getPartnerId());
		userGroupReferenceDataModel.setPartnerName(partnerGroupPermissionListViewModel.getPartnerName());
		userGroupReferenceDataModel.setUserPermissionList(partnerGroupPermissionListViewModel.getUserPermissionList());
		return userGroupReferenceDataModel;	
	}
	
	private UserGroupReferenceDataModel populateOldReferenceData(Long id) throws FrameworkCheckedException {
		PartnerGroupPermissionListViewModel partnerGroupPermissionListViewModel = new PartnerGroupPermissionListViewModel();	
		partnerGroupPermissionListViewModel.setPartnerGroupId(id);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(partnerGroupPermissionListViewModel);
		baseWrapper = this.partnerGroupManager.loadPartnerGroupView(baseWrapper);
		partnerGroupPermissionListViewModel = (PartnerGroupPermissionListViewModel) baseWrapper.getBasePersistableModel();
		
		return populateReferenceDataModel(partnerGroupPermissionListViewModel);
	}

	public void setPartnerGroupManager(PartnerGroupManager partnerGroupManager) {
		this.partnerGroupManager = partnerGroupManager;
	}

	public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

}
