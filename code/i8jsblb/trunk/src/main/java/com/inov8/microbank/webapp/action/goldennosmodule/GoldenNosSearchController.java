package com.inov8.microbank.webapp.action.goldennosmodule;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.goldennosmodule.GoldenNosListViewModel;
import com.inov8.microbank.server.service.goldennosmodule.GoldenNosManager;


public class GoldenNosSearchController extends BaseSearchController
{	
	private GoldenNosManager goldenNosManager;

	  public void setGoldenNosManager(GoldenNosManager goldenNosManager) {
		this.goldenNosManager = goldenNosManager;
	}

	public GoldenNosSearchController()
	  {
	    super.setFilterSearchCommandClass(GoldenNosListViewModel.class);
	  }

	  @Override
	  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
	                                  Object model,
	                                  HttpServletRequest httpServletRequest,
	                                  LinkedHashMap<String, SortingOrder>
	      sortingOrderMap) throws Exception
	  {
	    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);	    
	    GoldenNosListViewModel goldenNosListViewModel = (GoldenNosListViewModel) model;	    
	    searchBaseWrapper.setBasePersistableModel(goldenNosListViewModel);
	    if(sortingOrderMap.isEmpty())
	    sortingOrderMap.put("goldenNumber", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.goldenNosManager.searchGoldenNos(searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "goldenNosListViewModel",
	                            searchBaseWrapper.getCustomList().getResultsetList());
	  }

	  @Override
	    protected ModelAndView onToggleActivate(HttpServletRequest request,
	                                          HttpServletResponse response,Boolean activate) throws
	          Exception
	      {
	        ModelAndView modelAndView = new ModelAndView(new RedirectView(getSearchView() + ".html"));
	        return modelAndView;	        
	       
	      }        


	}
