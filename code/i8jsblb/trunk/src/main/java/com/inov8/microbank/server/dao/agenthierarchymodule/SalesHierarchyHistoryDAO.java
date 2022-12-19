package com.inov8.microbank.server.dao.agenthierarchymodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyHistoryModel;

public interface SalesHierarchyHistoryDAO extends BaseDAO<SalesHierarchyHistoryModel, Long>
{

	public List<SalesHierarchyHistoryModel> findSaleUserHistoryByBankUserId(Long bankUserId)
			throws FrameworkCheckedException;
	
}
