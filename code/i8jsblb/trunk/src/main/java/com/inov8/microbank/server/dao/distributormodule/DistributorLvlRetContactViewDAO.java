package com.inov8.microbank.server.dao.distributormodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.DistributorLvlRetContactViewModel;

import java.util.List;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Hassan Javaid
 * @version 1.0
 */

public interface DistributorLvlRetContactViewDAO
    extends BaseDAO<DistributorLvlRetContactViewModel, Long>
{
	public SearchBaseWrapper getParentAgentsBydistributorLevelId(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	List<DistributorLvlRetContactViewModel> getParentAgentsByDistributorLevelId(Long retailerId,Long distributorLevelId,Long distributorId) throws FrameworkCheckedException;

}
