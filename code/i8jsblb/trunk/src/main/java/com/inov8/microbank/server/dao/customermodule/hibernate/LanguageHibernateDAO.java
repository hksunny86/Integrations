package com.inov8.microbank.server.dao.customermodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.LanguageModel;
import com.inov8.microbank.server.dao.customermodule.LanguageDAO;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class LanguageHibernateDAO
    extends
    BaseHibernateDAO<LanguageModel, Long, LanguageDAO>
    implements
    LanguageDAO
{
}