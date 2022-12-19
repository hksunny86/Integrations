package com.inov8.microbank.server.service.customermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CustomerRemitterModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

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

public interface CustTransManager
{
  SearchBaseWrapper searchCustTrans(SearchBaseWrapper searchBaseWrapper) throws
      FrameworkCheckedException;

  BaseWrapper saveOrUpdate(BaseWrapper baseWrapper) throws
      FrameworkCheckedException;

  WorkFlowWrapper saveCustomerAndUser(WorkFlowWrapper wrapper) throws
      FrameworkCheckedException;

  SearchBaseWrapper loadCustomer(SearchBaseWrapper searchBaseWrapper) throws
  FrameworkCheckedException ;

  BaseWrapper loadCustomer(BaseWrapper baseWrapper) throws
  FrameworkCheckedException ;

  SearchBaseWrapper searchCustomer(SearchBaseWrapper searchBaseWrapper)throws
  FrameworkCheckedException ;

  public BaseWrapper searchCustomerByMobile(BaseWrapper wrapper) throws FrameworkCheckedException ;

  public BaseWrapper searchCustomerByInitialAppFormNo(BaseWrapper wrapper)
		throws FrameworkCheckedException;
  
  void saveOrUpdateCustomerRemitter(List<CustomerRemitterModel> customerRemitterModelList) throws FrameworkCheckedException;

}
