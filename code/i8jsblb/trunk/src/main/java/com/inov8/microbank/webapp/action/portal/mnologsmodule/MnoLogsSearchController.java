package com.inov8.microbank.webapp.action.portal.mnologsmodule;

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
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseFormSearchController;
import com.inov8.microbank.common.model.UsecaseModel;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewModel;
import com.inov8.microbank.common.model.portal.mnologsmodule.MnologsListViewSearchModel;
import com.inov8.microbank.server.facade.usecasemodule.UsecaseFacade;
import com.inov8.microbank.server.service.portal.mnologsmodule.MnoLogsManager;

public class MnoLogsSearchController extends BaseFormSearchController {

	private MnoLogsManager mnoLogsManager;
	private UsecaseFacade usecaseFacade;

	public static final String KEY_ACTION_NAME = "action";

	public MnoLogsSearchController() {
		setCommandName("mnologsListViewSearchModel");
		setCommandClass(MnologsListViewSearchModel.class);
	}

	public void setMnoLogsManager(MnoLogsManager mnoLogsManager) {
		this.mnoLogsManager = mnoLogsManager;
	}

	@Override
	protected Map<String, List<?>> loadReferenceData( HttpServletRequest request ) throws Exception {
		Map<String, List<?>> referenceDataMap = new HashMap<String, List<?>>();
		
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		LinkedHashMap<String, SortingOrder> sortingOrderMap = new LinkedHashMap<String, SortingOrder>();
		
		sortingOrderMap.put("name", SortingOrder.ASC);
        searchBaseWrapper.setSortingOrderMap(sortingOrderMap );
			
		searchBaseWrapper.setBasePersistableModel(new UsecaseModel());
		List<UsecaseModel> usecaseModelList = usecaseFacade.getAllUseCases();//usecaseFacade.searchUsecase(searchBaseWrapper).getCustomList().getResultsetList();
	    referenceDataMap.put("usecaseModelList", usecaseModelList );

		return referenceDataMap;
	}

	@Override
	protected ModelAndView onSearch(HttpServletRequest request,
			HttpServletResponse response, Object model, PagingHelperModel pagingHelperModel,
			LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {

		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();

		MnologsListViewSearchModel mnologsListViewSearchModel = (MnologsListViewSearchModel) model;
		MnologsListViewModel mnologsListViewModel = (MnologsListViewModel) mnologsListViewSearchModel;

		searchBaseWrapper.setBasePersistableModel( mnologsListViewModel );
		
        /*if( sortingOrderMap.isEmpty() ){
        	sortingOrderMap.put("startTime", SortingOrder.DESC);
        /// Comented on Request of DB Team as records are already present in sorted state in DataBase 
        }*/
        searchBaseWrapper.setSortingOrderMap( sortingOrderMap );
	 		
		searchBaseWrapper.setPagingHelperModel( pagingHelperModel );

		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel( "startTime", mnologsListViewSearchModel.getFromDate(), mnologsListViewSearchModel.getToDate() );
		searchBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
		
		searchBaseWrapper = this.mnoLogsManager.searchMnoLogs( searchBaseWrapper );
		List<MnologsListViewModel> list = searchBaseWrapper.getCustomList().getResultsetList();
		ModelAndView modelAndView = new ModelAndView( getSuccessView(), "mnologsListViewModels", list);
		return modelAndView;
	}
	public void setUsecaseFacade(UsecaseFacade usecaseFacade) {
		this.usecaseFacade = usecaseFacade;
	}
}
