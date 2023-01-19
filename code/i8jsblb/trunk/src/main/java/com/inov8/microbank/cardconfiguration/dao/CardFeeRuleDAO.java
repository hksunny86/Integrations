package com.inov8.microbank.cardconfiguration.dao;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.server.dao.framework.BaseDAO;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;

import java.util.List;

public interface CardFeeRuleDAO extends BaseDAO<CardFeeRuleModel,Long> {

    List<CardFeeRuleModel> loadCardFeeRuleModelList(CardFeeRuleModel model);
    public int removeAllCardFeeRules();

    public CardFeeRuleModel loadCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException;
    public void saveCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException;
    public CardFeeRuleModel searchCardFeeRule(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException;

}
