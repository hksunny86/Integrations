package com.inov8.microbank.server.service.mnomodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface MnoUserManager
{

  public SearchBaseWrapper loadMnoUser(SearchBaseWrapper   searchBaseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper loadMnoUser(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

public SearchBaseWrapper searchMnoUser(SearchBaseWrapper
    searchBaseWrapper) throws FrameworkCheckedException;

public BaseWrapper createMnoUser(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper updateMnoUser(BaseWrapper  baseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper createAppUserForMno(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
public BaseWrapper activateDeactivateMnoUser(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException;

}
