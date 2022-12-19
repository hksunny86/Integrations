package com.inov8.microbank.server.dao.customermodule.hibernate;

import java.util.Date;
import java.util.List;

import com.inov8.framework.common.util.CustomList;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.SessionImpl;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.server.dao.customermodule.CustomerDAO;

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

public class CustomerHibernateDAO
    extends
    BaseHibernateDAO<CustomerModel, Long, CustomerDAO>
    implements
    CustomerDAO
{
	@Override
	public List<CustomerModel> findCustomersByAppUserIds(List<Long> inClauseOfAppUserIds) throws FrameworkCheckedException{
		List<CustomerModel> customerList;
		StringBuilder query = new StringBuilder("");
		query.append("select appUser.relationCustomerIdCustomerModel as customer from AppUserModel appUser ")
		.append("where appUser.appUserId in ( :inClauseOfAppUserIds) " );
		String[] paramNames = { "inClauseOfAppUserIds"};
		Object[] values = { inClauseOfAppUserIds};
		try {
//			customerList = getHibernateTemplate().findByNamedParam(query.toString(), paramNames, values);
			SessionImpl session = (SessionImpl)getHibernateTemplate().getSessionFactory().getCurrentSession();
			customerList = session.createQuery(query.toString()).setParameterList("inClauseOfAppUserIds", inClauseOfAppUserIds).list();
			SessionFactoryUtils.releaseSession(session, getSessionFactory());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return customerList;
	}
	
	@Override
	public List<CustomerModel> getCustomerModelListByCustomerIDs(List<Long> customerIdList){
		List<CustomerModel> customerModelList = this.getHibernateTemplate().findByNamedParam("from CustomerModel cm where cm.customerId in (:customerIdList)", "customerIdList", customerIdList );
		return customerModelList;
	}

	@Override
	public int updateCustomerToUpgradeCustomerAccount(Long customerId) {

		CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
		String updatedOn = createNewDateFormat.formatDate(new Date());
		String query = "UPDATE CUSTOMER set CUSTOMER_ACCOUNT_TYPE_ID = "+ CustomerAccountTypeConstants.LEVEL_1
				+ " ,UPDATED_BY = '" + UserUtils.getCurrentUser().getAppUserId() + "'"
				+ " ,UPDATED_ON = '" + updatedOn + "'"
				+ " WHERE CUSTOMER_ID = " + customerId;

		int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();

		return updatedRows;
	}

	@Override
	public CustomerModel loadCustomerModelByCustomerId(Long customerId) {
		CustomerModel customerModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerModel.class);
		criteria.add( Restrictions.eq("customerId", customerId));
		List<CustomerModel> list = getHibernateTemplate().findByCriteria(criteria);


		if(list != null && !list.isEmpty()) {

			customerModel = list.get(0);
		}
		return customerModel;

	}

	@Override
	public CustomerModel loadCustomerModelByMobileNo(String mobileNo) {
		CustomerModel customerModel = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(CustomerModel.class);
		criteria.add( Restrictions.eq("mobileNo", mobileNo));
		List<CustomerModel> list = getHibernateTemplate().findByCriteria(criteria);


		if(list != null && !list.isEmpty()) {

			customerModel = list.get(0);
		}
		return customerModel;

	}

	@Override
	public List<CustomerModel> findCustomerByIsAccountUpdate() {
		String hql = "from CustomerModel cm where cm.accountUpdate = 1" ;
		List<CustomerModel> customerModelList=getHibernateTemplate().find(hql);
		return customerModelList;
	}
}
