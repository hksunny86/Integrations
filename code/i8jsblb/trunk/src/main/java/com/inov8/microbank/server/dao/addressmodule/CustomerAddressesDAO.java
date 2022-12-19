package com.inov8.microbank.server.dao.addressmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CustomerAddressesModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: </p>
 *
 * @author Usman Ashraf
 * @version 1.0
 */



public interface CustomerAddressesDAO
    extends BaseDAO<CustomerAddressesModel, Long>
{
    public CustomerAddressesModel findAddressByCustomerIdAndAddressType(Long customerId, Long addressType) throws FrameworkCheckedException;
}
