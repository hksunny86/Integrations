package com.inov8.microbank.webapp.action.portal.issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryListModel;
import com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryStatusModel;
import com.inov8.microbank.common.model.portal.issuemodule.TransIssueHistoryListViewModel;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;

public class TransactionIssueHistorySearchController extends AbstractController
{

	private IssueManager issueManager;
	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map<String,Object> issueMap = new HashMap<>();
		long issueId;
		TransIssueHistoryListViewModel tempTransIssueHistoryListViewModel;
		TransIssueHistoryListViewModel newTransIssueHistoryListViewModel = new TransIssueHistoryListViewModel();
		String transactionCode = ServletRequestUtils.getStringParameter(request, "transactionCode");
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
		issueMap.put("transIssueHistoryListViewModel", newTransIssueHistoryListViewModel);
		issueMap.put("issueHistoryListModelList",issueHistoryListModelList);
		return new ModelAndView("p_transactionissuehistorymanagement",issueMap);
	}

	public void setIssueManager(IssueManager issueManager)
	{
		this.issueManager = issueManager;
	}

}
