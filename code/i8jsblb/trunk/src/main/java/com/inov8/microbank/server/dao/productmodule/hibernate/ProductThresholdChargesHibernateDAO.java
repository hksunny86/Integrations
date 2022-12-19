package com.inov8.microbank.server.dao.productmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.LimitRuleModel;
import com.inov8.microbank.common.model.ProductChargesRuleModel;
import com.inov8.microbank.common.model.ProductThresholdChargesModel;
import com.inov8.microbank.server.dao.productmodule.ProductThresholdChargesDAO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ProductThresholdChargesHibernateDAO extends BaseHibernateDAO<ProductThresholdChargesModel, Long, BaseDAO<ProductThresholdChargesModel,Long>>
        implements ProductThresholdChargesDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ProductThresholdChargesModel> loadProductThresholdCharges(String hql, Object[] parameterList) throws FrameworkCheckedException {
//        StringBuilder stringBuilder = new StringBuilder(500);
//        stringBuilder.append("SELECT * FROM PRODUCT_THRESHOLD_CHARGES WHERE IS_DELETED = " + 0);
//        if(productThresholdChargesModel.getProductId() != null)
//            stringBuilder.append(" AND (PRODUCT_ID IS NULL OR PRODUCT_ID = " + productThresholdChargesModel.getProductId() + " )");
//
//        List<ProductThresholdChargesModel> list = (List<ProductThresholdChargesModel>) jdbcTemplate.query(stringBuilder.toString(),new ProductThresholdChargesModel());
        List<ProductThresholdChargesModel> list = getHibernateTemplate().find(hql,parameterList);
        return list;
    }

    @Override
    public void updateAndSaveProductThresholdCharges(List<ProductThresholdChargesModel> existingList, List<ProductThresholdChargesModel> newList) {
        if(CollectionUtils.isNotEmpty(existingList)){
            saveOrUpdateCollection(existingList);
        }

        // without following flush, DB trigger execution order is disturbed
        getHibernateTemplate().flush();

        if(CollectionUtils.isNotEmpty(newList)){
            saveOrUpdateCollection(newList);
        }
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
