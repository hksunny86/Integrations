package com.inov8.microbank.server.service.pendingaccountopeningmodule.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;

import java.util.List;

public interface PendingAccountOpeningDAO extends BaseDAO<AccountOpeningPendingSafRepoModel, Long> {

    List<AccountOpeningPendingSafRepoModel> loadAllPendingAccount() throws FrameworkCheckedException;

    public void updatePendingAccountSafRepo(AccountOpeningPendingSafRepoModel model) throws FrameworkCheckedException;

    public AccountOpeningPendingSafRepoModel loadExistingPendingAccountOpeningSafRepo(AccountOpeningPendingSafRepoModel accountOpeningPendingSafRepoModel) throws FrameworkCheckedException;

}
