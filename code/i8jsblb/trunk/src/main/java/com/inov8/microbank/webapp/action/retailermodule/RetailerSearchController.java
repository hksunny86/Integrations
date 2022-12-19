package com.inov8.microbank.webapp.action.retailermodule;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.RetailerTypeModel;
import com.inov8.microbank.common.model.retailermodule.RetailerListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.retailermodule.RetailerManager;

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

public class RetailerSearchController
    extends BaseSearchController
{
  private RetailerManager retailerManager;
  private ReferenceDataManager referenceDataManager;

  public RetailerSearchController()
  {
    //super.setCommandName("retailerListViewModel");
    //super.setCommandClass(RetailerListViewModel.class);
    super.setFilterSearchCommandClass(RetailerListViewModel.class);
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws
      Exception
  {
    Long id = ServletRequestUtils.getLongParameter(request,
        "retailerId");

    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug(
            "id is not null....retrieving object from DB and then updating it");
      }

      BaseWrapper baseWrapper = new BaseWrapperImpl();
      RetailerModel retailerModel = new RetailerModel();
      retailerModel.setRetailerId(id);

      baseWrapper.setBasePersistableModel(retailerModel);
      baseWrapper = this.retailerManager.loadRetailer(
          baseWrapper);
      //Set the active flag
      retailerModel = (RetailerModel) baseWrapper.getBasePersistableModel();
      retailerModel.setUpdatedOn(new Date());
      retailerModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      retailerModel.setActive(activate);
      this.retailerManager.createOrUpdateRetailer(baseWrapper);

    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(super.getSearchView() + ".html"));
    return modelAndView;
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    if (log.isDebugEnabled())
    {
      log.debug("Inside reference data");
    }

    /**
     * code fragment to load reference data for RetailerTypeModel
     */
    RetailerTypeModel retailerTypeModel = new RetailerTypeModel();
//    retailerTypeModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        retailerTypeModel, "name", SortingOrder.ASC);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex2)
    {
    }
    List<RetailerTypeModel> RetailerTypeModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      RetailerTypeModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("RetailerTypeModelList", RetailerTypeModelList);

    /**
     * code fragment to load reference data for AreaModel
     */
    AreaModel AreaModel = new AreaModel();
//    AreaModel.setActive(true);
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        AreaModel, "name", SortingOrder.DESC);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex1)
    {
    }
    List<AreaModel> AreaModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      AreaModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("AreaModelList", AreaModelList);

    /**
     * code fragment to load reference data for DistributorModel
     */

    DistributorModel distributorModel = new DistributorModel();
    distributorModel.setActive(true);
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        distributorModel, "name", SortingOrder.DESC);
    referenceDataWrapper.setBasePersistableModel(distributorModel);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex)
    {
    }
    List<DistributorModel> DistributorModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      DistributorModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("DistributorModelList", DistributorModelList);

    return referenceDataMap;

  }
  
  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	  RetailerListViewModel retailerModel = (RetailerListViewModel) model;
	  searchBaseWrapper.setBasePersistableModel(retailerModel);
	  if(sortingOrderMap.isEmpty())
	      sortingOrderMap.put("name", SortingOrder.ASC);
	  searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	  searchBaseWrapper = this.retailerManager.searchRetailer(
	        searchBaseWrapper);

	  return new ModelAndView(super.getSearchView(), "retailerModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
  }


  public void setRetailerManager(RetailerManager retailerManager)
  {
    this.retailerManager = retailerManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

}
