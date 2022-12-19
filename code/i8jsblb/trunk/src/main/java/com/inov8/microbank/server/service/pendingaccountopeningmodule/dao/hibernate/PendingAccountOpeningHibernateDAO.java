package com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.model.AccountOpeningPendingSafRepoModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.server.dao.debitCardChargesmodule.DebitCardChargesDAO;
import com.inov8.microbank.server.service.pendingaccountopeningmodule.dao.PendingAccountOpeningDAO;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class PendingAccountOpeningHibernateDAO extends BaseHibernateDAO<AccountOpeningPendingSafRepoModel, Long, PendingAccountOpeningDAO> implements PendingAccountOpeningDAO {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<AccountOpeningPendingSafRepoModel> loadAllPendingAccount() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ACCOUNT_PENDING_SAF_REPO");
        sb.append(" where IS_COMPLETE=0");
        List<AccountOpeningPendingSafRepoModel> list = (List<AccountOpeningPendingSafRepoModel>) jdbcTemplate.query(sb.toString(), new AccountOpeningPendingSafRepoModel());
        return list;
    }


    @Override
    public void updatePendingAccountSafRepo(AccountOpeningPendingSafRepoModel model) throws FrameworkCheckedException {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String updatedOn = createNewDateFormat.formatDate(new Date());
        String query = "UPDATE ACCOUNT_PENDING_SAF_REPO SET UPDATED_ON = '" + updatedOn + "'" +
                ",IS_COMPLETE = '" + model.getCompleted() + "'" +
                " WHERE MOBILE_NO = " + model.getMobileNo();

        int rows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    @Override
    public AccountOpeningPendingSafRepoModel loadExistingPendingAccountOpeningSafRepo(AccountOpeningPendingSafRepoModel accountOpeningPendingSafRepoModel) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ACCOUNT_PENDING_SAF_REPO");
        sb.append(" where MOBILE_NO= "+ accountOpeningPendingSafRepoModel.getMobileNo());
        accountOpeningPendingSafRepoModel = (AccountOpeningPendingSafRepoModel) jdbcTemplate.queryForObject(sb.toString(),new AccountOpeningPendingSafRepoModel());
        return accountOpeningPendingSafRepoModel;
    }



    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


}
