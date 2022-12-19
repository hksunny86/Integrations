package com.inov8.microbank.server.service.ussdmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface UssdMenuMappingManager {
	public BaseWrapper findMenuMapping(BaseWrapper param) throws FrameworkCheckedException;

}
