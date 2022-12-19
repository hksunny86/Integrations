package com.inov8.microbank.server.service.userdeviceaccount;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.UserDeviceAccountsModel;

public interface UserDeviceAccountListViewManager  {

	public SearchBaseWrapper searchUserDeviceAccount(SearchBaseWrapper  searchBaseWrapper) 
	throws FrameworkCheckedException;

	 public BaseWrapper loadUserDeviceAccount(BaseWrapper   baseWrapper) throws
     FrameworkCheckedException;

	 public BaseWrapper updateUserDeviceAccount(BaseWrapper   baseWrapper) throws
     FrameworkCheckedException;
	 
	 public BaseWrapper updateUserDeviceAccountStatus(BaseWrapper   baseWrapper) throws
     FrameworkCheckedException;
	 
	 
	 public BaseWrapper createUserDeviceAccount(BaseWrapper baseWrapper) throws FrameworkCheckedException;
	 
	 public void sendSMS( String mfsId, String randomPin, String mobileNo );

	 public UserDeviceAccountsModel findUserDeviceByAppUserId(Long appUserId) throws FrameworkCheckedException;

	 public BaseWrapper changeUserDeviceAccountPin(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public BaseWrapper changeUserDeviceAccountForgotPin(BaseWrapper baseWrapper) throws FrameworkCheckedException;


}
