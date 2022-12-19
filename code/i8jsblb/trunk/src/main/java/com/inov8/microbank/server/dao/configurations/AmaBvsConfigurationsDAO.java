package com.inov8.microbank.server.dao.configurations;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AmaBvsConfigurationsModel;
import com.inov8.microbank.common.model.ClsDebitCreditBlockModel;

import java.util.List;

public interface AmaBvsConfigurationsDAO extends BaseDAO<AmaBvsConfigurationsModel, Long> {
    List<AmaBvsConfigurationsModel> loadAmaBvsConfigurationsModel() throws FrameworkCheckedException;
}
