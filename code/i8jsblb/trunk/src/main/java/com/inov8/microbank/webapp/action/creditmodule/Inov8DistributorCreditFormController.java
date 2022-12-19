package com.inov8.microbank.webapp.action.creditmodule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.server.service.common.ReferenceDataManager;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.creditmodule.DistDistCreditManager;
import com.inov8.microbank.server.service.creditmodule.Inov8DistributorCreditManager;
import com.inov8.microbank.server.service.transactionmodule.TransactionModuleManager;
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

public class Inov8DistributorCreditFormController
    extends AdvanceFormController
{
  private Inov8DistributorCreditManager inov8DistributorCreditManager;
  private ReferenceDataManager referenceDataManager;
  private TransactionModuleManager transactionManager;
  private DistDistCreditManager distDistCreditManager;
  private WorkFlowController workFlowController;

  public Inov8DistributorCreditFormController()
  {
    setCommandName("transactionModel");
    setCommandClass(TransactionModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest request) throws Exception
  {
    return null;
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

	    try
	    {
              workFlowWrapper.setAppUserModel( new AppUserModel() );
              workFlowWrapper.setTransactionTypeModel( new TransactionTypeModel() );
              workFlowWrapper.setDeviceTypeModel( new DeviceTypeModel() );

              workFlowWrapper.setAppUserModel( UserUtils.getCurrentUser() ) ;
              workFlowWrapper.getTransactionTypeModel().setTransactionTypeId( TransactionTypeConstantsInterface.OPERATOR_TO_DISTR_TX ) ;
              workFlowWrapper.getDeviceTypeModel().setDeviceTypeId( DeviceTypeConstantsInterface.WEB );

              workFlowWrapper.setTransactionAmount( 100D );

              workFlowWrapper=this.workFlowController.workflowProcess(workFlowWrapper);

              transactionModel=workFlowWrapper.getTransactionModel();

              this.saveMessage(request, "The Transaction Completed Successfully. " + "Transaction Code: " + transactionModel.getTransactionCodeIdTransactionCodeModel().getCode());

              ModelAndView modelAndView = new ModelAndView(this.getSuccessView());
              return modelAndView;

	    }
	    catch (WorkFlowException ex)
	    {
	      if (ExceptionErrorCodes.DATA_INTEGRITY_VIOLATION_EXCEPTION ==
	          new Long(ex.getMessage()).longValue())
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
    return null;
  }

  public void setInov8DistributorCreditManager(Inov8DistributorCreditManager
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

  public void setDistDistCreditManager(DistDistCreditManager
          distDistCreditManager)
{
this.distDistCreditManager = distDistCreditManager;
}

public void setWorkFlowController(WorkFlowController workFlowController)
{
this.workFlowController = workFlowController;
}

}
