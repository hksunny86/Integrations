package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.OperatorModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.dao.operatormodule.OperatorDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;

public class Inov8DistributorCreditManagerImpl
    implements Inov8DistributorCreditManager
{
  private TransactionDAO transactionDAO;
  private DistributorContactDAO distributorContactDAO;
  private OperatorDAO operatorDAO;

  public BaseWrapper loadDistributorContactForInov8(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    DistributorContactModel distributorContactModel = this.
        distributorContactDAO.findByPrimaryKey(
            baseWrapper.getBasePersistableModel().
            getPrimaryKey());
    baseWrapper.setBasePersistableModel(distributorContactModel);
    return baseWrapper;

  }

  public BaseWrapper loadOperator(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    OperatorModel operatorModel = this.operatorDAO.findByPrimaryKey(
        baseWrapper.getBasePersistableModel().
        getPrimaryKey());
    baseWrapper.setBasePersistableModel(operatorModel);
    return baseWrapper;

  }

  public BaseWrapper createInov8DistributorTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    TransactionModel transactionModel = (TransactionModel)
        baseWrapper.getBasePersistableModel();

    transactionModel = this.transactionDAO.saveOrUpdate( (
        TransactionModel) baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(transactionModel);
    return baseWrapper;

  }

  public BaseWrapper updateInov8DistributorContactBalance(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException
  {
    WorkFlowWrapper workFlowWrapper = (WorkFlowWrapper) baseWrapper;

    DistributorContactModel nationalManager = workFlowWrapper.getToDistributorContactModel();
    nationalManager = this.distributorContactDAO.saveOrUpdate(
        nationalManager);
    workFlowWrapper.setToDistributorContactModel(nationalManager);

    OperatorModel operatorModel = workFlowWrapper.getOperatorModel();
    operatorModel = this.operatorDAO.saveOrUpdate(operatorModel);
    workFlowWrapper.setOperatorModel(operatorModel);

    return baseWrapper;
  }

  public void setDistributorContactDAO(DistributorContactDAO
                                       distributorContactDAO)
  {
    this.distributorContactDAO = distributorContactDAO;
  }

  public void setTransactionDAO(TransactionDAO transactionDAO)
  {
    this.transactionDAO = transactionDAO;
  }

  public void setOperatorDAO(OperatorDAO operatorDAO)
  {
    this.operatorDAO = operatorDAO;
  }

}
