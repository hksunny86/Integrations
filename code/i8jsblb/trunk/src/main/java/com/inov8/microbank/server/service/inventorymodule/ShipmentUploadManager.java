package com.inov8.microbank.server.service.inventorymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

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
public interface ShipmentUploadManager
{

  public BaseWrapper saveOrUpdate(BaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

}
