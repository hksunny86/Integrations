package com.inov8.microbank.cardconfiguration.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;

public interface CardConfigurationManager {

    public SearchBaseWrapper searchCardFeeRuleConfiguration(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

    public void saveOrUpdateAllCardFeeRules(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public int removeAllCardFeeRules(BaseWrapper baseWrapper) throws FrameworkCheckedException;

    public CardFeeRuleModel loadCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException;
}
