package com.inov8.microbank.server.service.advancesalaryloanmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;

import java.util.List;

public interface AdvanceSalaryLoanManager {
    List<AdvanceSalaryLoanModel> loadAllAdvanceSalaryLoanData() throws FrameworkCheckedException;
    String advanceLoanDeductionCommand(AdvanceSalaryLoanModel advanceSalaryLoanModel) throws  FrameworkCheckedException;
    List<AdvanceSalaryLoanModel> loadAdvanceSalaryLoanByIsCompleted() throws FrameworkCheckedException;

}
