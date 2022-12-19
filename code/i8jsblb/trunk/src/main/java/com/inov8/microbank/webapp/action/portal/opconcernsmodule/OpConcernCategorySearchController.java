package com.inov8.microbank.webapp.action.portal.opconcernsmodule;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.ConcernCategoryModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.concernmodule.ConcernManager;

public class OpConcernCategorySearchController extends BaseFormSearchController{
    
	private ConcernManager concernManager;
	
	public OpConcernCategorySearchController() {
		super.setCommandName("concernCategoryModel");
		super.setCommandClass(ConcernCategoryModel.class);
	}
	
	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest req, HttpServletResponse res, Boolean val) throws Exception {
		System.out.println("In on Toggle Activate..");
		Date nowDate = new Date();
		Long concernCategoryId = ServletRequestUtils.getLongParameter(req, "concernCategoryId");
		ConcernCategoryModel concernCategoryModel = new ConcernCategoryModel();
		concernCategoryModel.setConcernCategoryId(concernCategoryId);
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(concernCategoryModel);
		try{
		   baseWrapper = this.concernManager.searchConcernCategoryByPrimaryKey(baseWrapper);
		   concernCategoryModel = (ConcernCategoryModel)baseWrapper.getBasePersistableModel();
		   concernCategoryModel.setActive(false);
           concernCategoryModel.setName(concernCategoryModel.getName()+"_"+nowDate.getTime());
           concernCategoryModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
           concernCategoryModel.setUpdatesOn(nowDate);
           concernCategoryModel.setDescription("Deleted");
           concernCategoryModel.setComments("Deleted");
		   baseWrapper.setBasePersistableModel(concernCategoryModel);
		   baseWrapper = this.concernManager.updateConcernCategory(baseWrapper);
		   this.saveMessage(req, "Record deleted successfully.");
		}
		catch(FrameworkCheckedException fce){
		   fce.printStackTrace();
		   this.saveMessage(req, "Record could not delete.");
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView("p_opconcerncatdefmanagement.html?actionId=2"));
		return modelAndView;
	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest req, HttpServletResponse res, Object model, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
		System.out.println("In the OnSearch Method..");
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		ConcernCategoryModel concernCategoryModel = (ConcernCategoryModel) model;
		concernCategoryModel.setActive(true);
		searchBaseWrapper.setBasePersistableModel(concernCategoryModel);
		if(sortingOrderMap.isEmpty())
	    {
	       sortingOrderMap.put("name", SortingOrder.DESC );
	    }
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.concernManager.searchConcernCategory(searchBaseWrapper);
		return new ModelAndView(super.getSuccessView(), "concernCategoryList",
                searchBaseWrapper.getCustomList().
                getResultsetList());
	}

	public void setConcernManager(ConcernManager concernManager) {
		this.concernManager = concernManager;
	}
    
}
