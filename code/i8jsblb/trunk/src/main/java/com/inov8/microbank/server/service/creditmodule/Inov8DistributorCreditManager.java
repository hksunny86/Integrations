package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface Inov8DistributorCreditManager
{
  public BaseWrapper loadDistributorContactForInov8(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadOperator(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createInov8DistributorTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper updateInov8DistributorContactBalance(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException;

}
