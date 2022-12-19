package com.inov8.microbank.fonepay.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.webservice.vo.WebServiceVO;
import com.inov8.microbank.fonepay.common.FonePayLogModel;
import com.inov8.microbank.fonepay.dao.FonePayLogDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class FonePayLogHibernateDAO extends BaseHibernateDAO<FonePayLogModel, Long, FonePayLogDAO>
implements FonePayLogDAO{

    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean validateApiGeeRRN(WebServiceVO webServiceVO) throws FrameworkCheckedException {
        StringBuilder sqlBuilder = new StringBuilder(400);
        sqlBuilder.append("SELECT COUNT(*) FROM FONEPAY_INTEGERATION_LOG ");
        sqlBuilder.append(" WHERE RRN = '").append(webServiceVO.getRetrievalReferenceNumber()).append("'");
        sqlBuilder.append(" AND TRUNC(CREATED_ON) = TRUNC(SYSDATE)");
        logger.info("APIGEE RRN Validation: " + sqlBuilder.toString());
        int result = jdbcTemplate.queryForInt(sqlBuilder.toString());
        if(result > 0)
            return false;

        return true;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
