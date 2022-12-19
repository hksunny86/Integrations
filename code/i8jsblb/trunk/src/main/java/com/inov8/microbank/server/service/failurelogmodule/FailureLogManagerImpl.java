package com.inov8.microbank.server.service.failurelogmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.server.dao.failurelogmodule.AuditLogDAO;

/**
 *
 * @author not attributable
 * @version 1.0
 */
public class FailureLogManagerImpl
    implements FailureLogManager
{
  private AuditLogDAO failureLogDAO;
  private FailureReasonManager failureReasonManager;
  public FailureLogManagerImpl()
  {
  }

  /**
   * saveLog
   *
   * @param auditLogModuleWrapper AuditLogModuleWrapper
   * @return AuditLogModuleWrapper
   * @throws FrameworkCheckedException   *
   */
  /*
  public BaseWrapper saveLog(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    AuditLogModel auditLogModel = (AuditLogModel)baseWrapper.getBasePersistableModel();
    baseWrapper.setBasePersistableModel( this.failureLogDAO.saveOrUpdate( auditLogModel )) ;
    return baseWrapper;
  }
*/
  public BaseWrapper auditLogRequiresNewTransaction(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
    AuditLogModel auditLogModel = (AuditLogModel)baseWrapper.getBasePersistableModel();
    baseWrapper.setBasePersistableModel( this.failureLogDAO.saveOrUpdate( auditLogModel )) ;
    return baseWrapper;

  }


  public void setFailureLogDAO(AuditLogDAO failureLogDAO)
  {

    this.failureLogDAO = failureLogDAO;
  }

  public void setFailureReasonManager(FailureReasonManager failureReasonManager)
  {
    this.failureReasonManager = failureReasonManager;
  }
}
