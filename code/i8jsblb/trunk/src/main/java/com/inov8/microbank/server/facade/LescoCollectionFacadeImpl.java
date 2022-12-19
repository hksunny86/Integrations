package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.lescomodule.LescoCollectionManager;

public class LescoCollectionFacadeImpl implements LescoCollectionFacade
{
	
	private LescoCollectionManager lescoCollectionManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	

	public void setLescoCollectionManager(LescoCollectionManager lescoCollectionManager)
	{
		this.lescoCollectionManager = lescoCollectionManager;
	}

	public BaseWrapper createLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		// TODO Auto-generated method stub
		try
		{
			this.lescoCollectionManager.createLescoCollection(baseWrapper);
		}
		catch(FrameworkCheckedException fe)
		{
			throw this.frameworkExceptionTranslator.translate(fe,
			          this.frameworkExceptionTranslator.
			          INSERT_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper searchLescoCollection(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.lescoCollectionManager.searchLescoCollection(searchBaseWrapper);
		}
		catch(FrameworkCheckedException fe)
		{
			throw this.frameworkExceptionTranslator.translate(fe,
			          this.frameworkExceptionTranslator.
			          FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public BaseWrapper saveOrUpdateLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SearchBaseWrapper searchLescoLog(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public BaseWrapper loadLescoCollection(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			this.lescoCollectionManager.loadLescoCollection(baseWrapper);
		}
		catch(FrameworkCheckedException fe)
		{
			throw this.frameworkExceptionTranslator.translate(fe,
			          this.frameworkExceptionTranslator.
			          FIND_ACTION);
		}
		return baseWrapper;
	}

}
