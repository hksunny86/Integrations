package com.inov8.microbank.server.service.creditmodule;

import java.util.Date;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.DistributorContactModel;
import com.inov8.microbank.common.model.RetailerContactModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.dao.distributormodule.DistributorContactDAO;
import com.inov8.microbank.server.dao.retailermodule.RetailerContactDAO;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DistRetCreditManagerImpl
    implements DistRetCreditManager
{
  private DistributorContactDAO distributorContactDAO;
  private RetailerContactDAO retailerContactDAO;

  public BaseWrapper updateDistributorRetailerContactBalance(BaseWrapper
      baseWrapper) throws
      FrameworkCheckedException
  {
    WorkFlowWrapper workFlowWrapper = (WorkFlowWrapper) baseWrapper;

    DistributorContactModel distributorContactModel = workFlowWrapper.
        getDistributorContactModel();
    distributorContactModel.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
    distributorContactModel.setUpdatedOn(new Date());
    distributorContactModel = this.distributorContactDAO.saveOrUpdate(
        distributorContactModel);
    workFlowWrapper.setDistributorContactModel(distributorContactModel);

    RetailerContactModel retailerContact = workFlowWrapper.
        getToRetailerContactModel();
    retailerContact.setUpdatedBy(UserUtils.getCurrentUser().getAppUserId());
    retailerContact.setUpdatedOn(new Date());
    retailerContact = this.retailerContactDAO.saveOrUpdate(retailerContact);
    workFlowWrapper.setToRetailerContactModel(retailerContact);

    return workFlowWrapper;
  }

  public BaseWrapper loadDistributorContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    DistributorContactModel distributorContactModel = this.
        distributorContactDAO.findByPrimaryKey(
            baseWrapper.getBasePersistableModel().
            getPrimaryKey());
    baseWrapper.setBasePersistableModel(distributorContactModel);
    return baseWrapper;

  }

  public BaseWrapper loadRetailerContact(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    RetailerContactModel retailerContactModel = this.retailerContactDAO.
        findByPrimaryKey(
            baseWrapper.getBasePersistableModel().getPrimaryKey());
    baseWrapper.setBasePersistableModel(retailerContactModel);
    return baseWrapper;

  }

  public void setDistributorContactDAO(DistributorContactDAO
                                       distributorContactDAO)
  {
    this.distributorContactDAO = distributorContactDAO;
  }

  public void setRetailerContactDAO(RetailerContactDAO retailerContactDAO)
  {
    this.retailerContactDAO = retailerContactDAO;
  }

}
