package com.inov8.microbank.server.service.portal.authorizationmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationHistoryModel;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthorizationHistoryModelDAO;
import com.inov8.microbank.server.dao.portal.authorizationmodule.ActionAuthorizationModelDAO;

public class ActionAuthorizationHistoryManagerImpl implements ActionAuthorizationHistoryManager {

	private ActionAuthorizationHistoryModelDAO actionAuthorizationHistoryModelDAO;
	

	@Override
	public SearchBaseWrapper search(SearchBaseWrapper searchBaseWrapper)throws FrameworkCheckedException {
		CustomList<ActionAuthorizationHistoryModel> customList = this.actionAuthorizationHistoryModelDAO.findByExample( (ActionAuthorizationHistoryModel)
			 	searchBaseWrapper.getBasePersistableModel(),
		        searchBaseWrapper.getPagingHelperModel(),
		        searchBaseWrapper.getSortingOrderMap());
	searchBaseWrapper.setCustomList(customList);
	return searchBaseWrapper;
	}

	@Override
	public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws FrameworkCheckedException {
		ActionAuthorizationHistoryModel actionAuthorizationHistoryModel = actionAuthorizationHistoryModelDAO.saveOrUpdate((ActionAuthorizationHistoryModel) baseWrapper.getBasePersistableModel());
		baseWrapper.setBasePersistableModel(actionAuthorizationHistoryModel);
		return baseWrapper;
	}
	public void setActionAuthorizationHistoryModelDAO(
			ActionAuthorizationHistoryModelDAO actionAuthorizationHistoryModelDAO) {
		this.actionAuthorizationHistoryModelDAO = actionAuthorizationHistoryModelDAO;
	}
}
