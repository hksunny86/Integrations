package com.inov8.microbank.server.dao.retailermodule.hibernate;

import java.util.List;

import javax.sql.DataSource;

import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.server.dao.retailermodule.RetailerDAO;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.ApplicantDetailModel;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class RetailerContactHibernateDAO
    extends
    BaseHibernateDAO<RetailerContactModel, Long, RetailerContactDAO>
    implements
    RetailerContactDAO

{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	private RetailerDAO retailerDAO;

	public List<RetailerContactModel> findRetailerContactByDistributorLevelId(long distributorLevelId, long retailerId) throws FrameworkCheckedException
	{
		List<RetailerContactModel> retailerContactModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RetailerContactModel.class);
        detachedCriteria.add( Restrictions.eq("relationDistributorLevelModel.distributorLevelId", distributorLevelId));
        detachedCriteria.add( Restrictions.eq("relationRetailerIdRetailerModel.retailerId", retailerId));
        retailerContactModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return retailerContactModelList;
	}
	
	public List<RetailerContactModel> findHeadAgents(long distributorLevelId, long retailerId) throws FrameworkCheckedException
	{
		List<RetailerContactModel> retailerContactModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerContactModel.class );
        detachedCriteria.add( Restrictions.eq( "relationDistributorLevelModel.distributorLevelId", distributorLevelId) );
        detachedCriteria.add( Restrictions.eq( "relationRetailerIdRetailerModel.retailerId", retailerId) );
        retailerContactModelList = getHibernateTemplate().findByCriteria( detachedCriteria );
		return retailerContactModelList;
	}
	
	public List<RetailerContactModel> findChildRetailerContactsById(long retailerContactId) throws FrameworkCheckedException
	{
		List<RetailerContactModel> retailerContactModelList = null;
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass( RetailerContactModel.class );
        detachedCriteria.add(Restrictions.eq("parentRetailerContactModel.retailerContactId", retailerContactId));
        retailerContactModelList = getHibernateTemplate().findByCriteria(detachedCriteria);
		return retailerContactModelList;
	}
	
	@Override
	public boolean isHeadAgent(String mobileNo)
	{
		boolean isHead = false;
		String hql = "select model.head from RetailerContactModel model, AppUserModel au where model.retailerContactId = au.relationRetailerContactIdRetailerContactModel.retailerContactId and au.mobileNo=?";
		List<Boolean> list = getHibernateTemplate().find(hql, mobileNo);
		if( CollectionUtils.isNotEmpty(list) )
		{
			isHead = list.get(0);
		}
		return isHead;
	}

	public boolean checkAgentExistsForDistributor(Long distributorId) throws FrameworkCheckedException{
		boolean recordExists = false;
		try{
			String sql = "select count(*) from retailer_contact where is_active=1 and retailer_id in (select retailer_id from retailer where distributor_id=? and is_active=1)";
			int count = jdbcTemplate.queryForInt(sql, new Long[]{distributorId});
			recordExists = (count > 0) ? true : false;
		}catch(Exception ex){
			throw new FrameworkCheckedException(ex.getMessage(),ex);
		}
		return recordExists;
	}
	
	@Override
	public boolean isKinIdDocumentNumberAlreadyExist(String initialAppFormNumber, Long idDocumentType, 
			String idDocumentNumber) throws FrameworkCheckedException
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(RetailerContactModel.class);
		criteria.add( Restrictions.ne("initialAppFormNo", initialAppFormNumber));
		criteria.add( Restrictions.eq("nokIdType",idDocumentType));
		criteria.add( Restrictions.eq("nokIdNumber",idDocumentNumber));
		List<RetailerContactModel> list = getHibernateTemplate().findByCriteria(criteria);
		
		return list.size()>0 ? true:false;
	}

    @Override
    public RetailerModel getRetailerModelByRetailerId(Long retailerId) throws FrameworkCheckedException {
        return retailerDAO.findByPrimaryKey(retailerId);
    }

    @Override
    public DistributorModel findDistributorModelById(Long distributorId) throws FrameworkCheckedException {
        return retailerDAO.findDistributorModelById(distributorId);
    }

    public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

    public void setRetailerDAO(RetailerDAO retailerDAO) {
        this.retailerDAO = retailerDAO;
    }
}
