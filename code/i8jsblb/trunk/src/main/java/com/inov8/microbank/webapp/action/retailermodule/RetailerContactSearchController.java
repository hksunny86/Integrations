package com.inov8.microbank.webapp.action.retailermodule;

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
import com.inov8.microbank.common.model.AreaModel;
import com.inov8.microbank.common.model.RetailerModel;
import com.inov8.microbank.common.model.retailermodule.RetailerContactListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.retailermodule.RetailerContactManager;
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

public class RetailerContactSearchController extends BaseSearchController
{
  private RetailerContactManager retailerContactManager;
  private ReferenceDataManager referenceDataManager;
  private AppUserManager appUserManager;

  public RetailerContactSearchController()
  {
    //super.setCommandName("retailerContactListViewModel");
    //super.setCommandClass(RetailerContactListViewModel.class);
    super.setFilterSearchCommandClass(RetailerContactListViewModel.class);
  }

  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                        HttpServletResponse response,Boolean activate) throws
        Exception
    {
      

      Long id = ServletRequestUtils.getLongParameter(request, "retailerContactId");
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
				//BaseWrapper baseWrapperRetailerContact= new BaseWrapperImpl(); 
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				AppUserModel appUserModel = new AppUserModel();
				/*
				RetailerContactModel retailerContactModel =new RetailerContactModel();
				retailerContactModel.setRetailerContactId(id);
				baseWrapperRetailerContact.setBasePersistableModel(retailerContactModel);
				baseWrapperRetailerContact=retailerContactManager.loadRetailerContact(baseWrapperRetailerContact);
				retailerContactModel=(RetailerContactModel)baseWrapperRetailerContact.getBasePersistableModel();
				retailerContactModel.setActive(active);
				baseWrapperRetailerContact.setBasePersistableModel(retailerContactModel);
				retailerContactManager.createOrUpdateRetailerContact(baseWrapperRetailerContact);
				*/
				appUserModel.setRetailerContactId(id);
				searchBaseWrapper.setBasePersistableModel(appUserModel);
				baseWrapper= appUserManager.searchAppUser(searchBaseWrapper);			
				appUserModel=(AppUserModel)baseWrapper.getBasePersistableModel();
				appUserModel.setAccountEnabled(active);
				appUserModel.setUpdatedOn(new Date());
				appUserModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
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
     * code fragment to load reference data for RetailerTypeModel
     */
    RetailerModel retailerModel = new RetailerModel();
    retailerModel.setActive(true);
    ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
        retailerModel, "name", SortingOrder.DESC);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex2)
    {
    }
    List<RetailerModel> RetailerModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      RetailerModelList = referenceDataWrapper.getReferenceDataList();
    }

    Map referenceDataMap = new HashMap();
    referenceDataMap.put("RetailerModelList", RetailerModelList);

    /**
     * code fragment to load reference data for AreaModel
     */
    AreaModel AreaModel = new AreaModel();
//    AreaModel.setActive(true);
    referenceDataWrapper = new ReferenceDataWrapperImpl(
        AreaModel, "name", SortingOrder.DESC);

    try
    {
      referenceDataManager.getReferenceData(referenceDataWrapper);
    }
    catch (FrameworkCheckedException ex1)
    {
    }
    List<AreaModel> AreaModelList = null;
    if (referenceDataWrapper.getReferenceDataList() != null)
    {
      AreaModelList = referenceDataWrapper.getReferenceDataList();
    }

    referenceDataMap.put("AreaModelList", AreaModelList);


    /**
     * code fragment to load reference data for DistributorModel
     */

//    DistributorModel distributorModel = new DistributorModel();
//    distributorModel.setActive(true);
//    referenceDataWrapper = new ReferenceDataWrapperImpl(
//        distributorModel, "name", SortingOrder.DESC);
//    referenceDataWrapper.setBasePersistableModel(distributorModel);
//
//    try
//    {
//      referenceDataManager.getReferenceData(referenceDataWrapper);
//    }
//    catch (FrameworkCheckedException ex)
//    {
//    }
//    List<DistributorModel> DistributorModelList = null;
//    if (referenceDataWrapper.getReferenceDataList() != null)
//    {
//      DistributorModelList = referenceDataWrapper.getReferenceDataList();
//    }
//
//    referenceDataMap.put("DistributorModelList", DistributorModelList);

    return referenceDataMap;

  }
  
  @Override
  protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object model, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	  SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	  searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	  RetailerContactListViewModel retailerContactModel = (RetailerContactListViewModel) model;
	  searchBaseWrapper.setBasePersistableModel(retailerContactModel);
	  if (sortingOrderMap.isEmpty())
	  {
	      sortingOrderMap.put("username", SortingOrder.ASC);
	  }	    
	  searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	  searchBaseWrapper = this.retailerContactManager.searchRetailerContact(
	        searchBaseWrapper);

	  return new ModelAndView(super.getSearchView(), "retailerContactModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());
  }

  public void setRetailerContactManager(RetailerContactManager retailerContactManager)
  {
    this.retailerContactManager = retailerContactManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

public void setAppUserManager(AppUserManager appUserManager) {
	this.appUserManager = appUserManager;
}

}