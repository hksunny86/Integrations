package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.integrationmodule.IntegrationModuleManager;

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
public class IntegrationModuleFacadeImpl
    implements IntegrationModuleFacade
{
  private IntegrationModuleManager integrationModuleManager;
  private FrameworkExceptionTranslator frameworkExceptionTranslator;

  public IntegrationModuleFacadeImpl()
  {}

  /**
   * searchIntegrationModule
   *
   * @param searchBaseWrapper SearchBaseWrapper
   * @return IntegrationModuleModel
   * @throws FrameworkCheckedException
   * @todo Implement this com.inov8.microbank.server.service.integrationmodule.IntegrationModuleManager method
   */
  public SearchBaseWrapper searchIntegrationModule(SearchBaseWrapper
      searchBaseWrapper) throws FrameworkCheckedException
  {
    try
    {
      this.integrationModuleManager.searchIntegrationModule(searchBaseWrapper);
    }
    catch (Exception ex)
    {
      throw this.frameworkExceptionTranslator.translate(ex,
          this.frameworkExceptionTranslator.
          FIND_BY_PRIMARY_KEY_ACTION);
    }
    return searchBaseWrapper;
  }

  public void setIntegrationModuleManager(IntegrationModuleManager
                                          integrationModuleManager)
  {
    this.integrationModuleManager = integrationModuleManager;
  }

  public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator
                                              frameworkExceptionTranslator)
  {
    this.frameworkExceptionTranslator = frameworkExceptionTranslator;
  }

}
