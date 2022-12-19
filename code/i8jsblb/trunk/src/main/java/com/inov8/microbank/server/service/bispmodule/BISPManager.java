package com.inov8.microbank.server.service.bispmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;

public interface BISPManager {

    String saveOrUpdateBVSEntryRequiresNewTransaction(UserDeviceAccountsModel userDeviceAccountsModel, AppUserModel appUserModel,
                                                      String cNic, SwitchWrapper sWrapper, String transactionCode) throws FrameworkCheckedException;
}
