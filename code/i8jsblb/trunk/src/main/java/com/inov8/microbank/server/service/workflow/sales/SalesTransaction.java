package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.workflow.controller.TransactionProcessor;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Maqsood Shahzad
 * @version 1.0
 */



public abstract class SalesTransaction
    extends TransactionProcessor
{

  protected WorkFlowWrapper doProcess(WorkFlowWrapper wrapper) throws
      Exception
  {
    // your code here
    wrapper = this.doSale(wrapper);
    wrapper = this.logTransaction(wrapper);
    return wrapper;
  }

  protected abstract WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception;

}
