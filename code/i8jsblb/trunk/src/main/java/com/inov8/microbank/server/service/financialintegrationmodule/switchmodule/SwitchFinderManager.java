package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;


/**
 *
 * @author Jawwad Farooq
 *
 */

public interface SwitchFinderManager
{
	public BaseWrapper loadSwitchFinder(BaseWrapper searchBaseWrapper) throws
	FrameworkCheckedException;

	public BaseWrapper createOrUpdateSwitchFinder(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	public SearchBaseWrapper searchSwitchFinder(SearchBaseWrapper searchBaseWrapper)throws
	FrameworkCheckedException;


}
