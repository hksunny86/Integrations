package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface RetCustomerCreditManager
{
  public BaseWrapper updateRetToCustContactBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

}
