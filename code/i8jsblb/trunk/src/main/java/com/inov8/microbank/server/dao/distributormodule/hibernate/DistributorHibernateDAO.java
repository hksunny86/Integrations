package com.inov8.microbank.server.dao.distributormodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.distributormodule.DistributorDAO;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * <p>Title: Microbank</p>
 *
 *
 * <p>Copyright: Copyright (c) 2014</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Rashid Mahmood
 * @version 1.0
 */

public class DistributorHibernateDAO
    extends BaseHibernateDAO<DistributorModel, Long, DistributorDAO>
    implements DistributorDAO
{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public List<DistributorModel> findAllDistributor(){
		List<DistributorModel> distributorModels = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DistributorModel.class )
				.addOrder(Order.asc("name"));
		if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.MNO))
		{
			Criterion criterion = Restrictions.eq("mnoModel.mnoId",50028L);
			detachedCriteria.add(criterion);
		}
		else if(UserUtils.getCurrentUser().getAppUserTypeId().equals(UserTypeConstantsInterface.BANK)){
			Criterion nullCriterion = Restrictions.isNull("mnoModel.mnoId");
			Criterion valCriterion = Restrictions.eq("mnoModel.mnoId",50027L);
			LogicalExpression criteria = Restrictions.or(nullCriterion,valCriterion);
			detachedCriteria.add(criteria);
		}
		distributorModels = getHibernateTemplate().findByCriteria( detachedCriteria );
        
		return distributorModels;
	}
	
	public List<DistributorModel> findActiveDistributor(){
		List<DistributorModel> distributorModels = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DistributorModel.class )
				.add(Restrictions.eq( "active", Boolean.TRUE))
				.addOrder(Order.asc("name"));
		distributorModels = getHibernateTemplate().findByCriteria( detachedCriteria );
        
		return distributorModels;
	}

	public List<DistributorModel> findAllDistributorForMno(DistributorModel distributorModel) {

		List<DistributorModel> distributorModels = null;
		String query = null;
		query = "select * from DISTRIBUTOR where SERVICE_OP_ID=? and IS_ACTIVE=1";
		Object[] args = {distributorModel.getMnoId()};
//		List<DistributorModel> oResult= jdbcTemplate.queryForList(query,args);
		/*for(int i=0;i<oResult.size();i++){
		}*/

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DistributorModel.class )
				.add(Restrictions.eq( "active", Boolean.TRUE))
				.add(Restrictions.eq("mnoModel.mnoId", distributorModel.getMnoModel().getMnoId()));

		distributorModels = getHibernateTemplate().findByCriteria( detachedCriteria );


		return distributorModels;
	}

	public List<DistributorModel> findDistributorByserviceOperatorId(Long soId)
	{
		List<DistributorModel> distributorModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DistributorModel.class );
		detachedCriteria.add( Restrictions.eq( "mnoModel.mnoId", soId) );
		distributorModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return distributorModelList;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
