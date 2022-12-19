package com.inov8.microbank.server.dao.usergroupmodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.PartnerModel;

public interface PartnerDAO extends BaseDAO<com.inov8.microbank.common.model.PartnerModel,Long> {
	public List<PartnerModel> findPartnerByRetailerId(long retailerId) throws FrameworkCheckedException;

}
