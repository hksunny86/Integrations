package com.inov8.microbank.server.dao.usecasemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.UsecaseModel;

import java.util.List;

public interface UsecaseDAO extends BaseDAO<UsecaseModel, Long> {
	
	CustomList<UsecaseModel>  searchAuthorizationEnabledUsecases( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	CustomList<UsecaseModel>  searchUsecase( SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
	UsecaseModel saveOrUpdateUsecase(UsecaseModel usecaseModel) throws FrameworkCheckedException;
	boolean getAffectedAuthorizationRequests(UsecaseModel usecaseModel) throws FrameworkCheckedException;

	List<UsecaseModel> getAllUseCases() throws FrameworkCheckedException;
}
