package com.inov8.microbank.server.service.clspendingblinkcustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.ClsPendingBlinkCustomerModel;
import com.inov8.microbank.debitcard.model.DebitCardRequestsViewModel;

import java.util.List;

public interface ClsPendingBlinkCustomerManager {


    List<ClsPendingBlinkCustomerModel> loadAllClsPendingBlinkCustomer() throws FrameworkCheckedException;

    void makePendingAccountOpeningCommand(ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel) throws  FrameworkCheckedException;

    public List<ClsPendingBlinkCustomerModel> searchBlinkAccountCLSData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

}
