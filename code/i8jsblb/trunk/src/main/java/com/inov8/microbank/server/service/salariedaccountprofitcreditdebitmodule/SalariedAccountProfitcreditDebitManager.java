package com.inov8.microbank.server.service.salariedaccountprofitcreditdebitmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.SalariedAccountProfitModel;

import java.util.List;

public interface SalariedAccountProfitcreditDebitManager {

    List<SalariedAccountProfitModel> loadAllSalariedAccountProfitData() throws FrameworkCheckedException;
    String salariedAccountProfitDebitCreditDeductionCommand(SalariedAccountProfitModel salariedAccountProfitModel) throws  FrameworkCheckedException;

}
