package com.inov8.microbank.server.service.financialintegrationmodule.switchmodule;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;


/**
 *
 * @author Jawwad Farooq
 */

public class SwitchFactory implements ApplicationContextAware
{
	// HashMap to cache the Switch class's path
	//private static HashMap<String, String> switchClassPaths;
	private SwitchModuleManager switchManager;
	private ApplicationContext ctx;
	protected final Log logger = LogFactory.getLog(getClass());
	private SwitchFactory()
	{
	}

	/**
	 * Gets the Switch's implementation class
	 * @param switchWrapper SwitchWrapper
	 * @return Switch
	 * @throws Exception
	 */
	public Switch getSwitch(SwitchWrapper switchWrapper) throws WorkFlowException
	{
		String classPath = "";
		Class switchClass = null;

		if (switchWrapper.getBankId() == null || switchWrapper.getPaymentModeId() == null)
			throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);

		try
		{
			switchWrapper = switchManager.getSwitchClassPath(switchWrapper);
		}
		catch (FrameworkCheckedException ex)
		{
			throw new WorkFlowException(ex.getMessage(), ex);
		}

		// Returns the default implementation of Switch
		if (switchWrapper.getSwitchSwitchModel() == null
				|| switchWrapper.getSwitchSwitchModel().getClassName() == null
				|| switchWrapper.getSwitchSwitchModel().getClassName().equals(""))
		{
			logger.error("Throwing Exception..Switch NOT found or Switch Classname is NULL..See below Data Items\n\n");
			throw new WorkFlowException(WorkFlowErrorCodeConstants.GENERAL_ERROR);
		}

		//Check whether the Switch is Active or not
		if (!switchWrapper.getSwitchSwitchModel().getActive())
			throw new WorkFlowException(WorkFlowErrorCodeConstants.SWITCH_INACTIVE);

		classPath = switchWrapper.getSwitchSwitchModel().getClassName();

		try
		{
			switchClass = Class.forName(classPath);
			
			Class[] argsClass = new Class[] {ApplicationContext.class };
			Object[] argsVals = new Object[] {this.ctx};

			Constructor constructor = switchClass.getConstructor( argsClass ) ;			
    	    return  (Switch)constructor.newInstance(argsVals) ;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new WorkFlowException(ex.getMessage(), ex);
		}		
	}

	public void setSwitchManager(SwitchModuleManager switchModuleManager)
	{
		this.switchManager = switchModuleManager;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.ctx = applicationContext;
	}

}
