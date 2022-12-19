package com.inov8.microbank.webapp.action.inventorymodule;

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
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ShipmentModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.inventorymodule.ShipmentListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;

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

public class ShipmentSearchController
    extends BaseSearchController
{
  private ShipmentManager shipmentManager;
  private ReferenceDataManager referenceDataManager;

  public ShipmentSearchController()
  {
    //setCommandName("shipmentListViewModel");
    //setCommandClass(ShipmentListViewModel.class);
    super.setFilterSearchCommandClass(ShipmentListViewModel.class);
  }

  public void setShipmentManager(ShipmentManager shipmentManager)
  {
    this.shipmentManager = shipmentManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws

      Exception
  {
    Long id = ServletRequestUtils.getLongParameter(request,
        "shipmentId");
    if (null != id)
    {
      if (log.isDebugEnabled())
      {
        log.debug(
            "id is not null....retrieving object from DB and then updating it");
      }

      BaseWrapper baseWrapper = new BaseWrapperImpl();
      ShipmentModel shipmentModel = new ShipmentModel();
      shipmentModel.setShipmentId(id);
      baseWrapper.setBasePersistableModel(shipmentModel);
      baseWrapper = this.shipmentManager.loadShipment(
          baseWrapper);
      shipmentModel = (ShipmentModel) baseWrapper.getBasePersistableModel();
      shipmentModel.setActive(activate);
      shipmentModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
      shipmentModel.setUpdatedOn(new Date());
      Date expiryDate = new Date();
      expiryDate = shipmentModel.getExpiryDate();
      if(shipmentModel.getExpiryDate()!=null){
	      Date currentDate = new Date();
	      currentDate.getTime();
	      int result;
	      result = currentDate.compareTo(expiryDate);
	      if (result < 1)
	      {
	        baseWrapper.setBasePersistableModel(shipmentModel);
	        this.shipmentManager.createOrUpdateShipment(baseWrapper);
	
	      }
	      else
	      {
	        this.saveMessage(request, "Cannot Activate Expired Shipment");
	      }
      }
      else{ //i.e expirey date is not given so no need to check it.
    	  baseWrapper.setBasePersistableModel(shipmentModel);
	      this.shipmentManager.createOrUpdateShipment(baseWrapper);
      }
    }
    ModelAndView modelAndView = new ModelAndView(new RedirectView(
        getSearchView() +
        ".html"));

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
     * code fragment to load reference data for ProductModel
     */
    ProductModel productModel = new ProductModel();
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        productModel, "name", SortingOrder.DESC);

    referenceDataManager.getReferenceData(referenceDataWrapper);

    List<ProductModel> productModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      productModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("productModelList", productModelList);
    return referenceDataMap;
  }
  
  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	  ShipmentListViewModel shipmentListViewModel = (ShipmentListViewModel)object;
	  searchBaseWrapper.setBasePersistableModel(shipmentListViewModel);
	  if(sortingOrderMap.isEmpty())
		  sortingOrderMap.put("supplierName", SortingOrder.ASC);
	  searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	  searchBaseWrapper = this.shipmentManager.searchShipment(searchBaseWrapper);

	  return new ModelAndView(getSearchView(), "shipmentModelList",
	                            searchBaseWrapper.getCustomList().
	                            getResultsetList());
  }

}
