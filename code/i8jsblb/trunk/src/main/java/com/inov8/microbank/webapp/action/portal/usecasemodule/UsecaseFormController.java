package com.inov8.microbank.webapp.action.portal.usecasemodule;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.UsecaseLevelRefDataModel;
import com.inov8.microbank.common.model.portal.authorizationreferencedata.UsecaseReferenceDataModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.usermanagementmodule.UserManagementManager;
import com.inov8.microbank.webapp.action.authorizationmodule.AdvanceAuthorizationFormController;
import com.thoughtworks.xstream.XStream;

public class UsecaseFormController extends AdvanceAuthorizationFormController {
	
	private static final Logger LOGGER = Logger.getLogger( UsecaseFormController.class );
	private UserManagementManager	userManagementManager;
	private MessageSource messageSource;
	
	public UsecaseFormController() {
		setCommandName("usecaseModel");
		setCommandClass(UsecaseModel.class);	
	}
	
	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		  Map referenceDataMap = new HashMap();
		  List<LevelCheckerModel> levelCheckerModelList = new LinkedList<LevelCheckerModel>();
		  AppUserModel appUserModel = new AppUserModel();
			 appUserModel.setAppUserTypeId(6L); // For loading Bank AppUserModels
			 appUserModel.setAccountClosedSettled(false);
			 appUserModel.setAccountClosedUnsettled(false);
			 
			 ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(appUserModel, "username", SortingOrder.ASC);
			 try
			    {
			      referenceDataManager.getReferenceData(referenceDataWrapper);
			    }
			    catch (FrameworkCheckedException ex1)
			    {
			    }
			 List<AppUserModel> appUserModelList = null;
			  if (referenceDataWrapper.getReferenceDataList() != null)
			  {
				  appUserModelList = referenceDataWrapper.getReferenceDataList();
			  }
			  LevelCheckerModel	levelCheckerModel;
			  
			  for (AppUserModel temAppUserModel : appUserModelList) {
				  
				  levelCheckerModel= new LevelCheckerModel();
				  levelCheckerModel.setCheckerIdAppUserModel(temAppUserModel);
				  levelCheckerModelList.add(levelCheckerModel);
			}
		  
