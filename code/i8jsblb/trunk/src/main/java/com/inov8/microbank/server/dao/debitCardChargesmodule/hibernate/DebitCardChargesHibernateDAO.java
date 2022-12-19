package com.inov8.microbank.server.dao.debitCardChargesmodule.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.veriflymodule.DebitCardChargesSafRepoModel;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import com.inov8.microbank.server.dao.debitCardChargesmodule.DebitCardChargesDAO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class DebitCardChargesHibernateDAO extends BaseHibernateDAO<DebitCardChargesSafRepoModel, Long, DebitCardChargesDAO> implements DebitCardChargesDAO {

    private JdbcTemplate jdbcTemplate;


    @Override
    public List<DebitCardChargesSafRepoModel> loadAllDebitCardCharges() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM DEBIT_CARD_CHARGES_SAF_REPO");
        sb.append(" where IS_COMPLETED=0");
        List<DebitCardChargesSafRepoModel> list = (List<DebitCardChargesSafRepoModel>) jdbcTemplate.query(sb.toString(), new DebitCardChargesSafRepoModel());
        return list;
    }

    @Override
    public void updateDebitCardFeeDeductionSafRepo(DebitCardChargesSafRepoModel model) throws FrameworkCheckedException {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String updatedOn = createNewDateFormat.formatDate(new Date());
        String query = "UPDATE DEBIT_CARD_CHARGES_SAF_REPO SET UPDATED_ON = '" + updatedOn + "'" +
                ",TRANSACTION_STATUS = '" + model.getTransactionstatus() + "'" +
                ",IS_COMPLETED = '" + model.getIsCompleted() + "'" +
                " WHERE DEBIT_CARD_NO = " + model.getDebitCardNo();

        int rows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    @Override
    public DebitCardModel loadAllCardsOnRenewRequired(DebitCardChargesSafRepoModel debitCardModel) throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM DEBIT_CARD ");
        sb.append("WHERE ( (SYSDATE - CAST (ISSUANCE_DATE AS DATE) >= 365 ");
        sb.append("AND FEE_DEDUCTION_DATE IS NULL) ");
        sb.append("OR (FEE_DEDUCTION_DATE IS NOT NULL ");
        sb.append("AND SYSDATE - CAST (FEE_DEDUCTION_DATE AS DATE) >= 365)) ");
        sb.append(" AND CARD_STATUS_ID != " + CardConstantsInterface.CARD_STATUS_HOT);
        sb.append(" AND MOBILE_NO = " + "\'" + debitCardModel.getMobileNo() + "\'");
        List<DebitCardModel> list = (List<DebitCardModel>) jdbcTemplate.query(sb.toString(), new DebitCardModel());
        DebitCardModel debitCardModel1 = null;
        if (list != null && !list.isEmpty()) {
            debitCardModel1 = list.get(0);
        }

        return debitCardModel1;
    }


    @Override
    public DebitCardChargesSafRepoModel loadExistingDebitCardChargesSafRepe(DebitCardChargesSafRepoModel debitCardChargesSafRepoModel) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(DebitCardChargesSafRepoModel.class);
        Criterion criterionOne = Restrictions.eq("mobileNo", debitCardChargesSafRepoModel.getMobileNo());
        Criterion criterionTwo = Restrictions.eq("isCompleted", "0");
        Criterion criterionThree = Restrictions.eq("productId", debitCardChargesSafRepoModel.getProductId());

//        LogicalExpression expressionCriterion = Restrictions.and(criterionOne, criterionTwo);
        criteria.add(criterionOne);
        criteria.add(criterionTwo);
        criteria.add(criterionThree);
        List<DebitCardChargesSafRepoModel> list = getHibernateTemplate().findByCriteria(criteria);
        DebitCardChargesSafRepoModel appUserModel = null;
        if (list != null && !list.isEmpty()) {
            appUserModel = list.get(0);
        }

        return appUserModel;
    }

    public void createOrUpdateDebitCardChargesSafRepoRequiresNewTransaction(DebitCardChargesSafRepoModel model) throws FrameworkCheckedException {
        try {

            String query = "Insert into DEBIT_CARD_CHARGES_SAF_REPO (DEBIT_CARD_CHARGES_SAF_REPO_ID,DEBIT_CARD_NO,CNIC,MOBILE_NO,CARD_STATUS_ID,CARD_STATE_ID,PRDUCT_ID,CARD_TYPE_CONSTANT,DEBIT_CARD_ID,TRANSACTION_AMOUNT,TRANSACTION_DATE,TRANSACTION_STATUS,IS_COMPLETED,CREATED_ON,CREATED_BY,UPDATED_ON,UPDATED_BY)" +
                    " values (DEBIT_CARD_CHARGES_SREPO_SEQ.nextval,\'" + model.getDebitCardNo() + "\',\'" + model.getCnic() + "\',\'" + model.getMobileNo() + "\'," + model.getCardStatusId() + "," + model.getCardStateId() + "," + model.getProductId() + "," + model.getCardTypeConstant() + "," + model.getAccountId() + "," + model.getTransactionAmount() + ",SYSDATE, \'" + model.getTransactionstatus() + "\',"
                    + model.getIsCompleted() + ",SYSDATE," + model.getCreatedBy() + ",SYSDATE," + model.getCreatedBy() + ")";
            jdbcTemplate.execute(query);

        } catch (Exception e) {
            throw new FrameworkCheckedException(e.getLocalizedMessage(), e);
        }
    }

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
