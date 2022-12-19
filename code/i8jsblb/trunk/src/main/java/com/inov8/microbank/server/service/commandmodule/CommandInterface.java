package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface CommandInterface 
{
	public void doPrepare(BaseWrapper baseWrapper); 
	public void doValidate() throws FrameworkCheckedException;
	public void doExecute() throws FrameworkCheckedException; 
	public String doResponse(); 
}
