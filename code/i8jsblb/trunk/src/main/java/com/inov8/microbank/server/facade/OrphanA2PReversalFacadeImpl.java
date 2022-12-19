package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.microbank.common.vo.transactionreversal.TransactionReversalVo;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.transactionmodule.OrphanA2PReversalManager;

public class OrphanA2PReversalFacadeImpl implements OrphanA2PReversalFacade{
	private OrphanA2PReversalManager orphanA2PReversalManager;
	private CreditAccountQueingPreProcessor creditAccountQueingPreProcessor;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;
	
	@Override
	public WorkFlowWrapper makeAccountToCashReversal(TransactionReversalVo txReversalVo) throws FrameworkCheckedException {
		WorkFlowWrapper wrapper = null;
		try{
			wrapper = orphanA2PReversalManager.makeAccountToCashReversal(txReversalVo);
			creditAccountQueingPreProcessor.loadAndForwardAccountToQueue(txReversalVo.getTransactionCode());
        }catch( Exception e ){
            throw frameworkExceptionTranslator.translate( e, FrameworkExceptionTranslator.FIND_ACTION );
        }
        return wrapper;
    }

	public void setOrphanA2PReversalManager(OrphanA2PReversalManager orphanA2PReversalManager){
		this.orphanA2PReversalManager = orphanA2PReversalManager;
	}

	public void setFrameworkExceptionTranslator(FrameworkExceptionTranslator frameworkExceptionTranslator){
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public void setCreditAccountQueingPreProcessor(CreditAccountQueingPreProcessor creditAccountQueingPreProcessor) {
		this.creditAccountQueingPreProcessor = creditAccountQueingPreProcessor;
	}

}
