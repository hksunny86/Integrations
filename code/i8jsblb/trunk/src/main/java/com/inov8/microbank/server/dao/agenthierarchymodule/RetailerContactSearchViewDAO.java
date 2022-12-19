package com.inov8.microbank.server.dao.agenthierarchymodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.retailermodule.RetailerContactSearchViewModel;
import com.inov8.microbank.common.vo.RetailerContactDetailVO;

public interface RetailerContactSearchViewDAO extends BaseDAO<RetailerContactSearchViewModel, Long> {
	public Boolean deleteAgent(Long retailerContactId);
	public List<RetailerContactDetailVO> findRetailerContactModelList();
}
