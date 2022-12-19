package com.inov8.microbank.tax.dao.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.FilerRateConfigModel;
import com.inov8.microbank.common.model.OfflineBillersConfigModel;
import com.inov8.microbank.tax.dao.OfflineBillersConfigDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class OfflineBillersConfigHibernateDAO extends
        BaseHibernateDAO<OfflineBillersConfigModel, Long, OfflineBillersConfigDAO>
        implements OfflineBillersConfigDAO{

    private JdbcTemplate jdbcTemplate;

    @Override
    public OfflineBillersConfigModel loadOfflineBillersModelByProductId(String productId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM OFFLINE_BILLERS_CONFIG WHERE PRODUCT_ID = " + productId);

        List<OfflineBillersConfigModel> list = (List<OfflineBillersConfigModel>) jdbcTemplate.query(sql.toString(), new OfflineBillersConfigModel());
        if(!list.isEmpty())
            return list.get(0);
        return null;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
