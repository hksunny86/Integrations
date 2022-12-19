package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface RetHeadRetCreditManager
{
  public BaseWrapper loadRetailerContactForRetailer(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createRetHeadRetTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper updateRetailerContactBalanceForRetailer(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException;
}
