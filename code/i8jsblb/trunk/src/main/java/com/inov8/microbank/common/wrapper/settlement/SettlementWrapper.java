package com.inov8.microbank.common.wrapper.settlement;

import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;

public interface SettlementWrapper
{
  public CustomerModel getCustomerModel();

  public ProductModel getProductModel();

//  public SmartMoneyAccountModel getSmartMoneyAccountModel();
  public PaymentModeModel getPaymentModeModel();

  public BankModel getBankModel();

  public String getPin();

  public String getAccountNick();

  public void setCustomerModel(CustomerModel customerModel);

  public void setProductModel(ProductModel productModel);

//  public void setSmartMoneyAccountModel(SmartMoneyAccountModel
//                                        smartMoneyAccountModel);
  public void setPaymentModeModel(PaymentModeModel paymentModeModel);

  public void setBankModel(BankModel bankModel);

  public void setPin(String pin);

  public void setAccountNick(String accountNick);

}
