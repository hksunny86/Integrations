package com.inov8.microbank.server.dao.safrepo.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.VCFileModel;
import com.inov8.microbank.common.model.WalletSafRepoModel;
import com.inov8.microbank.server.dao.safrepo.VCFileDAO;
import com.inov8.microbank.server.dao.safrepo.WalletSafRepoDAO;
import com.inov8.ola.util.ReasonConstants;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

public class VCFileHibernateDAO extends BaseHibernateDAO<VCFileModel,Long, VCFileDAO> implements VCFileDAO{

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<VCFileModel> loadAllToBeProcessedRecords(String transactionType) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM VC_FILE");
        sb.append(" where IS_COMPLETED = 0");
        sb.append(" AND IS_MATCHED = 1");
        sb.append(" AND TRANSACTION_TYPE = " + "\'" + transactionType + "\'");
        List<VCFileModel> list = (List<VCFileModel>) jdbcTemplate.query(sb.toString(), new VCFileModel());
        return list;
    }

    @Override
    public void updateVCFileModel(VCFileModel model) throws FrameworkCheckedException {
        String query = "UPDATE VC_FILE set IS_COMPLETED ="+model.getIsCompleted()
                + " ,MICROBANK_TRANSACTION_CODE = '" + model.getMicrobankTransactionCode() + "'"
                + " where IS_MATCHED = '" + model.getIsMatched() + "'";
        int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    @Override
    public Double getTotalBalance(String transactionType) throws Exception {
        String sql = "SELECT SUM(TRANSACTION_AMOUNT)"
        + " FROM VC_FILE"
        + " AND TRANSACTION_TYPE = " + transactionType
                + " AND IS_COMPLETED = " + 0L
                + " AND IS_MATCHED = " + 1L;

        List<Object> objs = this.jdbcTemplate.queryForList(sql,Object.class);
        if(objs != null && objs.size() > 0){
            Double result = null;
            if(objs.get(0) != null)
            {
                BigDecimal bigDecimal = (BigDecimal)objs.get(0);
                if(bigDecimal==null) {
                    result = Double.valueOf(0d);
                }
                result = bigDecimal.doubleValue();
            }
            if(result == null){
                result = Double.valueOf(0d);
            }
            return result;
        }
        return null;
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
