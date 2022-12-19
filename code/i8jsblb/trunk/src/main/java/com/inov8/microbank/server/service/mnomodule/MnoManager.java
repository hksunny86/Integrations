package com.inov8.microbank.server.service.mnomodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.SupplierModel;

import java.util.List;


/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public interface MnoManager
{
  SearchBaseWrapper loadMno(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper loadMno(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper searchMno(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper createOrUpdateMno(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;
  
  public List<SupplierModel> searchSupplierModels() throws
  FrameworkCheckedException;

  SearchBaseWrapper searchMnoByMnoUser(SearchBaseWrapper searchBaseWrapper) throws
          FrameworkCheckedException;

}
