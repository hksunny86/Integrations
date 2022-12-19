package com.inov8.microbank.server.dao.bispcustnadraverification;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.integration.i8sb.vo.I8SBSwitchControllerResponseVO;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BISPCustNadraVerificationModel;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;

public interface BISPCustNadraVerificationDAO  extends BaseDAO<BISPCustNadraVerificationModel, Long> {

    long isBVSSuccessful(Long customerId,String AgentId,String cNic,Long isBVSVerified,Long appUerTypeId) throws FrameworkCheckedException;

    long isNicUsedInCurrentDate(String AgentId,String cNic,Long isBVSVerified,Long appUerTypeId) throws FrameworkCheckedException;

    String saveOrUpdateBVSEntryRequiresNewTransaction(UserDeviceAccountsModel userDeviceAccountsModel, AppUserModel appUserModel,
                              String cNic, SwitchWrapper sWrapper,String transactionCode) throws FrameworkCheckedException;
}
