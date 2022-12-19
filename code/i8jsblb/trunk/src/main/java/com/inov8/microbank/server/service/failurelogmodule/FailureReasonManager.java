package com.inov8.microbank.server.service.failurelogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public interface FailureReasonManager
{
  public BaseWrapper loadFailureReasonByName(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
}
