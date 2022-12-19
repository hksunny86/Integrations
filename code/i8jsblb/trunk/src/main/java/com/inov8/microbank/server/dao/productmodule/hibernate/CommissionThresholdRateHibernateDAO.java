package com.inov8.microbank.server.dao.productmodule.hibernate;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.CommissionThresholdRateModel;
import com.inov8.microbank.server.dao.productmodule.CommissionThresholdRateDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class CommissionThresholdRateHibernateDAO extends BaseHibernateDAO<CommissionThresholdRateModel, Long, BaseDAO<CommissionThresholdRateModel,Long>>
        implements CommissionThresholdRateDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CommissionThresholdRateModel> loadCommissionThresholdRateList(CommissionThresholdRateModel commissionThresholdRateModel) {
        logger.info("Starting CommissionThresholdRateHibernateDAO.loadCommissionThresholdRateList(commissionThresholdRateModel) for Product :: "
                + commissionThresholdRateModel.getProductId() + " at Time :: " + new Date());
        long isDeleted = 0l;
        if(commissionThresholdRateModel.getIsDeleted())
            isDeleted = 1l;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM COMMISSION_THRESHOLD_RATE WHERE IS_DELETED = " + isDeleted);
        if(commissionThresholdRateModel.getProductId() != null)
            sql.append(" AND (PRODUCT_ID IS NULL OR PRODUCT_ID = " + commissionThresholdRateModel.getProductId() + " )");

        if(commissionThresholdRateModel.getDeviceTypeId() != null)
            sql.append(" AND (DEVICE_TYPE_ID IS NULL OR DEVICE_TYPE_ID = " + commissionThresholdRateModel.getDeviceTypeId() + " )");

        if(commissionThresholdRateModel.getSegmentId() != null)
            sql.append(" AND (SEGMENT_ID IS NULL OR SEGMENT_ID = " + commissionThresholdRateModel.getSegmentId() + " )");

        if(commissionThresholdRateModel.getDistributorId() != null)
            sql.append(" AND (DISTRIBUTOR_ID IS NULL OR DISTRIBUTOR_ID = " + commissionThresholdRateModel.getDistributorId() + " )");

        if(commissionThresholdRateModel.getLimitTypeId() != null)
            sql.append(" AND (LIMIT_TYPE_ID IS NULL OR LIMIT_TYPE_ID = " + commissionThresholdRateModel.getLimitTypeId() + " )");

        logger.info("Query to CommissionRateHibernateDAO.loadCommissionRateList(): " + sql);
        List<CommissionThresholdRateModel> list = (List<CommissionThresholdRateModel>) jdbcTemplate.query(sql.toString(), new CommissionThresholdRateModel());
        int size = 0;
        if(!list.isEmpty())
            size = list.size();
        logger.info("Total " + size + " Records Fetched in CommissionThresholdRateHibernateDAO.loadCommissionThresholdRateList for Product :: "
                + commissionThresholdRateModel.getProductId() + " at Time :: " + new Date());
        return list;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
