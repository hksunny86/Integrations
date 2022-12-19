package com.inov8.microbank.cardconfiguration.dao.hibernate;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.cardconfiguration.dao.CardFeeRuleDAO;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class CardFeeRuleHibernateDAO extends BaseHibernateDAO<CardFeeRuleModel,Long,CardFeeRuleDAO>
    implements CardFeeRuleDAO{

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CardFeeRuleModel> loadCardFeeRuleModelList(CardFeeRuleModel model) {
        StringBuilder queryStr = new StringBuilder();

        Boolean isDeleted = model.getIsDeleted();
        isDeleted = isDeleted != null ? isDeleted : false;

        queryStr.append("select model from CardFeeRuleModel model, CardProgramModel cp,  CardFeeTypeModel cft ");

        queryStr.append("where model.isDeleted = :isDeleted ");
        queryStr.append("and model.cardFeeTypeModel.cardFeeTypeId = cft.cardFeeTypeId ");
        queryStr.append("and cft.cardFeeTypeId =:cardFeeTypeId ");

        Query query = getSession().createQuery(queryStr.toString());
        query.setParameter("isDeleted", isDeleted);
        query.setParameter("cardFeeTypeId", model.getCardFeeTypeId());

        return query.list();
    }

    public int removeAllCardFeeRules()
    {
        Long serviceOpId = null;
        if(UserUtils.getCurrentUser().getMnoId() != null && UserUtils.getCurrentUser().getMnoId().equals(50028L))
            serviceOpId = 50028L;
        else
            serviceOpId = 50027L;

        Object[] values = {UserUtils.getCurrentUser().getAppUserId(), new Date(), true, false,serviceOpId};
        return getHibernateTemplate().bulkUpdate("update CardFeeRuleModel model set model.updatedByAppUserModel.appUserId=?, model.updatedOn=?, model.isDeleted=? " +
                "where model.isDeleted=? and model.mnoIdMnoModel.mnoId = ? ",values);
    }

    @Override

    public CardFeeRuleModel loadCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException {

        StringBuilder sb = new StringBuilder();

        sb.append("SELECT * FROM CARD_FEE_RULE WHERE CARD_TYPE_ID= ").append(cardFeeRuleModel.getCardTypeId());
        sb.append(" AND CARD_FEE_TYPE_ID = ").append(cardFeeRuleModel.getCardFeeTypeId());
        sb.append(" AND (CARD_PRODUCT_TYPE_ID IS NULL OR CARD_PRODUCT_TYPE_ID = " + cardFeeRuleModel.getCardProductCodeId() + " )");
        sb.append(" AND APP_USER_TYPE_ID = ").append(cardFeeRuleModel.getAppUserTypeId());
        sb.append(" AND SERVICE_OP_ID = ").append(cardFeeRuleModel.getMnoId());
        sb.append(" AND IS_DELETED=0 ");
        //if(cardFeeRuleModel.getSegmentId() != null)
            sb.append(" AND (SEGMENT_ID IS NULL OR SEGMENT_ID = " + cardFeeRuleModel.getSegmentId() + " )");

        //if(cardFeeRuleModel.getDistributorId() != null)
            sb.append(" AND (DISTRIBUTOR_ID IS NULL OR DISTRIBUTOR_ID = " + cardFeeRuleModel.getDistributorId() + " )");

        //if(null != cardFeeRuleModel.getAccountTypeId())
            sb.append(" AND (ACCOUNT_TYPE_ID IS NULL OR ACCOUNT_TYPE_ID = " + cardFeeRuleModel.getAccountTypeId() + " )");
            sb.append("ORDER BY CARD_TYPE_ID,CARD_FEE_TYPE_ID,SEGMENT_ID,DISTRIBUTOR_ID");

        logger.info("Loading Debit Card Fee Rule with Criteria: " + sb.toString());
        List<CardFeeRuleModel> list = (List<CardFeeRuleModel>) jdbcTemplate.query(sb.toString(),new CardFeeRuleModel());
        if(!list.isEmpty())
            return list.get(0);
        return null;
    }

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
