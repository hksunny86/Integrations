package com.inov8.microbank.server.service.transactionmodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;

public interface OrphanA2PReversalManager{
	
	public WorkFlowWrapper makeAccountToCashReversal(TransactionReversalVo txReversalVo) throws FrameworkCheckedException;

}
