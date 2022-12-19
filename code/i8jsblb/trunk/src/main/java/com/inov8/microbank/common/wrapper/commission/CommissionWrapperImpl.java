package com.inov8.microbank.common.wrapper.commission;

import java.util.HashMap;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.CommissionTransactionModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Syed Ahmad Bilal
 * @version 1.0
 */
public class CommissionWrapperImpl extends BaseWrapperImpl implements CommissionWrapper 
{
  private CommissionTransactionModel commissionTransactionModel;
  private TransactionModel transactionModel;
  private PaymentModeModel paymentModeModel;
  private ProductModel productModel;
  private TransactionTypeModel transactionTypeModel;  

  private HashMap commissionWrapperHashMap;

  public CommissionWrapperImpl()
  {
  }

  public CommissionTransactionModel getCommissionTransactionModel()
  {
    return commissionTransactionModel;
  }

  public PaymentModeModel getPaymentModeModel()
  {
    return paymentModeModel;
  }

  public ProductModel getProductModel()
  {
    return productModel;
  }

  public TransactionModel getTransactionModel()
  {
    return transactionModel;
  }

  public HashMap getCommissionWrapperHashMap()
  {
    return commissionWrapperHashMap;
  }

  public TransactionTypeModel getTransactionTypeModel()
  {
    return transactionTypeModel;
  }

  public void setCommissionTransactionModel(CommissionTransactionModel
                                            commissionTransactionModel)
  {
    this.commissionTransactionModel = commissionTransactionModel;
  }

  public void setPaymentModeModel(PaymentModeModel paymentModeModel)
  {
    this.paymentModeModel = paymentModeModel;
  }

  public void setProductModel(ProductModel productModel)
  {
    this.productModel = productModel;
  }

  public void setTransactionModel(TransactionModel transactionModel)
  {
    this.transactionModel = transactionModel;
  }

  public void setCommissionWrapperHashMap(HashMap commissionWrapperHashMap)
  {
    this.commissionWrapperHashMap = commissionWrapperHashMap;
  }

  public void setTransactionTypeModel(TransactionTypeModel transactionTypeModel)
  {
    this.transactionTypeModel = transactionTypeModel;
  }
}
