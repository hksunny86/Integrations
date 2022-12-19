package com.inov8.microbank.server.service.portal.mnologsmodule;



import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;
import com.inov8.microbank.server.dao.portal.mnologsmodule.MnoLogsListViewDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class MnoLogsManagerImpl implements MnoLogsManager {

	private AppUserManager appUserManager;

	

	private ActionLogManager actionLogManager;

	private MnoLogsListViewDAO mnoLogsListViewDAO;

	public void setMnoLogsListViewDAO(MnoLogsListViewDAO mnoLogsListViewDAO) {
		this.mnoLogsListViewDAO = mnoLogsListViewDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public BaseWrapper createMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MnologsListViewModel> getActionLogData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		return mnoLogsListViewDAO.getActionLogData(searchBaseWrapper);
	}

	public SearchBaseWrapper loadMnoLogs(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		MnologsListViewModel mnologsListViewModel = mnoLogsListViewDAO.getActionLogXMLByActionLogId(Long.parseLong((String)searchBaseWrapper.getObject("actionLogId")));
		searchBaseWrapper.setBasePersistableModel(mnologsListViewModel);
		return searchBaseWrapper;
	}

	public BaseWrapper loadMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UsecaseModel> findUsecases( Long[] usecaseIds )
	{
		return mnoLogsListViewDAO.findUsecases( usecaseIds );
	}

	public SearchBaseWrapper searchMnoLogs(SearchBaseWrapper searchBaseWrapper)	throws FrameworkCheckedException {

		CustomList<MnologsListViewModel> list = this.mnoLogsListViewDAO
				.findByExampleUnSorted((MnologsListViewModel) searchBaseWrapper
						.getBasePersistableModel(), searchBaseWrapper
						.getPagingHelperModel(), searchBaseWrapper
						.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel(),null,(String)null);
		searchBaseWrapper.setCustomList(list);

		return searchBaseWrapper;

	}

	public BaseWrapper updateMnoLogs(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		// TODO Auto-generated method stub
		return null;
	}

}
