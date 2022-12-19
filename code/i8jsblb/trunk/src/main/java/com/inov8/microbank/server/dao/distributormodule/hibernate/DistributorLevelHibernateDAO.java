package com.inov8.microbank.server.dao.distributormodule.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.server.dao.distributormodule.DistributorLevelDAO;

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

public class DistributorLevelHibernateDAO
    extends BaseHibernateDAO<DistributorLevelModel, Long, DistributorLevelDAO>
    implements DistributorLevelDAO
{

	public List<DistributorLevelModel> findAgentLevelsByRegionId(long regionId) throws FrameworkCheckedException
	{
		List<DistributorLevelModel> agentLevelsModelList = null;
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DistributorLevelModel.class );
        detachedCriteria.add(Restrictions.eq("regionModel.regionId", regionId) );
        detachedCriteria.addOrder(Order.asc("distributorLevelId"));
        
        agentLevelsModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return agentLevelsModelList;
	}
	
	public List<DistributorLevelModel> findDistributorLevelByRegionalHierarchyId(long regionalHierarchyId) throws FrameworkCheckedException{
		List<DistributorLevelModel> levelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DistributorLevelModel.class );
        detachedCriteria.add( Restrictions.eq( "regionalHierarchyModel.regionalHierarchyId", regionalHierarchyId));
        /*.addOrder(Order.asc("name"));*/
        levelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return levelList;
	}
	
	public boolean deleteDistributorLevel(DistributorLevelModel model) {
		boolean retVal=false;
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("delete DistributorLevelModel where distributorLevelId =");
		stringBuilder.append(model.getDistributorLevelId());		
	    int rows=this.getHibernateTemplate().bulkUpdate(stringBuilder.toString());
	    if(rows >0){
	    	retVal=true;
	    }
	    return retVal;
		
	}
	
	public List<RetailerContactModel> findDistributorLevelReference(long distributorLevelId) throws FrameworkCheckedException{
		List<RetailerContactModel> retailerContactModels = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerContactModel.class );
        detachedCriteria.add(Restrictions.eq( "relationDistributorLevelModel.distributorLevelId.", distributorLevelId) );
        retailerContactModels = getHibernateTemplate().findByCriteria( detachedCriteria );
		return retailerContactModels;
	}
	
	
	@Override
	public CustomList<DistributorLevelModel> findDistributorLevelByRegionalHierarchyIdList(Collection<Long> regionalHierarchyIdList) throws FrameworkCheckedException {
		Criterion criterion = Restrictions.in("regionalHierarchyModel.regionalHierarchyId", regionalHierarchyIdList);
        CustomList<DistributorLevelModel> customList = findByCriteria(criterion);
        
        for (DistributorLevelModel model : customList.getResultsetList()) {
			model.setName(model.getName()+" ("+model.getRegionModel().getRegionName()+")");
		}
        
        return customList;
	}
	
	@Override
	public List<DistributorLevelModel> loadDistributorLevelByRegionAndRegionHierarchy(Long regionId, Long regionHierarchyId){
		
		StringBuffer query = new StringBuffer("");
		query.append("FROM DistributorLevelModel dl WHERE dl.relationManagingLevelIdDistributorLevelModel.distributorLevelId IS NULL ")
		.append(" AND dl.relationUltimateManagingLevelIdDistributorLevelModel.distributorLevelId IS NULL ")
		.append(" AND dl.regionModel.regionId = " + regionId)
		.append(" AND dl.regionalHierarchyModel.regionalHierarchyId = " + regionHierarchyId);
		
		
		List<DistributorLevelModel> list = this.getHibernateTemplate().find(query.toString());
		
		return list;
	}
}
