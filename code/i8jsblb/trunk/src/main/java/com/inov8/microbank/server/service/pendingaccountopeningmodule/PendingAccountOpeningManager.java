package com.inov8.microbank.server.service.pendingaccountopeningmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;

import java.util.List;

public interface PendingAccountOpeningManager {


    List<AccountOpeningPendingSafRepoModel> loadAllAccountOpeningPendingSafRepo() throws FrameworkCheckedException;

    void makePendingAccountOpeningCommand(AccountOpeningPendingSafRepoModel accountOpeningPendingSafRepoModel) throws  FrameworkCheckedException;


}
