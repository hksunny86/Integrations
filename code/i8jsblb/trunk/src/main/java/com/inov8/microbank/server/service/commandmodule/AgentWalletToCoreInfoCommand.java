package com.inov8.microbank.server.service.commandmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.model.*;
import com.inov8.microbank.common.util.*;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapper;
import com.inov8.microbank.common.wrapper.switchmodule.SwitchWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.mfs.jme.model.Message;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.financialintegrationmodule.switchmodule.phoenix.PhoenixConstantsInterface;
import com.inov8.microbank.server.service.switchmodule.iris.model.CustomerAccount;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;

import java.text.SimpleDateFormat;

import static com.inov8.microbank.common.util.XMLConstants.*;
import static com.inov8.microbank.common.util.XMLConstants.TAG_PARAMS;
import static com.inov8.microbank.common.util.XMLConstants.TAG_SYMBOL_CLOSE;

public class AgentWalletToCoreInfoCommand extends IBFTBaseCommand {

    protected final Log logger = LogFactory.getLog(AgentWalletToCoreInfoCommand.class);

    @Override
    public void execute() throws CommandException {
        logger.info(":- Started AgentWalletToCoreInfo Command -:");
        try{
            CommissionWrapper commissionWrapper = getCommonCommandManager().calculateCommission(workFlowWrapper);
            CommissionAmountsHolder commissionAmountsHolder = (CommissionAmountsHolder) commissionWrapper.getCommissionWrapperHashMap().get(CommissionConstantsInterface.COMMISSION_AMOUNTS_HOLDER);
            _commissionAmount = commissionAmountsHolder.getTotalCommissionAmount();
            TPAM += commissionAmountsHolder.getTransactionProcessingAmount();
            commissionAmount = _commissionAmount.toString();
            Double balance = 0.0D;
            Double totalAmount = Double.valueOf(transactionAmount) + TPAM;
            String accountBalance = commonCommandManager.getAccountBalance(accountInfoModel,smartMoneyAccountModel);
            if(accountBalance != null && !accountBalance.equals(""))
                balance = Double.parseDouble(accountBalance);
            if(balance < totalAmount)
                throw new CommandException(MessageUtil.getMessage("MINI.InsufficientBalance"), ErrorCodes.COMMAND_EXECUTION_ERROR,ErrorLevel.MEDIUM);

        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new CommandException(ex.getMessage(), ErrorCodes.COMMAND_EXECUTION_ERROR, ErrorLevel.MEDIUM, ex);
        }
    }

    @Override
    public String response() {
        return response.toString();
    }

}
