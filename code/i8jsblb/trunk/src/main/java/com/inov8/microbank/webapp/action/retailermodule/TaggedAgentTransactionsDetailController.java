package com.inov8.microbank.webapp.action.retailermodule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.PagingHelperModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.framework.webapp.action.BaseSearchController;
import com.inov8.microbank.common.model.AgentTaggingChildrenModel;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.TransactionDetailMasterModel;
import com.inov8.microbank.common.model.veriflymodule.TaggedAgentTransactionsVOModel;
import com.inov8.microbank.common.util.PortalDateUtils;
import com.inov8.microbank.server.service.agentgroup.AgentTaggingManager;
import com.inov8.microbank.server.service.securitymodule.AppUserManager;

public class TaggedAgentTransactionsDetailController extends BaseSearchController{

	//AppUserManager appUserManager;
	AgentTaggingManager agentTaggingManager;
	AppUserManager appUserManager;
	
	public void setAppUserManager(AppUserManager appUserManager) {
		this.appUserManager = appUserManager;
	}

	public TaggedAgentTransactionsDetailController() {
	    super.setFilterSearchCommandClass(TransactionDetailMasterModel.class);
	}

	public void setAgentTaggingManager(AgentTaggingManager agentTaggingManager) {
		this.agentTaggingManager = agentTaggingManager;
	}

@Override
protected ModelAndView onSearch(PagingHelperModel pagingHelperModel,
		Object model, HttpServletRequest request,
		LinkedHashMap<String, SortingOrder> sortingOrderMap) throws Exception {
	Map<String,Object> detailMap = new HashMap<>();
	List<TransactionDetailMasterModel> tDetailMasterList = new ArrayList<TransactionDetailMasterModel>();
	TransactionDetailMasterModel transactionDetailMasterModel = new TransactionDetailMasterModel();
	String tId= ServletRequestUtils.getStringParameter(request, "tid");
	String pId= ServletRequestUtils.getStringParameter(request, "pid");
	String appID= ServletRequestUtils.getStringParameter(request, "appUserId");
	String CDATE= ServletRequestUtils.getStringParameter(request, "cDate");
	String EDATE= ServletRequestUtils.getStringParameter(request, "eDate");
	
	request.setAttribute("TID", tId);
	request.setAttribute("appID", appID);
	request.setAttribute("cDate", CDATE);
	request.setAttribute("eDate", EDATE);
	
    
	if(pId!=null && (tId!=null || appID!=null)){
		
		
		AppUserModel appuserModel = new AppUserModel();
		appuserModel.setAppUserId(Long.parseLong(appID));
		BaseWrapper sbWrapper = new BaseWrapperImpl();
		sbWrapper.setBasePersistableModel(appuserModel);
		sbWrapper =  appUserManager.loadAppUser(sbWrapper);
		appuserModel = (AppUserModel) sbWrapper.getBasePersistableModel();
		Long userId =null;
		if(appuserModel!=null)
		{
			userId = appuserModel.getAppUserTypeId();
		}
		transactionDetailMasterModel.setProductId(Long.parseLong( pId));
		if(userId == 12){
			transactionDetailMasterModel.setHandlerMfsId(tId);
		}
		else{
		transactionDetailMasterModel.setAgent1Id(tId);
		}
		
		
		 SearchBaseWrapper sBaseWrapper = new SearchBaseWrapperImpl();
		 String eDate=null;
		  if(EDATE!=null || EDATE!="")
		  {
		  eDate = EDATE.substring(0, EDATE.length()-1).trim();
		  }
		  Date SDate=null ;
		  Date EDate=null ;
		  if(CDATE != ""){
			  SDate = PortalDateUtils.parseStringAsDate(CDATE, PortalDateUtils.SHORT_DATE_TIME_FORMAT);
			  SDate.setHours(0);
		  }
		  if(eDate.length() > 0){
			  EDate = PortalDateUtils.parseStringAsDate(EDATE, PortalDateUtils.SHORT_DATE_TIME_FORMAT);
			  EDate.setHours(23);
			  EDate.setMinutes(59);
			  EDate.setSeconds(59);
		  }
		  
		  DateRangeHolderModel dateRangeHolderModel = new DateRangeHolderModel("createdOn",SDate,EDate);

		  sBaseWrapper.setDateRangeHolderModel( dateRangeHolderModel );
	   	sBaseWrapper.setPagingHelperModel(pagingHelperModel);
		sBaseWrapper.setBasePersistableModel(transactionDetailMasterModel);
		if(sortingOrderMap.isEmpty()){
			sortingOrderMap.put( "createdOn", SortingOrder.DESC );
		}		
		sBaseWrapper.setSortingOrderMap( sortingOrderMap );
		tDetailMasterList = agentTaggingManager.getTaggedAgentTransactionDetailList(sBaseWrapper);				
	}
		
	if(tDetailMasterList.size()>0){
		pagingHelperModel.setTotalRecordsCount(tDetailMasterList.size());
        	detailMap.put("tDetailMasterList",tDetailMasterList);
        }
	else
	{
		pagingHelperModel.setTotalRecordsCount(0);
		detailMap.put("tDetailMasterList",tDetailMasterList);
	}
	return new ModelAndView("taggedAgentsTransactionsList",detailMap);
}

}
