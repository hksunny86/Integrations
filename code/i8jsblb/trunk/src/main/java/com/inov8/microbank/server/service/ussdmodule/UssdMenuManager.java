package com.inov8.microbank.server.service.ussdmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

public interface UssdMenuManager {
	public BaseWrapper findNextMenu(BaseWrapper param)throws FrameworkCheckedException;
	public BaseWrapper findMenu(BaseWrapper param)throws FrameworkCheckedException;
	public BaseWrapper findPreviousMenu(Long screenOutputCode,Long appUserTypeID)throws FrameworkCheckedException;
	public int findPreviousMenuCount(Long screenOutputCode,Long appUserTypeID)throws FrameworkCheckedException;
	

}
