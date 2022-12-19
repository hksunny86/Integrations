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
        sb.append("WHERE ( (SYSDATE - CAST (ISSUANCE_DATE AS DATE) >= 365 ");
        sb.append("AND FEE_DEDUCTION_DATE IS NULL) ");
        sb.append("OR (FEE_DEDUCTION_DATE IS NOT NULL ");
        sb.append("AND SYSDATE - CAST (FEE_DEDUCTION_DATE AS DATE) >= 365)) ");
        sb.append(" AND CARD_STATUS_ID != " + CardConstantsInterface.CARD_STATUS_HOT);
        List<DebitCardModel> list = (List<DebitCardModel>) jdbcTemplate.query(sb.toString(),new DebitCardModel());
        return list;
    }

    @Override
    public List<DebitCardModel>
    loadAllCardsOnReIssuanceRequired() throws FrameworkCheckedException {
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

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