		  referenceDataMap.put("checkersList", levelCheckerModelList);
		  referenceDataMap.put("maxLevel",  messageSource.getMessage("usecase.maxLevel",null,null));
		  return referenceDataMap;
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws Exception
	{
		Long id = ServletRequestUtils.getLongParameter(httpServletRequest, "usecaseId");
		
		boolean isReSubmit = ServletRequestUtils.getBooleanParameter(httpServletRequest, "isReSubmit",false);
			
		/// Added for Resubmit Authorization Request 
		if(isReSubmit){
			Long actionAuthorizationId = ServletRequestUtils.getLongParameter(httpServletRequest,"authId");
			ActionAuthorizationModel actionAuthorizationModel = actionAuthorizationFacade.load(actionAuthorizationId);
			
			if(actionAuthorizationModel.getCreatedById().longValue()!=UserUtils.getCurrentUser().getAppUserId().longValue()){
				throw new FrameworkCheckedException("illegal operation performed");
			}
			
			XStream xstream = new XStream();
			UsecaseReferenceDataModel usecaseReferenceDataModel = (UsecaseReferenceDataModel) xstream.fromXML(actionAuthorizationModel.getReferenceData());
			UsecaseModel usecaseModel = populateUsecaseModel(usecaseReferenceDataModel);
					
			return usecaseModel;
		}
		///End Added for Resubmit Authorization Request
		
		
		if (null != id)
		{
			
			UsecaseModel usecaseModel = new UsecaseModel();
			UsecaseLevelModel usecaseLevelModel = new UsecaseLevelModel();			
			List<UsecaseLevelModel> usecaseLevelModelList; 
			
			LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
						
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			
			usecaseModel = (UsecaseModel) usecaseFacade.loadUsecase(id);
			
			/// Load usecase Levels
			usecaseLevelModel.setUsecaseIdUsecaseModel(usecaseModel);
			sortingOrderMap.put("levelNo", SortingOrder.ASC);
			searchBaseWrapper.setBasePersistableModel(usecaseLevelModel);
			searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
			searchBaseWrapper=this.usecaseFacade.searchUsecaseLevels(searchBaseWrapper);
			usecaseLevelModelList = (List<UsecaseLevelModel>)searchBaseWrapper.getCustomList().getResultsetList();
						
			for(int i=0;i<usecaseLevelModelList.size();i++){
				
				List<LevelCheckerModel> levelCheckerModelList = this.usecaseFacade.getLevelCheckerModelList(usecaseLevelModelList.get(i).getUsecaseLevelId());
				usecaseLevelModelList.get(i).setUsecaseLevelIdLevelCheckerModelList(levelCheckerModelList);
			}
			
			int maxUsecaseLevels =Integer.parseInt(messageSource.getMessage("usecase.maxLevel",null,null));
			for(int i = usecaseLevelModelList.size(); i <maxUsecaseLevels; i++){
				usecaseLevelModelList.add(new UsecaseLevelModel());
			}
			String[] levelChecker = new String [maxUsecaseLevels];
			usecaseModel.setLevelcheckers(levelChecker);
			
			usecaseModel.setUsecaseIdLevelModelList(usecaseLevelModelList);
			
			
			if(usecaseModel.getUsecaseId().equals(PortalConstants.UPDATE_USECASE))
				httpServletRequest.setAttribute("isParentUsecase",true);
				
			return usecaseModel;
		}
		else
		{
			if(log.isDebugEnabled())
			{
				log.debug("id is null....creating new instance of Model");
			}

			return new UsecaseModel();
		}
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		UsecaseModel usecaseModel = (UsecaseModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse,usecaseModel, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception
	{
		UsecaseModel usecaseModel = (UsecaseModel) object;
		return this.createOrUpdate(httpServletRequest, httpServletResponse, usecaseModel, bindException);
	}

	private ModelAndView createOrUpdate(HttpServletRequest request, HttpServletResponse response, UsecaseModel model,
			BindException errors) throws Exception
	{
		ModelAndView modelAndView = null;
		UsecaseModel usecaseModel = model ;
		List<UsecaseLevelModel> usecaseLevelModelList = new ArrayList<UsecaseLevelModel>(); 
		
		//check for affected authorization requests by change in levels
		UsecaseModel currentUsecaseModel = usecaseFacade.loadUsecase(usecaseModel.getUsecaseId());
		if(usecaseModel.getEscalationLevels().intValue()<currentUsecaseModel.getEscalationLevels().intValue()){
			
			boolean records = usecaseFacade.getAffectedAuthorizationRequests(usecaseModel);
			if(records){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("usecaseId",usecaseModel.getUsecaseId());
				map.put("currentEscalationLevels",currentUsecaseModel.getEscalationLevels());
				map.put("modifiedEscalationLevels",usecaseModel.getEscalationLevels());
			    modelAndView = new ModelAndView(new RedirectView("p_conflictedauthorizationrequests.html"),map);	
			    return modelAndView;
			}
		}
				
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
		
		
		try
		{	
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
			baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_USECASE));
			baseWrapper.putObject("checkerMap",(Serializable) levelCheckerModelMap);
			
				
			//UsecaseModel
			usecaseModel.setUpdatedOn(new Date());
			usecaseModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());	
			usecaseModel.setIsAuthorizationEnable((Boolean) (usecaseModel.getIsAuthorizationEnable() == null ? false : usecaseModel.getIsAuthorizationEnable()));
			baseWrapper.setBasePersistableModel(usecaseModel);
				
			//Adjust Usecase levels 
			
			
			for (UsecaseLevelModel usecaseLevelModel: usecaseModel.getUsecaseIdLevelModelList()) {
				if(usecaseLevelModel.getLevelNo()<=usecaseModel.getEscalationLevels())
					usecaseLevelModelList.add(usecaseLevelModel);
			}
			
			usecaseModel.setUsecaseIdLevelModelList(usecaseLevelModelList);
			baseWrapper = this.usecaseFacade.saveOrUpdateUsecase(baseWrapper);
	
			this.saveMessage(request, "Record saved successfully");
			modelAndView = new ModelAndView(this.getSuccessView());
		}
		catch (Exception ex)
		{		
			ex.printStackTrace();
			LOGGER.error("Exception occured while updating Usecase ID : "+model.getUsecaseId().toString(), ex);
			super.saveMessage(request,"Record could not be saved, kindly contact administrator");
			modelAndView = super.showForm(request, response, errors);
		}
		return modelAndView;
	}
	
	@Override
	protected ModelAndView onAuthorization(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors) throws Exception {
		ModelAndView modelAndView = null;
		UsecaseModel usecaseModel= (UsecaseModel)command ;
		UsecaseModel currentUsecaseModel = usecaseFacade.loadUsecase(usecaseModel.getUsecaseId());
		String message=null;
		try
		{			
			boolean resubmitRequest = ServletRequestUtils.getBooleanParameter(request, "resubmitRequest",false);
			Long actionAuthorizationId = null;
			if(resubmitRequest)
				actionAuthorizationId=ServletRequestUtils.getLongParameter(request, "actionAuthorizationId");
			
			XStream xstream = new XStream();
			UsecaseReferenceDataModel usecaseReferenceDataModel = populateReferenceDataModel(usecaseModel); 
			String refDataModelString= xstream.toXML(usecaseReferenceDataModel);
			
			UsecaseReferenceDataModel oldUsecaseReferenceDataModel = getUsecaseReferenceDataModel(currentUsecaseModel);
			String oldRefDataModelString= xstream.toXML(oldUsecaseReferenceDataModel);
			
			
			UsecaseModel updateUsecaseModel = usecaseFacade.loadUsecase(PortalConstants.UPDATE_USECASE);
			Long nextAuthorizationLevel = usecaseFacade.getNextAuthorizationLevel(PortalConstants.UPDATE_USECASE,new Long(0));
				
			if(nextAuthorizationLevel.intValue()<1){
				
				//check for affected authorization requests by change in levels
				List<UsecaseLevelModel> usecaseLevelModelList = new ArrayList<UsecaseLevelModel>(); 
				if(usecaseModel.getEscalationLevels().intValue()<currentUsecaseModel.getEscalationLevels().intValue()){
					
					boolean records = usecaseFacade.getAffectedAuthorizationRequests(usecaseModel);
					if(records){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("usecaseId",usecaseModel.getUsecaseId());
						map.put("currentEscalationLevels",currentUsecaseModel.getEscalationLevels());
						map.put("modifiedEscalationLevels",usecaseModel.getEscalationLevels());
					    modelAndView = new ModelAndView(new RedirectView("p_conflictedauthorizationrequests.html"),map);	
					    return modelAndView;
					}
				}
						
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
				
				
				
					BaseWrapper baseWrapper = new BaseWrapperImpl();
					baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);
					baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, new Long(PortalConstants.UPDATE_USECASE));
					baseWrapper.putObject("checkerMap",(Serializable) levelCheckerModelMap);
					
						
					//UsecaseModel
					usecaseModel.setUpdatedOn(new Date());
					usecaseModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());	
					usecaseModel.setIsAuthorizationEnable((Boolean) (usecaseModel.getIsAuthorizationEnable() == null ? false : usecaseModel.getIsAuthorizationEnable()));
					baseWrapper.setBasePersistableModel(usecaseModel);
						
					//Adjust Usecase levels 
					
					
					for (UsecaseLevelModel usecaseLevelModel: usecaseModel.getUsecaseIdLevelModelList()) {
						if(usecaseLevelModel.getLevelNo()<=usecaseModel.getEscalationLevels())
							usecaseLevelModelList.add(usecaseLevelModel);
					}
					
					usecaseModel.setUsecaseIdLevelModelList(usecaseLevelModelList);
					baseWrapper = this.usecaseFacade.saveOrUpdateUsecase(baseWrapper);
				
				actionAuthorizationId = performActionWithAllIntimationLevels(nextAuthorizationLevel,null, refDataModelString,oldRefDataModelString,updateUsecaseModel,actionAuthorizationId,request);
				message = "Action is authorized successfully. Changes are saved against refernce Action ID : "+actionAuthorizationId;
				
			}
			else
			{									
				actionAuthorizationId = createAuthorizationRequest(nextAuthorizationLevel,null, refDataModelString,oldRefDataModelString,updateUsecaseModel.getUsecaseId() ,usecaseModel.getUsecaseId().toString(),resubmitRequest,actionAuthorizationId,request);
				message = "Action is pending for approval against reference Action ID : "+actionAuthorizationId;
			}
		
		}
		catch (FrameworkCheckedException ex)
		{	
			ex.printStackTrace();
			LOGGER.error("Exception occured while updating Usecase ID : "+usecaseModel.getUsecaseId().toString(), ex);
			super.saveMessage(request,ex.getMessage());
			modelAndView = super.showForm(request, response, errors);
		}
		catch (Exception ex)
		{	
			ex.printStackTrace();
			LOGGER.error("Exception occured while updating Usecase ID : "+usecaseModel.getUsecaseId().toString(), ex);
			super.saveMessage(request,MessageUtil.getMessage("6075"));
			modelAndView = super.showForm(request, response, errors);
		}
		this.saveMessage(request,message);
		modelAndView = new ModelAndView(this.getSuccessView());
		return modelAndView;
	}
	
	private UsecaseReferenceDataModel populateReferenceDataModel(UsecaseModel usecaseModel) throws Exception {
		
		UsecaseReferenceDataModel usecaseReferenceDataModel = new UsecaseReferenceDataModel();
		usecaseReferenceDataModel.setAuthorizationStatus(usecaseModel.getAuthorizationStatus());
		usecaseReferenceDataModel.setComments(usecaseModel.getComments());
		usecaseReferenceDataModel.setDescription(usecaseModel.getDescription());
		usecaseReferenceDataModel.setEscalationLevels(usecaseModel.getEscalationLevels());
		usecaseReferenceDataModel.setIsAuthorizationEnable(usecaseModel.getIsAuthorizationEnable());
		usecaseReferenceDataModel.setLevelcheckers(usecaseModel.getLevelcheckers());
		usecaseReferenceDataModel.setName(usecaseModel.getName());
		usecaseReferenceDataModel.setUsecaseId(usecaseModel.getUsecaseId());
		usecaseReferenceDataModel.setVersionNo(usecaseModel.getVersionNo());
		
		usecaseReferenceDataModel.setCreatedBy(usecaseModel.getCreatedBy());
		usecaseReferenceDataModel.setCreatedOn(usecaseModel.getCreatedOn());
		usecaseReferenceDataModel.setUpdatedBy(usecaseModel.getUpdatedBy());
		usecaseReferenceDataModel.setUpdatedOn(usecaseModel.getUpdatedOn());
				
		if(usecaseModel.getEscalationLevels()>0){
			
			//Adjust Usecase levels 
			List<UsecaseLevelModel> usecaseLevelModelList = new ArrayList<UsecaseLevelModel>(); 
	
			for (UsecaseLevelModel usecaseLevelModel: usecaseModel.getUsecaseIdLevelModelList()) {
				if(usecaseLevelModel.getLevelNo()<=usecaseModel.getEscalationLevels())
					usecaseLevelModelList.add(usecaseLevelModel);
			}
			
			usecaseModel.setUsecaseIdLevelModelList(usecaseLevelModelList);
			
			for (UsecaseLevelModel usecaseLevelModel : usecaseModel.getUsecaseIdLevelModelList()) {
				
				UsecaseLevelRefDataModel usecaseLevelRefDataModel = new UsecaseLevelRefDataModel();
				usecaseLevelRefDataModel.setIntimateOnly(usecaseLevelModel.getIntimateOnly());
				usecaseLevelRefDataModel.setLevelNo(usecaseLevelModel.getLevelNo());
				usecaseLevelRefDataModel.setUsecaseId(usecaseLevelModel.getUsecaseId());
				usecaseLevelRefDataModel.setUsecaseLevelId(usecaseLevelModel.getUsecaseLevelId());
				
				usecaseReferenceDataModel.getUsecaseIdLevelModelList().add(usecaseLevelRefDataModel);
			}
		}
		
		
		return usecaseReferenceDataModel;
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
				
				//Populate Level Checkers
				List<LevelCheckerModel> levelCheckerModelList = new ArrayList<LevelCheckerModel>();
				String[] levelCheckerIds =  usecaseModel.getLevelcheckers()[usecaseLevelModel.getLevelNo().intValue()-1].split(",");	
				for(int j=0; j< levelCheckerIds.length;j++){			
					BaseWrapper baseWrapper= new BaseWrapperImpl();
					AppUserModel appUserModel = new AppUserModel();
					appUserModel.setAppUserId(Long.parseLong(levelCheckerIds[j]));
					baseWrapper.setBasePersistableModel(appUserModel);
					appUserModel= (AppUserModel) userManagementManager.searchAppUserByPrimaryKey(baseWrapper).getBasePersistableModel();
					if(null!=appUserModel){
						LevelCheckerModel levelCheckerModel = new LevelCheckerModel();
						levelCheckerModel.setCheckerIdAppUserModel(appUserModel);
						
						levelCheckerModelList.add(levelCheckerModel);
					}
				}
				
				//End Populate Level Checkers
				usecaseLevelModel.setUsecaseLevelIdLevelCheckerModelList(levelCheckerModelList);
				usecaseModel.getUsecaseIdLevelModelList().add(usecaseLevelModel);
			}
		}	
		return usecaseModel;
	}
	
	private UsecaseReferenceDataModel getUsecaseReferenceDataModel(UsecaseModel currentUsecaseModel) throws Exception{
		
		//UsecaseModel currentUsecaseModel = usecaseFacade.loadUsecase(usecaseId);
		UsecaseReferenceDataModel usecaseReferenceDataModel = new UsecaseReferenceDataModel();
		UsecaseLevelModel usecaseLevelModel = new UsecaseLevelModel();			
		List<UsecaseLevelModel> usecaseLevelModelList; 			
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();	
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		String[] checkernamesArray = new String[10];
			
		usecaseLevelModel.setUsecaseIdUsecaseModel(currentUsecaseModel);
		sortingOrderMap.put("levelNo", SortingOrder.ASC);
		searchBaseWrapper.setBasePersistableModel(usecaseLevelModel);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper=this.usecaseFacade.searchUsecaseLevels(searchBaseWrapper);
		usecaseLevelModelList = (List<UsecaseLevelModel>)searchBaseWrapper.getCustomList().getResultsetList();
					
		for(int i=0;i<usecaseLevelModelList.size();i++){
			
			List<LevelCheckerModel> levelCheckerModelList = this.usecaseFacade.getLevelCheckerModelList(usecaseLevelModelList.get(i).getUsecaseLevelId());
			usecaseLevelModelList.get(i).setUsecaseLevelIdLevelCheckerModelList(levelCheckerModelList);
			
			/// Setting User Names of Checker
			StringBuilder names = new StringBuilder();
			for (LevelCheckerModel levelCheckerModel : levelCheckerModelList) {
				if(names.length()>1)
					names.append(",");
				names.append(levelCheckerModel.getCheckerIdAppUserModel().getUsername());				
			}
			checkernamesArray[i]=names.toString();
		}
						
		currentUsecaseModel.setUsecaseIdLevelModelList(usecaseLevelModelList);
		currentUsecaseModel.setLevelcheckers(checkernamesArray);
		
		
		usecaseReferenceDataModel.setAuthorizationStatus(currentUsecaseModel.getAuthorizationStatus());
		usecaseReferenceDataModel.setComments(currentUsecaseModel.getComments());
		usecaseReferenceDataModel.setDescription(currentUsecaseModel.getDescription());
		usecaseReferenceDataModel.setEscalationLevels(currentUsecaseModel.getEscalationLevels());
		usecaseReferenceDataModel.setIsAuthorizationEnable(currentUsecaseModel.getIsAuthorizationEnable());
		usecaseReferenceDataModel.setLevelcheckers(currentUsecaseModel.getLevelcheckers());
		usecaseReferenceDataModel.setName(currentUsecaseModel.getName());
		usecaseReferenceDataModel.setUsecaseId(currentUsecaseModel.getUsecaseId());
		usecaseReferenceDataModel.setVersionNo(currentUsecaseModel.getVersionNo());
		
		if(currentUsecaseModel.getEscalationLevels()>0){
			
			for (UsecaseLevelModel tempUsecaseLevelModel : currentUsecaseModel.getUsecaseIdLevelModelList()) {
				
				UsecaseLevelRefDataModel usecaseLevelRefDataModel = new UsecaseLevelRefDataModel();
				usecaseLevelRefDataModel.setIntimateOnly(tempUsecaseLevelModel.getIntimateOnly());
				usecaseLevelRefDataModel.setLevelNo(tempUsecaseLevelModel.getLevelNo());
				usecaseLevelRefDataModel.setUsecaseId(tempUsecaseLevelModel.getUsecaseId());
				usecaseLevelRefDataModel.setUsecaseLevelId(tempUsecaseLevelModel.getUsecaseLevelId());
				
				usecaseReferenceDataModel.getUsecaseIdLevelModelList().add(usecaseLevelRefDataModel);
			}
		}
				
		return usecaseReferenceDataModel;
	}  

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	public void setUserManagementManager(UserManagementManager userManagementManager) {
		this.userManagementManager = userManagementManager;
	}
}
