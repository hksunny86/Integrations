package com.inov8.microbank.server.dao.portal.ola.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.util.DateUtils;
import com.inov8.framework.common.util.ThreadLocalExportInfoModel;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.ola.SettlementBbStatementViewModel;
import com.inov8.microbank.server.dao.portal.ola.SettlementBbStatementViewDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/** 
 * Created By    : Naseer Ullah <br>
 * Creation Date : Aug 20, 2013 7:40:48 PM<p>
 * Purpose       : <p>
 * Updated By    : <br>
 * Updated Date  : <br>
 * Comments      : <br>
 */
public class SettlementBbStatementViewHibernateDao extends BaseHibernateDAO<SettlementBbStatementViewModel, Long, SettlementBbStatementViewDao> implements SettlementBbStatementViewDao
{
	

	/* (non-Javadoc)
	 * @see com.inov8.microbank.server.dao.portal.ola.SettlementBbStatementViewDao#getBalanceByDate(java.util.Calendar, java.lang.Long)
	 */
	public String getBalanceByDate(Date date, Long accountId) throws Exception {

		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    	String query = "select stats.start_day_balance from daily_account_stats stats where stats.stats_date = ? and stats.account_id= ?";
		Connection connection = null;
    	PreparedStatement preparedStatement = null;
    	ResultSet resultSet = null;  	
    	
    	String START_DAY_BALANCE = null;
    	
    	try {

    		java.util.Calendar calendar = new java.util.GregorianCalendar();
    		calendar.setTime(date);
    		calendar.set(Calendar.HOUR, 0);
    		calendar.set(Calendar.MINUTE, 0);
    		calendar.set(Calendar.SECOND, 0);
    		
    		connection = super.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();
    		
        	preparedStatement = connection.prepareStatement(query);
    		preparedStatement.setDate(1, new java.sql.Date(calendar.getTime().getTime()));
    		preparedStatement.setLong(2, accountId);
    		
    		resultSet = preparedStatement.executeQuery();

    		while (resultSet.next()) {
    			
    			START_DAY_BALANCE = resultSet.getString("START_DAY_BALANCE");
    		}
    	
    	} catch (SQLException e) {
    		
    		logger.error("Error in SettlementBbStatementViewHibernateDAO.getBalanceByDate() :: " + e);
    	
    	} finally {

    		if(preparedStatement != null) {
    			
    			preparedStatement.close();
    		}
    		
    		if(resultSet != null) {

    			resultSet.close();
    		}
    		if(connection != null)
			{
				connection.close();
			}
    	}
    	
    	return START_DAY_BALANCE;
	}

	@Override
	public CustomList<SettlementBbStatementViewModel> searchSettlementbbStatement(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
		CustomList customList;
		SettlementBbStatementViewModel model = (SettlementBbStatementViewModel) searchBaseWrapper.getBasePersistableModel();

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SettlementBbStatementViewModel.class);
		if (model.getAccountId() != null) {
			detachedCriteria.add(Restrictions.eq("relationAccountIdAccountModel.accountId", model.getAccountId()));
		}
		if (model.getAccountTittle() != null) {
			detachedCriteria.add(Restrictions.eq("accountTittle", model.getAccountTittle()));
		}
		if (ThreadLocalExportInfoModel.getExportInfo() != null) {
			ThreadLocalExportInfoModel.getExportInfo().setDetachedCriteria(detachedCriteria);
		}
//		if (model.getDateRangeHolderModel() != null) {
//			detachedCriteria.add(Restrictions.eq("transactionTime", model.getTransactionTime()));
//		}
		if(searchBaseWrapper.getDateRangeHolderModel() != null)
			this.addDateRangeToCriteria(searchBaseWrapper.getDateRangeHolderModel(),detachedCriteria);

		if(searchBaseWrapper.getSortingOrderMap() != null)
			this.addSortingToCriteria(detachedCriteria,searchBaseWrapper.getSortingOrderMap());

		customList = new CustomList(getHibernateTemplate().findByCriteria(detachedCriteria));

		return customList;
	}

	private void addDateRangeToCriteria(DateRangeHolderModel dateRangeHolderModel, DetachedCriteria detachedCriteria)
	{
		if (dateRangeHolderModel != null && dateRangeHolderModel.getDatePropertyName() != null
				&& (!("".equals(dateRangeHolderModel.getDatePropertyName()))))
		{
			if(dateRangeHolderModel.getFromDate() != null && dateRangeHolderModel.getToDate() != null)
			{
				detachedCriteria.add(Restrictions.between(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayStartDate(dateRangeHolderModel.getFromDate()), DateUtils.getDayEndDate(dateRangeHolderModel
						.getToDate())));
			}
			else if(dateRangeHolderModel.getFromDate() != null && dateRangeHolderModel.getToDate() == null)
			{
				detachedCriteria.add(Restrictions.ge(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayStartDate(dateRangeHolderModel.getFromDate())));
			}

			else if(dateRangeHolderModel.getFromDate() == null && dateRangeHolderModel.getToDate() != null)
			{
				detachedCriteria.add(Restrictions.le(dateRangeHolderModel.getDatePropertyName(), DateUtils.getDayEndDate(dateRangeHolderModel
						.getToDate())));
			}
		}
	}

	private void addSortingToCriteria(DetachedCriteria detachedCriteria, LinkedHashMap<String, SortingOrder> sortingOrderMap)
	{
		if (sortingOrderMap != null && sortingOrderMap.size() > 0)
		{
			for (String key : sortingOrderMap.keySet())
			{
				SortingOrder sortingOrder = sortingOrderMap.get(key);
				if (SortingOrder.DESC == sortingOrder)
				{
					detachedCriteria.addOrder(Order.desc(key));
				}
				else
				{
					detachedCriteria.addOrder(Order.asc(key));
				}
			}
		}
	}

}
