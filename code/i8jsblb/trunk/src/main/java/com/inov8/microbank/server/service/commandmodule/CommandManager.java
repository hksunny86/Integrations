package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;

public interface CommandManager
{
	String executeCommand(BaseWrapper baseWrapper) throws
	CommandException;
	
	String executeCommand(BaseWrapper baseWrapper,String action) throws
	CommandException;

	public CommonCommandManager getCommonCommandManager() throws CommandException;
}
