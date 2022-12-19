package com.inov8.microbank.server.service.clspendingblinkcustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;

import java.util.List;

public class ClsPendingAccountOpeningManagerImpl implements ClsPendingAccountOpeningManager{
private ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO;

    @Override
    public List<ClsPendingAccountOpeningModel> loadAllClsPendingAccountOpeningApprovedSmsAlert() throws FrameworkCheckedException {
        return clsPendingAccountOpeningDAO.loadAllPendingAccountApprovedSmsAlertRequired();

    }

    @Override
    public List<ClsPendingAccountOpeningModel> loadAllClsPendingAccountOpeningApprovedUpdateAMA() throws FrameworkCheckedException {
        return clsPendingAccountOpeningDAO.loadAllPendingAccountApprovedUpdateAMA();
    }

    public void setClsPendingAccountOpeningDAO(ClsPendingAccountOpeningDAO clsPendingAccountOpeningDAO) {
        this.clsPendingAccountOpeningDAO = clsPendingAccountOpeningDAO;
    }
}
