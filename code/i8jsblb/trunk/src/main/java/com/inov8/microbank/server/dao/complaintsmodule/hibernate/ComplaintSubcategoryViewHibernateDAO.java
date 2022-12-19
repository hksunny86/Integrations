package com.inov8.microbank.server.dao.complaintsmodule.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ComplaintSubcategoryViewModel;
import com.inov8.microbank.server.dao.complaintsmodule.ComplaintSubcategoryViewDAO;

public class ComplaintSubcategoryViewHibernateDAO
		extends
		BaseHibernateDAO<ComplaintSubcategoryViewModel, Long, ComplaintSubcategoryViewDAO>
		implements ComplaintSubcategoryViewDAO {

	private final static Log logger = LogFactory
			.getLog(ComplaintSubcategoryViewHibernateDAO.class);

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ComplaintSubcategoryViewModel> searchComplaintNature(
			ComplaintSubcategoryViewModel model) {

		Session	session=	this.getSession();
		
		Criteria queryCriteria=	session.createCriteria(ComplaintSubcategoryViewModel.class);
		
		if (model.getComplaintCategoryId() != null) {
			queryCriteria.add(Restrictions.eq("complaintCategoryId",
					model.getComplaintCategoryId()));
		}
		if (model.getComplaintSubcategoryName() != null) {
			queryCriteria.add(Restrictions.ilike(
					"complaintSubcategoryName", model.getComplaintSubcategoryName(),MatchMode.ANYWHERE));
		}
		if (model.getLevel0AssigneeId() != null) {
			queryCriteria.add(Restrictions.eq("level0AssigneeId",
					model.getLevel0AssigneeId()));
		}
		if (model.getLevel1AssigneeId() != null) {
			queryCriteria.add(Restrictions.eq("level1AssigneeId",
					model.getLevel1AssigneeId()));
		}
		if (model.getLevel2AssigneeId() != null) {
			queryCriteria.add(Restrictions.eq("level2AssigneeId",
					model.getLevel2AssigneeId()));
		}
		if (model.getLevel3AssigneeId() != null) {
			queryCriteria.add(Restrictions.eq("level3AssigneeId",
					model.getLevel3AssigneeId()));
		}
		if (model.getIsActive() != null) {
			queryCriteria.add(Restrictions.eq("isActive", model.getIsActive()));
		}
		
		return queryCriteria.list();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}