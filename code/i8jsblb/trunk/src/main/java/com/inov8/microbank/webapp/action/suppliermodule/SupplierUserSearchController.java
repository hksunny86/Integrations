package com.inov8.microbank.webapp.action.suppliermodule;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.SupplierModel;
import com.inov8.microbank.common.model.suppliermodule.SupplierUserListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;
import com.inov8.microbank.server.service.suppliermodule.SupplierUserManager;

/**
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backend Application for POS terminals.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Limited</p>
 *
 * @author Asad Hayat
 * @version 1.0
 */

public class SupplierUserSearchController extends BaseSearchController

{
	private SupplierUserManager supplierUserManager;

	private ReferenceDataManager referenceDataManager;
	
	private AppUserManager appUserManager ;

	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public SupplierUserSearchController() {

		super.setFilterSearchCommandClass(SupplierUserListViewModel.class);
	}

	@Override
	protected ModelAndView onToggleActivate(HttpServletRequest request,
			HttpServletResponse response, Boolean activate) throws

	Exception {
		
		Long id = ServletRequestUtils.getLongParameter(request, "supplierUserId");
		Boolean active = ServletRequestUtils.getBooleanParameter(request,
				"_setActivate");

		if (null != id) {
			if (log.isDebugEnabled()) {
				log
						.debug("id is not null....retrieving object from DB and then updating it");
			}

			
			
			

			// Set the active flag
			

			try {
				SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				AppUserModel appUserModel = new AppUserModel();
				appUserModel.setSupplierUserId(id);
				searchBaseWrapper.setBasePersistableModel(appUserModel);
				baseWrapper= appUserManager.searchAppUser(searchBaseWrapper);			
				appUserModel=(AppUserModel)baseWrapper.getBasePersistableModel();
				appUserModel.setAccountEnabled(active);
				appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				appUserModel.setUpdatedOn(new Date());
				baseWrapper.setBasePersistableModel(appUserModel);
				appUserManager.saveOrUpdateAppUser(baseWrapper);
			}

			catch (FrameworkCheckedException ex) {
				if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION == ex
						.getErrorCode()) {
					super.saveMessage(request, "Record could not be saved.");
				}
			}
		}
		ModelAndView modelAndView = new ModelAndView(new RedirectView(
				getSearchView() + ".html"));
		return modelAndView;


	}

	protected Map loadReferenceData(HttpServletRequest httpServletRequest)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside reference data");
		}

		/**
		 * code fragment to load reference data  for Supplier
		 *
		 */

		SupplierModel supplierModel = new SupplierModel();
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
				supplierModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(supplierModel);
		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<SupplierModel> supplierModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null) {
			supplierModelList = referenceDataWrapper.getReferenceDataList();
		}

		Map referenceDataMap = new HashMap();
		referenceDataMap.put("supplierModelList", supplierModelList);

		return referenceDataMap;
	}

	protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object object,
			PagingHelperModel pagingHelperModel, LinkedHashMap sortingOrderMap)
			throws Exception {
		return null;

	}

	public void setReferenceDataManager(
			ReferenceDataManager referenceDataManager) {
		this.referenceDataManager = referenceDataManager;
	}

	public void setSupplierUserManager(SupplierUserManager supplierUserManager) {
		this.supplierUserManager = supplierUserManager;
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
			Object object, HttpServletRequest arg2,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		SupplierUserListViewModel supplierUserListViewModel = (SupplierUserListViewModel) object;
		searchBaseWrapper.setBasePersistableModel(supplierUserListViewModel);
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("username", SortingOrder.ASC);
		}

		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = this.supplierUserManager
				.searchSupplierUser(searchBaseWrapper);

		return new ModelAndView(getSearchView(), "supplierUserModelList",
				searchBaseWrapper.getCustomList().getResultsetList());

	}

	

}
