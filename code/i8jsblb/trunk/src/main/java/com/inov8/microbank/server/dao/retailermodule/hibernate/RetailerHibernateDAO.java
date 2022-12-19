package com.inov8.microbank.server.dao.retailermodule.hibernate;

import java.util.List;

import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.server.dao.distributormodule.DistributorDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CountryModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;

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

public class RetailerHibernateDAO
    extends BaseHibernateDAO<RetailerModel, Long, RetailerDAO>
    implements RetailerDAO
{
	private DistributorDAO distributorDAO;
	public List<RetailerModel> findRetailersByRegionId(long regionId) throws FrameworkCheckedException
	{
		List<RetailerModel> retailerModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerModel.class );
        detachedCriteria.add(Restrictions.eq("regionModel.regionId", regionId) )
        .addOrder(Order.asc("name"));
        retailerModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return retailerModelList;
	}
	
	public List<RetailerModel> findRetailersByDistributorId(long distributorId) throws FrameworkCheckedException
	{
		List<RetailerModel> retailerModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerModel.class );
        detachedCriteria.add(Restrictions.eq("relationDistributorIdDistributorModel.distributorId", distributorId)).addOrder(Order.asc("name"));
        retailerModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return retailerModelList;
	}

	@Override
	public DistributorModel findDistributorModelById(Long distributorId) throws FrameworkCheckedException {
		return distributorDAO.findByPrimaryKey(distributorId);
	}

	public List<CountryModel> loadCountry(){
		List<CountryModel> countryList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( CountryModel.class );
		countryList = getHibernateTemplate().findByCriteria( detachedCriteria );
       return countryList;
	}
	
	public List<RetailerContactModel> franchiseReference(Long retailerId) throws FrameworkCheckedException{
		List<RetailerContactModel> retailerModels = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerContactModel.class )
				.add(Restrictions.eq("relationRetailerIdRetailerModel.retailerId", retailerId));
		retailerModels = getHibernateTemplate().findByCriteria( detachedCriteria );
       return retailerModels;
	}
	
	public List<RetailerContactModel> findDistributorLevelReference(long distributorLevelId) throws FrameworkCheckedException{
		List<RetailerContactModel> retailerContactModels = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerContactModel.class );
        detachedCriteria.add(Restrictions.eq( "relationDistributorLevelModel.distributorLevelId", distributorLevelId) );
        detachedCriteria.add(Restrictions.eq( "active", Boolean.TRUE) );
        retailerContactModels = getHibernateTemplate().findByCriteria( detachedCriteria );
		return retailerContactModels;
	}

	public void setDistributorDAO(DistributorDAO distributorDAO) {
		this.distributorDAO = distributorDAO;
	}
}
