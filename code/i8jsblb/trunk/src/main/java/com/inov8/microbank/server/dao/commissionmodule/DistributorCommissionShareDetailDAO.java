package com.inov8.microbank.server.dao.commissionmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareDetailModel;

public interface DistributorCommissionShareDetailDAO extends
		BaseDAO<DistributorCommissionShareDetailModel,Long> {

	void deleteDistributorCommShareDtlModel(Long deleteDistributorCommShareId);
}
