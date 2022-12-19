package com.inov8.microbank.server.messaging.listener;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.server.service.agenthierarchy.AgentHierarchyManager;

public class FranchiseListener implements MessageListener{
	protected static Log logger	= LogFactory.getLog(FranchiseListener.class);
	
	private AgentHierarchyManager agentHierarchyManager;
	
	@Override
	public void onMessage(Message message){
		if(logger.isDebugEnabled()){
			logger.debug("Franchise Object received on Queue ");
		}
		if (message instanceof ObjectMessage){
			try{
				Object obj = ((ObjectMessage) message).getObject();
				if (null != obj){
					if (obj instanceof RetailerModel){
						RetailerModel retModel = (RetailerModel) obj;
						boolean created = agentHierarchyManager.saveFranchiseFrmQueue(retModel);
					}
				}
			}catch (JMSException ex){
				logger.error(ex);
				throw new RuntimeException(ex.getMessage(), ex);
			}catch (Exception ex){
				logger.error("Exception occured creating Franchise in bulk...");
				throw new RuntimeException(ex.getMessage(), ex);
			}
		}
	}

	public void setAgentHierarchyManager(AgentHierarchyManager agentHierarchyManager) {
		this.agentHierarchyManager = agentHierarchyManager;
	}

}