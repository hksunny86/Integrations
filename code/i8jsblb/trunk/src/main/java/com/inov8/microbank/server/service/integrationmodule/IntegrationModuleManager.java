package com.inov8.microbank.server.service.integrationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

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
public interface IntegrationModuleManager
{
  public SearchBaseWrapper searchIntegrationModule(SearchBaseWrapper
      searchBaseWrapper) throws
      FrameworkCheckedException;
}
