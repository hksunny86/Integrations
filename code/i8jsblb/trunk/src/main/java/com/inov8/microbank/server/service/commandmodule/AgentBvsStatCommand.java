package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.AgentBvsStatModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.model.agentsegmentrestrictionmodule.AgentSegmentRestriction;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.common.util.ValidationErrors;
import com.inov8.microbank.server.service.agentbvsstate.AgentBvsStatManager;
import com.inov8.microbank.server.service.portal.agentsegmentrestrictionmodule.AgentSegmentRestrictionManager;
import org.hibernate.criterion.MatchMode;

import java.util.ArrayList;
import java.util.List;

import static com.inov8.microbank.common.util.XMLConstants.*;

public class AgentBvsStatCommand extends BaseCommand {


    private AgentBvsStatManager agentBvsStatManager;
    private String agentId;
    private String bvsfail;


    @Override
    public void prepare(BaseWrapper baseWrapper) {
        this.agentId = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_AGENT_ID);
        this.bvsfail = getCommandParameter(baseWrapper, CommandFieldConstants.KEY_BVS_FAIL);
    }

    @Override
    public ValidationErrors validate(ValidationErrors validationErrors) throws CommandException {
        return validationErrors;
    }

    @Override
    public void execute() throws CommandException {
        this.commonCommandManager = getCommonCommandManager();
        AgentBvsStatModel agentBvsStatModel = new AgentBvsStatModel();
        agentBvsStatModel.setAgentId(Long.valueOf(agentId));
        try {
            agentBvsStatModel= getCommonCommandManager().loadAgentBvsStat(agentBvsStatModel);
            if (agentBvsStatModel!=null){
                if (agentBvsStatModel.getBvsFail().equals(20L)){
                    agentBvsStatModel.setBvsFail(agentBvsStatModel.getBvsFail()+Long.valueOf(bvsfail));
                    agentBvsStatModel.setAlertRequired(1L);
                    this.getCommonCommandManager().getAgentBvsStatManager().saveOrUpdate(agentBvsStatModel);

                }else {
                    agentBvsStatModel.setBvsFail(agentBvsStatModel.getBvsFail()+Long.valueOf(bvsfail));
                    this.getCommonCommandManager().getAgentBvsStatManager().saveOrUpdate(agentBvsStatModel);
                }

            }else {
                AgentBvsStatModel agentBvsStatModel1 = new AgentBvsStatModel();
                agentBvsStatModel1.setAgentId(Long.valueOf(agentId));
                agentBvsStatModel1.setBvsFail(Long.valueOf(bvsfail));
                this.getCommonCommandManager().getAgentBvsStatManager().saveOrUpdate(agentBvsStatModel1);
            }
        } catch (FrameworkCheckedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String response() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAMS)
                .append(TAG_SYMBOL_CLOSE);
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_PARAM)
                .append(TAG_SYMBOL_SPACE).append(ATTR_PARAM_NAME)
                .append(TAG_SYMBOL_EQUAL).append(TAG_SYMBOL_QUOTE)
                .append(CommandFieldConstants.KEY_DEVICE_TYPE_ID)
                .append(TAG_SYMBOL_QUOTE).append(TAG_SYMBOL_CLOSE)
                .append(DeviceTypeConstantsInterface.MOBILE).append(TAG_SYMBOL_OPEN)
                .append(TAG_SYMBOL_SLASH).append(TAG_PARAM)
                .append(TAG_SYMBOL_CLOSE);
        strBuilder.append(TAG_SYMBOL_OPEN).append(TAG_SYMBOL_SLASH)
                .append(TAG_PARAMS).append(TAG_SYMBOL_CLOSE);

        return strBuilder.toString();
    }

}
