package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.operatormodule.OperatorDAO;
import com.inov8.microbank.server.service.operatormodule.OperatorManager;

public class Inov8CustomerCreditManagerImpl
    implements Inov8CustomerCreditManager
{
  OperatorDAO operatorDAO;
  private OperatorManager operatorManager;

  public BaseWrapper updateInov8ToCustomerContactBalance(BaseWrapper
      baseWrapper) throws FrameworkCheckedException
  {
    System.out.println("^^^^^^^^^^^^^ Inov8CustomerCreditManagerImpl.updateInov8ToCustomerContactBalance( BaseWrapper ) ");

    WorkFlowWrapper workFlowWrapper = (WorkFlowWrapper) baseWrapper;

    // Updates the balance of the Operator
    baseWrapper.setBasePersistableModel(workFlowWrapper.getOperatorModel());
    workFlowWrapper.setOperatorModel( (OperatorModel)this.operatorManager.
                                     loadOperator(
                                         baseWrapper).getBasePersistableModel());

    workFlowWrapper.getOperatorModel().setBalance(workFlowWrapper.
                                                  getOperatorModel().getBalance() +
                                                  workFlowWrapper.
                                                  getTransactionModel().
                                                  getTransactionAmount());

    baseWrapper.setBasePersistableModel(workFlowWrapper.getOperatorModel());
    workFlowWrapper.setOperatorModel( (OperatorModel) ( (this.operatorManager.
        updateOperator(baseWrapper)).getBasePersistableModel()));

    return baseWrapper;
  }

  public void setOperatorDAO(OperatorDAO operatorDAO)
  {
    this.operatorDAO = operatorDAO;
  }

  public void setOperatorManager(OperatorManager operatorManager)
  {
    this.operatorManager = operatorManager;
  }

}
