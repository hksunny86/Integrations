package com.inov8.microbank.server.dao.complaintsmodule.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ComplaintCategoryModel;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintCategoryDAO;

public class ComplaintCategoryHibernateDAO extends BaseHibernateDAO<ComplaintCategoryModel, Long, ComplaintCategoryDAO> implements ComplaintCategoryDAO {
	
	private final static Log logger = LogFactory.getLog(ComplaintCategoryHibernateDAO.class);
	

}