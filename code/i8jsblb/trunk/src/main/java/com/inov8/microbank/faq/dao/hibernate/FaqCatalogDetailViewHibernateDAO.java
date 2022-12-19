package com.inov8.microbank.faq.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.faq.dao.FaqCatalogDetailViewDAO;
import com.inov8.microbank.faq.model.FaqCatalogDetailViewModel;

public class FaqCatalogDetailViewHibernateDAO
        extends BaseHibernateDAO<FaqCatalogDetailViewModel, Long, FaqCatalogDetailViewDAO>
        implements FaqCatalogDetailViewDAO {

}
