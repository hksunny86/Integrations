package com.inov8.microbank.server.dao.productmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionThresholdRateModel;

import java.util.List;

public interface CommissionThresholdRateDAO extends BaseDAO<CommissionThresholdRateModel, Long> {
    List<CommissionThresholdRateModel> loadCommissionThresholdRateList(CommissionThresholdRateModel commissionThresholdRateModel);

}
