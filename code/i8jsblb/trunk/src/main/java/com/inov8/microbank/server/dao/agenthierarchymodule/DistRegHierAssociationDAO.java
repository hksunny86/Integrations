package com.inov8.microbank.server.dao.agenthierarchymodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistRegHierAssociationModel;

public interface DistRegHierAssociationDAO extends BaseDAO<DistRegHierAssociationModel, Long>
{
	public List<DistRegHierAssociationModel> findRegionalHierarchyByDistributorId(Long distributorId);
}
