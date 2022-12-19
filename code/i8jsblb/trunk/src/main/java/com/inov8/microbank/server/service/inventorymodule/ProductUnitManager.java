package com.inov8.microbank.server.service.inventorymodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ProductUnitModel;
import com.inov8.microbank.common.wrapper.inventorymodule.ShipmentBaseWrapper;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */
public interface ProductUnitManager
{
  public SearchBaseWrapper loadProductUnit(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createOrUpdateProductUnit(BaseWrapper
                                               baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadProductUnit(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper getUnsoldUnits(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchProductUnit(SearchBaseWrapper
                                             searchBaseWrapper) throws
      FrameworkCheckedException;

  public void updateShipmentreadfromcsvfile(ShipmentBaseWrapper
                                            shipmentBaseWrapper) throws
      FrameworkCheckedException;

  public void updateShipment(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  public void updateProductShipment(ProductUnitModel productUnitModel,int operation) throws FrameworkCheckedException;
  public BaseWrapper loadShipmentByPrimaryKey(Long shipmentId) throws 
      FrameworkCheckedException;
  
  public int  isProductUnit(ProductUnitModel productUnitModel) throws FrameworkCheckedException;
  
  public boolean isPriceUnitEmpty(Long productId) throws FrameworkCheckedException;
  
  public void updateProductShipment(List<ProductUnitModel> productUnitModelList) throws FrameworkCheckedException;

}
