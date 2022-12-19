package com.inov8.microbank.common.wrapper.suppliermodule;

import java.util.HashMap;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.TransactionModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface SupplierWrapper
    extends BaseWrapper
{

  public void setProductTypeProductModel(ProductModel productTypeProductModel);

  public void setTransactionTransactionModel(TransactionModel
                                             transactionTransactionModel);

  public void setReturnedValsFrmIM(HashMap returnedValsFrmIM);

  public void setActionType(String actionType);

  public void setSupplierModel(SupplierModel supplierModel);

  public void setCustomerModel(CustomerModel customerModel);

  public ProductModel getProductTypeProductModel();

  public TransactionModel getTransactionTransactionModel();

  public HashMap getReturnedValsFrmIM();

  public String getActionType();

  public SupplierModel getSupplierModel();

  public CustomerModel getCustomerModel();

  public String getResponseCode();

  public void setResponseCode(String responseCode);

}
