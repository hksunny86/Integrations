package com.inov8.microbank.server.dao.safrepo.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.MerchantDiscountCardModel;
import com.inov8.microbank.common.model.VCFileModel;
import com.inov8.microbank.server.dao.safrepo.MerchantDiscountCardDAO;
import com.inov8.microbank.server.dao.safrepo.VCFileDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

public class MerchantDiscountCardHibernateDAO extends BaseHibernateDAO<MerchantDiscountCardModel,Long, MerchantDiscountCardDAO> implements MerchantDiscountCardDAO {

    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
