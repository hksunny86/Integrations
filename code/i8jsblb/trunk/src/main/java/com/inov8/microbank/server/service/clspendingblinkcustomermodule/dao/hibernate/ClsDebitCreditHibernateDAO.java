package com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ClsDebitCreditBlockModel;
import com.inov8.microbank.hra.airtimetopup.model.ConversionRateModel;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsDebitCreditDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ClsDebitCreditHibernateDAO extends BaseHibernateDAO<ClsDebitCreditBlockModel, Long, ClsDebitCreditDAO>
        implements ClsDebitCreditDAO{

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ClsDebitCreditBlockModel> loadClsDebitCreditModel() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM CLS_DEBIT_CREDIT_BLOCKED WHERE CLS_DEBIT_CREDIT_BLOCKED_ID IS NOT NULL");
        List<ClsDebitCreditBlockModel> list = (List<ClsDebitCreditBlockModel>) jdbcTemplate.query(sb.toString(), new ClsDebitCreditBlockModel());
        logger.info(list.size());
        return list;
    }


    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
