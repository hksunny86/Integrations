package com.inov8.verifly.server.dao.mainmodule;


import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.verifly.common.model.AccountInfoModel;


/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  07-Sep-2006
 *
 */



public interface AccountInfoDAO extends BaseDAO<AccountInfoModel,Long>
{
    int updateAccountInfoModelToCloseAccount(Long accountInfoId);
    public AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws FrameworkCheckedException;
    public int updateAccountInfoModel(Long customerId, Long paymentModeId, Long isMigrated) throws FrameworkCheckedException;
}
