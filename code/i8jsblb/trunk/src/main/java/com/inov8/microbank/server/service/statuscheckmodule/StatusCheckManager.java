package com.inov8.microbank.server.service.statuscheckmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

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

public interface StatusCheckManager
{

  public SearchBaseWrapper getIMStatus(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException;

  public boolean isAllIntegrationModulesAlive(SearchBaseWrapper
                                              searchBaseWrapper,
                                              WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException;
  
  public void checkActiveMQStatus() throws WorkFlowException;

  
}
