package com.inov8.microbank.server.dao.jsloansmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.JSLoansModel;
import com.inov8.microbank.server.dao.jsloansmodule.JSLoansDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JSLoansHibernateDAO extends BaseHibernateDAO<JSLoansModel, Long, JSLoansDAO> implements JSLoansDAO{
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public JSLoansModel loadJSLoansByMobileNumber(String mobileNo) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(JSLoansModel.class);
        criteria.add( Restrictions.eq("mobileNo", mobileNo));
        criteria.add( Restrictions.eq("isCompleted", Boolean.FALSE));
        criteria.addOrder(Order.desc("createdOn"));
        List<JSLoansModel> list = getHibernateTemplate().findByCriteria(criteria);

        JSLoansModel jsLoansModel = null;

        if(list != null && !list.isEmpty()) {
            jsLoansModel = list.get(0);
        }

        return jsLoansModel;
    }
}
