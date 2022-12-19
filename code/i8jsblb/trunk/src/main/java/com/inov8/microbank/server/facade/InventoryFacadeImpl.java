package com.inov8.microbank.server.facade;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.wrapper.inventorymodule.ShipmentBaseWrapper;
import com.inov8.microbank.server.service.inventorymodule.ProductUnitManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentTypeManager;
import com.inov8.microbank.server.service.inventorymodule.ShipmentUploadManager;

public class InventoryFacadeImpl
    implements InventoryFacade
{
  private FrameworkExceptionTranslator frameworkExceptionTranslator;
  private ShipmentManager shipmentManager;
  private ShipmentTypeManager shipmentTypeManager;
  private ProductUnitManager productUnitManager;
  private ShipmentUploadManager shipmentUploadManager;

  public SearchBaseWrapper loadShipment(SearchBaseWrapper
                                        searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.shipmentManager.loadShipment(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;

  }

  public boolean isPriceUnitEmpty(Long productId) throws FrameworkCheckedException
  {
	  try
	    {
	      return this.productUnitManager.isPriceUnitEmpty(productId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	    }
  }
  
  public boolean isVariableProduct(Long productId) throws FrameworkCheckedException {
	    try
	    {
	      return this.shipmentManager.isVariableProduct(productId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	    }
}

  public boolean isCurDateGTExpiryDate(Date expiryDate) throws FrameworkCheckedException {
	    try
	    {
	      return this.shipmentManager.isCurDateGTExpiryDate(expiryDate);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
	    }
}
  
public BaseWrapper loadShipmentByPrimaryKey(Long shipmentId) throws FrameworkCheckedException {
	  BaseWrapper baseWrapper = new BaseWrapperImpl();
	  try
	    {
	      baseWrapper = this.productUnitManager.loadShipmentByPrimaryKey(shipmentId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_BY_PRIMARY_KEY_ACTION);

	    }

	    return baseWrapper;
  }

public BaseWrapper loadShipmentListViewByPrimaryKey(Long shipmentId) throws FrameworkCheckedException {
	  BaseWrapper baseWrapper = new BaseWrapperImpl();
	  try
	    {
		  
	      baseWrapper = this.shipmentManager.loadShipmentListViewByPrimaryKey(shipmentId);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.
	          FIND_BY_PRIMARY_KEY_ACTION);

	    }

	    return baseWrapper;
  }

public BaseWrapper loadShipment(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.shipmentManager.loadShipment(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }
    return baseWrapper;
  }

  public SearchBaseWrapper getOutstandingBalance(SearchBaseWrapper
                                                 searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.shipmentManager.getOutstandingBalance(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;
  }

  public SearchBaseWrapper searchShipment(SearchBaseWrapper
                                          searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try

    {
      this.shipmentManager.searchShipment(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper createOrUpdateShipment(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.shipmentManager.createOrUpdateShipment(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper loadProductUnit(SearchBaseWrapper
                                           searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productUnitManager.loadProductUnit(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }

    return searchBaseWrapper;

  }

  public BaseWrapper createOrUpdateProductUnit(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productUnitManager.createOrUpdateProductUnit(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
    }
    return baseWrapper;

  }

  public void setShipmentManager(ShipmentManager shipmentManager)
  {
    this.shipmentManager = shipmentManager;
  }

  public void setShipmentTypeManager(ShipmentTypeManager shipmentTypeManager)
  {
    this.shipmentTypeManager = shipmentTypeManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setProductUnitManager(ProductUnitManager productUnitManager)
  {
    this.productUnitManager = productUnitManager;
  }

  public void updateShipmentreadfromcsvfile(ShipmentBaseWrapper
                                            shipmentBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productUnitManager.updateShipmentreadfromcsvfile(shipmentBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }

  }

  public void updateShipment(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productUnitManager.updateShipment(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
  }

  public void setShipmentUploadManager(ShipmentUploadManager
                                       shipmentUploadManager)
  {
    this.shipmentUploadManager = shipmentUploadManager;
  }

  public BaseWrapper loadProductUnit(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productUnitManager.loadProductUnit(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);

    }
    return baseWrapper;
  }

  public SearchBaseWrapper searchProductUnit(SearchBaseWrapper
                                             searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.productUnitManager.searchProductUnit(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return searchBaseWrapper;

  }

  public BaseWrapper saveOrUpdate(BaseWrapper
                                  baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.shipmentUploadManager.saveOrUpdate(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;

  }

  public SearchBaseWrapper getUnsoldUnits(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.productUnitManager.getUnsoldUnits(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);

    }

  }

public void updateProductShipment(ProductUnitModel productUnitModel,int operation) throws FrameworkCheckedException {
	 try
	    {
	      this.productUnitManager.updateProductShipment(productUnitModel,operation);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	          this.frameworkExceptionTranslator.UPDATE_ACTION);
	    }
	
}

public List<ProductModel> searchProductModels(Long supplierId) throws FrameworkCheckedException {
	 List<ProductModel> productModelList = null;
	try
    {
			productModelList  =this.shipmentManager.searchProductModels(supplierId);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    
    return productModelList;
}

public int isProductUnit(ProductUnitModel productUnitModel) throws FrameworkCheckedException {
	
	
	try
    {
      return this.productUnitManager.isProductUnit(productUnitModel);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }

}

public void updateProductShipment(List<ProductUnitModel> productUnitModelList) throws FrameworkCheckedException
{
	// TODO Auto-generated method stub
	try
    {
      this.productUnitManager.updateProductShipment(productUnitModelList);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.UPDATE_ACTION);
    }

	
}

}
