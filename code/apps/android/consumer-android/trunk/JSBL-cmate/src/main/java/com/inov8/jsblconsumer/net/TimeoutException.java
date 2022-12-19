package com.inov8.jsblconsumer.net;

import java.io.IOException;

/**
 * A class to identify timeout conditions when reading from an OInputStream.
 */
public class TimeoutException extends IOException {
	private static final long serialVersionUID = 1L;

/**
   * Constructor that supports a text message
   *
   * @param msg a <code>String</code> provided to add some clarity to the
   * <code>TimeoutException</code> being created.
   */
  public TimeoutException(String msg) {
    super(msg);
  }
}