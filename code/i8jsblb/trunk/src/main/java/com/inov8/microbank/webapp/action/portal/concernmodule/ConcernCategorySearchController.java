package com.inov8.microbank.webapp.action.portal.concernmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class ConcernCategorySearchController extends BaseSearchController{
    
	private ConcernManager concernManager;
	
	public ConcernCategorySearchController() {
		super();
	}
	
	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest req, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		return null;
	}

	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}
}
