package com.inov8.microbank.server.dao.thirdpartcashoutmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MiddlewareRetryAdviceModel;
import com.inov8.microbank.common.model.ThirdPartyRetryAdviceModel;
import com.inov8.microbank.server.dao.messagingmodule.MiddlewareRetryAdviceDAO;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.ThirdPartyRetryAdviceDao;

/**
 * Created by Attique on 9/8/2018.
 */
public class ThirdPartyRetryAdviceHibernateDAO extends BaseHibernateDAO<ThirdPartyRetryAdviceModel, Long, ThirdPartyRetryAdviceDao> implements ThirdPartyRetryAdviceDao {
}
