package com.inov8.microbank.server.facade.usecasemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;
import com.inov8.microbank.server.service.portal.usecasemodule.UsecaseManager;

public class UsecaseFacadeImpl implements UsecaseFacade {
	
	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	private UsecaseManager usecaseManager; 
	
	@Override
	public SearchBaseWrapper searchAuthorizationEnableUsecase( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
	    try
        {
	        return usecaseManager.searchAuthorizationEnableUsecase( searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

	@Override
	public SearchBaseWrapper searchUsecase( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
	    try
        {
	        return usecaseManager.searchUsecase( searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	

	@Override
	public UsecaseModel loadUsecase(Long usecaseId) throws FrameworkCheckedException {
		try
        {
	        return usecaseManager.loadUsecase(usecaseId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	
	@Override
	public BaseWrapper saveOrUpdateUsecase(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		try
        {
	        return usecaseManager.saveOrUpdateUsecase(baseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.SAVE_OR_UPDATE_ACTION );
        }
	}
	@Override
	public SearchBaseWrapper searchUsecaseLevels(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException{
		try
        {
	        return usecaseManager.searchUsecaseLevels(searchBaseWrapper);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	public List<LevelCheckerModel> getLevelCheckerModelList(Long usecaseLevelId ) throws FrameworkCheckedException{
		try
        {
	        return usecaseManager.getLevelCheckerModelList(usecaseLevelId );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public List<LevelCheckerModel> getLevelCheckerModelListByCheckerAppUserId(Long checkerAppUserId) throws FrameworkCheckedException {
		try
        {
	        return usecaseManager.getLevelCheckerModelListByCheckerAppUserId(checkerAppUserId );
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
		
	}
	
	public UsecaseLevelModel findUsecaseLevel(Long usecaseId,Long levelNo) throws FrameworkCheckedException{
		try
        {
	        return usecaseManager.findUsecaseLevel(usecaseId, levelNo);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }

	}
	@Override
	public boolean isCheckerAtLevel(Long usecaseId, Long levelNo, Long appUserId)throws FrameworkCheckedException {
		try
        {
	        return usecaseManager.isCheckerAtLevel(usecaseId, levelNo, appUserId);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public Long getNextAuthorizationLevel(Long usecaseId, Long currentLevel)throws FrameworkCheckedException {
		try
        {
	        return usecaseManager.getNextAuthorizationLevel(usecaseId, currentLevel);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public boolean getAffectedAuthorizationRequests(UsecaseModel usecaseModel) throws FrameworkCheckedException {
		try
        {
	        return usecaseManager.getAffectedAuthorizationRequests(usecaseModel);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}
	@Override
	public List<AppUserModel> getAsociatedCheckers(List<UsecaseModel> usecaseModelList)
			throws FrameworkCheckedException {
		try
        {
	        return usecaseManager.getAsociatedCheckers(usecaseModelList);
        }
        catch( Exception e )
        {
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
	}

    @Override
    public List<UsecaseModel> getAllUseCases() throws FrameworkCheckedException {
        return usecaseManager.getAllUseCases();
    }

    public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setUsecaseManager(UsecaseManager usecaseManager) {
		this.usecaseManager = usecaseManager;
	}
	
}
