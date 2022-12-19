package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.RegistrationStateModel;

/**
 * @author soofiafa
 * 
 */

public interface RegistrationStateDAO
    extends BaseDAO<RegistrationStateModel, Long> {
	RegistrationStateModel getRegistrationStateById(Long registrationStateId);
	CustomList<RegistrationStateModel> getRegistrationStateByIds(Long[] registrationStateIds);
}