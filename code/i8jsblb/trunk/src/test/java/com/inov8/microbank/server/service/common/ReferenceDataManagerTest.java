package com.inov8.microbank.server.service.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.microbank.common.model.ServiceTypeModel;
import com.inov8.microbank.common.util.BaseManagerIntegrationTestCase;

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
 * @author not attributable
 * @version 1.0
 */
@Test(groups = { "integration", "commonIntegration" })
public class ReferenceDataManagerTest extends BaseManagerIntegrationTestCase
{
	protected final Log log = LogFactory.getLog(getClass());

	private ReferenceDataManager referenceDataManager = null;

	@BeforeClass
	protected void setUp()
	{
		referenceDataManager = (ReferenceDataManager) getBean("commonFacade");
	}

	@AfterClass
	protected void tearDown()
	{
		referenceDataManager = null;
	}

	public void testGetReferenceData() throws Exception
	{
		ServiceTypeModel serviceTypeModel = new ServiceTypeModel();

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(serviceTypeModel, "name", SortingOrder.DESC);
		referenceDataWrapper.setBasePersistableModel(serviceTypeModel);
		this.referenceDataManager.getReferenceData(referenceDataWrapper);
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			List<ServiceTypeModel> serviceTypeModelList = referenceDataWrapper.getReferenceDataList();
			for (ServiceTypeModel localServiceTypeModel : serviceTypeModelList)
			{
				log.info(localServiceTypeModel);
			}
		}
	}

}
