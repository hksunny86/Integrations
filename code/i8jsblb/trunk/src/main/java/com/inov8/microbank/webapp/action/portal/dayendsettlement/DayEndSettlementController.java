package com.inov8.microbank.webapp.action.portal.dayendsettlement;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.vo.dayendsettlement.DayEndSettlementVo;
import com.inov8.microbank.server.service.portal.dayendsettlement.DayEndSettlementManager;

public class DayEndSettlementController extends BaseSearchController
{
    private DayEndSettlementManager dayEndSettlementManager;

	public DayEndSettlementController()
	{
		setFilterSearchCommandClass(DayEndSettlementVo.class);
	}

	@Override
	protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object command,
			HttpServletRequest request, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{
		List<DayEndSettlementVo> dayEndSettlementVoList = null;

		dayEndSettlementVoList = dayEndSettlementManager.searchDayEndSettlementFiles();
		//CustomList<DayEndSettlementVo> customList = new CustomList<>(dayEndSettlementVoList);
		pagingHelperModel.setTotalRecordsCount(dayEndSettlementVoList.size());
		return new ModelAndView( getSearchView(), "dayEndSettlementVoList", dayEndSettlementVoList );
	}
	
    public void setDayEndSettlementManager(DayEndSettlementManager dayEndSettlementManager)
    {
		this.dayEndSettlementManager = dayEndSettlementManager;
	}

}
