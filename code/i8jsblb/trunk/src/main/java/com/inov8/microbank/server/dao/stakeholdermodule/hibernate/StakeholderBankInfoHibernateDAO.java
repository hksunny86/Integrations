package com.inov8.microbank.server.dao.stakeholdermodule.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.StakeholderBankInfoModel;
import com.inov8.microbank.common.util.BankConstantsInterface;
import com.inov8.microbank.common.util.CommissionConstantsInterface;
import com.inov8.microbank.server.dao.stakeholdermodule.StakeholderBankInfoDAO;

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

public class StakeholderBankInfoHibernateDAO extends BaseHibernateDAO<StakeholderBankInfoModel, Long, StakeholderBankInfoDAO> implements StakeholderBankInfoDAO {
  
	public StakeholderBankInfoHibernateDAO() {}
	
	  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(AppUserModel appUserModel) throws FrameworkCheckedException {

			StringBuilder query = new StringBuilder();

			query.append("select sbi.accountNo from StakeholderBankInfoModel sbi, RetailerContactModel rc, RetailerModel r, AppUserModel au ");
			query.append("where ");
			query.append("r.relationDistributorIdDistributorModel.distributorId  = sbi.relationDistributorIdDistributorModel.distributorId ");
			query.append("and ");
			query.append("rc.relationRetailerIdRetailerModel.retailerId = r.retailerId ");
			query.append("and ");
			query.append("au.relationRetailerContactIdRetailerContactModel.retailerContactId = rc.retailerContactId ");
			query.append("and ");
			query.append("au.mobileNo = ?");

			Object[] params = {appUserModel.getMobileNo()};

			List<String> list = getHibernateTemplate().find(query.toString(), params);
			
			StakeholderBankInfoModel stakeholderBankInfoModel = new StakeholderBankInfoModel();;
			
			if(list != null && !list.isEmpty()) {
			
				stakeholderBankInfoModel.setAccountNo(list.get(0));
			}
			
			return stakeholderBankInfoModel;
		}
	  
	  
	  public StakeholderBankInfoModel loadDistributorStakeholderBankInfoModel(DistributorModel distributorModel) throws FrameworkCheckedException {
		  
		  	StringBuilder query = new StringBuilder();

			query.append("from StakeholderBankInfoModel sbi where sbi.relationDistributorIdDistributorModel.distributorId = ? and sbi.active = ?");

			Object[] params = {distributorModel.getDistributorId(), Boolean.TRUE};

			List<StakeholderBankInfoModel> list = getHibernateTemplate().find(query.toString(), params);
			
			StakeholderBankInfoModel stakeholderBankInfoModel = null;
			
			if(list != null && !list.isEmpty()) {

				stakeholderBankInfoModel = list.get(0);
			}
			
			return stakeholderBankInfoModel;		  
		  
	  }

