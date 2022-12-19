package com.inov8.microbank.server.service.portal.mnologsmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;

public interface MnoLogsManager {
	SearchBaseWrapper loadMnoLogs(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	BaseWrapper loadMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;

	List<UsecaseModel> findUsecases( Long[] usecaseIds );

	SearchBaseWrapper searchMnoLogs(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	BaseWrapper updateMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;

	BaseWrapper createMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException;

	List<MnologsListViewModel> getActionLogData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
}
