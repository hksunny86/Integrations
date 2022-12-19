package com.inov8.microbank.webapp.action.actionlogmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.actionlogmodule.ActionLogListViewModel;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

public class ActionLogSearchController extends BaseSearchController {

	private ActionLogManager actionLogManager; 
	
	public ActionLogSearchController()
	  {
	    super.setFilterSearchCommandClass(ActionLogListViewModel.class);
	  }
	
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> linkedHashMap) throws Exception {
		
		if( null == ServletRequestUtils.getFloatParameter(arg2, "actionLogId") ){ // action Log case
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			ActionLogListViewModel actionLogListViewModel=(ActionLogListViewModel)object;
			searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
			searchBaseWrapper.setBasePersistableModel(actionLogListViewModel);
			searchBaseWrapper.setSortingOrderMap(linkedHashMap);
			searchBaseWrapper=this.actionLogManager.searchActionLog(searchBaseWrapper);
			
			return new ModelAndView(getSearchView(), "actionLogList", searchBaseWrapper.getCustomList().getResultsetList());
		}
		else{ // action log detail case
			Long actionLogId = ServletRequestUtils.getLongParameter(arg2, "actionLogId");
			ActionLogModel actionLogModel = new ActionLogModel();
			actionLogModel.setActionLogId(actionLogId);
			actionLogModel.setPrimaryKey(actionLogId);
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			baseWrapper.setBasePersistableModel(actionLogModel);
			baseWrapper = this.actionLogManager.loadUserActionLog(baseWrapper);
			actionLogModel = (ActionLogModel)baseWrapper.getBasePersistableModel();
		    String searchView = this.getSearchView();
			return new ModelAndView("actionlogdetail.html", "actionLogModel", actionLogModel);
		}
		
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

}
