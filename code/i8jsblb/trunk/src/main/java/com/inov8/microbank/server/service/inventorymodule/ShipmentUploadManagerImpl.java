package com.inov8.microbank.server.service.inventorymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.dao.inventorymodule.ProductUnitDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ShipmentUploadManagerImpl
    implements ShipmentUploadManager
{

  private ProductUnitDAO productUnitDAO;

  public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    return null;
  }

  public void setProductUnitDAO(ProductUnitDAO productUnitDAO)
  {
    this.productUnitDAO = productUnitDAO;
  }

}
