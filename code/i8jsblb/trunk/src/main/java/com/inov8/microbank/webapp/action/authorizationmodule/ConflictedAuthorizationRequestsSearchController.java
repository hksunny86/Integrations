package com.inov8.microbank.webapp.action.authorizationmodule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.util.ActionAuthorizationConstantsInterface;
import com.inov8.microbank.server.facade.portal.authorizationmodule.ActionAuthorizationFacade;

/**
 * <p>Title: i8Microbank</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Inov8 Limited</p>
 *
 * @author Hassan Javaid
 * @version 1.0
 */

public class ConflictedAuthorizationRequestsSearchController extends BaseSearchController
{

	private ActionAuthorizationFacade actionAuthorizationFacade;

	public ConflictedAuthorizationRequestsSearchController() 
	{
		super.setFilterSearchCommandClass(ActionAuthorizationModel.class);
	}
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{
		Long usecaseId = ServletRequestUtils.getLongParameter(httpServletRequest, "usecaseId");
		Long modifiedEscalationLevels = ServletRequestUtils.getLongParameter(httpServletRequest, "modifiedEscalationLevels");
		Long currentEscalationLevels = ServletRequestUtils.getLongParameter(httpServletRequest, "currentEscalationLevels");
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.putObject("modifiedEscalationLevels", modifiedEscalationLevels);
		searchBaseWrapper.putObject("currentEscalationLevels", currentEscalationLevels);
		
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		ActionAuthorizationModel actionAuthorizationModel = (ActionAuthorizationModel) model;
		actionAuthorizationModel.setUsecaseId(usecaseId);
		actionAuthorizationModel.setActionStatusId(ActionAuthorizationConstantsInterface.ACTION_STATUS_PENDING_APPROVAL);
		
		searchBaseWrapper.setBasePersistableModel(actionAuthorizationModel);
		if (sortingOrderMap.isEmpty())
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		
		CustomList<ActionAuthorizationModel> list = this.actionAuthorizationFacade.searchConflictedAuthorizationRequests(searchBaseWrapper).getCustomList();	
		return new ModelAndView( getSearchView(),"actionAuthorizationModellist",list.getResultsetList());
	}

	public void setActionAuthorizationFacade(ActionAuthorizationFacade actionAuthorizationFacade) {
		this.actionAuthorizationFacade = actionAuthorizationFacade;
	}

}
