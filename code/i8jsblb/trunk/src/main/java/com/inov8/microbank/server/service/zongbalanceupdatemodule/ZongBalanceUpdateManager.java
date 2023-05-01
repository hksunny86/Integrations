package com.inov8.microbank.server.service.zongbalanceupdatemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.MerchantAccountModel;
import com.inov8.microbank.common.model.ZongBalanceUpdateModel;

import java.util.List;

public interface ZongBalanceUpdateManager {
    List<ZongBalanceUpdateModel> loadAllAdvanceSalaryLoanData() throws FrameworkCheckedException;
    ZongBalanceUpdateModel createMerchantAccountModel(ZongBalanceUpdateModel merchantAccountModel);

}
