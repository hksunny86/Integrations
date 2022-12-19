package com.inov8.microbank.server.dao.customermodule.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.customermodule.CustomerPictureModel;
import com.inov8.microbank.common.util.ApplicantTypeConstants;
import com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * @author Soofiafa
 * 
 */

public class CustomerPictureHibernateDAO extends
		BaseHibernateDAO<CustomerPictureModel, Long, CustomerPictureDAO>
		implements CustomerPictureDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO#
	 * getCustomerPictureByTypeID(java.lang.Long, java.lang.Long)
	 */
	public CustomerPictureModel getCustomerPictureByTypeId(Long pictureTypeId,
			Long customerId) {

		CustomerPictureModel customerPictureModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPictureModel.class);
		criteria.add( Restrictions.eq("relationPictureTypeModel.pictureTypeId", pictureTypeId) );
		criteria.add( Restrictions.eq("relationCustomerModel.customerId", customerId) );
		//added by atif hussain
		//criteria.add( Restrictions.ne("discrepant", Boolean.TRUE));
		List<CustomerPictureModel> list = getHibernateTemplate().findByCriteria(criteria);

		if(list != null && !list.isEmpty()) {

			customerPictureModel = list.get(0);
		}

		return customerPictureModel;
	}

	@Override
	public CustomerPictureModel getCustomerPictureByTypeIdAndStatus(Long pictureTypeId, Long customerId) {
		CustomerPictureModel customerPictureModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPictureModel.class);
		criteria.add( Restrictions.eq("relationPictureTypeModel.pictureTypeId", pictureTypeId) );
		criteria.add( Restrictions.eq("relationCustomerModel.customerId", customerId) );
		criteria.add( Restrictions.ne("discrepant", Boolean.TRUE));
		List<CustomerPictureModel> list = getHibernateTemplate().findByCriteria(criteria);

		if(list != null && !list.isEmpty()) {

			customerPictureModel = list.get(0);
		}

		return customerPictureModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inov8.microbank.server.dao.customermodule.CustomerPictureDAO#
	 * getAllCustomerPictures(java.lang.Long)
	 */
	public List<CustomerPictureModel> getAllCustomerPictures(Long customerId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPictureModel.class);
		criteria.add( Restrictions.eq("relationCustomerModel.customerId", customerId) );
		List<CustomerPictureModel> list = getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
	
	public List<CustomerPictureModel> getDiscrepantCustomerPictures(Long customerId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPictureModel.class);
		criteria.add( Restrictions.eq("relationCustomerModel.customerId", customerId) );
		criteria.add( Restrictions.eq("applicantTypeId", ApplicantTypeConstants.APPLICANT_TYPE_1));
		List<CustomerPictureModel> list = getHibernateTemplate().findByCriteria(criteria);
		return list;
	}
	
	
	public List<CustomerPictureModel> getAllRetailerContactPictures(Long retailerContactId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPictureModel.class);
		criteria.add( Restrictions.eq("retailerContactModel.retailerContactId", retailerContactId) );
		List<CustomerPictureModel> list = getHibernateTemplate().findByCriteria(criteria);
		return list;
	}

	@Override
	public Boolean isCustomerIdExists(Long customerId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerPictureModel.class);
		criteria.add(Restrictions.eq("relationCustomerModel.customerId", customerId));
		List<CustomerPictureModel> list = this.getHibernateTemplate().findByCriteria(criteria);
		if(list != null && list.size()!=0) {
			return Boolean.TRUE;
		}
		else
		{
			return Boolean.FALSE;
		}
	}

	@Override
	public void updateCustomerPictureModel(Long pictureTypeId, byte[] picture, Long customerId) {
		Date updatedOn = new Date();
		logger.info("Updating Picture in CUSTOMER_PICTURE");
		String hql = "update CustomerPictureModel set picture=?, updatedOn=? where relationCustomerModel.customerId=? and relationPictureTypeModel.pictureTypeId=?";
		int updateCount = getHibernateTemplate().bulkUpdate(hql, picture, updatedOn, customerId,pictureTypeId);
		logger.info("Total number of records updated in CUSTOMER_PICTURE : " + updateCount);
	}
}
