package com.inov8.microbank.server.service.salariedaccountprofitmodule.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.SalariedAccountProfitModel;
import com.inov8.microbank.server.service.salariedaccountprofitmodule.dao.SalariedAccountProfitDebitCreditDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class SalariedAccountProfitDebitCreditHibernateDAO extends BaseHibernateDAO<SalariedAccountProfitModel, Long, SalariedAccountProfitDebitCreditDAO> implements SalariedAccountProfitDebitCreditDAO {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SalariedAccountProfitModel> loadAllSalariedAccountProfitDebitCredit() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM SALARIED_ACCOUNT_PROFIT ");
        List<SalariedAccountProfitModel> list = (List<SalariedAccountProfitModel>) jdbcTemplate.query(sb.toString(),new SalariedAccountProfitModel());
        return list;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
