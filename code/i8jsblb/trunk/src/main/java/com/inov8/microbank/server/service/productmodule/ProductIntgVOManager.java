package com.inov8.microbank.server.service.productmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface ProductIntgVOManager
{

  public BaseWrapper loadProductIntgVO(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

}
