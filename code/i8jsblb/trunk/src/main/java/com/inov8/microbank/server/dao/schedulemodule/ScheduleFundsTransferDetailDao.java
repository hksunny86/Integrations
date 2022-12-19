package com.inov8.microbank.server.dao.schedulemodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferDetailModel;

import java.util.Date;
import java.util.List;

/**
 * Created by shaista iqbal on 2/18/2020.
 */
public interface ScheduleFundsTransferDetailDao extends BaseDAO<ScheduleFundsTransferDetailModel, Long> {

    public void updateScheduleFundsTransferList(List<ScheduleFundsTransferDetailModel> sftList);
    public List<ScheduleFundsTransferDetailModel> getActiveFundTransferSchedule(Date date);


}
