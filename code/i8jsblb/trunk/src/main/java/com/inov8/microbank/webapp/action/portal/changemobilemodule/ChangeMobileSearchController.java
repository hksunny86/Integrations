package com.inov8.microbank.webapp.action.portal.changemobilemodule;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.changemobilemodule.ChangemobileListViewModel;
import com.inov8.microbank.server.service.portal.changemobilemodule.ChangeMobileManager;

public class ChangeMobileSearchController extends BaseFormSearchController {

	private ChangeMobileManager changeMobileManager;

	public ChangeMobileSearchController() {
		setCommandName("changemobileListViewModel");
		setCommandClass(ChangemobileListViewModel.class);
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap sortingOrderMap)
			throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		ChangemobileListViewModel changemobileListViewModel = (ChangemobileListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(changemobileListViewModel);
		
		if(sortingOrderMap.isEmpty()){
			sortingOrderMap.put("fullName", SortingOrder.ASC);
		}
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.changeMobileManager
				.searchChangeMobile(searchBaseWrapper);
		return new ModelAndView(getSuccessView(), "changeMobileList",
				searchBaseWrapper.getCustomList().getResultsetList());
	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {

		return null;
	}

	protected ModelAndView onToggleActivate(HttpServletRequest request,
			HttpServletResponse response, Boolean activate) throws Exception {
		return null;
	}

	public void setChangeMobileManager(ChangeMobileManager changeMobileManager) {
		this.changeMobileManager = changeMobileManager;
	}
}
