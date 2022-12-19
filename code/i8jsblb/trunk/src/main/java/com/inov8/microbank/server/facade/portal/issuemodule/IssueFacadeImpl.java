package com.inov8.microbank.server.facade.portal.issuemodule;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.exception.FrameworkExceptionTranslator;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;

public class IssueFacadeImpl implements IssueFacade {
	private IssueManager issueManager;

	private FrameworkExceptionTranslator frameworkExceptionTranslator;

	public SearchBaseWrapper searchIssueHistory(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return issueManager.searchIssueHistory(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper updateOrphanIssue(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return issueManager.updateOrphanIssue(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	public SearchBaseWrapper searchTransactionIssueHistory(
			SearchBaseWrapper searchBaseWrapper)
			throws FrameworkCheckedException {
		try {
			return issueManager
					.searchTransactionIssueHistory(searchBaseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inov8.microbank.server.service.portal.issuemodule.IssueManager#createOrUpdateIssue(com.inov8.framework.common.wrapper.BaseWrapper)
	 */
	public BaseWrapper createOrUpdateIssue(BaseWrapper baseWrapper,
			boolean isAppUserProvided) throws FrameworkCheckedException {
		try {
			return issueManager.createOrUpdateIssue(baseWrapper, false);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.UPDATE_ACTION);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inov8.microbank.server.service.portal.issuemodule.IssueManager#loadIssue(com.inov8.framework.common.wrapper.SearchBaseWrapper)
	 */
	public BaseWrapper loadIssue(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return issueManager.loadIssue(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public BaseWrapper loadTransactionForIssue(BaseWrapper baseWrapper)
			throws FrameworkCheckedException {
		try {
			return issueManager.loadTransactionForIssue(baseWrapper);
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public String getUniqueIssueCode() throws FrameworkCheckedException {
		try {
			return issueManager.getUniqueIssueCode();
		} catch (Exception ex) {
			throw this.frameworkExceptionTranslator.translate(ex,
					FrameworkExceptionTranslator.FIND_ACTION);
		}
	}

	public void setIssueManager(IssueManager issueManager) {
		this.issueManager = issueManager;
	}

	public void setFrameworkExceptionTranslator(
			FrameworkExceptionTranslator frameworkExceptionTranslator) {
		this.frameworkExceptionTranslator = frameworkExceptionTranslator;
	}

}
