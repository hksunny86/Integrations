package com.inov8.microbank.webapp.action.portal.issue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapperImpl;
import com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryListViewModel;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;


public class IssueHistorySearchController extends AbstractController
{
	
	private IssueManager issueManager;
	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
	{	
		Map issueMap = new HashMap();
		
		IssueHistoryListViewModel newIssueHistoryListViewModel = new IssueHistoryListViewModel();
		
		String issueCode = ServletRequestUtils.getStringParameter(request, "issueCode");
		List<IssueHistoryListViewModel> list = null;
		if(issueCode != null && !"".equals(issueCode))
		{	
			SearchBaseWrapper searchBaseWrapper = new SearchBaseWrapperImpl();
			IssueHistoryListViewModel issueHistoryListViewModel = new IssueHistoryListViewModel();
			issueHistoryListViewModel.setIssueCode(issueCode);
			searchBaseWrapper.setBasePersistableModel(issueHistoryListViewModel);
			searchBaseWrapper = this.issueManager.searchIssueHistory(searchBaseWrapper);
			if(searchBaseWrapper.getCustomList() != null)
			{
				list = searchBaseWrapper.getCustomList().getResultsetList();
				newIssueHistoryListViewModel = list.get(0);
			}
		}
		issueMap.put("issueHistoryListViewModel", newIssueHistoryListViewModel);
		issueMap.put("issueHistoryList",list);
		return new ModelAndView("p_issuehistorymanagement",issueMap);
	}


	public void setIssueManager(IssueManager issueManager)
	{
		this.issueManager = issueManager;
	}

}
