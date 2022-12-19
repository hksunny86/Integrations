package com.inov8.microbank.account.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.account.model.BlacklistedCnicsModel;

import java.util.Collection;
import java.util.List;

/**
 * Created by Malik on 10/9/2016.
 */
public interface BlacklistedCnicsDAO extends BaseDAO<BlacklistedCnicsModel,Long>
{

    List<String> loadBlacklistedCNICList();

    BlacklistedCnicsModel findBlacklistedCnicsModelByCnicNo(String cnicNo);

    List<BlacklistedCnicsModel> loadBlacklistedCnicsModelByCnicNos(Collection<String> cnicNos);
    Boolean isCnicBlacklisted(String cnic);
}
