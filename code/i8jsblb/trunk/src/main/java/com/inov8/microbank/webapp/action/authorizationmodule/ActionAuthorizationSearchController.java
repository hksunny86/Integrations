package com.inov8.microbank.webapp.action.authorizationmodule;
/*
 * Author : Hassan Javaid
 * Date   : 13-06-2014
 * Module : Action Authorization
 * Project: Mircobank	
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inov8.microbank.common.model.SegmentModel;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ActionStatusModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ExtendedActionAuthorizationModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;

public class ActionAuthorizationSearchController extends
		BaseFormSearchController {
	
	private ActionAuthorizationFacade actionAuthorizationFacade;
	private UsecaseFacade usecaseFacade;
	private ReferenceDataManager referenceDataManager;
	

	public ActionAuthorizationSearchController() {
		super.setCommandName("extendedActionAuthorizationModel");
		super.setCommandClass(ExtendedActionAuthorizationModel.class);
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest request)
			throws Exception {
		Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
		boolean myRequests= ServletRequestUtils.getBooleanParameter(request,"myRequests", false);
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<UsecaseModel> usecaseModelList = new ArrayList<UsecaseModel>();
		List<AppUserModel> bankUserAppUserModelList = null;
		
	    if(myRequests){
	    	List<LevelCheckerModel> levelCheckerModelList = new ArrayList<LevelCheckerModel>();
	    	levelCheckerModelList = usecaseFacade.getLevelCheckerModelListByCheckerAppUserId(UserUtils.getCurrentUser().getAppUserId());
	    	
	    	for (LevelCheckerModel levelCheckerModel : levelCheckerModelList) {
	    		usecaseModelList.add(levelCheckerModel.getUsecaselevelIdUsecaseLevelModel().getUsecaseIdUsecaseModel());
			}   	
	    	bankUserAppUserModelList = usecaseFacade.getAsociatedCheckers(usecaseModelList);
	    	
	    	referenceDataMap.put("usecaseModelList", usecaseModelList );
	    }
	    else
	    {
	    	searchBaseWrapper.setBasePersistableModel(new UsecaseModel());
			usecaseModelList = usecaseFacade.searchAuthorizationEnableUsecase(searchBaseWrapper).getCustomList().getResultsetList();
		    referenceDataMap.put("usecaseModelList", usecaseModelList );
		    
		    AppUserModel appUserModel = new AppUserModel();
			 appUserModel.setAppUserTypeId(6L); // For loading Bank AppUserModels
			 appUserModel.setAccountClosedSettled(false);
			 appUserModel.setAccountClosedUnsettled(false);
			 
			 ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(appUserModel, "username", SortingOrder.ASC);
			 
			 try
			    {
			      referenceDataManager.getReferenceData(referenceDataWrapper);
			      if (referenceDataWrapper.getReferenceDataList() != null)
				  {
			    	  bankUserAppUserModelList = referenceDataWrapper.getReferenceDataList();
				  }
			    }
		    catch (FrameworkCheckedException ex1)
		    {
		    	ex1.printStackTrace();
		    	throw new FrameworkCheckedException(ex1.getMessage());
		    }
	    }

		SegmentModel segmentModel = new SegmentModel();
	    List<SegmentModel> segmentModelList = null;

	    segmentModel.setIsActive(true);
	    ReferenceDataWrapperImpl referenceDataWrapper = new ReferenceDataWrapperImpl(
	    		segmentModel, "name", SortingOrder.ASC);
	    referenceDataManager.getReferenceData(referenceDataWrapper);
	    {
			segmentModelList = referenceDataWrapper.getReferenceDataList();
		}

	    
	    ActionStatusModel actionStatusModel = new ActionStatusModel();
		ReferenceDataWrapper refDataWrapper = new ReferenceDataWrapperImpl( actionStatusModel, "name", SortingOrder.ASC );
		referenceDataManager.getReferenceData( refDataWrapper );
		List<ActionStatusModel> actionStatusModelList;
		actionStatusModelList=refDataWrapper.getReferenceDataList();
		List<ActionStatusModel> tempActionStatusModelList = new ArrayList<>();
		
		for (ActionStatusModel actionStatusModel2 :  actionStatusModelList) {
			if(actionStatusModel2.getActionStatusId()>2 && actionStatusModel2.getActionStatusId()<10 )
				
				tempActionStatusModelList.add(actionStatusModel2);
		}		
		referenceDataMap.put( "actionStatusModel", tempActionStatusModelList);
		referenceDataMap.put( "bankUserAppUserModelList", bankUserAppUserModelList);
		referenceDataMap.put("segmentModelList", segmentModelList);

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,HttpServletResponse response, Object model, 
			PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		List<ActionAuthorizationModel> list = new ArrayList<ActionAuthorizationModel>();
		
		boolean myRequests= ServletRequestUtils.getBooleanParameter(request,"myRequests", false);
		
		
		ExtendedActionAuthorizationModel extendedActionAuthorizationModel = (ExtendedActionAuthorizationModel) model;
		ActionAuthorizationModel actionAuthorizationModel = (ActionAuthorizationModel)extendedActionAuthorizationModel;


		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);	
		searchBaseWrapper.setBasePersistableModel(actionAuthorizationModel);
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "createdOn", extendedActionAuthorizationModel.getCreatedOnFromDate(),
				extendedActionAuthorizationModel.getCreatedOnToDate());
		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );

		//sorting order
		if( sortingOrderMap.isEmpty() )
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
		
		if(myRequests){
			
			CustomList<ActionAuthorizationModel> customlist = this.actionAuthorizationFacade.searchMyRequests(searchBaseWrapper).getCustomList();
			list = customlist.getResultsetList();
			if(null==list || list.size()<0)
				pagingHelperModel.setTotalRecordsCount(0);
			return new ModelAndView( "p_myauthorizationrequests", "actionAuthorizationModellist",list);
		}
		
		CustomList<ActionAuthorizationModel> customlist = this.actionAuthorizationFacade.search(searchBaseWrapper).getCustomList();
		list = customlist.getResultsetList();	
		return new ModelAndView( getSuccessView(), "actionAuthorizationModellist",list);
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
}
