package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface CreditInfoManager
{
	
	public SearchBaseWrapper searchDistributorOrRetailer(SearchBaseWrapper searchBaseWrapper)throws
    FrameworkCheckedException;;

}
