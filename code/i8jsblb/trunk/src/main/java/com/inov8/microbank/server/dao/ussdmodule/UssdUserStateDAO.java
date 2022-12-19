package com.inov8.microbank.server.dao.ussdmodule;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.UserStateModel;

public interface UssdUserStateDAO extends BaseDAO<UserStateModel,Long>{
	public boolean deleteUserState(String msisdn);

}
