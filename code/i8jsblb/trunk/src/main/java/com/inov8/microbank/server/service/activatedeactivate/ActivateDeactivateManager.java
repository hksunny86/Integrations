/**
 * 
 */
package com.inov8.microbank.server.service.activatedeactivate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			5:51:13 PM
 * Description:				
 */
public interface ActivateDeactivateManager
{
	public static final String KEY_ID = "id";
	public static final String KEY_PROPERTY_NAME = "prop";
	public static final String KEY_MODEL_NAME = "model";
	public static final String KEY_RESPONSE = "label";
	public static final String KEY_MSG = "mesg";
	public static final String KEY_ACTIVE_MSG_ARG = "actMsg";
	public static final String KEY_DEACTIVE_MSG_ARG = "deactMsg";
	public static final String KEY_ACTIVE_LABEL = "actLabel";
	public static final String KEY_DEACTIVE_LABEL = "deactLabel";
	public static final String KEY_ISBUTTON = "isBtn";

	public static final String DEFAULT_ACTIVE_MSG_ARG = "activated";
	public static final String DEFAULT_DEACTIVE_MSG_ARG = "deactivated";

	public static final String DEFAULT_ACTIVE_LABEL = "Activate";
	public static final String DEFAULT_DEACTIVE_LABEL = "Deactivate";
	public static final Boolean DEFAULT_ISBUTTON = Boolean.FALSE;


	public BaseWrapper activateDeactivate(BaseWrapper  baseWrapper) 
		throws FrameworkCheckedException;

}
