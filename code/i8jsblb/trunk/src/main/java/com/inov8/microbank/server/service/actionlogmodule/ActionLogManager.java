package com.inov8.microbank.server.service.actionlogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.sun.xml.wss.saml.Action;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */


public interface ActionLogManager
{

	public static final String	KEY_IS_RELINK			= "isRelink";
	public static final String	KEY_APP_USER_ID			= "appUserId";
	public static final String	KEY_SMART_MONEY_ACC_ID	= "smartMoneyAccountId";	
	
	public SearchBaseWrapper searchActionLog(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;
	
	public BaseWrapper loadUserActionLog(BaseWrapper   baseWrapper) throws
    FrameworkCheckedException;
	
	BaseWrapper createOrUpdateActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

	BaseWrapper createOrUpdateActionLog(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

	ActionLogModel createActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	ActionLogModel createCustomActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	ActionLogModel completeActionLog(ActionLogModel actionLogModel) throws FrameworkCheckedException;

	ActionLogModel completeActionLogRequiresNewTransaction(ActionLogModel actionLogModel) throws FrameworkCheckedException;

	ActionLogModel prepareAndSaveActionLogDataRequiresNewTransaction(SearchBaseWrapper searchBaseWrapper, BaseWrapper baseWrapper,
																	 ActionLogModel actionLogModel) throws FrameworkCheckedException;

	ActionLogModel getActionLogModelByActionAuthId(Long actionAuthId) throws FrameworkCheckedException;
}
