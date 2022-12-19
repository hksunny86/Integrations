package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.goldennosmodule.GoldenNosManager;

public class GoldenNosFacadeImpl 
	implements GoldenNosFacade	
{
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private GoldenNosManager goldenNosManager;
	
	
	public BaseWrapper createOrUpdateGoldenNos(BaseWrapper baseWrapper) throws FrameworkCheckedException 
	{
		try
		  {
		    this.goldenNosManager.createOrUpdateGoldenNos(baseWrapper);
		  }
		  catch (Exception ex)
		  {
		    throw this.frameworkExceptionTranslator.translate(ex,
		        this.frameworkExceptionTranslator.
		        FIND_BY_PRIMARY_KEY_ACTION);
		  }
		  return baseWrapper;
	}
	
	public SearchBaseWrapper searchGoldenNos(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{
		try
		  {
		    this.goldenNosManager.searchGoldenNos(searchBaseWrapper);
		  }
		  catch (Exception ex)
		  {
		    throw this.frameworkExceptionTranslator.translate(ex,
		        this.frameworkExceptionTranslator.
		        FIND_BY_PRIMARY_KEY_ACTION);
		  }
		  return searchBaseWrapper;
	}


	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setGoldenNosManager(GoldenNosManager goldenNosManager) {
		this.goldenNosManager = goldenNosManager;
	}




}
