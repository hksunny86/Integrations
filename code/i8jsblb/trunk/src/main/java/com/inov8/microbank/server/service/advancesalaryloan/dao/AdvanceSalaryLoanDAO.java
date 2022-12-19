package com.inov8.microbank.server.service.advancesalaryloan.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;

import java.util.List;

public interface AdvanceSalaryLoanDAO extends BaseDAO<AdvanceSalaryLoanModel, Long> {

    List<AdvanceSalaryLoanModel> loadAllAdvanceSalaryLoan() throws FrameworkCheckedException;
    AdvanceSalaryLoanModel loadAdvanceSalaryLoanByMobileNumber(String mobileNo) throws FrameworkCheckedException;
    void update(Long noOfInstallmentPaid, Boolean isComplete, Long advanceSalaryLoanId);
    List<AdvanceSalaryLoanModel> loadAllAdvanceSalaryLoanByIsCompleted() throws FrameworkCheckedException;

}
