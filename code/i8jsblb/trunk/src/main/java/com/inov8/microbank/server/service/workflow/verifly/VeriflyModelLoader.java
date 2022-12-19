package com.inov8.microbank.server.service.workflow.verifly;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface VeriflyModelLoader
{
  public BaseWrapper loadVerifly( BaseWrapper baseWrapper ) throws FrameworkCheckedException ;


}
