package com.inov8.microbank.server.dao.blbnewreports;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.blbnewreports.DigiDebitCardAnnualReportViewModel;
import com.inov8.microbank.common.model.portal.blbnewreports.DigiPostedRdvReportViewModel;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DigiPostedRdvHibernateDAO extends BaseHibernateDAO<DigiPostedRdvReportViewModel,Long, DigiPostedRdvDAO> implements DigiPostedRdvDAO{
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
