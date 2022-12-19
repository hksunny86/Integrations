package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

public class ActionLogFacadeImpl implements ActionLogFacade {

	private ActionLogManager actionLogManager; 
	private FrameworkExceptionTranslator frameworkExceptionTranslator; 
	
	
	public SearchBaseWrapper searchActionLog(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.actionLogManager.searchActionLog(searchBaseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return searchBaseWrapper;
	}

	public BaseWrapper createOrUpdateActionLog(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.actionLogManager.createOrUpdateActionLog(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return baseWrapper;
	}

	public BaseWrapper createOrUpdateActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.actionLogManager.createOrUpdateActionLogRequiresNewTransaction(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return baseWrapper;
	}

	public BaseWrapper loadUserActionLog(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
	    {
	      
			this.actionLogManager.loadUserActionLog(baseWrapper);
			
	    
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    			FrameworkExceptionTranslator.FIND_ACTION);
	    	
	    }
	    return baseWrapper;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public ActionLogModel createActionLogRequiresNewTransaction(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
	}

	public ActionLogModel completeActionLog(ActionLogModel actionLogModel) throws FrameworkCheckedException {
		return actionLogManager.completeActionLog(actionLogModel);
	}
	
	public ActionLogModel completeActionLogRequiresNewTransaction(ActionLogModel actionLogModel) throws FrameworkCheckedException {
		return actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);
	}

	@Override
	public ActionLogModel prepareAndSaveActionLogDataRequiresNewTransaction(SearchBaseWrapper searchBaseWrapper, BaseWrapper baseWrapper, ActionLogModel actionLogModel) throws FrameworkCheckedException {
		return actionLogManager.prepareAndSaveActionLogDataRequiresNewTransaction(searchBaseWrapper,baseWrapper,actionLogModel);
	}

	@Override
	public ActionLogModel getActionLogModelByActionAuthId(Long actionAuthId) throws FrameworkCheckedException {
		return actionLogManager.getActionLogModelByActionAuthId(actionAuthId);
	}

	@Override
	public ActionLogModel createCustomActionLogRequiresNewTransaction(
			BaseWrapper baseWrapper) throws FrameworkCheckedException {
		return actionLogManager.createCustomActionLogRequiresNewTransaction(baseWrapper);	
	}
}
