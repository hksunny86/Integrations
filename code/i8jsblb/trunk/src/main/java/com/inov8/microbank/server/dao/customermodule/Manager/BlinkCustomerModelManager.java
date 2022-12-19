package com.inov8.microbank.server.dao.customermodule.Manager;
/* Muhammad Aqeel created on 3/17/2022*/

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.ClsPendingBlinkCustomerModel;

import java.util.List;

public interface BlinkCustomerModelManager {
    SearchBaseWrapper searchAllData(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    List<BlinkCustomerModel> loadAllClsPendingBlinkCustomer() throws FrameworkCheckedException;

}
