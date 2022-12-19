package com.inov8.microbank.server.service.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

/**
 * <p>Company: Inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface ProductIntgModuleInfoManager
{

  public SearchBaseWrapper loadProductIntgModuleInfo(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadProductIntgModuleInfo(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;


}
