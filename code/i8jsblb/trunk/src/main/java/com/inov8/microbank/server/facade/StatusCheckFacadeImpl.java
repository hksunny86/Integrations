package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.statuscheckmodule.StatusCheckManager;

/**
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
public class StatusCheckFacadeImpl
    implements StatusCheckFacade
{
  private StatusCheckManager statusCheckManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  /**
   * getIMStatus
   *
   * @param workFlowWrapper WorkFlowWrapper
   * @return SearchBaseWrapper
   * @throws FrameworkCheckedException
   *
   */
  public SearchBaseWrapper getIMStatus(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.statusCheckManager.getIMStatus(workFlowWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
  }

  /**
   * isAllIntegrationModulesAlive
   *
   * @param searchBaseWrapper SearchBaseWrapper
   * @param workFlowWrapper WorkFlowWrapper
   * @return boolean
   * @throws FrameworkCheckedException
   *
   */
  public boolean isAllIntegrationModulesAlive(SearchBaseWrapper
                                              searchBaseWrapper,
                                              WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    try
    {
      return this.statusCheckManager.isAllIntegrationModulesAlive(
          searchBaseWrapper, workFlowWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.INSERT_ACTION);
    }
  }

  public void setStatusCheckManager(StatusCheckManager statusCheckManager)
  {
    this.statusCheckManager = statusCheckManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

  @Override
  public void checkActiveMQStatus() throws WorkFlowException {
	  this.statusCheckManager.checkActiveMQStatus();
  }
  
}
