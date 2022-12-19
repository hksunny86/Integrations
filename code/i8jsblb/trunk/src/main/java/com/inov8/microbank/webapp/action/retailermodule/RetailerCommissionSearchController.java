package com.inov8.microbank.webapp.action.retailermodule;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.retailermodule.RetailerCommissionListViewModel;
import com.inov8.microbank.server.service.retailermodule.RetailerCommissionManager;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened Application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 * @todo There is a need to set Retailer Commission according to Retailer Head logged in.
 */

public class RetailerCommissionSearchController
    extends BaseFormSearchController
{
  private ReferenceDataManager referenceDataManager;
  private RetailerCommissionManager retailerCommissionManager;

  public RetailerCommissionSearchController()
  {
    super.setCommandName("retailerCommissionListViewModel");
    super.setCommandClass(RetailerCommissionListViewModel.class);
  }

  @Override
  protected Map loadReferenceData(HttpServletRequest request) throws Exception
  {
    return null;
  }

  @Override
  protected ModelAndView onSearch(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  PagingHelperModel pagingHelperModel,
                                  LinkedHashMap sortingOrderMap) throws
      Exception
  {
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
    RetailerCommissionListViewModel retailerCommissionListViewModel = (
        RetailerCommissionListViewModel) command;
    searchBaseWrapper.setBasePersistableModel(retailerCommissionListViewModel);
    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.retailerCommissionManager.searchRetailerCommission(
        searchBaseWrapper);

    return new ModelAndView(super.getSuccessView(),
                            "retailerCommissionModelList",
                            searchBaseWrapper.getCustomList().getResultsetList());

  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setRetailerCommissionManager(RetailerCommissionManager
                                           retailerCommissionManager)
  {
    this.retailerCommissionManager = retailerCommissionManager;
  }

}
