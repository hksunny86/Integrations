package com.inov8.verifly.common.util;
import java.io.Serializable;

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
public class Error implements Serializable
{
  private String errorMessage;
  private long errorCode;
  public Error()
  {
  }

  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  public void setErrorCode(long errorCode)
  {
    this.errorCode = errorCode;
  }

  public String getErrorMessage()
  {
    return errorMessage;
  }

  public long getErrorCode()
  {
    return errorCode;
  }
}
