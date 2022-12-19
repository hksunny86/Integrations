package com.inov8.microbank.webapp.action.bankmodule;

import java.util.Date;
import java.util.LinkedHashMap;
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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.bankmodule.BankUserListViewModel;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.bankmodule.BankUserManager;
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

public class BankUserSearchController
    extends BaseSearchController

{
  private BankUserManager bankUserManager;
  private ReferenceDataManager referenceDataManager;
  private AppUserManager appUserManager ;
  public void setAppUserManager(AppUserManager appUserManager) {
	this.appUserManager = appUserManager;
}





public BankUserSearchController()
  {
    
    super.setFilterSearchCommandClass(BankUserListViewModel.class);
  }





  @Override
  protected ModelAndView onToggleActivate(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Boolean activate) throws

      Exception
  {
   
	  
	  Long id = ServletRequestUtils.getLongParameter(request, "bankUserId");
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
				appUserModel.setBankUserId(id);
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
    
    return null;
  }
  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
 {
   this.referenceDataManager = referenceDataManager;
 }

  public void setBankUserManager(BankUserManager bankUserManager)
  {
    this.bankUserManager = bankUserManager;
  }





@Override
protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	// TODO Auto-generated method stub
	
	 SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
	   BankUserListViewModel bankUserListViewModel = (
	        BankUserListViewModel)
	        object;
	    searchBaseWrapper.setBasePersistableModel(bankUserListViewModel);
	    searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
	    if(sortingOrderMap.isEmpty())
	        sortingOrderMap.put("username", SortingOrder.ASC);
	    searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
	    searchBaseWrapper = this.bankUserManager.searchBankUser(
	        searchBaseWrapper);

	    return new ModelAndView(getSearchView(), "bankUserModelList",
	                            searchBaseWrapper.getCustomList().getResultsetList());


}

}
