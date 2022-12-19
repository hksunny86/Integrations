package com.inov8.verifly.common.encoder;

import java.security.MessageDigest;

/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  05-Sep-2006
 *
 */
public class SHAEncoder
    implements Encoder
{
  public SHAEncoder()
  {
  }

  /**
   * doHash
   *
   * @param text String
   * @return String
   * @todo Implement this com.inov8.microbank.integration.verifly.SHAUtils
   *   method
   */
  public String doHash(String text)
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("SHA");
      byte[] bytes = md.digest(text.getBytes());
      return getString(bytes);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   *
   * @param bytes byte[]
   * @return String
   */
  private String getString(byte[] bytes)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bytes.length; i++)
    {
      byte b = bytes[i];
      sb.append( (int) (0x00FF & b));
      if (i + 1 < bytes.length)
      {
        sb.append("-");
      }
    }
    return sb.toString();
  }
}
