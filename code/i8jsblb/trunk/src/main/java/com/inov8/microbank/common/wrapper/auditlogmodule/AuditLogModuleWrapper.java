package com.inov8.microbank.common.wrapper.auditlogmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.AuditLogModel;

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
public interface AuditLogModuleWrapper
    extends BaseWrapper
{

  public void setFailureReasonFailureReasonModel(AuditLogModel
                                                 failureReasonFailureReasonModel);

  public AuditLogModel getFailureReasonFailureReasonModel();

}
