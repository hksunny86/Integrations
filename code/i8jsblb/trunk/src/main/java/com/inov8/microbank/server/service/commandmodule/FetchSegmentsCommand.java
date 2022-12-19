package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.CardProdCodeModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.server.service.mfsmodule.CommonCommandManager;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import org.hibernate.criterion.MatchMode;

import java.util.ArrayList;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class FetchSegmentsCommand extends BaseCommand {

    List<SegmentModel> list;
    List<AgentSegmentRestriction> segmentModelListlist;
//        List<SegmentModel> segmentModelSortedList;
    private AgentSegmentRestrictionManager agentSegmentRestrictionManager;
    private AgentSegmentRestriction agentSegmentRestriction;
    private String agentId;

    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.agentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_ID);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        this.commonCommandManager = getCommonCommandManager();
        SegmentModel segmentModel = new SegmentModel();
//        segmentModel.setIsActive(true);
//        ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
//        exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);
//        exampleConfigHolderModel.setEnableLike(Boolean.FALSE);
//        list = commonCommandManager.getSegmentDao().findByExample(segmentModel, null, null, exampleConfigHolderModel).getResultsetList();
//        this.agentSegmentRestrictionManager = commonCommandManager.getAgentSegmentRestriction();
        agentSegmentRestriction = new AgentSegmentRestriction();
        agentSegmentRestriction.setIsActive(true);
        agentSegmentRestriction.setAgentID(agentId);
        agentSegmentRestriction.setProductId(ProductConstantsInterface.ACCOUNT_OPENING);
        BaseWrapperImpl baseWrapper = new BaseWrapperImpl();
        baseWrapper.setBasePersistableModel(agentSegmentRestriction);
        ExampleConfigHolderModel exampleConfigHolderModel1 = new ExampleConfigHolderModel();
        exampleConfigHolderModel1.setMatchMode(MatchMode.EXACT);
        exampleConfigHolderModel1.setEnableLike(Boolean.FALSE);
        segmentModelListlist = commonCommandManager.getAgentSegmentRestriction().agentSegmentRestrictionDAO().findByExample(agentSegmentRestriction, null, null, exampleConfigHolderModel1).getResultsetList();

        //        boolean contains = false;
//        segmentModelSortedList = new ArrayList<SegmentModel>();
//        for (int i = 0; i < list.size(); i++) {
//            for (int j = 0; j < segmentModelListlist.size(); j++) {
//                if (list.get(i).getSegmentId().intValue() == segmentModelListlist.get(j).getSegmentId().intValue()) {
//                    contains = true;
//                    break;
//                }
//            }
//            if (!contains) {
//                segmentModelSortedList.add(list.get(i));
//            } else {
//                contains = false;
//            }
//        }
    }

    @Override
    public String response() {
        StringBuilder strBuilder = new StringBuilder();
        StringBuilder sb = null;
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(THIRD_PARTY_SEGMENTS)
                .append(TAG_SYMBOL_CLOSE);
        if (segmentModelListlist != null && !segmentModelListlist.isEmpty()) {
            for (AgentSegmentRestriction model : segmentModelListlist) {
                if (model.getSegmentId().equals(10320L)||model.getSegmentId().equals(10072L)||model.getSegmentId().equals(2L)) {
                    model = new AgentSegmentRestriction();
                }else {
                    sb = new StringBuilder();
                    sb.append(TAG_SYMBOL_OPEN).append("segment")
                            .append(TAG_SYMBOL_SPACE)
                            .append(ATTR_PARAM_NAME)
                            .append(TAG_SYMBOL_EQUAL)
                            .append(TAG_SYMBOL_QUOTE)
                            .append(model.getName())
                            .append(TAG_SYMBOL_QUOTE)
                            .append(TAG_SYMBOL_SPACE)
                            .append("segmentId")
                            .append(TAG_SYMBOL_EQUAL)
                            .append(TAG_SYMBOL_QUOTE)
                            .append(model.getSegmentId())
                            .append(TAG_SYMBOL_QUOTE)
                            .append(TAG_SYMBOL_CLOSE)
                            .append(TAG_SYMBOL_OPEN)
                            .append(TAG_SYMBOL_SLASH)
                            .append("segment").append(TAG_SYMBOL_CLOSE);
                    strBuilder.append(sb.toString());
                }
            }
        }
        strBuilder.append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH)
                .append(THIRD_PARTY_SEGMENTS).append(TAG_SYMBOL_CLOSE);
        return strBuilder.toString();
    }

}
