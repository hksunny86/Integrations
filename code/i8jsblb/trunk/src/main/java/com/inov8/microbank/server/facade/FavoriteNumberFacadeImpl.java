package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.favoritenumbermodule.FavoriteNumberManager;


public class FavoriteNumberFacadeImpl implements FavoriteNumberFacade{

	private FavoriteNumberManager favoriteNumberManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator;


	public SearchBaseWrapper searchFavoriteNumbers(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.favoriteNumberManager.searchFavoriteNumbers(searchBaseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public void setFavoriteNumberManager(FavoriteNumberManager favoriteNumberManager) 
	{
		this.favoriteNumberManager = favoriteNumberManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator) 
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
}
