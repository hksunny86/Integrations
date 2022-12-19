package com.inov8.microbank.webapp.action.creditmodule;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.exception.FrameworkCheckedException;
import com.inov8.framework.common.model.SortingOrder;
import com.inov8.framework.common.wrapper.ReferenceDataWrapper;
import com.inov8.framework.common.wrapper.ReferenceDataWrapperImpl;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.DistributorModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.util.WorkFlowErrorCodeConstants;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;

/**
 *
 * <p>Title: Microbank</p>
 *
 * <p>Description: Backened application for POS terminal</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Inov8 Ltd</p>
 *
 * @author Ahmad Iqbal
 * @version 1.0
 * @todo :Hard coded the PK of OperatorModel as this id represents 'Inov8'
 */

public class OperatorDistributorCreditFormController
    extends AdvanceFormController
{
  //private Inov8DistributorCreditManager inov8DistributorCreditManager;
  //private ReferenceDataManager referenceDataManager;
  //private TransactionModuleManager transactionManager;
  //private DistDistCreditManager distDistCreditManager;
  private WorkFlowController workFlowController;
  private ReferenceDataManager referenceDataManager;
  public OperatorDistributorCreditFormController()
  {
    setCommandName("transactionModel");
    setCommandClass(TransactionModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest request) throws Exception
  {
	  DistributorModel distributorModel = new DistributorModel();
		distributorModel.setActive(true);
		ReferenceDataWrapper referenceDataWrapper = new ReferenceDataWrapperImpl(distributorModel, "name", SortingOrder.ASC);
		referenceDataWrapper.setBasePersistableModel(distributorModel);

		referenceDataManager.getReferenceData(referenceDataWrapper);
		List<DistributorModel> distributorModelList = null;
		if (referenceDataWrapper.getReferenceDataList() != null)
		{
			distributorModelList = referenceDataWrapper.getReferenceDataList();
		}
		Map referenceDataMap = new HashMap();
		referenceDataMap.put("distributorModelList", distributorModelList);
		
		
			

	return referenceDataMap;

  }

  protected Object loadFormBackingObject(HttpServletRequest request) throws
      Exception
  {
    return new TransactionModel();
  }

  protected ModelAndView onCreate(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  BindException errors) throws Exception
  {
	  TransactionModel transactionModel = (TransactionModel) command;
	    WorkFlowWrapper workFlowWrapper=new WorkFlowWrapperImpl();
	    DecimalFormat myFormatter = new DecimalFormat("###.##");

	    try
	    {
	    	workFlowWrapper.setTransactionTypeModel( new TransactionTypeModel() ) ;
	    	workFlowWrapper.setDeviceTypeModel( new DeviceTypeModel() );
	    	workFlowWrapper.setAppUserModel( new AppUserModel() );
	    	workFlowWrapper.getTransactionTypeModel().setTransactionTypeId((long)TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX ) ;
	    	workFlowWrapper.getDeviceTypeModel().setDeviceTypeId( DeviceTypeConstantsInterface.WEB );
	    	//workFlowWrapper.setOperatorModel( new OperatorModel() ) ;
	    	//workFlowWrapper.getOperatorModel().setOperatorId( UserUtils.getCurrentUser().getAppUserId() ) ;
	    	workFlowWrapper.setAppUserModel( UserUtils.getCurrentUser() ) ;

	    	//workFlowWrapper.setToDistributorContactModel(new DistributorContactModel()) ;
	    	//workFlowWrapper.getToDistributorContactModel().setDistributorId( transactionModel.getToDistContactId() ) ;
	    	DistributorModel distributorModel=new DistributorModel();
	    	distributorModel.setDistributorId(transactionModel.getDistributorId());
	    	workFlowWrapper.setDistributorModel(distributorModel);	
	    	workFlowWrapper.setTransactionAmount( transactionModel.getTotalAmount()) ;

	    	if(!(UserTypeConstantsInterface.INOV8.equals(UserUtils.getCurrentUser().getAppUserTypeId())))
	        {
	    		Double balance =transactionModel.getTotalAmount();
				
				String bal =myFormatter.format(balance);
			    request.setAttribute("transactionAmount", bal);
	          this.saveMessage(request, "Logged-In user is not an Operator.");
	          return super.showForm(request, response, errors);
	        }

		    workFlowWrapper.setTransactionModel(transactionModel);

		    workFlowWrapper=this.workFlowController.workflowProcess(workFlowWrapper);

		    transactionModel=workFlowWrapper.getTransactionModel();

		    this.saveMessage(request, "The transaction completed successfully. " + "Transaction code: " + transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());
		    Double balance =transactionModel.getTotalAmount();
			
			String bal =myFormatter.format(balance);
		    request.setAttribute("transactionAmount", bal);
		    ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
		    return modelAndView;

	    }
	    catch (FrameworkCheckedException ex)
	    {
	      
	    	Double balance =transactionModel.getTotalAmount();
			
			String bal =myFormatter.format(balance);
		    request.setAttribute("transactionAmount", bal);
		    
	    	if( ex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NULL ) )
			{
				super.saveMessage(request, "No distributor contact exist.");
				return super.showForm(request, response, errors);				
			}			
			else if( ex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NOT_ACTIVE ) )
			{
				super.saveMessage(request, "Distributor contact not active");
				return super.showForm(request, response, errors);				
			}
	    	
			else if( ex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.DISTRIBUTOR_CONTACT_NOT_NATIONAL_MANAGER ) )
			{
				super.saveMessage(request, "Distributor contact not national manager");
				return super.showForm(request, response, errors);				
			}
	    	
			else if( ex.getMessage().equalsIgnoreCase("DistributorInActive"))
			{
				super.saveMessage(request, "Distributor not active");
				return super.showForm(request, response, errors);				
			}
	    	
			else if( ex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.TRANSACTION_AMOUNT_NOT_SUPPLIED))
			{
				super.saveMessage(request, "Transaction amount not supplied");
				return super.showForm(request, response, errors);				
			}
	    	
			else if( ex.getMessage().equalsIgnoreCase(WorkFlowErrorCodeConstants.INSUFFICIENT_AMOUNT_TO_TRANSFER))
			{
				super.saveMessage(request, "Insufficent amount to transfer");
				return super.showForm(request, response, errors);				
			}
	    	 
			else if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	          ex.getErrorCode())
	      {
	        super.saveMessage(request, "Record could not be saved.");
	        return super.showForm(request, response, errors);
	      }
	      throw ex;
	    }
  }

  protected ModelAndView onUpdate(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  BindException errors) throws Exception
  {
    System.out.println("jflksafl;kdsfds");
	  return null;
  }

  /*public void setInov8DistributorCreditManager(Inov8DistributorCreditManager
                                               inov8DistributorCreditManager)
  {
    this.inov8DistributorCreditManager = inov8DistributorCreditManager;
  }

  public void setReferenceDataManager(ReferenceDataManager referenceDataManager)
  {
    this.referenceDataManager = referenceDataManager;
  }

  public void setTransactionManager(TransactionModuleManager transactionManager)
  {
    this.transactionManager = transactionManager;
  }

  public void setDistDistCreditManager(DistDistCreditManager distDistCreditManager)
{
this.distDistCreditManager = distDistCreditManager;
}
*/
public void setWorkFlowController(WorkFlowController workFlowController)
{
this.workFlowController = workFlowController;
}

public void setReferenceDataManager(ReferenceDataManager referenceDataManager) {
	this.referenceDataManager = referenceDataManager;
}

}
