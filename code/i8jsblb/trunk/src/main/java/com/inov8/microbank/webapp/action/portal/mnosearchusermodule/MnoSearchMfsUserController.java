package com.inov8.microbank.webapp.action.portal.mnosearchusermodule;

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
import com.inov8.microbank.common.model.mnosearchusermodule.MnoSearchMfsUsrListViewModel;
import com.inov8.microbank.server.service.portal.mnosearchusermodule.MnoSearchMfsUserManager;

public class MnoSearchMfsUserController extends BaseFormSearchController{

	
	private MnoSearchMfsUserManager mnoSearchMfsUserManager;
	
	public MnoSearchMfsUserController() {
		setCommandName("mnoSearchMfsUsrListViewModel");
		setCommandClass(MnoSearchMfsUsrListViewModel.class);
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object model,
			PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap)
			throws Exception {
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		MnoSearchMfsUsrListViewModel mnoSearchMfsUserListViewModel = (MnoSearchMfsUsrListViewModel) model;
		searchBaseWrapper.setBasePersistableModel(mnoSearchMfsUserListViewModel);
		
		if(sortingOrderMap.isEmpty()){
			sortingOrderMap.put("userId", SortingOrder.ASC);  
		}
		
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.mnoSearchMfsUserManager
				.searchMfsUser(searchBaseWrapper);
		return new ModelAndView(getSuccessView(), "mnoSearchUserList",  
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

	public void setMnoSearchMfsUserManager(
			MnoSearchMfsUserManager mnoSearchMfsUserManager) {
		this.mnoSearchMfsUserManager = mnoSearchMfsUserManager;
	}

}
