package com.inov8.microbank.server.service.integration.dispenser;

import com.inov8.framework.common.exception.FrameworkCheckedException;
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
public interface ProductDispenser
{

  public WorkFlowWrapper verify(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException;

  public WorkFlowWrapper doSale(WorkFlowWrapper workFlowWrapper) throws
      Exception;

  public WorkFlowWrapper rollback(WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException;

}
