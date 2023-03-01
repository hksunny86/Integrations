package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.TasdeeqDataModel;

public interface TasdeeqDataDAO  extends BaseDAO<TasdeeqDataModel, Long> {
    public TasdeeqDataModel loadTasdeeqDataByMobile(String mobileNo);

}
