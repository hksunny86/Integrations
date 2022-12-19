package com.inov8.microbank.server.service.salariedaccountprofitmodule.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.SalariedAccountProfitModel;

import java.util.List;

public interface SalariedAccountProfitDebitCreditDAO  extends BaseDAO<SalariedAccountProfitModel, Long> {
    List<SalariedAccountProfitModel> loadAllSalariedAccountProfitDebitCredit() throws FrameworkCheckedException;

}
