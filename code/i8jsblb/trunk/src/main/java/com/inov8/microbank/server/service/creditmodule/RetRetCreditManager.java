package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface RetRetCreditManager
{
  public BaseWrapper loadRetailerContactForRetailers(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createRetRetTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper updateRetailerContactBalanceforRetailers(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException;

  public WorkFlowWrapper updateRetailerContactBalanceforRetailersSecond(
      WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException;
}
