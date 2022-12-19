package com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.ClsPendingAccountOpeningModel;
import com.inov8.microbank.server.service.clspendingblinkcustomermodule.dao.ClsPendingAccountOpeningDAO;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ClsPendingAccountOpeningHibernateDAO extends BaseHibernateDAO<ClsPendingAccountOpeningModel, Long, ClsPendingAccountOpeningDAO>
        implements ClsPendingAccountOpeningDAO {

    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public ClsPendingAccountOpeningModel loadExistingPendingAccountOpeningSafRepo(ClsPendingAccountOpeningModel clsPendingAccountOpeningModel) throws FrameworkCheckedException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT * FROM CLS_PENDING_ACCOUNT_OPENING");
//        sb.append(" where MOBILE_NO= "+ clsPendingAccountOpeningModel.getMobileNo());
//        clsPendingAccountOpeningModel = (ClsPendingAccountOpeningModel) jdbcTemplate.queryForObject(sb.toString(),new ClsPendingAccountOpeningModel());
//        return clsPendingAccountOpeningModel;


        DetachedCriteria criteria = DetachedCriteria.forClass(ClsPendingAccountOpeningModel.class);
        criteria.add(Restrictions.eq("mobileNo", clsPendingAccountOpeningModel.getMobileNo()));
        criteria.add(Restrictions.eq("caseID", clsPendingAccountOpeningModel.getCaseID()));

        List<ClsPendingAccountOpeningModel> list = getHibernateTemplate().findByCriteria(criteria);

        ClsPendingAccountOpeningModel clsPendingAccountOpeningModel1 = null;

        if (list != null && !list.isEmpty()) {
            clsPendingAccountOpeningModel1 = list.get(0);
        }

        return clsPendingAccountOpeningModel1;

    }


    @Override
    public ClsPendingAccountOpeningModel loadExistingPendingAccountOpening(ClsPendingAccountOpeningModel clsPendingAccountOpeningModel) throws FrameworkCheckedException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT * FROM CLS_PENDING_ACCOUNT_OPENING");
//        sb.append(" where MOBILE_NO= "+ clsPendingAccountOpeningModel.getMobileNo());
//        clsPendingAccountOpeningModel = (ClsPendingAccountOpeningModel) jdbcTemplate.queryForObject(sb.toString(),new ClsPendingAccountOpeningModel());
//        return clsPendingAccountOpeningModel;


        DetachedCriteria criteria = DetachedCriteria.forClass(ClsPendingAccountOpeningModel.class);
        criteria.add(Restrictions.eq("relationAppUserModel.appUserId", clsPendingAccountOpeningModel.getAppUserId()));
        List<ClsPendingAccountOpeningModel> list = getHibernateTemplate().findByCriteria(criteria);

        ClsPendingAccountOpeningModel clsPendingAccountOpeningModel1 = null;

        if (list != null && !list.isEmpty()) {
            clsPendingAccountOpeningModel1 = list.get(0);
        }

        return clsPendingAccountOpeningModel1;

    }

    public ClsPendingAccountOpeningModel loadClsPendingAccountOpeningByMobileByQuery(final String mobileNo) {
        ClsPendingAccountOpeningModel clsPendingAccountOpeningModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM ClsPendingAccountOpeningModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.mobileNo = :mobileNo ");
        String[] paramNames = {"mobileNo"};
        Object[] values = {mobileNo};
        try {
            List<ClsPendingAccountOpeningModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if (CollectionUtils.isNotEmpty(userList)) {
                clsPendingAccountOpeningModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clsPendingAccountOpeningModel;
    }

    @Override
    public List<ClsPendingAccountOpeningModel> loadAllPendingAccountApprovedSmsAlertRequired() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM CLS_PENDING_ACCOUNT_OPENING");
        sb.append(" where IS_COMPLETED=1");
        sb.append(" AND ACCOUNT_STATE_ID=3");
        sb.append(" AND REGISTRATION_STATE_ID=3");
        sb.append(" AND IS_SMS_REQUIRED=1");
        List<ClsPendingAccountOpeningModel> list = (List<ClsPendingAccountOpeningModel>) jdbcTemplate.query(sb.toString(), new ClsPendingAccountOpeningModel());
        return list;
    }

    @Override
    public List<ClsPendingAccountOpeningModel> loadAllPendingAccountApprovedUpdateAMA() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM CLS_PENDING_ACCOUNT_OPENING");
        sb.append(" where IS_COMPLETED=1");
        sb.append(" AND ACCOUNT_STATE_ID=3");
        sb.append(" AND REGISTRATION_STATE_ID=3");
        sb.append(" AND ACC_OPENED_BY_AMA=1");
        sb.append(" AND UPDATE_AMA=0");
        List<ClsPendingAccountOpeningModel> list = (List<ClsPendingAccountOpeningModel>) jdbcTemplate.query(sb.toString(), new ClsPendingAccountOpeningModel());
        return list;

    }
}
