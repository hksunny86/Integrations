package com.inov8.microbank.server.service.customermodule;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.CustomerRemitterModel;
import com.inov8.microbank.common.model.SegmentModel;

public interface CustomerManager {

	
public SearchBaseWrapper loadCustomerListView(SearchBaseWrapper searchBaseWrapper) throws
    FrameworkCheckedException;



public SearchBaseWrapper searchCustomerListView(SearchBaseWrapper
                                            searchBaseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper createCustomer(BaseWrapper
                                      baseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper updateCustomer(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

public BaseWrapper createAppUserForCustomer(BaseWrapper baseWrapper) throws
    FrameworkCheckedException;

	public int getCustomerCountByCustomerAccountType(Long customerAccountTypeId) throws FrameworkCheckedException;
// Customer Segment Management

public SearchBaseWrapper searchCustomerSegmentList(SearchBaseWrapper searchBaseWrapper) throws
FrameworkCheckedException;

public BaseWrapper loadCustomerSegment(BaseWrapper baseWrapper) throws
FrameworkCheckedException;

public SearchBaseWrapper loadCustomerSegment(SearchBaseWrapper searchBaseWrapper) throws
FrameworkCheckedException;

public BaseWrapper createOrUpdateCustomerSegment(BaseWrapper baseWrapper) throws
FrameworkCheckedException;



public int countByExample(CustomerModel customerModel, ExampleConfigHolderModel exampleConfigHolder);

public CustomerModel findByPrimaryKey(Long primaryKey) throws FrameworkCheckedException;

public List<CustomerModel> getCustomerModelListByCustomerIDs(List<Long> customerIDsList) throws FrameworkCheckedException;
public void updateCustomerModels(List<CustomerModel> customerModels) throws FrameworkCheckedException;
public List<CustomerRemitterModel> getRemittanceModelList(Long customerId) throws FrameworkCheckedException;

List<SegmentModel> findActiveSegments(SegmentModel segmentModel) throws FrameworkCheckedException;

void updateBulkCustomerSegments(List<CustomerModel> customerModelList) throws FrameworkCheckedException;
}
