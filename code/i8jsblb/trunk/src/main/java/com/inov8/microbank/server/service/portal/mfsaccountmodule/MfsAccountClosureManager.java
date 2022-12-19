package com.inov8.microbank.server.service.portal.mfsaccountmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;


public interface MfsAccountClosureManager 
{
	public BaseWrapper makeCustomerAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper makeAgentAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public BaseWrapper makeHandlerAccountClosed(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	public Double checkAgentBalance(AppUserModel appUserModel)throws WorkFlowException, FrameworkCheckedException, Exception;
}
