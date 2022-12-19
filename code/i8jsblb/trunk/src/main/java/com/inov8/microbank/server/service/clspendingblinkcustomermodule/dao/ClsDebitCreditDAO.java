package com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ClsDebitCreditBlockModel;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;

import java.util.List;

public interface ClsDebitCreditDAO extends BaseDAO<ClsDebitCreditBlockModel, Long> {
    List<ClsDebitCreditBlockModel> loadClsDebitCreditModel() throws FrameworkCheckedException;

}
