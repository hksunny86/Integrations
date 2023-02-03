package com.inov8.verifly.server.dao.mainmodule.hibernate;


import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.verifly.common.model.VeriflyConfigurationModel;
import com.inov8.verifly.server.dao.mainmodule.VeriflyConfigurationDAO;

/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  07-Sep-2006
 *
 */

public class VeriflyConfigurationHibernateDAO extends
    BaseHibernateDAO<VeriflyConfigurationModel,Long,VeriflyConfigurationDAO> implements
    VeriflyConfigurationDAO
{
}