package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.BankSegmentsModel;
import com.inov8.microbank.common.model.CardProdCodeModel;
import com.inov8.microbank.common.model.CardTypeModel;
import com.inov8.microbank.common.util.ValidationErrors;
import org.hibernate.criterion.MatchMode;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class FetchCardTypeCommand extends BaseCommand {
    List<CardProdCodeModel> list;


    @Override
    public void prepare(BaseWrapper baseWrapper) {

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return validationErrors;

    }

    @Override
    public void execute() throws CommandException {
        CardProdCodeModel cardTypeModel = new CardProdCodeModel();

        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        //
        list = commonCommandManager.getCardTypeDao().findByExample(cardTypeModel, null, null, exampleConfigHolderModel).getResultsetList();


    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        StringBuilder sb = null;
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(CARD_TYPE)
                .append(TAG_SYMBOL_CLOSE);
        if (list != null && !list.isEmpty()) {
            for (CardProdCodeModel model : list) {
                if (model.getCardTypeCode().contains("612")) {
                    model = new CardProdCodeModel();
                } else {
                    sb = new StringBuilder();
                    sb.append(TAG_SYMBOL_OPEN).append("cardType ")
                            .append(TAG_SYMBOL_SPACE)
                            .append(ATTR_PARAM_NAME)
                            .append(TAG_SYMBOL_EQUAL)
                            .append(TAG_SYMBOL_QUOTE)
                            .append(model.getCardProductName())
                            .append(TAG_SYMBOL_QUOTE)
                            .append(TAG_SYMBOL_SPACE)
                            .append("code")
                            .append(TAG_SYMBOL_EQUAL)
                            .append(TAG_SYMBOL_QUOTE)
                            .append(model.getCardProductCodeId())
                            .append(TAG_SYMBOL_QUOTE)
                            .append(TAG_SYMBOL_CLOSE)
                            .append(TAG_SYMBOL_OPEN)
                            .append(TAG_SYMBOL_SLASH)
                            .append("cardType").append(TAG_SYMBOL_CLOSE);
                    strBuilder.append(sb.toString());
                }
            }
        }
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(CARD_TYPE).append(TAG_SYMBOL_CLOSE);
        return strBuilder.toString();
    }
}
