package com.inov8.microbank.debitcard.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;

public interface DebitCardRequestsViewModelDAO extends BaseDAO<DebitCardRequestsViewModel,Long> {
    public DebitCardRequestsViewModel loadDebitCardRequestsByAppUserId(Long appUserId, String mobileNo);
    public DebitCardRequestsViewModel loadDebitCardRequestsByDebitCardId(Long debitCardId);

}
