package com.inov8.microbank.server.service.workflow.controller;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 *
 *@author Maqsood Shahzad
 */
public interface TransactionController
{
  /**
   * <p>This method starts the transaction</p>
   *
   *
   * @param wrapper
   * @return
   */
  WorkFlowWrapper start(WorkFlowWrapper wrapper) throws Exception;

  /**
   * <p>Here the actual transaction processing takes place</p>
   *
   *
   * @param wrapper
   * @return
   */
  WorkFlowWrapper process(WorkFlowWrapper wrapper) throws Exception;

  /**
   * <p>Tasks that need to be performed after the transaction has been processed</p>
   *
   *
   * @param wrapper
   * @return
   */
  WorkFlowWrapper end(WorkFlowWrapper wrapper) throws Exception;
  
  /**
   * <p>Tasks that need to be performed when we rollback the transaction with some integration partner</p>
   *
   *
   * @param wrapper
   * @return
   */
  
  WorkFlowWrapper rollback(WorkFlowWrapper wrapper) throws Exception;
  
  
  WorkFlowWrapper postUpdate(WorkFlowWrapper wrapper) throws Exception;

}
