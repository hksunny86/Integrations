package com.inov8.microbank.debitcard.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.debitcard.dao.DebitCardMailingAddressDAO;
import com.inov8.microbank.debitcard.model.DebitCardMailingAddressModel;

public class DebitCardMailingAddressHibernateDAO
        extends BaseHibernateDAO<DebitCardMailingAddressModel,Long,DebitCardMailingAddressDAO> implements DebitCardMailingAddressDAO {
}
