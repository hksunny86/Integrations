/**
 * 
 */
package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.integration.common.model.OlaCustomerAccountTypeModel;
import com.inov8.microbank.common.model.CommissionShSharesModel;
import com.inov8.microbank.common.model.RegistrationStateModel;
import com.inov8.microbank.common.model.portal.level3account.Level3AccountsViewModel;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.common.util.RegistrationStateConstantsInterface;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.facade.portal.level3account.Level3AccountFacade;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;

/**
 * @author NaseerUl  
 *
 */
public class Level3AccountsViewController extends BaseFormSearchController
{
	private Level3AccountFacade level3AccountFacade;
	private CommonFacade commonFacade;
	private AccountFacade accountFacade;

	public Level3AccountsViewController()
	{
		setCommandName("level3AccountsViewModel");
		setCommandClass(Level3AccountsViewModel.class);
	}

	@Override
	protected Map<String,Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String,Object> refDataMap = new HashMap<String, Object>(2);
    	SearchBaseWrapper searchBaseWrapper=accountFacade.getAllAgentAccountTypes();
		if(searchBaseWrapper.getCustomList()!=null) {
			List<OlaCustomerAccountTypeModel>	accountTypeModelList	=	searchBaseWrapper.getCustomList().getResultsetList();
			refDataMap.put("accountTypeList", accountTypeModelList);
		}
		
		HttpSession session = request.getSession();
		session.removeAttribute("accountModelLevel3");

	    RegistrationStateModel registrationStateModel = new RegistrationStateModel();
	    ReferenceDataWrapper regStateRefDataWrapper = new ReferenceDataWrapperImpl(registrationStateModel,"name", SortingOrder.ASC);
	    try
	    {
	      commonFacade.getReferenceData(regStateRefDataWrapper);
	    }
	    catch (Exception e)
	    {
	    	logger.error(e.getMessage(), e);
	    }
	    
	    List<RegistrationStateModel> registerationStateModelsList = new ArrayList<RegistrationStateModel>();
	    registerationStateModelsList = regStateRefDataWrapper.getReferenceDataList();
	    CopyOnWriteArrayList<RegistrationStateModel> modifiedRegistrationStateModelsList = new CopyOnWriteArrayList<>();
	    modifiedRegistrationStateModelsList.addAll(registerationStateModelsList);
	    
	    for(RegistrationStateModel regStateModel : modifiedRegistrationStateModelsList ){
	    	if(regStateModel.getRegistrationStateId().longValue() == RegistrationStateConstantsInterface.CLOSED){
	    		modifiedRegistrationStateModelsList.remove(regStateModel);
	    	}
	    }
	    refDataMap.put("registrationStateModelList", modifiedRegistrationStateModelsList);
		return refDataMap;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object command, PagingHelperModel pagingHelperModel, LinkedHashMap<String,SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		Level3AccountsViewModel level3AccountsViewModel = (Level3AccountsViewModel) command;
		searchBaseWrapper.setBasePersistableModel(level3AccountsViewModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn",level3AccountsViewModel.getStartDate(), level3AccountsViewModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		if(sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = level3AccountFacade.searchLevel3AccountsView(searchBaseWrapper);
		CustomList<Level3AccountsViewModel> customList = searchBaseWrapper.getCustomList();
		List<Level3AccountsViewModel> level3AccountsViewModelList = null;
		if(customList != null)
		{
			level3AccountsViewModelList = customList.getResultsetList();
		}
		return new ModelAndView(getFormView(), "level3AccountsViewModelList", level3AccountsViewModelList);
	}

	public void setLevel3AccountFacade(Level3AccountFacade level3AccountFacade)
	{
		this.level3AccountFacade = level3AccountFacade;
	}

	public void setCommonFacade(CommonFacade commonFacade)
	{
		this.commonFacade = commonFacade;
	}

	public void setAccountFacade(AccountFacade accountFacade) {
		this.accountFacade = accountFacade;
	}

}
