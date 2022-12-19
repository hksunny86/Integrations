package com.inov8.microbank.webapp.action.customermodule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.MobileTypeModel;
import com.inov8.microbank.common.model.customermodule.CustomerListViewModel;
import com.inov8.microbank.common.util.MessageUtil;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.customermodule.CustomerManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;


public class CustomerFormController extends AdvanceFormController {
	private CustomerManager customerManager;

	private AppUserManager appUserManager;

	private ReferenceDataManager referenceDataManager;

	private Long id = null;

	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public CustomerFormController() {
		setCommandName("customerListViewModel");
		setCommandClass(CustomerListViewModel.class);
	}

	@Override
	protected Object loadFormBackingObject(HttpServletRequest httpServletRequest)
			throws Exception {
		id = ServletRequestUtils.getLongParameter(httpServletRequest,
				"customerId");
		if (null != id) {
			if (log.isDebugEnabled()) {
				log.debug("id is not null....retrieving object from DB");
			}

			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			CustomerListViewModel customerListViewModel = new CustomerListViewModel();

			customerListViewModel.setCustomerId(id);

			searchBaseWrapper.setBasePersistableModel(customerListViewModel);
			searchBaseWrapper = this.customerManager
					.loadCustomerListView(searchBaseWrapper);
			customerListViewModel = (CustomerListViewModel) searchBaseWrapper
					.getBasePersistableModel();

			return customerListViewModel;
		} else {
			if (log.isDebugEnabled()) {
				log.debug("id is null....creating new instance of Model");
			}

			return new CustomerListViewModel();
		}

	}

	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception {

		MobileTypeModel mobileTypeModel = new MobileTypeModel();

		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				mobileTypeModel, "name", SortingOrder.ASC);

		try {
			referenceDataManager.getReferenceData(referenceDataWrapper);
		} catch (FrameworkCheckedException ex1) {
			ex1.getStackTrace();
		}
		List<MobileTypeModel> mobileTypeModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			mobileTypeModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();

		referenceDataMap.put("mobileTypeModelList", mobileTypeModelList);

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	@Override
	protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			BindException bindException) throws Exception {
		return this.createOrUpdate(httpServletRequest, httpServletResponse,
				object, bindException);
	}

	private ModelAndView createOrUpdate(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		try {
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			long useCaseId = 0;
			long actionId = 0;

			CustomerListViewModel customerListViewModel = (CustomerListViewModel) command;
					
			
			
			if (request.getParameter(PortalConstants.KEY_USECASE_ID) != null)
			{
				useCaseId = Long.parseLong(request.getParameter(PortalConstants.KEY_USECASE_ID));
				System.out.println("usecase id " + useCaseId);
				baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, useCaseId);

			}
			if (request.getParameter(PortalConstants.KEY_ACTION_ID) != null)
			{
				actionId = Long.parseLong(request.getParameter(PortalConstants.KEY_ACTION_ID));
				System.out.println("action id " + actionId);
				baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, actionId);
			}
			

			if (null != id) {

				baseWrapper.setBasePersistableModel(customerListViewModel);

				baseWrapper = this.customerManager.updateCustomer(
				baseWrapper);

			} else {

				baseWrapper.setBasePersistableModel(customerListViewModel);
				baseWrapper = this.customerManager
						.createCustomer(baseWrapper);

			}

						this.saveMessage(request, "Record saved successfully");
			ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
			return modelAndView;

		} catch (FrameworkCheckedException ex) {
			int checkCode=1;			
			request.setAttribute("errorCode",checkCode);
			if( ex.getMessage().equalsIgnoreCase("MobileNumUniqueException") )
			{
				super.saveMessage(request, "Mobile number already exists.");
				return super.showForm(request, response, errors);				
			}			
			else if( ex.getMessage().equalsIgnoreCase("UsernameUniqueException") )
			{
				super.saveMessage(request, "User name already exists.");
				return super.showForm(request, response, errors);				
			}
			
			else if("MobileNumUniqueException".equals(ex.getMessage()))
			{
				this.saveMessage(request, super.getText("newMfsAccount.mobileNumNotUnique", request.getLocale() ));
				return super.showForm(request, response, errors);
			}
			
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
					.getErrorCode()) {
				super.saveMessage(request, "Record could not be saved.");
				return super.showForm(request, response, errors);
			}
			throw ex;
		}
		catch (Exception ex) {
			int checkCode=1;			
			request.setAttribute("errorCode",checkCode);
			
			super.saveMessage(request,MessageUtil.getMessage("6075"));
			return super.showForm(request, response, errors);
		}
	}

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

}
