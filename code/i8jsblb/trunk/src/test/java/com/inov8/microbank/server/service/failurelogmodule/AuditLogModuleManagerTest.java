package com.inov8.microbank.server.service.failurelogmodule;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.inov8.microbank.common.model.AuditLogModel;
import com.inov8.microbank.common.model.FailureReasonModel;
import com.inov8.microbank.common.model.TransactionCodeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.BaseManagerIntegrationTestCase;
import com.inov8.microbank.common.util.IntegrationModuleConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.failurelogmodule.FailureLogManager;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

@Test(groups = { "integration", "failurelogIntegration" })
public class AuditLogModuleManagerTest extends BaseManagerIntegrationTestCase
{
	private FailureLogManager auditLogModuleManager = null;

	@BeforeClass
	protected void setUp()
	{
		auditLogModuleManager = (FailureLogManager) getBean("auditLogModuleFacade");
	}

	@AfterClass
	protected void tearDown()
	{
		auditLogModuleManager = null;
	}

	public void testSaveAuditLog() throws Exception
	{
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		AuditLogModel failureLogModel = new AuditLogModel();
		FailureReasonModel failureReasonModel = new FailureReasonModel();

		failureReasonModel.setFailureReasonId(new Long(1));
//		failureLogModel.setFailureReasonIdFailureReasonModel(failureReasonModel);

		TransactionCodeModel txCode = new TransactionCodeModel();
		txCode.setPrimaryKey(1l);

		TransactionModel txModel = new TransactionModel();
		txModel.setTransactionCodeIdTransactionCodeModel(txCode);

		failureLogModel.setTransactionCodeIdTransactionCodeModel(txCode);

//		failureLogModel.setReason("Test Reason 1");
		failureLogModel.setIntegrationModuleId( IntegrationModuleConstants.VERIFLY_MODULE );
//		failureLogModel.setActive(true);
//		failureLogModel.setLogTimeStamp(new Date());
//		failureLogModel.setVersionNo(1);

		workFlowWrapper.setFailureLogModel(failureLogModel);
		workFlowWrapper.setTransactionModel(txModel);

		this.auditLogModuleManager.auditLogRequiresNewTransaction(workFlowWrapper);

	}

}
