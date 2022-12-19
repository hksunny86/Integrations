package com.inov8.microbank.server.dao.portal.partnergroupmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.UserPermissionModel;
import com.inov8.microbank.common.model.portal.partnergroupmodule.PartnerPermissionViewModel;

public interface PartnerPermissionViewDAO extends BaseDAO<PartnerPermissionViewModel, Long>{
	public List<UserPermissionModel> getFranchisePermissions() throws FrameworkCheckedException;

}
