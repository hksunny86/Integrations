package com.inov8.microbank.server.service.portal.issuemodule;

import java.util.Date;

import org.hibernate.criterion.MatchMode;

import com.inov8.common.util.RandomUtils;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.ExampleConfigHolderModel;
import com.inov8.framework.common.util.CustomList;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.common.wrapper.SearchBaseWrapper;
import com.inov8.microbank.common.model.ActionLogModel;
import com.inov8.microbank.common.model.IssueModel;
import com.inov8.microbank.common.model.IssueTypeStatusModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.portal.issuemodule.IssueHistoryListViewModel;
import com.inov8.microbank.common.model.portal.issuemodule.TransIssueHistoryListViewModel;
import com.inov8.microbank.common.util.IssueTypeStatusConstantsInterface;
import com.inov8.microbank.common.util.PortalConstants;
import com.inov8.microbank.common.util.ThreadLocalActionLog;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.server.dao.portal.issuemodule.IssueDAO;
import com.inov8.microbank.server.dao.portal.issuemodule.IssueHistoryListViewDAO;
import com.inov8.microbank.server.dao.portal.issuemodule.IssueTypeStatusDAO;
import com.inov8.microbank.server.dao.portal.issuemodule.TransactionIssueHistoryListViewDAO;
import com.inov8.microbank.server.dao.transactionmodule.TransactionDAO;
import com.inov8.microbank.server.service.actionlogmodule.ActionLogManager;

public class IssueManagerImpl implements IssueManager
{

	private IssueHistoryListViewDAO issueHistoryListViewDAO;
	private IssueDAO issueDAO;
	private TransactionDAO transactionDAO;
	private IssueTypeStatusDAO issueTypeStatusDAO;
	private ActionLogManager actionLogManager;
	
	private TransactionIssueHistoryListViewDAO transactionIssueHistoryListViewDAO;
	
	public SearchBaseWrapper searchIssueHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		CustomList<IssueHistoryListViewModel>
		list = this.issueHistoryListViewDAO.findByExample( (IssueHistoryListViewModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel());
		if(list != null)
		{
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}
	
	
	public SearchBaseWrapper searchTransactionIssueHistory(SearchBaseWrapper searchBaseWrapper) throws FrameworkCheckedException
	{
		CustomList<TransIssueHistoryListViewModel>
		list = this.transactionIssueHistoryListViewDAO.findByExample( (TransIssueHistoryListViewModel)
				searchBaseWrapper.
				getBasePersistableModel(),
				searchBaseWrapper.
				getPagingHelperModel(),
				searchBaseWrapper.
				getSortingOrderMap(),searchBaseWrapper.getDateRangeHolderModel(),
				PortalConstants.EXACT_CONFIG_HOLDER_MODEL);
		if(list != null)
		{
			searchBaseWrapper.setCustomList(list);
		}
		return searchBaseWrapper;
	}

	

  public BaseWrapper createOrUpdateIssue(BaseWrapper baseWrapper, boolean isAppUserProvided) throws
      FrameworkCheckedException
  {
    IssueModel tempIssueModel = (IssueModel) baseWrapper.getBasePersistableModel();
    IssueModel issueModel = null;
    ActionLogModel actionLogModel = null;
    
    TransactionModel transactionModel = new TransactionModel();
    //Load the transaction from the database against this issue
    transactionModel.setTransactionId(tempIssueModel.getTransactionId());
    //Load the transaction from the database against this issue
    transactionModel = this.transactionDAO.findByPrimaryKey(transactionModel.getTransactionId());
    
    if(transactionModel==null)
    {
    	//baseWrapper.putObject(KEY_ERROR_MSG, "No such transaction found");
    	throw new FrameworkCheckedException("No such transaction found");
    }
    
    Long issueTypeStId = tempIssueModel.getIssueTypeStatusId();

    
    Date theDate = new Date();
    
    if (null != tempIssueModel.getIssueId())
    {
		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.CHARGE_BACK_USECASE_ID);
		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_UPDATE);

    	actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());
    	
