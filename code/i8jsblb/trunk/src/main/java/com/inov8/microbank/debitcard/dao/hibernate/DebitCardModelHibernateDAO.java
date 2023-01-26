package com.inov8.microbank.debitcard.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.cardconfiguration.common.CardConstantsInterface;
import com.inov8.microbank.common.CreateNewDateFormat;
import com.inov8.microbank.debitcard.dao.DebitCardModelDAO;
import com.inov8.microbank.debitcard.model.DebitCardModel;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class DebitCardModelHibernateDAO extends BaseHibernateDAO<DebitCardModel,Long,DebitCardModelDAO> implements DebitCardModelDAO {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<DebitCardModel> getDebitCardModelByMobileAndNIC(String mobileNo, String nic) throws FrameworkCheckedException {
        DetachedCriteria criteria = DetachedCriteria.forClass(DebitCardModel.class);
        Criterion mobileCriterion = Restrictions.eq("mobileNo", mobileNo);
        Criterion nicCriterion = Restrictions.eq("cnic", nic);
        Criterion statusCriterion = Restrictions.eq("relationCardStatusIdCardStatusModel.cardStatusId", CardConstantsInterface.CARD_STATUS_REJECTED);
        //criteria.add( Restrictions.not(statusCriterion));
        criteria.add(Restrictions.or(mobileCriterion,nicCriterion));
        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(criteria);
        return list;
    }

    @Override
    public DebitCardModel getDebitCardModelByCardNumber(String cardNo) throws FrameworkCheckedException {
        DebitCardModel debitCardModel = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(DebitCardModel.class);
        criteria.add(Restrictions.eq("cardNo",cardNo));
        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(criteria);
        if(list != null && !list.isEmpty())
            debitCardModel = list.get(0);
        return debitCardModel;
    }

    @Override
    public List<DebitCardModel> getDebitCardModelByState(Long cardStateId) throws FrameworkCheckedException {
        List<DebitCardModel> debitCardList = null;
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DebitCardModel.class );
        detachedCriteria.add( Restrictions.eq( "relationCardStateIdCardStateModel.cardStateId", cardStateId) );
        debitCardList = getHibernateTemplate().findByCriteria( detachedCriteria );
        return debitCardList;
    }

    @Override
    public DebitCardModel getDebitCradModelByNicAndState(String cnic, Long cardStausId) throws FrameworkCheckedException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DebitCardModel.class );
        detachedCriteria.add( Restrictions.eq( "cnic", cnic) );
        //detachedCriteria.add( Restrictions.eq( "relationCardStatusIdCardStatusModel.cardStatusId", cardStausId) );
        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if(list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public DebitCardModel getDebitCardModelByCustomerAppUserId(Long appUserId) throws FrameworkCheckedException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DebitCardModel.class );
        detachedCriteria.add( Restrictions.eq( "relationAppUserIdAppUserModel.appUserId", appUserId) );
        detachedCriteria.add( Restrictions.in("relationCardStateIdCardStateModel.cardStateId", new Long[]{CardConstantsInterface.CARD_STATE_COLD,
                CardConstantsInterface.CARD_STATE_WARM}));
        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if(list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public DebitCardModel getDebitCardModelByDebitCardId(Long debitCardId) throws FrameworkCheckedException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DebitCardModel.class );
        detachedCriteria.add( Restrictions.eq( "debitCardId", debitCardId) );
        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if(list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public DebitCardModel getDebitCardModelByCustomerAppUserId(Long appUserId, Long cardStatus) throws FrameworkCheckedException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DebitCardModel.class );
        detachedCriteria.add( Restrictions.eq( "relationAppUserIdAppUserModel.appUserId", appUserId) );
        detachedCriteria.add( Restrictions.in("relationCardStatusIdCardStatusModel.cardStatusId",
                new Long[]{CardConstantsInterface.CARD_STATUS_IN_PROCESS}));
        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if(list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }

