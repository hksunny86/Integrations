package com.inov8.microbank.server.dao.safrepo;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SafRepoModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.WalletSafRepoModel;

import java.util.List;

public interface WalletSafRepoDAO extends BaseDAO<WalletSafRepoModel,Long> {
    List<WalletSafRepoModel> loadAllToBeProcessedRecords() throws FrameworkCheckedException;

    WalletSafRepoModel loadWalletSafRepo(String transactionCode) throws FrameworkCheckedException;

    void updateWalletSafRepo(WalletSafRepoModel model) throws FrameworkCheckedException;

    List<WalletSafRepoModel> updateWalletSafRepoStatus(WalletSafRepoModel model) throws FrameworkCheckedException;

}