     	BaseWrapper bW = new BaseWrapperImpl();
		issueModel = new IssueModel();
		issueModel.setIssueId(tempIssueModel.getIssueId());
		bW.setBasePersistableModel(issueModel);
		issueModel = (IssueModel) this.loadIssue(bW).getBasePersistableModel();
		issueModel.setComments(tempIssueModel.getComments());
		issueModel.setUpdatedOn(theDate);
		issueModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		issueModel.setIssueTypeStatusId(issueTypeStId);
		IssueTypeStatusModel issueTypeStatusModel = issueTypeStatusDAO.findByPrimaryKey(issueModel.getIssueTypeStatusIdIssueTypeStatusModel().getIssueTypeStatusId());
		
		if(issueTypeStatusModel.getIssueStatusIdIssueStatusModel().getClosing().booleanValue())
		{
			transactionModel.setIssue(false);
		    transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		    transactionModel.setUpdatedOn(theDate);
		  	transactionDAO.saveOrUpdate(transactionModel);
		}
    }
    else
    {
        if(IssueTypeStatusConstantsInterface.CHARGEBACK_NEW.equals(issueTypeStId) 
        		|| IssueTypeStatusConstantsInterface.DISPUTE_NEW.equals(issueTypeStId))
        {
    		baseWrapper.putObject(PortalConstants.KEY_USECASE_ID, PortalConstants.CHARGE_BACK_USECASE_ID);
    		baseWrapper.putObject(PortalConstants.KEY_ACTION_ID, PortalConstants.ACTION_CREATE);
        	actionLogModel = this.actionLogManager.createActionLogRequiresNewTransaction(baseWrapper);
    		ThreadLocalActionLog.setActionLogId(actionLogModel.getActionLogId());

            if(transactionModel.getIssue()!=null && transactionModel.getIssue().booleanValue())
            {
            	baseWrapper.putObject(KEY_ERROR_MSG, "An issue is already logged against this transaction ["+ transactionModel.getTransactionCodeIdTransactionCodeModel().getCode() +"]");
            	throw new FrameworkCheckedException("An issue is already logged against this transaction ["+ transactionModel.getTransactionCodeIdTransactionCodeModel().getCode() +"]");
            }
       	  	transactionModel.setIssue(true);
	    	issueModel = tempIssueModel;
		    issueModel.setCreatedOn(theDate);
		    issueModel.setUpdatedOn(theDate);
			if(!isAppUserProvided)
			{
			    issueModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			    issueModel.setCreatedByAppUserModel(UserUtils.getCurrentUser());
			}
		  	issueModel.setIssueCode(getUniqueIssueCode());
		  	issueModel.setIssueTypeStatusId(issueTypeStId);
		  	issueModel.setTransactionCodeId(transactionModel.getTransactionCodeId());
		    transactionModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
		    transactionModel.setUpdatedOn(theDate);
		  	transactionDAO.saveOrUpdate(transactionModel);
		  	
        }
    }
	    
    
        // Now save the issue
     	issueModel = issueDAO.saveOrUpdate(issueModel);
      
     	if(actionLogModel != null && issueModel.getPrimaryKey() != null){
     		actionLogModel.setCustomField1(""+issueModel.getPrimaryKey());
			actionLogModel.setCustomField11(issueModel.getCustTransCode());
			this.actionLogManager.completeActionLog(actionLogModel);
		}
		     	
      baseWrapper.putObject("transactionModel", transactionModel);  
      baseWrapper.setBasePersistableModel(issueModel);
      return baseWrapper;
  }
	  
  
	public BaseWrapper updateOrphanIssue(BaseWrapper baseWrapper) throws FrameworkCheckedException
	{
	    IssueModel tempIssueModel = (IssueModel) baseWrapper.getBasePersistableModel();
	    IssueModel issueModel = null;
	    
	    if (null != tempIssueModel.getIssueId())
	    {
		    Long issueTypeStId = tempIssueModel.getIssueTypeStatusId();
		    Date theDate = new Date();
		    
	     	BaseWrapper bW = new BaseWrapperImpl();
			issueModel = new IssueModel();
			issueModel.setIssueId(tempIssueModel.getIssueId());
			bW.setBasePersistableModel(issueModel);
			issueModel = (IssueModel) this.loadIssue(bW).getBasePersistableModel();
			issueModel.setComments(tempIssueModel.getComments());
			issueModel.setUpdatedOn(theDate);
			issueModel.setUpdatedByAppUserModel(UserUtils.getCurrentUser());
			issueModel.setIssueTypeStatusId(issueTypeStId);
		     // Now save the issue
		     issueModel = issueDAO.saveOrUpdate(issueModel);
		     baseWrapper.setBasePersistableModel(issueModel);
	    }
	    else
	    	 throw new FrameworkCheckedException("Issue can not be updated");
	    
	     return baseWrapper;
	}


