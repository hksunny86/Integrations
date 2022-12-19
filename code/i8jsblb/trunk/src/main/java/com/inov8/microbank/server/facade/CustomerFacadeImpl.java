package com.inov8.microbank.server.facade;

import java.util.List;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.CustomerModel;
import com.inov8.microbank.common.model.CustomerRemitterModel;
import com.inov8.microbank.common.model.SegmentModel;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.server.service.customermodule.CustTransManager;
import com.inov8.microbank.server.service.customermodule.CustomerManager;

/**
 *
 * <p>
 * Title: Microbank
 * </p>
 *
 * <p>
 * Description: Backened application for POS terminal
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company: Inov8 Ltd
 * </p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 *
 */

public class CustomerFacadeImpl implements CustomerFacade {
	private CustTransManager custTransManager;
	private CustomerManager customerManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public CustomerFacadeImpl() {
	}

	public void setCustTransManager(CustTransManager custTransManager) {
		this.custTransManager = custTransManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	public SearchBaseWrapper searchCustTrans(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			this.custTransManager.searchCustTrans(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
		return searchBaseWrapper;
	}

	public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			this.custTransManager.saveOrUpdate(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
		return baseWrapper;
	}

	public WorkFlowWrapper saveCustomerAndUser(WorkFlowWrapper wrapper)
			throws FrameworkCheckedException {
		try {
			this.custTransManager.saveCustomerAndUser(wrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
		return wrapper;
	}

	public SearchBaseWrapper loadCustomer(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			this.custTransManager.loadCustomer(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return searchBaseWrapper;
	}

	public BaseWrapper loadCustomer(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			this.custTransManager.loadCustomer(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return baseWrapper;
	}

	public SearchBaseWrapper searchCustomer(SearchBaseWrapper searchBaseWrapper)
	throws FrameworkCheckedException
	{
		try
		{
			this.custTransManager.searchCustomer(searchBaseWrapper);
		} catch (Exception ex)
		{
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
		}
		return searchBaseWrapper;
	}

        public BaseWrapper searchCustomerByMobile(BaseWrapper baseWrapper)
        throws FrameworkCheckedException
        {
                try
                {
                  return this.custTransManager.searchCustomerByMobile(baseWrapper);
                }
                catch (Exception ex)
                {
                  throw this.frameworkExceptionTranslator.translate(ex,
                		  FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
                }
        }

		public BaseWrapper createAppUserForCustomer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
			 try {
			this.customerManager.createAppUserForCustomer(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.INSERT_ACTION);
		}
		return baseWrapper;
		}

		public BaseWrapper createCustomer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
			try {
				this.customerManager.createCustomer(baseWrapper);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.INSERT_ACTION);
			}
			return baseWrapper;
		}
		
		public int getCustomerCountByCustomerAccountType(Long customerAccountTypeId) throws FrameworkCheckedException {
			int count = 0;
			try {
				count = this.customerManager.getCustomerCountByCustomerAccountType(customerAccountTypeId);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.FIND_ACTION);
			}
			return count;
		}

		public BaseWrapper updateCustomer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
			try {
				this.customerManager.updateCustomer(baseWrapper);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.UPDATE_ACTION);
			}
			return baseWrapper;
		}

		public void setCustomerManager(CustomerManager customerManager) {
			this.customerManager = customerManager;
		}

		public SearchBaseWrapper loadCustomerListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
			try {
				this.customerManager.loadCustomerListView(searchBaseWrapper);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
			}
			return searchBaseWrapper;
		}

