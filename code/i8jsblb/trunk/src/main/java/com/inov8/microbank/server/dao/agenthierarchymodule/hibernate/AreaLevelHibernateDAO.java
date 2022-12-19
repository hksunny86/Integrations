package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.AreaLevelModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.AreaLevelDAO;

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


public class AreaLevelHibernateDAO extends BaseHibernateDAO<AreaLevelModel, Long, AreaLevelDAO> implements AreaLevelDAO{

	public List<AreaLevelModel> findAreaLevelsByRegionId(long regionId) throws FrameworkCheckedException
	{
		List<AreaLevelModel> areaLevelsModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AreaLevelModel.class );
        detachedCriteria.add(Restrictions.eq("region.regionId", regionId) );
        detachedCriteria.addOrder(Order.asc("areaLevelId"));
        areaLevelsModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return areaLevelsModelList;
	}
	
	public List<AreaLevelModel> findAreaLevelsByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException
	{
		List<AreaLevelModel> areaLevelModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( AreaLevelModel.class );
        detachedCriteria.add( Restrictions.eq( "regionalHierarchyModel.regionalHierarchyId", regionalHierarchyId))
        .addOrder(Order.asc("areaLevelName"));
        areaLevelModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return areaLevelModelList;
	}
	
	public List<AreaLevelModel> areaLevelIntegrityCheck(AreaLevelModel areaLevelModel) throws FrameworkCheckedException
	{
		List<AreaLevelModel> areaLevelModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( AreaLevelModel.class );
        detachedCriteria.add( Restrictions.eq( "parentAreaLevel.areaLevelId", areaLevelModel.getParentAreaLevelId()) );
        detachedCriteria.add( Restrictions.eq( "active", areaLevelModel.getActive()) );
        areaLevelModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return areaLevelModelList;
	}
	
}
