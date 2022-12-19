package com.inov8.microbank.updatecustomername.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;
import com.inov8.microbank.updatecustomername.service.UpdateCustomerNameManager;
import com.inov8.microbank.updatecustomername.vo.UpdateCustomerNameVo;

public class UpdateCustomerNameFacadeImpl implements UpdateCustomerNameFacade{

    UpdateCustomerNameManager updateCustomerNameManager;


    @Override
    public SearchBaseWrapper searchUpdateCustomerNames(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        return updateCustomerNameManager.searchUpdateCustomerNames(searchBaseWrapper);
    }

    @Override
    public UpdateCustomerNameModel getUpdateCustomer(String cnic) {
        return updateCustomerNameManager.getUpdateCustomer(cnic);
    }

    @Override
    public BaseWrapper saveOrUpdateCustomerName(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        return updateCustomerNameManager.saveOrUpdateCustomerName(baseWrapper);
    }

    @Override
    public BaseWrapper updateCustomer(BaseWrapper baseWrapper, UpdateCustomerNameVo updateCustomerNameVo) throws FrameworkCheckedException {
        return updateCustomerNameManager.updateCustomer(baseWrapper,updateCustomerNameVo);
    }

    public void setUpdateCustomerNameManager(UpdateCustomerNameManager updateCustomerNameManager) {
        this.updateCustomerNameManager = updateCustomerNameManager;
    }


}
