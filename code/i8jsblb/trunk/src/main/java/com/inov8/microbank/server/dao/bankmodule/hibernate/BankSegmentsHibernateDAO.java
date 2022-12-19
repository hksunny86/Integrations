package com.inov8.microbank.server.dao.bankmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.BankSegmentsModel;
import com.inov8.microbank.server.dao.bankmodule.BankSegmentsDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.*;

public class BankSegmentsHibernateDAO extends BaseHibernateDAO<BankSegmentsModel,Long,BankSegmentsDAO>
        implements BankSegmentsDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> notAllowedSegments(String imd) throws FrameworkCheckedException {
        List<Map<String, Object>> result;

        String query="SELECT IMD from BANK_SEGMENTS where IMD is not NULL";

        logger.info("Query to validate Segment in MemberBankHibernateDAO.segmentAllowed() :: " + query.toString() );
            result = jdbcTemplate.queryForList(query);

        return result;
    }


    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }
}