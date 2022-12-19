package com.inov8.microbank.server.service.operatormodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface OperatorUserManager {

public SearchBaseWrapper loadOperatorUser(SearchBaseWrapper   searchBaseWrapper) throws
    FrameworkCheckedException;

public SearchBaseWrapper searchOperatorUser(SearchBaseWrapper
    searchBaseWrapper) throws FrameworkCheckedException;

public BaseWrapper createOperatorUser(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper updateOperatorUser(BaseWrapper  baseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper createAppUserForOperator(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

public  Long getAppUserPartnerGroupId(Long appUserId)throws FrameworkCheckedException;
}
