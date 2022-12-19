package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.jms.DestinationConstants;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.server.service.jms.JmsProducer;

public class FranchiseCreatorImpl implements FranchiseCreator{
	private JmsProducer jmsProducer;
	
	public void send(RetailerModel model) throws FrameworkCheckedException{		
		this.jmsProducer.produce(model, DestinationConstants.FRANCHISE_DESTINATION);
	}
	
	public void setJmsProducer(JmsProducer jmsProducer){
		this.jmsProducer = jmsProducer;
	}	
}
