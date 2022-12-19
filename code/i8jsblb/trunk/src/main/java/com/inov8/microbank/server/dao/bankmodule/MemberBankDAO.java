package com.inov8.microbank.server.dao.bankmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;

import java.util.List;

public interface MemberBankDAO extends BaseDAO<MemberBankModel,Long> {
    public List<MemberBankModel> findAllBanks();

}
