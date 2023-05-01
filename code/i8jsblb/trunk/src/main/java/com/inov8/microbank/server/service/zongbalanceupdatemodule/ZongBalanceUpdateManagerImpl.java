package com.inov8.microbank.server.service.zongbalanceupdatemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.v2.GenericDao;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.ZongBalanceUpdateModel;
import com.inov8.microbank.server.service.advancesalaryloan.dao.AdvanceSalaryLoanDAO;
import com.inov8.microbank.server.service.advancesalaryloan.dao.ZongBalanceUpdateDAO;

import java.util.List;

public class ZongBalanceUpdateManagerImpl implements ZongBalanceUpdateManager {

    private ZongBalanceUpdateDAO zongBalanceUpdateDAO;
    private GenericDao genericDao;


    @Override
    public List<ZongBalanceUpdateModel> loadAllAdvanceSalaryLoanData() throws FrameworkCheckedException {
        return zongBalanceUpdateDAO.loadAllZongUpdateBalance();

    }

    @Override
    public ZongBalanceUpdateModel createMerchantAccountModel(ZongBalanceUpdateModel zongBalanceUpdateModel) {
        return this.genericDao.createEntity(zongBalanceUpdateModel);

    }


    public void setZongBalanceUpdateDAO(ZongBalanceUpdateDAO zongBalanceUpdateDAO) {
        this.zongBalanceUpdateDAO = zongBalanceUpdateDAO;
    }

    public void setGenericDao(GenericDao genericDao) {
        this.genericDao = genericDao;
    }
}
