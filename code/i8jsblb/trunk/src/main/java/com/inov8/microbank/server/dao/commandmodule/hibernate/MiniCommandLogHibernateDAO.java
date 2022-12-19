package com.inov8.microbank.server.dao.commandmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MiniCommandLogModel;
import com.inov8.microbank.server.dao.commandmodule.MiniCommandLogDAO;

/**
 * 
 * @author Jawwad Farooq
 * @date August 31, 2007
 *
 */


public class MiniCommandLogHibernateDAO 
extends BaseHibernateDAO<MiniCommandLogModel,Long,MiniCommandLogDAO> 
implements MiniCommandLogDAO
{

}