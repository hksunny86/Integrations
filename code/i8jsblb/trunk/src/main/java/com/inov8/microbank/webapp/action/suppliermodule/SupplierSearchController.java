package com.inov8.microbank.webapp.action.suppliermodule;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;

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
 **/

public class SupplierSearchController
    extends BaseSearchController
{
  private SupplierManager supplierManager;
  private ReferenceDataManager referenceDataManager;

  public SupplierSearchController()
   {
	  
	  super.setFilterSearchCommandClass(SupplierListViewModel.class);
//     super.setCommandName("supplierListViewModel");
//     super.setCommandClass(SupplierListViewModel.class);
  }



   protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,Object model,HttpServletRequest httpServletRequest,
                                  
                                  LinkedHashMap sortingOrderMap) throws
      Exception
  {
	SupplierListViewModel supplierModel = (SupplierListViewModel) model;
    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
    searchBaseWrapper.setBasePersistableModel(supplierModel);
    if(sortingOrderMap.isEmpty())	
    	sortingOrderMap.put("name", SortingOrder.ASC);
    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.supplierManager.searchSupplier(
        searchBaseWrapper);

    return new ModelAndView(getSearchView(), "supplierModelList",
                            searchBaseWrapper.getCustomList().getResultsetList());

  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws
      Exception
  {
    Long id = ServletRequestUtils.getLongParameter(request,
        "supplierId");
    Integer versionNo = ServletRequestUtils.getIntParameter(request,
        "versionNo");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug(
            "id is not null....retrieving object from DB and then updating it");
      }

      BaseWrapper baseWrapper = new BaseWrapperImpl();
      SupplierModel supplierModel = new SupplierModel();
      supplierModel.setSupplierId(id);
      supplierModel.setVersionNo(versionNo);
      baseWrapper.setBasePersistableModel(supplierModel);
      baseWrapper = this.supplierManager.loadSupplier(
          baseWrapper);
      //Set the active flag
      
      
      supplierModel = (SupplierModel) baseWrapper.getBasePersistableModel();
      supplierModel.setActive(activate);
      supplierModel.setUpdatedOn(new Date());
      supplierModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());       
      this.supplierManager.createOrUpdateSupplier(baseWrapper);

    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(super.
        getSearchView() + ".html"));
    return modelAndView;
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    return null;
  }

  public void setSupplierManager(SupplierManager supplierManager)
 {
   this.supplierManager = supplierManager;
 }

 public void setReferenceDataManager(ReferenceDataManager refereceDataManager)
 {
   this.referenceDataManager = referenceDataManager;
 }







}
