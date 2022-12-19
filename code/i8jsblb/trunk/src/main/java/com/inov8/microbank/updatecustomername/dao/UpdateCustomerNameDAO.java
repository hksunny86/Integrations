package com.inov8.microbank.updatecustomername.dao;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.updatecustomername.model.UpdateCustomerNameModel;

import java.util.List;

public interface UpdateCustomerNameDAO extends BaseDAO<UpdateCustomerNameModel,Long> {

    List<UpdateCustomerNameModel> getCustomerNames();
    UpdateCustomerNameModel getCustomer(String cnic);
    UpdateCustomerNameModel getCustomerNameAndNameUpdate(String cnic,Boolean accupdate);

}
