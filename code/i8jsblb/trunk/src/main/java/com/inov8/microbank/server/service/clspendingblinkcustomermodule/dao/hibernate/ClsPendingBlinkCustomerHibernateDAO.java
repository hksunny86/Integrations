package com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ClsPendingBlinkCustomerModel;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingBlinkCustomerDAO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ClsPendingBlinkCustomerHibernateDAO extends BaseHibernateDAO<ClsPendingBlinkCustomerModel, Long, ClsPendingBlinkCustomerDAO> implements ClsPendingBlinkCustomerDAO  {

    private JdbcTemplate jdbcTemplate;


    @Override
    public List<ClsPendingBlinkCustomerModel> loadAllPendingAccount() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM CLS_PENDING_BLINK_CUSTOMER");
        sb.append(" where CLS_BOT_STATUS=1");
        sb.append(" AND IS_COMPLETED=0");
        List<ClsPendingBlinkCustomerModel> list = (List<ClsPendingBlinkCustomerModel>) jdbcTemplate.query(sb.toString(), new ClsPendingBlinkCustomerModel());
        return list;
    }

    @Override
    public void updatePendingAccountSafRepo(ClsPendingBlinkCustomerModel model) throws FrameworkCheckedException {

    }

    @Override
    public ClsPendingBlinkCustomerModel loadExistingPendingAccountOpeningSafRepo(ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel) throws FrameworkCheckedException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT * FROM CLS_PENDING_BLINK_CUSTOMER");
//        sb.append(" where MOBILE_NO= "+ clsPendingBlinkCustomerModel.getMobileNo());
//        clsPendingBlinkCustomerModel = (ClsPendingBlinkCustomerModel) jdbcTemplate.queryForObject(sb.toString(),new ClsPendingBlinkCustomerModel());
//        return clsPendingBlinkCustomerModel;
        DetachedCriteria criteria = DetachedCriteria.forClass(ClsPendingBlinkCustomerModel.class);
        criteria.add( Restrictions.eq("mobileNo", clsPendingBlinkCustomerModel.getMobileNo()));
        criteria.add( Restrictions.eq("caseID", clsPendingBlinkCustomerModel.getCaseID()));

        List<ClsPendingBlinkCustomerModel> list = getHibernateTemplate().findByCriteria(criteria);

        ClsPendingBlinkCustomerModel clsPendingBlinkCustomerModel1 = null;

        if(list != null && !list.isEmpty()) {
            clsPendingBlinkCustomerModel1 = list.get(0);
        }

        return clsPendingBlinkCustomerModel1;
    }


    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


}
