package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

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

public abstract class VariableProductSaleTransaction
    extends SalesTransaction
{
  public VariableProductSaleTransaction()
  {
  }

  /**
   * doSale
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @todo Implement this
   *   com.inov8.microbank.server.workflow.transaction.SalesTransaction method
   */
  protected WorkFlowWrapper doSale(WorkFlowWrapper wrapper) throws Exception
  {
    return this.doVariableProductSale(wrapper);
  }

  /**
   * doValidate
   *
   * @param wrapper WorkFlowWrapper
   * @return WorkFlowWrapper
   * @throws Exception
   * @todo Implement this
   *   com.inov8.microbank.server.workflow.transaction.TransactionProcessorImpl
   *   method
   */
  public abstract WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
      Exception;

  public abstract WorkFlowWrapper doVariableProductSale(WorkFlowWrapper wrapper) throws
      Exception;
}
