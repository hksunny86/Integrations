package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.TransactionModel;
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

public class RetHeadRetCreditManagerImpl
    implements RetHeadRetCreditManager
{
  private RetailerContactDAO retailerContactDAO;
  private TransactionDAO transactionDAO;

  public BaseWrapper loadRetailerContactForRetailer(BaseWrapper baseWrapper)
  {
    RetailerContactModel retailerModel = this.retailerContactDAO.
        findByPrimaryKey(
            baseWrapper.getBasePersistableModel().
            getPrimaryKey());
    baseWrapper.setBasePersistableModel(retailerModel);
    return baseWrapper;
  }

  public BaseWrapper createRetHeadRetTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {

    TransactionModel transactionModel = (TransactionModel)
        baseWrapper.getBasePersistableModel();

    transactionModel = this.transactionDAO.saveOrUpdate( (
        TransactionModel) baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(transactionModel);
    return baseWrapper;
  }

  public BaseWrapper updateRetailerContactBalanceForRetailer(BaseWrapper
      baseWrapper)
  {
    RetailerContactModel retailerContactModel = (RetailerContactModel)
        baseWrapper.getBasePersistableModel();

    retailerContactModel = this.retailerContactDAO.saveOrUpdate(
        (RetailerContactModel) baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(retailerContactModel);
    return baseWrapper;

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
