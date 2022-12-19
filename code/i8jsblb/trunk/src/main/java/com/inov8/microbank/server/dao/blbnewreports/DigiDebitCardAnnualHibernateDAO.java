package com.inov8.microbank.server.dao.blbnewreports;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
    import com.inov8.microbank.common.model.portal.blbnewreports.DigiDebitCardAnnualReportViewModel;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DigiDebitCardAnnualHibernateDAO extends BaseHibernateDAO<DigiDebitCardAnnualReportViewModel,Long, DigiDebitCardAnnualDAO> implements DigiDebitCardAnnualDAO{
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
