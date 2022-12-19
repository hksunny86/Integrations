package com.inov8.microbank.server.service.workflow.factory;

import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.workflow.controller.TransactionController;

public interface WorkFlowTransactionFactory
{
  public TransactionController getTransactionProcessor(WorkFlowWrapper
      workFlowWrapper)throws Exception;
}
