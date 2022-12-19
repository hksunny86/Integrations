package com.inov8.microbank.webapp.action.distributormodule;

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
import com.inov8.microbank.common.model.DistributorLevelModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.distributormodule.DistributorContactListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.distributormodule.DistributorContactManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

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

public class DistributorContactSearchController
    extends BaseSearchController

{
  private DistributorContactManager distributorContactManager;
  private ReferenceDataManager referenceDataManager;
  private AppUserManager appUserManager;

  public void setAppUserManager(AppUserManager appUserManager) {
	this.appUserManager = appUserManager;
}

public DistributorContactSearchController()
  {
    super.setFilterSearchCommandClass(DistributorContactListViewModel.class);
  }

  public void setDistributorContactManager(DistributorContactManager
                                           distributorContactManager)
  {
    this.distributorContactManager = distributorContactManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws

      Exception
  {
    
   
    Long id = ServletRequestUtils.getLongParameter(request, "distributorContactId");
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
			//SearchBaseWrapper searchBaseWrapperDistributorContact = new SearchBaseWrapperImpl();
			BaseWrapper baseWrapper = new BaseWrapperImpl();
			AppUserModel appUserModel = new AppUserModel();
			/*
		    DistributorContactModel distributorContactModel =new DistributorContactModel();
			distributorContactModel.setDistributorContactId(id);
			searchBaseWrapperDistributorContact.setBasePersistableModel(distributorContactModel);
			searchBaseWrapperDistributorContact=distributorContactManager.loadDistributorContact(searchBaseWrapperDistributorContact);
			distributorContactModel=(DistributorContactModel)searchBaseWrapperDistributorContact.getBasePersistableModel();
			distributorContactModel.setActive(active);
			baseWrapper.setBasePersistableModel(distributorContactModel);
			distributorContactManager.updateDistributorContactModel(baseWrapper);
			*/
			appUserModel.setDistributorContactId(id);
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

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    if (log.isDebugEnabled())
    {
      log.debug("Inside reference data");
    }

    /**
     * code fragment to load reference data  for Distributor
     *
     */

    DistributorModel distributorModel = new DistributorModel();
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        distributorModel, "name", SortingOrder.DESC);
    referenceDataWrapper.setBasePersistableModel(distributorModel);
    referenceDataManager.getReferenceData(referenceDataWrapper);
    List<DistributorModel> distributorModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      distributorModelList = referenceDataWrapper.
          getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("distributorModelList",
                         distributorModelList);

    /**
     * code fragment to load reference data for Distributor Contact
     */

//    DistributorContactModel distributorContactModel = new
//        DistributorContactModel();
//    referenceDataWrapper = new ReferenceDataWrapperImpl(
//        distributorContactModel, "firstName", SortingOrder.DESC);
//    referenceDataWrapper.setBasePersistableModel(distributorContactModel);
//
//    referenceDataManager.getReferenceData(referenceDataWrapper);
//    List<DistributorContactModel> distributorContactModelList = null;
//    if (referenceDataWrapper.getReferenceDataList() != null)
//    {
//      distributorContactModelList = referenceDataWrapper.getReferenceDataList();
//    }
//
//    referenceDataMap.put("distributorContactModelList",
//                         distributorContactModelList);

    /**
     * code fragment to load reference data for Distributor Level
     */

    DistributorLevelModel distributorLevelModel = new DistributorLevelModel();
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        distributorLevelModel, "name", SortingOrder.DESC);
    referenceDataWrapper.setBasePersistableModel(distributorLevelModel);

    referenceDataManager.getReferenceData(referenceDataWrapper);
    List<DistributorLevelModel> distributorLevelModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      distributorLevelModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("distributorLevelModelList", distributorLevelModelList);

    return referenceDataMap;
  }
  
  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	  DistributorContactListViewModel distributorContactListViewModel = (
	        DistributorContactListViewModel)object;
	  searchBaseWrapper.setBasePersistableModel(distributorContactListViewModel);
	  if(sortingOrderMap.isEmpty())
	      sortingOrderMap.put("username", SortingOrder.ASC);
	  searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	  searchBaseWrapper = this.distributorContactManager.searchDistributorContact(
	        searchBaseWrapper);

	  return new ModelAndView(getSearchView(), "distributorContactList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
  }

}
