package com.inov8.microbank.common.wrapper.transaction;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
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
public class TransactionWrapperImpl
    extends BaseWrapperImpl implements TransactionWrapper
{
  private TransactionModel transactionModel;
  private TransactionDetailModel transactionDetailModel;
  private TransactionCodeModel transactionCodeModel;
  private TransactionTypeModel transactionTypeModel;

  private long transactionCodeID;
  private String transactionCode;

  public TransactionWrapperImpl()
  {
  }

  public void setTransactionModel(TransactionModel transactionModel)
  {
    this.transactionModel = transactionModel;
  }

  public void setTransactionDetailModel(TransactionDetailModel
                                        transactionDetailModel)
  {
    this.transactionDetailModel = transactionDetailModel;
  }

  public TransactionModel getTransactionModel()
  {
    return this.transactionModel;
  }

  public TransactionDetailModel getTransactionDetailModel()
  {
    return this.transactionDetailModel;

  }

  public void setTransactionCodeModel(TransactionCodeModel transactionCodeModel)
  {
    this.transactionCodeModel = transactionCodeModel;
  }

  public TransactionCodeModel getTransactionCodeModel()
  {
    return this.transactionCodeModel;
  }

  public void setTransactionCodeID(long transactionCodeID)
  {
    this.transactionCodeID = transactionCodeID;
  }

  public long getTransactionCodeID()
  {
    return this.transactionCodeID;
  }

  public void setTransactionCode(String transactionCode)
  {
    this.transactionCode = transactionCode;
  }

  public void setTransactionTypeModel(TransactionTypeModel transactionTypeModel)
  {
    this.transactionTypeModel = transactionTypeModel;
  }

  public String getTransactionCode()
  {
    return this.transactionCode;
  }

  public TransactionTypeModel getTransactionTypeModel()
  {
    return transactionTypeModel;
  }

}
