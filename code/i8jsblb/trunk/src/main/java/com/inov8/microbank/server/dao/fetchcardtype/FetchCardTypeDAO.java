package com.inov8.microbank.server.dao.fetchcardtype;

import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.common.model.CardProdCodeModel;
import com.inov8.microbank.common.model.CardTypeModel;

public interface FetchCardTypeDAO extends BaseDAO<CardProdCodeModel,Long> {
    CardProdCodeModel getCardProdCodeModel (Long cardProductTypeId) throws Exception;
}
