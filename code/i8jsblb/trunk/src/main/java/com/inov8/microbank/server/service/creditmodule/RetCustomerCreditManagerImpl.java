package com.inov8.microbank.server.service.creditmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;

public class RetCustomerCreditManagerImpl
    implements RetCustomerCreditManager
{
  private RetailerContactDAO retailerContactDAO;

  public BaseWrapper updateRetToCustContactBalance(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    WorkFlowWrapper workFlowWrapper = (WorkFlowWrapper) baseWrapper;

    //Updates the 'Retailer Credit Register -- Following line changed by Maqsood Shahzad
    RetailerContactModel retailerContactModel = workFlowWrapper.getTransactionModel().getFromRetContactIdRetailerContactModel();
        
    retailerContactModel = this.retailerContactDAO.saveOrUpdate(
        retailerContactModel);
    workFlowWrapper.setToRetailerContactModel(retailerContactModel);

    return workFlowWrapper;
  }

  public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO)
  {
    this.retailerContactDAO = retailerContactDAO;
  }

}
