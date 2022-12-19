package com.inov8.microbank.server.dao.dailyjob;

import java.util.List;
import java.util.Map;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.TopupSchedulerExecModel;


public interface TopupSchedulerExecDAO extends BaseDAO<TopupSchedulerExecModel, Long>
{
	Map<Long, String> getAllDisputedEntries(List<Long> trxCodesList);
//	List<String> getFailedTransactionsRRNList();
	
}
