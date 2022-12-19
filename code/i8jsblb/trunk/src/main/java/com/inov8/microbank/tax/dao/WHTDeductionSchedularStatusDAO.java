package com.inov8.microbank.tax.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.tax.model.DailyWhtDeductionModel;
import com.inov8.microbank.tax.model.WHTDeductionSchedularStatusModel;

import java.util.Date;
import java.util.List;


public interface WHTDeductionSchedularStatusDAO extends BaseDAO<WHTDeductionSchedularStatusModel, Long>
{
	public List<WHTDeductionSchedularStatusModel> findWHTDeductionMissedEntries() throws Exception;
	public void updateWhtSchedulerStatus() throws Exception;
}
