package com.inov8.microbank.server.dao.settlementmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.VeriflyModel;
import com.inov8.microbank.server.dao.settlementmodule.VeriflyDAO;

public class VeriflyHibernateDAO
    extends BaseHibernateDAO<VeriflyModel, Long, VeriflyDAO>
    implements VeriflyDAO
{
}