public BaseWrapper loadIssue(BaseWrapper baseWrapper) throws
      FrameworkCheckedException
  {
	  	IssueModel issueModel = this.issueDAO.findByPrimaryKey(baseWrapper.getBasePersistableModel().getPrimaryKey()); 
	    baseWrapper.setBasePersistableModel(issueModel);	
	    return baseWrapper;
  }
	  
	  public BaseWrapper loadTransactionForIssue(BaseWrapper baseWrapper)
	  {
	    TransactionModel transactionModel = (TransactionModel)this.transactionDAO.findByPrimaryKey( (
	    baseWrapper.getBasePersistableModel()).getPrimaryKey());
	    baseWrapper.setBasePersistableModel(transactionModel);
	    return baseWrapper;
	  }

	private String generateIssueCode()
	{
	      String orgChars = "IX" ;
	      String alphaChars = RandomUtils.generateRandom(8, true, true).toUpperCase() ;

	       return orgChars + alphaChars ;
	}
	
	public String getUniqueIssueCode()
	{
	       IssueModel issueModel = new IssueModel();
    	   ExampleConfigHolderModel exampleConfigHolderModel = new ExampleConfigHolderModel();
    	   exampleConfigHolderModel.setMatchMode(MatchMode.EXACT);

	       boolean isNotUnique = true;
	       while (isNotUnique)
	       {
		       issueModel.setIssueCode(generateIssueCode());
		       try
		       {
			       Integer count = this.issueDAO.countByExample(issueModel, exampleConfigHolderModel);
			       if(count==0)
			    	   isNotUnique = false;
		       }
		       catch(Exception ex)
		       {
		    	   ex.printStackTrace();
		       }
	       }
	       return issueModel.getIssueCode();
	}
	
	
	public void setIssueHistoryListViewDAO(IssueHistoryListViewDAO issueHistoryListViewDAO)
	{
		this.issueHistoryListViewDAO = issueHistoryListViewDAO;
	}

	/**
	 * @param issueDAO the issueDAO to set
	 */
	public void setIssueDAO(IssueDAO issueDAO) {
		this.issueDAO = issueDAO;
	}
	
	
	public void setTransactionIssueHistoryListViewDAO(TransactionIssueHistoryListViewDAO transactionIssueHistoryListViewDAO)
	{
		this.transactionIssueHistoryListViewDAO = transactionIssueHistoryListViewDAO;
	}
	

	public void setTransactionDAO(TransactionDAO transactionDAO)
	{
		this.transactionDAO = transactionDAO;
	}


	/**
	 * @param issueTypeStatusDAO the issueTypeStatusDAO to set
	 */
	public void setIssueTypeStatusDAO(IssueTypeStatusDAO issueTypeStatusDAO) {
		this.issueTypeStatusDAO = issueTypeStatusDAO;
	}

	public void setActionLogManager(ActionLogManager actionLogManager) {
		this.actionLogManager = actionLogManager;
	}

}
