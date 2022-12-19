package com.inov8.microbank.server.service.workflow.credittransfer;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.BaseManagerIntegrationTestCase;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;

@Test(groups = { "integration", "workflowIntegration" })
public class RetailerToRetailerTransactionTest extends BaseManagerIntegrationTestCase
{

	private WorkFlowController workFlowController;

	private AppUserManager appUserManager;

	private AppUserModel appUserModel;

	@BeforeClass
	protected void setUp() throws Exception
	{
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		workFlowController = (WorkFlowController) getBean("workFlowFacade");
		appUserManager = (AppUserManager) getBean("securityFacade");

		this.appUserModel = new AppUserModel();
		this.appUserModel.setAppUserId(1000L);
		baseWrapper.setBasePersistableModel(appUserModel);
		appUserModel = (AppUserModel) this.appUserManager.loadAppUser(baseWrapper).getBasePersistableModel();
	}

	@AfterClass
	protected void tearDown()
	{
		workFlowController = null;
		appUserManager = null;
	}

	public void testCreditTransfer() throws Exception
	{
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		workFlowWrapper.setToRetailerContactAppUserModel(new AppUserModel());
		workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel());
		workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());

		workFlowWrapper.setFromRetailerContactAppUserModel(this.appUserModel);
		workFlowWrapper.getTransactionTypeModel().setTransactionTypeId(TransactionTypeConstantsInterface.RET_TO_RET_TX);
		workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);

		workFlowWrapper.setTransactionAmount(100D);
		workFlowWrapper.getToRetailerContactAppUserModel().setMobileNo("03344452829");

		workFlowWrapper = this.workFlowController.workflowProcess(workFlowWrapper);

	}

}
