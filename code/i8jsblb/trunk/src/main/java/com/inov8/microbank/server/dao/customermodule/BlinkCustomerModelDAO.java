package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.FailureReasonModel;
import org.richfaces.javascript.LibraryScript;

import java.util.List;

public interface BlinkCustomerModelDAO extends BaseDAO<BlinkCustomerModel, Long> {
    public BlinkCustomerModel loadBlinkCustomerModelByMobileAndAccUpdate(String mobileNo, Long accUpdate);
    public BlinkCustomerModel loadBlinkCustomerModelByBlinkCustomerId(Long accType);
    public List<BlinkCustomerModel> getAllData() throws FrameworkCheckedException;

}
