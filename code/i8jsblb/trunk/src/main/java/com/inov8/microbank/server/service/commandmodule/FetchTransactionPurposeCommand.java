package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.TransactionPurposeModel;
import com.inov8.microbank.common.util.ValidationErrors;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class FetchTransactionPurposeCommand extends BaseCommand {

    List<TransactionPurposeModel> list;

    @Override
    public void prepare(BaseWrapper baseWrapper) {

    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        list = commonCommandManager.getTransactionPurposeDao().loadTrxPurposeByCode();
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        StringBuilder sb = null;
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TRANS_PURPOSE_REASON)
                .append(TAG_SYMBOL_CLOSE);
        if(list != null && !list.isEmpty()){
            for(TransactionPurposeModel model : list){
                sb = new StringBuilder();
                sb.append(TAG_SYMBOL_OPEN).append("paymentreason ")
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getName())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_SPACE)
                        .append("code")
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getCode())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append("paymentreason").append(TAG_SYMBOL_CLOSE);
                strBuilder.append(sb.toString());
            }
        }
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(TRANS_PURPOSE_REASON).append(TAG_SYMBOL_CLOSE);
        return strBuilder.toString();
    }
}
