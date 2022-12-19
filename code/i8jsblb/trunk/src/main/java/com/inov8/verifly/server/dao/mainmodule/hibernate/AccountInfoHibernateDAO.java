package com.inov8.verifly.server.dao.mainmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.verifly.common.model.AccountInfoModel;
import com.inov8.verifly.server.dao.mainmodule.AccountInfoDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;

/**
 *
 * @author irfan mirza
 * @version 1.0
 * @date  07-Sep-2006
 *
 */


public class AccountInfoHibernateDAO extends
    BaseHibernateDAO<AccountInfoModel,Long,AccountInfoDAO> implements
    AccountInfoDAO

{
    private JdbcTemplate jdbcTemplate;
    @Override
    public int updateAccountInfoModelToCloseAccount(Long accountInfoId) {

        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String updatedOn = createNewDateFormat.formatDate(new Date());
        String query = "UPDATE ACCOUNT_INFO set IS_ACTIVE = 0,UPDATED_ON = '" + updatedOn + "'"
                + " WHERE ACCOUNT_INFO_ID = " + accountInfoId;

        int updatedRows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();

        return updatedRows;
    }

    @Override
    public AccountInfoModel getAccountInfoModel(Long customerId, Long paymentModeId) throws FrameworkCheckedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ACCOUNT_INFO WHERE IS_ACTIVE = " + 1L + " AND CUSTOMER_ID = " + customerId);
        if(paymentModeId != null && paymentModeId > 0)
            stringBuilder.append(" AND PAYMENT_MODE_ID = " + paymentModeId);
        return (AccountInfoModel) jdbcTemplate.queryForObject(stringBuilder.toString(),new AccountInfoModel());
    }

    @Override
    public int updateAccountInfoModel(Long customerId, Long paymentModeId, Long isMigrated) throws FrameworkCheckedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE ACCOUNT_INFO SET IS_MIGRATED = " + isMigrated + " WHERE CUSTOMER_ID = " + customerId);
        if(paymentModeId != null && paymentModeId > 0)
            stringBuilder.append(" AND PAYMENT_MODE_ID = " + paymentModeId);
        int updatedRows =this.getSession().createSQLQuery(String.valueOf(stringBuilder)).executeUpdate();
        return updatedRows;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


//	public AccountInfoModel saveOrUpdate(AccountInfoModel accountInfoModel) throws DataAccessException {
//		accountInfoModel =  super.saveOrUpdate(accountInfoModel);
//		super.getHibernateTemplate().flush();
//		return accountInfoModel;
//	}

}
