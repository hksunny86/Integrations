package com.inov8.microbank.webapp.action.productmodule;

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
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.productmodule.ProductListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.productmodule.ProductCatalogManager;
import com.inov8.microbank.server.service.productmodule.ProductManager;

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

public class ProductSearchController

    extends BaseSearchController
{
  private ProductManager productManager;
  private ReferenceDataManager referenceDataManager;
  private ProductCatalogManager catalogManager;

  public ProductSearchController()
  {
    super.setFilterSearchCommandClass(ProductListViewModel.class);
  }

  public void setProductManager(ProductManager productManager)
  {
    this.productManager = productManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setCatalogManager(ProductCatalogManager catalogManager)
  {
    this.catalogManager = catalogManager;
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws
      Exception
  {
    Long id = ServletRequestUtils.getLongParameter(request,
        "productId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug(
            "id is not null....retrieving object from DB and then updating it");
      }

      BaseWrapper baseWrapper = new BaseWrapperImpl();
      ProductModel productModel = new ProductModel();
      productModel.setPrimaryKey(id);
      baseWrapper.setBasePersistableModel(productModel);
      baseWrapper = this.productManager.loadProduct(
          baseWrapper);

      //If product is being activated, it needs to be added in catalog named All
      /*if (activate)
      {
        BaseWrapper catalogueBaseWrapper = new BaseWrapperImpl();
        catalogueBaseWrapper.setBasePersistableModel(productModel);
        this.catalogManager.includeProductInCatalogAll(baseWrapper);
      }
      else
      { //exclude product form catalog ALL if product is being de-activated
        this.catalogManager.excludeProductInCatalogAll(baseWrapper);

      }*/

      //Set the active flag
      
      productModel = (ProductModel) baseWrapper.getBasePersistableModel();
      productModel.setActive(activate);
      productModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      productModel.setUpdatedOn(new Date());
      baseWrapper.setBasePersistableModel(productModel);
      AppUserModel appUserModel = new AppUserModel();

      this.productManager.createOrUpdateProduct(baseWrapper);

    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(
        getSearchView() + ".html"));
    return modelAndView;
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    /**
     * code fragment to load reference data for SupplierModel
     */
    SupplierModel supplierModel = new SupplierModel();
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        supplierModel, "name", SortingOrder.DESC);

    referenceDataManager.getReferenceData(referenceDataWrapper);

    List<SupplierModel> supplierModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      supplierModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("supplierModelList", supplierModelList);

    /**
     * code fragment to load reference data for ServiceModel
     */

    ServiceModel serviceModel = new ServiceModel();
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        serviceModel, "name", SortingOrder.DESC);
    referenceDataWrapper.setBasePersistableModel(serviceModel);
    referenceDataManager.getReferenceData(referenceDataWrapper);
    List<ServiceModel> ServiceModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      ServiceModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("ServiceModelList", ServiceModelList);

    return referenceDataMap;

  }

  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	  ProductListViewModel productListViewModel = (ProductListViewModel) model;
	  searchBaseWrapper.setBasePersistableModel(productListViewModel);
	  if(sortingOrderMap.isEmpty())
	      sortingOrderMap.put("productName", SortingOrder.ASC);
	  searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	  searchBaseWrapper = this.productManager.searchProduct(
	        searchBaseWrapper);

	  return new ModelAndView(getSearchView(), "productModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
  }


  public ProductCatalogManager getCatalogManager()
  {
    return catalogManager;
  }

}
