/**
 * 
 */
package com.inov8.microbank.server.dao.dailyaccountstats;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.DailyAccountStatsModel;

/**
 * @author NaseerUl
 *
 */
public interface DailyAccountStatsDao extends BaseDAO<DailyAccountStatsModel, Long>
{
	int updateDailyAccountStats(Boolean isEndDayBalanceNull, String oldStatus, String newStatus, Long fromDailyAccountStatsId, Long toDailyAccountStatsId) throws FrameworkCheckedException;
}
