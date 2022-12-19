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


public interface CommissionReasonManager
{

  SearchBaseWrapper loadCommissionReason(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchCommissionReason(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper updateCommissionReason(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createCommissionReason(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

}
