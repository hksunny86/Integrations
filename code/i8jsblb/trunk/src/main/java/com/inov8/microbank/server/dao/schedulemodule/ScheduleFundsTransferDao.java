package com.inov8.microbank.server.dao.schedulemodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.schedulemodule.ScheduleFundsTransferModel;

import java.util.List;

/**
 * Created by Muhammad Sajid on 3/16/2017.
 */
public interface ScheduleFundsTransferDao extends BaseDAO<ScheduleFundsTransferModel, Long> {

    public void updateScheduleFundsTransferList(List<ScheduleFundsTransferModel> sftList);
    public List<ScheduleFundsTransferModel> getScheduleFundsTransferByCustomerId(long customerId);
}
