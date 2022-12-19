package com.inov8.microbank.server.service.devicemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface DeviceTypeCommandManager {
	
	public CustomList loadDeviceTypeCommand(BaseWrapper baseWrapper)throws
    FrameworkCheckedException;

}
