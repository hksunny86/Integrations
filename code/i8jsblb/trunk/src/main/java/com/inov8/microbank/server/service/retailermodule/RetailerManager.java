package com.inov8.microbank.server.service.retailermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.RetailerContactModel;

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

public interface RetailerManager
{
  SearchBaseWrapper loadRetailer(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadRetailer(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchRetailer(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createOrUpdateRetailer(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
}
