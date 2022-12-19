package com.inov8.microbank.faq.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.faq.dao.FaqCatalogDAO;
import com.inov8.microbank.faq.model.FaqCatalogModel;

public class FaqCatalogHibernateDAO
        extends BaseHibernateDAO<FaqCatalogModel, Long, FaqCatalogDAO>
        implements FaqCatalogDAO {

}
