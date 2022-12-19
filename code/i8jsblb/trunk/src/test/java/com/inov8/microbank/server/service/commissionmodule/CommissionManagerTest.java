package com.inov8.microbank.server.service.commissionmodule;

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
 * Company: inov8 Limited
 * </p>
 * 
 * @author Jawwad Farooq
 * @version 1.0
 */



import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.inov8.microbank.common.model.CommissionRateModel;
import com.inov8.microbank.common.model.PaymentModeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.BaseManagerIntegrationTestCase;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapper;
import com.inov8.microbank.common.wrapper.commission.CommissionWrapperImpl;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.commissionmodule.CommissionManager;

@Test(groups = { "integration", "commissionIntegration" })
public class CommissionManagerTest extends BaseManagerIntegrationTestCase
{
	protected final Log log = LogFactory.getLog(getClass());

	private CommissionManager commissionManager = null;

	@BeforeClass
	protected void setUp()
	{
		commissionManager = (CommissionManager) getBean("commissionFacade");
	}

	@AfterClass
	protected void tearDown()
	{
		commissionManager = null;
	}

	public void testGetSupplierClassPath() throws Exception
	{
		WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();
		CommissionWrapper commission = new CommissionWrapperImpl();

		TransactionModel txModel = new TransactionModel();
		txModel.setTransactionAmount(1000D);

		ProductModel product = new ProductModel();
		product.setPrimaryKey(1l);

		PaymentModeModel payMode = new PaymentModeModel();
		payMode.setPrimaryKey(1l);

		TransactionTypeModel txType = new TransactionTypeModel();
		txType.setPrimaryKey(1l);
		txModel.setPaymentModeIdPaymentModeModel(payMode);
		txModel.setTransactionTypeIdTransactionTypeModel(txType);
		workFlowWrapper.setTransactionModel(txModel);
		workFlowWrapper.setProductModel(product);
		// workFlowWrapper.setPaymentModeModel(payMode);
		// workFlowWrapper.setTransactionTypeModel(txType);

		commission = this.commissionManager.calculateCommission(workFlowWrapper);

		HashMap resultHashMap = commission.getCommissionWrapperHashMap();

		if (resultHashMap != null)
		{
			List<CommissionRateModel> resultSetList = (List) resultHashMap.get("commissionRateDetailsList");

			CommissionAmountsHolder comm = (CommissionAmountsHolder) resultHashMap.get("commissionAmountsHolder");

			log.info("\n\n\n" + " Transaction Amount : " + comm.getTransactionAmount() + "\n" + " Total Amount : "
					+ comm.getTotalAmount() + "\n" + " Billing Organization Amount : " + comm.getBillingOrganizationAmount()
					+ "\n" + " Commission Amount : " + comm.getTotalCommissionAmount());

			for (CommissionRateModel commModel : resultSetList)
			{
//				log.info("\n" + " Rate : " + commModel.getRate() + "\n" + " Is Extra : " + commModel.getExtra());
			}
		}
	}
}