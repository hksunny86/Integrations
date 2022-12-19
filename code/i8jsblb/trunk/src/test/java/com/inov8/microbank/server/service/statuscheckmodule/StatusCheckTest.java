package com.inov8.microbank.server.service.statuscheckmodule;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.IntegrationModuleModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.ServiceModel;
import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.BaseManagerIntegrationTestCase;
import com.inov8.microbank.common.util.ServiceTypeConstants;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.statuscheckmodule.StatusCheckManager;

/**
 * @author Jawwad Farooq
 * @version 1.0
 */

@Test(groups = { "integration", "statuscheckIntegration" })
public class StatusCheckTest extends BaseManagerIntegrationTestCase
{
	protected final Log log = LogFactory.getLog(getClass());

	private StatusCheckManager statusCheckController = null;

	@BeforeClass
	protected void setUp()
	{
		statusCheckController = (StatusCheckManager) getBean("statusCheckFacade");
	}

	@AfterClass
	protected void tearDown()
	{
		statusCheckController = null;
	}

	public void testGetSupplierClassPath() throws Exception
	{
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

		TransactionModel txModel = new TransactionModel();
		TransactionTypeModel txType = new TransactionTypeModel();
		PaymentModeModel payment = new PaymentModeModel();
		ServiceTypeModel serviceType = new ServiceTypeModel();
		ServiceModel service = new ServiceModel();

		txType.setTransactionTypeId(TransactionTypeConstantsInterface.CUST_DISC_PRODUCT_SALE_TX);
		txModel.setTransactionTypeIdTransactionTypeModel(txType);
		payment.setName("Credit");
		txModel.setPaymentModeIdPaymentModeModel(payment);
		workFlowWrapper.setTransactionModel(txModel);
		workFlowWrapper.setTransactionTypeModel(txType);
		workFlowWrapper.setPaymentModeModel(payment);

		ProductModel product = new ProductModel();
		SupplierModel supplier = new SupplierModel();

		supplier.setName("i8");
		product.setSupplierIdSupplierModel(supplier);
		serviceType.setName(ServiceTypeConstants.VARIABLE_SERVICE_TYPE);
		service.setServiceTypeIdServiceTypeModel(serviceType);
		product.setServiceIdServiceModel(service);
		workFlowWrapper.setProductModel(product);
		workFlowWrapper.setServiceTypeModel(serviceType);

		SearchBaseWrapper search = statusCheckController.getIMStatus(workFlowWrapper);

		// iterate on the resultset
		// ----------------------------------------------------------------------
		List<IntegrationModuleModel> list = search.getCustomList().getResultsetList();

		log.info("\n\n");

		for (IntegrationModuleModel integration : list)
		{
			log.info("Name : " + integration.getName());
			log.info("\t isActive : " + integration.getStatus());
		}
		// ------------------------------------------------------------------------------------------------
	}

}
