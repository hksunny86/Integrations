package com.inov8.microbank.webapp.action.commissionmodule;

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
import com.inov8.microbank.common.model.commissionmodule.CommissionReasonListViewModel;
import com.inov8.microbank.server.service.commissionmodule.CommissionReasonManager;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class CommissionReasonSearchController
    extends BaseSearchController
{

  private CommissionReasonManager commissionReasonManager;

  public CommissionReasonSearchController()
  {
    super.setFilterSearchCommandClass(CommissionReasonListViewModel.class);
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws Exception
  {
    return null;
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    return null;
  }

  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
                                  Object model, HttpServletRequest request,
                                  LinkedHashMap sortingOrderMap) throws
      Exception
  {
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
    CommissionReasonListViewModel commissionReasonListViewModel = (
        CommissionReasonListViewModel) model;
    searchBaseWrapper.setBasePersistableModel(commissionReasonListViewModel);
    if(sortingOrderMap.isEmpty())
        sortingOrderMap.put("name", SortingOrder.ASC);
    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.commissionReasonManager.searchCommissionReason(
        searchBaseWrapper);

    return new ModelAndView(getSearchView(), "commissionReasonList",
                            searchBaseWrapper.getCustomList().
                            getResultsetList());

  }

  public void setCommissionReasonManager(CommissionReasonManager
                                         commissionReasonManager)
  {
    this.commissionReasonManager = commissionReasonManager;
  }

}
