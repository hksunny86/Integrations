package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.PaymentTypeModel;
import com.inov8.microbank.server.dao.mfsmodule.PaymentTypeDAO;

public class PaymentTypeHibernateDAO 
extends BaseHibernateDAO<PaymentTypeModel, Long, PaymentTypeDAO>
implements PaymentTypeDAO
{

}
