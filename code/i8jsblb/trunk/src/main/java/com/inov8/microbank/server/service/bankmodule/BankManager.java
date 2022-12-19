package com.inov8.microbank.server.service.bankmodule;

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

public interface BankManager
{
  SearchBaseWrapper loadBank(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadBank(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchBank(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper updateBank(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createBank(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  public SearchBaseWrapper searchBankByExample(SearchBaseWrapper searchBaseWrapper)throws
  FrameworkCheckedException;
}
