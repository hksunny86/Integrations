package com.inov8.microbank.server.service.creditmodule;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;

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
public class DistDistCreditManagerImpl
    implements DistDistCreditManager
{
  private DistributorContactDAO distributorContactDAO;

  /**
   * updateInov8Balance
   *
   * @param baseWrapper BaseWrapper
   * @return BaseWrapper
   * @throws FrameworkCheckedException
   */
  public BaseWrapper updateDistToDistContactBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    WorkFlowWrapper workFlowWrapper = (WorkFlowWrapper) baseWrapper;

    //Updates the 'To Distributor'
    DistributorContactModel distributorContactModel = workFlowWrapper.getToDistributorContactModel();
    distributorContactModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
    distributorContactModel.setUpdatedOn(new Date());

    distributorContactModel = this.distributorContactDAO.saveOrUpdate(
        distributorContactModel);
    workFlowWrapper.getTransactionModel().setToDistContactIdDistributorContactModel(distributorContactModel);
    //Updates the 'From Distributor'
    DistributorContactModel fromDistributorContactModel = workFlowWrapper.getFromDistributorContactModel();
    fromDistributorContactModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
    fromDistributorContactModel.setUpdatedOn(new Date());
         //getFromDistributorContactModel() ;
    fromDistributorContactModel = this.distributorContactDAO.saveOrUpdate(
        fromDistributorContactModel);
    workFlowWrapper.getTransactionModel().
        setRelationFromDistContactIdDistributorContactModel(
            fromDistributorContactModel);

    return workFlowWrapper;
  }

  public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    DistributorContactModel distributorContactModel = this.
        distributorContactDAO.findByPrimaryKey(
            baseWrapper.getBasePersistableModel().
            getPrimaryKey());
    baseWrapper.setBasePersistableModel(distributorContactModel);
    return baseWrapper;
  }

  public void setDistributorContactDAO(DistributorContactDAO
                                       distributorContactDAO)
  {
    this.distributorContactDAO = distributorContactDAO;
  }

}
