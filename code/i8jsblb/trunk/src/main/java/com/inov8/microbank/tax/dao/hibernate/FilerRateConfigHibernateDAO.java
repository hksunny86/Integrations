package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionThresholdRateModel;
import com.inov8.microbank.common.model.FilerRateConfigModel;
import com.inov8.microbank.tax.dao.FilerRateConfigDAO;
import com.inov8.microbank.tax.dao.WHTConfigDAO;
import com.inov8.microbank.tax.model.WHTConfigModel;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class FilerRateConfigHibernateDAO extends
        BaseHibernateDAO<FilerRateConfigModel, Long, FilerRateConfigDAO>
        implements FilerRateConfigDAO{

    private JdbcTemplate jdbcTemplate;

    @Override
    public FilerRateConfigModel loadFilerRateConfigModelByFiler(Long filer) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM FILER_RATE_CONFIG WHERE IS_FILER = " + filer);

        List<FilerRateConfigModel> list = (List<FilerRateConfigModel>) jdbcTemplate.query(sql.toString(), new FilerRateConfigModel());
        if(!list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public List<FilerRateConfigModel> loadFilerRateConfigModelByNonFiler() {
        Session session=this.getSession();
        Criteria criteria=session.createCriteria(FilerRateConfigModel.class);
        Criterion isActive= Restrictions.eq("isFiler",false);
        criteria.add(isActive);
        return criteria.list();
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
