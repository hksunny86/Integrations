package com.inov8.microbank.server.service.bulkdisbursements;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;


public interface CustomerPendingTrxManager {

	public WorkFlowWrapper makeCustomerTrxByTransactionCode(String transactionId, String cnicCustomer) throws FrameworkCheckedException;
	int countCustomerPendingTrx(String customerCNIC) throws FrameworkCheckedException;
	int countCustomerPendingTrxByMobile(String recipientMobileNo) throws FrameworkCheckedException;
}
