package com.inov8.microbank.server.dao.configurations.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AmaBvsConfigurationsModel;
import com.inov8.microbank.common.model.ClsDebitCreditBlockModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.server.dao.configurations.AmaBvsConfigurationsDAO;
import com.inov8.microbank.server.dao.customermodule.RegistrationStateDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class AmaBvsConfigurationsHibernateDAO extends
        BaseHibernateDAO<AmaBvsConfigurationsModel, Long, AmaBvsConfigurationsDAO>
        implements AmaBvsConfigurationsDAO{
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<AmaBvsConfigurationsModel> loadAmaBvsConfigurationsModel() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM AMA_BVS_CONFIGURATIONS WHERE AMA_BVS_CONFIGURATIONS_ID IS NOT NULL");
        List<AmaBvsConfigurationsModel> list = (List<AmaBvsConfigurationsModel>) jdbcTemplate.query(sb.toString(), new AmaBvsConfigurationsModel());
        logger.info(list.size());
        return list;
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
