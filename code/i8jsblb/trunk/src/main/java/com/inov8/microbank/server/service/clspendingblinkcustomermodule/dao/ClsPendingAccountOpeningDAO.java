package com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.common.model.ClsPendingBlinkCustomerModel;

import java.util.List;

public interface ClsPendingAccountOpeningDAO extends BaseDAO<ClsPendingAccountOpeningModel, Long> {
    public ClsPendingAccountOpeningModel loadExistingPendingAccountOpeningSafRepo(ClsPendingAccountOpeningModel clsPendingAccountOpeningModel) throws FrameworkCheckedException;

    public ClsPendingAccountOpeningModel loadExistingPendingAccountOpening(ClsPendingAccountOpeningModel clsPendingAccountOpeningModel) throws FrameworkCheckedException;

    public ClsPendingAccountOpeningModel loadClsPendingAccountOpeningByMobileByQuery(final String mobileNo);

    List<ClsPendingAccountOpeningModel> loadAllPendingAccountApprovedSmsAlertRequired() throws FrameworkCheckedException;


    List<ClsPendingAccountOpeningModel> loadAllPendingAccountApprovedUpdateAMA() throws FrameworkCheckedException;

}
