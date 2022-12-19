package com.inov8.microbank.server.service.clspendingblinkcustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;

import java.util.List;

public interface ClsPendingAccountOpeningManager {

    List<ClsPendingAccountOpeningModel> loadAllClsPendingAccountOpeningApprovedSmsAlert() throws FrameworkCheckedException;

    List<ClsPendingAccountOpeningModel> loadAllClsPendingAccountOpeningApprovedUpdateAMA() throws FrameworkCheckedException;

}
