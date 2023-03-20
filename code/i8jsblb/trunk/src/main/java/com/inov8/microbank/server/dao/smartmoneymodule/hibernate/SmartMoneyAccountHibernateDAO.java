package com.inov8.microbank.server.dao.smartmoneymodule.hibernate;

import java.text.SimpleDateFormat;
import java.util.*;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.util.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author Asad Hayat
 * @version 1.0
 */


public class SmartMoneyAccountHibernateDAO
    extends BaseHibernateDAO<SmartMoneyAccountModel, Long, SmartMoneyAccountDAO>
    implements SmartMoneyAccountDAO
{
	
	public List<SmartMoneyAccountModel> loadOLASmartMoneyAccount( Long customerId )
	  {
	    String hql = "FROM SmartMoneyAccountModel sma WHERE sma.active = true"
	        + " AND sma.relationBankIdBankModel.relationFinancialIntegrationIdFinancialIntegrationModel.financialIntegrationId = "
	        + FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION ;
	    
	        if( customerId != null )
			{
				hql += " AND sma.relationCustomerIdCustomerModel.customerId = " + customerId;
			}
		

	    return this.getHibernateTemplate().find(hql) ;

	  }
	
	
	/**
	 * 
	 * @author Jawwad Farooq
	 * @date December 2008
	 * 
	 */
	public List<SmartMoneyAccountModel> loadOLASmartMoneyAccount( Long retailerContactId, Long distributorContactId)
	  {
	   	    return this.loadOLASmartMoneyAccount( retailerContactId, distributorContactId, null);
	  }

	public List<SmartMoneyAccountModel> loadOLASmartMoneyAccount( Long retailerContactId, Long distributorContactId, Long handlerId )
	  {
	    String hql = "FROM SmartMoneyAccountModel sma WHERE sma.active = true"
	        + " AND sma.relationBankIdBankModel.active = true"
	        + " AND sma.relationBankIdBankModel.relationFinancialIntegrationIdFinancialIntegrationModel.financialIntegrationId = "
	        + FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION ;
	    
	        if( retailerContactId != null )
			{
				hql += " AND sma.relationRetailerContactIdRetailerContactModel.retailerContactId = " + retailerContactId
				+ " AND sma.relationRetailerContactIdRetailerContactModel.active = true " ;
			}
			else if( distributorContactId != null )
			{
				hql += " AND sma.relationDistributorContactIdDistributorContactModel.distributorContactId = " + distributorContactId
				+ " AND sma.relationDistributorContactIdDistributorContactModel.active = true " ;
			}
			else if( handlerId != null )
			{
				hql += " AND sma.relationHandlerIdHandlerModel.handlerId = " + handlerId
				+ " AND sma.relationHandlerIdHandlerModel.active = true " ;
			}

	    return this.getHibernateTemplate().find(hql) ;

	  }
	
	
	/**
	 * Author Maqsood Shahzad
	 */
	
	public CustomList<SmartMoneyAccountModel> loadCustomerSmartMoneyAccountByHQL( SmartMoneyAccountModel smartMoneyAccountModel )
	  {
		
		Long customerId = smartMoneyAccountModel.getCustomerId();
	    String hql = "FROM SmartMoneyAccountModel sma WHERE sma.active = true";
//	        + " AND sma.relationBankIdBankModel.active = true";
//	        + " AND sma.relationBankIdBankModel.relationFinancialIntegrationIdFinancialIntegrationModel.financialIntegrationId = "
//	        + FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION ;
	    
	        if( customerId != null )
			{
				hql += " AND sma.relationCustomerIdCustomerModel.customerId = " + customerId;
//				+ " AND sma.relationCustomerIdCustomerModel.active = true " ;
			}
			if(smartMoneyAccountModel.getPaymentModeId() != null)
				hql += " AND sma.relationPaymentModeIdPaymentModeModel.paymentModeId = " + smartMoneyAccountModel.getPaymentModeId();

	    CustomList<SmartMoneyAccountModel> customList =  new CustomList(this.getHibernateTemplate().find(hql)) ;
	    return customList;

	  }
	

	/**
	 * 
	 * @author Jawwad Farooq
	 * @date December 2008
	 * 
	 */
	public List<SmartMoneyAccountModel> loadOLASMAForRetOrDistHead( Long retailerId, Long distributorId )
	  {
	    String hql = "FROM SmartMoneyAccountModel sma WHERE sma.active = true"
	        + " AND sma.relationBankIdBankModel.active = true"
	        + " AND sma.relationBankIdBankModel.relationFinancialIntegrationIdFinancialIntegrationModel.financialIntegrationId = "
	        + FinancialInstitutionConstants.OLA_FINANCIAL_INSTITUTION ;
	    
	        if( retailerId != null )
			{
				hql += " AND sma.relationRetailerContactIdRetailerContactModel.relationRetailerIdRetailerModel.retailerId = " + retailerId
				+ " AND sma.relationRetailerContactIdRetailerContactModel.head = true "
				+ " AND sma.relationRetailerContactIdRetailerContactModel.active = true " ;
			}
			else if( distributorId != null )
			{
				hql += " AND sma.relationDistributorContactIdDistributorContactModel.relationDistributorIdDistributorModel.distributorId = " + distributorId
				+ " AND sma.relationDistributorContactIdDistributorContactModel.head = true "
				+ " AND sma.relationDistributorContactIdDistributorContactModel.active = true " ;
			}

	    return this.getHibernateTemplate().find(hql) ;

	  }
	
	
	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.smartmoneymodule.SmartMoneyAccountDAO#getSmartMoneyAccountByWalkinCustomerId(com.inov8.microbank.common.model.SmartMoneyAccountModel)
	 */
	public SmartMoneyAccountModel getSmartMoneyAccountByWalkinCustomerId(SmartMoneyAccountModel smartMoneyAccountModel) {

		Map<String, Object> criterionMap = new HashMap<String, Object>(0);
		criterionMap.put("active", Boolean.TRUE);
		criterionMap.put("deleted", Boolean.FALSE);
		criterionMap.put("walkinCustomerModel.walkinCustomerId", smartMoneyAccountModel.getWalkinCustomerModel().getWalkinCustomerId());
		
		Criterion criterion = Restrictions.allEq(criterionMap);
		
		CustomList<SmartMoneyAccountModel> customList = super.findByCriteria(criterion);
		
		if(customList != null && !customList.getResultsetList().isEmpty()) {
			return customList.getResultsetList().get(0);
		}
		
		return null;
	}

	@Override
	public int updateSmartMoneyAccountModelToCloseAccount(SmartMoneyAccountModel smartMoneyAccountModel,Boolean isClosedSetteled) {

		String queryPart = null;
		Long paymentModeId = smartMoneyAccountModel.getPaymentModeId();
		Long smartMoneyAccountId = smartMoneyAccountModel.getSmartMoneyAccountId();
		CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
		String updatedOn = createNewDateFormat.formatDate(new Date());
		if(isClosedSetteled && paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
			queryPart = ",is_closed_setteled = " + 1L;
		else if(!isClosedSetteled && paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
			queryPart = ",is_closed_unsetteled = " + 1L;
		else
			queryPart = ",is_closed_unsetteled = " + 1L+",is_closed_setteled = "+1L;
		String query = "UPDATE SMART_MONEY_ACCOUNT set IS_ACTIVE = 0,REGISTRATION_STATE_ID="+RegistrationStateConstantsInterface.CLOSED
				+",ACCOUNT_STATE_ID="+AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED
				+",STATUS_ID="+OlaStatusConstants.ACCOUNT_STATUS_CLOSED
				+",UPDATED_ON= '" + updatedOn +"'"
				+",UPDATED_BY= '" + smartMoneyAccountModel.getUpdatedBy() + "'"
				+queryPart
				+ " WHERE SMART_MONEY_ACCOUNT_ID = " + smartMoneyAccountId;

		int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();

		query = "UPDATE CUSTOMER SET UPDATED_BY = '" + UserUtils.getCurrentUser().getAppUserId() + "'" +
				",UPDATED_ON = '" + updatedOn + "'" +
				" WHERE  CUSTOMER_ID = " + smartMoneyAccountModel.getCustomerId();

		int rows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();
		return rows;
	}

	@Override
	public int updateSmartMoneyAccountModelToCloseAgentAccount(SmartMoneyAccountModel smartMoneyAccountModel, Boolean isClosedSetteled) {
		String queryPart = null;
		Long paymentModeId = smartMoneyAccountModel.getPaymentModeId();
		Long smartMoneyAccountId = smartMoneyAccountModel.getSmartMoneyAccountId();
		CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
		String updatedOn = createNewDateFormat.formatDate(new Date());
		if(isClosedSetteled && paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
			queryPart = ",is_closed_setteled = " + 1L;
		else if(!isClosedSetteled && paymentModeId.equals(PaymentModeConstantsInterface.BRANCHLESS_BANKING_ACCOUNT))
			queryPart = ",is_closed_unsetteled = " + 1L;
		else
			queryPart = ",is_closed_unsetteled = " + 1L+",is_closed_setteled = "+1L;
		String query = "UPDATE SMART_MONEY_ACCOUNT set IS_ACTIVE = 0,REGISTRATION_STATE_ID="+RegistrationStateConstantsInterface.CLOSED
				+",ACCOUNT_STATE_ID="+AccountStateConstantsInterface.ACCOUNT_STATE_CLOSED
				+",STATUS_ID="+OlaStatusConstants.ACCOUNT_STATUS_CLOSED
				+",UPDATED_ON= '" + updatedOn +"'"
				+",UPDATED_BY= '" + smartMoneyAccountModel.getUpdatedBy() + "'"
				+queryPart
				+ " WHERE SMART_MONEY_ACCOUNT_ID = " + smartMoneyAccountId;

		int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();

		query = "UPDATE RETAILER_CONTACT SET UPDATED_BY = '" + UserUtils.getCurrentUser().getAppUserId() + "'" +
				",UPDATED_ON = '" + updatedOn + "'" +
				" WHERE  RETAILER_CONTACT_ID = " + smartMoneyAccountModel.getRetailerContactId();

		int rows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();
		return rows;
	}

	@Override
	public int updateSmartMoneyAccountModelToLockUnlockAccount(SmartMoneyAccountModel smartMoneyAccountModel)
	{
		String query = "UPDATE SMART_MONEY_ACCOUNT set STATUS_ID="+smartMoneyAccountModel.getStatusId()
				+" WHERE SMART_MONEY_ACCOUNT_ID = " + smartMoneyAccountModel.getSmartMoneyAccountId();

		int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();
		return updatedRows;
	}


	@Override
	public void updateSmartMoneyAccountCardTypeId(SmartMoneyAccountModel model) throws FrameworkCheckedException {
		String query = "UPDATE SMART_MONEY_ACCOUNT set CARD_PRODUCT_TYPE_ID ="+model.getCardProdId()
				+" WHERE SMART_MONEY_ACCOUNT_ID = " + model.getSmartMoneyAccountId();

		int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();

	}

	@Override
	public List<SmartMoneyAccountModel> getLastClosedSMAAccount(SmartMoneyAccountModel smartMoneyAccountModel)
	{
		String hql = "FROM SmartMoneyAccountModel sma WHERE sma.active = false"
				+ " AND sma.relationPaymentModeIdPaymentModeModel.paymentModeId= "+smartMoneyAccountModel.getPaymentModeId()
				+"  AND sma.relationCustomerIdCustomerModel.customerId = " + smartMoneyAccountModel.getCustomerId()
				+" order by sma.updatedOn desc";

		return this.getHibernateTemplate().find(hql) ;
	}

	@Override
	public List<SmartMoneyAccountModel> loadSmartMoneyAccountByIsOptasiaDebitBlocked() throws FrameworkCheckedException {
		String hql = "FROM SmartMoneyAccountModel sma WHERE sma.isOptasiaDebitBlocked = true";

		return this.getHibernateTemplate().find(hql) ;
//		StringBuilder sb = new StringBuilder();
//		Date date = new Date();
//		sb.append("SELECT * FROM LOAN ");
//		sb.append("WHERE IS_COMPLETED =1");
//		sb.append(" AND IS_INTIMATED =0 ");
//		List<AdvanceSalaryLoanModel> list = (List<AdvanceSalaryLoanModel>) jdbcTemplate.query(sb.toString(),new AdvanceSalaryLoanModel());
//		return list;
	}
}
