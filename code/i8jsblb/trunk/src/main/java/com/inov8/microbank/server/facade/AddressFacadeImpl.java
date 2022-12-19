package com.inov8.microbank.server.facade;

import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.addressmodule.AddressManager;

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
 * Copyright: Copyright (c) 2012
 * </p>
 *
 * <p>
 * Company: Inov8 Ltd
 * </p>
 *
 * @author Usman Ashraf
 * @version 1.0
 *
 */

public class AddressFacadeImpl implements AddressFacade {
	private AddressManager addressManager;
	

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public AddressFacadeImpl() {
	}

	public void setAddressManager(AddressManager addressManager) {
		this.addressManager = addressManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

	@Override
	public SearchBaseWrapper getPostalOfficesByCity(
			SearchBaseWrapper searchBaseWrapper) {
		return addressManager.getPostalOfficesByCity(searchBaseWrapper);
	}

//	public BaseWrapper saveOrUpdate(BaseWrapper baseWrapper)
//			throws FrameworkCheckedException {
//		try {
//			this.custTransManager.saveOrUpdate(baseWrapper);
//		} catch (Exception ex) {
//			throw this.frameworkExceptionTranslator.translate(ex,
//					this.frameworkExceptionTranslator.INSERT_ACTION);
//		}
//		return baseWrapper;
//	}

//	public WorkFlowWrapper saveCustomerAndUser(WorkFlowWrapper wrapper)
//			throws FrameworkCheckedException {
//		try {
//			this.custTransManager.saveCustomerAndUser(wrapper);
//		} catch (Exception ex) {
//			throw this.frameworkExceptionTranslator.translate(ex,
//					this.frameworkExceptionTranslator.INSERT_ACTION);
//		}
//		return wrapper;
//	}
//
//	public SearchBaseWrapper loadCustomer(SearchBaseWrapper searchBaseWrapper)
//			throws FrameworkCheckedException {
//		try {
//			this.custTransManager.loadCustomer(searchBaseWrapper);
//		} catch (Exception ex) {
//			throw this.frameworkExceptionTranslator.translate(ex,
//							this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
//		}
//		return searchBaseWrapper;
//	}
//
//	public BaseWrapper loadCustomer(BaseWrapper baseWrapper)
//			throws FrameworkCheckedException {
//		try {
//			this.custTransManager.loadCustomer(baseWrapper);
//		} catch (Exception ex) {
//			throw this.frameworkExceptionTranslator.translate(ex,
//							this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
//		}
//		return baseWrapper;
//	}
//
//	public SearchBaseWrapper searchCustomer(SearchBaseWrapper searchBaseWrapper)
//	throws FrameworkCheckedException
//	{
//		try
//		{
//			this.custTransManager.searchCustomer(searchBaseWrapper);
//		} catch (Exception ex)
//		{
//			throw this.frameworkExceptionTranslator.translate(ex,
//					this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
//		}
//		return searchBaseWrapper;
//	}
//
//        public BaseWrapper searchCustomerByMobile(BaseWrapper baseWrapper)
//        throws FrameworkCheckedException
//        {
//                try
//                {
//                  return this.custTransManager.searchCustomerByMobile(baseWrapper);
//                }
//                catch (Exception ex)
//                {
//                  throw this.frameworkExceptionTranslator.translate(ex,
//                      this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
//                }
//        }
//
//		public BaseWrapper createAppUserForCustomer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
//			 try {
//			this.customerManager.createAppUserForCustomer(baseWrapper);
//		} catch (Exception ex) {
//			throw this.frameworkExceptionTranslator.translate(ex,
//					this.frameworkExceptionTranslator.INSERT_ACTION);
//		}
//		return baseWrapper;
//		}
//
//		public BaseWrapper createCustomer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
//			try {
//				this.customerManager.createCustomer(baseWrapper);
//			} catch (Exception ex) {
//				throw this.frameworkExceptionTranslator.translate(ex,
//						this.frameworkExceptionTranslator.INSERT_ACTION);
//			}
//			return baseWrapper;
//		}
//
//		public BaseWrapper updateCustomer(BaseWrapper baseWrapper) throws FrameworkCheckedException {
//			try {
//				this.customerManager.updateCustomer(baseWrapper);
//			} catch (Exception ex) {
//				throw this.frameworkExceptionTranslator.translate(ex,
//						this.frameworkExceptionTranslator.UPDATE_ACTION);
//			}
//			return baseWrapper;
//		}
//
//		public void setCustomerManager(CustomerManager customerManager) {
//			this.customerManager = customerManager;
//		}
//
//		public SearchBaseWrapper loadCustomerListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
//			try {
//				this.customerManager.loadCustomerListView(searchBaseWrapper);
//			} catch (Exception ex) {
//				throw this.frameworkExceptionTranslator.translate(ex,
//						this.frameworkExceptionTranslator.FIND_BY_PRIMARY_KEY_ACTION);
//			}
//			return searchBaseWrapper;
//		}
//
//		public SearchBaseWrapper searchCustomerListView(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException {
//			try {
//				this.customerManager.searchCustomerListView(searchBaseWrapper);
//			} catch (Exception ex) {
//				throw this.frameworkExceptionTranslator.translate(ex,
//						this.frameworkExceptionTranslator.FIND_ACTION);
//			}
//			return searchBaseWrapper;
//		}


}
