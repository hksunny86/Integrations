package com.inov8.microbank.demographics.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.demographics.dao.DemographicsDAO;
import com.inov8.microbank.demographics.model.DemographicsModel;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;


public class DemographicsHibernateDAO extends BaseHibernateDAO<DemographicsModel, Long, DemographicsDAO>
		implements DemographicsDAO {
	@Override
	public DemographicsModel loadDemographicsModel(String mobileNo) {
		StringBuilder queryStr = new StringBuilder();
		queryStr.append("from DemographicsModel dm ");
		queryStr.append("where dm.appUserIdAppUserModel.mobileNo =:mobileNo");

		Query query = getSession().createQuery(queryStr.toString());
		query.setParameter("mobileNo", mobileNo);

		return (DemographicsModel) query.uniqueResult();
	}

	public DemographicsModel loadDemographicsModel(long appUserId){
		StringBuilder queryStr = new StringBuilder();
		queryStr.append("from DemographicsModel dm ");
		queryStr.append("where dm.appUserIdAppUserModel.appUserId =:appUserId");

		Query query = getSession().createQuery(queryStr.toString());
		query.setParameter("appUserId", appUserId);

		return (DemographicsModel) query.uniqueResult();
	}
	
	public List<DemographicsModel> loadDistinctOS()  throws FrameworkCheckedException
	{
		String hql = "select distinct(os) from DemographicsModel";
		
		List<DemographicsModel> customerDemographicsModelList = this.getHibernateTemplate().find(hql, new Object[]{}) ;
		
		return customerDemographicsModelList;
	}
	
	public List<String> loadDistinctOSVersion(String os)  throws FrameworkCheckedException
	{
		String hql = "select distinct(osVerison) from DemographicsModel where os='" + os + "'";
		
		List<String> osVersionList = this.getHibernateTemplate().find(hql, new Object[]{}) ;
		
		return osVersionList;
	}
	
	public List<String> loadDistinctVendor(String os)  throws FrameworkCheckedException
	{
		String hql = "select distinct(vendor) from DemographicsModel where os='" + os + "'";
		
		List<String> vendorList = this.getHibernateTemplate().find(hql, new Object[]{}) ;
		
		return vendorList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DemographicsModel loadDeviceKeyByAppUserId(Long appUserId) throws FrameworkCheckedException
	{
		String hql = "from DemographicsModel cdm where cdm.relationAppUserIdAppUserModel.appUserId = ?";
		List<DemographicsModel> deviceKeys = this.getHibernateTemplate().find(hql, new Object[]{appUserId});
		if(CollectionUtils.isNotEmpty(deviceKeys)){
			return deviceKeys.get(0);
		}
		return null;
	}
}
