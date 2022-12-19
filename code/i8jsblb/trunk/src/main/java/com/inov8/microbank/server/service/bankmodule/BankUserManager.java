package com.inov8.microbank.server.service.bankmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface BankUserManager
{


  public SearchBaseWrapper loadBankUser(SearchBaseWrapper   searchBaseWrapper) throws
     FrameworkCheckedException;

 public BaseWrapper loadBankUser(BaseWrapper baseWrapper) throws
     FrameworkCheckedException;

 public SearchBaseWrapper searchBankUser(SearchBaseWrapper
     searchBaseWrapper) throws FrameworkCheckedException;

 public BaseWrapper createBankUser(BaseWrapper baseWrapper) throws
     FrameworkCheckedException;

 public BaseWrapper updateBankUser(BaseWrapper  baseWrapper) throws
     FrameworkCheckedException;

 public BaseWrapper createAppUserForBank(BaseWrapper baseWrapper) throws
     FrameworkCheckedException;
 
 public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException;



}
