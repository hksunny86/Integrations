package com.inov8.ola.server.dao.account.hibernate;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.util.OlaStatusConstants;
import com.inov8.ola.util.CustomerAccountTypeConstants;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.AccountHolderModel;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.AccountsWithStatsListViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.StringUtil;
import com.inov8.ola.integration.vo.OLAVO;
import com.inov8.ola.server.dao.account.AccountDAO;
import com.inov8.ola.util.EncryptionUtil;



public class AccountHibernateDAO  extends
BaseHibernateDAO<AccountModel, Long, AccountDAO>
implements AccountDAO  
{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public List<AccountModel> getAllAccounts( SearchBaseWrapper searchBaseWrapper )
	{
		OLAVO olaVO = (OLAVO)searchBaseWrapper.getObject("OLA") ;
		AccountModel accountModel = (AccountModel)searchBaseWrapper.getBasePersistableModel() ;
		LinkedHashMap<String, SortingOrder> sortingOrderMap = searchBaseWrapper.getSortingOrderMap() ;
		
		Session session = this.getSession();
        Criteria accountCriteria = session.createCriteria( AccountModel.class ) ;
		Criteria accountHolderCriteria = accountCriteria.createCriteria( "relationAccountHolderIdAccountHolderModel" ) ; 
		Criteria statusCriteria = accountCriteria.createCriteria( "relationStatusIdStatusModel" ) ;
		
		
		if( olaVO != null )
		{
			if( olaVO.getFirstName() != null && !olaVO.getFirstName().equals("")  )
			{
				Criterion accountNoCri = Restrictions.ilike("firstName", olaVO.getFirstName()+"%") ;
				accountHolderCriteria.add(accountNoCri) ;
			}
			if( olaVO.getLastName() != null && !olaVO.getLastName().equals("")  )
			{
				Criterion accountNoCri = Restrictions.ilike("lastName", olaVO.getLastName()+"%") ;
				accountHolderCriteria.add(accountNoCri) ;
			}
			if( accountModel.getAccountHolderIdAccountHolderModel().getCnic() != null 
					&& !accountModel.getAccountHolderIdAccountHolderModel().getCnic().equals("")  )
			{
				Criterion accountNoCri = Restrictions.like("cnic", accountModel.getAccountHolderIdAccountHolderModel().getCnic()+"%") ;
				accountHolderCriteria.add(accountNoCri) ;
			}
			if( olaVO.getMobileNumber() != null && !olaVO.getMobileNumber().equals("")  )
			{
				Criterion accountNoCri = Restrictions.like("mobileNumber", olaVO.getMobileNumber()+"%") ;
				accountHolderCriteria.add(accountNoCri) ;
			}
			if( olaVO.getDob() != null && !olaVO.getDob().equals("") )
			{
				Criterion accountNoCri = Restrictions.like("dob", accountModel.getAccountHolderIdAccountHolderModel().getDob()+"%") ;
				accountHolderCriteria.add(accountNoCri) ;
			}		
			if( accountModel.getAccountNumber() != null && !accountModel.getAccountNumber().equals("")  )
			{
				Criterion accountNoCri = Restrictions.like("accountNumber", accountModel.getAccountNumber()+"%") ;
				accountCriteria.add(accountNoCri) ;
			}
			
			if( olaVO.getBalance() != null && !olaVO.getBalance().equals("") )
			{
//				Criterion balanceCri = Restrictions.eq("balance", EncryptionUtil.encryptPin(olaVO.getBalance().toString())) ;
				Criterion balanceCri = Restrictions.eq("balance", olaVO.getBalance().toString()) ;
				accountCriteria.add(balanceCri) ;
			}
		}
		
		if( sortingOrderMap != null )
		{
			Set<String> keys = sortingOrderMap.keySet() ;
			Iterator<String> iterator = keys.iterator();
			
			while( iterator.hasNext() )
			{
				String key = iterator.next();
				SortingOrder sortingOrder = sortingOrderMap.get(key) ;
				
				if( key.equalsIgnoreCase("payingAccNo") )
				{
					if( sortingOrder == SortingOrder.ASC )
						accountCriteria.addOrder( Order.asc("accountNumber") ) ;
					else
						accountCriteria.addOrder( Order.desc("accountNumber") ) ;
					
				} else if(key.equalsIgnoreCase("balance")){
					//balance sorting
				}
				else
				{
					if( sortingOrder == SortingOrder.ASC )
						accountHolderCriteria.addOrder( Order.asc(key) ) ;
					else
						accountHolderCriteria.addOrder( Order.desc(key) ) ;
					
				}				
			}			
		}
		
		
		List<AccountModel> results = accountCriteria.list() ;
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
		return results;
	}
	
	public void init()
	{
		
		try
		{
			 this.getAccountStatsWithRange( new Date(), new Date());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public List<AccountsWithStatsListViewModel> getAccountBalanceStats( Date date )
	{
		Session session = this.getSession();
        Criteria dailyAccountCriteria = session.createCriteria( AccountsWithStatsListViewModel.class ) ;
		
//		if (accountId != null)
//		{
//			Criterion accountNoCri = Restrictions.eq("accountId", accountId);
//			dailyAccountCriteria.add(accountNoCri);
//		}
		if (date != null)
		{
			try
			{
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String dateStr = format.format(date) ; 
//				Date startDate = (Date)format.parse( "2009-10-21" );
				
				
				Criterion accountNoCri = Restrictions.eq("statsDate", dateStr);
				dailyAccountCriteria.add(accountNoCri);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		List<AccountsWithStatsListViewModel> results = dailyAccountCriteria.list();
		SessionFactoryUtils.releaseSession(session, getSessionFactory());

		return results;
		
		
		
		
		
//		
// String hql = "SELECT sm.shipmentId FROM ShipmentModel sm, ProductModel pm
// WHERE pm.productId = ? and"
//	        + " sm.relationProductIdProductModel.productId = pm.productId AND sm.purchaseDate = ( select MIN(smm.purchaseDate) from ShipmentModel smm"
//	        + " where smm.active = true and smm.outstandingCredit >= ?"
//	        + " and smm.relationProductIdProductModel.productId = pm.productId and Decode( smm.expiryDate, null, sysdate , to_date(smm.expiryDate)) >= sysdate ) " ;
//
//	    return this.getHibernateTemplate().find(hql, new Object[]{accountId, date}) ;

	}
	
	
	public List<Object> getAccountStatsWithRange( Date startDate, Date endDate )
	{
//		Criteria dailyAccountCriteria = this.getSession().createCriteria( AccountsWithStatsListViewModel.class ) ;
//		
////		if (accountId != null)
////		{
////			Criterion accountNoCri = Restrictions.eq("accountId", accountId);
////			dailyAccountCriteria.add(accountNoCri);
////		}
//		if (date != null)
//		{
//			try
//			{
//				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//				String dateStr = format.format(date) ; 
////				Date startDate = (Date)format.parse( "2009-10-21" );
//				
//				
//				Criterion accountNoCri = Restrictions.eq("statsDate", dateStr);
//				dailyAccountCriteria.add(accountNoCri);
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//		List<AccountsWithStatsListViewModel> results = dailyAccountCriteria.list();
//
//		return results;
		
		
		
//		"FROM DailyAccountStatsModel WHERE AccountId = ? "
//		" AND statsDate BETWEEN TO_DATE('10/29/2009', 'MM/DD/YYYY') AND TO_DATE('11/03/2009', 'MM/DD/YYYY')"

		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String dateStr = format.format(startDate) ;
		String endDateStr = format.format(endDate) ;
		
//		String hql = "FROM AccountsStatsRangeListViewModel WHERE " +
//			" statsDate BETWEEN TO_DATE('10/29/2009', 'MM/DD/YYYY') AND TO_DATE('11/03/2009', 'MM/DD/YYYY')" ;
		
		
		String hql = "SELECT DISTINCT strtAcc.accountId, strtAcc.startDayBalance, endAcc.endDayBalance, endAcc.accountNumber, strtAcc.balance "
		+ " FROM AccountsStatsRangeListViewModel strtAcc, AccountsStatsRangeListViewModel endAcc " 
		+ " WHERE strtAcc.accountId = endAcc.accountId AND strtAcc.statsDate  =  '" + dateStr
		+ "' AND endAcc.statsDate  =  '" + endDateStr
		+ "' " ;
 
			
//			"FROM ShipmentModel sm, ProductModel pm WHERE pm.productId = ? and"
//	        + " sm.relationProductIdProductModel.productId = pm.productId AND sm.purchaseDate = ( select MIN(smm.purchaseDate) from ShipmentModel smm"
//	        + " where smm.active = true and smm.outstandingCredit >= ?"
//	        + " and smm.relationProductIdProductModel.productId = pm.productId and Decode( smm.expiryDate, null, sysdate , to_date(smm.expiryDate)) >= sysdate ) " ;

	    return this.getHibernateTemplate().find(hql, new Object[]{}) ;
		
		

	}
	/*
	 * Maqsood Shahzad
	 * (non-Javadoc)
	 * @see com.inov8.ola.server.dao.account.AccountDAO#loadAccountAndLock(com.inov8.framework.common.wrapper.BaseWrapper)
	 */

	public BaseWrapper loadAccountAndLock(BaseWrapper baseWrapper) {
		logger.info("Load AccountModel in AccountHibernateDAO.loadAccountAndLock() at Time :: " + new Date());
		AccountModel accountModel = (AccountModel) baseWrapper.getBasePersistableModel();
		baseWrapper.setBasePersistableModel(null);
		AccountModel accountModelLocked = null;
		Session session = null;
		
		try {
			
			session = getSessionFactory().getCurrentSession();
			String queryString = "from AccountModel acModel where accountNumber = :accountNo";
			List<AccountModel> accList = session.createQuery(queryString).setString("accountNo", accountModel.getAccountNumber()).setLockMode("acModel", LockMode.UPGRADE).list();
			
			if (accList != null && accList.size() > 0) {
			
				accountModelLocked = accList.get((0));
				baseWrapper.setBasePersistableModel(accountModelLocked);
			}
			
		} finally {
			
			if(session != null) {
				SessionFactoryUtils.releaseSession(session, getSessionFactory());
			}
		}
		
		//************************ getting latest balance to avoid Hibernate Cache problem on Prod
		if(accountModelLocked != null && accountModelLocked.getAccountNumber() != null){
			accountModelLocked.setBalance(fetchOLABalanceByJDBC(accountModelLocked.getBalance(),null,accountModelLocked.getAccountNumber()));
		}
		logger.info("AccountModel Loaded in AccountHibernateDAO.loadAccountAndLock() at Time :: " + new Date());

		return baseWrapper;	
	}
	
	
	public BaseWrapper loadAccountAndLock(Long PK) {
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		AccountModel accountModel = (AccountModel) this.getHibernateTemplate().get(new AccountModel().getClass(), PK, LockMode.UPGRADE);
		
		baseWrapper.setBasePersistableModel(accountModel);
		
		//************************ getting latest balance to avoid Hibernate Cache problem on Prod
		if(accountModel != null){
			accountModel.setBalance(fetchOLABalanceByJDBC(accountModel.getBalance(),PK,null));
		}
		
		return baseWrapper;	
	}			

	private String fetchOLABalanceByJDBC(String oldBalance, Long accountId, String accountNumber) {
        
		String colName = (accountId != null) ? "account_id" : "account_number";
        Object objValue = (accountId != null) ? accountId: accountNumber;
        String query = "select balance from account where " + colName + " = ?";     
        
    	Map<String,Object> valueMap = jdbcTemplate.queryForMap(query, objValue);
    	String bal = (String) valueMap.get("balance");
    	
    	if(StringUtil.isNullOrEmpty(bal)){
        	logger.error("Fresh Balance is null for ********************** fetchOLABalanceByJDBC "+colName+":"+objValue+"\n\n Returning already loaded balance:"+oldBalance);
        	return oldBalance;
    	}
    	
    	if(!bal.equals(oldBalance)){
        	logger.error("\n\n***********************************************\n"
        					+ "*** Account Cached Balance Scenario occurred ***\n"
        					+ "*** fetchOLABalanceByJDBC "+colName+":"+objValue+"***\n"
        					+ "***********************************************\n");
    	}
    	
		return bal;	

	}			

	 
	public AccountModel getAccountModelByCNIC(String cnic){
		AccountModel accountModel = null;
		try{
			String hql = " FROM AccountHolderModel ah WHERE ah.cnic = '" + cnic + "'"; 
//					+ " WHERE ah.firstName = 'CustomerOne'";
////					+ " AND ah.cnic = " + cnic ;
			
			List<AccountHolderModel> acctHolderModelsList =  (List<AccountHolderModel>)this.getHibernateTemplate().find(hql);
			
			if( ! CollectionUtils.isEmpty(acctHolderModelsList)){
				List<AccountModel> accountModelList = (List<AccountModel>)acctHolderModelsList.get(0).getAccountHolderIdAccountModelList();
				accountModel = accountModelList.get(0);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return accountModel;
	}

	@Override
	public AccountModel getAccountModelByCNICAndMobile(String cnic, String mobileNo) {
		AccountModel accountModel = null;
		try{
			String hql = "from AccountModel am inner join fetch am.relationAccountHolderIdAccountHolderModel where am.relationAccountHolderIdAccountHolderModel.cnic = '" +
					cnic + "'"
					+ " AND am.relationAccountHolderIdAccountHolderModel.mobileNumber = '" + mobileNo + "'";

			List<AccountModel> list =  (List<AccountModel>)this.getHibernateTemplate().find(hql);

			if( ! CollectionUtils.isEmpty(list)){
				accountModel = list.get(0);
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return accountModel;
	}

	/**
	 * @author Abu Turab
	 * @param cnicList
	 * @return
	 */
	@Override
	public List<AccountModel> getAccountModelListByCNICs(List<String> cnicList){
	//	List<AccountModel> accountModelList = this.getHibernateTemplate().findByNamedParam("from AccountModel am where am.relationAccountHolderIdAccountHolderModel.cnic in (:cnicList)", "cnicList", cnicList );
		//return accountModelList;
		String hql = "from AccountModel am inner join fetch am.relationAccountHolderIdAccountHolderModel " +
				"where am.relationAccountHolderIdAccountHolderModel.cnic in (:cnicList) and am.relationStatusIdStatusModel.statusId not in ("
				+ OlaStatusConstants.ACCOUNT_STATUS_CLOSED  + "," + OlaStatusConstants.ACCOUNT_STATUS_DORMANT  + "," + OlaStatusConstants.ACCOUNT_STATUS_DELETED  + ")" +
				" and am.customerAccountTypeModel.customerAccountTypeId in ( "
				+ CustomerAccountTypeConstants.RETAILER + "," + CustomerAccountTypeConstants.LEVEL_1  + "," + CustomerAccountTypeConstants.LEVEL_0  + ")";
		List<AccountModel> accountModelList = this.getHibernateTemplate().findByNamedParam(hql, "cnicList", cnicList );
		return accountModelList;
	}

	
	public int updateAccountBalanceByAccountId(Long accountId, Double amount, Long ledgerId, boolean isCredit){
		int affectedRows = 0;
		String hql = "";
		
		if (isCredit) {
			hql = " update AccountModel am set am.balance = balance + ? , am.txLedgerId = ? , am.updatedOn = ? WHERE am.accountId = ?";  
		}else{
			hql = " update AccountModel am set am.balance = balance - ? , am.txLedgerId = ? , am.updatedOn = ? WHERE am.accountId = ?"; 
		}

		Object[] values = { String.valueOf(amount), ledgerId, new Date(), accountId };

		affectedRows = this.getHibernateTemplate().bulkUpdate(hql, values);

		return affectedRows;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<LabelValueBean> loadAccountIdsAndTitles( Long olaCustomerAccountTypeId )
	{
	    List<LabelValueBean> accountIdsAndTitles = null;
	    String hql = "SELECT accountModel.accountId, accountModel.relationAccountHolderIdAccountHolderModel.firstName FROM AccountModel as accountModel WHERE accountModel.customerAccountTypeModel.customerAccountTypeId=?";
        List<Object[]> list = getHibernateTemplate().find( hql, olaCustomerAccountTypeId );
	    if( list != null && !list.isEmpty() )
	    {
	        accountIdsAndTitles = new ArrayList<>( list.size() );
	        for( Object[] accountIdAndTitle : list )
	        {
	            accountIdsAndTitles.add( new LabelValueBean( accountIdAndTitle[1].toString(), accountIdAndTitle[0].toString() ) );
	        }
	    }
	    return accountIdsAndTitles;
	}
	
	@Override
	public AccountModel saveOrUpdate(AccountModel accountModel) {
						
		this.getHibernateTemplate().saveOrUpdate(accountModel);
		
		this.getHibernateTemplate().flush();
		
		this.getHibernateTemplate().evict(accountModel);
		
		return accountModel;
		
	}

	@Override
	public Long getAccountIdByCnic(String cnic, Long accountTypeId){
		Long accountId;
		String queryString="select a from AccountModel a where a.relationAccountHolderIdAccountHolderModel.cnic =:cnic and a.customerAccountTypeModel.customerAccountTypeId=:accountTypeId" ;
		Query query=getSession().createQuery(queryString);
		query.setParameter("cnic",cnic);
		query.setParameter("accountTypeId",accountTypeId);
		List<AccountModel> accountModelList= query.list();

		return accountModelList.get(0).getAccountId();

	}

	@Override
	public Long getAccountIdByCustomerAccountType(String cnic,Long customerAccountTypeId){

		String where=null;
		if(customerAccountTypeId == 4)
			where = " and ac.customerAccountTypeModel.customerAccountTypeId = "+customerAccountTypeId;
		else
			where = " and ac.customerAccountTypeModel.customerAccountTypeId != "+customerAccountTypeId;


		String hql = "select ac.accountId FROM AccountModel ac,AccountHolderModel ah WHERE ac.relationAccountHolderIdAccountHolderModel.accountHolderId = ah.accountHolderId AND " +
				"ah.cnic = '" + cnic + "'" + where;

		List<Long> list= this.getHibernateTemplate().find(hql);

		if(!CollectionUtils.isEmpty(list))
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public AccountModel getAccountModelByCnicAndCustomerAccountTypeAndStatusId(String cnic, Long customerAccountTypeId, Long statusId) {

		String where =  " and ac.customerAccountTypeModel.customerAccountTypeId = "+customerAccountTypeId;

		String hql = "SELECT ac FROM AccountModel as ac " +
				"inner join ac.relationAccountHolderIdAccountHolderModel as ah where " +
				"ah.cnic = '" + cnic + "'" + " AND ac.relationStatusIdStatusModel.statusId= "+statusId +
				" and ah.active = " + Boolean.TRUE + where;

		List<AccountModel> list= this.getHibernateTemplate().find(hql);

		if(!CollectionUtils.isEmpty(list))
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public AccountModel getLastClosedAccountModel(String cnic, Long customerAccountTypeId) {

		String where =  " and ac.customerAccountTypeModel.customerAccountTypeId = "+customerAccountTypeId;

		String hql = "SELECT ac FROM AccountModel as ac " +
				"inner join ac.relationAccountHolderIdAccountHolderModel as ah where " +
				"ah.cnic = '" + cnic + "'" + " AND ac.relationStatusIdStatusModel.statusId= "+OlaStatusConstants.ACCOUNT_STATUS_CLOSED +
				" and ah.active = " + Boolean.FALSE + where + " order by ac.updatedOn DESC";

		List<AccountModel> list= this.getHibernateTemplate().find(hql);

		if(!CollectionUtils.isEmpty(list))
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public int updateAccountModelToCloseAccount(AccountModel accountModel) throws FrameworkCheckedException {
		CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
		String updatedOn = createNewDateFormat.formatDate(new Date());
		String query = "UPDATE ACCOUNT set STATUS_ID = " + OlaStatusConstants.ACCOUNT_STATUS_CLOSED
				+ ",UPDATED_ON= '" + updatedOn + "'"
				+ " WHERE ACCOUNT_ID = " + accountModel.getAccountId();

		int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
		this.getHibernateTemplate().flush();

		return updatedRows;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
