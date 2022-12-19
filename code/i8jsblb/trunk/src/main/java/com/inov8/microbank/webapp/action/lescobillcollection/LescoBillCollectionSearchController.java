package com.inov8.microbank.webapp.action.lescobillcollection;

import java.util.LinkedHashMap;
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
import com.inov8.microbank.common.model.ExtendedLescoCollectionModel;
import com.inov8.microbank.common.model.LescoCollectionModel;
import com.inov8.microbank.server.service.lescomodule.LescoCollectionManager;

public class LescoBillCollectionSearchController extends BaseFormSearchController {

private LescoCollectionManager lescoCollectionManager;

	

	public LescoBillCollectionSearchController() {
		setCommandName("extendedLescoCollectionModel");
		setCommandClass(ExtendedLescoCollectionModel.class);

	}



	/*public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		LescoCollectionModel lescoCollectionModel = new LescoCollectionModel();
		lescoCollectionModel.setFileName(new Date().toString());
		lescoCollectionModel.setCreatedBy(1L);
		lescoCollectionModel.setUpdatedBy(1L);
		lescoCollectionModel.setFileData("Hi, this is a sample lbc file data sdlkfjsa;dflkjsa;ldkfjsa;ldkjf;aslkdjf;aslkdjf;asldkfj;asldkjf;aslkdjf;aslkdjf;aslkdjf;laskjdf;laksjdf;laksjdf;laskjdf;laksjdf;laksjdf;lkasjd;flkjas;dflkjas;dlkfja;sldkjf;alskdjfsa;ldkfj");
		lescoCollectionModel.setUpdatedOn(new Date());
		lescoCollectionModel.setCreatedOn(new Date());
		BaseWrapper baseWrapper =  new BaseWrapperImpl();
		baseWrapper.setBasePersistableModel(lescoCollectionModel);
		this.lescoCollectionManager.createLescoCollection(baseWrapper);
		return new ModelAndView("lescocollectionmanagement.html");
	}
	*/



	public void setLescoCollectionManager(LescoCollectionManager lescoCollectionManager)
	{
		this.lescoCollectionManager = lescoCollectionManager;
	}



	@Override
	protected Map loadReferenceData(HttpServletRequest arg0) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected ModelAndView onSearch(HttpServletRequest request, HttpServletResponse response, Object object, PagingHelperModel pagingHelperModel, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception
	{
		SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
		searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
		//TransactionDetInfoListViewModel transactionDetInfoListViewModel = (TransactionDetInfoListViewModel) object;
		ExtendedLescoCollectionModel lescoCollectionModel = (ExtendedLescoCollectionModel) object;
		DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel(
				"createdOn", lescoCollectionModel.getStartDate(),
				lescoCollectionModel.getEndDate());
		searchBaseWrapper.setDateRangeHolderModel(dateRangeHolderModel);
		if (sortingOrderMap.isEmpty()) {
			sortingOrderMap.put("createdOn", SortingOrder.DESC);
		}
		searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
		searchBaseWrapper.setBasePersistableModel((LescoCollectionModel)lescoCollectionModel);
		searchBaseWrapper = this.lescoCollectionManager.searchLescoCollection(searchBaseWrapper);
		return new ModelAndView(super.getSuccessView(), "lescoCollectionList", searchBaseWrapper.getCustomList()
				.getResultsetList());
		 
		
	}



	

}
 



