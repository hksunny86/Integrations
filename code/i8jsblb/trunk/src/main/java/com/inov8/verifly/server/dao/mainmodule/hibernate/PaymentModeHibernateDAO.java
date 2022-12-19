package com.inov8.verifly.server.dao.mainmodule.hibernate;


import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.verifly.common.model.VfPaymentModeModel;
import com.inov8.verifly.server.dao.mainmodule.PaymentModeDAO;

/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  07-Sep-2006
 *
 */

public class PaymentModeHibernateDAO extends
    BaseHibernateDAO<VfPaymentModeModel,Long,PaymentModeDAO> implements
    PaymentModeDAO
{
}
