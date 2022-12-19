package com.inov8.microbank.server.service.suppliermodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SupplierBankInfoModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */
public interface SupplierBankInfoManager
{

  BaseWrapper loadSupplierBankInfo(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;
  
  SearchBaseWrapper loadSupplierBankInfo(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException;
  
  SearchBaseWrapper searchSupplierBankInfo(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException;
  
  BaseWrapper createSupplierBankInfo(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;
  
  BaseWrapper updateSupplierBankInfo(BaseWrapper baseWrapper) throws
  FrameworkCheckedException;
  
  SupplierBankInfoModel getSupplierBankInfoModel(SupplierBankInfoModel example) throws FrameworkCheckedException;



}
