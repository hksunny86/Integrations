package com.inov8.microbank.server.dao.failurelogmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.FailureReasonModel;
import com.inov8.microbank.server.dao.failurelogmodule.FailureReasonDAO;

/**
 *
 * @author Rizwan-ur-Rehman
 * @version 1.0
 */

public class FailureReasonHibernateDAO
    extends BaseHibernateDAO<FailureReasonModel, Long, FailureReasonDAO>
    implements FailureReasonDAO
{
}
