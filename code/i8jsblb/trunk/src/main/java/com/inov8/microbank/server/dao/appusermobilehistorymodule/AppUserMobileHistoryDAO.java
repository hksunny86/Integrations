package com.inov8.microbank.server.dao.appusermobilehistorymodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserMobileHistoryModel;

public interface AppUserMobileHistoryDAO extends BaseDAO<AppUserMobileHistoryModel, Long> {

	public SearchBaseWrapper find(SearchBaseWrapper wrapper) throws FrameworkCheckedException;

}