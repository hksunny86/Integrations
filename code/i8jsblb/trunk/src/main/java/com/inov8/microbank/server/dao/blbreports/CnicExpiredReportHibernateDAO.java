package com.inov8.microbank.server.dao.blbreports;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.blbreports.CnicExpiredViewModel;

public class CnicExpiredReportHibernateDAO extends BaseHibernateDAO<CnicExpiredViewModel, Long, CnicExpiredReportDAO> implements
        CnicExpiredReportDAO{
}
