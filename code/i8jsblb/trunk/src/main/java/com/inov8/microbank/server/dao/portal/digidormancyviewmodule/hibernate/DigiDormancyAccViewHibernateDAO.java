package com.inov8.microbank.server.dao.portal.digidormancyviewmodule.hibernate;


import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.digidormancyaccountmodule.DigiDormancyAccountViewModel;
import com.inov8.microbank.server.dao.portal.digidormancyviewmodule.DigiDormancyAccountViewDAO;

public class DigiDormancyAccViewHibernateDAO
        extends BaseHibernateDAO<DigiDormancyAccountViewModel, Long, DigiDormancyAccountViewDAO>
        implements DigiDormancyAccountViewDAO {
}
