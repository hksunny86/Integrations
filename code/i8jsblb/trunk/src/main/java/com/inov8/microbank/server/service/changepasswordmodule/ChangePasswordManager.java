package com.inov8.microbank.server.service.changepasswordmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface ChangePasswordManager {

	
	public BaseWrapper loadAppUser(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;


	public BaseWrapper savePassword(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

	public BaseWrapper validatePassword(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

}
