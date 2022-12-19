package com.inov8.microbank.common.wrapper.transaction;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionDetailModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface TransactionWrapper
    extends BaseWrapper
{
  public void setTransactionModel(TransactionModel transactionModel);

  public void setTransactionDetailModel(TransactionDetailModel
                                        transactionDetailModel);

  public void setTransactionCodeModel(TransactionCodeModel transactionCodeModel);

  public void setTransactionTypeModel(TransactionTypeModel transactionTypeModel);

  public TransactionModel getTransactionModel();

  public TransactionDetailModel getTransactionDetailModel();

  public TransactionCodeModel getTransactionCodeModel();

  public TransactionTypeModel getTransactionTypeModel();

}
