package com.inov8.microbank.server.service.ussdmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.UserStateModel;
import com.inov8.microbank.common.vo.ussd.UserState;

public interface UssdUserStateManager {
	public UserState findUserState(String msisdn, int senderID)throws FrameworkCheckedException;
	public UserState findUserState(String mobileNo)throws FrameworkCheckedException;
	public UserStateModel saveUserState(UserState userState)throws FrameworkCheckedException;
	public boolean deleteUserState(String msisdn) ;
}
