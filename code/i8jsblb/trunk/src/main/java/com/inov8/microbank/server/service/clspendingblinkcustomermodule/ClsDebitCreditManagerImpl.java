package com.inov8.microbank.server.service.clspendingblinkcustomermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ClsDebitCreditBlockModel;
import com.inov8.microbank.server.dao.securitymodule.AppUserDAO;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsDebitCreditDAO;

import java.util.List;

public class ClsDebitCreditManagerImpl implements ClsDebitCreditManager{
    private ClsDebitCreditDAO clsDebitCreditDAO;

    @Override
    public List<ClsDebitCreditBlockModel> loadClsDebitCreditBlockModel(ClsDebitCreditBlockModel clsDebitCreditBlockModel) throws FrameworkCheckedException {
        return clsDebitCreditDAO.loadClsDebitCreditModel();
    }

    public void setClsDebitCreditDAO(ClsDebitCreditDAO clsDebitCreditDAO) {
        this.clsDebitCreditDAO = clsDebitCreditDAO;
    }
}
