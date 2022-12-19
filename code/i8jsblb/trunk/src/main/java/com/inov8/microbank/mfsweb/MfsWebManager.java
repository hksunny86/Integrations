package com.inov8.microbank.mfsweb;

import javax.servlet.http.HttpServletRequest;

public interface MfsWebManager
{

	public abstract String handleRequest(HttpServletRequest request, String commandId) throws Exception;
	public abstract String handleRequest(HttpServletRequest request, String commandId, boolean removeFromThreadLocal) throws Exception;
	public void logoutUser( HttpServletRequest request );

}
