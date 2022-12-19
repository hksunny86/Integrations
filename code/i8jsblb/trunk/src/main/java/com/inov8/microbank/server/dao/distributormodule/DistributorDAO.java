package com.inov8.microbank.server.dao.distributormodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.DistributorModel;

import java.util.List;

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

public interface DistributorDAO
    extends BaseDAO<DistributorModel, Long>
{
	public List<DistributorModel> findAllDistributor();
	public List<DistributorModel> findActiveDistributor();
	public List<DistributorModel> findAllDistributorForMno(DistributorModel distributorModel);
	public List<DistributorModel> findDistributorByserviceOperatorId(Long soId);
}
