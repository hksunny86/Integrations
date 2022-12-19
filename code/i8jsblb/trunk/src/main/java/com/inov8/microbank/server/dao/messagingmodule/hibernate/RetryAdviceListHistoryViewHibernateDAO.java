package com.inov8.microbank.server.dao.messagingmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.RetryAdviceListHistoryViewModel;
import com.inov8.microbank.server.dao.messagingmodule.RetryAdviceListHistoryViewDAO;

public class RetryAdviceListHistoryViewHibernateDAO
		extends
		BaseHibernateDAO<RetryAdviceListHistoryViewModel, Long, RetryAdviceListHistoryViewDAO>
		implements RetryAdviceListHistoryViewDAO {

}
