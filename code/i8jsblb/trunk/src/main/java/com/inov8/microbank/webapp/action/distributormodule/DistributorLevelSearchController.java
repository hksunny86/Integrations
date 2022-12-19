package com.inov8.microbank.webapp.action.distributormodule;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.distributormodule.DistributorLevelListViewModel;
import com.inov8.microbank.server.service.distributormodule.DistributorLevelManager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class DistributorLevelSearchController
    extends BaseSearchController
{
  private DistributorLevelManager distributorLevelManager;
  
  public DistributorLevelSearchController()
  {
	  super.setFilterSearchCommandClass(DistributorLevelListViewModel.class);
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
          HttpServletResponse response,
          Boolean activate) throws Exception
  {
	  return null;
  }
  
  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest arg2, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
  {
	SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	DistributorLevelListViewModel distributorLevelListViewModel = (DistributorLevelListViewModel) model;
	searchBaseWrapper.setBasePersistableModel(distributorLevelListViewModel);
	if(sortingOrderMap.isEmpty())
	{
		sortingOrderMap.put("distributorLevelName", SortingOrder.ASC);
	}
	searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	searchBaseWrapper = this.distributorLevelManager.searchDistributorLevel(searchBaseWrapper);
	return new ModelAndView(getSearchView(), "distributorLevelList",
                        searchBaseWrapper.getCustomList().
                         getResultsetList());	
  }
  
  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws Exception
  {
	  return null;
  }

  public void setDistributorLevelManager(DistributorLevelManager distributorLevelManager)
  {
	  this.distributorLevelManager = distributorLevelManager;
  }


}
