package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;


import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.RegionalHierarchyModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.RegionalHierarchyDAO;

public class RegionalHierarchyHibernateDAO extends BaseHibernateDAO<RegionalHierarchyModel, Long, RegionalHierarchyDAO>
implements RegionalHierarchyDAO 
{
	/*public List<RegionModel> findActiveRegionofRegionalHierarchy(Long regionalHierarchyId){
		List<RegionModel> regionList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RegionModel.class );
        detachedCriteria.add( Restrictions.eq( "regionalHierarchyModel.regionalHierarchyId", regionalHierarchyId) );
        detachedCriteria.add( Restrictions.eq( "active", Boolean.TRUE) );
        regionList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return regionList;
	}*/

}
