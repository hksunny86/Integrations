package com.inov8.microbank.server.service.common;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;




/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface AreaManager
{
  SearchBaseWrapper loadArea(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadArea(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchArea(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createOrUpdateArea(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  boolean findDistributorContactByAreaId(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
}
