package com.inov8.microbank.server.service.creditmodule;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class RetRetCreditManagerImpl
    implements RetRetCreditManager
{
  private RetailerContactDAO retailerContactDAO;
  private TransactionDAO transactionDAO;

  public BaseWrapper loadRetailerContactForRetailers(BaseWrapper baseWrapper)
  {
    RetailerContactModel retailerModel = this.retailerContactDAO.
        findByPrimaryKey(
            baseWrapper.getBasePersistableModel().
            getPrimaryKey());
    baseWrapper.setBasePersistableModel(retailerModel);
    return baseWrapper;
  }

  public BaseWrapper createRetRetTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {

    TransactionModel transactionModel = (TransactionModel)
        baseWrapper.getBasePersistableModel();

    transactionModel = this.transactionDAO.saveOrUpdate( (
        TransactionModel) baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(transactionModel);
    return baseWrapper;
  }

  public BaseWrapper updateRetailerContactBalanceforRetailers(BaseWrapper
      baseWrapper)
  {
    RetailerContactModel retailerContactModel = (RetailerContactModel)
        baseWrapper.getBasePersistableModel();
    retailerContactModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
    retailerContactModel.setUpdatedOn(new Date());

    retailerContactModel = this.retailerContactDAO.saveOrUpdate(
        (RetailerContactModel) baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(retailerContactModel);
    return baseWrapper;
  }

  public WorkFlowWrapper updateRetailerContactBalanceforRetailersSecond(
      WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException
  {
    //Updates the 'To Retailer'
    RetailerContactModel toRetailerContactModel = workFlowWrapper.getToRetailerContactModel();
    toRetailerContactModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
    toRetailerContactModel.setUpdatedOn(new Date());
    toRetailerContactModel = this.retailerContactDAO.saveOrUpdate(
        toRetailerContactModel);
    workFlowWrapper.getTransactionModel().setToRetContactIdRetailerContactModel(
        toRetailerContactModel);

    //Updates the 'From Retailer'
    RetailerContactModel fromRetailerContactModel = workFlowWrapper.
        getFromRetailerContactModel();
    fromRetailerContactModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
    fromRetailerContactModel.setUpdatedOn(new Date());
    fromRetailerContactModel = this.retailerContactDAO.saveOrUpdate(
        fromRetailerContactModel);
    workFlowWrapper.getTransactionModel().
        setFromRetContactIdRetailerContactModel(fromRetailerContactModel);
    return workFlowWrapper;
  }

  public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO)
  {
    this.retailerContactDAO = retailerContactDAO;
  }

  public void setTransactionDAO(TransactionDAO transactionDAO)
  {
    this.transactionDAO = transactionDAO;
  }

}
