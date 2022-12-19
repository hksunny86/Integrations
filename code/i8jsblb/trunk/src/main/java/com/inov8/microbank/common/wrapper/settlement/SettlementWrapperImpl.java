package com.inov8.microbank.common.wrapper.settlement;

import com.inov8.microbank.common.model.BankModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;

public class SettlementWrapperImpl
    implements SettlementWrapper
{
  private PaymentModeModel paymentModeModel;
  private ProductModel productModel;
  private CustomerModel customerModel;
  private BankModel bankModel;
  private String pin;
  private String accountNick;

  public PaymentModeModel getPaymentModeModel()
  {
    return paymentModeModel;
  }

  public CustomerModel getCustomerModel()
  {
    return customerModel;
  }

  public ProductModel getProductModel()
  {
    return productModel;
  }

  public BankModel getBankModel()
  {
    return bankModel;
  }

  public String getAccountNick()
  {
    return accountNick;
  }

  public String getPin()
  {
    return pin;
  }

  public void setCustomerModel(CustomerModel customerModel)
  {
    this.customerModel = customerModel;
  }

  public void setProductModel(ProductModel productModel)
  {
    this.productModel = productModel;
  }

  public void setPaymentModeModel(PaymentModeModel paymentModeModel)
  {
    this.paymentModeModel = paymentModeModel;
  }

  public void setBankModel(BankModel bankModel)
  {
    this.bankModel = bankModel;
  }

  public void setAccountNick(String accountNick)
  {
    this.accountNick = accountNick;
  }

  public void setPin(String pin)
  {
    this.pin = pin;
  }

}
