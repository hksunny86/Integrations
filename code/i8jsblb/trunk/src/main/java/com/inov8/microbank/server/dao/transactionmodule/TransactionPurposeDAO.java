package com.inov8.microbank.server.dao.transactionmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.TransactionPurposeModel;

import java.util.List;

public interface TransactionPurposeDAO extends BaseDAO<TransactionPurposeModel,Long> {

    int loadTrxPurposeModelByCode(String code);
    public List <TransactionPurposeModel> loadTrxPurposeByCode();
}
