package com.inov8.microbank.server.service.advancesalaryloan.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.ZongBalanceUpdateModel;

import java.util.List;

public interface ZongBalanceUpdateDAO  extends BaseDAO<ZongBalanceUpdateModel, Long> {

    List<ZongBalanceUpdateModel> loadAllZongUpdateBalance() throws FrameworkCheckedException;

}
