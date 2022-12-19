package com.inov8.microbank.server.dao.mfsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerDetailsViewModel;

import java.util.Date;
import java.util.List;

/**
 * Created by Inov8 on 4/11/2018.
 */
public interface CustomerDetailsCommandDAO extends BaseDAO<CustomerDetailsViewModel,Long> {

    List<String> getCustomerDetails(AppUserModel appUserModel) throws FrameworkCheckedException;

    Double getConsumedBalanceByDateRange(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId);

    Double getConsumedBalanceByDateRangeForIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId);

    Double getConsumedBalanceByDateRangeForAgentIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId);

    Double getDailyConsumedBalance(Long accountId, Long transactionTypeId, Date date, Long handlerId);

    Double getDailyConsumedBalanceForIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId);

    Double getDailyConsumedBalanceForAgentIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId);

    List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId)throws FrameworkCheckedException;
}
