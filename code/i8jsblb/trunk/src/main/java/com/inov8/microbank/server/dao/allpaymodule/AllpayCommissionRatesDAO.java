package com.inov8.microbank.server.dao.allpaymodule;

import java.util.List;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AllpayCommissionRateModel;

public interface AllpayCommissionRatesDAO extends BaseDAO<AllpayCommissionRateModel, Long>
{

	public boolean getDuplicateCommissionRateRecords(AllpayCommissionRateModel allpayCommissionRateModel);

	List<AllpayCommissionRateModel> getNationalDistAndDistLevelComm(BaseWrapper baseWrapper);
}
