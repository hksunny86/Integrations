package com.inov8.microbank.server.service.portal.sales;

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
 * @author Asad Hayat
 * @version 1.0
 */
public interface SalesManager


{
   SearchBaseWrapper searchSales(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createOrUpdateTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  SearchBaseWrapper getSupplierProcessingStatus(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException;
  
  BaseWrapper updateTransactionSupStatus(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;

  BaseWrapper loadTransaction(BaseWrapper baseWrapper) throws
        FrameworkCheckedException;


}
