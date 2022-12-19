package com.inov8.microbank.server.dao.fetchcardtype.hibernate;

import com.inov8.framework.server.dao.framework.hibernate.BaseHibernateDAO;
import com.inov8.microbank.common.model.CardProdCodeModel;
import com.inov8.microbank.server.dao.fetchcardtype.FetchCardTypeDAO;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class FetchCardTypeHibernateDAO extends BaseHibernateDAO<CardProdCodeModel,Long,FetchCardTypeDAO>
        implements FetchCardTypeDAO {
    @Override
    public CardProdCodeModel getCardProdCodeModel(Long cardProductTypeId) throws Exception {
        CardProdCodeModel cardProdCodeModel = null;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT user ");
        queryBuilder.append(" FROM CardProdCodeModel user ");
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" user.cardProductTypeId = :cardProductTypeId ");
        String[] paramNames = { "cardProductTypeId"};
        Object[] values = { cardProductTypeId};
        try {
            List<CardProdCodeModel> userList = getHibernateTemplate().findByNamedParam(queryBuilder.toString(), paramNames, values);
            if(CollectionUtils.isNotEmpty(userList)){
                cardProdCodeModel = userList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardProdCodeModel;
    }
}
