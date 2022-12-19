package com.inov8.microbank.server.dao.portal.mfsaccountmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BulkDisbursementsModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.UserInfoListViewModel;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Mohammad Shehzad Ashraf
 * @version 1.0
 */

public interface UserInfoListViewDAO extends BaseDAO<UserInfoListViewModel, Long>{

	public List<BulkDisbursementsModel> validateMobileNos(List<BulkDisbursementsModel> bulkList) throws FrameworkCheckedException;
	public String getAreaByAppUserId(Long appUserId) throws FrameworkCheckedException;
}
