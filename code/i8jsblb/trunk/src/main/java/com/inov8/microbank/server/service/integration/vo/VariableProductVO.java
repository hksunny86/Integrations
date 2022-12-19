package com.inov8.microbank.server.service.integration.vo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;


/**
 * <p>Title: </p>
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class VariableProductVO
    implements ProductVO
{
  String responseCode ;
  private String pin;

  public void setResponseCode(String responseCode)
  {
    this.responseCode = responseCode;
  }

  public void setPin(String pin)
  {
    this.pin = pin;
  }

  public String getResponseCode()
  {
    return responseCode;
  }

  public String getPin()
  {
    return pin;
  }

  public ProductVO populateVO( ProductVO billPaymentVO, BaseWrapper baseWrapper )
  {
    return (ProductVO)billPaymentVO;
  }

  public void validateVO( ProductVO billPaymentVO )throws FrameworkCheckedException
  {
  }



}
