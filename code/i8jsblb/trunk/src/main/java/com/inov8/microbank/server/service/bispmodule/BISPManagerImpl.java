package com.inov8.microbank.server.service.bispmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.server.dao.bispcustnadraverification.BISPCustNadraVerificationDAO;

public class BISPManagerImpl implements BISPManager {

    private BISPCustNadraVerificationDAO custNadraVerificationDAO;

    public void setCustNadraVerificationDAO(BISPCustNadraVerificationDAO custNadraVerificationDAO) {
        this.custNadraVerificationDAO = custNadraVerificationDAO;
    }

    @Override
    public String saveOrUpdateBVSEntryRequiresNewTransaction(UserDeviceAccountsModel userDeviceAccountsModel, AppUserModel appUserModel, String cNic, SwitchWrapper sWrapper, String transactionCode) throws FrameworkCheckedException {
        return custNadraVerificationDAO.saveOrUpdateBVSEntryRequiresNewTransaction(userDeviceAccountsModel,appUserModel,cNic,sWrapper,transactionCode);
    }
}
