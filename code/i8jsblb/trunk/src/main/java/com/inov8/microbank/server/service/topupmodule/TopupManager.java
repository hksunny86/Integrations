package com.inov8.microbank.server.service.topupmodule;

import java.util.List;
import java.util.Map;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.TopupSchedulerExecModel;



public interface TopupManager
{
	SearchBaseWrapper loadTopupExecModelList(SearchBaseWrapper searchBaseWrapper);
	boolean saveTopupExecModelRequiresNewTransaction(TopupSchedulerExecModel topupSchedulerExecModel);
	boolean saveTopupExecModel(TopupSchedulerExecModel topupSchedulerExecModel);
	Map<Long, String> getAllDisputedEntries(List<Long> trxCodesList);
}
