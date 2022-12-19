package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.RegionDAO;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Rashid Mahmood
 * @version 1.0
 */

public class RegionHibernateDAO
    extends BaseHibernateDAO<RegionModel, Long, RegionDAO>
    implements RegionDAO
{
	public List<RegionModel> findRegionsByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException
	{
		List<RegionModel> regionModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RegionModel.class );
        detachedCriteria.add( Restrictions.eq( "regionalHierarchyModel.regionalHierarchyId", regionalHierarchyId))
        .addOrder(Order.asc("regionName"));
        
        regionModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return regionModelList;
	}
	
	/*public List<RegionModel> findActiveRegionsByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException
	{
		List<RegionModel> regionModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RegionModel.class );
		detachedCriteria.add( Restrictions.eq( "regionalHierarchyModel.regionalHierarchyId", regionalHierarchyId))
        .addOrder(Order.asc("regionName"));
        
        regionModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return regionModelList;
	}*/
}
