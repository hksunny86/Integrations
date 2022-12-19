package com.inov8.microbank.server.service.financialintegrationmodule.veriflymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;


/**
 *
 * @author Jawwad Farooq
 *
 */

public interface VeriflyModuleManager
{
	public BaseWrapper loadVerifly(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	public BaseWrapper createOrUpdateVerifly(BaseWrapper baseWrapper) throws
	FrameworkCheckedException;

	public SearchBaseWrapper searchVerifly(SearchBaseWrapper searchBaseWrapper)throws
	FrameworkCheckedException;


}
