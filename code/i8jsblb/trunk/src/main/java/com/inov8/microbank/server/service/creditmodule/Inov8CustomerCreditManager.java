package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface Inov8CustomerCreditManager
{
  public BaseWrapper updateInov8ToCustomerContactBalance(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException;
}
