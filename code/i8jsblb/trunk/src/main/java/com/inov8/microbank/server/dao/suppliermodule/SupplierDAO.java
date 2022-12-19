package com.inov8.microbank.server.dao.suppliermodule;

import java.util.List;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.SupplierModel;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface SupplierDAO
    extends BaseDAO<SupplierModel, Long>
{
	
	public List getServicesAgainstSupplier(Long supplierId);

}