		public SearchBaseWrapper searchCustomerListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
			try {
				this.customerManager.searchCustomerListView(searchBaseWrapper);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.FIND_ACTION);
			}
			return searchBaseWrapper;
		}

		@Override
		public SearchBaseWrapper searchCustomerSegmentList(
				SearchBaseWrapper searchBaseWrapper)
				throws FrameworkCheckedException {
			
			    try
			    {
			      this.customerManager.searchCustomerSegmentList(searchBaseWrapper);
			    }

			    catch (Exception ex)
			    {
			      throw this.frameworkExceptionTranslator.translate(ex,
			    		  FrameworkExceptionTranslator.FIND_ACTION);
			    }
			    return searchBaseWrapper;
		}

		@Override
		public BaseWrapper loadCustomerSegment(BaseWrapper baseWrapper)
				throws FrameworkCheckedException {
			 try
			    {
			      this.customerManager.loadCustomerSegment(baseWrapper);
			    }
			    catch (Exception ex)
			    {
			      throw this.frameworkExceptionTranslator.translate(ex,
			    		  FrameworkExceptionTranslator.
			          FIND_BY_PRIMARY_KEY_ACTION);
			    }
			    return baseWrapper;
		}

		@Override
		public BaseWrapper createOrUpdateCustomerSegment(BaseWrapper baseWrapper)
				throws FrameworkCheckedException {
			try
		    {
		      this.customerManager.createOrUpdateCustomerSegment(baseWrapper);
		    }
		    catch (Exception ex)
		    {
		      throw this.frameworkExceptionTranslator.translate(ex,
		    		  FrameworkExceptionTranslator.INSERT_ACTION);
		    }
		    return baseWrapper;
		}

		@Override
		public SearchBaseWrapper loadCustomerSegment(SearchBaseWrapper searchBaseWrapper) throws
	      FrameworkCheckedException
	  {
	    try
	    {
	      this.customerManager.loadCustomerSegment(searchBaseWrapper);
	    }
	    catch (Exception ex)
	    {
	      throw this.frameworkExceptionTranslator.translate(ex,
	    		  FrameworkExceptionTranslator.
	          FIND_BY_PRIMARY_KEY_ACTION);
	    }
	    return searchBaseWrapper;

	  }

		@Override
		public int countByExample(CustomerModel customerModel,
				ExampleConfigHolderModel exampleConfigHolder) {

			return this.customerManager.countByExample(customerModel, exampleConfigHolder);
		}

		@Override
		public BaseWrapper searchCustomerByInitialAppFormNo(BaseWrapper wrapper)
				throws FrameworkCheckedException {
			return this.custTransManager.searchCustomerByInitialAppFormNo(wrapper);
		}
		
		@Override
		public CustomerModel findByPrimaryKey(Long primaryKey) throws FrameworkCheckedException {
			return customerManager.findByPrimaryKey(primaryKey);
		}

		@Override
		public List<CustomerModel> getCustomerModelListByCustomerIDs(
				List<Long> customerIDsList) throws FrameworkCheckedException {
			 try
			    {
			      return this.customerManager.getCustomerModelListByCustomerIDs(customerIDsList);
			    }
			    catch (FrameworkCheckedException ex)
			    {
			      throw this.frameworkExceptionTranslator.translate(ex,
			    		  FrameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
			    }
		}

		@Override
		public void updateCustomerModels(List<CustomerModel> customerModels)
				throws FrameworkCheckedException {
			 try
			    {
			     customerManager.updateCustomerModels(customerModels);
			    }
			    catch (FrameworkCheckedException ex)
			    {
			      throw this.frameworkExceptionTranslator.translate(ex,
			    		  FrameworkExceptionTranslator.UPDATE_ACTION);
			    }
			
		}
		
		@Override
		public void saveOrUpdateCustomerRemitter(
				List<CustomerRemitterModel> customerRemitterModelList)
				throws FrameworkCheckedException {
			try {
				this.custTransManager.saveOrUpdateCustomerRemitter(customerRemitterModelList);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.INSERT_ACTION);
			}
			
		}

		@Override
		public List<CustomerRemitterModel> getRemittanceModelList(
				Long customerId) throws FrameworkCheckedException {
			try {
				return this.customerManager.getRemittanceModelList(customerId);
			} catch (Exception ex) {
				throw this.frameworkExceptionTranslator.translate(ex,
						FrameworkExceptionTranslator.FIND_ACTION);
			}
		}

	@Override
	public List<SegmentModel> findActiveSegments(SegmentModel segmentModel) throws FrameworkCheckedException {
		return customerManager.findActiveSegments(segmentModel);
	}

	@Override
	public void updateBulkCustomerSegments(List<CustomerModel> customerModelList) throws FrameworkCheckedException {
		customerManager.updateBulkCustomerSegments(customerModelList);
	}


}
