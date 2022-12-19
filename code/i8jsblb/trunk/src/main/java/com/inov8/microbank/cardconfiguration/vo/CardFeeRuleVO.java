package com.inov8.microbank.cardconfiguration.vo;

import com.inov8.microbank.cardconfiguration.model.CardFeeRuleModel;
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.Factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardFeeRuleVO implements Serializable {

    private static final long serialVersionUID = 6058253636035612722L;

    private List<CardFeeRuleModel> cardFeeRuleModelList;

    @SuppressWarnings("unchecked")
    public CardFeeRuleVO() {
        super();
        cardFeeRuleModelList = LazyList.decorate(new ArrayList<CardFeeRuleModel>(), new Factory() {
            @Override
            public CardFeeRuleModel create() {
                return new CardFeeRuleModel();
            }
        });
    }

    public List<CardFeeRuleModel> getCardFeeRuleModelList() {
        return cardFeeRuleModelList;
    }

    public void setCardFeeRuleModelList(List<CardFeeRuleModel> cardFeeRuleModelList) {
        this.cardFeeRuleModelList = cardFeeRuleModelList;
    }

    public void addCardFeeRuleModel(CardFeeRuleModel cardFeeRuleModel) {
        this.cardFeeRuleModelList.add(cardFeeRuleModel);
    }
}
