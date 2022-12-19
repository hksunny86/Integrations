package com.inov8.microbank.server.dao.mfsmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.integration.common.model.LimitModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.CustomerDetailsViewModel;
import com.inov8.microbank.server.dao.mfsmodule.CustomerDetailsCommandDAO;
import com.inov8.ola.server.service.limit.LimitManager;
import com.inov8.ola.util.LimitTypeConstants;
import com.inov8.ola.util.ReasonConstants;
import com.inov8.ola.util.TransactionTypeConstants;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Inov8 on 4/11/2018.
 */
public class CustomerDetailsCommandHibernateDAO
        extends BaseHibernateDAO<CustomerDetailsViewModel,Long,CustomerDetailsCommandDAO>
        implements CustomerDetailsCommandDAO{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public List getCustomerDetails(AppUserModel appUserModel) throws FrameworkCheckedException {
        String sql="SELECT " +
                "C.NAME AS CUSTOMER_NAME," +
                "C.FATHER_HUSBAND_NAME," +
                "APP.MOTHER_MAIDEN_NAME," +
                "C.GENDER," +
                "C.BIRTH_PLACE," +
                "       ACTYPE.NAME AS ACCOUNT_TYPE," +
                "       SEG.NAME AS SEGMENT," +
                "       CTY.NAME AS CITY_NAME," +
                "       TAX.NAME AS TAX_REGION_NAME," +
                "       TAX.FED," +
                "APP.NIC_EXPIRY_DATE," +
                "APP.DOB," +
                "       REGSTATE.NAME AS REGISTRATION_STATE," +
                "       ACSTATE.NAME AS ACCOUNT_STATE" +
                "  FROM APP_USER APP," +
                "       CUSTOMER C," +
                "       CUSTOMER_ADDRESSES ADR," +
                "       ADDRESS AD," +
                "       CITY CTY," +
                "       TAX_REGIME TAX," +
                "       OLA_CUSTOMER_ACCOUNT_TYPE ACTYPE," +
                "       SEGMENT SEG," +
                "       REGISTRATION_STATE REGSTATE," +
                "       ACCOUNT_STATE ACSTATE" +
                " WHERE     C.CUSTOMER_ID = APP.CUSTOMER_ID" +
                "       AND C.CUSTOMER_ID = ADR.CUSTOMER_ID(+)" +
                "       AND ADR.ADDRESS_ID = AD.ADDRESS_ID(+)" +
                "       AND AD.CITY_ID = CTY.CITY_ID(+)" +
                "       AND C.TAX_REGIME_ID = TAX.TAX_REGIME_ID(+)" +
                "       AND C.CUSTOMER_ACCOUNT_TYPE_ID = ACTYPE.CUSTOMER_ACCOUNT_TYPE_ID(+)" +
                "       AND C.SEGMENT_ID = SEG.SEGMENT_ID(+)" +
                "       AND APP.REGISTRATION_STATE_ID = REGSTATE.REGISTRATION_STATE_ID(+)" +
                "       AND APP.ACCOUNT_STATE_ID = ACSTATE.ACCOUNT_STATE_ID(+)" +
                "       AND APP.MOBILE_NO = '" + appUserModel.getMobileNo() + "'" +
                "       AND ADR.ADDRESS_TYPE_ID(+) = "+1 +
                "       AND APP.REGISTRATION_STATE_ID = 3" +
                " AND APP.APP_USER_TYPE_ID= "+2 +
                "       AND APPLICANT_TYPE_ID(+) = "+1;
        //
        List customerDtails=jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> temp=jdbcTemplate.queryForList(sql);
        Map<String,Object> temp1 = temp.get(0);
        List<Object> list = new ArrayList<Object>(temp1.values());
        //
        return list;
    }

    @Override
    public Double getConsumedBalanceByDateRange(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId)
    {
        try{
            Calendar c = Calendar.getInstance();
            c.setTime(endDate);
            c.add(Calendar.DATE, 1);
            endDate = c.getTime();
            DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
            String dateStr = format.format(startDate) ;
            String endStr = format.format(endDate);

            String accountColumn = "";
            if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
                accountColumn = "l.accountId";
            }else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
                accountColumn = "l.toAccountId";
            }

            String hql = "SELECT SUM(l.transactionAmount)"
                    + " FROM LedgerModel l"
                    + " INNER JOIN l.relationReasonIdReasonModel reason"
                    + " WHERE " + accountColumn + " = " + accountId
                    + " AND reason.reasonId <> "+ ReasonConstants.REVERSAL
                    + " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
					/*added by mudassir: */
                    + " AND reason.reasonId <> "+ ReasonConstants.BULK_PAYMENT
                    + " AND reason.reasonId <> "+ ReasonConstants.SETTLEMENT
                    + " AND reason.reasonId <> "+ ReasonConstants.ROLLBACK_WALKIN_CUSTOMER
                    + " AND reason.reasonId <> "+ ReasonConstants.REVERSE_BILL_PAYMENT
                    + " AND reason.reasonId <> "+ ReasonConstants.FUND_CUSTOMER_BB_CORE_AC
                    + " AND l.transactionTime >='" + dateStr+ "' "
                    + " AND l.transactionTime <'" + endStr+ "' ";

            // Handler Limit Changes
            if(handlerId != null && handlerId.longValue() > 0){
                hql += " AND l.handlerId = "+ handlerId
                        + " AND l.excludeLimitForHandler <> "+ Boolean.TRUE;
            }else{
                hql += " AND l.excludeLimit <> "+ Boolean.TRUE;
            }

            List<Object> objs = this.getHibernateTemplate().find(hql);
            if(objs != null && objs.size() > 0){
                Double result = (Double)objs.get(0);
                if(result == null){
                    result = Double.valueOf(0d);
                }
                return result;
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Double getConsumedBalanceByDateRangeForIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId) {
        try{
            Calendar c = Calendar.getInstance();
            c.setTime(endDate);
            c.add(Calendar.DATE, 1);
            endDate = c.getTime();
            DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
            String dateStr = format.format(startDate) ;
            String endStr = format.format(endDate);

            String accountColumn = "";
            if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
                accountColumn = "l.accountId";
            }else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
                accountColumn = "l.toAccountId";
            }

            String hql = "SELECT SUM(l.transactionAmount)"
                    + " FROM LedgerModel l"
                    + " INNER JOIN l.relationReasonIdReasonModel reason"
                    + " WHERE " + accountColumn + " = " + accountId
                    + " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
                    /*added by mudassir: */
                    + " AND reason.reasonId = "+ com.inov8.microbank.common.util.ReasonConstants.CUSTOMER_BB_TO_CORE_AC
                    + " AND l.transactionTime >='" + dateStr+ "' "
                    + " AND l.transactionTime <'" + endStr+ "' ";

            // Handler Limit Changes
            if(handlerId != null && handlerId.longValue() > 0){
                hql += " AND l.handlerId = "+ handlerId
                        + " AND l.excludeLimitForHandler <> "+ Boolean.TRUE;
            }else{
                hql += " AND l.excludeLimit <> "+ Boolean.TRUE;
            }

            List<Object> objs = this.getHibernateTemplate().find(hql);
            if(objs != null && objs.size() > 0){
                Double result = (Double)objs.get(0);
                if(result == null){
                    result = Double.valueOf(0d);
                }
                return result;
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Double getConsumedBalanceByDateRangeForAgentIBFT(Long accountId, Long transactionTypeId, Date startDate, Date endDate, Long handlerId) {
        try{
            Calendar c = Calendar.getInstance();
            c.setTime(endDate);
            c.add(Calendar.DATE, 1);
            endDate = c.getTime();
            DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");
            String dateStr = format.format(startDate) ;
            String endStr = format.format(endDate);

            String accountColumn = "";
            if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
                accountColumn = "l.accountId";
            }else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
                accountColumn = "l.toAccountId";
            }

            String hql = "SELECT SUM(l.transactionAmount)"
                    + " FROM LedgerModel l"
                    + " INNER JOIN l.relationReasonIdReasonModel reason"
                    + " WHERE " + accountColumn + " = " + accountId
                    + " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
                    /*added by mudassir: */
                    + " AND reason.reasonId = "+ com.inov8.microbank.common.util.ReasonConstants.AGENT_IBFT
                    + " AND l.transactionTime >='" + dateStr+ "' "
                    + " AND l.transactionTime <'" + endStr+ "' ";

            // Handler Limit Changes
            if(handlerId != null && handlerId.longValue() > 0){
                hql += " AND l.handlerId = "+ handlerId
                        + " AND l.excludeLimitForHandler <> "+ Boolean.TRUE;
            }else{
                hql += " AND l.excludeLimit <> "+ Boolean.TRUE;
            }

            List<Object> objs = this.getHibernateTemplate().find(hql);
            if(objs != null && objs.size() > 0){
                Double result = (Double)objs.get(0);
                if(result == null){
                    result = Double.valueOf(0d);
                }
                return result;
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Double getDailyConsumedBalance(Long accountId, Long transactionTypeId, Date date, Long handlerId)
    {
        try{
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");

            String accountColumn = "";
            if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
                accountColumn = "l.accountId";
            }else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
                accountColumn = "l.toAccountId";
            }

            String hql = "SELECT SUM(l.transactionAmount)"
                    + " FROM LedgerModel l"
                    + " INNER JOIN l.relationReasonIdReasonModel reason"
                    + " WHERE " + accountColumn + " = " + accountId
                    + " AND reason.reasonId <> "+ ReasonConstants.REVERSAL
                    + " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
					/*added by mudassir: */
                    + " AND reason.reasonId <> "+ ReasonConstants.BULK_PAYMENT
                    + " AND reason.reasonId <> "+ ReasonConstants.SETTLEMENT
                    + " AND reason.reasonId <> "+ ReasonConstants.ROLLBACK_WALKIN_CUSTOMER
                    + " AND reason.reasonId <> "+ ReasonConstants.REVERSE_BILL_PAYMENT
                    + " AND reason.reasonId <> "+ ReasonConstants.FUND_CUSTOMER_BB_CORE_AC
                    + " AND day(l.transactionTime) ="+cal.get(Calendar.DATE)+" AND month(l.transactionTime) ="+(cal.get(Calendar.MONTH)+1)+" AND year(l.transactionTime) ="+cal.get(Calendar.YEAR);

            // Handler Limit Changes
            if(handlerId != null && handlerId.longValue() > 0){
                hql += " AND l.handlerId = "+ handlerId
                        + " AND l.excludeLimitForHandler <> "+ Boolean.TRUE;
            }else{
                hql += " AND l.excludeLimit <> "+ Boolean.TRUE;
            }

            List<Object> objs = this.getHibernateTemplate().find(hql);
            if(objs != null && objs.size() > 0){
                Double result = (Double)objs.get(0);
                if(result == null){
                    result = Double.valueOf(0d);
                }
                return result;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Double getDailyConsumedBalanceForIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId) {
        try{
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");

            String accountColumn = "";
            if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
                accountColumn = "l.accountId";
            }else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
                accountColumn = "l.toAccountId";
            }

            String hql = "SELECT SUM(l.transactionAmount)"
                    + " FROM LedgerModel l"
                    + " INNER JOIN l.relationReasonIdReasonModel reason"
                    + " WHERE " + accountColumn + " = " + accountId
                    + " AND reason.reasonId <> "+ ReasonConstants.REVERSAL
                    + " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
                    /*added by mudassir: */
                    + " AND reason.reasonId = "+ com.inov8.microbank.common.util.ReasonConstants.CUSTOMER_BB_TO_CORE_AC
                    + " AND day(l.transactionTime) ="+cal.get(Calendar.DATE)+" AND month(l.transactionTime) ="+(cal.get(Calendar.MONTH)+1)+" AND year(l.transactionTime) ="+cal.get(Calendar.YEAR);

            // Handler Limit Changes
            if(handlerId != null && handlerId.longValue() > 0){
                hql += " AND l.handlerId = "+ handlerId
                        + " AND l.excludeLimitForHandler <> "+ Boolean.TRUE;
            }else{
                hql += " AND l.excludeLimit <> "+ Boolean.TRUE;
            }

            List<Object> objs = this.getHibernateTemplate().find(hql);
            if(objs != null && objs.size() > 0){
                Double result = (Double)objs.get(0);
                if(result == null){
                    result = Double.valueOf(0d);
                }
                return result;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Double getDailyConsumedBalanceForAgentIBFT(Long accountId, Long transactionTypeId, Date date, Long handlerId) {
        try{
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            DateFormat format = new SimpleDateFormat("dd/MMM/yyyy");

            String accountColumn = "";
            if(transactionTypeId == TransactionTypeConstants.DEBIT.longValue()){
                accountColumn = "l.accountId";
            }else if(transactionTypeId == TransactionTypeConstants.CREDIT.longValue()){
                accountColumn = "l.toAccountId";
            }

            String hql = "SELECT SUM(l.transactionAmount)"
                    + " FROM LedgerModel l"
                    + " INNER JOIN l.relationReasonIdReasonModel reason"
                    + " WHERE " + accountColumn + " = " + accountId
                    + " AND reason.reasonId <> "+ ReasonConstants.REVERSAL
                    + " AND (l.isReversal is null OR l.isReversal = "+ Boolean.FALSE + ")"
                    /*added by mudassir: */
                    + " AND reason.reasonId = "+ com.inov8.microbank.common.util.ReasonConstants.AGENT_IBFT
                    + " AND day(l.transactionTime) ="+cal.get(Calendar.DATE)+" AND month(l.transactionTime) ="+(cal.get(Calendar.MONTH)+1)+" AND year(l.transactionTime) ="+cal.get(Calendar.YEAR);

            // Handler Limit Changes
            if(handlerId != null && handlerId.longValue() > 0){
                hql += " AND l.handlerId = "+ handlerId
                        + " AND l.excludeLimitForHandler <> "+ Boolean.TRUE;
            }else{
                hql += " AND l.excludeLimit <> "+ Boolean.TRUE;
            }

            List<Object> objs = this.getHibernateTemplate().find(hql);
            if(objs != null && objs.size() > 0){
                Double result = (Double)objs.get(0);
                if(result == null){
                    result = Double.valueOf(0d);
                }
                return result;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LimitModel> getLimitsByCustomerAccountType(Long customerAccountTypeId)throws FrameworkCheckedException{
        List<LimitModel> resultList = new ArrayList<LimitModel>();
        try{
            String hql = "FROM LimitModel limit WHERE limit.relationCustomerAccountTypeIdCustomerAccountTypeModel.customerAccountTypeId = ? ";

            resultList = (List<LimitModel>)this.getHibernateTemplate().find(hql, customerAccountTypeId);


        }catch(Exception ex){
            logger.error(ex.getMessage(), ex);
        }

        return resultList;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
