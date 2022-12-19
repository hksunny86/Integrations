package com.inov8.microbank.updatecustomername.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import com.inov8.microbank.updatecustomername.vo.UpdateCustomerNameVo;

public interface UpdateCustomerNameManager {

    SearchBaseWrapper searchUpdateCustomerNames(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;
    UpdateCustomerNameModel getUpdateCustomer(String cnic);
    BaseWrapper saveOrUpdateCustomerName(BaseWrapper baseWrapper) throws FrameworkCheckedException;
    BaseWrapper updateCustomer(BaseWrapper baseWrapper, UpdateCustomerNameVo updateCustomerNameVo) throws FrameworkCheckedException;
}
