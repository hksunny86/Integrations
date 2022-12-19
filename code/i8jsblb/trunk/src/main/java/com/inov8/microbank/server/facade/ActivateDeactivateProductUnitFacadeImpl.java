package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.server.service.activatedeactivate.ActivateDeactivateManager;

public class ActivateDeactivateProductUnitFacadeImpl implements ActivateDeactivateFacade{

	private ActivateDeactivateManager activateDeactivateProductUnitManager;
	
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	public BaseWrapper activateDeactivate(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
		{
			return activateDeactivateProductUnitManager.activateDeactivate(baseWrapper);
		}
		catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex, FrameworkExceptionTranslator.UPDATE_ACTION);
		}
		
	}


	public void setActivateDeactivateProductUnitManager(
			ActivateDeactivateManager activateDeactivateProductUnitManager) {
		this.activateDeactivateProductUnitManager = activateDeactivateProductUnitManager;
	}


	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
}
