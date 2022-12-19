package com.inov8.ola.server.dao.accountholder.hibernate;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.util.Date;
import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.CreateNewDateFormat;
import org.apache.log4j.Logger;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.ola.server.dao.accountholder.AccountHolderDAO;



public class AccountHolderHibernateDAO  extends
BaseHibernateDAO<AccountHolderModel, Long, AccountHolderDAO>
implements AccountHolderDAO  {
	private static final Logger LOGGER = Logger.getLogger( AccountHolderHibernateDAO.class );

	@Override
	public void updateCnicAndMobileNo(String oldCnic, String newCnic, String newMobileNo)
	{
		LOGGER.info("Updating CNIC and Mobile # in ACCOUNT_HOLDER");
		String hql = "update AccountHolderModel set cnic=?, mobileNumber=? where cnic=?";
		int updateCount = getHibernateTemplate().bulkUpdate(hql, newCnic, newMobileNo, oldCnic);
		LOGGER.info("Total number of records updated in ACCOUNT_HOLDER : " + updateCount);
	}

	@Override
	public void updateMobileNo(String cnic, String mobileNo)
	{
		LOGGER.info("Updating Mobile # in ACCOUNT_HOLDER");
		String hql = "update AccountHolderModel set mobileNumber=? where cnic=?";
		int updateCount = getHibernateTemplate().bulkUpdate(hql, mobileNo, cnic);
		LOGGER.info("Total number of records updated in ACCOUNT_HOLDER : " + updateCount);
	}
	
	@Override
	public void updateCnic(String cnic, String mobileNo)
	{
		LOGGER.info("Updating CNIC # in ACCOUNT_HOLDER");
		String hql = "update AccountHolderModel set cnic=? where mobileNumber=?";
		int updateCount = getHibernateTemplate().bulkUpdate(hql, cnic, mobileNo);
		LOGGER.info("Total number of records updated in ACCOUNT_HOLDER : " + updateCount);
	}

	@Override
	public int updateAccountHolderModelToCloseAccount(AccountHolderModel accountHolderModelModel) throws FrameworkCheckedException {
		CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
		String updatedOn = createNewDateFormat.formatDate(new Date());
		String query = "UPDATE ACCOUNT_HOLDER set IS_ACTIVE = 0,UPDATED_ON= '" + updatedOn + "'"
				+ " WHERE ACCOUNT_HOLDER_ID = " + accountHolderModelModel.getAccountHolderId();

		int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();

		return updatedRows;
	}

	@Override
	public List<AccountHolderModel> getAccountHolderModelListByAccountIds(List<Long> accountIds){
		String hql = "select am.relationAccountHolderIdAccountHolderModel from AccountModel am where am.accountId in (:accountIds)";
		List<AccountHolderModel> accountHolderModelList = this.getHibernateTemplate().findByNamedParam(hql, "accountIds", accountIds );
		
		return accountHolderModelList;
	}
}
