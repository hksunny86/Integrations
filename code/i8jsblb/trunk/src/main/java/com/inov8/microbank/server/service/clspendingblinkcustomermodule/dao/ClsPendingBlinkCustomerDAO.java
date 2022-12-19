package com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.ClsPendingBlinkCustomerModel;

import java.util.List;


public interface ClsPendingBlinkCustomerDAO extends BaseDAO<ClsPendingBlinkCustomerModel, Long> {

    List<ClsPendingBlinkCustomerModel> loadAllPendingAccount() throws FrameworkCheckedException;

    public void updatePendingAccountSafRepo(ClsPendingBlinkCustomerModel model) throws FrameworkCheckedException;

    public ClsPendingBlinkCustomerModel loadExistingPendingAccountOpeningSafRepo(ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel) throws FrameworkCheckedException;

}
