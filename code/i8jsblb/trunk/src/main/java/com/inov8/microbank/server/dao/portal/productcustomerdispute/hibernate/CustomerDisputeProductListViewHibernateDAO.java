package com.inov8.microbank.server.dao.portal.productcustomerdispute.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.productcustomerdisputemodule.CustDisputeProductListViewModel;
import com.inov8.microbank.server.dao.portal.productcustomerdispute.CustomerDisputeProductListViewDAO;

public class CustomerDisputeProductListViewHibernateDAO
	extends BaseHibernateDAO<CustDisputeProductListViewModel, Long,CustomerDisputeProductListViewDAO>
	implements CustomerDisputeProductListViewDAO
{

}
