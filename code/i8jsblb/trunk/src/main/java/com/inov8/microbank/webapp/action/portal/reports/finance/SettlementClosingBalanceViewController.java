package com.inov8.microbank.webapp.action.portal.reports.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.portal.financereportsmodule.ExtendedSettlementClosingBalanceViewModel;
import com.inov8.microbank.common.model.portal.financereportsmodule.SettlementClosingBalanceViewModel;
import com.inov8.microbank.common.util.CommonUtils;
import com.inov8.microbank.common.util.LabelValueBean;
import com.inov8.microbank.server.facade.StakeholderFacade;
import com.inov8.microbank.server.facade.portal.financereportsmodule.FinanceReportsFacacde;
/**
 * 
 * @author AtifHu
 *
 */
public class SettlementClosingBalanceViewController extends
		BaseFormSearchController {
	
	private FinanceReportsFacacde financeReportsFacacde;
	private StakeholderFacade stakeholderFacade;

	public SettlementClosingBalanceViewController() {
		super.setCommandName("extendedSettlementClosingBalanceViewModel");
		super.setCommandClass(ExtendedSettlementClosingBalanceViewModel.class);
	}

	@Override
	public void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		super.initBinder(request, binder);
		CommonUtils.bindCustomDateEditor(binder);
	}

	@Override
	protected Map<String, Object> loadReferenceData(HttpServletRequest request)
			throws Exception {
		Long paramAccountType = ServletRequestUtils.getLongParameter(request, "accountType", -2);

		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		
		if (paramAccountType > 0) {
			
			List<Object[]> resultList	=	stakeholderFacade.loadOfSettlementAccounts(paramAccountType);

			LabelValueBean labelValueBean;
			for (Object[] item : resultList) {
				labelValueBean = new LabelValueBean(item[2] + " - " + item[1], String.valueOf(item[0]));
				list.add(labelValueBean);
			}
		}
		referenceDataMap.put("ofSettlementAccountList", list);
		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object model, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap)
			throws Exception {
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		ExtendedSettlementClosingBalanceViewModel extendedSettlementClosingBalanceViewModel = (ExtendedSettlementClosingBalanceViewModel) model;

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"statsDate", extendedSettlementClosingBalanceViewModel.getStartDate(),
				extendedSettlementClosingBalanceViewModel.getEndDate());

		searchBaseWrapper.setBasePersistableModel((SettlementClosingBalanceViewModel) extendedSettlementClosingBalanceViewModel);
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);

		// sorting order
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("blbAccountNumber", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);

		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);

		searchBaseWrapper = financeReportsFacacde.searchSettlementClosingBalanceView(searchBaseWrapper);
		List<Object[]> list = new ArrayList<>();
		if (searchBaseWrapper.getCustomList() != null) {
			list = searchBaseWrapper.getCustomList().getResultsetList();
		}

		List<SettlementClosingBalanceViewModel> settlementClosingBalanceViewModelList = new ArrayList<SettlementClosingBalanceViewModel>();

		SettlementClosingBalanceViewModel settClsBalViewModel;

		for (Object[] resultItem : list) {
			settClsBalViewModel = new SettlementClosingBalanceViewModel();
			settClsBalViewModel.setBlbAccountNumber(resultItem[0] == null ? "" : String.valueOf(resultItem[0]));
			settClsBalViewModel.setOfAccountNumber(resultItem[1] == null ? "" : String.valueOf(resultItem[1]));
			settClsBalViewModel.setBbAccountTitle(resultItem[2] == null ? "" : String.valueOf(resultItem[2]));
			settClsBalViewModel.setCoreAccountTitle(resultItem[3] == null ? "" : String.valueOf(resultItem[3]));
			settClsBalViewModel.setDebitMovement(resultItem[4] == null ? 0.0 : Double.valueOf(String.valueOf(resultItem[4])));
			settClsBalViewModel.setCreditMovement(resultItem[5] == null ? 0.0 : Double.valueOf(String.valueOf(resultItem[5])));
			settClsBalViewModel.setOpeningBalance(resultItem[6] == null ? 0.0 : Double.valueOf(String.valueOf(resultItem[6])));
			settClsBalViewModel.setClosingBalance(resultItem[7] == null ? 0.0 : Double.valueOf(String.valueOf(resultItem[7])));
			
			//settClsBalViewModel.setStatsDate((Date) resultItem[8]);
			settlementClosingBalanceViewModelList.add(settClsBalViewModel);
		}

		pagingHelperModel.setTotalRecordsCount(list.size());

		return new ModelAndView(getSuccessView(),"settlementClosingBalanceViewModelList",settlementClosingBalanceViewModelList);
	}

	public void setFinanceReportsFacacde(
			FinanceReportsFacacde financeReportsFacacde) {
		this.financeReportsFacacde = financeReportsFacacde;
	}

	public void setStakeholderFacade(StakeholderFacade stakeholderFacade) {
		this.stakeholderFacade = stakeholderFacade;
	}
}