//    @Override
//    public DebitCardModel getDebitCardModelByAppUserId(Long appUserId) throws FrameworkCheckedException {
//        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DebitCardModel.class );
//        detachedCriteria.add( Restrictions.eq( "relationAppUserIdAppUserModel.appUserId", appUserId) );
//        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
//        if(list != null && !list.isEmpty())
//            return list.get(0);
//        return null;
//    }


    @Override
    public DebitCardModel getDebitCardModelByAppUserIdAndCardStateId(Long aapUserId, Long[] cardStateId) throws FrameworkCheckedException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( DebitCardModel.class );
        detachedCriteria.add( Restrictions.eq( "relationAppUserIdAppUserModel.appUserId", aapUserId) );
        Criterion criterion = Restrictions.in("relationCardStateIdCardStateModel.cardStateId",cardStateId);
        detachedCriteria.add( Restrictions.not(criterion));
        List<DebitCardModel> list = getHibernateTemplate().findByCriteria(detachedCriteria);
        if(list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }

    @Override
    public List<DebitCardModel> loadAllCardsOnRenewRequired() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM DEBIT_CARD ");
        sb.append("WHERE TRUNC(NVL(NEW_INSTALL_DATE_REISSUANCE,NEW_INSTALLMENT_DATE_ISSUANCE)) = TRUNC(SYSDATE) ");
        sb.append(" AND CARD_STATE_ID != " + CardConstantsInterface.CARD_STATE_HOT);
        sb.append(" AND IS_INSTALLMENT = 1");
        sb.append(" AND SYSDATE - NVL(REISSUANCE_DATE,ISSUANCE_DATE) < 365");

        List<DebitCardModel> list = (List<DebitCardModel>) jdbcTemplate.query(sb.toString(),new DebitCardModel());
        return list;
    }

    @Override
    public List<DebitCardModel> loadAllCardsOnRenewRequiredForAnnualFee() throws FrameworkCheckedException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM DEBIT_CARD ");
        sb.append("WHERE ( (SYSDATE - CAST (ISSUANCE_DATE AS DATE) >= 365 ");
        sb.append("AND FEE_DEDUCTION_DATE IS NULL) ");
        sb.append("OR (FEE_DEDUCTION_DATE IS NOT NULL ");
        sb.append("AND SYSDATE - CAST (FEE_DEDUCTION_DATE AS DATE) >= 365)) ");
        sb.append(" AND CARD_STATE_ID != " + CardConstantsInterface.CARD_STATE_HOT);
        sb.append(" AND NVL(IS_INSTALLMENT,0) = 0");

        sb.append(" UNION ALL ");

        sb.append("SELECT * FROM DEBIT_CARD ");
        sb.append("WHERE (TRUNC(NEW_INSTALLMENT_DATE_ANNUAL) = TRUNC(SYSDATE) ");
        sb.append("OR (TRUNC(ADD_MONTHS(NVL(REISSUANCE_DATE,ISSUANCE_DATE),12)) = TRUNC(SYSDATE) AND NEW_INSTALLMENT_DATE_ANNUAL IS NULL)) ");
        sb.append(" AND CARD_STATE_ID != " + CardConstantsInterface.CARD_STATE_HOT);
        sb.append(" AND IS_INSTALLMENT = 1");

        List<DebitCardModel> list = (List<DebitCardModel>) jdbcTemplate.query(sb.toString(),new DebitCardModel());
        return list;
    }

    @Override
    public List<DebitCardModel> loadAllCardsOnReIssuanceRequired() throws FrameworkCheckedException {
        Long status=1l;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM DEBIT_CARD ");
        sb.append("WHERE RE_ISSUANCE="+status);
        List<DebitCardModel> list = (List<DebitCardModel>) jdbcTemplate.query(sb.toString(),new DebitCardModel());
        return list;
    }

    @Override
    public void updateDebitCardFeeDeductionDate(DebitCardModel model) throws FrameworkCheckedException {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String updatedOn = createNewDateFormat.formatDate(new Date());
        String query = "UPDATE DEBIT_CARD SET UPDATED_ON = '" + updatedOn + "'" +
                ",FEE_DEDUCTION_DATE = '" + updatedOn + "'" +
                " WHERE DEBIT_CARD_ID = " + model.getDebitCardId();

        int rows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    @Override
    public void updateDebitCardFeeDeductionDateForAnnualFee(DebitCardModel model) throws FrameworkCheckedException {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String updatedOn = createNewDateFormat.formatDate(new Date());
        String feeDeductionDateAnnual = createNewDateFormat.getFormattedDate(model.getFeeDeductionDateAnnual());
//        String lastInstallmentDateAnnual = createNewDateFormat.formatDate(model.getLastInstallmentDateForAnnual());
//        String newInstallmentDateAnnual = null;
//        if(model.getRemainingNoOfInstallmentsAnnual() != 0) {
        String newInstallmentDateAnnual = createNewDateFormat.getFormattedDate(model.getNewInstallmentDateForAnnual());
//        }

        String query = "";
//        if(model.getRemainingNoOfInstallmentsAnnual() != 0) {
            query = "UPDATE DEBIT_CARD SET UPDATED_ON = '" + updatedOn + "'" +
                    ",FEE_DEDUCTION_DATE_ANNUAL = '" + feeDeductionDateAnnual + "'" +
                    ",FEE_DEDUCTION_DATE = '" + updatedOn + "'" +
                    ",NO_OF_INSTALLMENTS_ANNUAL = '" + model.getNoOfInstallmentsAnnual() + "'" +
                    ",REMAINING_NO_OF_INSTALL_ANNUAL = '" + model.getRemainingNoOfInstallmentsAnnual() + "'" +
                    ",NEW_INSTALLMENT_DATE_ANNUAL = '" + newInstallmentDateAnnual + "'" +
                    ",LAST_INSTALLMENT_DATE_ANNUAL = '" + updatedOn + "'" +
                    ",TRANSACTION_CODE = " + model.getTransactionCode() +
                    ",IS_INSTALLMENT = " + 1L +
                    ",FEE = " + model.getFee() +
                    " WHERE DEBIT_CARD_ID = " + model.getDebitCardId();
//        }
//        else{
//            query = "UPDATE DEBIT_CARD SET UPDATED_ON = '" + updatedOn + "'" +
//                    ",FEE_DEDUCTION_DATE_ANNUAL = '" + feeDeductionDateAnnual + "'" +
//                    ",FEE_DEDUCTION_DATE = '" + updatedOn + "'" +
//                    ",NO_OF_INSTALLMENTS_ANNUAL = '" + model.getNoOfInstallmentsAnnual() + "'" +
//                    ",REMAINING_NO_OF_INSTALL_ANNUAL = '" + model.getRemainingNoOfInstallmentsAnnual() + "'" +
//                    ",NEW_INSTALLMENT_DATE_ANNUAL = " + newInstallmentDateAnnual +
//                    ",LAST_INSTALLMENT_DATE_ANNUAL = '" + updatedOn + "'" +
//                    ",TRANSACTION_CODE = " + model.getTransactionCode() +
//                    ",FEE = " + model.getFee() +
//                    " WHERE DEBIT_CARD_ID = " + model.getDebitCardId();
//        }

        int rows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    @Override
    public void updateDebitCardFeeDeductionDateForReIssuanceFee(DebitCardModel model) throws FrameworkCheckedException {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String updatedOn = createNewDateFormat.formatDate(new Date());
//        String feeDeductionDateAnnual = createNewDateFormat.formatDate(model.getFeeDeductionDateAnnual());
        String lastInstallmentDateReIssuance = createNewDateFormat.getFormattedDate(model.getLastInstallmentDateForReIssuance());
        String newInstallmentDateReIssuance = null;
        if(model.getRemainingNoOfInstallments() != 0) {
            newInstallmentDateReIssuance = createNewDateFormat.getFormattedDate(model.getNewInstallmentDateForReIssuance());
        }

        String query = "";
        if(model.getRemainingNoOfInstallments() != 0) {
            query = "UPDATE DEBIT_CARD SET UPDATED_ON = '" + updatedOn + "'" +
//                ",FEE_DEDUCTION_DATE_ANNUAL = '" + feeDeductionDateAnnual + "'" +
                    ",NO_OF_INSTALLMENT = '" + model.getNoOfInstallments() + "'" +
                    ",REMAINING_NO_OF_INSTALLMENTS = '" + model.getRemainingNoOfInstallments() + "'" +
                    ",LAST_INSTALL_DATE_REISSUANCE = '" + lastInstallmentDateReIssuance + "'" +
                    ",NEW_INSTALL_DATE_REISSUANCE = '" + newInstallmentDateReIssuance + "'" +
                    ",TRANSACTION_CODE = " + model.getTransactionCode() +
                    ",FEE = " + model.getFee() +
                    " WHERE DEBIT_CARD_ID = " + model.getDebitCardId();
        }
        else{
            query = "UPDATE DEBIT_CARD SET UPDATED_ON = '" + updatedOn + "'" +
//                ",FEE_DEDUCTION_DATE_ANNUAL = '" + feeDeductionDateAnnual + "'" +
                    ",NO_OF_INSTALLMENT = '" + model.getNoOfInstallments() + "'" +
                    ",REMAINING_NO_OF_INSTALLMENTS = '" + model.getRemainingNoOfInstallments() + "'" +
                    ",LAST_INSTALL_DATE_REISSUANCE = '" + lastInstallmentDateReIssuance + "'" +
                    ",NEW_INSTALL_DATE_REISSUANCE = " + newInstallmentDateReIssuance +
                    ",TRANSACTION_CODE = " + model.getTransactionCode() +
                    ",FEE = " + model.getFee() +
                    " WHERE DEBIT_CARD_ID = " + model.getDebitCardId();
        }
        int rows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    @Override
    public void updateDebitCardFeeDeductionDateForIssuanceFee(DebitCardModel model) throws FrameworkCheckedException {
        CreateNewDateFormat createNewDateFormat = new CreateNewDateFormat();
        String updatedOn = createNewDateFormat.formatDate(new Date());
//        String feeDeductionDateAnnual = createNewDateFormat.formatDate(model.getFeeDeductionDateAnnual());
        String lastInstallmentDateIssuance = createNewDateFormat.getFormattedDate(model.getLastInstallmentDateForIssuance());
        String newInstallmentDateIssuance = null;
        if(model.getRemainingNoOfInstallments() != 0) {
            newInstallmentDateIssuance = createNewDateFormat.getFormattedDate(model.getNewInstallmentDateForIssuance());
        }
        String query = "";
        if(model.getRemainingNoOfInstallments() != 0) {
            query = "UPDATE DEBIT_CARD SET UPDATED_ON = '" + updatedOn + "'" +
//                ",FEE_DEDUCTION_DATE_ANNUAL = '" + feeDeductionDateAnnual + "'" +
                    ",NO_OF_INSTALLMENT = '" + model.getNoOfInstallments() + "'" +
                    ",REMAINING_NO_OF_INSTALLMENTS = '" + model.getRemainingNoOfInstallments() + "'" +
                    ",LAST_INSTALLMENT_DATE_ISSUANCE = '" + lastInstallmentDateIssuance + "'" +
                    ",NEW_INSTALLMENT_DATE_ISSUANCE = '" + newInstallmentDateIssuance + "'" +
                    ",TRANSACTION_CODE = " + model.getTransactionCode() +
                    ",FEE = " + model.getFee() +
                    " WHERE DEBIT_CARD_ID = " + model.getDebitCardId();
        }
        else{
            query = "UPDATE DEBIT_CARD SET UPDATED_ON = '" + updatedOn + "'" +
//                ",FEE_DEDUCTION_DATE_ANNUAL = '" + feeDeductionDateAnnual + "'" +
                    ",NO_OF_INSTALLMENT = '" + model.getNoOfInstallments() + "'" +
                    ",REMAINING_NO_OF_INSTALLMENTS = '" + model.getRemainingNoOfInstallments() + "'" +
                    ",LAST_INSTALLMENT_DATE_ISSUANCE = '" + lastInstallmentDateIssuance + "'" +
                    ",NEW_INSTALLMENT_DATE_ISSUANCE = " + newInstallmentDateIssuance +
                    ",TRANSACTION_CODE = " + model.getTransactionCode() +
                    ",FEE = " + model.getFee() +
                    " WHERE DEBIT_CARD_ID = " + model.getDebitCardId();
        }
        int rows = this.getSession().createSQLQuery(query).executeUpdate();
        this.getHibernateTemplate().flush();
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
