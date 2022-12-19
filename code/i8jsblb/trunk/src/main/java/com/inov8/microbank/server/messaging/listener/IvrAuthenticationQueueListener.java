package com.inov8.microbank.server.messaging.listener;

import com.inov8.microbank.common.util.CommandFieldConstants;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.ivr.IvrRequestHandler;
import com.inov8.microbank.server.webserviceclient.ivr.IvrRequestDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class IvrAuthenticationQueueListener implements MessageListener {
	
	private static Log logger = LogFactory.getLog(IvrAuthenticationQueueListener.class);
	
	private String delay;
	private IvrRequestHandler ivrRequestHandler;

	@Override
	public void onMessage(Message message) {

		try {

			IvrRequestDTO requestDTO = (IvrRequestDTO) ((ObjectMessage) message).getObject();
                        
			Thread.sleep(Long.valueOf(delay));

            if(requestDTO.getProductId() != null && (requestDTO.getProductId().toString().equals(CommandFieldConstants.CMD_CUSTOMER_CREATE_PIN) || requestDTO.getProductId().toString().equals(CommandFieldConstants.REGENERATE_PIN_IVR ))){
                Thread.sleep(Long.valueOf(MessageUtil.getMessage("IVR_CUSTOMER_PIN_INIT_DELAY")));
            }

			ivrRequestHandler.makeIvrRequest(requestDTO);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public IvrRequestHandler getIvrRequestHandler() {
		return ivrRequestHandler;
	}

	public void setIvrRequestHandler(IvrRequestHandler ivrRequestHandler) {
		this.ivrRequestHandler = ivrRequestHandler;
	}

}