package com.inov8.microbank.server.dao.thirdpartcashoutmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BOPCardIssuanceViewModel;
import com.inov8.microbank.server.dao.thirdpartcashoutmodule.BOPCardIssuanceViewDAO;
import org.hibernate.criterion.MatchMode;

public class BOPCardIssuanceViewHibernateDAO extends BaseHibernateDAO<BOPCardIssuanceViewModel, Long,BOPCardIssuanceViewDAO>
        implements BOPCardIssuanceViewDAO {
}
