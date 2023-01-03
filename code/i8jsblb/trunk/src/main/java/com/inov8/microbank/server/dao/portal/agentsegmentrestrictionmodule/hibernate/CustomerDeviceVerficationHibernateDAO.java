package com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.CustomerUDIDDeviceVerification;
import com.inov8.microbank.server.dao.portal.agentsegmentrestrictionmodule.CustomerDeviceVerificationDAO;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class CustomerDeviceVerficationHibernateDAO extends BaseHibernateDAO<CustomerUDIDDeviceVerification, Long, CustomerDeviceVerificationDAO> implements CustomerDeviceVerificationDAO {
    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
