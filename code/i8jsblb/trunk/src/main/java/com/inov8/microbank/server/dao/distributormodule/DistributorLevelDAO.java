package com.inov8.microbank.server.dao.distributormodule;

import java.util.Collection;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.RetailerContactModel;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface DistributorLevelDAO
    extends BaseDAO<DistributorLevelModel, Long>
{

	public List<DistributorLevelModel> findDistributorLevelByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException;
	public boolean deleteDistributorLevel(DistributorLevelModel model);
	public List<DistributorLevelModel> findAgentLevelsByRegionId(long regionId) throws FrameworkCheckedException;
	public List<RetailerContactModel> findDistributorLevelReference(long distributorLevelId) throws FrameworkCheckedException;
	public CustomList<DistributorLevelModel> findDistributorLevelByRegionalHierarchyIdList(Collection<Long> regionalHierarchyIdList) throws FrameworkCheckedException;
	public List<DistributorLevelModel> loadDistributorLevelByRegionAndRegionHierarchy(
			Long regionId, Long regionHierarchyId);
}
