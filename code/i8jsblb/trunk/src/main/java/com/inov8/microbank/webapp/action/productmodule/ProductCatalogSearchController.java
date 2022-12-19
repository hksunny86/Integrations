package com.inov8.microbank.webapp.action.productmodule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.ProductCatalogDetailModel;
import com.inov8.microbank.common.model.ProductCatalogModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.productmodule.ProductListViewModel;
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

public class ProductCatalogSearchController

    extends BaseSearchController
{
  private ProductManager productManager;
  private ReferenceDataManager referenceDataManager;
  private ProductCatalogManager catalogManager;

  public ProductCatalogSearchController()
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
   
   
	  return null;
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
	  return null;
  }

  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	 
	  System.out.println(" onSearch of ProductCatalogSearchController called..... ");
	  String pCatIdStr = httpServletRequest.getParameter("productCatalogId");
	  
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	  ProductListViewModel productListViewModel = (ProductListViewModel) model;
	  productListViewModel.setActive(true);
	  sortingOrderMap.put("productName", SortingOrder.ASC);
	  searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	  searchBaseWrapper.setBasePersistableModel(productListViewModel);
	  //sortingOrderMap.put("supplierName", SortingOrder.ASC);
	  String value = (String)httpServletRequest.getAttribute("checkBox3213");
	  searchBaseWrapper = this.productManager.searchProduct(
	        searchBaseWrapper);
	  List<ProductListViewModel> productsList = searchBaseWrapper.getCustomList().getResultsetList();
	  
      if(pCatIdStr==null || "".equals(pCatIdStr)){ // new product catalog scenario
    	  
    	  return new ModelAndView(getSearchView(), "prodList",
                  searchBaseWrapper.getCustomList().getResultsetList());  
      }
      else{ // edit scenario
    	  //httpServletRequest.getSession().setAttribute("productList", searchBaseWrapper.getCustomList().getResultsetList());  
          //httpServletRequest.getSession().setAttribute("pagingHelperModel", pagingHelperModel);
          //httpServletRequest.getSession().setAttribute("sortingOrderMap", sortingOrderMap);
          
          ////////////////// new code ///////////////
          SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
          ProductCatalogModel productCatalogModel = new ProductCatalogModel();
          productCatalogModel.setPrimaryKey(Long.valueOf(pCatIdStr.trim()));
          sBaseWrapper.setBasePersistableModel(productCatalogModel);
          searchBaseWrapper = this.catalogManager.loadCatalog(sBaseWrapper);
          productCatalogModel = (ProductCatalogModel)searchBaseWrapper.getBasePersistableModel();
          httpServletRequest.setAttribute("name", productCatalogModel.getName());
          httpServletRequest.setAttribute("description", productCatalogModel.getDescription());
          httpServletRequest.setAttribute("comments", productCatalogModel.getComments());
          httpServletRequest.setAttribute("productCatalogId", productCatalogModel.getProductCatalogId());
          httpServletRequest.setAttribute("createdOn", productCatalogModel.getCreatedOn());
          httpServletRequest.setAttribute("createdBy", productCatalogModel.getCreatedBy());
          httpServletRequest.setAttribute("updatedBy", productCatalogModel.getUpdatedBy());
          httpServletRequest.setAttribute("updatedOn", productCatalogModel.getUpdatedOn());
          httpServletRequest.setAttribute("versionNo", productCatalogModel.getVersionNo());

          ///////////////////////////////////////////
          ProductCatalogDetailModel catalogProdutDetailModel = new
          ProductCatalogDetailModel();
          catalogProdutDetailModel.setProductCatalogId(Long.valueOf(pCatIdStr.trim()));
      
          BaseWrapper baseWrapper = new BaseWrapperImpl();
          baseWrapper.setBasePersistableModel(catalogProdutDetailModel);
          baseWrapper = this.catalogManager.loadCatalogProducts(baseWrapper);
          ProductCatalogModel catModel = (ProductCatalogModel) baseWrapper.
               getBasePersistableModel();

          Collection<ProductCatalogDetailModel> list = catModel.
                getProductCatalogIdProductCatalogDetailModelList();
          List<ProductListViewModel> prodList = new ArrayList<ProductListViewModel> ();
          Iterator<ProductCatalogDetailModel> iter = list.iterator();
          while (iter.hasNext())
          {

              ProductListViewModel prodListModel = new ProductListViewModel();
              ProductCatalogDetailModel productCatDetailModel = iter.next();

              ProductModel prodModel = productCatDetailModel.getProductIdProductModel();
              prodListModel.setActive(prodModel.getActive());
              prodListModel.setProductId(prodModel.getPrimaryKey());
              prodListModel.setProductName(prodModel.getName());
              prodListModel.setVersionNo(prodModel.getVersionNo());
              prodListModel.setSupplierName(prodModel.getSupplierIdSupplierModel().
                                      getName());
              prodListModel.setChecked(true);
              prodList.add(prodListModel);
          }

          //Get all product list but include only those which are not already
          //included
          List allProductsList = this.getAllActiveProducts(httpServletRequest);
          for (int i = 0; i < productsList.size(); i++)
          {
              ProductListViewModel prod = (ProductListViewModel) productsList.get(i);
              
          if ( isAMemberOfTheList(prod, prodList) )
          {
              prod.setChecked(true);
              productsList.set(i, prod);
          }
      }

          ///////////////////////////////////////////
          return new ModelAndView(getSearchView(), "prodList", productsList);
      }
	  
      
	  
  }
  
  private boolean isAMemberOfTheList(ProductListViewModel prod, List selectedProducts){
	  Iterator itr = selectedProducts.iterator();
	  while(itr.hasNext()){
		  ProductListViewModel plvm = (ProductListViewModel)itr.next();
		  if(plvm.getProductId().equals(prod.getProductId()))
			  return true;
	  }
	  return false;
  }
  
///////////////////////////////////////////////////////////////////////////////
  private List getAllActiveProducts(HttpServletRequest request) throws Exception
  {

    //Fetch list of all products
    ProductListViewModel prodModel = new ProductListViewModel();
    //Only active products need to be shown.  So  setting active
    //method of ProductListViewModel so that Hibernate could ultimately
    //translate it to a "where is_active = 1" clause
    prodModel.setActive(true);
    SearchBaseWrapper wrapper = new SearchBaseWrapperImpl();
    wrapper.setBasePersistableModel(prodModel);
    LinkedHashMap<String, SortingOrder>
        sortingOrderMap = new LinkedHashMap<String, SortingOrder> ();
    if(request.getSession().getAttribute("sortingOrderMap")==null){
        sortingOrderMap.put("supplierName", SortingOrder.ASC);
        sortingOrderMap.put("productName", SortingOrder.ASC);
        wrapper.setSortingOrderMap(sortingOrderMap);
    }
    else{
    	wrapper.setSortingOrderMap((LinkedHashMap)request.getSession().getAttribute("sortingOrderMap"));
    	wrapper.setPagingHelperModel((PagingHelperModel)request.getSession().getAttribute("pagingHelperModel"));
    }
    

//    wrapper.setSortingOrderMap();
    SearchBaseWrapper result = productManager.searchProduct(wrapper);
    if (log.isDebugEnabled())
    {
      log.debug("Prodcut list lengtth is : " +
                result.getCustomList().getResultsetList().size());
    }

    return result.getCustomList().getResultsetList();

  }

///////////////////////////////////////////////////////////////////////////////

  
  public ProductCatalogManager getCatalogManager()
  {
    return catalogManager;
  }

}
