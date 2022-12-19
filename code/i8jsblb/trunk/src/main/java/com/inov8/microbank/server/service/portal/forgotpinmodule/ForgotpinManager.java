package com.inov8.microbank.server.service.portal.forgotpinmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;


public interface ForgotpinManager
{

	public SearchBaseWrapper searchForgotPinUser(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;
	
	public BaseWrapper updateForgotPin(BaseWrapper  baseWrapper) 
	throws FrameworkCheckedException;
	public BaseWrapper updateForgotPassword(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	
	

}
