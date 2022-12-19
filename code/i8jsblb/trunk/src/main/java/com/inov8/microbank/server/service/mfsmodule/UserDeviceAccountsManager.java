package com.inov8.microbank.server.service.mfsmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;

public interface UserDeviceAccountsManager {	
	
	
	public BaseWrapper loadUserDeviceAccount(BaseWrapper baseWrapper)throws FrameworkCheckedException;
	public BaseWrapper updateUserDeviceAccount(BaseWrapper baseWrapper)throws FrameworkCheckedException;
	public void sendSMS( String mfsId, String randomPin, String mobileNo ) ;
	UserDeviceAccountsModel loadUserDeviceAccountByUserId(String userId) throws FrameworkCheckedException;
	public abstract UserDeviceAccountsModel loadUserDeviceAccountsModelByAppUserIdAndDeviceTypeId(
			Long deviceTypeId, Long appUserId) throws FrameworkCheckedException;
}
