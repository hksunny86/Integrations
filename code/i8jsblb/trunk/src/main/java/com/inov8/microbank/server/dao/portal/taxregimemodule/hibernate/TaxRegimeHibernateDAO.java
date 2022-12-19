package com.inov8.microbank.server.dao.portal.taxregimemodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TaxRegimeModel;
import com.inov8.microbank.server.dao.portal.taxregimemodule.TaxRegimeDAO;

public class TaxRegimeHibernateDAO extends 
    BaseHibernateDAO<TaxRegimeModel, Long, TaxRegimeDAO>
    implements TaxRegimeDAO
{

}
