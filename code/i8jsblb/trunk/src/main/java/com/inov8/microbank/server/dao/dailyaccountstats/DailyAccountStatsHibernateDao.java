/**
 * 
 */
package com.inov8.microbank.server.dao.dailyaccountstats;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.DailyAccountStatsModel;

/**
 * @author NaseerUl
 *
 */
public class DailyAccountStatsHibernateDao extends BaseHibernateDAO<DailyAccountStatsModel, Long, DailyAccountStatsDao> implements DailyAccountStatsDao
{
	@Override
	public int updateDailyAccountStats(Boolean isEndDayBalanceNull, String oldStatus, String newStatus, Long fromDailyAccountStatsId, Long toDailyAccountStatsId) throws FrameworkCheckedException
	{
		int updateCount = 0;
		
		StringBuilder updateDailyAccountStatsHqlBuilder = new StringBuilder(300);
		updateDailyAccountStatsHqlBuilder.append("update DailyAccountStatsModel model set model.decryptionSchedulerStatus=? where model.decryptionSchedulerStatus=? ");
		if( isEndDayBalanceNull != null && isEndDayBalanceNull )
		{
			updateDailyAccountStatsHqlBuilder.append("and model.endDayBalance is null ");
		}
		else
		{
			updateDailyAccountStatsHqlBuilder.append("and model.endDayBalance is not null ");
		}
		updateDailyAccountStatsHqlBuilder.append("and model.dailyAccountStatsId between ? and ?");

		Object[] args = {newStatus, oldStatus, fromDailyAccountStatsId, toDailyAccountStatsId};
		updateCount = getHibernateTemplate().bulkUpdate(updateDailyAccountStatsHqlBuilder.toString(), args);

		return updateCount;
	}
}
