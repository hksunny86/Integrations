package com.inov8.microbank.webapp.action.portal.ola;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.DateRangeHolderModel;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.SettlementTransactionViewModel;
import com.inov8.microbank.common.model.commissionmodule.CommissionTransactionViewModel;
import com.inov8.microbank.common.model.portal.inovtransactiondetailmodule.TransactionDetailPortalListModel;
import com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryListModel;
import com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryStatusModel;
import com.inov8.microbank.common.model.portal.issuemodule.TransIssueHistoryListViewModel;
import com.inov8.microbank.common.model.portal.ola.BBSettlementAccountsViewModel;
import com.inov8.microbank.common.model.portal.ola.BbStatementAllViewModel;
import com.inov8.microbank.common.model.portal.ola.SettlementBbStatementViewModel;
import com.inov8.microbank.common.model.postedrransactionreportmodule.PostedTransactionViewModel;
import com.inov8.microbank.server.facade.portal.commissionmodule.CommissionTransactionViewFacade;
import com.inov8.microbank.server.facade.portal.financereportsmodule.FinanceReportsFacacde;
import com.inov8.microbank.server.facade.portal.ola.PortalOlaFacade;
import com.inov8.microbank.server.facade.postedtransactionreportmodule.PostedTransactionReportFacade;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;
import com.inov8.microbank.server.service.portal.transactiondetaili8module.TransactionDetailI8Manager;
import com.inov8.ola.util.EncryptionUtil;

public class TransactionHistoryController extends AbstractController
{

	private TransactionDetailI8Manager transactionDetailI8Manager;
	private PortalOlaFacade portalOlaFacade;
	private PostedTransactionReportFacade postedTransactionReportFacade;
	private CommissionTransactionViewFacade commissionTransactionViewFacade;
	private IssueManager issueManager;
	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map<String,Object> detailMap = new HashMap<>();		
		String transactionCode= ServletRequestUtils.getStringParameter(request,"transactionCode");
		
		TransactionDetailPortalListModel transactionDetailPortalListModel= new TransactionDetailPortalListModel();
		transactionDetailPortalListModel.setTransactionCode(transactionCode);
		List<TransactionDetailPortalListModel> transactionDetailPortalListModelList =  transactionDetailI8Manager.searchTransactionDetailForI8(transactionDetailPortalListModel).getResultsetList();
		if(null!=transactionDetailPortalListModelList && transactionDetailPortalListModelList.size()>0){
			transactionDetailPortalListModel = transactionDetailPortalListModelList.get(0);
		}	
		/// Setting Header Model
		detailMap.put("transactionDetailPortalListModel",transactionDetailPortalListModel);
		
		
		
		BbStatementAllViewModel bbStatementAllViewModel = new BbStatementAllViewModel();
		bbStatementAllViewModel.setTransactionCode(transactionCode);
		
		LinkedHashMap<String,SortingOrder> sortingMap = new LinkedHashMap<String,SortingOrder>();
		sortingMap.put("category", SortingOrder.ASC);
		sortingMap.put("ledgerId", SortingOrder.ASC);
		
		SearchBaseWrapper sbWrapper = new SearchBaseWrapperImpl();
		sbWrapper.setSortingOrderMap(sortingMap); 
		sbWrapper.setBasePersistableModel(bbStatementAllViewModel);

		List<BbStatementAllViewModel>  settlementBBStatementList =  portalOlaFacade.searchBbStatementAllView(sbWrapper).getResultsetList();
		//java.util.Collections.sort(settlementBBStatementList);
		if(null!=settlementBBStatementList && settlementBBStatementList.size()>0){
			for (BbStatementAllViewModel bbStatementAllViewModel2 : settlementBBStatementList) {
				bbStatementAllViewModel2.setAccountNumber(EncryptionUtil.decryptAccountNo( bbStatementAllViewModel2.getAccountNumber()));
			}
		}	
		///Setting Settlement Accounts Model List
		detailMap.put("settlementBBStatementList",settlementBBStatementList);
		
		PostedTransactionViewModel postedTransactionViewModel = new PostedTransactionViewModel();
		postedTransactionViewModel.setTransactionCode(transactionCode);
		
		//Setting Posted Trans RDV List
		detailMap.put("postedTransRDVList",this.postedTransactionReportFacade.searchPostedTransactionView(postedTransactionViewModel).getResultsetList());
		
		SettlementTransactionViewModel settlementTransactionViewModel = new SettlementTransactionViewModel(); 
		settlementTransactionViewModel.setTransactionId(transactionCode); 
		
		sbWrapper = new SearchBaseWrapperImpl();
		sortingMap = new LinkedHashMap<String,SortingOrder>();
		sortingMap.put("createdOn", SortingOrder.ASC);
		sortingMap.put("debitAmount", SortingOrder.DESC);
		
		sbWrapper.setSortingOrderMap(sortingMap); 
		sbWrapper.setBasePersistableModel(settlementTransactionViewModel);
		
		detailMap.put("ofTransList",this.transactionDetailI8Manager.searchOFSettlementTransactions(sbWrapper).getResultsetList());
		
		CommissionTransactionViewModel commissionTransactionViewModel = new CommissionTransactionViewModel();
		commissionTransactionViewModel.setTransactionId(transactionCode);
		
