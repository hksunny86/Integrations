package com.inov8.microbank.common.wrapper.suppliermodule;

import java.util.HashMap;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.TransactionModel;

public class SupplierWrapperImpl
    extends BaseWrapperImpl implements SupplierWrapper
{
  private ProductModel productTypeProductModel;
  private TransactionModel transactionTransactionModel;
  private HashMap returnedValsFrmIM;
  private String actionType;
  private SupplierModel supplierModel;
  private CustomerModel customerModel;
  private String responseCode;

  public SupplierWrapperImpl()
  {
  }

  public void setProductTypeProductModel(ProductModel productTypeProductModel)
  {
    this.productTypeProductModel = productTypeProductModel;
  }

  public void setTransactionTransactionModel(TransactionModel
                                             transactionTransactionModel)
  {
    this.transactionTransactionModel = transactionTransactionModel;
  }

  public void setReturnedValsFrmIM(HashMap returnedValsFrmIM)
  {
    this.returnedValsFrmIM = returnedValsFrmIM;
  }

  public void setActionType(String actionType)
  {
    this.actionType = actionType;
  }

  public void setSupplierModel(SupplierModel supplierModel)
  {
    this.supplierModel = supplierModel;
  }

  public void setCustomerModel(CustomerModel customerModel)
  {
    this.customerModel = customerModel;
  }

  public void setResponseCode(String responseCode)
  {
    this.responseCode = responseCode;
  }

  public ProductModel getProductTypeProductModel()
  {
    return productTypeProductModel;
  }

  public TransactionModel getTransactionTransactionModel()
  {
    return transactionTransactionModel;
  }

  public HashMap getReturnedValsFrmIM()
  {
    return returnedValsFrmIM;
  }

  public String getActionType()
  {
    return actionType;
  }

  public SupplierModel getSupplierModel()
  {
    return supplierModel;
  }

  public CustomerModel getCustomerModel()
  {
    return customerModel;
  }

  public String getResponseCode()
  {
    return responseCode;
  }

}
