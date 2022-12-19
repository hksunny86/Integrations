package com.inov8.microbank.server.dao.agenthierarchymodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.agenthierarchy.SalesHierarchyModel;

public interface SalesHierarchyDAO extends BaseDAO<SalesHierarchyModel, Long>
{
	public SalesHierarchyModel findSaleUserByBankUserId(Long bankUserId) throws FrameworkCheckedException;
	public List<SalesHierarchyModel> findUltimateSaleUsers() throws FrameworkCheckedException;
	public List<SalesHierarchyModel> findSaleUsersByUltimateParentSaleUserId(Long ultimateParentSaleUserId) throws FrameworkCheckedException;
}
