package com.inov8.microbank.server.dao.portal.concernmodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ConcernModel;

public interface ConcernDAO extends BaseDAO<ConcernModel, Long>{
	public ConcernModel findPrimaryConcernRecord(String concernCode);
	public ConcernModel findActiveIndirectPartner(String concernCode);
	public List<ConcernModel>  findSecondaryConcernRecords(String concernCode);
}
