package com.inov8.microbank.disbursement.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.disbursement.dao.DisbursementFileInfoViewDAO;
import com.inov8.microbank.disbursement.model.DisbursementFileInfoViewModel;

/**
 * Created by AtieqRe on 2/12/2017.
 */
public class DisbursementFileInfoViewHibernateDAO
        extends BaseHibernateDAO<DisbursementFileInfoViewModel, Long, DisbursementFileInfoViewDAO>
        implements DisbursementFileInfoViewDAO {
}
