package com.inov8.microbank.webapp.action.portal.escalateinov8;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class EscalateInov8AjaxController extends AjaxController
{

	private IssueManager issueManager;

	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		Long issueTypeStatusId = ServletRequestUtils.getLongParameter(request, "status");
		
		if(IssueTypeStatusConstantsInterface.CHARGEBACK_OPEN.equals(issueTypeStatusId)
				|| IssueTypeStatusConstantsInterface.DISPUTE_OPEN.equals(issueTypeStatusId))
			throw new FrameworkCheckedException("The issue can not be reopened");
		
		Long issueId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "issueId")));
		
		String entLd = ServletRequestUtils.getStringParameter(request, "tId");		
		Long tId = null;
		if(entLd != null && entLd.equals("-999")){
			tId = -999l;
		}else{
			tId = new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(request, "tId")));
		}
			
		BaseWrapper baseWrapper = new BaseWrapperImpl();
		
		IssueModel issueModel = new IssueModel();
		issueModel.setIssueId(issueId);
		issueModel.setComments("");
		issueModel.setIssueTypeStatusId(issueTypeStatusId);
		issueModel.setTransactionId(tId);
		baseWrapper.setBasePersistableModel(issueModel);
		if(tId.equals(-999L))
			baseWrapper = this.issueManager.updateOrphanIssue(baseWrapper);
		else
			baseWrapper = this.issueManager.createOrUpdateIssue(baseWrapper, false);

		if (baseWrapper.getBasePersistableModel() != null)
		{
			String msg = "has been updated successfully.";
			if(IssueTypeStatusConstantsInterface.INOV8_CHARGEBACK_RIFM.equals(issueTypeStatusId))
			{
				msg = "resolved in favor of Merchant";
			}
			else if(IssueTypeStatusConstantsInterface.INOV8_CHARGEBACK_RIFC.equals(issueTypeStatusId)
					||	IssueTypeStatusConstantsInterface.INOV8_DISPUTE_RIFC.equals(issueTypeStatusId)
					)
			{
				msg = "resolved in favor of Customer";
			}
			else if(IssueTypeStatusConstantsInterface.INOV8_DISPUTE_INVALID.equals(issueTypeStatusId))
			{
				msg = "resolved as invalid";
			}
			
			buffer.append(getMessageSourceAccessor().getMessage("issue.inov8.success", new String[] { msg },
					request.getLocale()));
		}
		return buffer.toString();
	}

	/**
	 * @param issueManager the issueManager to set
	 */
	public void setIssueManager(IssueManager issueManager) {
		this.issueManager = issueManager;
	}


	
}
