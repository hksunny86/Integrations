package com.inov8.microbank.server.dao.messagingmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.RetryAdviceListSummaryViewModel;
import com.inov8.microbank.server.dao.messagingmodule.RetryAdviceListSummaryViewDAO;

public class RetryAdviceListSummaryViewHibernateDAO
		extends
		BaseHibernateDAO<RetryAdviceListSummaryViewModel, Long, RetryAdviceListSummaryViewDAO>
		implements RetryAdviceListSummaryViewDAO {

}
