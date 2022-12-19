package com.inov8.microbank.server.service.suppliermodule;

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

public interface SupplierUserManager
{
  public SearchBaseWrapper loadSupplierUser(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper loadSupplierUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public SearchBaseWrapper searchSupplierUser(SearchBaseWrapper
                                              searchBaseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createSupplierUser(BaseWrapper
                                        baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper updateSupplierUser(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  public BaseWrapper createAppUserForSupplier(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException;

}
