package com.inov8.microbank.server.service.portal.usecasemodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.common.model.usecasemodule.LevelCheckerModel;
import com.inov8.microbank.common.model.usecasemodule.UsecaseLevelModel;

public interface UsecaseManager {
	
	SearchBaseWrapper searchAuthorizationEnableUsecase( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper searchUsecase( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	UsecaseModel loadUsecase(Long usecaseId) throws FrameworkCheckedException;
	BaseWrapper saveOrUpdateUsecase(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	SearchBaseWrapper searchUsecaseLevels(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	UsecaseLevelModel findUsecaseLevel(Long usecaseId,Long levelNo) throws FrameworkCheckedException;
	List<LevelCheckerModel> getLevelCheckerModelList(Long usecaseLevelId ) throws FrameworkCheckedException;
	boolean isCheckerAtLevel(Long usecaseId, Long levelNo, Long appUserId)throws FrameworkCheckedException;
	Long getNextAuthorizationLevel(Long usecaseId, Long currentLevel) throws FrameworkCheckedException;
	boolean getAffectedAuthorizationRequests(UsecaseModel usecaseModel) throws FrameworkCheckedException;
	List<LevelCheckerModel> getLevelCheckerModelListByCheckerAppUserId(Long checkerAppUserId) throws FrameworkCheckedException;
	List<AppUserModel> getAsociatedCheckers(List<UsecaseModel> usecaseModelList) throws FrameworkCheckedException;

	List<UsecaseModel> getAllUseCases() throws FrameworkCheckedException;
}
