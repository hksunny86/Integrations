package com.inov8.microbank.webapp.action.creditmodule;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: inov8 Limited</p>
 *
 * @author Jawwad Farooq
 * @version 1.0
 */

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.inov8.framework.common.exception.ExceptionErrorCodes;
import com.inov8.framework.webapp.action.AdvanceFormController;
import com.inov8.microbank.common.exception.WorkFlowException;
import com.inov8.microbank.common.model.AppUserModel;
import com.inov8.microbank.common.model.DeviceTypeModel;
import com.inov8.microbank.common.model.ProductModel;
import com.inov8.microbank.common.model.SmartMoneyAccountModel;
import com.inov8.microbank.common.model.TransactionModel;
import com.inov8.microbank.common.model.TransactionTypeModel;
import com.inov8.microbank.common.util.DeviceTypeConstantsInterface;
import com.inov8.microbank.common.util.TransactionTypeConstantsInterface;
import com.inov8.microbank.common.util.UserUtils;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapper;
import com.inov8.microbank.common.wrapper.workflow.WorkFlowWrapperImpl;
import com.inov8.microbank.server.service.commissionmodule.CommissionAmountsHolder;
import com.inov8.microbank.server.service.workflow.controller.WorkFlowController;


public class CustomerVariableProductSaleController extends AdvanceFormController
{
  private WorkFlowController workFlowController;

  public CustomerVariableProductSaleController()
  {
    setCommandName("transactionModel");
    setCommandClass(TransactionModel.class);
  }

  protected Map loadReferenceData(HttpServletRequest request) throws Exception
  {
    return null;
  }

  protected Object loadFormBackingObject(HttpServletRequest request) throws Exception
  {
    return new TransactionModel();
  }

  protected ModelAndView onCreate(HttpServletRequest request,
                                  HttpServletResponse response, Object command,
                                  BindException errors) throws Exception
  {
    TransactionModel transactionModel = (TransactionModel) command;
    WorkFlowWrapper workFlowWrapper = new WorkFlowWrapperImpl();

    workFlowWrapper.setAppUserModel( new AppUserModel() );
    workFlowWrapper.setCustomerAppUserModel( new AppUserModel() );
    workFlowWrapper.setTransactionTypeModel( new TransactionTypeModel() );
    workFlowWrapper.setDeviceTypeModel( new DeviceTypeModel() );
    workFlowWrapper.setProductModel( new ProductModel() );
    workFlowWrapper.setSmartMoneyAccountModel( new SmartMoneyAccountModel() );


    // ------- Insert Commission values ------------------------------------
    workFlowWrapper.setCommissionAmountsHolder( new CommissionAmountsHolder() ) ;
    workFlowWrapper.getCommissionAmountsHolder().setTotalAmount(0d) ;
    workFlowWrapper.getCommissionAmountsHolder().setBillingOrganizationAmount(0d) ;
    workFlowWrapper.getCommissionAmountsHolder().setTotalCommissionAmount(5d) ;
    // ---------------------------------------------------------------------

    workFlowWrapper.setAppUserModel( UserUtils.getCurrentUser() ) ;


//    workFlowWrapper.getTransactionTypeModel().setTransactionTypeId( TransactionTypeConstantsInterface.CUST_VAR_PRODUCT_SALE_TX ) ;

    workFlowWrapper.getTransactionTypeModel().setTransactionTypeId( TransactionTypeConstantsInterface.RET_VAR_PRODUCT_SALE_TX ) ;

    workFlowWrapper.getDeviceTypeModel().setDeviceTypeId( DeviceTypeConstantsInterface.MOBILE );
    workFlowWrapper.getSmartMoneyAccountModel().setSmartMoneyAccountId( 14L );

    workFlowWrapper.getProductModel().setProductId( 3L );
    workFlowWrapper.setTransactionAmount( 100D );

    workFlowWrapper.getCustomerAppUserModel().setMobileNo( "03344227165" );

    try
    {
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


  public void setWorkFlowController(WorkFlowController workFlowController)
  {
    this.workFlowController = workFlowController;
  }
}
