package com.inov8.microbank.server.dao.portal.mfsaccountmodule.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.portal.mfsaccountmodule.MinorUserInfoListViewModel;
import com.inov8.microbank.server.dao.portal.mfsaccountmodule.MinorUserListViewDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class MinorUserInfoListViewHibernateDAO extends BaseHibernateDAO<MinorUserInfoListViewModel, Long, MinorUserListViewDAO>
        implements MinorUserListViewDAO{

    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
