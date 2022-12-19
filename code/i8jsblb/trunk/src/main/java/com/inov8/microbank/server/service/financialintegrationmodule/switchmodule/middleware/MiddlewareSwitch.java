package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.middleware;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;

public interface MiddlewareSwitch {

	public abstract SwitchWrapper billPayment(SwitchWrapper switchWrapper)
			throws WorkFlowException, FrameworkCheckedException;

	public abstract SwitchWrapper billInquiry(SwitchWrapper switchWrapper)
			throws WorkFlowException, FrameworkCheckedException;
/*
	public abstract SwitchWrapper checkBalance(SwitchWrapper switchWrapper)
			throws WorkFlowException, FrameworkCheckedException;
*/
}