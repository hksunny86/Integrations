package com.inov8.microbank.server.service.failurelogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface FailureLogManager
{
	/*
  public BaseWrapper saveLog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
*/
  public BaseWrapper auditLogRequiresNewTransaction(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

}
