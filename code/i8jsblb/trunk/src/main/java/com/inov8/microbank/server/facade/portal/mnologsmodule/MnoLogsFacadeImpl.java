package com.inov8.microbank.server.facade.portal.mnologsmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;
import com.inov8.microbank.server.service.portal.mnologsmodule.MnoLogsManager;

public class MnoLogsFacadeImpl implements MnoLogsFacade {

	private MnoLogsManager mnoLogsManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public BaseWrapper createMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MnologsListViewModel> getActionLogData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return mnoLogsManager.getActionLogData(searchBaseWrapper);
	}

	public SearchBaseWrapper loadMnoLogs(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		return mnoLogsManager.loadMnoLogs(searchBaseWrapper);
	}

	public BaseWrapper loadMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UsecaseModel> findUsecases( Long[] usecaseIds )
	{
		return mnoLogsManager.findUsecases( usecaseIds );
	}

	public SearchBaseWrapper searchMnoLogs(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		try {
			return mnoLogsManager.searchMnoLogs(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setMnoLogsManager(MnoLogsManager mnoLogsManager) {
		this.mnoLogsManager = mnoLogsManager;
	}

}
