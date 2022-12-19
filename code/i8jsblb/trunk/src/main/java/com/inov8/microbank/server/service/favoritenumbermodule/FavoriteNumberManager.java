package com.inov8.microbank.server.service.favoritenumbermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface FavoriteNumberManager 
{
	SearchBaseWrapper searchFavoriteNumbers(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;
}
