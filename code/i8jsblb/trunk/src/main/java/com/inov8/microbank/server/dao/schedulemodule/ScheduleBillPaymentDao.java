package com.inov8.microbank.server.dao.schedulemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ThirdPartyAccountOpeningModel;
import com.inov8.microbank.common.model.schedulemodule.ScheduleLoanPaymentModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

import java.util.List;

/**
 * Created by Muhammad Sajid on 3/20/2017.
 */
public interface ScheduleBillPaymentDao extends BaseDAO<ScheduleLoanPaymentModel, Long> {
    public void updateScheduleBillPaymentList(List<ScheduleLoanPaymentModel> sbpList);

    public List<ScheduleLoanPaymentModel> getSchedulePaymentsByCustomerId(long customerId);

    ScheduleLoanPaymentModel saveOrUpdateScheduleLoanRequest(WorkFlowWrapper wrapper) throws FrameworkCheckedException;

}
