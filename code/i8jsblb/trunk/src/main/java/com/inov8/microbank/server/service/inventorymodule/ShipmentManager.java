package com.inov8.microbank.server.service.inventorymodule;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ProductModel;

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

public interface ShipmentManager
{

  public SearchBaseWrapper loadShipment(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadShipment(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchShipment(SearchBaseWrapper
                                          searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createOrUpdateShipment(BaseWrapper
                                            baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper getOutstandingBalance(SearchBaseWrapper
                                                 searchBaseWrapper) throws
      FrameworkCheckedException;
  public BaseWrapper loadShipmentListViewByPrimaryKey(Long shipmentId) throws
      FrameworkCheckedException;
  
  public List<ProductModel> searchProductModels(Long supplierId)throws
  FrameworkCheckedException;
  
  public boolean isVariableProduct(Long productId) throws FrameworkCheckedException;
  
  public boolean isCurDateGTExpiryDate(Date expiryDate) throws Exception;
  

}
