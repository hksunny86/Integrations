package com.inov8.microbank.server.dao.customermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CustomerRemitterModel;

import java.util.List;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 20018</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author WAQAR ASHRAF
 * @version 1.0
 *
 */

public interface CustomerRemitterDAO
    extends BaseDAO<CustomerRemitterModel, Long>
{
    public List<CustomerRemitterModel> getActiveCustomerRemitterModelByCustomerId(Long customerId) throws FrameworkCheckedException;
}
