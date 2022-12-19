package com.inov8.microbank.server.service.commissionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public interface CommissionRateManager
{

  SearchBaseWrapper loadCommissionRate(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadCommissionRate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchCommissionRate(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper updateCommissionRate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createCommissionRate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

}
