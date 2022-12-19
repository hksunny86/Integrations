package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface DistDistCreditManager
{
  public BaseWrapper updateDistToDistContactBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
}
