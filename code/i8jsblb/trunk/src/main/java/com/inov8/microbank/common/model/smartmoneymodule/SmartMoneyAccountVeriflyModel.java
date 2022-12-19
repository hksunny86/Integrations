package com.inov8.microbank.common.model.smartmoneymodule;

import java.util.Date;

import com.inov8.microbank.common.model.SmartMoneyAccountModel;

/**
 @author Asad Hayat Inov8 Limited
 */
public class SmartMoneyAccountVeriflyModel extends SmartMoneyAccountModel
{


 /**
	 * 
	 */
	private static final long serialVersionUID = 1223606351792666175L;

private Long cardTypeId;

 private Long accountInfoId;
 private String accountNick;
 private String cardNo;
 private Date cardExpiryDate;
 private String pin;
 private String firstName;
 private String lastName;
 private String customerMobileNo;
 private String comments;
 private com.inov8.verifly.common.model.VfPaymentModeModel paymentModeModelIdPaymentModeModel;


  public SmartMoneyAccountVeriflyModel()
  {
  }

  @javax.persistence.Transient
  public Long getAccountInfoId()
  {
    return accountInfoId;
  }

  @javax.persistence.Transient
  public String getAccountNick()
  {
    return accountNick;
  }

  @javax.persistence.Transient
  public Date getCardExpiryDate()
  {
    return cardExpiryDate;
  }

  @javax.persistence.Transient
  public String getCardNo()
  {
    return cardNo;
  }



  @javax.persistence.Transient
  public String getCustomerMobileNo()
  {
    return customerMobileNo;
  }

  @javax.persistence.Transient
  public String getFirstName()
  {
    return firstName;
  }

  @javax.persistence.Transient
  public String getLastName()
  {
    return lastName;
  }

  @javax.persistence.Transient
  public String getPin()
  {
    return pin;
  }


  @javax.persistence.Transient
  public com.inov8.verifly.common.model.VfPaymentModeModel
      getPaymentModeModelIdPaymentModeModel()
  {
    return paymentModeModelIdPaymentModeModel;
  }

  @javax.persistence.Transient
  public String getComments()
  {
    return comments;
  }

  @javax.persistence.Transient
  public Long getCardTypeId()
  {
    return cardTypeId;
  }
  @javax.persistence.Transient

  public void setAccountInfoId(Long accountInfoId)
  {
    this.accountInfoId = accountInfoId;
  }

  @javax.persistence.Transient
  public void setAccountNick(String accountNick)
  {
    this.accountNick = accountNick;
  }


//  @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//        @JoinColumn(name = "CARDTYPE_ID")
//        public CardTypeModel getRelationCardTypeIdCardTypeModel()
//        {
//                return cardTypeIdCardTypeModel;
//        }
//
//
//        @javax.persistence.Transient
//         public void setRelationCardTypeIdCardTypeModel(CardTypeModel cardTypeModel)
//         {
//                 this.cardTypeIdCardTypeModel = cardTypeModel;
//         }


  @javax.persistence.Transient
  public void setCardExpiryDate(Date cardExpiryDate)
  {
    this.cardExpiryDate = cardExpiryDate;
  }



  @javax.persistence.Transient
  public void setCardNo(String cardNo)
  {
    this.cardNo = cardNo;
  }

  @javax.persistence.Transient
  public void setCustomerMobileNo(String customerMobileNo)
  {
    this.customerMobileNo = customerMobileNo;
  }

  @javax.persistence.Transient
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  @javax.persistence.Transient
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  @javax.persistence.Transient
  public void setPin(String pin)
  {
    this.pin = pin;
  }


  @javax.persistence.Transient
  public void setPaymentModeModelIdPaymentModeModel(com.inov8.verifly.common.
      model.VfPaymentModeModel paymentModeModelIdPaymentModeModel)
  {
    this.paymentModeModelIdPaymentModeModel =
        paymentModeModelIdPaymentModeModel;
  }

  @javax.persistence.Transient
  public void setComments(String comments)
  {
    this.comments = comments;
  }

  @javax.persistence.Transient
  public void setCardTypeId(Long cardTypeId)
  {
    this.cardTypeId = cardTypeId;
  }

}
