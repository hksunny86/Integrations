package com.inov8.microbank.server.service.workflow.credittransfer;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.BaseManagerIntegrationTestCase;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;

@Test(groups = { "integration", "workflowIntegration" })
public class OperatorToDistributorTransactionTest extends BaseManagerIntegrationTestCase
{
	private WorkFlowController workFlowController;

	@BeforeClass
	protected void setUp()
	{
		workFlowController = (WorkFlowController) getBean("workFlowFacade");
	}

	@AfterClass
	protected void tearDown()
	{
		workFlowController = null;
	}

	public void testCreditTransfer() throws Exception
	{
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		workFlowWrapper.setAppUserModel(new AppUserModel());
		workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel());
		workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
		workFlowWrapper.setToDistributorContactAppUserModel(new AppUserModel());

		workFlowWrapper.getToDistributorContactAppUserModel().setMobileNo("0300");
		workFlowWrapper.setAppUserModel(UserUtils.getCurrentUser());
		workFlowWrapper.getTransactionTypeModel().setTransactionTypeId(TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX);
		workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

		workFlowWrapper.setTransactionAmount(100D);

		workFlowWrapper = this.workFlowController.workflowProcess(workFlowWrapper);
	}

}
