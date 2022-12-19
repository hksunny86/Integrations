package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BlinkCustomerRegistrationStateModel;
import com.inov8.microbank.common.model.RegistrationStateModel;

/**
 * @author soofiafa
 * 
 */

public interface BlinkCustomerRegistrationStateDAO
    extends BaseDAO<BlinkCustomerRegistrationStateModel, Long> {
	BlinkCustomerRegistrationStateModel getRegistrationStateById(Long registrationStateId);
	CustomList<BlinkCustomerRegistrationStateModel> getRegistrationStateByIds(Long[] registrationStateIds);
}