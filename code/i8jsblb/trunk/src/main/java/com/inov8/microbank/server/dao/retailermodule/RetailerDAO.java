package com.inov8.microbank.server.dao.retailermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CountryModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;

public interface RetailerDAO
    extends BaseDAO<RetailerModel, Long>
{
	public List<RetailerModel> findRetailersByRegionId(long regionId) throws FrameworkCheckedException;
	public List<CountryModel> loadCountry();
	public List<RetailerContactModel> franchiseReference(Long retailerId) throws FrameworkCheckedException;
	public List<RetailerContactModel> findDistributorLevelReference(long distributorLevelId) throws FrameworkCheckedException;
	public List<RetailerModel> findRetailersByDistributorId(long distributorId) throws FrameworkCheckedException;

	DistributorModel findDistributorModelById(Long distributorId) throws FrameworkCheckedException;
}
