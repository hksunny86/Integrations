package com.inov8.microbank.tax.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.FilerRateConfigModel;
import com.inov8.microbank.common.model.OfflineBillersConfigModel;

public interface OfflineBillersConfigDAO extends BaseDAO<OfflineBillersConfigModel, Long> {

    public OfflineBillersConfigModel loadOfflineBillersModelByProductId(String productId);
}
