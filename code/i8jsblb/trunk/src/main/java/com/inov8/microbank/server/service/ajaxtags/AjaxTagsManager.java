/**
 * 
 */
package com.inov8.microbank.server.service.ajaxtags;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;

/**
 * Project Name: 			Microbank	
 * @author Imran Sarwar
 * Creation Date: 			May 3, 2007
 * Creation Time: 			5:51:13 PM
 * Description:				
 */
public interface AjaxTagsManager
{
	public static final String KEY_ID = "id";
	public static final String KEY_PROPERTY_NAME = "prop";
	public static final String KEY_MODEL_NAME = "model";
	public static final String KEY_RESPONSE = "label";
	public static final String KEY_DEL_LABEL = "delLabel";
	public static final String KEY_DELTD_LABEL = "deltdLabel";
	public static final String KEY_HARD_DEL = "hardDel";
	public static final String KEY_MSG = "mesg";
	public static final String KEY_ISBUTTON = "isBtn";
	public static final String KEY_USECASE_ID = "useCaseId";
	
	public static final String DEFAULT_DEL_LABEL = "Delete";
	public static final String DEFAULT_DELTD_LABEL = "Deleted";
	public static final Boolean DEFAULT_ISBUTTON = Boolean.FALSE;
	public static final Boolean DEFAULT_HARD_DEL = Boolean.FALSE;

	public BaseWrapper deleteRecord(BaseWrapper  baseWrapper) 
		throws FrameworkCheckedException;

}
