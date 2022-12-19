package com.inov8.microbank.server.dao.safrepo.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionStakeholderModel;
import com.inov8.microbank.common.model.WalletSafRepoModel;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.verifly.common.model.AccountInfoModel;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class WalletSafRepoHibernateDAO extends BaseHibernateDAO<WalletSafRepoModel,Long,WalletSafRepoDAO> implements WalletSafRepoDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<WalletSafRepoModel> loadAllToBeProcessedRecords() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM WALLET_SAF_REPO");
        sb.append(" where IS_COMPLETE=0");
        List<WalletSafRepoModel> list = (List<WalletSafRepoModel>) jdbcTemplate.query(sb.toString(), new WalletSafRepoModel());
        return list;
    }

    @Override
    public WalletSafRepoModel loadWalletSafRepo(String transactionCode) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM WALLET_SAF_REPO");
        sb.append(" where TRANSACTION_CODE =" +transactionCode);
        List<WalletSafRepoModel> list = (List<WalletSafRepoModel>) jdbcTemplate.query(sb.toString(), new WalletSafRepoModel());
        if(!list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public void updateWalletSafRepo(WalletSafRepoModel model) throws FrameworkCheckedException {
        String query = "UPDATE WALLET_SAF_REPO set IS_COMPLETE ="+model.getIsComplete()
                + " ,TRANSACTION_STATUS = '" + model.getTransactionStatus() + "'"
                +" WHERE TRANSACTION_CODE = " + model.getTransactionCode();

        int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    @Override
    public List<WalletSafRepoModel> updateWalletSafRepoStatus(WalletSafRepoModel model) throws FrameworkCheckedException {
        String query = "UPDATE WALLET_SAF_REPO set IS_COMPLETE ="+model.getIsComplete()
                + " ,TRANSACTION_STATUS = '" + model.getTransactionStatus() + "'"
                +" WHERE TRANSACTION_CODE = " + model.getTransactionCode();

        int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();

        return new ArrayList<>();
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
