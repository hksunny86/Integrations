package com.inov8.microbank.common.wrapper.auditlogmodule;

import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AuditLogModel;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class AuditLogModuleWrapperImpl
    extends BaseWrapperImpl implements AuditLogModuleWrapper
{
  private AuditLogModel failureReasonFailureReasonModel;

  public AuditLogModuleWrapperImpl()
  {
  }

  public void setFailureReasonFailureReasonModel(AuditLogModel
                                                 failureReasonFailureReasonModel)
  {
    this.failureReasonFailureReasonModel = failureReasonFailureReasonModel;
  }

  public AuditLogModel getFailureReasonFailureReasonModel()
  {
    return failureReasonFailureReasonModel;
  }

}
