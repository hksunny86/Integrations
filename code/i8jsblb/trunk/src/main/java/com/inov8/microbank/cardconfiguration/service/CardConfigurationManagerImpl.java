package com.inov8.microbank.cardconfiguration.service;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.cardconfiguration.dao.CardFeeRuleDAO;
import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import com.inov8.microbank.cardconfiguration.vo.CardFeeRuleVO;
import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class CardConfigurationManagerImpl implements CardConfigurationManager {

    private CardFeeRuleDAO cardFeeRuleDAO;
    private ActionLogManager actionLogManager;

    @Override
    public SearchBaseWrapper searchCardFeeRuleConfiguration(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
        CardFeeRuleModel cardFeeRuleModel = (CardFeeRuleModel) searchBaseWrapper.getBasePersistableModel();
        CustomList<CardFeeRuleModel> customList = cardFeeRuleDAO.findByExample(cardFeeRuleModel, searchBaseWrapper.getPagingHelperModel(),
                searchBaseWrapper.getSortingOrderMap(), searchBaseWrapper.getDateRangeHolderModel());
        searchBaseWrapper.setCustomList(customList);
        return searchBaseWrapper;
    }

    @Override
    public CardFeeRuleModel searchCardFeeRule(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException {
        return cardFeeRuleDAO.searchCardFeeRule(cardFeeRuleModel);
    }

    @Override
    public void saveOrUpdateAllCardFeeRules(BaseWrapper baseWrapper) throws FrameworkCheckedException {
        /*ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());*/

        CardFeeRuleVO cardFeeRuleVOModel = (CardFeeRuleVO) baseWrapper.getObject(CardFeeRuleVO.class.getSimpleName());

        if (CollectionUtils.isNotEmpty(cardFeeRuleVOModel.getCardFeeRuleModelList())) {
            cardFeeRuleDAO.saveOrUpdateCollection(cardFeeRuleVOModel.getCardFeeRuleModelList());
        }
        // actionLogModel.setCustomField1(cardLimtVOModel);
        //this.actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);

    }

    @Override
    public int removeAllCardFeeRules(BaseWrapper baseWrapper) throws FrameworkCheckedException
    {
        int updatedRows;
        /*ActionLogModel actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
        ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());*/

        updatedRows = cardFeeRuleDAO.removeAllCardFeeRules();

        //this.actionLogManager.completeActionLogRequiresNewTransaction(actionLogModel);

        return updatedRows;
    }

    @Override
    public CardFeeRuleModel loadCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException {
        return cardFeeRuleDAO.loadCardFeeRuleModel(cardFeeRuleModel);
    }

    @Override
    public void saveCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel) throws FrameworkCheckedException {
        cardFeeRuleDAO.saveCardFeeRuleModel(cardFeeRuleModel);
    }

    public void setCardFeeRuleDAO(CardFeeRuleDAO cardFeeRuleDAO) {
        this.cardFeeRuleDAO = cardFeeRuleDAO;
    }

    public void setActionLogManager(ActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }
}
