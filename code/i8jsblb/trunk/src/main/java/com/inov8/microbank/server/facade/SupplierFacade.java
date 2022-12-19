package com.inov8.microbank.server.facade;

import com.inov8.microbank.server.service.suppliermodule.SupplierBankInfoManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierUserManager;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface SupplierFacade
    extends SupplierManager, SupplierUserManager, SupplierBankInfoManager
{
}
