package com.inov8.microbank.hra.paymtnc.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.hra.paymtnc.dao.PayMtncRequestDAO;
import com.inov8.microbank.hra.paymtnc.model.PayMtncRequestModel;

public class PayMtncRequestHibernateDAO extends BaseHibernateDAO<PayMtncRequestModel,Long,PayMtncRequestDAO>
     implements PayMtncRequestDAO{
}
