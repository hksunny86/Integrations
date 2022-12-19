package com.inov8.microbank.server.service.workflow.credittransfer;

import com.inov8.microbank.common.model.NotificationMessageModel;
import com.inov8.microbank.common.util.MessageConstantsInterface;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
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
public abstract class CreditTransferTransaction
    extends TransactionProcessor
{
  private TransactionModuleManager transactionManager;

  /**
   * <p>Insert the transaction into the transaction tables</p>
   *
   *
   * @param wrapper
   * @return
   */



  /**
   * <p>Contains all the business logic for processing a transaction</p>
   *
   *
   * @param wrapper
   * @return
   */
  protected WorkFlowWrapper doProcess(WorkFlowWrapper wrapper) throws Exception
  {

    wrapper = this.doCreditTransfer(wrapper);
    wrapper = this.logTransaction(wrapper);
    NotificationMessageModel successMessage = new NotificationMessageModel();
    successMessage.setSmsMessageText(MessageConstantsInterface.CREIT_TRANSFER_MESSAGE);
    wrapper.setSuccessMessage(successMessage);
    return wrapper;

  }

  protected abstract WorkFlowWrapper doValidate(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper doCreditTransfer(WorkFlowWrapper wrapper) throws
      Exception;

  protected abstract WorkFlowWrapper logTransaction(WorkFlowWrapper wrapper) throws
      Exception;

  public void setTransactionManager(TransactionModuleManager transactionManager)
  {
    this.transactionManager = transactionManager;
  }

}
