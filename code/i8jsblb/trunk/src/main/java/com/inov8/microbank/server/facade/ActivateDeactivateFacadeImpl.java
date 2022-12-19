/**
 * 
 */
package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.activatedeactivate.ActivateDeactivateManager;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			7:07:38 PM
 * Description:				
 */
public class ActivateDeactivateFacadeImpl implements ActivateDeactivateFacade
{

	private ActivateDeactivateManager activateDeactivateManager;
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public BaseWrapper activateDeactivate(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
		try
		{
			return activateDeactivateManager.activateDeactivate(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	/**
	 * @param activateDeactivateManager the activateDeactivateManager to set
	 */
	public void setActivateDeactivateManager(ActivateDeactivateManager activateDeactivateManager)
	{
		this.activateDeactivateManager = activateDeactivateManager;
	}

	/**
	 * @param frameworkExceptionTranslator the frameworkExceptionTranslator to set
	 */
	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator)
	{
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
