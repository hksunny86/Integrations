package com.inov8.microbank.server.dao.auditlogmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.auditlogmodule.AuditLogListViewModel;
import com.inov8.microbank.server.dao.auditlogmodule.AuditLogListViewDAO;


public class AuditLogListViewHibernateDAO extends BaseHibernateDAO<AuditLogListViewModel,Long,AuditLogListViewDAO>
   implements AuditLogListViewDAO{

}
