package com.inov8.microbank.server.handler;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.ProductConstantsInterface;
import com.inov8.microbank.fonepay.common.FonePayConstants;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class CreditPaymentRequestHandler {


    protected final Log logger = LogFactory.getLog(getClass());

    public void makeCreditPaymentAdviceRequest(MiddlewareAdviceVO messageVO) throws CommandException {

        logger.info("------------- Credit Payment Request Recieved ----------------- mobileNo:" + messageVO.getAccountNo2());


        try {


                BaseWrapper baseWrapper = new BaseWrapperImpl();
                baseWrapper.putObject(CommandFieldConstants.KEY_CUSTOMER_MOBILE, messageVO.getAccountNo2());
                baseWrapper.putObject(CommandFieldConstants.KEY_STAN, messageVO.getStan());
                baseWrapper.putObject(CommandFieldConstants.KEY_TX_AMOUNT, messageVO.getTransactionAmount());
                baseWrapper.putObject(CommandFieldConstants.KEY_TX_DATE, messageVO.getRequestTime());
                baseWrapper.putObject(CommandFieldConstants.KEY_PROD_ID, messageVO.getProductId());
                baseWrapper.putObject(FonePayConstants.KEY_EXTERNAL_TRANSACTION_CODE, messageVO.getRetrievalReferenceNumber());
                baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.WEB_SERVICE);
                this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CMD_CREDIT_PAYMENT_API);

        } catch (CommandException e) {
            throw e;
        }

    }

    public CommandManager getCommandManager() {
        ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        return (CommandManager) applicationContext.getBean("cmdManager");
    }
}
