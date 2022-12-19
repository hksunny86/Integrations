package com.inov8.microbank.webapp.action.inventorymodule;

import java.util.Date;
import java.util.LinkedHashMap;

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
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.common.model.inventorymodule.ProductUnitListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;

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

public class ProductUnitSearchController
    extends BaseSearchController
{
  private ProductUnitManager productUnitManager;

  private Long productId;

  private Long shipmentId;

  public ProductUnitSearchController()
  {
    super.setFilterSearchCommandClass(ProductUnitListViewModel.class);
  }

  public void setProductUnitManager(ProductUnitManager productUnitManager)
  {
    this.productUnitManager = productUnitManager;
  }

  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
                                  Object object,
                                  HttpServletRequest httpServletRequest,
                                  LinkedHashMap sortingOrderMap) throws
      Exception
  {
    shipmentId = ServletRequestUtils.getLongParameter(httpServletRequest,
        "shipmentId");

    productId = ServletRequestUtils.getLongParameter(httpServletRequest,
        "productId");

    if (null != shipmentId)
    {
      if (log.isDebugEnabled())
      {
        log.debug(
            "shipmentId is not null....retrieving object from DB for List");
      }
    }

    SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
    ProductUnitListViewModel productUnitListViewModel = (
        ProductUnitListViewModel)
        object;
    productUnitListViewModel.setShipmentId(shipmentId);
    searchBaseWrapper.setBasePersistableModel(productUnitListViewModel);
    if(sortingOrderMap.isEmpty())
	      sortingOrderMap.put("userName", SortingOrder.ASC);
    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
    searchBaseWrapper = this.productUnitManager.searchProductUnit(
        searchBaseWrapper);
    
    /////////////////////// loading the data for shipment //////////////////////
    BaseWrapper baseWrapper = new BaseWrapperImpl();
    baseWrapper = this.productUnitManager.loadShipmentByPrimaryKey(shipmentId);
    ShipmentModel shipmentModel = (ShipmentModel)baseWrapper.getBasePersistableModel();
    httpServletRequest.setAttribute("isShipmentActive", shipmentModel.getActive());
    Boolean isShipmentNotExpired = true;
    Date expiryDate = new Date();
    expiryDate = shipmentModel.getExpiryDate();
       if(shipmentModel.getExpiryDate() != null){
	      Date currentDate = new Date();
	      currentDate.getTime();
	      int result;
	      result = currentDate.compareTo(expiryDate);
	      if (result < 1)
	      {
	         isShipmentNotExpired = true;
	      }
	      else
	      {
	        isShipmentNotExpired = false;
	      }
       }
    httpServletRequest.setAttribute("isShipmentNotExpired", isShipmentNotExpired);
    
    ////////////////////////////////////////////////////////////////////////////
    
    return new ModelAndView(getSearchView(), "productUnitModelList",
                            searchBaseWrapper.getCustomList().
                            getResultsetList());

  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws

      Exception
  {
    Long id = ServletRequestUtils.getLongParameter(request,
        "productUnitId");

    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug(
            "id is not null....retrieving object from DB and then updating it");
      }

      BaseWrapper baseWrapper = new BaseWrapperImpl();
      ProductUnitModel productUnitModel = new ProductUnitModel();
      productUnitModel.setProductUnitId(id);
      baseWrapper.setBasePersistableModel(productUnitModel);
      baseWrapper = this.productUnitManager.loadProductUnit(baseWrapper);
      productUnitModel = (ProductUnitModel) baseWrapper.getBasePersistableModel();
      productUnitModel.setActive(activate);
      productUnitModel.setUpdatedBy( UserUtils.getCurrentUser().getAppUserId() );
      productUnitModel.setUpdatedOn( new Date() );
      baseWrapper.setBasePersistableModel(productUnitModel);
      this.productUnitManager.createOrUpdateProductUnit(baseWrapper);
      /*if (activate==true)
      {
      this.productUnitManager.updateProductShipment(productUnitModel,0);
      }
      else 
      {
    	  
    	  this.productUnitManager.updateProductShipment(productUnitModel,1);
      }*/
    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(getSearchView() +
        ".html?shipmentId=" + shipmentId + "&productId=" + productId));
    return modelAndView;
  }

}
