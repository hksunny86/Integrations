package com.inov8.microbank.server.service.workflow.sales;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.BaseManagerIntegrationTestCase;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;
import com.inov8.verifly.common.model.AccountInfoModel;

@Test(groups = { "integration", "workflowIntegration" })
public class CustomerDiscreteProductSaleTest extends BaseManagerIntegrationTestCase
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
		this.appUserModel.setAppUserId(1L);
		baseWrapper.setBasePersistableModel(appUserModel);
		appUserModel = (AppUserModel) this.appUserManager.loadAppUser(baseWrapper).getBasePersistableModel();
	}

	@AfterClass
	protected void tearDown()
	{
		workFlowController = null;
		appUserManager = null;
	}

	public void testDoDiscreteProductSaleTransaction() throws Exception
	{
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		workFlowWrapper.setAppUserModel(new AppUserModel());
		workFlowWrapper.setCustomerAppUserModel(new AppUserModel());
		workFlowWrapper.setTransactionTypeModel(new TransactionTypeModel());
		workFlowWrapper.setDeviceTypeModel(new DeviceTypeModel());
		workFlowWrapper.setProductModel(new ProductModel());
		workFlowWrapper.setSmartMoneyAccountModel(new SmartMoneyAccountModel());
                workFlowWrapper.setAccountInfoModel( new AccountInfoModel() );

		workFlowWrapper.setAppUserModel(this.appUserModel);
		workFlowWrapper.getTransactionTypeModel().setTransactionTypeId(
				TransactionTypeConstantsInterface.CUST_DISC_PRODUCT_SALE_TX);

		workFlowWrapper.getDeviceTypeModel().setDeviceTypeId(DeviceTypeConstantsInterface.MOBILE);
		workFlowWrapper.getSmartMoneyAccountModel().setSmartMoneyAccountId(14L);

		workFlowWrapper.getProductModel().setProductId(1L);

		workFlowWrapper.getCustomerAppUserModel().setMobileNo("03344227165");
		workFlowWrapper = this.workFlowController.workflowProcess(workFlowWrapper);

	}

}
