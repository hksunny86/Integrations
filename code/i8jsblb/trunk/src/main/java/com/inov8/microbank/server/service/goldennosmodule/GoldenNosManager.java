package com.inov8.microbank.server.service.goldennosmodule;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface GoldenNosManager 
{
	
	public BaseWrapper createOrUpdateGoldenNos(BaseWrapper baseWrapper) throws
    	FrameworkCheckedException;
	
	public SearchBaseWrapper searchGoldenNos(SearchBaseWrapper searchBaseWrapper)throws
	FrameworkCheckedException;


}
