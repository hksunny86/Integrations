package com.inov8.microbank.server.dao.appuserpartnergroupmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserPartnerGroupModel;

public interface AppUserPartnerGroupDAO extends BaseDAO<AppUserPartnerGroupModel,Long> {
	public AppUserPartnerGroupModel findByAppUserId(long appUserId) throws FrameworkCheckedException;
}