		detailMap.put("commissionList",this.commissionTransactionViewFacade.searchCommissionTransactionView(commissionTransactionViewModel));
		
		//////////////////Transaction Issue History//////
		long issueId;
		TransIssueHistoryListViewModel tempTransIssueHistoryListViewModel;
		TransIssueHistoryListViewModel newTransIssueHistoryListViewModel = new TransIssueHistoryListViewModel();
		//String transactionCode = ServletRequestUtils.getStringParameter(request, "transactionCode");
		newTransIssueHistoryListViewModel.setTransactionCode(transactionCode);
		
		List<TransIssueHistoryListViewModel> list = null;
		ArrayList<IssueHistoryListModel> issueHistoryListModelList = new ArrayList<IssueHistoryListModel>();
		if(transactionCode != null && !"".equals(transactionCode))
		{	
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			LinkedHashMap<String,SortingOrder> sortingOrderMap = new LinkedHashMap<String,SortingOrder>();
			sortingOrderMap.put("issueCreatedDate", SortingOrder.ASC);
			sortingOrderMap.put("issueHistoryDate", SortingOrder.ASC);

			searchBaseWrapper.setSortingOrderMap(sortingOrderMap); 
			TransIssueHistoryListViewModel transIssueHistoryListViewModel = new TransIssueHistoryListViewModel();
			transIssueHistoryListViewModel.setTransactionCode(transactionCode);
			searchBaseWrapper.setBasePersistableModel(transIssueHistoryListViewModel);
			searchBaseWrapper = this.issueManager.searchTransactionIssueHistory(searchBaseWrapper);
	
			if(searchBaseWrapper.getCustomList() != null)
			{
				list = searchBaseWrapper.getCustomList().getResultsetList();
				if(list != null && list.size() > 0)
				{
					newTransIssueHistoryListViewModel = list.get(0);
					issueId = newTransIssueHistoryListViewModel.getIssueId();
				
					boolean issueCreateFlag = true;
				
					ArrayList<IssueHistoryStatusModel> issueHistoryStatusModelList = new ArrayList<IssueHistoryStatusModel>();
					IssueHistoryListModel issueHistoryListModel = new IssueHistoryListModel();
					for(int i = 0; i < list.size(); i++)
					{
					
						tempTransIssueHistoryListViewModel = list.get(i);
					
						if(issueId == tempTransIssueHistoryListViewModel.getIssueId())
						{
							if(issueCreateFlag)
							{
								issueCreateFlag = false;
								issueHistoryListModel.setIssueCode(tempTransIssueHistoryListViewModel.getIssueCode());
								issueHistoryListModel.setIssueDate(tempTransIssueHistoryListViewModel.getIssueCreatedDate());
							}
						
							IssueHistoryStatusModel issueHistoryStatusModel = new IssueHistoryStatusModel();
							issueHistoryStatusModel.setComments(tempTransIssueHistoryListViewModel.getComments());
							issueHistoryStatusModel.setIssueHistoryDate(tempTransIssueHistoryListViewModel.getIssueHistoryDate());
							issueHistoryStatusModel.setStatusName(tempTransIssueHistoryListViewModel.getStatusName());
							issueHistoryStatusModel.setIssueTypeStatusId(tempTransIssueHistoryListViewModel.getIssueTypeStatusId());
							issueHistoryStatusModelList.add(issueHistoryStatusModel);
						
							if(list.size() > i+1)
							{
								if(list.get(i+1).getIssueId() != issueId)
								{
									issueHistoryListModel.setIssueHistoryStatusModellist(issueHistoryStatusModelList);
									issueHistoryListModelList.add(issueHistoryListModel);
									issueHistoryListModel = new IssueHistoryListModel();
									issueHistoryStatusModelList = new ArrayList<IssueHistoryStatusModel>();
									issueId = list.get(i+1).getIssueId();
									issueCreateFlag = true;
								}
							}
							else if(list.size() > i)
							{
								issueHistoryListModel.setIssueHistoryStatusModellist(issueHistoryStatusModelList);
								issueHistoryListModelList.add(issueHistoryListModel);
							}
						}
					}
				}
			}
		}
		detailMap.put("transIssueHistoryListViewModel", newTransIssueHistoryListViewModel);
		detailMap.put("issueHistoryListModelList",issueHistoryListModelList);	
		return new ModelAndView("p_transactionissuehistorymanagement",detailMap);
	}


	public void setTransactionDetailI8Manager(
			TransactionDetailI8Manager transactionDetailI8Manager) {
		this.transactionDetailI8Manager = transactionDetailI8Manager;
	}


	public void setPortalOlaFacade(PortalOlaFacade portalOlaFacade) {
		this.portalOlaFacade = portalOlaFacade;
	}


	public void setPostedTransactionReportFacade(
			PostedTransactionReportFacade postedTransactionReportFacade) {
		this.postedTransactionReportFacade = postedTransactionReportFacade;
	}

	public void setCommissionTransactionViewFacade(
			CommissionTransactionViewFacade commissionTransactionViewFacade) {
		this.commissionTransactionViewFacade = commissionTransactionViewFacade;
	}


	public void setIssueManager(IssueManager issueManager) {
		this.issueManager = issueManager;
	}
}

