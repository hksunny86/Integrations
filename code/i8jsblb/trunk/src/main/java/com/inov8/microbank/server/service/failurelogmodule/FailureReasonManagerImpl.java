package com.inov8.microbank.server.service.failurelogmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.FailureReasonModel;
import com.inov8.microbank.server.dao.failurelogmodule.FailureReasonDAO;

/**
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class FailureReasonManagerImpl
    implements FailureReasonManager
{
  private FailureReasonDAO failureReasonDAO;

  public BaseWrapper loadFailureReasonByName(BaseWrapper baseWrapper)
  {
    CustomList<FailureReasonModel>
        list = this.failureReasonDAO.findByExample( (FailureReasonModel)
        baseWrapper.getBasePersistableModel());
    baseWrapper.setBasePersistableModel(list.getResultsetList().get(0));
    return baseWrapper;
  }

  public void setFailureReasonDAO(FailureReasonDAO failureReasonDAO)
  {
    this.failureReasonDAO = failureReasonDAO;
  }

}
