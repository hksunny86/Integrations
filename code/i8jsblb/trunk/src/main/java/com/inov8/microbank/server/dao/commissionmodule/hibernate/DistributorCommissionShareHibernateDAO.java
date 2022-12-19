package com.inov8.microbank.server.dao.commissionmodule.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agenthierarchy.DistributorCommissionShareModel;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.dao.commissionmodule.DistributorCommissionShareDAO;

public class DistributorCommissionShareHibernateDAO
		extends
		BaseHibernateDAO<DistributorCommissionShareModel, Long, DistributorCommissionShareDAO>
		implements DistributorCommissionShareDAO {

	@Override
	public void createDistributorCommissionShare(
			DistributorCommissionShareModel distributorCommissionShareModel) {
		getHibernateTemplate().saveOrUpdate(distributorCommissionShareModel);
		getHibernateTemplate().flush();
	}
	
	@Override
	public CustomList<DistributorCommissionShareModel> searchDistributorCommissionShare(Long distributorId,
			Long regionId, Long productId, Long currentLevelId) {
		
		Session session = this.getSession();
		Criteria distributorCommissionShareModelCriteria = session.createCriteria( DistributorCommissionShareModel.class ) ;
		
		Criterion distributorCr = Restrictions.eq("distributorIdModel.distributorId", distributorId) ;
		Criterion regionCr = Restrictions.eq("regionIdModel.regionId", regionId) ;
		
		Criterion productCr;
		if(productId==null)
		{
			productCr = Restrictions.isNull("productIdModel.productId") ;
		}
		else
		{
			productCr = Restrictions.eq("productIdModel.productId", productId) ;
		}
		
		Criterion currentLevelCr = Restrictions.eq("currentDistributorLevelIdModel.distributorLevelId", currentLevelId) ;
		
		distributorCommissionShareModelCriteria.add(distributorCr);
		distributorCommissionShareModelCriteria.add(productCr);
		distributorCommissionShareModelCriteria.add(currentLevelCr);
		distributorCommissionShareModelCriteria.add(regionCr);
		
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		
		return this.findByCriteria(distributorCr, currentLevelCr,regionCr, productCr);
	}
	
}
