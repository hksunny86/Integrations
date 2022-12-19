package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;

/**
 *
 * @author Jawwad Farooq
 *
 */

public interface SwitchModuleManager
{
  public SwitchWrapper getSwitchClassPath(SwitchWrapper accountInfo) throws
      FrameworkCheckedException;
  
	public BaseWrapper loadSwitch(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	public BaseWrapper createOrUpdateSwitch(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	public SearchBaseWrapper searchSwitch(SearchBaseWrapper searchBaseWrapper)throws
	FrameworkCheckedException;


}
