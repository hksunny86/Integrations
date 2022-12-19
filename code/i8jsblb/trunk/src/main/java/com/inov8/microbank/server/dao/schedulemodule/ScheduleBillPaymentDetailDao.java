package com.inov8.microbank.server.dao.schedulemodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.schedulemodule.ScheduleLoanPaymentDetailModel;

import java.util.Date;
import java.util.List;

/**
 * Created by shaista iqbal on 2/18/2020.
 */
public interface ScheduleBillPaymentDetailDao extends BaseDAO<ScheduleLoanPaymentDetailModel, Long> {
    public void updateScheduleBillPaymentDetailList(List<ScheduleLoanPaymentDetailModel> sbpList);
    public List<ScheduleLoanPaymentDetailModel> getActiveBillPaymentSchedule(Date date);


}
