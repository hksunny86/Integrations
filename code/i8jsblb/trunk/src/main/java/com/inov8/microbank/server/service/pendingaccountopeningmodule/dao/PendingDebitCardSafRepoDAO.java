package com.inov8.microbank.server.service.pendingaccountopeningmodule.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.DebitCardPendingSafRepo;

public interface PendingDebitCardSafRepoDAO extends BaseDAO<DebitCardPendingSafRepo, Long> {

    public DebitCardPendingSafRepo loadDebitCardSafRepoByMobileNoAndCnic(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException;
    public DebitCardPendingSafRepo loadDebitCardSafRepo(DebitCardPendingSafRepo debitCardPendingSafRepo) throws FrameworkCheckedException;

}
