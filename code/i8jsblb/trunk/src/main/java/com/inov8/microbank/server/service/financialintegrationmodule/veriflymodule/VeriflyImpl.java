package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.veriflymodule.VeriflyWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public class VeriflyImpl
    implements Verifly
{
  /**
   * changePin
   *
   * @param transactions VeriflyWrapper
   * @return VeriflyWrapper
   * @throws FrameworkCheckedException
   *
   */
  public VeriflyWrapper changePin(VeriflyWrapper veriflyWrapper) throws
      FrameworkCheckedException
  {
    veriflyWrapper.setNewPin("123456");
    veriflyWrapper.setResponseCode("00");
    return veriflyWrapper;
  }

  /**
   * registerPin
   *
   * @param transactions VeriflyWrapper
   * @return VeriflyWrapper
   * @throws FrameworkCheckedException
   *
   */
  public VeriflyWrapper registerPin(VeriflyWrapper veriflyWrapper) throws
      FrameworkCheckedException
  {
    veriflyWrapper.setNewPin("123456");
    veriflyWrapper.setNickname("nick");
    veriflyWrapper.setResponseCode("00");

    return veriflyWrapper;
  }

  /**
   * resetPin
   *
   * @param transactions VeriflyWrapper
   * @return VeriflyWrapper
   * @throws FrameworkCheckedException
   *
   */
  public VeriflyWrapper resetPin(VeriflyWrapper veriflyWrapper) throws
      FrameworkCheckedException
  {
    veriflyWrapper.setNewPin("000000");
    veriflyWrapper.setOldPin("123456");
    veriflyWrapper.setResponseCode("00");

    return veriflyWrapper;
  }

  /**
   * verifyPin
   *
   * @param accountInfo VeriflyWrapper
   * @return VeriflyWrapper
   * @throws FrameworkCheckedException
   *
   */
  public VeriflyWrapper verifyPin(VeriflyWrapper veriflyWrapper) throws
      FrameworkCheckedException
  {
    veriflyWrapper.setNewPin("123456");
    veriflyWrapper.setNickname("nick");
    veriflyWrapper.setResponseCode("00");

    return veriflyWrapper;
  }
}
