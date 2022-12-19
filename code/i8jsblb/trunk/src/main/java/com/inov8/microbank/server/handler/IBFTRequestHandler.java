package com.inov8.microbank.server.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.exception.CommandException;
import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.server.service.commandmodule.CommandManager;
import com.inov8.microbank.server.service.integration.vo.MiddlewareAdviceVO;

public class IBFTRequestHandler {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public void makeCreditAdviceRequest(MiddlewareAdviceVO messageVO)throws CommandException{
		
		logger.info("------------- IBFT Request Recieved ----------------- mobileNo:"+messageVO.getAccountNo2());
		
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		baseWrapper.putObject(CommandFieldConstants.KEY_MOB_NO, messageVO.getAccountNo2());
		baseWrapper.putObject(CommandFieldConstants.KEY_ACCOUNT_NUMBER, messageVO.getAccountNo1());
		baseWrapper.putObject(CommandFieldConstants.KEY_AMOUNT, messageVO.getTransactionAmount());
		baseWrapper.putObject(CommandFieldConstants.KEY_TX_DATE, messageVO.getRequestTime());
		baseWrapper.putObject(CommandFieldConstants.KEY_ST_TX_NO, messageVO.getStan());
		baseWrapper.putObject(CommandFieldConstants.KEY_RRN, messageVO.getRetrievalReferenceNumber());
		baseWrapper.putObject(CommandFieldConstants.KEY_DEVICE_TYPE_ID, DeviceTypeConstantsInterface.ALL_PAY);
		baseWrapper.putObject(CommandFieldConstants.KEY_BANK_ID, messageVO.getBankIMD());

		try {
			
			this.getCommandManager().executeCommand(baseWrapper, CommandFieldConstants.CREDIT_ADVICE_COMMAND);		
		
		} catch (CommandException e) {
			throw e;
		}
		
	}
	
	public CommandManager getCommandManager() {
		ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
		return (CommandManager) applicationContext.getBean("cmdManager");
	}

}
