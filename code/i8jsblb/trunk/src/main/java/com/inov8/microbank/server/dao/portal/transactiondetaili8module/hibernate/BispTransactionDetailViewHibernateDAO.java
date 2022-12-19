package com.inov8.microbank.server.dao.portal.transactiondetaili8module.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.BispTransactionDetailViewModel;
import com.inov8.microbank.server.dao.portal.transactiondetaili8module.BispTransactionDetailViewDAO;

public class BispTransactionDetailViewHibernateDAO extends BaseHibernateDAO<BispTransactionDetailViewModel, Long,BispTransactionDetailViewDAO>
        implements BispTransactionDetailViewDAO {
    @Override
    public SearchBaseWrapper searchBispTransactionDetail(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        return null;
    }
}
