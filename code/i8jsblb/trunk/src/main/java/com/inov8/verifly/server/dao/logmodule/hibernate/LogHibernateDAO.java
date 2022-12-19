package com.inov8.verifly.server.dao.logmodule.hibernate;


import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.verifly.common.model.LogModel;
import com.inov8.verifly.server.dao.logmodule.LogDAO;

/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  07-Sep-2006
 *
 */

public class LogHibernateDAO extends
    BaseHibernateDAO<LogModel,Long,LogDAO> implements
    LogDAO
{
}
