package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;
import com.inov8.microbank.server.service.failurelogmodule.FailureReasonManager;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class AuditLogFacadeImpl
    implements AuditLogFacade
{
  private FrameworkExceptionTranslator frameworkExceptionTranslator;
  private FailureLogManager failureLogManager;
  private FailureReasonManager failureReasonManager;

  public AuditLogFacadeImpl()
  {
  }

  /**
   * saveLog
   *
   * @param auditLogModuleWrapper AuditLogModuleWrapper
   * @return AuditLogModuleWrapper
   * @throws FrameworkCheckedException
   */
  /*
  public BaseWrapper saveLog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.failureLogManager.saveLog(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
  }
*/
  //*******************************************//
  //       FailureReasonManagerMethods
  //******************************************//

  public BaseWrapper loadFailureReasonByName(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      this.failureReasonManager.loadFailureReasonByName(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
    return baseWrapper;
  }

  public BaseWrapper auditLogRequiresNewTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.failureLogManager.auditLogRequiresNewTransaction(baseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.FIND_ACTION);
    }
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  public void setFailureLogManager(FailureLogManager
                                   failureLogManager)
  {

    this.failureLogManager = failureLogManager;
  }

  public void setFailureReasonManager(FailureReasonManager failureReasonManager)
  {
    this.failureReasonManager = failureReasonManager;
  }

}
