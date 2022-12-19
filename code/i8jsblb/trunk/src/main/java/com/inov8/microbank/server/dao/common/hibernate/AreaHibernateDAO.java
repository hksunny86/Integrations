package com.inov8.microbank.server.dao.common.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.server.dao.common.AreaDAO;


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

public class AreaHibernateDAO
    extends BaseHibernateDAO<AreaModel, Long, AreaDAO>
    implements AreaDAO
{
	
	public List<AreaModel> findAreaNamesByRegionId(long regionId) throws FrameworkCheckedException
	{
		List<AreaModel> areaNamesModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AreaModel.class );
        detachedCriteria.add(Restrictions.eq("regionModel.regionId", regionId) );
        detachedCriteria.addOrder(Order.asc("areaId"));
        areaNamesModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return areaNamesModelList;
	}
	
	public List<AreaModel> findAgentAreaByRegionId(long regionId) throws FrameworkCheckedException
	{
		List<AreaModel> agentAreaModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AreaModel.class );
        detachedCriteria.add(Restrictions.eq("regionModel.regionId", regionId) );
        detachedCriteria.add(Restrictions.isNull("relationParentAreaIdAreaModel") );
        agentAreaModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return agentAreaModelList;
	}
	
	public List<AreaModel> findAreaByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException
	{
		List<AreaModel> areaList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( AreaModel.class );
		detachedCriteria.add( Restrictions.eq( "regionalHierarchyModel.regionalHierarchyId", regionalHierarchyId));
        areaList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return areaList;
	}
	
	public List<RetailerContactModel> findAreaReference(long areaId) throws FrameworkCheckedException{
		List<RetailerContactModel> retailerContactModels = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerContactModel.class );
        detachedCriteria.add(Restrictions.eq( "relationAreaIdAreaModel.areaId", areaId) );
        retailerContactModels = getHibernateTemplate().findByCriteria( detachedCriteria );
		return retailerContactModels;
	}
}
