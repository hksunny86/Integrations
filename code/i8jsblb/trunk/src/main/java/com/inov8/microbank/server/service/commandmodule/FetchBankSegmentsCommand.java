package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.BankSegmentsModel;
import com.inov8.microbank.common.util.ValidationErrors;
import org.hibernate.criterion.MatchMode;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class FetchBankSegmentsCommand extends BaseCommand {

    List<BankSegmentsModel> list;

    @Override
    public void prepare(BaseWrapper baseWrapper) {

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        BankSegmentsModel bankSegmentsModel = new BankSegmentsModel();
        bankSegmentsModel.setActive(Boolean.TRUE);
        //
        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
        //
        list = commonCommandManager.getFetchThirdPartySegmentsDao().findByExample(bankSegmentsModel,null,null,exampleConfigHolderModel).getResultsetList();
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        StringBuilder sb = null;
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(THIRD_PARTY_SEGMENTS)
                .append(TAG_SYMBOL_CLOSE);
        if(list != null && !list.isEmpty()){
            for(BankSegmentsModel model : list){
                sb = new StringBuilder();
                sb.append(TAG_SYMBOL_OPEN).append("segment ")
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getDestinationSegmentName())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_SPACE)
                        .append("code")
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getDestinationSegmentId())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append("segment").append(TAG_SYMBOL_CLOSE);
                strBuilder.append(sb.toString());
            }
        }
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(THIRD_PARTY_SEGMENTS).append(TAG_SYMBOL_CLOSE);
        return strBuilder.toString();
    }
}
