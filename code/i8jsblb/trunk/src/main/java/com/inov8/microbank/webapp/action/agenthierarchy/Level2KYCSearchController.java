/**
 * 
 */
package com.inov8.microbank.webapp.action.agenthierarchy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.KYCModel;
import com.inov8.microbank.server.facade.CommonFacade;
import com.inov8.microbank.server.facade.portal.mfsaccountmodule.MfsAccountFacade;
import com.inov8.ola.server.facade.AccountFacade;
import com.inov8.ola.util.CustomerAccountTypeConstants;

/**
 * @author NaseerUl
 * 
 */
public class Level2KYCSearchController extends BaseFormSearchController
{
	private MfsAccountFacade mfsAccountFacade;
	private CommonFacade commonFacade;
	private AccountFacade accountFacade;

	public Level2KYCSearchController()
	{
		setCommandName("level2KycModel");
		setCommandClass(KYCModel.class);
	}

	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest request) throws Exception
	{
		Map<String, Object> refDataMap = new HashMap<String, Object>(2);
		return refDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object command, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		KYCModel kycSearchModel = (KYCModel) command;
		kycSearchModel.setAcType(CustomerAccountTypeConstants.LEVEL_2);
		searchBaseWrapper.setBasePersistableModel(kycSearchModel);
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn", kycSearchModel.getStartDate(),
				kycSearchModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		if (sortingOrderMap.isEmpty())
		{
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper = mfsAccountFacade.searchKYC(searchBaseWrapper);

		CustomList<KYCModel> customList = searchBaseWrapper.getCustomList();

		List<KYCModel> kycModelList = null;

		if (customList != null)
		{
			kycModelList = customList.getResultsetList();
			for(KYCModel kyc : kycModelList){
				kyc.setStartDate(kycSearchModel.getStartDate());
				kyc.setEndDate(kycSearchModel.getEndDate());
			}
		}
		
		return new ModelAndView(getFormView(), "kycModelList", kycModelList);
	}

	public void setMfsAccountFacade(MfsAccountFacade mfsAccountFacade)
	{
		this.mfsAccountFacade = mfsAccountFacade;
	}

	public void setCommonFacade(CommonFacade commonFacade)
	{
		this.commonFacade = commonFacade;
	}

	public void setAccountFacade(AccountFacade accountFacade)
	{
		this.accountFacade = accountFacade;
	}

}
