package com.inov8.microbank.server.service.portal.issuemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;

public interface IssueManager
{
	
	public final static String KEY_ERROR_MSG = "errMsg"; 
	SearchBaseWrapper searchIssueHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException;

	SearchBaseWrapper searchTransactionIssueHistory(SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException;

	public BaseWrapper createOrUpdateIssue(BaseWrapper baseWrapper, boolean isAppUserProvided) throws FrameworkCheckedException;

	public BaseWrapper updateOrphanIssue(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public BaseWrapper loadIssue(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public BaseWrapper loadTransactionForIssue(BaseWrapper baseWrapper) throws FrameworkCheckedException;

	public String getUniqueIssueCode() throws FrameworkCheckedException;
}
