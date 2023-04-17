package com.inov8.microbank.server.dao.jsloansmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.JSLoansModel;

public interface JSLoansDAO extends BaseDAO<JSLoansModel, Long> {
    JSLoansModel loadJSLoansByMobileNumber(String mobileNo) throws FrameworkCheckedException;
}