	  public StakeholderBankInfoModel getStakeholderAccountBankInfoModel(Long accTypeId) throws FrameworkCheckedException {
			
		  StakeholderBankInfoModel stakeholderBankInfoModel = null;
		  StringBuilder query = new StringBuilder();
		  Long idParam = null;
		  	
		  query.append("from StakeholderBankInfoModel sbi where sbi.customerAccountTypeIdModel.customerAccountTypeId = ? and sbi.relationBankIdBankModel.bankId=? and sbi.active = ?");
		  idParam = accTypeId;
		  
		  Object[] params = {idParam, BankConstantsInterface.ASKARI_BANK_ID, Boolean.TRUE};
		  
		  List<StakeholderBankInfoModel> list = getHibernateTemplate().find(query.toString(), params);
		  if(list != null && !list.isEmpty()) {
			  stakeholderBankInfoModel = list.get(0);
		  }
		  
		  return stakeholderBankInfoModel;		  
	  }

	  
	  public List<StakeholderBankInfoModel> getStakeholderBankInfoModelList() throws FrameworkCheckedException {
		  
		    List<StakeholderBankInfoModel> stakeholderBankInfoModelList = new ArrayList<StakeholderBankInfoModel>(0);
		  
		  	StringBuilder query = new StringBuilder();
			query.append("SELECT DISTINCT BANK_INFO_ID FROM ");
			query.append("(SELECT ST1.FROM_BANK_INFO_ID AS BANK_INFO_ID FROM SETTLEMENT_TRANSACTION ST1 union all SELECT ST2.TO_BANK_INFO_ID AS BANK_INFO_ID FROM SETTLEMENT_TRANSACTION ST2)  ");
			query.append("WHERE BANK_INFO_ID > 0 ");
			
			logger.info(query.toString());
			
			Connection connection = getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

			try {
			
				Statement statement = connection.createStatement();
				
				ResultSet resultSet = statement.executeQuery(query.toString());
				
				while(resultSet.next()) {
				
					Long stakeholderBankInfoId = resultSet.getLong(1);
					
					stakeholderBankInfoModelList.add(new StakeholderBankInfoModel(stakeholderBankInfoId));
				}
				
				connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return stakeholderBankInfoModelList;
	  }
	  
		@Override
		public boolean isAccountTypeUnique(String accountNumber, Long accountTypeId) {
			
			Session session = this.getSession();
	        Criteria query = session.createCriteria(StakeholderBankInfoModel.class ) ;

	        Criterion coreAccountCr = Restrictions.eq("relationBankIdBankModel.bankId", CommissionConstantsInterface.BANK_ID.longValue());
	        query.add(coreAccountCr);

	        Criterion accountNumberCr = Restrictions.eq("accountNo", accountNumber);
	        query.add(accountNumberCr);

	        Criterion accountTypeIdNullCr = Restrictions.isNotNull("customerAccountTypeIdModel.customerAccountTypeId");
	        query.add(accountTypeIdNullCr);
	        
	        Criterion accountTypeIdCr ;
	        if(accountTypeId!=null)
	        {
	        	accountTypeIdCr = Restrictions.ne("customerAccountTypeIdModel.customerAccountTypeId", accountTypeId.longValue());
		        query.add(accountTypeIdCr);
	        }else{
	        	accountTypeIdCr = Restrictions.ne("customerAccountTypeIdModel.customerAccountTypeId", 0L);
		        query.add(accountTypeIdCr);
	        }
	        
	        int resultSize	= query.list().size();
			SessionFactoryUtils.releaseSession(session, getSessionFactory());
			return (resultSize==0);
		}
		
		/**
	     * @author AtifHussain
	     */
		@Override
		public List<Object[]> loadOfSettlementAccounts(Long accountType) throws FrameworkCheckedException {
			
			StringBuilder query = new StringBuilder();
			
			query.append("select sbi.stakeholderBankInfoId, sbi.name, sbi.accountNo from StakeholderBankInfoModel sbi ");
			query.append("where sbi.accountNo != '-1' ");
			query.append("and sbi.accountType='OF_SET' ");
			
			if(accountType==2L)//agent
			{
				query.append("and sbi.customerAccountTypeIdModel.customerAccountTypeId!=null ");
				query.append("and sbi.customerAccountTypeIdModel.customerAccountTypeId IN (select customerAccountTypeId from OlaCustomerAccountTypeModel where isCustomerAccountType=FALSE) ");
			}
			else if(accountType==3L)//customer
			{
				query.append("and sbi.customerAccountTypeIdModel.customerAccountTypeId!=null ");
				query.append("and sbi.customerAccountTypeIdModel.customerAccountTypeId IN (select customerAccountTypeId from OlaCustomerAccountTypeModel where isCustomerAccountType=TRUE) ");
			}
			else if(accountType==4L)//internal
			{
				query.append("and (sbi.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId!=50020L ");
				query.append(" OR sbi.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId=null) ");
				query.append("and sbi.customerAccountTypeIdModel.customerAccountTypeId IS NULL ");
				
			}
			else if(accountType==5L)//commission
			{
				query.append("and sbi.relationCommissionStakeholderIdCommissionStakeholderModel.commissionStakeholderId=50020L ");
				query.append("and sbi.relationCmshaccttypeIdCommissionShAcctsTypeModel.cmshacctstypeId=3L ");
			}
			
			query.append("order by sbi.accountNo asc");
			
			List<Object[]> list = getHibernateTemplate().find(query.toString());
			
			return list;
		}
}