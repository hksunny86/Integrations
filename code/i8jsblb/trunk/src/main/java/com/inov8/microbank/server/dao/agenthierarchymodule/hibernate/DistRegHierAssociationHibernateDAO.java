package com.inov8.microbank.server.dao.agenthierarchymodule.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistRegHierAssociationModel;
import com.inov8.microbank.common.model.agenthierarchy.RegionModel;
import com.inov8.microbank.server.dao.agenthierarchymodule.DistRegHierAssociationDAO;

public class DistRegHierAssociationHibernateDAO extends BaseHibernateDAO<DistRegHierAssociationModel, Long, DistRegHierAssociationDAO>
implements DistRegHierAssociationDAO 
{
	public List<DistRegHierAssociationModel> findRegionalHierarchyByDistributorId(Long distributorId)
	{
		List<DistRegHierAssociationModel> distRegHieAssociationModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DistRegHierAssociationModel.class );
        detachedCriteria.add( Restrictions.eq( "distributor.distributorId", distributorId) );
        distRegHieAssociationModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return distRegHieAssociationModelList;
	}
}
