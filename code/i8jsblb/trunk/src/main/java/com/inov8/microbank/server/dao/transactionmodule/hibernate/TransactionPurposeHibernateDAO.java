package com.inov8.microbank.server.dao.transactionmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.TransactionPurposeModel;
import com.inov8.microbank.server.dao.transactionmodule.TransactionPurposeDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class TransactionPurposeHibernateDAO
        extends BaseHibernateDAO<TransactionPurposeModel,Long,TransactionPurposeDAO>
    implements TransactionPurposeDAO{

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int loadTrxPurposeModelByCode(String code) {
        StringBuilder sb = new StringBuilder("SELECT TRANS_PURPOSE_ID FROM TRANSACTION_PURPOSE WHERE CODE = '" + code + "'");
        return jdbcTemplate.queryForInt(sb.toString());
    }

    @Override
    public List<TransactionPurposeModel> loadTrxPurposeByCode() {
        List<TransactionPurposeModel> transactionPurposeModel = null;

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TransactionPurposeModel.class);
        detachedCriteria.add(Restrictions.eq("isActive", true));
        detachedCriteria.addOrder(Order.asc("code"));

        transactionPurposeModel = getHibernateTemplate().findByCriteria( detachedCriteria );

        return transactionPurposeModel;
    }
}
