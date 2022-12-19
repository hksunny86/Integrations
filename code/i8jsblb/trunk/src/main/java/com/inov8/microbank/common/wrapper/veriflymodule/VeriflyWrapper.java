package com.inov8.microbank.common.wrapper.veriflymodule;

import com.inov8.framework.common.wrapper.BaseWrapper;

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
public interface VeriflyWrapper
    extends BaseWrapper
{

  public void setNewPin(String newPin);

  public void setOldPin(String oldPin);

  public void setCustomerId(String customerId);

  public void setNickname(String nickname);

  public void setUserId(String userId);

  public String getNewPin();

  public String getOldPin();

  public String getCustomerId();

  public String getNickname();

  public String getUserId();

  public void setResponseCode(String responseCode);

  public String getResponseCode();

}
