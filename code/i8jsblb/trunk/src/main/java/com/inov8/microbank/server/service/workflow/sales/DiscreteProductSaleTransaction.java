package com.inov8.microbank.server.service.workflow.sales;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

public abstract class DiscreteProductSaleTransaction
    extends SalesTransaction
{
  public DiscreteProductSaleTransaction()
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
    return this.doDiscreteProductSaleTransaction(wrapper);
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

  public abstract WorkFlowWrapper doDiscreteProductSaleTransaction(
      WorkFlowWrapper wrapper) throws Exception;

}
