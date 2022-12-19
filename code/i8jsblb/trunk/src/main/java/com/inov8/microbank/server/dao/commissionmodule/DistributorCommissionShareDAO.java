package com.inov8.microbank.server.dao.commissionmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareModel;

public interface DistributorCommissionShareDAO extends
		BaseDAO<DistributorCommissionShareModel,Long> {

	void createDistributorCommissionShare(DistributorCommissionShareModel distributorCommissionShareModel);
	CustomList<DistributorCommissionShareModel> searchDistributorCommissionShare(Long distributorId, Long regionId, Long productId, Long currentLevelId);
}
