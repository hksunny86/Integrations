package com.inov8.microbank.server.service.suppliermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.suppliermodule.SupplierWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface Supplier
{

  public SupplierWrapper verify(SupplierWrapper supplier,
                                WorkFlowWrapper workFlowWrapper) throws
      FrameworkCheckedException;

  public SupplierWrapper updateSupplier(SupplierWrapper supplier) throws
      FrameworkCheckedException;

  public SupplierWrapper rollback(SupplierWrapper transactions) throws
      FrameworkCheckedException;

}
