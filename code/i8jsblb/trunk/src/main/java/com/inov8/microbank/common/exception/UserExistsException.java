package com.inov8.microbank.common.exception;

public class UserExistsException
    extends Exception
{
  private static final long serialVersionUID = 4050482305178810162L;

  /**
   * Constructor for UserExistsException.
   *
   * @param message
   */
  public UserExistsException(String message)
  {
    super(message);
  }
}
