package com.inov8.microbank.common.util;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewFormModel;

public interface JMSAgentSender {

	
	public abstract void send(RetailerContactListViewFormModel agentFormModel) throws FrameworkCheckedException;
	
}
