package com.inov8.ola.server.dao.ledger.hibernate;

/** 	
 * @author 					Jawwad Farooq
 * Project Name: 			OLA
 * Creation Date: 			November 2008  			
 * Description:				
 */


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.springframework.jdbc.core.JdbcTemplate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.AccountModel;
import com.inov8.integration.common.model.LedgerModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.SupplierProcessingStatusConstants;
import com.inov8.ola.server.dao.ledger.LedgerDAO;
import com.inov8.ola.util.ReasonConstants;
import com.inov8.ola.util.TransactionTypeConstants;



public class LedgerHibernateDAO  extends
BaseHibernateDAO<LedgerModel, Long, LedgerDAO>
implements LedgerDAO  {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public Double getDailyConsumedBalance(Long accountId,Long transactionTypeId,Date date, Long handlerId)
	{
		logger.info("Start of LedgerHibernateDAO.getDailyConsumedBalance() at Time :: " + new Date());
		try{
			DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
			String dateStr = format.format(date) ;
			String accountColumn = "";
			if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
				accountColumn = "L.FROM_ACCOUNT_ID";
			}else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
				accountColumn = "L.TO_ACCOUNT_ID";
			}
			String sql = "SELECT SUM(L.TRANSACTION_AMOUNT)"
					+ " FROM Ledger L"
					+ " INNER JOIN REASON R "
					+ " ON R.REASON_ID = L.REASON_ID"
					+ " WHERE " + accountColumn + " = " + accountId
					+ " AND L.REASON_ID NOT IN (" + ReasonConstants.REVERSAL + "," + ReasonConstants.SETTLEMENT
							+ "," + ReasonConstants.ROLLBACK_WALKIN_CUSTOMER + "," + ReasonConstants.REVERSE_BILL_PAYMENT + ","+ReasonConstants.FUND_CUSTOMER_BB_CORE_AC + ")"
					+ " AND (L.IS_REVERSAL IS NULL OR L.IS_REVERSAL = " + 0L + ")"
					+ " AND L.TRANSACTION_TIME >= '" + dateStr + "'";
			// Handler Limit Changes
			if(handlerId != null && handlerId.longValue() > 0){
				sql += " AND L.HANDLER_ID = "+ handlerId
						+ " AND L.EXCLUDE_LIMIT_HANDLER <> "+ 1L;
			}else{
				sql += " AND L.EXCLUDE_LIMIT <> "+ 1L;
			}
			logger.info("Query to calculate LedgerHibernateDAO.getDailyConsumedBalance() :: " + sql);
			if(accountId != null){
				List<Object> objs = this.jdbcTemplate.queryForList(sql,Object.class);
				if(objs != null && objs.size() > 0){
					Double result = null;
					if(objs.get(0) != null)
					{
						BigDecimal bigDecimal = (BigDecimal)objs.get(0);
						if(bigDecimal==null) {
							result = Double.valueOf(0d);
						}
						result = bigDecimal.doubleValue();
					}
					if(result == null){
						result = Double.valueOf(0d);
					}
					return result;
				}
			}
		}catch(Exception ex){
			logger.error("No Record Found in LedgerHibernateDAO.getDailyConsumedBalance(),Sor returning null." + ex);
		}
		finally {
			logger.info("End of LedgerHibernateDAO.getDailyConsumedBalance() at Time :: " + new Date());
		}
		return null;
	}
	
	public Double getConsumedBalanceByDateRange(Long accountId,Long transactionTypeId,Date startDate,Date endDate, Long handlerId)
	{
		try{
			logger.info("Start of LedgerHibernateDAO.getConsumedBalanceByDateRange() at Time :: " + new Date());
			LocalDate startingDate = new LocalDate(startDate);
			LocalDate endingDate = new LocalDate(endDate);
			if(Months.monthsBetween(startingDate,endingDate).getMonths()>0){
				logger.info("Yearly limit ignored ,StartDate : "+startDate+"  EndDate : "+endDate );
				return Double.valueOf(0d);
			}
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
			endDate = c.getTime();
			DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
			String dateStr = format.format(startDate) ;
			String endStr = format.format(endDate);
			String accountColumn = "";
			if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
				accountColumn = "L.FROM_ACCOUNT_ID";
			}else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
				accountColumn = "L.TO_ACCOUNT_ID";
			}
			String sql = "SELECT SUM(L.TRANSACTION_AMOUNT)"
					+ " FROM Ledger L"
					+ " INNER JOIN REASON R "
					+ " ON R.REASON_ID = L.REASON_ID"
					+ " WHERE " + accountColumn + " = " + accountId
					+ " AND L.REASON_ID NOT IN (" + ReasonConstants.REVERSAL + "," + ReasonConstants.BULK_PAYMENT + "," + ReasonConstants.SETTLEMENT
					+ "," + ReasonConstants.ROLLBACK_WALKIN_CUSTOMER + "," + ReasonConstants.REVERSE_BILL_PAYMENT + ","+ReasonConstants.FUND_CUSTOMER_BB_CORE_AC + ")"
					+ " AND (L.IS_REVERSAL IS NULL OR L.IS_REVERSAL = " + 0L + ")"
					+ " AND L.TRANSACTION_TIME >= '" + dateStr + "'"
					+ " AND L.TRANSACTION_TIME < '" + endStr + "'";
			// Handler Limit Changes
			if(handlerId != null && handlerId.longValue() > 0){
				sql += " AND L.HANDLER_ID = "+ handlerId
						+ " AND L.EXCLUDE_LIMIT_HANDLER <> "+ 1L;
			}else{
				sql += " AND L.EXCLUDE_LIMIT <>"+ 1L;
			}
			logger.info("Query to calculate LedgerHibernateDAO.getConsumedBalanceByDateRange() :: " + sql);
			if(accountId != null)
			{
				List<Object> objs = this.jdbcTemplate.queryForList(sql,Object.class);
				if(objs != null && objs.size() > 0){
//					Double result = (Double)objs.get(0);
					Double result = null;
					if(objs.get(0) != null)
					{
						BigDecimal bigDecimal = (BigDecimal)objs.get(0);
						if(bigDecimal==null){
							result = Double.valueOf(0d);
						}
						result = bigDecimal.doubleValue();
					}
					if(result == null){
						result = Double.valueOf(0d);
					}
					return result;
				}
			}
		}catch(Exception ex){
			logger.error("Error in LedgerHibernateDAO.getConsumedBalanceByDateRange() :: " + ex);
		}
		finally {
			logger.info("End of LedgerHibernateDAO.getConsumedBalanceByDateRange() at Time :: " + new Date());
		}
		return null;
	}
	
	public Double getWalkinCustomerConsumedBalanceByDateRange(Long accountId,Long transactionTypeId,
											Date startDate,Date endDate, boolean excludeInProcessTx)
	{
		logger.info("Start of LedgerHibernateDAO.getWalkinCustomerConsumedBalanceByDateRange() at Time :: " + new Date());
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
			endDate = c.getTime();
			DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
			String dateStr = format.format(startDate) ;
			String endStr = format.format(endDate);

			String accountColumn = "";
			if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
				accountColumn = "l.accountId";
			}else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
				accountColumn = "l.toAccountId";
			}
			
			String joinTable = "";
			String joinCondition = "";
			if(excludeInProcessTx){
				
				joinTable = ", TransactionDetailMasterModel tdm";
				
				joinCondition = " AND l.microbankTransactionCode = tdm.transactionCode"
							  + " AND tdm.supProcessingStatusId <> "+SupplierProcessingStatusConstants.IN_PROGRESS;
			}
			
			String hql = "SELECT SUM(l.transactionAmount)"
					+ " FROM LedgerModel l"+joinTable
					+ " INNER JOIN l.relationReasonIdReasonModel reason"
					+ " WHERE " + accountColumn + " = " + accountId
					+ joinCondition
					+ " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
					+ " AND l.excludeLimit <> "+ Boolean.TRUE
					+ " AND reason.reasonId  in ( "+ ReasonConstants.CUSTOMER_ACCOUNT_TO_CASH 
						+ ", " + ReasonConstants.CUSTOMER_CASH_TO_CASH 
						+ ", " + ReasonConstants.CNIC_TO_CORE_AC
						+ ", " + ReasonConstants.CNIC_TO_BB_AC
						+ ", " + ReasonConstants.BULK_PAYMENT + " ) "
					+ " AND l.transactionTime >='" + dateStr+ "' " 
					+ " AND l.transactionTime <'" + endStr+ "' ";
			List<Object> objs = this.getHibernateTemplate().find(hql);
			if(objs != null && objs.size() > 0){
				Double result = (Double)objs.get(0);
				if(result == null){
					result = Double.valueOf(0d);
				}
				return result;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally {
			logger.info("End of LedgerHibernateDAO.getWalkinCustomerConsumedBalanceByDateRange() at Time :: " + new Date());
		}
		return null;
	}
	
	public List<LedgerModel> getLedgerModelByDateRangeAndAccountID(Date fromDate, Date toDate,Long accountId){
		List<LedgerModel> ledgerModels = null;
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();
			DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
			String dateStr = format.format(fromDate) ;
			String endStr = format.format(toDate);
			String hql = " FROM LedgerModel l"
					+ " WHERE (l.accountId = '"+accountId+"' OR l.toAccountId = '"+accountId+"')" 
					+ " AND l.transactionTime >='" + dateStr+ "' " 
					+ " AND l.transactionTime <'" + endStr+ "' "
					+ " ORDER BY l.transactionTime ASC";
			ledgerModels = (List<LedgerModel>)this.getHibernateTemplate().find(hql);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ledgerModels;
	}
	
	public List<LedgerModel> getLedgerModelByAccountID(Long accountId,Integer numberOfTransactions){
		List<LedgerModel> ledgerModels = null;
		try{
			DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
//			String dateStr = format.format(fromDate) ;
//			String endStr = format.format(toDate);
			String hql = " FROM LedgerModel l"
					+ " WHERE (l.accountId = '"+accountId+"' OR l.toAccountId = '"+accountId+"')" 
					+ " ORDER BY l.transactionTime DESC LIMIT 0,"+numberOfTransactions.intValue();
			ledgerModels = (List<LedgerModel>)this.getHibernateTemplate().find(hql);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ledgerModels;
	}

	public boolean markLedgerReversalEntries(Long ledgerId){
		boolean updated = false;
		try{
			String query = "update ledger set is_reversal=1 where ledger_id=?";
			Object[] args = {ledgerId};
			jdbcTemplate.update(query, args);
			updated = true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return updated;
	}

	@Override
	public LedgerModel saveOrUpdate(LedgerModel ledgerModel) {
						
		this.getHibernateTemplate().saveOrUpdate(ledgerModel);
		
		this.getHibernateTemplate().flush();
		
		this.getHibernateTemplate().evict(ledgerModel);
		
		return ledgerModel;
		
	}

	@Override
	public Date getLastTrxDate(Long accountId, Long transactionTypeId, Long handlerId) throws Exception {
		logger.info("Start of LedgerHibernateDAO.getLastTrxDate() at Time :: " + new Date());
		Date lastTrxDate = null;
		String accountColumn = "";
		if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
			accountColumn = "l.accountId";
		}else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
			accountColumn = "l.toAccountId";
		}

		String hql = "SELECT l.transactionTime"
				+ " FROM LedgerModel l"
				+ " INNER JOIN l.relationReasonIdReasonModel reason"
				+ " WHERE " + accountColumn + " = " + accountId
				+ " AND reason.reasonId <> "+ ReasonConstants.REVERSAL
				+ " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
					/*added by mudassir: */
				+ " AND reason.reasonId <> "+ ReasonConstants.BULK_PAYMENT
				+ " AND reason.reasonId <> "+ ReasonConstants.SETTLEMENT
				+ " AND reason.reasonId <> "+ ReasonConstants.ROLLBACK_WALKIN_CUSTOMER
				+ " AND reason.reasonId <> "+ ReasonConstants.REVERSE_BILL_PAYMENT
				+ " AND reason.reasonId <> "+ ReasonConstants.FUND_CUSTOMER_BB_CORE_AC;

		// Handler Limit Changes
		if(handlerId != null && handlerId.longValue() > 0){
			hql += " AND l.handlerId = "+ handlerId
					+ " AND l.excludeLimitForHandler <> "+ Boolean.TRUE;
		}else{
			hql += " AND l.excludeLimit <> "+ Boolean.TRUE;
		}
		hql += " ORDER BY l.transactionTime DESC";
		List<Object> objs = this.getHibernateTemplate().find(hql);
		if(objs != null && !objs.isEmpty())
			lastTrxDate = (Date) objs.get(0);
		logger.info("End of LedgerHibernateDAO.getLastTrxDate() at Time :: " + new Date());
		return lastTrxDate;
	}

	@Override
	public Double getConsumedBalanceByDateRangeForDormancy(Long accountId, Long transactionTypeId, Date startDate, Date endDate) throws Exception {
		logger.info("Start of LedgerHibernateDAO.getConsumedBalanceByDateRangeForDormancy() at Time :: " + new Date());
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
			endDate = c.getTime();
			DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
			String dateStr = format.format(startDate) ;
			String endStr = format.format(endDate);
			String accountColumn = "";
			if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
				accountColumn = "l.accountId";
				}else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
				accountColumn = "l.toAccountId";
			}
			String hql = "SELECT SUM(l.transactionAmount)"
					+ " FROM LedgerModel l"
					+ " INNER JOIN l.relationReasonIdReasonModel reason"
					+ " WHERE " + accountColumn + " = " + accountId
					+ " AND reason.reasonId <> "+ ReasonConstants.REVERSAL
					+ " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
					+ " AND reason.reasonId <> "+ ReasonConstants.BULK_PAYMENT
					+ " AND reason.reasonId <> "+ ReasonConstants.SETTLEMENT
					+ " AND reason.reasonId <> "+ ReasonConstants.ROLLBACK_WALKIN_CUSTOMER
					+ " AND reason.reasonId <> "+ ReasonConstants.REVERSE_BILL_PAYMENT
					+ " AND reason.reasonId <> "+ ReasonConstants.FUND_CUSTOMER_BB_CORE_AC
					+ " AND l.transactionTime >='" + dateStr+ "' "
					+ " AND l.transactionTime <'" + endStr+ "' ";

			List<Object> list = this.getHibernateTemplate().find(hql);
            Double result = null;
			if(list != null && list.size() > 0)
			    result = (Double)list.get(0);
            if(result == null)
                result = Double.valueOf(0d);

            return result;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally {
			logger.info("Start of LedgerHibernateDAO.getConsumedBalanceByDateRangeForDormancy() at Time :: " + new Date());
		}
		return null;
	}

	@Override
	public Double getConsumedBalanceByDateRangeReports(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId) {
		try{
			logger.info("Start of LedgerHibernateDAO.getConsumedBalanceByDateRange() at Time :: " + new Date());
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
			endDate = c.getTime();
			DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
			String dateStr = format.format(startDate) ;
			String endStr = format.format(endDate);
			String accountColumn = "";
			if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
				accountColumn = "L.FROM_ACCOUNT_ID";
			}else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
				accountColumn = "L.TO_ACCOUNT_ID";
			}
			String sql = "SELECT SUM(L.TRANSACTION_AMOUNT)"
					+ " FROM Ledger L"
					+ " INNER JOIN REASON R "
					+ " ON R.REASON_ID = L.REASON_ID"
					+ " WHERE " + accountColumn + " = " + accountId
					+ " AND L.REASON_ID NOT IN (" + ReasonConstants.REVERSAL + "," + ReasonConstants.BULK_PAYMENT + "," + ReasonConstants.SETTLEMENT
					+ "," + ReasonConstants.ROLLBACK_WALKIN_CUSTOMER + "," + ReasonConstants.REVERSE_BILL_PAYMENT + "," + ReasonConstants.FUND_CUSTOMER_BB_CORE_AC +")"
					+ " AND (L.IS_REVERSAL IS NULL OR L.IS_REVERSAL = " + 0L + ")"
					+ " AND L.TRANSACTION_TIME >= '" + dateStr + "'"
					+ " AND L.TRANSACTION_TIME < '" + endStr + "'";
			// Handler Limit Changes
			if(handlerId != null && handlerId.longValue() > 0){
				sql += " AND L.HANDLER_ID = "+ handlerId
						+ " AND L.EXCLUDE_LIMIT_HANDLER <> "+ 1L;
			}else{
				sql += " AND L.EXCLUDE_LIMIT <>"+ 1L;
			}
			logger.info("Query to calculate LedgerHibernateDAO.getConsumedBalanceByDateRange() :: " + sql);
			if(accountId != null)
			{
				List<Object> objs = this.jdbcTemplate.queryForList(sql,Object.class);
				if(objs != null && objs.size() > 0){
//					Double result = (Double)objs.get(0);
					Double result = null;
					if(objs.get(0) != null)
					{
						BigDecimal bigDecimal = (BigDecimal)objs.get(0);
						if(bigDecimal==null){
							result = Double.valueOf(0d);
						}
						result = bigDecimal.doubleValue();
					}
					if(result == null){
						result = Double.valueOf(0d);
					}
					return result;
				}
			}
		}catch(Exception ex){
			logger.error("Error in LedgerHibernateDAO.getConsumedBalanceByDateRange() :: " + ex);
		}
		finally {
			logger.info("End of LedgerHibernateDAO.getConsumedBalanceByDateRange() at Time :: " + new Date());
		}
		return null;
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
