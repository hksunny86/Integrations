package com.inov8.microbank.common.wrapper.commission;

import java.util.HashMap;

import com.inov8.framework.common.wrapper.BaseWrapper;
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
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface CommissionWrapper
    extends BaseWrapper
{
  public void setCommissionTransactionModel(CommissionTransactionModel
                                            commissionTransactionModel);

  public CommissionTransactionModel getCommissionTransactionModel();

  public void setTransactionModel(TransactionModel
                                  transactionModel);

  public TransactionModel getTransactionModel();

  public void setPaymentModeModel(PaymentModeModel
                                  paymentModeModel);

  public PaymentModeModel getPaymentModeModel();

  public void setProductModel(ProductModel
                              productModel);

  public ProductModel getProductModel();

  public void setTransactionTypeModel(TransactionTypeModel
                                      transactionTypeModel);

  public TransactionTypeModel getTransactionTypeModel();

  public HashMap getCommissionWrapperHashMap();

  public void setCommissionWrapperHashMap(HashMap commissionWrapperHashMap);

}
