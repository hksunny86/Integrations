package com.inov8.microbank.server.dao.portal.authorizationmodule;

import java.util.LinkedHashMap;
import java.util.List;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.portal.authorizationmodule.ActionAuthorizationModel;

public interface ActionAuthorizationModelDAO extends BaseDAO<ActionAuthorizationModel, Long> {

	CustomList<ActionAuthorizationModel> searchConflictedAuthorizationRequests(SearchBaseWrapper searchBaseWrapper);
	CustomList<ActionAuthorizationModel> checkExistingRequest(ActionAuthorizationModel actionAuthorizationModel);
	CustomList<ActionAuthorizationModel> searchMyRequests(SearchBaseWrapper searchBaseWrapper);
	CustomList<ActionAuthorizationModel> search(SearchBaseWrapper searchBaseWrapper);
}
