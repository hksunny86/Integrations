package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.bankmodule.MemberBankModel;
import com.inov8.microbank.common.util.ValidationErrors;

import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class FetchMemberBanksCommand extends BaseCommand {

    private String isIBFT;
    private List<MemberBankModel> list;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        isIBFT = this.getCommandParameter(baseWrapper,"IS_IBFT");
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        list = commonCommandManager.getMemberBankDao().findAllBanks();
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        StringBuilder sb = null;
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(MEMBER_BANKS)
                .append(TAG_SYMBOL_CLOSE);
        if(list != null && !list.isEmpty()){
            for(MemberBankModel model : list){
                sb = new StringBuilder();
                sb.append(TAG_SYMBOL_OPEN).append("bank ")
                        .append(TAG_SYMBOL_SPACE)
                        .append(ATTR_PARAM_NAME)
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getBankShortName())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_SPACE)
                        .append("IMD ")
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getBankImd())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_SPACE)
                        .append("MIN_LENGTH ")
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getMinAccountNoLength())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_SPACE)
                        .append("MAX_LENGTH ")
                        .append(TAG_SYMBOL_EQUAL)
                        .append(TAG_SYMBOL_QUOTE)
                        .append(model.getMaxAccountNoLength())
                        .append(TAG_SYMBOL_QUOTE)
                        .append(TAG_SYMBOL_CLOSE)
                        .append(TAG_SYMBOL_OPEN)
                        .append(TAG_SYMBOL_SLASH)
                        .append("bank").append(TAG_SYMBOL_CLOSE);
                strBuilder.append(sb.toString());
            }
        }
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(MEMBER_BANKS).append(TAG_SYMBOL_CLOSE);
        return strBuilder.toString();
    }
}
