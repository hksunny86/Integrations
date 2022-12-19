package com.inov8.ola.server.dao.blinkcustomerlimit.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.AdvanceSalaryLoanModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.BlinkCustomerLimitModel;
import com.inov8.ola.server.dao.blinkcustomerlimit.BlinkCustomerDAO;
import com.inov8.ola.server.dao.limit.LimitDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.faces.view.facelets.FaceletException;
import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class BlinkCustomerHibernateDAO extends
        BaseHibernateDAO<BlinkCustomerLimitModel, Long, BlinkCustomerDAO>
        implements BlinkCustomerDAO {

    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    public BlinkCustomerLimitModel getLimitsByCustomerAccountType(Long customerAccountTypeId, Long accountId, Long transactionType, Long limitType) throws FrameworkCheckedException {
        logger.info("Start of LimitHibernateDAO.getLimitsByCustomerAccountType() at Time :: " + new Date());
        BlinkCustomerLimitModel blinkCustomerLimitModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(BlinkCustomerLimitModel.class);
        criteria.add( Restrictions.eq("customerAccTypeId", customerAccountTypeId));
        criteria.add( Restrictions.eq("accountId", accountId));
        criteria.add( Restrictions.eq("transactionType", transactionType));
        criteria.add( Restrictions.eq("limitType", limitType));

        List<BlinkCustomerLimitModel> list = getHibernateTemplate().findByCriteria(criteria);

        logger.info("Query for BlinkCustomerLimitHibernateDAO.getLimitByTransactionType() :: " + criteria.toString());
//        List<BlinkCustomerLimitModel> resultList =  getHibernateTemplate().find(stringBuilder.toString());
        if (list != null&&!list.isEmpty()) {
            blinkCustomerLimitModel = list.get(0);
        }
        logger.info("End of LimitHibernateDAO.getLimitsByCustomerAccountType() at Time :: " + new Date());
        return blinkCustomerLimitModel;
    }

    @Override
    public void insertData(BlinkCustomerLimitModel model) throws FrameworkCheckedException {
        String query = "INSERT INTO BLINK_CUSTOMER_LIMIT (LIMIT_ID,MINIMUM,MAXIMUM,CUSTOMER_ACC_TYPE_ID,IS_APPLICABLE,CREATED_BY,UPDATED_BY,CREATED_ON,UPDATED_ON,LIMIT_TYPE,CUSTOMER_ID,TRANSACTION_TYPE,ACCOUNT_ID)" +
                " values (DEBIT_CARD_CHARGES_SREPO_SEQ.nextval,\'" + model.getMinimum() + "\'," + model.getMaximum() + ",\'" + model.getCustomerAccTypeId() + "\'," + model.getIsApplicable() + ",\'" + model.getCreatedBy() + "\',\'" + model.getUpdatedBy() + "\',SYSDATE" + ",SYSDATE," + model.getLimitType() + "," + model.getCustomerId() + "," + model.getTransactionType() + "," + model.getAccountId() + ")";

        jdbcTemplate.execute((query));
    }

    public void setDataSource(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
