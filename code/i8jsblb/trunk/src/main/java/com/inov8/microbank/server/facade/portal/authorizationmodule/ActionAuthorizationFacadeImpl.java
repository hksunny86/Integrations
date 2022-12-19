package com.inov8.microbank.server.facade.portal.authorizationmodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthPictureModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.server.service.portal.authorizationmodule.ActionAuthorizationManager;

public class ActionAuthorizationFacadeImpl  implements ActionAuthorizationFacade {
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private ActionAuthorizationManager actionAuthorizationManager;
	
	@Override
	public SearchBaseWrapper search( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
	    try
        {
	        return actionAuthorizationManager.search( searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public SearchBaseWrapper searchMyRequests(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException 
	{ 
		try
	    {
	        return actionAuthorizationManager.searchMyRequests( searchBaseWrapper);
	    }
	    catch( Exception e )
	    {
	        throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
	    }
	}
	@Override
	public SearchBaseWrapper searchConflictedAuthorizationRequests( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
	    try
        {
	        return actionAuthorizationManager.searchConflictedAuthorizationRequests(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
        {
	        return actionAuthorizationManager.saveOrUpdate(baseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION );
        }
	}
	@Override
	public ActionAuthorizationModel load(Long actionAuthorizationId) throws FrameworkCheckedException{
		try
        {
	        return actionAuthorizationManager.load(actionAuthorizationId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public CustomList<ActionAuthorizationModel> checkExistingRequest(ActionAuthorizationModel actionAuthorizationModel)
			throws FrameworkCheckedException {
		try
        {
	        return actionAuthorizationManager.checkExistingRequest(actionAuthorizationModel);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public ActionAuthPictureModel getActionAuthPictureModelByTypeId(Long actionAuthorizationId, Long pictureTypeId) throws FrameworkCheckedException {
		try
        {
	        return actionAuthorizationManager.getActionAuthPictureModelByTypeId(actionAuthorizationId, pictureTypeId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public ActionAuthPictureModel saveOrUpdate(ActionAuthPictureModel actionAuthPictureModel) throws FrameworkCheckedException 
	{
		 try
	        {
		        return actionAuthorizationManager.saveOrUpdate(actionAuthPictureModel);
	        }
	        catch( Exception e )
	        {
	            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
	        }
	}
	
	
	@Override
	public String loadAuthorizationVOJson(HttpServletRequest req) throws Exception {
		try
        {
	        return actionAuthorizationManager.loadAuthorizationVOJson(req);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION);
        }
	}
	
	//******************************************************************************************************//
	//                              New Action Authorization Model Methods									//
	//******************************************************************************************************//
	@Override
	public void requestApproved(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
        {
	        actionAuthorizationManager.requestApproved(baseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
		
	}
	
	@Override
	public void actionDeniedOrCancelled(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException {
		try
        {
	        actionAuthorizationManager.actionDeniedOrCancelled(actionAuthorizationModel, commandModel);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
		
	}
	
	@Override
	public void requestAssignedBack(ActionAuthorizationModel actionAuthorizationModel,ActionAuthorizationModel commandModel) throws FrameworkCheckedException {
		try
        {
	        actionAuthorizationManager.requestAssignedBack(actionAuthorizationModel, commandModel);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
		
	}
	
	@Override
	public Boolean performAuthorization(BaseWrapper baseWrapper)throws FrameworkCheckedException {
		try
        {
	        return actionAuthorizationManager.performAuthorization(baseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION);
        }
	}
	
	
	//********************************************************************************************************
	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}
	public void setActionAuthorizationManager(
			ActionAuthorizationManager actionAuthorizationManager) {
		this.actionAuthorizationManager = actionAuthorizationManager;
	}
	
	
	
}
