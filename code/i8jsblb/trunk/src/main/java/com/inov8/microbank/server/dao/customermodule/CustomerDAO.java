package com.inov8.microbank.server.dao.customermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.BlinkCustomerModel;
import com.inov8.microbank.common.model.CustomerModel;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public interface CustomerDAO
    extends BaseDAO<CustomerModel, Long>
{
	public List<CustomerModel> findCustomersByAppUserIds(List<Long> inClauseOfAppUserIds)
			throws FrameworkCheckedException;
	public List<CustomerModel> getCustomerModelListByCustomerIDs(List<Long> customerIdList);
	public int updateCustomerToUpgradeCustomerAccount(Long customerId);
	public CustomerModel loadCustomerModelByCustomerId(Long customerId);
	public CustomerModel loadCustomerModelByMobileNo(String mobileNo);
	public List<CustomerModel> findCustomerByIsAccountUpdate();
}
