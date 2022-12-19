package com.inov8.microbank.debitcard.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.debitcard.dao.DebitCardExportDataViewDAO;
import com.inov8.microbank.debitcard.model.DebitCardExportDataViewModel;

public class DebitCardExportDataViewHibernateDAO extends
        BaseHibernateDAO<DebitCardExportDataViewModel,Long,DebitCardExportDataViewDAO>
        implements DebitCardExportDataViewDAO
{
}
