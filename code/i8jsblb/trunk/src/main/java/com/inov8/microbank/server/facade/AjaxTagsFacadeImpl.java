/**
 * 
 */
package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.ajaxtags.AjaxTagsManager;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			7:07:38 PM
 * Description:				
 */
public class AjaxTagsFacadeImpl implements AjaxTagsFacade
{

	private AjaxTagsManager ajaxTagsManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public BaseWrapper deleteRecord(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return ajaxTagsManager.deleteRecord(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	/**
	 * @param frameworkExceptionTranslator the frameworkExceptionTranslator to set
	 */
	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setAjaxTagsManager(AjaxTagsManager ajaxTagsManager) {
		this.ajaxTagsManager = ajaxTagsManager;
	}

}
