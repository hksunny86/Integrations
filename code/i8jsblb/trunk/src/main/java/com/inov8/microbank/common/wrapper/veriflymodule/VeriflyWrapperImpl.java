package com.inov8.microbank.common.wrapper.veriflymodule;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;

public class VeriflyWrapperImpl
    extends BaseWrapperImpl implements VeriflyWrapper
{
  private String newPin;
  private String oldPin;
  private String customerId;
  private String nickname;
  private String userId;
  private String responseCode;

  public VeriflyWrapperImpl()
  {
  }

  public void setNewPin(String newPin)
  {
    this.newPin = newPin;
  }

  public void setOldPin(String oldPin)
  {
    this.oldPin = oldPin;
  }

  public void setCustomerId(String customerId)
  {
    this.customerId = customerId;
  }

  public void setNickname(String nickname)
  {
    this.nickname = nickname;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public void setResponseCode(String responseCode)
  {
    this.responseCode = responseCode;
  }

  public String getNewPin()
  {
    return newPin;
  }

  public String getOldPin()
  {
    return oldPin;
  }

  public String getCustomerId()
  {
    return customerId;
  }

  public String getNickname()
  {
    return nickname;
  }

  public String getUserId()
  {
    return userId;
  }

  public String getResponseCode()
  {
    return responseCode;
  }
}
