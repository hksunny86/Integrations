package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.veriflymodule.VeriflyWrapper;
import com.inov8.microbank.server.service.statuscheckmodule.IntegrationModuleStatus;

public interface Verifly
    extends IntegrationModuleStatus
{
  public VeriflyWrapper verifyPin(VeriflyWrapper accountInfo) throws
      FrameworkCheckedException;

  public VeriflyWrapper resetPin(VeriflyWrapper transactions) throws
      FrameworkCheckedException;

  public VeriflyWrapper registerPin(VeriflyWrapper transactions) throws
      FrameworkCheckedException;

  public VeriflyWrapper changePin(VeriflyWrapper transactions) throws
      FrameworkCheckedException;

}
