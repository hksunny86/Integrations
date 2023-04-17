package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.MerchantAccountModel;

import java.util.List;

public interface MerchantAccountModelDAO extends BaseDAO<MerchantAccountModel, Long> {
    public MerchantAccountModel loadMerchantCustomerModelByMobileAndAccUpdate(String mobileNo, Long accUpdate);
    public MerchantAccountModel loadMerchantModelByBlinkCustomerId(Long accType);


}
