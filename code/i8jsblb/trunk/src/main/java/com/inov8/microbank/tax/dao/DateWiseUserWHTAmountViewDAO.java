package com.inov8.microbank.tax.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.tax.model.DateWiseUserWHTAmountViewModel;
import com.inov8.microbank.tax.model.WHTConfigModel;

import java.util.Date;
import java.util.List;


public interface DateWiseUserWHTAmountViewDAO extends BaseDAO<DateWiseUserWHTAmountViewModel, Long>
{
    List<DateWiseUserWHTAmountViewModel> loadWithholdingUsersList(WHTConfigModel cashWithdrawalWHTConfigModel ,WHTConfigModel transferWHTConfigModel) throws Exception;
}
