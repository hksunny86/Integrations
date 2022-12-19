package com.inov8.microbank.server.dao.failurelogmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.server.dao.failurelogmodule.AuditLogDAO;

/**
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

public class AuditLogHibernateDAO
    extends BaseHibernateDAO<AuditLogModel, Long, AuditLogDAO>
    implements AuditLogDAO
{

}
