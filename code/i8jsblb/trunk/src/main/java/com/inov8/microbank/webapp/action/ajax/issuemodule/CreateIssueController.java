package com.inov8.microbank.webapp.action.ajax.issuemodule;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;

import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.service.portal.servicecustomerdispute.CustomerDisputeManager;
import com.inov8.microbank.webapp.action.ajax.AjaxController;

public class CreateIssueController extends AjaxController
{
	
	private CustomerDisputeManager customerDisputeManager;
	
	
	@Override
	public String getResponseContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		
//		try
//		{
			String transactionCode = ServletRequestUtils.getRequiredStringParameter(request, PortalConstants.KEY_TRANS_CODE);
			String issueComment = ServletRequestUtils.getRequiredStringParameter(request, PortalConstants.KEY_ISSUE_COMMENTS);
			String issueMfsId = ServletRequestUtils.getRequiredStringParameter(request, PortalConstants.ISSUE_MFS_ID);
			
			if(transactionCode != null && !"".equals(transactionCode) && issueComment != null && !"".equals(issueComment) &&
					issueMfsId != null && !"".equals(issueMfsId)  
					)
				{
				
				BaseWrapper baseWrapper = new BaseWrapperImpl();
				baseWrapper.putObject("issueMfsId", issueMfsId);
				//If MWallet id is not for customer or retailer throw exception
				baseWrapper = customerDisputeManager.isValidMfsId(baseWrapper);
				String errorCode = (String)baseWrapper.getObject("errorCode"); //

				if(errorCode != null)
				{
					buffer.append(getMessageSourceAccessor().getMessage("issuemodule.mfsId.notExist", request.getLocale()));
					return buffer.toString();
				}
				
				Date nowDate = new Date();
				baseWrapper = new BaseWrapperImpl();	
				IssueModel issueModel = new IssueModel(); 
				issueModel.setComments(issueComment);
				issueModel.setCustTransCode(transactionCode);
				issueModel.setMfsId(issueMfsId);
				issueModel.setCreatedOn(nowDate);
				issueModel.setUpdatedOn(nowDate);
				issueModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
				issueModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
				issueModel.setIssueTypeStatusId(IssueTypeStatusConstantsInterface.DISPUTE_OPEN);
				baseWrapper.setBasePersistableModel(issueModel);
				logger.info("Saving Issue with  Transaction Code : "+transactionCode);
				baseWrapper = this.customerDisputeManager.createIssue(baseWrapper);

				if(baseWrapper.getBasePersistableModel() != null)
				{
					buffer.append(getMessageSourceAccessor().getMessage("issue.success", new String[]{issueModel.getIssueCode()}, request.getLocale()));
				}
				else
				{
					throw new FrameworkCheckedException("Error occured");
				}
			}
			else
			{
				throw new FrameworkCheckedException("Error occured");
			}
//		}
//		catch (ServletRequestBindingException ex)
//		{
//				buffer.append("<p>")
//				.append(getMessageSourceAccessor().getMessage("issue.failure", request.getLocale()))
//				. append("</p>");
//			logger.error("Error parsing request", ex);
//		}
//		catch (FrameworkCheckedException ex)
//		{
//				buffer.append("<p>")
//				.append(getMessageSourceAccessor().getMessage("issue.failure", request.getLocale()))
//				. append("</p>");
//				logger.error("Error In Saving The Issue", ex);
//				throw new FrameworkCheckedException(buffer.toString());
//		}
//			

	
		return buffer.toString();
	}

	public void setCustomerDisputeManager(CustomerDisputeManager customerDisputeManager)
	{
		this.customerDisputeManager = customerDisputeManager;
	}

}
