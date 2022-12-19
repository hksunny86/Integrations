package com.inov8.microbank.webapp.action.tickermodule;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.tickermodule.TickerListViewModel;
import com.inov8.microbank.server.service.tickermodule.TickerManager;

public class TickerSearchController  extends BaseSearchController {

	 private TickerManager tickerManager;
	  private ReferenceDataManager referenceDataManager;

	 public void setTickerManager(TickerManager tickerManager) {
		this.tickerManager = tickerManager;
	}

	public TickerSearchController()
	  {
	    
	    super.setFilterSearchCommandClass(TickerListViewModel.class);
	  }

	protected ModelAndView onToggleActivate(HttpServletRequest request,
            HttpServletResponse response,
            Boolean activate) throws

Exception
{
return null;
}
protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
Exception
{
/*
if (log.isDebugEnabled())
{
log.debug("Inside reference data");
}



BankModel bankModel = new BankModel();
ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(
bankModel, "name", SortingOrder.DESC);
referenceDataWrapper.setBasePersistableModel(bankModel);
referenceDataManager.getReferenceData(referenceDataWrapper);
List<BankModel> bankModelList = null;
if (referenceDataWrapper.getReferenceDataList() != null)
{
bankModelList = referenceDataWrapper.
getReferenceDataList();
}

Map referenceDataMap = new HashMap();
referenceDataMap.put("bankModelList",
bankModelList);



return referenceDataMap;
*/

return null;
}
/*
protected ModelAndView onSearch(HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse,
    Object object,
    PagingHelperModel pagingHelperModel,
    LinkedHashMap sortingOrderMap) throws
Exception
{

SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
BankUserListViewModel bankUserListViewModel = (
BankUserListViewModel)
object;
searchBaseWrapper.setBasePersistableModel(bankUserListViewModel);
searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
searchBaseWrapper = this.bankUserManager.searchBankUser(
searchBaseWrapper);

return new ModelAndView(getSuccessView(), "bankUserModelList",
searchBaseWrapper.getCustomList().getResultsetList());



}

*/
public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
{
this.referenceDataManager = referenceDataManager;
}







@Override
protected ModelAndView onSearch(PagingHelperModel pagingHelperModel, Object object, HttpServletRequest httpServletRequest, LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
// TODO Auto-generated method stub

SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
TickerListViewModel tickerListViewModel = (TickerListViewModel)object;
searchBaseWrapper.setBasePersistableModel(tickerListViewModel);
searchBaseWrapper.setPagingHelperModel(pagingHelperModel);
if(sortingOrderMap.isEmpty())
    sortingOrderMap.put("mfsId", SortingOrder.ASC);
searchBaseWrapper.setSortingOrderMap(sortingOrderMap);
searchBaseWrapper = this.tickerManager.searchTickerUser(
searchBaseWrapper);

return new ModelAndView(getSearchView(), "tickerModelList",
  searchBaseWrapper.getCustomList().getResultsetList());


}

	
	
	
	
}
