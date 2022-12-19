package com.inov8.microbank.server.service.switchutilitymappingmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface SwitchUtilityMappingManager
{
	public SearchBaseWrapper findUtilityCompanyCodeByExample(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	public BaseWrapper findUtilityCompanyCodeByExample(BaseWrapper baseWrapper) throws FrameworkCheckedException;
}




