package com.inov8.microbank.webapp.action.creditmodule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.common.wrapper.BaseWrapper;
import com.inov8.framework.common.wrapper.BaseWrapperImpl;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.creditmodule.RetCustomerCreditManager;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;

public class RetailerCustomerCreditFormController extends AdvanceFormController
{
  private RetCustomerCreditManager retCustomerCreditManager;
  private WorkFlowController workFlowController;
  public RetailerCustomerCreditFormController()
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
    WorkFlowWrapper workFlowWrapper=new WorkFlowWrapperImpl();
    BaseWrapper baseWrapper=new BaseWrapperImpl();
    TransactionModel transactionModel = (
          TransactionModel) command;

    try
    {

      //    	ServiceTypeModel serviceTypeModel = new ServiceTypeModel();
      //    	serviceTypeModel.setServiceTypeId(ServiceTypeConstantsInterface.SERVICE_TYPE_DISCRETE);

      workFlowWrapper.setTransactionTypeModel( new TransactionTypeModel() );
      workFlowWrapper.getTransactionTypeModel().setTransactionTypeId(TransactionTypeConstantsInterface.RET_DISC_PRODUCT_SALE_TX);

      workFlowWrapper.setProductModel( new ProductModel() );
      workFlowWrapper.getProductModel().setProductId( transactionModel.getDistributorId() ) ;

      workFlowWrapper.setCustomerAppUserModel( new AppUserModel() );
      workFlowWrapper.getCustomerAppUserModel().setMobileNo( transactionModel.getCustomerMobileNo() );

      workFlowWrapper.setTransactionAmount(transactionModel.getTransactionAmount());

      workFlowWrapper.setFromRetailerContactModel(UserUtils.getCurrentUser().getRetailerContactIdRetailerContactModel());

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

  public void setRetCustomerCreditManager(RetCustomerCreditManager retCustomerCreditManager)
  {
    this.retCustomerCreditManager = retCustomerCreditManager;
  }

  public void setWorkFlowController(WorkFlowController workFlowController)
  {
    this.workFlowController = workFlowController;
  }
}
