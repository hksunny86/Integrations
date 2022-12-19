package com.inov8.microbank.server.service.suppliermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.wrapper.suppliermodule.SupplierWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

public interface SupplierManager
{
  public SupplierWrapper getSupplierClassPath(SupplierWrapper supplierInfo) throws
      FrameworkCheckedException;

  SearchBaseWrapper loadSupplier(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadSupplier(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchSupplier(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createOrUpdateSupplier(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SupplierWrapper verify(SupplierWrapper supplierInfo,
                         WorkFlowWrapper workFlowWrapper) throws Exception;

  SupplierWrapper updateSupplier(SupplierWrapper supplierInfo) throws Exception;

  SupplierWrapper rollback(SupplierWrapper supplierInfo) throws Exception;
  
  public List getServicesAgainstSupplier(Long supplierId)throws
  FrameworkCheckedException;

}
