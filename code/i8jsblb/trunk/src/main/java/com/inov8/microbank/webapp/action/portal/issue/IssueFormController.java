package com.inov8.microbank.webapp.action.portal.issue;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.util.EncryptionUtil;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.server.service.portal.issuemodule.IssueManager;


public class IssueFormController extends AdvanceFormController
{
	
  private IssueManager issueManager;
  
  public IssueFormController()
  {
    setCommandName("issueModel");
    setCommandClass(IssueModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest httpServletRequest) throws
      Exception
  {
    return null;
  }

  protected Object loadFormBackingObject(HttpServletRequest httpServletRequest) throws
      Exception
  {
	String eIssueId = ServletRequestUtils.getStringParameter(httpServletRequest, "issueId");   
    Long id = null;

    BaseWrapper baseWrapper = new BaseWrapperImpl();

     if (null != eIssueId && eIssueId.trim().length() > 0 )
     {
       id = new Long(EncryptionUtil.decryptWithDES(eIssueId));  
       IssueModel issueModel = new IssueModel();
       issueModel.setIssueId(id);
       baseWrapper.setBasePersistableModel(issueModel);
       baseWrapper = this.issueManager.loadIssue(baseWrapper);
       issueModel = (IssueModel)baseWrapper.getBasePersistableModel(); 
  	   issueModel.setComments("");
       httpServletRequest.setAttribute("transactionCode", ServletRequestUtils.getStringParameter(httpServletRequest, "transactionCode"));
       return issueModel; 
     }
     else
     {
       IssueModel issueModel = new IssueModel();
       issueModel.setTransactionCodeId(new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest,"transactionCodeId"))));
       issueModel.setTransactionId(new Long(EncryptionUtil.decryptWithDES(ServletRequestUtils.getStringParameter(httpServletRequest,"transactionId"))));

       TransactionModel transactionModel = new TransactionModel();
       //Load the transaction from the database against this issue
       transactionModel.setTransactionId(issueModel.getTransactionId());
       baseWrapper.setBasePersistableModel(transactionModel);
       baseWrapper = this.issueManager.loadTransactionForIssue(baseWrapper);
       transactionModel = (TransactionModel)baseWrapper.getBasePersistableModel();
       
       if(transactionModel.getIssue()!=null && transactionModel.getIssue().booleanValue())
       {
    	   httpServletRequest.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
  	       httpServletRequest.setAttribute("message", "An issue is already logged against this transaction");
       } 
       httpServletRequest.setAttribute("transactionCode", ServletRequestUtils.getStringParameter(httpServletRequest, "transactionCode"));
       return issueModel;
     }


  }

  protected ModelAndView onCreate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
                               bindException);
  }

  protected ModelAndView onUpdate(HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse,
                                  Object object, BindException bindException) throws
      Exception
  {
    return this.createOrUpdate(httpServletRequest, httpServletResponse, object,
                               bindException);
  }

  /**
  *
  * @param request HttpServletRequest
  * @param response HttpServletResponse
  * @param command Object
  * @param errors BindException
  * @return ModelAndView
  * @throws Exception
  */
 private ModelAndView createOrUpdate(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object command,
                                     BindException errors) throws Exception
 {
     BaseWrapper baseWrapper = new BaseWrapperImpl();
   try
   {
	   String enIssueTypeStId = ServletRequestUtils.getStringParameter(request,IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME); 

     if(null!=enIssueTypeStId && enIssueTypeStId.trim().length() > 0 )
     { 
	     IssueModel issueModel = (IssueModel) command;
	     issueModel.setCustTransCode(ServletRequestUtils.getStringParameter(request, "transactionCode"));
	     issueModel.setIssueTypeStatusId(new Long(EncryptionUtil.decryptWithDES(enIssueTypeStId)));
//	     if(true) throw new RuntimeException("sad");
	     //baseWrapper.putObject("issueTypeStId", issueTypeStId);
	     baseWrapper.setBasePersistableModel(issueModel);
	     baseWrapper = issueManager.createOrUpdateIssue(baseWrapper, false);
	     TransactionModel transactionModel = (TransactionModel) baseWrapper.getObject("transactionModel");
	     issueModel = (IssueModel)baseWrapper.getBasePersistableModel();
	     
	     Map<String, String> map = new HashMap<String, String>();
	     map.put("status", IssueTypeStatusConstantsInterface.SUCCESS);
	     ModelAndView modelAndView = new ModelAndView(this.getSuccessView()
	    		 +"?isIssue="+transactionModel.getIssue()
	    		 +"&"+ PortalConstants.KEY_ACTION_ID+"="+PortalConstants.ACTION_DEFAULT
	    		 +"&transactionId="+EncryptionUtil.encryptWithDES(transactionModel.getTransactionId()+"")
	    		 +"&"+IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME+"="+ServletRequestUtils.getStringParameter(request,IssueTypeStatusConstantsInterface.MGMT_PAGE_BTN_NAME)
	    		 +"&issueCode="+issueModel.getIssueCode()
	    		 +"&issueId="+EncryptionUtil.encryptWithDES(issueModel.getIssueId()+"")
	    		 +"&status="+IssueTypeStatusConstantsInterface.SUCCESS
	    		 +"&"+IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME+"="+ServletRequestUtils.getLongParameter(request,IssueTypeStatusConstantsInterface.REQUEST_PARAMETER_NAME), map );
	     return modelAndView;
     }
     else
     {
  	   if(log.isDebugEnabled())
		   log.debug("Issue could not be created. Please contact with the adminsitrator for more details");

	     request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
         request.setAttribute("transactionCode", ServletRequestUtils.getStringParameter(request, "transactionCode"));
    	 request.setAttribute("message", "Issue could not be logged. Please contact with the adminsitrator for more details");
         return super.showForm(request, response, errors);
     }
   }
   catch (Exception ex)
   {
	     request.setAttribute("status",IssueTypeStatusConstantsInterface.FAILURE);
	   
	   log.error("Error =>", ex);
	   
	   String msg = (String)baseWrapper.getObject(IssueManager.KEY_ERROR_MSG);
	   if(log.isDebugEnabled())
		   log.debug(msg);
	   
	   if(msg!=null)
	   {
	    	 request.setAttribute("message", msg);
	   }
	   else
	   {
	    	 request.setAttribute("message", "Issue could not be logged. Please contact with the adminsitrator for more details");
	   }
       request.setAttribute("transactionCode", ServletRequestUtils.getStringParameter(request, "transactionCode"));
       return super.showForm(request, response, errors);
   }
 }

/**
 * @param issueManager the issueManager to set
 */
public void setIssueManager(IssueManager issueManager) {
	this.issueManager = issueManager;
}

  

}
