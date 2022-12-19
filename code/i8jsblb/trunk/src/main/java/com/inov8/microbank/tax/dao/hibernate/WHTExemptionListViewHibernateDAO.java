package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AccountCreditQueueLogModel;
import com.inov8.microbank.server.dao.messagingmodule.AccountCreditQueueLogDAO;
import com.inov8.microbank.tax.dao.WHTExemptionListViewDAO;
import com.inov8.microbank.tax.model.WHTExemptionListViewModel;

/**
 * Created by Zeeshan on 7/1/2016.
 */
public class WHTExemptionListViewHibernateDAO extends BaseHibernateDAO<WHTExemptionListViewModel, Long, WHTExemptionListViewDAO> implements WHTExemptionListViewDAO {
}
